package com.boomerang.vigilant.data;

import java.util.ArrayList;
import java.util.List;

public class MissionConf {
    protected String secret;
    protected List<MissionItem> missionItems;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<MissionItem> getMissionItems() {
        if (this.missionItems == null) {
            this.missionItems = new ArrayList<>();
        }
        return missionItems;
    }

    public void setMissionItems(List<MissionItem> missionItems) {
        this.missionItems = missionItems;
    }

    public class MissionItem {
        protected String phrase;
        protected Boolean active;

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }
    }
}