package com.example.wayfinding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mapComponents.IndoorMap;
import mapComponents.Room;
import viewComponents.MapFileListAdapter;
import viewComponents.RoomListAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.wayfinding.databinding.RoomNamePopupBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RoomSelectionActivity  extends AppCompatActivity implements RoomListAdapter.OnItemClickListener{
    private RecyclerView roomListRecyclerView;
    private RoomListAdapter adapter;
    private ArrayList<Room> roomList;
    private IndoorMap indoorMap;
    private Button newRoomButton, mainMenuButton, saveButton;
    private boolean editRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_selection);

        setRoomList();
        setInterface();
    }

    private void setRoomList() {
        Intent incomingIntent = getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra("IMmap")) {
            this.indoorMap = (IndoorMap) incomingIntent.getSerializableExtra("IMmap");
            this.roomList = this.indoorMap.getMap();
        }
        else if (incomingIntent != null && incomingIntent.hasExtra("Smap")){
            String mapString = incomingIntent.getStringExtra("Smap");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.indoorMap = objectMapper.readValue(mapString, IndoorMap.class);
                this.roomList = this.indoorMap.getMap();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else if(incomingIntent != null && incomingIntent.hasExtra("new")) {Log.d("RSA", "Pilla el extra new");
            this.roomList = new ArrayList<Room>();
            this.indoorMap = new IndoorMap(incomingIntent.getStringExtra("name"));
        }
    }

    private void setInterface(){
        //Recycler view config
        this.editRoom = false;
        this.roomListRecyclerView = findViewById(R.id.room_recyclerview);
        this.adapter = new RoomListAdapter(this, this.roomList);
        this.adapter.setOnItemClickListener(this);
        this.roomListRecyclerView.setAdapter(this.adapter);
        this.roomListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Button
        this.saveButton = findViewById(R.id.saveMap_button);
        this.newRoomButton = findViewById(R.id.newRoom_button);
        this.mainMenuButton = findViewById(R.id.mainMenu_button);
        this.mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapSelectionActivity();
            }
        });


        this.newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomNamePopupBinding popupBinding = RoomNamePopupBinding.inflate(getLayoutInflater());
                View popupView = popupBinding.getRoot();

                Room bindedRoom = new Room(indoorMap.nextId());
                popupBinding.setRoom(bindedRoom);

                Button saveButton = popupBinding.roomNameSaveButton;

                AlertDialog dialog = new AlertDialog.Builder(RoomSelectionActivity.this)
                        .setView(popupView)
                        .create();

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        openActivityCreate(bindedRoom);

                    }
                });

                dialog.show();
            }
        });
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMap();
                openMapSelectionActivity();
            }
        });
    }

    public void openActivityCreate(Room room){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("room", room);
        intent.putExtra("map", this.indoorMap);
        intent.putExtra("new", this.editRoom);
        startActivity(intent);
        finish();
    }

    private void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMapSelectionActivity(){
        Intent intent = new Intent(this, MapSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteRoom(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_confirm_popup, null);

        Button delete = popupView.findViewById(R.id.confirm_button);
        Button cancel = popupView.findViewById(R.id.cancel_button);

        AlertDialog dialog = new AlertDialog.Builder(RoomSelectionActivity.this)
                .setView(popupView)
                .create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, roomList.size());
                dialog.dismiss();
                indoorMap.setnRoom(indoorMap.getnRoom() -1);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Log.d("RoomSelectionActivity", "Se ha pulsado el botón delete del elemento nº: " + position);
    }

    @Override
    public void editRoom(int position) {
        Log.d("RoomSelectionActivity", "Se ha pulsado el botón edit del elemento nº: " + position);
        this.editRoom = true;
        openActivityCreate(roomList.get(position));
    }

    public void saveMap(){
        File file = new File(this.getFilesDir(), this.indoorMap.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(this.indoorMap);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
