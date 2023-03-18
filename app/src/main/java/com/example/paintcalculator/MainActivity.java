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
        int[] marginsRow = {0, 0, 0, 0};
        int[] paddingRow = {0, 0, 0, 0};
        int[] paddingRowData = {0, 10, 0, 10};
        int[] paddingRoomTitleCell = {10, 20, 10, 20};
        int[] paddingRoomInfoCell = {10, 20, 10, 20};
        int[] paddingRoomInfoDataCell = {10, 5, 10, 5};
        int[] paddingRoomAddonsTitle = {10, 10, 10, 5};

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
                    boolean border = cell.charAt(0) == '#';
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
                TableLayout tableWindowTitle = new TableLayout(this);
                tableWindowTitle.setStretchAllColumns(true);
                tableWindowTitle.removeAllViews();
                // create row
                final TableRow rowWindowTitle = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRow);
                // create cell
                final TextView tableCellWindowTitle = createTextViewCell(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        Gravity.START,
                        paddingRoomAddonsTitle,
                        false,
                        roomsAddonsBackgroundColor,
                        "#000000", //--------------------------
                        true,
                        mediumTextSize,
                        "Windows:"
                );
                // add cell to row
                rowWindowTitle.addView(tableCellWindowTitle);
                // add row to table
                tableWindowTitle.addView(rowWindowTitle);

                // create table for windows info (size, trims width and color)
                TableLayout tableWindowInfo = new TableLayout(this);
                tableWindowInfo.setStretchAllColumns(true);
                tableWindowInfo.removeAllViews();



                //create table for doors title
                TableLayout tableDoorTitle = new TableLayout(this);
                tableDoorTitle.setStretchAllColumns(true);
                tableDoorTitle.removeAllViews();
                // create row
                final TableRow tableRowDoorTitle = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRow);
                // create cell
                final TextView tableCellDoorTitle = createTextViewCell(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        Gravity.START,
                        paddingRoomAddonsTitle,
                        false,
                        roomsAddonsBackgroundColor,
                        "#000000", //--------------------------
                        true,
                        mediumTextSize,
                        "Doors:"
                );
                // add cell to row
                tableRowDoorTitle.addView(tableCellDoorTitle);
                // add row to table
                tableDoorTitle.addView(tableRowDoorTitle);

                // create table for doors info (size, trims width and color)
                TableLayout tableDoorInfo = new TableLayout(this);
                tableDoorInfo.setStretchAllColumns(true);
                tableDoorInfo.removeAllViews();


                if (parsedRoom.length > 1) {
                    if (parsedRoom[1].length() > 1) {
                        // create row
                        final TableRow rowWindowInfoTitle = createTableRow(
                                PARENT_CONTENT_TABLE_LAYOUT,
                                marginsRow,
                                paddingRow
                        );
                        // create cells
                        String[] windowInfoTableTitles = {"Width", "Height", "Quantity", "Trim color", "W"};

                        for (String cell : windowInfoTableTitles) {

                            final TextView cellWindowInfoTitle = createTextViewCell(
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
                            rowWindowInfoTitle.addView(cellWindowInfoTitle);
                        }
                        // add row to table
                        tableWindowInfo.addView(rowWindowInfoTitle);

                        // split windows
                        String[] window = parsedRoom[1].split("!");
                        for (String el : window) {
                            String[] parsedWindowInfo = el.split(":");

                            int widthWindow = Integer.parseInt(parsedWindowInfo[0]);
                            int heightWindow = Integer.parseInt(parsedWindowInfo[1]);
                            int quantityWindow = Integer.parseInt(parsedWindowInfo[2]);
                            int widthTrimWindow = Integer.parseInt(parsedWindowInfo[3]);
                            String colorWindow = parsedWindowInfo[4];

                            // create row
                            final TableRow rowWindowInfo = createTableRow(
                                    PARENT_CONTENT_TABLE_LAYOUT,
                                    marginsRow,
                                    paddingRowData);
                            // create cells
                            String[] windowInfoTableData = {
                                    String.valueOf(widthWindow / 12 + "' " + widthWindow % 12 + "\""),
                                    String.valueOf(heightWindow / 12 + "' " + heightWindow % 12 + "\""),
                                    String.valueOf(quantityWindow + " pcs."),
                                    colorWindow,
                                    String.valueOf(widthTrimWindow + "\"")};

                            for (String cell : windowInfoTableData) {
                                boolean border = cell.charAt(0) == '#';
                                final TextView cellWindowInfoData = createTextViewCell(
                                        CONTENT_CONTENT_TABLE_LAYOUT,
                                        Gravity.END,
                                        paddingRoomInfoDataCell,
                                        border,
                                        border ? colorWindow : "#FFFFFF",
                                        border ? getContrastingColor(Color.parseColor(colorWindow)) : "#000000",
                                        false,
                                        smallTextSize,
                                        cell);
                                // add cells to row
                                rowWindowInfo.addView(cellWindowInfoData);
                            }
                            // add row to table
                            tableWindowInfo.addView(rowWindowInfo);
                        }
                    }

                    if (parsedRoom[2].length() > 1) {
                        // create row
                        final TableRow rowDoorInfoTitle = createTableRow(
                                PARENT_CONTENT_TABLE_LAYOUT,
                                marginsRow,
                                paddingRow
                        );
                        // create cells
                        String[] doorInfoTableTitles = {"Width", "Height", "Quantity", "Trim color", "W"};

                        for (String cell : doorInfoTableTitles) {

                            final TextView cellDoorInfoTitle = createTextViewCell(
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
                            rowDoorInfoTitle.addView(cellDoorInfoTitle);
                        }
                        // add row to table
                        tableDoorInfo.addView(rowDoorInfoTitle);

                        // split doors
                        String[] door = parsedRoom[2].split("!");
                        for (String el : door) {
                            String[] parsedDoorInfo = el.split(":");

                            int widthDoor = Integer.parseInt(parsedDoorInfo[0]);
                            int lengthDoor = Integer.parseInt(parsedDoorInfo[1]);
                            int quantityDoor = Integer.parseInt(parsedDoorInfo[2]);
                            int widthTrimDoor = Integer.parseInt(parsedDoorInfo[3]);
                            String colorDoor = parsedDoorInfo[4];

                            // create row
                            final TableRow rowDoorInfo = createTableRow(
                                    PARENT_CONTENT_TABLE_LAYOUT,
                                    marginsRow,
                                    paddingRowData);
                            // create cells
                            String[] doorInfoTableData = {
                                    String.valueOf(widthDoor / 12 + "' " + widthDoor % 12 + "\""),
                                    String.valueOf(lengthDoor / 12 + "' " + lengthDoor % 12 + "\""),
                                    String.valueOf(quantityDoor + " pcs."),
                                    colorDoor,
                                    String.valueOf(widthTrimDoor + "\"")
                            };

                            for (String cell : doorInfoTableData) {
                                boolean border = cell.charAt(0) == '#';
                                final TextView cellDoorInfoData = createTextViewCell(
                                        CONTENT_CONTENT_TABLE_LAYOUT,
                                        Gravity.END,
                                        paddingRoomInfoDataCell,
                                        border,
                                        border ? colorDoor : "#FFFFFF",
                                        border ? getContrastingColor(Color.parseColor(colorDoor)) : "#000000",
                                        false,
                                        smallTextSize,
                                        cell);
                                // add cells to row
                                rowDoorInfo.addView(cellDoorInfoData);
                            }
                            // add row to table
                            tableDoorInfo.addView(rowDoorInfo);
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
                        tblRoomsLayout.addView(tableWindowTitle, tbTablesParams);
                        tblRoomsLayout.addView(tableWindowInfo, tbTablesParams);
                    }
                    if (parsedRoom[2].length() > 1) {
                        tblRoomsLayout.addView(tableDoorTitle, tbTablesParams);
                        tblRoomsLayout.addView(tableDoorInfo, tbTablesParams);
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