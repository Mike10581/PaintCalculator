package com.example.paintcalculator;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //TODO remove reading all keys and values and read only needed
    SharedPreferences sharedPref;
    TextView textView;

    // Handle the result of DeleteConfirmationActivity
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        SharedPreferences.Editor editor = sharedPref.edit();
                        Map<String, ?> dataset = sharedPref.getAll();
                        for (Map.Entry<String, ?> entry : dataset.entrySet()) {
                            editor.remove(entry.getKey());
                        }
                        editor.commit();
                        textView.setText("");
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addRoom = (Button) findViewById(R.id.btnAddRoom);
        Button addWindow = (Button) findViewById(R.id.btnAddWindow);
        Button addDoor = (Button) findViewById(R.id.btnAddDoor);
        //TODO add checks for empty room list and block add windows and doors buttons
        Button clear = (Button) findViewById(R.id.btnClear);
        textView = findViewById(R.id.txtRoomsList);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRoomActivity.class));
            }
        });

        addWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddWindowActivity.class));
            }
        });

        addDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDoorActivity.class));
            }
        });


        Map<String, ?> dataset = sharedPref.getAll();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : dataset.entrySet()) {
            if (!entry.getKey().equals("MESSAGE")) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
            }
        }
        String store = sb.toString();

        textView.setText(store);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteConfirmationActivity.class);
                launcher.launch(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String message = sharedPref.getString("MESSAGE", "");
        Map<String, ?> dataset = sharedPref.getAll();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : dataset.entrySet()) {
            if (!entry.getKey().equals("MESSAGE")) {
                if (entry.getKey().equals("ROOMS")) {
                    String[] parsedRooms = entry.getValue().toString().split("~");

                    for (String room : parsedRooms) {
                        String[] parsedRoom = room.split(",");

                        String[] parsedInfo = parsedRoom[0].split(":");

                        sb.append(parsedInfo[0]).append(". ").append(parsedInfo[5]).append("\n");

                        int width = Integer.parseInt(parsedInfo[1]);
                        int length = Integer.parseInt(parsedInfo[2]);
                        int height = Integer.parseInt(parsedInfo[3]);

                        sb.append("  - Size: ");
                        sb.append(width / 12).append("' ").append(width % 12).append("\"W x ");
                        sb.append(length / 12).append("' ").append(length % 12).append("\"L x ");
                        sb.append(height / 12).append("' ").append(height % 12).append("\"H\n");
                        sb.append("  - Color: ").append(parsedInfo[4]).append("\n");

                        if (parsedRoom.length > 1) {
                            if (parsedRoom[1].length() > 1) {
                                sb.append("* Windows:").append("\n");

                                String[] window = parsedRoom[1].split("!");
                                for (String el : window) {
                                    String[] parsedWindowInfo = el.split(":");
                                    int windowWidth = Integer.parseInt(parsedWindowInfo[0]);
                                    int windowLength = Integer.parseInt(parsedWindowInfo[1]);
                                    int windowQuantity = Integer.parseInt(parsedWindowInfo[2]);
                                    int windowTrimsWidth = Integer.parseInt(parsedWindowInfo[3]);
                                    String windowTrimsColor = parsedWindowInfo[4];
                                    sb.append("  - Size: ");
                                    sb.append(windowWidth / 12).append("' ").append(windowWidth % 12).append("\"W x ");
                                    sb.append(windowLength / 12).append("' ").append(windowLength % 12).append("\"L - ");
                                    sb.append(windowQuantity).append("pcs.\n");
                                    sb.append("  - Trims: ");
                                    sb.append(windowTrimsWidth).append("\"W, color - ").append(windowTrimsColor).append("\n");

                                }
                            }

                            if (parsedRoom[2].length() > 1) {
                                sb.append("* Doors:").append("\n");

                                String[] door = parsedRoom[2].split("!");
                                for (String el : door) {
                                    String[] parsedDoorInfo = el.split(":");
                                    int doorWidth = Integer.parseInt(parsedDoorInfo[0]);
                                    int doorLength = Integer.parseInt(parsedDoorInfo[1]);
                                    int doorQuantity = Integer.parseInt(parsedDoorInfo[2]);
                                    int doorTrimsWidth = Integer.parseInt(parsedDoorInfo[3]);
                                    String doorTrimsColor = parsedDoorInfo[4];
                                    sb.append("  - Size: ");
                                    sb.append(doorWidth / 12).append("' ").append(doorWidth % 12).append("\"W x ");
                                    sb.append(doorLength / 12).append("' ").append(doorLength % 12).append("\"L - ");
                                    sb.append(doorQuantity).append("pcs.\n");
                                    sb.append("  - Trims: ");
                                    sb.append(doorTrimsWidth).append("\"W, color - ").append(doorTrimsColor).append("\n");
                                }
                            }
                        }

                        sb.append("\n");

                    }

                }

                sb.append("\n\n* - ").append(entry.getKey()).append("-> ").append(entry.getValue().toString()).append("\n\n");
            }

        }

        String store = sb.toString();

        textView.setText(store);
        if (!message.equals("")) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("MESSAGE");
            editor.commit();
        }
    }

}