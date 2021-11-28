package com.example.attendancemonitoringqrcode.PackageRecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Class;
import com.example.attendancemonitoringqrcode.R;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolderClass> {


    private List<_Class> classList;

    private Context context;


    public AdapterClass(List<_Class> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

        _Class _class = classList.get(position);

        holder.lblClassName.setText(_class.getSubject());
        holder.lblSection.setText(String.format("Section: %s", _class.getSection()));

    }


    @Override
    public int getItemCount() {
        return classList.size();
    }


    public class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView lblClassName;
        TextView lblSection;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            lblClassName = itemView.findViewById(R.id.lblClassName);
            lblSection = itemView.findViewById(R.id.lblSection);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                _Class _class = classList.get(getAdapterPosition());

                SuperGlobals.currentClassId = _class.getId();
                SuperGlobals.currentSubject = _class.getSubject();
                SuperGlobals.currentSection = _class.getSection();

                IntentManager.goToActivityClass(context);

                }
            });

        }

    }


}
