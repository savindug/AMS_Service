package com.qss.AMS.Controller;

import com.qss.AMS.DBHandler.DBHandler;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IControllerImpl implements IController{

    DBHandler dbHandler = new DBHandler();

    @Override
    public String hello() {
        return "Hello";
    }

    @Override
    public ArrayList<Users> getEmployees() {
        return dbHandler.getEmployees();
    }

    @Override
    public ArrayList<Attendance> getAllAttendance() {
        return dbHandler.getAllAttendance();
    }

    @Override
    public ArrayList<Users> getAllLeaves() {
        return dbHandler.getAllLeaves();
    }

    @Override
    public ArrayList<Attendance> getAttendanceByDuration(String from, String to) {
        return dbHandler.getAttendanceByDuration(from, to);
    }

    @Override
    public ArrayList<Users> getLeavesByDuration(String from, String to) {
        return dbHandler.getLeavesByDuration(from, to);
    }

    @Override
    public ArrayList<Attendance> getOTByDuration(String from, String to) {
        return dbHandler.getOTByDuration(from, to);
    }


    @Override
    public String usersExport(ArrayList<Users> usrL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException {
        new WorkbookFactory();

        String flag = null;

        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        //Row row = sheet.createRow(2); //Row created at line 3

        String ls = null;

        Row headerRow = sheet.createRow(0); //Create row at line 0
        for(int headings = 0; headings < headers.size(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(headers.get(headings));//Write column name
            sheet.autoSizeColumn(headings);
        }

        int rowNum = 1;

        if(fl == 1){
            for(Users urs: usrL){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(urs.getuID());
                row.createCell(1)
                        .setCellValue(urs.getuName());
                row.createCell(2)
                        .setCellValue(urs.getLeaveStart());
                row.createCell(3)
                        .setCellValue(urs.getLeaveEnd());
                row.createCell(4)
                        .setCellValue(urs.getLeaveSubmitted());
            }

            ls = "\\Leave_List_";

        }else if(fl == 2){
            for(Users urs: usrL){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(urs.getuID());
                row.createCell(1)
                        .setCellValue(urs.getuName());
                row.createCell(2)
                        .setCellValue(urs.getUserdepart());
                row.createCell(3)
                        .setCellValue(urs.getGender());
                row.createCell(4)
                        .setCellValue(urs.getCreateDate());
            }
            ls = "\\Employee_List_";
        }


        for(int headings = 0; headings < headers.size(); headings++){ //For each column
            sheet.autoSizeColumn(headings);
        }

        String fn = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());

        String fileName = path+ls+fn+".xlsx";

        System.out.println("pathN: "+fn);

        File newFile = new File(fileName);
        System.out.println(fileName);
        newFile.createNewFile(); // if file already exists will do nothing
        FileOutputStream out = new FileOutputStream(newFile);
        wb.write(out);//Save the file

        if(newFile.exists()){
            System.out.println("Excel File has successfully Exported to "+fileName);
            flag = fileName;
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    @Override
    public String attExport(ArrayList<Attendance> attL, String path, ArrayList<String> headers, int fl) throws FileNotFoundException, IOException {

        new WorkbookFactory();

        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        //Row row = sheet.createRow(2); //Row created at line 3

        String flag = null;
        String ls = null;

        Row headerRow = sheet.createRow(0); //Create row at line 0
        for(int headings = 0; headings < headers.size(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(headers.get(headings));//Write column name
            sheet.autoSizeColumn(headings);
        }

        int rowNum = 1;

        if(fl == 1){
            for(Attendance att: attL){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(att.getuId());
                row.createCell(1)
                        .setCellValue(att.getuName());
                row.createCell(2)
                        .setCellValue(att.getDepart());
                row.createCell(3)
                        .setCellValue(att.getAttTime());
                row.createCell(4)
                        .setCellValue(att.getVerifyMode());
            }
            ls = "\\Attendance_List_";
        }else if(fl == 2){
            for(Attendance att: attL){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0)
                        .setCellValue(att.getuId());
                row.createCell(1)
                        .setCellValue(att.getuName());
                row.createCell(2)
                        .setCellValue(att.getClockIn());
                row.createCell(3)
                        .setCellValue(att.getClockOut());
                row.createCell(4)
                        .setCellValue(att.getOtHrs());
                row.createCell(5)
                        .setCellValue(att.getDate());
            }
            ls = "\\OT_List_";
        }

        for(int headings = 0; headings < headers.size(); headings++){ //For each column
            sheet.autoSizeColumn(headings);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date dt = new Date(timestamp.getTime());


        String fn = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());

        String fileName = path+ls+fn+".xlsx";

        System.out.println("pathN: "+path);

        File newFile = new File(fileName);
        System.out.println(fileName);
        newFile.createNewFile(); // if file already exists will do nothing
        FileOutputStream out = new FileOutputStream(newFile);
        wb.write(out);//Save the file

        if(newFile.exists()){
            System.out.println("Excel File has successfully Exported to "+fileName);

            flag = fileName;
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return flag;
    }
}
