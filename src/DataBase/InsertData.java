package DataBase;

import java.sql.PreparedStatement;

public class InsertData
{
    Database DB=new Database();

    public void ITOfficer_Details() throws Exception
    {
        String IToffd =
                "INSERT INTO officer_details " +
                        "(OfficerID,Name,DOB,Age,Gender,Rank,Department,StationID,JoiningDate,OfficerStatus,AssignedCase,CaseStatus) VALUES " +

                        "('CBO101','Rajesh Kumar','1988-05-14',37,'Male','Inspector','Crime Branch','ST-CB01','2012-07-15','Active','R1001','Investigating')," +
                        "('CBO102','Priya Mehta','1990-03-22',36,'Female','ACP','Crime Branch','ST-CB02','2014-06-10','On Leave',NULL,'Pending')," +
                        "('CBO103','Amit Patel','1985-09-10',40,'Male','Sub Inspector','Crime Branch','ST-CB03','2010-01-12','Suspended',NULL,'Solved')," +

                        "('CYO201','Neha Sharma','1992-11-22',33,'Female','Inspector','Cyber Cell','ST-CY01','2018-03-20','Active','Y1002','Investigating')," +
                        "('CYO202','Vikram Joshi','1987-07-18',38,'Male','Head Constable','Cyber Cell','ST-CY02','2015-09-05','Active',NULL,'Pending')," +
                        "('CYO203','Kavita Rao','1994-01-30',32,'Female','Sub Inspector','Cyber Cell','ST-CY03','2020-02-14','On Leave',NULL,'Solved')," +

                        "('WCO301','Sunita Verma','1995-02-18',31,'Female','Inspector','Women Cell','ST-WC01','2020-08-05','Active','K1004','Investigating')," +
                        "('WCO302','Meera Iyer','1991-06-25',35,'Female','ACP','Women Cell','ST-WC02','2016-11-12','Active',NULL,'Pending')," +
                        "('WCO303','Arjun Nair','1989-12-08',36,'Male','Head Constable','Women Cell','ST-WC03','2013-04-20','Suspended',NULL,'Solved')," +

                        "('TRO401','Vikram Singh','1990-07-30',35,'Male','Inspector','Traffic','ST-TR01','2015-12-10','Active',NULL,'Pending')," +
                        "('TRO402','Pooja Gupta','1993-04-15',33,'Female','Sub Inspector','Traffic','ST-TR02','2019-01-08','On Leave','T1008','Investigating')," +
                        "('TRO403','Ravi Tiwari','1986-08-20',39,'Male','ACP','Traffic','ST-TR03','2011-05-22','Active',NULL,'Solved')";

        PreparedStatement QIToffd = Database.getConnection().prepareStatement(IToffd);
        QIToffd.executeUpdate();
        QIToffd.close();
    }

    public void ITCriminal_Details() throws Exception
    {
        String ITCriminal =
                "INSERT INTO criminal_details " +
                        "(CriminalID,Name,Age,Gender,CaseID,CrimeType,CrimeDate,InvestingOfficerID,CaseStatus,PunishmentType,CriminalStatus,BailDate,ReleaseDate,PictureID) VALUES " +

                        "('R501','Rakesh Yadav',35,'Male','R1001','Robbery','2026-01-10','CBO101','Investigating',NULL,'In Custody',NULL,NULL,NULL)," +
                        "('Y502','Kunal Patel',29,'Male','Y1002','Cyber Crime','2026-02-12','CYO201','Investigating',NULL,'Wanted',NULL,NULL,NULL)," +
                        "('M503','Rohan Singh',42,'Male','M1003','Murder','2025-12-18',NULL,'Pending','Life Imprisonment','Convicted',NULL,NULL,NULL)," +
                        "('K504','Sameer Khan',31,'Male','K1004','Kidnapping','2026-03-20','WCO301','Investigating',NULL,'In Custody',NULL,NULL,NULL)," +
                        "('D505','Arjun Das',38,'Male','D1005','Drug Crime','2026-04-05',NULL,'Pending',NULL,'Under Trial',NULL,NULL,NULL)," +
                        "('Y506','Sneha Reddy',27,'Female','Y1006','Cyber Crime','2026-05-15',NULL,'Solved','3 Years','Convicted',NULL,'2029-05-15',NULL)," +
                        "('K507','Farhan Ali',33,'Male','K1007','Kidnapping','2026-06-22',NULL,'Solved','7 Years','Convicted',NULL,NULL,NULL)," +
                        "('T508','Priya Dubey',25,'Female','T1008','Traffic Violation','2026-07-01','TRO402','Investigating',NULL,'Under Trial',NULL,NULL,NULL)," +
                        "('R509','Deepak Chauhan',40,'Male','R1009','Robbery','2026-01-28',NULL,'Pending',NULL,'Wanted',NULL,NULL,NULL)," +
                        "('M510','Aisha Begum',30,'Female','M1010','Murder','2025-11-05',NULL,'Pending',NULL,'In Custody',NULL,NULL,NULL)," +
                        "('D511','Vijay Patil',36,'Male','D1011','Drug Crime','2026-03-10',NULL,'Pending',NULL,'Under Trial','2026-09-10',NULL,NULL)," +
                        "('R512','Nisha Kumari',22,'Female','R1012','Robbery','2026-08-01',NULL,'Pending',NULL,'Wanted',NULL,NULL,NULL)";

        PreparedStatement QITCriminal = Database.getConnection().prepareStatement(ITCriminal);
        QITCriminal.executeUpdate();
        QITCriminal.close();
    }

