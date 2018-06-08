package slugquest.slugquest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Event {

    //Variable Declaration
    public String name;
    public String imageName;
    public double xCoordinate;
    public double yCoordinate;
    public int next;
    public float[] direction;

    public Event(String nameInput, double xCoordinateInput, double yCoordinateInput, String imageInput, int nextInput, float[] directionInput){

        // String Names
        name = nameInput;
        imageName = imageInput; //Could possibly have image2 here if we fade in the new image when they find it.

        //The actual coordinates
        xCoordinate = xCoordinateInput;
        yCoordinate = yCoordinateInput;

        next = nextInput;

        direction = directionInput;
    }

    //Helper Functions

}
