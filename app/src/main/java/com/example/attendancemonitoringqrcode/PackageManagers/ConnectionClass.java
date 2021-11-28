package com.example.attendancemonitoringqrcode.PackageManagers;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {


    String classString = "net.sourceforge.jtds.jdbc.Driver";

    String url = "jdbc:mysql://192.168.1.71:8080/db_crud";

    String dbHost = "192.168.1.71";
    String dbUsername = "root";
    String dbPassword = "";
    String dbName = "db_crud";


    @SuppressLint("NewApi")
    public Connection CONN() {

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Connection conn = null;
        String connUrl = null;

        try {

            Class.forName(classString);

            connUrl = "jdbc:jtds:sqlserver://" + dbHost +";databaseName="+ dbName + ";user=" + dbUsername + ";password=" + dbPassword + ";";
            conn = DriverManager.getConnection(connUrl);

        } catch (SQLException sqlE) {

            Log.e("Error1", sqlE.getMessage());

        } catch (ClassNotFoundException classNotFoundE) {

            Log.e("Error2", classNotFoundE.getMessage());

        } catch (Exception e) {

            Log.e("Error3", e.getMessage());

        }

        return conn;

    }


}
