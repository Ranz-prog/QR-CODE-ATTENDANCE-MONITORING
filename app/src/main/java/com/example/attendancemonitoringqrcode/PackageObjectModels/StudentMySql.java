package com.example.attendancemonitoringqrcode.PackageObjectModels;

import com.google.gson.annotations.SerializedName;

public class StudentMySql {


    @SerializedName("response")
    private String Response;

    @SerializedName("name")
    private String Name;


    public String getResponse() {
        return Response;
    }

    public String getName() {
        return Name;
    }


}
