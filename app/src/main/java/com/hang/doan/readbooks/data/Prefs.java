package com.hang.doan.readbooks.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.hang.doan.readbooks.models.Font;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("story", Context.MODE_PRIVATE);
    }

    public void saveFont(Font font) {
        preferences.edit().putString("font", font.getName()).apply();
    }

    public Font getFont() {
        String name = preferences.getString("font", null);
        if (name == null) name = "Default";
        for (Font f : FontManager.getInstance().getFonts()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return new Font("Default", null, null);
    }

    public void saveBrightness(float value) {
        preferences.edit().putFloat("brightness", value).apply();
    }

    public float getBrightness() {
        return preferences.getFloat("brightness", 1f);
    }

    public void saveFontSize(int size) {
        preferences.edit().putInt("font_size", size).apply();
    }

    public int getFontSize() {
        return preferences.getInt("font_size", 14);
    }
}
