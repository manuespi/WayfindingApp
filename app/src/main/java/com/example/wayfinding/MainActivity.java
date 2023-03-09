package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button create_button, view_button, exit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_button = (Button) findViewById(R.id.create_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openActivityCreate();
                openActivityMapSelection();
            }
        });

        view_button = (Button) findViewById(R.id.view_button);
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityView();
            }
        });

        exit_button = (Button) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityExit();
            }
        });
    }

    public void openActivityCreate(){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
        finish();
    }

    public void openActivityView(){
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
        finish();
    }

    public void openActivityMapSelection(){
        Intent intent = new Intent(this, MapSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void openActivityExit(){
       /* Intent intent = new Intent(this, Exit.class);
        startActivity(intent);*/
        System.exit(0);
    }
}