package com.example.attendancemonitoringqrcode.PackageRecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoringqrcode.PackageManagers.IntentManager;
import com.example.attendancemonitoringqrcode.PackageManagers.SuperGlobals;
import com.example.attendancemonitoringqrcode.PackageObjectModels._Date;
import com.example.attendancemonitoringqrcode.R;

import java.util.List;

public class AdapterDate extends RecyclerView.Adapter<AdapterDate.ViewHolderDate> {


    private List<_Date> dateList;

    private Context context;


    public AdapterDate(List<_Date> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderDate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_date, parent, false);

        AdapterDate.ViewHolderDate viewHolderDate = new AdapterDate.ViewHolderDate(view);

        return viewHolderDate;

    }


    @Override
    public void onBindViewHolder(@NonNull AdapterDate.ViewHolderDate holder, int position) {

        _Date _date = dateList.get(position);

        holder.lblDate.setText(_date.getDate());

    }


    @Override
    public int getItemCount() {
        return dateList.size();
    }


    public class ViewHolderDate extends RecyclerView.ViewHolder {

        ImageView imgCheckbox;

        TextView lblDate;

        public ViewHolderDate(@NonNull View itemView) {
            super(itemView);

            lblDate = itemView.findViewById(R.id.lblDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                _Date _date = dateList.get(getAdapterPosition());

                SuperGlobals.currentDateId = _date.getId();
                SuperGlobals.currentDate = _date.getDate();
                SuperGlobals.currentMilliseconds = _date.getMilliseconds();

                IntentManager.goToActivityDate(context);

                }
            });

        }

    }


}
