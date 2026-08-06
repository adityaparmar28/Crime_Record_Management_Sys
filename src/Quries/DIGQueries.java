package Quries;

import DataBase.DataBase;
import DataStructure.IOFiles;
import Profile.Login_SignUpPage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class DIGQueries
{
    DataBase db = new DataBase();
    Scanner sc = new Scanner(System.in);
    OODataQueries OODQ=new OODataQueries();
    IOFiles IOF=new IOFiles();

    public void AddPoliceRecord() throws Exception
    {
        // Implementation for adding police record
        System.out.print("Enter Officer ID: ");
        int officerID = sc.nextInt();

        System.out.print("Enter Officer Name: ");
        String officerName = sc.next();

        System.out.print("Enter Officer DOB: ");
        String officerDOB = sc.next();

        System.out.print("Enter Gender:");
        String gender=sc.next();

        System.out.print("Enter Officer Rank:");
        String officerRank = sc.next();

        System.out.print("Enter Department: ");
        String dept=sc.next();

        System.out.print("Enter Officer Police Station ID: ");
        String PSID= sc.next();

        System.out.print("Enter Officer Joining Date: ");
        String Joining=sc.next();

        System.out.print("Enter Officer Status: ");
        String officerStatus = sc.next();


        String AddOfficer="insert into officer_details(Officer_ID,Name,DOB,Gender,Rank,Department,StationID,JoiningDate,OfficerStatus) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement QAddOff=db.getConnection().prepareStatement(AddOfficer);
        QAddOff.setInt(1,officerID);
        QAddOff.setString(2,officerName);
        QAddOff.setDate(3,java.sql.Date.valueOf(officerDOB));
        QAddOff.setString(4,gender);
        QAddOff.setString(5,officerRank);
        QAddOff.setString(6,dept);
        QAddOff.setString(7,PSID);
        QAddOff.setDate(8,java.sql.Date.valueOf(Joining));
        QAddOff.setString(9,officerStatus);
        int AOff=QAddOff.executeUpdate();

        if(AOff>0)
        {
            System.out.println("[UPDATED] Officer added successfully....");
            QAddOff.close();
        }
        else
        {
            System.out.println("[FAILED] Failed to add officer....");
            QAddOff.close();
        }

    }

    public void SearchOfficer() throws Exception
    {
        //logic pending.....
        System.out.println("Do you Know Anything Perticular Details about Officer???(yes/no)");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            KSOffRec();
        }
        else if(ans=='N'||ans=='n')
        {
            UnkSOffRec();
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
        }
    }

    void KSOffRec() throws Exception
    {
        char ans;
        System.out.println("Which data do you know about Officer???");
        System.out.println("[INFO] Officer_ID,Name,Gender,Rank,Department,OfficerStatus are Valid....");
        String ColummName=sc.next();

        //>>>if UpData column name found in case_details table then process ahead otherwise show error message that column not found in database....

        String TableName="officer_details";

        System.out.print("Enter "+ColummName+" Details: ");

        //input variable static public banana padega..!!
        Object Kdetails=OODQ.SQLDType2JDType(ColummName,TableName);

        String SearchOffRec="select *, count(*) as TResults from officer_details where "+ColummName+"=%?%";
        PreparedStatement QSOR=db.getConnection().prepareStatement(SearchOffRec);
        //QSOR.setString(1,ColummName);
        QSOR.setObject(1,Kdetails);
        ResultSet OffData_rs=QSOR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(OffData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + OffData_rs.getInt("TResults |-----"));
                CountResult=OffData_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Officer Record Found in Database....");
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
                                            "| Officer ID: %-5d | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                            OffData_rs.getInt("Officer_ID"),
                                            OffData_rs.getString("Name"),
                                            OffData_rs.getString("Gender"),
                                            OffData_rs.getString("Rank"),
                                            OffData_rs.getString("Department")
                                    )
                    );

            //>>>If Multiple result found then ask for which data do you want....
            OfficerDetails();
        }
    }

    void OfficerDetails() throws Exception
    {
        System.out.println("Enter Officer ID to see more details about that Officer Record: ");
        int offID=sc.nextInt();

        //>>>other Query....
        String PerSerOffRec="select * from officer_details where Officer_ID=?";
        PreparedStatement QPSOR=db.getConnection().prepareStatement(PerSerOffRec);
        QPSOR.setInt(1,offID);
        ResultSet PerOffData_rs=QPSOR.executeQuery();
        PerOffData_rs.next();

        System.out.println("-----------------------| Officer Record Details |--------------------------");
        System.out.println("Officer ID: "+PerOffData_rs.getInt("Officer_ID"));
        System.out.println("Name: "+PerOffData_rs.getString("Name"));
        System.out.println("DOB: "+PerOffData_rs.getDate("DOB"));
        System.out.println("Age: "+PerOffData_rs.getInt("Age"));
        System.out.println("Gender: "+PerOffData_rs.getString("Gender"));
        System.out.println("Rank: "+PerOffData_rs.getString("Rank"));
        System.out.println("Department: "+PerOffData_rs.getString("Department"));
        System.out.println("Station ID: "+PerOffData_rs.getInt("StationID"));
        System.out.println("Joining Date: "+PerOffData_rs.getDate("JoiningDate"));
        System.out.println("Officer Status: "+PerOffData_rs.getString("OfficerStatus"));
        System.out.println("Assigned Case: "+PerOffData_rs.getInt("Assigned_Case"));
        System.out.println("Case Status: "+PerOffData_rs.getString("CaseStatus"));
        System.out.println("---------------------------------------------------------------------------");

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

        System.out.println("Do you want to see Picture of Officer???");
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

    void UnkSOffRec() throws Exception
    {
        char ans;

        System.out.print("Enter Details Related about that Criminal or Crime: ");
        System.out.println("[INFO] Name,Gender,Rank,Department,OfficerStatus are Valid....");
        String details=sc.next();

        String UnkSOffRec="select *, count(*) as TResults from officer_details where Name like ? or Rank like ? or Gender like ? or Department like ? or OfficerStatus like ?";
        PreparedStatement QUkSOR=db.getConnection().prepareStatement(UnkSOffRec);
        QUkSOR.setString(1,"%"+details+"%");
        QUkSOR.setString(2,"%"+details+"%");
        QUkSOR.setString(3,"%"+details+"%");
        QUkSOR.setString(4,"%"+details+"%");
        QUkSOR.setString(5,"%"+details+"%");
        ResultSet OffData_rs=QUkSOR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        while(OffData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + OffData_rs.getInt("TResults |-----"));
                CountResult=OffData_rs.getInt("TResults");

                if(CountResult==0)
                {
                    System.out.println("[INFO] No Officer Record Found in Database....");
                    QUkSOR.close();
                    return;
                }
                else
                {
                    System.out.println("Would you like to see AlL Results???");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("[INFO] Displaying All Results....");
                        QUkSOR.close();
                    }
                    else if(ans=='N'||ans=='n')
                    {
                        System.out.println("[INFO] Returning to Main Menu....");
                        QUkSOR.close();
                        return;
                    }
                    else
                    {
                        System.err.println("[INVALID] Give Answer only by Yes or No...");
                        QUkSOR.close();
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
                                            "| Officer ID: %-5d | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                            OffData_rs.getInt("Officer_ID"),
                                            OffData_rs.getString("Name"),
                                            OffData_rs.getString("Gender"),
                                            OffData_rs.getString("Rank"),
                                            OffData_rs.getString("Department")
                                    )
                    );


            //>>>If Multiple result found then ask for which data do you want....
            OfficerDetails();
        }
    }

    public void AllPoliceOfficers() throws Exception
    {
        // Implementation for displaying all police officers
        String AllOfficers="select * from officer_details";
        Statement QAO=db.getConnection().createStatement();
        ResultSet rs=QAO.executeQuery(AllOfficers);

        System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
        System.out.printf("| %-4s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Officer ID", "Name", "DOB","Age", "Rank","Station ID", "Joining Date", "Status", "Assigned Case", "Case Status");
        System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");

        while(rs.next())
        {
            System.out.println(String.format("| %-10s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                    rs.getInt("Officer_ID"),
                    rs.getString("Name"),
                    rs.getDate("DOB"),
                    rs.getInt("Age"),
                    rs.getString("Rank"),
                    rs.getString("StationID"),
                    rs.getDate("JoiningDate"),
                    rs.getString("OfficerStatus"),
                    rs.getString("Assigned_Case"),
                    rs.getString("CaseStatus")));
        }

        System.out.println("Would you like to Download Police Officer Data as Text file???");
        char is_text=sc.next().charAt(0);

        if (is_text=='Y' || is_text=='y')
        {
            ResultSet fileSet=QAO.executeQuery(AllOfficers);
            //Text file creation method here....
            IOF.FetchOfficerData(fileSet);
            System.out.println("[INFO] Police Officer Data Downloaded Successfully as Text file....");
            rs.close();
            fileSet.close();
            QAO.close();
        }
        else
        {
            System.out.println("[CANCELLED] Police Officer Data Download Cancelled...");
            rs.close();
            QAO.close();
            return;
        }

    }

    public void UpdatePoliceRecord() throws Exception
    {
        // Implementation for updating police record
        System.out.print("Enter Officer ID you want to Update: ");
        int officerID = sc.nextInt();

        if(!isFound(officerID))
        {
            System.err.println("[NOT FOUND] Officer with ID " + officerID + " not found.");
            return;
        }

        System.out.println("Which Details do you want to Update?");
        System.out.println("1. Rank");
        System.out.println("2. Station ID");
        System.out.println("3. Officer Status");
        System.out.println("4. Assigned Case");
        System.out.println("5. Case Status");
        System.out.print("Enter your Choice: ");
        int UpdateOff_ch=sc.nextInt();

        switch (UpdateOff_ch)
        {
            case 1:
            {
                // Update Rank
                UpdateRank(officerID);
                break;
            }
            case 2:
            {
                // Update Station ID
                UpdateStationID(officerID);
                break;
            }
            case 3:
            {
                // Update Officer Status
                UpdateOfficerStatus(officerID);
                break;
            }
            case 4:
            {
                // Update Assigned Case
                UpdateAssignedCase(officerID);
                break;
            }
            case 5:
            {
                // Update Case Status
                UpdateCaseStatus(officerID);
                break;
            }
            default:
            {
                System.err.println("[INVALID] Invalid choice...");
                return;
            }
        }
    }

    boolean isFound(int OID) throws Exception
    {
        String FOID="select is_OfficerD(?)";
        PreparedStatement QFOID=db.getConnection().prepareStatement(FOID);
        QFOID.setInt(1,OID);
        ResultSet found=QFOID.executeQuery();
        found.next();
        QFOID.close();
        return found.getBoolean(1);
    }

    void UpdateRank(int OID) throws Exception
    {
        System.out.print("Enter Updating Rank: ");
        String URank=sc.next();

        String Update_Rank="update officer_details set Rank=? where Officer_ID=?";
        PreparedStatement QRank=db.getConnection().prepareStatement(Update_Rank);
        QRank.setString(1,URank);
        QRank.setInt(2,OID);
        int URankRes=QRank.executeUpdate();

        if(URankRes>0)
        {
            System.out.println("[UPDATED] Officer Rank Updated Successfully....");
            QRank.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Rank....");
            QRank.close();
        }
    }

    void UpdateStationID(int OID) throws Exception
    {
        System.out.print("Enter Updating Station ID: ");
        String UStationID = sc.next();

        String Update_StationID = "update officer_details set StationID=? where Officer_ID=?";
        PreparedStatement QUSID = db.getConnection().prepareStatement(Update_StationID);
        QUSID.setString(1, UStationID);
        QUSID.setInt(2, OID);
        int USIDRes = QUSID.executeUpdate();

        if (USIDRes > 0)
        {
            System.out.println("[UPDATED] Officer Station ID Updated Successfully....");
            QUSID.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Station ID....");
            QUSID.close();
        }
    }

    void UpdateOfficerStatus(int OID) throws Exception
    {
        System.out.print("Enter Updating Officer Status: ");
        String UOfficerStatus = sc.next();

        String Update_OfficerStatus = "update officer_details set OfficerStatus=? where Officer_ID=?";
        PreparedStatement QUOffS = db.getConnection().prepareStatement(Update_OfficerStatus);
        QUOffS.setString(1, UOfficerStatus);
        QUOffS.setInt(2, OID);
        int UOffSRes = QUOffS.executeUpdate();

        if (UOffSRes > 0)
        {
            System.out.println("[UPDATED] Officer Status Updated Successfully....");
            QUOffS.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Status....");
            QUOffS.close();
        }
    }

    void UpdateCaseStatus(int OID) throws Exception
    {
        System.out.print("Enter Updating Case Status: ");
        String UCaseStatus = sc.next();

        db.con.setAutoCommit(false);
        //>>>Update Case Status in Officer Details table....
        String Update_CaseStatus = "update officer_details set CaseStatus=? where Officer_ID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(Update_CaseStatus);
        QUCS.setString(1, UCaseStatus);
        QUCS.setInt(2, OID);
        int UCStatusRes = QUCS.executeUpdate();

        //>>>Automatically Update Case Status in Case Details Table....
        String UCaseD_CaseStatus="update case_details set Case_Status=? where OfficerID=?";
        PreparedStatement QUCaD_CaS=db.getConnection().prepareStatement(UCaseD_CaseStatus);
        QUCaD_CaS.setString(1,UCaseStatus);
        QUCaD_CaS.setInt(2,OID);
        int UCD_CSRes=QUCaD_CaS.executeUpdate();
        QUCaD_CaS.close();

        //>>>Automatically Update Case Status in Criminal Details table....
        String UCriD_CaSta="update criminal_details set Case_Status=? where InvestingOfficerID=?";
        PreparedStatement QUCriD_CaS=db.getConnection().prepareStatement(UCriD_CaSta);
        QUCriD_CaS.setString(1,UCaseStatus);
        QUCriD_CaS.setInt(2,OID);
        int UCriD_CSRes=QUCriD_CaS.executeUpdate();
        QUCriD_CaS.close();

        if (UCStatusRes > 0 && UCD_CSRes>0 && UCriD_CSRes>0)
        {
            System.out.println("[UPDATED] Case Status Updated Successfully....");
            db.con.commit();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Case Status....");
            db.con.rollback();
        }
    }

    void UpdateAssignedCase(int OID) throws Exception
    {
        System.out.print("Enter Updating Assigned Case ID: ");
        int UAssignedCase = sc.nextInt();

        //>>>Updating Assigned Case to Officer....
        db.con.setAutoCommit(false);

        String Update_AssignedCase = "update officer_details set Assigned_Case=? where Officer_ID=?";
        PreparedStatement QUAC = db.getConnection().prepareStatement(Update_AssignedCase);
        QUAC.setInt(1, UAssignedCase);
        QUAC.setInt(2, OID);
        int UACRes = QUAC.executeUpdate();

        //>>>Automatically Update Officer in Case_Details table....
        String UOffId_CaD="update case_details set OfficerID=? where CaseID=?";
        PreparedStatement QOidCaD=db.getConnection().prepareStatement(UOffId_CaD);
        QOidCaD.setInt(1,OID);
        QOidCaD.setInt(2,UAssignedCase);
        int UOIDRes=QOidCaD.executeUpdate();
        QOidCaD.close();

        //>>>Automatically Update Officer in Criminal_details table....
        String UOffId_CriD="update criminal_details set InvestingOfficerID=? where CaseID=?";
        PreparedStatement QOidCriD=db.getConnection().prepareStatement(UOffId_CriD);
        QOidCriD.setInt(1,OID);
        QOidCriD.setInt(2,UAssignedCase);
        int UOIDRes2=QOidCriD.executeUpdate();
        QOidCriD.close();

        if (UACRes > 0 && UOIDRes>0 && UOIDRes2>0)
        {
            System.out.println("[UPDATED] Assigned Case Updated Successfully....");
            db.con.commit();
            QOidCriD.close();
            QOidCaD.close();
            QUAC.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Assigned Case....");
            db.con.rollback();
            QUAC.close();
            QOidCaD.close();
            QUAC.close();
        }
    }
}