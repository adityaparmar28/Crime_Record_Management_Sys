import Profile.Login_SignUpPage;
import Profile.User;
import Layout.CRMngr;
import Layout.CrimeMngr;
import Layout.DIG;
import Layout.Investigation;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        Login_SignUpPage loginPage = new Login_SignUpPage();

        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|               WELCOME TO CRIME RECORD MANAGEMENT SYSTEM              |");
        System.out.println("+----------------------------------------------------------------------+");

        boolean running = true;
        while (running)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                             MAIN MENU                                |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("1. User Login");
            System.out.println("2. User SignUp");
            System.out.println("3. Exit");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");

            int choice = 0;
            try
            {
                choice = sc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] Invalid input. Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            switch (choice)
            {
                case 1:
                {
                    try
                    {
                        boolean loggedIn = loginPage.userLogin();
                        if (loggedIn)
                        {
                            showDashboard(sc);
                        }
                    }
                    catch (Exception e)
                    {
                        System.err.println("[ERROR] Login error: " + e.getMessage());
                    }
                    break;
                }

                case 2:
                {
                    try
                    {
                        loginPage.SignUp();
                        loginPage.assign();
                    }
                    catch (Exception e)
                    {
                        System.err.println("[ERROR] Sign Up error: " + e.getMessage());
                    }
                    break;
                }

                case 3:
                {
                    System.out.println("Exiting System... Goodbye!");
                    running = false;
                    break;
                }

                default:
                {
                    System.err.println("[ERROR] Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void showDashboard(Scanner sc)
    {
        CRMngr crMngr = new CRMngr();
        CrimeMngr crimeMngr = new CrimeMngr();
        DIG dig = new DIG();
        Investigation investigation = new Investigation();
        User userProfile = new User();

        boolean inDashboard = true;
        while (inDashboard)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                         CRMS SYSTEM DASHBOARD                        |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("1. Crime Management (FIR & Cases)");
            System.out.println("2. Crime Record Manager (Criminals)");
            System.out.println("3. Directory of Police Officers (DIG)");
            System.out.println("4. Crime Investigation Bureau (Pending Cases)");
            System.out.println("5. User Profile");
            System.out.println("6. Logout");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");

            int ch = 0;
            try
            {
                ch = sc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] Invalid input. Please enter a valid number.");
                sc.nextLine();
                continue;
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
                        dig.DirectoryOfPoliceOfficer();
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
                        System.out.println("Logged out successfully.");
                        inDashboard = false;
                        break;
                    }

                    default:
                    {
                        System.err.println("[ERROR] Invalid choice.");
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