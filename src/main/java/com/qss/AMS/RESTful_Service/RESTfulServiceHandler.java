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


    /*Report Related Methods*/

    @GetMapping("/exportEmployees/{filename:.+}")
    public String exportEmp(@PathVariable String filename) throws IOException {

        System.out.println("In Emp Admin");
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


    @GetMapping("/AdminexportEmployees/{filename:.+}")
    public String exportEmpAdmin(@PathVariable String filename, @PathParam("branchname") String branchname) throws IOException {
        System.out.println("In Emp Admin");
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

        ArrayList<Users> empL = iController.getEmployeesAdmin(branchname);
        System.out.println("Emp List from Download" + empL);

        flag = iController.usersExportAdmin(empL, path, headers, "00/00/0000", "00/00/0000" , branchname, 5);
        System.out.println("flG HERE" + flag);
        return flag;
    }

    @GetMapping("/AdminexportAtt/{filename:.+}")
    public String exporAtttbydurationAdmin(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to, @PathParam("branchname") String branchname) throws IOException {

        String flag = null;
        ArrayList<String> headers = new ArrayList<>();

        ArrayList<Attendance> attL = null;

        String path = filename.replace(',', '\\');

        headers.add("Employee ID");
        headers.add("Username");
        headers.add("Department");
        headers.add("Attend Time");
        headers.add("Verify Mode");
        attL = iController.getAttendanceByDurationAdmin(from, to, branchname);
        System.out.println("Emp List from Download" + attL);
        flag = iController.attExportAdmin(attL, path, headers, from, to, branchname, 4);

        return flag;
    }

    @GetMapping("/AdminexportLv/{filename:.+}")
    public String exportLvbydurationAdmin(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to, @PathParam("branchname") String branchname) throws IOException {

        String flag = null;
        ArrayList<String> headers = new ArrayList<>();

        ArrayList<Users> usrL = null;

        String path = filename.replace(',', '\\');

        headers.add("Employee ID");
        headers.add("Username");
        headers.add("Starts from");
        headers.add("Ends on");
        headers.add("Leave Submitted on");
        usrL = iController.getLeavesByDurationAdmin(from, to, branchname);
        flag = iController.usersExportAdmin(usrL, path, headers, from, to, branchname,4);

        return flag;
    }

    @GetMapping("/AdminexportOt/{filename:.+}")
    public String exportOtbydurationAdmin(@PathVariable String filename, @PathParam("from") String from, @PathParam("to") String to, @PathParam("branchname") String branchname) throws IOException {

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
        attL = iController.getOTByDurationAdmin(from, to, branchname);
        flag = iController.attExportAdmin(attL, path, headers, from, to, branchname,5);;

        return flag;
    }











    /*Admin Implementation-----------------------------------------------------------------------*/

    @GetMapping("/getEmployeesAdmin")
    public Iterable<Users> getEmployeesAdmin(@PathParam("branchname") String branchname){
        return iController.getEmployeesAdmin(branchname);
    }

    @GetMapping("/getAttendanceByDurationAdmin")
    public Iterable<Attendance> getAttendanceByDurationAdmin(@PathParam("from") String from, @PathParam("to") String to, @PathParam("branchname") String branchname){
        return iController.getAttendanceByDurationAdmin(from, to, branchname);
    }

    @GetMapping("/getLeavesByDurationAdmin")
    public Iterable<Users> getLeavesByDurationAdmin(@PathParam("from") String from, @PathParam("to") String to , @PathParam("branchname") String branchname){
        return iController.getLeavesByDurationAdmin(from, to, branchname);
    }

    @GetMapping("/getOTByDurationAdmin")
    public Iterable<Attendance> getOTByDurationAdmin(@PathParam("from") String from, @PathParam("to") String to , @PathParam("branchname") String branchname){
        return iController.getOTByDurationAdmin(from, to, branchname);
    }



    /*upload-----------------------------------------------------------------------------------------*/

    @GetMapping("/uploadEmployees")
    public int uploadDataEmp(@PathParam("branchname") String branchname){
        return iController.uploadDataEmp(branchname);
    }

    @GetMapping("/uploadAttendance")
    public int uploadDataAtt(@PathParam("branchname") String branchname){
        return iController.uploadDataAtt(branchname);
    }

    @GetMapping("/uploadLeaves")
    public int uploadDataLv( @PathParam("branchname") String branchname){
        return iController.uploadDataLv(branchname);
    }

    @GetMapping("/uploadOT")
    public int uploadDataOt(@PathParam("branchname") String branchname){
        return iController.uploadDataOt(branchname);
    }
    

}