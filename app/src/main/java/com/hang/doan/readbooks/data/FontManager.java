package com.hang.doan.readbooks.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hang.doan.readbooks.models.Font;
import com.hang.doan.readbooks.utils.AssetsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class FontManager {

    private static FontManager instance;

    public static FontManager getInstance() {
        return instance;
    }

    private List<Font> fonts;
    private FontManager() {}

    public static void init(Context context) {
        instance = new FontManager();
        String json = AssetsUtils.readFile(context, "fonts/fonts.json");
        instance.fonts = new Gson().fromJson(json, new TypeToken<List<Font>>(){}.getType());
        instance.fonts.add(0, new Font("Default", null, null));
    }

    public List<Font> getFonts() {
        return fonts;
    }
}
