package Quries;

import DataBase.DataBase;
import Profile.Login_SignUpPage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserQuries
{
    Scanner sc=new Scanner(System.in);
    Login_SignUp_Queries LSQ=new Login_SignUp_Queries();
    DataBase db=new DataBase();
    OODataQueries OODQ=new OODataQueries();

    public void ChangePassword() throws Exception
    {
        //Forget Passwd and Change Passwd both logic are Same....
        LSQ.ForgetPassword();
    }

    public void Profile() throws Exception
    {
        String Profile = "SELECT Users_name,UserId,Role,EmailId,MobileNo,DOB FROM users WHERE UserId=?";

        PreparedStatement QPro = db.getConnection().prepareStatement(Profile);

        QPro.setString(1,Login_SignUpPage.LoggedUserID);

        ResultSet rs=QPro.executeQuery();

        if(rs.next())
        {
            System.out.println("+---------------- USER PROFILE ----------------+");
            System.out.println("User ID     : "+rs.getString("UserId"));
            System.out.println("Name        : "+rs.getString("Users_name"));
            System.out.println("Role        : "+rs.getString("Role"));
            System.out.println("Email       : "+rs.getString("EmailId"));
            System.out.println("Mobile      : "+rs.getString("MobileNo"));
            System.out.println("DOB         : "+rs.getDate("DOB"));
            System.out.println("+----------------------------------------------+");
        }
        else
        {
            System.out.println("[ERROR] User not found.");
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
                column="Users_name";
                break;

            case 2:
                column="EmailId";
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

        String sql="UPDATE users SET "+column+"=? WHERE UserId=?";

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
