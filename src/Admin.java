import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//Admin class
public class Admin extends Employee
{
    
    //Attribute
    private static String ADID;
    
   
    
    //Constructor
    public Admin(int EI,String IDN,String fn, String Sn, String em,String usrn, String pass, int ag, String rl,String ad)
    {
        super(EI, IDN, fn, Sn, em, usrn, pass, ag, rl);
        ADID=ad;
    }//end of Constructor
    
    //Accessors

    public String getADID() {
        return ADID;
    }
    
    
    
    
    public String Tostring()
    {
        String sline = super.Tostring()+"#"+getADID();
        return sline;
    } 
}//end of Admin Class
