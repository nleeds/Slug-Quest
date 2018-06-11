package slugquest.slugquest;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

public class Globals extends Application{

    private float[] dummyDirection = {0.0f,0.0f,0.0f};

    // Declares active event that will be overwritten as user progresses
    private Event activeEvent = new Event("House",36.976465, -122.054937,"imagetest",0,dummyDirection);
    //    private Event activeEvent = new Event("Global Event",36.994803, -122.067737,"sunset",1, dummyDirection);

    //public Event[] eventArray;
    //Declares all events

    private Event[] eventArray= {
            new Event("Crown Fountain",36.9999711, -122.0544336,"crownfountain",1,dummyDirection),
            new Event("Crown bust",37.000155, -122.054505,"crownbust",2,dummyDirection),
            new Event("stevenson bench",36.9969099, -122.0518351,"stevensonbench",3,dummyDirection),
            new Event("stevenson bust",36.9972888, -122.0522983,"stevensonbust",4,dummyDirection),
            new Event("Cowel fountain",36.996964, -122.053889,"cowelfountain",5,dummyDirection),
            new Event("the maternal tree",36.9971121, -122.0539536,"thematernaltree",6,dummyDirection),
            new Event("bookstore rock",36.9980634, -122.0557556,"bookstorerock",7,dummyDirection),
            new Event("amphitheatre",36.9988193, -122.0563132,"amphitheatre",8,dummyDirection),
            new Event("rock stairs",36.9988193, -122.0563132,"rockstairs",9,dummyDirection),
            new Event("slug tunnel",36.9990133, -122.055885,"slugtunnel",10,dummyDirection),
            new Event("c10 mural",37.0030467, -122.058443,"c10mural",11,dummyDirection),
            new Event("dumpster art",37.000304, -122.058739,"dumpsterart",12,dummyDirection),
            new Event("engineering bars",37.000497,-122.061762,"engineeringbars",13,dummyDirection),
            new Event("chaotic pendulum",36.998669, -122.059941,"chaoticpendulum",14,dummyDirection),
            new Event("seal sculpture",36.998244, -122.061368,"sealsculpture",15,dummyDirection),
            new Event("wood art",36.998175, -122.061495,"woodart",16,dummyDirection),
            new Event("carousel",36.997887, -122.062278,"carousel",17,dummyDirection),
            new Event("media theatre seat",36.995894, -122.061044, "mediatheatreseat",18,dummyDirection),
            new Event("happy lamp",36.993012, -122.063405,"happylamp",19,dummyDirection),
            new Event("Porter squiggle",36.993188, -122.065176,"squiggle",20,dummyDirection),
            new Event("koi pond",36.994140,-122.065172,"koiopnd",21,dummyDirection),
            new Event("Porter totem pole",36.994986,-122.065322,"totempole",22,dummyDirection),
            new Event("lego-head pot",36.998645,-122.066009,"legopothead",23,dummyDirection),
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
