package Layout;

import Profile.Login_SignUpPage;
import Quries.IAQueries;

import java.util.Scanner;

public class Investigation
{
    Scanner sc=new Scanner(System.in);
    DIG dig=new DIG();
    IAQueries IAQ=new IAQueries();
    Login_SignUpPage LSP=new Login_SignUpPage();

    public void Investigation_Menu() throws Exception
    {
        boolean isIML=false;

        while (!isIML)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                      CRIME INVESTIGATION BEUERO                      |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("| 1. View Pending Cases");
            System.out.println("| 2. Update Investigation Status");
            System.out.println("| 3. Home Page");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("| Enter Your Choice: ");
            int invMenu_ch = sc.nextInt();

            switch (invMenu_ch)
            {
                case 1:
                {
                    // View Pending Cases functionality
                    ViewPendingCases();
                    break;
                }

                case 2:
                {
                    // Update Investigation Status functionality
                    UpdateInvestigationStatus();
                    break;
                }

                case 3:
                {
                    // Home Page
                    isIML=true;
                    return;
                }

                default:
                {
                    System.err.println("[ERROR] Invalid Choice....Enter a valid choice....");
                }
            }
        }
    }

    void ViewPendingCases() throws Exception
    {
        // Implementation for viewing pending unsolved cases....
        IAQ.PendingCases();
    }

    void UpdateInvestigationStatus() throws Exception
    {
        if (Login_SignUpPage.LoggedUserID.equals(""))
        {
            System.out.println("[INFO] You must be logged in to update investigation status....Log in first....");

            if (!LSP.DGPLogin())
            {
                System.err.println("[ERROR] Login failed.....Returning....");
                return;
            }
        }

        if (!(Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Admin") ||
                Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Officer")))
        {
            System.err.println("[WARNING] Only Directory of Police Officers (Admin/Officer) can update investigation status....");
            return;
        }

        IAQ.UpdateInvesting();
    }
}