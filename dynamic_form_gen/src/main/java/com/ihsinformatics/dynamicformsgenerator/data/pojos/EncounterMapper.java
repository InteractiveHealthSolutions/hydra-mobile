package com.ihsinformatics.dynamicformsgenerator.data.pojos;

public class EncounterMapper {

    String uuid;
    int encounterMapperId;
    XRayResultEncounter resultEncounterId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getEncounterMapperId() {
        return encounterMapperId;
    }

    public void setEncounterMapperId(int encounterMapperId) {
        this.encounterMapperId = encounterMapperId;
    }

    public XRayResultEncounter getResultEncounterId() {
        return resultEncounterId;
    }

    public void setResultEncounterId(XRayResultEncounter resultEncounterId) {
        this.resultEncounterId = resultEncounterId;
    }
}
