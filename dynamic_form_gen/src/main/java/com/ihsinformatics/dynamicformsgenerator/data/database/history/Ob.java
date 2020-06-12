package com.ihsinformatics.dynamicformsgenerator.data.database.history;

public class Ob {

    private String display;
    private String obsDatetime;

    private Concept concept;

    private String resourceVersion;
    private String status;
    private String uuid;
    private Boolean voided;

    private Object obsGroup;
    private Object order;
    private Object value;
    private Object valueCodedName;
    private Object valueModifier;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getObsDatetime() {
        return obsDatetime;
    }

    public void setObsDatetime(String obsDatetime) {
        this.obsDatetime = obsDatetime;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public Object getObsGroup() {
        return obsGroup;
    }

    public void setObsGroup(Object obsGroup) {
        this.obsGroup = obsGroup;
    }

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValueCodedName() {
        return valueCodedName;
    }

    public void setValueCodedName(Object valueCodedName) {
        this.valueCodedName = valueCodedName;
    }

    public Object getValueModifier() {
        return valueModifier;
    }

    public void setValueModifier(Object valueModifier) {
        this.valueModifier = valueModifier;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}
