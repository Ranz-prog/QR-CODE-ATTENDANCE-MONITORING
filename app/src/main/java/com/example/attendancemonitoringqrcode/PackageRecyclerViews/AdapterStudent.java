package com.example.attendancemonitoringqrcode.PackageRecyclerViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attendancemonitoringqrcode.PackageActivities.ActivityClass;
import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageManagers.Urls;
import com.example.attendancemonitoringqrcode.PackageObjectModels.Student;
import com.example.attendancemonitoringqrcode.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterStudent extends RecyclerView.Adapter<AdapterStudent.ViewHolderStudent> {


    private boolean isCheckable;

    private List<Student> studentList;
    private int indexInLocalStudentList;

    private Context context;

    private ProgressDialog progressDialog;


    public AdapterStudent(List<Student> studentList, Context context, boolean isCheckable) {
        this.studentList = studentList;
        this.context = context;
        this.isCheckable = isCheckable;
    }


    @NonNull
    @Override
    public ViewHolderStudent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (!isCheckable) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_student, parent, false);

        } else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_student_checkable, parent, false);

        }

        AdapterStudent.ViewHolderStudent viewHolderStudent = new AdapterStudent.ViewHolderStudent(view);

        return viewHolderStudent;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderStudent holder, int position) {

        Student student = studentList.get(position);

        if (isCheckable) {

            switch (student.getState()) {

                case 0:
                    holder.imgCheckbox.setImageResource(R.drawable.indicator2);
//                    holder.imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.red));
                    break;

                case 1:
                    holder.imgCheckbox.setImageResource(R.drawable.indicator);
//                    holder.imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.green));
                    break;

                case 2:
                    holder.imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    break;

            }

        }

        String studentName = String.format("%s, %s %s.", student.getLastName(), student.getFirstName(), student.getMiddleName().charAt(0));
        holder.lblStudentName.setText(studentName);

        holder.lblStudentNumber.setText(student.getStudentNumber());

    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public class ViewHolderStudent extends RecyclerView.ViewHolder {

        CircleImageView imgCheckbox;

        TextView lblStudentName;
        TextView lblStudentNumber;

        public ViewHolderStudent(@NonNull View itemView) {
            super(itemView);

            if (isCheckable) {

                imgCheckbox = itemView.findViewById(R.id.imgCheckbox);

            }

            lblStudentName = itemView.findViewById(R.id.lblStudentName);
            lblStudentNumber = itemView.findViewById(R.id.lblStudentNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    indexInLocalStudentList = getAdapterPosition();

                    final Student student = studentList.get(indexInLocalStudentList);

                    if (!isCheckable) {

                        SuperGlobals.currentStudent2Id = student.getId();
                        SuperGlobals.currentStudent2StudentNumber = student.getStudentNumber();
                        SuperGlobals.currentStudent2FirstName = student.getFirstName();
                        SuperGlobals.currentStudent2MiddleName = student.getMiddleName();
                        SuperGlobals.currentStudent2LastName = student.getLastName();
                        IntentManager.goToStudent2(context);

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Change into...");

                        builder.setItems(SuperGlobals.stateArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Modifying...");
                                progressDialog.show();

                                switch (which) {

                                    case 0:
                                        recordStudent(1, student, imgCheckbox);
                                        break;

                                    case 1:
                                        recordStudent(0, student, imgCheckbox);
                                        break;

                                    case 2:
                                        recordStudent(2, student, imgCheckbox);
                                        break;

                                }

                            }
                        });

                        builder.show();

                    }

                }
            });

        }

    }


    private void recordStudent(final int state, final Student student, final ImageView imgCheckbox) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Urls.URL_RECORD_STUDENT_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {

                                SuperGlobals.studentList.get(indexInLocalStudentList).setState(state);

                                switch (state) {

                                    case 0:
                                        imgCheckbox.setImageResource(R.drawable.indicator2);
//                                        imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.red));
                                        break;

                                    case 1:
                                        imgCheckbox.setImageResource(R.drawable.indicator);
//                                        imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.green));
                                        break;

                                    case 2:
                                        imgCheckbox.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                                        break;

                                }

                                progressDialog.dismiss();

                                Toast.makeText(context, "Modified.", Toast.LENGTH_SHORT).show();

                            } else if (status.equals("failed")) {

                                Toast.makeText(context, "Process failed.", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(context, "Process failed", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {

                            Toast.makeText(context, "Process failed", Toast.LENGTH_SHORT).show();
                            Log.e("Error1", e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Process failed", Toast.LENGTH_SHORT).show();
                        Log.e("Error2", error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("state", String.valueOf(state));

                params.put("studentId", student.getId());
                params.put("dateId", SuperGlobals.currentDateId);
                params.put("classId", SuperGlobals.currentClassId);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


}
