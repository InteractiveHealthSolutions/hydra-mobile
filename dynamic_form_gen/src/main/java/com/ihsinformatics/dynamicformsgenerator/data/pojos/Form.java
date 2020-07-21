package com.ihsinformatics.dynamicformsgenerator.data.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created byTaha Asif on 7/20/2020.
 */

public class Form {

    @SerializedName("uuid")
    private String uuid;

    // @PrimaryKey
    @SerializedName("hydramoduleFormId")
    private long formId;

    @SerializedName("name")
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
