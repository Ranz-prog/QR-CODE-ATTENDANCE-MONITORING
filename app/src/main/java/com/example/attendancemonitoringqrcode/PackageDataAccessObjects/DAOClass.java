//package com.example.attendancemonitoringqrcode.PackageDataAccessObjects;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.util.Log;
//
//import com.example.attendancemonitoringqrcode.PackageManagers.Constants;
//import com.example.attendancemonitoringqrcode.PackageManagers.DatabaseManager;
//import com.example.attendancemonitoringqrcode.PackageObjectModels._Class;
//
//import java.util.ArrayList;
//
//public class DAOClass {
//
//
//    private static _Class cursorToClass(Cursor cursor) {
//
//        _Class _class = new _Class(
//                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CLASS_ID)),
//                cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CLASS_NAME))
//        );
//
//        return _class;
//
//    }
//
//
//    public static _Class addNewClass(_Class _class) {
//
//        DatabaseManager.getInstance().openDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(Constants.COLUMN_CLASS_NAME, _class.getClassName());
//
//        long lastCreatedId = DatabaseManager.getInstance().createData(Constants.TABLE_CLASS, contentValues);
//
//        if (lastCreatedId == -1) {
//
//            return null;
//
//        } else {
//
//            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_CLASSES + String.format(" WHERE %s = %s", Constants.COLUMN_CLASS_ID, lastCreatedId));
//            cursor.moveToFirst();
//
//            _class = cursorToClass(cursor);
//
//            cursor.close();
//
//            Log.e(Constants.SQLITE_PROCESS, String.format("Created _Class (Id: %s, Name: %s)", _class.getId(), _class.getClassName()));
//
//            return _class;
//
//        }
//
//    }
//
//
//    public static ArrayList<_Class> getAllClasses() {
//
//        ArrayList<_Class> classList = new ArrayList<>();
//
//        try {
//
//            DatabaseManager.getInstance().openDatabase();
//
//            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_CLASSES);
//
//            if (cursor.getCount() > 0) {
//
//                cursor.moveToFirst();
//
//                while (!cursor.isAfterLast()) {
//
//                    _Class _class = new _Class(
//                            cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CLASS_ID)),
//                            cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CLASS_NAME))
//                    );
//
//                    classList.add(_class);
//
//                    cursor.moveToNext();
//
//                }
//
//            }
//
//            cursor.close();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//
//        return classList;
//
//    }
//
//
//    public static _Class updateClass(String classId, _Class _class) {
//
//        DatabaseManager.getInstance().openDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(Constants.COLUMN_CLASS_NAME, _class.getClassName());
//
//        long lastUpdatedId = DatabaseManager.getInstance().updateData(Constants.TABLE_CLASS, contentValues, String.format("%s = ?", Constants.COLUMN_CLASS_ID), new String[] {classId});
//
//        if (lastUpdatedId == -1) {
//
//            return null;
//
//        } else {
//
//            Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_CLASSES + String.format(" WHERE %s = %s", Constants.COLUMN_CLASS_ID, classId));
//            cursor.moveToFirst();
//
//            _class = cursorToClass(cursor);
//
//            cursor.close();
//
//            Log.e(Constants.SQLITE_PROCESS, String.format("Updated _Class (Id: %s, Name: %s)", _class.getId(), _class.getClassName()));
//
//            return _class;
//
//        }
//
//    }
//
//
//    public static _Class deleteClass(String classId) {
//
//        DatabaseManager.getInstance().openDatabase();
//
//        Cursor cursor = DatabaseManager.getInstance().retrieveData(Constants.RETRIEVE_ALL_CLASSES + String.format(" WHERE %s = %s", Constants.COLUMN_CLASS_ID, classId));
//        cursor.moveToFirst();
//
//        _Class _class = cursorToClass(cursor);
//
//        cursor.close();
//
//        int affectedRows = DatabaseManager.getInstance().deleteData(Constants.TABLE_CLASS, String.format("%s = ?", Constants.COLUMN_CLASS_ID), new String[] {classId});
//
//        if (affectedRows < 0) {
//
//            return null;
//
//        } else {
//
//            DAODate.deleteAllDates(classId);
//
//            Log.e(Constants.SQLITE_PROCESS, String.format("Deleted _Class (Id: %s, Name: %s)", _class.getId(), _class.getClassName()));
//
//            return _class;
//
//        }
//
//    }
//
//
//}
