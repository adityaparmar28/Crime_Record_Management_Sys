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
import DataStructure.*;
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

        String C_CrimeType = "";
        String prefix = "";

        while (true)
        {
            C_CrimeType = v.readNonEmptyString("Enter Crime Type: ");
            String crimeLower = C_CrimeType.trim().toLowerCase();

            if (crimeLower.contains("cyber"))
            {
                C_CrimeType = "Cyber Crime";
                prefix = "CY";
                break;
            }
            else if (crimeLower.contains("robbery"))
            {
                C_CrimeType = "Robbery";
                prefix = "R";
                break;
            }
            else if (crimeLower.contains("murder"))
            {
                C_CrimeType = "Murder";
                prefix = "M";
                break;
            }
            else if (crimeLower.contains("kidnap"))
            {
                C_CrimeType = "Kidnapping";
                prefix = "K";
                break;
            }
            else if (crimeLower.contains("drug"))
            {
                C_CrimeType = "Drug Crime";
                prefix = "D";
                break;
            }
            else if (crimeLower.contains("traffic"))
            {
                C_CrimeType = "Traffic Violation";
                prefix = "T";
                break;
            }
            else
            {
                System.err.println("[INVALID] Invalid Crime Type....");
                System.out.println("[VALID]  crime types are: Robbery, Cyber Crime, Murder, Kidnapping, Drug Crime, Traffic Violation....");
            }
        }

        System.out.print("Enter Criminal ID: " + prefix);
        String cIdSuffix = sc.next();
        sc.nextLine(); // Consume rest of line
        String C_Id = prefix + cIdSuffix;

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

        System.out.print("Enter Case ID: " + prefix);
        String caseIdSuffix = sc.next();
        sc.nextLine(); // Consume rest of line
        String C_CaseID = prefix + caseIdSuffix;

        String C_CrimeDate=v.Date("Crime Date");

        // Determine recommended department and prefix for officer
        String recDept = "";
        String prefixO = "";
        String ctLower = C_CrimeType.toLowerCase();

        if (ctLower.contains("cyber"))
        {
            recDept = "Cyber Cell";
            prefixO = "CYO";
        }
        else if (ctLower.contains("robbery") || ctLower.contains("murder") || ctLower.contains("drug"))
        {
            recDept = "Crime Branch";
            prefixO = "CBO";
        }
        else if (ctLower.contains("kidnap"))
        {
            recDept = "Women Cell";
            prefixO = "WCO";
        }
        else if (ctLower.contains("traffic"))
        {
            recDept = "Traffic";
            prefixO = "TRO";
        }

        System.out.println("\nRecommended Department for this Crime: " + (recDept.isEmpty() ? "Any" : recDept));

        // Query available officers sorted by recommended department first
        String AvailOffQuery = "select OfficerID, Name, Department, StationID, OfficerStatus from officer_details " +
                               "where AssignedCase is null or AssignedCase = '' or lower(CaseStatus) = 'solved' " +
                               "order by case when Department = ? then 0 else 1 end, Department, Name";

        try (PreparedStatement QAvail = Database.getConnection().prepareStatement(AvailOffQuery))
        {
            QAvail.setString(1, recDept);

            try (ResultSet av_rs = QAvail.executeQuery())
            {
                System.out.println("Available Officers for Assignment (Recommended Department Listed First):");
                System.out.println("+------------+----------------------+------------------+------------+-----------------+");
                System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n", "Officer ID", "Name", "Department", "Station ID", "Officer Status");
                System.out.println("+------------+----------------------+------------------+------------+-----------------+");

                boolean hasAvail = false;

                while (av_rs.next())
                {
                    hasAvail = true;
                    System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n",
                        av_rs.getString("OfficerID"),
                        av_rs.getString("Name"),
                        av_rs.getString("Department"),
                        av_rs.getString("StationID") != null ? av_rs.getString("StationID") : "N/A",
                        av_rs.getString("OfficerStatus") != null ? av_rs.getString("OfficerStatus") : "N/A"
                    );

                }

                if (!hasAvail)
                {
                    System.out.println("|                         No available officers found....                           |");
                }
                System.out.println("+------------+----------------------+------------------+------------+-----------------+");
            }
        }

        System.out.print("Enter Investigating Officer ID: " + prefixO);
        String officerSuffix = sc.next();
        sc.nextLine(); // Clear buffer
        String C_IOffID = prefixO + officerSuffix;

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

        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String addCRQ = "insert into criminal_details(CriminalID,Name,Age,Gender,CaseID,CrimeType,CrimeDate,InvestingOfficerID,CaseStatus) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement addCR = Database.getConnection().prepareStatement(addCRQ);
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
            String upCDQuery = "update case_details set CriminalID=?, OfficerID=?, CaseStatus='Investigating' where CaseID=?";
            upCD = Database.getConnection().prepareStatement(upCDQuery);
            upCD.setString(1, C_Id);
            upCD.setString(2, C_IOffID);
            upCD.setString(3, C_CaseID);
            updateCase = upCD.executeUpdate();

            // Sync with officer_details
            String upOffQuery = "update officer_details set AssignedCase=?, CaseStatus='Investigating', OfficerStatus='Active' where OfficerID=?";
            try (PreparedStatement upOff = Database.getConnection().prepareStatement(upOffQuery))
            {
                upOff.setString(1, C_CaseID);
                upOff.setString(2, C_IOffID);
                upOff.executeUpdate();
            }

            // Sync with Criminal_Pictures
            String upCPQuery = "insert into Criminal_Pictures(CriminalID,CaseID,CriminalName) values(?,?,?)";
            upCP = Database.getConnection().prepareStatement(upCPQuery);
            upCP.setString(1, C_Id);
            upCP.setString(2, C_CaseID);
            upCP.setString(3, C_Name);
            updatePic = upCP.executeUpdate();
        }

        if (update > 0 && updateCase > 0 && updatePic > 0)
        {
            System.out.println("[UPDATED] Criminal details added successfully...");
            Database.con.commit();
            DataStructure.ActivityLog.push("Added criminal record with ID: " + C_Id);
            addCR.close();
            if (upCD != null)
            {
                upCD.close();
            }

            if (upCP != null)
            {
                upCP.close();
            }
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Added...");
            Database.con.rollback();
            addCR.close();
            if (upCD != null)
            {
                upCD.close();
            }
            if (upCP != null)
            {
                upCP.close();
            }
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
                    // Fetch CaseType to determine recommended department
                    String recDept = "";
                    String prefixO = "";
                    String getCaseType = "select CaseType from case_details where CaseID=?";
                    try (PreparedStatement qct = Database.getConnection().prepareStatement(getCaseType))
                    {
                        qct.setString(1, C_CaseID);

                        try (ResultSet rsct = qct.executeQuery())
                        {
                            if (rsct.next())
                            {
                                String caseType = rsct.getString("CaseType");
                                String ctLower = caseType.toLowerCase();

                                if (ctLower.contains("cyber"))
                                {
                                    recDept = "Cyber Cell";
                                    prefixO = "CYO";
                                }
                                else if (ctLower.contains("robbery") || ctLower.contains("murder") || ctLower.contains("drug"))
                                {
                                    recDept = "Crime Branch";
                                    prefixO = "CBO";
                                }
                                else if (ctLower.contains("kidnap"))
                                {
                                    recDept = "Women Cell";
                                    prefixO = "WCO";
                                }
                                else if (ctLower.contains("traffic"))
                                {
                                    recDept = "Traffic";
                                    prefixO = "TRO";
                                }
                            }
                        }
                    }

                    System.out.println("Recommended Department for this Crime: " + (recDept.isEmpty() ? "Any" : recDept));

                    // Query available officers sorted by recommended department first
                    String AvailOffQuery = "select OfficerID, Name, Department, StationID, OfficerStatus from officer_details " +
                                           "where AssignedCase is null or AssignedCase = '' or lower(CaseStatus) = 'solved' " +
                                           "order by case when Department = ? then 0 else 1 end, Department, Name";

                    try (PreparedStatement QAvail = Database.getConnection().prepareStatement(AvailOffQuery))
                    {
                        QAvail.setString(1, recDept);

                        try (ResultSet av_rs = QAvail.executeQuery())
                        {
                            System.out.println("Available Officers for Assignment (Recommended Department Listed First):");
                            System.out.println("+------------+----------------------+------------------+------------+-----------------+");
                            System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n", "Officer ID", "Name", "Department", "Station ID", "Officer Status");
                            System.out.println("+------------+----------------------+------------------+------------+-----------------+");

                            boolean hasAvail = false;

                            while (av_rs.next())
                            {
                                hasAvail = true;
                                System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n",
                                    av_rs.getString("OfficerID"),
                                    av_rs.getString("Name"),
                                    av_rs.getString("Department"),
                                    av_rs.getString("StationID") != null ? av_rs.getString("StationID") : "N/A",
                                    av_rs.getString("OfficerStatus") != null ? av_rs.getString("OfficerStatus") : "N/A"
                                );
                            }

                            if (!hasAvail)
                            {
                                System.out.println("|                      No available officers found.                           |");
                            }
                            System.out.println("+------------+----------------------+------------------+------------+-----------------+\n");
                        }
                    }

                    System.out.print("Enter New Investigating Officer ID: " + prefixO);
                    String officerSuffix = sc.next();
                    sc.nextLine(); // Clear buffer
                    String C_IOffID = prefixO + officerSuffix;

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
                    return;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] Invalid Choice....Enter a valid choice....");
            return;
        }
        //DataStructure.ActivityLog.push("Updated criminal record linked to Case ID: " + C_CaseID);
    }

    void UpdateIOffID(String oid, String CaseID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String UCri_IOID = "update criminal_details set InvestingOfficerID=?, CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUCrIOID = Database.getConnection().prepareStatement(UCri_IOID);
        QUCrIOID.setString(1, oid);
        QUCrIOID.setString(2, CaseID);
        int updateCIO = QUCrIOID.executeUpdate();

        String UOf_CaseID = "update officer_details set AssignedCase=?, CaseStatus='Investigating', OfficerStatus='Active' where OfficerID=?";
        PreparedStatement QUOfCID = Database.getConnection().prepareStatement(UOf_CaseID);
        QUOfCID.setString(1, CaseID);
        QUOfCID.setString(2, oid);
        int updateIO_cid = QUOfCID.executeUpdate();

        String UOIdCase = "update case_details set OfficerID=?, CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUOIdCase = Database.getConnection().prepareStatement(UOIdCase);
        QUOIdCase.setString(2, CaseID);
        QUOIdCase.setString(1, oid);
        int upOidCase = QUOIdCase.executeUpdate();

        if (updateCIO > 0 && updateIO_cid > 0 && upOidCase>0)
        {
            System.out.println("[UPDATED] Criminal details updated successfully...");
            DataStructure.ActivityLog.push("Updated Investing Officer ID for Case ID: " + CaseID + " to: " + oid);
            Database.con.commit();
            QUCrIOID.close();
            QUOfCID.close();
            QUOIdCase.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal Data Couldn't be Updated...");
            Database.con.rollback();
            QUCrIOID.cancel();
            QUOfCID.cancel();
            QUOIdCase.cancel();
        }
    }

    void UpdateCaseStatus(String Status, String CaseID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String UCase_Status = "update criminal_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCS = Database.getConnection().prepareStatement(UCase_Status);
        QUCS.setString(1, Status);
        QUCS.setString(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        String UCD_CaseStatus = "update case_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCD_CS = Database.getConnection().prepareStatement(UCD_CaseStatus);
        QUCD_CS.setString(1, Status);
        QUCD_CS.setString(2, CaseID);
        int updateCD_CS = QUCD_CS.executeUpdate();

        String UOD_CaseStatus = "update officer_details set CaseStatus=? where AssignedCase=?";
        PreparedStatement QUOD_CS = Database.getConnection().prepareStatement(UOD_CaseStatus);
        QUOD_CS.setString(1, Status);
        QUOD_CS.setString(2, CaseID);
        int updateOD_CS = QUOD_CS.executeUpdate();

        if (updateCS > 0 && updateCD_CS > 0 && updateOD_CS>0)
        {
            System.out.println("[UPDATED] Case status updated successfully...");
            DataStructure.ActivityLog.push("Updated Case Status for Case ID: " + CaseID + " to: " + Status);
            Database.con.commit();
            QUCS.close();
            QUCD_CS.close();
            QUOD_CS.close();
        }
        else
        {
            System.out.println("[FAILED] Case status couldn't be updated...");
            Database.con.rollback();
            QUCS.cancel();
            QUCD_CS.cancel();
            QUOD_CS.cancel();
        }
    }

    void UpdateCriminalStatus(String Status, String CaseID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String UCri_Status = "update criminal_details set CriminalStatus=? where CaseID=?";
        PreparedStatement QUCS = Database.getConnection().prepareStatement(UCri_Status);
        QUCS.setString(1, Status);
        QUCS.setString(2, CaseID);
        int updateCS = QUCS.executeUpdate();

        if (updateCS > 0)
        {
            System.out.println("[UPDATED] Criminal status updated successfully...");
            DataStructure.ActivityLog.push("Updated Criminal Status for Case ID: " + CaseID + " to: " + Status);
            Database.con.commit();
            QUCS.close();
        }
        else
        {
            System.out.println("[FAILED] Criminal status couldn't be updated...");
            Database.con.rollback();
            QUCS.cancel();
        }
    }

    void SetUpdateBail(String BailDate, String CaseID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String UBail_Date = "update criminal_details set BailDate=? where CaseID=?";
        PreparedStatement QUBD = Database.getConnection().prepareStatement(UBail_Date);
        QUBD.setString(1, BailDate);
        QUBD.setString(2, CaseID);
        int updateBD = QUBD.executeUpdate();

        if (updateBD > 0)
        {
            System.out.println("[UPDATED] Bail date updated successfully...");
            DataStructure.ActivityLog.push("Updated Bail Date for Case ID: " + CaseID + " to: " + BailDate);
            Database.con.commit();
            QUBD.close();
        }
        else
        {
            System.out.println("[FAILED] Bail date couldn't be updated...");
            Database.con.rollback();
            QUBD.cancel();
        }
    }

    void UpdateReleaseDate(String ReleaseDate, String CaseID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha())
        {
            return;
        }

        Database.con.setAutoCommit(false);

        String URelease_Date = "update criminal_details set ReleaseDate=? where CaseID=?";
        PreparedStatement QURD = Database.getConnection().prepareStatement(URelease_Date);
        QURD.setString(1, ReleaseDate);
        QURD.setString(2, CaseID);
        int updateRD = QURD.executeUpdate();

        if (updateRD > 0)
        {
            System.out.println("[UPDATED] Release date updated successfully...");
            DataStructure.ActivityLog.push("Updated Release Date for Case ID: " + CaseID + " to: " + ReleaseDate);
            Database.con.commit();
            QURD.close();
        }
        else
        {
            System.out.println("[FAILED] Release date couldn't be updated...");
            Database.con.rollback();
            QURD.cancel();
        }
    }

    public void AllCriminalRecord() throws Exception
    {
        String ACriRec = "select *, count(*) over() as TResults from criminal_details";
        PreparedStatement QACRec = Database.getConnection().prepareStatement(ACriRec);
        ResultSet rsACRec = QACRec.executeQuery();

        CustomDoublyLinkList DLL=new CustomDoublyLinkList();

        boolean DTR=false;
        int Tcount=0;

        if (rsACRec.next())
        {
            if(!DTR)
            {
                System.out.println("-----| Total Results Found: " + rsACRec.getInt("TResults")+" |-----");
                Tcount=rsACRec.getInt("TResults");
                DTR=true;
            }

            do {

                String recordStr = String.format("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |",
                        rsACRec.getString("CriminalID"),
                        rsACRec.getString("Name"),
                        rsACRec.getInt("Age"),
                        rsACRec.getString("Gender"),
                        rsACRec.getString("CaseID"),
                        rsACRec.getString("CrimeType"),
                        rsACRec.getDate("CrimeDate"),
                        rsACRec.getString("PunishmentType"),
                        rsACRec.getString("CriminalStatus")
                );

                //System.out.println(recordStr);
                DLL.InsertLast(recordStr);

            }while (rsACRec.next());
        }

        DataStructure.ActivityLog.push("Viewed list of all criminals.");

        if(Tcount==0)
        {
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

            System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");
            System.out.printf("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Criminal ID", "Name", "Age", "Gender","Case ID", "Crime", "Crime Date","Judgement", "Status");
            System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");

            DLL.DLLTravalser(sc);
        }
        else if (navAns=='n')
        {
            //all calling pending.....
            System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");
            System.out.printf("| %-11s | %-20s | %-3s | %-7s | %-10s | %-20s | %-10s | %-12s | %-10s |\n", "Criminal ID", "Name", "Age", "Gender","Case ID", "Crime", "Crime Date","Judgement", "Status");
            System.out.println("+-------------+----------------------+-----+---------+------------+----------------------+------------+--------------+------------+");

            DLL.DisplayAllData();
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
        }
    }

    public void SearchCriminalRecord() throws Exception
    {
        System.out.println("Select Search Option:");
        System.out.println("| 1. Search by Particular Details");
        System.out.println("| 2. Search by Autocomplete (Trie)");
        System.out.println("| 3. Search by Database Matching");
        System.out.print(">>> ");
        int choice = sc.nextInt();

        if (choice == 1)
        {
            KSCriRec();
        }
        else if (choice == 2)
        {
            AutocompleteSearch();
        }
        else if (choice == 3)
        {
            UnkSCriRec();
        }
        else
        {
            System.err.println("[INVALID] Invalid Choice...");
        }
        //DataStructure.ActivityLog.push("Performed criminal record search.");
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
        DataStructure.ActivityLog.push("Performed criminal search by " + ColummName + ": " + Kdetails);

        String SearchCriRec="select *, count(*) over() as TResults from criminal_details where "+ColummName+" like ?";
        PreparedStatement QSCR= Database.getConnection().prepareStatement(SearchCriRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Criminal ID: %-11s | Name: %-20s | Gender: %-8s | Case ID: %-11s | Crime Type: %-15s | Punishment: %-15s |",
                                CriData_rs.getString("CriminalID"),
                                CriData_rs.getString("Name"),
                                CriData_rs.getString("Gender"),
                                CriData_rs.getString("CaseID"),
                                CriData_rs.getString("CrimeType"),
                                CriData_rs.getString("PunishmentType")
                        );
                        DLL.InsertLast(recordStr);
                    } while (CriData_rs.next());

                    System.out.println("Would you like to see AlL Results???");
                    System.out.print(">>> ");
                    ans = sc.next().charAt(0);

                    if (ans == 'Y' || ans == 'y')
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

            if(Login_SignUpPage.getLoggedUserID().equals(""))
            {
                if(LSP.userLogin())
                {
                    CriminalDetails(criID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
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
        PreparedStatement QPSCR= Database.getConnection().prepareStatement(PerSerCriRec);
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
        DataStructure.ActivityLog.push("Viewed criminal record details for ID: " + criID);

        System.out.println("Do you want to see Picture of Criminal???");
        System.out.print(">>> ");
        char isPic=sc.next().charAt(0);

        if (isPic=='Y'||isPic=='y')
        {
            //fetch criminal photo from database and display it....
            IOF.FetchCriPic(criID);
        }
        else if(isPic=='N'||isPic=='n')
        {
            System.out.println("[INFO] Returning to Main Menu....");
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
        }
    }

    void UnkSCriRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] Name,Gender,Age,CrimeType,PunishmentType are valid....");
        System.out.print("Enter Details Related about that Criminal or Crime: ");
        String details=sc.next();

        DataStructure.ActivityLog.push("Performed matching criminal search for: " + details);

        String UnkSCriRec="select *, count(*) over() as TResults from criminal_details where Name like ? or CrimeType like ? or PunishmentType like ? or Gender like ? or Age like ?";
        PreparedStatement QUkSCR= Database.getConnection().prepareStatement(UnkSCriRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Criminal ID: %-11s | Name: %-20s | Gender: %-8s | Case ID: %-11s | Crime Type: %-15s | Punishment: %-15s |",
                                CriData_rs.getString("CriminalID"),
                                CriData_rs.getString("Name"),
                                CriData_rs.getString("Gender"),
                                CriData_rs.getString("CaseID"),
                                CriData_rs.getString("CrimeType"),
                                CriData_rs.getString("PunishmentType")
                        );
                        DLL.InsertLast(recordStr);
                    } while (CriData_rs.next());

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

            if(Login_SignUpPage.getLoggedUserID().equals(""))
            {
                if(LSP.userLogin())
                {
                    CriminalDetails(criID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
                }
            }
            else
            {
                CriminalDetails(criID);
            }
        }
    }

    void AutocompleteSearch() throws Exception
    {
        CustomBinarySearchTree tree = new CustomBinarySearchTree();

        String selectNames = "select Name from criminal_details";
        PreparedStatement ps = Database.getConnection().prepareStatement(selectNames);
        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            tree.Insert(rs.getString("Name"));
        }

        rs.close();
        ps.close();

        System.out.print("Enter prefix to autocomplete (e.g. AM): ");
        String prefix = sc.next().toUpperCase();
        tree.searchPrefix(prefix);
        DataStructure.ActivityLog.push("Performed autocomplete search for prefix: " + prefix);
    }

    public void DisplayCriminalRelations() throws Exception
    {
        CustomGraph graph = APIs.RelationAPI.buildGraph();
        graph.displayNetwork();

        DataStructure.ActivityLog.push("Viewed criminal graph.");
    }
}