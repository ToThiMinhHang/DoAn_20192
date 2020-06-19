package com.hang.doan.readbooks.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Metrics {

    public static int dp(Context context, int dp) {
        return context.getResources().getDisplayMetrics().densityDpi * dp / DisplayMetrics.DENSITY_DEFAULT;
    }
}
