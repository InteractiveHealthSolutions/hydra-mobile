package com.ihsinformatics.dynamicformsgenerator.data.database.history;

import java.util.List;

public class Encounters {

    String display;
    String encounterDatetime;
    EncounterType encounterType;
    List<Ob> obs;
    String uuid;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
    }

    public List<Ob> getObs() {
        return obs;
    }

    public void setObs(List<Ob> obs) {
        this.obs = obs;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
