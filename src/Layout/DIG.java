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
            System.out.println("| 1. Add Police Officer Record");
            System.out.println("| 2. Search Police Officer");
            System.out.println("| 3. List of All Police Officers");
            System.out.println("| 4. Update Police Officer Record");
            System.out.println("| 5. Home Page");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.print("| Enter Your Choice: ");
            int dig_ch = sc.nextInt();

            switch (dig_ch)
            {
                case 1:
                {
                    if (Login_SignUpPage.LoggedUserID.equals(""))
                    {
                        System.out.println("[INFO] You must be logged in to add police officer records....Log-In first....");
                        if (!LS.DGPLogin())
                        {
                            System.err.println("[ERROR] Login failed....Returning....");
                            break;
                        }
                    }

                    if (!(Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Admin") ||
                            Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Officer")))
                    {
                        System.err.println("[WARNING] Only DGP members (Admin/Officer) can Add Police Officer Record....");
                    }
                    else
                    {
                        // Add Police Officer Record
                        addPoliceOfficerRecord();
                    }
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
                    if (Login_SignUpPage.LoggedUserID.equals(""))
                    {
                        System.out.println("[INFO] You must be logged in to update police officer records....Log-In first....");
                        if (!LS.DGPLogin())
                        {
                            System.err.println("[ERROR] Login failed....Returning....");
                            break;
                        }
                    }

                    if (!(Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Admin") ||
                            Login_SignUpPage.LoggedUserRole.equalsIgnoreCase("Officer")))
                    {
                        System.err.println("[WARNING] Only DGP members (Admin/Officer) can Update Police Officer Record....");
                    }
                    else
                    {
                        // Update Police Officer Record
                        UpdatePoliceOfficerRecord();
                    }
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
                    System.err.println("[ERROR] Invalid Choice....Enter a valid choice....");
                }
            }
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