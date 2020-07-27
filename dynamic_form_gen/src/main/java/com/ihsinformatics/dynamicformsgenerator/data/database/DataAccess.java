package com.ihsinformatics.dynamicformsgenerator.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.ihsinformatics.dynamicformsgenerator.App;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters;
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Ob;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormEncounterMapper;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormType;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormTypeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.History;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.HistoryDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Image;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ImageDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttribute;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeType;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationAttributeTypeDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTag;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTagDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTagMap;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.LocationTagMapDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Option;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.OptionDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Procedure;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SystemSettings;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SystemSettingsDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.User;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentialsDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserDao;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserReports;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserReportsDao;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class DataAccess {
    private static DataAccess instance;

    private DataAccess() {
    }

    public synchronized static DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }

    public synchronized void insertUserCredentials(Context context, UserCredentials user) {
        try {
            App.getDaoSession(context).getUserCredentialsDao().insert(user);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized void insertOfflinePatient(Context context, OfflinePatient offlinePatient) {
        try {
            App.getDaoSession(context).getOfflinePatientDao().insertOrReplace(offlinePatient);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized OfflinePatient getPatientByMRNumber(Context context, String mrNumber) {
        return App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.MrNumber.eq(mrNumber)).unique();
    }

    public synchronized List<OfflinePatient> getPatientMatchesByMRNumber(Context context, String mrNumber) {
        return App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.MrNumber.eq("%" + mrNumber + "%")).list();
    }

    public synchronized OfflinePatient getPatientByName(Context context, String name) {
        System.out.println("");
        return App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.Name.like(name)).unique();
    }

    public synchronized List<OfflinePatient> getPatientMatchesByName(Context context, String name) {
        System.out.println("");
        return App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.Name.like("%" + name + "%")).list();
    }

    public synchronized OfflinePatient getPatientByAllFields(Context context, String id, String name, String dob, String gender) {
        System.out.println("");
        return App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.Name.like(name))
                .where(OfflinePatientDao.Properties.MrNumber.eq(id))
                .where(OfflinePatientDao.Properties.Gender.eq(gender))
                .where(OfflinePatientDao.Properties.Dob.eq(dob)).unique();
    }


    public synchronized void insertUserCredentialsInTx(Context context, Iterable<UserCredentials> users) {
        try {
            App.getDaoSession(context).getUserCredentialsDao().insertInTx(users);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized void insertOrReplaceUserCredentialsInTx(Context context, Iterable<UserCredentials> users) {
        try {
            App.getDaoSession(context).getUserCredentialsDao().insertOrReplaceInTx(users);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized UserCredentials getUserCredentials(Context context, String username) {
        UserCredentials userCred = null;
        userCred = App.getDaoSession(context)
                .getUserCredentialsDao().queryBuilder()
                .where(UserCredentialsDao.Properties.Username.eq(username)).unique();
        return userCred;
    }

    public synchronized List<UserCredentials> getUserCredentials(Context context) {
        List<UserCredentials> userCred = null;
        userCred = App.getDaoSession(context).getUserCredentialsDao().queryBuilder().list();
        return userCred;
    }

    public synchronized void insertProceduresInTx(Context context, Iterable<Procedure> procedures) {
        try {
            App.getDaoSession(context).getProcedureDao().insertOrReplaceInTx(procedures);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized List<Procedure> getProcedures(Context context) {
        List<Procedure> procedureList = null;
        procedureList = App.getDaoSession(context).getProcedureDao().queryBuilder().list();
        return procedureList;
    }

    public synchronized void insertFormTypes(Context context, List<FormType> formsTypes, SQLiteDatabase db) {
        App.getDaoSession(context).getFormTypeDao().insertInTx(formsTypes);
    }

    public synchronized void insertFormType(Context context, FormType formType, SQLiteDatabase db) {
        App.getDaoSession(context).getFormTypeDao().insert(formType);
    }

    public synchronized int getFormTypeId(Context context, String formTypeName) {
        int id = 0;
        FormType formType = App.getDaoSession(context).getFormTypeDao().queryBuilder().where(FormTypeDao.Properties.TypeName.eq(formTypeName)).unique();
        if (formType != null) {
            id = (int) formType.getTypeId();
        }
        return id;
    }

    public synchronized long insertOrReplaceForm(Context context, SaveableForm form) {
        return App.getDaoSession(context).getSaveableFormDao().insertOrReplace(form);
    }


    public synchronized int getNumberOfForms(Context context, String formTypeName) {
        int formtypeId = getFormTypeId(context, formTypeName);
        return App.getDaoSession(context).getSaveableFormDao().queryBuilder().where(SaveableFormDao.Properties.FormTypeId.eq(formtypeId)).list().size();
    }

    public synchronized List<SaveableForm> getAllFormsByFormTypeName(Context context, String formTypeName) {
        int formtypeId = getFormTypeId(context, formTypeName);
        return App.getDaoSession(context).getSaveableFormDao().queryBuilder().where(SaveableFormDao.Properties.FormTypeId.eq(formtypeId)).list();
    }

    public synchronized List<SaveableForm> getAllFormsByHydraUrl(Context context, String hydraURL) {
        List<SaveableForm> toReturn =
                App.getDaoSession(context)
                        .getSaveableFormDao()
                        .queryBuilder()
                        .orderAsc(SaveableFormDao.Properties.FormId)
                        .list();
        return toReturn;
    }

    public synchronized List<SaveableForm> getAllFormsByPatientIdentifier(Context context, String patientIdentifier) {
        List<SaveableForm> toReturn =
                App.getDaoSession(context)
                        .getSaveableFormDao()
                        .queryBuilder().where(SaveableFormDao.Properties.Identifier.eq(patientIdentifier)).list();
        return toReturn;
    }

    public synchronized SaveableForm getOldIdentifierBySaveableFormID(Context context, Long editedFormId) {


        return App.getDaoSession(context).getSaveableFormDao().queryBuilder().where(SaveableFormDao.Properties.FormId.eq(editedFormId)).unique();

    }

    public synchronized void deleteForm(Context context, int formid) {
        App.getDaoSession(context).getSaveableFormDao().deleteByKey((long) formid);

       /* final DeleteQuery<SaveableForm> tableDeleteQuery = App.getDaoSession(context).queryBuilder(SaveableForm.class)
                .where(SaveableFormDao.Properties.FormId.eq(formid))
                .buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        App.getDaoSession(context).clear();*/
    }


    public synchronized void deleteFormByFormID(Context context, long formid) {
        App.getDaoSession(context).getSaveableFormDao().deleteByKey((long) formid);

    }


    //This will delete all related data like locations, their maps, currency and system settings
    public synchronized void deleteRelatedData(Context context) {

        App.getDaoSession(context).getLocationTagMapDao().deleteAll();
        App.getDaoSession(context).getLocationDao().deleteAll();
        App.getDaoSession(context).getLocationAttributeDao().deleteAll();
        App.getDaoSession(context).getLocationAttributeTypeDao().deleteAll();
        App.getDaoSession(context).getLocationTagDao().deleteAll();
        App.getDaoSession(context).getSystemSettingsDao().deleteAll();

    }

    public synchronized void updateFormError(Context context, int formid, String error) {
        SaveableFormDao saveableFormDao = App.getDaoSession(context).getSaveableFormDao();
        SaveableForm saveableForm = App.getDaoSession(context).getSaveableFormDao().load((long) formid);
        if (saveableForm != null) {
            saveableForm.setFormError(error);
            saveableFormDao.update(saveableForm);
        } else {
            Toasty.error(context, "No Such form found", Toast.LENGTH_SHORT).show();
        }
    }

    public synchronized boolean insertFormImage(Context context, Image image) {
        boolean status = false;
        long id = App.getDaoSession(context).getImageDao().insert(image);
        if (id != -1) {
            status = true;
        }
        return status;
    }

    public synchronized void updateFormImage(Context context, Image image) {
        App.getDaoSession(context).getImageDao().update(image);
    }

    public synchronized void updateFormImage(Context context, long formId, String UUID) {
        ImageDao imageDao = App.getDaoSession(context).getImageDao();
        List<Image> imageList = imageDao.queryBuilder().where(ImageDao.Properties.FormId.eq(formId)).list();
        for (int i = 0; i < imageList.size(); i++) {
            Image image = imageList.get(i);
            image.setFormUUID(UUID);
            imageDao.update(image);
        }
    }

    public synchronized List<Image> getImagesByFormId(Context context, int formId) {
        return App.getDaoSession(context).getImageDao().queryBuilder().where(ImageDao.Properties.FormId.eq(formId)).list();
    }

    public synchronized List<Image> getAllImages(Context context) {
        return App.getDaoSession(context).getImageDao().loadAll();
    }

    public synchronized int getCountOfImagesById(Context context, int formId) {
        return App.getDaoSession(context).getImageDao().queryBuilder().where(ImageDao.Properties.FormId.eq(formId)).list().size();
    }

    public synchronized void deleteImages(Context context, int imageId) {
        App.getDaoSession(context).getImageDao().deleteByKey((long) imageId);
    }

    public synchronized void updateImageError(Context context, int imageId, String error) {
        ImageDao imageDao = App.getDaoSession(context).getImageDao();
        Image image = imageDao.load((long) imageId);
        if (image != null) {
            image.setImageError(error);
            imageDao.update(image);
        } else {
            Toasty.error(context, "No such image found", Toast.LENGTH_SHORT).show();
        }
    }

    public void inserDefaultData(Context context) {
        List<FormType> formTypes = DataProvider.getInstance(context).getFormTypes();
        App.getDaoSession(context).getFormTypeDao().insertInTx(formTypes);
    }

    public LocationAttributeType fetchLocationAttributeTypeByName(Context context, String typeName) {
        LocationAttributeTypeDao locationAttributeTypeDao = App.getDaoSession(context).getLocationAttributeTypeDao();
        List<LocationAttributeType> tagList = locationAttributeTypeDao.queryBuilder()
                .where(LocationAttributeTypeDao.Properties.Name.eq(typeName))
                .list();

        if (tagList.size() > 0)
            return tagList.get(0);
        return null;
    }

    public LocationTag fetchLocationTagByName(Context context, String tagName) {
        LocationTagDao locationTagDao = App.getDaoSession(context).getLocationTagDao();
        List<LocationTag> tagList = locationTagDao.queryBuilder()
                .where(LocationTagDao.Properties.Name.eq(tagName))
                .list();

        if (tagList.size() > 0)
            return tagList.get(0);
        return null;
    }

    // parent optional
    public List<Location> fetchLocationsByTag(Context context, String tag, String parentLocationName, String parentTag) throws IllegalStateException {
        Location parent = fetchLocationsByNameAndTag(context, parentTag, parentLocationName);
        List<Location> locationList = new ArrayList<>();
        LocationTag locationTag = fetchLocationTagByName(context, tag);
        if (locationTag == null)
            throw new IllegalStateException("No location tag found against location tag " + tag);
        // QueryBuilder.LOG_SQL = true;

        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        QueryBuilder<Location> queryBuilder = locationDao.queryBuilder();
        if (parent != null)
            queryBuilder.where(LocationDao.Properties.ParentLocationUUID.eq(parent.getUuid()));

        Join locationTagMapJoin = queryBuilder.join(LocationTagMap.class, LocationTagMapDao.Properties.LocationId);
        locationTagMapJoin.where(LocationTagMapDao.Properties.LocationTagId.eq(locationTag.getId()));

        locationList = queryBuilder.list();
        return locationList;
    }

    public Location fetchLocationsByNameAndTag(Context context, String tag, String locationName) throws IllegalStateException {
        if (locationName == null || tag == null) return null;
        List<Location> locationList = new ArrayList<>();
        LocationTag locationTag = fetchLocationTagByName(context, tag);
        if (locationTag == null)
            throw new IllegalStateException("No location tag found against location tag " + tag);
        // QueryBuilder.LOG_SQL = true;

        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        QueryBuilder<Location> queryBuilder = locationDao.queryBuilder();
        queryBuilder.where(LocationDao.Properties.Name.eq(locationName));

        Join locationTagMapJoin = queryBuilder.join(LocationTagMap.class, LocationTagMapDao.Properties.LocationId);
        locationTagMapJoin.where(LocationTagMapDao.Properties.LocationTagId.eq(locationTag.getId()));

        locationList = queryBuilder.list();
        return locationList.get(0);
    }

    //parent mandatory
    public List<Location> fetchLocationsByTagAndParent(Context context, String tag, String parentLocationName) throws IllegalStateException {
        Location parent = fetchLocationByName(context, parentLocationName);
        List<Location> locationList = new ArrayList<>();
        LocationTag locationTag = fetchLocationTagByName(context, tag);
        if (locationTag == null)
            throw new IllegalStateException("No location tag found against location tag " + tag);
        // QueryBuilder.LOG_SQL = true;

        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        QueryBuilder<Location> queryBuilder = locationDao.queryBuilder();
        if (parent != null)
            return locationList; // return empty list

        Join locationTagMapJoin = queryBuilder.join(LocationTagMap.class, LocationTagMapDao.Properties.LocationId);
        locationTagMapJoin.where(LocationTagMapDao.Properties.LocationTagId.eq(locationTag.getId()));

        locationList = queryBuilder.list();
        return locationList;
    }

    public Location fetchLocationByName(Context context, String name) {
        if (name == null)
            return null;
        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        List<Location> locationsList = locationDao.queryBuilder()
                .where(LocationDao.Properties.Name.eq(name))
                .list();
        if (locationsList.size() > 0)
            return locationsList.get(0);
        else
            return null;
    }

    public void insertLocationTagsInTx(Context context, LocationTag... locationTag) {
        LocationTagDao locationTagDao = App.getDaoSession(context).getLocationTagDao();
        locationTagDao.insertOrReplaceInTx(locationTag);
    }

    public Long insertLocationTag(Context context, LocationTag locationTag) {
        LocationTagDao locationTagDao = App.getDaoSession(context).getLocationTagDao();
        LocationTag existingTag = fetchLocationTagByName(context, locationTag.getName());
        if (existingTag == null)
            return locationTagDao.insert(locationTag);
        return existingTag.getId();
    }

    public synchronized void insertLocations(Context context, List<Location> locations) {
        Database db = App.getDaoSession(context).getDatabase();
        db.beginTransaction();
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            Location savedLocation = fetchLocationByUUID(context, location.getUuid());
            if (savedLocation != null)
                continue;

            insertLocation(context, location);
            System.out.println("Added: " + i);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Option fetchOptionByValue(Context context, String value) {
        if (value == null)
            return null;
        OptionDao locationDao = App.getDaoSession(context).getOptionDao();
        List<Option> locationsList = locationDao.queryBuilder()
                .where(OptionDao.Properties.Value.eq(value))
                .list();
        if (locationsList.size() > 0)
            return locationsList.get(0);
        else
            return null;
    }

    public List<Option> fetchOptionsByTagName(Context context, String tag) {
        OptionDao locationDao = App.getDaoSession(context).getOptionDao();
        List<Option> locationsList = locationDao.queryBuilder()
                .where(OptionDao.Properties.OptionTag.eq(tag))
                .list();

        return locationsList;
    }

    public synchronized void insertOptions(Context context, List<Option> options) {
        Database db = App.getDaoSession(context).getDatabase();
        db.beginTransaction();
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            Option savedOption = fetchOptionByValue(context, option.getValue());
            if (savedOption != null)
                continue;

            insertOption(context, option);
            System.out.println("Added: " + i);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void insertOption(Context context, Option option) {
        App.getDaoSession(context).getOptionDao().insert(option);
    }

    public Location fetchLocationByUUID(Context context, String uuid) {
        if (uuid == null)
            return null;
        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        List<Location> locationsList = locationDao.queryBuilder()
                .where(LocationDao.Properties.Uuid.eq(uuid))
                .list();
        if (locationsList.size() > 0)
            return locationsList.get(0);
        else
            return null;
    }

    public void insertLocation(Context context, Location location) {

        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        LocationAttributeDao locationAttributeDao = App.getDaoSession(context).getLocationAttributeDao();
        LocationTagMapDao locationTagMapDao = App.getDaoSession(context).getLocationTagMapDao();
        LocationTagDao locationTagDao = App.getDaoSession(context).getLocationTagDao();
        Location parent = fetchLocationByUUID(context, location.getParentLocationUUID());
        //if(parent!=null)
        //location.setParentLocation(parent.getId());
        Long locationId = locationDao.insertOrReplace(location);
        ArrayList<LocationTag> locationTagsList = location.getLocationTags();
        if (locationTagsList != null) {
            ArrayList<LocationTagMap> locationTagMaps = new ArrayList<>();
            for (LocationTag locationTag : locationTagsList) {
                locationTag.setId(insertLocationTag(context, locationTag));
                LocationTagMap locationTagMap = new LocationTagMap(null, locationId, locationTag.getId());
                locationTagMaps.add(locationTagMap);
            }
            locationTagMapDao.insertOrReplaceInTx(locationTagMaps);
        }
        ArrayList<LocationAttribute> locationAttributesList = location.getLocationAttributes();
        if (locationAttributesList != null) {
            for (LocationAttribute la : locationAttributesList) {
                la.setLocationId(locationId);
            }
            locationAttributeDao.insertOrReplaceInTx(locationAttributesList);
        }
    }


    public SystemSettings fetchSystemSettingsByUUID(Context context, String uuid) {
        SystemSettingsDao locationTagDao = App.getDaoSession(context).getSystemSettingsDao();
        List<SystemSettings> tagList = locationTagDao.queryBuilder()
                .where(SystemSettingsDao.Properties.Uuid.eq(uuid))
                .list();

        if (tagList.size() > 0)
            return tagList.get(0);
        return null;
    }


    public void insertSystemSettings(Context context, List<SystemSettings> systemSettings) {
        SystemSettingsDao systemSettingsDao = App.getDaoSession(context).getSystemSettingsDao();

        for (int i = 0; i < systemSettings.size(); i++) {
            systemSettingsDao.insertOrReplace(systemSettings.get(i));
        }
    }

    public SystemSettings fetchSystemSettingsByFormat(Context context, String format) {
        SystemSettingsDao locationTagDao = App.getDaoSession(context).getSystemSettingsDao();
        List<SystemSettings> tagList = locationTagDao.queryBuilder()
                .where(SystemSettingsDao.Properties.Property.eq(format))
                .list();

        if (tagList.size() > 0)
            return tagList.get(0);
        return null;
    }


    public List<Location> fetchLocationsByTagCountryAndParent(Context context, String tag, String country, String parentLocationName) throws IllegalStateException {
        Location parent = fetchLocationByName(context, parentLocationName);
        List<Location> locationList = new ArrayList<>();
        LocationTag locationTag = fetchLocationTagByName(context, tag);
        if (locationTag == null)
            throw new IllegalStateException("No location tag found against location tag " + tag);
        if (country == null)
            throw new IllegalStateException("No Country found in global settings");
        // QueryBuilder.LOG_SQL = true;

        LocationDao locationDao = App.getDaoSession(context).getLocationDao();
        QueryBuilder<Location> queryBuilder = locationDao.queryBuilder().where(LocationDao.Properties.Country.eq(country));
        if (parent != null)
            return locationList; // return empty list

        Join locationTagMapJoin = queryBuilder.join(LocationTagMap.class, LocationTagMapDao.Properties.LocationId);
        locationTagMapJoin.where(LocationTagMapDao.Properties.LocationTagId.eq(locationTag.getId()));

        locationList = queryBuilder.list();
        return locationList;
    }


    public HashMap<String, List<String>> fetchSaveableFormsByPatientIdentifer(Context context, String patientID) throws JSONException {
        List<SaveableForm> listOfForms = App.getDaoSession(context).getSaveableFormDao().queryBuilder().where(SaveableFormDao.Properties.Identifier.eq(patientID)).list();
        HashMap<String, List<String>> formsData = new HashMap<String, List<String>>();
        for (int i = 0; i < listOfForms.size(); i++) {
            ArrayList<String> conceptsAndValues = new ArrayList<>();
            String formValues = listOfForms.get(i).getFormValues();
            JSONObject formValuesJSON = new JSONObject(formValues);
            JSONArray values = formValuesJSON.optJSONArray(ParamNames.SERVICE_HISTORY);
            for (int j = 0; j < values.length(); j++) {
                JSONObject temp = (JSONObject) values.get(j);
                String tempString = temp.optString(ParamNames.PARAM_QUESTION) + " : " + temp.optString(ParamNames.PARAM_VALUE);
                conceptsAndValues.add(tempString);
            }
            formsData.put(listOfForms.get(i).getEncounterType(), conceptsAndValues);
        }

        return formsData;
    }

    public void clearAllOfflineData(Context context) {

        ServiceHistoryDao serviceHistoryDao = App.getDaoSession(context).getServiceHistoryDao();
        serviceHistoryDao.deleteAll();

        HistoryDao historyDao = App.getDaoSession(context).getHistoryDao();
        historyDao.deleteAll();

        OfflinePatientDao offlinePatientDao = App.getDaoSession(context).getOfflinePatientDao();
        offlinePatientDao.deleteAll();

        SaveableFormDao offlineFormsDao = App.getDaoSession(context).getSaveableFormDao();
        offlineFormsDao.deleteAll();
    }


    public synchronized void insertUser(Context context, User user) {
        try {
            App.getDaoSession(context).getUserDao().insertOrReplace(user);
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public synchronized void deleteAllUsers(Context context) {
        App.getDaoSession(context).getUserDao().deleteAll();
    }

    public synchronized List<User> getAllUser(Context context) {
        List<User> toReturn =
                App.getDaoSession(context)
                        .getUserDao()
                        .queryBuilder()
                        .orderAsc(UserDao.Properties.Id)
                        .list();
        return toReturn;
    }

    public List<User> getUserByUsernameAndPass(Context context, String username, String password) {
        UserDao userDao = App.getDaoSession(context).getUserDao();
        List<User> userList = userDao.queryBuilder().where(UserDao.Properties.Username.eq(username), UserDao.Properties.Password.eq(password)).list();
        return userList;
    }


    public synchronized void insertFormDetailsInUserReport(Context context, String username, String encounter, long offlineFormID, String workflowUUID, String componentFormUUID, String url) {
        UserReportsDao userReportsDao = App.getDaoSession(context).getUserReportsDao();

        String dateValue = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        UserReports userReport = null;

        try {
            userReport = userReportsDao.queryBuilder().where(UserReportsDao.Properties.Username.eq(username),
                    UserReportsDao.Properties.WorkflowUUID.eq(workflowUUID),
                    UserReportsDao.Properties.ComponentFormUUID.eq(componentFormUUID),
                    UserReportsDao.Properties.Date.eq(dateValue),
                    UserReportsDao.Properties.Encounter.eq(encounter)).unique();
        } catch (Exception e) {

        }

        if (userReport != null) {
            userReportsDao.update(new UserReports(userReport.getUsername(), userReport.getEncounter(), userReport.getOffline_form_id(), userReport.getEncounter_filled() + 1, userReport.getEncounter_uploaded(), dateValue, userReport.getWorkflowUUID(), userReport.getComponentFormUUID(), userReport.getUrl(), userReport.getId()));

        } else {
            userReportsDao.insert(new UserReports(username, encounter, offlineFormID, 1, 0, dateValue, workflowUUID, componentFormUUID, url, null));
        }
    }

    public List<UserReports> getAllUserReportsByUserNameAndWorkflow(Context context, String username, String workflowUUID, String date) {
        UserReportsDao userReportsDao = App.getDaoSession(context).getUserReportsDao();
        List<UserReports> userReportsList = userReportsDao.queryBuilder().where(UserReportsDao.Properties.Username.eq(username),
                UserReportsDao.Properties.WorkflowUUID.eq(workflowUUID),
                UserReportsDao.Properties.Date.eq(date)
        ).list();

        return userReportsList;
    }


    public synchronized void updateEncounterUploadCount(Context context, String username, String encounter, long offlineFormID, String workflowUUID, String componentFormUUID, String url) {
        UserReportsDao userReportsDao = App.getDaoSession(context).getUserReportsDao();
        String dateValue = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        UserReports userReport = null;
        try {
            userReport = userReportsDao.queryBuilder().where(UserReportsDao.Properties.Username.eq(username),
                    UserReportsDao.Properties.WorkflowUUID.eq(workflowUUID),
                    UserReportsDao.Properties.ComponentFormUUID.eq(componentFormUUID),
                    UserReportsDao.Properties.Date.eq(dateValue),
                    UserReportsDao.Properties.Encounter.eq(encounter)).unique();
        } catch (Exception e) {

        }

        if (userReport != null) {
            userReportsDao.update(new UserReports(userReport.getUsername(), userReport.getEncounter(), userReport.getOffline_form_id(), userReport.getEncounter_filled(), userReport.getEncounter_uploaded() + 1, dateValue, userReport.getWorkflowUUID(), userReport.getComponentFormUUID(), userReport.getUrl(), userReport.getId()));

        } else {
            userReportsDao.insert(new UserReports(username, encounter, offlineFormID, 0, 1, dateValue, workflowUUID, componentFormUUID, url, null));
        }
    }

    public synchronized void decreaseEncounterFilledCount(Context context, String username, String encounter, long offlineFormID, String workflowUUID, String componentFormUUID, String url) {
        UserReportsDao userReportsDao = App.getDaoSession(context).getUserReportsDao();
        String dateValue = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

        UserReports userReport = null;

        try {
            userReport = userReportsDao.queryBuilder().where(UserReportsDao.Properties.Username.eq(username),
                    UserReportsDao.Properties.WorkflowUUID.eq(workflowUUID),
                    UserReportsDao.Properties.ComponentFormUUID.eq(componentFormUUID),
                    UserReportsDao.Properties.Date.eq(dateValue),
                    UserReportsDao.Properties.Encounter.eq(encounter)).unique();
        } catch (Exception e) {

        }

        // in this case if userReport is null then it means form was filled previously and was deleted today
        if (userReport != null) {
            if (userReport.getEncounter_filled() <= 1) {
                userReportsDao.delete(userReport);
            } else {
                userReportsDao.update(new UserReports(userReport.getUsername(), userReport.getEncounter(), userReport.getOffline_form_id(), userReport.getEncounter_filled() - 1, userReport.getEncounter_uploaded(), userReport.getDate(), userReport.getWorkflowUUID(), userReport.getComponentFormUUID(), userReport.getUrl(), userReport.getId()));
            }
        }
    }


    public synchronized String getPatientOfflineNumber(Context context, String mrNumber) {
        OfflinePatient offlinePatient = App.getDaoSession(context).getOfflinePatientDao().queryBuilder()
                .where(OfflinePatientDao.Properties.MrNumber.eq(mrNumber)).unique();

        String toReturn = "";

        if (null != offlinePatient) {
            toReturn = offlinePatient.getOfflineContact();
        }

        return toReturn;
    }

    public synchronized void deleteOfflinePatient(Context context, String identifier) {

        OfflinePatient offlinePatient = App.getDaoSession(context).getOfflinePatientDao().queryBuilder().where(OfflinePatientDao.Properties.MrNumber.eq(identifier)).unique();

        App.getDaoSession(context).getOfflinePatientDao().delete(offlinePatient);

    }


    // deletes old history and updates with new one, when encounters fetched online
    public synchronized void insertOnlineHistory(Context context, ArrayList<FormEncounterMapper> encountersList, String patientID) {

        if (encountersList != null && encountersList.size() > 0) {
            // delete all existing forms of patient with matching identifer (we dont want to keep duplicates. Also we want to store latest forms that were uploaded)
            final DeleteQuery<History> historyDelete = App.getDaoSession(context).getHistoryDao().queryBuilder()
                    .where(HistoryDao.Properties.PatientIdentifier.eq(patientID))
                    .whereOr(HistoryDao.Properties.EncounterSaved.eq(ParamNames.ONLINE_ENCOUNTERS), HistoryDao.Properties.EncounterSaved.eq(ParamNames.OFFLINE_UPLOADED_ENCOUNTERS))
                    .buildDelete();
            historyDelete.executeDeleteWithoutDetachingEntities();
            App.getDaoSession(context).clear();

            // Insert all latest forms uploaded in history
            HistoryDao historyDao = App.getDaoSession(context).getHistoryDao();

            Gson gson = new Gson();

            for (FormEncounterMapper enc : encountersList) {
                String encountersJSON = gson.toJson(enc.getEncounters());
                History history = new History(null, patientID, enc.getComponentForm().getUuid(), enc.getComponentForm().getId(), enc.getComponentForm().getPhase().getUuid(), enc.getComponentForm().getPhase().getPhaseId()
                        , enc.getComponentForm().getComponent().getUuid(), enc.getComponentForm().getComponent().getComponentId(), enc.getComponentForm().getWorkflow().getUuid(), enc.getComponentForm().getWorkflow().getWorkflowId(),
                        enc.getComponentForm().getForm().getUuid(), enc.getComponentForm().getForm().getFormId(), encountersJSON, enc.getEncounters().getAuditInfo().getDateCreated(), ParamNames.ONLINE_ENCOUNTERS);

                historyDao.insertOrReplace(history);

            }
        }
    }


    public synchronized void insertOrReplaceOfflineHistory(Context context, History history) {

        App.getDaoSession(context).getHistoryDao().insertOrReplace(history);

    }


    public List<Encounters> fetchOnlineHistoryByPatientIdentifier(Context context, String patientID) {

        List<Encounters> toReturn = new ArrayList<>();

        HistoryDao historyDao = App.getDaoSession(context).getHistoryDao();
        List<History> historyList = historyDao.queryBuilder().where(HistoryDao.Properties.PatientIdentifier.eq(patientID), HistoryDao.Properties.EncounterSaved.eq(ParamNames.ONLINE_ENCOUNTERS)).list();

        Gson gson = new Gson();

        if (historyList != null) {
            for (History his : historyList) {
                Encounters toInsert = gson.fromJson(his.getEncounter(), new TypeToken<Encounters>() {
                }.getType());
                toReturn.add(toInsert);
            }
        }

        return toReturn;

    }


    // This function returns value to be auto-populated
    public String fetchValueFromOnlineEncounterHistory(Context context, String patientID, String componentFormUUID, Boolean autoCompleteFromEarliest, String fieldUUID) throws JSONException {

        String toReturn = "";

        Encounters encounter = new Encounters();
        List<History> historyList = new ArrayList<>();
        History history = null;


        HistoryDao historyDao = App.getDaoSession(context).getHistoryDao();
        if (autoCompleteFromEarliest) {
            historyList = historyDao.queryBuilder().where(HistoryDao.Properties.PatientIdentifier.eq(patientID),  HistoryDao.Properties.ComponentFormUUID.eq(componentFormUUID)).orderAsc(HistoryDao.Properties.EncounterDateTime).limit(1).list();
        } else {
            historyList = historyDao.queryBuilder().where(HistoryDao.Properties.PatientIdentifier.eq(patientID),  HistoryDao.Properties.ComponentFormUUID.eq(componentFormUUID)).orderDesc(HistoryDao.Properties.EncounterDateTime).limit(1).list();
        }


        if (null != historyList && historyList.size() > 0) {
            history = historyList.get(0);
        }

        Gson gson = new Gson();

        if (history != null) {
            if (history.getEncounterSaved().equals(ParamNames.ONLINE_ENCOUNTERS)) {
                encounter = gson.fromJson(history.getEncounter(), new TypeToken<Encounters>() {
                }.getType());

                for (Ob ob : encounter.getObs()) {
                    if (ob.getConcept().getUuid().equals(fieldUUID)) {
                        Object value = ob.getValue();
                        if (value instanceof JSONObject) {
                            toReturn = ((JSONObject) value).optString("display");
                            break;
                        } else if (value instanceof String) {
                            toReturn = value.toString();
                            break;
                        } else if (value instanceof LinkedTreeMap) {
                            toReturn = ((LinkedTreeMap) value).get("uuid").toString();
                            break;
                        }

                    }
                }
            }
            else{
                JSONObject offlineData = new JSONObject(history.getEncounter());
                JSONArray data = offlineData.optJSONArray(ParamNames.FORM_DATA);
                for(int i=0; i<data.length();i++)
                {
                    JSONObject field = data.getJSONObject(i);
                    String paramName = field.optString(ParamNames.PARAM_NAME);
                    if(paramName.equals(fieldUUID))
                    {
                        toReturn = field.optString(ParamNames.VALUE);
                        i = data.length(); // just a simple break statement, inorder to avoid bad break statement practice
                    }
                }
            }
        }

        return toReturn;
    }


    public synchronized void deleteOfflineHistoryByPatientIdentifier(Context context, String patientID) {

        final DeleteQuery<History> historyDelete = App.getDaoSession(context).getHistoryDao().queryBuilder().where(HistoryDao.Properties.PatientIdentifier.eq(patientID), HistoryDao.Properties.EncounterSaved.eq(ParamNames.OFFLINE_ENCOUNTERS)).buildDelete();
        historyDelete.executeDeleteWithoutDetachingEntities();
        App.getDaoSession(context).clear();


    }


    // This function changes tag of offline history from "offline" to "offline_uploaded". We need this when offline saved form is uploaded successfully.
    // Once we change tag from "offline" to "offline_uploaded", this will help us delete this offline history when new history is fetched from form-encounter endpoint
    public synchronized void changeOfflineFormTagInHistory(Context context, long offlineFormId) {

        HistoryDao historyDao = App.getDaoSession(context).getHistoryDao();
        History history = historyDao.queryBuilder().where(HistoryDao.Properties.FormID.eq(offlineFormId), HistoryDao.Properties.EncounterSaved.eq(ParamNames.OFFLINE_ENCOUNTERS)).unique();

        if (history != null) {
            history.setEncounterSaved(ParamNames.OFFLINE_UPLOADED_ENCOUNTERS);
            historyDao.insertOrReplace(history);
        }

    }

}
