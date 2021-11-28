package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.attendancemonitoringqrcode.PackageDataAccessObjects.DAODate;
import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterDate;
import com.example.attendancemonitoringqrcode.R;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubject extends AppCompatActivity {


    private String subjectId;
    private String subjectName;

    private List<_Date> dateList;

    private Intent intent;

    private Toolbar toolbar;

    private RecyclerView recyclerDate;

    private Button btnTakeAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_subject);

        intent = getIntent();

        subjectId = intent.getStringExtra("SubjectId");
        subjectName = intent.getStringExtra("SubjectName");

        setUpToolbar(subjectName);

        updateViews();

        setUpRecyclerView();

    }


    @Override
    protected void onResume() {
        super.onResume();

        setUpRecyclerView();

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);

        btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentManager.goToQRCodeScanner(ActivitySubject.this, true);

            }
        });

    }


    private void setUpRecyclerView() {

        recyclerDate = findViewById(R.id.recyclerDate);

//        dateList = DAODate.getAllDates(subjectId);
        dateList = new ArrayList<>();
        dateList.add(new _Date("", "January 01, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 02, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 03, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 04, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 05, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 06, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 07, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 08, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 09, 2020 (Wednesday)", 0, subjectId));
        dateList.add(new _Date("", "January 10, 2020 (Wednesday)", 0, subjectId));
        AdapterDate adapterDate = new AdapterDate(dateList, ActivitySubject.this/*, subjectName, true*/);

        recyclerDate.setLayoutManager(new LinearLayoutManager(ActivitySubject.this, LinearLayoutManager.VERTICAL, false));
        recyclerDate.setAdapter(adapterDate);

    }


}
