package slugquest.slugquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

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
