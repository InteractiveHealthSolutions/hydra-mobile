package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.common.Constants;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTracingConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetailsSendable;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters.ContactDetailsAdapter;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContactTracingWidget extends InputWidget {

    private RecyclerView contactRecyclerView;
    private ArrayList<String> relations = new ArrayList<String>();
    private ArrayList<ContactDetails> contactsText = new ArrayList<ContactDetails>();
    ContactDetailsAdapter adapter;
    private int currentPosition = 0;


    private List<RangeOption> rangeOptions;
    private ContactTracingConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    LinearLayoutManager mLinearLayoutManager;
    TextView next;
    TextView previous;
    TextView questionText;

    boolean firstTime = true;

    LinearLayout optionsLayout;

    Button submitNumber;
    EditText etNumberOfContacts;

    private ArrayList<ContactDetailsSendable> currentData = new ArrayList<>();
    private ArrayList<ContactDetailsSendable> editedData;

    public ContactTracingWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof ContactTracingConfiguration)
            configuration = (ContactTracingConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);

        baseActivity = ((BaseActivity) getContext());

        relations.add("Parent");
        relations.add("Child");
        relations.add("Sibling");
        relations.add("Spouse");
        relations.add("Aunt/Uncle");
        relations.add("Contact");
        relations.add("Other Relative");
        relations.add("Other incl live-in Domestic stuff");


        submitNumber = findViewById(R.id.submitNumber);

        etNumberOfContacts = findViewById(R.id.etNumberOfContacts);
        optionsLayout = findViewById(R.id.optionsLayout);


        submitNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactCount = etNumberOfContacts.getText().toString();

                if (null == contactCount || contactCount.equalsIgnoreCase("")) {
                    etNumberOfContacts.setError("Required Field");
                } else if (Integer.valueOf(contactCount) <= 0) {

                    etNumberOfContacts.setError("Enter any number between 1 to 15");

                } else if (firstTime) {

                    int count = Integer.valueOf(contactCount);
                    if (count == 0) {
                        etNumberOfContacts.setError("Enter any number between 1 to 15");
                    } else if (count > 0 && count <= 15) {
                        etNumberOfContacts.setError(null);
                        for (int i = 0; i < count; i++) {
                            contactsText.add(new ContactDetails("Contact Details", String.valueOf(i + 1), configuration.getIdentifier().getDisplayText(), configuration.getFirstName().getDisplayText(), configuration.getFamilyName().getDisplayText(), configuration.getAge().getDisplayText(), configuration.getGender().getDisplayText(), configuration.getRelationship().getDisplayText()));
                        }
                        adapter.notifyDataSetChanged();
                        contactRecyclerView.setVisibility(View.VISIBLE);
                        optionsLayout.setVisibility(View.VISIBLE);
                        questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
                        firstTime = false;
                        if (contactsText.size() == 1) {
                            next.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        etNumberOfContacts.setError("Enter any number between 1 to 15");
                    }
                } else {
                    final int previousSize = contactsText.size();
                    final int newSize = Integer.valueOf(contactCount);
                    if (newSize > 0 && newSize <= 15) {
                        if (newSize < previousSize) {

                            final int addedSize = previousSize - newSize;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("Last " + addedSize + " forms will be deleted. You wish to proceed?");
                            alertDialogBuilder.setTitle("Warning");
                            alertDialogBuilder.setCancelable(true);
                            alertDialogBuilder.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            for (int i = previousSize - 1; i >= newSize; i--) {
                                                contactsText.remove(i);
                                            }

                                            resetToFirstViewHolder();

                                            dialog.dismiss();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialogBuilder.show();

                        } else {
                            int difference = newSize - previousSize;
                            for (int i = 0; i < difference; i++) {
                                contactsText.add(new ContactDetails("Contact Details", String.valueOf(previousSize + i + 1), configuration.getIdentifier().getDisplayText(), configuration.getFirstName().getDisplayText(), configuration.getFamilyName().getDisplayText(), configuration.getAge().getDisplayText(), configuration.getGender().getDisplayText(), configuration.getRelationship().getDisplayText()));
                            }
                            if (difference > 0)
                                next.setVisibility(View.VISIBLE);

                            questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
                        }
                    } else {
                        etNumberOfContacts.setError("Enter any number between 1 to 15");
                    }
                }
            }
        });

        contactRecyclerView = (RecyclerView) findViewById(R.id.contactDetails);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        contactRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new ContactDetailsAdapter(context, contactsText, relations, configuration);
        contactRecyclerView.setAdapter(adapter);
        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setItemViewCacheSize(15);
        contactRecyclerView.setDrawingCacheEnabled(true);

        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }

        questionText = findViewById(R.id.contactNumber);

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        if (currentPosition == 0)
            previous.setVisibility(View.INVISIBLE);  //initially the recycler view is on first form
        if (currentPosition == (contactsText.size() - 1)) {
            next.setVisibility(View.INVISIBLE);
        }

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid(currentPosition)) {
                    if (currentPosition == (contactsText.size() - 2)) {
                        next.setVisibility(View.INVISIBLE);
                    }

                    saveCurrentPositionData(currentPosition, "next");
                    currentPosition++;
                    questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
                    previous.setVisibility(View.VISIBLE);
                    mLinearLayoutManager.scrollToPosition(currentPosition);
                    dismissMessage();

                } else {
                    mLinearLayoutManager.scrollToPosition(currentPosition);
                }
            }
        });


        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPosition == 1) {
                    previous.setVisibility(View.INVISIBLE);
                }
                currentPosition--;
                saveCurrentPositionData(currentPosition, "prev");
                questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
                next.setVisibility(View.VISIBLE);
                mLinearLayoutManager.scrollToPosition(currentPosition);

            }
        });


        contactRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                fillDataForEditing();
            }
        });


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {


        boolean validation = true;
        if (isMendatory) {
            if (isValid(currentPosition) && currentPosition == (contactsText.size() - 1)) {
                saveCurrentPositionData(currentPosition, "validInput");
                validation = true;
            } else {
                validation = false;
            }
        } else {
            if (isValid(currentPosition) && currentPosition == (contactsText.size() - 1)) {
                saveCurrentPositionData(currentPosition, "validInput");
                validation = true;
            } else if (!etNumberOfContacts.getText().toString().trim().equals("")) {
                validation = false;
            }
        }
        return validation;
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }


    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject params = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            params.put(ParamNames.PARAM_NAME, "ContactRegistry");

            //Necessary for every widget to have PAYLOAD_TYPE AND PERSON_ATTRIBUTE
            params.put(ParamNames.PAYLOAD_TYPE, "CONTACT_TRACING");

            if (question.getAttribute())
                params.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_TRUE);
            else
                params.put(ParamNames.PERSON_ATTRIBUTE, ParamNames.PERSON_ATTRIBUTE_FALSE);


            params.put(ParamNames.PARAM_CREATE_PATIENT, configuration.isCreatePatient());


            JSONArray arr = new JSONArray();

            for (int val = 0; val < currentData.size(); val++) {


                JSONObject temp = new JSONObject();
                temp.put("age", currentData.get(val).getContactAge());
                temp.put("dob", currentData.get(val).getDob());

                if (configuration.isCreatePatient())
                    temp.put("patientID", currentData.get(val).getContactID());

                temp.put("patientGivenName", currentData.get(val).getContactFirstName());
                temp.put("patientFamilyName", currentData.get(val).getContactFamilyName());
                temp.put("gender", currentData.get(val).getGender());
                temp.put("relation", currentData.get(val).getRelation());

                arr.put(temp);

                initOfflinePatient(currentData.get(val));

            }
            params.put("numberOfPeople", currentData.size());

            params.put(ParamNames.PARAM_VALUE, arr);
        } else {

            resetToFirstViewHolder();

            if (!(currentPosition == (contactsText.size() - 1)))
                activity.addValidationError(question.getQuestionId(), "Mandatory field. Press Save from last form");
            else
                activity.addValidationError(question.getQuestionId(), question.getErrorMessage());
        }
        return params;

    }

    public String getValue() {

        String toReturn = "";
        try {
            toReturn = getAnswer().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toReturn;

    }

    @Override
    public void destroy() {

    }

    @Override
    public String getServiceHistoryValue() {

        String toReturn = "";

        toReturn += currentData.size() + "\n";

        for (int val = 0; val < currentData.size(); val++) {


            String identifier = currentData.get(val).getContactID();
            if ("".equals(identifier.trim())) {
                toReturn += "\n\nIdentifier: " + "-";
            }
            else
            {
                toReturn += "\n\nIdentifier: " + identifier;
            }


            String firstName = currentData.get(val).getContactFirstName();
            if ("".equals(firstName.trim())) {
                toReturn += "\nFirst Name: " +  "-";
            }
            else
            {
                toReturn += "\nFirst Name: " + firstName;
            }


            String familyName = currentData.get(val).getContactFamilyName();
            if ("".equals(familyName.trim())) {
                toReturn += "\nFamily Name: " +  "-";
            }
            else
            {
                toReturn += "\nFamily Name: " + familyName;
            }


            String[] ageFields = currentData.get(val).getContactAge().split("-");

            if (ageFields.length > 0) {
                toReturn += "\nAge: " + ageFields[0] + " Years " + ageFields[1] + " Months " + ageFields[2] + " Days";
            } else {
                toReturn += "\nAge: " + "0" + " Years " + "0" + " Months " + "0" + " Days";
            }


            String gender = currentData.get(val).getGender();
            if ("".equals(gender.trim())) {
                toReturn += "\nGender: " +  "-";
            }
            else
            {
                toReturn += "\nGender: " + gender;
            }


            String relation = currentData.get(val).getRelation();
            if ("".equals(relation.trim())) {
                toReturn += "\nRelation: " +  "-";
            }
            else
            {
                toReturn += "\nRelation: " + currentData.get(val).getRelation();
            }


        }

        return toReturn;
    }


    @Override
    public void onFocusGained() {

    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {

        String[] numberAndEntries = answer.split("\n\n\n");

        int contactsCount = Integer.valueOf(numberAndEntries[0]);
        etNumberOfContacts.setText(numberAndEntries[0]);


        if (contactsCount > 0) {

            //Repeated code
            for (int i = 0; i < contactsCount; i++) {
                contactsText.add(new ContactDetails("Contact Details", String.valueOf(i + 1), configuration.getIdentifier().getDisplayText(), configuration.getFirstName().getDisplayText(), configuration.getFamilyName().getDisplayText(), configuration.getAge().getDisplayText(), configuration.getGender().getDisplayText(), configuration.getRelationship().getDisplayText()));
            }
            adapter.notifyDataSetChanged();
            contactRecyclerView.setVisibility(View.VISIBLE);
            optionsLayout.setVisibility(View.VISIBLE);
            questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
            firstTime = false;
            //till here (for refactoring)

            String[] allAnswers = numberAndEntries[1].split("\n\n");
            editedData = new ArrayList<>();

            for (int i = 0; i < allAnswers.length; i++) {

                String[] singleEntry = allAnswers[i].split("\n");
                String identifier = singleEntry[0].split(": ")[1];
                String fname = singleEntry[1].split(": ")[1];
                String familyName = singleEntry[2].split(": ")[1];
                String age = singleEntry[3].split(": ")[1];
                String gender = singleEntry[4].split(": ")[1];
                String relation = singleEntry[5].split(": ")[1];

                if("-".equals(identifier))
                    identifier="";
                if("-".equals(fname))
                    fname="";
                if("-".equals(familyName))
                    familyName="";

                editedData.add(new ContactDetailsSendable(identifier, fname, familyName, age, gender, relation, ""));  // no need to date of birth here since its automatically calculated using age

            }

            currentPosition = 0;
            mLinearLayoutManager.scrollToPosition(currentPosition);

        }
    }


    private void fillDataForEditing() {

        if (null != editedData && editedData.size() > currentPosition) {
            View row = contactRecyclerView.getLayoutManager().findViewByPosition(currentPosition);

            EditText etDOB = row.findViewById(R.id.etPatientDOB);
            EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
            EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
            EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);
            RadioGroup gender = (RadioGroup) row.findViewById(R.id.genderWidget);
            EditText etAgeYears = row.findViewById(R.id.etAgeYears);
            EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
            EditText etAgeDays = row.findViewById(R.id.etAgeDays);
            Spinner spRelation = (Spinner) row.findViewById(R.id.spRelations);

            //etDOB.setText(editedData.get(currentPosition).getDob());
            etPatientID.setText(editedData.get(currentPosition).getContactID());
            etPatientName.setText(editedData.get(currentPosition).getContactFirstName());
            etPatientFamilyName.setText(editedData.get(currentPosition).getContactFamilyName());

            String[] extractingYears = editedData.get(currentPosition).getContactAge().split(" Years ");
            etAgeYears.setText(extractingYears[0]);

            String[] extractingMonths = extractingYears[1].split(" Months ");
            etAgeMonths.setText(extractingMonths[0]);

            String[] extractingDays = extractingMonths[1].split(" Days");
            etAgeDays.setText(extractingDays[0]);

            spRelation.setSelection(relations.indexOf(editedData.get(currentPosition).getRelation()));

            if (editedData.get(currentPosition).getGender().equalsIgnoreCase("female")) {
                RadioButton rb = (RadioButton) gender.findViewById(R.id.female);
                rb.setChecked(true);
            } else {
                RadioButton rb = (RadioButton) gender.findViewById(R.id.male);
                rb.setChecked(true);
            }
        }
    }

    private boolean isValid(int position) {
        View row = contactRecyclerView.getLayoutManager().findViewByPosition(position);
        Boolean finalValidation = true;

        if (row != null) {
            EditText etAgeYears = row.findViewById(R.id.etAgeYears);
            EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
            EditText etAgeDays = row.findViewById(R.id.etAgeDays);
            EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
            EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
            EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);


            ArrayList<View> myview = new ArrayList<>();
            myview.add(etAgeYears);
            myview.add(etAgeMonths);
            myview.add(etAgeDays);
            myview.add(etPatientID);
            myview.add(etPatientName);
            myview.add(etPatientFamilyName);


            for (int i = 0; i < myview.size(); i++) {
                Boolean validation = checkValidation(myview.get(i), myview.get(i).getId());
                if (!validation) {
                    finalValidation = false;
                }
            }
        } else {
            finalValidation = false;
        }
        return finalValidation;

    }

    private boolean checkValidation(View view, int id) {
        if (id == R.id.etAgeYears && (configuration.getAge().isMandatory() || configuration.isCreatePatient())) {
            EditText editText = (EditText) view;
            String years = editText.getText().toString();
            if (years != null && !years.equals("") && !years.equals(" ") && years.length() > 0) {
                if (TextUtils.isDigitsOnly(years) && Integer.parseInt(years) >= 0)
                    return true;
                else {
                    editText.setError("Age can't be negative");
                    return false;
                }
            } else if (years == null || years.equals("") || years.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeMonths && (configuration.getAge().isMandatory() || configuration.isCreatePatient())) {

            EditText editText = (EditText) view;
            String months = editText.getText().toString();
            if (months != null && months.length() > 0) {
                if (TextUtils.isDigitsOnly(months) && Integer.parseInt(months) >= 0)
                    return true;
                else {
                    editText.setError("Age can't be negative");
                    return false;
                }
            } else if (months == null || months.equals("") || months.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeDays && (configuration.getAge().isMandatory() || configuration.isCreatePatient())) {

            EditText editText = (EditText) view;
            String days = editText.getText().toString();
            if (days != null && days.length() > 0) {
                if (TextUtils.isDigitsOnly(days) && Integer.parseInt(days) >= 0)
                    return true;
                else {
                    editText.setError("Age can't be negative");
                    return false;
                }
            } else if (days == null || days.equals("") || days.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientID && (configuration.getIdentifier().isMandatory() || configuration.isCreatePatient())) {

            EditText editText = (EditText) view;
            String patId = editText.getText().toString();
            if (patId != null && patId.matches(Global.identifierFormat)) {
                return true;
            } else if (patId == null || patId.equals("") || patId.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else if (!patId.matches(Global.identifierFormat)) {
                editText.setError("Invalid identifier format. Check your web configuration");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientName && (configuration.getFirstName().isMandatory() || configuration.isCreatePatient())) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() >= 2) {
                return true;
            } else if (patName == null || patName.equals("") || patName.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientFamilyName && (configuration.getFamilyName().isMandatory() || configuration.isCreatePatient())) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() >= 2) {
                return true;
            } else if (patName == null || patName.equals("") || patName.equals(" ")) {
                editText.setError("Required field");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        }

        return true;
    }


    private void saveCurrentPositionData(int position, String buttonType) {

        if (buttonType.equalsIgnoreCase("next")) {
            View row = contactRecyclerView.getLayoutManager().findViewByPosition(position);
            EditText etAgeYears = row.findViewById(R.id.etAgeYears);
            EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
            EditText etAgeDays = row.findViewById(R.id.etAgeDays);
            EditText etDOB = row.findViewById(R.id.etPatientDOB);
            EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
            EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
            EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);

            RadioGroup gender = (RadioGroup) row.findViewById(R.id.genderWidget);
            RadioButton rb = (RadioButton) gender.findViewById(gender.getCheckedRadioButtonId());

            Spinner spRelation = (Spinner) row.findViewById(R.id.spRelations);

            currentData.add(position, new ContactDetailsSendable(etPatientID.getText().toString(), etPatientName.getText().toString(), etPatientFamilyName.getText().toString(), etAgeYears.getText().toString() + "-" + etAgeMonths.getText().toString() + "-" + etAgeDays.getText().toString(), rb.getText().toString(), spRelation.getSelectedItem().toString(), etDOB.getText().toString()));

        } else if (buttonType.equalsIgnoreCase("prev")) {
            currentData.remove(position);

        } else if (buttonType.equalsIgnoreCase("validInput")) {
            if (currentData.size() == contactsText.size()) {

            } else {
                View row = contactRecyclerView.getLayoutManager().findViewByPosition(position);
                EditText etAgeYears = row.findViewById(R.id.etAgeYears);
                EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
                EditText etAgeDays = row.findViewById(R.id.etAgeDays);
                EditText etDOB = row.findViewById(R.id.etPatientDOB);
                EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
                EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
                EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);

                RadioGroup gender = (RadioGroup) row.findViewById(R.id.genderWidget);
                RadioButton rb = (RadioButton) gender.findViewById(gender.getCheckedRadioButtonId());

                Spinner spRelation = (Spinner) row.findViewById(R.id.spRelations);

                currentData.add(position, new ContactDetailsSendable(etPatientID.getText().toString(), etPatientName.getText().toString(), etPatientFamilyName.getText().toString(), etAgeYears.getText().toString() + "-" + etAgeMonths.getText().toString() + "-" + etAgeDays.getText().toString(), rb.getText().toString(), spRelation.getSelectedItem().toString(), etDOB.getText().toString()));

            }
        }
    }

    private void resetToFirstViewHolder() {
        mLinearLayoutManager.scrollToPosition(0);
        currentPosition = 0;
        currentData.clear();
        previous.setVisibility(View.INVISIBLE);

        if (currentPosition == (contactsText.size() - 1)) {
            next.setVisibility(View.INVISIBLE);
        } else if (next.getVisibility() == View.INVISIBLE) {
            next.setVisibility(View.VISIBLE);
        }
        questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());

    }

    private void initOfflinePatient(ContactDetailsSendable contactDetails) throws JSONException {
        OfflinePatient offlinePatient = new OfflinePatient();
        OfflinePatient exisitingOfflinePatient = DataAccess.getInstance().getPatientByMRNumber(context, contactDetails.getContactID());
        if (exisitingOfflinePatient == null) {
            offlinePatient.setMrNumber(contactDetails.getContactID());
            offlinePatient.setName(contactDetails.getContactFirstName() + " " + contactDetails.getContactFamilyName());
            offlinePatient.setGender(contactDetails.getGender());
            try {

                Date date = Global.DATE_TIME_FORMAT.parse(contactDetails.getDob());
                String value = Global.OPENMRS_DATE_FORMAT.format(date);
                offlinePatient.setDob(Global.OPENMRS_DATE_FORMAT.parse(value).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String fieldJsonString = new JSONObject().toString();
            JSONObject existingFieldsJson = new JSONObject(fieldJsonString);

            for (String i : ParamNames.SUMMARY_VARIBALES) {
                existingFieldsJson.put(i, "");
            }

            for (String i : ParamNames.SUMMARY_VARIABLES_OBJECTS) {
                existingFieldsJson.put(i, new JSONObject());
            }

            for (String i : ParamNames.SUMMARY_VARIABLES_ARRAYS) {
                existingFieldsJson.put(i, new JSONArray());
            }


            offlinePatient.setFieldDataJson(existingFieldsJson.toString());

            JSONObject encounterCount = new JSONObject();

            //Initialization of all filled encounters count via this device as 0
            Collection<String> encounters = Constants.getEncounterTypes().values();
            for (String i : encounters) {
                encounterCount.put(i, 0);
            }
            offlinePatient.setEncounterJson(encounterCount.toString());


            DataAccess.getInstance().insertOfflinePatient(context, offlinePatient);

        }
    }
}
