package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.widget.LinearLayout;

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
import com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters.ContactDetailsAdapter;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactTracingWidget extends InputWidget{

    private RecyclerView contactRecyclerView;
    private ArrayList<String> relations = new ArrayList<String>();
    private ArrayList<ContactDetails> contactsText = new ArrayList<ContactDetails>();


    private List<RangeOption> rangeOptions;
    private QuestionConfiguration configuration;
    private InputWidgetBakery widgetBakery;
    private LinearLayout llRepeatSpace;
    private List<Map<Integer, InputWidget>> repeatGroups;
    BaseActivity baseActivity;

    public ContactTracingWidget(Context context, Question question, int layoutId) {
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


        contactsText.add(new ContactDetails("Question Contact 1","1","Contact ID 1", "Contact Name 1", "Contact Age 1", "Contact Gender 1","Contact Relation 1"));
        contactsText.add(new ContactDetails("Question Contact 2","2","Contact ID 2", "Contact Name 2", "Contact Age 2", "Contact Gender 2","Contact Relation 2"));
        contactsText.add(new ContactDetails("Question Contact 3","3","Contact ID 3", "Contact Name 3", "Contact Age 3", "Contact Gender 3","Contact Relation 3"));
        contactsText.add(new ContactDetails("Question Contact 4","4","Contact ID 4", "Contact Name 4", "Contact Age 4", "Contact Gender 4","Contact Relation 4"));


        contactRecyclerView = (RecyclerView) findViewById(R.id.contactDetails);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        contactRecyclerView.setAdapter(new ContactDetailsAdapter(context,contactsText,relations));


        widgetBakery = new InputWidgetBakery();
        if (question.getRepeatables() != null && question.getRepeatables().size() > 0) {
            llRepeatSpace = findViewById(R.id.llRepeats);
            repeatGroups = new ArrayList<>();
        }

        baseActivity = ((BaseActivity) getContext());


    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        return true;
    }

    @Override
    public void setOptionsOrHint(Option... data) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
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

}
