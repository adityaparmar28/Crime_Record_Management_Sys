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
package Profile;

import DataBase.Database;
import DataBase.Validation;
import Quries.Login_SignUp_Queries;

import java.util.Arrays;
import java.util.Scanner;

public class Login_SignUpPage
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();
    Login_SignUp_Queries LSQ=new Login_SignUp_Queries();
    Validation v=new Validation();

    //>>>Variables assignation...
    String name;    //same username for sup & sin
    int dob;        //same dob for sup & sin
    long m_no;      //uses in signup
    String email;
    String UID;     //uses for assigned ID
    String passwd;  //uses for assigned passwd
    String passwd1; //uses for input passwd
    String UID1;    //uses for input UID
    int atp=2;      //limited Attempt of login
    boolean status; //for further uses if account blocked...
    boolean SDob=false; //status of String DOB
    boolean Sm_no=false; //Status of String m_no
    String dobS; //Convert to String
    String m_noS; //Convert to String
    public static String Role="Citizen";
    private static String LoggedUserID="";
    private static String LoggedUserRole="";
    //public static int atp = 2;

    public static String getLoggedUserID() {
        return LoggedUserID;
    }

    public static void setLoggedUserID(String id) {
        LoggedUserID = id;
    }

    public static String getLoggedUserRole() {
        return LoggedUserRole;
    }

    public static void setLoggedUserRole(String role) {
        LoggedUserRole = role;
    }

    public void SignUp() throws Exception
    {
        boolean isName=false;
        boolean isEmail=false;
        System.out.println("USER SIGNUP |-----");


            try
            {
                try
                {
                    sc.nextLine();
                }
                catch (RuntimeException e)
                {
                }

                while (!isName)
                {
                    System.out.print("ENTER YOUR NAME: ");
                    name = sc.nextLine();

                    if (!name.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid Name..!!");
                        System.out.println("Name must not contain special characters..!!");
                        continue;
                    } else {
                        isName = true;
                    }
                }
            }
            catch (Exception e)
            {
            }

            name = name.toUpperCase();

            while (!isEmail)
            {
                System.out.print("Enter your Email: ");
                email = sc.nextLine();

                String valid = "@gmail.com";
                if(!email.matches("^[A-Za-z0-9._%+-]+@gmail.com"))
                {
                    System.out.println("Invalid Email..!!");
                    System.out.println("Email must be a valid Gmail address..!!");
                }
                else
                {
                    isEmail=true;
                }
            }

        String Input="DOB";

        DOB(Input);

        M_NO();

        assign();
    }

    void DOB(String Input)
    {
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
                if(dobS.charAt(0)==0 || dobS.length()==7)
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
                    if(MM==02 && DD<29)
                    {
                        born1=true;
                        return;
                    }

                    if (DD > 0 && DD < 32 && (MM==01||MM==03||MM==05||MM==07||MM==8||MM==10||MM==12))
                    {
                        born1=true;
                    }
                    else if (DD > 0 && DD < 31 && (MM==04||MM==06||MM==9||MM==11))
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
    }

    void M_NO()
    {
        boolean b=false;
        while(!b)
        {
            try
            {
                System.out.print("ENTER YOUR MOBILE NUMBER: ");
                m_no = sc.nextLong();

                if (m_no > 6000000000L && m_no < 9999999999L)
                {
                    b = true;
                    break;
                }
                else
                {
                    System.out.println("Enter Valid Mobile number..!!");
                    System.out.println("Try Again....");
                }
            }
            catch (Exception e)
            {
                System.out.println("Enter Valid Mobile number..!!");
                System.out.println("Try Again....");
                sc.nextLine(); // Clear the invalid input from buffer
            }
        }

        m_noS=String.valueOf(m_no);

        while(!Sm_no)
        {
            if(m_noS.length()==10)
            {
                Sm_no=true;
            }
            else
            {
                System.out.println("Invalid Mobile Number....");
                System.out.println("Mobile number must be 10 Digits..!!");
            }
        }
    }

    void UID_pass()
    {
        String dob4S;   //DOB last 4 digit in String.
        String m_no4S;  //mobile_no last 4 digit in String.
        String name4f;  //Name first4 alpha.

        //>>>Conversation of int data type of DOB & Mobile number to String...

        dobS=String.valueOf(dob);
        m_noS=String.valueOf(m_no);

        //>>>Get last 4 char(int) of DOB & Mobile number...

        dob4S=dobS.substring(dobS.length()-4);
        m_no4S=m_noS.substring(m_noS.length()-4);

        //>>>Converting into Array...
        int[] I_P=new int[8];   //for adding DOB & Mobile last 4 digits.
        int index=0;

        //__DOB
        for(int i=0;i<4;i++)
        {
            I_P[index++]=dob4S.charAt(i)-'0';
        }

        //__M_no
        for(int j=0;j<4;j++)
        {
            I_P[index++]=m_no4S.charAt(j)-'0';
        }

        // //sorting of array...
        Arrays.sort(I_P);

        //>>>>>GENERATION OF UID...
        UID="";
        for(int d: I_P)
        {
            UID+=d;
        }

        if(name.length()==3)
        {
            name4f=name;
        }
        else
        {
            name4f=name.substring(0,4);
        }
        String ID4L=UID.substring(UID.length()-4);

        passwd=name4f+"@"+ID4L;

        //System.out.println("USER ID: "+UID);
        //System.out.println("USER PASSWORD: "+passwd);
    }

    //>>> After Signup, Applied candidate got details from below...

    public void assign() throws Exception
    {
        UID_pass();

        String DOB = dobS.substring(4)+"-"+ dobS.substring(2,4)+"-"+ dobS.substring(0,2);

        if(LSQ.SignUpQuery(name,email,m_noS,passwd,DOB,UID))
        {
            System.out.println("--------------------------------------------");
            System.out.println("USER NAME : "+name);
            System.out.println("USER ID   : "+UID);
            System.out.println("PASSWORD  : "+passwd);
            System.out.println("--------------------------------------------");
        }
    }

    public boolean IP_Auth() throws Exception
    {
        return IP_Auth(true); // Default to citizen login for compatibility
    }

    public boolean IP_Auth(boolean isCitizen) throws Exception //ID & PASSWD Authentication
    {
        if(LSQ.LoginQuery(UID1,passwd1))
        {
            System.out.println("------| USER SUCCESSFULLY LOGGED ON |------");
            return true;
        }
        else
        {
            atp--;
            if(atp>0)
            {
                System.out.println("------| USER LOGIN FAILED |------");
                System.out.println("Attempt left: "+atp);

                if (atp == 1)
                {
                    Thread forgotThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.print("Did you forget your password? (Y/N): ");
                                String ans = sc.next();
                                if (ans.equalsIgnoreCase("Y")) {
                                    LSQ.ForgetPassword();
                                }
                            } catch (Exception e) {
                                System.err.println("[ERROR] Forgot password thread error: " + e.getMessage());
                            }
                        }
                    });
                    forgotThread.start();
                    forgotThread.join();
                }

                System.out.println("Do you want to SignUp? (Y/N)");
                char ask0=sc.next().charAt(0);
                if(ask0=='Y' || ask0=='y')
                {
                    SignUp();
                    return isCitizen ? userLoginInternal() : DGPLoginInternal();
                }
                else
                {
                    return isCitizen ? userLoginInternal() : DGPLoginInternal();
                }
            }
            else
            {
                System.out.println("------| USER LOGIN BLOCKED |------");
                status=true;
                return false;
            }
        }
    }

    public boolean userLogin() throws Exception
    {
        atp = 3; // Reset attempts for new login request (3 attempts total)
        return userLoginInternal();
    }

    private boolean userLoginInternal() throws Exception
    {
        System.out.println("------| USER LOGIN |------");
        System.out.print("Enter UserID: ");
        UID1=sc.next();
        System.out.print("Enter Password: ");
        passwd1=sc.next();

        if(UID1.trim().isEmpty() || passwd1.trim().isEmpty())
        {
            System.out.println("Invalid UserID or Password!!");
            return false;
        }
        else
        {
            return IP_Auth(true);
        }
    }

    public boolean DGPLogin() throws Exception
    {
        atp = 3; // Reset attempts for new login request (3 attempts total)
        return DGPLoginInternal();
    }

    private boolean DGPLoginInternal() throws Exception
    {
        System.out.println("------| DIRECTORY OF POLICE OFFICER LOGIN |------");
        System.out.print("Enter UserID: ");
        UID1=sc.next();
        System.out.print("Enter Password: ");
        passwd1=sc.next();

        if(UID1.trim().isEmpty() || passwd1.trim().isEmpty())
        {
            System.out.println("Invalid UserID or Password!!");
            return false;
        }
        else
        {
            return IP_Auth(false);
        }
    }
}