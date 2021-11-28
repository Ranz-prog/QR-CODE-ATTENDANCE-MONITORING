package com.example.attendancemonitoringqrcode.PackageManagers;

public class Urls {


//    private static final String IP_ADDRESS = "localhost";
//    private static final String IP_ADDRESS = "192.168.1.71";
//    private static final String IP_ADDRESS = "192.168.43.31";
    private static final String IP_ADDRESS = "192.168.1.83";

    public static final String URL_LOGIN = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidLogin.php", IP_ADDRESS);

    public static final String URL_CREATE_CLASS = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidCreateClass.php", IP_ADDRESS);
    public static final String URL_GET_CLASSES = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetClasses.php", IP_ADDRESS);
    public static final String URL_DELETE_CLASS = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidDeleteClass.php", IP_ADDRESS);

    public static final String URL_CREATE_DATE = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidCreateDate.php", IP_ADDRESS);
    public static final String URL_GET_DATES = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetDates.php", IP_ADDRESS);
    public static final String URL_DELETE_DATE = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidDeleteDate.php", IP_ADDRESS);

    public static final String URL_ADD_STUDENT_TO_CLASS = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidAddStudentToClass.php", IP_ADDRESS);

    public static final String URL_GET_STUDENTS = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetStudents.php", IP_ADDRESS);

    public static final String URL_GET_STUDENTS_ATTENDANCE = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetStudentsAttendance.php", IP_ADDRESS);
    public static final String URL_RECORD_STUDENT_ATTENDANCE = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidRecordStudentAttendance.php", IP_ADDRESS);

    public static final String URL_GET_DATES_ATTENDANCE = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetDatesAttendance.php", IP_ADDRESS);

    public static final String URL_GET_STUDENT_STATISTICS = String.format("http://%s/SystemsIntegrationAndArchitecture/AttendanceMonitoringSystemQRCode/androidGetStudentStatistics.php", IP_ADDRESS);


}
