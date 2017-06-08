package com.boomerang.vigilant.service;

import com.boomerang.vigilant.data.PageBinding;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class GoogleSearchHandler {
    private static final Logger LOG = Logger.getLogger(GoogleSearchHandler.class.getName());
    @Value("${vigil.mediablade.id}")
    String searchId;
    @Value("${vigil.mediablade.key}")
    String searchKey;
    private String ogfields = "items/pagemap(metatags(og:url,og:site_name,og:type,og:description))";
    private String articles = "items/pagemap(newsarticle)";

    public String search(String query) {
        LOG.info("Scanning Google CSE (mediablade)");
        String results = null;

        try {
            URL target = new URL(new SearchURL(searchKey, searchId, query, articles).toString());
            HttpURLConnection connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/rss+xml");
            connection.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder reponseBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                // Process each line (date) of data
                reponseBuilder.append(line);
            }
            rd.close();
            results = reponseBuilder.toString();

        } catch (MalformedURLException e) {
            LOG.log(Level.WARNING, "Search URL not successful", e);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Connection not successful", e);
        }
        Gson gson = new Gson();
        gson.fromJson(results, PageBinding.class);
        return results;
    }

    private class SearchURL {
        private static final String HOST = "https://www.googleapis.com/customsearch/v1";
        private String path;

        public SearchURL() {
            this.path = HOST;
        }

        public SearchURL(String key, String cx, String q) {
            this();
            this.buildPath(key, cx, q);
        }

        public SearchURL(String key, String cx, String q, String fields) {
            this(key, cx, q);
            this.buildPath(fields);
        }

        private void buildPath(String key, String cx, String q) {
            String params = String.format("?key=%s&cx=%s&q=%s", key, cx, q);
            this.path = new StringBuilder()
                    .append(this.path)
                    .append(params)
                    .toString();
        }

        private void buildPath(String fields) {
            String param = String.format("&fields=%s", fields);
            this.path = new StringBuilder()
                    .append(this.path)
                    .append(param)
                    .toString();
        }

        @Override
        public String toString() {
            return this.path;
        }
    }
}
