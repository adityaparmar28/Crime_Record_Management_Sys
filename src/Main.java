import DataBase.Database;
import Layout.Dashboard;
import Profile.Login_SignUpPage;

import java.util.Scanner;

class CRMS
{
    public static void main(String[] args) throws Exception
    {
        Database DB=new Database();
        DB.DefaultDataBase();
        Login_SignUpPage LSP=new Login_SignUpPage();
        Scanner sc=new Scanner(System.in);

        boolean pageLoop=false;
        int Page_ch=0;

        while (!pageLoop)
        {
            System.out.println("+---------------------------------------------------+");
            System.out.println("| 1. User Login");
            System.out.println("| 2. Sign Up");
            System.out.println("| 3. Continue without Login or Sign Up");
            System.out.println("| 4. Exit");
            System.out.println("+---------------------------------------------------+");

            try {
                System.out.print("| Enter Choice: ");
                Page_ch = sc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] Invalid Input.....");
                return;
            }

            switch (Page_ch)
            {
                case 1:
                {
                    if(LSP.userLogin())
                    {
                        Dashboard DashB=new Dashboard();
                    }

                    break;
                }

                case 2:
                {
                    LSP.SignUp();

                    if(LSP.userLogin())
                    {
                        Dashboard DashB=new Dashboard();
                    }

                    break;
                }

                case 3:
                {
                    Dashboard DashB=new Dashboard();
                    break;
                }

                case 4:
                {
                    System.out.println("[EXITING..] Exiting Crime Record Management System....");
                    System.exit(0);
                    break;
                }

                default:
                {
                    System.out.println("[INVALID] Invalid Input....");
                    break;
                }
            }
        }
    }
}