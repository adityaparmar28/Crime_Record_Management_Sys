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
import Quries.IAQueries;

import java.util.Scanner;

public class Investigation extends IAQueries
{
    Scanner sc=new Scanner(System.in);
    DGP dgp=new DGP();
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

            try
            {
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
            catch (APIs.AuthorizationException e)
            {
                System.err.println("[WARNING] " + e.getMessage());
            }
        }
    }

    void ViewPendingCases() throws Exception
    {
        // Implementation for viewing pending unsolved cases....
        PendingCases();
    }

    void UpdateInvestigationStatus() throws Exception
    {
        if (Login_SignUpPage.getLoggedUserID().equals(""))
        {
            System.out.println("[INFO] You must be logged in to update investigation status....Log in first....");

            if (!LSP.DGPLogin())
            {
                System.err.println("[ERROR] Login failed.....Returning....");
                return;
            }
        }

        if (!(Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Admin") ||
                Login_SignUpPage.getLoggedUserRole().equalsIgnoreCase("Officer")))
        {
            throw new APIs.AuthorizationException("Only Directory of Police Officers (Admin/Officer) can update investigation status....");
        }

        UpdateInvesting();
    }
}