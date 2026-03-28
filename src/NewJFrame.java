
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class NewJFrame extends javax.swing.JFrame {

    
    //Static Variables
    //static Connection conn;
    //private static Connection dbconn;
    static PreparedStatement pst;
    static ResultSet rs;
    static int icount = 3;
    static String sql;
    static DBM dbm=null;  
    static Connection dbconn;
    static int columnIndex = 0;
    static int ii =0;
    private static final String[] WORDS = {
        "apple", "banana", "cherry", "date", "elderberry",
        "fig", "grape", "honeydew", "kiwi", "lemon"
    };
    private static final String SYMBOLS = "!@#$%^&*()-_+=";
    private static final String NUMBERS = "0123456789";
    private static int empid=0;
    
    //Methods
    //Report
    public static void Report(Connection con,JTable jt,String sql,LocalDate from,LocalDate to)  
    {
     try 
     {      int itotal=0;
            DefaultTableModel tblmodel = (DefaultTableModel) jt.getModel();
            tblmodel.setRowCount(0);
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {   
            Date ddate = rs.getDate("DatePurchased");
            
             
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String sddate = sdf.format(ddate);
            LocalDate date = LocalDate.parse(sddate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            
            if(date.isAfter(from) && date.isBefore(to))
            {
                String prnm = rs.getString("ProductName");
                String qnt = rs.getString("Quantity");
                String tot = String.valueOf(rs.getInt("Total"));
                int itot= rs.getInt("Total");
                itotal = itotal + itot;
                String TableData[] = {prnm,qnt,tot}; //Save array 

            tblmodel.addRow(TableData);
            }
            gtotal.setText(String.valueOf(itotal));
        }//end loop
         
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch
    }//end Report
    
    //Monitor
    public static void Monitor( Connection con,JTable jt,String sql,LocalDate from,LocalDate to)  
    {
        //Method to populate the table based on 
     try 
     {      
            DefaultTableModel tblmodel = (DefaultTableModel) jt.getModel();
            tblmodel.setRowCount(0);
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {   
            Date ddate = rs.getDate("DatePurchased");
            
             
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String sddate = sdf.format(ddate);
            LocalDate date = LocalDate.parse(sddate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            if(date.isAfter(from) && date.isBefore(to))
            {
            String emnm = rs.getString("EmployeeName");
            String cusnm = rs.getString("CustomerName");
            String prnm = rs.getString("ProductName");
            String tot = rs.getString("Total");
            String TableData[] = {emnm,cusnm,prnm,tot}; 
            
           
            tblmodel.addRow(TableData);
            }
            
        }//end loop
         
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch
    }//end Monitor
    
    
    //Stock_Get
    public static void Stock_Get( Connection con,JTable jt,String sql)  
    {
        //Method to Get Stock
     try 
     {   
         DefaultTableModel tblmodel = (DefaultTableModel) jt.getModel();
         tblmodel.setRowCount(0);
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {   
            String prid = rs.getString("ProductID");
            String prnm = rs.getString("Productname");
            String ctg = rs.getString("Category");
            String stk = rs.getString("Stock");
            String TableData[] = {prid,prnm,ctg,stk}; 
            cbxitems.addItem(prnm);
             tblmodel = (DefaultTableModel) jt.getModel();
            tblmodel.addRow(TableData);
          icount++;    
        }//end loop
         
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch
    }//end of Stock
    
    //Stock_Update
    public void Stock_Update(String sql,Connection con,String cbx,int qnt) 
    {
        //Method to Update stock of products
      try
      {
       PreparedStatement partinsert = con.prepareStatement(sql);
       int row = partinsert.executeUpdate();
        if (row>0)
        {
            JOptionPane.showMessageDialog(null, "Updated stock of"+cbx+" to "+qnt+" items");  
        }//end of if
        else
        {
            JOptionPane.showMessageDialog(null, "Failed to Update stock of"+cbx+" to "+qnt+" items"); 
        }
      }//end of try
      catch (SQLException ex)
      {
          System.err.println(ex);
      }
    }//end update
    
    //Stock_Delete
    public void Stock_Delete(Connection con, String sql,String Item) 
    {
        //Method to delete products from the product information 
        try
        {
            PreparedStatement partinsert = con.prepareStatement(sql);
            int row = partinsert.executeUpdate();
            if (row>0)
            {
                JOptionPane.showMessageDialog(null, "Deleted "+ Item);  
            }//end of if
            else
            {
                JOptionPane.showMessageDialog(null, "Failure"); 
            }//end of else
                     
       }//end of try
        catch (SQLException ex)
        {
            System.err.println(ex);
        }
    }  //end delete
    
    //Stock_Add
    public void Stock_Add(Connection con,String sql,int i,JTable jt)
    {
        //Method to add new Products
        int pid= 0;
        
      try
      {
       PreparedStatement partinsert = con.prepareStatement(sql);
       String[] Columns={"ProductName","Category","Stock","Price"};
       Statement SQLM = con.createStatement();  
       ResultSet rs = SQLM.executeQuery(sql);
       
       for (int a=1;a<=i;a++)
       {
       
        partinsert.setString(a,JOptionPane.showInputDialog("enter data for "+ Columns[a-1]));   
       }

       int row = partinsert.executeUpdate();
        if (row>0)
            JOptionPane.showMessageDialog(null, "Inserted");  
        else
            JOptionPane.showMessageDialog(null, "Failure/datatype error");   
      }
      catch (SQLException ex)
      {
          System.err.println(ex);
      }
    }//end Add
    
    //User_get
    public void User_get(Connection con,String sql)
    {
        //Method to Get Stock
     try 
     {
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {
            String usr = rs.getString("Username");
            cbxusername1.addItem(usr);
            
        }//end loop
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch 
    }//end of User_get
    
    //User_getin
    public void User_getin(Connection con,String sql)
    {
        //Method to populate textfields based on the username from the combo box 
        txttfldemid.setText("");
        tfldidnumber.setText("");
        tfldfirstname.setText("");
        tfldsurname.setText("");
        tfldemail.setText("");
        tfldusername1.setText("");
        tfldpassword.setText("");
        tfldage.setText(""); 
        
        
        
      try 
     {
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {
            String aid = rs.getString("AdminID");
            
            if(rs.wasNull())
            {
                txttfldemid.setText(String.valueOf(rs.getInt("EmployeeId")));
                tfldidnumber.setText(rs.getString("IDNumber"));
                tfldfirstname.setText(rs.getString("EmployeeName"));
                tfldsurname.setText(rs.getString("EmployeeSurname"));
                tfldemail.setText(rs.getString("Email"));
                tfldusername1.setText(rs.getString("Username"));
                tfldpassword.setText(rs.getString("Password"));
                tfldage.setText(String.valueOf(rs.getInt("Age")));
                cbxrole1.setSelectedItem(rs.getString("Role")); 
                tfldadid.setRequestFocusEnabled(false);
            }
            else
            {   
                tfldadid.setRequestFocusEnabled(true);
                txttfldemid.setText(String.valueOf(rs.getInt("EmployeeId")));
                tfldidnumber.setText(rs.getString("IDNumber"));
                tfldfirstname.setText(rs.getString("EmployeeName"));
                tfldsurname.setText(rs.getString("EmployeeSurname"));
                tfldemail.setText(rs.getString("Email"));
                tfldusername1.setText(rs.getString("Username"));
                tfldpassword.setText(rs.getString("Password"));
                tfldage.setText(String.valueOf(rs.getInt("Age")));
                cbxrole1.setSelectedItem(rs.getString("Role")); 
                tfldadid.setText(rs.getString("AdminID"));
            }
        }//end loop
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch
       
    }//end of User_getin
    
    //User_Createnew
    public void User_Createnew(Connection con,String sql,String role)
    {
        //Method to insert a new user information to the Employee database
        int emid = Integer.parseInt(txttfldemid.getText());
        String IDno = tfldidnumber.getText();
        String  surnme = tfldsurname.getText();
        String fnme = tfldfirstname.getText();
        String eml = tfldemail.getText();
        String usrnme = tfldusername1.getText();
        String passwd = tfldpassword.getText();
        int age= Integer.parseInt(tfldage.getText());
        String rle = cbxrole1.getItemAt(cbxrole1.getSelectedIndex());
        String adid = tfldadid.getText();
        
      try
      {
       PreparedStatement partinsert = con.prepareStatement(sql);
       
       
       
        partinsert.setInt(1,emid);  
        partinsert.setString(2, IDno);
        partinsert.setString(3, fnme);
        partinsert.setString(4,surnme);
        partinsert.setString(5, eml);
        partinsert.setString(6, usrnme);
        partinsert.setString(7, passwd);
        partinsert.setInt(8, age);
        partinsert.setString(9,rle);
        if(role.equals("Admin"))
        {
            partinsert.setString(10,adid);
        }
        
       

       int row = partinsert.executeUpdate();
        if (row>0)
            JOptionPane.showMessageDialog(null, "Inserted");  
        else
            JOptionPane.showMessageDialog(null, "Failure/datatype error");   
      }
      catch (SQLException ex)
      {
          System.err.println(ex);
      } 
    }//end of User_Createnew
    
    //User_Update
    public void User_Update(Connection con,String role)
    {
        //Method to update the information of an employee from the Employee database
        String sql="";
        
        int emid = Integer.parseInt(txttfldemid.getText());
        String IDno = tfldidnumber.getText();
        String  surnme = tfldsurname.getText();
        String fnme = tfldfirstname.getText();
        String eml = tfldemail.getText();
        String usrnme = tfldusername1.getText();
        String passwd = tfldpassword.getText();
        int age= Integer.parseInt(tfldage.getText());
        String rle = cbxrole1.getItemAt(cbxrole1.getSelectedIndex());
        String adid = tfldadid.getText();
        switch(role)
        {
            case "Admin":
                 sql = "Update Employee Set IDNumber='"+IDno+"',EmployeeName='"+fnme+"',EmployeeSurname='"+surnme+"',Email='"+eml+"',Username='"+usrnme+"',Password= '"+passwd+"',Age="+age+",Role='"+rle+"' Where Username= '"+cbxusername1.getItemAt(cbxusername1.getSelectedIndex())+"'";
                break;
                
            case "Employee":
                 sql = "Update Employee Set EmployeeID="+emid+", IDNumber= '"+IDno+"', EmployeeName= '"+fnme+"', EmployeeSurname= '"+surnme+"', Email= '"+eml+"', Username= '"+usrnme+"', Password= '"+passwd+"', Age= "+age+", Role= '"+rle+"',AdminID='"+adid+"' Where Username= '"+cbxusername1.getItemAt(cbxusername1.getSelectedIndex())+"'";
        }//end of if 
    
        
        try
        {
            PreparedStatement partinsert = con.prepareStatement(sql);
            int row = partinsert.executeUpdate();
            if (row>0)
                JOptionPane.showMessageDialog(null, "Updated");  
            else
                JOptionPane.showMessageDialog(null, "Failure");   
        }
        catch (SQLException ex)
        {
          System.err.println(ex);
        }
    }//end of User_Update
    
    //User_Delete
    public void User_Delete(Connection con, String sql)
    {
        //Method to delete a user from the employee database
        try
        {
            PreparedStatement partinsert = con.prepareStatement(sql);
            int row = partinsert.executeUpdate();
            if (row>0)
                JOptionPane.showMessageDialog(null, "Deleted");   
            else
                JOptionPane.showMessageDialog(null, "Failure");      
        }
        catch (SQLException ex)
        {
            System.err.println(ex);
        }
    }//end of User_delete
    
    //EmployeeAdd
    public static void EmployeeAdd(Connection con, JTable jt, String sql)
    {
        //Method to add orders to the table
        
        try 
     {   
         
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
         while (rs.next())
        {   
            
            DefaultTableModel tblmodel = (DefaultTableModel) jt.getModel();
            tblmodel.setRowCount(ii);
            String prid = String.valueOf(rs.getInt("ProductID"));
            String prnm = rs.getString("Productname");
            String prx = rs.getString("Price");
            String qnt = rs.getString("Quantity");
            String tot = rs.getString("Total");
            String TableData[] = {prid,prnm,prx,qnt,tot}; 
            cbxitems.addItem(prnm);
            tblmodel = (DefaultTableModel) jt.getModel();
            tblmodel.insertRow(ii,TableData);
            ii++; 
            
        }//end loop
        
     }//end try
     catch (SQLException ex)
     {
         System.err.println(ex);
     }//end catch
    }//end of EmployeeAdd
    
    //Emplyee_Remove
    public static void deleteRowByValue(JTable table, Object valueToFind, int columnIndex) 
    {
        //Method to remove a row of data from the table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) 
        {
            if (model.getValueAt(i, columnIndex).equals(valueToFind)) 
            {
                model.removeRow(i);
                ii--;
                System.out.println("ii: "+ii);
                break; // Only removes the first matching row
            }
        }
    }//end of deleteRowByValue
    
    //Populate the box
    public static void populateComboBoxFromColumn(JTable table, JComboBox<String> comboBox, int columnIndex) 
    {
        //Method to populate the combo box based from Product column in the database  
        comboBox.removeAllItems();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i< model.getRowCount(); i++) 
        {
            String value = String.valueOf(model.getValueAt(i, columnIndex));
            comboBox.addItem(value);
            
        }//end of loop
    }//end of populateComboBoxFromColumn
    
    //Password Generator
    public static String generatePassword() 
    {
        //Method to generate a random password
        Random random = new Random();

        // Select two random words
        String word1 = WORDS[random.nextInt(WORDS.length)];
        String word2 = WORDS[random.nextInt(WORDS.length)];

        // Select a random symbol
        char symbol = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));

        // Select a random number
        char number = NUMBERS.charAt(random.nextInt(NUMBERS.length()));

        // Combine them into a password
        String password = word1 + symbol + word2 + number;

        // Ensure the password is at least 8 characters long
        while (password.length() < 8) {
            password += NUMBERS.charAt(random.nextInt(NUMBERS.length()));
        }

        return password;
    }
    
    
    //GetMac Address
    public static String getMacaddress()
    {
        try {
            // Get the local host address
            InetAddress localHost = InetAddress.getLocalHost();

            // Get the network interface for the local host address
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

            // Get the MAC address
            byte[] macAddressBytes = networkInterface.getHardwareAddress();

            // Convert the MAC address bytes to a readable format
            StringBuilder macAddress = new StringBuilder();
            for (int i = 0; i < macAddressBytes.length; i++) {
                macAddress.append(String.format("%02X%s", macAddressBytes[i], (i < macAddressBytes.length - 1) ? "-" : ""));
            }

            // Print the MAC address
            return "MAC Address: " + macAddress.toString();
        } 
        catch (UnknownHostException | SocketException e) 
        {
            return e.toString();
        }
    }//end of getMacaddress
    
    //Login logs
    public static void Log(Connection con,String role,String Usr,String pass,boolean bfound)
    { 
        //method to log all logins 
       String sline="";
       try
       {
            String sql = "SELECT * FROM Employee Where Username='"+Usr+"'";
            int EmployeeID,Age;
            String IDNumber,EmployeeName,EmployeeSurname,Email,Username,Password,Role,ADID;
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
            FileWriter out = new FileWriter("logs.txt",true);
            
            //SQL
             while(rs.next())
            {
                EmployeeID = rs.getInt("EmployeeID");
                IDNumber = rs.getString("IDNumber");
                EmployeeName = rs.getString("EmployeeName");
                EmployeeSurname = rs.getString("EmployeeSurname");
                Email = rs.getString("Email");
                Username = rs.getString("Username");
                Password = rs.getString("Password");
                Age = rs.getInt("Age");
                Role = rs.getString("Role");
                ADID = rs.getString("AdminID");
                Employee em = new Employee(EmployeeID, IDNumber, EmployeeName, EmployeeSurname, Email, Username, Password, Age, Role);
                Admin ad = new Admin(EmployeeID, IDNumber, EmployeeName, EmployeeSurname, Email, Username, Password, Age, Role,ADID);
                //File
                
            
            
                if(role.equals("Admin"))
                {
                    out.append(ad.Tostring()+"#"+getMacaddress()+"\n");  
                }
                else if(role.equals("Employee"))
                {
                    out.append(em.Tostring()+"#"+getMacaddress()+"\n");
                }
                
                
                
               
                
            }//end of loop
            if(!bfound)
            {
                out.append(LocalDateTime.now().toString()+"# Failed attempt#"+role+"#"+Usr+"#"+pass+"#"+getMacaddress()+"\n");
                System.out.println("Written!!!");
            }
            
            out.close();
            
       }//end of try
       catch(IOException | SQLException ex)
       {
           System.err.println(ex);
       }//end of catch
       
    }//end of login logs
    
    //Check Login
    public String CheckUserNamePassword(Connection con,String username,String password,String role)
    { 
        //Method to check login and log user into a window based on the role of the user
        boolean bfound=false;
        String emname="";
        String comment="";  
        
        try 
        {
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery("Select * From Employee Where Username='"+username+"' AND Password='"+password+"'"+"AND Role='"+role+"'");
            while (rs.next())
            {
                emname  = rs.getString("EmployeeName");
                empid = rs.getInt("EmployeeID");
                String usrn = rs.getString("Username");//change to your database
                String pass = rs.getString("Password");
                String rolee = rs.getString("Role");
                if (username.equals(usrn) && password.equals(pass) && role.equals(rolee))
                    bfound=true;
            }//end loop
        
            if (bfound)
            {
                switch(role)
                {
                    case "Admin":
                        AdminPane.setVisible(true);
                        LoginPane.setVisible(false);
                        getMacaddress();
                        Log(con, role, username, password,bfound);
                        comment=  "Welcome, "+emname;
                        break;
                    case "Employee":
                        EmployeePane.setVisible(true);
                        LoginPane.setVisible(false);
                        getMacaddress();
                        Log(con, role, username, password,bfound);
                        comment= "Welcome, "+emname;
                        break;
                        
                }//end of switch
               
                 
            }
         
            
            else
            {
                icount--;
                
                comment="login unsuccessful\n"+icount+" Attempts left";
                Log(con, role, username, password,bfound);
                
                if(icount==0)
                {
                    comment="You have used up all your 3 attempts. Please Close the program, contact admin if you cannot remeber your login info and then try again";
                    try{
                        LoginPane.setClosed(true);
                    }//end of try
                    catch(PropertyVetoException ex)
                    {
                        System.err.println(ex);
                    }//end of catch
                    
                }
            }//end of else
             
                    
        }//end try
        catch (SQLException ex)
        {
         System.err.println(ex);
        }//end catch  
        return comment;
}//end CheckUserNamePaswword
    
    //Checkout
    public static void Checkout(Connection con, JTable jt, String sql)
    {   
        //Method to insert data into the order table in the database
        //Customers
        int cid = Integer.parseInt(tfldcid.getText());
        String cunme = txfldfirstname.getText();
        String csnme = txfldsurname.getText();
        String ceml = txfldemail.getText();
        int age = Integer.parseInt(tfldage1.getText());
        String gen = tfldgender.getText();
       
        DefaultTableModel tblmodel = (DefaultTableModel) jt.getModel();
        try
        {
            
            Statement SQLM = con.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
            
            PreparedStatement partinsert = con.prepareStatement(sql);
            partinsert.setInt(1,cid);  
            partinsert.setString(2, cunme);
            partinsert.setString(3, csnme);
            partinsert.setString(4,ceml);
            partinsert.setInt(5, age);
            partinsert.setString(6, gen);
                
            int row = partinsert.executeUpdate();
            if (row>0)
                JOptionPane.showMessageDialog(null, "Inserted");  
            else
                JOptionPane.showMessageDialog(null, "Failure/datatype error");
            
        }
        catch(SQLException ex)
        {
            System.err.println(ex);
        }
        
        //Orders
        int i = 0;
        sql = "INSERT INTO Orders(OrderID,CustomerID,EmployeeID,ProductID,DatePurchased,Quantity,Total) VALUES(?,?,?,?,?,?,?)";
        while(i<jt.getRowCount())
        {
            int oid = Integer.parseInt(tfldoid.getText());//Integer.parseInt(JOptionPane.showInputDialog("Please enter the orderID"));
            int prid = Integer.parseInt(tblmodel.getValueAt(i, 0).toString());
            LocalDate date = LocalDate.now();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            LocalDate sddate = LocalDate.parse(sdf.format(date));
           
            LocalDate Dtepurchsd = LocalDate.parse(LocalDate.now().toString(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int qnt = Integer.parseInt(tblmodel.getValueAt(i, 3).toString());
            int tot = Integer.parseInt(tblmodel.getValueAt(i, 4).toString());
            java.sql.Date sqlDate = java.sql.Date.valueOf(Dtepurchsd);
            try
            {
                Statement SQLM = con.createStatement();  
                ResultSet rs = SQLM.executeQuery(sql); 
                PreparedStatement partinsert = con.prepareStatement(sql);
                partinsert.setInt(1,oid);  
                partinsert.setInt(2, cid);
                partinsert.setInt(3, empid);
                partinsert.setInt(4,prid);
                partinsert.setDate(5,sqlDate);
                partinsert.setInt(6, qnt);
                partinsert.setInt(7, tot);
                
                int row = partinsert.executeUpdate();
                if (row>0)
                    JOptionPane.showMessageDialog(null, "Inserted");  
                else
                    JOptionPane.showMessageDialog(null, "Failure/datatype error");
                
            }
            catch(SQLException ex)
            {
                System.err.println(ex);
            }
            i++;
        }
            
        
    }
    
    
    
    
    public NewJFrame() {
        initComponents(); 
        String url ="jdbc:ucanaccess://"+System.getProperty("user.dir")+"//Employee.accdb";
       try
       {
        dbconn = DriverManager.getConnection(url);
               
                dbm = new DBM(dbconn);
         dbm.DMBC(url);
         
       }
       catch (SQLException ex)
       {
           System.err.println(ex);
       }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoginPane = new javax.swing.JInternalFrame();
        cbxRole = new javax.swing.JComboBox<>();
        lblLogin = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        tfldUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        btnhelp = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        chkshwpass = new javax.swing.JCheckBox();
        tfldPassword = new javax.swing.JPasswordField();
        Background = new javax.swing.JLabel();
        AdminPane = new javax.swing.JInternalFrame();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Usermanagement = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfldidnumber = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfldsurname = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfldfirstname = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfldemail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfldusername1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfldpassword = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfldage = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cbxrole1 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cbxusername1 = new javax.swing.JComboBox<>();
        btndelete = new javax.swing.JButton();
        btnupdate = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        btncreatenew = new javax.swing.JButton();
        btnuserget = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txttfldemid = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfldadid = new javax.swing.JTextField();
        Stock = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblstock = new javax.swing.JTable();
        btnget = new javax.swing.JButton();
        btnupdate1 = new javax.swing.JButton();
        btndelete1 = new javax.swing.JButton();
        spnquantity = new javax.swing.JSpinner();
        btnadd1 = new javax.swing.JButton();
        cbxitems = new javax.swing.JComboBox<>();
        Monitor1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblmonitor = new javax.swing.JTable();
        btnmonitor = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        dtechsrfrom = new com.toedter.calendar.JDateChooser();
        dtechsrto = new com.toedter.calendar.JDateChooser();
        Report = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblreport = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        gtotal = new javax.swing.JLabel();
        btngenerate = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        dtechsr1from = new com.toedter.calendar.JDateChooser();
        dtechsr1to = new com.toedter.calendar.JDateChooser();
        jLabel22 = new javax.swing.JLabel();
        btnhelp1 = new javax.swing.JButton();
        EmployeePane = new javax.swing.JInternalFrame();
        jLabel20 = new javax.swing.JLabel();
        txfldfirstname = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txfldsurname = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txfldemail = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblemployee = new javax.swing.JTable();
        btnadd = new javax.swing.JButton();
        btnremove = new javax.swing.JButton();
        cbxadd = new javax.swing.JComboBox<>();
        spnadd = new javax.swing.JSpinner();
        btncheckout = new javax.swing.JButton();
        cbxremove = new javax.swing.JComboBox<>();
        btnhelp2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        tfldcid = new javax.swing.JTextField();
        tfldage1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tfldgender = new javax.swing.JTextField();
        tfldoid = new javax.swing.JTextField();
        Background1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 600));
        setPreferredSize(new java.awt.Dimension(700, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LoginPane.setTitle("Login Window");
        LoginPane.setMaximumSize(new java.awt.Dimension(700, 600));
        LoginPane.setMinimumSize(new java.awt.Dimension(700, 600));
        LoginPane.setName(""); // NOI18N
        LoginPane.setPreferredSize(new java.awt.Dimension(700, 600));
        LoginPane.setVisible(true);
        LoginPane.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Employee", " " }));
        LoginPane.getContentPane().add(cbxRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, -1, -1));

        lblLogin.setFont(new java.awt.Font("Microsoft YaHei", 1, 48)); // NOI18N
        lblLogin.setForeground(new java.awt.Color(255, 255, 255));
        lblLogin.setText("Login");
        LoginPane.getContentPane().add(lblLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, -1));

        lblGender.setFont(new java.awt.Font("Microsoft YaHei", 1, 24)); // NOI18N
        lblGender.setForeground(new java.awt.Color(255, 255, 255));
        lblGender.setText("Role");
        LoginPane.getContentPane().add(lblGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, -1, -1));

        lblUsername.setFont(new java.awt.Font("Microsoft YaHei", 3, 24)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblUsername.setText("Username");
        LoginPane.getContentPane().add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 130, -1));

        tfldUsername.setText("Enter your name");
        tfldUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfldUsernameMouseClicked(evt);
            }
        });
        tfldUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfldUsernameActionPerformed(evt);
            }
        });
        LoginPane.getContentPane().add(tfldUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 140, -1));

        lblPassword.setFont(new java.awt.Font("Microsoft YaHei", 1, 24)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblPassword.setText("Password");
        LoginPane.getContentPane().add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, -1, -1));

        btnhelp.setText("Help");
        LoginPane.getContentPane().add(btnhelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        btnLogin.setFont(new java.awt.Font("Microsoft YaHei", 0, 18)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        LoginPane.getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, -1, -1));

        chkshwpass.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        chkshwpass.setText("Show Password");
        chkshwpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkshwpassActionPerformed(evt);
            }
        });
        LoginPane.getContentPane().add(chkshwpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, -1, -1));

        tfldPassword.setText("jPasswordField1");
        tfldPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfldPasswordMouseClicked(evt);
            }
        });
        LoginPane.getContentPane().add(tfldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 140, -1));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login.png"))); // NOI18N
        Background.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackgroundMouseClicked(evt);
            }
        });
        LoginPane.getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 670, 560));

        getContentPane().add(LoginPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        AdminPane.setBackground(new java.awt.Color(51, 51, 255));
        AdminPane.setBorder(null);
        AdminPane.setForeground(new java.awt.Color(0, 0, 255));
        AdminPane.setTitle("Admin Window");
        AdminPane.setMaximumSize(new java.awt.Dimension(700, 600));
        AdminPane.setMinimumSize(new java.awt.Dimension(700, 600));
        AdminPane.setName(""); // NOI18N
        AdminPane.setVisible(false);
        AdminPane.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 0));

        Usermanagement.setBackground(new java.awt.Color(0, 102, 102));
        Usermanagement.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel3.setText("User Management");
        Usermanagement.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 6, -1, -1));

        jLabel4.setText("ID Number");
        Usermanagement.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));
        Usermanagement.add(tfldidnumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 124, -1));

        jLabel5.setText("Surname");
        Usermanagement.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));
        Usermanagement.add(tfldsurname, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 150, -1));

        jLabel6.setText("Firstname");
        Usermanagement.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));
        Usermanagement.add(tfldfirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 190, -1));

        jLabel7.setText("Email");
        Usermanagement.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));
        Usermanagement.add(tfldemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 190, -1));

        jLabel8.setText("Username");
        Usermanagement.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));
        Usermanagement.add(tfldusername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 158, -1));

        jLabel9.setText("Password");
        Usermanagement.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));
        Usermanagement.add(tfldpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 167, -1));

        jLabel11.setText("Age");
        Usermanagement.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));
        Usermanagement.add(tfldage, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 46, -1));

        jLabel12.setText("Role");
        Usermanagement.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        cbxrole1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Employee", "Customer", " " }));
        Usermanagement.add(cbxrole1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        jLabel13.setText("Username");
        Usermanagement.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 77, 20));

        cbxusername1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxusername1ItemStateChanged(evt);
            }
        });
        cbxusername1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxusername1ActionPerformed(evt);
            }
        });
        Usermanagement.add(cbxusername1, new org.netbeans.lib.awtextra.AbsoluteConstraints(399, 60, 140, -1));

        btndelete.setText("Delete");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });
        Usermanagement.add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(383, 119, -1, -1));

        btnupdate.setText("Update");
        btnupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateActionPerformed(evt);
            }
        });
        Usermanagement.add(btnupdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(473, 119, -1, -1));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/refresh.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        Usermanagement.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 30, 30));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/refresh.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        Usermanagement.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, 30, 30));

        btncreatenew.setText("Create new");
        btncreatenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncreatenewActionPerformed(evt);
            }
        });
        Usermanagement.add(btncreatenew, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, -1, -1));

        btnuserget.setText("Get");
        btnuserget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnusergetActionPerformed(evt);
            }
        });
        Usermanagement.add(btnuserget, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, -1, -1));

        jLabel16.setText("EmployeeID");
        Usermanagement.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        txttfldemid.setToolTipText("");
        Usermanagement.add(txttfldemid, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 130, -1));

        jButton2.setText("Get User Details");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Usermanagement.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 120, -1, -1));

        jLabel1.setText("AdminID");
        Usermanagement.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        Usermanagement.add(tfldadid, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 80, -1));

        jTabbedPane1.addTab("UserManagement", Usermanagement);

        Stock.setBackground(new java.awt.Color(0, 51, 102));
        Stock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblstock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ProductID", "ProductName", "Category", "Stock"
            }
        ));
        jScrollPane2.setViewportView(tblstock);

        Stock.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 682, 260));

        btnget.setText("Get");
        btnget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngetActionPerformed(evt);
            }
        });
        Stock.add(btnget, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, -1, -1));

        btnupdate1.setText("Update");
        btnupdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdate1ActionPerformed(evt);
            }
        });
        Stock.add(btnupdate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, -1, -1));

        btndelete1.setText("Delete");
        btndelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndelete1ActionPerformed(evt);
            }
        });
        Stock.add(btndelete1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, -1, -1));
        Stock.add(spnquantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 280, 50, -1));

        btnadd1.setText("Add");
        btnadd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnadd1ActionPerformed(evt);
            }
        });
        Stock.add(btnadd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 320, -1, -1));

        Stock.add(cbxitems, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 162, -1));

        jTabbedPane1.addTab("Stock", Stock);

        Monitor1.setBackground(new java.awt.Color(204, 0, 0));
        Monitor1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblmonitor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EmployeeName", "Customer", "PoductName", "Total"
            }
        ));
        jScrollPane5.setViewportView(tblmonitor);

        Monitor1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 688, 238));

        btnmonitor.setText("Monitor ");
        btnmonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmonitorActionPerformed(evt);
            }
        });
        Monitor1.add(btnmonitor, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 363, -1, -1));

        jLabel30.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel30.setText("From");
        Monitor1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 279, -1, -1));

        jLabel32.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel32.setText("To");
        Monitor1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 315, -1, -1));
        Monitor1.add(dtechsrfrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 280, 150, -1));
        Monitor1.add(dtechsrto, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 150, -1));

        jTabbedPane1.addTab("Monitor", Monitor1);

        Report.setBackground(new java.awt.Color(204, 204, 0));
        Report.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Report.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblreport.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tblreport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Quantity", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblreport);

        Report.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 289));

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        jLabel2.setText("Grand Total:");
        Report.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 301, -1, -1));

        gtotal.setText("0");
        Report.add(gtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(196, 302, -1, -1));

        btngenerate.setText("Generate");
        btngenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngenerateActionPerformed(evt);
            }
        });
        Report.add(btngenerate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 380, -1, -1));

        jLabel17.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel17.setText("From");
        Report.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, -1, -1));

        jLabel31.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel31.setText("To");
        Report.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, -1, -1));
        Report.add(dtechsr1from, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, 140, -1));
        Report.add(dtechsr1to, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 150, -1));

        jTabbedPane1.addTab("Report", Report);

        AdminPane.getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 700, 470));

        jLabel22.setBackground(new java.awt.Color(204, 0, 0));
        jLabel22.setFont(new java.awt.Font("Microsoft YaHei", 1, 36)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(153, 0, 0));
        jLabel22.setText("Admin");
        AdminPane.getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, -1));

        btnhelp1.setText("Help");
        AdminPane.getContentPane().add(btnhelp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, -1));

        getContentPane().add(AdminPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        EmployeePane.setTitle("Employee Window");
        EmployeePane.setMaximumSize(new java.awt.Dimension(700, 600));
        EmployeePane.setMinimumSize(new java.awt.Dimension(700, 600));
        EmployeePane.setName(""); // NOI18N
        EmployeePane.setPreferredSize(new java.awt.Dimension(700, 600));
        EmployeePane.setVisible(false);
        EmployeePane.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                EmployeePaneInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        EmployeePane.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel20.setText("Firstname");
        EmployeePane.getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));
        EmployeePane.getContentPane().add(txfldfirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 160, -1));

        jLabel21.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel21.setText("Surname");
        EmployeePane.getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        txfldsurname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfldsurnameActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(txfldsurname, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 160, -1));

        jLabel23.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel23.setText("Email");
        EmployeePane.getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));
        EmployeePane.getContentPane().add(txfldemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 200, -1));

        jLabel24.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel24.setText("Age");
        EmployeePane.getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jLabel25.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel25.setText("Gender");
        EmployeePane.getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        tblemployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ProductID", "Productname", "Price", "Quantity", "Total"
            }
        ));
        jScrollPane4.setViewportView(tblemployee);

        EmployeePane.getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 450, 300));

        btnadd.setText("Add");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(btnadd, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, -1, -1));

        btnremove.setText("Remove ");
        btnremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(btnremove, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 390, -1, -1));

        EmployeePane.getContentPane().add(cbxadd, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 430, 170, -1));
        EmployeePane.getContentPane().add(spnadd, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 50, -1));

        btncheckout.setText("Checkout ");
        btncheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncheckoutActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(btncheckout, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, -1, -1));

        EmployeePane.getContentPane().add(cbxremove, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 430, 150, -1));

        btnhelp2.setText("Help");
        EmployeePane.getContentPane().add(btnhelp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, -1, -1));

        jButton1.setText("Get");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 470, -1, -1));

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        EmployeePane.getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 440, -1, -1));

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel10.setText("CustomerID");
        EmployeePane.getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));
        EmployeePane.getContentPane().add(tfldcid, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 110, -1));
        EmployeePane.getContentPane().add(tfldage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 60, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("OrderID");
        EmployeePane.getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, -1, -1));
        EmployeePane.getContentPane().add(tfldgender, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 110, -1));
        EmployeePane.getContentPane().add(tfldoid, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 370, 80, -1));

        Background1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/backgroundemp.png"))); // NOI18N
        EmployeePane.getContentPane().add(Background1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 530));

        getContentPane().add(EmployeePane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        EmployeePane.getAccessibleContext().setAccessibleDescription("");

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfldUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfldUsernameActionPerformed
        
    }//GEN-LAST:event_tfldUsernameActionPerformed

    private void tfldUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfldUsernameMouseClicked
       tfldUsername.setText("");
    }//GEN-LAST:event_tfldUsernameMouseClicked
    
    //Login button
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
       //Variables
        String Username = tfldUsername.getText();
        String Role = cbxRole.getItemAt(cbxRole.getSelectedIndex());
        char[] pass = tfldPassword.getPassword();
        String Password = String.valueOf(pass);
        
        DBM dbm = new DBM(dbconn);
        JOptionPane.showMessageDialog(rootPane,CheckUserNamePassword(dbconn, Username, Password, Role));
        
    }//GEN-LAST:event_btnLoginActionPerformed

    private void tfldPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfldPasswordMouseClicked
        tfldPassword.setText("");
    }//GEN-LAST:event_tfldPasswordMouseClicked
    
    
    //Report - Generate button
    private void btngenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngenerateActionPerformed
        // Variables
        LocalDate from,to;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sfrom = sdf.format(dtechsr1from.getDate());
        from = LocalDate.parse(sfrom, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        

        String sto = sdf.format(dtechsr1to.getDate());
        to = LocalDate.parse(sto, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        
        DBM dbm = new DBM(dbconn);
        String sql = "SELECT ProductName,Quantity,DatePurchased,Total FROM Products, Orders Where Products.ProductID=Orders.ProductID";
        Report(dbconn,tblreport, sql,from,to);
        System.out.println("Report");

    }//GEN-LAST:event_btngenerateActionPerformed
    
   
    
    //Stock - Get button
    private void btngetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngetActionPerformed
        String sql ="SELECT ProductID,ProductName, Category, Stock FROM Products";
        
        DBM dbm = new DBM(dbconn);
        Stock_Get(dbconn,tblstock, sql);
        
    }//GEN-LAST:event_btngetActionPerformed
    
    //Stock - Delete button
    private void btndelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndelete1ActionPerformed
        String items = cbxitems.getItemAt(cbxitems.getSelectedIndex());
        String sql = "Delete * from Products Where ProductName= \""+items+"\"";
        DBM dbm = new DBM(dbconn);
        Stock_Delete(dbconn,sql,items);
    }//GEN-LAST:event_btndelete1ActionPerformed
    //Stock - Update button
    private void btnupdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdate1ActionPerformed
        String items = cbxitems.getItemAt(cbxitems.getSelectedIndex());
        int qnt = Integer.parseInt(spnquantity.getValue().toString());
        String sql = "Update Products Set Stock=Stock+"+qnt+" where ProductName='"+items+"'"; 
        DBM dbm = new DBM(dbconn);
        Stock_Update(sql,dbconn,items,qnt);
    }//GEN-LAST:event_btnupdate1ActionPerformed
    
    //Stock_Add button
    private void btnadd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnadd1ActionPerformed
        String sql = "INSERT INTO Products(ProductID,ProductName,Category,Stock,Price) VALUES(?,?,?,?,?) ";
        DBM dbm = new DBM(dbconn);
        int icount =4;
    
        Stock_Add(dbconn,sql,icount,tblstock);
    }//GEN-LAST:event_btnadd1ActionPerformed

