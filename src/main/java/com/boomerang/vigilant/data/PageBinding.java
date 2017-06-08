package com.boomerang.vigilant.data;

import java.util.ArrayList;
import java.util.List;

public class PageBinding {
    protected List<PageItem> items;

    public List<PageItem> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }

    private class PageItem {
        protected PageMap pagemap;

        public PageMap getPagemap() {
            return pagemap;
        }

        public void setPagemap(PageMap pagemap) {
            this.pagemap = pagemap;
        }
    }

    private class PageMap {
        protected List<NewsArticle> newsarticle;

        public List<NewsArticle> getNewsarticle() {
            if (this.newsarticle == null) {
                this.newsarticle = new ArrayList<>();
            }
            return newsarticle;
        }

        public void setNewsarticle(List<NewsArticle> newsarticle) {
            this.newsarticle = newsarticle;
        }
    }

    private class NewsArticle {
        protected String mainentityofpage;
        protected String alternativeheadline;
        protected String description;
        protected String genre;
        protected String identifier;
        protected String usageterms;
        protected String inlanguage;
        protected String datepublished;
        protected String datemodified;
        protected String articlesection;
        protected String thumbnailurl;
        protected String caption;
        protected String copyrightholder;
        protected String height;
        protected String width;
        protected String headline;
        protected String printedition;
        protected String copyrightnotice;
        protected String copyrightyear;

        public String getMainentityofpage() {
            return mainentityofpage;
        }

        public void setMainentityofpage(String mainentityofpage) {
            this.mainentityofpage = mainentityofpage;
        }

        public String getAlternativeheadline() {
            return alternativeheadline;
        }

        public void setAlternativeheadline(String alternativeheadline) {
            this.alternativeheadline = alternativeheadline;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getUsageterms() {
            return usageterms;
        }

        public void setUsageterms(String usageterms) {
            this.usageterms = usageterms;
        }

        public String getInlanguage() {
            return inlanguage;
        }

        public void setInlanguage(String inlanguage) {
            this.inlanguage = inlanguage;
        }

        public String getDatepublished() {
            return datepublished;
        }

        public void setDatepublished(String datepublished) {
            this.datepublished = datepublished;
        }

        public String getDatemodified() {
            return datemodified;
        }

        public void setDatemodified(String datemodified) {
            this.datemodified = datemodified;
        }

        public String getArticlesection() {
            return articlesection;
        }

        public void setArticlesection(String articlesection) {
            this.articlesection = articlesection;
        }

        public String getThumbnailurl() {
            return thumbnailurl;
        }

        public void setThumbnailurl(String thumbnailurl) {
            this.thumbnailurl = thumbnailurl;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getCopyrightholder() {
            return copyrightholder;
        }

        public void setCopyrightholder(String copyrightholder) {
            this.copyrightholder = copyrightholder;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getPrintedition() {
            return printedition;
        }

        public void setPrintedition(String printedition) {
            this.printedition = printedition;
        }

        public String getCopyrightnotice() {
            return copyrightnotice;
        }

        public void setCopyrightnotice(String copyrightnotice) {
            this.copyrightnotice = copyrightnotice;
        }

        public String getCopyrightyear() {
            return copyrightyear;
        }

        public void setCopyrightyear(String copyrightyear) {
            this.copyrightyear = copyrightyear;
        }
    }
}
