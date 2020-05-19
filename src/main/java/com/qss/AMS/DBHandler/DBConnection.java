package com.qss.AMS.DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:ucanaccess://Database\\RAS.mdb";

    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";

    private static final String USERNAME = "Admin";

    private static final String PASSWORD = "ras258";

    public static Connection connection = null;

    public static Connection openConnection() {

//checking for the connection---------------------------
        if (connection != null) {

            return connection;

        } else {

            try {

                Class.forName(DRIVER);

                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                if (connection != null) {
                    System.out.println("Successfully Connected to the Database!");

                }

            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            } catch (SQLException e) {

                e.printStackTrace();

            }

            return connection;
        }
    }
}
