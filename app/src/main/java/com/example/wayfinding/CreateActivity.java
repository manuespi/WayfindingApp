package com.example.wayfinding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wayfinding.mapComponents.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.example.wayfinding.mapComponents.IndoorMap;

public class CreateActivity extends AppCompatActivity {
    private IndoorMap indoorMap;
    private int nRoom;
    private Room room;
    //private LinearLayout createLayout;
    private RelativeLayout createLayout;
    private String element;
    private ArrayList<String> orientationList;
    private int orientation, capacity;
    private boolean open, wheelchair;
    private Button mainMenuButton, saveButton, newRoomButton, doorButton, stairsButton,
            elevatorButton, openButton, closeButton,
            addElementButton, newElementButton;
    private Spinner orientationSpinner;
    private CheckBox upCheckBox, downCheckBox, wheelchairCheckBox;
    private EditText capacityInput;
    private TextView roomElementsView, roomsView, currentRoom;
    private AlertDialog.Builder roomConnectionAlert;
    private Gson gson;
    private boolean editingRoom, newMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initializeAttributes();
        setInterface();
        refreshRoomElementsView();
    }

    private void setDefaultValues(){
        element = "empty";
        capacity = 6;
        open = true;
        wheelchair = false;
    }

    private void initializeAttributes(){
        orientationList = new ArrayList<>();
        orientationList.add("North");
        orientationList.add("East");
        orientationList.add("South");
        orientationList.add("West");
        orientation = 0;
        roomConnectionAlert = new AlertDialog.Builder(this);
        gson = new Gson();
        this.indoorMap = new IndoorMap();
        roomElementsView = findViewById(R.id.roomElements);

        Intent incomingIntent = getIntent();

        if(incomingIntent != null && incomingIntent.hasExtra("room")) {
            this.room = (Room) incomingIntent.getSerializableExtra("room");
            this.indoorMap = (IndoorMap) incomingIntent.getSerializableExtra("map");
            this.editingRoom = true;
            this.newMap = false;
            Toast.makeText(CreateActivity.this, "Mapa cargado (room)", Toast.LENGTH_SHORT).show();

        }
        else if(incomingIntent != null && incomingIntent.hasExtra("id")){
            String name = incomingIntent.getStringExtra("name");
            int id = incomingIntent.getIntExtra("id", 0);
            this.indoorMap = (IndoorMap) incomingIntent.getSerializableExtra("map");
            this.room = new Room();
            this.room.setName(name);
            this.room.setId(id);
            this.editingRoom = false;
            Toast.makeText(CreateActivity.this, "Mapa cargado (id) " + id, Toast.LENGTH_SHORT).show();

            if(incomingIntent.hasExtra("new"))
                this.newMap = true;
            else this.newMap = false;
        }
        else{
            Log.e("CreateActivity", "Algo ha ido mal al inicializar atributos.");
            Toast.makeText(CreateActivity.this, "Algo ha ido mal al inicializar atributos.", Toast.LENGTH_SHORT).show();
            this.editingRoom = false;
            this.newMap = false;
        }

        setDefaultValues();
    }

    private void receiveMap(String map, String name){
        this.indoorMap.setName(name);
        ObjectMapper objectMapper = new ObjectMapper();

        Log.d("CreateActivity", this.indoorMap.getName() + "receiving map");
        try {
            this.indoorMap = objectMapper.readValue(map, IndoorMap.class);
            nRoom = this.indoorMap.getMap().size() - 1;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addElementToRoom(){
            if(element == "empty"){
                Toast.makeText(CreateActivity.this, "No element selected", Toast.LENGTH_SHORT).show();
            }
            else {
                //indoorMap.addElementToRoom(nRoom, element, orientation, capacity, open, wheelchair);
                this.room.addElement(element, orientation, capacity, open, wheelchair);
            }
    }

    private void refreshRoomElementsView(){
        //int nElem = indoorMap.getRoom(nRoom).getnElements();
        Log.d("refresh", room.toString());
        int nElem = this.room.getnElements();
        String elementsText = "";
        for(int i = 0; i < nElem; i++){
            //elementsText += indoorMap.getRoom(nRoom).getElement(i).getType() + ": " + indoorMap.getRoom(nRoom).getElement(i).orientationString();
            elementsText += this.room.getElement(i).getType() + ": " + this.room.getElement(i).orientationString();Log.d("refresh", "Entra");

            if(i < nElem - 1) elementsText += "\n";
        }
        roomElementsView.setText(elementsText);
    }

    private void refreshRoomsView(){
        String roomsText = "";
        for(int i = 0; i <= nRoom; i++){
            roomsText += "Room " + i + ": " + indoorMap.getRoom(i).getnElements() + " elements";

            if(i < nRoom) roomsText += "\n";
        }
        roomsView.setText(roomsText);
    }

    private void refreshCurrentRoom(){
        String currentRoom = "Current room: " + nRoom;

        this.currentRoom.setText(currentRoom);
    }

    @SuppressLint("ResourceType")
    private void setDefaultLayout(){
        if(createLayout.findViewById(1) != null) {
            createLayout.removeView(doorButton);
        }
        if(createLayout.findViewById(2) != null) {
            createLayout.removeView(stairsButton);
        }
        if(createLayout.findViewById(3) != null) {
            createLayout.removeView(elevatorButton);
        }
        if(createLayout.findViewById(4) != null) {
            createLayout.removeView(orientationSpinner);
        }
        if(createLayout.findViewById(5) != null) {
            createLayout.removeView(wheelchairCheckBox);
        }
        if(createLayout.findViewById(6) != null) {
            createLayout.removeView(capacityInput);
        }
    }

    private void roomConnectionAlert() {
        roomConnectionAlert.create().show();
    }

    @SuppressLint("ResourceType")
    private void setInterface(){
        createLayout = findViewById(R.id.createLayout);

        mainMenuButton = findViewById(R.id.mainMenu_button);
        saveButton = findViewById(R.id.save_button);
        newRoomButton = findViewById(R.id.newRoom_button);
        addElementButton = findViewById(R.id.addElement_button);
        newElementButton = findViewById(R.id.newElement_button);

        //roomElementsView = findViewById(R.id.roomElements);
        roomElementsView.setText("Room empty");
        roomsView = findViewById(R.id.rooms);
        //roomsView.setText("Room 0: 0 elements");
        currentRoom = findViewById(R.id.currentRoom);
        refreshCurrentRoom();

        doorButton = new Button(this);
        stairsButton = new Button(this);
        elevatorButton = new Button(this);
        openButton = new Button(this);
        closeButton = new Button(this);

        doorButton.setText("Door");
        stairsButton.setText("Stairs");
        elevatorButton.setText("Elevator");
        openButton.setText("Open");
        closeButton.setText("Closed");

        RelativeLayout.LayoutParams doorButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        doorButton.setId(1);
        doorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        doorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //doorButtonParams.setMargins(20, 300, 20, 20);
        doorButton.setLayoutParams(doorButtonParams);

        RelativeLayout.LayoutParams stairsButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stairsButton.setId(2);
        stairsButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        stairsButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //stairsButtonParams.setMargins(20, 300, 20, 20);
        stairsButton.setLayoutParams(stairsButtonParams);

        RelativeLayout.LayoutParams elevatorButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        elevatorButton.setId(3);
        elevatorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        elevatorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //elevatorButtonParams.setMargins(20, 300, 20, 20);
        elevatorButton.setLayoutParams(elevatorButtonParams);
////Spinner
        orientationSpinner = new Spinner(this);

        RelativeLayout.LayoutParams orientationSpinnerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        orientationSpinner.setId(4);
        orientationSpinnerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        orientationSpinnerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        orientationSpinnerParams.setMargins(150, 550, 20, 20);
        orientationSpinner.setLayoutParams(orientationSpinnerParams);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, orientationList);
        orientationSpinner.setAdapter(spinnerAdapter);

        orientationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orientation = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
////CheckBox
        wheelchairCheckBox = new CheckBox(this);

        wheelchairCheckBox.setText("Wheelchair");

        RelativeLayout.LayoutParams wheelchairCheckBoxParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        wheelchairCheckBox.setId(5);
        wheelchairCheckBoxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        wheelchairCheckBoxParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        wheelchairCheckBoxParams.setMargins(20, 550, 150, 20);
        wheelchairCheckBox.setLayoutParams(wheelchairCheckBoxParams);

        wheelchairCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wheelchair = isChecked;
            }
        });

