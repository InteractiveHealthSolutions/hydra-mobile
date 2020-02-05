package com.ihsinformatics.dynamicformsgenerator.views.widgets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider;
import com.ihsinformatics.dynamicformsgenerator.data.Translator.LANGUAGE;
import com.ihsinformatics.dynamicformsgenerator.data.datatypes.GPSCoordinate;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question;
import com.ihsinformatics.dynamicformsgenerator.utils.GPS;
import com.ihsinformatics.dynamicformsgenerator.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class GPSWidget extends InputWidget implements ActivityCompat.OnRequestPermissionsResultCallback,OnClickListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 111;

    // // -------- GPS_PARAM related variables-------\\\\

    GoogleApiClient mGoogleApiClient;

    private GPSCoordinate coordinates;

    LocationRequest mLocationRequest;

    /**
     * Tracks the status of the location updates request. Value changes when the
     * Activity starts
     */
    protected Boolean mRequestingLocationUpdates;

    // Bool to track whether the app is already resolving an error of Google Api
    // Client
    private boolean mResolvingError = false;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    // // ---------End of GPS_PARAM variables ----------\\\\


    static final Integer GPS_SETTINGS = 0x7;

    private EditText etAnswer;

    public GPSWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options = DataProvider.getInstance(context).getOptions(question.getQuestionId());
        coordinates = new GPSCoordinate(0, 0);
        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
        }
        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(this);

        // Setting Google Play Service for GPS_PARAM

        mRequestingLocationUpdates = true;

        mGoogleApiClient = GPS.buildGoogleApiClient(this, this, context);
        mLocationRequest = GPS.createLocationRequest(GPS.REQ_INTERVAL, GPS.REQ_FASTEST_INTERVAL);

        startLocationUpdates();
        // //-------------End of GPS_PARAM settings -------------\\\\
    }

    @Override
    public boolean isValidInput(boolean isMendatory) {
        Validation validation = Validation.getInstance();
        return validation.validate(etAnswer, question.getValidationFunction(), isMendatory);
    }

    @Override
    public void setOptionsOrHint(Option... data) {
        if (data.length > 0) {
            etAnswer.setHint(data[0].getText());
        }
    }

    @Override
    public JSONObject getAnswer() throws JSONException {
        JSONObject param = new JSONObject();
        if (isValidInput(question.isMandatory())) {
            dismissMessage();
            param.put(question.getParamName(), etAnswer.getText().toString());
        } else {
            activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, LANGUAGE language) {
        etAnswer.setText(answer);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onFocusGained() {
        etAnswer.performClick();
    }

    @Override
    public void onClick(View v) {
        // CODE TO REFRESH GPS_PARAM, NOT APPLICABLE IN THIS SCENARIO

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(activity, GPS.REQUEST_RESOLVE_ERROR);
            } catch (SendIntentException e) {
                // There was an error with the resolution intent. Try again.

                checkGPS();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            // Toast.makeText(this, "Error: " + result.getErrorCode(),
            // Toast.LENGTH_LONG).show();

            GPS.showErrorDialog(context, result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void checkGPS() {
        if (GPS.isGPSEnabled(context)) {
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(context, "Please enable GPS_PARAM", Toast.LENGTH_LONG).show();
            closeForm();
        }

    }


    @Override
    public void onConnected(Bundle arg0) {
        // This method is called when the Google API Client is connected

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
          //  return;
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mCurrentLocation != null) {
                // if lastlocation was fetched more than 30 seconds before, it is
                // better to start listen for new update
                Date currentDate = new Date();

                if (currentDate.getTime() - mCurrentLocation.getTime() < GPS.SECONDS_30) {
                    updateGeoVariables(mCurrentLocation);
             //       return;
                }
            }

            // If the user presses the Start Updates button before GoogleApiClient
            // connects, we set
            // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()).
            // Here, we check
            // the value of mRequestingLocationUpdates and if it is true, we start
            // location updates

            startLocationUpdates();
        }


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateGeoVariables(mCurrentLocation);
    }

    protected void startLocationUpdates() {
        if (GPS.isGPSEnabled(context)) {

            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected() == false) {
                    mGoogleApiClient.connect();
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,GPS_SETTINGS);
                            }
                            else
                            {
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                            }


                            //  return;
                    } else {
                        // Write you code here if permission already given.
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    }
                }
            }
        } else {
            Toast.makeText(context, "Please enable GPS_PARAM", Toast.LENGTH_LONG).show();
            closeForm();
        }

    }

    /**
     * Removes location updates from the FusedLocationApi. But first it checks
     * whether GoogleClient is already connected or not
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity
        // is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a
        // LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).

        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }
    }

    private void updateGeoVariables(Location location) {
        coordinates.setLatitude(location.getLatitude());
        coordinates.setLongitude(location.getLongitude());

        String coordinate = coordinates.getLatitude() + "," + coordinates.getLongitude();
        etAnswer.setText(coordinate);
    }

    @Override
    public void destroy() {
        stopLocationUpdates();

    }

    @Override
    public String getValue() {
        return etAnswer.getText().toString();
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(ActivityCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    checkGPS();
                    break;

            }

            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
