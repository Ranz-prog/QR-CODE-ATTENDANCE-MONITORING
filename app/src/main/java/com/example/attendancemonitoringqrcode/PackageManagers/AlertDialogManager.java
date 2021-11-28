package com.example.attendancemonitoringqrcode.PackageManagers;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogManager {


    public static void showAlertDialogBuilderWithDefaultButton(Context context, String title, String message, String neutralButtonTitle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(neutralButtonTitle, null);

        builder.show();

    }


    public static AlertDialog.Builder showAlertDialogBuilder(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        return builder;

    }



}
