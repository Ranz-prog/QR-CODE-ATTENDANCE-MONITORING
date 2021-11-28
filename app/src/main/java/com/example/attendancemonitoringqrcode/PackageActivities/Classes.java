package com.example.attendancemonitoringqrcode.PackageActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.example.attendancemonitoringqrcode.PackageObjectModels._Class;
import com.example.attendancemonitoringqrcode.R;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classes extends AppCompatActivity {


    private String id;

    private AdapterClass adapterClass;

    private List<_Class> classList;

    private Toolbar toolbar;

    private RecyclerView recyclerClass;

    private FloatingActionButton btnCreateNewClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        id = SuperGlobals.currentId;

        setUpToolbar(String.format("Welcome, %s!", SuperGlobals.currentFirstName));

        updateViews();

    }


    @Override
    protected void onResume() {
        super.onResume();

        loadClasses();

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
                logout();
                break;

        }

        return true;

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = AlertDialogManager.showAlertDialogBuilder(Classes.this, "Confirm Exit", "Do you really want to quit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

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

        btnCreateNewClass = findViewById(R.id.btnCreateNewClass);

        btnCreateNewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentManager.goToCreateClass(Classes.this, false);

            }
        });

    }


    private void loadClasses() {

        classList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_CLASSES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject2 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject2.getJSONArray("classes");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                _Class _class = new _Class(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("subject"),
                                        jsonObject.getString("section"),
                                        jsonObject.getString("faculty_id")
                                );

                                classList.add(_class);

                            }

                            setUpRecyclerView();

                        } catch (JSONException e) {

                            Toast.makeText(Classes.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Classes.this, "Problem loading data.", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("facultyId", id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Classes.this);
        requestQueue.add(stringRequest);

    }


    private void setUpRecyclerView() {

        recyclerClass = findViewById(R.id.recyclerClass);

        adapterClass = new AdapterClass(classList, Classes.this);

        recyclerClass.setLayoutManager(new LinearLayoutManager(Classes.this, LinearLayoutManager.VERTICAL, false));
        recyclerClass.setAdapter(adapterClass);

    }


    private void logout() {

        PreferencesManager.insertToPreferences(Classes.this, PreferencesManager.ONLINE_STATUS, "");

        IntentManager.goToMainActivity(Classes.this);
        finish();

    }


}
