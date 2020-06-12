package com.ihsinformatics.dynamicformsgenerator.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.utils.SkipRange;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Validation {
    public static final String CHECK_FOR_MRNO = "indus_mrno";
    public static final String CHECK_FOR_SSID = "ssid";
    public static final String CHECK_SID = "tbr3_id";
    public static final String CHECK_FOR_PATIENT_ID = "patient_id";
    public static final String CHECK_FOR_PATIENT_ID_KARACHI = "patient_id_karachi";
    public static final String CHECK_FOR_PATIENT_ID_MUZAFFARABAD = "patient_id_muzaffarabad";
    public static final String CHECK_FOR_PATIENT_ID_MANAWAN = "patient_id_manawan";
    public static final String CHECK_FOR_SPINNER = "spinner";
    public static final String CHECK_FOR_DECIMAL_ONE_DIGIT = "decimal_one_digit";
    public static final String CHECK_FOR_MR_NUMBER = "empty";
    public static final String CHECK_FOR_EMPTY = "empty";
    public static final String CHECK_FOR_CSC_ID = "CSC_identifier";
    public static final String CHECK_FOR_CELL_NUMBER = "check_cell_no";
    public static final String CHECK_FOR_MOBILE_NUMBER = "check_mobile_no";
    public static final String CHECK_FOR_MULTI_SELECT = "multi";
    public static final String CHECK_FOR_DATE_TIME = "date_time";
    public static final String CHECK_FOR_DATE = "date";
    public static final String CHECK_FOR_RANGE = "range";
    public static final String CHECK_FOR_1 = "1";
    public static final String CHECK_FOR_2 = "2";
    public static final String CHECK_FOR_3 = "3";
    public static final String CHECK_FOR_4 = "4";
    public static final String CHECK_FOR_5 = "5";
    public static final String CHECK_FOR_6 = "6";
    public static final String CHECK_FOR_7 = "7";
    public static final String CHECK_FOR_8 = "8";
    public static final String CHECK_FOR_9 = "9";
    public static final String CHECK_FOR_10 = "10";
    public static final String CHECK_FOR_11 = "11";
    public static final String CHECK_FOR_12 = "12";
    public static final String CHECK_FOR_13 = "13";
    public static final String CHECK_FOR_14 = "14";
    public static final String CHECK_FOR_15 = "15";
    public static final String CHECK_FOR_16 = "16";
    public static final String CHECK_FOR_17 = "17";
    public static final String CHECK_FOR_18 = "18";
    public static final String CHECK_FOR_19 = "19";
    public static final String CHECK_FOR_20 = "20";
    public static final String CHECK_FOR_21 = "21";
    public static final String CHECK_FOR_22 = "22";
    private static Validation instance;

    private Validation() {
        // TODO Auto-generated constructor stub
    }

    public static Validation getInstance() {
        if (instance == null) {
            instance = new Validation();
        }
        return instance;
    }

    // takes string and check if it is null or contains spaces only
    public Boolean areAllSpacesOrNull(String s) {
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validate(View v, String functionToCall, boolean isMandatory) {
        String value = null;
        if (v instanceof Spinner) {
            value = ((Spinner) v).getSelectedItem().toString();
        } else if (v instanceof EditText) {
            value = ((EditText) v).getText().toString();
        } else if (v instanceof RadioButton) {
            value = ((RadioButton) v).getText().toString();
        }
        if (!isMandatory && areAllSpacesOrNull(value)) {
            return true;
        }
        if (functionToCall.equals(CHECK_FOR_EMPTY)) {
            return checkForEmpty(value, isMandatory);
        } else if (functionToCall.equals(CHECK_FOR_SPINNER)) {
            return checkForEmpty(value, isMandatory);
        } else if (functionToCall.equals(CHECK_FOR_MULTI_SELECT)) {
        } else if (functionToCall.equals(CHECK_FOR_DATE_TIME)) {
            return checkForDateTime(value);
        } else if (functionToCall.equals(CHECK_FOR_DATE)) {
            return checkForDate(value);
        } else if (functionToCall.equals(CHECK_FOR_MRNO)) {
            return checkIfIndusMrno(value);
        } else if (functionToCall.equals(CHECK_SID)) {
            return checkIfValidId(value);
        } else if (functionToCall.equals(CHECK_FOR_PATIENT_ID)) {
            return checkIfValidParticipantId(value);
        } else if (functionToCall.equals(CHECK_FOR_PATIENT_ID_KARACHI)) {
            return checkIfValidKarachiPatientId(value);
        } else if (functionToCall.equals(CHECK_FOR_PATIENT_ID_MUZAFFARABAD)) {
            return checkIfValidMuzaffarabadPatientId(value);
        } else if (functionToCall.equals(CHECK_FOR_PATIENT_ID_MANAWAN)) {
            return checkIfValidManawanPatientId(value);
        } else if(functionToCall.equals(CHECK_FOR_CSC_ID)){
            return checkIfValidCSCPatientId(value);
        } else if (functionToCall.equals(CHECK_FOR_CELL_NUMBER)) {
            return isValidCellNumber(value);
        } else if (functionToCall.equals(CHECK_FOR_MOBILE_NUMBER)) {
            return isValidMobileNumber(value);
        } else if (functionToCall.equals(CHECK_FOR_DECIMAL_ONE_DIGIT)) {
            if (value.matches("[0-9][0-9][0-9].[0-9]")) {
                return true;
            } else if (value.matches("[0-9]{3}")) {
                return true;
            }
        } else if (functionToCall.equals(CHECK_FOR_1)) {
            return checkForSingleSelecctSpinner(value, 1, 95, isMandatory, "666", "888", "999");
        } else if (functionToCall.equals(CHECK_FOR_2)) {
            return checkForSingleSelecctSpinner(value, 1, 3700, isMandatory, "444", "555", "888", "999");
        } else if (functionToCall.equals(CHECK_FOR_3)) {
            return checkForSingleSelecctSpinner(value, 7, 110, isMandatory, "555", "888", "999");// TODO ask
        } else if (functionToCall.equals(CHECK_FOR_4)) {
            return checkForSingleSelecctSpinner(value, 0, 99, isMandatory, "000", "666", "888", "999");
        } else if (functionToCall.equals(CHECK_FOR_5)) {
            return checkForSingleSelecctSpinner(value, 1, 100, isMandatory, "666", "888", "999");
        } else if (functionToCall.equals(CHECK_FOR_6)) {
            return checkForSingleSelecctSpinner(value, 74, 250, isMandatory, "888", "999");
        } else if (functionToCall.equals(CHECK_FOR_7)) {
            return checkForSingleSelecctSpinnerDecimal(value, 20, 200, isMandatory, "888.8", "999.9");
        } else if (functionToCall.equals(CHECK_FOR_8)) {
            return checkForSingleSelecctSpinnerDecimal(value, 20, 600, isMandatory, "888.8", "999.9");
        } else if (functionToCall.equals(CHECK_FOR_9)) {
            return checkForSingleSelecctSpinnerDecimal(value, 0, 999.99, isMandatory);
        }
        else if(functionToCall!="" && functionToCall!=null)   //checking regix
        {
            if(checkForEmpty(value, isMandatory) && value.matches(functionToCall))
            return true;
        }
        return false;
    }

    public boolean validateForRange(String value, List<RangeOption> rangeOptions, boolean isMandatory) {
        try {
            for (RangeOption rangeOption : rangeOptions) {
                if (SkipRange.VALIDATION_TYPE.BETWEEN.equals(rangeOption.getSkipRange().getValidationType())) {
                    return rangeOption.getSkipRange().validate(Integer.parseInt(value));
                }
            }
        } catch (NumberFormatException e) {
            Logger.log(e);
        }
        return false;
    }

    private boolean checkForSingleSelecctSpinnerDecimal(String value, double min, double max, boolean isMandatory, String... allowedValues) {
        if (areAllSpacesOrNull(value)) {
            return false;
        }
        if (value.equals("") && !isMandatory) {
            return true;
        }
        if (value.indexOf(".") != value.lastIndexOf("."))
            return false;
        double val = Double.parseDouble(value);
        if (val < min || val > max) {
            for (int i = 0; i < allowedValues.length; i++) {
                if (value.equals(allowedValues[i])) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean checkForSingleSelecctSpinner(String value, int min, int max, boolean isMandatory, String... allowedValues) {
        if (areAllSpacesOrNull(value)) {
            return false;
        }
        if (value.equals("") && !isMandatory) {
            return true;
        }
        int val = Integer.parseInt(value);
        if (val < min || val > max) {
            for (int i = 0; i < allowedValues.length; i++) {
                if (value.equals(allowedValues[i])) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean isValidCellNumber(String number) {
        if (areAllSpacesOrNull(number)) {
            return true;
        } else if (number.matches(Regex.CELL_NUMBER.toString())) {
            return true;
        }
        return false;
    }

    private boolean isValidMobileNumber(String number) {
        if (areAllSpacesOrNull(number)) {
            return true;
        } else if (number.matches(Regex.MOBILE_NUMBER.toString())) {
            return true;
        }
        return false;
    }

    private boolean isValidDecimalNumber(String number) {
        if (areAllSpacesOrNull(number)) {
            return false;
        } else if (number.matches(Regex.DECIMAL_NUMBER.toString())) {
            return true;
        }
        return false;
    }

    private boolean checkForEmpty(String value, boolean isMandatory) {
        if (areAllSpacesOrNull(value)) {
            return false;
        }
        if (value.length() == 0 || value.equals("<Select an option>")) {
            if (!isMandatory) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean allowEmpty(String value) {
        return true;
    }

    private boolean checkForDateTime(String date) {
        Date d;
        try {
            d = Global.DATE_TIME_FORMAT.parse(date);
            if (d != null) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkForDate(String date) {
        Date d;
        try {
            d = Global.DATE_FORMAT.parse(date);
            if (d != null) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkIfIndusMrno(String mrno) {
        boolean isValid = true;
        isValid = mrno.matches(Global.identifierFormat);
        return isValid;
    }

    private boolean checkRooms(String rooms) {
        boolean isValid = true;
        isValid = rooms.matches(Regex.NO_OF_ROOMS.toString());
        return isValid;
    }

    private boolean checkNonZero2digit(String value) {
        boolean isValid = true;
        isValid = value.matches(Regex.NON_ZERO_2_DIGIT.toString());
        return isValid;
    }

    private boolean checkIfSsid(String ssid) {
        boolean isValid = true;
        isValid = ssid.matches(Regex.SSID.toString());
        return isValid;
    }

    // [A-Z]{1}[0-9]{12}
    private boolean checkIfValidId(String id) {
        if (id.matches("[A-Z]{1}[0-9]{12}")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidParticipantId(String id) {
        if (id.matches("[C]{1}[F]{1}[A-Z]{2}[A-Z]{4}]")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidManawanPatientId(String id) {
        if (id.matches("(cfman|CFMAN)([-])([0-9]{2}-)([0-9]{4})")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidMuzaffarabadPatientId(String id) {
        if (id.matches("(cfmuz|CFMUZ)([-])([0-9]{2}-)([0-9]{4})")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidCSCPatientId(String id) {
        if (id.matches("(esc|ESC)([-])([0-9]{2}-)([0-9]{4})")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidKarachiPatientId(String id) {
        if (id.matches("(cf|CF)([-])([0-9]{2}-)([0-9]{4})")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidMR(String value) {
        if (value.startsWith("201")) {
            return true;
        }
        return false;
    }

    private boolean checkIfValidTBR3Id(String id) {
        boolean isValid = true;
        isValid = id.length() == 14;
        id = id.replaceAll("\\W", "");
        // Validate Luhn check digit
        if (isValid) {
            String idWithoutCheckdigit = id.substring(0, id.length() - 1);
            char idCheckdigit = id.charAt(id.length() - 1);
            String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";
            idWithoutCheckdigit = idWithoutCheckdigit.trim();
            int sum = 0;
            for (int i = 0; i < idWithoutCheckdigit.length(); i++) {
                char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit.length() - i - 1);
                if (validChars.indexOf(ch) == -1)
                    isValid = false;
                int digit = (int) ch - 48;
                int weight;
                if (i % 2 == 0) {
                    weight = (2 * digit) - (int) (digit / 5) * 9;
                } else {
                    weight = digit;
                }
                sum += weight;
            }
            sum = Math.abs(sum) + 10;
            int checkDigit = (10 - (sum % 10)) % 10;
            isValid = checkDigit == Integer.parseInt(String.valueOf(idCheckdigit));
        }
        return isValid;
    }
}
