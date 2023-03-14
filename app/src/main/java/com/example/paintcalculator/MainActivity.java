package com.example.paintcalculator;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //TODO remove reading all keys and values and read only needed
    SharedPreferences sharedPref;


    private TableLayout tblRoomsLayout;

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
                        tblRoomsLayout.removeAllViews();
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

        tblRoomsLayout = (TableLayout) findViewById(R.id.tblRoomsLayout);
        tblRoomsLayout.setStretchAllColumns(true);

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

        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int bigTextSize = 70, smallTextSize = 45, mediumTextSize = 55;

        String titleBackgroundColor = "#FF6200EE";
        String titleTextColor = "#FFFFFFFF";
        String descriptionTitleBackgroundColor = "#220000ff";
        String roomsAddonsBackgroundColor = "#33ff0000";

        tblRoomsLayout.removeAllViews();

        //TODO hide Windows and Doors titles if data are empty or add line that going to show text that section is empty
        if (!sharedPref.getString("ROOMS", "").equals("")) {
            String[] parsedRooms = sharedPref.getString("ROOMS", "").split("~");

            int roomsQuantity = parsedRooms.length;

            for (int i = 0; i < roomsQuantity; i++) {
                String[] parsedRoom = parsedRooms[i].split(",");
                String[] parsedInfo = parsedRoom[0].split(":");


                // create table for room title
                TableLayout roomTitleLayout = new TableLayout(this);
                roomTitleLayout.setStretchAllColumns(true);
                roomTitleLayout.removeAllViews();

                // create cell
                final TextView tvRoomTitle = new TextView(this);
                tvRoomTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomTitle.setGravity(Gravity.LEFT);
                tvRoomTitle.setPadding(10, 20, 10, 20);
                tvRoomTitle.setBackgroundColor(Color.parseColor(titleBackgroundColor));
                tvRoomTitle.setTextColor(Color.parseColor(titleTextColor));
                tvRoomTitle.setTypeface(null, Typeface.BOLD);
                tvRoomTitle.setText(String.valueOf(parsedInfo[0] + ". " + parsedInfo[5]));
                tvRoomTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, bigTextSize);

                // create row
                final TableRow trRoomTitle = new TableRow(this);
                trRoomTitle.setId(i + 1);
                TableLayout.LayoutParams trRoomTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trRoomTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trRoomTitle.setPadding(0, 0, 0, 0);
                trRoomTitle.setLayoutParams(trRoomTitleParams);

                // add cell to row
                trRoomTitle.addView(tvRoomTitle);

                // add row to table
                roomTitleLayout.addView(trRoomTitle, trRoomTitleParams);


                // create table for room info (size and color)
                TableLayout roomInfoLayout = new TableLayout(this);
                roomInfoLayout.setStretchAllColumns(true);
                roomInfoLayout.removeAllViews();

                // create cells
                final TextView tvRoomWidthTitle = new TextView(this);
                tvRoomWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomWidthTitle.setGravity(Gravity.RIGHT);
                tvRoomWidthTitle.setPadding(10, 20, 10, 20);
                tvRoomWidthTitle.setTypeface(null, Typeface.BOLD);
                tvRoomWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomWidthTitle.setText("Width");

                final TextView tvRoomLengthTitle = new TextView(this);
                tvRoomLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomLengthTitle.setGravity(Gravity.RIGHT);
                tvRoomLengthTitle.setPadding(10, 20, 10, 20);
                tvRoomLengthTitle.setTypeface(null, Typeface.BOLD);
                tvRoomLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomLengthTitle.setText("Length");

                final TextView tvRoomHeightTitle = new TextView(this);
                tvRoomHeightTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomHeightTitle.setGravity(Gravity.RIGHT);
                tvRoomHeightTitle.setPadding(10, 20, 10, 20);
                tvRoomHeightTitle.setTypeface(null, Typeface.BOLD);
                tvRoomHeightTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomHeightTitle.setText("Height");

                final TextView tvRoomColorTitle = new TextView(this);
                tvRoomColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomColorTitle.setGravity(Gravity.RIGHT);
                tvRoomColorTitle.setPadding(10, 20, 10, 20);
                tvRoomColorTitle.setTypeface(null, Typeface.BOLD);
                tvRoomColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomColorTitle.setText("Color");

                // create row
                final TableRow trRoomInfoTitle = new TableRow(this);
//                        trRoomInfoTitle.setId(i + 1);
                TableLayout.LayoutParams trRoomInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trRoomInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trRoomInfoTitle.setPadding(0, 0, 0, 0);
                trRoomInfoTitle.setBackgroundColor(Color.parseColor(descriptionTitleBackgroundColor));
                trRoomInfoTitle.setLayoutParams(trRoomInfoParamsTitle);

                // add cells to row
                trRoomInfoTitle.addView(tvRoomWidthTitle);
                trRoomInfoTitle.addView(tvRoomLengthTitle);
                trRoomInfoTitle.addView(tvRoomHeightTitle);
                trRoomInfoTitle.addView(tvRoomColorTitle);

                // add row to table
                roomInfoLayout.addView(trRoomInfoTitle, trRoomInfoParamsTitle);


                int width = Integer.parseInt(parsedInfo[1]);
                int length = Integer.parseInt(parsedInfo[2]);
                int height = Integer.parseInt(parsedInfo[3]);

                // create cells
                final TextView tvRoomWidth = new TextView(this);
                tvRoomWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomWidth.setGravity(Gravity.RIGHT);
                tvRoomWidth.setPadding(10, 20, 10, 20);
                tvRoomWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomWidth.setText(String.valueOf(width / 12 + "' " + width % 12 + "\""));

                final TextView tvRoomLength = new TextView(this);
                tvRoomLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomLength.setGravity(Gravity.RIGHT);
                tvRoomLength.setPadding(10, 20, 10, 20);
                tvRoomLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomLength.setText(String.valueOf(length / 12 + "' " + length % 12 + "\""));

                final TextView tvRoomHeight = new TextView(this);
                tvRoomHeight.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomHeight.setGravity(Gravity.RIGHT);
                tvRoomHeight.setPadding(10, 20, 10, 20);
                tvRoomHeight.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomHeight.setText(String.valueOf(height / 12 + "' " + height % 12 + "\""));

                final TextView tvRoomColor = new TextView(this);
                tvRoomColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvRoomColor.setGravity(Gravity.RIGHT);
                tvRoomColor.setPadding(10, 20, 10, 20);
                tvRoomColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tvRoomColor.setText(String.valueOf(parsedInfo[4]));

                // create row
                final TableRow trRoomInfo = new TableRow(this);
//                        trRoomInfo.setId(i + 1);
                TableLayout.LayoutParams trRoomInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trRoomInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trRoomInfo.setPadding(0, 0, 0, 0);
                trRoomInfo.setLayoutParams(trRoomInfoParams);

                // add cells to row
                trRoomInfo.addView(tvRoomWidth);
                trRoomInfo.addView(tvRoomLength);
                trRoomInfo.addView(tvRoomHeight);
                trRoomInfo.addView(tvRoomColor);

                // add row to table
                roomInfoLayout.addView(trRoomInfo, trRoomInfoParams);

//                        if (i > -1) {
//                            tr.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    TableRow tr = (TableRow) v;
//                                }
//                            });


                // create table for windows title
                TableLayout windowTitleLayout = new TableLayout(this);
                windowTitleLayout.setStretchAllColumns(true);
                windowTitleLayout.removeAllViews();

                // create cell
                final TextView tvWindowTitle = new TextView(this);
                tvWindowTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvWindowTitle.setGravity(Gravity.LEFT);
                tvWindowTitle.setPadding(10, 10, 10, 5);
                tvWindowTitle.setBackgroundColor(Color.parseColor(roomsAddonsBackgroundColor));
                tvWindowTitle.setTypeface(null, Typeface.BOLD);
                tvWindowTitle.setText("Windows:");
                tvWindowTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

                // create row
                final TableRow trWindowTitle = new TableRow(this);
//                        trWindowTitle.setId(i + 1);
                TableLayout.LayoutParams trWindowTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trWindowTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trWindowTitle.setPadding(0, 0, 0, 0);
                trWindowTitle.setLayoutParams(trWindowTitleParams);

                // add cell to row
                trWindowTitle.addView(tvWindowTitle);

                // add row to table
                windowTitleLayout.addView(trWindowTitle, trWindowTitleParams);

                // create table for windows info (size, trims width and color)
                TableLayout windowInfoLayout = new TableLayout(this);
                windowInfoLayout.setStretchAllColumns(true);
                windowInfoLayout.removeAllViews();


                //create table for doors title
                TableLayout doorTitleLayout = new TableLayout(this);
                doorTitleLayout.setStretchAllColumns(true);
                doorTitleLayout.removeAllViews();

                // create cell
                final TextView tvDoorTitle = new TextView(this);
                tvDoorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvDoorTitle.setGravity(Gravity.LEFT);
                tvDoorTitle.setPadding(10, 10, 10, 5);
                tvDoorTitle.setBackgroundColor(Color.parseColor(roomsAddonsBackgroundColor));
                tvDoorTitle.setTypeface(null, Typeface.BOLD);
                tvDoorTitle.setText("Doors:");
                tvDoorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

                // create row
                final TableRow trDoorTitle = new TableRow(this);
//                        trDoorTitle.setId(i + 1);
                TableLayout.LayoutParams trDoorTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trDoorTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trDoorTitle.setPadding(0, 0, 0, 0);
                trDoorTitle.setLayoutParams(trDoorTitleParams);

                // add cell to row
                trDoorTitle.addView(tvDoorTitle);

                // add row to table
                doorTitleLayout.addView(trDoorTitle, trDoorTitleParams);

                // create table for doors info (size, trims width and color)
                TableLayout doorInfoLayout = new TableLayout(this);
                doorInfoLayout.setStretchAllColumns(true);
                doorInfoLayout.removeAllViews();

                if (parsedRoom.length > 1) {
                    if (parsedRoom[1].length() > 1) {

                        // create cells
                        final TextView tvWindowWidthTitle = new TextView(this);
                        tvWindowWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowWidthTitle.setGravity(Gravity.RIGHT);
                        tvWindowWidthTitle.setPadding(10, 20, 10, 20);
                        tvWindowWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowWidthTitle.setText("Width");

                        final TextView tvWindowLengthTitle = new TextView(this);
                        tvWindowLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowLengthTitle.setGravity(Gravity.RIGHT);
                        tvWindowLengthTitle.setPadding(10, 20, 10, 20);
                        tvWindowLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowLengthTitle.setText("Length");

                        final TextView tvWindowQuantityTitle = new TextView(this);
                        tvWindowQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowQuantityTitle.setGravity(Gravity.RIGHT);
                        tvWindowQuantityTitle.setPadding(10, 20, 10, 20);
                        tvWindowQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowQuantityTitle.setText("Quantity");

                        final TextView tvWindowTrimColorTitle = new TextView(this);
                        tvWindowTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowTrimColorTitle.setGravity(Gravity.RIGHT);
                        tvWindowTrimColorTitle.setPadding(10, 20, 10, 20);
                        tvWindowTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowTrimColorTitle.setText("Trim color");

                        final TextView tvWindowTrimWidthTitle = new TextView(this);
                        tvWindowTrimWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowTrimWidthTitle.setGravity(Gravity.RIGHT);
                        tvWindowTrimWidthTitle.setPadding(10, 20, 10, 20);
                        tvWindowTrimWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowTrimWidthTitle.setText("Trim width");

                        // create row
                        final TableRow trWindowInfoTitle = new TableRow(this);
//                        trWindowInfoTitle.setId(i + 1);
                        TableLayout.LayoutParams trWindowInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trWindowInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trWindowInfoTitle.setPadding(0, 0, 0, 0);
                        trWindowInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                        trWindowInfoTitle.setLayoutParams(trWindowInfoParamsTitle);

                        // add cells to row
                        trWindowInfoTitle.addView(tvWindowWidthTitle);
                        trWindowInfoTitle.addView(tvWindowLengthTitle);
                        trWindowInfoTitle.addView(tvWindowQuantityTitle);
                        trWindowInfoTitle.addView(tvWindowTrimColorTitle);
                        trWindowInfoTitle.addView(tvWindowTrimWidthTitle);

                        // add row to table
                        windowInfoLayout.addView(trWindowInfoTitle, trWindowInfoParamsTitle);

                        String[] window = parsedRoom[1].split("!");
                        for (String el : window) {
                            String[] parsedWindowInfo = el.split(":");

                            int widthWindow = Integer.parseInt(parsedWindowInfo[0]);
                            int lengthWindow = Integer.parseInt(parsedWindowInfo[1]);
                            int quantityWindow = Integer.parseInt(parsedWindowInfo[2]);
                            int widthTrimWindow = Integer.parseInt(parsedWindowInfo[3]);
                            String colorWindow = parsedWindowInfo[4];

                            // create cells
                            final TextView tvWindowWidth = new TextView(this);
                            tvWindowWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowWidth.setGravity(Gravity.RIGHT);
                            tvWindowWidth.setPadding(10, 20, 10, 20);
                            tvWindowWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowWidth.setText(String.valueOf(widthWindow / 12 + "' " + widthWindow % 12 + "\""));

                            final TextView tvWindowLength = new TextView(this);
                            tvWindowLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowLength.setGravity(Gravity.RIGHT);
                            tvWindowLength.setPadding(10, 20, 10, 20);
                            tvWindowLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowLength.setText(String.valueOf(lengthWindow / 12 + "' " + lengthWindow % 12 + "\""));

                            final TextView tvWindowQuantity = new TextView(this);
                            tvWindowQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowQuantity.setGravity(Gravity.RIGHT);
                            tvWindowQuantity.setPadding(10, 20, 10, 20);
                            tvWindowQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowQuantity.setText(String.valueOf(quantityWindow + "pcs."));

                            final TextView tvWindowTrimColor = new TextView(this);
                            tvWindowTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowTrimColor.setGravity(Gravity.RIGHT);
                            tvWindowTrimColor.setPadding(10, 20, 10, 20);
                            tvWindowTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowTrimColor.setText(colorWindow);

                            final TextView tvWindowTrimWidth = new TextView(this);
                            tvWindowTrimWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowTrimWidth.setGravity(Gravity.RIGHT);
                            tvWindowTrimWidth.setPadding(10, 20, 10, 20);
                            tvWindowTrimWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowTrimWidth.setText(String.valueOf(widthTrimWindow + "\""));

                            // create row
                            final TableRow trWindowInfo = new TableRow(this);
//                        trWindowInfo.setId(i + 1);
                            TableLayout.LayoutParams trWindowInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            trWindowInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                            trWindowInfo.setPadding(0, 0, 0, 0);
                            trWindowInfo.setLayoutParams(trWindowInfoParams);

                            // add cells to row
                            trWindowInfo.addView(tvWindowWidth);
                            trWindowInfo.addView(tvWindowLength);
                            trWindowInfo.addView(tvWindowQuantity);
                            trWindowInfo.addView(tvWindowTrimColor);
                            trWindowInfo.addView(tvWindowTrimWidth);

                            // add row to table
                            windowInfoLayout.addView(trWindowInfo, trWindowInfoParams);
                        }
                    }

                    if (parsedRoom[2].length() > 1) {

                        // create cells
                        final TextView tvDoorWidthTitle = new TextView(this);
                        tvDoorWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorWidthTitle.setGravity(Gravity.RIGHT);
                        tvDoorWidthTitle.setPadding(10, 20, 10, 20);
                        tvDoorWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorWidthTitle.setText("Width");

                        final TextView tvDoorLengthTitle = new TextView(this);
                        tvDoorLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorLengthTitle.setGravity(Gravity.RIGHT);
                        tvDoorLengthTitle.setPadding(10, 20, 10, 20);
                        tvDoorLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorLengthTitle.setText("Length");

                        final TextView tvDoorQuantityTitle = new TextView(this);
                        tvDoorQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorQuantityTitle.setGravity(Gravity.RIGHT);
                        tvDoorQuantityTitle.setPadding(10, 20, 10, 20);
                        tvDoorQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorQuantityTitle.setText("Quantity");

                        final TextView tvDoorTrimColorTitle = new TextView(this);
                        tvDoorTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorTrimColorTitle.setGravity(Gravity.RIGHT);
                        tvDoorTrimColorTitle.setPadding(10, 20, 10, 20);
                        tvDoorTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorTrimColorTitle.setText("Trim color");

                        final TextView tvDoorTrimWidthTitle = new TextView(this);
                        tvDoorTrimWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorTrimWidthTitle.setGravity(Gravity.RIGHT);
                        tvDoorTrimWidthTitle.setPadding(10, 20, 10, 20);
                        tvDoorTrimWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorTrimWidthTitle.setText("Trim width");

                        // create row
                        final TableRow trDoorInfoTitle = new TableRow(this);
//                        trDoorInfoTitle.setId(i + 1);
                        TableLayout.LayoutParams trDoorInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trDoorInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trDoorInfoTitle.setPadding(0, 0, 0, 0);
                        trDoorInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                        trDoorInfoTitle.setLayoutParams(trDoorInfoParamsTitle);

                        // add cells to row
                        trDoorInfoTitle.addView(tvDoorWidthTitle);
                        trDoorInfoTitle.addView(tvDoorLengthTitle);
                        trDoorInfoTitle.addView(tvDoorQuantityTitle);
                        trDoorInfoTitle.addView(tvDoorTrimColorTitle);
                        trDoorInfoTitle.addView(tvDoorTrimWidthTitle);

                        // add row to table
                        doorInfoLayout.addView(trDoorInfoTitle, trDoorInfoParamsTitle);

                        String[] door = parsedRoom[2].split("!");
                        for (String el : door) {
                            String[] parsedDoorInfo = el.split(":");

                            int widthDoor = Integer.parseInt(parsedDoorInfo[0]);
                            int lengthDoor = Integer.parseInt(parsedDoorInfo[1]);
                            int quantityDoor = Integer.parseInt(parsedDoorInfo[2]);
                            int widthTrimDoor = Integer.parseInt(parsedDoorInfo[3]);
                            String colorDoor = parsedDoorInfo[4];

                            // create cells
                            final TextView tvDoorWidth = new TextView(this);
                            tvDoorWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorWidth.setGravity(Gravity.RIGHT);
                            tvDoorWidth.setPadding(10, 20, 10, 20);
                            tvDoorWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorWidth.setText(String.valueOf(widthDoor / 12 + "' " + widthDoor % 12 + "\""));

                            final TextView tvDoorLength = new TextView(this);
                            tvDoorLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorLength.setGravity(Gravity.RIGHT);
                            tvDoorLength.setPadding(10, 20, 10, 20);
                            tvDoorLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorLength.setText(String.valueOf(lengthDoor / 12 + "' " + lengthDoor % 12 + "\""));

                            final TextView tvDoorQuantity = new TextView(this);
                            tvDoorQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorQuantity.setGravity(Gravity.RIGHT);
                            tvDoorQuantity.setPadding(10, 20, 10, 20);
                            tvDoorQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorQuantity.setText(String.valueOf(quantityDoor + "pcs."));

                            final TextView tvDoorTrimColor = new TextView(this);
                            tvDoorTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorTrimColor.setGravity(Gravity.RIGHT);
                            tvDoorTrimColor.setPadding(10, 20, 10, 20);
                            tvDoorTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorTrimColor.setText(colorDoor);

                            final TextView tvDoorTrimWidth = new TextView(this);
                            tvDoorTrimWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorTrimWidth.setGravity(Gravity.RIGHT);
                            tvDoorTrimWidth.setPadding(10, 20, 10, 20);
                            tvDoorTrimWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorTrimWidth.setText(String.valueOf(widthTrimDoor + "\""));

                            // create row
                            final TableRow trDoorInfo = new TableRow(this);
//                        trDoorInfo.setId(i + 1);
                            TableLayout.LayoutParams trDoorInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            trDoorInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                            trDoorInfo.setPadding(0, 0, 0, 0);
                            trDoorInfo.setLayoutParams(trDoorInfoParams);

                            // add cells to row
                            trDoorInfo.addView(tvDoorWidth);
                            trDoorInfo.addView(tvDoorLength);
                            trDoorInfo.addView(tvDoorQuantity);
                            trDoorInfo.addView(tvDoorTrimColor);
                            trDoorInfo.addView(tvDoorTrimWidth);

                            // add row to table
                            doorInfoLayout.addView(trDoorInfo, trDoorInfoParams);
                        }
                    }
                }

//                        if (i > -1) {
//                            tr.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    TableRow tr = (TableRow) v;
//                                }
//                            });


                // create table for room title
                TableLayout roomFooterLayout = new TableLayout(this);
                roomFooterLayout.setStretchAllColumns(true);
                roomFooterLayout.removeAllViews();

                // create cell
                final TextView tvFooter = new TextView(this);
                tvFooter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tvFooter.setGravity(Gravity.LEFT);
                tvFooter.setPadding(0, 0, 0, 0);
                tvFooter.setBackgroundColor(Color.parseColor(titleBackgroundColor));
                tvFooter.setTextColor(Color.parseColor(titleTextColor));
                tvFooter.setTypeface(null, Typeface.BOLD);
                tvFooter.setText("");
                tvFooter.setTextSize(TypedValue.COMPLEX_UNIT_PX, 10);

                // create row
                final TableRow trFooter = new TableRow(this);
//                        trFooter.setId(i + 1);
                TableLayout.LayoutParams trFooterParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                trFooterParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trFooter.setPadding(0, 0, 0, 0);
                trFooter.setLayoutParams(trFooterParams);

                // add cell to row
                trFooter.addView(tvFooter);

                // add row to table
                roomFooterLayout.addView(trFooter, trFooterParams);


                // add tables into main table
                TableLayout.LayoutParams tbTitleTableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                tbTitleTableParams.setMargins(10, 60, 10, 0);

                TableLayout.LayoutParams tbTablesParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                tbTablesParams.setMargins(10, 0, 10, 0);

                tblRoomsLayout.addView(roomTitleLayout, tbTitleTableParams);
                tblRoomsLayout.addView(roomInfoLayout, tbTablesParams);
                tblRoomsLayout.addView(windowTitleLayout, tbTablesParams);
                tblRoomsLayout.addView(windowInfoLayout, tbTablesParams);
                tblRoomsLayout.addView(doorTitleLayout, tbTablesParams);
                tblRoomsLayout.addView(doorInfoLayout, tbTablesParams);
                tblRoomsLayout.addView(roomFooterLayout, tbTablesParams);

//                        if (i > -1) {
//                            // add separator row
//                            final TableRow trSep = new TableRow(this);
//                            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//                            trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
//                            trSep.setLayoutParams(trParamsSep);
//                            TextView tvSep = new TextView(this);
//                            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
//                            tvSepLay.span = 4;
//                            tvSep.setLayoutParams(tvSepLay);
//                            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
//                            tvSep.setHeight(1);
//                            trSep.addView(tvSep);
//                            tblRoomsLayout.addView(trSep, trParamsSep);
//                        }
            }
        }

        String message = sharedPref.getString("MESSAGE", "");
        if (!message.equals("")) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("MESSAGE");
            editor.commit();

        }
    }

}