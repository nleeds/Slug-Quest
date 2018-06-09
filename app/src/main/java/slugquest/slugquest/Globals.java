package slugquest.slugquest;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

public class Globals extends Application{


    // Declares active event that will be overwritten as user progresses
    private Event activeEvent = new Event("Global Event",36.994803, -122.067737,"sunset",1);
    //public Event[] eventArray;

    //Declares all events
    //TODO: replace with actual values
    double unknownlat = 36.994803;
    double unknownlng = -122.067737;

    //
    private Event[] eventArray= {
            new Event("Porter Meadows",36.994803, -122.067737,"sunset",1),
            new Event("amphitheatre",unknownlat, unknownlng,"amphitheatre",2),
            new Event("bookstore rock",unknownlat, unknownlng,"bookstorerock",3),
            new Event("c10 mural",unknownlat, unknownlng,"c10mural",4),
            new Event("House",36.976465, -122.054937,"imagetest",5),
            new Event("carousel",unknownlat, unknownlng,"carousel",6),
            new Event("chaotic pendulum",36.998669, -122.059941,"chaoticpendulum",7),
            new Event("cowel fountain",unknownlat, unknownlng,"cowelfountain",8),
            new Event("deathly hallows",unknownlat, unknownlng,"deathlyhallows",9),
            new Event("dumpster art",37.000304, -122.058739,"dumpsterart",10),
            new Event("happy lamp",36.993012, -122.063405,"happylamp",11),
            new Event("media theatre seat",36.995894, -122.061044, "mediatheatreseat",12),
            new Event("rock stairs",unknownlat, unknownlng,"rockstairs",13),
            new Event("slug tunnel",unknownlat, unknownlng,"slugtunnel",14),
            new Event("stevenson bench",unknownlat, unknownlng,"stevensonbench",15),
            new Event("stevenson bust",unknownlat, unknownlng,"stevensonbust",16),
            new Event("the maternal tree",unknownlat, unknownlng,"thematernaltree",17),
            new Event("tree in building",unknownlat, unknownlng,"treeinbuilding",18),
            new Event("wood art",36.998175, -122.061495,"woodart",19),
            new Event("seal sculpture",36.998244, -122.061368,"sealsculpture",0),
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
