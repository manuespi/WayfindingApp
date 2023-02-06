package com.example.wayfinding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private List<Room> map;
    private int nRoom;
    private RelativeLayout createLayout, defaultLayout;
    private String element;
    private ArrayList<String> orientationList;
    private int orientation, capacity;
    private boolean open, wheelchair;
    private Button mainMenuButton, showButton, newRoomButton, doorButton, stairsButton,
            elevatorButton, openButton, closeButton, wheelchairButton, upButton, downButton,
            addElementButton, newElementButton;
    private Spinner orientationSpinner;
    private CheckBox upCheckBox, downCheckBox, wheelchairCheckBox;
    private EditText capacityInput;
    private TextView roomElementsView, roomsView, currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initializeAttributes();
        setInterface();
    }

    private void setDefaultValues(){
        element = "empty";
        capacity = 6;
        open = true;
        wheelchair = false;
    }

    private void initializeAttributes(){
        map = new ArrayList<Room>();
        orientationList = new ArrayList<>();
        nRoom = 0;
        map.add(new Room(nRoom));
        orientation = 0;

        orientationList.add("North");
        orientationList.add("East");
        orientationList.add("South");
        orientationList.add("West");

        setDefaultValues();
    }

    private void openActivityMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addElementToRoom(){
        if(!map.isEmpty()) {
            map.get(nRoom).addElement(element, orientation, capacity, open, wheelchair);
        }
    }

    private void refreshRoomElementsView(){
        int nElem = map.get(nRoom).nElements();
        String elementsText = "";
        for(int i = 0; i < nElem; i++){
            elementsText += map.get(nRoom).get(i).getType() + ": " + map.get(nRoom).get(i).getOrientationString();

            if(i < nElem - 1) elementsText += "\n";
        }
        roomElementsView.setText(elementsText);
    }

    private void refreshRoomsView(){
        String roomsText = "";
        for(int i = 0; i <= nRoom; i++){
            roomsText += "Room " + i + ": " + map.get(i).nElements() + " elements";

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
        //add the elements and room list
    }

    @SuppressLint("ResourceType")
    private void setInterface(){
        createLayout = findViewById(R.id.createLayout);
        defaultLayout = createLayout;

        mainMenuButton = findViewById(R.id.mainMenu_button);
        showButton = findViewById(R.id.show_button);
        newRoomButton = findViewById(R.id.newRoom_button);
        addElementButton = findViewById(R.id.addElement_button);
        newElementButton = findViewById(R.id.newElement_button);

        roomElementsView = findViewById(R.id.roomElements);
        roomElementsView.setText("Room empty");
        roomsView = findViewById(R.id.rooms);
        roomsView.setText("Room 0: 0 elements");
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
        doorButtonParams.setMargins(20, 300, 20, 20);
        doorButton.setLayoutParams(doorButtonParams);

        RelativeLayout.LayoutParams stairsButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stairsButton.setId(2);
        stairsButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        stairsButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        stairsButtonParams.setMargins(20, 300, 20, 20);
        stairsButton.setLayoutParams(stairsButtonParams);

        RelativeLayout.LayoutParams elevatorButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        elevatorButton.setId(3);
        elevatorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        elevatorButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        elevatorButtonParams.setMargins(20, 300, 20, 20);
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
                    refreshRoomsView();
                    setDefaultValues();
                    setDefaultLayout();
                }
            }
        });

        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nRoom++;
                map.add(new Room(nRoom));

                refreshRoomElementsView();
                refreshRoomsView();
                refreshCurrentRoom();
                setDefaultValues();
                setDefaultLayout();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(map.isEmpty()) Log.d("Map" , "Empty");
                else {
                    String mapString = "";
                    for (int i = 0; i < map.size(); ++i) {
                        mapString += map.get(i).toString() + "\n";
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

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mapa", ArrayList<>(map);
    }*/
}