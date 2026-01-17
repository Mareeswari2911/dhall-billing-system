package connectionprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionProvider {
    
    private static final String URL = "jdbc:mysql://localhost:3306/dealership"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "2910"; 
    public static Connection getConnection()  {
        Connection con=null;
        try 
        { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console.");
        }
        return con;
    }
}