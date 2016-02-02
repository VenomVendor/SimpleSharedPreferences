package com.venomvendor.sample.simplesharedpreferences.core;

import android.app.Application;

import com.venomvendor.library.SimpleSharedPreferences;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize here.
        SimpleSharedPreferences.initialize(this);
    }
}
