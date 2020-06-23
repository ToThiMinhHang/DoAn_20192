package com.hang.doan.readbooks.models;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.Nullable;

public class Font {
    private String name;
    private String regular;
    private String bold;

    public Font(String name, String regular, String bold) {
        this.name = name;
        this.regular = regular;
        this.bold = bold;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Typeface getRegular(Context context) {
        return regular == null ? null : Typeface.createFromAsset(context.getAssets(), "fonts/" + regular);
    }

    @Nullable
    public Typeface getBold(Context context) {
        return bold == null ? null : Typeface.createFromAsset(context.getAssets(), "fonts/" + bold);
    }
}
