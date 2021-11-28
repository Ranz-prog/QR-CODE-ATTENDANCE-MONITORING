package com.example.attendancemonitoringqrcode.PackageTabs;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.attendancemonitoringqrcode.PackageObjectModels.Student;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterDate;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterStudent;
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Students extends Fragment {


    private String classId;

    private AdapterStudent adapterStudent;

    private List<Student> studentList;

    private View view;
    private Context context;

    private RecyclerView recyclerStudent;


    public Students() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_students, container, false);
        context = getActivity();

        classId = SuperGlobals.currentClassId;

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        loadStudents();

    }


    private void loadStudents() {

        studentList = new ArrayList<>();

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

                            setUpRecyclerView();

                        } catch (JSONException e) {

                            Toast.makeText(context, "Problem loading data.", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Problem loading data.", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    private void setUpRecyclerView() {

        recyclerStudent = view.findViewById(R.id.recyclerStudent);

        adapterStudent = new AdapterStudent(studentList, context, false);

        recyclerStudent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerStudent.setAdapter(adapterStudent);

    }


}
