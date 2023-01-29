package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    List<Room> map;
    int nRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        map = new ArrayList<Room>();
        nRoom = 0;
        map.add(new Room(nRoom));

        Button mainMenuButton, showButton, leftButton, rightButton, topButton, bottomButton, newRoomButton;

        mainMenuButton = findViewById(R.id.mainMenu_button);
        showButton = findViewById(R.id.show_button);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
        topButton = findViewById(R.id.top_button);
        bottomButton = findViewById(R.id.bottom_button);
        newRoomButton = findViewById(R.id.newRoom_button);

        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nRoom++;
                map.add(new Room(nRoom));
                Log.d("NewRoom pressed", "Room: " + nRoom);
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!map.isEmpty()) {
                    Log.d("Left pressed", "Room: " + nRoom);
                    map.get(nRoom).addElement("door", 3, 0, true, true, true, true);
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!map.isEmpty()) {
                    Log.d("Right pressed", "Room: " + nRoom);
                    map.get(nRoom).addElement("door", 1, 0, false, true, true, true);
                }
            }
        });

        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!map.isEmpty()) {
                    Log.d("Top pressed", "Room: " + nRoom);
                    map.get(nRoom).addElement("elevator", 0, 5, true, true, true, true);
                }
            }
        });

        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!map.isEmpty()) {
                    Log.d("Bottom pressed", "Room: " + nRoom);
                    map.get(nRoom).addElement("stairs", 2, 0, true, false, true, true);
                }
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(map.isEmpty()) Log.d("Mapa" , "Vacío");
                else {
                    String mapString = "";
                    for (int i = 0; i < map.size(); ++i) {
                        //Log.d("Sala", map.get(i).toString());
                        mapString += "Sala: " + map.get(i).toString();
                    }
                    Intent intent = new Intent(CreateActivity.this, ViewActivity.class);
                    intent.putExtra("map", mapString);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });
    }

    public void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mapa", ArrayList<>(map);
    }*/
}