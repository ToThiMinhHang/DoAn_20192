package com.hang.doan.readbooks;

import android.app.Application;

import com.hang.doan.readbooks.data.FontManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontManager.init(this);
    }
}
