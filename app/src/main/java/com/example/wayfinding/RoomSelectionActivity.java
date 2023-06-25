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

import com.example.wayfinding.mapComponents.IndoorMap;
import com.example.wayfinding.mapComponents.Room;
import com.example.wayfinding.viewComponents.MapFileListAdapter;
import com.example.wayfinding.viewComponents.RoomListAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private boolean newMap;

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
            this.newMap = false;
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
            this.newMap = true;
            this.roomList = new ArrayList<Room>();
            this.indoorMap = new IndoorMap(incomingIntent.getStringExtra("name"));
        }
    }

    private void setInterface(){
        //Recycler view config
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
                openActivityMain();
            }
        });
        this.newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//TODO hacer el popup del nombre y cambiar R.id.s
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.room_name_popup, null);

                Button saveButton = popupView.findViewById(R.id.roomName_edit_button);
                EditText nameEditText = popupView.findViewById(R.id.roomName_edit_text);

                AlertDialog dialog = new AlertDialog.Builder(RoomSelectionActivity.this)
                        .setView(popupView)
                        .create();

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameEditText.getText().toString();
                        // Crear un nuevo mapa utilizando el nombre ingresado
                        dialog.dismiss();
                        Log.d("RoomSelectionActivity", "Se crea la habitación " + name);

                        openActivityCreate(name, indoorMap.NextId());
                    }
                });

                dialog.show();
            }
        });
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO guardar mapa en json en la memoria
                saveMap();
                openMapSelectionActivity();
            }
        });
    }

    public void openActivityCreate(String name, int id){//TODO modificar createActivity pa recibir todo bn
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("name", name); //Hay que comprobar que no se repita o poner (numreps) si se repite al final del nombre.
        intent.putExtra("id", id);
        intent.putExtra("map", this.indoorMap);
        if(this.newMap) intent.putExtra("new", true);

        startActivity(intent);
        finish();
    }

    public void openActivityCreate(Room room){//TODO modificar createActivity pa recibir todo bn
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("room", room);
        intent.putExtra("map", this.indoorMap);
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
    public void deleteRoom(int position) { //TODO preguntar confirmacion y borrar la room correspondiente
        Log.d("RoomSelectionActivity", "Se ha pulsado el botón delete del elemento nº: " + position);
        roomList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, roomList.size());
    }

    @Override
    public void editRoom(int position) {
        Log.d("RoomSelectionActivity", "Se ha pulsado el botón edit del elemento nº: " + position);
        openActivityCreate(roomList.get(position));
        //TODO crear el editor de rooms común
    }

    public void saveMap(){
        File file = new File(this.getFilesDir(), this.indoorMap.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();

        if(newMap) {
            Log.d("SaveMap", "NEW MAP");//Para comprobar que el nuevo mapa es nombre único TODO mejorar
            if (file.exists())
                file = new File(this.getFilesDir(), this.indoorMap.getName() + "_copy.json");
        }

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
