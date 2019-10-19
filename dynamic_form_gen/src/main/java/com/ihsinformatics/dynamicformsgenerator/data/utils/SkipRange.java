package com.ihsinformatics.dynamicformsgenerator.data.utils;

/**
 * Created by Owais on 11/8/2017.
 */
public class SkipRange {
    private int value;
    private int minVal;
    private int maxVal;
    VALIDATION_TYPE validationType;

    public enum VALIDATION_TYPE {
        GREATER_THAN,
        LESS_THAN,
        EQUALS,
        NOT_EQUAL,
        BETWEEN,
        IS_EMPTY,
    }

    public SkipRange(VALIDATION_TYPE validationType, int value) {
        this.value = value;
        this.validationType = validationType;
    }

    public SkipRange(VALIDATION_TYPE validationType, int minVal, int maxVal) {
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.validationType = validationType;
    }

    public boolean validate(int userValue) {
        if (VALIDATION_TYPE.EQUALS == validationType) {
            if (value == userValue)
                return true;
        } else if (VALIDATION_TYPE.NOT_EQUAL == validationType) {
            if (value != userValue)
                return true;
        } else if (VALIDATION_TYPE.GREATER_THAN == validationType) {
            if (value < userValue)
                return true;
        } else if (VALIDATION_TYPE.LESS_THAN == validationType) {
            if (value > userValue)
                return true;
        } else if (VALIDATION_TYPE.BETWEEN == validationType) {
            if (userValue >= minVal && userValue <= maxVal) {
                return true;
            }
        }
        return false;
    }

    public boolean validateEmpty(String userValue) {
        if (VALIDATION_TYPE.IS_EMPTY == validationType) {
            if ("".equals(userValue))
                return true;
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public VALIDATION_TYPE getValidationType() {
        return validationType;
    }

    public void setValidationType(VALIDATION_TYPE validationType) {
        this.validationType = validationType;
    }
}
