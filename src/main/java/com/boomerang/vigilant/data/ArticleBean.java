package com.boomerang.vigilant.data;

public class ArticleBean {
    private String title;
    private String publisher;
    private Integer pubId;
    private String link;
    private String date;
    private Integer articleId;

    public ArticleBean() {
        this.title = "UNKNOWN";
        this.publisher = "UNKNOWN";
        this.articleId = 0;
    }

    public ArticleBean(String title, String publisher, int pubId, String link, String date) {
        this(title, publisher, pubId, link, date, 0);
    }

    public ArticleBean(
            String title, String publisher, int pubId, String link, String date, int articleId) {
        this.title = title;
        this.publisher = publisher;
        this.pubId = pubId;
        this.link = link;
        this.date = date;
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPubId() {
        return pubId;
    }

    public void setPubId(Integer pubId) {
        this.pubId = pubId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    @Override
    public int hashCode() {
        String kernel =
                new StringBuilder()
                        .append(this.title)
                        .append('.')
                        .append(this.publisher)
                        .append('.')
                        .append(this.date)
                        .toString();
        return kernel.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n  - ArticleId: ")
                .append(this.articleId)
                .append("\n    Title: ")
                .append(this.title)
                .append("\n    Publisher: ")
                .append(this.publisher)
                .append("\n    PubId: ")
                .append(this.pubId)
                .append("\n    Date: ")
                .append(this.date)
                .append("\n    Link: ")
                .append(this.link)
                .toString();
    }
}
