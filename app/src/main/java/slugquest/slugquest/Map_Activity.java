package slugquest.slugquest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(36.9915, -122.0583);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in UCSC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
    }

    //Permission request
    public void permissionRequest(){
        if(ContextCompat.checkSelfPermission(Map_Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Map_Activity.this,"Already granted",Toast.LENGTH_SHORT).show();
        }else{
            requestLocationPermission();
        }
    }
    private void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This is the whole app, come on")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Map_Activity.this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);
                        }
                    })
                    .setNegativeButton("Fuck you", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Access denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
