package Quries;

import DataBase.DataBase;
import DataStructure.DataStructure;
import DataStructure.IOFiles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Queue;
import java.util.Scanner;

public class CrimeMngrQueries
{
    Scanner sc=new Scanner(System.in);
    DataBase db=new DataBase();
    OODataQueries OODQ=new OODataQueries();
    Queue<String> PCS=DataStructure.PendingCase;
    IOFiles IOF=new IOFiles();

    public void FileFIRQ() throws Exception
    {
        System.out.println("-----| FILE FIR REPORT |-----");
        System.out.println("Enter the following details to file a FIR report....");
        System.out.println("---------------------------------------------------------");

        System.out.print("Enter Case ID: ");
        int CaseID=sc.nextInt();

        System.out.print("Enter Case Type: ");
        String CType=sc.next();

        System.out.print("Enter Crime Location: ");
        String CLocation=sc.next();

        System.out.print("Is there Involved Weapon at CrimeScene???");
        String CWIn=sc.next();
        String CWeapon=null;

        if(CWIn.equalsIgnoreCase("Y"))
        {
            System.out.print("Enter Crime Weapon: ");
            CWeapon=sc.next();
        }

        System.out.print("Enter Suspect Name: ");
        String SuspectName=sc.next();

        System.out.print("Enter Victim Name: ");
        String VictimName=sc.next();

        System.out.print("Enter Crime Description: ");
        String CDesc=sc.next();

        System.out.print("Enter Case Name: ");
        String CName=sc.next();

        String FileFIR="insert into case_details(CaseID,Case_Type,Crime_Location,Crime_Weapon,SuspectName,Victim_Name,Crime_Description,CaseName) values(?,?,?,?,?,?,?,?)";
        PreparedStatement QFIR=db.getConnection().prepareStatement(FileFIR);
        QFIR.setInt(1,CaseID);
        QFIR.setString(2,CType);
        QFIR.setString(3,CLocation);
        QFIR.setString(4,CWeapon);
        QFIR.setString(5,SuspectName);
        QFIR.setString(6,VictimName);
        QFIR.setString(7,CDesc);
        QFIR.setString(8,CName);

        int update=QFIR.executeUpdate();

        if(update>0)
        {
            System.out.println("[UPDATED] FIR report filed successfully...");
            QFIR.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to file FIR report. Please try again...");
            QFIR.close();
        }
    }

