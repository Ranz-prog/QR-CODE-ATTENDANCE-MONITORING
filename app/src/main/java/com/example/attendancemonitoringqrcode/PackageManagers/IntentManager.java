package com.example.attendancemonitoringqrcode.PackageManagers;

import android.content.Context;
import android.content.Intent;

import com.example.attendancemonitoringqrcode.PackageActivities.ActivityClass;
import com.example.attendancemonitoringqrcode.PackageActivities.ActivityDate;
import com.example.attendancemonitoringqrcode.PackageActivities.ActivityStudent;
import com.example.attendancemonitoringqrcode.PackageActivities.ActivityStudent2;
import com.example.attendancemonitoringqrcode.PackageActivities.ActivitySubject;
import com.example.attendancemonitoringqrcode.PackageActivities.MainActivity;
import com.example.attendancemonitoringqrcode.PackageActivities.MyStudentDetails;
import com.example.attendancemonitoringqrcode.PackageActivities.QRCodeGenerator;
import com.example.attendancemonitoringqrcode.PackageForms.CreateClass;
import com.example.attendancemonitoringqrcode.PackageForms.CreateDate;
import com.example.attendancemonitoringqrcode.PackageForms.EditStudent;
import com.example.attendancemonitoringqrcode.PackageActivities.QRCodeScanner;
import com.example.attendancemonitoringqrcode.PackageActivities.Classes;


public class IntentManager {


    public static void goToQRCodeGenerator(Context context, String toolbarTitle, String qrCodeString) {

        Intent intent = new Intent(context, QRCodeGenerator.class);

        intent.putExtra("ToolbarTitle", toolbarTitle);
        intent.putExtra("QRCodeString", qrCodeString);

        context.startActivity(intent);

    }


    public static void goToQRCodeScanner(Context context, boolean isInRegistrationMode) {

        Intent intent = new Intent(context, QRCodeScanner.class);

        intent.putExtra("IsInRegistrationMode", isInRegistrationMode);

        context.startActivity(intent);

    }


    public static void goToMainActivity(Context context) {

        Intent intent = new Intent(context, MainActivity.class);

        context.startActivity(intent);

    }


    public static void goToTeacher(Context context) {

        Intent intent = new Intent(context, Classes.class);

        context.startActivity(intent);

    }


    public static Intent goToCreateClass(Context context, boolean isInEditMode) {

        Intent intent = new Intent(context, CreateClass.class);

        intent.putExtra("IsInEditMode", isInEditMode);

        if (!isInEditMode) {

            context.startActivity(intent);

            return null;

        } else {

            return intent;

        }

    }


    public static void goToActivityClass(Context context) {

        Intent intent = new Intent(context, ActivityClass.class);

        context.startActivity(intent);

    }


    public static Intent goToCreateDate(Context context, boolean isInEditMode) {

        Intent intent = new Intent(context, CreateDate.class);

        intent.putExtra("IsInEditMode", isInEditMode);

        if (!isInEditMode) {

            context.startActivity(intent);

            return null;

        } else {

            return intent;

        }

    }


    public static void goToActivityDate(Context context) {

        Intent intent = new Intent(context, ActivityDate.class);

        context.startActivity(intent);

    }


    public static void goToStudent(Context context) {

        Intent intent = new Intent(context, ActivityStudent.class);

        context.startActivity(intent);

    }


    public static void goToStudent2(Context context) {

        Intent intent = new Intent(context, ActivityStudent2.class);

        context.startActivity(intent);

    }


    public static void goToMyStudentDetails(Context context, String qrCodeString) {

        Intent intent = new Intent(context, MyStudentDetails.class);

        intent.putExtra("QRCodeString", qrCodeString);

        context.startActivity(intent);

    }


    public static void goToActivitySubject(Context context, String subjectId, String subjectName) {

        Intent intent = new Intent(context, ActivitySubject.class);

        intent.putExtra("SubjectId", subjectId);
        intent.putExtra("SubjectName", subjectName);

        context.startActivity(intent);

    }


    public static void goToEditStudent(Context context, String studentNumber, String firstName, String lastName) {

        Intent intent = new Intent(context, EditStudent.class);

        intent.putExtra("StudentNumber", studentNumber);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);

        context.startActivity(intent);

    }


}
