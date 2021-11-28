package com.example.attendancemonitoringqrcode.PackageManagers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {


    private static DatabaseManager instance;

    private SQLiteDatabase db;
    private static SQLiteOpenHelper sqLiteOpenHelper;

    private AtomicInteger openCounter = new AtomicInteger();


    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {

        if (instance == null) {

            instance = new DatabaseManager();
            sqLiteOpenHelper = helper;

        }

    }


    public static synchronized DatabaseManager getInstance() {

        if (instance == null) {

            throw new IllegalStateException(DatabaseManager.class.getSimpleName() + " is not initialized. " +
                    "Call initializeInstance(...) method first.");

        }

        return instance;

    }


    public synchronized void openDatabase() {

        if (openCounter.incrementAndGet() == 1) {

            db = sqLiteOpenHelper.getWritableDatabase();

        }

    }


    public synchronized void closeDatabase() {

        if (openCounter.decrementAndGet() == 0) {

            db.close();

        }

    }


    public long createData(String tableName, ContentValues contentValues) {

        long lastCreatedId = -1;

        try {

            lastCreatedId = db.insert(tableName, null, contentValues);

        } catch (SQLiteException e) {

            e.printStackTrace();

        }

        return lastCreatedId;

    }


    public Cursor retrieveData(String query) {

        return db.rawQuery(query, null);

    }


    public long updateData(String table, ContentValues contentValues, String whereClause, String[] whereArgs) {

        long lastUpdatedId = -1;

        try {

            lastUpdatedId = db.update(table, contentValues, whereClause, whereArgs);

        } catch (SQLiteException e) {

            e.printStackTrace();

        }

        return lastUpdatedId;

    }


    public int deleteData(String table, String whereClause, String[] whereArgs) {

        int affectedRows = 0;

        try {

            affectedRows = db.delete(table, whereClause, whereArgs);

        } catch (SQLiteException e) {

            e.printStackTrace();

        }

        return affectedRows;

    }


}
