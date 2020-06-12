package ihsinformatics.com.hydra_mobile.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.LocationTag;
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Identifier;
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.IdentifierType;
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Person;
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.ReferenceConcept;

public class Converters {

    @TypeConverter
    public ReferenceConcept toReferenceConcept(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        ReferenceConcept attribute = gson.fromJson(json, ReferenceConcept.class);
        return attribute;
    }

    @TypeConverter
    public String fromReferenceConcept(ReferenceConcept attribute) {
        if (attribute == null) {
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(attribute);
        return json;
    }

    @TypeConverter
    public Person toPerson(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Person person = gson.fromJson(json, Person.class);
        return person;
    }

    @TypeConverter
    public String fromPerson(Person person) {
        if (person == null) {
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(person);
        return json;
    }

//
//    @TypeConverter
//    public LocationTag toLocationTag(String json) {
//        if (json == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        LocationTag locationTag = gson.fromJson(json, LocationTag.class);
//        return locationTag;
//    }
//
//    @TypeConverter
//    public String fromLocationTag(LocationTag locationTag) {
//        if (locationTag == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        String json = gson.toJson(locationTag);
//        return json;
//    }


//    @TypeConverter
//    public IdentifierType toIdentifierType(IdentifierType json) {
//        if (json == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        IdentifierType identifierType = gson.fromJson(json, IdentifierType.class);
//        return identifierType;
//    }
//
//    @TypeConverter
//    public String fromIdentifierType(IdentifierType identifierType) {
//        if (identifierType == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        String json = gson.toJson(identifierType);
//        return json;
//    }


    @TypeConverter
    public String fromIdentifier(List<Identifier> userRoles) {
        if (userRoles == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Identifier>>() {
        }.getType();
        String json = gson.toJson(userRoles, type);
        return json;
    }

    @TypeConverter
    public List<Identifier> toIdentifier(String privilege) {
        if (privilege == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Identifier>>() {
        }.getType();
        List<Identifier> rolePrivileges = gson.fromJson(privilege, type);
        return rolePrivileges;
    }


    @TypeConverter
    public String fromTags(List<LocationTag> userRoles) {
        if (userRoles == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LocationTag>>() {
        }.getType();
        String json = gson.toJson(userRoles, type);
        return json;
    }

    @TypeConverter
    public List<LocationTag> toTags(String privilege) {
        if (privilege == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LocationTag>>() {
        }.getType();
        List<LocationTag> rolePrivileges = gson.fromJson(privilege, type);
        return rolePrivileges;
    }
}
