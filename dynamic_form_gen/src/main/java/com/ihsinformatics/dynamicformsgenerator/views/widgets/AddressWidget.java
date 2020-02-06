package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.AddressConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.Location;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.utils.AppUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naveed Iqbal on 6/13/2018.
 */

public class AddressWidget extends InputWidget {

    private AddressConfiguration configuration;
    private List<AddressConfiguration.AddressTag> addressTags;
    private List<AddressConfiguration.OpenAddressField> openAddressFields;
    private LinearLayout llMain;
    private List<SpinnerAddressItem> spinnerAddressItems;
    private List<EditTextAddressItem> editTextAddressItems;

    public AddressWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        if (super.configuration instanceof AddressConfiguration)
            configuration = (AddressConfiguration) super.configuration;
        else
            throw new UnsupportedOperationException("Unsupported configuration found. \nRequired " + AddressConfiguration.class.getName());

        spinnerAddressItems = new ArrayList<>();
        editTextAddressItems = new ArrayList<>();
        llMain = (LinearLayout) findViewById(R.id.llMain);
        setOptionsOrHint();
    }

    @Override
    public boolean isValidInput(boolean isNullable) {
        return false;
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        addressTags = configuration.getSortedAddressTags();
        for (AddressConfiguration.AddressTag addressTag : addressTags) {
            LinearLayout addressItem = generateSpinnerAddressItem(addressTag, addressTag.getTagName());
            llMain.addView(addressItem);
        }

        openAddressFields = configuration.getSortedOpenAddressFields();
        for (AddressConfiguration.OpenAddressField openAddressField : openAddressFields) {
            RelativeLayout addressItem = generateEditTextAddressItem(openAddressField);
            llMain.addView(addressItem);
        }

        spinnerAddressItems.get(0).resetAdapter(null, null);

    }

    @Override
    public void onFocusGained() {

    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {

    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject answer = new JSONObject();
        JSONObject addressJson = new JSONObject();
        boolean isValidOther = true;

        for (SpinnerAddressItem addressItem : spinnerAddressItems) {

            String response;
            if (addressItem.etOther.getVisibility() != View.VISIBLE)
                response = addressItem.spValues.getSelectedItem().toString();
            else {
                response = addressItem.etOther.getText().toString();
            }
            if (response.trim().length() < 1) {
                addressItem.tvOtherMessage.setVisibility(VISIBLE);
                addressItem.tvOtherMessage.setText(R.string.invalid_input);
                // TODO uncheck this to make other mandatory isValidOther = false;
            } else {
                addressItem.tvOtherMessage.setVisibility(GONE);
                addressItem.tvOtherMessage.setText("");
                addressJson.put(addressItem.tvTag.getText().toString(), response);
            }
        }

        if (!isValidOther) {
            activity.addValidationError(getQuestionId(), context.getResources().getString(R.string.invalid_input));

        } else {
            dismissMessage();
        }

        for (EditTextAddressItem addressItem : editTextAddressItems) {
            addressJson.put(addressItem.paramName, addressItem.etValues.getText().toString());
        }

        answer.put(ParamNames.PARAM_NAME, question.getParamName());
        answer.put(ParamNames.VALUE, addressJson);
        answer.put(ParamNames.PAYLOAD_TYPE,question.getPayload_type());


        return answer;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void destroy() {

    }

    private RelativeLayout generateEditTextAddressItem(AddressConfiguration.OpenAddressField openAddressField) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout llItem = (RelativeLayout) layoutInflater.inflate(R.layout.layout_widget_address_edit_text, this, false);
        llItem.findViewById(R.id.tvNumber).setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, AppUtility.dpToPx(getResources(), 5), 0, 0);
        llItem.setLayoutParams(layoutParams);
        EditTextAddressItem addressItem = new EditTextAddressItem(llItem, openAddressField);
        editTextAddressItems.add(addressItem);
        return llItem;
    }

    private LinearLayout generateSpinnerAddressItem(AddressConfiguration.AddressTag addressTag, String tagName) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout llItem = (LinearLayout) layoutInflater.inflate(R.layout.layout_address_item, this, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, AppUtility.dpToPx(getResources(), 5));
        llItem.setLayoutParams(layoutParams);
        SpinnerAddressItem addressItem = new SpinnerAddressItem(llItem, addressTag, tagName);
        spinnerAddressItems.add(addressItem);
        return llItem;
    }

    private class SpinnerAddressItem implements AdapterView.OnItemSelectedListener {

        TextView tvTag;
        Spinner spValues;
        EditText etOther;
        TextView tvOtherMessage;

        AddressConfiguration.AddressTag addressTag;

        public SpinnerAddressItem(LinearLayout linearLayout, AddressConfiguration.AddressTag addressTag, String tag) {
            tvTag = (TextView) linearLayout.findViewById(R.id.tvQuestion);
            spValues = (Spinner) linearLayout.findViewById(R.id.spAnswer);
            etOther = (EditText) linearLayout.findViewById(R.id.etAnswer);
            tvOtherMessage = (TextView) linearLayout.findViewById(R.id.tvMessage);

            this.addressTag = addressTag;

            tvTag.setText(tag);
            spValues.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int selected = addressTags.indexOf(addressTag/*new AddressConfiguration.AddressTag(0, tvTag.getText().toString())*/);
            if (selected + 1 < spinnerAddressItems.size()) {
                String selectedText = spValues.getItemAtPosition(position).toString();
                spinnerAddressItems.get(selected + 1).resetAdapter(selectedText, addressTag.getTagName());
            }

            if (spValues.getItemAtPosition(position).toString().equals(context.getString(R.string.other))) {
                etOther.setVisibility(VISIBLE);
            } else {
                etOther.setVisibility(GONE);
            }
        }

        private void resetAdapter(String parentValue, String parentTag) {
            List<Location> locations;
            if (parentValue != context.getString(R.string.other)) {
                locations = DataAccess.getInstance().fetchLocationsByTag(context, tvTag.getText().toString(), parentValue, parentTag);
                etOther.setVisibility(GONE);
            } else {
                locations = new ArrayList<>();
                etOther.setVisibility(VISIBLE);

            }
            Location otherLocation = new Location();
            otherLocation.setName(context.getString(R.string.other));
            locations.add(otherLocation);
            ArrayAdapter<Location> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, locations);
            spValues.setAdapter(adapter);
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class EditTextAddressItem {

        TextView tvTag;
        EditText etValues;
        String paramName;

        public EditTextAddressItem(RelativeLayout linearLayout, AddressConfiguration.OpenAddressField openAddressField) {
            tvTag = (TextView) linearLayout.findViewById(R.id.tvQuestion);
            etValues = (EditText) linearLayout.findViewById(R.id.etAnswer);

            tvTag.setText(openAddressField.getFieldName());
            paramName = openAddressField.getParamName();
        }
    }
}