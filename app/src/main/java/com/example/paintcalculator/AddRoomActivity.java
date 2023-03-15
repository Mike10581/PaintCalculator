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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRoomActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    Button selectColorButton;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Button saveButton = (Button) findViewById(R.id.btnAddRoomSave);
        Button cancelButton = (Button) findViewById(R.id.btnAddRoomCancel);

        selectColorButton = (Button) findViewById(R.id.btnSelectColor);

        SharedPreferences.Editor editor = sharedPref.edit();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText widthFT = (EditText) findViewById(R.id.txtRoomWidthFT);
                EditText widthInch = (EditText) findViewById(R.id.txtRoomWidthInch);
                EditText lengthFT = (EditText) findViewById(R.id.txtRoomLengthFT);
                EditText lengthInch = (EditText) findViewById(R.id.txtRoomLengthInch);
                EditText heightFT = (EditText) findViewById(R.id.txtRoomHeightFT);
                EditText heightInch = (EditText) findViewById(R.id.txtRoomHeightInch);
                EditText roomTitle = (EditText) findViewById(R.id.txtRoomTitle);

                int width = 0;
                int length = 0;
                int height = 0;

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
                    height += Integer.parseInt(heightFT.getText().toString()) * 12;
                } catch (Exception e) {
                }

                try {
                    height += Integer.parseInt(heightInch.getText().toString());
                } catch (Exception e) {
                }

                String title = roomTitle.getText().toString().replaceAll("[~!:,]", "");

                boolean error = color.equals("") || width == 0 || length == 0 || height == 0 || title.length() == 0;

                if (error) {
                    Toast.makeText(AddRoomActivity.this, "ERROR!!! Missing data.", Toast.LENGTH_SHORT).show();
                } else {
                    String response = "Room \"" + title + "\" in color: " + color + " was added.";

                    String roomsAmountKey = "ROOMS_AMOUNT";
                    String roomsKey = "ROOMS";
                    String messageKey = "MESSAGE";

                    int roomNumber = sharedPref.getInt(roomsAmountKey, 0) + 1;
                    String rooms = sharedPref.getString(roomsKey, "");

                    if (rooms.length() > 0) {
                        rooms = rooms + "~";
                    }

                    rooms = rooms + roomNumber + ":" + width + ":" + length + ":" + height + ":" + color + ":" + title + ", " + ", ";

                    editor.putString(messageKey, response);
                    editor.putString(roomsKey, rooms);
                    editor.putInt(roomsAmountKey, roomNumber);
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
                startActivity(new Intent(AddRoomActivity.this, ColorSelectorActivity.class));
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