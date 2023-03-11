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

    SharedPreferences sharedPref;
    TextView textView;
//    SharedPreferences.Editor editor = sharedPref.edit();
    // Handle the result of AddRoomActivity
    private ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
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
                                sb.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
                            }

                        }
                        String store = sb.toString();

                        textView.setText(store);

//                        ListView listView = (ListView) findViewById(R.id.lstView);
//                        listView.setEnt
//                        entries.addAll(preferences.getAll().entrySet());


                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addRoom = (Button) findViewById(R.id.btnAddRoom);
        Button clear = (Button) findViewById(R.id.btnClear);
        textView = findViewById(R.id.txtRoomsList);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
                addRoomLauncher.launch(intent);
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