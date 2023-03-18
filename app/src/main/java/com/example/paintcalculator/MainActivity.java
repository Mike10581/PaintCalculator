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
import android.graphics.drawable.GradientDrawable;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Button addWindow;
    Button addDoor;
    Button clear;
    Button calculate;
    Button seeder;

    public static String getContrastingColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int average = (red + green + blue) / 3;
        return (average >= 128) ? "#000000" : "#FFFFFF";
    }

    private TableLayout tblRoomsLayout;

    // Create TextView cell
    public TextView createTextViewCell(
            int[] tableRowLayoutParams,
            int gravity,
            int[] padding,
            boolean border,
            String backgroundColor,
            String textColor,
            boolean boldText,
            int textSize,
            String text) {
        final TextView tvCell = new TextView(this);
        tvCell.setLayoutParams(new TableRow.LayoutParams(tableRowLayoutParams[0], tableRowLayoutParams[1]));
        tvCell.setGravity(gravity);
        tvCell.setPadding(padding[0], padding[1], padding[2], padding[3]);
        if (border) {
            tvCell.setBackgroundResource(R.drawable.border);
            GradientDrawable drawableRoom = (GradientDrawable) tvCell.getBackground();
            drawableRoom.setColor(Color.parseColor(backgroundColor));
        } else {
            tvCell.setBackgroundColor(Color.parseColor(backgroundColor));
        }
        tvCell.setTextColor(Color.parseColor(textColor));
        if (boldText) {
            tvCell.setTypeface(null, Typeface.BOLD);
        }
        tvCell.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvCell.setText(text);
        return tvCell;
    }

    public TableRow createTableRow(int[] tableLayoutParams, int[] margins, int[] padding) {
        final TableRow tblRow = new TableRow(this);
        TableLayout.LayoutParams tblRowParams = new TableLayout.LayoutParams(tableLayoutParams[0], tableLayoutParams[1]);
        tblRowParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        tblRow.setPadding(padding[0], padding[1], padding[2], padding[3]);
        tblRow.setLayoutParams(tblRowParams);
        return tblRow;
    }

    // Handle the result of DeleteConfirmationActivity
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        editor.remove("ROOMS");
                        editor.remove("ROOMS_AMOUNT");
                        editor.commit();
                        addWindow.setEnabled(false);
                        addDoor.setEnabled(false);
                        clear.setEnabled(false);
                        calculate.setEnabled(false);
                        seeder.setEnabled(true);
                        tblRoomsLayout.removeAllViews();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addRoom = (Button) findViewById(R.id.btnAddRoom);
        addWindow = (Button) findViewById(R.id.btnAddWindow);
        addDoor = (Button) findViewById(R.id.btnAddDoor);
        clear = (Button) findViewById(R.id.btnClear);
        calculate = (Button) findViewById(R.id.btnCalculate);
        seeder = (Button) findViewById(R.id.btnSeeder);
        //TODO add checks for empty room list and block add windows and doors buttons
        tblRoomsLayout = (TableLayout) findViewById(R.id.tblRoomsLayout);
        tblRoomsLayout.setStretchAllColumns(true);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SET_COLOR", "#FFFFFF");
                editor.commit();
                startActivity(new Intent(MainActivity.this, AddRoomActivity.class));
            }
        });

        addWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SET_COLOR", "#FFFFFF");
                editor.commit();
                startActivity(new Intent(MainActivity.this, AddWindowActivity.class));
            }
        });

        addDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SET_COLOR", "#FFFFFF");
                editor.commit();
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

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalculateResultActivity.class));
            }
        });

        seeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestSeederActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        int bigTextSize = 70, smallTextSize = 45, mediumTextSize = 55;

        String titleBackgroundColor = "#FF6200EE";
        String titleTextColor = "#FFFFFFFF";
        String descriptionTitleBackgroundColor = "#220000ff";
        String roomsAddonsBackgroundColor = "#33ff0000";
        String descriptionTitleTextColor = "#000000";

        int[] PARENT_CONTENT_TABLE_LAYOUT = {
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        };
        int[] CONTENT_CONTENT_TABLE_LAYOUT = {
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        };
        int[] paddingRoomTitleCell = {10, 20, 10, 20};
        int[] paddingRoomInfoCell = {10, 20, 10, 20};
        int[] paddingRoomInfoDataCell = {10, 5, 10, 5};

        tblRoomsLayout.removeAllViews();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        addWindow.setEnabled(false);
        addDoor.setEnabled(false);
        clear.setEnabled(false);
        calculate.setEnabled(false);
        seeder.setEnabled(true);
