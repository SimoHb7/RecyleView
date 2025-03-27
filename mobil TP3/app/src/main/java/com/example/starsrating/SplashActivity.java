package com.example.starsrating;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.logo);

        // Applying animations
        logo.animate().rotation(360f).setDuration(2000);
        logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000);
        logo.animate().translationYBy(1000f).setDuration(2000);
        logo.animate().alpha(0f).setDuration(6000);

        // Thread to handle screen transition
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(SplashActivity.this, com.example.starsrating.ListActivity.class);
                    startActivity(intent);
                    finish();  // Ensure the splash screen is removed from the back stack
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
