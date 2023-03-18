package com.example.paintcalculator;

import java.util.HashMap;
import java.util.Map;

public class ColorValueMap {
    private Map<String, Float> colorValueMap;

    public ColorValueMap() {
        colorValueMap = new HashMap<>();
    }

    public void addColorValue(String color, float value) {
        if (colorValueMap.containsKey(color)) {
            float oldValue = colorValueMap.get(color);
            colorValueMap.put(color, oldValue + value);
        } else {
            colorValueMap.put(color, value);
        }
    }

    public Map<String, Float> getColorValueMap() {
        return colorValueMap;
    }

    public float getValueForKey(String key) {
        if (colorValueMap.containsKey(key)) {
            return colorValueMap.get(key);
        } else {
            return 0.0f; // Or throw an exception or return null, depending on your needs
        }
    }
}