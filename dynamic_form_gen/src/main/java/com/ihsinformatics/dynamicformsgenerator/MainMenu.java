package com.ihsinformatics.dynamicformsgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.view.MenuItemCompat;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserCredentials;
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;
import com.ihsinformatics.dynamicformsgenerator.screens.Form;
import com.ihsinformatics.dynamicformsgenerator.screens.LoginActivity;
import com.ihsinformatics.dynamicformsgenerator.screens.SelectProgram;
import com.ihsinformatics.dynamicformsgenerator.screens.ToolbarActivity;
import com.ihsinformatics.dynamicformsgenerator.utils.Global;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences;
import com.ihsinformatics.dynamicformsgenerator.utils.GlobalPreferences.KEY;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends ToolbarActivity implements OnClickListener {
    private TextView btn1, btn2, btn3, btn4, btn5, btn6;
    private TextView tvNotification;
    private boolean logout;
    // private Button btnSync;
    private LinearLayout llMenuButtons, llPreOpDemographics, llPostOpDemographics, llScreenCallIn, llPostOpFollowUp, llSurgenEvaluation, llPatientCreation, llBeforeCircumcision, llAfterCircumcision, llAfterCircumcision2;
    private ScrollView svMain;
    private PopupMenu notificationMenu;
    private RelativeLayout actionNotification;
    private NotificationItemClickListener notificationItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Question> list = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manu1);
        addToolbar();
        notificationItemClickListener = new NotificationItemClickListener();
        logout = false;
        btn1 = (TextView) findViewById(R.id.button1);
        btn2 = (TextView) findViewById(R.id.button2);
        btn4 = (TextView) findViewById(R.id.button4);
        btn6 = (TextView) findViewById(R.id.button6);
        svMain = (ScrollView) findViewById(R.id.svMain);
        llPatientCreation = (LinearLayout) findViewById(R.id.llPatientCreation);
        llPreOpDemographics = (LinearLayout) findViewById(R.id.llPreOpDemographics);
        llPostOpDemographics = (LinearLayout) findViewById(R.id.llPostOpDemographics);
        llScreenCallIn = (LinearLayout) findViewById(R.id.llScreenCallIn);
        llPostOpFollowUp = (LinearLayout) findViewById(R.id.llPostOpFollowUp);
        llSurgenEvaluation = (LinearLayout) findViewById(R.id.llSurgenEvaluation);
        llBeforeCircumcision = (LinearLayout) findViewById(R.id.llBeforeCircumcision);
        llAfterCircumcision = (LinearLayout) findViewById(R.id.llAfterCircumcision);
        llAfterCircumcision2 = (LinearLayout) findViewById(R.id.llAfterCircumcision2);
        llPatientCreation.setOnClickListener(this);
        llPreOpDemographics.setOnClickListener(this);
        llPostOpDemographics.setOnClickListener(this);
        llScreenCallIn.setOnClickListener(this);
        llPostOpFollowUp.setOnClickListener(this);
        llSurgenEvaluation.setOnClickListener(this);
        llBeforeCircumcision.setOnClickListener(this);
        llAfterCircumcision.setOnClickListener(this);
        llAfterCircumcision2.setOnClickListener(this);
        llMenuButtons = (LinearLayout) findViewById(R.id.llMenuButtons);

		/*if(llMenuButtons.getViewTreeObserver().isAlive()) {
            llMenuButtons.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
		        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
		        public void onGlobalLayout() {
				llPreOpDemographics.setLayoutParams(new LinearLayout.LayoutParams(llPreOpDemographics.getMeasuredWidth(), llPreOpDemographics.getMeasuredWidth() - 40));
				llPostOpDemographics.setLayoutParams(new LinearLayout.LayoutParams(llPostOpDemographics.getMeasuredWidth(), llPostOpDemographics.getMeasuredWidth() - 40));
				llScreenCallIn.setLayoutParams(new LinearLayout.LayoutParams(llScreenCallIn.getMeasuredWidth(), llScreenCallIn.getMeasuredWidth() - 40));
                llPostOpFollowUp.setLayoutParams(new LinearLayout.LayoutParams(llPostOpFollowUp.getMeasuredWidth(), llPostOpFollowUp.getMeasuredWidth() - 40));

                llMenuButtons.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		        }
		    });
		}*/
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent("com.ihsinformatics.DATA_UPLOAD_ATTEMPT");
        sendBroadcast(intent);
        if (Global.USERNAME == null || Global.PASSWORD == null) {
            final String username = GlobalPreferences.getinstance(this).findPrferenceValue(KEY.USERNAME, null);
            final String password = GlobalPreferences.getinstance(this).findPrferenceValue(KEY.PASSWORD, null);
            if (username != null && password != null) {
                Global.USERNAME = username;
                Global.PASSWORD = password;
            } else {
                startActivity(new Intent(MainMenu.this, LoginActivity.class));
                finish();
            }
        }
