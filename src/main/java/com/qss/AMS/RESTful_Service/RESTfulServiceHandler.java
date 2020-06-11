package com.qss.AMS.RESTful_Service;

import com.qss.AMS.Controller.IController;
import com.qss.AMS.Controller.IControllerImpl;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/AMS/RESTful_Service/")
public class RESTfulServiceHandler {

    private IController iController = new IControllerImpl();

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public String hello(){
        System.out.println("hello");
        return iController.hello();
    }

    @GetMapping("/serverStatus")
    public int serverStatus(){
        return 1;
    }

    @GetMapping("/getEmployees")
    public Iterable<Users> getEmployees(){
        return iController.getEmployees();
    }

    @GetMapping("/getAllAttendance")
    public Iterable<Attendance> getAllAttendance(){return iController.getAllAttendance();}

    @GetMapping("/getAllLeaves")
    public Iterable<Users> getAllLeaves(){return iController.getAllLeaves();}

    @GetMapping("/getAttendanceByDuration")
    public Iterable<Attendance> getAttendanceByDuration(@PathParam("from") String from, @PathParam("to") String to){
        return iController.getAttendanceByDuration(from, to);
    }

    @GetMapping("/getLeavesByDuration")
    public Iterable<Users> getLeavesByDuration(@PathParam("from") String from, @PathParam("to") String to){
        return iController.getLeavesByDuration(from, to);
    }

    @GetMapping("/getOTByDuration")
    public Iterable<Attendance> getOTByDuration(@PathParam("from") String from, @PathParam("to") String to){
        return iController.getOTByDuration(from, to);
    }

    @GetMapping("/exportEmployees/{filename:.+}")
    public String exportEmp(@PathVariable String filename) throws IOException {

        String flag = null;
        System.out.println("export filename: " + filename);

        ArrayList<String> headers = new ArrayList<>();
        headers.add("Employee ID");
        headers.add("Username");
        headers.add("Department");
        headers.add("Gender");
        headers.add("Create Date");

        String path = filename.replace(',', '\\');

        System.out.println(" path: " + path);

        ArrayList<Users> empL = iController.getEmployees();

        flag = iController.usersExport(empL, path, headers, 2);

        return flag;
    }

    @GetMapping("/exportAtt/{filename:.+}")
    public String exporAtttbyduration(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to) throws IOException {

        String flag = null;
        ArrayList<String> headers = new ArrayList<>();

        ArrayList<Attendance> attL = null;

        String path = filename.replace(',', '\\');

            headers.add("Employee ID");
            headers.add("Username");
            headers.add("Department");
            headers.add("Attend Time");
            headers.add("Verify Mode");
            attL = iController.getAttendanceByDuration(from, to);
            flag = iController.attExport(attL, path, headers, 1);

        return flag;
    }

    @GetMapping("/exportLv/{filename:.+}")
    public String exportLvbyduration(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to) throws IOException {

        String flag = null;
        ArrayList<String> headers = new ArrayList<>();

        ArrayList<Users> usrL = null;

        String path = filename.replace(',', '\\');

        headers.add("Employee ID");
        headers.add("Username");
        headers.add("Starts from");
        headers.add("Ends on");
        headers.add("Leave Submitted on");
        usrL = iController.getLeavesByDuration(from, to);
        flag = iController.usersExport(usrL, path, headers, 1);

        return flag;
    }

    @GetMapping("/exportOt/{filename:.+}")
    public String exportOtbyduration(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to) throws IOException {

        String flag = null;
        ArrayList<String> headers = new ArrayList<>();


        ArrayList<Attendance> attL = null;

        String path = filename.replace(',', '\\');

        headers.add("Employee ID");
        headers.add("Username");
        headers.add("Clock In");
        headers.add("Clock out");
        headers.add("OT or Late hours");
        headers.add("Date");
        attL = iController.getOTByDuration(from, to);
        flag = iController.attExport(attL, path, headers, 2);;

        return flag;
    }

//    public void expsssortbyduration(@PathParam("path") String filename, @PathParam("from") String from, @PathParam("to") String to) throws IOException {
//
//        String flag = null;
//        System.out.println(" filename: " + filename);
//
//        ArrayList<String> headers = new ArrayList<>();
//
//        ArrayList<Users> usrL = null;
//
//        String path = filename.replace(',', '\\');
//
//
//            headers.add("Employee ID");
//            headers.add("Username");
//            headers.add("Department");
//            headers.add("Attend Time");
//            headers.add("Verify Mode");
//            attL = iController.getAttendanceByDuration(from, to);
//            flag = iController.attExport(attL, path, headers, 1);
//
//            headers.add("Employee ID");
//            headers.add("Username");
//            headers.add("Starts from");
//            headers.add("Ends on");
//            headers.add("Leave Submitted on");
//            usrL = iController.getLeavesByDuration(from, to);
//            flag = iController.usersExport(usrL, path, headers, 1);
//
//            headers.add("Employee ID");
//            headers.add("Username");
//            headers.add("Clock In");
//            headers.add("Clock out");
//            headers.add("OT or Late hours");
//            headers.add("Date");
//            attL = iController.getOTByDuration(from, to);
//            flag = iController.attExport(attL, path, headers, 2);
//
//        }


}