    public void SearchCRQ() throws Exception
    {
        System.out.println("Do you Know Anything Perticular Details about Case???(yes /no)");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            KSCaseRec();
        }
        else if(ans=='N'||ans=='n')
        {
            UnkSCaseRec();
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
        }
    }

    void KSCaseRec() throws Exception
    {
        char ans;
        System.out.println("Which data do you know about Case???");
        System.out.println("[INFO] CaseName,Crime_Location,Crime_Weapon,SuspectName,Victim_Name are Valid....");
        String ColummName=sc.next();

        //>>>if UpData column name found in case_details table then process ahead otherwise show error message that column not found in database....

        String TableName="case_details";

        System.out.print("Enter "+ColummName+" Details: ");

        //input variable static public banana padega..!!
        Object Kdetails=OODQ.SQLDType2JDType(ColummName,TableName);

        String SearchCaseRec="select count(*) as TResults from case_details where "+ColummName+"is like ?";
        PreparedStatement QSOR=db.getConnection().prepareStatement(SearchCaseRec);
        QSOR.setObject(1,Kdetails);
        ResultSet CaseD_rs=QSOR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(CaseD_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CaseD_rs.getInt("TResults |-----"));
                CountResult=CaseD_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Case Record Found in Database....");
                    QSOR.close();
                    return;
                }
                else
                {
                    System.out.println("Would you like to see AlL Results???");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                        QSOR.close();
                    }
                    else if(ans=='N'||ans=='n')
                    {
                        System.out.println("[INFO] Returning to Main Menu....");
                        QSOR.close();
                        return;
                    }
                    else
                    {
                        System.err.println("[INVALID] Give Answer only by Yes or No...");
                        QSOR.close();
                        return;
                    }
                }

                DTR=false;
            }

            //show all results as log type normal....
            System.out.println
                    (
                            String.format
                                    (
                                            "| Case ID: %-5d | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                            CaseD_rs.getInt("Case_ID"),
                                            CaseD_rs.getString("Case_Type"),
                                            CaseD_rs.getString("Case_Status"),
                                            CaseD_rs.getString("Description")
                                    )
                    );

            //>>>If Multiple result found then ask for which data do you want....
            CaseDetails();
        }
    }

    void CaseDetails() throws Exception
    {
        System.out.println("Enter Case ID to see more details about that Case Record: ");
        int caseID=sc.nextInt();

        //>>>other Query....
        String PerSCaseRec="select * from case_details where CaseID=?";
        PreparedStatement QPSR=db.getConnection().prepareStatement(PerSCaseRec);
        QPSR.setInt(1,caseID);
        ResultSet PerCaseD=QPSR.executeQuery();
        QPSR.close();
        PerCaseD.next();

        System.out.println("-----------------------| Case Record Details |--------------------------");
        System.out.println("Case ID: "+PerCaseD.getInt("CaseID"));
        System.out.println("Criminal ID: "+PerCaseD.getInt("CriminalID"));
        System.out.println("Case Name: "+PerCaseD.getString("CaseName"));
        System.out.println("Officer ID: "+PerCaseD.getInt("OfficerID"));
        System.out.println("Case Type: "+PerCaseD.getString("Case_Type"));
        System.out.println("Crime Location: "+PerCaseD.getString("Crime_Location"));
        System.out.println("Crime Weapon: "+PerCaseD.getString("Crime_Weapon"));
        System.out.println("Suspect Name: "+PerCaseD.getString("SuspectName"));
        System.out.println("Victim Name: "+PerCaseD.getString("Victim_Name"));
        System.out.println("Crime Description: "+PerCaseD.getString("Crime_Description"));
        System.out.println("Case Status: "+PerCaseD.getString("Case_Status"));
        System.out.println("---------------------------------------------------------------------------");

        //>>>
            /*
            System.out.println
                    (
                            String.format
                                    (
                                        "| Criminal ID: %-5d | Name: %-20s | Age: %-3d | Gender: %-8s | Case ID: %-5d | Crime Type: %-15s | Crime Date: %-10s | Officer ID: %-5d | Case Status: %-15s | Punishment: %-15s | Criminal Status: %-15s | Bail Date: %-10s | Release Date: %-10s |",
                                        PerCaseD.getInt("CriminalID"),
                                        PerCaseD.getString("CriminalName"),
                                        PerCaseD.getInt("CriminalAge"),
                                        PerCaseD.getString("CriminalGender"),
                                        PerCaseD.getInt("CaseID"),
                                        PerCaseD.getString("Crime_Type"),
                                        PerCaseD.getDate("Crime_Date"),
                                        PerCaseD.getInt("InvestingOfficerID"),
                                        PerCaseD.getString("Case_Status"),
                                        PerCaseD.getString("Punishment_Type"),
                                        PerCaseD.getString("Criminal_Status"),
                                        PerCaseD.getDate("Bail_Date"),
                                        PerCaseD.getDate("Release_Date")
                                    )
                    )*/
    }

    void UnkSCaseRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] CaseName,Crime_Location,Crime_Weapon,SuspectName,Victim_Name are Valid....");
        System.out.print("Enter Details Related about that Criminal or Crime: ");
        String details=sc.next();

        String UnkSCaseRec="select *, count(*) as TResults from case_details where CaseName like ? or Crime_Location like ? or Crime_Weapon like ? or SuspectName like ? or Victim_Name like ?";
        PreparedStatement QUkSCR=db.getConnection().prepareStatement(UnkSCaseRec);
        QUkSCR.setString(1,"%"+details+"%");
        QUkSCR.setString(2,"%"+details+"%");
        QUkSCR.setString(3,"%"+details+"%");
        QUkSCR.setString(4,"%"+details+"%");
        QUkSCR.setString(5,"%"+details+"%");
        ResultSet CaseD_rs=QUkSCR.executeQuery();
        QUkSCR.close();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(CaseD_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CaseD_rs.getInt("TResults |-----"));
                CountResult=CaseD_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Case Record Found in Database....");
                    QUkSCR.close();
                    return;
                }
                else
                {
                    System.out.println("Would you like to see AlL Results???");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                        QUkSCR.close();
                    }
                    else if(ans=='N'||ans=='n')
                    {
                        System.out.println("[INFO] Returning to Main Menu....");
                        QUkSCR.close();
                        return;
                    }
                    else
                    {
                        System.err.println("[INVALID] Give Answer only by Yes or No...");
                        QUkSCR.close();
                        return;
                    }
                }

                DTR=false;
            }

            //show all results as log type normal....
            System.out.println
                    (
                            String.format
                                    (
                                            "| Case ID: %-5d | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                            CaseD_rs.getInt("Case_ID"),
                                            CaseD_rs.getString("Case_Type"),
                                            CaseD_rs.getString("Case_Status"),
                                            CaseD_rs.getString("Description")
                                    )
                    );


            //>>>If Multiple result found then ask for which data do you want....
            CaseDetails();
        }
    }

    public void AllCasesQ() throws Exception
    {
        String AllCase="Select * from case_details";
        PreparedStatement QAC=db.getConnection().prepareStatement(AllCase);
        ResultSet ACrs=QAC.executeQuery();

        while (ACrs.next())
        {
            System.out.println("Case ID: "+ACrs.getInt("CaseID"));
            System.out.println("Case Type: "+ACrs.getString("Case_Type"));
            System.out.println("Crime Location: "+ACrs.getString("Crime_Location"));
            //System.out.println("Crime Weapon: "+ACrs.getString("Crime_Weapon"));
            //System.out.println("Suspect Name: "+ACrs.getString("SuspectName"));
            //System.out.println("Victim Name: "+ACrs.getString("Victim_Name"));
            //System.out.println("Crime Description: "+ACrs.getString("Crime_Description"));
            System.out.println("Case Name: "+ACrs.getString("CaseName"));
            System.out.println("---------------------------------------------");
        }

        System.out.println("Do you want Complete Case Details???");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            // Display complete case details
            //login method then IO file....
            ResultSet fileSet=QAC.executeQuery();
            IOF.FetchAllCases(fileSet);
            System.out.println("[INFO] Case Data Downloaded Successfully as Text file....");
            ACrs.close();
            fileSet.close();
            QAC.close();
        }
        else if (ans=='N'||ans=='n')
        {
            System.out.println("[INFO] Returning to Main Menu....");

            return;
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
            return;
        }

    }

    public void UpdateCaseData() throws Exception
    {
        System.out.print("Enter CASE ID to update: ");
        int CId= sc.nextInt();

        //validation proocedure call....

        //>>>
        System.out.println("Which Data do you want to Update???");
        System.out.println("1. Criminal ID");
        System.out.println("2. Officer ID");
        System.out.println("3. Case Type");
        System.out.println("4. Crime Weapon");
        System.out.println("5. Suspect Name");
        System.out.println("6. Victim Name");
        System.out.println("7. Crime Description");
        System.out.println("8. Case Status");
        System.out.print("Enter Your Choice: ");
        int UpCD_ch= sc.nextInt();

         switch (UpCD_ch)
         {
             case 1:
             {
                 //CriminalID
                 UpdateCriID(CId);
                 break;
             }

             case 2:
             {
                 //OfficerID
                 UpdateOffID(CId);
                 break;
             }

             case 3:
             {
                 //Case Type
                 UpdateCType(CId);
                 break;
             }

             case 4:
             {
                 //Crime Weapon
                 UpdateWeapon(CId);
                 break;
             }

             case 5:
             {
                 //Suspect Name
                 UpdateSuspect(CId);
                 break;
             }

             case 6:
             {
                 //Victim Name
                 UpdateVictim(CId);
                 break;
             }

             case 7:
             {
                 //Crime Description
                 UpdateDesc(CId);
                 break;
             }

             case 8:
             {
                 //Case Status
                 UpdateStatus(CId);
                 break;
             }

             default:
             {
                 System.err.println("[ERROR] Invalid Choice. Please try again...");
                 break;
             }
         }
    }

    void UpdateCriID(int c_id) throws Exception
    {
        System.out.print("Enter Updating Criminal ID:");
        int CriID=sc.nextInt();

        String UpdateCriID="update case_details set CriminalID=? where CaseID=?";
        PreparedStatement QUCriID=db.getConnection().prepareStatement(UpdateCriID);
        QUCriID.setInt(1,CriID);
        QUCriID.setInt(2,c_id);
        int UCID=QUCriID.executeUpdate();
        QUCriID.close();

        if (UCID>0)
        {
            System.out.println("[UPDATED] Criminal ID updated successfully...");
            QUCriID.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Criminal ID. Please try again...");
            QUCriID.close();
        }
    }

    void UpdateOffID(int c_id) throws Exception
    {
        System.out.print("Enter Updating Officer ID:");
        String OffID=sc.next();

        //If updating officer have not investigating any case....if found that updating officer is investing other case than show free officers list then ask for oficerID

        String UpdateOffID="update case_details set OfficerID=? where CaseID=?";
        PreparedStatement QUOffID=db.getConnection().prepareStatement(UpdateOffID);
        QUOffID.setString(1,OffID);
        QUOffID.setInt(2,c_id);
        int UOID=QUOffID.executeUpdate();
        QUOffID.close();

        if (UOID>0)
        {
            System.out.println("[UPDATED] Officer ID updated successfully...");
        }
        else
        {
            System.err.println("[ERROR] Failed to update Officer ID. Please try again...");
        }
    }

    void UpdateCType(int c_id) throws Exception
    {
        System.out.print("Enter Updating Case Type:");
        String CType=sc.next();

        String UpdateCType="update case_details set Case_Type=? where CaseID=?";
        PreparedStatement QUCType=db.getConnection().prepareStatement(UpdateCType);
        QUCType.setString(1,CType);
        QUCType.setInt(2,c_id);
        int UCT=QUCType.executeUpdate();
        QUCType.close();

        if (UCT>0)
        {
            System.out.println("[UPDATED] Case Type updated successfully...");
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Type. Please try again...");
        }
    }

    void UpdateWeapon(int c_id) throws Exception
    {
        System.out.print("Enter Updating Crime Weapon:");
        String CWeapon=sc.next();

        String UpdateCWeapon="update case_details set Crime_Weapon=? where CaseID=?";
        PreparedStatement QUCWeapon=db.getConnection().prepareStatement(UpdateCWeapon);
        QUCWeapon.setString(1,CWeapon);
        QUCWeapon.setInt(2,c_id);
        int UCW=QUCWeapon.executeUpdate();
        QUCWeapon.close();

        if (UCW>0)
        {
            System.out.println("[UPDATED] Crime Weapon updated successfully...");
        }
        else
        {
            System.err.println("[ERROR] Failed to update Crime Weapon. Please try again...");
        }
    }

    void UpdateSuspect(int c_id) throws Exception
    {
        System.out.print("Enter Updating Suspect Name:");
        String SName=sc.next();

        String UpdateSuspect="update case_details set SuspectName=? where CaseID=?";
        PreparedStatement QUSuspect=db.getConnection().prepareStatement(UpdateSuspect);
        QUSuspect.setString(1,SName);
        QUSuspect.setInt(2,c_id);
        int USN=QUSuspect.executeUpdate();
        QUSuspect.close();

        if (USN>0)
        {
            System.out.println("[UPDATED] Suspect Name updated successfully...");
            QUSuspect.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Suspect Name. Please try again...");
            QUSuspect.close();
        }
    }

    void UpdateVictim(int c_id) throws Exception
    {
        System.out.print("Enter Updating Victim Name:");
        String VName=sc.next();

        String UpdateVictim="update case_details set Victim_Name=? where CaseID=?";
        PreparedStatement QUVictim=db.getConnection().prepareStatement(UpdateVictim);
        QUVictim.setString(1,VName);
        QUVictim.setInt(2,c_id);
        int UVN=QUVictim.executeUpdate();
        QUVictim.close();

        if (UVN>0)
        {
            System.out.println("[UPDATED] Victim Name updated successfully...");
            QUVictim.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Victim Name. Please try again...");
            QUVictim.close();
        }
    }

    void UpdateDesc(int c_id) throws Exception
    {
        System.out.print("Enter Updating Case Description:");
        String CDesc=sc.next();

        String UpdateCDesc="update case_details set Case_Description=? where CaseID=?";
        PreparedStatement QUCDesc=db.getConnection().prepareStatement(UpdateCDesc);
        QUCDesc.setString(1,CDesc);
        QUCDesc.setInt(2,c_id);
        int UCD=QUCDesc.executeUpdate();

        if (UCD>0)
        {
            System.out.println("[UPDATED] Case Description updated successfully...");
            QUCDesc.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Description. Please try again...");
            QUCDesc.close();
        }
    }

    void UpdateStatus(int c_id) throws Exception
    {
        System.out.print("Enter Updating Case Status:");
        String CStatus=sc.next();

        String UpdateCStatus="update case_details set Case_Status=? where CaseID=?";
        PreparedStatement QUCStatus=db.getConnection().prepareStatement(UpdateCStatus);
        QUCStatus.setString(1,CStatus);
        QUCStatus.setInt(2,c_id);
        int UCS=QUCStatus.executeUpdate();

        if (UCS>0)
        {
            System.out.println("[UPDATED] Case Status updated successfully...");
            QUCStatus.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Status. Please try again...");
            QUCStatus.close();
        }
    }

    public void CrimeRatio() throws Exception
    {
        String CrimeRatioQuery="select Case_Type, count(*) as TotalCases from case_details group by Case_Type";
        PreparedStatement QCR=db.getConnection().prepareStatement(CrimeRatioQuery);
        ResultSet CRrs=QCR.executeQuery();

        System.out.println("Crime Ratio Report:");
        System.out.println("-------------------");
        while (CRrs.next())
        {
            System.out.println("Case Type: "+CRrs.getString("Case_Type")+" | Total Cases: "+CRrs.getInt("TotalCases"));
        }
        QCR.close();
    }

    public void AssignPCase() throws Exception
    {
        if (PCS.isEmpty())
        {
            System.out.println("[INFO] No pending cases in queue.");
            return;
        }

        System.out.println("Next Pending Case to Assign:");
        System.out.println(PCS.peek());

        String AssPCase="select CaseID as case_id from case_details where Case_Status is null or lower(Case_Status)='pending' limit 1";
        PreparedStatement QAPCase=db.getConnection().prepareStatement(AssPCase);
        ResultSet PCase_rs=QAPCase.executeQuery();
        if (PCase_rs.next())
        {
            int case_id=PCase_rs.getInt(1);
            QAPCase.close();

            System.out.println("Do you want to Assign this Case to any Officer???(yes/no)");
            char ans=sc.next().charAt(0);

            if (ans=='Y'||ans=='y')
            {
                UpdateOffID(case_id);
                UpdateCType(case_id);
                PCS.poll();
            }
            else if (ans=='N'||ans=='n')
            {
                System.out.println("[INFO] CASE ARE STILL PENDING....");
            }
            else
            {
                System.err.println("[INVALID] Enter Valid Answer by Yes or No....");
            }
        }
        else
        {
            QAPCase.close();
            System.out.println("[INFO] No pending cases found in database.");
        }
    }
}