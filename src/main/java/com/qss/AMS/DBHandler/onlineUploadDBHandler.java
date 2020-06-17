package com.qss.AMS.DBHandler;

import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*onlineDBHandler oo = new onlineDBHandler();
        oo.uploadAttendanceByDuration("2025-05-05 05:05:05","Elle");*/
/*onlineDBHandler oo = new onlineDBHandler();
        oo.uploadEmployees("1000000","Elle");*/
public class onlineUploadDBHandler {

    private Users user;
    private Attendance att;

    private Connection connection;
    private PreparedStatement ps;

    private String GET_EMPLOYEES_SQL = "select DISTINCT ras_Users.PIN, ras_Users.UserName, ras_Users.Sex, ras_Dept.DeptName, ras_Users.CreateDate, ras_Users.CreateDate \n" +
            "				  from ras_Users, ras_Dept \n" +
            "				  where ras_Dept.DeptId = ras_Users.DeptId  ";
    private String GET_ALL_ATTENDANCE_SQL = "select DISTINCT [ras_Users].PIN as Employee_ID, [ras_Users].[UserName] as User_Name, [ras_AttRecord].[Clock] as Clock, [ras_AttTypeItem].[ItemName] as Attend_Type, [ras_AttRecord].[Remark] from  \n" +
            "        [ras_AttRecord], [ras_Dept], [ras_Users], [ras_AttTypeItem] \n" +
            "                  where  [ras_Users].[UID] = [ras_AttRecord].ID and [ras_AttRecord].[AttTypeId] = [ras_AttTypeItem].[ItemId]";

    private String GET_LEAVES_BY_DURATION = "select DISTINCT ras_Users.pin, ras_Users.UserName, ras_AttLeaveRecord.FromDate, ras_AttLeaveRecord.ToDate, ras_AttLeaveRecord.LastUpdatedDate, ras_AttLeaveRecord.Remark  from\n" +
            "ras_AttLeaveRecord\n" +
            "inner join ras_Users\n" +
            "on ras_AttLeaveRecord.UID = ras_Users.UID \n" +
            " where DateValue(ras_AttLeaveRecord.LastUpdatedDate) between  ? and ? \n" +
            "order by ras_Users.PIN";

    private String GET_OT_BY_DURATION = "SELECT DISTINCT ras_Users.pin as Employee_ID, ras_Users.UserName as User_Name, Min(clock) as Clock_In, Max(clock) as Clock_out, DATEDIFF('h', MIN(clock), MAX(clock)) - 8 AS [OT/Late_Covering_Hrs], CAST(clock AS DATE) as Date\n" +
            "				  FROM ras_AttRecord, ras_Users, ras_AttTypeItem \n" +
            "						where ras_Users.din = ras_AttRecord.din and DateValue(clock)  between ?  and ?\n" +
            "					GROUP BY CAST(clock AS DATE), ras_Users.pin, ras_Users.UserName";


    private String GET_ATTENDANCE_BY_ID = "select DISTINCT [ras_Users].PIN as Employee_ID, [ras_Users].[UserName] as User_Name, [ras_AttRecord].[Clock] as Clock, [ras_AttTypeItem].[ItemName] as Attend_Type, [ras_AttRecord].[Remark]  \n" +
            "from    [ras_AttRecord], [ras_Dept], [ras_Users], [ras_AttTypeItem] \n" +
            " where  [ras_Users].[UID] = [ras_AttRecord].ID and [ras_AttRecord].[AttTypeId] = [ras_AttTypeItem].[ItemId] and DateValue(Clock) between ?  and ? ";


    public onlineUploadDBHandler() { }

    @Autowired
    JdbcTemplate template;



    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/



