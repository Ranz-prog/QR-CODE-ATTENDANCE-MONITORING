package com.example.attendancemonitoringqrcode.PackageTabs;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;
import com.example.attendancemonitoringqrcode.PackageRecyclerViews.AdapterDate;
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Attendance extends Fragment {


    private String classId;

    private AdapterDate adapterDate;

    private List<_Date> dateList;

    private View view;
    private Context context;

    private SwipeRefreshLayout refresh;

    private RecyclerView recyclerDate;


    public Attendance() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance, container, false);
        context = getActivity();

        classId = SuperGlobals.currentClassId;

        updateViews();

        return  view;
    }


    @Override
    public void onResume() {
        super.onResume();

        loadDates();

    }


    private void updateViews() {

//        refresh = view.findViewById(R.id.refresh);
//
//        loadDates();
//
//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        refresh.setRefreshing(false);
//
//                    }
//                }, 250);
//
//            }
//        });

    }


    private void loadDates() {

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

        recyclerDate = view.findViewById(R.id.recyclerDate);

        adapterDate = new AdapterDate(dateList, context);

        recyclerDate.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerDate.setAdapter(adapterDate);

    }


}
