package slugquest.slugquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class TitleActivity extends AppCompatActivity {

    //Test Var
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //Global Test   => How to use global variables                                       ((Globals) this.getApplication())
        //String globTest = ((Globals) this.getApplication()).getImage();


        //Image test => Shows that we can read global variables from different files! Yay!!

        //Event porterMeadowsEvent = new Event("Porter Meadows",36.994803, -122.067737,"sunset",1);
        Event porterMeadowsEvent = ((Globals) this.getApplication()).getEvent();
        String imageName = porterMeadowsEvent.imageName;
        //String imageName = "slugquest";

        //iv = (ImageView)findViewById(R.id.testImage);
        //iv.setImageResource(getResources().getIdentifier(imageName, "drawable", getPackageName()));


        Button startButton = findViewById(R.id.startButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button creditsButton = findViewById(R.id.creditsButton);

        startButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){


                Intent i = new Intent(TitleActivity.this, Map_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        settingsButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(TitleActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        creditsButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(TitleActivity.this, CreditsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
