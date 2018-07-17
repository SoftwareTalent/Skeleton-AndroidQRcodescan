package com.luke.skeleton.utils;

import android.graphics.drawable.Drawable;

import com.luke.skeleton.app.CustomApp;

public class ResourcesUtils {

    public static float getScaledDensity(){
        return CustomApp.getContext().getResources().getDisplayMetrics().scaledDensity;
    }

    public static String getString(int resId) {
        return CustomApp.getContext().getString(resId);
    }

    public static String getString(int resId, Object... args) {
        return CustomApp.getContext().getString(resId, args);
    }

    public static float getDimension(int resId) {
        return CustomApp.getContext().getResources().getDimension(resId);
    }

    public static int getColor(int resId) {
        return CustomApp.getContext().getResources().getColor(resId);
    }

    public static Drawable getDrawable(int resId) {
        return CustomApp.getContext().getResources().getDrawable(resId);
    }

}