//GEN-FIRST:event_btnmonitorActionPerformed
    //Monitor button     
    private void btnmonitorActionPerformed(java.awt.event.ActionEvent evt)
    {
        LocalDate from,to;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String sfrom = sdf.format(dtechsrfrom.getDate());
        from = LocalDate.parse(sfrom, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        

        String sto = sdf.format(dtechsrto.getDate());
        to = LocalDate.parse(sto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        String sql = "SELECT EmployeeName, DatePurchased, CustomerName, ProductName, Total FROM Customers, Employee, Orders, Products WHERE Products.ProductID=Orders.ProductID AND Employee.EmployeeID=Orders.OrderID AND Customers.CustomerID=Orders.CustomerID";
        DBM dbm = new DBM(dbconn);
        Monitor(dbconn,tblmonitor,sql,from,to);
    }
//GEN-LAST:event_btnmonitorActionPerformed
    //User_Get
    private void btnusergetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnusergetActionPerformed
        String sql ="SELECT Username From Employee";
        cbxusername1.removeAllItems();
        DBM dbm = new DBM(dbconn);
        User_get(dbconn, sql);
    }//GEN-LAST:event_btnusergetActionPerformed
    
    //User_Createnew button
    private void btncreatenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncreatenewActionPerformed
        String sql;  
        String rle = cbxrole1.getItemAt(cbxrole1.getSelectedIndex());
        if(rle.equals("Admin"))
        {
           sql = "INSERT INTO Employee(EmployeeID,IDNumber,EmployeeName,EmployeeSurname,Email,Username,Password	,Age,Role,AdminID) VALUES(?,?,?,?,?,?,?,?,?,?)"; 
        }
        else
        {
            sql = "INSERT INTO Employee(EmployeeID,IDNumber,EmployeeName,EmployeeSurname,Email,Username,Password,Age,Role,AdminID) VALUES(?,?,?,?,?,?,?,?,?)";
        }
        DBM dbm = new DBM(dbconn);
        User_Createnew(dbconn, sql,rle);
    }//GEN-LAST:event_btncreatenewActionPerformed
    
    //User_Update button
    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed
        //User_update
        
        String rle = cbxrole1.getItemAt(cbxrole1.getSelectedIndex());
        DBM dbm = new DBM(dbconn);
        User_Update(dbconn,rle);
    }//GEN-LAST:event_btnupdateActionPerformed

    private void cbxusername1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxusername1ActionPerformed
        
    }//GEN-LAST:event_cbxusername1ActionPerformed

    private void cbxusername1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxusername1ItemStateChanged
        
    }//GEN-LAST:event_cbxusername1ItemStateChanged
    
    //User_Delete button
    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        String usrn = cbxusername1.getItemAt(cbxusername1.getSelectedIndex());
        String sql = "Delete * from Employee where Username= '"+usrn+"'";
        DBM dbm = new DBM(dbconn);
        User_Delete(dbconn, sql);
    }//GEN-LAST:event_btndeleteActionPerformed
    
    //Employee_Add button
    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        
        String items = cbxadd.getItemAt(cbxadd.getSelectedIndex());
        int qnt = Integer.parseInt(spnadd.getValue().toString());
        
        
          String sql = "SELECT ProductID,ProductName,Price,("+ qnt +") AS [Quantity],(("+qnt+")*Price) AS [Total] from  Products Where ProductName='"+items+"'";  
          DBM dbm = new DBM(dbconn);
          EmployeeAdd(dbconn, tblemployee, sql);
     
      
        
    }//GEN-LAST:event_btnaddActionPerformed
    
    
    //Employee_Comboxes
    private void EmployeePaneInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_EmployeePaneInternalFrameActivated
        //Near Add button
        try
        {
            String sql= "SELECT ProductName from Products";
            Statement SQLM = dbconn.createStatement();  
            ResultSet rs = SQLM.executeQuery(sql);
            while (rs.next())
            {
              cbxadd.addItem(rs.getString("ProductName"));
            } 
        }//end of try
        catch(SQLException ex)
        {
            System.err.println(ex);    
        }//end of catch
        
        
        
        
        
    }//GEN-LAST:event_EmployeePaneInternalFrameActivated
    
    //Employee_remove button
    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        
        String val2find = cbxremove.getItemAt(cbxremove.getSelectedIndex());
        deleteRowByValue(tblemployee, val2find, columnIndex);
    }//GEN-LAST:event_btnremoveActionPerformed
    
    //Employee_Checkout button
    private void btncheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncheckoutActionPerformed
        DBM dbm = new DBM(dbconn);
        String sql = "INSERT INTO Customers(CustomerID,CustomerName,CustomerSurname,Email,Age,Gender) VALUES(?,?,?,?,?,?)";
        Checkout(dbconn, tblemployee, sql);
    }//GEN-LAST:event_btncheckoutActionPerformed
    
    //Get Button near the remove button
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //button to populate combobox from a column index
        populateComboBoxFromColumn(tblemployee, cbxremove, columnIndex);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    //Get_User_Details button
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Method to display the userdetails based on the username from the combobox to the textfields
        
        String usrn = cbxusername1.getItemAt(cbxusername1.getSelectedIndex());
        
        String sql ="SELECT * From Employee where Username='"+usrn+"'";
        DBM dbm = new DBM(dbconn);
        User_getin(dbconn, sql);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    //Generate button
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       //Method to generate username based on the Firstname and Surname of the user and two random numbers 
       String Sn = tfldsurname.getText().substring(tfldsurname.getText().length()-3);
       String fn = tfldfirstname.getText().substring(0,3);
       String rdnum = String.valueOf(Math.round((Math.random()*90)+10));
       String username = fn+Sn+rdnum;
       tfldusername1.setText(username);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void BackgroundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackgroundMouseClicked
        
    }//GEN-LAST:event_BackgroundMouseClicked

    private void chkshwpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkshwpassActionPerformed
        // Method to show Password in the login Pane
        if(chkshwpass.isSelected())
        {
            tfldPassword.setEchoChar((char)0);
            System.out.println("Clicked!");
        }
        else
        {
            tfldPassword.setEchoChar('*');
            System.out.println("Dejected!");
        }
       
    }//GEN-LAST:event_chkshwpassActionPerformed
    
    //Employee - Clear button
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultTableModel tblmodel = (DefaultTableModel) tblemployee.getModel();
        tblmodel.setRowCount(0);
        ii=0;
        
    }//GEN-LAST:event_jButton3ActionPerformed
    
    //Usermanagement- Generate Password button
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Method to generate userpassword
        tfldpassword.setText(generatePassword());
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txfldsurnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfldsurnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfldsurnameActionPerformed
    
    
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
       
        
       
        
        
      

       
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame AdminPane;
    private javax.swing.JLabel Background;
    private javax.swing.JLabel Background1;
    private javax.swing.JInternalFrame EmployeePane;
    private javax.swing.JInternalFrame LoginPane;
    private javax.swing.JPanel Monitor1;
    private javax.swing.JPanel Report;
    private javax.swing.JPanel Stock;
    private javax.swing.JPanel Usermanagement;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btnadd1;
    private javax.swing.JButton btncheckout;
    private javax.swing.JButton btncreatenew;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btndelete1;
    private javax.swing.JButton btngenerate;
    public static javax.swing.JButton btnget;
    private javax.swing.JButton btnhelp;
    private javax.swing.JButton btnhelp1;
    private javax.swing.JButton btnhelp2;
    private javax.swing.JButton btnmonitor;
    private javax.swing.JButton btnremove;
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton btnupdate1;
    private javax.swing.JButton btnuserget;
    private javax.swing.JComboBox<String> cbxRole;
    private javax.swing.JComboBox<String> cbxadd;
    public static javax.swing.JComboBox<String> cbxitems;
    private javax.swing.JComboBox<String> cbxremove;
    private javax.swing.JComboBox<String> cbxrole1;
    private javax.swing.JComboBox<String> cbxusername1;
    private javax.swing.JCheckBox chkshwpass;
    private com.toedter.calendar.JDateChooser dtechsr1from;
    private com.toedter.calendar.JDateChooser dtechsr1to;
    private com.toedter.calendar.JDateChooser dtechsrfrom;
    private com.toedter.calendar.JDateChooser dtechsrto;
    public static javax.swing.JLabel gtotal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JSpinner spnadd;
    private javax.swing.JSpinner spnquantity;
    private javax.swing.JTable tblemployee;
    private javax.swing.JTable tblmonitor;
    private javax.swing.JTable tblreport;
    private javax.swing.JTable tblstock;
    private javax.swing.JPasswordField tfldPassword;
    private javax.swing.JTextField tfldUsername;
    private javax.swing.JTextField tfldadid;
    private javax.swing.JTextField tfldage;
    private static javax.swing.JTextField tfldage1;
    private static javax.swing.JTextField tfldcid;
    private javax.swing.JTextField tfldemail;
    private javax.swing.JTextField tfldfirstname;
    private static javax.swing.JTextField tfldgender;
    private javax.swing.JTextField tfldidnumber;
    private static javax.swing.JTextField tfldoid;
    private javax.swing.JTextField tfldpassword;
    private javax.swing.JTextField tfldsurname;
    private javax.swing.JTextField tfldusername1;
    private static javax.swing.JTextField txfldemail;
    private static javax.swing.JTextField txfldfirstname;
    private static javax.swing.JTextField txfldsurname;
    private javax.swing.JTextField txttfldemid;
    // End of variables declaration//GEN-END:variables
}
