package com.example.attendancemonitoringqrcode.PackageForms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attendancemonitoringqrcode.PackageManagers.Constants;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CreateDate extends AppCompatActivity {


    private String classId;

    private boolean isInEditMode;
    private String dateId;
    private String previousName;
    private long previousMilliseconds;

    private String date;
    private long milliseconds;

    private Intent intent;

    private Toolbar toolbar;

    private TextView lblDate;
    private ImageButton btnSelectDate;

    private Calendar calendar;

    private SimpleDateFormat simpleDateFormat;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private Button btnAddDate;

    private ProgressBar progressAddDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_create_date);

        classId = SuperGlobals.currentClassId;

        intent = getIntent();
        isInEditMode = intent.getBooleanExtra("IsInEditMode", false);

        if (!isInEditMode) {

            setUpToolbar("Add Date");

        } else {

            previousName = SuperGlobals.currentDate;
            previousMilliseconds = SuperGlobals.currentMilliseconds;
            dateId = SuperGlobals.currentDateId;

            setUpToolbar("Edit Date");

        }

        updateViews();

        setUpCalendar();

    }


    private void setUpToolbar(String title) {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

    }


    private void updateViews() {

        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);

        lblDate = findViewById(R.id.lblDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);

        btnAddDate = findViewById(R.id.btnAddDate);

        progressAddDate = findViewById(R.id.progressAddDate);

        if (!isInEditMode) {

            Calendar calendar = Calendar.getInstance();

            lblDate.setText(simpleDateFormat.format(calendar.getTime()));
            milliseconds = calendar.getTimeInMillis();

        } else {

            lblDate.setText(previousName);
            milliseconds = previousMilliseconds;

            btnAddDate.setText("Edit");

        }

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCalendar();

            }
        });

        btnAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInEditMode) {

                    addDate("create");

                } else {

                    addDate("edit");

                }

            }
        });

    }


    private void setUpCalendar() {

        calendar = Calendar.getInstance();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateTextViewDate();

            }

        };

    }


    private void showCalendar() {

        new DatePickerDialog(CreateDate.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    private void updateTextViewDate() {

        lblDate.setText(simpleDateFormat.format(calendar.getTime()));
        milliseconds = calendar.getTimeInMillis();

    }


    private void addDate(final String type) {

        hideViewsAction();

        date = lblDate.getText().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_CREATE_DATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                Toast.makeText(CreateDate.this, "Process successful.", Toast.LENGTH_SHORT).show();

                                if (isInEditMode) {

                                    SuperGlobals.currentDateId = dateId;
                                    SuperGlobals.currentDate = date;
                                    SuperGlobals.currentMilliseconds = milliseconds;

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

                    params.put("dateId", dateId);

                }

                params.put("date", date);
                params.put("milliseconds", String.valueOf(milliseconds));
                params.put("classId", classId);

                params.put("type", type);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(CreateDate.this);
        requestQueue.add(stringRequest);

    }


    private void showError(String message) {

        Toast.makeText(CreateDate.this, message, Toast.LENGTH_SHORT).show();

        showViewsAction();

    }


    private void hideViewsAction() {

        btnAddDate.setVisibility(View.GONE);

        progressAddDate.setVisibility(View.VISIBLE);

    }


    private void showViewsAction() {

        progressAddDate.setVisibility(View.GONE);

        btnAddDate.setVisibility(View.VISIBLE);

    }


//    private void addDate() {
//
//        date = lblDate.getText().toString();
//
//        if (date.isEmpty()) {
//
//            Toast.makeText(CreateDate.this, "Field must not be empty!", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            _Date _date = new _Date("", date, calendar.getTime().getTime(), classId);
//
//            _date = DAODate.addNewDate(_date);
//
//            if (_date == null) {
//
//                Toast.makeText(CreateDate.this, "Add Failed!", Toast.LENGTH_SHORT).show();
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
//    private void editDate() {
//
//        date = lblDate.getText().toString();
//
//        if (date.isEmpty()) {
//
//            Toast.makeText(CreateDate.this, "Field must not be empty!", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            _Date _date = new _Date("", date, calendar.getTime().getTime(), classId);
//
//            _date = DAODate.updateDate(dateId, _date);
//
//            if (_date == null) {
//
//                Toast.makeText(CreateDate.this, "Edit Failed!", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                Intent intent = new Intent();
//                intent.putExtra("NewDate", date);
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
