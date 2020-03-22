package com.ihsinformatics.dynamicformsgenerator.data.core.questions.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nabil shafi on 6/13/2018.
 */

public class ContactTracingConfiguration extends Configuration {

    private boolean createPatient;
    private ContactTraceChildFields identifier;
    private ContactTraceChildFields firstName;
    private ContactTraceChildFields familyName;
    private ContactTraceChildFields age;
    private ContactTraceChildFields gender;
    private ContactTraceChildFields relationship;

    public ContactTracingConfiguration(boolean createPatient, List<ContactTraceChildFields> childrenConfigurations ) {
        this.createPatient = createPatient;
        filterChilrenConfiguration(childrenConfigurations);

    }

    private void filterChilrenConfiguration(List<ContactTraceChildFields> config) {

        for(int i=0;i<config.size();i++)
        {
            switch(config.get(i).getField())
            {
                case "73e557b7-0be7-4e96-b1f2-11c39534ec29":
                    age=config.get(i);
                    break;

                case "73eb7357-7eb0-4e96-b1f2-11c39534ec29":
                    gender=config.get(i);
                    break;

                case "73e557b7-7eb0-4e96-2f1b-11c39534ec29":
                    firstName=config.get(i);
                    break;

                case "73e557b7-7eb0-4e96-b1f2-11c39534e92c":
                    familyName=config.get(i);
                    break;

                case "37e557b7-7eb0-4e96-b1f2-11c395ec4329":
                    identifier=config.get(i);
                    break;

                case "37e557b7-0be7-4e96-b1f2-11c395ec4329":
                    relationship=config.get(i);
                    break;


            }

        }
    }

    public boolean isCreatePatient() {
        return createPatient;
    }

    public ContactTraceChildFields getIdentifier() {
        return identifier;
    }

    public ContactTraceChildFields getFirstName() {
        return firstName;
    }

    public ContactTraceChildFields getFamilyName() {
        return familyName;
    }

    public ContactTraceChildFields getAge() {
        return age;
    }

    public ContactTraceChildFields getGender() {
        return gender;
    }

    public ContactTraceChildFields getRelationship() {
        return relationship;
    }
}
