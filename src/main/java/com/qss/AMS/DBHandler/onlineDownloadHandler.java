package com.qss.AMS.DBHandler;

import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class onlineDownloadHandler {

    private Users user;
    private Attendance att;

    private Connection connection;

    public onlineDownloadHandler() { }

    @Autowired
    JdbcTemplate template;

    public ArrayList<Users> downloadEmployees(String branchname){

        ArrayList<Users> employeesList = new ArrayList<>();


        String jsonString;
        String finalJsonString = null;
        try {
            URL url = new URL("http://localhost/AMS-API/api/employee/read.php?searchInput="+branchname);
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

                user = new Users();

                user.setuID((jsonObject1.optString("userId")).toString());
                user.setuName((jsonObject1.optString("UserName")).toString());
                user.setGender((jsonObject1.optString("Gender")).toString());
                user.setUserdepart((jsonObject1.optString("deptName")).toString());
                user.setBranchname((jsonObject1.optString("branchName")).toString());
                employeesList.add(user);






            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(employeesList);
        return employeesList;
    }



    public ArrayList<Users> downloadLeaves(String from, String to, String branchname){




        ArrayList<Users> leaveList = new ArrayList<>();
        String jsonString;
        String finalJsonString = null;
        try {

            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(from);
            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(to);
            java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());


            URL url = new URL("http://localhost/AMS-API/api/leave/read.php?searchInput="+branchname+"&searchClock1="+sqlDate1+"&searchClock2="+sqlDate2);
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
                user = new Users();

                user.setuID((jsonObject1.optString("userId")).toString());
                user.setuName((jsonObject1.optString("userName")).toString());
                user.setLeaveStart((jsonObject1.optString("fromDate")).toString());
                user.setLeaveEnd((jsonObject1.optString("toDate")).toString());
                user.setLeaveSubmitted((jsonObject1.optString("submittedDate")).toString());
                user.setBranchname((jsonObject1.optString("branchName")).toString());
                user.setLeaveRemark((jsonObject1.optString("remarks")).toString());

                System.out.println(user.toString());


                leaveList.add(user);


            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return leaveList;
    }

    public ArrayList<Attendance> downloadAttendance(String from, String to, String branchname){





        ArrayList<Attendance> attList = new ArrayList<>();

        String jsonString;
        String finalJsonString = null;
        try {



            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(from);
            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(to);
            java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());



            URL url = new URL("http://localhost/AMS-API/api/attendance/read.php?searchInput="+branchname+"&searchClock1="+sqlDate1+"&searchClock2="+sqlDate2);
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
                att = new Attendance();
                att.setuId((jsonObject1.optString("userId")).toString());
                att.setuName((jsonObject1.optString("userName")).toString());
                att.setAttTime((jsonObject1.optString("clock")).toString());
                att.setRemark((jsonObject1.optString("remarks")).toString());
                att.setBranchname((jsonObject1.optString("branchName")).toString());
                System.out.println(att.toString());
                attList.add(att);

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return attList;
    }


    public ArrayList<Attendance> downloadOt(String from, String to, String branchname){



        ArrayList<Attendance> otList = new ArrayList<>();

        String jsonString;
        String finalJsonString = null;
        try {


            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(from);
            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(to);
            java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());


            URL url = new URL("http://localhost/AMS-API/api/ot/read.php?searchInput="+branchname+"&searchClock1="+sqlDate1+"&searchClock2="+sqlDate2);
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
                att = new Attendance();
                att.setuId((jsonObject1.optString("userId")).toString());
                att.setuName((jsonObject1.optString("userName")).toString());
                att.setClockIn((jsonObject1.optString("clockIn")).toString());
                att.setClockOut((jsonObject1.optString("clockOut")).toString());
                att.setDate((jsonObject1.optString("dateo")).toString());
                att.setOtHrs(Integer.parseInt((jsonObject1.optString("OT_or_LC_hrs")).toString()));
                att.setBranchname((jsonObject1.optString("branchName")).toString());
                System.out.println(att.toString());
                otList.add(att);

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return otList;
    }

}
