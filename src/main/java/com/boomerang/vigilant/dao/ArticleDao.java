package com.boomerang.vigilant.dao;

import com.boomerang.vigilant.data.ArticleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ArticleDao {
    private static final Logger LOG = Logger.getLogger(ArticleDao.class.getName());

    @Value("${vigil.article.insert}")
    String insert_articles;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ArticleDao() {
        LOG.info("articleDao instantiated");
    }

    public void insertAll(Collection<ArticleBean> articleBeans) {
        for (ArticleBean articleBean : articleBeans) {
            insert(articleBean);
        }
        LOG.info("Insertion complete");
    }

    public void insert(ArticleBean article) {
        try {
            jdbcTemplate.update(insert_articles,
                    article.getTitle(),
                    article.getPublisher(),
                    article.getPubId(),
                    article.getLink(),
                    article.getDate()
            );
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("Persistence disconnected; dropping article: %s", article), e);
        }
    }
}
