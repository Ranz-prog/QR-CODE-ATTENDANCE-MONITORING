package com.example.attendancemonitoringqrcode.PackageApplication;

import android.app.Application;

import com.example.attendancemonitoringqrcode.PackageManagers.DatabaseHelper;
import com.example.attendancemonitoringqrcode.PackageManagers.DatabaseManager;
import com.example.attendancemonitoringqrcode.PackageManagers.TypefaceUtil;

public class ClassApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/comfortaa_regular.ttf");
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

    }


}
