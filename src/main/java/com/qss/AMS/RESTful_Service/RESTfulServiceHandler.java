package com.qss.AMS.RESTful_Service;

import com.qss.AMS.Controller.IController;
import com.qss.AMS.Controller.IControllerImpl;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    

}
