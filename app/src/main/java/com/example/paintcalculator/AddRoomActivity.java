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

                int width = 0;
                if (!widthFT.getText().toString().equals("")) {
                    width += Integer.parseInt(widthFT.getText().toString()) * 12;
                }
                if (!widthInch.getText().toString().equals("")) {
                    width += Integer.parseInt(widthInch.getText().toString());
                }

                int length = 0;
                if (!lengthFT.getText().toString().equals("")) {
                    length += Integer.parseInt(lengthFT.getText().toString()) * 12;
                }
                if (!lengthInch.getText().toString().equals("")) {
                    length += Integer.parseInt(lengthInch.getText().toString());
                }

                int height = 0;
                if (!heightFT.getText().toString().equals("")) {
                    height += Integer.parseInt(heightFT.getText().toString()) * 12;
                }
                if (!heightInch.getText().toString().equals("")) {
                    height += Integer.parseInt(heightInch.getText().toString());
                }

                String color = roomColor.getText().toString();
                boolean error = color.equals("") || width == 0 || length == 0 || height == 0;

                if (error) {
                    Toast.makeText(AddRoomActivity.this, "ERROR!!! Missing data.", Toast.LENGTH_SHORT).show();
                } else {
                    String res = "Room " + width + "in X " + length + "in X " + height + "in, color: " + color + " was added.";

                    String areaKey = "COLOR:" + roomColor.getText().toString();
                    String ceilingKey = "CEILING";
                    String roomsAmountKey = "ROOMS_AMOUNT";

                    int area = sharedPref.getInt(areaKey, 0);
                    int ceiling = sharedPref.getInt(ceilingKey, 0);
                    int roomNumber = sharedPref.getInt(roomsAmountKey, 0) + 1;

                    area = area + (width + length) * height * 2;
                    ceiling = ceiling + width * length;










                    editor.putString("MESSAGE", res);
                    editor.putInt(areaKey, area);
                    editor.putInt(ceilingKey, ceiling);
                    editor.putInt(roomsAmountKey, roomNumber);
                    String roomKey = "ROOM:" + roomNumber;
                    String roomDescription = width + ":" + length + ":" + height + ":" + color;
                    editor.putString(roomKey, roomDescription);
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