package DataBase;

import java.sql.*;
import java.util.Scanner;

public class DataBase extends Thread
{
    private static final String URL = "jdbc:mysql://localhost:3306/c115";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection con = null;
    private static boolean driverLoaded = false;
    public static boolean conStatus=testConnection();

    Scanner sc=new Scanner(System.in);

    // Load JDBC Driver once
    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("[WARNING] MySQL JDBC Driver not found in classpath. SQL features will be disabled.");
        }
    }

    public static Connection getConnection() throws SQLException
    {
        if (!driverLoaded)
        {
            throw new SQLException("JDBC Driver was not loaded.");
        }

        // Return active connection or create a new one if closed
        if (con == null || con.isClosed())
        {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return con;
    }

    public static boolean testConnection()
    {
        try
        {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public static void closeConnection()
    {
        if (con != null)
        {
            try
            {
                if (!con.isClosed())
                {
                    con.close();
                }
            }
            catch (SQLException e)
            {
                System.err.println("[ERROR] Failed to close database connection: " + e.getMessage());
            }
        }
    }

    void DBSync()
    {
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|              DATABASE & SYSTEM CONFIGURATION MANAGEMENT              |");
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println();

        if(testConnection())
        {
            System.out.println("Status: ONLINE MODE (Connected to MySQL Database)");
            System.out.println("Connection String: jdbc:mysql://localhost:3306/c103");
            System.out.println();
        }
        else
        {
            System.out.println("Status: OFFLINE MODE (In-memory simulation active)");
            System.out.println("Crime Record Management System could not connect to XAMPP MySQL Database at start...");
            System.out.println("Make sure XAMPP Control Panel is running and MySQL service is started....");
            System.out.println();

            System.out.print("Do you want to test and establish MySQL connection now???");
            char conAns=sc.next().charAt(0);

            if (conAns=='y' || conAns=='Y')
            {
                System.out.println("Attempting to connect to jdbc:mysql://localhost:3306/c103...");
                if (testConnection())
                {
                    System.out.println("Database connection established! System synchronized to live database.");
                    System.out.println("System reconnected and synchronized with live MySQL DB");
                }
                else
                {
                    System.out.println("Reconnection failed. Staying in Offline Mode.");
                }
            }
        }
    }

    public DataBase()
    {
        conStatus=testConnection();
    }

    @Override
    public void run()
    {
        while(true)
        {
            Status();
        }
    }


    void Status()
    {
        do {
            try {
                Thread.sleep(60000); // Check every 60 seconds
                if (testConnection()) {
                    if (!conStatus) {
                        System.out.println("[INFO] Database connection restored.");
                        conStatus = true;
                    }
                } else {
                    if (conStatus) {
                        System.out.println("[WARNING] Database connection lost.");
                        conStatus = false;
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("[ERROR] DBSync thread interrupted: " + e.getMessage());
            }
        } while (true);
    }


    void DefaultDataBase() throws Exception
    {
        if (!tableExists("case_details"))
            CTcase_details();

        if (isTableEmpty("case_details"))
            ITCase_Details();


        if (!tableExists("crime_records"))
            CTcrime_records();

        if (isTableEmpty("crime_records"))
            ITCrime_Records();


        if (!tableExists("criminal_details"))
            CTcriminal_details();

        if (isTableEmpty("criminal_details"))
            ITCriminal_Details();


        if (!tableExists("officer_details"))
            CTofficer_details();

        if (isTableEmpty("officer_details"))
            ITOfficer_Details();


        if (!tableExists("users"))
            CTusers();

        if (isTableEmpty("users"))
            ITUsers();
    }

    public boolean tableExists(String tableName) throws SQLException
    {
        Connection con = getConnection();

        String TableExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";

        PreparedStatement QTE = con.prepareStatement(TableExists);
        QTE.setString(1, tableName);

        ResultSet existsT_rs = QTE.executeQuery();

        existsT_rs.next();
        boolean exists = existsT_rs.getInt(1) > 0;

        existsT_rs.close();
        QTE.close();
        return exists;
    }

    public boolean isTableEmpty(String tableName) throws Exception
    {
        Connection con = getConnection();

        String isTValues = "SELECT COUNT(*) FROM " + tableName;
        PreparedStatement QITV = con.prepareStatement(isTValues);
        ResultSet rs = QITV.executeQuery();

        rs.next();
        boolean empty = rs.getInt(1) == 0;

        rs.close();
        QITV.close();
        return empty;
    }

    void CTcase_details() throws Exception
    {
        String CTcd="CREATE TABLE  if not exists case_details" +
                " (CaseID INT PRIMARY KEY, CriminalID INT NOT NULL," +
                " CaseName VARCHAR(45) NOT NULL,OfficerID INT NOT NULL," +
                " Case_Type VARCHAR(60) NOT NULL," +
                " Crime_Location VARCHAR(45) NOT NULL," +
                " Crime_Weapon VARCHAR(45) NOT NULL, SuspectName VARCHAR(60) NOT NULL," +
                " Victim_Name VARCHAR(30) NOT NULL, Crime_Description TEXT NOT NULL, " +
                "Case_Status VARCHAR(60) NOT NULL)";

        PreparedStatement QCTcd= con.prepareStatement(CTcd);
        int run=QCTcd.executeUpdate();

        if(run>0)
        {
            QCTcd.close();
            ITCase_Details();
        }
    }

    void CTcrime_records() throws Exception
    {
        String CTcr="CREATE TABLE  if not exists crime_records" +
                "(Crime_Type VARCHAR(30) NOT NULL," +
                "Total_Cases INT NOT NULL," +
                "Solved_Cases INT NOT NULL," +
                "Pending_Cases INT NOT NULL," +
                "Crime_Rate DOUBLE NOT NULL)";

        PreparedStatement QCTcr=con.prepareStatement(CTcr);
        int run=QCTcr.executeUpdate();

        if (run>0)
        {
            QCTcr.close();
            ITCrime_Records();
        }
    }

    void CTcriminal_details() throws Exception
    {
        String CTcrid = "CREATE TABLE  if not exists criminal_details" +
                "(Criminal_ID INT PRIMARY KEY,Name VARCHAR(40) NOT NULL,Age YEAR NOT NULL,Gender VARCHAR(12) NOT NULL," +
                "CaseID INT NOT NULL,Crime_Type VARCHAR(50) NOT NULL,Crime_Date DATE NOT NULL," +
                "InvestingOfficerID INT NOT NULL,Case_Status VARCHAR(50) NOT NULL,Punishment_Type VARCHAR(30)," +
                "Criminal_Status VARCHAR(30),Bail_Date DATE,Release_Date DATE)";

        PreparedStatement QCTcrid = con.prepareStatement(CTcrid);
        int run = QCTcrid.executeUpdate();

        if (run > 0)
        {
            QCTcrid.close();
            ITCriminal_Details();
        }
    }

    void CTofficer_details() throws Exception
    {
        String CToffd="CREATE TABLE  if not exists officer_details" +
                "    (Officer_ID INT PRIMARY KEY," +
                "    Name varchar(40) NOT NULL," +
                "    DOB DATE NOT NULL," +
                "    Age INT NOT NULL," +
                "    Gender VARCHAR(10) NOT NULL," +
                "    Rank VARCHAR(30) NOT NULL," +
                "    Department VARCHAR(30) NOT NULL," +
                "    StationID VARCHAR(30) NOT NULL," +
                "    JoiningDate DATE NOT NULL," +
                "    OfficerStatus VARCHAR(30) NOT NULL," +
                "    Assigned_Case INT NOT NULL," +
                "    CaseStatus VARCHAR(15) NOT NULL)";

        PreparedStatement QCToffd=con.prepareStatement(CToffd);
        int run=QCToffd.executeUpdate();

        if(run>0)
        {
            QCToffd.close();
            ITOfficer_Details();
        }
    }

    void CTusers() throws Exception
    {
        String CTUsers ="CREATE TABLE if not exists users  " +
                "    (Users_name VARCHAR(60) NOT NULL," +
                "    EmailId VARCHAR(40) NOT NULL UNIQUE," +
                "    MobileNo VARCHAR(10) NOT NULL," +
                "    Password VARCHAR(12) NOT NULL," +
                "    DOB DATE NOT NULL," +
                "    UserId VARCHAR(10) PRIMARY KEY," +
                "    Role VARCHAR(10) DEFAULT 'Citizen')";

        PreparedStatement QCTU=con.prepareStatement(CTUsers);
        int run=QCTU.executeUpdate();

        if(run>0)
        {
            QCTU.close();
            //ITUsers();
        }
    }

    void ITOfficer_Details() throws Exception
    {
        String IToffd =
                "INSERT INTO officer_details " +
                        "(Officer_ID,Name,DOB,Age,Gender,Rank,Department,StationID,JoiningDate,OfficerStatus,Assigned_Case,CaseStatus) VALUES " +

                        "(101,'Rajesh Kumar','1988-05-14',37,'Male','Inspector','Crime Branch','ST101','2012-07-15','Active',1001,'Investigating')," +

                        "(102,'Priya Sharma','1992-11-22',33,'Female','Sub Inspector','Cyber Cell','ST102','2018-03-20','Active',1002,'Pending')," +

                        "(103,'Amit Patel','1985-09-10',40,'Male','ACP','Crime Branch','ST103','2010-01-12','Active',1003,'Solved')," +

                        "(104,'Neha Verma','1995-02-18',30,'Female','Inspector','Women Cell','ST104','2020-08-05','Active',1004,'Investigating')," +

                        "(105,'Vikram Singh','1990-07-30',35,'Male','Head Constable','Traffic','ST105','2015-12-10','On Leave',1005,'Pending')";

        PreparedStatement QIToffd = con.prepareStatement(IToffd);
        QIToffd.executeUpdate();
        QIToffd.close();
    }

    void ITCase_Details() throws Exception
    {
        String ITCase =
                "INSERT INTO case_details " +
                        "(CaseID,CriminalID,CaseName,OfficerID,Case_Type,Crime_Location,Crime_Weapon,SuspectName,Victim_Name,Crime_Description,Case_Status) VALUES " +

                        "(1001,201,'Bank Robbery',101,'Robbery','Ahmedabad','Pistol','Rakesh Yadav','Amit Shah','Robbery at SBI Bank','Investigating')," +

                        "(1002,202,'Cyber Fraud',102,'Cyber Crime','Surat','Laptop','Kunal Patel','Riya Mehta','Online Banking Fraud','Pending')," +

                        "(1003,203,'Murder Case',103,'Murder','Vadodara','Knife','Rohan Singh','Vikas Patel','Intentional Murder','Solved')," +

                        "(1004,204,'Kidnapping',104,'Kidnapping','Rajkot','Gun','Sameer Khan','Anjali Shah','Kidnapping for Ransom','Investigating')," +

                        "(1005,205,'Drug Smuggling',105,'Drug Crime','Bhavnagar','None','Arjun Das','Police Department','Illegal Drug Transport','Pending')";

        PreparedStatement QITCase = con.prepareStatement(ITCase);
        QITCase.executeUpdate();
        QITCase.close();
    }

    void ITCriminal_Details() throws Exception
    {
        String ITCriminal =
                "INSERT INTO criminal_details " +

                        "(Criminal_ID,Name,Age,Gender,CaseID,Crime_Type,Crime_Date,InvestingOfficerID,Case_Status,Punishment_Type,Criminal_Status,Bail_Date,Release_Date) VALUES " +
                        "(201,'Rakesh Yadav',35,'Male',1001,'Robbery','2026-01-10',101,'Investigating',NULL,'In Custody',NULL,NULL)," +
                        "(202,'Kunal Patel',29,'Male',1002,'Cyber Crime','2026-02-12',102,'Pending',NULL,'Wanted',NULL,NULL)," +
                        "(203,'Rohan Singh',42,'Male',1003,'Murder','2025-12-18',103,'Solved','Life Imprisonment','Convicted',NULL,NULL)," +
                        "(204,'Sameer Khan',31,'Male',1004,'Kidnapping','2026-03-20',104,'Investigating',NULL,'In Custody',NULL,NULL)," +
                        "(205,'Arjun Das',38,'Male',1005,'Drug Crime','2026-04-05',105,'Pending',NULL,'Under Trial',NULL,NULL)";

        PreparedStatement QITCriminal = con.prepareStatement(ITCriminal);
        QITCriminal.executeUpdate();
        QITCriminal.close();
    }

    void ITCrime_Records() throws Exception
    {
        String ITCrime =
                "INSERT INTO crime_records " +
                        "(Crime_Type,Total_Cases,Solved_Cases,Pending_Cases,Crime_Rate) VALUES " +

                        "('Robbery',15,10,5,66.67)," +

                        "('Cyber Crime',20,12,8,60.00)," +

                        "('Murder',8,6,2,75.00)," +

                        "('Kidnapping',5,3,2,60.00)," +

                        "('Drug Crime',12,7,5,58.33)";

        PreparedStatement QITCrime = con.prepareStatement(ITCrime);
        QITCrime.executeUpdate();
        QITCrime.close();
    }

    void ITUsers() throws Exception
    {
        String ITUsers="INSERT INTO users " +
                "(Users_name, EmailId, MobileNo, Password, DOB, UserId, Role)\n" +
                "VALUES\n" +
                "('Rahul Sharma', 'rahul@gmail.com', '9876543210', 'Rahul@123', '2002-05-15', 'USR001', 'Citizen'),\n" +
                "\n" +
                "('Priya Patel', 'priya@gmail.com', '9876543211', 'Priya@123', '2001-09-20', 'USR002', 'Citizen'),\n" +
                "\n" +
                "('Amit Kumar', 'amit@gmail.com', '9876543212', 'Amit@123', '1999-12-10', 'USR003', 'Officer'),\n" +
                "\n" +
                "('Neha Singh', 'neha@gmail.com', '9876543213', 'Neha@123', '2000-07-25', 'USR004', 'Citizen'),\n" +
                "\n" +
                "('Admin User', 'admin@crms.com', '9999999999', 'Admin@123', '1995-01-01', 'ADMIN001', 'Admin')";


        PreparedStatement QITUsers = con.prepareStatement(ITUsers);
        QITUsers.executeUpdate();
        QITUsers.close();
    }
}