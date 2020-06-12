package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EncountersWithObs implements Serializable {
    public static final long serialVersionUID = 1l;

    private String name;
    @Id
    @Unique
    private String uuid;

    private String encounterDatetime;

    private String obs;

    @Generated(hash = 34119513)
    public EncountersWithObs(String name, String uuid, String encounterDatetime,
            String obs) {
        this.name = name;
        this.uuid = uuid;
        this.encounterDatetime = encounterDatetime;
        this.obs = obs;
    }

    @Generated(hash = 724388790)
    public EncountersWithObs() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEncounterDatetime() {
        return this.encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public String getObs() {
        return this.obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

}