    public int uploadEmployees( String to, String branchname){

        /*Fetch max userId ---------------------------------------------------------------------------*/

        String userId = "0";
        String jsonString;
        String finalJsonString = "{\"data\":[{\"userId\":\"0\"}]}";
        try {
            URL url = new URL("http://ams-agri.qsslanka.com/employee/readmax.php?searchInput="+branchname);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((jsonString = br.readLine()) != null) {
                //System.out.println(jsonString);
                finalJsonString = jsonString;
                System.out.println("To check final String");
                System.out.println(finalJsonString);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(finalJsonString);

        //manipulating json object
        try{
            JSONObject object = new JSONObject(finalJsonString);
            JSONArray Jarray  = object.getJSONArray("data");



            for(int i=0;i<Jarray.length();i++){
                JSONObject jsonObject1 = Jarray.getJSONObject(i);
                System.out.println(jsonObject1);
                userId = (jsonObject1.optString("userId")).toString();
                System.out.println(userId);


            }
            if (userId == "null"){
                userId = "0";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*Write Employee based on fetched max userId ---------------------------------------------------------------------------*/
        int responseCode = 0;
        ArrayList<Users> employeesList = new ArrayList<>();


        try{
            connection = DBConnection.openConnection();
            ps = connection.prepareStatement(GET_EMPLOYEES_SQL);
            /*ps.setString(1, userId);*/
            /*ps.setInt(2, Integer.parseInt(to));*/
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            System.out.println("============== upload Employees ==============");
            while(rs.next()){
                user = new Users();
                user.setuID(rs.getString(1));
                user.setuName(rs.getString(2));
                user.setGender(rs.getString(3));
                user.setUserdepart(rs.getString(4));
                user.setLastLoggedIn(rs.getString(5));
                user.setCreateDate(rs.getString(6));
                user.setBranchname(branchname);
                if(Integer.parseInt(userId) > Integer.parseInt(user.getuID())){
                    continue;
                }
                System.out.println(user.empToString());


                try {
                    URL url = new URL("http://ams-agri.qsslanka.com/api/employee/write.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    //setting json object
                    String input = user.empToString();  /*from user string is created*/

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();


                    //saving response code to a variable
                    responseCode = conn.getResponseCode();


                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(responseCode == 200){
                    System.out.println("pass");
                }
                else{
                    System.out.println("fail");
                    return 0;  /*Return 0 if something fails*/

                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 1;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/


    public int uploadLeavesbyDuration(String to, String branchname){


        /*Fetch max submitted Date ---------------------------------------------------------------------------*/

        String submittedDate = "2005-05-05 05:05:05";
        String jsonString;
        String finalJsonString = null;
        try {
            URL url = new URL("http://ams-agri.qsslanka.com/api/leave/readmax.php?searchInput="+branchname);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((jsonString = br.readLine()) != null) {
                //System.out.println(jsonString);
                finalJsonString = jsonString;
            }

            conn.disconnect();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(finalJsonString);

        //manipulating json object
        try{
            JSONObject object = new JSONObject(finalJsonString);
            JSONArray Jarray  = object.getJSONArray("data");



            for(int i=0;i<Jarray.length();i++){
                JSONObject jsonObject1 = Jarray.getJSONObject(i);
                System.out.println(jsonObject1);
                submittedDate = (jsonObject1.optString("submittedDate")).toString();
                System.out.println(submittedDate);

            }


            if (submittedDate == "null"){
                submittedDate = "2005-05-05 05:05:05";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*--------------------------------Upload leaves based on max date--------------------------------------------------*/

        ArrayList<Users> leaveList = new ArrayList<>();
        Users leave;
        int responseCode = 0;
        try{
            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();

            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(submittedDate);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            System.out.println("At online DB Handler - 1");
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);



            ps = connection.prepareStatement(GET_LEAVES_BY_DURATION);
            ps.setDate(1, sqlDate2);
            ps.setDate(2, sqlDate1);
            ResultSet rs = ps.executeQuery();
            System.out.println("============== Upload LeavesByDuration ==============");
            while(rs.next()){
                leave = new Users();
                leave.setuID(rs.getString(1));
                leave.setuName(rs.getString(2));
                leave.setLeaveStart(rs.getString(3));
                leave.setLeaveEnd(rs.getString(4));
                leave.setLeaveSubmitted(rs.getString(5));
                leave.setLeaveRemark(rs.getString(6));
                leave.setBranchname(branchname);
                System.out.println(leave.toString());
                System.out.println(leave.leaveToString());


       /* ArrayList<Users> leaveList = new ArrayList<>();
        Users leave;
        ResultSet rs = null;

        try{
            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(submittedDate);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            System.out.println("At online DB Handler - 1");
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);



            ps = connection.prepareStatement(GET_LEAVES_BY_DURATION);
            ps.setDate(1, sqlDate2);
            ps.setDate(2, sqlDate1);
            System.out.println("==============upload AllLeaves ==============");
            while(rs.next()){
                leave = new Users();
                leave.setuID(rs.getString(1));
                leave.setuName(rs.getString(2));
                leave.setLeaveStart(rs.getString(3));
                leave.setLeaveEnd(rs.getString(4));
                leave.setLeaveSubmitted(rs.getString(5));
                leave.setLeaveRemark(rs.getString(6));
                leave.setBranchname(branchname);
                System.out.println(leave.toString());
                System.out.println(leave.leaveToString());*/


                try {
                    URL url = new URL("http://ams-agri.qsslanka.com/api/leave/write.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    //setting json object
                    String input = leave.leaveToString();  /*from attendance string is created*/

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();


                    //saving response code to a variable
                    responseCode = conn.getResponseCode();


                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(responseCode == 200){
                    /**/
                }
                else{
                    return 0;  /*Return 0 if something fails*/
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 1; /*return 1 if success*/
    }




    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/






    public int uploadAttendanceByDuration( String to, String branchname){


        /*Fetch max clock ---------------------------------------------------------------------------*/

        String clock = "2005-05-05 05:05:05";
        String jsonString;
        String finalJsonString = "{\"data\":[{\"clock\":\"2005-05-05 05:05:05\"}]}";;
        try {
            URL url = new URL("http://ams-agri.qsslanka.com/api/attendance/readmax.php?searchInput="+branchname);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((jsonString = br.readLine()) != null) {
                //System.out.println(jsonString);
                finalJsonString = jsonString;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(finalJsonString);

        //manipulating json object
        try{
            JSONObject object = new JSONObject(finalJsonString);
            JSONArray Jarray  = object.getJSONArray("data");



            for(int i=0;i<Jarray.length();i++){
                JSONObject jsonObject1 = Jarray.getJSONObject(i);
                System.out.println(jsonObject1);
                clock = (jsonObject1.optString("clock")).toString();
                System.out.println(clock);


            }
            if (clock == "null"){
                clock = "2005-05-05 05:05:05";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*Write Att based on fetched max clock ---------------------------------------------------------------------------*/

        int responseCode = 0;


        try{


            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(clock);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            System.out.println("At online DB Handler - 1");
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);



            ps = connection.prepareStatement(GET_ATTENDANCE_BY_ID);
            ps.setDate(1, sqlDate2);
            ps.setDate(2, sqlDate1);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            while(rs.next()){

                att = new Attendance();
                att.setuId(rs.getString(1));
                att.setuName(rs.getString(2));
                att.setAttTime(rs.getString(3));
                att.setDepart(rs.getString(4));
                att.setRemark(rs.getString(5));
                att.setBranchname(branchname);

                System.out.println(att.attToString());

                try {
                    URL url = new URL("http://ams-agri.qsslanka.com/api/attendance/write.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    //setting json object
                    String input = att.attToString();  /*from attendance string is created*/

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();


                    //saving response code to a variable
                    responseCode = conn.getResponseCode();


                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(responseCode == 200){
                    /**/
                }
                else{
                    return 0;  /*Return 0 if something fails*/
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 1; /*return 1 if success*/
    }




    /*------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------*/



    public int uploadOtByDuration( String to, String branchname){


        /*Fetch max ot ---------------------------------------------------------------------------*/

        String clockIn = "2005-05-05 05:05:05";
        String jsonString;
        String finalJsonString = "{\"data\":[{\"clock\":\"2005-05-05 05:05:05\"}]}";;
        try {
            URL url = new URL("http://ams-agri.qsslanka.com/api/ot/readmax.php?searchInput="+branchname);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((jsonString = br.readLine()) != null) {
                //System.out.println(jsonString);
                finalJsonString = jsonString;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(finalJsonString);

        //manipulating json object
        try{
            JSONObject object = new JSONObject(finalJsonString);
            JSONArray Jarray  = object.getJSONArray("data");



            for(int i=0;i<Jarray.length();i++){
                JSONObject jsonObject1 = Jarray.getJSONObject(i);
                System.out.println(jsonObject1);
                clockIn = (jsonObject1.optString("clockIn")).toString();
                System.out.println(clockIn);


            }
            if (clockIn == "null"){
                clockIn = "2005-05-05 05:05:05";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*Write OT based on fetched max ot ---------------------------------------------------------------------------*/

        int responseCode = 0;
        Attendance ot;

        try{


            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(to);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(clockIn);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            System.out.println("At online DB Handler - 1");
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);



            ps = connection.prepareStatement(GET_OT_BY_DURATION);
            ps.setDate(1, sqlDate2);
            ps.setDate(2, sqlDate1);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            while(rs.next()){

                ot = new Attendance();
                ot.setuId(rs.getString(1));
                ot.setuName(rs.getString(2));
                ot.setClockIn(rs.getString(3));
                ot.setClockOut(rs.getString(4));
                ot.setOtHrs(rs.getInt(5));
                ot.setDate(rs.getString(6));
                ot.setBranchname(branchname);

                if(ot.getClockIn() == ot.getClockOut()){
                    continue;
                }

                System.out.println(ot.otToString());

                try {
                    URL url = new URL("http://ams-agri.qsslanka.com/api/ot/write.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    //setting json object
                    String input = ot.otToString();  /*from attendance string is created*/

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();


                    //saving response code to a variable
                    responseCode = conn.getResponseCode();


                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(responseCode == 200){
                    /**/
                }
                else{
                    return 0;  /*Return 0 if something fails*/
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 1; /*return 1 if success*/
    }

}