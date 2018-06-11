package slugquest.slugquest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.view.Gravity;
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

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.Glide;

import java.util.Random;

import static android.content.ContentValues.TAG;


public class Map_Activity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, LocationListener {

    private GoogleMap mMap;
    private int LOCATION_PERMISSION = 1;

    private Event activeEvent = null;
    private Circle activeCircle = null;
    private Circle randomizedCircle = null;
    private LatLng randomizedCenter = null;
    private LatLng activeLatLng = null;

    private ImageView image;
    private ImageView compassImage;
    private ImageView scrollImage;
    private ImageView locationImage;

    int scrollInt = 0;
    float azimuth = 0;
    float pitch = 0;
    float roll = 0;

    //Compass Vars
    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

    private static final float VALUE_DRIFT = 0.05f;
    public SoundPoolPlayer sound;

    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // sound player
        sound = new SoundPoolPlayer(this);

        //Foundation
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Comment out according view depending on what you're testing
        //setContentView(R.layout.activity_map_developer);
        setContentView(R.layout.activity_map);


        // compass code
        compassImage = (ImageView)findViewById(R.id.compassImage);

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


        locationImage = findViewById(R.id.locationImage);
        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compassCompare();
                if (checkInsideEvent() == true) {
                    sound.playShortResource(R.raw.levelcomplete);
                    nextEvent();
                }
            }
        });


        // IMPORTANT loads active event's image
        updateImage();

        //Scroll Image
        scrollImage = (ImageView) findViewById(R.id.scrollImageButton);
        scrollImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollInt == 1){
                    image.setAlpha((float)1.0);
                    Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
                    image.startAnimation(animSlideDown);

                    animSlideDown.setAnimationListener(new Animation.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animation arg0) {
                        }
                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }
                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            image.setAlpha((float)0.0);
                        }
                    });


                    scrollInt = 0;

                }else{

                    image.setAlpha((float)1.0);
                    Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                    image.startAnimation(animSlideUp);

                    animSlideUp.setAnimationListener(new Animation.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animation arg0) {
                        }
                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }
                        @Override
                        public void onAnimationEnd(Animation arg0) {
                        }
                    });

                    //image.setAlpha((float)0.0);
                    scrollInt = 1;
                }
                sound.playShortResource(R.raw.powerup);


            }
        });

        requestLocationPermission();
        registerLocationUpdates();
    }

    protected void onResume() {
        super.onResume();


        mSensorManager.registerListener(this,mSensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mSensorMagnetometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        activeEvent = ((Globals) this.getApplication()).getEvent();
        plotEvent();

        LatLng Ucsc = new LatLng(36.9915, -122.0583);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ucsc));

        // Location Services -> need to ask permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Toast.makeText(Map_Activity.this, "Style parsing failed.", Toast.LENGTH_SHORT).show();

            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(Map_Activity.this, "Can't find style. Error: ", Toast.LENGTH_SHORT).show();
        }

        // Constrain the camera target to the general area
        LatLngBounds GAMEAREA = new LatLngBounds(
                new LatLng(36.965118, -122.078809), new LatLng(37.026231, -122.030529));
        mMap.setLatLngBoundsForCameraTarget(GAMEAREA);

        // restrict zoom
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
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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

        float azimuthLoc = (float)Math.toDegrees(orientationValues[0]);
        azimuthLoc = (azimuthLoc + 360)%360;

        Animation anim = new RotateAnimation(-azimuth, -azimuthLoc,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        azimuth = azimuthLoc;

        anim.setDuration(500);
        anim.setRepeatCount(0);
        anim.setFillAfter(true);

        compassImage.startAnimation(anim);

        //azimuth = orientationValues[0];
        pitch = orientationValues[1];
        roll = orientationValues[2];
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

    String getImageName(){
         return   ((Globals) this.getApplication()).getImage();
    }

    void updateImage(){
        //Image allocation
        image = (ImageView)findViewById(R.id.imageScroll);
        String imageName = getImageName();

        Glide.with(this)  // Activity or Fragment
                .load(getResources().getIdentifier(imageName, "drawable", getPackageName()))
                .into(image);
    }

    boolean checkInsideEvent() {

        //location setup
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //Redundant permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Map_Activity.this, "location permission failed.", Toast.LENGTH_SHORT).show();
            return false;
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
        String message;
        if (check != true) {
            message = "You are not at " + activeEvent.name ;
            sound.playShortResource(R.raw.negativefeedback);
        }else{
            message = "Congratulations you found " + activeEvent.name + "!" ;
        }

        Toast toast= Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        if(compassCompare()){
            return check;
        }else{
            return false;
        }
    }



    void checkInsideCircle() {

        //location setup
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //Redundant permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Map_Activity.this, "location permission failed.", Toast.LENGTH_SHORT).show();
        }

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double circleLong = randomizedCircle.getCenter().longitude;
        double circleLat = randomizedCircle.getCenter().latitude;

        //Checks distance from player location to center of the circle
        double inRadius =  calculateDistance(circleLong, circleLat, longitude, latitude);

        //See's if the player is in the current radius of the circle
        boolean check = inRadius < randomizedCircle.getRadius();
        double currentRadius = randomizedCircle.getRadius();
        if (check == true && currentRadius > 40) {
//            Toast.makeText(Map_Activity.this, "in circle", Toast.LENGTH_SHORT).show();
            currentRadius -= 20;
            randomizedCenter = generateRandomLatLngWithinArea(activeLatLng, currentRadius-20);
            randomizedCircle.setCenter(randomizedCenter);
            randomizedCircle.setRadius(currentRadius);
        }

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
        activeLatLng = new LatLng(activeEvent.xCoordinate,activeEvent.yCoordinate);

        // puts point on map for actual locations
        mMap.addMarker(new MarkerOptions().position(activeLatLng).title(activeEvent.name));

        //Global Circle
        activeCircle = mMap.addCircle(new CircleOptions()
                .center(activeLatLng)
                .radius(20)
                .fillColor(0xFF0000FF)
                .visible(false)
        );

        randomizedCenter = generateRandomLatLngWithinArea(activeLatLng, 80);
        randomizedCircle = mMap.addCircle(new CircleOptions()
                .center(randomizedCenter)
                .radius(100)
                .fillColor(0x220000FF));
    }

    //Clears old events, updates global active event, plots event with above function
    void nextEvent(){
        //checkInsideEvent();
        mMap.clear();
        ((Globals) this.getApplication()).updateEvent();
        activeEvent = ((Globals) this.getApplication()).getEvent();
        plotEvent();
        updateImage();
    }

    void viewEvent(){
        Event activeEvent = ((Globals) this.getApplication()).getEvent();
        String activeEventName = activeEvent.name;
        String activeEventImage = activeEvent.imageName;
        Intent i = new Intent(Map_Activity.this, EventViewer.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("eventName", activeEventName);
        i.putExtra("eventImageName", activeEventImage);
        startActivity(i);
    }

    void registerLocationUpdates() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    void compassDirection(){
        Toast.makeText(Map_Activity.this, "Azimuth : " + azimuth + "\nPitch: " + pitch + "\nRoll : " + roll, Toast.LENGTH_SHORT).show();
    }

    boolean compassCompare(){

        // Variables for degrees
        float range = 200;
        float activeAngle = ((Globals) this.getApplication()).getCompassAngle();

        float angleDiff = (azimuth - activeAngle + 180 + 360) % 360 - 180;

        Toast.makeText(Map_Activity.this, "Azimuth: " + azimuth + "\nActive Angle: " + activeAngle + "\nAngleDiff: " + angleDiff, Toast.LENGTH_SHORT).show();

        if(angleDiff < range){
            return true;
        }else{
            return false;
        }

        //  return true;
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

    @Override
    public void onLocationChanged(Location location) {
        checkInsideCircle();
        Log.v(TAG, "In Map Activity");
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
}