package Quries;

import DataBase.DataBase;
import Profile.Login_SignUpPage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login_SignUp_Queries
{
    Scanner sc=new Scanner(System.in);
    DataBase db=new DataBase();
    String role="Citizen";

    public boolean SignUpQuery(String name,String email,String mobile,String passwd,String dob,String userID) throws Exception
    {
        String SignUp = "INSERT INTO users (Users_name,EmailId,MobileNo,Password,DOB,UserId,Role) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement QSU = db.getConnection().prepareStatement(SignUp);

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
            return true;
        }

        System.err.println("[FAILED] User SigningUp Failed....");
        return false;
    }

    public boolean isUSer(String uId,String pass) throws Exception
    {
        String isUser = "SELECT COUNT(*) FROM users WHERE UserID = ?";
        PreparedStatement QiU = db.getConnection().prepareStatement(isUser);
        QiU.setString(1, uId);

        ResultSet isU_rs = QiU.executeQuery();

        isU_rs.next();

        int count = isU_rs.getInt(1);

        if (count > 0)
        {
            return true;
        }
        else
        {
            System.err.println("[NOT FOUND] User Not Found....");
            return false;
        }
    }

    public boolean LoginQuery(String uId,String pass) throws Exception
    {
        String Login =
                "SELECT UserId FROM users WHERE UserId=? AND Password=?";

        PreparedStatement Qlogin =
                db.getConnection().prepareStatement(Login);

        Qlogin.setString(1,uId);
        Qlogin.setString(2,pass);

        ResultSet rs=Qlogin.executeQuery();

        if(rs.next())
        {
            Login_SignUpPage.LoggedUserID=uId;

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
        String pass="";

        //>>>Input of mobile number and give oldpass as otp with timeout thread if both match then update the password....
        //>>>it is pending....

        if(isUSer(uId,pass))
        {
            System.out.println("Enter your new Password: ");
            String pass2 = sc.next();

            String ForgetPass = "update users set Password=? where UserId=?";
            PreparedStatement QFPass = db.getConnection().prepareStatement(ForgetPass);
            QFPass.setString(1, pass2);
            QFPass.setString(2, uId);
            int run = QFPass.executeUpdate();

            if (run > 0)
            {
                System.out.println("[DONE'] Password Updated Successfully....");
                return;
            }
            else
            {
                System.err.println("[FAILED] Password Updation Failed....");
                return;
            }
        }
    }

}
