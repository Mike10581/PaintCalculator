package com.example.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selector);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        SeekBar barRed = (SeekBar) findViewById(R.id.barRed);
        SeekBar barGreen = (SeekBar) findViewById(R.id.barGreen);
        SeekBar barBlue = (SeekBar) findViewById(R.id.barBlue);

        String colorString = sharedPref.getString("SET_COLOR", "");
        if (!colorString.equals("")) {
            int color = Color.parseColor(colorString);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);

            barRed.setProgress(red, true);
            barGreen.setProgress(green, true);
            barBlue.setProgress(blue, true);
        }

        Button btnSelect = (Button) findViewById(R.id.btnSelectColor);
        Button btnCancel = (Button) findViewById(R.id.btnSelectColorCancel);

        TextView pickedColor = (TextView) findViewById(R.id.txtPickedColor);
        pickedColor.setBackgroundColor(Color.rgb(barRed.getProgress(), barGreen.getProgress(), barBlue.getProgress()));

        EditText editTextRed = findViewById(R.id.editRed);
        EditText editTextGreen = findViewById(R.id.editGreen);
        EditText editTextBlue = findViewById(R.id.editBlue);

        editTextRed.setText(Integer.toString(barRed.getProgress()));
        editTextGreen.setText(Integer.toString(barGreen.getProgress()));
        editTextBlue.setText(Integer.toString(barBlue.getProgress()));

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(3); // Maximum length is 3 digits

        editTextRed.setFilters(filterArray);
        editTextGreen.setFilters(filterArray);
        editTextBlue.setFilters(filterArray);

        editTextRed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextRed.setSelection(editTextRed.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // If the text is empty, leave the value as 0
                    barRed.setProgress(0);
                    return;
                }
                // Parse the text as a number and clamp it to the range between 0 and 255
                int value = 0;
                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    // Not a valid number, leave the value as 0
                }
                if (value < 0) {
                    value = 0;
                } else if (value > 255) {
                    value = 255;
                }
                if (value != Integer.parseInt(s.toString())) {
                    editTextRed.setText(Integer.toString(value));
                }
                barRed.setProgress(value);
            }
        });

        editTextGreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextGreen.setSelection(editTextGreen.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // If the text is empty, leave the value as 0
                    barGreen.setProgress(0);
                    return;
                }
                // Parse the text as a number and clamp it to the range between 0 and 255
                int value = 0;
                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    // Not a valid number, leave the value as 0
                }
                if (value < 0) {
                    value = 0;
                } else if (value > 255) {
                    value = 255;
                }
                if (value != Integer.parseInt(s.toString())) {
                    editTextGreen.setText(Integer.toString(value));
                }
                barGreen.setProgress(value);
            }
        });

        editTextBlue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextBlue.setSelection(editTextBlue.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // If the text is empty, leave the value as 0
                    barBlue.setProgress(0);
                    return;
                }
                // Parse the text as a number and clamp it to the range between 0 and 255
                int value = 0;
                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    // Not a valid number, leave the value as 0
                }
                if (value < 0) {
                    value = 0;
                } else if (value > 255) {
                    value = 255;
                }
                if (value != Integer.parseInt(s.toString())) {
                    editTextBlue.setText(Integer.toString(value));
                }
                barBlue.setProgress(value);
            }
        });

        barRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pickedColor.setBackgroundColor(Color.rgb(barRed.getProgress(), barGreen.getProgress(), barBlue.getProgress()));
                editTextRed.setText(Integer.toString(barRed.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        barGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pickedColor.setBackgroundColor(Color.rgb(barRed.getProgress(), barGreen.getProgress(), barBlue.getProgress()));
                editTextGreen.setText(Integer.toString(barGreen.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        barBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pickedColor.setBackgroundColor(Color.rgb(barRed.getProgress(), barGreen.getProgress(), barBlue.getProgress()));
                editTextBlue.setText(Integer.toString(barBlue.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                String redString = Integer.toHexString(barRed.getProgress());
                String greenString = Integer.toHexString(barGreen.getProgress());
                String blueString = Integer.toHexString(barBlue.getProgress());

                if (redString.length() == 1) {
                    redString = "0" + redString;
                }
                if (greenString.length() == 1) {
                    greenString = "0" + greenString;
                }
                if (blueString.length() == 1) {
                    blueString = "0" + blueString;
                }

                editor.putString("SET_COLOR", "#" + redString.toUpperCase() + greenString.toUpperCase() + blueString.toUpperCase());
                editor.commit();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}