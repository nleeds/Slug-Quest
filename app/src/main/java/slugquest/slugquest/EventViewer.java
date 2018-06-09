package slugquest.slugquest;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EventViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_event_viewer);

        ImageView eventImageView = (ImageView) findViewById(R.id.imageViewEventView);
        TextView eventTextView = findViewById(R.id.textViewEventView);
        Button eventNextButton = findViewById(R.id.buttonEventViewProceed);

        Intent i = getIntent();
        String eventName = i.getStringExtra("eventName");
        String eventImageName = i.getStringExtra("eventImageName");

        eventTextView.setText(eventName);

        Glide.with(this)  // Activity or Fragment
                .load(getResources().getIdentifier(eventImageName, "drawable", getPackageName()))
                .into(eventImageView);

        eventNextButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //pop the activity off the stack
                Intent i = new Intent(EventViewer.this, Map_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

}
