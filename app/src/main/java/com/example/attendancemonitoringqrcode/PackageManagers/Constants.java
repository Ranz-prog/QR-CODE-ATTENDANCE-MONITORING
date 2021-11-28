package com.example.attendancemonitoringqrcode.PackageManagers;

public class Constants {


    // Request Codes
    public static final int REQUEST_CODE_EDIT_CLASS = 1;
    public static final int REQUEST_CODE_EDIT_DATE = 2;

    // Exported State Titles
    public static final String PRESENT = "PRESENT";
    public static final String ABSENT = "ABSENT";
    public static final String CHANGED_DEVICE = "PRESENT";

    // QrCode
    public static final String QR_CODE_INITIAL_KEY = "_$0$";
    public static final String QR_CODE_SPLITTER = ", ";
    public static final String QR_CODE_FINAL_KEY = "$0$_";

    // Date
    public static final String DATE_FORMAT = "MMMM dd, yyyy (EEEE)";

    // SharedPrefs
    public static final String BITMAP_QR_CODE = "BitmapQRCode";
    public static final String STUDENT_NUMBER = "StudentNumber";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";

    // SQLite Database
    public static final String SQLITE_PROCESS = "SQLite Process";

    public static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "Main.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CLASS = "TableClass";

    public static final String COLUMN_CLASS_ID = "ID";
    public static final String COLUMN_CLASS_NAME = "ClassName";

    public static final String TABLE_DATE = "TableDate";

    public static final String COLUMN_DATE_ID = "ID";
    public static final String COLUMN_DATE_DATE = "Date";
    public static final String COLUMN_DATE_MILLISECONDS = "Milliseconds";
    public static final String COLUMN_DATE_CLASS_ID = "DateClassId";

    public static final String CREATE_TABLE_CLASS = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT NOT NULL" +
            ")", TABLE_CLASS, COLUMN_CLASS_ID, COLUMN_CLASS_NAME);

    public static final String RETRIEVE_ALL_CLASSES = String.format("SELECT * FROM %s", TABLE_CLASS);

    public static final String DROP_TABLE_CLASS = String.format("DROP TABLE IF EXISTS %s", TABLE_CLASS);

    public static final String CREATE_TABLE_DATE = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT NOT NULL, " +
            "%s TEXT NOT NULL, " +
            "%s TEXT NOT NULL" +
            ")", TABLE_DATE, COLUMN_DATE_ID, COLUMN_DATE_DATE, COLUMN_DATE_MILLISECONDS, COLUMN_DATE_CLASS_ID);

    public static final String RETRIEVE_ALL_DATES = String.format("SELECT * FROM %s", TABLE_DATE);

    public static final String DROP_TABLE_DATE = String.format("DROP TABLE IF EXISTS %s", TABLE_DATE);


}
