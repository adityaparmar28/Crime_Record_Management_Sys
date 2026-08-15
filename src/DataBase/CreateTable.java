package DataBase;

import java.sql.PreparedStatement;

public class CreateTable
{
    Database DB=new Database();

    public void CTofficer_details() throws Exception
    {
        String CToffd = "CREATE TABLE IF NOT EXISTS officer_details" +
                "    (OfficerID VARCHAR(11) PRIMARY KEY," +
                "    Name VARCHAR(40) NOT NULL," +
                "    DOB DATE NOT NULL," +
                "    Age INT(3) NOT NULL," +
                "    Gender VARCHAR(10) NOT NULL," +
                "    Rank VARCHAR(30) NOT NULL," +
                "    Department VARCHAR(30) NOT NULL," +
                "    StationID VARCHAR(30) NOT NULL," +
                "    JoiningDate DATE NOT NULL," +
                "    OfficerStatus VARCHAR(30) DEFAULT 'Active'," +
                "    AssignedCase VARCHAR(11) DEFAULT NULL," +
                "    CaseStatus VARCHAR(15) DEFAULT 'Pending')";

        try (PreparedStatement QCToffd = Database.getConnection().prepareStatement(CToffd))
        {
            QCToffd.executeUpdate();
        }
    }

    public void CTcriminal_details() throws Exception
    {
        String CTcrid = "CREATE TABLE IF NOT EXISTS criminal_details" +
                "    (CriminalID VARCHAR(11) PRIMARY KEY," +
                "    Name VARCHAR(40) NOT NULL," +
                "    Age INT(3) NOT NULL," +
                "    Gender VARCHAR(12) NOT NULL," +
                "    CaseID VARCHAR(11) NOT NULL," +
                "    CrimeType VARCHAR(50) NOT NULL," +
                "    CrimeDate DATE NOT NULL," +
                "    InvestingOfficerID VARCHAR(11) DEFAULT NULL," +
                "    CaseStatus VARCHAR(50) DEFAULT 'Pending'," +
                "    PunishmentType VARCHAR(30)," +
                "    CriminalStatus VARCHAR(30)," +
                "    BailDate DATE," +
                "    ReleaseDate DATE," +
                "    PictureID INT DEFAULT NULL," +
                "    FOREIGN KEY (InvestingOfficerID) REFERENCES officer_details(OfficerID))";

        try (PreparedStatement QCTcrid = Database.getConnection().prepareStatement(CTcrid))
        {
            QCTcrid.executeUpdate();
        }
    }

    public void CTcase_details() throws Exception
    {
        String CTcd="CREATE TABLE IF NOT EXISTS case_details" +
                " (CaseID VARCHAR(11) PRIMARY KEY," +
                " CriminalID VARCHAR(11) DEFAULT NULL," +
                " CaseName VARCHAR(45) NOT NULL," +
                " OfficerID VARCHAR(11) DEFAULT NULL," +
                " CaseType VARCHAR(60) NOT NULL," +
                " CrimeLocation VARCHAR(45) NOT NULL," +
                " CrimeWeapon VARCHAR(45) DEFAULT NULL," +
                " SuspectName VARCHAR(60) NOT NULL," +
                " VictimName VARCHAR(30) NOT NULL," +
                " CrimeDetails TEXT NOT NULL," +
                " CaseStatus VARCHAR(60) DEFAULT 'Pending'," +
                " FOREIGN KEY (CriminalID) REFERENCES criminal_details(CriminalID)," +
                " FOREIGN KEY (OfficerID) REFERENCES officer_details(OfficerID))";

        try (PreparedStatement QCTcd = Database.getConnection().prepareStatement(CTcd))
        {
            QCTcd.executeUpdate();
        }
    }

    public void CTcriminal_pictures() throws Exception
    {
        String CTcp = "CREATE TABLE IF NOT EXISTS Criminal_Pictures" +
                "    (PictureID INT PRIMARY KEY AUTO_INCREMENT," +
                "    CriminalID VARCHAR(11) DEFAULT NULL," +
                "    CaseID VARCHAR(11) DEFAULT NULL," +
                "    CriminalName VARCHAR(30) NOT NULL," +
                "    Picture LONGBLOB," +
                "    FOREIGN KEY (CriminalID) REFERENCES criminal_details(CriminalID)," +
                "    FOREIGN KEY (CaseID) REFERENCES case_details(CaseID))";

        try (PreparedStatement QCTcp = Database.getConnection().prepareStatement(CTcp))
        {
            QCTcp.executeUpdate();
        }
    }

    public void ApplyCircularFKs() throws Exception
    {
        String alterDetails = "ALTER TABLE criminal_details ADD CONSTRAINT FK_CriminalDetails_Picture " +
                "FOREIGN KEY (PictureID) REFERENCES Criminal_Pictures(PictureID)";
        try (PreparedStatement qAlter = Database.getConnection().prepareStatement(alterDetails))
        {
            qAlter.executeUpdate();
        }
        catch (Exception e)
        {
            // Ignore if already added
        }
    }

    public void CTcrime_records() throws Exception
    {
        String CTcr="CREATE TABLE IF NOT EXISTS crime_records" +
                "(CrimeType VARCHAR(30) PRIMARY KEY," +
                "TotalCases INT NOT NULL," +
                "SolvedCases INT NOT NULL," +
                "PendingCases INT NOT NULL," +
                "CrimeRate DOUBLE NOT NULL)";

        try (PreparedStatement QCTcr = Database.getConnection().prepareStatement(CTcr))
        {
            QCTcr.executeUpdate();
        }
    }

    public void CTusers() throws Exception
    {
        String CTUsers ="CREATE TABLE IF NOT EXISTS users  " +
                "    (UsersName VARCHAR(60) NOT NULL," +
                "    EmailID VARCHAR(40) NOT NULL UNIQUE," +
                "    MobileNo VARCHAR(10) NOT NULL," +
                "    DOB DATE NOT NULL," +
                "    UserID VARCHAR(10) PRIMARY KEY," +
                "    Password VARCHAR(12) NOT NULL," +
                "    Role VARCHAR(10) DEFAULT 'Citizen')";

        try (PreparedStatement QCTU = Database.getConnection().prepareStatement(CTUsers))
        {
            QCTU.executeUpdate();
        }
    }
}