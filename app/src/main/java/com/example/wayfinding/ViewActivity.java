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

        TextView incomingData;// = (TextView) findViewById(R.id.etIncomingData);

        Intent incomingIntent = getIntent();
        String mapString = incomingIntent.getStringExtra("map");

        Log.d("Showing map", mapString);
        //incomingData.setText(mapString);

        mainMenu_button = (Button) findViewById(R.id.mainMenu_button);
        mainMenu_button.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                openActivityMain();
            }
        });
    }

    public void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}