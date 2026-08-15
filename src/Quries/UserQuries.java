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

        PreparedStatement QPro = db.getConnection().prepareStatement(Profile);

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

        PreparedStatement ps=
                db.getConnection().prepareStatement(sql);

        ps.setObject(1,value);
        ps.setString(2,LoggedId);

        int run=ps.executeUpdate();

        if(run>0)
            System.out.println("Profile Updated Successfully.");
        else
            System.out.println("Profile Update Failed.");

        ps.close();
    }
}