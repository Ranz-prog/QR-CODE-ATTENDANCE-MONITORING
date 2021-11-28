package com.example.attendancemonitoringqrcode.PackageForms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attendancemonitoringqrcode.PackageManagers.Constants;
import com.example.attendancemonitoringqrcode.PackageManagers.PreferencesManager;
import com.example.attendancemonitoringqrcode.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class EditStudent extends AppCompatActivity {


    private Intent intent;

    private Toolbar toolbar;

    private EditText txtStudentNumber;
    private EditText txtFirstName;
    private EditText txtLastName;

    private Button btnSaveDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_edit_student);

        setUpToolbar("Edit Details");

        setUpTextViews();

        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDetails();

            }
        });

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void setUpTextViews() {

        intent = getIntent();

        txtStudentNumber = findViewById(R.id.txtStudentNumber);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);

        btnSaveDetails = findViewById(R.id.btnSaveDetails);

        txtStudentNumber.setText(intent.getStringExtra("StudentNumber"));
        txtFirstName.setText(intent.getStringExtra("FirstName"));
        txtLastName.setText(intent.getStringExtra("LastName"));

    }


    private void saveDetails() {

        String studentNumber = txtStudentNumber.getText().toString();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();

        if (studentNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {

            return;

        }

        try {

            String currentTimeMillis = String.valueOf(System.currentTimeMillis());

            String combinedStudentDetails = String.format("%s_#@#_%s_#@#_%s_#@#_%s", currentTimeMillis, studentNumber, firstName, lastName);

            Log.e("jeo", combinedStudentDetails);

            Bitmap bitmap = getQRCodeImage(combinedStudentDetails, 350, 350);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            SharedPreferences sharedPreferences = getSharedPreferences(PreferencesManager.SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(Constants.STUDENT_NUMBER, studentNumber);
            editor.putString(Constants.FIRST_NAME, firstName);
            editor.putString(Constants.LAST_NAME, lastName);

            if (studentNumber.equals("Not Set Yet") || firstName.equals("Not Set Yet") || lastName.equals("Not Set Yet")) {

                editor.putString(Constants.BITMAP_QR_CODE, "Not Set Yet");

            } else {

                editor.putString(Constants.BITMAP_QR_CODE, encoded);

            }

            editor.apply();

            finish();

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


}
