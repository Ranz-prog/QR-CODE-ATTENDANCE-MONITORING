package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attendancemonitoringqrcode.PackageManagers.AlertDialogManager;
import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.PreferencesManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class ActivityStudent extends AppCompatActivity {


    private String id, studentId;

    private String firstName, middleName, lastName;

    private String macAddress;

    private String fullName;

    private Toolbar toolbar;

    private TextView lblStudentName;
    private TextView lblStudentNumber;
    private TextView lblMacAddress;

    private ImageView imgQRCode;

    private Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_student);

        id = SuperGlobals.currentId;
        studentId = SuperGlobals.currentStudentId;

        firstName = SuperGlobals.currentFirstName;
        middleName = SuperGlobals.currentMiddleName;
        lastName = SuperGlobals.currentLastName;

        getMacAddress();

        fullName = lastName + ", " + firstName + " " + middleName.charAt(0) + ".";

        setUpToolbar("Welcome, " + firstName + "!");

        updateViews();

        String qrCodeMessage = String.format("_$0$%s, %s, %s, %s, %s$0$_", id, studentId, firstName, middleName, lastName);
        generateQRCode(qrCodeMessage);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btnLogout:
                logout();
                break;

        }

        return true;

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(ActivityStudent.this, "Confirm Exit", "Do you really want to quit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        builder.setNegativeButton("No", null);

        builder.show();

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        lblStudentName = findViewById(R.id.lblStudentName);
        lblStudentNumber = findViewById(R.id.lblStudentNumber);
//        lblMacAddress = findViewById(R.id.lblMacAddress);
//        btnLogout = findViewById(R.id.btnLogout);

        lblStudentName.setText(fullName);
        lblStudentNumber.setText(studentId);
//        lblMacAddress.setText(macAddress);

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                logout();
//
//            }
//        });

    }


    private void getMacAddress() {

        try {

            // get all the interfaces
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());

            //find network interface wlan0
            for (NetworkInterface networkInterface : all) {

                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;

                //get the hardware address (MAC) of the interface
                byte[] macBytes = networkInterface.getHardwareAddress();

                if (macBytes == null) {

                    macAddress = "";
                    return;

                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {

                    //gets the last byte of b
                    res1.append(Integer.toHexString(b & 0xFF) + ":");

                }

                if (res1.length() > 0) {

                    res1.deleteCharAt(res1.length() - 1);

                }

                macAddress = res1.toString();
                return;

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }


    private void generateQRCode(String qrCodeString) {

        imgQRCode = findViewById(R.id.imgQRCode);

        try {

            imgQRCode.setImageBitmap(getQRCodeImage(qrCodeString, 350, 350));

        } catch (WriterException e) {

            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());

        }

    }


    private Bitmap getQRCodeImage(String text, int width, int height) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

        return barcodeEncoder.createBitmap(bitMatrix);

    }


    private void logout() {

        PreferencesManager.insertToPreferences(ActivityStudent.this, PreferencesManager.ONLINE_STATUS, "");

        IntentManager.goToMainActivity(ActivityStudent.this);
        finish();

    }


}
