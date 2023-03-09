package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wayfinding.viewComponents.MapFileListAdapter;

import java.io.File;
import java.util.ArrayList;

public class MapSelectionActivity extends AppCompatActivity implements MapFileListAdapter.OnItemClickListener{
    private RecyclerView mapListRecyclerView;
    private MapFileListAdapter adapter;
    private ArrayList<File> jsonFileList;

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
    }

    private void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int position) { //preguntar confirmacion y borrar el fichero correspondiente
        Log.d("MapSelectoinActivity", "Se ha pulsado el botón delete del elemento nº: " + position);
    }
}