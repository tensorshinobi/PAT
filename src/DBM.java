//Class for Database management
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


public class DBM {
    //Attributes
    //private static Connection conn; 
    private static Connection dbconn;
    //private static Connection conn;
    private static PreparedStatement pst;
    static ResultSet rs;
    
    //Constructor
    public DBM(Connection Scon)
    {
        dbconn=Scon;
      
    }//end constructor
    
    public void DMBC(String URL)//checking if connection is valid
    {
        try
        {
            dbconn = DriverManager.getConnection(URL);
            System.out.println("connection good");  
        }
        catch(SQLException ex)
        {
            System.err.println("File not found");
        }  
    }//end 
}//end of class
