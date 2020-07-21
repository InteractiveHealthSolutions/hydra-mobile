package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Phase {

    @SerializedName("uuid")
    private String uuid;

    // @PrimaryKey
    @SerializedName("phaseId")
    private long phaseId;

    @SerializedName("name")
    private String name;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(long phaseId) {
        this.phaseId = phaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
