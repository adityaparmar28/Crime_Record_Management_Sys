package DataStructure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
            bw.write(" | Criminal ID: " + rs.getInt("Criminal_ID")+" |");
            bw.newLine();
            bw.write(" | Name: " + rs.getString("Name"));
            bw.write(" | Age: " + rs.getInt("Age"));
            bw.write(" | Gender: " + rs.getString("Gender"));
            bw.newLine();
            bw.write(" | Case ID: "+rs.getString("CaseID"));
            bw.write(" | Crime: " + rs.getString("Crime_Type"));
            bw.write(" | Crime Date: " + rs.getString("Crime_Date"));
            bw.newLine();
            bw.write(" | Investigating Officer ID: "+rs.getInt("InvestingOfficerID"));
            bw.write(" | Case Status: "+rs.getString("Case_Status"));
            bw.write(" | Punishment: "+rs.getString("Punishment_Type"));
            bw.newLine();
            bw.write(" | Criminal Status: "+rs.getString("Criminal_Status"));
            bw.write(" | Bail Date: "+rs.getDate("Bail_Date"));
            bw.write(" | Release Date: "+rs.getDate("Release_Date"));
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
            bw.write(" | Officer ID: " + rs.getInt("Officer_ID")+" |");
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
            bw.write(" | Assigned Case: " + rs.getString("Assigned_Case"));
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
            bw.write(" | Criminal ID: " + rs.getInt("CriminalID")+" |");
            bw.newLine();
            bw.write(" | Investigating Officer ID: " + rs.getInt("OfficerID")+" |");
            bw.newLine();
            bw.write(" | Case: "+rs.getString("CaseName")+" |");
            bw.newLine();
            bw.write(" | Case Type: " + rs.getString("Case_Type"));
            bw.write(" | Crime Location: "+rs.getString("Crime_Location"));
            bw.write(" | Crime Weapon: "+rs.getString("Crime_Weapon"));
            bw.newLine();
            bw.write(" | Criminal or Suspect Name: "+rs.getString("SuspectName"));
            bw.write(" | Victim Name: "+rs.getString("Victim_Name"));
            //bw.write(" | Crime Date: " + rs.getString("Crime_Date"));
            bw.newLine();
            bw.write(" | Case Description: " + rs.getString("Crime_Description"));
            bw.write(" | Case Status: " + rs.getString("Case_Status"));
            //bw.write(" | Punishment Type: " + rs.getString("Punishment_Type"));
        }
        bw.newLine();
        bw.newLine();
        bw.write("-------------------------| END OF CASE DATA |-------------------------");
        bw.close();
        fw.close();
    }
}