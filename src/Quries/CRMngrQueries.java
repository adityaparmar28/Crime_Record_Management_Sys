package Quries;

import DataBase.DataFound;
import DataBase.Database;
import DataBase.Validation;
import DataStructure.IOFiles;
import Profile.Login_SignUpPage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CRMngrQueries
{
    Database db = new Database();
    Scanner sc = new Scanner(System.in);
    OODataQueries OODQ=new OODataQueries();
    IOFiles IOF=new IOFiles();
    Validation v=new Validation();
    DataFound found=new DataFound();
    Login_SignUpPage LSP=new Login_SignUpPage();

    public void AddCriminalRQuery() throws Exception
    {
        String C_Gender="";
        String C_CaseStatus="";

        String C_Id=v.readNonEmptyString("Enter Criminal ID: ");

        String C_Name=v.readNonEmptyString("Enter Criminal Name: ");

        int C_Age=v.readIntRange("Enter Criminal Age: ",10,100);

        try
        {
            System.out.print("Enter Criminal Gender: ");
            C_Gender = sc.next();

            if(!(C_Gender.equalsIgnoreCase("Female") || C_Gender.equalsIgnoreCase("Male")))
            {
                System.err.println("[INVALID] Invalid Gender....");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid Gender....Enter a valid Criminal Gender....");
        }

        String C_CaseID=v.readNonEmptyString("Enter Case ID: ");

        String C_CrimeType=v.readNonEmptyString("Enter Crime Type: ");

        String C_CrimeDate=v.Date("Crime Date");

        String C_IOffID=v.readNonEmptyString("Enter Investigating Officer ID: ");

        boolean CaseSLoop=false;
        while (!CaseSLoop)
        {
            C_CaseStatus = v.readAlphaString("Enter Case Status: ");
            C_CaseStatus = C_CaseStatus.toUpperCase();

            if (!(C_CaseStatus.matches("PENDING") || C_CaseStatus.matches("INVESTIGATING") || C_CaseStatus.matches("SOLVED")))
            {
                System.err.println("[INVALID] Invalid Case Status.....");
            }
            else
            {
                CaseSLoop=true;
            }
        }

        db.con.setAutoCommit(false);

        String addCRQ = "insert into criminal_details(CriminalID,Name,Age,Gender,CaseID,CrimeType,CrimeDate,InvestingOfficerID,CaseStatus) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement addCR = db.getConnection().prepareStatement(addCRQ);
        addCR.setString(1,C_Id);
        addCR.setString(2, C_Name);
        addCR.setInt(3, C_Age);
        addCR.setString(4, C_Gender);
        addCR.setString(5, C_CaseID);
        addCR.setString(6, C_CrimeType);
        addCR.setDate(7, java.sql.Date.valueOf(C_CrimeDate));
        addCR.setString(8, C_IOffID);
        addCR.setString(9, C_CaseStatus);
        int update = addCR.executeUpdate();

        int updateCase = 0;
        int updatePic = 0;
        PreparedStatement upCD = null;
        PreparedStatement upCP = null;

        if (update > 0)
        {
            // Sync with case_details
            String upCDQuery = "update case_details set CriminalID=? where CaseID=?";
            upCD = db.getConnection().prepareStatement(upCDQuery);
            upCD.setString(1, C_Id);
            upCD.setString(2, C_CaseID);
            updateCase = upCD.executeUpdate();

            // Sync with Criminal_Pictures
            String upCPQuery = "insert into Criminal_Pictures(CriminalID,CaseID,CriminalName) values(?,?,?)";
            upCP = db.getConnection().prepareStatement(upCPQuery);
            upCP.setString(1, C_Id);
            upCP.setString(2, C_CaseID);
            upCP.setString(3, C_Name);
            updatePic = upCP.executeUpdate();
        }

        if (update > 0 && updateCase > 0 && updatePic > 0)
        {
            System.out.println("[UPDATED] Criminal details added successfully...");
            db.con.commit();
            addCR.close();
            if (upCD != null) upCD.close();
            if (upCP != null) upCP.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Added...");
            db.con.rollback();
            addCR.close();
            if (upCD != null) upCD.close();
            if (upCP != null) upCP.close();
        }
    }

    public void UpdateCriminalRQuery() throws Exception
    {
        String C_CaseID=v.readNonEmptyString("Enter Case ID to Update: ");

        if(!found.isCaseFound(C_CaseID))
        {
            System.err.println("[ERROR] Case not Found....");
            return;
        }

        try
        {
            System.out.println("Which Details do you want to Update???");
            System.out.println("| 1. Investingating Officer ID.");
            System.out.println("| 2. Case Status.");
            System.out.println("| 3. Criminal Status.");
            System.out.println("| 4. Bail Date.");
            System.out.println("| 5. Release Date.");
            System.out.println("| 0. Cancel...");
            System.out.print("| Enter Your Choice: ");
            int updateCri_ch = sc.nextInt();

            switch (updateCri_ch)
            {
                case 0:
                {
                    System.out.println("[CANCELLED] Criminal Details Updation Cancelled....");
                    break;
                }

                case 1:
                {
                    String C_IOffID = v.readNonEmptyString("Enter New Investigating Officer ID: ");
                    UpdateIOffID(C_IOffID, C_CaseID);

                    break;
                }

                case 2:
                {
                    String C_CaseStatus="";

                    boolean CaseSLoop=false;
                    while (!CaseSLoop)
                    {
                        C_CaseStatus = v.readAlphaString("Enter Case Status: ");
                        C_CaseStatus = C_CaseStatus.toUpperCase();

                        if (!(C_CaseStatus.matches("PENDING") || C_CaseStatus.matches("INVESTIGATING") || C_CaseStatus.matches("SOLVED")))
                        {
                            System.err.println("[INVALID] Invalid Case Status.....");
                        }
                        else
                        {
                            CaseSLoop=true;
                        }
                    }
                    UpdateCaseStatus(C_CaseStatus, C_CaseID);

                    break;
                }

                case 3:
                {
                    System.out.print("Enter Updating Criminal Status: ");
                    String C_CriminalStatus ="";

                    boolean CriminalSLoop=false;
                    while (!CriminalSLoop)
                    {
                        C_CriminalStatus = v.readAlphaString("Enter Case Status: ");
                        C_CriminalStatus = C_CriminalStatus.toUpperCase();

                        if (!(C_CriminalStatus.matches("JAILED") || C_CriminalStatus.matches("BAILED") || C_CriminalStatus.matches("DEAD")||C_CriminalStatus.matches("RELEASED")))
                        {
                            System.err.println("[INVALID] Invalid Case Status.....");
                        }
                        else
                        {
                            CriminalSLoop=true;
                        }
                    }

                    UpdateCriminalStatus(C_CriminalStatus, C_CaseID);

                    break;
                }

                case 4:
                {

                    String C_BailDate = v.Date("Updating Bail Date");
                    SetUpdateBail(C_BailDate, C_CaseID);

                    break;
                }

                case 5:
                {
                    String C_ReleaseDate = v.Date("Updating Release Date");
                    UpdateReleaseDate(C_ReleaseDate, C_CaseID);

                    break;
                }

                default:
                {
                    System.out.println("[ERROR] Invalid number format. Please enter a valid choice....");
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid Choice....Enter a valid choice....");
        }
    }

    void UpdateIOffID(String oid, String CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UCri_IOID = "update criminal_details set InvestingOfficerID=? where CaseID=?";
        PreparedStatement QUCrIOID = db.getConnection().prepareStatement(UCri_IOID);
        QUCrIOID.setString(1, oid);
        QUCrIOID.setString(2, CaseID);
        int updateCIO = QUCrIOID.executeUpdate();

        String UOf_CaseID = "update officer_details set AssignedCase=? where OfficerID=?";
        PreparedStatement QUOfCID = db.getConnection().prepareStatement(UOf_CaseID);
        QUOfCID.setString(1, CaseID);
        QUOfCID.setString(2, oid);
        int updateIO_cid = QUOfCID.executeUpdate();

        String UOIdCase = "update case_details set OfficerID=? where CaseID=?";
        PreparedStatement QUOIdCase = db.getConnection().prepareStatement(UOIdCase);
        QUOIdCase.setString(2, CaseID);
        QUOIdCase.setString(1, oid);
        int upOidCase = QUOIdCase.executeUpdate();

        if (updateCIO > 0 && updateIO_cid > 0 && upOidCase>0)
        {
            System.out.println("[UPDATED] Criminal details updated successfully...");
            db.con.commit();
            QUCrIOID.close();
            QUOfCID.close();
            QUOIdCase.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Updated...");
            db.con.rollback();
            QUCrIOID.cancel();
            QUOfCID.cancel();
            QUOIdCase.cancel();
        }
    }

    void UpdateCaseStatus(String Status, String CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UCase_Status = "update criminal_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(UCase_Status);
        QUCS.setString(1, Status);
        QUCS.setString(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        String UCD_CaseStatus = "update case_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCD_CS = db.getConnection().prepareStatement(UCD_CaseStatus);
        QUCD_CS.setString(1, Status);
        QUCD_CS.setString(2, CaseID);
        int updateCD_CS = QUCD_CS.executeUpdate();

        String UOD_CaseStatus = "update officer_details set CaseStatus=? where AssignedCase=?";
        PreparedStatement QUOD_CS = db.getConnection().prepareStatement(UOD_CaseStatus);
        QUOD_CS.setString(1, Status);
        QUOD_CS.setString(2, CaseID);
        int updateOD_CS = QUOD_CS.executeUpdate();

        if (updateCS > 0 && updateCD_CS > 0 && updateOD_CS>0)
        {
            System.out.println("[UPDATED] Case status updated successfully...");
            db.con.commit();
            QUCS.close();
            QUCD_CS.close();
            QUOD_CS.close();
        }
        else
        {
            System.out.println("[FAILED] Case status couldn't be updated...");
            db.con.rollback();
            QUCS.cancel();
            QUCD_CS.cancel();
            QUOD_CS.cancel();
        }
    }

    void UpdateCriminalStatus(String Status, String CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UCri_Status = "update criminal_details set CriminalStatus=? where CaseID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(UCri_Status);
        QUCS.setString(1, Status);
        QUCS.setString(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        if (updateCS > 0)
        {
            System.out.println("[UPDATED] Criminal status updated successfully...");
            db.con.commit();
            QUCS.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal status couldn't be updated...");
            db.con.rollback();
            QUCS.cancel();
        }
    }

    void SetUpdateBail(String BailDate, String CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UBail_Date = "update criminal_details set BailDate=? where CaseID=?";
        PreparedStatement QUBD = db.getConnection().prepareStatement(UBail_Date);
        QUBD.setString(1, BailDate);
        QUBD.setString(2, CaseID);
        int updateBD = QUBD.executeUpdate();

        if (updateBD > 0)
        {
            System.out.println("[UPDATED] Bail date updated successfully...");
            db.con.commit();
            QUBD.close();
        }
        else
        {
            System.out.println("[FAILED] Bail date couldn't be updated...");
            db.con.rollback();
            QUBD.cancel();
        }
    }

    void UpdateReleaseDate(String ReleaseDate, String CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String URelease_Date = "update criminal_details set ReleaseDate=? where CaseID=?";
        PreparedStatement QURD = db.getConnection().prepareStatement(URelease_Date);
        QURD.setString(1, ReleaseDate);
        QURD.setString(2, CaseID);
        int updateRD = QURD.executeUpdate();

        if (updateRD > 0)
        {
            System.out.println("[UPDATED] Release date updated successfully...");
            db.con.commit();
            QURD.close();
        }
        else
        {
            System.out.println("[FAILED] Release date couldn't be updated...");
            db.con.rollback();
            QURD.cancel();
        }
    }

    public void AllCriminalRecord() throws Exception
    {
        String ACriRec = "select * from criminal_details";
        PreparedStatement QACRec = db.getConnection().prepareStatement(ACriRec);
        ResultSet rsACRec = QACRec.executeQuery();

        System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");
        System.out.printf("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Criminal ID", "Name", "Age", "Gender","Case ID", "Crime", "Crime Date","Judgement", "Status");
        System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");

        while (rsACRec.next())
        {
            System.out.println(String.format("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                rsACRec.getString("CriminalID"),
                rsACRec.getString("Name"),
                rsACRec.getInt("Age"),
                rsACRec.getString("Gender"),
                rsACRec.getString("CaseID"),
                rsACRec.getString("CrimeType"),
                rsACRec.getDate("CrimeDate"),
                rsACRec.getString("PunishmentType"),
                rsACRec.getString("CriminalStatus")));
        }

        System.out.println("Would you like to download Criminal Data as Text File???");
        char is_txt = sc.next().charAt(0);

        if(is_txt=='Y' || is_txt=='y')
        {
            ResultSet fileSet= QACRec.executeQuery();
            IOF.FetchCriminalData(fileSet);
            System.out.println("[INFO] Criminal Data Downloaded as Text File Successfully...");
            rsACRec.close();
            fileSet.close();
            QACRec.close();
        }
        else
        {
            System.out.println("[CANCELED] Criminal Data Download Cancelled...");
            rsACRec.close();
            QACRec.close();
            return;
        }
    }

    public void SearchCriminalRecord() throws Exception
    {
        System.out.println("Do you Know Anything Perticular Details about Criminal???(Yes / No)");
        System.out.print(">>> ");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            KSCriRec();
        }
        else if(ans=='N'||ans=='n')
        {
            UnkSCriRec();
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
        }
    }

    void KSCriRec() throws Exception
    {
        char ans;
        System.out.println("Which data do you know about Criminal???");
        System.out.println("[INFO] CriminalID,Name,Gender,CaseID,CrimeType,CaseStatus,PunishmentType are valid....");
        System.out.print(">>> ");
        String ColummName=sc.next();

        String TableName="criminal_details";

        System.out.print("Enter "+ColummName+" Details: ");

        Object Kdetails=OODQ.SQLDType2JDType(ColummName,TableName);

        String SearchCriRec="select *, count(*) over() as TResults from criminal_details where "+ColummName+" like ?";
        PreparedStatement QSCR=db.getConnection().prepareStatement(SearchCriRec);
        QSCR.setObject(1,"%"+Kdetails+"%");
        ResultSet CriData_rs=QSCR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;

        if(CriData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CriData_rs.getInt("TResults")+" |-----");
                CountResult=CriData_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Criminal Record Found in Database....");
                    QSCR.close();
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
                                                            "| Criminal ID: %-11s | Name: %-20s | Gender: %-8s | Case ID: %-11s | Crime Type: %-15s | Punishment: %-15s |",
                                                            CriData_rs.getString("CriminalID"),
                                                            CriData_rs.getString("Name"),
                                                            CriData_rs.getString("Gender"),
                                                            CriData_rs.getString("CaseID"),
                                                            CriData_rs.getString("CrimeType"),
                                                            CriData_rs.getString("PunishmentType")
                                                    )
                                    );
                        }while (CriData_rs.next());
                    }
                    else if (ans == 'N' || ans == 'n')
                    {
                        System.out.println("[INFO] Returning to Main Menu....");
                        QSCR.close();
                        return;
                    }
                    else
                    {
                        System.err.println("[INVALID] Give Answer only by Yes or No...");
                        QSCR.close();
                        return;
                    }

                }
                DTR=false;
            }

            System.out.print("Enter Criminal ID to see more details about that Criminal Record: ");
            String criID=sc.next();

            if(!found.isCriFound(criID))
            {
                System.err.println("[ERROR] Criminal ID not found....");
                return;
            }

            if(LSP.LoggedUserID=="")
            {
                if(LSP.userLogin())
                {
                    CriminalDetails(criID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                    return;
                }
            }
            else
            {
                CriminalDetails(criID);
            }
        }
    }

    void CriminalDetails(String criID) throws Exception
    {
        String PerSerCriRec="select * from criminal_details where CriminalID=?";
        PreparedStatement QPSCR=db.getConnection().prepareStatement(PerSerCriRec);
        QPSCR.setString(1,criID);
        ResultSet PerCriData_rs=QPSCR.executeQuery();
        PerCriData_rs.next();

        System.out.println("-----| Criminal Record Details |-----");
        System.out.println("Criminal ID: "+PerCriData_rs.getString("CriminalID"));
        System.out.println("Name: "+PerCriData_rs.getString("Name"));
        System.out.println("Age: "+PerCriData_rs.getInt("Age"));
        System.out.println("Gender: "+PerCriData_rs.getString("Gender"));
        System.out.println("Case ID: "+PerCriData_rs.getString("CaseID"));
        System.out.println("Crime Type: "+PerCriData_rs.getString("CrimeType"));
        System.out.println("Crime Date: "+PerCriData_rs.getDate("CrimeDate"));
        System.out.println("Officer ID: "+PerCriData_rs.getString("InvestingOfficerID"));
        System.out.println("Case Status: "+PerCriData_rs.getString("CaseStatus"));
        System.out.println("Punishment: "+PerCriData_rs.getString("PunishmentType"));
        System.out.println("Criminal Status: "+PerCriData_rs.getString("CriminalStatus"));
        System.out.println("Bail Date: "+PerCriData_rs.getDate("BailDate"));
        System.out.println("Release Date: "+PerCriData_rs.getDate("ReleaseDate"));
        QPSCR.close();

        System.out.println("Do you want to see Picture of Criminal???");
        System.out.print(">>> ");
        char isPic=sc.next().charAt(0);

        if (isPic=='Y'||isPic=='y')
        {
            //fetch criminal photo from database and display it....
        }
        else if(isPic=='N'||isPic=='n')
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

    void UnkSCriRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] Name,Gender,Age,CrimeType,PunishmentType are valid....");
        System.out.print("Enter Details Related about that Criminal or Crime: ");
        String details=sc.next();

        String UnkSCriRec="select *, count(*) over() as TResults from criminal_details where Name like ? or CrimeType like ? or PunishmentType like ? or Gender like ? or Age like ?";
        PreparedStatement QUkSCR=db.getConnection().prepareStatement(UnkSCriRec);
        QUkSCR.setString(1,"%"+details+"%");
        QUkSCR.setString(2,"%"+details+"%");
        QUkSCR.setString(3,"%"+details+"%");
        QUkSCR.setString(4,"%"+details+"%");
        QUkSCR.setString(5,"%"+details+"%");
        ResultSet CriData_rs=QUkSCR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        if(CriData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CriData_rs.getInt("TResults")+" |-----");
                CountResult=CriData_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Criminal Record Found in Database....");
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
                                                            "| Criminal ID: %-11s | Name: %-20s | Gender: %-8s | Case ID: %-11s | Crime Type: %-15s | Punishment: %-15s |",
                                                            CriData_rs.getString("CriminalID"),
                                                            CriData_rs.getString("Name"),
                                                            CriData_rs.getString("Gender"),
                                                            CriData_rs.getString("CaseID"),
                                                            CriData_rs.getString("CrimeType"),
                                                            CriData_rs.getString("PunishmentType")
                                                    )
                                    );

                        }while (CriData_rs.next());
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

            System.out.print("Enter Criminal ID to see more details about that Criminal Record: ");
            String criID=sc.next();

            if(!found.isCriFound(criID))
            {
                System.err.println("[ERROR] Criminal ID not found....");
                return;
            }

            if(LSP.LoggedUserID=="")
            {
                if(LSP.userLogin())
                {
                    CriminalDetails(criID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                    return;
                }
            }
            else
            {
                CriminalDetails(criID);
            }
        }
    }
}