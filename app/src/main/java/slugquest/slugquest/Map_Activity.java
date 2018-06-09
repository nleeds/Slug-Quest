package slugquest.slugquest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;
import android.location.LocationListener;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


import java.util.Calendar;
import java.util.Random;


public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private int LOCATION_PERMISSION = 1;
    private Button eventButton;
    private Button locationCheckButton;
    private Button compassCheckButton;

    private Event activeEvent = null;
    private Circle activeCircle = null;

    float azimuth = 0;
    float pitch = 0;
    float roll = 0;
    /*
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    */

    //Compass variables
    //private float[] mGravity = new float[3];
    //private float[] mGeomagnetic = new float[3];
//    private float azimuth = 0f;
//    private float currentAzimuth = 0f;
//    private SensorManager mSensorManager;
//    private String comp = "Nurt";

    //TRIAL FUCKING 3
    private SensorManager mSensorManager;

    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

    private static final float VALUE_DRIFT = 0.05f;


    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Foundation
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_map);

        // compass code
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        mSensorAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        mSensorMagnetometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);

        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //mPointer = (ImageView) findViewById(R.id.pointer);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Button Setup : Event, location, and compass buttons. Combine compass and location later
        eventButton = findViewById(R.id.eventButton);
        eventButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){    nextEvent();    }
        });

        locationCheckButton = findViewById(R.id.locationButton);
        locationCheckButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){    checkInsideEvent();    }
        });

        compassCheckButton = findViewById(R.id.compassButton);
        compassCheckButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){    compassDirection();   }
        });

        requestLocationPermission();
        registerLocationUpdates();
    }

    protected void onResume() {
        super.onResume();


        mSensorManager.registerListener(this,mSensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mSensorMagnetometer,SensorManager.SENSOR_DELAY_NORMAL);

//        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
//                SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_GAME);

        //mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        //mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        //mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Event Input  (String name, double xCoordinate, double yCoordinate, String image)
        //Event porterMeadowsEvent = new Event("Porter Meadows",36.994803, -122.067737,"sunset",1);
        activeEvent = ((Globals) this.getApplication()).getEvent();

        plotEvent();

        //Active LatLang
        //LatLng activeLatLng = new LatLng(activeEvent.xCoordinate,activeEvent.yCoordinate);

        // points of interest
        LatLng Ucsc = new LatLng(36.9915, -122.0583);
        LatLng BudaHut = new LatLng(37.006443, -122.059919);
        LatLng CatShrine = new LatLng(37.006699, -122.056143);
        LatLng McHenry = new LatLng(36.995587, -122.058374);
        LatLng MerrillGarden = new LatLng(36.998865, -122.051996);
        LatLng PorterMeadows = new LatLng(36.994803, -122.067737);
        LatLng Arboretum = new LatLng(36.983092, -122.060712);
        LatLng KoiPond = new LatLng(37.000288, -122.048409);
        LatLng RockGarden = new LatLng(37.001445, -122.049664);
        LatLng QuarryPlaza = new LatLng(36.997631, -122.055762);

        // Global Active Test Marker
        //mMap.addMarker(new MarkerOptions().position(activeLatLng).title("Global Test"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ucsc));

        // Location Services -> need to ask permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //   here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);



        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Toast.makeText(Map_Activity.this, "Style parsing failed.", Toast.LENGTH_SHORT).show();

            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(Map_Activity.this, "Can't find style. Error: ", Toast.LENGTH_SHORT).show();
        }


        //example of geofence area
        Circle QUARRYPLAZA = mMap.addCircle(new CircleOptions()
                .center(QuarryPlaza)
                .radius(150)
                .fillColor(0x220000FF));


        //test to check if geofences work.

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng myLocation = new LatLng(latitude, longitude);
        boolean inside = checkInside(QUARRYPLAZA, longitude, latitude);
        String isInside;
        if (inside == true) {
            isInside = "you are in QUARRYPLAZA";
        } else {
            isInside = "you are not in QUARRYPLAZA";
        }
        mMap.addMarker(new MarkerOptions().position(myLocation).title((isInside)));
        //end test


        checkInsideEvent();


        // Constrain the camera target to the general area
        LatLngBounds GAMEAREA = new LatLngBounds(
                new LatLng(36.965118, -122.078809), new LatLng(37.026231, -122.030529));

        mMap.setLatLngBoundsForCameraTarget(GAMEAREA);
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(30.0f);
    }

    //Permission request
    public void permissionRequest() {
        if (ContextCompat.checkSelfPermission(Map_Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Map_Activity.this, "Already granted", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This is the whole app, come on")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Map_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                        }
                    })
                    .setNegativeButton("Fuck you", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //NEW TEST
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        azimuth = orientationValues[0];
        pitch = orientationValues[1];
        roll = orientationValues[2];


//        final float alpha = 0.97f;
//        synchronized (this){
//
//            Accelerometer stuff
//            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//                mGravity[0] = alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
//                mGravity[1] = alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
//                mGravity[2] = alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
//
//            }
//
//             switch to Geo
//            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
//                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
//                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
//                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
//
//            }
//
//            float R[] = new float[9];
//            float I[] = new float[9];
//            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
//
//            if(success){
//
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R,orientation);
//                azimuth = (float)Math.toDegrees(orientation[0]);
//                azimuth = (azimuth + 360)%360;
//                if (azimuth < 0) {
//                    azimuth += 360;
//
//            }
//
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    // check if point is inside geofence using the Haversine formula
    // https://stackoverflow.com/questions/30719757/is-there-any-api-for-calculating-geofence-breach-other-than-android-apis
    boolean checkInside(Circle circle, double longitude, double latitude) {
        return calculateDistance(
                circle.getCenter().longitude, circle.getCenter().latitude, longitude, latitude
        ) < 2;
    }


    void checkInsideEvent() {

        //location setup
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //Redundant permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double circleLong = activeCircle.getCenter().longitude;
        double circleLat = activeCircle.getCenter().latitude;

        //Checks distance from player location to center of the circle
        double inRadius =  calculateDistance(circleLong, circleLat, longitude, latitude);

        //See's if the player is in the current radius of the circle
        boolean check = inRadius < activeCircle.getRadius();

        //Placeholder toasts
        String isInside;
        if (check == true) {
            isInside = "you are in " + activeEvent.name ;
        } else {
            isInside = "you are not in " + activeEvent.name ;
        }
        Toast.makeText(Map_Activity.this, isInside, Toast.LENGTH_SHORT).show();
    }


    // helper function for geofence
    double calculateDistance(
            double longitude1, double latitude1,
            double longitude2, double latitude2) {
        double c =
                Math.sin(Math.toRadians(latitude1)) *
                        Math.sin(Math.toRadians(latitude2)) +
                        Math.cos(Math.toRadians(latitude1)) *
                                Math.cos(Math.toRadians(latitude2)) *
                                Math.cos(Math.toRadians(longitude2) -
                                        Math.toRadians(longitude1));
        c = c > 0 ? Math.min(1, c) : Math.max(-1, c);
        return 3959 * 1.609 * 1000 * Math.acos(c);
    }


   // Boils down the google maps plotting and circles to a function
    void plotEvent(){
        // x and y coordinates
        LatLng activeLatLng = new LatLng(activeEvent.xCoordinate,activeEvent.yCoordinate);

        // puts point on map for actual locations
        mMap.addMarker(new MarkerOptions().position(activeLatLng).title(activeEvent.name));

        //Global Circle
        activeCircle = mMap.addCircle(new CircleOptions()
                .center(activeLatLng)
                .radius(150)
                .fillColor(0x220000FF));
    }

    //Clears old events, updates global active event, plots event with above function
    void nextEvent(){
        //checkInsideEvent();
        mMap.clear();
        ((Globals) this.getApplication()).updateEvent();
        activeEvent = ((Globals) this.getApplication()).getEvent();
        plotEvent();
    }

    void registerLocationUpdates() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    void compassDirection(){

        
        Toast.makeText(Map_Activity.this, "Azimuth : " + azimuth + "\nPitch: " + pitch + "\nRoll : " + roll, Toast.LENGTH_SHORT).show();
    }


    // adapted from https://stackoverflow.com/questions/33976732/generate-random-latlng-given-device-location-and-radius
    // takes as input a center and radius
    // outputs a random point within circle
    // can be used to create random circles containing events
    LatLng generateRandomLatLngWithinArea(LatLng eventPoint, double radius){
        double radiusInDegrees = radius / 111000f;
        double lat = eventPoint.latitude;
        double lon = eventPoint.longitude;
        Random random = new Random();

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(lon);

        double foundLatitude = new_x + lat;
        double foundLongitude = y + lon;
        LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
        return randomLatLng;
    }




}