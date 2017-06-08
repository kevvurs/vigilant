package com.boomerang.vigilant.dao;

import com.boomerang.vigilant.data.PublisherBean;
import com.google.appengine.api.ThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PublisherCache {
    private static final Logger LOG = Logger.getLogger(PublisherCache.class.getName());
    @Value("${vigil.publication.select}")
    String select_publications;
    @Value("${vigil.publication.insert}")
    String insert_publications;
    @Value("${vigil.publication.get}")
    String get_publication;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private Map<String, Integer> cache;

    public PublisherCache() {
        LOG.info("publisherCache instantiated");
    }

    @PostConstruct // Update the in-memory publisher cache
    public void init() {
        LOG.info("Resetting the publisher cache");
        Map<String, Integer> newcache = new ConcurrentHashMap<>();
        List<PublisherBean> boxes = fetchPubIds();
        for (PublisherBean publisherBean : boxes) {
            newcache.put(publisherBean.getPublisher(), publisherBean.getPubId());
        }
        this.cache = newcache;
    }

    // Gets simplified Publisher data
    public List<PublisherBean> fetchPubIds() {
        LOG.info("Fetching publisher associations");
        List<PublisherBean> publisherBeans = new ArrayList<>();
        try {
            publisherBeans =
                    jdbcTemplate.query(select_publications, new BeanPropertyRowMapper(PublisherBean.class));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Publication data disconnected", e);
        }
        return publisherBeans;
    }

    // Add abbreviated Publisher to database, return on success
    private Integer insertPublisher(String publisher) {
        LOG.info(String.format("Inserting publisher '%s' in DB",publisher));
        Boolean unsafe = false;
        try {
            jdbcTemplate.update(insert_publications, publisher, unsafe);
            PublisherBean publisherBean =
                    (PublisherBean)
                            jdbcTemplate.queryForObject(
                                    get_publication,
                                    new Object[]{publisher},
                                    new BeanPropertyRowMapper(PublisherBean.class));
            return publisherBean.getPubId();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("Persisting new publisher '%s' failed", publisher), e);
        }
        return 0;
    }

    public Map<String, Integer> getCache() {
        return cache;
    }

    public void setCache(Map<String, Integer> cache) {
        this.cache = cache;
    }

    public Integer getPubId(String publisher) {
        if (this.cache.containsKey(publisher)) {
            return this.cache.get(publisher);
        } else {
            // Add a discovered Publisher to the DB and cache
            LOG.info(String.format("Discovering publisher '%s'",publisher));
            return insertPublisher(publisher);
        }
    }

    public void refreshInBackground() {
        Thread thread = ThreadManager.createBackgroundThread(new AsynchRefresh(this));
    }

    // Background thread to update cache
    private class AsynchRefresh implements Runnable {
        private Thread thread;
        private PublisherCache publisherCache;

        public AsynchRefresh(PublisherCache publisherCache) {
            this.thread = Thread.currentThread();
            this.publisherCache = publisherCache;
        }

        public long getThreadId() {
            return this.thread.getId();
        }

        @Override
        public void run() {
            publisherCache.init();
        }
    }
}
