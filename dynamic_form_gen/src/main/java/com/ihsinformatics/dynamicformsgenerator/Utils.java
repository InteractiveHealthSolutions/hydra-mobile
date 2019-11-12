package com.ihsinformatics.dynamicformsgenerator;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by Owais on 11/13/2017.
 */
public class Utils {
    public static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static JSONObject converToServerResponse(OfflinePatient offlinePatient) throws JSONException {

        JSONObject preferredName = new JSONObject();
        preferredName.put("givenName", offlinePatient.getName()==null?"":offlinePatient.getName());
        preferredName.put("familyName", "");

        JSONObject person = new JSONObject();
        person.put("gender", offlinePatient.getGender());
        person.put("birthdate", Global.OPENMRS_TIMESTAMP_FORMAT.format(new Date(offlinePatient.getDob())));
        person.put("age", 0);
        person.put("preferredName", preferredName);

        JSONObject identifierType = new JSONObject();
        identifierType.put("display", ParamNames.INDUS_PROJECT_IDENTIFIER);

        JSONObject pqIdentifierType = new JSONObject();
        pqIdentifierType.put("display", ParamNames.PQ_PROJECT_IDENTIFIER);

        JSONObject scIdentifierType = new JSONObject();
        scIdentifierType.put("display", ParamNames.CSC_PROJECT_IDENTIFIER);

        JSONObject identifier = new JSONObject();
        identifier.put("identifier", offlinePatient.getMrNumber());
        identifier.put("identifierType", identifierType);

        JSONObject pqIdentifier = new JSONObject();
        pqIdentifier.put("identifier", offlinePatient.getPqId());
        pqIdentifier.put("identifierType", pqIdentifierType);

        JSONObject scIdentifier = new JSONObject();
        scIdentifier.put("identifier", offlinePatient.getScId());
        scIdentifier.put("identifierType", scIdentifierType);

        JSONArray identifiers = new JSONArray();
        identifiers.put(identifier);
        identifiers.put(pqIdentifier);
        identifiers.put(scIdentifier);

        JSONObject patient = new JSONObject();
        patient.put("identifiers", identifiers);
        patient.put("person", person);

        String encounterJsonString = offlinePatient.getEncounterJson();
        if(encounterJsonString == null) encounterJsonString = new JSONArray().toString();
        JSONObject encounters = new JSONObject(encounterJsonString);

        String summaryJsonString = offlinePatient.getSummaryJSON();
        if(summaryJsonString == null) summaryJsonString = new JSONObject().toString();
        JSONObject summary = new JSONObject(summaryJsonString);



        JSONArray patientsArray = new JSONArray();
        patientsArray.put(patient);
        JSONObject toReturn = new JSONObject();
        toReturn.put("patient", patientsArray);
        toReturn.put("encountersCount", encounters);
        toReturn.put(ParamNames.SUMMARY, summary);



        String fieldJsonString = offlinePatient.getFieldDataJson();
        if(fieldJsonString == null) fieldJsonString = new JSONObject().toString();
        JSONObject fieldJson = new JSONObject(fieldJsonString);
        Iterator<String> fieldsKeys = fieldJson.keys();
        while (fieldsKeys.hasNext()) {
            String key = fieldsKeys.next();
            toReturn.put(key, fieldJson.opt(key));
        }

        return toReturn;
    }
}
