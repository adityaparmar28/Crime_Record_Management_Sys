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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Queue;
import java.util.Scanner;

public class IAQueries
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();
    OODataQueries OODQ=new OODataQueries();
    CrimeMngrQueries CMQ=new CrimeMngrQueries();
    Queue<String> PCS=DataStructure.PendingCase;
    Validation v=new Validation();
    DataFound found=new DataFound();

    public void PendingCases() throws Exception
    {
        PCS.clear();
        String PenCases="select * from case_details where CaseStatus is null or lower(CaseStatus)=?";
        PreparedStatement QPCase=db.getConnection().prepareStatement(PenCases);
        QPCase.setString(1,"pending");
        ResultSet PCase_rs=QPCase.executeQuery();

        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                                  PENDING CASES LIST                                                |");
        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");

        boolean flag = false;
        while(PCase_rs.next())
        {
            flag = true;
            String caseId = PCase_rs.getString("CaseID");
            String caseName = PCase_rs.getString("CaseName");
            String caseType = PCase_rs.getString("CaseType");
            String location = PCase_rs.getString("CrimeLocation");
            String weapon = PCase_rs.getString("CrimeWeapon");
            String suspect = PCase_rs.getString("SuspectName");
            String victim = PCase_rs.getString("VictimName");
            String desc = PCase_rs.getString("CrimeDetails");
            String status = PCase_rs.getString("CaseStatus");

            String caseDetails = String.format
                    ("Case ID: %s | Name: %s | Type: %s | Location: %s | Suspect: %s | Victim: %s | Status: %s",
                    caseId, (caseName != null ? caseName : "N/A"),
                            (caseType != null ? caseType : "N/A"),
                    (location != null ? location : "N/A"),
                            (suspect != null ? suspect : "N/A"),
                    (victim != null ? victim : "N/A"),
                            (status != null ? status : "Pending")
                    );

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

        if (!flag)
        {
            System.out.println("[INFO] No pending cases found.");
        }

        System.out.println("+-------------------------------------------------------------------------------------------------------------------+");

        PCase_rs.close();
        QPCase.close();

        CMQ.AssignPCase();
    }

    public void UpdateInvesting() throws Exception
    {
        String caseID = v.readNonEmptyString("Enter Investigating Case ID: ");

        if(!found.isCaseFound(caseID))
        {
            System.out.println("[ERROR] Case ID not found in database....");
            return;
        }

        System.out.println("Which data do you want to Update in Investigation???");
        System.out.println("[INFO] CaseStatus,SuspectName,CaseName,CrimeWeapon are Valid....");
        System.out.print(">>> ");
        String UpColummName=sc.next();

        String TableName="case_details";

        System.out.print("Enter Updating "+UpColummName+": ");

        Object UpdatingValue=OODQ.SQLDType2JDType(UpColummName,TableName);

        db.con.setAutoCommit(false);

        String UpdateIn="update "+TableName+" set "+UpColummName+"=? where CaseID=?";
        PreparedStatement QUIn=db.getConnection().prepareStatement(UpdateIn);
        QUIn.setObject(1, UpdatingValue);
        QUIn.setString(2,caseID);
        int run = QUIn.executeUpdate();

        int runCri = 1;
        int runOff = 1;
        int runPic = 1;
        PreparedStatement QUInCri = null;
        PreparedStatement QUInOff = null;
        PreparedStatement QUInPic = null;

        if (run > 0)
        {
            if (UpColummName.equalsIgnoreCase("CaseStatus"))
            {
                String valStr = String.valueOf(UpdatingValue);

                String UpdateCri = "update criminal_details set CaseStatus=? where CaseID=?";
                QUInCri = db.getConnection().prepareStatement(UpdateCri);
                QUInCri.setString(1, valStr);
                QUInCri.setString(2, caseID);
                runCri = QUInCri.executeUpdate();

                String UpdateOff = "update officer_details set CaseStatus=? where AssignedCase=?";
                QUInOff = db.getConnection().prepareStatement(UpdateOff);
                QUInOff.setString(1, valStr);
                QUInOff.setString(2, caseID);
                runOff = QUInOff.executeUpdate();
            }
            else if (UpColummName.equalsIgnoreCase("SuspectName"))
            {
                String valStr = String.valueOf(UpdatingValue);

                String UpdateCri = "update criminal_details set Name=? where CaseID=?";
                QUInCri = db.getConnection().prepareStatement(UpdateCri);
                QUInCri.setString(1, valStr);
                QUInCri.setString(2, caseID);
                runCri = QUInCri.executeUpdate();

                String UpdatePic = "update Criminal_Pictures set CriminalName=? where CaseID=?";
                QUInPic = db.getConnection().prepareStatement(UpdatePic);
                QUInPic.setString(1, valStr);
                QUInPic.setString(2, caseID);
                runPic = QUInPic.executeUpdate();
            }
        }

        if (run > 0 && runCri >= 0 && runOff >= 0 && runPic >= 0)
        {
            System.out.println("[UPDATED] Investigation details updated successfully...");
            db.con.commit();
        }
        else
        {
            System.err.println("[FAILED] Investigation details update failed...");
            db.con.rollback();
        }

        if (QUIn != null) QUIn.close();
        if (QUInCri != null) QUInCri.close();
        if (QUInOff != null) QUInOff.close();
        if (QUInPic != null) QUInPic.close();
    }
}