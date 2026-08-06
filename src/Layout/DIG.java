package Layout;

import Profile.Login_SignUpPage;
import Quries.DIGQueries;
import Quries.Login_SignUp_Queries;
import java.util.Scanner;

public class DIG
{
    Scanner sc=new Scanner(System.in);
    DIGQueries DIGQ=new DIGQueries();
    Login_SignUp_Queries LSQ=new Login_SignUp_Queries();
    Login_SignUpPage LS=new Login_SignUpPage();

    public void DirectoryOfPoliceOfficer() throws Exception
    {
        boolean isDIGML=false;

        while (!isDIGML)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                    DIRECTORY OF POLICE OFFICER                       |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println();
            System.out.println("1. Add Police Officer Record");
            System.out.println("2. Search Police Officer");
            System.out.println("3. List of All Police Officers");
            System.out.println("4. Update Police Officer Record");
            System.out.println("5. Home Page");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");
            int dig_ch = sc.nextInt();

            switch (dig_ch)
            {
                case 1:
                {
                    // Add Police Officer Record
                    addPoliceOfficerRecord();
                    break;
                }

                case 2:
                {
                    // Search Police Officer
                    searchPoliceOfficer();
                    break;
                }

                case 3:
                {
                    // List of All Police Officers
                    AllPoliceOfficers();
                    break;
                }

                case 4:
                {
                    // Update Police Officer Record
                    UpdatePoliceOfficerRecord();
                    break;
                }

                case 5:
                {
                    // Home Page
                    isDIGML=true;
                    return;
                }

                default:
                {
                    System.err.println("[ERROR] Invalid number format. Please enter a valid choice....");
                }
            }
        }
    }

    public boolean DIGLogin() throws Exception
    {
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|                          DIG LOGIN PAGE                              |");
        System.out.println("+----------------------------------------------------------------------+");

        if(LS.userLogin())
        {
            //check credential if false then show forgot user credential option....
            //Login Functionality through Query....
            return true;
        }
        else
        {
            return false;
        }
    }

    void addPoliceOfficerRecord() throws Exception
    {
        //Adding Police Officer Record through Query....
        DIGQ.AddPoliceRecord();
    }

    void searchPoliceOfficer() throws Exception
    {
        //Searching Police Officer through Query with same logic of crime and criminal....
        DIGQ.SearchOfficer();
    }

    void AllPoliceOfficers() throws Exception
    {
        //List of All Police Officers through Query....
        DIGQ.AllPoliceOfficers();

    }

    void UpdatePoliceOfficerRecord() throws Exception
    {
        //Updating Police Officer Record through Query....
        DIGQ.UpdatePoliceRecord();
    }
}