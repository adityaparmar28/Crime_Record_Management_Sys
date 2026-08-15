package Layout;

import Profile.Login_SignUpPage;
import Profile.User;

import java.util.Scanner;

public class Dashboard
{
    public Dashboard()
    {
        MainMenu();
    }

    void MainMenu()
    {
        CRMngr crMngr = new CRMngr();
        CrimeMngr crimeMngr = new CrimeMngr();
        DIG dig = new DIG();
        Investigation investigation = new Investigation();
        User userProfile = new User();
        Scanner sc=new Scanner(System.in);
        Login_SignUpPage LS=new Login_SignUpPage();

        boolean inDashboard = true;
        while (inDashboard)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                         CRMS SYSTEM DASHBOARD                        |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("| 1. Crime Management (FIR & Cases)");
            System.out.println("| 2. Crime Record Manager (Criminals)");
            System.out.println("| 3. Directory of Police Officers (DIG)");
            System.out.println("| 4. Crime Investigation Bureau (Pending Cases)");
            System.out.println("| 5. User Profile");
            System.out.println("| 6. Logout");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("| Enter Your Choice: ");

            int ch;

            try
            {
                ch = sc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] Invalid input....Please enter a valid number....");
                System.out.println();
                break;
            }

            try
            {
                switch (ch)
                {
                    case 1:
                    {
                        crimeMngr.CrimeManagement_Menu();
                        break;
                    }

                    case 2:
                    {
                        crMngr.CrimeRecordManager();
                        break;
                    }

                    case 3:
                    {
                        System.out.println("+----------------------------------------------------------------------+");
                        System.out.println("|                DIRECTORY POLICE OFFICER LOGIN PAGE                   |");
                        System.out.println("+----------------------------------------------------------------------+");

                        if(LS.userLogin())
                        {
                            dig.DirectoryOfPoliceOfficer();
                        }
                        else
                        {
                            System.err.println("[ERROR] Invalid Officer Credential.....");
                            return;
                        }
                        break;
                    }

                    case 4:
                    {
                        investigation.Investigation_Menu();
                        break;
                    }

                    case 5:
                    {
                        userProfile.userMenu();
                        break;
                    }

                    case 6:
                    {
                        Login_SignUpPage.LoggedUserID="";
                        System.out.println("Logged out successfully....");
                        inDashboard = false;
                        break;
                    }

                    default:
                    {
                        System.err.println("[ERROR] Invalid choice....");
                        System.out.println();
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] System error: " + e.getMessage());
            }
        }
    }
}
