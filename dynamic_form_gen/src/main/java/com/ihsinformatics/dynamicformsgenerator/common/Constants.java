package com.ihsinformatics.dynamicformsgenerator.common;

import java.util.LinkedHashMap;

public class Constants {

    private LinkedHashMap<Integer, String> encounterTypes;
    private LinkedHashMap<String, String> encounterTypesData;

    private static Constants constants;

    private Constants()
    {
        if(encounterTypes==null)
        {
            encounterTypes= new LinkedHashMap<Integer,String>();
            encounterTypesData= new LinkedHashMap<String,String>();

        }
    }

    public static Constants getInstance()
    {
        if(constants==null)
        {
            constants=new Constants();
        }
        return constants;
    }

    public LinkedHashMap<Integer, String> getEncounterTypes()
    {
        return encounterTypes;
    }

    public LinkedHashMap<String, String> getEncounterTypesData()
    {
        return encounterTypesData;
    }



}
