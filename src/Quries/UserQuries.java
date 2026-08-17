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

    public void ChangePassword() throws Exception
    {
        LSQ.ForgetPassword();
    }

    public void Profile() throws Exception
    {
        String Profile = "SELECT UsersName,UserID,Role,EmailID,MobileNo,DOB FROM users WHERE UserID=?";

        PreparedStatement QPro = Database.getConnection().prepareStatement(Profile);

        QPro.setString(1,Login_SignUpPage.LoggedUserID);

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
            System.out.println("User not found.");
        }

        rs.close();
        QPro.close();
    }

    public void UpdateProfile(String LoggedId) throws Exception
    {
        System.out.println("Select Field");

        System.out.println("1.Name");
        System.out.println("2.Email");
        System.out.println("3.Mobile");

        int ch=sc.nextInt();

        String column="";

        switch(ch)
        {
            case 1:
                column="UsersName";
                break;

            case 2:
                column="EmailID";
                break;

            case 3:
                column="MobileNo";
                break;

            default:
                System.out.println("Invalid Choice");
                return;
        }

        System.out.print("Enter New Value : ");

        Object value=OODQ.SQLDType2JDType(column,"users");

        String sql="UPDATE users SET "+column+"=? WHERE UserID=?";

        PreparedStatement ps= Database.getConnection().prepareStatement(sql);

        ps.setObject(1,value);
        ps.setString(2,LoggedId);

        int run=ps.executeUpdate();

        if(run>0)
        {
            System.out.println("Profile Updated Successfully.");
            DataStructure.DataStructure.ActivityLog.push("Updated profile field '" + column + "' for user: " + LoggedId);
        }
        else
            System.out.println("Profile Update Failed.");

        ps.close();
    }
}