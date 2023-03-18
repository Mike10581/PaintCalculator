package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.DecimalFormat;
import java.util.Map;

public class CalculateResultActivity extends AppCompatActivity {
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

    int bigTextSize = 70, smallTextSize = 50, mediumTextSize = 60;
    String titleBackgroundColor = "#FF363636";
    String titleTextColor = "#FFFFFFFF";
    String descriptionTitleBackgroundColor = "#FFCACACA";
    String roomsAddonsBackgroundColor = "#220000FF";
    String descriptionTitleTextColor = "#000000";

    public static String getContrastingColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int average = (red + green + blue) / 3;
        return (average >= 128) ? "#000000" : "#FFFFFF";
    }

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

    public TableRow createRowSeparator(int numberCells, String color, int rowHeight) {

        final TableRow rowSeparator = createTableRow(
                PARENT_CONTENT_TABLE_LAYOUT,
                marginsRow,
                paddingRow);

        TextView cellSeparator = new TextView(this);
        TableRow.LayoutParams cellSeparatorLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        cellSeparatorLayoutParams.span = numberCells;
        cellSeparator.setLayoutParams(cellSeparatorLayoutParams);
        cellSeparator.setBackgroundColor(Color.parseColor(color));
        cellSeparator.setHeight(rowHeight);
        rowSeparator.addView(cellSeparator);

        return rowSeparator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_result);

        ColorValueMap resultObjectColorSqFT = new ColorValueMap();

        // It's always a good idea to follow the manufacturer's recommendations for the number of coats needed for their specific paint product,
        // as they may have different requirements based on the paint's formulation and intended use.
        // In general, most interior painting projects require at least two coats of paint for a smooth and even finish.
        int numberOfCoats = 2;

        // The median coverage rate of paint for inside painting can vary depending on the type of paint and its quality.
        // However, a typical coverage rate for interior paint is around 350 to 400 square feet per gallon, assuming a smooth surface and even application.
        int coverageRateOfPaint = 350; // sq.ft. / gal

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TableLayout tblResultLayout = findViewById(R.id.tblResultLayout);
        tblResultLayout.setStretchAllColumns(true);
        tblResultLayout.removeAllViews();

        TableLayout.LayoutParams tbTitleTableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        tbTitleTableParams.setMargins(10, 60, 10, 0);

        TableLayout.LayoutParams tbTablesParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        tbTablesParams.setMargins(10, 0, 10, 0);

        // create table for room info
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
        String[] roomInfoTableTitles = {"Room", "SQ. FT.", "Color", "Gal"};

        int numberOfInfoTableTitles = roomInfoTableTitles.length;

        for (int i = 0; i < numberOfInfoTableTitles; i++) {

            final TextView cellRoomInfoTitle = createTextViewCell(
                    CONTENT_CONTENT_TABLE_LAYOUT,
                    i == 0 ? Gravity.START : i == 2 ? Gravity.CENTER_HORIZONTAL : Gravity.END,
                    paddingRoomInfoCell,
                    false,
                    titleBackgroundColor,
                    titleTextColor,
                    true,
                    smallTextSize,
                    roomInfoTableTitles[i]);
            // add cells to row
            rowRoomInfoTitle.addView(cellRoomInfoTitle);
        }
        // add row to table
        tableRoomInfo.addView(rowRoomInfoTitle);

        // add tables into main table
        tblResultLayout.addView(tableRoomInfo, tbTitleTableParams);


        String[] parsedRooms = sharedPref.getString("ROOMS", "").split("~");
        int numberOfRooms = parsedRooms.length;
        for (int j = 0; j < numberOfRooms; j++) {
            String[] parsedRoom = parsedRooms[j].split(",");
            String[] parsedInfo = parsedRoom[0].split(":");

            String roomNumber = parsedInfo[0];
            float roomWidthFT = Float.parseFloat(parsedInfo[1]) / 12;
            float roomLengthFT = Float.parseFloat(parsedInfo[2]) / 12;
            float roomHeightFT = Float.parseFloat(parsedInfo[3]) / 12;
            String roomColor = parsedInfo[4];
            String roomTitle = parsedInfo[5];

            float roomWallsSqFt = (roomWidthFT * roomHeightFT + roomLengthFT * roomHeightFT) * 2;
            float roomPaintGal = roomWallsSqFt * numberOfCoats / coverageRateOfPaint;

            // Add a new key-value pair (save color -> gallons)
            resultObjectColorSqFT.addColorValue(roomColor, roomWallsSqFt);
            resultObjectColorSqFT.addColorValue("CEILING", roomWidthFT * roomLengthFT);

            //TODO ceiling color add at the end of table
//            float ceilingPaintGal = ceilingSqFt * numberOfCoats / coverageRateOfPaint;


            if (parsedRoom.length > 1) {
                if (parsedRoom[1].length() > 1) {

                    // split windows
                    String[] window = parsedRoom[1].split("!");
                    int numberOfWindows = window.length;

                    for (int i = 0; i < numberOfWindows; i++) {
                        String[] parsedWindowInfo = window[i].split(":");

                        float windowWidthFT = Float.parseFloat(parsedWindowInfo[0]) / 12;
                        float windowHeightFT = Float.parseFloat(parsedWindowInfo[1]) / 12;
                        int windowQuantity = Integer.parseInt(parsedWindowInfo[2]);
                        float windowTrimWidthFT = Float.parseFloat(parsedWindowInfo[3]) / 12;
                        String colorWindow = parsedWindowInfo[4];

                        // subtract windows area from walls area
                        resultObjectColorSqFT.addColorValue(roomColor, -windowWidthFT * windowHeightFT * windowQuantity);
                        // add trims area to paint color
                        resultObjectColorSqFT.addColorValue(colorWindow, (windowWidthFT + windowHeightFT + 2 * windowTrimWidthFT) * 2 * windowTrimWidthFT);
                    }
                }

                if (parsedRoom[2].length() > 1) {

                    // split doors
                    String[] door = parsedRoom[2].split("!");
                    int numberOfDoors = door.length;

                    for (int i = 0; i < numberOfDoors; i++) {
                        String[] parsedDoorInfo = door[i].split(":");

                        float doorWidthFT = Float.parseFloat(parsedDoorInfo[0]) / 12;
                        float doorHeightFT = Float.parseFloat(parsedDoorInfo[1]) / 12;
                        int doorQuantity = Integer.parseInt(parsedDoorInfo[2]);
                        float doorTrimWidthFT = Float.parseFloat(parsedDoorInfo[3]) / 12;
                        String colorDoor = parsedDoorInfo[4];

                        // subtract doors area from walls area
                        resultObjectColorSqFT.addColorValue(roomColor, -doorWidthFT * doorHeightFT * doorQuantity);
                        // add doors area to paint color
                        resultObjectColorSqFT.addColorValue(colorDoor, doorWidthFT * doorHeightFT * doorQuantity);
                        // add trims area to paint color
                        resultObjectColorSqFT.addColorValue(colorDoor, (doorWidthFT + doorHeightFT * 2 + 2 * doorTrimWidthFT) * doorTrimWidthFT);

                    }
                }


            }


            // create row
            final TableRow rowRoomInfoData = createTableRow(
                    PARENT_CONTENT_TABLE_LAYOUT,
                    marginsRow,
                    paddingRowData
            );

            // create cells
            String[] roomInfoTableData = {
                    roomNumber + ". " + roomTitle,
                    String.valueOf(Math.round(roomWallsSqFt)),
                    roomColor,
                    String.valueOf(Float.valueOf(decimalFormat.format(roomPaintGal)))
            };

            int numberOfInfoTableData = roomInfoTableData.length;

            for (int i = 0; i < numberOfInfoTableData; i++) {
                boolean border = roomInfoTableData[i].charAt(0) == '#';
                final TextView cellRoomInfoData = createTextViewCell(
                        CONTENT_CONTENT_TABLE_LAYOUT,
                        i == 0 ? Gravity.START : i == 2 ? Gravity.CENTER_HORIZONTAL : Gravity.END,
                        paddingRoomInfoDataCell,
                        border,
                        border ? roomColor : "#FFFFFF",
                        border ? getContrastingColor(Color.parseColor(roomColor)) : "#000000",
                        false,
                        smallTextSize,
                        roomInfoTableData[i]
                );
                // add cells to row
                rowRoomInfoData.addView(cellRoomInfoData);
            }
            // add row to table
            tableRoomInfo.addView(rowRoomInfoData);
            //TODO add ceiling row somewhere here

            // add row separator
            tableRoomInfo.addView(createRowSeparator(roomInfoTableTitles.length, descriptionTitleBackgroundColor, j < numberOfRooms - 1 ? 4 : 32));
        }


        // create table for paint summary
        TableLayout tablePaintSummary = new TableLayout(this);
        tablePaintSummary.setStretchAllColumns(true);
        tablePaintSummary.removeAllViews();
        // create row
        final TableRow rowPaintSummary = createTableRow(
                PARENT_CONTENT_TABLE_LAYOUT,
                marginsRow,
                paddingRow
        );
        // create cells
        String[] paintSummaryTitles = {"Color", "SQ. FT.", "Two coats", "+15%"};

        int numberOfPaintSummaryTitles = paintSummaryTitles.length;

        for (int i = 0; i < numberOfPaintSummaryTitles; i++) {
            final TextView cellPaintSummaryTitle = createTextViewCell(
                    CONTENT_CONTENT_TABLE_LAYOUT,
                    i == 0 ? Gravity.START : i == 2 ? Gravity.CENTER_HORIZONTAL : Gravity.END,
                    paddingRoomInfoCell,
                    false,
                    titleBackgroundColor,
                    titleTextColor,
                    true,
                    smallTextSize,
                    paintSummaryTitles[i]);
            // add cells to row
            rowPaintSummary.addView(cellPaintSummaryTitle);
        }
        // add row to table
        tablePaintSummary.addView(rowPaintSummary);


        // Get all key-value pairs
        Map<String, Float> allColorValuePairs = resultObjectColorSqFT.getColorValueMap();
        for (Map.Entry<String, Float> entry : allColorValuePairs.entrySet()) {


            String color = entry.getKey();

            if (!color.equals("CEILING")) {
                float value = entry.getValue();
                float paintGal = value * numberOfCoats / coverageRateOfPaint;

                // create row
                final TableRow rowPaintSummaryData = createTableRow(
                        PARENT_CONTENT_TABLE_LAYOUT,
                        marginsRow,
                        paddingRowData
                );

                // create cells
                String[] paintSummaryData = {
                        color,
                        String.valueOf(Math.round(value)),
                        String.valueOf(Float.valueOf(decimalFormat.format(paintGal))) + " Gal.",
                        String.valueOf((int) Math.ceil(paintGal * 1.15)) + " Gal."
                };

                int numberOfPaintSummaryData = paintSummaryData.length;

                for (int i = 0; i < numberOfPaintSummaryData; i++) {
                    boolean border = paintSummaryData[i].charAt(0) == '#';
                    final TextView cellPaintSummaryData = createTextViewCell(
                            CONTENT_CONTENT_TABLE_LAYOUT,
                            i == 0 ? Gravity.START : i == 2 ? Gravity.CENTER_HORIZONTAL : Gravity.END,
                            paddingRoomInfoDataCell,
                            border,
                            border ? color : "#FFFFFF",
                            border ? getContrastingColor(Color.parseColor(color)) : "#000000",
                            false,
                            smallTextSize,
                            paintSummaryData[i]);
                    // add cells to row
                    rowPaintSummaryData.addView(cellPaintSummaryData);
                }
                // add row to table
                tablePaintSummary.addView(rowPaintSummaryData);

                // add row separator
                tablePaintSummary.addView(createRowSeparator(roomInfoTableTitles.length, descriptionTitleBackgroundColor, 4));
            }
        }


        //row for ceiling only
        float valueForKeyCeiling = resultObjectColorSqFT.getValueForKey("CEILING");
        float paintGalCeiling = valueForKeyCeiling * numberOfCoats / coverageRateOfPaint;
        // create row
        final TableRow rowPaintSummaryDataForCeiling = createTableRow(
                PARENT_CONTENT_TABLE_LAYOUT,
                marginsRow,
                paddingRowData
        );
        // create cells
        String[] paintSummaryDataCeiling = {
                "CEILING",
                String.valueOf(Math.round(valueForKeyCeiling)),
                String.valueOf(Float.valueOf(decimalFormat.format(paintGalCeiling))) + " Gal.",
                String.valueOf((int) Math.ceil(paintGalCeiling * 1.15)) + " Gal."
        };

        int numberOfPaintSummaryData = paintSummaryDataCeiling.length;

        for (int i = 0; i < numberOfPaintSummaryData; i++) {
            final TextView cellPaintSummaryData = createTextViewCell(
                    CONTENT_CONTENT_TABLE_LAYOUT,
                    i == 0 ? Gravity.START : i == 2 ? Gravity.CENTER_HORIZONTAL : Gravity.END,
                    paddingRoomInfoDataCell,
                    false,
                    "#FFFFFF",
                    "#000000",
                    false,
                    smallTextSize,
                    paintSummaryDataCeiling[i]);
            // add cells to row
            rowPaintSummaryDataForCeiling.addView(cellPaintSummaryData);
        }
        // add row to table
        tablePaintSummary.addView(rowPaintSummaryDataForCeiling);

        // add row separator
        tablePaintSummary.addView(createRowSeparator(roomInfoTableTitles.length, descriptionTitleBackgroundColor, 32));


        // add paint summary table into main table
        tblResultLayout.addView(tablePaintSummary, tbTitleTableParams);


    }
}