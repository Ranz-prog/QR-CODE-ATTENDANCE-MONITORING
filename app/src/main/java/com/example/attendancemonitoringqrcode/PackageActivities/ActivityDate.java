package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterStudent;
import com.example.attendancemonitoringqrcode.PackageTabs.DummyTab;
import com.example.attendancemonitoringqrcode.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDate extends AppCompatActivity {


    private String classId;
    private String subject;

    private String dateId;
    private String date;

    private AdapterStudent adapterStudent;

    private Toolbar toolbar;

    private TextView lblDate;

    private TabLayout tabLayoutDate;
    private ViewPager viewPagerDate;

    private RecyclerView recyclerStudent;

    private FloatingActionButton btnRecordAttendance;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_date);

        classId = SuperGlobals.currentClassId;
        subject = SuperGlobals.currentSubject;

        dateId = SuperGlobals.currentDateId;
        date = SuperGlobals.currentDate;

        setUpToolbar(subject);

        setUpTabLayout();

        updateViews();

    }


    @Override
    protected void onResume() {
        super.onResume();

        classId = SuperGlobals.currentClassId;
        subject = SuperGlobals.currentSubject;

        dateId = SuperGlobals.currentDateId;
        date = SuperGlobals.currentDate;

        setUpToolbar(subject);

        lblDate.setText(date);

        loadStudents(viewPagerDate.getCurrentItem());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_date, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btnEditDate:
                editDate();
                break;

            case R.id.btnDeleteDate:
                showConfirmationMessage();
                break;

        }

        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_EDIT_DATE && resultCode == RESULT_OK) {

            date = data.getStringExtra("NewDate");

            setUpToolbar(subject);

            lblDate.setText(date);

        }

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void setUpTabLayout() {

        tabLayoutDate = findViewById(R.id.tabLayoutDate);
        viewPagerDate = findViewById(R.id.viewPagerDate);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment("All", new DummyTab());
        viewPagerAdapter.addFragment("Present", new DummyTab());
        viewPagerAdapter.addFragment("Absent", new DummyTab());
//        viewPagerAdapter.addFragment("Changed Device", new DummyTab());

        viewPagerDate.setAdapter(viewPagerAdapter);
        tabLayoutDate.setupWithViewPager(viewPagerDate);

        viewPagerDate.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                loadStudents(position);

            }

        });

    }


    private void updateViews() {

        lblDate = findViewById(R.id.lblDate);

        btnRecordAttendance = findViewById(R.id.btnRecordAttendance);

        lblDate.setText(date);

        btnRecordAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentManager.goToQRCodeScanner(ActivityDate.this, false);

            }
        });

    }


    private void loadStudents(final int viewPagerCurrentItem) {

        SuperGlobals.studentList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_STUDENTS_ATTENDANCE,
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
                                        Integer.parseInt(jsonObject.getString("state"))
                                );

                                switch (viewPagerCurrentItem) {

                                    case 1:
                                        if (student.getState() == 1) {
                                            SuperGlobals.studentList.add(student);
                                        }
                                        break;

                                    case 2:
                                        if (student.getState() == 0) {
                                            SuperGlobals.studentList.add(student);
                                        }
                                        break;

                                    case 3:
                                        if (student.getState() == 2) {
                                            SuperGlobals.studentList.add(student);
                                        }
                                        break;

                                    default:
                                        SuperGlobals.studentList.add(student);

                                }

                            }

                            setUpRecyclerView();

                        } catch (JSONException e) {

                            Toast.makeText(ActivityDate.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ActivityDate.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("classId", classId);
                params.put("dateId", dateId);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityDate.this);
        requestQueue.add(stringRequest);

    }


    private void setUpRecyclerView() {

        recyclerStudent = findViewById(R.id.recyclerStudent);

        adapterStudent = new AdapterStudent(SuperGlobals.studentList, ActivityDate.this, true);

        recyclerStudent.setLayoutManager(new LinearLayoutManager(ActivityDate.this, LinearLayoutManager.VERTICAL, false));
        recyclerStudent.setAdapter(adapterStudent);

    }


    private void editDate() {

        Intent intent = IntentManager.goToCreateDate(ActivityDate.this, true);

        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_DATE);

    }


    private void showConfirmationMessage() {

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(ActivityDate.this, "Delete this date?", "You can not undo this action.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteDate();

            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();

    }


    private void deleteDate() {

        hideViewsAction();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_DELETE_DATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            Log.e("jeo", "1");

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

                params.put("dateId", dateId);

                Log.e("jeo", params.toString());

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityDate.this);
        requestQueue.add(stringRequest);

    }


    private void showError(String message) {

        Toast.makeText(ActivityDate.this, message, Toast.LENGTH_SHORT).show();

        showViewsAction();

    }


    private void hideViewsAction() {

        progressDialog = new ProgressDialog(ActivityDate.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

    }


    private void showViewsAction() {

        progressDialog.dismiss();

    }


}
