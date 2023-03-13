package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//TODO add removing ~ and : from text inputs
public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Button saveButton = (Button) findViewById(R.id.btnAddRoomSave);
        Button cancelButton = (Button) findViewById(R.id.btnAddRoomCancel);

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
                EditText roomColor = (EditText) findViewById(R.id.txtRoomColor);
                EditText roomTitle = (EditText) findViewById(R.id.txtRoomTitle);

                int width = 0;
                int length = 0;
                int height = 0;

                try {
                    width += Integer.parseInt(widthFT.getText().toString()) * 12;
                } catch (Exception e) {}
                try {
                    width += Integer.parseInt(widthInch.getText().toString());
                } catch (Exception e) {}

                try {
                    length += Integer.parseInt(lengthFT.getText().toString()) * 12;
                } catch (Exception e) {}
                try {
                    length += Integer.parseInt(lengthInch.getText().toString());
                } catch (Exception e) {}

                try {
                    height += Integer.parseInt(heightFT.getText().toString()) * 12;
                } catch (Exception e) {}

                 try {
                    height += Integer.parseInt(heightInch.getText().toString());
                } catch (Exception e) {}


                String color = roomColor.getText().toString();
                String title = roomTitle.getText().toString();

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

                    if (rooms.length()>0)
                    {
                        rooms = rooms + "~";
                    }
                    rooms = rooms + roomNumber + ":" + width + ":" + length + ":" + height + ":" + color.toUpperCase() + ":" + title + ", " + ", ";

                    editor.putString(messageKey, response);
                    editor.putString(roomsKey, rooms);
                    editor.putInt(roomsAmountKey, roomNumber);
                    editor.commit();

//                    Intent intent = new Intent();
//                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}