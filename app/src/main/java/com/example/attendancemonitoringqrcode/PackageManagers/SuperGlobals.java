package com.example.attendancemonitoringqrcode.PackageManagers;

import com.example.attendancemonitoringqrcode.PackageObjectModels.Student;

import java.util.ArrayList;
import java.util.List;

public class SuperGlobals {


    public static String currentId = "";

    public static String currentStudentId = "";
    public static String currentFacultyId = "";

    public static String currentFirstName = "";
    public static String currentMiddleName = "";
    public static String currentLastName = "";

    public static String currentClassId = "";
    public static String currentSubject = "";
    public static String currentSection = "";

    public static String currentStudent2Id = "";
    public static String currentStudent2StudentNumber = "";
    public static String currentStudent2FirstName = "";
    public static String currentStudent2MiddleName = "";
    public static String currentStudent2LastName = "";

    public static String currentDateId = "";
    public static String currentDate = "";
    public static long currentMilliseconds = 0;

    public static int currentTabSelectedSubject = 0;

    public static int currentTabSelectedDate = 0;

    public static List<Student> studentList = new ArrayList<>();

    public static String[] stateArray = {
            "Present",
            "Absent"
//            "Changed Device"
    };


}
