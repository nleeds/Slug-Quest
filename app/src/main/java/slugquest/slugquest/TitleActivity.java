package slugquest.slugquest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        Button instructionsButton = findViewById(R.id.instructionsButton);

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
        instructionsButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(TitleActivity.this, InstructionsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