//        Toast.makeText(MainActivity.this, "start", Toast.LENGTH_LONG).show();
        if (!sharedPref.getString("ROOMS", "").equals("")) {

            addWindow.setEnabled(true);
            addDoor.setEnabled(true);
            clear.setEnabled(true);
            calculate.setEnabled(true);
            seeder.setEnabled(false);

            String[] parsedRooms = sharedPref.getString("ROOMS", "").split("~");

            int roomsQuantity = parsedRooms.length;

            for (int i = 0; i < roomsQuantity; i++) {
                String[] parsedRoom = parsedRooms[i].split(",");
                String[] parsedInfo = parsedRoom[0].split(":");

                // create table for room title
                TableLayout tableRoomTitle = new TableLayout(this);
                tableRoomTitle.setStretchAllColumns(true);
                tableRoomTitle.removeAllViews();
                // create row for room title
                int[] marginsRow = {0, 0, 0, 0};
                int[] paddingRow = {0, 0, 0, 0};
                int[] paddingRowData = {0, 10, 0, 10};
                final TableRow tableRowRoomTitle = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRow);
                // create cell for room title
                final TextView tableCellRoomTitle = createTextViewCell(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        Gravity.START,
                        paddingRoomTitleCell,
                        false,
                        titleBackgroundColor,
                        titleTextColor,
                        true,
                        bigTextSize,
                        parsedInfo[0] + ". " + parsedInfo[5]);

                // add cell to row
                tableRowRoomTitle.addView(tableCellRoomTitle);
                // add row to table
                tableRoomTitle.addView(tableRowRoomTitle);


                // create table for room info (size and color)
                TableLayout tableRoomInfo = new TableLayout(this);
                tableRoomInfo.setStretchAllColumns(true);
                tableRoomInfo.removeAllViews();
                // create row
                final TableRow rowRoomInfoTitle = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRow
                );
                // create cells
                String[] roomInfoTableTitles = {"Color", "Width", "Length", "Height"};

                for (String cell : roomInfoTableTitles) {

                    final TextView cellRoomInfoTitle = createTextViewCell(
                            CONTENT_CONTENT_TABLE_LAYOUT,
                            Gravity.END,
                            paddingRoomInfoCell,
                            false,
                            descriptionTitleBackgroundColor,
                            descriptionTitleTextColor,
                            true,
                            smallTextSize,
                            cell);
                    // add cells to row
                    rowRoomInfoTitle.addView(cellRoomInfoTitle);
                }
                // add row to table
                tableRoomInfo.addView(rowRoomInfoTitle);

                int width = Integer.parseInt(parsedInfo[1]);
                int length = Integer.parseInt(parsedInfo[2]);
                int height = Integer.parseInt(parsedInfo[3]);
                String color = parsedInfo[4];

                // create row
                final TableRow rowRoomInfoData = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRowData);
                // create cells
                String[] roomInfoTableData = {
                        color,
                        String.valueOf(width / 12 + "' " + width % 12 + "\""),
                        String.valueOf(length / 12 + "' " + length % 12 + "\""),
                        String.valueOf(height / 12 + "' " + height % 12 + "\"")};

                for (String cell : roomInfoTableData) {
                    boolean border = cell.length() == 7 && Objects.equals(cell.split("")[0], "#");
                    final TextView cellRoomInfoData = createTextViewCell(
                            CONTENT_CONTENT_TABLE_LAYOUT,
                            Gravity.END,
                            paddingRoomInfoDataCell,
                            border,
                            border ? color : "#FFFFFF",
                            border ? getContrastingColor(Color.parseColor(color)) : "#000000",
                            false,
                            smallTextSize,
                            cell);
                    // add cells to row
                    rowRoomInfoData.addView(cellRoomInfoData);
                }
                // add row to table
                tableRoomInfo.addView(rowRoomInfoData);

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
                trWindowTitleParams.setMargins(0, 0, 0, 0);
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
                trDoorTitleParams.setMargins(0, 0, 0, 0);
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
                        tvWindowWidthTitle.setTypeface(null, Typeface.BOLD);
                        tvWindowWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowWidthTitle.setText("Width");

                        final TextView tvWindowLengthTitle = new TextView(this);
                        tvWindowLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowLengthTitle.setGravity(Gravity.RIGHT);
                        tvWindowLengthTitle.setPadding(10, 20, 10, 20);
                        tvWindowLengthTitle.setTypeface(null, Typeface.BOLD);
                        tvWindowLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowLengthTitle.setText("Length");

                        final TextView tvWindowQuantityTitle = new TextView(this);
                        tvWindowQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowQuantityTitle.setGravity(Gravity.RIGHT);
                        tvWindowQuantityTitle.setPadding(10, 20, 10, 20);
                        tvWindowQuantityTitle.setTypeface(null, Typeface.BOLD);
                        tvWindowQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowQuantityTitle.setText("Quantity");

                        final TextView tvWindowTrimColorTitle = new TextView(this);
                        tvWindowTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvWindowTrimColorTitle.setGravity(Gravity.RIGHT);
                        tvWindowTrimColorTitle.setPadding(10, 20, 10, 20);
                        tvWindowTrimColorTitle.setTypeface(null, Typeface.BOLD);
                        tvWindowTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvWindowTrimColorTitle.setText("Trims");

                        // create row
                        final TableRow trWindowInfoTitle = new TableRow(this);