    public void ITCriminal_Pictures() throws Exception
    {
        String ITCriPic =
                "INSERT INTO Criminal_Pictures " +
                        "(CriminalID,CaseID,CriminalName,Picture) VALUES " +

                        "('R501','R1001','Rakesh Yadav',NULL)," +
                        "('Y502','Y1002','Kunal Patel',NULL)," +
                        "('M503','M1003','Rohan Singh',NULL)," +
                        "('K504','K1004','Sameer Khan',NULL)," +
                        "('D505','D1005','Arjun Das',NULL)," +
                        "('Y506','Y1006','Sneha Reddy',NULL)," +
                        "('K507','K1007','Farhan Ali',NULL)," +
                        "('T508','T1008','Priya Dubey',NULL)," +
                        "('R509','R1009','Deepak Chauhan',NULL)," +
                        "('M510','M1010','Aisha Begum',NULL)," +
                        "('D511','D1011','Vijay Patil',NULL)," +
                        "('R512','R1012','Nisha Kumari',NULL)";

        PreparedStatement QITCriPic = Database.getConnection().prepareStatement(ITCriPic);
        QITCriPic.executeUpdate();
        QITCriPic.close();
    }

    public void ITCase_Details() throws Exception
    {
        String ITCase =
                "INSERT INTO case_details " +
                        "(CaseID,CriminalID,CaseName,OfficerID,CaseType,CrimeLocation,CrimeWeapon,SuspectName,VictimName,CrimeDetails,CaseStatus) VALUES " +

                        "('R1001','R501','Bank Robbery','CBO101','Robbery','Ahmedabad','Pistol','Rakesh Yadav','Amit Shah','Armed robbery at SBI Bank','Investigating')," +
                        "('Y1002','Y502','Cyber Fraud','CYO201','Cyber Crime','Surat','Laptop','Kunal Patel','Riya Mehta','Online banking phishing fraud','Investigating')," +
                        "('M1003','M503','Murder Case',NULL,'Murder','Vadodara','Knife','Rohan Singh','Vikas Patel','Intentional murder in dispute','Pending')," +
                        "('K1004','K504','Kidnapping','WCO301','Kidnapping','Rajkot','Gun','Sameer Khan','Anjali Shah','Kidnapping minor for ransom','Investigating')," +
                        "('D1005','D505','Drug Smuggling',NULL,'Drug Crime','Bhavnagar',NULL,'Arjun Das','Police Dept','Illegal drug transport across state','Pending')," +
                        "('Y1006','Y506','Identity Theft',NULL,'Cyber Crime','Gandhinagar','Computer','Sneha Reddy','Manish Jain','Stolen identity for bank fraud','Solved')," +
                        "('K1007','K507','Child Abduction',NULL,'Kidnapping','Junagadh','Chloroform','Farhan Ali','Sita Devi','Abduction of 8-year-old child','Solved')," +
                        "('T1008','T508','Hit and Run','TRO402','Traffic Violation','Ahmedabad','Vehicle','Priya Dubey','Ramesh Solanki','Fatal hit-and-run on highway','Investigating')," +
                        "('R1009','R509','Jewelry Heist',NULL,'Robbery','Surat','Crowbar','Deepak Chauhan','Gold Palace','Break-in at jewelry showroom','Pending')," +
                        "('M1010','M510','Domestic Murder',NULL,'Murder','Vadodara','Poison','Aisha Begum','Zaheer Khan','Suspected poisoning case','Pending')," +
                        "('D1011','D511','Drug Ring',NULL,'Drug Crime','Rajkot',NULL,'Vijay Patil','Narcotics Bureau','Organized drug distribution network','Pending')," +
                        "('R1012','R512','ATM Robbery',NULL,'Robbery','Bhavnagar','Hammer','Nisha Kumari','HDFC Bank','Vandalized ATM and stole cash','Pending')";

        PreparedStatement QITCase = Database.getConnection().prepareStatement(ITCase);
        QITCase.executeUpdate();
        QITCase.close();
    }

    public void ITCrime_Records() throws Exception
    {
        String ITCrime =
                "INSERT INTO crime_records " +
                        "(CrimeType,TotalCases,SolvedCases,PendingCases,CrimeRate) VALUES " +

                        "('Robbery',3,0,3,0.00)," +
                        "('Cyber Crime',2,1,1,50.00)," +
                        "('Murder',2,0,2,0.00)," +
                        "('Kidnapping',2,1,1,50.00)," +
                        "('Drug Crime',2,0,2,0.00)," +
                        "('Traffic Violation',1,0,1,0.00)";

        PreparedStatement QITCrime = Database.getConnection().prepareStatement(ITCrime);
        QITCrime.executeUpdate();
        QITCrime.close();
    }

    public void ITUsers() throws Exception
    {
        String ITUsers="INSERT INTO users " +
                "(UsersName, EmailID, MobileNo, Password, DOB, UserID, Role) " +
                "VALUES " +
                "('Rahul Sharma', 'rahul@gmail.com', '9876543210', 'Rahul@123', '2002-05-15', 'USR001', 'Citizen')," +
                "('Priya Patel', 'priya@gmail.com', '9876543211', 'Priya@123', '2001-09-20', 'USR002', 'Citizen')," +
                "('Amit Kumar', 'amit@gmail.com', '9876543212', 'Amit@123', '1999-12-10', 'USR003', 'Officer')," +
                "('Neha Singh', 'neha@gmail.com', '9876543213', 'Neha@123', '2000-07-25', 'USR004', 'Citizen')," +
                "('Admin User', 'admin@crms.com', '9999999999', 'Admin@123', '1995-01-01', 'ADMIN001', 'Admin')";

        PreparedStatement QITUsers = Database.getConnection().prepareStatement(ITUsers);
        QITUsers.executeUpdate();
        QITUsers.close();
    }
}