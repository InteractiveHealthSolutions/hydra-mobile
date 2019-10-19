package com.ihsinformatics.dynamicformsgenerator.data.utils;

import android.content.Context;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttribute;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeType;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTag;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Option;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Procedure;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.network.pojos.Patient;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Naveed Iqbal on 11/22/2017.
 * Email: h.naveediqbal@gmail.com
 */
public class JsonHelper {
    private static JsonHelper ourInstance;
    private static Context context;

    public synchronized static JsonHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new JsonHelper(context);
        }
        return ourInstance;
    }

    private JsonHelper(Context context) {
        this.context = context;
    }

    public Set<UserCredentials> parseUsersFromJson(JSONArray jsonObject) {
        Set<UserCredentials> userCredentials = new HashSet<>();
        for (int i = 0; i < jsonObject.length(); i++) {
            UserCredentials user = null;
            try {
                user = parseUserFromJson(null, null, jsonObject.getJSONObject(i));
                if (user!=null)
                    if (!user.getUsername().equals("scheduler") && !user.getUsername().equals("daemon") && !user.getUsername().equals("null"))
                        userCredentials.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userCredentials;
    }

    public UserCredentials parseUserFromJson(String username, String password, JSONObject jsonObject) {
        UserCredentials user = null;
        try {
            String labUUID = null;
            String providerUUID = null;
            String uuid = jsonObject.getString(ParamNames.UUID);
            if (jsonObject.has(ParamNames.PROVIDER)) {
                if (!jsonObject.isNull(ParamNames.PROVIDER)) {
                    providerUUID = jsonObject.getJSONObject(ParamNames.PROVIDER).getString(ParamNames.UUID);
                }
            }
            if(providerUUID == null) return null;
            JSONObject person = jsonObject.getJSONObject(ParamNames.PERSON);
            String fullName = person.getString(ParamNames.DISPLAY);
            String gender;
            if (person.has(ParamNames.PERSON_GENDER)) {
                gender = person.getString(ParamNames.GENDER.toLowerCase());
                gender = gender.startsWith("M") ? "Male" : "Female";
            } else {
                gender = "";
            }
            /*if(!jsonObject.isNull(ParamNames.LOCATION))
                labUUID = jsonObject.getJSONObject(ParamNames.LOCATION).getString(ParamNames.UUID);*/
            if (username == null) {
                username = jsonObject.optString((ParamNames.USERNAME.toLowerCase()));
                if (username == null)
                    return null;
            }

            user = new UserCredentials(null, username, password, gender, fullName, uuid, providerUUID);
        } catch (JSONException e) {
            Logger.log(e);
        }
        return user;
    }

    public Patient ParsePatientFromUser(JSONObject resultJson) {
        Patient patient = null;
        try {
            // JSONObject resultJson = jsonObject.optJSONObject(ParamNames.SERVER_RESPONSE);
            JSONArray results = resultJson.getJSONArray(ParamNames.PATIENT);
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                String identifier = result.getJSONArray(ParamNames.IDENTIFIERS).getJSONObject(0).getString(ParamNames.IDENTIFIER);
                JSONObject person = result.getJSONObject(ParamNames.PERSON);
                JSONObject preferredName = person.getJSONObject(ParamNames.PREFERRED_NAME);
                String givenName = preferredName.getString(ParamNames.GIVEN_NAME);
                String familyName = preferredName.getString(ParamNames.FAMILY_NAME);
                String uuid = result.optString(ParamNames.UUID);
                int age = Integer.parseInt(person.getString(ParamNames.PERSON_AGE));
                Date birthDate = null;
                birthDate = Global.OPENMRS_TIMESTAMP_FORMAT.parse(person.getString(ParamNames.BIRTH_DATE));
                String gender = person.getString(ParamNames.PERSON_GENDER);
                int locationId = -1;
                patient = new Patient(
                        identifier,
                        givenName,
                        familyName,
                        uuid,
                        age,
                        birthDate,
                        gender,
                        locationId
                );
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public List<Procedure> parseProceduresFromJson(JSONArray jsonArray) {
        List<Procedure> procedureList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                procedureList.add(new Procedure(null, jsonObject.getString("uuid"), jsonObject.getString("display")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return procedureList;
    }

    public static LinkedList<Option> parseOptionsFromJson(JSONArray jsonArray) {
        LinkedList<Option> options = new LinkedList<>();
        try {
        for(int i=0; i<jsonArray.length(); i++) {

                JSONObject person = jsonArray.getJSONObject(i).getJSONObject(ParamNames.PERSON);
                String name = person.getJSONObject(ParamNames.PREFERRED_NAME).getString(ParamNames.DISPLAY);
                JSONArray attributes = person.optJSONArray(ParamNames.ATTRIBUTES);
                if(attributes!=null) {
                    for(int j=0; j<attributes.length(); j++) {
                        JSONObject attribute = attributes.getJSONObject(j);
                        if(attribute.getString(ParamNames.DISPLAY).contains(ParamNames.ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE_NAME)) {
                            Option option = new Option();
                            option.setValue(name);
                            option.setOptionTag(ParamNames.ATTRIBUTE_TYPE_PQ_PIRANI_SCORING_ROLE_NAME);
                            options.add(option);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return options;
    }
    public static LinkedList<Location> parseLocationsFromJson(JSONArray jsonArray) {
        LinkedList<Location> locationLinkedList = new LinkedList<>(); // faster than array list in add/ remove
        try {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String parentLocationName = null;
                if(!obj.isNull(ParamNames.PARENT_LOCATION)) {
                    JSONObject parentLocation = obj.getJSONObject(ParamNames.PARENT_LOCATION);
                    parentLocationName = parentLocation.getString(ParamNames.UUID);
                }

                String dateCreatedString = null;
                JSONObject auditInfo = obj.optJSONObject(ParamNames.AUDIT_INFO);
                if(auditInfo!=null)
                    dateCreatedString = auditInfo.getString(ParamNames.DATE_CREATED);

                Date dateCreated;
                if(dateCreatedString == null) dateCreated = new Date();
                else dateCreated = Global.OPENMRS_TIMESTAMP_FORMAT.parse(dateCreatedString);

                Location location = new Location(
                        null,
                        obj.getString(ParamNames.UUID),
                        obj.getString(ParamNames.DISPLAY),
                        obj.getString(ParamNames.COUNTRY),
                        obj.getString(ParamNames.STATE),
                        obj.getString(ParamNames.COUNTRY_DISTRICT),
                        obj.getString(ParamNames.CITY_VILLAGE),
                        obj.getString(ParamNames.ADDRESS1),
                        obj.getString(ParamNames.ADDRESS2),
                        obj.getString(ParamNames.ADDRESS3),
                        obj.getString(ParamNames.ADDRESS4),
                        obj.getString(ParamNames.ADDRESS5),
                        obj.getString(ParamNames.ADDRESS6),
                        parentLocationName,
                        dateCreated);
                if(!obj.isNull(ParamNames.LOCATION_TAGS)) {
                    ArrayList<LocationTag> tagsList = new ArrayList<>();
                    JSONArray tagsArray = obj.getJSONArray(ParamNames.LOCATION_TAGS);
                    for(int j=0; j<tagsArray.length(); j++) {
                        JSONObject tag = tagsArray.getJSONObject(j);
                        LocationTag locationTag = DataAccess.getInstance().fetchLocationTagByName(context, tag.getString(ParamNames.DISPLAY));
                        if(locationTag == null) {
                            locationTag = new LocationTag(null, tag.getString(ParamNames.DISPLAY), tag.getString(ParamNames.UUID));
                            long locationId = DataAccess.getInstance().insertLocationTag(context, locationTag);
                            locationTag.setId(locationId);
                        }
                        tagsList.add(locationTag);
                    }
                    location.setLocationTags(tagsList);
                }

                /*+if(!obj.isNull(ParamNames.ATTRIBUTES)) {
                    ArrayList<LocationAttribute> LocationAttributeList = new ArrayList<>();
                    JSONArray locationAttributesArray = obj.getJSONArray(ParamNames.ATTRIBUTES);
                    for(int j=0; j<locationAttributesArray.length(); j++) {
                        JSONObject locationAttribute = locationAttributesArray.getJSONObject(j);
                        String valueString = locationAttribute.getString(ParamNames.VALUE);
                        String uuidString = locationAttribute.getString(ParamNames.UUID);
                        JSONObject attributeTypeJson = locationAttribute.getJSONObject(ParamNames.ATTRIBUTE_TYPES);
                        String attributeTypeString = attributeTypeJson.getString(ParamNames.DISPLAY);
                        LocationAttributeType locationAttributeType = DataAccess.getInstance().fetchLocationAttributeTypeByName(attributeTypeString);
                        if(locationAttributeType!=null)
                            LocationAttributeList.add(new LocationAttribute(null, valueString, uuidString, location.getId(),  locationAttributeType.getId()));
                    }
                    location.setLocationAttributes(LocationAttributeList);
                }*/

                locationLinkedList.add(location);
            }
        } catch (JSONException e) {
            Logger.log(e);
        } catch (ParseException e) {
            Logger.log(e);
        }

        return locationLinkedList;
    }
}
