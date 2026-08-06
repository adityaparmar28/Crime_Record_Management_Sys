package Quries;

import DataBase.DataBase;
import DataStructure.IOFiles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CRMngrQueries
{
    DataBase db = new DataBase();
    Scanner sc = new Scanner(System.in);
    OODataQueries OODQ=new OODataQueries();
    IOFiles IOF=new IOFiles();

    public void AddCriminalRQuery() throws Exception
    {
        int C_Id=0;
        String C_Name="";
        int C_Age=0;
        String C_Gender="";
        int C_CaseID=0;
        String C_CrimeType="";
        String C_CrimeDate="";
        int C_IOffID=0;
        String C_CaseStatus="";

        try
        {
            System.out.print("Enter Criminal ID: ");
            C_Id = sc.nextInt();
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for Criminal ID.");
        }

        try
        {
            System.out.print("Enter Criminal Name: ");
            C_Name = sc.next();

            if(C_Name.isBlank())
            {
                System.out.println("[ERROR] Name cannot be empty.");
                return;
            }

            if(!C_Name.matches("[A-Za-z ]{3,50}"))
            {
                System.out.println("[ERROR] Invalid Name.");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid string for Criminal Name.");
        }

        try
        {
            System.out.print("Enter Criminal Age: ");
            C_Age = sc.nextInt();
            if(C_Age<10)
            {
                System.err.println("[INVALID] Invalid Age for Criminal....");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for Criminal Age.");
        }

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
            System.err.println("[ERROR] Invalid input. Please enter a valid string for Criminal Gender.");
        }

        try
        {
            System.out.print("Enter Case ID: ");
            C_CaseID = sc.nextInt();
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for Case ID.");
        }

        try
        {
            System.out.print("Enter Crime Type: ");
            C_CrimeType = sc.next();

            if(C_CrimeType.isBlank())
            {
                System.out.println("[ERROR] Name cannot be empty.");
                return;
            }

            if(!C_CrimeType.matches("[A-Za-z ]{3,50}"))
            {
                System.out.println("[ERROR] Invalid Name.");
                return;
            }

        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid string for Crime Type.");
        }

        try
        {
            System.out.print("Enter Crime Date: ");
            C_CrimeDate = sc.next();
            //dob valid check....
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid string for Crime Date.");
        }

        try
        {
            System.out.print("Enter Investigating Officer ID: ");
            C_IOffID = sc.nextInt();
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for Investigating Officer ID.");
        }

        try
        {
            System.out.print("Enter Case Status: ");
            C_CaseStatus = sc.next();

            if(C_CaseStatus.isBlank())
            {
                System.out.println("[ERROR] Name cannot be empty.");
                return;
            }

            if(!C_CaseStatus.matches("[A-Za-z ]{3,50}"))
            {
                System.out.println("[ERROR] Invalid Name.");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid string for Case Status.");
        }

        String addCRQ = "insert into criminal_details(Criminal_ID,Name,Age,Gender,CaseID,Crime_Type,Crime_Date,InvestingOfficerID,Case_Status) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement addCR = db.getConnection().prepareStatement(addCRQ);
        addCR.setInt(1,C_Id);
        addCR.setString(2, C_Name);
        addCR.setInt(3, C_Age);
        addCR.setString(4, C_Gender);
        addCR.setInt(5, C_CaseID);
        addCR.setString(6, C_CrimeType);
        addCR.setDate(7, java.sql.Date.valueOf(C_CrimeDate));
        addCR.setInt(8, C_IOffID);
        addCR.setString(9, C_CaseStatus);
        int update = addCR.executeUpdate();

        if (update > 0)
        {
            System.out.println("[UPDATED] Criminal details added successfully...");
            addCR.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Added...");
            addCR.close();
        }
    }

    public void UpdateCriminalRQuery() throws Exception
    {
        int C_CaseID=0;
        try
        {
            System.out.print("Enter Case ID to Update: ");
            C_CaseID = sc.nextInt();
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for Case ID.");
        }

        try
        {
            System.out.println("Which Details do you want to Update?");
            System.out.println("1. Investingating Officer ID.");
            System.out.println("2. Case Status.");
            System.out.println("3. Criminal Status.");
            System.out.println("4. Bail Date.");
            System.out.println("5. Release Date.");
            System.out.println();
            System.out.print("Enter Your Choice: ");
            int updateCri_ch = sc.nextInt();

            switch (updateCri_ch)
            {
                case 1:
                {
                    System.out.print("Enter New Investigating Officer ID: ");
                    String C_IOffID = sc.next();
                    UpdateIOffID(C_IOffID, C_CaseID);

                    break;
                }

                case 2:
                {
                    System.out.print("Enter Updating Case Status: ");
                    String C_CaseStatus = sc.next();
                    UpdateCaseStatus(C_CaseStatus, C_CaseID);

                    break;
                }

                case 3:
                {
                    System.out.print("Enter Updating Criminal Status: ");
                    String C_CriminalStatus = sc.next();
                    UpdateCriminalStatus(C_CriminalStatus, C_CaseID);

                    break;
                }

                case 4:
                {
                    System.out.print("Enter Updating Bail Date(DDMMYYYY): ");
                    String C_BailDate = sc.next();
                    SetUpdateBail(C_BailDate, C_CaseID);

                    break;
                }

                case 5:
                {
                    System.out.print("Enter Updating Release Date(DDMMYYYY): ");
                    String C_ReleaseDate = sc.next();
                    UpdateReleaseDate(C_ReleaseDate, C_CaseID);

                    break;
                }

                default: {
                    System.out.println("[ERROR] Invalid number format. Please enter a valid choice....");
                }
            }
        }catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input. Please enter a valid integer for your choice.");
        }
    }

    void UpdateIOffID(String oid, int CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UCri_IOID = "update criminal_details set InvestingOfficerID=? where CaseID=?";
        PreparedStatement QUCrIOID = db.getConnection().prepareStatement(UCri_IOID);
        QUCrIOID.setString(1, oid);
        QUCrIOID.setInt(2, CaseID);
        int updateCIO = QUCrIOID.executeUpdate();

        String UOf_CaseID = "update officer_details set CaseID=? where InvestingOfficerID=?";
        PreparedStatement QUOfCID = db.getConnection().prepareStatement(UOf_CaseID);
        QUOfCID.setInt(1, CaseID);
        QUOfCID.setString(2, oid);
        int updateIO_cid = QUOfCID.executeUpdate();

        if (updateCIO > 0 && updateIO_cid > 0)
        {
            System.out.println("[UPDATED] Criminal details updated successfully...");
            db.con.commit();
            QUCrIOID.close();
            QUOfCID.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Updated...");
            db.con.rollback();
            QUCrIOID.close();
            QUOfCID.close();

        }
    }

    void UpdateCaseStatus(String Status, int CaseID) throws Exception
    {
        db.con.setAutoCommit(false);

        String UCase_Status = "update criminal_details set Case_Status=? where CaseID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(UCase_Status);
        QUCS.setString(1, Status);
        QUCS.setInt(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        String UCD_CaseStatus = "update case_details set Case_Status=? where CaseID=?";
        PreparedStatement QUCD_CS = db.getConnection().prepareStatement(UCD_CaseStatus);
        QUCD_CS.setString(1, Status);
        QUCD_CS.setInt(2, CaseID);
        int updateCD_CS = QUCD_CS.executeUpdate();

        if (updateCS > 0 && updateCD_CS > 0)
        {
            System.out.println("[UPDATED] Case status updated successfully...");
            db.con.commit();
            QUCS.close();
            QUCD_CS.close();
        }
        else
        {
            System.out.println("[FAILED] Case status couldn't be updated...");
            db.con.rollback();
            QUCS.close();
            QUCD_CS.close();
        }
    }

    void UpdateCriminalStatus(String Status, int CaseID) throws Exception
    {
        String UCri_Status = "update criminal_details set Criminal_Status=? where CaseID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(UCri_Status);
        QUCS.setString(1, Status);
        QUCS.setInt(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        if (updateCS > 0)
        {
            System.out.println("[UPDATED] Criminal status updated successfully...");
            QUCS.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal status couldn't be updated...");
            QUCS.close();
        }
    }

    void SetUpdateBail(String BailDate, int CaseID) throws Exception
    {
        String UBail_Date = "update criminal_details set Bail_Date=? where CaseID=?";
        PreparedStatement QUBD = db.getConnection().prepareStatement(UBail_Date);
        QUBD.setString(1, BailDate);
        QUBD.setInt(2, CaseID);
        int updateBD = QUBD.executeUpdate();

        if (updateBD > 0)
        {
            System.out.println("[UPDATED] Bail date updated successfully...");
            QUBD.close();
        }
        else
        {
            System.out.println("[FAILED] Bail date couldn't be updated...");
            QUBD.close();
        }
    }

    void UpdateReleaseDate(String ReleaseDate, int CaseID) throws Exception
    {
        String URelease_Date = "update criminal_details set Release_Date=? where CaseID=?";
        PreparedStatement QURD = db.getConnection().prepareStatement(URelease_Date);
        QURD.setString(1, ReleaseDate);
        QURD.setInt(2, CaseID);
        int updateRD = QURD.executeUpdate();

        if (updateRD > 0)
        {
            System.out.println("[UPDATED] Release date updated successfully...");
            QURD.close();
        }
        else
        {
            System.out.println("[FAILED] Release date couldn't be updated...");
            QURD.close();
        }
    }

    public void AllCriminalRecord() throws Exception
    {
        String ACriRec = "select * from criminal_details";
        PreparedStatement QACRec = db.getConnection().prepareStatement(ACriRec);
        ResultSet rsACRec = QACRec.executeQuery();



        System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");
        System.out.printf("| %-4s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Criminal ID", "Name", "Age", "Gender","Case ID", "Crime", "Crime Date","Judgement", "Status");
        System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");

        while (rsACRec.next())
        {
            System.out.println(String.format("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                rsACRec.getInt("Criminal_ID"),
                rsACRec.getString("Name"),
                rsACRec.getInt("Age"),
                rsACRec.getString("Gender"),
                rsACRec.getInt("CaseID"),
                rsACRec.getString("Crime_Type"),
                rsACRec.getDate("Crime_Date"),
                rsACRec.getString("Punishment_Type"),
                rsACRec.getString("Criminal_Status")));
        }

        System.out.println("Would you like to download Criminal Data as Text File???");
        char is_txt = sc.next().charAt(0);

        if(is_txt=='Y' || is_txt=='y')
        {
            //login first...
            //>>>method pending....
            //Text file creation method here....
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
        System.out.println("[INFO] Criminal_ID,Name,Gender,CaseID,Crime_Type,Case_Status,Punishment_Type are valid....");
        String ColummName=sc.next();

        //>>>if UpData column name found in case_details table then process ahead otherwise show error message that column not found in database....

        String TableName="criminal_details";

        System.out.print("Enter "+ColummName+" Details: ");

        //input variable static public banana padega..!!
        Object Kdetails=OODQ.SQLDType2JDType(ColummName,TableName);

        String SearchCriRec="select *, count(*) as TResults from criminal_details where "+ColummName+" like ?";
        PreparedStatement QSCR=db.getConnection().prepareStatement(SearchCriRec);
        //QSCR.setString(1,ColummName);
        QSCR.setObject(1,Kdetails);
        ResultSet CriData_rs=QSCR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(CriData_rs.next())
        {
            if(DTR)
            {
                /*System.out.println("-----| Total Results Found: " + CriData_rs.getInt("TResults |-----"));
                CountResult=CriData_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Criminal Record Found in Database....");
                    QSCR.close();
                    return;
                }*/
                //else
                //{
                    System.out.println("Would you like to see AlL Results???");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                    }
                    else if(ans=='N'||ans=='n')
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


                DTR=false;
            }

            //show all results as log type normal....
            System.out.println
                    (
                            String.format
                                    (
                                            "| Criminal ID: %-5d | Name: %-20s | Gender: %-8s | Case ID: %-5d | Crime Type: %-15s | Punishment: %-15s |",
                                            CriData_rs.getInt("Criminal_ID"),
                                            CriData_rs.getString("Name"),
                                            CriData_rs.getString("Gender"),
                                            CriData_rs.getInt("CaseID"),
                                            CriData_rs.getString("Crime_Type"),
                                            CriData_rs.getString("Punishment_Type")
                                    )
                    );


            //>>>If Multiple result found then ask for which data do you want....
            CriminalDetails();
        }
    }

    void CriminalDetails() throws Exception
    {
        System.out.println("Enter Criminal ID to see more details about that Criminal Record: ");
        int criID=sc.nextInt();

        //>>>other Query....
        String PerSerCriRec="select * from criminal_details where Criminal_ID=?";
        PreparedStatement QPSCR=db.getConnection().prepareStatement(PerSerCriRec);
        QPSCR.setInt(1,criID);
        ResultSet PerCriData_rs=QPSCR.executeQuery();
        PerCriData_rs.next();

        System.out.println("-----| Criminal Record Details |-----");
        System.out.println("Criminal ID: "+PerCriData_rs.getInt("Criminal_ID"));
        System.out.println("Name: "+PerCriData_rs.getString("Name"));
        System.out.println("Age: "+PerCriData_rs.getInt("Age"));
        System.out.println("Gender: "+PerCriData_rs.getString("Gender"));
        System.out.println("Case ID: "+PerCriData_rs.getInt("CaseID"));
        System.out.println("Crime Type: "+PerCriData_rs.getString("Crime_Type"));
        System.out.println("Crime Date: "+PerCriData_rs.getDate("Crime_Date"));
        System.out.println("Officer ID: "+PerCriData_rs.getInt("InvestingOfficerID"));
        System.out.println("Case Status: "+PerCriData_rs.getString("Case_Status"));
        System.out.println("Punishment: "+PerCriData_rs.getString("Punishment_Type"));
        System.out.println("Criminal Status: "+PerCriData_rs.getString("Criminal_Status"));
        System.out.println("Bail Date: "+PerCriData_rs.getDate("Bail_Date"));
        System.out.println("Release Date: "+PerCriData_rs.getDate("Release_Date"));
        QPSCR.close();

        //>>>
            /*System.out.println
                    (
                            String.format
                                    (
                                        "| Criminal ID: %-5d | Name: %-20s | Age: %-3d | Gender: %-8s | Case ID: %-5d | Crime Type: %-15s | Crime Date: %-10s | Officer ID: %-5d | Case Status: %-15s | Punishment: %-15s | Criminal Status: %-15s | Bail Date: %-10s | Release Date: %-10s |",
                                        PerCriData_rs.getInt("Criminal_ID"),
                                        PerCriData_rs.getString("Name"),
                                        PerCriData_rs.getInt("Age"),
                                        PerCriData_rs.getString("Gender"),
                                        PerCriData_rs.getInt("CaseID"),
                                        PerCriData_rs.getString("Crime_Type"),
                                        PerCriData_rs.getDate("Crime_Date"),
                                        PerCriData_rs.getInt("InvestingOfficerID"),
                                        PerCriData_rs.getString("Case_Status"),
                                        PerCriData_rs.getString("Punishment_Type"),
                                        PerCriData_rs.getString("Criminal_Status"),
                                        PerCriData_rs.getDate("Bail_Date"),
                                        PerCriData_rs.getDate("Release_Date")
                                    )
                    )*/

        System.out.println("Do you want to see Picture of Criminal???");
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

        System.out.print("Enter Details Related about that Criminal or Crime: ");
        System.out.println("[INFO] Name,Gender,Age,Crime_Type,Punishment_Type are valid....");
        String details=sc.next();

        String UnkSCriRec="select *, count(*) as TResults from criminal_details where Name like ? or Crime_Type like ? or Punishment_Type like ? or Gender like ? or Age like ?";
        PreparedStatement QUkSCR=db.getConnection().prepareStatement(UnkSCriRec);
        QUkSCR.setString(1,"%"+details+"%");
        QUkSCR.setString(2,"%"+details+"%");
        QUkSCR.setString(3,"%"+details+"%");
        QUkSCR.setString(4,"%"+details+"%");
        QUkSCR.setString(5,"%"+details+"%");
        ResultSet CriData_rs=QUkSCR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(CriData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + CriData_rs.getInt("TResults |-----"));
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
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
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
                                            "| Criminal ID: %-5d | Name: %-20s | Gender: %-8s | Case ID: %-5d | Crime Type: %-15s | Punishment: %-15s |",
                                            CriData_rs.getInt("Criminal_ID"),
                                            CriData_rs.getString("Name"),
                                            CriData_rs.getString("Gender"),
                                            CriData_rs.getInt("CaseID"),
                                            CriData_rs.getString("Crime_Type"),
                                            CriData_rs.getString("Punishment_Type")
                                    )
                    );


            //>>>If Multiple result found then ask for which data do you want....
            CriminalDetails();
        }
    }
}