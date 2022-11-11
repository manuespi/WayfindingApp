package com.example.wayfinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                openActivityCreate();
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
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
    }

    public void openActivityView(){
        Intent intent = new Intent(this, View.class);
        startActivity(intent);
    }

    public void openActivityExit(){
       /* Intent intent = new Intent(this, Exit.class);
        startActivity(intent);*/
        finish();
    }
}