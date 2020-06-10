package com.qss.AMS.RESTful_Service;

import com.qss.AMS.Controller.IController;
import com.qss.AMS.Controller.IControllerImpl;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

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

}
