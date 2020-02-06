package com.ihsinformatics.dynamicformsgenerator.common;

import java.util.LinkedHashMap;

public class Constants {

    private static LinkedHashMap<Integer, String> encounterTypes = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> encounterTypesData = new LinkedHashMap<>();

    private static LinkedHashMap<Integer, FormDetails> formDetails = new LinkedHashMap<>();



    public static LinkedHashMap<Integer, String> getEncounterTypes() {

        if (encounterTypes.size() == 0) {

        }
        return encounterTypes;
    }

    public static LinkedHashMap<Integer, FormDetails> getFormDetails() {

        if (formDetails.size() == 0) {

        }
        return formDetails;
    }

    public static void setFormDetails(Integer formId, FormDetails formName) {

        formDetails.put(formId, formName);

    }

    public static LinkedHashMap<String, String> getEncounterTypesData() {

        return encounterTypesData;
    }


    public static void setEncounterType(Integer formId, String formName) {

        encounterTypes.put(formId, formName);

    }

    public static void setEncounterTypeData(String formName, String formData) {

        encounterTypesData.put(formName, formData);

    }

    public static void clearEncounters()
    {
        if (encounterTypes!=null) encounterTypes.clear();
        if (encounterTypesData!=null) encounterTypesData.clear();

    }


}

