package com.example.attendancemonitoringqrcode.PackageDataAccessObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.attendancemonitoringqrcode.PackageManagers.Constants;
import com.example.attendancemonitoringqrcode.PackageManagers.DatabaseManager;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Class;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;

import java.util.ArrayList;

public class DAODate {


    private static _Date cursorToDate(Cursor cursor) {

        _Date _date = new _Date(
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_DATE)),
                cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_MILLISECONDS)),
                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_CLASS_ID))
        );

        return _date;

    }


    public static _Date addNewDate(_Date _date) {

        DatabaseManager.getInstance().openDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.COLUMN_DATE_DATE, _date.getDate());
        contentValues.put(Constants.COLUMN_DATE_MILLISECONDS, _date.getMilliseconds());
        contentValues.put(Constants.COLUMN_DATE_CLASS_ID, _date.getDateClassId());

        long lastCreatedId = DatabaseManager.getInstance().createData(Constants.TABLE_DATE, contentValues);

        if (lastCreatedId == -1) {

            return null;

        } else {

            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_DATES + String.format(" WHERE %s = %s", Constants.COLUMN_DATE_ID, lastCreatedId));
            cursor.moveToFirst();

            _date = cursorToDate(cursor);

            cursor.close();

            Log.e(Constants.SQLITE_PROCESS, String.format("Created _Date (Id: %s, Name: %s, DateClassId: %s)", _date.getId(), _date.getDate(), _date.getDateClassId()));

            return _date;

        }

    }


    public static ArrayList<_Date> getAllDates(String dateClassId) {

        ArrayList<_Date> dateList = new ArrayList<>();

        try {

            DatabaseManager.getInstance().openDatabase();

            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_DATES + String.format(" WHERE %s = %s ORDER BY %s", Constants.COLUMN_DATE_CLASS_ID, dateClassId, Constants.COLUMN_DATE_MILLISECONDS));

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {

                    _Date _date = new _Date(
                            cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_ID)),
                            cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_DATE)),
                            cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_MILLISECONDS)),
                            cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DATE_CLASS_ID))
                    );

                    dateList.add(_date);

                    cursor.moveToNext();

                }

            }

            cursor.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return dateList;

    }


    public static _Date updateDate(String dateId, _Date _date) {

        DatabaseManager.getInstance().openDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.COLUMN_DATE_DATE, _date.getDate());

        long lastUpdatedId = DatabaseManager.getInstance().updateData(Constants.TABLE_DATE, contentValues, String.format("%s = ?", Constants.COLUMN_DATE_ID), new String[] {dateId});

        if (lastUpdatedId == -1) {

            return null;

        } else {

            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_DATES + String.format(" WHERE %s = %s", Constants.COLUMN_DATE_ID, dateId));
            cursor.moveToFirst();

            _date = cursorToDate(cursor);

            cursor.close();

            Log.e(Constants.SQLITE_PROCESS, String.format("Updated _Date (Id: %s, Name: %s, DateClassId: %s)", _date.getId(), _date.getDate(), _date.getDateClassId()));

            return _date;

        }

    }


    public static _Date deleteDate(String dateId) {

        DatabaseManager.getInstance().openDatabase();

        Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_DATES + String.format(" WHERE %s = %s", Constants.COLUMN_DATE_ID, dateId));
        cursor.moveToFirst();

        _Date _date = cursorToDate(cursor);

        cursor.close();

        int affectedRows = DatabaseManager.getInstance().deleteData(Constants.TABLE_DATE, String.format("%s = ?", Constants.COLUMN_DATE_ID), new String[] {dateId});

        if (affectedRows < 0) {

            return null;

        } else {

//            DAOStudent.deleteAllStudents(classId);

            Log.e(Constants.SQLITE_PROCESS, String.format("Deleted _Class (Id: %s, Name: %s, DateClassId: %s)", _date.getId(), _date.getDate(), _date.getDateClassId()));

            return _date;

        }

    }


    public static boolean deleteAllDates(String dateClassId) {

        DatabaseManager.getInstance().openDatabase();

        int affectedRows = DatabaseManager.getInstance().deleteData(Constants.TABLE_DATE, String.format("%s = ?", Constants.COLUMN_DATE_CLASS_ID), new String[] {dateClassId});

        return affectedRows >= 0;

    }


}
