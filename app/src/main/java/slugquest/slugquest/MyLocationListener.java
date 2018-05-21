package slugquest.slugquest;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;



import static android.content.ContentValues.TAG;

class MyLocationListener implements android.location.LocationListener {
    public void onLocationChanged(Location location) {
        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());
        Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + latitude + ", lon=" + longitude);

    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.v(TAG, "Status changed: " + s);
        Log.v(TAG, "hello");
    }

    public void onProviderEnabled(String s) {
        Log.e(TAG, "PROVIDER DISABLED: " + s);
    }

    public void onProviderDisabled(String s) {
        Log.e(TAG, "PROVIDER DISABLED: " + s);
    }
}