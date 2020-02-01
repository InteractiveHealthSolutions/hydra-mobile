package com.ihsinformatics.dynamicformsgenerator.common;

import java.util.LinkedHashMap;

public class Constants {

    private static LinkedHashMap<Integer, String> encounterTypes = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> encounterTypesData = new LinkedHashMap<>();


    public static LinkedHashMap<Integer, String> getEncounterTypes() {

        if (encounterTypes.size() == 0) {

        }
        return encounterTypes;
    }

    public static LinkedHashMap<String, String> getEncounterTypesData() {
        if (encounterTypesData.size() == 0) {
            //TODO implement logic for fetching from forms
        }
        return encounterTypesData;
    }


    public static void setEncounterType(Integer formId, String formName) {

        encounterTypes.put(formId, formName);

    }

    public static void setEncounterTypeData(String formName, String formData) {

        encounterTypesData.put(formName, formData);

    }

}
