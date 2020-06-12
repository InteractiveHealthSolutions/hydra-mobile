package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ContactDetailsListAdapter  extends ArrayAdapter<ContactDetails> {

    private Context context;
    private List<ContactDetails> contactDetails;
    private List<String> relations;
    private HashMap<Integer, ContactDetails> data = new HashMap<Integer, ContactDetails>();
    private int lastPosition = -1;

    public ContactDetailsListAdapter(Context context, List<ContactDetails> contactDetails, List<String> relations) {
        super(context, R.layout.layout_widget_contact_registry, contactDetails);
        this.context = context;
        this.contactDetails = contactDetails;
        this.relations=relations;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ContactDetails dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_widget_contact_registry, parent, false);

            viewHolder.contactQuestionText = (TextView) convertView.findViewById(R.id.tvQuestionContact);
            viewHolder.contactTvNumber = (TextView) convertView.findViewById(R.id.tvNumberContact);

            viewHolder.contactID = (TextView) convertView.findViewById(R.id.patientID);
            viewHolder.contactFirstName = (TextView) convertView.findViewById(R.id.patientName);
            viewHolder.contactFamilyName = (TextView) convertView.findViewById(R.id.patientFamilyName);
            viewHolder.contactGender = (TextView) convertView.findViewById(R.id.contactGender);
            viewHolder.contactRelation = (TextView) convertView.findViewById(R.id.contactRelation);
            viewHolder.relationsList = (Spinner) convertView.findViewById(R.id.spRelations);

            viewHolder.etPatientID = (EditText) convertView.findViewById(R.id.etPatientID);
            viewHolder.etPatientName = (EditText) convertView.findViewById(R.id.etPatientName);
            viewHolder.genderWidget = (RadioGroup) convertView.findViewById(R.id.genderWidget);
            viewHolder.spRelations = (Spinner) convertView.findViewById(R.id.spRelations);


            viewHolder.calendar = Calendar.getInstance();


            viewHolder.etAgeYears = convertView.findViewById(R.id.etAgeYears);
            viewHolder.etAgeMonths = convertView.findViewById(R.id.etAgeMonths);
            viewHolder.etAgeDays = convertView.findViewById(R.id.etAgeDays);

            viewHolder.clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    viewHolder.picker = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Toast.makeText(context,dayOfMonth + "/" + (monthOfYear + 1) + "/" + year,Toast.LENGTH_SHORT).show();
                                    viewHolder. etAgeYears.setText(String.valueOf(year));
                                    viewHolder.etAgeMonths.setText(String.valueOf(monthOfYear));
                                    viewHolder.etAgeDays.setText(String.valueOf(dayOfMonth));

                                    viewHolder.etAgeYears.setError(null);
                                    viewHolder.etAgeMonths.setError(null);
                                    viewHolder.etAgeDays.setError(null);


                                }
                            }, year, month, day);
                    viewHolder.picker.show();
                }
            };

            viewHolder.tvYears = convertView.findViewById(R.id.tvYears);
            viewHolder.tvYears.setOnClickListener(viewHolder.clickListener);

            viewHolder.tvMonths = convertView.findViewById(R.id.tvMonths);
            viewHolder.tvMonths.setOnClickListener(viewHolder.clickListener);

            viewHolder.tvDays = convertView.findViewById(R.id.tvDays);
            viewHolder.tvDays.setOnClickListener(viewHolder.clickListener);


            result=convertView;

            convertView.setTag(viewHolder);
        }
        lastPosition = position;

        return convertView;
    }















    private static class ViewHolder {

        private TextView contactQuestionText;
        private TextView contactTvNumber;

        private TextView contactID;
        private TextView contactFirstName;
        private TextView contactFamilyName;

        private TextView contactGender;
        private TextView contactRelation;
        private Spinner relationsList;

        private EditText etPatientID ;
        private EditText etPatientName;
        private RadioGroup genderWidget;
        private Spinner spRelations;

        private DatePickerDialog picker;
        private Calendar calendar;

        private EditText etAgeYears;
        private EditText etAgeMonths;
        private EditText etAgeDays;
        private TextView tvYears;
        private TextView tvMonths;
        private TextView tvDays;
        private View.OnClickListener clickListener;


    }


}
