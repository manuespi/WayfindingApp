package com.example.wayfinding;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.example.wayfinding.databinding.ActivityCreateBinding;
import com.example.wayfinding.databinding.BasicRoomInputsBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mapComponents.IndoorMap;
import mapComponents.Room;

public class CreateActivity extends AppCompatActivity {
    private IndoorMap indoorMap;
    private Canvas canvas;
    private int nRoom = 0;
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
    private EditText capacityInput;
    private TextView roomElementsView, roomsView, currentRoom, spinnerText, wheelchairText, capacityText;
    private AlertDialog.Builder roomConnectionAlert;
    public String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic_room_inputs);

        BasicRoomInputsBinding basicRoomInputsBinding = DataBindingUtil.setContentView(this, R.layout.basic_room_inputs);

        Room room = new Room(1);
        room.setName("");
        room.setLength("15");
        room.setWidth("20");
        basicRoomInputsBinding.setRoom(room);

        //nextButton = findViewById(R.id.next);
        nextButton = basicRoomInputsBinding.next;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCreateBinding activityCreateBinding = DataBindingUtil.setContentView(CreateActivity.this, R.layout.activity_create);


                ImageView roomView = findViewById(R.id.roomView);
                roomView.post(() -> {
                    String widthStr = room.getWidth();
                    Log.d(TAG, "Width string: " + widthStr);
                    float width = Float.parseFloat(widthStr) * 40; //el 40 por ejemplo
                    int pixelsW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                    Log.d(TAG, "Parsed width: " + pixelsW);

                    String lengthStr = room.getLength();
                    Log.d(TAG, "Length string: " + lengthStr);
                    float length = Float.parseFloat(lengthStr) * 40;
                    int pixelsL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, length, getResources().getDisplayMetrics());
                    Log.d(TAG, "Parsed width: " + pixelsL);

                    // hay que hacer exception handling para cuando las dimensiones se saldrían de la pantalla
                    //1 opcion es dividir por un numero y hacer escala mas pequeña de las mismas dimensiones

                    ViewGroup.LayoutParams layoutParams = roomView.getLayoutParams();
                    layoutParams.width = (int) width;
                    layoutParams.height = (int) length;
                    roomView.setLayoutParams(layoutParams);
                    room.setLength(String.valueOf(length));
                    room.setWidth(String.valueOf(width));
                });



                initializeAttributes();
                setInterface();
                setDefaultValues();
                setDefaultLayout();
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
        if(createLayout.findViewById(R.id.spinner).getVisibility() != View.INVISIBLE) {
            createLayout.removeView(orientationSpinner);
        }
        if(createLayout.findViewById(R.id.checkBox).getVisibility() != View.INVISIBLE) {
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
//        TextView roomTag = findViewById(R.id.roomName);
//        roomTag.setText(roomName);

        resizeRoom = findViewById(R.id.resize);

        mainMenuButton = findViewById(R.id.mainMenu_button);
        showButton = findViewById(R.id.show_button);
        newRoomButton = findViewById(R.id.newRoom_button);
        addElementButton = findViewById(R.id.addElement_button);
     //   newElementButton = findViewById(R.id.newElement_button);

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

        //doorButton.setText("Door");
        //stairsButton.setText("Stairs");
       // elevatorButton.setText("Elevator");
        openButton.setText("Open");
        closeButton.setText("Closed");


////Spinner
        orientationSpinner = new Spinner(this);
        orientationSpinner = findViewById(R.id.spinner);
        //orientationSpinner = setId(4);
        spinnerText = new TextView(this);
        spinnerText = findViewById(R.id.spinnerText);

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


//CheckBox
        wheelchairCheckBox = new CheckBox(this);
        wheelchairCheckBox = findViewById(R.id.checkBox);
        //wheelchairCheckBox.setId(5);
        wheelchairText = new TextView(this);
        wheelchairText = findViewById(R.id.checkBoxText);

        wheelchairCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wheelchair = isChecked;
            }
        });

//// EditText
        capacityInput = new EditText(this);
        capacityInput = findViewById(R.id.capacity); //6
        capacityText = new TextView(this);
        capacityText = findViewById(R.id.capacityText);

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

        doorButton.findViewById(R.id.newDoor);
        doorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "door";
              //  RoomElement doorObj = new RoomElement(10,10); //coordinate donde empieza
              //  doorObj.draw(canvas);

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerText.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.INVISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.VISIBLE);
                }

            }
        });



        stairsButton = findViewById(R.id.newStairs);
        stairsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "stairs";

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerText.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.INVISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.VISIBLE);
                    wheelchairText.setVisibility(View.VISIBLE);
                }

                if (capacityInput.getVisibility() == View.VISIBLE){ //id 6
                    capacityInput.setVisibility(View.INVISIBLE);
                    capacityText.setVisibility(View.INVISIBLE);
                }

            }
        });

        elevatorButton = findViewById(R.id.newElevator);
        elevatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "elevator";

                if (orientationSpinner.getVisibility() == View.INVISIBLE){ //id 4
                    orientationSpinner.setVisibility(View.VISIBLE);
                    spinnerText.setVisibility(View.VISIBLE);
                }
                if (wheelchairCheckBox.getVisibility() == View.VISIBLE){ //id 5
                    wheelchairCheckBox.setVisibility(View.INVISIBLE);
                    wheelchairText.setVisibility(View.INVISIBLE);
                }

                if (capacityInput.getVisibility() == View.INVISIBLE){ //id 6
                    capacityInput.setVisibility(View.VISIBLE);
                    capacityText.setVisibility(View.VISIBLE);
                }
            }
        });

        //se puede borrar
//        newElementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
////                if(createLayout.findViewById(1) == null) {
////                    createLayout.addView(doorButton);
////                }
//                if(createLayout.findViewById(2) == null) {
//                    createLayout.addView(stairsButton);
//                }
//                if(createLayout.findViewById(3) == null) {
//                    createLayout.addView(elevatorButton);
//                }
//            }
//        });

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
                //indoorMap.getMap().get(nRoom).setParameters(roomXInt, roomYInt);


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
                setContentView(R.layout.basic_room_inputs);
            }
        });
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mapa", ArrayList<>(map);
    }*/
}