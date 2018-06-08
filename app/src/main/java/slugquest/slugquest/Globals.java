package slugquest.slugquest;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

public class Globals extends Application{

    private float[] dummyDirection = {0.0f,0.0f,0.0f};

    // Declares active event that will be overwritten as user progresses
    private Event activeEvent = new Event("Global Event",36.994803, -122.067737,"sunset",1, dummyDirection);
    //public Event[] eventArray;


    //Declares all events
    private Event[] eventArray= {
            new Event("Porter Meadows",36.994803, -122.067737,"sunset",1, dummyDirection),
            new Event("House",36.976465, -122.054937,"imagetest",0, dummyDirection),
    };

    public Event getEvent(){
        return this.activeEvent;
    }

    //Active event updated to child of current event
    public void updateEvent(){
        this.activeEvent = this.eventArray[this.activeEvent.next];
    }

    public String getImage(){
        return this.activeEvent.imageName;
        //return "sunset";
    }

}
