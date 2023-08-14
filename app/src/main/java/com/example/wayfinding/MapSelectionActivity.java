package com.example.wayfinding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wayfinding.mapComponents.IndoorMap;
import com.example.wayfinding.viewComponents.MapFileListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapSelectionActivity extends AppCompatActivity implements MapFileListAdapter.OnItemClickListener{
    private RecyclerView mapListRecyclerView;
    private MapFileListAdapter adapter;
    private ArrayList<File> jsonFileList;
    private Button newMapButton, mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        setMapFileList();
        setInterface();
    }

    private void setMapFileList(){
        File[] mapFileList = this.getFilesDir().listFiles();
        this.jsonFileList = new ArrayList<File>();

        for (File file : mapFileList) {
            if (file.getName().endsWith(".json")) {
                jsonFileList.add(file);
                Log.d("MapSelectionActivity", "fichero " + file.getName());
            }
        }
    }

    private void setInterface(){
        //Recycler view config
        this.mapListRecyclerView = findViewById(R.id.map_recyclerview);
        this.adapter = new MapFileListAdapter(this, this.jsonFileList);
        this.adapter.setOnItemClickListener(this);
        this.mapListRecyclerView.setAdapter(this.adapter);
        this.mapListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Button
        this.newMapButton = findViewById(R.id.newMap_button);
        //this.mainMenuButton = findViewById(R.id.mainMenu_button);
        /*this.mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });*/
        this.newMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.map_name_popup, null);

                Button saveButton = popupView.findViewById(R.id.mapName_edit_button);
                EditText nameEditText = popupView.findViewById(R.id.mapName_edit_text);

                AlertDialog dialog = new AlertDialog.Builder(MapSelectionActivity.this)
                        .setView(popupView)
                        .create();

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameEditText.getText().toString();
                        // Crear un nuevo mapa utilizando el nombre ingresado
                        dialog.dismiss();
                        //openActivityCreate(name);
                        openActivityEmptyRoomSelection(name);
                    }
                });

                dialog.show();
            }
        });
    }

    public void openActivityCreate(String name){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("name", name); //Hay que comprobar que no se repita o poner (numreps) si se repite al final del nombre.
        intent.putExtra("new", true);
        startActivity(intent);
        finish();
    }

    public void openActivityRoomSelection(String map){
        Intent intent = new Intent(this, RoomSelectionActivity.class);
        intent.putExtra("Smap", map);
        startActivity(intent);
        finish();
    }

    public void openActivityEmptyRoomSelection(String name){
        Intent intent = new Intent(this, RoomSelectionActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("new", true);
        startActivity(intent);
        finish();
    }

    private void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openActivityPlay(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteMap(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_confirm_popup, null);

        Button delete = popupView.findViewById(R.id.confirm_button);
        Button cancel = popupView.findViewById(R.id.cancel_button);

        AlertDialog dialog = new AlertDialog.Builder(MapSelectionActivity.this)
                .setView(popupView)
                .create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = jsonFileList.get(position);
                jsonFileList.remove(position);
                f.delete();
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, jsonFileList.size());
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Log.d("MapSelectionActivity", "Se ha pulsado el botón delete del elemento nº: " + position);
    }

    @Override
    public void editMap(int position) {
        String name = jsonFileList.get(position).getName().replace(".json", "");
        Log.d("MapSelectionActivity", "Se ha pulsado el botón edit del elemento nº: " + position + ", " + name);
        String map = "";
        try {
            FileReader reader = new FileReader(jsonFileList.get(position));
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                map += line;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("editMap", map);
        openActivityRoomSelection(map);
    }

    @Override
    public void playMap(int position) {
        Log.d("MapSelectionActivity", "Se ha pulsado el botón play del elemento nº: " + position);

        openActivityPlay();
    }
}