//                        trWindowInfoTitle.setId(i + 1);
                        TableLayout.LayoutParams trWindowInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trWindowInfoParamsTitle.setMargins(0, 0, 0, 0);
                        trWindowInfoTitle.setPadding(0, 0, 0, 0);
                        trWindowInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                        trWindowInfoTitle.setLayoutParams(trWindowInfoParamsTitle);

                        // add cells to row
                        trWindowInfoTitle.addView(tvWindowWidthTitle);
                        trWindowInfoTitle.addView(tvWindowLengthTitle);
                        trWindowInfoTitle.addView(tvWindowQuantityTitle);
                        trWindowInfoTitle.addView(tvWindowTrimColorTitle);

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
                            tvWindowWidth.setPadding(10, 5, 10, 5);
                            tvWindowWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowWidth.setText(String.valueOf(widthWindow / 12 + "' " + widthWindow % 12 + "\""));

                            final TextView tvWindowLength = new TextView(this);
                            tvWindowLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowLength.setGravity(Gravity.RIGHT);
                            tvWindowLength.setPadding(10, 5, 10, 5);
                            tvWindowLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowLength.setText(String.valueOf(lengthWindow / 12 + "' " + lengthWindow % 12 + "\""));

                            final TextView tvWindowQuantity = new TextView(this);
                            tvWindowQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowQuantity.setGravity(Gravity.RIGHT);
                            tvWindowQuantity.setPadding(10, 5, 10, 5);
                            tvWindowQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowQuantity.setText(String.valueOf(quantityWindow + " pcs."));

                            final TextView tvWindowTrimColor = new TextView(this);
                            tvWindowTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvWindowTrimColor.setGravity(Gravity.RIGHT);
                            tvWindowTrimColor.setPadding(10, 5, 10, 5);
                            tvWindowTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvWindowTrimColor.setText(colorWindow + " - " + String.valueOf(widthTrimWindow + "\""));

//                            tvWindowTrimColor.setTextColor(getContrastingColor(Color.parseColor(colorWindow)));
                            tvWindowTrimColor.setBackgroundResource(R.drawable.border);
                            GradientDrawable drawableWindow = (GradientDrawable) tvWindowTrimColor.getBackground();
                            drawableWindow.setColor(Color.parseColor(colorWindow));

                            // create row
                            final TableRow trWindowInfo = new TableRow(this);
//                        trWindowInfo.setId(i + 1);
                            TableLayout.LayoutParams trWindowInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            trWindowInfoParams.setMargins(0, 0, 0, 0);
                            trWindowInfo.setPadding(0, 10, 0, 10);
                            trWindowInfo.setLayoutParams(trWindowInfoParams);

                            // add cells to row
                            trWindowInfo.addView(tvWindowWidth);
                            trWindowInfo.addView(tvWindowLength);
                            trWindowInfo.addView(tvWindowQuantity);
                            trWindowInfo.addView(tvWindowTrimColor);

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
                        tvDoorWidthTitle.setTypeface(null, Typeface.BOLD);
                        tvDoorWidthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorWidthTitle.setText("Width");

                        final TextView tvDoorLengthTitle = new TextView(this);
                        tvDoorLengthTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorLengthTitle.setGravity(Gravity.RIGHT);
                        tvDoorLengthTitle.setPadding(10, 20, 10, 20);
                        tvDoorLengthTitle.setTypeface(null, Typeface.BOLD);
                        tvDoorLengthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorLengthTitle.setText("Length");

                        final TextView tvDoorQuantityTitle = new TextView(this);
                        tvDoorQuantityTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorQuantityTitle.setGravity(Gravity.RIGHT);
                        tvDoorQuantityTitle.setPadding(10, 20, 10, 20);
                        tvDoorQuantityTitle.setTypeface(null, Typeface.BOLD);
                        tvDoorQuantityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorQuantityTitle.setText("Quantity");

                        final TextView tvDoorTrimColorTitle = new TextView(this);
                        tvDoorTrimColorTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tvDoorTrimColorTitle.setGravity(Gravity.RIGHT);
                        tvDoorTrimColorTitle.setPadding(10, 20, 10, 20);
                        tvDoorTrimColorTitle.setTypeface(null, Typeface.BOLD);
                        tvDoorTrimColorTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                        tvDoorTrimColorTitle.setText("Trims");

                        // create row
                        final TableRow trDoorInfoTitle = new TableRow(this);
