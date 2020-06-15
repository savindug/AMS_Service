package com.qss.AMS.Controller;

import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IController {

    String hello();

    ArrayList<Users> getEmployees();

    ArrayList<Attendance> getAllAttendance();

    ArrayList<Users> getAllLeaves();

    ArrayList<Attendance> getAttendanceByDuration(String from, String to);

    ArrayList<Users> getLeavesByDuration(String from, String to);

    ArrayList<Attendance> getOTByDuration(String from, String to);



    /*Report Print-------------------------------------------------------------*/

    String usersExport(ArrayList<Users> userL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException;

    String attExport(ArrayList<Attendance> attL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException;

    String usersExportAdmin(ArrayList<Users> userL, String path, ArrayList<String> headers, String from, String to, String branchname, int fl) throws FileNotFoundException, IOException;

    String attExportAdmin(ArrayList<Attendance> attL, String path, ArrayList<String> headers, String from, String to, String branchname, int fl) throws FileNotFoundException, IOException;





    /*Admin Side---------------------------------------------------------------*/

    ArrayList<Users> getEmployeesAdmin(String branchname);

    ArrayList<Attendance> getAttendanceByDurationAdmin(String from, String to, String branchname);

    ArrayList<Users> getLeavesByDurationAdmin(String from, String to , String branchname);

    ArrayList<Attendance> getOTByDurationAdmin(String from, String to , String branchname);


    /*Data Upload---------------------------------------------------------------*/

    int uploadDataEmp(String branchname);
    int uploadDataAtt(String branchname);
    int uploadDataLv(String branchname);
    int uploadDataOt(String branchname);

}
