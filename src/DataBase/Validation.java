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
package DataBase;

import java.util.Scanner;

public class Validation
{
    Scanner sc=new Scanner(System.in);

    public int readInt(String sout)
    {
        while(true)
        {
            try
            {
                System.out.print(sout);
                int read=sc.nextInt();

                if(read<0)
                {
                    System.out.println("[WARNING] Enter Only Non - Nagetive Integer....");
                    continue;
                }
                sc.nextLine(); // Clear the remaining newline if any
                return read;
            }
            catch (Exception e)
            {
                System.err.println("[INVALID] Invalid Input....");
                sc.nextLine(); // Consume the invalid input to avoid infinite loop
            }
        }
    }

    public int readIntRange(String sout, int min, int max)
    {
        while (true)
        {
            int read = readInt(sout);
            if (read >= min && read <= max)
            {
                return read;
            }
            System.err.println("[INVALID] Value must be between " + min + " and " + max + "....");
        }
    }

    public String readNonEmptyString(String sout)
    {
        while (true)
        {
            System.out.print(sout);
            String read = sc.nextLine().trim();
            if (read.isEmpty())
            {
                System.err.println("[INVALID] Input can't be empty....");
            }
            else
            {
                return read;
            }

        }
    }

    public String readAlphaString(String sout)
    {
        while (true)
        {
            String read = readNonEmptyString(sout);
            if (read.matches("[a-zA-Z ]+"))
            {
                return read;
            }
            System.err.println("[INVALID] Input must contain only letters and spaces....");
        }
    }

    public String Date(String Input)
    {
        int dob = 0;
        boolean SDob = false;
        String dobS = "";
        boolean born1=false; //for birth loop
        while(!born1)
        {

            while (!SDob)
            {
                System.out.print("ENTER "+Input+" (DDMMYYYY): ");
                try
                {
                    dob = sc.nextInt();
                }
                catch (Exception e)
                {
                    System.out.println("Invalid "+Input+"..!!");
                    sc.next();
                    continue;
                }

                dobS = String.valueOf(dob);
                if(dobS.charAt(0)=='0' || dobS.length()==7)
                {
                    SDob=true;
                }
                else if(dobS.length() == 8 )
                {
                    SDob = true;
                }
                else
                {
                    System.out.println("Invalid "+Input+"....");
                    System.out.println(Input+" must be 8 Digits..!!");
                }
            }

            //>>>Date validation for DD | MM | YYYY...

            int DD; //Date
            int MM; //Month
            int YYYY; //Year

            DD = dob / 1000000; //First 2 digits
            MM = (dob / 10000) % 100; //Middle 2 digits
            YYYY = dob % 10000; //Last 4 digits


            if(YYYY<=2026 && ((YYYY%400==0)||(YYYY%4==0 && YYYY %100!=0)))
            {
                if(MM > 0 && MM < 13)
                {
                    if (DD > 0 && DD < 30)
                    {
                        born1=true;
                    }
                    else
                    {
                        System.out.println("Invalid Date of "+Input+" (DD).");
                        System.out.println("Try Again...");
                        SDob = false;
                    }
                }
                else
                {
                    System.out.println("Invalid Month of "+Input+" (MM).");
                    System.out.println("Try Again...");
                    SDob = false;
                }
            }
            else if(YYYY>2026 ||YYYY<1926)
            {
                System.out.println("Invalid Year of "+Input+" (YYYY).");
                System.out.println("Try Again...");
                SDob = false;
            }
            else
            {
                if(MM > 0 && MM < 13)
                {
                    if(MM==2 && DD<29)
                    {
                        born1=true;
                        return String.format("%04d-%02d-%02d", YYYY, MM, DD);
                    }

                    if (DD > 0 && DD < 32 && (MM==1||MM==3||MM==5||MM==7||MM==8||MM==10||MM==12))
                    {
                        born1=true;
                    }
                    else if (DD > 0 && DD < 31 && (MM==4||MM==6||MM==9||MM==11))
                    {
                        born1=true;
                    }
                    else
                    {
                        System.out.println("Invalid Date of "+Input+" (DD).");
                        System.out.println("Try Again...");
                        SDob = false;
                    }
                }
                else
                {
                    System.out.println("Invalid Month of "+Input+"(MM).");
                    System.out.println("Try Again...");
                    SDob = false;
                }
            }
        }
        int DD = dob / 1000000;
        int MM = (dob / 10000) % 100;
        int YYYY = dob % 10000;
        return String.format("%04d-%02d-%02d", YYYY, MM, DD);
    }
}