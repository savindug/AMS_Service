package com.qss.AMS;

import com.qss.AMS.Entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class QssAttendanceManagementApplication {

	public static void main(String[] args) {

		System.out.println("AMS Service Started Successfully!");
		SpringApplication.run(QssAttendanceManagementApplication.class, args);
	}


}
