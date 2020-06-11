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

    String usersExport(ArrayList<Users> userL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException;

    String attExport(ArrayList<Attendance> attL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException;

}
