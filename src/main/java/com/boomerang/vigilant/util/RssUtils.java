package com.boomerang.vigilant.util;


import com.boomerang.vigilant.data.Rss;

public class RssUtils {

    public static String getPublisherName(Rss.Channel.Item newsItem) {
        String[] headlineDescriptors = getHeadlineDescriptors(newsItem);
        String publisher = headlineDescriptors[headlineDescriptors.length - 1].trim();
        return publisher;
    }

    public static String getHeadline(Rss.Channel.Item newsItem) {
        String[] headlineDescriptors = getHeadlineDescriptors(newsItem);
        StringBuilder buil = new StringBuilder();
        for (int i = 0; i < headlineDescriptors.length - 1; i++) {
            buil.append(headlineDescriptors[i]);
        }
        return buil.toString().trim();
    }

    private static String[] getHeadlineDescriptors(Rss.Channel.Item newsItem) {
        String[] headlineDescriptors = newsItem.getTitle().split("-");
        if (headlineDescriptors.length == 1) {
            String[] headlineDefault = {headlineDescriptors[0], "UNKNOWN"};
            return headlineDefault;
        } else {
            return headlineDescriptors;
        }
    }

    public static boolean deepValidate(Rss.Channel.Item newsItem) {
        if (newsItem != null) {
            String title = newsItem.getTitle();
            if (title != null && !title.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
