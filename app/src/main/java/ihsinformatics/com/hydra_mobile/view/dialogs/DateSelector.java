package ihsinformatics.com.hydra_mobile.view.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import androidx.appcompat.app.AppCompatActivity;
import ihsinformatics.com.hydra_mobile.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateSelector extends AppCompatActivity implements OnClickListener {

	/*public final static int DATE = 0;
	public final static int DATE_TIME = 4;
	public final static int TIME = 8;*/

    public static enum WIDGET_TYPE {
        DATE,
        DATE_TIME,
        TIME
    }

    public final static String DATE_TYPE = "content";
    private Button btnSubmit;
    private DatePicker datePicker;
    private NumberPicker hourPicker, minutePicker;
    private int yr;
    private String cYr;
    private final String[] dayInMonth = {"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31"};

    private final String[] month = {"01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12"};

    private WIDGET_TYPE dialogType;
    private String selectedTime;
    private LinearLayout llTimePicker;
    public static Date MAX_DATE;
    public static Date MIN_DATE;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_date_selector);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        // datePicker.getCalendarView().setVisibility(View.GONE); this is now configured in xml layout file
        try {
            datePicker.setMaxDate(MAX_DATE.getTime());
            datePicker.setMinDate(MIN_DATE.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSubmit.setOnClickListener(this);

        Intent i = getIntent();
        dialogType = WIDGET_TYPE.valueOf(i.getStringExtra(DATE_TYPE));
        llTimePicker = (LinearLayout) findViewById(R.id.llDateTime);
        if (dialogType == WIDGET_TYPE.DATE_TIME || dialogType == WIDGET_TYPE.TIME) {
            Date d = new Date();
            DateFormat df = new SimpleDateFormat("HH", Locale.US);

            llTimePicker.setVisibility(View.VISIBLE);
            hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
            hourPicker.setMaxValue(24);
            hourPicker.setMinValue(1);
            hourPicker.setValue(Integer.parseInt(df.format(d)));

            df = new SimpleDateFormat("mm", Locale.US);
            minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
            minutePicker.setMaxValue(59);
            minutePicker.setMinValue(0);
            minutePicker.setValue(Integer.parseInt(df.format(d)));
        }
        if (dialogType == WIDGET_TYPE.DATE) {
            llTimePicker.setVisibility(View.GONE);
        }
        if (dialogType == WIDGET_TYPE.TIME) {
            datePicker.setVisibility(View.GONE);
        }
		/*Date date = new Date();
		
		datePicker.setMaxDate(date.getTime());*/
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSubmit) {
            datePicker.clearFocus();
            Intent sendBack = new Intent();
            Calendar c = Calendar.getInstance();
            if (WIDGET_TYPE.DATE == dialogType) {
                c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            } else if (WIDGET_TYPE.DATE_TIME == dialogType) {
                c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), hourPicker.getValue(), minutePicker.getValue());
            } else if (WIDGET_TYPE.TIME == dialogType) {
                c.set(Calendar.HOUR_OF_DAY, hourPicker.getValue());
                c.set(Calendar.MINUTE, minutePicker.getValue());
            }
            sendBack.putExtra("value", c);
            sendBack.putExtra(DateSelector.DATE_TYPE, dialogType.toString());
            setResult(RESULT_OK, sendBack);
            finish();
        } else {
        }
    }


    @SuppressWarnings("unused")
    private String parseYear(int yr) {
        String y = Integer.toString(datePicker.getYear());
        y = y.substring(y.length() - 2, y.length());
        return y;
    }

}
