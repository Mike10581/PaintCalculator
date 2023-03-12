package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWindowActivity extends AppCompatActivity {
    Spinner roomSpinner;
    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_window);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String rooms = sharedPref.getString("ROOMS", "");

        String[] parsedRooms = rooms.split("~");
        int roomsQuantity = parsedRooms.length;
        String[] roomsForSpinner = new String[roomsQuantity];

        for (int i = 0; i < roomsQuantity; i++) {

            String[] parsedRoom = parsedRooms[i].split(",");
            String[] parsedInfo = parsedRoom[0].split(":");

            StringBuilder sb = new StringBuilder();
            sb.append(parsedInfo[0]).append(". ").append(parsedInfo[5]).append(" (").append(parsedInfo[4]).append(")");
            String roomStringForSpinner = sb.toString();
            roomsForSpinner[i] = roomStringForSpinner;
        }

        roomSpinner = (Spinner) findViewById(R.id.txtExistingRoom);
        spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_rooms_list, roomsForSpinner);
        roomSpinner.setAdapter(spinnerArrayAdapter);

        Button saveButton = (Button) findViewById(R.id.btnAddWindowSave);
        Button cancelButton = (Button) findViewById(R.id.btnAddWindowCancel);

        SharedPreferences.Editor editor = sharedPref.edit();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText widthFT = (EditText) findViewById(R.id.txtWindowWidthFT);
                EditText widthInch = (EditText) findViewById(R.id.txtWindowWidthInch);
                EditText lengthFT = (EditText) findViewById(R.id.txtWindowLengthFT);
                EditText lengthInch = (EditText) findViewById(R.id.txtWindowLengthInch);
                EditText quantityPcs = (EditText) findViewById(R.id.txtWindowQuantity);

                EditText trimsWidthInch = (EditText) findViewById(R.id.txtWindowTrimsWidth);
                EditText trimsColor = (EditText) findViewById(R.id.txtWindowTrimsColor);

                int width = 0;
                int length = 0;
                int quantity = 0;
                int widthTrims = 0;

                try {
                    width += Integer.parseInt(widthFT.getText().toString()) * 12;
                } catch (Exception e) {
                }
                try {
                    width += Integer.parseInt(widthInch.getText().toString());
                } catch (Exception e) {
                }

                try {
                    length += Integer.parseInt(lengthFT.getText().toString()) * 12;
                } catch (Exception e) {
                }
                try {
                    length += Integer.parseInt(lengthInch.getText().toString());
                } catch (Exception e) {
                }

                try {
                    quantity += Integer.parseInt(quantityPcs.getText().toString());
                } catch (Exception e) {
                }

                try {
                    widthTrims += Integer.parseInt(trimsWidthInch.getText().toString());
                } catch (Exception e) {
                }

                String color = trimsColor.getText().toString();
                boolean error = color.equals("") || width == 0 || length == 0 || quantity == 0 || widthTrims == 0;
                int roomIndex = 0;
                try {
                    roomIndex = roomSpinner.getSelectedItemPosition();
                } catch (Exception e) {
                    error = true;
                }

                if (error) {
                    Toast.makeText(AddWindowActivity.this, "ERROR!!! Missing data.", Toast.LENGTH_SHORT).show();
                } else {
                    String response = "Window was added.";

                    StringBuilder roomsSB = new StringBuilder();
                    for (int i = 0; i < roomsQuantity; i++) {
                        if (i == roomIndex) {
                            //TODO add windows data
                            String[] parsedRoom = parsedRooms[i].split(",");
                            StringBuilder roomSB = new StringBuilder();
                            roomSB.append(parsedRoom[0]).append(","); //room info

                            if (!parsedRoom[1].equals(" ")) {
                                roomSB.append(parsedRoom[1]).append("!"); //room windows if have
                            }
                            roomSB.append(width).append(":").append(length).append(":").append(quantity).append(":").append(widthTrims).append(":").append(color.toUpperCase()).append(","); //room new window

                            roomSB.append(parsedRoom[2]); //room doors
                            String roomStringForRooms = roomSB.toString();
                            roomsSB.append(roomStringForRooms);
                        } else {
                            roomsSB.append(parsedRooms[i]);
                        }

                        if (i != roomsQuantity - 1) {
                            roomsSB.append("~");
                        }

                    }
                    String roomsStringForShared = roomsSB.toString();
                    //TODO save value as ROOMS


//                    String roomsAmountKey = "ROOMS_AMOUNT";
                    String roomsKey = "ROOMS";
                    String messageKey = "MESSAGE";

//                    int roomNumber = sharedPref.getInt(roomsAmountKey, 0) + 1;
//                    String rooms = sharedPref.getString(roomsKey, "");

//                    if (rooms.length()>0)
//                    {
//                        rooms = rooms + "~";
//                    }
//                    rooms = rooms + roomNumber + ":" + width + ":" + length + ":" + height + ":" + color.toUpperCase() + ":" + title + "," + ",";

                    editor.putString(messageKey, response);
                    editor.putString(roomsKey, roomsStringForShared);
//                    editor.putInt(roomsAmountKey, roomNumber);
                    editor.commit();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}