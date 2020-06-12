package com.ihsinformatics.dynamicformsgenerator.data.utils;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Date;

/**
 * Created by Naveed Iqbal on 3/4/2017.
 * Email: h.naveediqbal@gmail.com
 */

public class SkipDateRange {

    private Date date;
    private  int validDurationInHours;
    DATE_VALIDATION_TYPE date_validation_type;

    public static enum DATE_VALIDATION_TYPE {
        AGE_BETWEEN,
        AGE_GREATER_THAN,
        AGE_LESS_THAN,
    }

    public SkipDateRange(DATE_VALIDATION_TYPE date_validation_type, Date date, int validDurationInHours) {
        this.date = date;
        this.date_validation_type = date_validation_type;
        this.date_validation_type = date_validation_type;
        this.validDurationInHours = validDurationInHours;
    }

    public boolean validate(Date userDate) {
        if(date_validation_type == DATE_VALIDATION_TYPE.AGE_BETWEEN) {

            Period p = new Period(new LocalDate(userDate), new LocalDate(date));
            int years = p.getYears();
            int hours = years*12*30*24;
            if(hours<=validDurationInHours) {
                return true;
            }
        }

        return false;
    }

}
