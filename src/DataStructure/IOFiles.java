package DataStructure;

import DataBase.Database;
import Profile.Login_SignUpPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IOFiles
{
    public void FetchCriminalData(ResultSet rs) throws Exception
    {
        File f=new File("CriminalData.txt");
        f.createNewFile();

        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write("-------------------------| CRIMINAL DATA |-------------------------");

        while (rs.next())
        {
            bw.newLine();
            bw.newLine();
            bw.write("----------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.newLine();
            bw.write(" | Criminal ID: " + rs.getString("CriminalID")+" |");
            bw.newLine();
            bw.write(" | Name: " + rs.getString("Name"));
            bw.write(" | Age: " + rs.getInt("Age"));
            bw.write(" | Gender: " + rs.getString("Gender"));
            bw.newLine();
            bw.write(" | Case ID: "+rs.getString("CaseID"));
            bw.write(" | Crime: " + rs.getString("CrimeType"));
            bw.write(" | Crime Date: " + rs.getString("CrimeDate"));
            bw.newLine();
            bw.write(" | Investigating Officer ID: "+rs.getString("InvestingOfficerID"));
            bw.write(" | Case Status: "+rs.getString("CaseStatus"));
            bw.write(" | Punishment: "+rs.getString("PunishmentType"));
            bw.newLine();
            bw.write(" | Criminal Status: "+rs.getString("CriminalStatus"));
            bw.write(" | Bail Date: "+rs.getDate("BailDate"));
            bw.write(" | Release Date: "+rs.getDate("ReleaseDate"));
        }
        bw.newLine();
        bw.newLine();
        bw.write("-------------------------| END OF CRIMINAL DATA |-------------------------");
        bw.close();
        fw.close();
    }

    public void FetchOfficerData(ResultSet rs) throws Exception
    {
        File f=new File("OfficerData.txt");
        f.createNewFile();

        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);

        bw.write("-------------------------| OFFICER DATA |-------------------------");

        while (rs.next())
        {
            bw.newLine();
            bw.newLine();
            bw.write("----------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.newLine();
            bw.write(" | Officer ID: " + rs.getString("OfficerID")+" |");
            bw.newLine();
            bw.write(" | Name: " + rs.getString("Name"));
            bw.write(" | Date Of Birth: " + rs.getString("DOB"));
            bw.write(" | Age: " + rs.getInt("Age"));
            bw.write(" | Gender: " + rs.getString("Gender"));
            bw.newLine();
            bw.write(" | Rank: " + rs.getString("Rank"));
            bw.write(" | Department: " + rs.getString("Department"));
            bw.write(" | Station ID: " + rs.getString("StationID"));
            bw.newLine();
            bw.write(" | Joining Date: " + rs.getDate("JoiningDate"));
            bw.write(" | Officer Status: " + rs.getString("OfficerStatus"));
            bw.write(" | Assigned Case: " + rs.getString("AssignedCase"));
            bw.write(" | Investigation Status: " + rs.getString("CaseStatus"));
        }
        bw.newLine();
        bw.newLine();
        bw.write("-------------------------| END OF OFFICER DATA |-------------------------");
        bw.close();
        fw.close();

    }

    public void FetchAllCases(ResultSet rs) throws Exception
    {
        File f=new File("CasesData.txt");
        f.createNewFile();

        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write("-------------------------| CASES DATA |-------------------------");

        while (rs.next())
        {
            bw.newLine();
            bw.newLine();
            bw.write("----------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.newLine();
            bw.write(" | Case ID: " + rs.getString("CaseID")+" |");
            bw.newLine();
            bw.write(" | Criminal ID: " + rs.getString("CriminalID")+" |");
            bw.newLine();
            bw.write(" | Investigating Officer ID: " + rs.getString("OfficerID")+" |");
            bw.newLine();
            bw.write(" | Case: "+rs.getString("CaseName")+" |");
            bw.newLine();
            bw.write(" | Case Type: " + rs.getString("CaseType"));
            bw.write(" | Crime Location: "+rs.getString("CrimeLocation"));
            bw.write(" | Crime Weapon: "+rs.getString("CrimeWeapon"));
            bw.newLine();
            bw.write(" | Criminal or Suspect Name: "+rs.getString("SuspectName"));
            bw.write(" | Victim Name: "+rs.getString("VictimName"));
            bw.newLine();
            bw.write(" | Case Description: " + rs.getString("CrimeDetails"));
            bw.write(" | Case Status: " + rs.getString("CaseStatus"));
        }
        bw.newLine();
        bw.newLine();
        bw.write("-------------------------| END OF CASE DATA |-------------------------");
        bw.close();
        fw.close();
    }

    public void FetchFIR(String CaseID,String Date) throws Exception
    {
        String FIR="Select * from case_details where CaseID=?";
        PreparedStatement QFIR=Database.getConnection().prepareStatement(FIR);
        QFIR.setString(1,CaseID);
        ResultSet FetchRs= QFIR.executeQuery();
        FetchRs.next();

        String UserD="SELECT UsersName,MobileNo,EmailID from users where UserID=?";
        PreparedStatement QUDetails=Database.getConnection().prepareStatement(UserD);
        QUDetails.setString(1, Login_SignUpPage.LoggedUserID);
        ResultSet UserRs= QUDetails.executeQuery();
        UserRs.next(); // Advance cursor to first row of results

        File f=new File("FIR_Report_"+Date+"_"+CaseID+".txt");
        f.createNewFile();

        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);

        bw.write("+----------------------------------| FILED FIR REPORT |------------------------------------------+");
        bw.newLine();
        bw.newLine();
        bw.write("                                                                        | Crime Date: "+java.sql.Date.valueOf(Date)+" |");
        bw.newLine();
        bw.newLine();
        bw.write(" | Case Name        : "+FetchRs.getString("CaseName")+" |");
        bw.newLine();
        bw.write(" | Case Type        : "+FetchRs.getString("CaseType")+" |");
        bw.newLine();
        bw.write(" | Assigned Case ID : "+FetchRs.getString("CaseID")+" |");
        bw.newLine();
        bw.newLine();
        bw.write(" | Victim Name              : "+FetchRs.getString("VictimName")+" |");
        bw.newLine();
        bw.write(" | Criminal ID              : "+FetchRs.getString("CriminalID")+" |");
        bw.newLine();
        bw.write(" | Criminal or Suspect Name : "+FetchRs.getString("SuspectName")+" |");
        bw.newLine();
        bw.newLine();
        bw.write(" | Crime Location : "+FetchRs.getString("CrimeLocation")+" |");
        bw.newLine();
        bw.write(" | Crime Weapon   : "+FetchRs.getString("CrimeWeapon")+" |");
        bw.newLine();
        bw.newLine();
        bw.write(" | Crime Description |");
        bw.newLine();
        bw.newLine();
        bw.write(" |                    >>> "+FetchRs.getNString("CrimeDetails")+" <<<");
        bw.newLine();
        bw.newLine();
        bw.write("                                                                           | Officer ID: "+FetchRs.getString("OfficerID")+" |");
        bw.newLine();
        bw.newLine();
        bw.write("                                                        | FIR Filled By....");
        bw.newLine();
        bw.newLine();
        bw.write("                                                        >>> | Name          : "+UserRs.getString("UsersName")+" |");
        bw.newLine();
        bw.write("                                                            | Mobile Number : "+UserRs.getString("MobileNo")+" |");
        bw.newLine();
        bw.newLine();
        bw.write("+------------------------------------------------------------------------------------------------+");
        bw.newLine();
        bw.close();
        fw.close();
        FetchRs.close();
        UserRs.close();
    }
}