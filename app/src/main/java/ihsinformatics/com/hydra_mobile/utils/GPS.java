package ihsinformatics.com.hydra_mobile.utils;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Note: See <b>"BaseActivity"</b> for complete implementation of GPS
 * <p/>
 * Target class should implement "ConnectionCallbacks" and
 * "OnConnectionFailedListener" interfaces
 * <p/>
 * Steps for using this class:
 * <p/>
 * 1) Get its instance on "onCreate()" method of Activity*
 * <p/>
 * 2) Populate your location variables by using
 * "LocationServices.FusedLocationApi.getLastLocation()" * on
 * "onConnected(Bundle arg0)" method of "ConnectionCallbacks" interface
 * <p/>
 * 3) GoogleApiClient should be disconnected when Activity is no longer visible
 * like: "onStop()" method
 * <p/>
 * 4) Location variables should be populated again on "onStart()" method after
 * connecting GoogleApiClient if not connected.
 * <p/>
 */
public class GPS {
	/*
	 * GPS related variables
	 */
	// private static GoogleApiClient mGoogleApiClient;

	public final static Integer SECONDS_10 = 10000;

	public final static Integer SECONDS_15 = 15000;

	public final static Integer SECONDS_30 = 30000;

	public final static Integer MINUTE_1 = 60000;
	public final static Integer MINUTE_2 = 120000;
	public final static Integer MINUTE_3 = 180000;
	public final static Integer MINUTE_4 = 240000;

	public static final int REQ_INTERVAL = 5000;
	public static final int REQ_FASTEST_INTERVAL = 3000;

	// Request code to use when launching the resolution activity
	public static final int REQUEST_RESOLVE_ERROR = 1001;
	// Unique tag for the error dialog fragment
	public static final String DIALOG_ERROR = "dialog_error";

	private static GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

	/**
	 * Create an instance of Google API Client using GoogleApiClient.Builder and
	 * register some events also
	 * 
	 * @return
	 */
	public synchronized static GoogleApiClient buildGoogleApiClient(ConnectionCallbacks connectionCallbacks,
                                                                    OnConnectionFailedListener onConnectionFailedListener, Context context) {
		return new GoogleApiClient.Builder(context).addConnectionCallbacks(connectionCallbacks)
				.addOnConnectionFailedListener(onConnectionFailedListener).addApi(LocationServices.API).build();
	}

	/**
	 * Requests location updates from the FusedLocationApi.
	 */
	// protected static void startLocationUpdates(GoogleApiClient
	// mGoogleApiClient, LocationRequest mLocationRequest, LocationListener
	// locationListener,
	// Context context)
	// {
	// checkGPS(context);
	//
	// LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
	// mLocationRequest, locationListener);
	// }

	// public static LocationRequest createLocationRequest(int reqQ_interval,
	// int REQ_FASTEST_INTERVAL, int locationReq)
	// {
	// LocationRequest _mLocationRequest = new LocationRequest();
	// _mLocationRequest.setInterval(reqQ_interval);
	// _mLocationRequest.setFastestInterval(REQ_FASTEST_INTERVAL);
	// _mLocationRequest.setPriority(locationReq);
	//
	// return _mLocationRequest;
	// }

	/**
	 * Removes location updates from the FusedLocationApi.
	 */
	// public static void stopLocationUpdates(GoogleApiClient mGoogleApiClient,
	// LocationListener locationListener)
	// {
	// // It is a good practice to remove location requests when the activity is
	// in a paused or
	// // stopped state. Doing so helps battery performance and is especially
	// // recommended in applications that request frequent location updates.
	//
	// // The final argument to {@code requestLocationUpdates()} is a
	// LocationListener
	// //
	// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
	// LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
	// locationListener);
	// }

	/**
	 * Checks if gps in enabled
	 */

	public static Boolean isGPSEnabled(Context context) {
		LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean checkPlayServices(Context context) {
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);

		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode))

			{
				googleApiAvailability.getErrorDialog((Activity) context, resultCode, 9000).show();
			} else {
				Toast.makeText(context, "This device doesn't have \"Google Play services\", App will not work normally", Toast.LENGTH_LONG).show();
			}

			return false;
		}
		
		return true;
	}

	/**
	 * Sets up the location request. Android has two location request settings:
	 * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These
	 * settings control the accuracy of the current location. This sample uses
	 * ACCESS_FINE_LOCATION, as defined in the AndroidManifest.xml.
	 * <p/>
	 * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast
	 * update interval (5 seconds), the Fused Location Provider API returns
	 * location updates that are accurate to within a few feet.
	 * <p/>
	 * These settings are appropriate for mapping applications that show
	 * real-time location updates.
	 */
	public static LocationRequest createLocationRequest(int REQ_INTERVAL, int REQ_FASTEST_INTERVAL) {
		LocationRequest _mLocationRequest = new LocationRequest();
		_mLocationRequest.setInterval(REQ_INTERVAL);
		_mLocationRequest.setFastestInterval(REQ_FASTEST_INTERVAL);
		_mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		return _mLocationRequest;
	}

	public static void showErrorDialog(Context context, int resultCode) {
		/*if (googleApiAvailability.isUserResolvableError(resultCode)) {*/
			googleApiAvailability.getErrorDialog((Activity) context, resultCode, GPS.REQUEST_RESOLVE_ERROR).show();
		/*} else {
			Toast.makeText(context, "This device doesn't have \"Google Play services\", App will not work normally", Toast.LENGTH_LONG).show();
		}*/
	}

}
