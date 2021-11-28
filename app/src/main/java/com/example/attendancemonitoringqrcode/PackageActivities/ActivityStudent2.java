package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Class;
import com.example.attendancemonitoringqrcode.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActivityStudent2 extends AppCompatActivity {


    private String classId;
    private String subject;

    private String studentId;

    private String studentNumber;

    private String firstName;
    private String middleName;
    private String lastName;

    private String fullName;

    private Toolbar toolbar;

    private TextView lblStudentName;
    private TextView lblStudentNumber;

    private TextView lblNumberOfPresents;
    private TextView lblNumberOfAbsences;
    private TextView lblNumberOfMeetings;

    private ImageView imgQRCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_student2);

        classId = SuperGlobals.currentClassId;
        subject = SuperGlobals.currentSubject;

        studentId = SuperGlobals.currentStudent2Id;

        studentNumber = SuperGlobals.currentStudent2StudentNumber;

        firstName = SuperGlobals.currentStudent2FirstName;
        middleName = SuperGlobals.currentStudent2MiddleName;
        lastName = SuperGlobals.currentStudent2LastName;

        fullName = lastName + ", " + firstName + " " + middleName.charAt(0) + ".";

        setUpToolbar(subject);

        updateViews();

        loadStudentStatistics();

        String qrCodeMessage = String.format("_$0$%s, %s, %s, %s, %s$0$_", studentId, studentNumber, firstName, middleName, lastName);
        generateQRCode(qrCodeMessage);

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        lblStudentName = findViewById(R.id.lblStudentName);
        lblStudentNumber = findViewById(R.id.lblStudentNumber);

        lblNumberOfPresents = findViewById(R.id.lblNumberOfPresents);
        lblNumberOfAbsences = findViewById(R.id.lblNumberOfAbsences);
        lblNumberOfMeetings = findViewById(R.id.lblNumberOfMeetings);

        lblStudentName.setText(fullName);
        lblStudentNumber.setText(studentNumber);

    }


    private void loadStudentStatistics() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_STUDENT_STATISTICS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String numberOfPresents = jsonObject.getString("numberOfPresents");
                            String numberOfAbsences = jsonObject.getString("numberOfAbsences");
                            String numberOfMeetings = jsonObject.getString("numberOfMeetings");

                            lblNumberOfPresents.setText(numberOfPresents);
                            lblNumberOfAbsences.setText(numberOfAbsences);
                            lblNumberOfMeetings.setText(numberOfMeetings);

                        } catch (JSONException e) {

                            Toast.makeText(ActivityStudent2.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ActivityStudent2.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("studentId", studentId);
                params.put("classId", classId);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityStudent2.this);
        requestQueue.add(stringRequest);

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


}
