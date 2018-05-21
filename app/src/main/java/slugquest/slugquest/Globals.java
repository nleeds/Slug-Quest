package slugquest.slugquest;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

public class Globals extends Application{


    // Declares active event that will be overwritten as user progresses
    private Event activeEvent = new Event("Porter Meadows",36.994803, -122.067737,"sunset",1);
    //public Event[] eventArray;

    //Declares all events
    private Event[] eventArray= {
            new Event("Porter Meadows",36.994803, -122.067737,"sunset",1),
            new Event("Buddha Hut",37.006443, -122.059919,"imagetest",0),
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
