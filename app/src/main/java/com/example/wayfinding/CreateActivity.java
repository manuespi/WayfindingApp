package com.example.wayfinding;

import static java.lang.Integer.parseInt;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.wayfinding.databinding.ActivityCreateBinding;
import com.example.wayfinding.databinding.BasicRoomInputsBinding;

import java.util.ArrayList;

import mapComponents.IndoorMap;

public class CreateActivity extends AppCompatActivity {
    private IndoorMap indoorMap;
    private int nRoom;
    private LinearLayout createLayout;
    private String element;
    private ArrayList<String> orientationList;
    private int orientation, capacity;
    private boolean open, wheelchair;
    private Button mainMenuButton, showButton, newRoomButton, doorButton, stairsButton,
            elevatorButton, openButton, closeButton,
            addElementButton, newElementButton, resizeRoom, nextButton;
    private Spinner orientationSpinner;
    private CheckBox upCheckBox, downCheckBox, wheelchairCheckBox;
    private EditText capacityInput, roomXInput, roomYInput;
    private int roomXInt;
    private int roomYInt;
    private Integer roomWidth;
    private Integer roomLength;
    private TextView roomElementsView, roomsView, currentRoom;
    private AlertDialog.Builder roomConnectionAlert;
    private String roomName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_room_inputs);

//        BasicRoomInputsBinding basicRoomInputsBinding = DataBindingUtil.setContentView(this, R.layout.basic_room_inputs);
//        roomName = basicRoomInputsBinding.getRoomName() != null ? basicRoomInputsBinding.getRoomName().toString() : "";
//        roomWidth = basicRoomInputsBinding.getRoomWidth() != null ? basicRoomInputsBinding.getRoomWidth() : 0;
//        roomLength = basicRoomInputsBinding.getRoomLength() != null ? basicRoomInputsBinding.getRoomLength() : 0;

//        roomName = basicRoomInputsBinding.getRoomName().toString();
//        roomWidth = basicRoomInputsBinding.getRoomWidth();
//        roomLength = basicRoomInputsBinding.getRoomLength();




//        activityCreateBinding.setRoomName(roomName);
//        activityCreateBinding.setRoomWidth(((Integer) roomWidth));
//        activityCreateBinding.setRoomLength(((Integer) roomLength));



        nextButton = findViewById(R.id.next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_create);
                ActivityCreateBinding activityCreateBinding = DataBindingUtil.setContentView(CreateActivity.this, R.layout.activity_create);


//                activityCreateBinding.setRoomName("Salon");
//                activityCreateBinding.setRoomWidth(400);
//                activityCreateBinding.setRoomLength(500);


                TextView roomTextView = activityCreateBinding.roomView;
                roomTextView.setText("hola");
                initializeAttributes();
                setInterface();
            }
        });


    }


    private void setDefaultValues(){
        element = "empty";
        capacity = 6;
        open = true;
        wheelchair = false;
    }

    private void initializeAttributes(){
        indoorMap = new IndoorMap();
        orientationList = new ArrayList<>();
        nRoom = 0;
        indoorMap.addRoom();
        orientation = 0;
        roomConnectionAlert = new AlertDialog.Builder(this);

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
        if(!indoorMap.getMap().isEmpty()) {
            if(element == "empty"){
                Toast.makeText(CreateActivity.this, "No element selected", Toast.LENGTH_SHORT).show();
            }
            else {
                indoorMap.getMap().get(nRoom).addElement(element, orientation, capacity, open, wheelchair);
            }
        }
    }

    private void refreshRoomElementsView(){
        int nElem = indoorMap.getMap().get(nRoom).nElements();
        String elementsText = "";
        for(int i = 0; i < nElem; i++){
            elementsText += indoorMap.getMap().get(nRoom).get(i).getType() + ": " + indoorMap.getMap().get(nRoom).get(i).getOrientationString();

            if(i < nElem - 1) elementsText += "\n";
        }
        roomElementsView.setText(elementsText);
    }

    private void refreshRoomsView(){
        String roomsText = "";
        for(int i = 0; i <= nRoom; i++){
            roomsText += "Room " + i + ": " + indoorMap.getMap().get(i).nElements() + " elements";

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
        /*String[] elements = new String[map.get(nRoom).nElements()];
        List<Integer> connects = new ArrayList<Integer>();
        connects.add(nRoom);
        connects.add(nRoom + 1); //esto no tiene sentido

        for(int i = 0; i < map.get(nRoom).nElements(); i++){
            elements[i] = i + " " + map.get(nRoom).get(i).getType();
        }

        Log.d("roomConnectionAlert", elements.toString());*/

        roomConnectionAlert.create().show();
    }

    @SuppressLint("ResourceType")
    private void setInterface(){
        createLayout = findViewById(R.id.createLayout);

        resizeRoom = findViewById(R.id.prueba);

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
                        capacity = parseInt(capacityInput.getText().toString());
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
                roomConnectionAlert();

                nRoom++;
                indoorMap.addRoom();
                indoorMap.getMap().get(nRoom).setParameters(roomXInt, roomYInt);


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
                if(indoorMap.getMap().isEmpty()) Log.d("Map" , "Empty");
                else {
                    String mapString = "";
                    for (int i = 0; i < indoorMap.getMap().size(); ++i) {
                        mapString += indoorMap.getMap().get(i).toString() + "\n";
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

        resizeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                roomXInt = parseInt(roomXInput.getText().toString());
//                roomYInt = parseInt(roomYInput.getText().toString());
//                findViewById(R.id.roomView).getLayoutParams().width = roomXInt;
//                findViewById(R.id.roomView).getLayoutParams().height = roomYInt;

      //          System.out.print("x: " + roomXInt + "y: " + roomYInt);


                //New idea: Antes de crear esta vista, pedir las dimensiones y el nombre del room
                //Una vez tengamos estos inputs, creamos el dibujo del Room como NEW TextView (no forma parte del xml)
                //Si se le da a resize, borra este textView y crea uno nuevo.
            }
        });
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mapa", ArrayList<>(map);
    }*/
}