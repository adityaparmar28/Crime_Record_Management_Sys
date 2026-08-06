package Layout;

import Quries.IAQueries;
import java.util.Scanner;

public class Investigation
{
    Scanner sc=new Scanner(System.in);
    DIG dig=new DIG();
    IAQueries IAQ=new IAQueries();

    public void Investigation_Menu() throws Exception
    {
        boolean isIML=false;

        while (!isIML)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                      CRIME INVESTIGATION BEUERO                      |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println();
            System.out.println("1. View Pending Cases");
            System.out.println("2. Update Investigation Status");
            System.out.println("3. Home Page");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");
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
                    System.err.println("[ERROR] Invalid number format. Please enter a valid choice....");
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
        boolean isLoggedIn = dig.DIGLogin();
        //if DIGLogin success then update data....
        //only DIG and police officer can  status of the case....
        //Only dig can assign officer to the case....
        // Implementation for updating investigation status....
        //if case solved then update case investing status as solved....
        IAQ.UpdateInvesting();
    }
}