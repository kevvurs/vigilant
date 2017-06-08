package com.boomerang.vigilant.service;

import com.boomerang.vigilant.dao.ArticleDao;
import com.boomerang.vigilant.dao.PublisherCache;
import com.boomerang.vigilant.data.ArticleBean;
import com.boomerang.vigilant.data.Rss;
import com.boomerang.vigilant.util.RssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GoogleNewsArchiver {
    private static final Logger LOG = Logger.getLogger(GoogleNewsArchiver.class.getName());
    @Autowired
    ArticleDao articleDao;
    @Autowired
    PublisherCache publisherCache;
    private Set<ArticleBean> shortTermCache;

    public GoogleNewsArchiver() {
        LOG.info("googleNewsArchiver instantiated");
    }

    // Obtain the news feed and persist article metadata to DB
    public void archive() throws ConnectException {
        LOG.info("Archiving process initiated");
        Rss feed = scan();
        if (feed != null) {
            Set<ArticleBean> articles = transformFeed(feed);
            distinctify(articles);

            if (articles.isEmpty()) {
                LOG.log(Level.WARNING, "No news discovered");
                return;
            }
            articleDao.insertAll(articles);
        } else {
            throw new ConnectException();
        }
    }

    // JAXB deserialization of Google's RSS news feed
    private Rss scan() {
        LOG.info("Reading external RSS feed");
        Rss rss = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.boomerang.vigilant.data");
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL("https://news.google.com/news?cf=all&hl=en&ned=us&output=rss");
            rss = (Rss) u.unmarshal(url); // Wrap in auto-generated XSD bindings
        } catch (JAXBException | MalformedURLException e) {
            LOG.log(Level.SEVERE, "RSS Scanning failed", e);
        }
        return rss;
    }

    // Capture the RSS feed in DB bindings
    private Set<ArticleBean> transformFeed(Rss rss) {
        LOG.info("Binding articles into DB format");
        Set<ArticleBean> articles = new HashSet<>();
        try {
            for (Rss.Channel.Item item : rss.getChannel().getItem()) {
                if (RssUtils.deepValidate(item)) {
                    // Structure relevant data
                    String title = RssUtils.getHeadline(item);
                    String publisher = RssUtils.getPublisherName(item);
                    int pubId = publisherCache.getPubId(publisher);
                    String link = item.getLink();
                    String date = item.getPubDate();

                    ArticleBean article = new ArticleBean(title, publisher, pubId, link, date);

                    articles.add(article);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Transferring RSS feed to beans failed", e);
        }
        return articles;
    }

    // Remove incoming articles that were previously discovered
    private synchronized void distinctify(Set<ArticleBean> incomingArticles) {
        Set<ArticleBean> tempSet = new HashSet<>(incomingArticles);
        for (ArticleBean articleBean : this.shortTermCache) {
            if (incomingArticles.contains(articleBean)) {
                incomingArticles.remove(articleBean);
            }
        }
        this.shortTermCache = tempSet; // Update the short-term memory
    }
}
