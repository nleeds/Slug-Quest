package slugquest.slugquest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;
import android.location.LocationListener;

import java.util.Calendar;


public class Map_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int LOCATION_PERMISSION = 1;
    private Button eventButton;
    private Event activeEvent = null;
    private Circle activeCircle = null;

    protected LocationListener locationListener;
    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventButton = findViewById(R.id.eventButton);
        eventButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                nextEvent();
            }
        });
        requestLocationPermission();
        registerLocationUpdates();
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

        //markers
        //mMap.addMarker(new MarkerOptions().position(Ucsc).title("Marker in UCSC"));
        //mMap.addMarker(new MarkerOptions().position(BudaHut).title("Marker in BudaHut"));
        //mMap.addMarker(new MarkerOptions().position(CatShrine).title("Marker in CatShrine"));
        //mMap.addMarker(new MarkerOptions().position(McHenry).title("Marker in McHenry"));
        //mMap.addMarker(new MarkerOptions().position(MerrillGarden).title("Marker in MerrillGarden"));
        //mMap.addMarker(new MarkerOptions().position(PorterMeadows).title("Marker in PorterMeadows"));
        //mMap.addMarker(new MarkerOptions().position(Arboretum).title("Marker in Arboretum"));
        //mMap.addMarker(new MarkerOptions().position(KoiPond).title("Marker in KoiPond"));
        //mMap.addMarker(new MarkerOptions().position(RockGarden).title("Marker in RockGarden"));
//        mMap.addMarker(new MarkerOptions().position(QuarryPlaza).title("Marker in QuarryPlaza"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ucsc));

        // Location Services -> need to ask permission
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);


        //Global Circle
        //Circle ActiveCircle = mMap.addCircle(new CircleOptions()
        //        .center(activeLatLng)
        //        .radius(150)
        //        .fillColor(0x220000FF));


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
                new LatLng(36.977163, -122.038426), new LatLng(37.010616, -122.044647));

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

    // check if point is inside geofence using the Haversine formula
    // https://stackoverflow.com/questions/30719757/is-there-any-api-for-calculating-geofence-breach-other-than-android-apis
    boolean checkInside(Circle circle, double longitude, double latitude) {
        return calculateDistance(
                circle.getCenter().longitude, circle.getCenter().latitude, longitude, latitude
        ) < 2;
    }


    void checkInsideEvent() {

        double eventLong= activeEvent.yCoordinate;
        double eventLat = activeEvent.xCoordinate;
        double circleLong = activeCircle.getCenter().longitude;
        double circleLat = activeCircle.getCenter().latitude;

        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //test to check if geofences work.
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //Redundant permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double playerlatitude = location.getLatitude();
        double playerlongitude = location.getLongitude();

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        double inRadius =  calculateDistance(circleLong, circleLat, longitude, latitude);


        Log.v("REEDTEST", "\n=======PLAYER \n  longitude : " + longitude + "\n  latitude : " + latitude + "\n  inRadius : " + inRadius);
        Log.v("REEDTEST", "+++++++EVENT \n  longitude : " + circleLong + "\n  latitude : " + circleLat + "\n  inRadius : " + inRadius);

        boolean check = inRadius < activeCircle.getRadius();
        Log.v("REEDTEST", "Check : " + check + "inRadius: " + inRadius);



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
        checkInsideEvent();
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
}