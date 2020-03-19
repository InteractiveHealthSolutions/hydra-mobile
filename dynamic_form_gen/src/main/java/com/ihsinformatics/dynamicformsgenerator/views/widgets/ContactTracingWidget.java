package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.RangeOption;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters.ContactDetailsAdapter;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget;

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


    public ContactTracingWidget(final Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof QuestionConfiguration)
            configuration = (QuestionConfiguration) super.configuration;
        rangeOptions = new ArrayList<>(0);


        relations.add("Mother");
        relations.add("Father");
        relations.add("Daughter");
        relations.add("Son");
        relations.add("Brother");
        relations.add("Sister");
        relations.add("Spouse");


        contactsText.add(new ContactDetails("Question Contact 1", "1", "Contact ID 1", "Contact Name 1", "Contact Age 1", "Contact Gender 1", "Contact Relation 1"));
        contactsText.add(new ContactDetails("Question Contact 2", "2", "Contact ID 2", "Contact Name 2", "Contact Age 2", "Contact Gender 2", "Contact Relation 2"));
        contactsText.add(new ContactDetails("Question Contact 3", "3", "Contact ID 3", "Contact Name 3", "Contact Age 3", "Contact Gender 3", "Contact Relation 3"));
        contactsText.add(new ContactDetails("Question Contact 4", "4", "Contact ID 4", "Contact Name 4", "Contact Age 4", "Contact Gender 4", "Contact Relation 4"));


        contactRecyclerView = (RecyclerView) findViewById(R.id.contactDetails);
        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };


        contactRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new ContactDetailsAdapter(context, contactsText, relations);
        contactRecyclerView.setAdapter(adapter);


        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }


        questionText = findViewById(R.id.tvQuestion);
        questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        previous.setVisibility(View.INVISIBLE);  //initially the recycler view is on first form

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid(currentPosition)) {
                    if (currentPosition == (contactsText.size() - 2)) {
                        next.setVisibility(View.INVISIBLE);
                    }
                    currentPosition++;
                    questionText.setText("Form for Patient Contact " + (currentPosition + 1) + " of " + contactsText.size());
                    previous.setVisibility(View.VISIBLE);
                    mLinearLayoutManager.scrollToPosition(currentPosition);
                    dismissMessage();
                } else {
                    ToastyWidget.getInstance().displayError(context, "Some missing fields", Toast.LENGTH_SHORT);
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

        baseActivity = ((BaseActivity) getContext());


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {

        boolean validation = true;
        if (currentPosition == (contactsText.size() - 1) && isValid(currentPosition)) {
            validation = true;
        } else {
            isValid(currentPosition);
            validation = false;
        }
        return validation;
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        if (isValidInput(question.isMandatory())) {

        } else {
            activity.addValidationError(question.getQuestionId(), question.getErrorMessage());
        }
        return new JSONObject();
    }

    public String getValue() {

        return new String();
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
        EditText etAgeYears = row.findViewById(R.id.etAgeYears);
        EditText etAgeMonths = row.findViewById(R.id.etAgeMonths);
        EditText etAgeDays = row.findViewById(R.id.etAgeDays);
        EditText etPatientID = (EditText) row.findViewById(R.id.etPatientID);
        EditText etPatientName = (EditText) row.findViewById(R.id.etPatientName);

        ArrayList<View> myview = new ArrayList<>();
        myview.add(etAgeYears);
        myview.add(etAgeMonths);
        myview.add(etAgeDays);
        myview.add(etPatientID);
        myview.add(etPatientName);

        Boolean finalValidation = true;

        for (int i = 0; i < myview.size(); i++) {
            Boolean validation = checkValidation(myview.get(i), myview.get(i).getId());
            if (!validation) {
                finalValidation = false;
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
        }

        return false;
    }
}
