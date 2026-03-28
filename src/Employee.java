
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Employee Class
public class Employee {
    //Attributes
    private static int EmployeeID;
    private static int   Age;
    private static String IDNumber;
    private static String FirstName;
    private static String Surname;
    private static String Email;
    private static String Username;
    private static String Password;
    private static String Role;
    
  
    
    public Employee(int EI,String IDN,String fn, String Sn, String em,String usrn, String pass, int ag, String rl)
    {
        EmployeeID = EI;
        IDNumber = IDN;
        FirstName = fn;
        Surname = Sn;
        Email = em;
        Username = usrn;
        Password = pass;
        Age = ag;
        Role = rl;
    }//end of Constructor
    
    //Accessor

    public  int getEmployeeID() {
        return EmployeeID;
    }

    public  String getIDNumber() {
        return IDNumber;
    }

    public  String getFirstName() {
        return FirstName;
    }

    public String getSurname() {
        return Surname;
    }

    public String getEmail() {
        return Email;
    }

    public static String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public int getAge() {
        return Age;
    }

    public String getRole() {
        return Role;
    }
    
    public String  getDateTime()
    {
        String sdate;
        
        LocalDateTime ldt = LocalDateTime.parse(String.valueOf(LocalDateTime.now()));
        sdate = String.valueOf(ldt);
        return sdate;
    }
     
    
    
    public String Tostring()
    {
        String sline = getDateTime()+"#"+getEmployeeID()+"#"+getIDNumber()+"#"+getFirstName()+"#"+getSurname()+"#"+getEmail()+"#"+getUsername()+"#"+getPassword()+"#"+getAge()+"#"+getRole();
        return sline;
    }
    
    
    
}//end of Employee Class
