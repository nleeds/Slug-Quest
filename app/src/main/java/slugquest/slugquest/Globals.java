package slugquest.slugquest;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

public class Globals extends Application{

    private float dummyDirection = 0.0f;

    // Declares active event that will be overwritten as user progresses
    private Event activeEvent = new Event("House",36.976465, -122.054937,"imagetest",0,dummyDirection);
    //    private Event activeEvent = new Event("Global Event",36.994803, -122.067737,"sunset",1, dummyDirection);

    //public Event[] eventArray;
    //Declares all events

    private Event[] eventArray= {
            new Event("The Crown Fountain",36.9999711, -122.0544336,"crownfountain",1,100),
            new Event("The Crown Bust",37.000155, -122.054505,"crownbust",2,200),
            new Event("The Stevenson Bench",36.9969099, -122.0518351,"stevensonbench",3,50),
            new Event("The Stevenson Bust",36.9972888, -122.0522983,"stevensonbust",4,10),
            new Event("Teh Cowel Fountain",36.996964, -122.053889,"cowelfountain",5,40),
            new Event("The Maternal Tree",36.9971121, -122.0539536,"thematernaltree",6,150),
            new Event("The Bookstore Rock",36.9980634, -122.0557556,"bookstorerock",7,300),
            new Event("The Amphitheatre",36.9988193, -122.0563132,"amphitheatre",8,20),
            new Event("The Rock Stairs",36.9988193, -122.0563132,"rockstairs",9,5),
            new Event("The Slug Tunnel",36.9990133, -122.055885,"slugtunnel",10,100),
            new Event("The College Ten Murals",37.0030467, -122.058443,"c10mural",11,150),
            new Event("The Dumpster Art",37.000304, -122.058739,"dumpsterart",12,50),
            new Event("The Engineering Bars",37.000497,-122.061762,"engineeringbars",13,230),
            new Event("The Chaotic Pendulum",36.998669, -122.059941,"chaoticpendulum",14,123),
            new Event("The Seal Sculpture",36.998244, -122.061368,"sealsculpture",15,52),
            new Event("Thimann Wood Art",36.998175, -122.061495,"woodart",16,69),
            new Event("The Carousel",36.997887, -122.062278,"carousel",17,42),
            new Event("The Media Ttheatre Forest Seat",36.995894, -122.061044, "mediatheatreseat",18,190),
            new Event("The Happy Lamp",36.993012, -122.063405,"happylamp",19,234),
            new Event("The Porter Squiggle",36.993188, -122.065176,"squiggle",20,12),
            new Event("The Porter Koi Pond",36.994140,-122.065172,"koiopnd",21,75),
            new Event("The Porter Totem Pole",36.994986,-122.065322,"totempole",22,93),
            new Event("The Lego-head P ots",36.998645,-122.066009,"legopothead",23,290),
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

    public float getCompassAngle(){
        return this.activeEvent.direction;
    }

}
