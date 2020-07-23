package com.ihsinformatics.dynamicformsgenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationDTO;
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.Patient;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import es.dmoral.toasty.Toasty;

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
        preferredName.put("givenName", offlinePatient.getName() == null ? "" : offlinePatient.getName());
        preferredName.put("familyName", "");

        JSONObject person = new JSONObject();
        person.put("gender", offlinePatient.getGender());
        person.put("birthdate", Global.OPENMRS_TIMESTAMP_FORMAT.format(new Date(offlinePatient.getDob())));
        person.put("age", 0);
        person.put("preferredName", preferredName);
        person.put(ParamNames.CONTACT, offlinePatient.getContact());
        person.put(ParamNames.OFFLINE_CONTACT, offlinePatient.getOfflineContact());


        JSONObject otherDetails = new JSONObject();
        otherDetails.put("covidResult", offlinePatient.getCovidResult());

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
        patient.put(ParamNames.OTHER_DETAILS, otherDetails);


        String encounterJsonString = offlinePatient.getEncounterJson();
        if (encounterJsonString == null) encounterJsonString = new JSONArray().toString();
        JSONObject encounters = new JSONObject(encounterJsonString);


        JSONArray patientsArray = new JSONArray();
        patientsArray.put(patient);
        JSONObject toReturn = new JSONObject();
        toReturn.put("patient", patientsArray);
        toReturn.put("encountersCount", encounters);


        String fieldJsonString = offlinePatient.getFieldDataJson();
        if (fieldJsonString == null) fieldJsonString = new JSONObject().toString();
        JSONObject fieldJson = new JSONObject(fieldJsonString);
        Iterator<String> fieldsKeys = fieldJson.keys();
        while (fieldsKeys.hasNext()) {
            String key = fieldsKeys.next();
            toReturn.put(key, fieldJson.opt(key));
        }

        return toReturn;
    }

    public static void convertPatientToPatientData(Context context, JSONObject resp, int respId, String requestType) {
        try {
            if (!resp.has(ParamNames.SERVER_ERROR)) {
                if (requestType.equals(ParamNames.GET_PATIENT_INFO)) {

                    PatientData patientData = null;
                    Patient patient = JsonHelper.getInstance(context).ParsePatientFromUser(resp);
                    OfflinePatient offlinePatient = new OfflinePatient();

                    if (patient != null) {
                        patientData = new PatientData(patient);
                        JSONArray identifiers = resp.optJSONArray(ParamNames.PATIENT).getJSONObject(0).optJSONArray(ParamNames.IDENTIFIERS);
                        if (identifiers != null)
                            for (int i = 0; i < identifiers.length(); i++) {
                                JSONObject id = identifiers.getJSONObject(i);
                                String identifier = id.optString(ParamNames.IDENTIFIER);
                                JSONObject idType = id.getJSONObject(ParamNames.IDENTIFIER_TYPE);
                                String identifierType = idType.getString(ParamNames.DISPLAY);
                                patientData.addIdentifier(identifierType, identifier);

                                if (identifierType.equals(ParamNames.INDUS_PROJECT_IDENTIFIER)) {
                                    offlinePatient.setMrNumber(identifier);
                                }
                            }

                    }

                    JSONObject encounters = (JSONObject) resp.getJSONObject(ParamNames.ENCOUNTERS_COUNT);


                    if (offlinePatient.getMrNumber() != null) {
                        offlinePatient.setEncounterJson(encounters.toString());
                        offlinePatient.setFieldDataJson(generateFieldsJon(resp).toString());
                        offlinePatient.setName(patient.getGivenName() + " " + patient.getFamilyName());
                        offlinePatient.setGender(patient.getGender());
                        offlinePatient.setDob(patient.getBirthDate().getTime());
                        offlinePatient.setCovidResult(patient.getCovidResult());
                        offlinePatient.setContact(patient.getContactNumber());
                        offlinePatient.setOfflineContact(patient.getOfflineContactNumber());


                        DataAccess.getInstance().insertOfflinePatient(context, offlinePatient);
                    }
                    Global.patientData = patientData;

                   /* if (getEncounterName().equals(ParamNames.ENCOUNTER_TYPE_PATIENT_INFO)) {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    } else if (getEncounterName().equals(ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING)) {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    } else {
                        Form.setENCOUNTER_NAME(getEncounterName());
                        startForm(patientData, null);
                    }
*/

                }
            } else {
                String value;
                value = resp.getString(ParamNames.SERVER_ERROR);
                Toasty.error(context, value, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toasty.error(context, "Could not parse server response", Toast.LENGTH_LONG).show();
            Logger.log(e);
        }
    }

    private static JSONObject generateFieldsJon(JSONObject resp) {
        JSONObject jsonObject = resp;
        jsonObject.remove(ParamNames.ENCOUNTERS_COUNT);
        jsonObject.remove(ParamNames.PATIENT);
        return jsonObject;
    }


    public static List<Location> convertLocationDTOToLocation(List<LocationDTO> locationsDTO) {
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < locationsDTO.size(); i++) {
            Location singleLocation = new Location();

            singleLocation.setId(locationsDTO.get(i).getId());
            singleLocation.setUuid(locationsDTO.get(i).getUuid());
            singleLocation.setName(locationsDTO.get(i).getName());
            singleLocation.setCountry(locationsDTO.get(i).getCountry());
            singleLocation.setStateProvince(locationsDTO.get(i).getStateProvince());
            singleLocation.setCountryDistrict(locationsDTO.get(i).getCountryDistrict());
            singleLocation.setCityVillage(locationsDTO.get(i).getCityVillage());
            singleLocation.setAddress1(locationsDTO.get(i).getAddress1());
            singleLocation.setAddress2(locationsDTO.get(i).getAddress2());
            singleLocation.setAddress3(locationsDTO.get(i).getAddress3());
            singleLocation.setAddress4(locationsDTO.get(i).getAddress4());
            singleLocation.setAddress5(locationsDTO.get(i).getAddress5());
            singleLocation.setAddress6(locationsDTO.get(i).getAddress6());
            singleLocation.setLocationTags(locationsDTO.get(i).getLocationTags());
            // singleLocation.setDateCreated(locationsDTO.get(i).getDateCreated());
            if (null != locationsDTO.get(i).getParentLocation())
                singleLocation.setParentLocationUUID(locationsDTO.get(i).getParentLocation().getUuid());
            locations.add(singleLocation);
        }
        return locations;
    }

    public static Boolean isInternetConnected(Context context) {
        Boolean isInternetConnected = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            isInternetConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            ToastyWidget.getInstance().displayError(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT);
        }

        return isInternetConnected;
    }


    // Function that can recursively change identifier in all forms that are related to that identifier. This is used only when identifier field in create patient form is changed.
    public static void changeRecursiveIdentifier(Context context, SaveableForm oldForm, SaveableForm newForm) throws JSONException {

        String oldIdentifier = oldForm.getIdentifier();

        String newIdentifier = newForm.getIdentifier();
        String newName = newForm.getPatient_name();


        List<SaveableForm> forms = DataAccess.getInstance().getAllFormsByPatientIdentifier(context, oldIdentifier);

        for (SaveableForm f : forms) {

            //Changing identifier in SaveableForm Identifer column
            f.setIdentifier(newIdentifier);
            f.setPatient_name(newName);


            //Need to change identifier inside form data (json field) which is used to send to server.
            //This step needs not to be done in CREATE PATIENT FORM because new identifier is already in metaData of CREATE PATIENT FORM
            if (!f.getEncounterType().equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                JSONObject formData = f.getFormData();
                JSONObject metaData = formData.getJSONObject(ParamNames.METADATA);
                JSONObject patientData = metaData.getJSONObject(ParamNames.PATIENT);

                patientData.put(ParamNames.GIVEN_NAME, newName);

                // This is LIST of identifiers because in old projects we used to have multiple identifer formats (fore.g one format for indus, and other for any other hospital)
                JSONArray identifiersList = patientData.getJSONArray(ParamNames.IDENTIFIERS);

                for (int i = 0; i < identifiersList.length(); i++) {
                    JSONObject identifier = identifiersList.getJSONObject(i);
                    if (identifier.optString(ParamNames.TYPE).equals(ParamNames.MR_NUMBER)) {
                        identifier.put(ParamNames.VALUE, newIdentifier);

                        identifiersList.remove(i);
                        identifiersList.put(identifier);
                    }
                }
                patientData.put(ParamNames.IDENTIFIERS, identifiersList);
                metaData.put(ParamNames.PATIENT, patientData);
                formData.put(ParamNames.METADATA, metaData);
                f.setFormData(formData);
                DataAccess.getInstance().insertOrReplaceForm(context, f);
            }
        }

        //Need to delete offline Patient of old identifier because there could be a case where user entered wrong identifier which is not a duplicate
        // In this case if we dont delete offline patient, then same device will not let repeat this identifier
        if (!oldIdentifier.equals(newIdentifier)) {
            DataAccess.getInstance().deleteOfflinePatient(context, oldIdentifier);
            DataAccess.getInstance().deleteOfflineHistoryByPatientIdentifier(context, oldIdentifier);
        }
    }

}
