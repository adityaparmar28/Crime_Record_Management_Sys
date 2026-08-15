package Quries;

import DataBase.DataFound;
import DataBase.Database;
import DataBase.Validation;
import DataStructure.DataStructure;
import DataStructure.IOFiles;
import Profile.Login_SignUpPage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Queue;
import java.util.Scanner;

public class CrimeMngrQueries
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();
    OODataQueries OODQ=new OODataQueries();
    Queue<String> PCS=DataStructure.PendingCase;
    IOFiles IOF=new IOFiles();
    Validation v=new Validation();
    DataFound found=new DataFound();
    Login_SignUpPage LSP=new Login_SignUpPage();

    public void FileFIRQ() throws Exception
    {
        String CDesc="";

        System.out.println("-------------------| FILE FIR REPORT |-------------------");
        System.out.println("| Enter the following details to file a FIR report....");
        System.out.println("---------------------------------------------------------");
        System.out.println();

        String CType=v.readAlphaString("Enter Case Type: ");
        char CtypeAlpha=CType.toUpperCase().charAt(0);

        String CDate=v.Date("Enter Crime Date: ");

        String CaseID=v.readNonEmptyString("Enter Case ID (Numeric part, e.g. 1001): ");
        String Case_ID=CtypeAlpha + CaseID;

        String CLocation=v.readAlphaString("Enter Crime Location: ");

        System.out.print("Is there Involved Weapon at CrimeScene???(yes/no): ");
        char CWIn=sc.next().charAt(0);

        String CWeapon=null;

        if(CWIn=='Y'||CWIn=='y')
        {
            System.out.print("Enter Crime Weapon: ");
            CWeapon=sc.next();
        } else if (CWIn=='N'||CWIn=='n')
        {
        }
        else
        {
            System.err.println("[INVALID] Invalid Input....");
            System.out.println("[INFO] Considering there is no weapon at Crime Scene....");
            return;
        }

        String SuspectName=v.readAlphaString("Enter Suspect Name: ");

        String VictimName=v.readAlphaString("Enter Victim Name: ");

        try
        {
            sc.nextLine();

            System.out.print("Enter Crime Description: ");
            CDesc= sc.nextLine();
        }
        catch (Exception e)
        {
        }

        String CName=v.readAlphaString("Enter Case Name: ");

        db.con.setAutoCommit(false);

        String CriminalID = CtypeAlpha + "5" + (CaseID.length() >= 2 ? CaseID.substring(CaseID.length()-2) : CaseID);

        // 1. Insert into case_details (leaving CriminalID NULL initially to avoid FK constraint fail)
        String FileFIR="insert into case_details(CaseID,CaseType,CrimeLocation,CrimeWeapon,SuspectName,VictimName,CrimeDetails,CaseName) values(?,?,?,?,?,?,?,?)";

        PreparedStatement QFIR=db.getConnection().prepareStatement(FileFIR);
        QFIR.setString(1,Case_ID);
        QFIR.setString(2,CType);
        QFIR.setString(3,CLocation);
        QFIR.setString(4,CWeapon);
        QFIR.setString(5,SuspectName);
        QFIR.setString(6,VictimName);
        QFIR.setString(7,CDesc);
        QFIR.setString(8,CName);
        int update=QFIR.executeUpdate();

        // 2. Insert into criminal_details
        String AddCriData="insert into criminal_details(CriminalID,Name,Age,Gender,CaseID,CrimeType,CrimeDate) values(?,?,?,?,?,?,?)";

        PreparedStatement QACriData=db.getConnection().prepareStatement(AddCriData);
        QACriData.setString(1,CriminalID);
        QACriData.setString(2,SuspectName);
        QACriData.setInt(3,0);
        QACriData.setString(4,"Male");
        QACriData.setString(5,Case_ID);
        QACriData.setString(6,CType);
        QACriData.setDate(7,java.sql.Date.valueOf(CDate));
        int addUpdate=QACriData.executeUpdate();

        // 3. Update case_details to link the CriminalID (which now exists in criminal_details)
        String LinkCriCase = "update case_details set CriminalID=? where CaseID=?";

        PreparedStatement QLinkCriCase = db.getConnection().prepareStatement(LinkCriCase);
        QLinkCriCase.setString(1, CriminalID);
        QLinkCriCase.setString(2, Case_ID);
        int linkUpdate = QLinkCriCase.executeUpdate();

        // 4. Add picture placeholder details to Criminal_Pictures
        String AddCriPic = "insert into Criminal_Pictures(CriminalID,CaseID,CriminalName) values(?,?,?)";

        PreparedStatement QACriPic = db.getConnection().prepareStatement(AddCriPic);
        QACriPic.setString(1, CriminalID);
        QACriPic.setString(2, Case_ID);
        QACriPic.setString(3, SuspectName);
        int picUpdate = QACriPic.executeUpdate();

        if(update>0 && addUpdate>0 && linkUpdate>0 && picUpdate>0)
        {
            System.out.println("[UPDATED] FIR report filed successfully...");

            db.con.commit();

            /**>>>FIR IO File Showing.....**/
            IOF.FetchFIR(Case_ID,CDate);

            QFIR.close();
            QACriData.close();
            QLinkCriCase.close();
            QACriPic.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to file FIR....Try again...");

            db.con.rollback();
            QFIR.close();
            QACriData.close();
            QLinkCriCase.close();
            QACriPic.close();
        }
    }

    public void SearchCRQ() throws Exception
    {
        System.out.println("Do you Know Anything Particular Details about Case???(Yes/No)");
        System.out.print(">>> ");
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
            System.err.println("[INVALID] Give Answer only by Yes or No....");
        }
    }

    void KSCaseRec() throws Exception
    {
        char ans;
        System.out.println("Which data do you know about Case???");
        System.out.println("[INFO] CaseName,CrimeLocation,CrimeWeapon,SuspectName,VictimName are Valid....");
        System.out.print(">>> ");
        String ColummName = sc.next();

        String TableName = "case_details";
        System.out.print("Enter " + ColummName + " Details: ");

        Object Kdetails = OODQ.SQLDType2JDType(ColummName, TableName);

        String SearchCaseRec = "select *, count(*) over() as TResults from case_details where " + ColummName + " like ?";

        PreparedStatement QSOR = db.getConnection().prepareStatement(SearchCaseRec);
        QSOR.setObject(1,"%"+Kdetails+"%");
        ResultSet CaseD_rs = QSOR.executeQuery();

        boolean DTR = true;//Display total results....
        int CountResult = 0;

        if (CaseD_rs.next())
        {
            if (DTR)
            {
                System.out.println("-----| Total Results Found: " + CaseD_rs.getInt("TResults") + " |-----");
                CountResult = CaseD_rs.getInt("TResults");

                if (CountResult == 0)
                {
                    System.out.println("[INFO] No Case Record Found in Database....");
                    QSOR.close();
                    return;
                }
                else
                {
                    System.out.println("Would you like to see AlL Results???");
                    System.out.print(">>> ");
                    ans = sc.next().charAt(0);

                    if (ans == 'Y' || ans == 'y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                        do
                        {
                            System.out.println
                                    (
                                            String.format
                                                    (
                                                            "| Case ID: %-11s | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                                            CaseD_rs.getString("CaseID"),
                                                            CaseD_rs.getString("CaseType"),
                                                            CaseD_rs.getString("CaseStatus"),
                                                            CaseD_rs.getString("CrimeDetails")
                                                    )
                                    );
                        } while (CaseD_rs.next());

                        DTR = false;
                    }
                    else if (ans == 'N' || ans == 'n')
                    {
                        System.out.println("[INFO] Returning to Main Menu....");
                        QSOR.close();
                        DTR = false;
                        return;
                    }
                    else
                    {
                        System.err.println("[INVALID] Give Answer only by Yes or No...");
                        QSOR.close();
                        DTR = false;
                        return;
                    }
                }
            }

            System.out.print("Enter Case ID to see more details about that Case Record: ");
            String caseID=sc.next();

            if(!found.isCaseFound(caseID))
            {
                System.err.println("[ERROR] Case Not Found....");
                return;
            }

            if(LSP.LoggedUserID=="")
            {
                if(LSP.userLogin())
                {
                    CaseDetails(caseID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                    return;
                }
            }
            else
            {
                CaseDetails(caseID);
            }
        }
    }

    void CaseDetails(String caseID) throws Exception
    {
        String PerSCaseRec="select * from case_details where CaseID=?";
        PreparedStatement QPSR=db.getConnection().prepareStatement(PerSCaseRec);
        QPSR.setString(1,caseID);
        ResultSet PerCaseD=QPSR.executeQuery();

        PerCaseD.next();
        System.out.println("-----------------------| Case Record Details |--------------------------");
        System.out.println("Case ID: "+PerCaseD.getString("CaseID"));
        System.out.println("Criminal ID: "+PerCaseD.getString("CriminalID"));
        System.out.println("Case Name: "+PerCaseD.getString("CaseName"));
        System.out.println("Officer ID: "+PerCaseD.getString("OfficerID"));
        System.out.println("Case Type: "+PerCaseD.getString("CaseType"));
        System.out.println("Crime Location: "+PerCaseD.getString("CrimeLocation"));
        System.out.println("Crime Weapon: "+PerCaseD.getString("CrimeWeapon"));
        System.out.println("Suspect Name: "+PerCaseD.getString("SuspectName"));
        System.out.println("Victim Name: "+PerCaseD.getString("VictimName"));
        System.out.println("Crime Description: "+PerCaseD.getString("CrimeDetails"));
        System.out.println("Case Status: "+PerCaseD.getString("CaseStatus"));
        System.out.println("---------------------------------------------------------------------------");
        QPSR.close();
    }

    void UnkSCaseRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] CaseName,CrimeLocation,CrimeWeapon,SuspectName,VictimName are Valid....");
        System.out.print("Enter Details Related about that Criminal or Crime: ");
        String details=sc.next();

        String UnkSCaseRec="select *, count(*) over() as TResults from case_details where CaseName like ? or CrimeLocation like ? or CrimeWeapon like ? or SuspectName like ? or VictimName like ?";

        PreparedStatement QUkSCR=db.getConnection().prepareStatement(UnkSCaseRec);
        QUkSCR.setString(1,"%"+details+"%");
        QUkSCR.setString(2,"%"+details+"%");
        QUkSCR.setString(3,"%"+details+"%");
        QUkSCR.setString(4,"%"+details+"%");
        QUkSCR.setString(5,"%"+details+"%");
        ResultSet CaseD_rs=QUkSCR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;

        if(CaseD_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CaseD_rs.getInt("TResults")+" |-----");
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
                    System.out.print(">>> ");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                        do
                        {
                            System.out.println
                                    (
                                            String.format
                                                    (
                                                            "| Case ID: %-11s | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                                            CaseD_rs.getString("CaseID"),
                                                            CaseD_rs.getString("CaseType"),
                                                            CaseD_rs.getString("CaseStatus"),
                                                            CaseD_rs.getString("CrimeDetails")
                                                    )
                                    );
                        } while (CaseD_rs.next());

                        DTR=false;
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
            }

            System.out.print("Enter Case ID to see more details about that Case Record: ");
            String caseID=sc.next();

            if(!found.isCaseFound(caseID))
            {
                System.err.println("[ERROR] Case Not Found....");
                return;
            }

            if(LSP.LoggedUserID=="")
            {
                if(LSP.userLogin())
                {
                    CaseDetails(caseID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                    return;
                }
            }
            else
            {
                CaseDetails(caseID);
            }
        }
    }

    public void AllCasesQ() throws Exception
    {
        String AllCase="Select * from case_details";
        PreparedStatement QAC=db.getConnection().prepareStatement(AllCase);
        ResultSet ACrs=QAC.executeQuery();

        while (ACrs.next())
        {
            System.out.println("Case ID: "+ACrs.getString("CaseID"));
            System.out.println("Case Type: "+ACrs.getString("CaseType"));
            System.out.println("Crime Location: "+ACrs.getString("CrimeLocation"));
            System.out.println("Case Name: "+ACrs.getString("CaseName"));
            System.out.println("---------------------------------------------");
        }

        System.out.println("Do you want Complete Case Details???");
        System.out.print(">>> ");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            ResultSet fileSet=QAC.executeQuery();

            if(LSP.LoggedUserID=="")
            {
                if(LSP.userLogin())
                {
                    IOF.FetchAllCases(fileSet);
                    System.out.println("[INFO] Case Data Downloaded Successfully as Text file....");
                    ACrs.close();
                    fileSet.close();
                    QAC.close();
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                    return;
                }
            }
            else
            {
                IOF.FetchAllCases(fileSet);
                System.out.println("[INFO] Case Data Downloaded Successfully as Text file....");
                ACrs.close();
                fileSet.close();
                QAC.close();
            }
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
        String CId= sc.next();

        if(!found.isCaseFound(CId))
        {
            System.out.println("[NOT FOUND] Case not found....");
            return;
        }

        System.out.println("Which Data do you want to Update???");
        System.out.println("| 1. Criminal ID");
        System.out.println("| 2. Officer ID");
        System.out.println("| 3. Case Type");
        System.out.println("| 4. Crime Weapon");
        System.out.println("| 5. Suspect Name");
        System.out.println("| 6. Victim Name");
        System.out.println("| 7. Crime Description");
        System.out.println("| 8. Case Status");
        System.out.println("| 0. Cancel.....");
        System.out.print("| Enter Your Choice: ");
        int UpCD_ch= sc.nextInt();

        switch (UpCD_ch)
        {
            case 0:
            {
                System.out.println("[CANCELLED] Case Details Updation Cancelled....");
                break;
            }

            case 1:
            {
                UpdateCriID(CId);
                break;
            }

            case 2:
            {
                UpdateOffID(CId);
                break;
            }

            case 3:
            {
                UpdateCType(CId);
                break;
            }

            case 4:
            {
                UpdateWeapon(CId);
                break;
            }

            case 5:
            {
                UpdateSuspect(CId);
                break;
            }

            case 6:
            {
                UpdateVictim(CId);
                break;
            }

            case 7:
            {
                UpdateDesc(CId);
                break;
            }

            case 8:
            {
                UpdateStatus(CId);
                break;
            }

            default:
            {
                System.err.println("[ERROR] Invalid Choice....Try again...");
                break;
            }
        }
    }

    void UpdateCriID(String c_id) throws Exception
    {
        String CriID=v.readNonEmptyString("Enter Updating Criminal ID: ");

        db.con.setAutoCommit(false);

        String UpdateCriID="update case_details set CriminalID=? where CaseID=?";
        PreparedStatement QUCriID=db.getConnection().prepareStatement(UpdateCriID);
        QUCriID.setString(1,CriID);
        QUCriID.setString(2,c_id);
        int UCID=QUCriID.executeUpdate();

        /**Updating Criminal ID of that case Id in Criminal_details....**/

        String UpCriIdDetails="update criminal_details set CriminalID=? where CaseID=?";
        PreparedStatement QUCriIdData=db.getConnection().prepareStatement(UpCriIdDetails);
        QUCriIdData.setString(1,CriID);
        QUCriIdData.setString(2,c_id);
        int UCIdData=QUCriIdData.executeUpdate();

        if (UCID>0 && UCIdData>0)
        {
            System.out.println("[UPDATED] Criminal ID updated successfully...");
            db.con.commit();
            QUCriID.close();
            QUCriIdData.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Criminal ID....Try again...");
            db.con.rollback();
            QUCriID.cancel();
            QUCriIdData.cancel();
        }
    }

    void UpdateOffID(String c_id) throws Exception
    {
        String OffID=v.readNonEmptyString("Enter Updating Officer ID: ");

        db.con.setAutoCommit(false);

        String UpdateOffID="update case_details set OfficerID=? , CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUOffID=db.getConnection().prepareStatement(UpdateOffID);
        QUOffID.setString(1,OffID);
        QUOffID.setString(2,c_id);
        int UOID=QUOffID.executeUpdate();

        /**Updating Criminal's officer Id....**/

        String UpOffIdCriD="update criminal_details set InvestingOfficerID=?, CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUOffCriD=db.getConnection().prepareStatement(UpOffIdCriD);
        QUOffCriD.setString(1,OffID);
        QUOffCriD.setString(2,c_id);
        int UOffCri=QUOffCriD.executeUpdate();

        /**Updating officers Investing CaseId.....**/

        String UpCaIdOff="update officer_details set AssignedCase=? , CaseStatus='Investigating' where OfficerID=?";
        PreparedStatement QUCaIOff=db.getConnection().prepareStatement(UpCaIdOff);
        QUCaIOff.setString(1,c_id);
        QUCaIOff.setString(2,OffID);
        int UCIOff=QUCaIOff.executeUpdate();

        if (UOID>0 && UOffCri>0 && UCIOff>0)
        {
            System.out.println("[UPDATED] Officer ID updated successfully...");
            db.con.commit();
            QUOffID.close();
            QUOffCriD.close();
            QUCaIOff.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Officer ID....Try again...");
            db.con.rollback();
            QUOffID.close();
            QUOffCriD.close();
            QUCaIOff.close();
        }
    }

    void UpdateCType(String c_id) throws Exception
    {
        String CType=v.readAlphaString("Enter Updating Case Type: ");

        db.con.setAutoCommit(false);

        String UpdateCType="update case_details set CaseType=? where CaseID=?";
        PreparedStatement QUCType=db.getConnection().prepareStatement(UpdateCType);
        QUCType.setString(1,CType);
        QUCType.setString(2,c_id);
        int UCT=QUCType.executeUpdate();

        String UpCType="update criminal_details set CrimeType=? where CaseID=?";
        PreparedStatement QUpCType=db.getConnection().prepareStatement(UpCType);
        QUpCType.setString(1,CType);
        QUpCType.setString(2,c_id);
        int UpCT=QUpCType.executeUpdate();

        if (UCT>0 && UpCT>0)
        {
            System.out.println("[UPDATED] Case Type updated successfully...");
            db.con.commit();
            QUCType.close();
            QUpCType.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Type....Try again...");
            db.con.rollback();
            QUCType.close();
            QUpCType.close();
        }
    }

    void UpdateWeapon(String c_id) throws Exception
    {
        String CWeapon=v.readAlphaString("Enter Updating Crime Weapon: ");

        db.con.setAutoCommit(false);

        String UpdateCWeapon="update case_details set CrimeWeapon=? where CaseID=?";
        PreparedStatement QUCWeapon=db.getConnection().prepareStatement(UpdateCWeapon);
        QUCWeapon.setString(1,CWeapon);
        QUCWeapon.setString(2,c_id);
        int UCW=QUCWeapon.executeUpdate();

        if (UCW>0)
        {
            System.out.println("[UPDATED] Crime Weapon updated successfully...");
            db.con.commit();
            QUCWeapon.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Crime Weapon....Try again...");
            db.con.rollback();
            QUCWeapon.close();
        }
    }

    void UpdateSuspect(String c_id) throws Exception
    {
        String SName=v.readAlphaString("Enter Updating Suspect Name: ");

        db.con.setAutoCommit(false);

        String UpdateSuspect="update case_details set SuspectName=? where CaseID=?";
        PreparedStatement QUSuspect=db.getConnection().prepareStatement(UpdateSuspect);
        QUSuspect.setString(1,SName);
        QUSuspect.setString(2,c_id);
        int USN=QUSuspect.executeUpdate();

        String UpCriName="update criminal_details set Name=? where CaseID=?";
        PreparedStatement QUCriN=db.getConnection().prepareStatement(UpCriName);
        QUCriN.setString(1,SName);
        QUCriN.setString(2,c_id);
        int UCriN=QUCriN.executeUpdate();

        String UpCriPicName="update Criminal_Pictures set CriminalName=? where CaseID=?";
        PreparedStatement QUCriPicN=db.getConnection().prepareStatement(UpCriPicName);
        QUCriPicN.setString(1,SName);
        QUCriPicN.setString(2,c_id);
        int UCriPicN=QUCriPicN.executeUpdate();

        if (USN>0 && UCriN>0 && UCriPicN>=0)
        {
            System.out.println("[UPDATED] Suspect Name updated successfully...");
            db.con.commit();
            QUSuspect.close();
            QUCriN.close();
            QUCriPicN.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Suspect Name....Try again...");
            db.con.rollback();
            QUSuspect.close();
            QUCriN.close();
            QUCriPicN.close();
        }
    }

    void UpdateVictim(String c_id) throws Exception
    {
        String VName=v.readAlphaString("Enter Updating Victim Name: ");

        db.con.setAutoCommit(false);

        String UpdateVictim="update case_details set VictimName=? where CaseID=?";
        PreparedStatement QUVictim=db.getConnection().prepareStatement(UpdateVictim);
        QUVictim.setString(1,VName);
        QUVictim.setString(2,c_id);
        int UVN=QUVictim.executeUpdate();

        if (UVN>0)
        {
            System.out.println("[UPDATED] Victim Name updated successfully...");
            db.con.commit();
            QUVictim.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Victim Name....Try again...");
            db.con.rollback();
            QUVictim.cancel();
        }
    }

    void UpdateDesc(String c_id) throws Exception
    {
        String CDesc=v.readAlphaString("Enter Updating Case Description: ");

        db.con.setAutoCommit(false);

        String UpdateCDesc="update case_details set CrimeDetails=? where CaseID=?";
        PreparedStatement QUCDesc=db.getConnection().prepareStatement(UpdateCDesc);
        QUCDesc.setString(1,CDesc);
        QUCDesc.setString(2,c_id);
        int UCD=QUCDesc.executeUpdate();

        if (UCD>0)
        {
            System.out.println("[UPDATED] Case Description updated successfully...");
            db.con.commit();
            QUCDesc.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Description....Try again...");
            db.con.rollback();
            QUCDesc.cancel();
        }
    }

    void UpdateStatus(String c_id) throws Exception
    {
        String CStatus=v.readAlphaString("Enter Updating Case Status: ");

        db.con.setAutoCommit(false);

        String UpdateCStatus="update case_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCStatus=db.getConnection().prepareStatement(UpdateCStatus);
        QUCStatus.setString(1,CStatus);
        QUCStatus.setString(2,c_id);
        int UCS=QUCStatus.executeUpdate();

        String UpCStCri="update criminal_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCSCri=db.getConnection().prepareStatement(UpCStCri);
        QUCSCri.setString(1,CStatus);
        QUCSCri.setString(2,c_id);
        int UCSCri=QUCSCri.executeUpdate();

        String UpCSOff="update officer_details set CaseStatus=? where AssignedCase=?";
        PreparedStatement QUCSOff=db.getConnection().prepareStatement(UpCSOff);
        QUCSOff.setString(1,CStatus);
        QUCSOff.setString(2,c_id);
        int UCSOff=QUCSOff.executeUpdate();

        if (UCS>0 && UCSCri>0 && UCSOff>0)
        {
            System.out.println("[UPDATED] Case Status updated successfully...");
            db.con.commit();
            QUCStatus.close();
            QUCSOff.close();
            QUCSCri.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Status...Try again...");
            db.con.rollback();
            QUCStatus.close();
            QUCSOff.close();
            QUCSCri.close();
        }
    }

    public void CrimeRatio() throws Exception
    {
        String CrimeRatioQuery="select CaseType, count(*) as TotalCases from case_details group by CaseType";
        PreparedStatement QCR=db.getConnection().prepareStatement(CrimeRatioQuery);
        ResultSet CRrs=QCR.executeQuery();

        System.out.println("Crime Ratio Report:");
        System.out.println("-------------------");
        while (CRrs.next())
        {
            System.out.println("Case Type: "+CRrs.getString("CaseType")+" | Total Cases: "+CRrs.getInt("TotalCases"));
        }
        QCR.close();
    }

    public void AssignPCase() throws Exception
    {
        if (PCS.isEmpty())
        {
            System.out.println("[INFO] No pending cases in queue....");
            return;
        }

        if(!(Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Admin") ||
                Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Officer")))
        {
            System.err.print("[WARNING] Only Directory of Police Officers Members can Assign Case....");
            return;
        }

        System.out.print("Next Pending Case to Assign: ");
        System.out.println(PCS.peek());

        String AssPCase="select CaseID as case_id from case_details where CaseStatus is null or lower(CaseStatus)='pending' limit 1";
        PreparedStatement QAPCase=db.getConnection().prepareStatement(AssPCase);
        ResultSet PCase_rs=QAPCase.executeQuery();
        if (PCase_rs.next())
        {
            String case_id=PCase_rs.getString(1);
            QAPCase.close();

            System.out.println("Do you want to Assign this Case to any Officer???(yes/no)");
            System.out.print(">>> ");
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
            System.out.println("[INFO] No pending cases found in database....");
        }
    }
}