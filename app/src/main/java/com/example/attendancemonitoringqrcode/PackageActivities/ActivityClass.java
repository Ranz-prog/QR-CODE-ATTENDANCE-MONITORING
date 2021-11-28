package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.PackageManagers.ViewPagerAdapter;
import com.example.attendancemonitoringqrcode.PackageObjectModels.Student;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;
import com.example.attendancemonitoringqrcode.PackageTabs.Attendance;
import com.example.attendancemonitoringqrcode.PackageTabs.Students;
import com.example.attendancemonitoringqrcode.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ActivityClass extends AppCompatActivity {


    private String classId;
    private String className;
    private String section;

    private List<_Date> dateList;
    private List<Student> studentList;
    private List<ArrayList<String>> studentsAttendanceGridList;

    private StringBuilder attendanceTableString;

    private Toolbar toolbar;

    private TextView lblClassName;

    private TabLayout tabLayoutSubject;
    private ViewPager viewPagerSubject;

    private FloatingActionButton btnAddNewDate;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_class);

        classId = SuperGlobals.currentClassId;
        className = SuperGlobals.currentSubject;
        section = SuperGlobals.currentSection;

        setUpToolbar("Class");

        setUpTabLayout();

        updateViews();

    }


    @Override
    protected void onResume() {
        super.onResume();

        classId = SuperGlobals.currentClassId;
        className = SuperGlobals.currentSubject;

        lblClassName.setText(className);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_class, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btnEditClass:
                editClass();
                break;

            case R.id.btnDeleteClass:
                showConfirmationMessage();
                break;

            case R.id.btnExportAttendance:
                gatherData();
                break;

        }

        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_EDIT_CLASS && resultCode == RESULT_OK) {

            className = data.getStringExtra("NewClassName");

            lblClassName.setText(className);

        }

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void setUpTabLayout() {

        tabLayoutSubject = findViewById(R.id.tabLayoutSubject);
        viewPagerSubject = findViewById(R.id.viewPagerSubject);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment("Attendance", new Attendance());
        viewPagerAdapter.addFragment("Students", new Students());

        viewPagerSubject.setAdapter(viewPagerAdapter);
        tabLayoutSubject.setupWithViewPager(viewPagerSubject);

        viewPagerSubject.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        btnAddNewDate.setImageResource(R.drawable.ic_add_black_24dp);
                        break;

                    case 1:
                        btnAddNewDate.setImageResource(R.drawable.ic_camera_alt_black_24dp);
                        break;

                }

            }

        });

    }


    private void updateViews() {

        lblClassName = findViewById(R.id.lblClassName);

        btnAddNewDate = findViewById(R.id.btnAddNewDate);

        lblClassName.setText(className);

        btnAddNewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (viewPagerSubject.getCurrentItem()) {

                    case 0:
                        IntentManager.goToCreateDate(ActivityClass.this, false);
                        break;

                    case 1:
                        IntentManager.goToQRCodeScanner(ActivityClass.this, true);
                        break;

                }

            }
        });

    }


    private void editClass() {

        Intent intent = IntentManager.goToCreateClass(ActivityClass.this, true);

        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_CLASS);

    }


    private void showConfirmationMessage() {

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(ActivityClass.this, "Delete " + className + "?", "You can not undo this action.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteClass();

            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();

    }


    private void deleteClass() {

        hideViewsAction("Deleting...");

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_DELETE_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                showError("Process successful.");

                                finish();

                            } else if (status.equals("failed")) {

                                showError("Process failed.");

                            } else {

                                showError("Process failed.");

                            }

                        } catch (JSONException e) {

                            showError("Process failed.");
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showError("Process failed.");
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("classId", classId);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityClass.this);
        requestQueue.add(stringRequest);

    }


    private void showError(String message) {

        Toast.makeText(ActivityClass.this, message, Toast.LENGTH_SHORT).show();

        showViewsAction();

    }


    private void hideViewsAction(String progressDialogMessage) {

        progressDialog = new ProgressDialog(ActivityClass.this);
        progressDialog.setMessage(progressDialogMessage);
        progressDialog.show();

    }


    private void showViewsAction() {

        progressDialog.dismiss();

    }


    private void gatherData() {

        hideViewsAction("Generating table...");

        getDates();

    }


    private void getDates() {

        dateList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_DATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject2 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject2.getJSONArray("dates");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                _Date _date = new _Date(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("date"),
                                        Long.parseLong(jsonObject.getString("milliseconds")),
                                        jsonObject.getString("class_id")
                                );

                                dateList.add(_date);

                            }

                            getStudents();

                        } catch (JSONException e) {

                            Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("classId", classId);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityClass.this);
        requestQueue.add(stringRequest);

    }


    private void getStudents() {

        studentList = new ArrayList<>();
        studentsAttendanceGridList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_STUDENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject2 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject2.getJSONArray("students");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Student student = new Student(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("student_id"),
                                        jsonObject.getString("first_name"),
                                        jsonObject.getString("middle_name"),
                                        jsonObject.getString("last_name"),
                                        1
                                );

                                studentList.add(student);

                            }

                            getStudentsAttendance();

                        } catch (JSONException e) {

                            Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("classId", classId);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityClass.this);
        requestQueue.add(stringRequest);

    }


    private void getStudentsAttendance() {

        Log.e("jeo1", "jeo");

        Iterator iterator = studentList.iterator();

        while (iterator.hasNext()) {

            final Student student = (Student) iterator.next();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Urls.URL_GET_DATES_ATTENDANCE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObject2 = new JSONObject(response);
                                JSONArray jsonArray = jsonObject2.getJSONArray("students");

                                Log.e("jeo2", jsonArray.toString());

                                studentsAttendanceGridList.add(new ArrayList<String>());

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Log.e("jeo3", "jeo");

                                    String state = jsonObject.getString("state");
                                    String stateString;

                                    switch (state) {

                                        case "0":
                                            stateString  = Constants.ABSENT;
                                            break;

                                        default:
                                            stateString  = Constants.PRESENT;

                                    }

                                    studentsAttendanceGridList.get(studentsAttendanceGridList.size() - 1).add(stateString);

                                    Log.e("jeo4", "jeo");

                                }

                                generateTable();

                            } catch (JSONException e) {

                                Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                                Log.e("Error1", e.getMessage());

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ActivityClass.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error2", error.getMessage());

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();

                    params.put("classId", classId);
                    params.put("studentId", student.getId());

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(ActivityClass.this);
            requestQueue.add(stringRequest);

        }

    }


    private void generateTable() {

        // For debugging...
        for (int i = 0; i < studentsAttendanceGridList.size(); i++) {

            Log.e("jeo9", studentsAttendanceGridList.get(i).toString());

        }

        attendanceTableString = new StringBuilder();
        attendanceTableString.append("Name,");

        for (int i = 0; i < dateList.size(); i++) {

            _Date _date = dateList.get(i);

            String date = _date.getDate();
            int indexComma = date.indexOf(",");
            String dateFormat = date.substring(0, indexComma);

            attendanceTableString.append(dateFormat);

            if (i < dateList.size() - 1) {

                attendanceTableString.append(",");

            } else {

                attendanceTableString.append("\n");

            }

        }

        for (int i = 0; i < studentsAttendanceGridList.size(); i++) {

            Student student = studentList.get(i);
            String fullName = String.format("%s %s %s.", student.getLastName(), student.getFirstName(), student.getMiddleName().charAt(0));

            attendanceTableString.append(fullName);
            attendanceTableString.append(",");

            List<String> stateList = studentsAttendanceGridList.get(i);

            for (int j = 0; j < stateList.size(); j++) {

                attendanceTableString.append(stateList.get(j));

                attendanceTableString.append(",");

            }

            attendanceTableString.append("\n");

        }

        exportAttendance();

    }


    private void exportAttendance() {

        try {

            // Save
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((attendanceTableString.toString()).getBytes());
            out.close();

            progressDialog.dismiss();

            // Export
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.attendancemonitoringqrcode.fileprovider", fileLocation);

            Intent fileIntent = new Intent(Intent.ACTION_SEND);

            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, String.format("Attendance: %s (Section %s)", className, section));
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);

            startActivity(Intent.createChooser(fileIntent, "Send File"));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}
