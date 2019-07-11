package ihsinformatics.com.hydra_mobile.ui.widgets;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import ihsinformatics.com.hydra_mobile.R;
import ihsinformatics.com.hydra_mobile.data.core.options.Option;
import ihsinformatics.com.hydra_mobile.data.core.question.Question;
import ihsinformatics.com.hydra_mobile.utils.GPS;
import ihsinformatics.com.hydra_mobile.utils.GPSCoordinate;
import ihsinformatics.com.hydra_mobile.utils.Translator;
import ihsinformatics.com.hydra_mobile.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class GPSWidget extends InputWidget implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    // // -------- GPS related variables-------\\\\

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

    // // ---------End of GPS variables ----------\\\\

    private EditText etAnswer;

    public GPSWidget(Context context, Question question, int layoutId) {
        super(context, question, layoutId);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        options = null;//DataProvider.getInstance(context).getOptions(question.getQuestionId());
        coordinates = new GPSCoordinate(0, 0);
        if (options.size() > 0) {
            setOptionsOrHint(options.get(0));
        }
        etAnswer.setFocusable(false);
        etAnswer.setOnClickListener(this);

        // Setting Google Play Service for GPS

        mRequestingLocationUpdates = true;

        mGoogleApiClient = GPS.buildGoogleApiClient(this, this, context);
        mLocationRequest = GPS.createLocationRequest(GPS.REQ_INTERVAL, GPS.REQ_FASTEST_INTERVAL);

        startLocationUpdates();
        // //-------------End of GPS settings -------------\\\\
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
            //activity.addValidationError(getQuestionId(), "Invalid input");
        }
        return param;
    }

    @Override
    public void setAnswer(String answer, String uuid, Translator.LANGUAGE language) {
        etAnswer.setText(answer);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onFocusGained() {
        etAnswer.performClick();
    }

    @Override
    public void onClick(View v) {
        // CODE TO REFRESH GPS, NOT APPLICABLE IN THIS SCENARIO

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
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
            closeForm();
        }

    }


    @Override
    public void onConnected(Bundle arg0) {
        // This method is called when the Google API Client is connected

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            // if lastlocation was fetched more than 30 seconds before, it is
            // better to start listen for new update
            Date currentDate = new Date();

            if (currentDate.getTime() - mCurrentLocation.getTime() < GPS.SECONDS_30) {
                updateGeoVariables(mCurrentLocation);
                return;
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void startLocationUpdates() {
        if (GPS.isGPSEnabled(context)) {

            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected() == false) {
                    mGoogleApiClient.connect();
                } else {
                    // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
            }
        } else {
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
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
                //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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


}
