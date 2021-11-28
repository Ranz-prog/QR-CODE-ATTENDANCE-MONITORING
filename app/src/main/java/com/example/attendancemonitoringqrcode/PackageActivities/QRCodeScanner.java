package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attendancemonitoringqrcode.PackageManagers.AlertDialogManager;
import com.example.attendancemonitoringqrcode.PackageManagers.Constants;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.PackageObjectModels.Student;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private String dateId;
    private String classId;

    private boolean isInRegistrationMode;

    private String id, studentId;
    private String firstName, middleName, lastName;
    private String macAddress;

    private String title;

    private String qrCodeRawMessage;
    private List<String> qrCodeMessageAsList;
    private String qrCodeFeedbackMessage;

    private Student student;
    private int indexInLocalStudentList;

    private Intent intent;

    private ZXingScannerView zXingScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateId = SuperGlobals.currentDateId;
        classId = SuperGlobals.currentClassId;

        intent = getIntent();

        isInRegistrationMode = intent.getBooleanExtra("IsInRegistrationMode", false);

        startScanningQRs();

    }


    @Override
    protected void onPause() {
        super.onPause();

        stopScanningQRs();

    }


    @Override
    protected void onResume() {
        super.onResume();

        resumeScanningQRs();

    }


    @Override
    public void handleResult(Result result) {

        Log.e("jeo", "1");

        qrCodeRawMessage = result.getText();
        decryptMessage();

    }


    private void decryptMessage() {

        Log.e("jeo", "2");

        if (!qrCodeRawMessage.startsWith(Constants.QR_CODE_INITIAL_KEY) || !qrCodeRawMessage.endsWith(Constants.QR_CODE_FINAL_KEY)) {

            Log.e("jeo", "7");

            title = "Invalid QR Code";
            qrCodeFeedbackMessage = "You scanned a QR Code that is not from this app.";
            showDialog();

        } else {

            Log.e("jeo", "8");

            qrCodeRawMessage = qrCodeRawMessage.substring(Constants.QR_CODE_INITIAL_KEY.length(), qrCodeRawMessage.length() - Constants.QR_CODE_FINAL_KEY.length());

            Log.e("jeo", "11");

            qrCodeMessageAsList = new ArrayList<>(Arrays.asList(qrCodeRawMessage.split(Constants.QR_CODE_SPLITTER)));

            Log.e("jeo", "12");

            id = qrCodeMessageAsList.get(0);
            studentId = qrCodeMessageAsList.get(1);
            firstName = qrCodeMessageAsList.get(2);
            middleName = qrCodeMessageAsList.get(3);
            lastName = qrCodeMessageAsList.get(4);
//            macAddress = qrCodeMessageAsList.get(5);

            Log.e("jeo", "13");

            if (isInRegistrationMode) {

                Log.e("jeo", "9");

                registerStudent();

            } else {

                Log.e("jeo", "10");

                findStudentFromSubjectStudents();

            }

        }

    }


    private void registerStudent() {

        Log.e("jeo", "3");

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_ADD_STUDENT_TO_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                title = "Registered";
                                qrCodeFeedbackMessage = String.format("NEW STUDENT - %s, %s %s.", lastName, firstName, middleName.charAt(0));
                                showDialog();

                            } else if (status.equals("exists")) {

                                title = "Already Registered";
                                qrCodeFeedbackMessage = "This student is already a part of this class.";
                                showDialog();

                            } else if (status.equals("failed")) {

                                Toast.makeText(QRCodeScanner.this, "Process failed.", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {

                            Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("studentId", id);
                params.put("classId", classId);

                Log.e("jeo", params.toString());

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(QRCodeScanner.this);
        requestQueue.add(stringRequest);

    }


    private void findStudentFromSubjectStudents() {

        Log.e("jeo", "4");

        boolean someoneMatched = false;

        for (int i = 0; i < SuperGlobals.studentList.size(); i++) {

            student = SuperGlobals.studentList.get(i);

            if (student.getId().equals(id)) {

                someoneMatched = true;
                indexInLocalStudentList = i;
                break;

            }

        }

        if (!someoneMatched) {

            title = "Unregistered";
            qrCodeFeedbackMessage = "This student is not a part of this class.";
            showDialog();

        } else {

            // Check if the mac address changed...
            // to be continued...

            recordStudent(1);

        }

    }


    private void recordStudent(final int state) {

        Log.e("jeo", "5");

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_RECORD_STUDENT_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                SuperGlobals.studentList.get(indexInLocalStudentList).setState(state);

                                title = "Recorded";

                                switch (state) {

                                    case 1:
                                        qrCodeFeedbackMessage = String.format("PRESENT - %s, %s %s.", lastName, firstName, middleName.charAt(0));
                                        break;

                                    case 2:
                                        qrCodeFeedbackMessage = String.format("CHANGED DEVICE - %s, %s %s.", lastName, firstName, middleName.charAt(0));
                                        break;

                                }

                                showDialog();

                            } else if (status.equals("failed")) {

                                Toast.makeText(QRCodeScanner.this, "Process failed.", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {

                            Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(QRCodeScanner.this, "Process failed", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("state", String.valueOf(state));

                params.put("studentId", id);
                params.put("dateId", dateId);
                params.put("classId", classId);

                Log.e("jeo", params.toString());

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(QRCodeScanner.this);
        requestQueue.add(stringRequest);

    }


    private void showDialog() {

        Log.e("jeo", "6");

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(QRCodeScanner.this, title, qrCodeFeedbackMessage);

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                resumeScanningQRs();

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                resumeScanningQRs();

            }
        });

        builder.show();

    }


    private void startScanningQRs() {

        zXingScannerView = new ZXingScannerView(QRCodeScanner.this);
        setContentView(zXingScannerView);

    }


    private void resumeScanningQRs() {

        zXingScannerView.setResultHandler(QRCodeScanner.this);
        zXingScannerView.startCamera();

    }


    private void stopScanningQRs() {

        zXingScannerView.stopCamera();

    }


}
