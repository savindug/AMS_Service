package com.qss.AMS.DBHandler;

import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHandler {

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


    public DBHandler() { }

    @Autowired
    JdbcTemplate template;

    public ArrayList<Users> getEmployees(){

        ArrayList<Users> employeesList = new ArrayList<>();

//        List<Users> employees = template.query(GET_EMPLOYEES_SQL, new RowMapper<Users>() {
//            @Override
//            public Users mapRow(ResultSet rs, int i) throws SQLException {
//                user = new Users();
//                user.setuID(rs.getString(1));
//                user.setuName(rs.getString(4));
//                user.setCreateDate(rs.getString(11));
//                System.out.println(user.getuID()+"\t"+user.getuName()+"\t"+user.getCreateDate()+"\t");
//                return user;
//            }
//        });

        

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

    public ArrayList<Attendance> getAttendanceByDuration(String from, String to){


        ArrayList<Attendance> attList = new ArrayList<>();

        try{

            connection = DBConnection.openConnection();
            Statement st = connection.createStatement();
            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(from);
            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(to);
            java.sql.Date sqlDate1= new java.sql.Date(date1.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
            ps = connection.prepareStatement(GET_ATTENDANCE_BY_ID);
            ps.setDate(1, sqlDate1);
            ps.setDate(2, sqlDate2);
            System.out.println("At DB Handler");
            System.out.println(sqlDate1);
            System.out.println(sqlDate2);
            ResultSet rs = ps.executeQuery();
            System.out.println("============== getAttendanceByDuration ==============");
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

}
