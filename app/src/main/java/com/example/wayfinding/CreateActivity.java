package com.example.wayfinding;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import mapComponents.Room;

public class CreateActivity extends AppCompatActivity {
    private IndoorMap indoorMap;
    private int nRoom = 0;
    private LinearLayout createLayout;
    private String element, tempName, tempWidth, tempLength;
    private ArrayList<String> orientationList;
    private int orientation, capacity, xCoordinate, yCoordinate;
    private boolean open, wheelchair;
    private Button mainMenuButton, showButton, newRoomButton, doorButton, stairsButton,
            elevatorButton, openButton, closeButton,
            addElementButton, newElementButton, editRoom, nextButton;
    private Spinner orientationSpinner;
    private CheckBox wheelchairCheckBox;
    private EditText capacityInput, coordXInput, coordYInput;
    private TextView roomElementsView, roomsView, currentRoom, spinnerPrompt, wheelchairPrompt, capacityPrompt, coordinatesPrompt, coordXPrompt, coordYPrompt;
    private AlertDialog.Builder roomConnectionAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOriginalContentView(true);

    }

    private void setOriginalContentView(Boolean firstTime) {

        //inflamos layout y creamos objeto enlazado q nos da acceso a Views definidos en el layout basic_room_inputs
        BasicRoomInputsBinding basicRoomInputsBinding = DataBindingUtil.setContentView(this, R.layout.basic_room_inputs);
        setContentView(basicRoomInputsBinding.getRoot()); //cleaner

        Room room = new Room(1);
        if(firstTime){
            room.setName("");
            room.setLength("15");
            room.setWidth("20");
        }
        else{
            room.setName(tempName);
            room.setLength(tempLength);
            room.setWidth(tempWidth);
        }

        basicRoomInputsBinding.setRoom(room);

        nextButton = basicRoomInputsBinding.next; //vs nextButton = findViewById(R.id.next), no parece q haya diferencia

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCreateBinding activityCreateBinding = DataBindingUtil.setContentView(CreateActivity.this, R.layout.activity_create);
                setContentView(activityCreateBinding.getRoot());

                ImageView roomView = findViewById(R.id.roomView);
                roomView.post(() -> {
                    String widthStr = room.getWidth();
                    tempWidth = widthStr;
                    Log.d(TAG, "Width string: " + widthStr);
                    float width = Float.parseFloat(widthStr) * 40; //el 40 por ejemplo
                    int pixelsW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                    Log.d(TAG, "Parsed width: " + pixelsW);

                    String lengthStr = room.getLength();
                    tempLength = lengthStr;
                    Log.d(TAG, "Length string: " + lengthStr);
                    float length = Float.parseFloat(lengthStr) * 40;
                    int pixelsL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, length, getResources().getDisplayMetrics());
                    Log.d(TAG, "Parsed width: " + pixelsL);

                    String nameStr = room.getName();
                    tempName = nameStr;
                    room.setName(nameStr);
                    Log.d(TAG, "Room name: " + nameStr);
                    currentRoom = findViewById(R.id.currentRoom);
                    currentRoom.setText(nameStr);

                    // hay que hacer exception handling para cuando las dimensiones se saldrían de la pantalla
                    //1 opcion es dividir por un numero y hacer escala mas pequeña de las mismas dimensiones

                    // Update the layout parametes & name of the room View
                    ViewGroup.LayoutParams roomLayoutParams = roomView.getLayoutParams();
                    roomLayoutParams.width = (int) width;
                    roomLayoutParams.height = (int) length;
                    roomView.setLayoutParams(roomLayoutParams);
                    room.setLength(String.valueOf(length));
                    room.setWidth(String.valueOf(width));

                    // Update the layout parameters of the grid overlay View
                    View gridOverlay = findViewById(R.id.gridOverlay);
                    ViewGroup.LayoutParams gridOverlayLayoutParams = gridOverlay.getLayoutParams();
                    gridOverlayLayoutParams.width = (int) width; // Set the width
                    gridOverlayLayoutParams.height = (int) length; // Set the height
                    gridOverlay.setLayoutParams(gridOverlayLayoutParams);
                    gridOverlay.setBackground(new GridDrawable(getResources().getColor(R.color.teal_700)));
                });

                initializeAttributes();
                setInterface();
                setDefaultValues();
                // setDefaultLayout();
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

        nRoom++;
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

//    @SuppressLint("ResourceType")
//    private void setDefaultLayout(){
//       // if(createLayout.findViewById(R.id.newDoor).getVisibility() != View.INVISIBLE) {
//         //   createLayout.removeView(doorButton); //id 1
//       // }
//        if(createLayout.findViewById(R.id.newStairs).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(stairsButton); //id 2
//        }
//        if(createLayout.findViewById(R.id.newElevator).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(elevatorButton);
//        }
//        if(createLayout.findViewById(R.id.spinner).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(orientationSpinner);
//        }
//        if(createLayout.findViewById(R.id.wcCheckBox).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(wheelchairCheckBox);
//        }
//        if(createLayout.findViewById(R.id.capacityInput).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(capacityInput);
//        }
//        if(createLayout.findViewById(R.id.coordXWidth).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(coordXInput);
//        }
//        if(createLayout.findViewById(R.id.coordYLength).getVisibility() != View.INVISIBLE) {
//            createLayout.removeView(coordYInput);
//        }
//    }

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
        editRoom = findViewById(R.id.resize);


        mainMenuButton = findViewById(R.id.mainMenu_button);
        showButton = findViewById(R.id.show_button);
        newRoomButton = findViewById(R.id.newRoom_button);
        addElementButton = findViewById(R.id.addElement_button);
     //   newElementButton = findViewById(R.id.newElement_button);

        roomElementsView = findViewById(R.id.roomElements);
        roomElementsView.setText("Room empty");
        roomsView = findViewById(R.id.rooms);
        roomsView.setText("Room 0: 0 elements");
       // currentRoom = findViewById(R.id.currentRoom);
       // refreshCurrentRoom();


        doorButton = findViewById(R.id.newDoor);
        //doorButton.findViewById(R.id.newDoor);
        //stairsButton = new Button(this);
        //elevatorButton = new Button(this);
        openButton = new Button(this);
        closeButton = new Button(this);

        //doorButton.setText("Door");
        //stairsButton.setText("Stairs");
       // elevatorButton.setText("Elevator");
        openButton.setText("Open");
        closeButton.setText("Closed");

