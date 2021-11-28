package com.example.attendancemonitoringqrcode.PackageActivities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attendancemonitoringqrcode.PackageManagers.AlertDialogManager;
import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.PreferencesManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private String username;
    private String password;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Toolbar toolbar;

    private EditText txtUsername;
    private EditText txtPassword;

    private Button btnLoginAsStudent;
    private TextView lblOr;
    private Button btnLoginAsTeacher;

    private ProgressBar progressLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PreferencesManager.SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String onlineStatus = PreferencesManager.retrieveFromSharedPreferences(MainActivity.this, PreferencesManager.ONLINE_STATUS);

        switch (onlineStatus) {

            case "student":
                SuperGlobals.currentId = sharedPreferences.getString(PreferencesManager.ID, "");
                SuperGlobals.currentStudentId = sharedPreferences.getString(PreferencesManager.STUDENT_ID, "");
                SuperGlobals.currentFirstName = sharedPreferences.getString(PreferencesManager.FIRST_NAME, "");
                SuperGlobals.currentMiddleName = sharedPreferences.getString(PreferencesManager.MIDDLE_NAME, "");
                SuperGlobals.currentLastName = sharedPreferences.getString(PreferencesManager.LAST_NAME, "");

                IntentManager.goToStudent(MainActivity.this);
                finish();
                break;

            case "faculty":
                SuperGlobals.currentId = sharedPreferences.getString(PreferencesManager.ID, "");
                SuperGlobals.currentFacultyId = sharedPreferences.getString(PreferencesManager.FACULTY_ID, "");
                SuperGlobals.currentFirstName = sharedPreferences.getString(PreferencesManager.FIRST_NAME, "");
                SuperGlobals.currentMiddleName = sharedPreferences.getString(PreferencesManager.MIDDLE_NAME, "");
                SuperGlobals.currentLastName = sharedPreferences.getString(PreferencesManager.LAST_NAME, "");

                IntentManager.goToTeacher(MainActivity.this);
                finish();
                break;

        }

//        setUpToolbar("Attendance Monitoring (QR Code)");

        updateViews();

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(MainActivity.this, "Confirm Exit", "Do you really want to quit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MainActivity.super.onBackPressed();

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

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        btnLoginAsStudent = findViewById(R.id.btnLoginAsStudent);
        lblOr = findViewById(R.id.lblOr);
        btnLoginAsTeacher = findViewById(R.id.btnLoginAsTeacher);

        progressLogin = findViewById(R.id.progressLogin);

        btnLoginAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login("student");

            }

        });

        btnLoginAsTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login("teacher");

            }

        });

    }


    private void login(final String loginAs) {

        hideViewsAction();

        username = txtUsername.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {

            showError("Please fill out all the fields.");
            return;

        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            String id = jsonObject.getString("id");
                            String firstName = jsonObject.getString("firstName");
                            String middleName = jsonObject.getString("middleName");
                            String lastName = jsonObject.getString("lastName");

                            editor.putString(PreferencesManager.ID, id);
                            editor.putString(PreferencesManager.FIRST_NAME, firstName);
                            editor.putString(PreferencesManager.MIDDLE_NAME, middleName);
                            editor.putString(PreferencesManager.LAST_NAME, lastName);

                            if (status.equals("success")) {

                                Toast.makeText(MainActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();

                                switch (loginAs) {

                                    case "student":
                                        String studentId = jsonObject.getString("studentId");

                                        editor.putString(PreferencesManager.STUDENT_ID, studentId);
                                        editor.putString(PreferencesManager.ONLINE_STATUS, "student");

                                        editor.apply();

                                        SuperGlobals.currentId = id;
                                        SuperGlobals.currentStudentId = studentId;
                                        SuperGlobals.currentFirstName = firstName;
                                        SuperGlobals.currentMiddleName = middleName;
                                        SuperGlobals.currentLastName = lastName;

                                        IntentManager.goToStudent(MainActivity.this);
                                        finish();
                                        break;

                                    case "teacher":
                                        String facultyId = jsonObject.getString("facultyId");

                                        editor.putString(PreferencesManager.FACULTY_ID, facultyId);
                                        editor.putString(PreferencesManager.ONLINE_STATUS, "faculty");

                                        editor.apply();

                                        SuperGlobals.currentId = id;
                                        SuperGlobals.currentFacultyId = facultyId;
                                        SuperGlobals.currentFirstName = firstName;
                                        SuperGlobals.currentMiddleName = middleName;
                                        SuperGlobals.currentLastName = lastName;

                                        IntentManager.goToTeacher(MainActivity.this);
                                        finish();
                                        break;

                                }

                            } else if (status.equals("failed")) {

                                showError("Invalid credentials.");

                            } else {

                                showError("Login failed.");

                            }

                        } catch (JSONException e) {

                            showError("Login failed.");
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showError("Login failed.");
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("password", password);
                params.put("loginAs", loginAs);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }


    private void showError(String message) {

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        showViewsAction();

    }


    private void hideViewsAction() {

        btnLoginAsStudent.setVisibility(View.GONE);
        lblOr.setVisibility(View.GONE);
        btnLoginAsTeacher.setVisibility(View.GONE);

        progressLogin.setVisibility(View.VISIBLE);

    }


    private void showViewsAction() {

        progressLogin.setVisibility(View.GONE);

        btnLoginAsStudent.setVisibility(View.VISIBLE);
        lblOr.setVisibility(View.VISIBLE);
        btnLoginAsTeacher.setVisibility(View.VISIBLE);

    }


}