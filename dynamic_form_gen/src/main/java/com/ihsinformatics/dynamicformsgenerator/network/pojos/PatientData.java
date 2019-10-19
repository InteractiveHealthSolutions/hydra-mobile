package com.ihsinformatics.dynamicformsgenerator.network.pojos;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Naveed Iqbal on 11/23/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class PatientData implements Serializable {

    private HashMap<String, String> identifiers;
    Patient patient;

    public PatientData(Patient patient) {
        this.patient = patient;
    }

    public void addIdentifier(String key, String value) {
        if(identifiers == null) {
            identifiers = new HashMap<>();
        }

        identifiers.put(key, value);
    }
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public HashMap<String, String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(HashMap<String, String> identifiers) {
        this.identifiers = identifiers;
    }
}
