package com.thetechshrine.mangareader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thetechshrine.mangareader.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LaunchActivity extends AppCompatActivity {

    private TextView txtAppName;
    private Button btnStart;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_launch);

        // Bind views
        txtAppName = findViewById(R.id.txtAppName);
        btnStart = findViewById(R.id.btnStart);

        // Set custom fonts
        txtAppName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/poppins/Poppins-SemiBold.ttf"));
        btnStart.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/open_sans/OpenSans-Bold.ttf"));

        // Set Event listeners
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            btnStart.setStateListAnimator(null);
//        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
