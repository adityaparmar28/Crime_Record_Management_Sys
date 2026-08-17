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
import DataStructure.DataStructure;
import DataStructure.IOFiles;
import Profile.Login_SignUpPage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Scanner;
import DataStructure.CustomPriorityQueue;
import DataStructure.CustomDoublyLinkList;

public class CrimeMngrQueries
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();
    OODataQueries OODQ=new OODataQueries();
    CustomPriorityQueue PCS=DataStructure.PendingCase;
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

        String CType = "";
        String prefix = "";

        while (true)
        {
            CType = v.readNonEmptyString("Enter Case Type: ");
            String crimeLower = CType.trim().toLowerCase();

            if (crimeLower.contains("cyber"))
            {
                CType = "Cyber Crime";
                prefix = "CY";
                break;
            }
            else if (crimeLower.contains("robbery"))
            {
                CType = "Robbery";
                prefix = "R";
                break;
            }
            else if (crimeLower.contains("murder"))
            {
                CType = "Murder";
                prefix = "M";
                break;
            }
            else if (crimeLower.contains("kidnap"))
            {
                CType = "Kidnapping";
                prefix = "K";
                break;
            }
            else if (crimeLower.contains("drug"))
            {
                CType = "Drug Crime";
                prefix = "D";
                break;
            }
            else if (crimeLower.contains("traffic"))
            {
                CType = "Traffic Violation";
                prefix = "T";
                break;
            }
            else
            {
                System.err.println("[INVALID] Invalid Case Type. Valid case types are: Robbery, Cyber Crime, Murder, Kidnapping, Drug Crime, Traffic Violation.");
            }
        }

        String CDate=v.Date("Enter Crime Date: ");

        System.out.print("Enter Case ID: " + prefix);
        String CaseID = sc.next();
        sc.nextLine(); // Consume rest of line
        String Case_ID = prefix + CaseID;

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

        Database.con.setAutoCommit(false);

        String CriminalID = prefix + "5" + (CaseID.length() >= 2 ? CaseID.substring(CaseID.length()-2) : CaseID);

        System.out.println("Do you have Suspect Picture???");
        char picQ=sc.next().toUpperCase().charAt(0);

        if(picQ=='Y')
        {
            boolean isPic=false;

            while (!isPic)
            {
                System.out.println("Enter Picture Path in Double Quote: ");
                String path = sc.next();

                if (path == null)
                {
                    System.out.println("[WARNING] Thre is no Picture to Update....");
                    isPic=false;
                    return;
                }

                if (!(path.contains(".jpg") || path.contains(".jpeg") || path.contains(".png")))
                {
                    System.err.println("[INVALID] Invalid Picture extention....");
                    System.out.println("[VALID] Only PNG, JPEG, JPG format pictures are allowed....");
                    isPic=false;
                    return;
                }

                IOF.InsertCriminalPic(CriminalID, path);
                isPic=true;
            }
        }
        else if (picQ=='N')
        {
            return;
        }
        else
        {
            System.err.println("[ERROR] Invalid Input....");
            System.out.println("[INFO] Considering Picture as NA....");
            return;
        }


        // 1. Insert into case_details (leaving CriminalID NULL initially to avoid FK constraint fail)
        String FileFIR="insert into case_details(CaseID,CaseType,CrimeLocation,CrimeWeapon,SuspectName,VictimName,CrimeDetails,CaseName) values(?,?,?,?,?,?,?,?)";

        PreparedStatement QFIR= Database.getConnection().prepareStatement(FileFIR);
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

        PreparedStatement QACriData= Database.getConnection().prepareStatement(AddCriData);
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

        PreparedStatement QLinkCriCase = Database.getConnection().prepareStatement(LinkCriCase);
        QLinkCriCase.setString(1, CriminalID);
        QLinkCriCase.setString(2, Case_ID);
        int linkUpdate = QLinkCriCase.executeUpdate();

        // 4. Add picture placeholder details to Criminal_Pictures
        String AddCriPic = "insert into Criminal_Pictures(CriminalID,CaseID,CriminalName) values(?,?,?)";

        PreparedStatement QACriPic = Database.getConnection().prepareStatement(AddCriPic);
        QACriPic.setString(1, CriminalID);
        QACriPic.setString(2, Case_ID);
        QACriPic.setString(3, SuspectName);
        int picUpdate = QACriPic.executeUpdate();

        if(update>0 && addUpdate>0 && linkUpdate>0 && picUpdate>0)
        {
            System.out.println("[UPDATED] FIR report filed successfully...");

            Database.con.commit();
            DataStructure.ActivityLog.push("Filed FIR report with Case ID: " + Case_ID);

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

            Database.con.rollback();
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

        PreparedStatement QSOR = Database.getConnection().prepareStatement(SearchCaseRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Case ID: %-11s | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                CaseD_rs.getString("CaseID"),
                                CaseD_rs.getString("CaseType"),
                                CaseD_rs.getString("CaseStatus"),
                                CaseD_rs.getString("CrimeDetails")
                        );
                        DLL.InsertLast(recordStr);
                    } while (CaseD_rs.next());

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

            if(Login_SignUpPage.LoggedUserID =="")
            {
                if(LSP.userLogin())
                {
                    CaseDetails(caseID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
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
        PreparedStatement QPSR= Database.getConnection().prepareStatement(PerSCaseRec);
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
        DataStructure.ActivityLog.push("Viewed case details for Case ID: " + caseID);

        System.out.println("Do you want to see Picture of Criminal???");
        System.out.print(">>> ");
        char isPic=sc.next().charAt(0);

        if (isPic=='Y'||isPic=='y')
        {
            //fetch criminal photo from database and display it....
            IOF.FetchCriPic(caseID);
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

    void UnkSCaseRec() throws Exception
    {
        char ans;

        System.out.println("[INFO] CaseName,CrimeLocation,CrimeWeapon,SuspectName,VictimName are Valid....");
        System.out.print("Enter Details Related about that Criminal or Crime: ");
        String details=sc.next();

        String UnkSCaseRec="select *, count(*) over() as TResults from case_details where CaseName like ? or CrimeLocation like ? or CrimeWeapon like ? or SuspectName like ? or VictimName like ?";

        PreparedStatement QUkSCR= Database.getConnection().prepareStatement(UnkSCaseRec);
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
                    CustomDoublyLinkList DLL = new CustomDoublyLinkList();
                    do
                    {
                        String recordStr = String.format(
                                "| Case ID: %-11s | Case Type: %-20s | Case Status: %-15s | Description: %-50s |",
                                CaseD_rs.getString("CaseID"),
                                CaseD_rs.getString("CaseType"),
                                CaseD_rs.getString("CaseStatus"),
                                CaseD_rs.getString("CrimeDetails")
                        );
                        DLL.InsertLast(recordStr);
                    } while (CaseD_rs.next());

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

            if(Objects.equals(Login_SignUpPage.LoggedUserID, ""))
            {
                if(LSP.userLogin())
                {
                    CaseDetails(caseID);
                }
                else
                {
                    System.out.println("[INFO] Can't see Detailed Case Details Without User Login....");
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
        String AllCase="select *, count(*) over() as TResults from case_details";
        PreparedStatement QAC= Database.getConnection().prepareStatement(AllCase);
        ResultSet ACrs=QAC.executeQuery();

        CustomDoublyLinkList DLL=new CustomDoublyLinkList();
        boolean DTR=false;
        int Tcount=0;

        if (ACrs.next())
        {
            if(!DTR)
            {
                System.out.println("-----| Total Results Found: " + ACrs.getInt("TResults")+" |-----");
                Tcount=ACrs.getInt("TResults");
                DTR=true;
            }

            do {
                String recordStr = String.format(
                        "| Case ID: %-11s | Case Name: %-20s | Case Type: %-20s | Location: %-20s | Suspect: %-15s | Victim: %-15s | Status: %-10s |",
                        ACrs.getString("CaseID"),
                        ACrs.getString("CaseName") != null ? ACrs.getString("CaseName") : "N/A",
                        ACrs.getString("CaseType") != null ? ACrs.getString("CaseType") : "N/A",
                        ACrs.getString("CrimeLocation") != null ? ACrs.getString("CrimeLocation") : "N/A",
                        ACrs.getString("SuspectName") != null ? ACrs.getString("SuspectName") : "N/A",
                        ACrs.getString("VictimName") != null ? ACrs.getString("VictimName") : "N/A",
                        ACrs.getString("CaseStatus") != null ? ACrs.getString("CaseStatus") : "Pending"
                );
                DLL.InsertLast(recordStr);
            } while (ACrs.next());
        }

        DataStructure.ActivityLog.push("Viewed list of all cases.");

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
            DLL.DLLTravalser(sc);
        }
        else if (navAns=='n')
        {
            DLL.DisplayAllData();
        }

        System.out.println("Do you want Complete Case Details???");
        System.out.print(">>> ");
        char ans=sc.next().charAt(0);

        if(ans=='Y'||ans=='y')
        {
            ResultSet fileSet=QAC.executeQuery();

            if(Login_SignUpPage.LoggedUserID =="")
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
                    ACrs.close();
                    fileSet.close();
                    QAC.close();
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
            ACrs.close();
            QAC.close();
        }
        else
        {
            System.err.println("[INVALID] Give Answer only by Yes or No...");
            ACrs.close();
            QAC.close();
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
        System.out.println("| 9. Criminal Picture");
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

            case 9:
            {
                boolean isPic=false;

                while (!isPic)
                {
                    System.out.println("Enter Picture Path in Double Quote: ");
                    String path = sc.next();

                    if (path == null)
                    {
                        System.out.println("[WARNING] Thre is no Picture to Update....");
                        isPic=false;
                        return;
                    }

                    if (!(path.contains(".jpg") || path.contains(".jpeg") || path.contains(".png")))
                    {
                        System.err.println("[INVALID] Invalid Picture extention....");
                        System.out.println("[VALID] Only PNG, JPEG, JPG format pictures are allowed....");
                        isPic=false;
                        return;
                    }

                    IOF.InsertCriminalPic(CId, path);
                    isPic=true;
                }
                break;
            }

            default:
            {
                System.err.println("[ERROR] Invalid Choice....Try again...");
                break;
            }
        }
        DataStructure.ActivityLog.push("Updated case details for Case ID: " + CId);
    }

    void UpdateCriID(String c_id) throws Exception
    {
        String CriID=v.readNonEmptyString("Enter Updating Criminal ID: ");

        Database.con.setAutoCommit(false);

        String UpdateCriID="update case_details set CriminalID=? where CaseID=?";
        PreparedStatement QUCriID= Database.getConnection().prepareStatement(UpdateCriID);
        QUCriID.setString(1,CriID);
        QUCriID.setString(2,c_id);
        int UCID=QUCriID.executeUpdate();

        /**Updating Criminal ID of that case Id in Criminal_details....**/

        String UpCriIdDetails="update criminal_details set CriminalID=? where CaseID=?";
        PreparedStatement QUCriIdData= Database.getConnection().prepareStatement(UpCriIdDetails);
        QUCriIdData.setString(1,CriID);
        QUCriIdData.setString(2,c_id);
        int UCIdData=QUCriIdData.executeUpdate();

        if (UCID>0 && UCIdData>0)
        {
            System.out.println("[UPDATED] Criminal ID updated successfully...");
            Database.con.commit();
            QUCriID.close();
            QUCriIdData.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Criminal ID....Try again...");
            Database.con.rollback();
            QUCriID.cancel();
            QUCriIdData.cancel();
        }
    }

    void UpdateOffID(String c_id) throws Exception
    {
        // Fetch CaseType to determine recommended department
        String recDept = "";
        String prefixO = "";
        String getCaseType = "select CaseType from case_details where CaseID=?";
        try (PreparedStatement qct = Database.getConnection().prepareStatement(getCaseType)) {
            qct.setString(1, c_id);
            try (ResultSet rsct = qct.executeQuery()) {
                if (rsct.next()) {
                    String caseType = rsct.getString("CaseType");
                    String ctLower = caseType.toLowerCase();
                    if (ctLower.contains("cyber")) {
                        recDept = "Cyber Cell";
                        prefixO = "CYO";
                    } else if (ctLower.contains("robbery") || ctLower.contains("murder") || ctLower.contains("drug")) {
                        recDept = "Crime Branch";
                        prefixO = "CBO";
                    } else if (ctLower.contains("kidnap")) {
                        recDept = "Women Cell";
                        prefixO = "WCO";
                    } else if (ctLower.contains("traffic")) {
                        recDept = "Traffic";
                        prefixO = "TRO";
                    }
                }
            }
        }

        System.out.println("\nRecommended Department for this Crime: " + (recDept.isEmpty() ? "Any" : recDept));

        // Query available officers sorted by recommended department first
        String AvailOffQuery = "select OfficerID, Name, Department, StationID, OfficerStatus from officer_details " +
                               "where AssignedCase is null or AssignedCase = '' or lower(CaseStatus) = 'solved' " +
                               "order by case when Department = ? then 0 else 1 end, Department, Name";
        try (PreparedStatement QAvail = Database.getConnection().prepareStatement(AvailOffQuery)) {
            QAvail.setString(1, recDept);
            try (ResultSet av_rs = QAvail.executeQuery()) {
                System.out.println("\nAvailable Officers for Assignment (Recommended Department Listed First):");
                System.out.println("+------------+----------------------+------------------+------------+-----------------+");
                System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n", "Officer ID", "Name", "Department", "Station ID", "Officer Status");
                System.out.println("+------------+----------------------+------------------+------------+-----------------+");

                boolean hasAvail = false;
                while (av_rs.next()) {
                    hasAvail = true;
                    System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n",
                        av_rs.getString("OfficerID"),
                        av_rs.getString("Name"),
                        av_rs.getString("Department"),
                        av_rs.getString("StationID") != null ? av_rs.getString("StationID") : "N/A",
                        av_rs.getString("OfficerStatus") != null ? av_rs.getString("OfficerStatus") : "N/A"
                    );
                }
                if (!hasAvail) {
                    System.out.println("|                      No available officers found.                           |");
                }
                System.out.println("+------------+----------------------+------------------+------------+-----------------+\n");
            }
        }

        System.out.print("Enter Updating Officer ID: " + prefixO);
        String officerSuffix = sc.next();
        sc.nextLine(); // Clear buffer
        String OffID = prefixO + officerSuffix;

        Database.con.setAutoCommit(false);

        String UpdateOffID="update case_details set OfficerID=? , CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUOffID= Database.getConnection().prepareStatement(UpdateOffID);
        QUOffID.setString(1,OffID);
        QUOffID.setString(2,c_id);
        int UOID=QUOffID.executeUpdate();

        /**Updating Criminal's officer Id....**/

        String UpOffIdCriD="update criminal_details set InvestingOfficerID=?, CaseStatus='Investigating' where CaseID=?";
        PreparedStatement QUOffCriD= Database.getConnection().prepareStatement(UpOffIdCriD);
        QUOffCriD.setString(1,OffID);
        QUOffCriD.setString(2,c_id);
        int UOffCri=QUOffCriD.executeUpdate();

        /**Updating officers Investing CaseId.....**/

        String UpCaIdOff="update officer_details set AssignedCase=? , CaseStatus='Investigating', OfficerStatus='Active' where OfficerID=?";
        PreparedStatement QUCaIOff= Database.getConnection().prepareStatement(UpCaIdOff);
        QUCaIOff.setString(1,c_id);
        QUCaIOff.setString(2,OffID);
        int UCIOff=QUCaIOff.executeUpdate();

        if (UOID>0 && UOffCri>0 && UCIOff>0)
        {
            System.out.println("[UPDATED] Officer ID updated successfully...");
            Database.con.commit();
            QUOffID.close();
            QUOffCriD.close();
            QUCaIOff.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Officer ID....Try again...");
            Database.con.rollback();
            QUOffID.close();
            QUOffCriD.close();
            QUCaIOff.close();
        }
    }

    void UpdateCType(String c_id) throws Exception
    {
        String CType=v.readAlphaString("Enter Updating Case Type: ");

        Database.con.setAutoCommit(false);

        String UpdateCType="update case_details set CaseType=? where CaseID=?";
        PreparedStatement QUCType= Database.getConnection().prepareStatement(UpdateCType);
        QUCType.setString(1,CType);
        QUCType.setString(2,c_id);
        int UCT=QUCType.executeUpdate();

        String UpCType="update criminal_details set CrimeType=? where CaseID=?";
        PreparedStatement QUpCType= Database.getConnection().prepareStatement(UpCType);
        QUpCType.setString(1,CType);
        QUpCType.setString(2,c_id);
        int UpCT=QUpCType.executeUpdate();

        if (UCT>0 && UpCT>0)
        {
            System.out.println("[UPDATED] Case Type updated successfully...");
            Database.con.commit();
            QUCType.close();
            QUpCType.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Type....Try again...");
            Database.con.rollback();
            QUCType.close();
            QUpCType.close();
        }
    }

    void UpdateWeapon(String c_id) throws Exception
    {
        String CWeapon=v.readAlphaString("Enter Updating Crime Weapon: ");

        Database.con.setAutoCommit(false);

        String UpdateCWeapon="update case_details set CrimeWeapon=? where CaseID=?";
        PreparedStatement QUCWeapon= Database.getConnection().prepareStatement(UpdateCWeapon);
        QUCWeapon.setString(1,CWeapon);
        QUCWeapon.setString(2,c_id);
        int UCW=QUCWeapon.executeUpdate();

        if (UCW>0)
        {
            System.out.println("[UPDATED] Crime Weapon updated successfully...");
            Database.con.commit();
            QUCWeapon.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Crime Weapon....Try again...");
            Database.con.rollback();
            QUCWeapon.close();
        }
    }

    void UpdateSuspect(String c_id) throws Exception
    {
        String SName=v.readAlphaString("Enter Updating Suspect Name: ");

        Database.con.setAutoCommit(false);

        String UpdateSuspect="update case_details set SuspectName=? where CaseID=?";
        PreparedStatement QUSuspect= Database.getConnection().prepareStatement(UpdateSuspect);
        QUSuspect.setString(1,SName);
        QUSuspect.setString(2,c_id);
        int USN=QUSuspect.executeUpdate();

        String UpCriName="update criminal_details set Name=? where CaseID=?";
        PreparedStatement QUCriN= Database.getConnection().prepareStatement(UpCriName);
        QUCriN.setString(1,SName);
        QUCriN.setString(2,c_id);
        int UCriN=QUCriN.executeUpdate();

        String UpCriPicName="update Criminal_Pictures set CriminalName=? where CaseID=?";
        PreparedStatement QUCriPicN= Database.getConnection().prepareStatement(UpCriPicName);
        QUCriPicN.setString(1,SName);
        QUCriPicN.setString(2,c_id);
        int UCriPicN=QUCriPicN.executeUpdate();

        if (USN>0 && UCriN>0 && UCriPicN>=0)
        {
            System.out.println("[UPDATED] Suspect Name updated successfully...");
            Database.con.commit();
            QUSuspect.close();
            QUCriN.close();
            QUCriPicN.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Suspect Name....Try again...");
            Database.con.rollback();
            QUSuspect.close();
            QUCriN.close();
            QUCriPicN.close();
        }
    }

    void UpdateVictim(String c_id) throws Exception
    {
        String VName=v.readAlphaString("Enter Updating Victim Name: ");

        Database.con.setAutoCommit(false);

        String UpdateVictim="update case_details set VictimName=? where CaseID=?";
        PreparedStatement QUVictim= Database.getConnection().prepareStatement(UpdateVictim);
        QUVictim.setString(1,VName);
        QUVictim.setString(2,c_id);
        int UVN=QUVictim.executeUpdate();

        if (UVN>0)
        {
            System.out.println("[UPDATED] Victim Name updated successfully...");
            Database.con.commit();
            QUVictim.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Victim Name....Try again...");
            Database.con.rollback();
            QUVictim.cancel();
        }
    }

    void UpdateDesc(String c_id) throws Exception
    {
        String CDesc=v.readAlphaString("Enter Updating Case Description: ");

        Database.con.setAutoCommit(false);

        String UpdateCDesc="update case_details set CrimeDetails=? where CaseID=?";
        PreparedStatement QUCDesc= Database.getConnection().prepareStatement(UpdateCDesc);
        QUCDesc.setString(1,CDesc);
        QUCDesc.setString(2,c_id);
        int UCD=QUCDesc.executeUpdate();

        if (UCD>0)
        {
            System.out.println("[UPDATED] Case Description updated successfully...");
            Database.con.commit();
            QUCDesc.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Description....Try again...");
            Database.con.rollback();
            QUCDesc.cancel();
        }
    }

    void UpdateStatus(String c_id) throws Exception
    {
        String CStatus=v.readAlphaString("Enter Updating Case Status: ");

        Database.con.setAutoCommit(false);

        String UpdateCStatus="update case_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCStatus= Database.getConnection().prepareStatement(UpdateCStatus);
        QUCStatus.setString(1,CStatus);
        QUCStatus.setString(2,c_id);
        int UCS=QUCStatus.executeUpdate();

        String UpCStCri="update criminal_details set CaseStatus=? where CaseID=?";
        PreparedStatement QUCSCri= Database.getConnection().prepareStatement(UpCStCri);
        QUCSCri.setString(1,CStatus);
        QUCSCri.setString(2,c_id);
        int UCSCri=QUCSCri.executeUpdate();

        String UpCSOff="update officer_details set CaseStatus=? where AssignedCase=?";
        PreparedStatement QUCSOff= Database.getConnection().prepareStatement(UpCSOff);
        QUCSOff.setString(1,CStatus);
        QUCSOff.setString(2,c_id);
        int UCSOff=QUCSOff.executeUpdate();

        if (UCS>0 && UCSCri>0 && UCSOff>0)
        {
            System.out.println("[UPDATED] Case Status updated successfully...");
            Database.con.commit();
            QUCStatus.close();
            QUCSOff.close();
            QUCSCri.close();
        }
        else
        {
            System.err.println("[ERROR] Failed to update Case Status...Try again...");
            Database.con.rollback();
            QUCStatus.close();
            QUCSOff.close();
            QUCSCri.close();
        }
    }

    public void CrimeRatio() throws Exception
    {
        String CrimeRatioQuery="select CaseType, count(*) as TotalCases from case_details group by CaseType";
        PreparedStatement QCR= Database.getConnection().prepareStatement(CrimeRatioQuery);
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
        PreparedStatement QAPCase= Database.getConnection().prepareStatement(AssPCase);
        ResultSet PCase_rs=QAPCase.executeQuery();
        if (PCase_rs.next())
        {
            String case_id=PCase_rs.getString(1);
            QAPCase.close();

            // Fetch CaseType to determine recommended department
            String recDept = "";
            String getCaseType = "select CaseType from case_details where CaseID=?";
            try (PreparedStatement qct = Database.getConnection().prepareStatement(getCaseType)) {
                qct.setString(1, case_id);
                try (ResultSet rsct = qct.executeQuery()) {
                    if (rsct.next()) {
                        String caseType = rsct.getString("CaseType");
                        String ctLower = caseType.toLowerCase();
                        if (ctLower.contains("cyber")) {
                            recDept = "Cyber Cell";
                        } else if (ctLower.contains("robbery") || ctLower.contains("murder") || ctLower.contains("drug")) {
                            recDept = "Crime Branch";
                        } else if (ctLower.contains("kidnap")) {
                            recDept = "Women Cell";
                        } else if (ctLower.contains("traffic")) {
                            recDept = "Traffic";
                        }
                    }
                }
            }

            System.out.println("\nRecommended Department for this Crime: " + (recDept.isEmpty() ? "Any" : recDept));

            // Query available officers sorted by recommended department first
            String AvailOffQuery = "select OfficerID, Name, Department, StationID, OfficerStatus from officer_details " +
                                   "where AssignedCase is null or AssignedCase = '' or lower(CaseStatus) = 'solved' " +
                                   "order by case when Department = ? then 0 else 1 end, Department, Name";
            PreparedStatement QAvail = Database.getConnection().prepareStatement(AvailOffQuery);
            QAvail.setString(1, recDept);
            ResultSet av_rs = QAvail.executeQuery();

            System.out.println("\nAvailable Officers for Assignment (Recommended Department Listed First):");
            System.out.println("+------------+----------------------+------------------+------------+-----------------+");
            System.out.printf("| %-10s | %-20s | %-16s | %-10s | %-15s |\n", "Officer ID", "Name", "Department", "Station ID", "Officer Status");
            System.out.println("+------------+----------------------+------------------+------------+-----------------+");

            boolean hasAvail = false;
            while(av_rs.next())
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
            av_rs.close();
            QAvail.close();

            System.out.println("Do you want to Assign this Case to any Officer???(yes/no)");
            System.out.print(">>> ");
            char ans=sc.next().charAt(0);

            if (ans=='Y'||ans=='y')
            {
                UpdateOffID(case_id);
                UpdateCType(case_id);
                PCS.Dequeue();
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