////Element coordinates
        //text prompts
        coordinatesPrompt = new TextView(this);
        coordinatesPrompt = findViewById(R.id.coordinatesPrompt);
        coordXPrompt = new TextView(this);
        coordXPrompt = findViewById(R.id.coordXPrompt);
        coordYPrompt = new TextView(this);
        coordYPrompt = findViewById(R.id.coordYPrompt);

        //number input for WIDTH coordinate
        coordXInput = new EditText(this);
        coordXInput = findViewById(R.id.coordXWidth);
        //number input for LENGTH coordinate
        coordYInput = new EditText(this);
        coordYInput = findViewById(R.id.coordYLength);



////Element Orientation Spinner
        orientationSpinner = new Spinner(this);
        orientationSpinner = findViewById(R.id.spinner); //id 4
        spinnerPrompt = new TextView(this);
        spinnerPrompt = findViewById(R.id.spinnerPrompt);

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


//Wheelchair CheckBox
        wheelchairCheckBox = new CheckBox(this);
        wheelchairCheckBox = findViewById(R.id.wcCheckBox); //id 5
        wheelchairPrompt = new TextView(this);
        wheelchairPrompt = findViewById(R.id.wheelchairPrompt);

        wheelchairCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wheelchair = isChecked;
            }
        });

//// Elevator Capacity
        capacityInput = new EditText(this);
        capacityInput = findViewById(R.id.capacityInput); //6
        capacityPrompt = new TextView(this);
        capacityPrompt = findViewById(R.id.capacityPrompt);

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

//// DOOR BUTTON
        doorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button clicked");
                element = "door";

                if (coordinatesPrompt.getVisibility() == View.INVISIBLE){
                    coordinatesPrompt.setVisibility(View.VISIBLE);
                    coordXPrompt.setVisibility(View.VISIBLE);
                    coordXInput.setVisibility(View.VISIBLE);
                    coordYPrompt.setVisibility(View.VISIBLE);
                    coordYInput.setVisibility(View.VISIBLE);
                }

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerPrompt.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.INVISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.VISIBLE);
                    wheelchairPrompt.setVisibility(View.VISIBLE);
                }

            }
        });



        stairsButton = findViewById(R.id.newStairs);
        stairsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "stairs";

                if (coordinatesPrompt.getVisibility() == View.INVISIBLE){
                    coordinatesPrompt.setVisibility(View.VISIBLE);
                    coordXPrompt.setVisibility(View.VISIBLE);
                    coordXInput.setVisibility(View.VISIBLE);
                    coordYPrompt.setVisibility(View.VISIBLE);
                    coordYInput.setVisibility(View.VISIBLE);
                }

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerPrompt.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.INVISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.VISIBLE);
                    wheelchairPrompt.setVisibility(View.VISIBLE);
                }

                if (capacityInput.getVisibility() == View.VISIBLE){ //id 6
                    capacityInput.setVisibility(View.INVISIBLE);
                    capacityPrompt.setVisibility(View.INVISIBLE);
                }


            }
        });

        elevatorButton = findViewById(R.id.newElevator);
        elevatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "elevator";

                if (coordinatesPrompt.getVisibility() == View.INVISIBLE){
                    coordinatesPrompt.setVisibility(View.VISIBLE);
                    coordXPrompt.setVisibility(View.VISIBLE);
                    coordXInput.setVisibility(View.VISIBLE);
                    coordYPrompt.setVisibility(View.VISIBLE);
                    coordYInput.setVisibility(View.VISIBLE);
                }

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerPrompt.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.VISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.INVISIBLE);
                    wheelchairPrompt.setVisibility(View.INVISIBLE);
                }

                if (capacityInput.getVisibility() == View.INVISIBLE){ //id 6
                    capacityInput.setVisibility(View.VISIBLE);
                    capacityPrompt.setVisibility(View.VISIBLE);
                }
            }
        });


        addElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean parametersOk = true;

                //x & y
                try
                {
                    xCoordinate = parseInt(coordXInput.getText().toString());
                    yCoordinate = parseInt(coordYInput.getText().toString());
                    Log.d(TAG, "X: " + xCoordinate + " Y:" + yCoordinate);
                }
                catch (NumberFormatException nfe)
                {
                    Toast.makeText(CreateActivity.this, "X & Y should be numbers", Toast.LENGTH_SHORT).show();
                    parametersOk = false;
                }

                if(createLayout.findViewById(R.id.capacityInput).getVisibility() != View.INVISIBLE) {
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
                    //setDefaultLayout();
                }
            }
        });

        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomConnectionAlert();

                nRoom++;
                indoorMap.addRoom();
                //indoorMap.getMap().get(nRoom).setParameters(roomXInt, roomYInt);


                refreshRoomElementsView();
                refreshRoomsView();
                //refreshCurrentRoom();
                setDefaultValues();
                //setDefaultLayout();
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

        editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOriginalContentView(false);
            }
        });
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mapa", ArrayList<>(map);
    }*/
}