//                        trDoorInfoTitle.setId(i + 1);
                        TableLayout.LayoutParams trDoorInfoParamsTitle = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        trDoorInfoParamsTitle.setMargins(0, 0, 0, 0);
                        trDoorInfoTitle.setPadding(0, 0, 0, 0);
                        trDoorInfoTitle.setBackgroundColor(Color.parseColor("#220000ff"));
                        trDoorInfoTitle.setLayoutParams(trDoorInfoParamsTitle);

                        // add cells to row
                        trDoorInfoTitle.addView(tvDoorWidthTitle);
                        trDoorInfoTitle.addView(tvDoorLengthTitle);
                        trDoorInfoTitle.addView(tvDoorQuantityTitle);
                        trDoorInfoTitle.addView(tvDoorTrimColorTitle);

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
                            tvDoorWidth.setPadding(10, 5, 10, 5);
                            tvDoorWidth.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorWidth.setText(String.valueOf(widthDoor / 12 + "' " + widthDoor % 12 + "\""));

                            final TextView tvDoorLength = new TextView(this);
                            tvDoorLength.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorLength.setGravity(Gravity.RIGHT);
                            tvDoorLength.setPadding(10, 5, 10, 5);
                            tvDoorLength.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorLength.setText(String.valueOf(lengthDoor / 12 + "' " + lengthDoor % 12 + "\""));

                            final TextView tvDoorQuantity = new TextView(this);
                            tvDoorQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorQuantity.setGravity(Gravity.RIGHT);
                            tvDoorQuantity.setPadding(10, 5, 10, 5);
                            tvDoorQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorQuantity.setText(String.valueOf(quantityDoor + " pcs."));

                            final TextView tvDoorTrimColor = new TextView(this);
                            tvDoorTrimColor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tvDoorTrimColor.setGravity(Gravity.RIGHT);
                            tvDoorTrimColor.setPadding(10, 5, 10, 5);
                            tvDoorTrimColor.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                            tvDoorTrimColor.setText(colorDoor + " - " + String.valueOf(widthTrimDoor + "\""));

//                            tvDoorTrimColor.setTextColor(getContrastingColor(Color.parseColor(colorDoor)));
                            tvDoorTrimColor.setBackgroundResource(R.drawable.border);
                            GradientDrawable drawableDoor = (GradientDrawable) tvDoorTrimColor.getBackground();
                            drawableDoor.setColor(Color.parseColor(colorDoor));

                            // create row
                            final TableRow trDoorInfo = new TableRow(this);
//                        trDoorInfo.setId(i + 1);
                            TableLayout.LayoutParams trDoorInfoParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            trDoorInfoParams.setMargins(0, 0, 0, 0);
                            trDoorInfo.setPadding(0, 10, 0, 10);
                            trDoorInfo.setLayoutParams(trDoorInfoParams);

                            // add cells to row
                            trDoorInfo.addView(tvDoorWidth);
                            trDoorInfo.addView(tvDoorLength);
                            trDoorInfo.addView(tvDoorQuantity);
                            trDoorInfo.addView(tvDoorTrimColor);

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
                trFooterParams.setMargins(0, 0, 0, 0);
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

                tblRoomsLayout.addView(tableRoomTitle, tbTitleTableParams);
                tblRoomsLayout.addView(tableRoomInfo, tbTablesParams);
                if (parsedRoom.length > 1) {
                    if (parsedRoom[1].length() > 1) {
                        tblRoomsLayout.addView(windowTitleLayout, tbTablesParams);
                        tblRoomsLayout.addView(windowInfoLayout, tbTablesParams);
                    }
                    if (parsedRoom[2].length() > 1) {
                        tblRoomsLayout.addView(doorTitleLayout, tbTablesParams);
                        tblRoomsLayout.addView(doorInfoLayout, tbTablesParams);
                    }
                }
                tblRoomsLayout.addView(roomFooterLayout, tbTablesParams);

//                        if (i > -1) {
//                            // add separator row
//                            final TableRow trSep = new TableRow(this);
//                            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//                            trParamsSep.setMargins(0, 0, 0, 0);
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
            editor.remove("MESSAGE");
            editor.commit();

        }
    }

}