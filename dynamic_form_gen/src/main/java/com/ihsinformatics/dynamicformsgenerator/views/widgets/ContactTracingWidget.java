package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetailsSendable;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.DeviceUtils;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters.ContactDetailsAdapter;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget;

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
    private QuestionConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    LinearLayoutManager mLinearLayoutManager;
    TextView next;
    TextView previous;
    TextView questionText;

    private ArrayList<ContactDetailsSendable> currentData = new ArrayList<>();

    public ContactTracingWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);

        baseActivity = ((BaseActivity) getContext());

        relations.add("Parent");
        relations.add("Child");
        relations.add("Sibling");
        relations.add("Spouse");


        contactsText.add(new ContactDetails("Question Contact 1", "1", "Contact ID 1", "Contact First Name 1", "Contact Family Name 1", "Contact Age 1", "Contact Gender 1", "Contact Relation 1"));
        contactsText.add(new ContactDetails("Question Contact 2", "2", "Contact ID 2", "Contact First Name 2", "Contact Family Name 2", "Contact Age 2", "Contact Gender 2", "Contact Relation 2"));
        contactsText.add(new ContactDetails("Question Contact 3", "3", "Contact ID 3", "Contact First Name 3", "Contact Family Name 3", "Contact Age 3", "Contact Gender 3", "Contact Relation 3"));


        contactRecyclerView = (RecyclerView) findViewById(R.id.contactDetails);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };


//        layoutManager = new
//                PreCachingLayoutManager(baseActivity);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        layoutManager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(baseActivity));
//        contactRecyclerView.setLayoutManager(layoutManager);


        contactRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new ContactDetailsAdapter(context, contactsText, relations);
        contactRecyclerView.setAdapter(adapter);
        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setItemViewCacheSize(10);
        contactRecyclerView.setDrawingCacheEnabled(true);


        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }


        questionText = findViewById(R.id.tvQuestion);
        questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
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

                    saveCurrentPositionData(currentPosition);

                    currentPosition++;
                    questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
                    previous.setVisibility(View.VISIBLE);
                    mLinearLayoutManager.scrollToPosition(currentPosition);
                    dismissMessage();
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
                questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
                next.setVisibility(View.VISIBLE);
                mLinearLayoutManager.scrollToPosition(currentPosition);
            }
        });


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {

        boolean validation = true;
        if (currentPosition == (contactsText.size() - 1) && currentData.size() == contactsText.size() && isValid(currentPosition)) {
            saveCurrentPositionData(currentPosition);
            validation = true;
        } else {
            validation = false;
        }
        isValid(currentPosition);//just to add error on fields
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

            JSONArray arr = new JSONArray();

            for (int val = 0; val < currentData.size(); val++) {

                JSONObject temp = new JSONObject();
                temp.put("age", currentData.get(val).getContactAge());

                temp.put("patientID", currentData.get(val).getContactID());

                temp.put("PatientFirstName", currentData.get(val).getContactFirstName());

                temp.put("PatientFamilyName", currentData.get(val).getContactFamilyName());

                arr.put(temp);

            }

            params.put(ParamNames.PARAM_VALUE, arr);
        } else {
            mLinearLayoutManager.scrollToPosition(0);
            currentPosition=0;
            previous.setVisibility(View.INVISIBLE);

            if(currentPosition==(contactsText.size()-1))
            {
                next.setVisibility(View.INVISIBLE);
            }
            questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
            activity.addValidationError(question.getQuestionId(), question.getErrorMessage());
        }
        return params;

    }

    public String getValue() {

        String toReturn="";
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
    public void onFocusGained() {

    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {

    }


    private boolean isValid(int position) {
        View row = contactRecyclerView.getLayoutManager().findViewByPosition(position);
        Boolean finalValidation = true;

        if(null==row){
            currentPosition=0;
            mLinearLayoutManager.scrollToPosition(currentPosition);
            previous.setVisibility(View.INVISIBLE);

            if(currentPosition==contactsText.size())
            {
                next.setVisibility(View.INVISIBLE);
            }
            finalValidation=false;
        }else {
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
        }
        return finalValidation;

    }

    private boolean checkValidation(View view, int id) {
        if (id == R.id.etAgeYears) {
            EditText editText = (EditText) view;
            String years = editText.getText().toString();
            if (years != null && years.length() == 4) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeMonths) {

            EditText editText = (EditText) view;
            String months = editText.getText().toString();
            if (months != null && months.length() > 0) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etAgeDays) {

            EditText editText = (EditText) view;
            String days = editText.getText().toString();
            if (days != null && days.length() > 0) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientID) {

            EditText editText = (EditText) view;
            String patId = editText.getText().toString();
            if (patId != null && patId.matches(Global.identifierFormat)) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientName) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() > 3) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        } else if (id == R.id.etPatientFamilyName) {

            EditText editText = (EditText) view;
            String patName = editText.getText().toString();
            if (patName != null && patName.length() > 3) {
                return true;
            } else {
                editText.setError("Invalid field");
                return false;
            }
        }

        return false;
    }


    private void saveCurrentPositionData(int position) {
        View row = contactRecyclerView.getLayoutManager().findViewByPosition(position);
        EditText etAgeYears = row.findViewById(R.id.etAgeYears);
        EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
        EditText etAgeDays = row.findViewById(R.id.etAgeDays);
        EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
        EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);
        EditText etPatientFamilyName = (EditText) row.findViewById(R.id.etPatientFamilyName);

        if (currentData.size() < contactsText.size()) {
            currentData.add(position, new ContactDetailsSendable(etPatientID.getText().toString(), etPatientName.getText().toString(), etPatientFamilyName.getText().toString(), etAgeYears.getText().toString() + "-" + etAgeMonths.getText().toString() + "-" + etAgeDays.getText().toString()));
        } else {
            currentData.set(position, new ContactDetailsSendable(etPatientID.getText().toString(), etPatientName.getText().toString(), etPatientFamilyName.getText().toString(), etAgeYears.getText().toString() + "-" + etAgeMonths.getText().toString() + "-" + etAgeDays.getText().toString()));
        }
    }
}
