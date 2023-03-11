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

    // Handle the result of AddRoomActivity, AddWindowActivity,
    private ActivityResultLauncher<Intent> addDataLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

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

                                        sb.append("- Size: ");
                                        sb.append(width / 12).append("' ").append(width % 12).append("\"W x ");
                                        sb.append(length / 12).append("' ").append(length % 12).append("\"L x ");
                                        sb.append(height / 12).append("' ").append(height % 12).append("\"H\n");
                                        sb.append("- Color: ").append(parsedInfo[4]).append("\n");

                                        if (parsedRoom.length > 1) {
                                            if (parsedRoom[1].length() > 0) {
                                                sb.append("Windows->").append(parsedRoom[1]).append("<-\n");
//                                            String[] parsedWindows = parsedRoom[1].split("!");
                                            }

                                            if (parsedRoom[2].length() > 0) {
                                                sb.append("Doors->").append(parsedRoom[2]).append("<-\n");
//                                            String[] parsedDoors = parsedRoom[2].split("!");
                                            }
                                        }





                                    }

                                }
//                                String[] parsedKey = entry.getKey().split(":");
//                                if (parsedKey[0].equals("COLOR")) {
//                                    sb.append(parsedKey[0]).append(" - ").append(parsedKey[1]).append("\n\n");
//                                } else if (parsedKey[0].equals("ROOM")) {
//                                    String[] parsedValue = entry.getValue().toString().split(":");
//                                    sb.append(parsedKey[0]).append(" - ").append(parsedKey[1]).append("\n")
//                                            .append("- ").append(parsedValue[0]).append("W x ")
//                                            .append(parsedValue[1]).append("L x ")
//                                            .append(parsedValue[2]).append("H (inch)\n").append("- color: ").append(parsedValue[3]).append("\n\n");
//                                } else {
//                                    sb.append("! - ").append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n\n");
//                                }
//
                                sb.append("* - ").append(entry.getKey()).append("-> ").append(entry.getValue().toString()).append("\n\n");
                            }

                        }
                        String store = sb.toString();

                        textView.setText(store);

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addRoom = (Button) findViewById(R.id.btnAddRoom);
        Button addWindow = (Button) findViewById(R.id.btnAddWindow);
        Button clear = (Button) findViewById(R.id.btnClear);
        textView = findViewById(R.id.txtRoomsList);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
                addDataLauncher.launch(intent);
            }
        });

        addWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWindowActivity.class);
                addDataLauncher.launch(intent);
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
                SharedPreferences.Editor editor = sharedPref.edit();
                Map<String, ?> dataset = sharedPref.getAll();
                for (Map.Entry<String, ?> entry : dataset.entrySet()) {
                    editor.remove(entry.getKey());
                }
                editor.commit();
                textView.setText("");
            }
        });
    }

}