//        updateUserInfoBar();
        updateNotification();
        super.onResume();
    }

    private void updateUserInfoBar() {
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        ImageView ivGender = (ImageView) findViewById(R.id.ivGender);
        tvUsername.setText(Global.USERNAME);
        UserCredentials user = DataAccess.getInstance().getUserCredentials(this, Global.USERNAME);
        if (user.getFullName() != null || user.getGender() != null) {
            tvUsername.setText(user.getFullName());
            if (user.getGender().toLowerCase().startsWith("m")) {
                ivGender.setImageDrawable(getDrawable(R.drawable.male_icon));
            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.female_icon));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        // Line below is mandatory to add if you are using AppComppatActivity
        MenuItemCompat.setActionView(item, R.layout.action_notification);
        actionNotification = (RelativeLayout) MenuItemCompat.getActionView(item);
        tvNotification = (TextView) actionNotification.findViewById(R.id.tvCount);
        initNotificationsMenu();
        updateNotification();
        return super.onCreateOptionsMenu(menu);
    }

    private void initNotificationsMenu() {
        notificationMenu = new PopupMenu(this, tvNotification);
        // notificationMenu.getMenuInflater().inflate(R.menu.login, notificationMenu.getMenu());
        actionNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationMenu.show();
            }
        });
    }

    private void updateNotification() {
        if (tvNotification != null) {
            int sum = 0;
            DataAccess dataAccess = DataAccess.getInstance();
            notificationMenu.getMenu().clear();
            String[] formNames = DataProvider.getInstance(this).getEncounterNames();
            for (int i = 0; i < formNames.length; i++) {
                String formName = formNames[i];
                MenuItem item = notificationMenu.getMenu().add(0, i, i, formName);
                item.setOnMenuItemClickListener(notificationItemClickListener);
                sum += dataAccess.getAllForms(this, formName).size();
            }
            if (sum > 0) {
                tvNotification.setVisibility(View.VISIBLE);
                tvNotification.setText(String.format("%02d", sum));
            } else {
                tvNotification.setVisibility(View.GONE);
                tvNotification.setText("00");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            logout = true;
            GlobalPreferences.getinstance(this).addOrUpdatePreference(KEY.USERNAME, null);
            GlobalPreferences.getinstance(this).addOrUpdatePreference(KEY.PASSWORD, null);
            startActivity(new Intent(MainMenu.this, LoginActivity.class));
            finish();
        } else if (id == R.id.action_language) {
            startActivity(new Intent(MainMenu.this, LanguageSelector.class));
        } else if (id == R.id.action_programme) {
            startActivityForResult(new Intent(MainMenu.this, SelectProgram.class), 0);
        } else if (id == R.id.action_notifications) {
            /*Intent intent = new Intent(MainMenu.this, SavedFormsDisplayActivity.class);
            startActivity(intent);*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String encounterName = null;
        if (id == R.id.llPatientCreation) {
            encounterName = ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llPreOpDemographics) {
            encounterName = ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llPostOpDemographics) {
            encounterName = ParamNames.ENCOUNTER_TYPE_POST_OP_DEMOGRAPHICS;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llScreenCallIn) {
            encounterName = ParamNames.ENCOUNTER_TYPE_SCREENING_CALL_IN;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llPostOpFollowUp) {
            encounterName = ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llSurgenEvaluation) {
            encounterName = ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llBeforeCircumcision) {
            encounterName = ParamNames.ENCOUNTER_TYPE_PRE_CIRCUMCISION;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llAfterCircumcision) {
            encounterName = ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        } else if (id == R.id.llAfterCircumcision2) {
            encounterName = ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION2;
            startActivity(new Intent(this, Form.class));
            Form.setENCOUNTER_NAME(encounterName);
        }
    }

    private class NotificationItemClickListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            /*Intent intent = new Intent(MainMenu.this, SavedFormsDisplayActivity.class);
            intent.putExtra(ParamNames.ENCOUNER_NAME, item.getOrder());
            startActivity(intent);*/
            return false;
        }
    }

    ;
}
