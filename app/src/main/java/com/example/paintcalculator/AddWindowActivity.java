package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    SharedPreferences sharedPref;
    Button selectColorButton;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_window);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

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
        roomSpinner.setSelection(roomsQuantity - 1);

        Button saveButton = (Button) findViewById(R.id.btnAddWindowSave);
        Button cancelButton = (Button) findViewById(R.id.btnAddWindowCancel);

        selectColorButton = (Button) findViewById(R.id.btnWindowTrimsColor);

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
                    String roomsKey = "ROOMS";
                    String messageKey = "MESSAGE";

                    editor.putString(messageKey, response);
                    editor.putString(roomsKey, roomsStringForShared);
                    editor.remove("SET_COLOR");
                    editor.commit();

                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddWindowActivity.this, ColorSelectorActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        color = sharedPref.getString("SET_COLOR", "");
        if (!color.equals("")) {
            Drawable buttonIcon = selectColorButton.getCompoundDrawables()[0];
            if (buttonIcon != null) {
                buttonIcon.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
                selectColorButton.setCompoundDrawables( null, buttonIcon, null, null);
            }
        }
    }
}