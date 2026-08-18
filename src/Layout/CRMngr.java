/*
 * Copyright 2026 MR. ADITYA PARMAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Layout;

import Profile.Login_SignUpPage;
import Quries.CRMngrQueries;

import java.util.Scanner;

public class CRMngr extends CRMngrQueries
{
    Scanner sc=new Scanner(System.in);
    Login_SignUpPage LSP=new Login_SignUpPage();

    public void CrimeRecordManager() throws Exception
    {
        boolean isCRML=false;
        int crm_ch;

        while (!isCRML)
        {
            try
            {
                //sc.nextLine();
                System.out.println("+----------------------------------------------------------------------+");
                System.out.println("|                          CRIME RECORD MANAGER                        |");
                System.out.println("+----------------------------------------------------------------------+");
                System.out.println("| 1. Add Criminal Record");
                System.out.println("| 2. Search Criminal");
                System.out.println("| 3. List of All Criminals");
                System.out.println("| 4. Update Criminal Record");
                System.out.println("| 5. Show Criminal by Graph");
                System.out.println("| 6. Home Page");
                System.out.println("+----------------------------------------------------------------------+");
                System.out.print("| Enter Your Choice: ");
                crm_ch = sc.nextInt();
            }
            catch (Exception e)
            {
                System.err.println("[ERROR] Invalid Input....");
                sc.nextLine(); // Clear the invalid input
                continue; // Restart the loop
            }

            try
            {
                switch (crm_ch)
                {
                    case 1:
                    {
                        if (Login_SignUpPage.getLoggedUserID().equals(""))
                        {
                            System.out.println("[INFO] You must be logged in to add criminal records....LogIn First....");
                            if (!LSP.DGPLogin())
                            {
                                System.err.println("[ERROR] Login failed....Returning....");
                                break;
                            }
                        }

                        if(!(Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Admin") ||
                                Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Officer")))
                        {
                            throw new APIs.AuthorizationException("Only Directory of Police Officers Members (Admin/Officer) can Add Criminal....");
                        }
                        else
                        {
                            // Add Criminal Record
                            addCriminalRecord();
                        }
                        break;
                    }

                    case 2:
                    {
                        // Search Criminal
                        searchCriminal();
                        break;
                    }

                    case 3:
                    {
                        // List of All Criminals
                        AllCriminal();
                        break;
                    }

                    case 4:
                    {
                        if (Login_SignUpPage.getLoggedUserID().equals(""))
                        {
                            System.out.println("[INFO] You must be logged in to update criminal records....Log-In first....");
                            if (!LSP.DGPLogin())
                            {
                                System.err.println("[ERROR] Login failed....Returning....");
                                break;
                            }
                        }

                        if(!(Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Admin") ||
                                Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Officer")))
                        {
                            throw new APIs.AuthorizationException("Only Directory of Police Officers Members (Admin/Officer) can Update Criminal Details....");
                        }
                        else
                        {
                            // Update Criminal Record
                            UpdateCriminalRecord();
                        }
                        break;
                    }

                    case 5:
                    {
                        // Show Accomplice Graph
                        showGraph();
                        break;
                    }

                    case 6:
                    {
                        // Home Page
                        sc.nextLine();
                        isCRML=true;

                        sc.nextLine();
                        return;
                    }

                    default:
                    {
                        System.err.println("[ERROR] Invalid choice....Enter a valid choice....");
                    }
                }
            }
            catch (APIs.AuthorizationException e)
            {
                System.err.println("[WARNING] " + e.getMessage());
            }
        }
    }

    void addCriminalRecord() throws Exception
    {
        //first login as adg or police officer then process of add criminal record
        AddCriminalRQuery();
    }

    void searchCriminal() throws Exception
    {
        // Implementation for searching criminal
        //throgh logic and query...
        SearchCriminalRecord();
    }

    void AllCriminal() throws Exception
    {
        //all criminals and recently added criminals synced data through Query....
        AllCriminalRecord();
    }

    void UpdateCriminalRecord() throws Exception
    {
        //first login as adg then process of update criminal record
        UpdateCriminalRQuery();
    }

    void showGraph() throws Exception
    {
        DisplayCriminalRelations();
    }
}