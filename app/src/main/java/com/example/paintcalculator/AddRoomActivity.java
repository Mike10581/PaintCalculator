package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button3);
        SharedPreferences.Editor editor = sharedPref.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("message", "My first message");
                editor.commit();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("message", "My second message");
                editor.commit();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}