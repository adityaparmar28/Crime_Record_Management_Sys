/*
 * Copyright 2026 MR. ADITYA PARMAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

                        "('CYO201','Neha Sharma','1992-11-22',33,'Female','Inspector','Cyber Cell','ST-CY01','2018-03-20','Active','CY1002','Investigating')," +
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
                        "('CY502','Kunal Patel',29,'Male','CY1002','Cyber Crime','2026-02-12','CYO201','Investigating',NULL,'Wanted',NULL,NULL,NULL)," +
                        "('M503','Rohan Singh',42,'Male','M1003','Murder','2025-12-18',NULL,'Pending','Life Imprisonment','Convicted',NULL,NULL,NULL)," +
                        "('K504','Sameer Khan',31,'Male','K1004','Kidnapping','2026-03-20','WCO301','Investigating',NULL,'In Custody',NULL,NULL,NULL)," +
                        "('D505','Arjun Das',38,'Male','D1005','Drug Crime','2026-04-05',NULL,'Pending',NULL,'Under Trial',NULL,NULL,NULL)," +
                        "('CY506','Sneha Reddy',27,'Female','CY1006','Cyber Crime','2026-05-15',NULL,'Solved','3 Years','Convicted',NULL,'2029-05-15',NULL)," +
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
        String[][] criminals = {
            {"R501", "R1001", "Rakesh Yadav", "Robbery"},
            {"CY502", "CY1002", "Kunal Patel", "Cyber Crime"},
            {"M503", "M1003", "Rohan Singh", "Murder"},
            {"K504", "K1004", "Sameer Khan", "Kidnapping"},
            {"D505", "D1005", "Arjun Das", "Drug Crime"},
            {"CY506", "CY1006", "Sneha Reddy", "Cyber Crime"},
            {"K507", "K1007", "Farhan Ali", "Kidnapping"},
            {"T508", "T1008", "Priya Dubey", "Traffic Violation"},
            {"R509", "R1009", "Deepak Chauhan", "Robbery"},
            {"M510", "M1010", "Aisha Begum", "Murder"},
            {"D511", "D1011", "Vijay Patil", "Drug Crime"},
            {"R512", "R1012", "Nisha Kumari", "Robbery"}
        };

        String insertQuery = "INSERT INTO Criminal_Pictures (CriminalID, CaseID, CriminalName, Picture) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(insertQuery))
        {
            for (String[] cri : criminals)
            {
                String id = cri[0];
                String caseId = cri[1];
                String name = cri[2];
                String type = cri[3];

                // Create a simple placeholder image for each criminal
                int width = 200;
                int height = 200;
                java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g2d = img.createGraphics();

                // Set random background color based on name hash
                int hash = name.hashCode();
                int r = Math.abs((hash & 0xFF0000) >> 16) % 200 + 50;
                int g = Math.abs((hash & 0x00FF00) >> 8) % 200 + 50;
                int b = Math.abs(hash & 0x0000FF) % 200 + 50;
                g2d.setColor(new java.awt.Color(r, g, b));
                g2d.fillRect(0, 0, width, height);

                // Draw face/mugshot outline
                g2d.setColor(java.awt.Color.WHITE);
                g2d.fillOval(50, 40, 100, 100); // Head
                g2d.fillOval(30, 130, 140, 100); // Shoulders

                // Text
                g2d.setColor(java.awt.Color.BLACK);
                g2d.drawString(name, 10, 180);
                g2d.drawString("ID: " + id, 10, 195);
                g2d.dispose();

                // Convert to PNG byte stream
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(img, "png", baos);
                byte[] imgBytes = baos.toByteArray();
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(imgBytes);

                ps.setString(1, id);
                ps.setString(2, caseId);
                ps.setString(3, name);
                ps.setBinaryStream(4, bais, imgBytes.length);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void ITCase_Details() throws Exception
    {
        String ITCase =
                "INSERT INTO case_details " +
                        "(CaseID,CriminalID,CaseName,OfficerID,CaseType,CrimeLocation,CrimeWeapon,SuspectName,VictimName,CrimeDetails,CaseStatus) VALUES " +

                        "('R1001','R501','Bank Robbery','CBO101','Robbery','Ahmedabad','Pistol','Rakesh Yadav','Amit Shah','Armed robbery at SBI Bank','Investigating')," +
                        "('CY1002','CY502','Cyber Fraud','CYO201','Cyber Crime','Surat','Laptop','Kunal Patel','Riya Mehta','Online banking phishing fraud','Investigating')," +
                        "('M1003','M503','Murder Case',NULL,'Murder','Vadodara','Knife','Rohan Singh','Vikas Patel','Intentional murder in dispute','Pending')," +
                        "('K1004','K504','Kidnapping','WCO301','Kidnapping','Rajkot','Gun','Sameer Khan','Anjali Shah','Kidnapping minor for ransom','Investigating')," +
                        "('D1005','D505','Drug Smuggling',NULL,'Drug Crime','Bhavnagar',NULL,'Arjun Das','Police Dept','Illegal drug transport across state','Pending')," +
                        "('CY1006','CY506','Identity Theft',NULL,'Cyber Crime','Gandhinagar','Computer','Sneha Reddy','Manish Jain','Stolen identity for bank fraud','Solved')," +
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

    public void ITActivityLog() throws Exception
    {
        String ITAct = "INSERT INTO ActivityLog (Time, UserID, Role, Activity, ActivityEndTime, ActivityDuration) VALUES " +
                "('2026-08-17 10:00:00', 'USR001', 'Citizen', 'Logged in', '2026-08-17 10:05:00', '5 minutes')," +
                "('2026-08-17 10:15:00', 'USR002', 'Citizen', 'Viewed Profile', '2026-08-17 10:17:30', '2 minutes 30 seconds')," +
                "('2026-08-17 11:00:00', 'USR003', 'Officer', 'Viewed Pending Cases List', '2026-08-17 11:02:15', '2 minutes 15 seconds')," +
                "('2026-08-17 12:00:00', 'ADMIN001', 'Admin', 'Updated case details for Case ID: R1001', '2026-08-17 12:05:00', '5 minutes')";

        try (PreparedStatement QITAct = Database.getConnection().prepareStatement(ITAct))
        {
            QITAct.executeUpdate();
        }
    }
}