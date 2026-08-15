package Quries;

import DataBase.DataFound;
import DataBase.Database;
import DataBase.Validation;
import DataStructure.IOFiles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class DIGQueries
{
    Database db = new Database();
    Scanner sc = new Scanner(System.in);
    OODataQueries OODQ=new OODataQueries();
    IOFiles IOF=new IOFiles();
    Validation v=new Validation();
    DataFound found=new DataFound();

    public void AddPoliceRecord() throws Exception
    {
        String officerID = v.readNonEmptyString("Enter Officer ID: ");

        String officerName =v.readNonEmptyString("Enter Officer Name: ");

        String officerDOB =v.Date("Officer DOB");

        String Gender="";

        try
        {
            Gender = v.readNonEmptyString("Enter Gender: ");

            if(!(Gender.equalsIgnoreCase("Female") || Gender.equalsIgnoreCase("Male")))
            {
                System.err.println("[INVALID] Invalid Gender....");
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid input....");
        }

        String officerRank = v.readNonEmptyString("Enter Officer Rank: ");

        String dept=v.readNonEmptyString("Enter Department: ");

        String PSID= v.readNonEmptyString("Enter Officer Police Station ID: ");

        String Joining=v.Date("Officer Joining Date");

        boolean OffSLoop=false;
        String officerStatus="ACTIVE";

        while (!OffSLoop)
        {
             officerStatus= v.readNonEmptyString("Enter Officer Status: ");
            officerStatus = officerStatus.toUpperCase();

            if (!(officerStatus.matches("ACTIVE") || officerStatus.matches("INACTIVE") || officerStatus.matches("SUSPENDED"))) {
                System.err.println("[INVALID] Invalid Officer Status....");
            }
            else
            {
                OffSLoop=true;
            }
        }

        db.con.setAutoCommit(false);

        String AddOfficer="insert into officer_details(OfficerID,Name,DOB,Age,Gender,Rank,Department,StationID,JoiningDate,OfficerStatus) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement QAddOff=db.getConnection().prepareStatement(AddOfficer);
        QAddOff.setString(1,officerID);
        QAddOff.setString(2,officerName);
        QAddOff.setDate(3,java.sql.Date.valueOf(officerDOB));
        
        // Calculate age from DOB
        int dobYear = Integer.parseInt(officerDOB.substring(0, 4));
        int currentYear = 2026;
        int age = currentYear - dobYear;
        
        QAddOff.setInt(4,age);
        QAddOff.setString(5,Gender);
        QAddOff.setString(6,officerRank);
        QAddOff.setString(7,dept);
        QAddOff.setString(8,PSID);
        QAddOff.setDate(9,java.sql.Date.valueOf(Joining));
        QAddOff.setString(10,officerStatus);
        int AOff=QAddOff.executeUpdate();

        if(AOff>0)
        {
            System.out.println("[UPDATED] Officer added successfully....");
            db.con.commit();
            QAddOff.close();
        }
        else
        {
            System.out.println("[FAILED] Failed to add officer....");
            db.con.rollback();
            QAddOff.cancel();
        }

    }

    public void SearchOfficer() throws Exception
    {
        System.out.println("Do you Know Anything Perticular Details about Officer???(yes/no)");
        System.out.print(">>> ");
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
        System.out.println("[INFO] OfficerID,Name,Gender,Rank,Department,OfficerStatus are Valid....");
        System.out.print(">>> ");
        String ColummName=sc.next();

        String TableName="officer_details";

        System.out.print("Enter "+ColummName+" Details: ");

        Object Kdetails=OODQ.SQLDType2JDType(ColummName,TableName);

        String SearchOffRec="select *, count(*) over() as TResults from officer_details where "+ColummName+" like ?";
        PreparedStatement QSOR=db.getConnection().prepareStatement(SearchOffRec);
        QSOR.setObject(1,"%"+Kdetails+"%");
        ResultSet OffData_rs=QSOR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        if(OffData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + OffData_rs.getInt("TResults")+" |-----");
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
                                                            "| Officer ID: %-11s | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                                            OffData_rs.getString("OfficerID"),
                                                            OffData_rs.getString("Name"),
                                                            OffData_rs.getString("Gender"),
                                                            OffData_rs.getString("Rank"),
                                                            OffData_rs.getString("Department")
                                                    )
                                    );

                        }while (OffData_rs.next());

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

            OfficerDetails();
        }
    }

    void OfficerDetails() throws Exception
    {
        System.out.print("Enter Officer ID to see more details about that Officer Record: ");
        String offID=sc.next();

        if(!found.isOffFound(offID))
        {
            System.err.println("[ERROR] Officer Not Found....");
            return;
        }

        String PerSerOffRec="select * from officer_details where OfficerID=?";
        PreparedStatement QPSOR=db.getConnection().prepareStatement(PerSerOffRec);
        QPSOR.setString(1,offID);
        ResultSet PerOffData_rs=QPSOR.executeQuery();
        PerOffData_rs.next();

        System.out.println("-----------------------| Officer Record Details |--------------------------");
        System.out.println("Officer ID: "+PerOffData_rs.getString("OfficerID"));
        System.out.println("Name: "+PerOffData_rs.getString("Name"));
        System.out.println("DOB: "+PerOffData_rs.getDate("DOB"));
        System.out.println("Age: "+PerOffData_rs.getInt("Age"));
        System.out.println("Gender: "+PerOffData_rs.getString("Gender"));
        System.out.println("Rank: "+PerOffData_rs.getString("Rank"));
        System.out.println("Department: "+PerOffData_rs.getString("Department"));
        System.out.println("Station ID: "+PerOffData_rs.getString("StationID"));
        System.out.println("Joining Date: "+PerOffData_rs.getDate("JoiningDate"));
        System.out.println("Officer Status: "+PerOffData_rs.getString("OfficerStatus"));
        System.out.println("Assigned Case: "+PerOffData_rs.getString("AssignedCase"));
        System.out.println("Case Status: "+PerOffData_rs.getString("CaseStatus"));
        System.out.println("---------------------------------------------------------------------------");
        QPSOR.close();
    }

    void UnkSOffRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] Name,Gender,Rank,Department,OfficerStatus are Valid....");
        System.out.println("Enter Details Related about that Criminal or Crime: ");
        System.out.print(">>> ");
        String details=sc.next();

        String UnkSOffRec="select *, count(*) over() as TResults from officer_details where Name like ? or Rank like ? or Gender like ? or Department like ? or OfficerStatus like ?";
        PreparedStatement QUkSOR=db.getConnection().prepareStatement(UnkSOffRec);
        QUkSOR.setString(1,"%"+details+"%");
        QUkSOR.setString(2,"%"+details+"%");
        QUkSOR.setString(3,"%"+details+"%");
        QUkSOR.setString(4,"%"+details+"%");
        QUkSOR.setString(5,"%"+details+"%");
        ResultSet OffData_rs=QUkSOR.executeQuery();

        boolean DTR=true;//Display total results....
        int CountResult=0;
        if(OffData_rs.next())
        {
            if(DTR)
            {
                System.out.println("-----| Total Results Found: " + OffData_rs.getInt("TResults")+" |-----");
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
                                                            "| Officer ID: %-11s | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                                            OffData_rs.getString("OfficerID"),
                                                            OffData_rs.getString("Name"),
                                                            OffData_rs.getString("Gender"),
                                                            OffData_rs.getString("Rank"),
                                                            OffData_rs.getString("Department")
                                                    )
                                    );

                        }while (OffData_rs.next());

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
            }

            OfficerDetails();
        }
    }

    public void AllPoliceOfficers() throws Exception
    {
        String AllOfficers="select * from officer_details";
        Statement QAO=db.getConnection().createStatement();
        ResultSet rs=QAO.executeQuery(AllOfficers);

        System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
        System.out.printf("| %-11s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Officer ID", "Name", "DOB","Age", "Rank","Station ID", "Joining Date", "Status", "Assigned Case", "Case Status");
        System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");

        while(rs.next())
        {
            System.out.println(String.format("| %-11s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                    rs.getString("OfficerID"),
                    rs.getString("Name"),
                    rs.getDate("DOB"),
                    rs.getInt("Age"),
                    rs.getString("Rank"),
                    rs.getString("StationID"),
                    rs.getDate("JoiningDate"),
                    rs.getString("OfficerStatus"),
                    rs.getString("AssignedCase"),
                    rs.getString("CaseStatus")));
        }

        System.out.println("Would you like to Download Police Officer Data as Text file???");
        System.out.print(">>> ");
        char is_text=sc.next().charAt(0);

        if (is_text=='Y' || is_text=='y')
        {
            ResultSet fileSet=QAO.executeQuery(AllOfficers);
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
        System.out.print("Enter Officer ID you want to Update: ");
        String officerID = sc.next();

        if(!found.isOffFound(officerID))
        {
            System.err.println("[NOT FOUND] Officer with ID " + officerID + " not found.");
            return;
        }

        System.out.println("Which Details do you want to Update?");
        System.out.println("| 1. Rank");
        System.out.println("| 2. Station ID");
        System.out.println("| 3. Officer Status");
        System.out.println("| 4. Assigned Case");
        System.out.println("| 5. Case Status");
        System.out.println("| 0. Cancel....");
        System.out.print("| Enter your Choice: ");
        int UpdateOff_ch=sc.nextInt();

        switch (UpdateOff_ch)
        {
            case 0:
            {
                System.out.println("[CANCELLED] Police Record Updation Cancelled....");
                break;
            }

            case 1:
            {
                UpdateRank(officerID);
                break;
            }
            case 2:
            {
                UpdateStationID(officerID);
                break;
            }
            case 3:
            {
                UpdateOfficerStatus(officerID);
                break;
            }
            case 4:
            {
                UpdateAssignedCase(officerID);
                break;
            }
            case 5:
            {
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

    void UpdateRank(String OID) throws Exception
    {
        System.out.print("Enter Updating Rank: ");
        String URank=sc.next();

        String Update_Rank="update officer_details set Rank=? where OfficerID=?";
        PreparedStatement QRank=db.getConnection().prepareStatement(Update_Rank);
        QRank.setString(1,URank);
        QRank.setString(2,OID);
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

    void UpdateStationID(String OID) throws Exception
    {
        System.out.print("Enter Updating Station ID: ");
        String UStationID = sc.next();

        db.con.setAutoCommit(false);

        String Update_StationID = "update officer_details set StationID=? where OfficerID=?";
        PreparedStatement QUSID = db.getConnection().prepareStatement(Update_StationID);
        QUSID.setString(1, UStationID);
        QUSID.setString(2, OID);
        int USIDRes = QUSID.executeUpdate();

        if (USIDRes > 0)
        {
            System.out.println("[UPDATED] Officer Station ID Updated Successfully....");
            db.con.commit();
            QUSID.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Station ID....");
            db.con.rollback();
            QUSID.close();
        }
    }

    void UpdateOfficerStatus(String OID) throws Exception
    {
        boolean OffSLoop=false;
        String UOfficerStatus="ACTIVE";

        while (!OffSLoop)
        {
            UOfficerStatus= v.readNonEmptyString("Enter Updating Officer Status: ");
            UOfficerStatus = UOfficerStatus.toUpperCase();

            if (!(UOfficerStatus.matches("ACTIVE") || UOfficerStatus.matches("INACTIVE") || UOfficerStatus.matches("SUSPENDED")))
            {
                System.err.println("[INVALID] Invalid Officer Status....");
            }
            else
            {
                OffSLoop=true;
            }
        }

        db.con.setAutoCommit(false);

        String Update_OfficerStatus = "update officer_details set OfficerStatus=? where OfficerID=?";
        PreparedStatement QUOffS = db.getConnection().prepareStatement(Update_OfficerStatus);
        QUOffS.setString(1, UOfficerStatus);
        QUOffS.setString(2, OID);
        int UOffSRes = QUOffS.executeUpdate();

        if (UOffSRes > 0)
        {
            System.out.println("[UPDATED] Officer Status Updated Successfully....");
            db.con.commit();
            QUOffS.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Status....");
            db.con.rollback();
            QUOffS.cancel();
        }
    }

    void UpdateCaseStatus(String OID) throws Exception
    {
        String UCaseStatus = "";
        boolean CaseSLoop=false;

        while (!CaseSLoop)
        {
            UCaseStatus = v.readAlphaString("Enter Case Status: ");
            UCaseStatus = UCaseStatus.toUpperCase();

            if (!(UCaseStatus.matches("PENDING") || UCaseStatus.matches("INVESTIGATING") || UCaseStatus.matches("SOLVED")))
            {
                System.err.println("[INVALID] Invalid Case Status.....");
            }
            else
            {
                CaseSLoop=true;
            }
        }

        db.con.setAutoCommit(false);

        String Update_CaseStatus = "update officer_details set CaseStatus=? where OfficerID=?";
        PreparedStatement QUCS = db.getConnection().prepareStatement(Update_CaseStatus);
        QUCS.setString(1, UCaseStatus);
        QUCS.setString(2, OID);
        int UCStatusRes = QUCS.executeUpdate();

        String UCaseD_CaseStatus="update case_details set CaseStatus=? where OfficerID=?";
        PreparedStatement QUCaD_CaS=db.getConnection().prepareStatement(UCaseD_CaseStatus);
        QUCaD_CaS.setString(1,UCaseStatus);
        QUCaD_CaS.setString(2,OID);
        int UCD_CSRes=QUCaD_CaS.executeUpdate();

        String UCriD_CaSta="update criminal_details set CaseStatus=? where InvestingOfficerID=?";
        PreparedStatement QUCriD_CaS=db.getConnection().prepareStatement(UCriD_CaSta);
        QUCriD_CaS.setString(1,UCaseStatus);
        QUCriD_CaS.setString(2,OID);
        int UCriD_CSRes=QUCriD_CaS.executeUpdate();

        if (UCStatusRes > 0 && UCD_CSRes>0 && UCriD_CSRes>0)
        {
            System.out.println("[UPDATED] Case Status Updated Successfully....");
            db.con.commit();
            QUCS.close();
            QUCaD_CaS.close();
            QUCriD_CaS.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Case Status....");
            db.con.rollback();
            QUCS.cancel();
            QUCriD_CaS.cancel();
            QUCaD_CaS.cancel();
        }
    }

    void UpdateAssignedCase(String OID) throws Exception
    {
        String UAssignedCase =v.readNonEmptyString("Enter Updating Assigned Case ID: ");

        db.con.setAutoCommit(false);

        String Update_AssignedCase = "update officer_details set AssignedCase=? where OfficerID=?";
        PreparedStatement QUAC = db.getConnection().prepareStatement(Update_AssignedCase);
        QUAC.setString(1, UAssignedCase);
        QUAC.setString(2, OID);
        int UACRes = QUAC.executeUpdate();

        String UOffId_CaD="update case_details set OfficerID=? where CaseID=?";
        PreparedStatement QOidCaD=db.getConnection().prepareStatement(UOffId_CaD);
        QOidCaD.setString(1,OID);
        QOidCaD.setString(2,UAssignedCase);
        int UOIDRes=QOidCaD.executeUpdate();

        String UOffId_CriD="update criminal_details set InvestingOfficerID=? where CaseID=?";
        PreparedStatement QOidCriD=db.getConnection().prepareStatement(UOffId_CriD);
        QOidCriD.setString(1,OID);
        QOidCriD.setString(2,UAssignedCase);
        int UOIDRes2=QOidCriD.executeUpdate();

        if (UACRes > 0 && UOIDRes>0 && UOIDRes2>0)
        {
            System.out.println("[UPDATED] Assigned Case Updated Successfully....");
            db.con.commit();
            QUAC.close();
            QOidCaD.close();
            QOidCriD.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Assigned Case....");
            db.con.rollback();
            QUAC.cancel();
            QOidCaD.cancel();
            QOidCriD.cancel();
        }
    }
}