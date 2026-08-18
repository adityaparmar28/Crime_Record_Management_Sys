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
import Profile.Login_SignUpPage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login_SignUp_Queries
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();
    DataFound found=new DataFound();

    String role="Citizen";

    public boolean SignUpQuery(String name,String email,String mobile,String passwd,String dob,String userID) throws Exception
    {
        if (!APIs.Captcha.verifyCaptcha()) {
            System.err.println("[FAILED] CAPTCHA verification failed. Signing up cancelled.");
            return false;
        }

        String SignUp = "INSERT INTO users (UsersName,EmailID,MobileNo,Password,DOB,UserID,Role) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement QSU = Database.getConnection().prepareStatement(SignUp);

        QSU.setString(1, name);
        QSU.setString(2, email);
        QSU.setString(3, mobile);
        QSU.setString(4, passwd);
        QSU.setDate(5, java.sql.Date.valueOf(dob));
        QSU.setString(6, userID);
        QSU.setString(7, Login_SignUpPage.Role);

        int run = QSU.executeUpdate();
        QSU.close();

        if(run>0)
        {
            System.out.println("[UPDATED] User SignedUp Successfully....");
            APIs.TimeStamp.resetTimer();
            DataStructure.DataStructure.ActivityLog.push("Signed up new user: " + userID);
            return true;
        }

        System.err.println("[FAILED] User SigningUp Failed....");
        return false;
    }

    public boolean LoginQuery(String uId,String pass) throws Exception
    {
        String Login = "SELECT UserID,Role FROM users WHERE UserID=? AND Password=?";

        PreparedStatement Qlogin = Database.getConnection().prepareStatement(Login);

        Qlogin.setString(1,uId);
        Qlogin.setString(2,pass);
        ResultSet rs=Qlogin.executeQuery();

        if(rs.next())
        {
            Login_SignUpPage.setLoggedUserID(uId);
            Login_SignUpPage.setLoggedUserRole(rs.getString("Role"));
            APIs.TimeStamp.resetTimer();

            DataStructure.DataStructure.ActivityLog.push("Logged in user: " + uId + " (" + Login_SignUpPage.getLoggedUserRole() + ")");
            rs.close();
            Qlogin.close();
            return true;
        }

        rs.close();
        Qlogin.close();

        System.err.println("[INCORRECT] Invalid UserID or Password....");
        return false;
    }

    public void ForgetPassword() throws Exception
    {
        System.out.println("Enter your User ID or Email ID: ");
        String uId=sc.next();

        if(found.isUSer(uId))
        {
            System.out.println("Enter your new Password: ");
            String pass2 = sc.next();

            String ForgetPass = "update users set Password=? where UserID=? or EmailID=?";
            PreparedStatement QFPass = Database.getConnection().prepareStatement(ForgetPass);
            QFPass.setString(1, pass2);
            QFPass.setString(2, uId);
            QFPass.setString(3, uId);

            if (!APIs.OTP.sendAndVerifyOTP())
            {
                System.out.println("[FAILED] Verification failed. Password update cancelled.");
                QFPass.close();
                return;
            }

            int run = QFPass.executeUpdate();

            if (run > 0)
            {
                System.out.println("[DONE] Password Updated Successfully....");
                DataStructure.DataStructure.ActivityLog.push("Updated password for user: " + uId);
                QFPass.close();
            }
            else
            {
                System.err.println("[FAILED] Password Updation Failed....");
                QFPass.close();
            }
        }
    }
}
