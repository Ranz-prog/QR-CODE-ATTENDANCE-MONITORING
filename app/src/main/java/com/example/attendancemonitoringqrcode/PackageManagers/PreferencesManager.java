package com.example.attendancemonitoringqrcode.PackageManagers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {


    public static final String SHARED_PREFS_NAME = "MySharedPrefs";

    public static final String ONLINE_STATUS = "OnlineStatus";

    public static final String ID = "Id";

    public static final String STUDENT_ID = "StudentId";
    public static final String FACULTY_ID = "FacultyId";

    public static final String FIRST_NAME = "FirstName";
    public static final String MIDDLE_NAME = "MiddleName";
    public static final String LAST_NAME = "LastName";


    public static void insertToPreferences(Context context, String key, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.apply();

    }


    public static String retrieveFromSharedPreferences(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);

        String result = sharedPreferences.getString(key, "");

        return result;

    }


}
