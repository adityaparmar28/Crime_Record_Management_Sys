package Profile;

import Quries.Login_SignUp_Queries;
import Quries.UserQuries;
import java.util.Scanner;

public class User
{
    Scanner sc=new Scanner(System.in);
    Login_SignUp_Queries LSQ=new Login_SignUp_Queries();
    Login_SignUpPage LS=new Login_SignUpPage();
    UserQuries UQ=new UserQuries();

    public void userMenu() throws Exception
    {
        boolean isUML=false;

        while (!isUML)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                            USER PROFILE                              |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println();
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Change Password");
            System.out.println("4. User Activity Log");
            System.out.println("5. Home Page");
            System.out.println();
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");
            int user_ch = sc.nextInt();

            switch (user_ch)
            {
                case 1:
                {
                    // View Profile
                    viewProfile();
                    break;
                }

                case 2:
                {
                    // Update Profile
                    updateProfile();
                    break;
                }

                case 3:
                {
                    // Change Password
                    changePassword();
                    break;
                }

                case 4:
                {
                    // User Activity Log
                    userActivityLog();
                    break;
                }

                case 5:
                {
                    // Home Page
                    isUML=true;
                    return;
                }

                default:
                {
                    System.err.println("[ERROR] Invalid number format. Please enter a valid choice....");
                }
            }
        }
    }

    void viewProfile() throws Exception
    {
        //Show last logged in user data throgh query....
        //Aani mate user login na log alag thi create kari emathi last login user details fetch karishu....
        UQ.Profile();
    }

    void updateProfile() throws Exception
    {
        UQ.UpdateProfile(Login_SignUpPage.LoggedUserID);
    }

    void changePassword() throws Exception
    {
        //Change user password throgh query....
        //OPT ya phir Captch fetch karaishu....
        UQ.ChangePassword();
    }

    void userActivityLog() throws Exception
    {
        //Show user activity log throgh query....
        //as text file ma store karishu and fetch karishu....
    }
}
