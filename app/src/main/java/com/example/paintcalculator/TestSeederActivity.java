package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class TestSeederActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_seeder);

        Button deleteButton = (Button) findViewById(R.id.btnSeed);
        Button cancelButton = (Button) findViewById(R.id.btnSeedCancel);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        String testRoomsData = "1:281:392:100:#F3EBD3:Kitchen,53:65:3:4:#EDEBEB!53:105:1:4:#EDEBEB,80:53:1:6:#3D2C10!80:40:2:4:#EDEBEB~2:324:415:100:#FFCC71:Living room,100:76:3:4:#EDEBEB,80:53:2:4:#EDEBEB!80:100:1:6:#3D2C10~3:265:315:100:#FFDAFF:Bedroom (master),53:105:2:4:#EDEBEB,80:40:2:4:#EDEBEB~4:175:112:100:#593542:Bathroom (master),53:105:1:4:#EDEBEB,80:40:1:4:#EDEBEB~5:208:263:100:#655BFF:Bedroom (John),53:105:2:4:#EDEBEB,80:40:1:4:#EDEBEB~6:224:245:100:#9170AD:Bedroom (Kenny),53:105:2:4:#EDEBEB,80:40:1:4:#EDEBEB~7:104:77:100:#475B72:Bathroom,53:45:1:4:#EDEBEB,80:40:1:4:#EDEBEB~8:404:57:100:#475B72:Hallway, ,80:40:3:4:#EDEBEB!80:53:1:4:#EDEBEB";

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ROOMS", testRoomsData);
                editor.putInt("ROOMS_AMOUNT", 8);
                editor.commit();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}