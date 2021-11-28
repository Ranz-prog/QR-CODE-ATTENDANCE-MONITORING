package com.example.attendancemonitoringqrcode.PackageRecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageObjectModels.Subject;
import com.example.attendancemonitoringqrcode.R;

import java.util.List;

public class AdapterSubject extends RecyclerView.Adapter<AdapterSubject.ViewHolderSubject> {


    public List<Subject> subjectList;

    private Context context;


    public AdapterSubject(List<Subject> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject, parent, false);
        AdapterSubject.ViewHolderSubject viewHolderSubject = new ViewHolderSubject(view);

        return viewHolderSubject;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderSubject holder, int position) {

        Subject subject = subjectList.get(position);

        holder.lblSubjectName.setText(subject.getSubjectName());

    }


    @Override
    public int getItemCount() {
        return subjectList.size();
    }


    public class ViewHolderSubject extends RecyclerView.ViewHolder {

        TextView lblSubjectName;

        public ViewHolderSubject(@NonNull View itemView) {
            super(itemView);

            lblSubjectName = itemView.findViewById(R.id.lblSubjectName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Subject subject = subjectList.get(getAdapterPosition());

                    IntentManager.goToActivitySubject(context, subject.getId(), subject.getSubjectName());

                }
            });

        }

    }


}
