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

import DataBase.Database;
import Profile.Login_SignUpPage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserQuries
{
    Scanner sc=new Scanner(System.in);
    Login_SignUp_Queries LSQ=new Login_SignUp_Queries();
    Database db=new Database();
    OODataQueries OODQ=new OODataQueries();

    private boolean checkLoggedIn()
    {
        String loggedUserId = Login_SignUpPage.getLoggedUserID();
        String loggedUserRole = Login_SignUpPage.getLoggedUserRole();

        if (loggedUserId.equals("") || loggedUserRole.equalsIgnoreCase("Guest"))
        {
            System.err.println("[ERROR] Only logged user profile can be viewed. Login first....");
            return false;
        }
        return true;
    }

    public void ChangePassword() throws Exception
    {
        if (!checkLoggedIn())
        {
            return;
        }
        LSQ.ForgetPassword();
    }

    public void Profile() throws Exception
    {
        if (!checkLoggedIn())
        {
            return;
        }
        if (!APIs.OTP.sendAndVerifyOTP())
        {
            System.err.println("[FAILED] Verification failed....Access to profile denied....");
            return;
        }

        DataStructure.DataStructure.ActivityLog.push("Viewed user profile for user: " + Login_SignUpPage.getLoggedUserID());

        String Profile = "SELECT UsersName,UserID,Role,EmailID,MobileNo,DOB FROM users WHERE UserID=?";
        PreparedStatement QPro = Database.getConnection().prepareStatement(Profile);
        QPro.setString(1,Login_SignUpPage.getLoggedUserID());
        ResultSet rs=QPro.executeQuery();

        if(rs.next())
        {
            System.out.println("+---------------- USER PROFILE ----------------+");
            System.out.println("User ID     : "+rs.getString("UserID"));
            System.out.println("Name        : "+rs.getString("UsersName"));
            System.out.println("Role        : "+rs.getString("Role"));
            System.out.println("Email       : "+rs.getString("EmailID"));
            System.out.println("Mobile      : "+rs.getString("MobileNo"));
            System.out.println("DOB         : "+rs.getDate("DOB"));
            System.out.println("+----------------------------------------------+");
        }
        else
        {
            System.out.println("[INFO] User not found....");
        }

        rs.close();
        QPro.close();
    }

    public void UpdateProfile(String LoggedId) throws Exception
    {
        if (!checkLoggedIn())
        {
            return;
        }
        System.out.println("| 1.Name");
        System.out.println("| 2.Email");
        System.out.println("| 3.Mobile");
        System.out.println("-------------------");
        System.out.println("| Select Field: ");
        int ch=sc.nextInt();

        String column="";

        switch(ch)
        {
            case 1:
            {
                column = "UsersName";
                break;
            }

            case 2:
            {
                column = "EmailID";
                break;
            }

            case 3:
            {
                column = "MobileNo";
                break;
            }

            default:
            {
                System.err.println("[INVALID] Invalid Choice....");
                return;
            }
        }

        System.out.print("Enter New Value : ");

        Object value=OODQ.SQLDType2JDType(column,"users");

        String sql="UPDATE users SET "+column+"=? WHERE UserID=?";

        PreparedStatement ps= Database.getConnection().prepareStatement(sql);

        ps.setObject(1,value);
        ps.setString(2,LoggedId);

        if (!APIs.OTP.sendAndVerifyOTP())
        {
            System.err.println("[FAILED] Verification failed....Profile update cancelled....");
            ps.close();
            return;
        }

        int run=ps.executeUpdate();

        if(run>0)
        {
            System.out.println("[UPDATED] Profile Updated Successfully....");
            DataStructure.DataStructure.ActivityLog.push("Updated profile field '" + column + "' for user: " + LoggedId);
        }
        else
        {
            System.err.println("[ERROR] Profile Update Failed....");
        }

        ps.close();
    }
}