package Quries;

import DataBase.DataBase;
import DataStructure.DataStructure;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Queue;
import java.util.Scanner;

public class IAQueries
{
    Scanner sc=new Scanner(System.in);
    DataBase db=new DataBase();
    OODataQueries OODQ=new OODataQueries();
    CrimeMngrQueries CMQ=new CrimeMngrQueries();
    Queue<String> PCS=DataStructure.PendingCase;

    public void PendingCases() throws Exception
    {
        PCS.clear();
        String PenCases="select * from case_details where Case_Status is null or lower(Case_Status)=?";
        PreparedStatement QPCase=db.getConnection().prepareStatement(PenCases);
        QPCase.setString(1,"pending");
        ResultSet PCase_rs=QPCase.executeQuery();

        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                                  PENDING CASES LIST                                                |");
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");

        boolean found = false;
        while(PCase_rs.next())
        {
            found = true;
            int caseId = PCase_rs.getInt("CaseID");
            String caseName = PCase_rs.getString("CaseName");
            String caseType = PCase_rs.getString("Case_Type");
            String location = PCase_rs.getString("Crime_Location");
            String weapon = PCase_rs.getString("Crime_Weapon");
            String suspect = PCase_rs.getString("SuspectName");
            String victim = PCase_rs.getString("Victim_Name");
            String desc = PCase_rs.getString("Crime_Description");
            String status = PCase_rs.getString("Case_Status");

            String caseDetails = String.format("Case ID: %d | Name: %s | Type: %s | Location: %s | Suspect: %s | Victim: %s | Status: %s",
                    caseId, (caseName != null ? caseName : "N/A"), (caseType != null ? caseType : "N/A"),
                    (location != null ? location : "N/A"), (suspect != null ? suspect : "N/A"),
                    (victim != null ? victim : "N/A"), (status != null ? status : "Pending"));

            PCS.add(caseDetails);

            System.out.println(" | Case ID      : " + caseId);
            System.out.println(" | Case Name    : " + (caseName != null ? caseName : "N/A"));
            System.out.println(" | Case Type    : " + (caseType != null ? caseType : "N/A"));
            System.out.println(" | Location     : " + (location != null ? location : "N/A"));
            System.out.println(" | Weapon       : " + (weapon != null ? weapon : "N/A"));
            System.out.println(" | Suspect Name : " + (suspect != null ? suspect : "N/A"));
            System.out.println(" | Victim Name  : " + (victim != null ? victim : "N/A"));
            System.out.println(" | Description  : " + (desc != null ? desc : "N/A"));
            System.out.println(" | Status       : " + (status != null ? status : "Pending"));
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
        }

        if (!found)
        {
            System.out.println("[INFO] No pending cases found.");
        }
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");

        PCase_rs.close();
        QPCase.close();

        //>>>Assign pending cases....
        CMQ.AssignPCase();
    }

    public void UpdateInvesting() throws Exception
    {
        System.out.print("Enter Invasting Case ID: ");
        int caseID = sc.nextInt();

        if(!isCaseFound(caseID))
        {
            System.out.println("[ERROR] Case ID not found in database....");
            return;
        }

        System.out.println("Which data do you want to Update in Investigation???");
        System.out.println("[INFO] Case_Status,SuspectName,CaseName,Crime_Weapon are Valid....");
        String UpColummName=sc.next();

        String TableName="case_details";

        System.out.print("Enter Updating "+UpColummName+": ");

        Object UpdatingValue=OODQ.SQLDType2JDType(UpColummName,TableName);

        String UpdateIn="update "+TableName+" set "+UpColummName+"=? where CaseID=?";
        PreparedStatement QUIn=db.getConnection().prepareStatement(UpdateIn);
        QUIn.setObject(1, UpdatingValue);
        QUIn.setInt(2,caseID);
        int run = QUIn.executeUpdate();
        if (run > 0)
        {
            System.out.println("[UPDATED] Investigation details updated successfully...");
        }
        else
        {
            System.err.println("[FAILED] Investigation details update failed...");
        }
        QUIn.close();
    }

    boolean isCaseFound(int cid) throws Exception
    {
        String FCID="select count(*) from case_details where CaseID=?";
        PreparedStatement QFCID=db.getConnection().prepareStatement(FCID);
        QFCID.setInt(1, cid);
        ResultSet FCIDrs = QFCID.executeQuery();
        if (FCIDrs.next())
        {
            boolean res = FCIDrs.getInt(1) > 0;
            FCIDrs.close();
            QFCID.close();
            return res;
        }
        QFCID.close();
        return false;
    }
}