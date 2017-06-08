package com.boomerang.vigilant.service;

import com.boomerang.vigilant.dao.PublisherCache;
import com.boomerang.vigilant.data.MissionConf;
import com.boomerang.vigilant.data.PageBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api")
@Component
public class VigilantService {
    private static final Logger LOG = Logger.getLogger(VigilantService.class.toString());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String JSON_NULL = "{}";
    private static final String JSON_OK = "{\"status\":\"OK\"}";

    @Autowired
    HashMap<String, Boolean> mission;

    @Autowired
    GoogleSearchHandler googleSearchHandler;

    @Autowired
    GoogleNewsArchiver googleNewsArchiver;

    @Autowired
    PublisherCache publisherCache;

    @Value("${vigil.app.secret}")
    String appSecret;

    public VigilantService() {
        LOG.log(Level.INFO, "vigilantService instantiated");
    }

    @GET
    @Path("/ping")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String ping() {
        return JSON_OK;
    }

    @GET
    @Path("/watcher") // TODO: app-secret validation OR use AppEngine endpoint protection
    @Produces(MediaType.APPLICATION_JSON)
    public String watcher() {
        LOG.info("Archiving data from Goolge News");

        try {
            googleNewsArchiver.archive();
            publisherCache.refreshInBackground();
            return JSON_OK;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Data uptake failed", e);
            return JSON_NULL;
        }
    }

    @GET
    @Path("/cse/{q}")
    @Produces(MediaType.APPLICATION_JSON)
    public String searchCSE(@PathParam("q") String q) {
        LOG.info("Searching on mediablade");

        try {
            String response = googleSearchHandler.search(q);
            PageBinding pageBinding = GSON.fromJson(response, PageBinding.class);
            return GSON.toJson(pageBinding);
        } catch (Exception e) {
            return JSON_NULL;
        }
    }

    @POST
    @Path("/config")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String configure(String request) {
        LOG.info("Configuring");

        try {
            MissionConf conf = GSON.fromJson(request, MissionConf.class);
            if (conf.getSecret().equals(appSecret)) {
                LOG.info("Verified config request");

                for (MissionConf.MissionItem item : conf.getMissionItems()) {
                    if (item.getPhrase() == null) {
                        LOG.warning("Null phrase detected");
                        continue;
                    } else {
                        mission.put(item.getPhrase(), item.getActive());
                    }
                }
                LOG.info("Target updated");
                return GSON.toJson(mission);
            } else {
                throw new IllegalStateException("API secret incorrect");
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Config not successful", e);
            return JSON_NULL;
        }
    }
}
