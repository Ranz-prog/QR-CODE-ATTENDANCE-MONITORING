package com.example.attendancemonitoringqrcode.PackageObjectModels;

public class Subject {


    private String id;

    private String subjectName;


    public Subject(String id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


}
