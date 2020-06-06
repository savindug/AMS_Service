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

public class onlineDBHandler {

    private Users user;
    private Attendance att;

    private Connection connection;
    private PreparedStatement ps;

    private String GET_EMPLOYEES_SQL = "select DISTINCT ras_Users.PIN, ras_Users.UserName, ras_Users.Sex, ras_Dept.DeptName, ras_Users.CreateDate, ras_Users.CreateDate \n" +
            "				  from ras_Users, ras_Dept \n" +
            "				  where ras_Dept.DeptId = ras_Users.DeptId";
    private String GET_ALL_ATTENDANCE_SQL = "select DISTINCT [ras_Users].PIN as Employee_ID, [ras_Users].[UserName] as User_Name, [ras_AttRecord].[Clock] as Clock, [ras_AttTypeItem].[ItemName] as Attend_Type, [ras_AttRecord].[Remark] from  \n" +
            "        [ras_AttRecord], [ras_Dept], [ras_Users], [ras_AttTypeItem] \n" +
            "                  where  [ras_Users].[UID] = [ras_AttRecord].ID and [ras_AttRecord].[AttTypeId] = [ras_AttTypeItem].[ItemId]";

    private  String GET_ALL_LEAVES_SQL = "select DISTINCT ras_Users.pin, ras_Users.UserName, ras_AttLeaveRecord.FromDate, ras_AttLeaveRecord.ToDate, ras_AttLeaveRecord.LastUpdatedDate, ras_AttLeaveRecord.Remark  from\n" +
            "				  ras_AttLeaveRecord\n" +
            "					 inner join ras_Users\n" +
            "					 on ras_AttLeaveRecord.UID = ras_Users.UID \n" +
            "					 order by ras_Users.PIN";


    private String GET_ATTENDANCE_BY_ID = "select DISTINCT [ras_Users].PIN as Employee_ID, [ras_Users].[UserName] as User_Name, [ras_AttRecord].[Clock] as Clock, [ras_AttTypeItem].[ItemName] as Attend_Type, [ras_AttRecord].[Remark] \n" +
            "from    [ras_AttRecord], [ras_Dept], [ras_Users], [ras_AttTypeItem] \n" +
            " where  [ras_Users].[UID] = [ras_AttRecord].ID and [ras_AttRecord].[AttTypeId] = [ras_AttTypeItem].[ItemId] and DateValue(Clock) between ?  and ? ";


    public onlineDBHandler() { }

    @Autowired
    JdbcTemplate template;

    public ArrayList<Users> getEmployees(){

        ArrayList<Users> employeesList = new ArrayList<>();


        try{
            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(GET_EMPLOYEES_SQL);
            System.out.println("============== getEmployees ==============");
            while(rs.next()){
                user = new Users();
                user.setuID(rs.getString(1));
                user.setuName(rs.getString(2));
                user.setGender(rs.getString(3));
                user.setUserdepart(rs.getString(4));
                user.setLastLoggedIn(rs.getString(5));
                user.setCreateDate(rs.getString(6));
                System.out.println(user.toString());
                employeesList.add(user);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return employeesList;
    }

    public ArrayList<Attendance> getAllAttendance(){

        ArrayList<Attendance> attList = new ArrayList<>();

        try{
            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL_ATTENDANCE_SQL);
            System.out.println("============== getAllAttendance ==============");
            while(rs.next()){
                att = new Attendance();
                att.setuId(rs.getString(1));
                att.setuName(rs.getString(2));
                att.setAttTime(rs.getString(3));
                att.setVerifyMode(rs.getString(4));
                att.setRemark(rs.getString(5));
                System.out.println(att.toString());
                attList.add(att);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return attList;
    }

    public ArrayList<Users> getAllLeaves(){

        ArrayList<Users> leaveList = new ArrayList<>();
        Users leave;
        ResultSet rs = null;
        try{
            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            rs = st.executeQuery(GET_ALL_LEAVES_SQL);
            System.out.println("============== getAllLeaves ==============");
            while(rs.next()){
                leave = new Users();
                leave.setuID(rs.getString(1));
                leave.setuName(rs.getString(2));
                leave.setLeaveStart(rs.getString(3));
                leave.setLeaveEnd(rs.getString(4));
                leave.setLeaveSubmitted(rs.getString(5));
                leave.setLeaveRemark(rs.getString(6));
                System.out.println(leave.toString());
                leaveList.add(leave);

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return leaveList;
    }

    public int getAttendanceByDuration(String from, String to, String branchname){


        /*Fetch max clock ---------------------------------------------------------------------------*/

        String clock = "2005-05-05 05:05:05";
        String jsonString;
        String finalJsonString = null;
        try {
            URL url = new URL("http://localhost/AMS-API/api/attendance/readmax.php?searchInput="+branchname);
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
        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*Write Att based on fetched max clock ---------------------------------------------------------------------------*/

        int responseCode = 0;


        try{


            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(to);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(clock);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);
            ps = connection.prepareStatement(GET_ATTENDANCE_BY_ID);
            ps.setDate(1, sqlDate1);
            ps.setDate(2, sqlDate2);
            ResultSet rs = ps.executeQuery();
            System.out.println("============== getAttendanceByDuration ==============");
            while(rs.next()){
                att = new Attendance();
                att.setuId(rs.getString(1));
                att.setuName(rs.getString(2));
                att.setAttTime(rs.getString(3));
                att.setVerifyMode(rs.getString(4));
                att.setRemark(rs.getString(5));
                att.setBranchname(branchname);
                System.out.println(att.attToString());

                try {
                    URL url = new URL("http://localhost/AMS-API/api/attendance/write.php");
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

}