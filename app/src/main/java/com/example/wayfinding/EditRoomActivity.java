package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



import mapComponents.Room;

public class EditRoomActivity extends AppCompatActivity {
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initializeAttributes();
    }

    public void initializeAttributes(){
        Intent incomingIntent = getIntent();
        TextView pruebatv = findViewById(R.id.editRoom_prueba);

        if(incomingIntent != null && incomingIntent.hasExtra("room")) {
            this.room = (Room) incomingIntent.getSerializableExtra("room");
            pruebatv.setText("room recogida: " + this.room.getId());
        }
        else{
            String name = incomingIntent.getStringExtra("name");
            int id = incomingIntent.getIntExtra("id", 0);
            pruebatv.setText("crear room " + id + ": " + name);
            this.room = new Room();
            this.room.setName(name);
            this.room.setId(id);
        }
    }
}