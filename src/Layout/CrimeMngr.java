package Layout;

import Quries.CrimeMngrQueries;
import java.util.Scanner;

public class CrimeMngr
{
    Scanner sc=new Scanner(System.in);
    CrimeMngrQueries CMQ=new CrimeMngrQueries();

    public void CrimeManagement_Menu() throws Exception
    {
        boolean isCMML=false;

        while (!isCMML)
        {
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("|                    CRIME MANAGEMENT SYSTEM                           |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println();
            System.out.println("1. File Crime Report ( FIR )");
            System.out.println("2. Search Crime Report");
            System.out.println("3. List of All Cases");
            System.out.println("4. Update Case Details");
            System.out.println("5. Crime Ratio (OverAll Crime Statistics)");
            System.out.println("6. Home Page");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("Enter Your Choice: ");
            int cmMenu_ch = sc.nextInt();

            switch (cmMenu_ch)
            {
                case 1:
                {
                    // File Crime Report
                    FIR();
                    break;
                }

                case 2:
                {
                    // Search Crime Report
                    SearchCases();
                    break;
                }

                case 3:
                {
                    // List of All Cases
                    ListAllCases();
                    break;
                }

                case 4:
                {
                    // Update Case Details
                    UpdateCaseDetails();
                    break;
                }

                case 5:
                {
                    // Crime Ratio
                    CrimeRatio();
                    break;
                }

                case 6:
                {
                    // Home Page
                    isCMML=true;
                    break;
                }

                default:
                {
                    System.err.println("[ERROR] Invalid number format. Please enter a valid choice....");
                    break;
                }
            }
        }
    }

    void FIR() throws Exception
    {
        //FIR through Query....
        CMQ.FileFIRQ();
    }

    void SearchCases() throws Exception
    {
        // Search Cases through Query....
        CMQ.SearchCRQ();
    }

    void ListAllCases() throws Exception
    {
        // List All Cases through Query....
        //overview of cases should be displayed in a table format with caseID, caseTitle, caseStatus, and dateFiled.....
        //if user want to see full details of case then....user must have to login in system....
        CMQ.AllCasesQ();
    }

    void UpdateCaseDetails() throws Exception
    {
        //only officers can update the case details, so we need to check if the user is an officer or not....
        //only officerDetails and CaseStatus can be updated by the officer, so we need to check if the user is an officer or not....
        // Update Case Details through Query....
        CMQ.UpdateCaseData();
    }

    void CrimeRatio() throws Exception
    {
        // Crime Ratio through Query....
        CMQ.CrimeRatio();
    }
}