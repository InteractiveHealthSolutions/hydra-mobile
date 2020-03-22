package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTracingConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ContactDetailsAdapter extends RecyclerView.Adapter<ContactDetailsAdapter.ContactViewHolder> {

    private Context context;
    private List<ContactDetails> contactDetails;
    private List<String> relations;
    private ContactTracingConfiguration configuration;

    private HashMap<Integer, ContactDetails> data = new HashMap<Integer, ContactDetails>();

    public ContactDetailsAdapter(Context context, List<ContactDetails> contactDetails, List<String> relations, ContactTracingConfiguration configuration) {
        this.context = context;
        this.contactDetails = contactDetails;
        this.relations = relations;
        this.configuration = configuration;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_widget_contact_registry, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        holder.contactQuestionText.setText(contactDetails.get(position).getQuestionText());
        holder.contactTvNumber.setText(contactDetails.get(position).getQuestionNumber());

        holder.contactID.setText(contactDetails.get(position).getContactID());
        holder.contactFirstName.setText(contactDetails.get(position).getContactFirstName());
        holder.contactFamilyName.setText(contactDetails.get(position).getContactFamilyName());

        holder.contactGender.setText(contactDetails.get(position).getContactGender());
        holder.contactRelation.setText(contactDetails.get(position).getContactRelationships());

        holder.relationsList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, relations));

    }

    @Override
    public int getItemCount() {
        return contactDetails.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView contactQuestionText;
        private TextView contactTvNumber;

        private TextView contactID;
        private TextView contactFirstName;
        private TextView contactFamilyName;

        private TextView contactGender;
        private TextView contactRelation;
        private Spinner relationsList;

        private EditText etPatientID;
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

        private RelativeLayout ageWidget;


        private View.OnClickListener clickListener;


        public ContactViewHolder(View itemView) {
            super(itemView);

            contactQuestionText = (TextView) itemView.findViewById(R.id.tvQuestionContact);
            contactTvNumber = (TextView) itemView.findViewById(R.id.tvNumberContact);

            contactID = (TextView) itemView.findViewById(R.id.patientID);
            contactFirstName = (TextView) itemView.findViewById(R.id.patientName);
            contactFamilyName = (TextView) itemView.findViewById(R.id.patientFamilyName);
            contactGender = (TextView) itemView.findViewById(R.id.contactGender);
            contactRelation = (TextView) itemView.findViewById(R.id.contactRelation);
            relationsList = (Spinner) itemView.findViewById(R.id.spRelations);

            etPatientID = (EditText) itemView.findViewById(R.id.etPatientID);

            if (!configuration.isCreatePatient()) {
                contactID.setVisibility(View.GONE);
                etPatientID.setVisibility(View.GONE);
                etPatientID.setText("-");
            }

            etPatientName = (EditText) itemView.findViewById(R.id.etPatientName);
            genderWidget = (RadioGroup) itemView.findViewById(R.id.genderWidget);
            spRelations = (Spinner) itemView.findViewById(R.id.spRelations);


            calendar = Calendar.getInstance();


            etAgeYears = itemView.findViewById(R.id.etAgeYears);
            etAgeYears.setEnabled(false);
            etAgeMonths = itemView.findViewById(R.id.etAgeMonths);
            etAgeMonths.setEnabled(false);
            etAgeDays = itemView.findViewById(R.id.etAgeDays);
            etAgeDays.setEnabled(false);


            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    final int dayToday = cldr.get(Calendar.DAY_OF_MONTH);
                    final int monthToday = cldr.get(Calendar.MONTH);
                    final int yearToday = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(year, monthOfYear, dayOfMonth);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateString = dateFormat.format(calendar.getTime());

                                    Period p = null;
                                    try {
                                        p = new Period(new LocalDate(Global.DATE_TIME_FORMAT.parse(dateString)), new LocalDate(), PeriodType.yearMonthDayTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(null!=p) {
                                        etAgeYears.setText(p.getYears() + " ");
                                        etAgeMonths.setText(p.getMonths() + " ");
                                        etAgeDays.setText(p.getDays() + " ");
                                    }else
                                    {
                                        etAgeYears.setText(year + " ");
                                        etAgeMonths.setText(monthOfYear + " ");
                                        etAgeDays.setText(dayOfMonth + " ");
                                    }
                                    etAgeYears.setError(null);
                                    etAgeMonths.setError(null);
                                    etAgeDays.setError(null);


                                }
                            }, yearToday, monthToday, dayToday);
                    picker.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                    Date today = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(today);
                    c.add(Calendar.YEAR, -110); // Subtract 6 years
                    long minDate = c.getTime().getTime(); // Twice!
                    picker.getDatePicker().setMinDate(minDate);   // now min date is set to 110years ago max
                    picker.show();
                }
            };

            tvYears = itemView.findViewById(R.id.tvYears);
            tvYears.setOnClickListener(clickListener);

            tvMonths = itemView.findViewById(R.id.tvMonths);
            tvMonths.setOnClickListener(clickListener);

            tvDays = itemView.findViewById(R.id.tvDays);
            tvDays.setOnClickListener(clickListener);

            ageWidget = itemView.findViewById(R.id.ageWidgetRelativeLayout);
            ageWidget.setOnClickListener(clickListener);

        }

    }


}
