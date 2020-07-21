package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Workflow {

    @SerializedName("uuid")
    private String uuid;

    //    @PrimaryKey
    @SerializedName("workflowId")
    private long workflowId;

    @SerializedName("name")
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(long workflowId) {
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
