package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Component {

    @SerializedName("uuid")
    private String uuid;

    //@PrimaryKey
    @SerializedName("componentId")
    private long componentId;

    @SerializedName("name")
    private String name;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
