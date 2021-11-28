package com.example.attendancemonitoringqrcode.PackageForms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateClass extends AppCompatActivity {


    private String id;

    private boolean isInEditMode;
    private String classId;
    private String previousName;
    private String previousSection;

    private String subject;
    private String section;

    private Intent intent;

    private Toolbar toolbar;

    private EditText txtSubject;
    private EditText txtSection;

    private Button btnCreateClass;

    private ProgressBar progressCreateClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_create_class);

        id = SuperGlobals.currentId;

        intent = getIntent();
        isInEditMode = intent.getBooleanExtra("IsInEditMode", false);

        if (!isInEditMode) {

            setUpToolbar("Create Class");

        } else {

            classId = SuperGlobals.currentClassId;
            previousName = SuperGlobals.currentSubject;
            previousSection = SuperGlobals.currentSection;

            setUpToolbar("Edit Class");

        }

        updateViews();

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        txtSubject = findViewById(R.id.txtSubject);
        txtSection = findViewById(R.id.txtSection);

        btnCreateClass = findViewById(R.id.btnCreateClass);

        progressCreateClass = findViewById(R.id.progressCreateClass);

        if (isInEditMode) {

            txtSubject.setText(previousName);
            txtSection.setText(previousSection);

            btnCreateClass.setText("Edit");

        }

        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInEditMode) {

                    createClass("create");

                } else {

                    createClass("edit");

                }

            }
        });

    }


    private void createClass(final String type) {

        hideViewsAction();

        subject = txtSubject.getText().toString().trim();
        section = txtSection.getText().toString().trim();

        if (subject.isEmpty() || section.isEmpty()) {

            showError("Please fill out all the fields.");
            return;

        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_CREATE_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                Toast.makeText(CreateClass.this, "Process successful.", Toast.LENGTH_SHORT).show();

                                if (isInEditMode) {

                                    SuperGlobals.currentClassId = classId;
                                    SuperGlobals.currentSubject = subject;
                                    SuperGlobals.currentSection = section;

                                }

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

                if (isInEditMode) {

                    params.put("classId", classId);

                }

                params.put("subject", subject);
                params.put("section", section);
                params.put("id", id);

                params.put("type", type);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(CreateClass.this);
        requestQueue.add(stringRequest);

    }


    private void showError(String message) {

        Toast.makeText(CreateClass.this, message, Toast.LENGTH_SHORT).show();

        showViewsAction();

    }


    private void hideViewsAction() {

        btnCreateClass.setVisibility(View.GONE);

        progressCreateClass.setVisibility(View.VISIBLE);

    }


    private void showViewsAction() {

        progressCreateClass.setVisibility(View.GONE);

        btnCreateClass.setVisibility(View.VISIBLE);

    }


//    private void createClass() {
//
//        className = txtClassName.getText().toString();
//
//        if (className.isEmpty()) {
//
//            Toast.makeText(CreateClass.this, "Field must not be empty!", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            _Class _class = new _Class("", className);
//
//            _class = DAOClass.addNewClass(_class);
//
//            if (_class == null) {
//
//                Toast.makeText(CreateClass.this, "Add Failed!", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                finish();
//
//            }
//
//        }
//
//    }
//
//
//    private void editClass() {
//
//        className = txtClassName.getText().toString();
//
//        if (className.isEmpty()) {
//
//            Toast.makeText(CreateClass.this, "Field must not be empty!", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            _Class _class = new _Class("", className);
//
//            _class = DAOClass.updateClass(classId, _class);
//
//            if (_class == null) {
//
//                Toast.makeText(CreateClass.this, "Edit Failed!", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                Intent intent = new Intent();
//                intent.putExtra("NewClassName", className);
//
//                setResult(RESULT_OK, intent);
//
//                finish();
//
//            }
//
//        }
//
//    }


}
