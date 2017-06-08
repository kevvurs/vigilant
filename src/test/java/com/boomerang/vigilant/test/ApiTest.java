package com.boomerang.vigilant.test;

import com.boomerang.vigilant.data.Rss;
import com.boomerang.vigilant.service.VigilantService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@ContextConfiguration(locations = {"classpath:application-test-context.xml"})
public class ApiTest extends AbstractTestNGSpringContextTests {
    private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());

    @Autowired
    VigilantService vigilantService;

    @Test(
            groups = {"universal"},
            priority = 1
    )
    public void testEndpoints() {
        boolean passing = true;

        try {
            LOG.info("Testing Ping endpoint");
            String ping = vigilantService.ping();
            LOG.info(ping);

            LOG.info("Testing search CSE endpoint");
            String search = vigilantService.searchCSE("Trump");
            LOG.info(search);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Unsuccessful ping", e);
            e.printStackTrace();
            passing = false;
        }

        Assert.assertEquals(passing, true);
    }

    @Test(
            groups = {"universal"},
            priority = 2
    )
    public void rssFeed() throws JAXBException, MalformedURLException {
        JAXBContext jc = JAXBContext.newInstance("com.boomerang.vigilant.data");
        Unmarshaller u = jc.createUnmarshaller();
        URL url = new URL("https://news.google.com/news?cf=all&hl=en&ned=us&output=rss");
        Rss rss = (Rss) u.unmarshal(url);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(rss));
    }
}
