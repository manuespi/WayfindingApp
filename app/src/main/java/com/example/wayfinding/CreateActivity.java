package com.example.wayfinding;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wayfinding.databinding.ActivityCreateBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import mapComponents.Element;
import mapComponents.IndoorMap;
import mapComponents.Room;
import viewComponents.ElementListAdapter;

//manu
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;


public class CreateActivity extends AppCompatActivity implements ElementListAdapter.OnItemClickListener {
    //manu
    private IndoorMap indoorMap;
    private int nRoom;
    private Room room;
    private Gson gson;
    private boolean editingRoom, newMap;

    //mio
   // private int nRoom = 0;
    private LinearLayout createLayout;
    private RelativeLayout layout1;
    private String element, tempName, tempWidth, tempLength;
    private ArrayList<String> orientationList;
    private int orientation, capacity, xCoordinate, yCoordinate;
    private boolean open, wheelchair;
    private Button mainMenuButton, saveButton, doorButton, stairsButton,
            elevatorButton, openButton, closeButton,
            addElementButton, editRoom, nextButton, clearButton;
    private Spinner orientationSpinner;
    private CheckBox wheelchairCheckBox;
    private Element elem;
    private EditText capacityInput, coordXInput, coordYInput;
    private TextView roomElementsView, roomElementsCounter, currentRoom, spinnerPrompt, wheelchairPrompt,
            capacityPrompt, coordinatesPrompt, coordXPrompt, coordYPrompt, addElemHeader, editingHeader;
    private AlertDialog.Builder roomConnectionAlert;
    private RecyclerView elementsListRecyclerView;
    private ElementListAdapter adapter;
    private ArrayList<Element> elementList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOriginalContentView(true);
    }

    private void setOriginalContentView(Boolean firstTime) {

        if (!firstTime){
            room.setName(tempName);
            room.setLength(tempLength);
            room.setWidth(tempWidth);
        }

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
                    //room.setName(nameStr);
                    Log.d(TAG, "Room name: " + nameStr);
                    currentRoom = findViewById(R.id.currentRoom);
                    currentRoom.setText(nameStr);


                    // Update the layout parametes & name of the room View
                    ViewGroup.LayoutParams roomLayoutParams = roomView.getLayoutParams();
                    roomLayoutParams.width = (int) width;
                    roomLayoutParams.height = (int) length;
                    roomView.setLayoutParams(roomLayoutParams);
                   // room.setLength(lengthStr);
                   // room.setWidth(widthStr);

                    // Update the layout parameters of the grid overlay View
                    View gridOverlay = findViewById(R.id.gridOverlay);
                    ViewGroup.LayoutParams gridOverlayLayoutParams = gridOverlay.getLayoutParams();
                    gridOverlayLayoutParams.width = (int) width;
                    gridOverlayLayoutParams.height = (int) length;
                    gridOverlay.setLayoutParams(gridOverlayLayoutParams);
                    gridOverlay.setBackground(new GridDrawable(getResources().getColor(R.color.teal_700)));

                    //Update layout for the marker Container
                    RelativeLayout markerContainer = findViewById(R.id.markerContainer);
                    ViewGroup.LayoutParams markerContainerParams = markerContainer.getLayoutParams();
                    markerContainerParams.width = (int) width;
                    markerContainerParams.height = (int) length;
                    markerContainer.setLayoutParams(markerContainerParams);

                    TextView xGuide = findViewById(R.id.xGuide);
                    xGuide.setText(tempWidth + ", 0");
                    TextView yGuide = findViewById(R.id.yGuide);
                    yGuide.setText("0, " + tempLength);

                });

                initializeAttributes();
                setInterface();
                // setDefaultLayout();
            }
      //  });

   // }


    private void setDefaultValues(){
        element = "empty";
        capacity = 6;
        open = true;
        wheelchair = false;
    }

    private void initializeAttributes(){
        //Manu
        orientationList = new ArrayList<>();
        orientationList.add("North");
        orientationList.add("East");
        orientationList.add("South");
        orientationList.add("West");
        orientation = 0;
        roomConnectionAlert = new AlertDialog.Builder(this);
        gson = new Gson();
        this.indoorMap = new IndoorMap();

        Intent incomingIntent = getIntent();

        if(incomingIntent != null && incomingIntent.hasExtra("room")) {
            this.room = (Room) incomingIntent.getSerializableExtra("room");
            this.indoorMap = (IndoorMap) incomingIntent.getSerializableExtra("map");
            this.editingRoom =  incomingIntent.getBooleanExtra("new", false);
            //this.newMap = false;
            if(this.editingRoom)
            Toast.makeText(CreateActivity.this, "Editando Room", Toast.LENGTH_SHORT).show();
            else Toast.makeText(CreateActivity.this, "Nueva Room", Toast.LENGTH_SHORT).show();

        }
        else{
            Log.e("CreateActivity", "Algo ha ido mal al inicializar atributos.");
            Toast.makeText(CreateActivity.this, "Algo ha ido mal al inicializar atributos.", Toast.LENGTH_SHORT).show();
            this.editingRoom = false;
            this.newMap = false;
        }

        /*if(incomingIntent != null && incomingIntent.hasExtra("room")) {
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
        }*/

        setDefaultValues();
        setElementList();

        this.elementsListRecyclerView = findViewById(R.id.elem_recyclerview);
        this.adapter = new ElementListAdapter(this, this.elementList);
        this.adapter.setOnItemClickListener(this);
        this.elementsListRecyclerView.setAdapter(this.adapter);
        this.elementsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*roomElementsView = findViewById(R.id.roomElements);
        roomElementsView.setText("Room empty");*/
        roomElementsCounter = findViewById(R.id.elemCount);
        roomElementsCounter.setText("0 ELEMENTS");
        refreshRoomElementsView();
    }

    private void setElementList(){
        this.elementList = new ArrayList<Element>();
        for (int i = 0; i < this.room.getnElements(); ++i){
            this.elementList.add(this.room.getElement(i));
        }
    }

    private void updateAdapter(){
        adapter.notifyDataSetChanged();
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

    //Manu
    private void openMapSelectionActivity(){
        Intent intent = new Intent(this, MapSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRoomSelectionActivity(){
        Intent intent = new Intent(CreateActivity.this, RoomSelectionActivity.class);
        if(indoorMap == null) Log.d("null", "IM is null: ");
        else Log.d("no null", "IM is not null: "+indoorMap.getnRoom());
        intent.putExtra("IMmap", indoorMap);
        startActivity(intent);
        finish();
    }

    //Manu
    private void addElementToRoom(){
        if(element == "empty"){
            Toast.makeText(CreateActivity.this, "No element selected", Toast.LENGTH_SHORT).show();
        }
        else {
            //indoorMap.addElementToRoom(nRoom, element, orientation, capacity, open, wheelchair);
            elem = this.room.addElement(element, orientation, capacity, open, wheelchair, xCoordinate, yCoordinate);
            elementList.add(elem);
        }
        
        drawElement(elem);
    }



    private class MarkerView extends AppCompatImageView {
        public MarkerView(Context context, Element element) {
            super(context);
            if (element.getType() == "door"){
                setImageResource(R.drawable.door);
            }
            else if (element.getType() == "stairs"){
                setImageResource(R.drawable.stairs);

            }
            else if ( element.getType() == "elevator"){
                setImageResource(R.drawable.elevator);
            }
        }

    }

    private void drawElement(Element element) {

        MarkerView markerView = new MarkerView(CreateActivity.this, element);
        int markerViewWidth = 100;
        int markerViewHeight = 100;
        RelativeLayout.LayoutParams markerLayoutParams = new RelativeLayout.LayoutParams(markerViewWidth, markerViewHeight);
        markerView.setLayoutParams(markerLayoutParams);
        RelativeLayout markerContainer = findViewById(R.id.markerContainer);


        int markerLeft = markerLayoutParams.leftMargin; //represents the X-coordinate
        int markerTop = markerLayoutParams.topMargin;  //represents the Y-coordinate

        if(element.orientationString() == "south" && (yCoordinate == Integer.parseInt(tempLength))){
            yCoordinate = yCoordinate - 2;
        }
        else if(element.orientationString() == "east" && (xCoordinate == Integer.parseInt(tempWidth))){
            xCoordinate = xCoordinate - 2;
        }


        float markerXfloat = xCoordinate * 40;
        float markerYfloat = yCoordinate * 40;

        markerView.setX(markerLeft + markerXfloat);
        markerView.setY(markerTop + markerYfloat);

        Log.d(TAG, "X : " + markerLayoutParams.leftMargin);
        Log.d(TAG, "Y : " +  markerLayoutParams.bottomMargin);
        markerContainer.addView(markerView);



    }


    //Manu
    private void refreshRoomElementsView(){
        //int nElem = indoorMap.getRoom(nRoom).getnElements();
        Log.d("refreshRoomElementsView", room.toString());
        int nElem = this.room.getnElements();
        String elementsText = "";
        for(int i = 0; i < nElem; i++){
            //elementsText += indoorMap.getRoom(nRoom).getElement(i).getType() + ": " + indoorMap.getRoom(nRoom).getElement(i).orientationString();
            elementsText += this.room.getElement(i).getType() + ": " + this.room.getElement(i).orientationString();Log.d("refresh", "Entra");

            if(i < nElem - 1) elementsText += "\n";
        }
        //roomElementsView.setText(elementsText);
        roomElementsCounter.setText(room.getnElements() + " elements");
    }


    private void refreshRoomsView(){
        String roomsText = "";
        for(int i = 0; i <= nRoom; i++){
            roomsText += "Room " + i + ": " + indoorMap.getRoom(i).getnElements() + " elements";

            if(i < nRoom) roomsText += "\n";
        }
        roomElementsCounter.setText(roomsText);
    }


    private void roomConnectionAlert() {
        roomConnectionAlert.create().show();
    }

    @SuppressLint("ResourceType")
    private void setInterface(){
        createLayout = findViewById(R.id.createLayout);
        layout1 = findViewById(R.id.layout1);
        editRoom = findViewById(R.id.resize);


        mainMenuButton = findViewById(R.id.mainMenu_button);
        saveButton = findViewById(R.id.save_button);
        addElementButton = findViewById(R.id.addElement_button);
        clearButton = findViewById(R.id.clear_button);

        doorButton = findViewById(R.id.newDoor);
        openButton = new Button(this);
        closeButton = new Button(this);
        openButton.setText("Open");
        closeButton.setText("Closed");

////Element coordinates

        coordinatesPrompt = findViewById(R.id.coordinatesPrompt);
        coordXPrompt = findViewById(R.id.coordXPrompt);
        coordYPrompt = findViewById(R.id.coordYPrompt);
        addElemHeader = findViewById(R.id.addElementHeader);
        editingHeader = findViewById(R.id.editingHeader);
        //number input for WIDTH coordinate
        coordXInput = findViewById(R.id.coordXWidth);
        //number input for LENGTH coordinate
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
                editingHeader.setText("DOOR");

                if (addElemHeader.getVisibility() == View.VISIBLE){
                    addElemHeader.setVisibility(View.INVISIBLE);
                    editingHeader.setVisibility(View.VISIBLE);
                    addElementButton.setVisibility(View.VISIBLE);
                }

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



        stairsButton = findViewById(R.id.newStairs);
        stairsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element = "stairs";
                editingHeader.setText("STAIRS");

                if (addElemHeader.getVisibility() == View.VISIBLE){
                    addElemHeader.setVisibility(View.INVISIBLE);
                    editingHeader.setVisibility(View.VISIBLE);
                    addElementButton.setVisibility(View.VISIBLE);
                }

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
                editingHeader.setText("ELEVATOR");

                if (addElemHeader.getVisibility() == View.VISIBLE){
                    addElemHeader.setVisibility(View.INVISIBLE);
                    editingHeader.setVisibility(View.VISIBLE);
                    addElementButton.setVisibility(View.VISIBLE);
                }

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
                try {
                    xCoordinate = Integer.parseInt(coordXInput.getText().toString());
                    yCoordinate = Integer.parseInt(coordYInput.getText().toString());

                    //que un elemento solamente se pueda pinta en una pared y no en medio de la habitacion5
                    boolean LeftOrRightIsaWall = false;
                    boolean TopOrBottomIsAWall = false;
                    if(xCoordinate != 0 && xCoordinate !=Integer.parseInt(tempWidth)){
                        //x is random number, so y is either top wall or bottom wall
                        if(yCoordinate == 0 || yCoordinate == Integer.parseInt(tempLength)){
                            TopOrBottomIsAWall = true;
                        }
                    }

                    if(yCoordinate != 0 && xCoordinate !=Integer.parseInt(tempLength)){
                        //y is random number, so x is either left wall or right wall
                        if(xCoordinate == 0 || xCoordinate == Integer.parseInt(tempWidth)){
                            LeftOrRightIsaWall = true;
                        }
                    }

                    if (!TopOrBottomIsAWall && !LeftOrRightIsaWall) {
                        Toast.makeText(CreateActivity.this, "The element should be on a wall!", Toast.LENGTH_SHORT).show();
                        parametersOk = false;
                    }
                    else {

                        parametersOk = true;
                        Log.d(TAG, "X: " + xCoordinate + " Y: " + yCoordinate);
                    }
                } catch (NumberFormatException nfe) {
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
                    setDefaultValues();
                    //setDefaultLayout();
                    //setElementList();
                    adapter.notifyItemInserted(elementList.size()-1);

                    //para cuando se añada un elemento se restaure el panel de editing y esté vacío
                    //en un futuro podriamos meter esto en setDefaultLayout() o en un metodo aparte
                    if (editingHeader.getVisibility() == View.VISIBLE){
                        editingHeader.setVisibility(View.INVISIBLE);
                        addElemHeader.setVisibility(View.VISIBLE);
                        addElementButton.setVisibility(View.INVISIBLE);


                        coordinatesPrompt.setVisibility(View.INVISIBLE);
                        coordXPrompt.setVisibility(View.INVISIBLE);
                        coordXInput.setVisibility(View.INVISIBLE);
                        coordYPrompt.setVisibility(View.INVISIBLE);
                        coordYInput.setVisibility(View.INVISIBLE);

                        orientationSpinner.setVisibility(View.INVISIBLE);
                        spinnerPrompt.setVisibility(View.INVISIBLE);

                        wheelchairCheckBox.setVisibility(View.INVISIBLE);
                        wheelchairPrompt.setVisibility(View.INVISIBLE);

                        capacityInput.setVisibility(View.INVISIBLE);
                        capacityPrompt.setVisibility(View.INVISIBLE);
                    }



                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editingRoom){
                    ArrayList<Room> auxMap = indoorMap.getMap();

                    for(int i = 0; i < auxMap.size(); ++i)
                        if(auxMap.get(i).getId() == room.getId())
                            auxMap.set(i, room);

                    indoorMap.setMap(auxMap);
                }
                else{
                    indoorMap.addRoom(room);
                }

                saveMap();
                openRoomSelectionActivity();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.delete_confirm_popup, null);

                Button delete = popupView.findViewById(R.id.confirm_button);
                Button cancel = popupView.findViewById(R.id.cancel_button);

                AlertDialog dialog = new AlertDialog.Builder(CreateActivity.this)
                        .setView(popupView)
                        .create();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {Toast.makeText(CreateActivity.this, "DELETE CLEAR", Toast.LENGTH_SHORT).show();
                        int id = room.getId();
                        String name = room.getName();
                        String w = room.getWidth();
                        String l = room.getLength();
                        room = new Room(id);
                        room.setName(name);
                        room.setWidth(w);
                        room.setLength(l);
                        elementList.clear();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        /*

                        setElementList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });*/
                        refreshRoomElementsView();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoomSelectionActivity();
            }
        });

        editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOriginalContentView(false);
            }
        });
    }

    public void saveMap(){
        File file = new File(this.getFilesDir(), this.indoorMap.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();

        /*if(newMap) {
            Log.d("SaveMap", "NEW MAP");//Para comprobar que el nuevo mapa es nombre único TODO mejorar
            if (file.exists())
                file = new File(this.getFilesDir(), this.indoorMap.getName() + "_copy.json");
        }*/

        try {
            String json = objectMapper.writeValueAsString(this.indoorMap);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteElement(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_confirm_popup, null);

        Button delete = popupView.findViewById(R.id.confirm_button);
        Button cancel = popupView.findViewById(R.id.cancel_button);

        AlertDialog dialog = new AlertDialog.Builder(CreateActivity.this)
                .setView(popupView)
                .create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = elementList.get(position).getId();
                boolean aux = room.removeById(id);
                elementList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, elementList.size());
                dialog.dismiss();
                //room.setnElements(room.getnElements() -1);
                refreshRoomElementsView();
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
}