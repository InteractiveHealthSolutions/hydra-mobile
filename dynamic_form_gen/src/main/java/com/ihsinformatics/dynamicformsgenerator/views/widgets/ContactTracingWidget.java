package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTracingConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetailsSendable;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters.ContactDetailsAdapter;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


        submitNumber = findViewById(R.id.submitNumber);

        etNumberOfContacts = findViewById(R.id.etNumberOfContacts);
        optionsLayout = findViewById(R.id.optionsLayout);


        submitNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactCount = etNumberOfContacts.getText().toString();

                if (null == contactCount || contactCount.equalsIgnoreCase("")) {
                    etNumberOfContacts.setError("Required Field");
                } else if (firstTime) {

                    int count = Integer.valueOf(contactCount);
                    if (count == 0) {
                        etNumberOfContacts.setError("Enter any number between 1 to 10");
                    } else if (count > 0 && count <= 10) {
                        etNumberOfContacts.setError(null);
                        for (int i = 0; i < count; i++) {
                            contactsText.add(new ContactDetails("Contact Details", String.valueOf(i + 1), configuration.getIdentifier().getDisplayText(), configuration.getFirstName().getDisplayText(), configuration.getFamilyName().getDisplayText(), configuration.getAge().getDisplayText(), configuration.getGender().getDisplayText(), configuration.getRelationship().getDisplayText()));
                        }
                        adapter.notifyDataSetChanged();
                        contactRecyclerView.setVisibility(View.VISIBLE);
                        optionsLayout.setVisibility(View.VISIBLE);
                        questionText.setText("Contact " + (currentPosition + 1) + " of " + contactsText.size());
                        firstTime = false;
                    } else {
                        etNumberOfContacts.setError("Enter any number between 1 to 10");
                    }
                } else {
                    final int previousSize = contactsText.size();
                    final int newSize = Integer.valueOf(contactCount);

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
        contactRecyclerView.setItemViewCacheSize(10);
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


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {


        boolean validation = true;
        if (isMendatory) {
            if (currentPosition == (contactsText.size() - 1) && isValid(currentPosition)) {
                saveCurrentPositionData(currentPosition, "validInput");
                validation = true;
            } else {
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
            params.put(ParamNames.PAYLOAD_TYPE, "CONTACT_TRACING");

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

            }
            params.put("numberOfPeople", currentData.size());

            params.put(ParamNames.PARAM_VALUE, arr);
        } else {

            resetToFirstViewHolder();

            if (!(currentPosition == (contactsText.size() - 1)))
                activity.addValidationError(question.getQuestionId(), "Mandatory field. Press Submit from last form");
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
            toReturn += "\n\nIdentifier: " + currentData.get(val).getContactID();
            toReturn += "\nName: " + currentData.get(val).getContactFirstName() + " " + currentData.get(val).getContactFamilyName();
            toReturn += "\nAge: " + currentData.get(val).getContactAge();
            toReturn += "\nGender: " + currentData.get(val).getGender();
            toReturn += "\nRelation: " + currentData.get(val).getRelation();

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
                //  String familyName = singleEntry[2].split(": ")[1];
                String age = singleEntry[2].split(": ")[1];
                String gender = singleEntry[3].split(": ")[1];
                String relation = singleEntry[4].split(": ")[1];
                //   String dob = singleEntry[6].split(": ")[1];

                editedData.add(new ContactDetailsSendable(identifier, fname, fname, age, gender, relation, age));
            }
            currentPosition = 0;
            mLinearLayoutManager.scrollToPosition(currentPosition);

            View row = contactRecyclerView.getLayoutManager().findViewByPosition(0);

            EditText etDOB = row.findViewById(R.id.etPatientDOB);
            EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
            EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
            EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);

            etDOB.setText(editedData.get(0).getDob());
            etPatientID.setText(editedData.get(0).getContactID());
            etPatientName.setText(editedData.get(0).getContactFirstName());
            etPatientFamilyName.setText(editedData.get(0).getContactFamilyName());

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
        if (id == R.id.etAgeYears && configuration.getAge().isMandatory()) {
            EditText editText = (EditText) view;
            String years = editText.getText().toString();
            if (years != null && years != "" && years != " " && years.length() > 0) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeMonths && configuration.getAge().isMandatory()) {

            EditText editText = (EditText) view;
            String months = editText.getText().toString();
            if (months != null && months.length() > 0) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeDays && configuration.getAge().isMandatory()) {

            EditText editText = (EditText) view;
            String days = editText.getText().toString();
            if (days != null && days.length() > 0) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientID && configuration.isCreatePatient() && configuration.getIdentifier().isMandatory()) {

            EditText editText = (EditText) view;
            String patId = editText.getText().toString();
            if (patId != null && patId.matches(Global.identifierFormat)) {
                return true;
            } else if (patId == null) {
                editText.setError("Required field");
                return false;
            } else if (!patId.matches(Global.identifierFormat)) {
                editText.setError("Invalid identifier format. Check your web configuration");
                return false;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientName && configuration.getFirstName().isMandatory()) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() >= 2) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientFamilyName && configuration.getFamilyName().isMandatory()) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() >= 2) {
                return true;
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
}
