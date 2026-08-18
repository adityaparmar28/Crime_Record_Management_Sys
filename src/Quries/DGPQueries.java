/*
 * Copyright 2026 MR. ADITYA PARMAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Quries;

import DataBase.DataFound;
import DataBase.Database;
import DataBase.Validation;
import DataStructure.CustomDoublyLinkList;
import DataStructure.IOFiles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DGPQueries
{
    Database db = new Database();
    Scanner sc = new Scanner(System.in);
    OODataQueries OODQ=new OODataQueries();
    IOFiles IOF=new IOFiles();
    Validation v=new Validation();
    DataFound found=new DataFound();

    public void AddPoliceRecord() throws Exception
    {
        String dept = "";
        String prefixO = "";
        String prefixS = "";

        while (true)
        {
            dept = v.readNonEmptyString("Enter Department: ");
            String deptLower = dept.trim().toLowerCase();

            if (deptLower.contains("crime") || deptLower.equalsIgnoreCase("crime branch"))
            {
                dept = "Crime Branch";
                prefixO = "CBO";
                prefixS = "ST-CB";
                break;
            }
            else if (deptLower.contains("cyber") || deptLower.equalsIgnoreCase("cyber cell"))
            {
                dept = "Cyber Cell";
                prefixO = "CYO";
                prefixS = "ST-CY";
                break;
            }
            else if (deptLower.contains("women") || deptLower.equalsIgnoreCase("women cell"))
            {
                dept = "Women Cell";
                prefixO = "WCO";
                prefixS = "ST-WC";
                break;
            }
            else if (deptLower.contains("traffic"))
            {
                dept = "Traffic";
                prefixO = "TRO";
                prefixS = "ST-TR";
                break;
            }
            else
            {
                System.err.println("[INVALID] Invalid Department. Valid departments are: Crime Branch, Cyber Cell, Women Cell, Traffic.");
            }
        }

        System.out.print("Enter Officer ID: " + prefixO);
        String officerIDSuffix = sc.next();
        sc.nextLine(); // Consume rest of line
        String officerID = prefixO + officerIDSuffix;

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

        System.out.print("Enter Officer Police Station ID: " + prefixS);
        String psIDSuffix = sc.next();
        sc.nextLine(); // Consume rest of line
        String PSID = prefixS + psIDSuffix;

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

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String AddOfficer="insert into officer_details(OfficerID,Name,DOB,Age,Gender,Rank,Department,StationID,JoiningDate,OfficerStatus) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement QAddOff= Database.getConnection().prepareStatement(AddOfficer);
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
            DataStructure.DataStructure.ActivityLog.push("Added new officer record for Officer ID: " + officerID);
            Database.con.commit();
            QAddOff.close();
        }
        else
        {
            System.out.println("[FAILED] Failed to add officer....");
            Database.con.rollback();
            QAddOff.cancel();
        }

    }

    public void SearchOfficer() throws Exception
    {
        //DataStructure.DataStructure.ActivityLog.push("Performed officer record search.");
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
        DataStructure.DataStructure.ActivityLog.push("Performed officer search by " + ColummName + ": " + Kdetails);

        String SearchOffRec="select *, count(*) over() as TResults from officer_details where "+ColummName+" like ?";
        PreparedStatement QSOR= Database.getConnection().prepareStatement(SearchOffRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Officer ID: %-11s | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                OffData_rs.getString("OfficerID"),
                                OffData_rs.getString("Name"),
                                OffData_rs.getString("Gender"),
                                OffData_rs.getString("Rank"),
                                OffData_rs.getString("Department")
                        );
                        DLL.InsertLast(recordStr);
                    } while (OffData_rs.next());

                    System.out.println("Would you like to see AlL Results???");
                    System.out.print(">>> ");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("Do you want to navigate these records interactively??? (yes/no)");
                        System.out.print(">>> ");
                        char navAns = sc.next().toLowerCase().charAt(0);

                        if (navAns == 'y')
                        {
                            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
                            System.out.println("[INFO] Here, don’t ask every time whether to show next or previous travel records....");
                            System.out.println("[INFO] Handle the Data watching direction automatically based on your choice/input....");
                            System.out.println("[INFO] [Navigation Method] |>>> [N]ext Record | [P]revious Record | [E]xit Navigation <<<|");
                            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
                            DLL.DLLTravalser(sc);
                        }
                        else
                        {
                            DLL.DisplayAllData();
                        }
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
        PreparedStatement QPSOR= Database.getConnection().prepareStatement(PerSerOffRec);
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
        DataStructure.DataStructure.ActivityLog.push("Viewed officer record for ID: " + offID);
    }

    void UnkSOffRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] Name,Gender,Rank,Department,OfficerStatus are Valid....");
        System.out.println("Enter Details Related about that Officer: ");
        System.out.print(">>> ");
        String details=sc.next();

        DataStructure.DataStructure.ActivityLog.push("Performed matching officer search for: " + details);

        String UnkSOffRec="select *, count(*) over() as TResults from officer_details where Name like ? or Rank like ? or Gender like ? or Department like ? or OfficerStatus like ?";
        PreparedStatement QUkSOR= Database.getConnection().prepareStatement(UnkSOffRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Officer ID: %-11s | Name: %-20s | Gender: %-8s | Rank: %-15s | Department: %-15s |",
                                OffData_rs.getString("OfficerID"),
                                OffData_rs.getString("Name"),
                                OffData_rs.getString("Gender"),
                                OffData_rs.getString("Rank"),
                                OffData_rs.getString("Department")
                        );
                        DLL.InsertLast(recordStr);
                    } while (OffData_rs.next());

                    System.out.println("Would you like to see AlL Results???");
                    System.out.print(">>> ");
                    ans=sc.next().charAt(0);

                    if (ans=='Y'||ans=='y')
                    {
                        System.out.println("Do you want to navigate these records interactively??? (yes/no)");
                        System.out.print(">>> ");
                        char navAns = sc.next().toLowerCase().charAt(0);

                        if (navAns == 'y')
                        {
                            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
                            System.out.println("[INFO] Here, don’t ask every time whether to show next or previous travel records....");
                            System.out.println("[INFO] Handle the Data watching direction automatically based on your choice/input....");
                            System.out.println("[INFO] [Navigation Method] |>>> [N]ext Record | [P]revious Record | [E]xit Navigation <<<|");
                            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
                            DLL.DLLTravalser(sc);
                        }
                        else
                        {
                            DLL.DisplayAllData();
                        }
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
        String AllOfficers="select *, count(*) over() as TResults from officer_details";
        PreparedStatement QAO= Database.getConnection().prepareStatement(AllOfficers);
        ResultSet rs=QAO.executeQuery();

        CustomDoublyLinkList DLL=new CustomDoublyLinkList();
        boolean DTR=false;
        int Tcount=0;

        if (rs.next())
        {
            if(!DTR)
            {
                System.out.println("-----| Total Results Found: " + rs.getInt("TResults")+" |-----");
                Tcount=rs.getInt("TResults");
                DTR=true;
            }

            do {
                String recordStr = String.format("| %-11s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                        rs.getString("OfficerID"),
                        rs.getString("Name"),
                        rs.getDate("DOB"),
                        rs.getInt("Age"),
                        rs.getString("Rank"),
                        rs.getString("StationID"),
                        rs.getDate("JoiningDate"),
                        rs.getString("OfficerStatus"),
                        rs.getString("AssignedCase"),
                        rs.getString("CaseStatus"));
                DLL.InsertLast(recordStr);
            } while (rs.next());
        }

        if(Tcount==0)
        {
            QAO.close();
            return;
        }

        System.out.println("Do you want to navigate these records interactively??? (yes/no)");
        System.out.print(">>> ");
        char navAns = sc.next().toLowerCase().charAt(0);

        if (navAns == 'y')
        {
            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
            System.out.println("[INFO] Here, don’t ask every time whether to show next or previous travel records....");
            System.out.println("[INFO] Handle the Data watching direction automatically based on your choice/input....");
            System.out.println("[INFO] [Navigation Method] |>>> [N]ext Record | [P]revious Record | [E]xit Navigation <<<|");
            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");

            System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
            System.out.printf("| %-11s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Officer ID", "Name", "DOB","Age", "Rank","Station ID", "Joining Date", "Status", "Assigned Case", "Case Status");
            System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
            DLL.DLLTravalser(sc);
        }
        else if (navAns=='n')
        {
            System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
            System.out.printf("| %-11s | %-20s | %-13s | %-7s | %-10s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Officer ID", "Name", "DOB","Age", "Rank","Station ID", "Joining Date", "Status", "Assigned Case", "Case Status");
            System.out.println("+------------+----------------------+---------------+---------+------------+----------------------+------------+--------------+-------------+-------------+");
            DLL.DisplayAllData();
        }

        System.out.println("Would you like to Download Police Officer Data as Text file???");
        System.out.print(">>> ");
        char is_text=sc.next().charAt(0);

        if (is_text=='Y' || is_text=='y')
        {
            ResultSet fileSet=QAO.executeQuery();
            IOF.FetchOfficerData(fileSet);
            System.out.println("[INFO] Police Officer Data Downloaded Successfully as Text file....");
            rs.close();
            fileSet.close();
            QAO.close();
            DataStructure.DataStructure.ActivityLog.push("Viewed list of all police officers.");
        }
        else
        {
            System.out.println("[CANCELLED] Police Officer Data Download Cancelled...");
            rs.close();
            QAO.close();
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
            }
        }
    }

    void UpdateRank(String OID) throws Exception
    {
        System.out.print("Enter Updating Rank: ");
        String URank=sc.next();

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        String Update_Rank="update officer_details set Rank=? where OfficerID=?";
        PreparedStatement QRank= Database.getConnection().prepareStatement(Update_Rank);
        QRank.setString(1,URank);
        QRank.setString(2,OID);
        int URankRes=QRank.executeUpdate();

        if(URankRes>0)
        {
            System.out.println("[UPDATED] Officer Rank Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Updated Rank for Officer ID: " + OID + " to: " + URank);
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

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String Update_StationID = "update officer_details set StationID=? where OfficerID=?";
        PreparedStatement QUSID = Database.getConnection().prepareStatement(Update_StationID);
        QUSID.setString(1, UStationID);
        QUSID.setString(2, OID);
        int USIDRes = QUSID.executeUpdate();

        if (USIDRes > 0)
        {
            System.out.println("[UPDATED] Officer Station ID Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Updated Station ID for Officer ID: " + OID + " to: " + UStationID);
            Database.con.commit();
            QUSID.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Station ID....");
            Database.con.rollback();
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

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String Update_OfficerStatus = "update officer_details set OfficerStatus=? where OfficerID=?";
        PreparedStatement QUOffS = Database.getConnection().prepareStatement(Update_OfficerStatus);
        QUOffS.setString(1, UOfficerStatus);
        QUOffS.setString(2, OID);
        int UOffSRes = QUOffS.executeUpdate();

        if (UOffSRes > 0)
        {
            System.out.println("[UPDATED] Officer Status Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Updated Officer Status for Officer ID: " + OID + " to: " + UOfficerStatus);
            Database.con.commit();
            QUOffS.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Officer Status....");
            Database.con.rollback();
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

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String Update_CaseStatus = "update officer_details set CaseStatus=? where OfficerID=?";
        PreparedStatement QUCS = Database.getConnection().prepareStatement(Update_CaseStatus);
        QUCS.setString(1, UCaseStatus);
        QUCS.setString(2, OID);
        int UCStatusRes = QUCS.executeUpdate();

        String UCaseD_CaseStatus="update case_details set CaseStatus=? where OfficerID=?";
        PreparedStatement QUCaD_CaS= Database.getConnection().prepareStatement(UCaseD_CaseStatus);
        QUCaD_CaS.setString(1,UCaseStatus);
        QUCaD_CaS.setString(2,OID);
        int UCD_CSRes=QUCaD_CaS.executeUpdate();

        String UCriD_CaSta="update criminal_details set CaseStatus=? where InvestingOfficerID=?";
        PreparedStatement QUCriD_CaS= Database.getConnection().prepareStatement(UCriD_CaSta);
        QUCriD_CaS.setString(1,UCaseStatus);
        QUCriD_CaS.setString(2,OID);
        int UCriD_CSRes=QUCriD_CaS.executeUpdate();

        if (UCStatusRes > 0 && UCD_CSRes>0 && UCriD_CSRes>0)
        {
            System.out.println("[UPDATED] Case Status Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Updated Case Status for Case ID linked to Officer ID: " + OID + " to: " + UCaseStatus);
            Database.con.commit();
            QUCS.close();
            QUCaD_CaS.close();
            QUCriD_CaS.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Case Status....");
            Database.con.rollback();
            QUCS.cancel();
            QUCriD_CaS.cancel();
            QUCaD_CaS.cancel();
        }
    }

    void UpdateAssignedCase(String OID) throws Exception
    {
        String UAssignedCase =v.readNonEmptyString("Enter Updating Assigned Case ID: ");

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String Update_AssignedCase = "update officer_details set AssignedCase=? where OfficerID=?";
        PreparedStatement QUAC = Database.getConnection().prepareStatement(Update_AssignedCase);
        QUAC.setString(1, UAssignedCase);
        QUAC.setString(2, OID);
        int UACRes = QUAC.executeUpdate();

        String UOffId_CaD="update case_details set OfficerID=? where CaseID=?";
        PreparedStatement QOidCaD= Database.getConnection().prepareStatement(UOffId_CaD);
        QOidCaD.setString(1,OID);
        QOidCaD.setString(2,UAssignedCase);
        int UOIDRes=QOidCaD.executeUpdate();

        String UOffId_CriD="update criminal_details set InvestingOfficerID=? where CaseID=?";
        PreparedStatement QOidCriD= Database.getConnection().prepareStatement(UOffId_CriD);
        QOidCriD.setString(1,OID);
        QOidCriD.setString(2,UAssignedCase);
        int UOIDRes2=QOidCriD.executeUpdate();

        if (UACRes > 0 && UOIDRes>0 && UOIDRes2>0)
        {
            System.out.println("[UPDATED] Assigned Case Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Assigned Case ID: " + UAssignedCase + " to Officer ID: " + OID);
            Database.con.commit();
            QUAC.close();
            QOidCaD.close();
            QOidCriD.close();
        }
        else
        {
            System.err.println("[FAILED] Failed to Update Assigned Case....");
            Database.con.rollback();
            QUAC.cancel();
            QOidCaD.cancel();
            QOidCriD.cancel();
        }
    }

    public void DatabaseUpdatesActivity() throws Exception
    {
        DataStructure.DataStructure.ActivityLog.push("Viewed Database Updates Activity Log.");

        // Simple SQL query to fetch all activity logs ordered by LogID desc (latest first)
        String sql = "SELECT Time, UserID, Role, Activity, ActivityEndTime, ActivityDuration FROM ActivityLog ORDER BY LogID DESC";
        
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            boolean hasLogs = false;
            while (rs.next())
            {
                hasLogs = true;
                java.sql.Timestamp time = rs.getTimestamp("Time");
                String userId = rs.getString("UserID");
                String role = rs.getString("Role");
                String activity = rs.getString("Activity");
                java.sql.Timestamp endTime = rs.getTimestamp("ActivityEndTime");
                String duration = rs.getString("ActivityDuration");

                String timeStr = time != null ? time.toString() : "N/A";
                String endTimeStr = endTime != null ? endTime.toString() : "N/A";

                System.out.printf("[%s] %s - %s | %s | [%s] | %s\n",
                        timeStr, userId, role, activity, endTimeStr, duration);
            }
            if (!hasLogs)
            {
                System.out.println("[INFO] No activity logs found in system....");
            }
        }
        catch (Exception e)
        {
            System.err.println("[WARNING] Failed to load database updates activity: " + e.getMessage());
        }
    }
}