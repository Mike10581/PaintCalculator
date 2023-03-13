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
    TextView textView;

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
                        textView.setText("");
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

        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 40, smallTextSize = 0, mediumTextSize = 0;
//        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
//        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
//        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        tblRoomsLayout.removeAllViews();

        String message = sharedPref.getString("MESSAGE", "");
        Map<String, ?> dataset = sharedPref.getAll();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : dataset.entrySet()) {
            if (!entry.getKey().equals("MESSAGE")) {
                if (entry.getKey().equals("ROOMS")) {
                    String[] parsedRooms = entry.getValue().toString().split("~");

                    int roomsQuantity = parsedRooms.length;

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


                    for (int i = 0; i < roomsQuantity; i++) {
                        String[] parsedRoom = parsedRooms[i].split(",");
                        String[] parsedInfo = parsedRoom[0].split(":");

                        TableLayout roomTitleLayout = new TableLayout(this);
                        roomTitleLayout.setStretchAllColumns(true);
                        roomTitleLayout.removeAllViews();

                        //add table cell
                        final TextView tvRoomTitle = new TextView(this);
                        tvRoomTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomTitle.setGravity(Gravity.LEFT);
                        tvRoomTitle.setPadding(5, 15, 0, 15);
                        tvRoomTitle.setBackgroundColor(Color.parseColor("#44000000"));
                        tvRoomTitle.setTypeface(null, Typeface.BOLD);
                        tvRoomTitle.setText(String.valueOf(parsedInfo[0] + ". " + parsedInfo[5]));
                        tvRoomTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                        // add table row
                        final TableRow trRoomTitle = new TableRow(this);
                        trRoomTitle.setId(i + 1);
                        TableLayout.LayoutParams trRoomTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trRoomTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trRoomTitle.setPadding(0, 0, 0, 0);
                        trRoomTitle.setLayoutParams(trRoomTitleParams);
                        trRoomTitle.addView(tvRoomTitle);

                        roomTitleLayout.addView(trRoomTitle, trRoomTitleParams);


                        TableLayout roomInfoLayout = new TableLayout(this);
                        roomInfoLayout.setStretchAllColumns(true);
                        roomInfoLayout.removeAllViews();

                        //add table cell
                        final TextView tvRoomWidthTitle = new TextView(this);
                        tvRoomWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomWidthTitle.setGravity(Gravity.LEFT);
                        tvRoomWidthTitle.setPadding(5, 15, 0, 15);
                        tvRoomWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomWidthTitle.setText("Width");

                        final TextView tvRoomLengthTitle = new TextView(this);
                        tvRoomLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomLengthTitle.setGravity(Gravity.LEFT);
                        tvRoomLengthTitle.setPadding(5, 15, 0, 15);
                        tvRoomLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomLengthTitle.setText("Length");

                        final TextView tvRoomHeightTitle = new TextView(this);
                        tvRoomHeightTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomHeightTitle.setGravity(Gravity.LEFT);
                        tvRoomHeightTitle.setPadding(5, 15, 0, 15);
                        tvRoomHeightTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomHeightTitle.setText("Height");

                        final TextView tvRoomColorTitle = new TextView(this);
                        tvRoomColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomColorTitle.setGravity(Gravity.LEFT);
                        tvRoomColorTitle.setPadding(5, 15, 0, 15);
                        tvRoomColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomColorTitle.setText("Color");

                        final TableRow trRoomInfoTitle = new TableRow(this);
//                        trRoomInfoTitle.setId(i + 1);
                        TableLayout.LayoutParams trRoomInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trRoomInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trRoomInfoTitle.setPadding(0, 0, 0, 0);
                        trRoomInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                        trRoomInfoTitle.setLayoutParams(trRoomInfoParamsTitle);
                        trRoomInfoTitle.addView(tvRoomWidthTitle);
                        trRoomInfoTitle.addView(tvRoomLengthTitle);
                        trRoomInfoTitle.addView(tvRoomHeightTitle);
                        trRoomInfoTitle.addView(tvRoomColorTitle);

                        //add table cell

                        int width = Integer.parseInt(parsedInfo[1]);
                        int length = Integer.parseInt(parsedInfo[2]);
                        int height = Integer.parseInt(parsedInfo[3]);

                        final TextView tvRoomWidth = new TextView(this);
                        tvRoomWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomWidth.setGravity(Gravity.LEFT);
                        tvRoomWidth.setPadding(5, 15, 0, 15);
                        tvRoomWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomWidth.setText(String.valueOf(width / 12 + "' " + width % 12 + "\""));

                        final TextView tvRoomLength = new TextView(this);
                        tvRoomLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomLength.setGravity(Gravity.LEFT);
                        tvRoomLength.setPadding(5, 15, 0, 15);
                        tvRoomLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomLength.setText(String.valueOf(length / 12 + "' " + length % 12 + "\""));

                        final TextView tvRoomHeight = new TextView(this);
                        tvRoomHeight.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomHeight.setGravity(Gravity.LEFT);
                        tvRoomHeight.setPadding(5, 15, 0, 15);
                        tvRoomHeight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomHeight.setText(String.valueOf(height / 12 + "' " + height % 12 + "\""));

                        final TextView tvRoomColor = new TextView(this);
                        tvRoomColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvRoomColor.setGravity(Gravity.LEFT);
                        tvRoomColor.setPadding(5, 15, 0, 15);
                        tvRoomColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        tvRoomColor.setText(String.valueOf(parsedInfo[4]));

                        final TableRow trRoomInfo = new TableRow(this);
//                        trRoomInfo.setId(i + 1);
                        TableLayout.LayoutParams trRoomInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trRoomInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trRoomInfo.setPadding(0, 0, 0, 0);
                        trRoomInfo.setLayoutParams(trRoomInfoParams);
                        trRoomInfo.addView(tvRoomWidth);
                        trRoomInfo.addView(tvRoomLength);
                        trRoomInfo.addView(tvRoomHeight);
                        trRoomInfo.addView(tvRoomColor);

//                        tr.addView(tv);
//                        tr.addView(tv2);
//                        tr.addView(layCustomer);
//                        tr.addView(layAmounts);
//                        if (i > -1) {
//                            tr.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    TableRow tr = (TableRow) v;
//                                }
//                            });
//
//                        tblRoomsLayout.addView(trRoomTitle, trRoomTitleParams);

                        roomInfoLayout.addView(trRoomInfoTitle, trRoomInfoParamsTitle);
                        roomInfoLayout.addView(trRoomInfo, trRoomInfoParams);




                        TableLayout windowTitleLayout = new TableLayout(this);
                        windowTitleLayout.setStretchAllColumns(true);
                        windowTitleLayout.removeAllViews();

                        //add table cell
                        final TextView tvWindowTitle = new TextView(this);
                        tvWindowTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowTitle.setGravity(Gravity.LEFT);
                        tvWindowTitle.setPadding(5, 15, 0, 15);
                        tvWindowTitle.setBackgroundColor(Color.parseColor("#44ff0000"));
                        tvWindowTitle.setTypeface(null, Typeface.BOLD);
                        tvWindowTitle.setText("Windows:");
                        tvWindowTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                        // add table row
                        final TableRow trWindowTitle = new TableRow(this);
//                        trWindowTitle.setId(i + 1);
                        TableLayout.LayoutParams trWindowTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trWindowTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trWindowTitle.setPadding(0, 0, 0, 0);
                        trWindowTitle.setLayoutParams(trWindowTitleParams);
                        trWindowTitle.addView(tvWindowTitle);

                        windowTitleLayout.addView(trWindowTitle, trWindowTitleParams);


                        TableLayout windowInfoLayout = new TableLayout(this);
                        windowInfoLayout.setStretchAllColumns(true);
                        windowInfoLayout.removeAllViews();
                        
                        
                        
                        
                        
                        
                        TableLayout doorTitleLayout = new TableLayout(this);
                        doorTitleLayout.setStretchAllColumns(true);
                        doorTitleLayout.removeAllViews();

                        //add table cell
                        final TextView tvDoorTitle = new TextView(this);
                        tvDoorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorTitle.setGravity(Gravity.LEFT);
                        tvDoorTitle.setPadding(5, 15, 0, 15);
                        tvDoorTitle.setBackgroundColor(Color.parseColor("#44ff0000"));
                        tvDoorTitle.setTypeface(null, Typeface.BOLD);
                        tvDoorTitle.setText("Doors:");
                        tvDoorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                        // add table row
                        final TableRow trDoorTitle = new TableRow(this);
//                        trDoorTitle.setId(i + 1);
                        TableLayout.LayoutParams trDoorTitleParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trDoorTitleParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                        trDoorTitle.setPadding(0, 0, 0, 0);
                        trDoorTitle.setLayoutParams(trDoorTitleParams);
                        trDoorTitle.addView(tvDoorTitle);

                        doorTitleLayout.addView(trDoorTitle, trDoorTitleParams);


                        TableLayout doorInfoLayout = new TableLayout(this);
                        doorInfoLayout.setStretchAllColumns(true);
                        doorInfoLayout.removeAllViews();

                        if (parsedRoom.length > 1) {
                            if (parsedRoom[1].length() > 1) {



                                //add table cell
                                final TextView tvWindowWidthTitle = new TextView(this);
                                tvWindowWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvWindowWidthTitle.setGravity(Gravity.LEFT);
                                tvWindowWidthTitle.setPadding(5, 15, 0, 15);
                                tvWindowWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvWindowWidthTitle.setText("Width");

                                final TextView tvWindowLengthTitle = new TextView(this);
                                tvWindowLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvWindowLengthTitle.setGravity(Gravity.LEFT);
                                tvWindowLengthTitle.setPadding(5, 15, 0, 15);
                                tvWindowLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvWindowLengthTitle.setText("Length");

                                final TextView tvWindowQuantityTitle = new TextView(this);
                                tvWindowQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvWindowQuantityTitle.setGravity(Gravity.LEFT);
                                tvWindowQuantityTitle.setPadding(5, 15, 0, 15);
                                tvWindowQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvWindowQuantityTitle.setText("Quantity");

                                final TextView tvWindowTrimColorTitle = new TextView(this);
                                tvWindowTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvWindowTrimColorTitle.setGravity(Gravity.LEFT);
                                tvWindowTrimColorTitle.setPadding(5, 15, 0, 15);
                                tvWindowTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvWindowTrimColorTitle.setText("Trim color");

                                final TextView tvWindowTrimWidthTitle = new TextView(this);
                                tvWindowTrimWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvWindowTrimWidthTitle.setGravity(Gravity.LEFT);
                                tvWindowTrimWidthTitle.setPadding(5, 15, 0, 15);
                                tvWindowTrimWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvWindowTrimWidthTitle.setText("Trim width");

                                final TableRow trWindowInfoTitle = new TableRow(this);
//                        trWindowInfoTitle.setId(i + 1);
                                TableLayout.LayoutParams trWindowInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                trWindowInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                                trWindowInfoTitle.setPadding(0, 0, 0, 0);
                                trWindowInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                                trWindowInfoTitle.setLayoutParams(trWindowInfoParamsTitle);
                                trWindowInfoTitle.addView(tvWindowWidthTitle);
                                trWindowInfoTitle.addView(tvWindowLengthTitle);
                                trWindowInfoTitle.addView(tvWindowQuantityTitle);
                                trWindowInfoTitle.addView(tvWindowTrimColorTitle);
                                trWindowInfoTitle.addView(tvWindowTrimWidthTitle);

                                windowInfoLayout.addView(trWindowInfoTitle, trWindowInfoParamsTitle);
                                
                                
                                
                                

                                String[] window = parsedRoom[1].split("!");
                                for (String el : window) {
                                    String[] parsedWindowInfo = el.split(":");


                                    int widthWindow = Integer.parseInt(parsedWindowInfo[0]);
                                    int lengthWindow = Integer.parseInt(parsedWindowInfo[1]);
                                    int quantityWindow = Integer.parseInt(parsedWindowInfo[2]);
                                    int widthTrimWindow = Integer.parseInt(parsedWindowInfo[3]);
                                    String colorWindow = parsedWindowInfo[4];

                                    final TextView tvWindowWidth = new TextView(this);
                                    tvWindowWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvWindowWidth.setGravity(Gravity.LEFT);
                                    tvWindowWidth.setPadding(5, 15, 0, 15);
                                    tvWindowWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvWindowWidth.setText(String.valueOf(widthWindow / 12 + "' " + widthWindow % 12 + "\""));

                                    final TextView tvWindowLength = new TextView(this);
                                    tvWindowLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvWindowLength.setGravity(Gravity.LEFT);
                                    tvWindowLength.setPadding(5, 15, 0, 15);
                                    tvWindowLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvWindowLength.setText(String.valueOf(lengthWindow / 12 + "' " + lengthWindow % 12 + "\""));

                                    final TextView tvWindowQuantity = new TextView(this);
                                    tvWindowQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvWindowQuantity.setGravity(Gravity.LEFT);
                                    tvWindowQuantity.setPadding(5, 15, 0, 15);
                                    tvWindowQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvWindowQuantity.setText(String.valueOf(quantityWindow + "pcs."));

                                    final TextView tvWindowTrimColor = new TextView(this);
                                    tvWindowTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvWindowTrimColor.setGravity(Gravity.LEFT);
                                    tvWindowTrimColor.setPadding(5, 15, 0, 15);
                                    tvWindowTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvWindowTrimColor.setText(colorWindow);

                                    final TextView tvWindowTrimWidth = new TextView(this);
                                    tvWindowTrimWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvWindowTrimWidth.setGravity(Gravity.LEFT);
                                    tvWindowTrimWidth.setPadding(5, 15, 0, 15);
                                    tvWindowTrimWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvWindowTrimWidth.setText(String.valueOf(widthTrimWindow + "\""));

                                    final TableRow trWindowInfo = new TableRow(this);
//                        trWindowInfo.setId(i + 1);
                                    TableLayout.LayoutParams trWindowInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                    trWindowInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                                    trWindowInfo.setPadding(0, 0, 0, 0);
                                    trWindowInfo.setLayoutParams(trWindowInfoParams);
                                    trWindowInfo.addView(tvWindowWidth);
                                    trWindowInfo.addView(tvWindowLength);
                                    trWindowInfo.addView(tvWindowQuantity);
                                    trWindowInfo.addView(tvWindowTrimColor);
                                    trWindowInfo.addView(tvWindowTrimWidth);

                                    windowInfoLayout.addView(trWindowInfo, trWindowInfoParams);
                                    
                                    
                                    
                                }
                            }




                            if (parsedRoom[2].length() > 1) {

                                //add table cell
                                final TextView tvDoorWidthTitle = new TextView(this);
                                tvDoorWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvDoorWidthTitle.setGravity(Gravity.LEFT);
                                tvDoorWidthTitle.setPadding(5, 15, 0, 15);
                                tvDoorWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvDoorWidthTitle.setText("Width");

                                final TextView tvDoorLengthTitle = new TextView(this);
                                tvDoorLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvDoorLengthTitle.setGravity(Gravity.LEFT);
                                tvDoorLengthTitle.setPadding(5, 15, 0, 15);
                                tvDoorLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvDoorLengthTitle.setText("Length");

                                final TextView tvDoorQuantityTitle = new TextView(this);
                                tvDoorQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvDoorQuantityTitle.setGravity(Gravity.LEFT);
                                tvDoorQuantityTitle.setPadding(5, 15, 0, 15);
                                tvDoorQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvDoorQuantityTitle.setText("Quantity");

                                final TextView tvDoorTrimColorTitle = new TextView(this);
                                tvDoorTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvDoorTrimColorTitle.setGravity(Gravity.LEFT);
                                tvDoorTrimColorTitle.setPadding(5, 15, 0, 15);
                                tvDoorTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvDoorTrimColorTitle.setText("Trim color");

                                final TextView tvDoorTrimWidthTitle = new TextView(this);
                                tvDoorTrimWidthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tvDoorTrimWidthTitle.setGravity(Gravity.LEFT);
                                tvDoorTrimWidthTitle.setPadding(5, 15, 0, 15);
                                tvDoorTrimWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                tvDoorTrimWidthTitle.setText("Trim width");

                                final TableRow trDoorInfoTitle = new TableRow(this);
//                        trDoorInfoTitle.setId(i + 1);
                                TableLayout.LayoutParams trDoorInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                trDoorInfoParamsTitle.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                                trDoorInfoTitle.setPadding(0, 0, 0, 0);
                                trDoorInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                                trDoorInfoTitle.setLayoutParams(trDoorInfoParamsTitle);
                                trDoorInfoTitle.addView(tvDoorWidthTitle);
                                trDoorInfoTitle.addView(tvDoorLengthTitle);
                                trDoorInfoTitle.addView(tvDoorQuantityTitle);
                                trDoorInfoTitle.addView(tvDoorTrimColorTitle);
                                trDoorInfoTitle.addView(tvDoorTrimWidthTitle);

                                doorInfoLayout.addView(trDoorInfoTitle, trDoorInfoParamsTitle);

                                String[] door = parsedRoom[2].split("!");
                                for (String el : door) {
                                    String[] parsedDoorInfo = el.split(":");


                                    //add table cell

                                    int widthDoor = Integer.parseInt(parsedDoorInfo[0]);
                                    int lengthDoor = Integer.parseInt(parsedDoorInfo[1]);
                                    int quantityDoor = Integer.parseInt(parsedDoorInfo[2]);
                                    int widthTrimDoor = Integer.parseInt(parsedDoorInfo[3]);
                                    String colorDoor = parsedDoorInfo[4];

                                    final TextView tvDoorWidth = new TextView(this);
                                    tvDoorWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDoorWidth.setGravity(Gravity.LEFT);
                                    tvDoorWidth.setPadding(5, 15, 0, 15);
                                    tvDoorWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvDoorWidth.setText(String.valueOf(widthDoor / 12 + "' " + widthDoor % 12 + "\""));

                                    final TextView tvDoorLength = new TextView(this);
                                    tvDoorLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDoorLength.setGravity(Gravity.LEFT);
                                    tvDoorLength.setPadding(5, 15, 0, 15);
                                    tvDoorLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvDoorLength.setText(String.valueOf(lengthDoor / 12 + "' " + lengthDoor % 12 + "\""));

                                    final TextView tvDoorQuantity = new TextView(this);
                                    tvDoorQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDoorQuantity.setGravity(Gravity.LEFT);
                                    tvDoorQuantity.setPadding(5, 15, 0, 15);
                                    tvDoorQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvDoorQuantity.setText(String.valueOf(quantityDoor + "pcs."));

                                    final TextView tvDoorTrimColor = new TextView(this);
                                    tvDoorTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDoorTrimColor.setGravity(Gravity.LEFT);
                                    tvDoorTrimColor.setPadding(5, 15, 0, 15);
                                    tvDoorTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvDoorTrimColor.setText(colorDoor);

                                    final TextView tvDoorTrimWidth = new TextView(this);
                                    tvDoorTrimWidth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tvDoorTrimWidth.setGravity(Gravity.LEFT);
                                    tvDoorTrimWidth.setPadding(5, 15, 0, 15);
                                    tvDoorTrimWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                                    tvDoorTrimWidth.setText(String.valueOf(widthTrimDoor + "\""));

                                    final TableRow trDoorInfo = new TableRow(this);
//                        trDoorInfo.setId(i + 1);
                                    TableLayout.LayoutParams trDoorInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                                    trDoorInfoParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                                    trDoorInfo.setPadding(0, 0, 0, 0);
                                    trDoorInfo.setLayoutParams(trDoorInfoParams);
                                    trDoorInfo.addView(tvDoorWidth);
                                    trDoorInfo.addView(tvDoorLength);
                                    trDoorInfo.addView(tvDoorQuantity);
                                    trDoorInfo.addView(tvDoorTrimColor);
                                    trDoorInfo.addView(tvDoorTrimWidth);

                                    doorInfoLayout.addView(trDoorInfo, trDoorInfoParams);
                                }


                            }

                        }


//                        tr.addView(tv);
//                        tr.addView(tv2);
//                        tr.addView(layCustomer);
//                        tr.addView(layAmounts);
//                        if (i > -1) {
//                            tr.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View v) {
//                                    TableRow tr = (TableRow) v;
//                                }
//                            });
//
//                        tblDoorsLayout.addView(trDoorTitle, trDoorTitleParams);


                        tblRoomsLayout.addView(roomTitleLayout);
                        tblRoomsLayout.addView(roomInfoLayout);
                        tblRoomsLayout.addView(windowTitleLayout);
                        tblRoomsLayout.addView(windowInfoLayout);
                        tblRoomsLayout.addView(doorTitleLayout);
                        tblRoomsLayout.addView(doorInfoLayout);


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