package com.ihsinformatics.dynamicformsgenerator.data.database;

import com.ihsinformatics.dynamicformsgenerator.data.database.history.Ob;

import java.util.List;

public class XRayResultEncounter {

    private String uuid;
    private String display;
    List<XRayOb> obs;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<XRayOb> getObs() {
        return obs;
    }

    public void setObs(List<XRayOb> obs) {
        this.obs = obs;
    }
}
