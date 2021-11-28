package com.example.attendancemonitoringqrcode.PackageObjectModels;

public class _Date {


    private String id;

    private String date;
    private long milliseconds;

    private String dateClassId;


    public _Date(String id, String date, long milliseconds, String dateClassId) {
        this.id = id;
        this.date = date;
        this.milliseconds = milliseconds;
        this.dateClassId = dateClassId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getDateClassId() {
        return dateClassId;
    }

    public void setDateClassId(String dateClassId) {
        this.dateClassId = dateClassId;
    }


}
