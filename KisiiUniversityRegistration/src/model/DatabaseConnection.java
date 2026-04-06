package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // XAMPP default credentials
    private static final String URL = "jdbc:mysql://localhost:3306/kisii_university";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP default password is blank

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
