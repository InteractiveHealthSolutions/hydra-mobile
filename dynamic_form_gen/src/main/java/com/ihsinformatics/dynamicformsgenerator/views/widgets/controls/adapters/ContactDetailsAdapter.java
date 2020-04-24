package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.ihsinformatics.dynamicformsgenerator.PatientInfoFetcher;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.Translator;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.ContactTracingConfiguration;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.ContactDetails;
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.Logger;
import com.ihsinformatics.dynamicformsgenerator.views.widgets.QRReaderWidget;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ihsinformatics.dynamicformsgenerator.Utils.showMessageOKCancel;

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

    public class ContactViewHolder extends RecyclerView.ViewHolder  {

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

        private EditText  etPatientFamilyName;
        private RadioGroup genderWidget;
        private Spinner spRelations;

        private DatePickerDialog picker;
        private Calendar calendar;

        private EditText etAgeYears;
        private EditText etAgeMonths;
        private EditText etAgeDays;
        private EditText etDOB;

        private LinearLayout ageWidget;

        protected LinearLayout QRCodeReader;


        private View.OnClickListener clickListener;

        private View.OnClickListener QRListener;

        private boolean deadLock = false;

        private Period period;

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

            calendar = Calendar.getInstance();

            Date periodToday =  calendar.getTime();
            calendar.add(Calendar.YEAR, -110);
            Date minDate = calendar.getTime();

            period = new Period(new LocalDate(minDate), new LocalDate(periodToday));

            calendar = Calendar.getInstance();
            QRCodeReader = itemView.findViewById(R.id.qrCodeReader);

            if (!configuration.isCreatePatient()) {
                contactID.setVisibility(View.GONE);
                etPatientID.setVisibility(View.GONE);
                QRCodeReader.setVisibility(View.GONE);
                etPatientID.setText("-");
            }

            etPatientName = (EditText) itemView.findViewById(R.id.etPatientName);
            etPatientFamilyName = (EditText) itemView.findViewById(R.id.etPatientFamilyName);
            genderWidget = (RadioGroup) itemView.findViewById(R.id.genderWidget);
            spRelations = (Spinner) itemView.findViewById(R.id.spRelations);





            etAgeYears = itemView.findViewById(R.id.etAgeYears);
            etAgeMonths = itemView.findViewById(R.id.etAgeMonths);
            etAgeDays = itemView.findViewById(R.id.etAgeDays);
            etDOB = itemView.findViewById(R.id.etPatientDOB);




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
                                    etDOB.setText(dateString);
                                    Period p = null;
                                    try {
                                        p = new Period(new LocalDate(Global.DATE_TIME_FORMAT.parse(dateString)), new LocalDate(), PeriodType.yearMonthDayTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(null!=p) {
                                        etAgeYears.setText(p.getYears() + "");
                                        etAgeMonths.setText(p.getMonths() + "");
                                        etAgeDays.setText(p.getDays() + "");
                                    }else
                                    {
                                        etAgeYears.setText(year + "");
                                        etAgeMonths.setText(monthOfYear + "");
                                        etAgeDays.setText(dayOfMonth + "");
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

            TextWatcher textWatcher = new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!callTextChanged) {
                        callTextChanged = true;
                        return;
                    }
                    if (!deadLock && !s.toString().equals("")) {

                        try {

                            if(etAgeYears.getText().toString().equals(""))
                            {
                                etAgeYears.setText("0");
                            }

                            if(etAgeDays.getText().toString().equals(""))
                            {
                                etAgeDays.setText("0");
                            }

                            if(etAgeMonths.getText().toString().equals(""))
                            {
                                etAgeMonths.setText("0");
                            }

                            int ageYears = Integer.parseInt(etAgeYears.getText().toString());
                            int ageMonths = Integer.parseInt(etAgeMonths.getText().toString());
                            int ageDays = Integer.parseInt(etAgeDays.getText().toString());

                            if (ageYears > 110) {
                                etAgeYears.setError("Years should be <= 110");
                            } else {
                                if (ageYears >= 110) {

                                    setText(etAgeMonths, "0", false);
                                    setText(etAgeDays, "0", false);

                                } else {
                                    etAgeMonths.setEnabled(true);
                                    etAgeDays.setEnabled(true);


                                }
                            }
                            if (ageMonths > 11) {
                                etAgeMonths.setError("Months should be <= 11");
                            }
                            if (ageDays > 30) {
                                etAgeDays.setError("Days should be <= 30");
                            }


                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.YEAR, ageYears - (ageYears + ageYears));
                            calendar.add(Calendar.MONTH, ageMonths - (ageMonths + ageMonths));
                            calendar.add(Calendar.DAY_OF_MONTH, ageDays - (ageDays + ageDays));

                            if (ageYears <= period.getYears()) {

                                deadLock = true;
                                setAnswer(Global.DATE_TIME_FORMAT.format(calendar.getTime()), null, null);
                            } else {
//                    Toasty.info(context, "Age should be between 0 to " + period.getYears(), Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException e) {
                            //Toasty.error(context, "Invalid Age", Toast.LENGTH_LONG).show();
                            Logger.log(e);
                        }


                    } else if (deadLock) {
                        deadLock = false;
            /*
            etAgeYears.setText(s.toString());
            etAgeMonths.setText(s.toString());
            etAgeDays.setText(s.toString());*/
                    }
                }
            };

            ageWidget = itemView.findViewById(R.id.linearAgeWidgetLayout);
            ageWidget.setOnClickListener(clickListener);

            etDOB.setOnClickListener(clickListener);

            etAgeYears.addTextChangedListener(textWatcher);
            etAgeMonths.addTextChangedListener(textWatcher);
            etAgeDays.addTextChangedListener(textWatcher);


            QRListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        checkCameraPermission();
                    } else {
                        showQRCodeReaderDialog();
                    }
                }
            };


            QRCodeReader.setOnClickListener(QRListener);


        }




        protected void checkCameraPermission() {
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.CAMERA)) {
                    showMessageOKCancel(context, context.getResources().getString(R.string.qr_code_permission_request_message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context,
                                            new String[]{Manifest.permission.CAMERA},
                                            100);
                                }
                            });
                    return;
                }
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.CAMERA},
                        100);
                return;
            }
            showQRCodeReaderDialog();
        }

        public void showQRCodeReaderDialog() {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_qrcode);
            dialog.show();
            final QRCodeReaderView qrCodeReaderView;
            qrCodeReaderView = (QRCodeReaderView) dialog.findViewById(R.id.qrdecoderview);
            qrCodeReaderView.startCamera();
            qrCodeReaderView.setQRDecodingEnabled(true);
            qrCodeReaderView.setAutofocusInterval(2000L);
            qrCodeReaderView.setTorchEnabled(true);
            qrCodeReaderView.setFrontCamera();
            qrCodeReaderView.setBackCamera();
            qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
                @Override
                public void onQRCodeRead(String text, PointF[] points) {
                    etPatientID.setText(text);
                    etPatientID.setEnabled(true);
                    qrCodeReaderView.stopCamera();
                    dialog.dismiss();
                }
            });
        }







        private boolean callTextChanged = true;

        private void setText(EditText tv, String text, boolean callTextChanged) {
            this.callTextChanged = callTextChanged;
            tv.setText(text);
            tv.setEnabled(false);

        }


        public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
            try {
                if (!deadLock) {
                    deadLock = true;
                    Period p = new Period(new LocalDate(Global.DATE_TIME_FORMAT.parse(answer)), new LocalDate(), PeriodType.yearMonthDayTime());
                    int years = p.getYears();
                    int months = p.getMonths();
                    int days = p.getDays();
                    etAgeYears.setText(years + "");
                    etAgeMonths.setText(months + "");
                    etAgeDays.setText(days + "");
                } else {
                    deadLock = false;
                }

                etDOB.setText(answer.substring(0, 10));


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }


}
