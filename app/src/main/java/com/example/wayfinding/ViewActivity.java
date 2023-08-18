package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
    private Button mainMenu_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        TextView incomingData = findViewById(R.id.map);

        Intent incomingIntent = getIntent();
        if(incomingIntent != null && incomingIntent.hasExtra("map")) {
            String mapString = incomingIntent.getStringExtra("map");

            Log.d("Showing map", mapString);
            incomingData.setText(mapString);
        }
        else incomingData.setText("Not available yet");

        mainMenu_button = (Button) findViewById(R.id.mainMenu_button);
        mainMenu_button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                //openActivityMain();
                openMapSelectionActivity();
            }
        });
    }

    public void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMapSelectionActivity(){
        Intent intent = new Intent(this, MapSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}