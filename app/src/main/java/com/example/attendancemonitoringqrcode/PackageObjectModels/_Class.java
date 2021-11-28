package com.example.attendancemonitoringqrcode.PackageObjectModels;

public class _Class {


    private String id;

    private String subject;
    private String section;

    private String classFacultyId;


    public _Class(String id, String subject, String section, String classFacultyId) {
        this.id = id;
        this.subject = subject;
        this.section = section;
        this.classFacultyId = classFacultyId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getClassFacultyId() {
        return classFacultyId;
    }

    public void setClassFacultyId(String classFacultyId) {
        this.classFacultyId = classFacultyId;
    }


}
