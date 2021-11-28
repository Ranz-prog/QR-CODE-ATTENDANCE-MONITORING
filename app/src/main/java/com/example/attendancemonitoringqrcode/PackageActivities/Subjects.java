package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageObjectModels.Subject;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterSubject;
import com.example.attendancemonitoringqrcode.R;

import java.util.ArrayList;
import java.util.List;

public class Subjects extends AppCompatActivity {


    List<Subject> subjectList;

    private Toolbar toolbar;

    private RecyclerView recyclerSubject;

    private Button btnAddSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        setUpToolbar("Subjects");

        updateViews();

        setUpRecyclerView();

    }


    @Override
    protected void onResume() {
        super.onResume();

        setUpRecyclerView();

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
                String qrCodeString = "Jeofferson O. Dela Peña\n03-1617-03796";

                IntentManager.goToMyStudentDetails(Subjects.this, qrCodeString);
                break;

        }

        return true;

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        btnAddSubject = findViewById(R.id.btnAddSubject);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentManager.goToQRCodeScanner(Subjects.this, true);

            }
        });

    }


    private void setUpRecyclerView() {

        recyclerSubject = findViewById(R.id.recyclerSubject);

//        subjectList = DAOSubject.getAllSubjects();
        subjectList = new ArrayList<>();
        subjectList.add(new Subject("", "Dummy Subject 1"));
        subjectList.add(new Subject("", "Dummy Subject 2"));
        subjectList.add(new Subject("", "Dummy Subject 3"));
        subjectList.add(new Subject("", "Dummy Subject 4"));
        subjectList.add(new Subject("", "Dummy Subject 5"));
        subjectList.add(new Subject("", "Dummy Subject 6"));
        subjectList.add(new Subject("", "Dummy Subject 7"));
        subjectList.add(new Subject("", "Dummy Subject 8"));
        subjectList.add(new Subject("", "Dummy Subject 9"));
        subjectList.add(new Subject("", "Dummy Subject 10"));
        AdapterSubject adapterSubject = new AdapterSubject(subjectList, Subjects.this);

        recyclerSubject.setLayoutManager(new LinearLayoutManager(Subjects.this, LinearLayoutManager.VERTICAL, false));
        recyclerSubject.setAdapter(adapterSubject);

    }


}