//// EditText
        capacityInput = new EditText(this);

        //capacityInput.setText("");
        capacityInput.setId(6);
        capacityInput.setHint("Capacity");

        RelativeLayout.LayoutParams capacityInputParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        capacityInputParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        capacityInputParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        capacityInputParams.setMargins(20, 550, 20, 20);
        capacityInput.setLayoutParams(capacityInputParams);
//// AlertDialog Builder
        roomConnectionAlert.setTitle("Now I`m going through:");
        String[] elements = {"1", "2", "3"};
        roomConnectionAlert.setItems(elements, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //map.get(nRoom).get(which).setConnects(connects);
                Log.d("Alert", "Item " + which + "selected.");
            }
        });
////
        doorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "door";

                if(createLayout.findViewById(5) != null) {
                    createLayout.removeView(wheelchairCheckBox);
                }
                if(createLayout.findViewById(6) != null) {
                    createLayout.removeView(capacityInput);
                }

                if(createLayout.findViewById(4) == null) {
                    createLayout.addView(orientationSpinner);
                }
            }
        });

        stairsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "stairs";

                if(createLayout.findViewById(6) != null) {
                    createLayout.removeView(capacityInput);
                }

                if(createLayout.findViewById(4) == null) {
                    createLayout.addView(orientationSpinner);
                }
                if(createLayout.findViewById(5) == null) {
                    createLayout.addView(wheelchairCheckBox);
                }
            }
        });

        elevatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "elevator";

                if(createLayout.findViewById(4) == null) {
                    createLayout.addView(orientationSpinner);
                }
                if(createLayout.findViewById(5) == null) {
                    createLayout.addView(wheelchairCheckBox);
                }
                if(createLayout.findViewById(6) == null) {
                    createLayout.addView(capacityInput);
                }
            }
        });

        newElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultValues();
                setDefaultLayout();

                if(createLayout.findViewById(1) == null) {
                    createLayout.addView(doorButton);
                }
                if(createLayout.findViewById(2) == null) {
                    createLayout.addView(stairsButton);
                }
                if(createLayout.findViewById(3) == null) {
                    createLayout.addView(elevatorButton);
                }
            }
        });

        addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean parametersOk = true;
                if(createLayout.findViewById(6) != null) {
                    try
                    {
                        capacity = Integer.parseInt(capacityInput.getText().toString());
                    }
                    catch (NumberFormatException nfe)
                    {
                        Toast.makeText(CreateActivity.this, "Capacity should be a number", Toast.LENGTH_SHORT).show();
                        parametersOk = false;
                    }
                }

                if(parametersOk) {
                    addElementToRoom();
                    refreshRoomElementsView();
                    //refreshRoomsView();
                    setDefaultValues();
                    setDefaultLayout();
                }
            }
        });

        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //roomConnectionAlert();

                nRoom++;
                indoorMap.addRoom();

                refreshRoomElementsView();
                //refreshRoomsView();
                refreshCurrentRoom();
                setDefaultValues();
                setDefaultLayout();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editingRoom){Toast.makeText(CreateActivity.this, "Editing Room", Toast.LENGTH_SHORT).show();
                    ArrayList<Room> auxMap = indoorMap.getMap();

                    for(int i = 0; i < auxMap.size(); ++i)
                        if(auxMap.get(i).getId() == room.getId())
                            auxMap.set(i, room);

                    indoorMap.setMap(auxMap);
                }
                else{Toast.makeText(CreateActivity.this, "Not Editing Room", Toast.LENGTH_SHORT).show();
                    indoorMap.addRoom(room);
                }

                saveMap();

                Intent intent = new Intent(CreateActivity.this, RoomSelectionActivity.class);
                if(indoorMap == null) Log.d("CALVERIO", "IM is null: ");
                else Log.d("CALVERIO", "IM is not null: "+indoorMap.getnRoom());
                intent.putExtra("IMmap", indoorMap);
                startActivity(intent);
                finish();

                /*String mapString = "";
                for (int i = 0; i < indoorMap.getMap().size(); ++i) {
                    mapString += indoorMap.getRoom(i).toString() + "\n";
                }

                Intent intent = new Intent(CreateActivity.this, ViewActivity.class);
                intent.putExtra("map", mapString);
                startActivity(intent);
                finish();*/
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMain();
            }
        });
    }

    public void saveMap(){
        File file = new File(this.getFilesDir(), this.indoorMap.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();
        
        if(newMap) {
            Log.d("SaveMap", "NEW MAP");//Para comprobar que el nuevo mapa es nombre único TODO mejorar
            if (file.exists())
                file = new File(this.getFilesDir(), this.indoorMap.getName() + "_copy.json");
        }
        if(editingRoom)Log.d("SaveMap", "EDITING ROOM");

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