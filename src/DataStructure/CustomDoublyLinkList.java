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
package DataStructure;

public class CustomDoublyLinkList
{
    class node
    {
        node prev;
        node next;
        String data;

        node(String value)
        {
            prev=null;
            data=value;
            next=null;
        }
    }

    node first=null;

    public void InsertLast(String value)
    {
        node n=new node(value);

        if(first==null)
        {
            first=n;
        }
        else
        {
            node temp=first;

            while(temp.next!=null)
            {
                temp=temp.next;
            }

            temp.next=n;
            n.prev=temp;
            temp=null;
        }
    }

    public void DLLTravalser(java.util.Scanner sc)
    {
        if (first == null)
        {
            System.out.println("[WARNING] No records found to navigate.....");
            return;
        }

        node temp = first;
        boolean exit = false;

        while (!exit)
        {
            System.out.println(temp.data);

            char choice = sc.next().toUpperCase().charAt(0);

            if (choice == 'N')
            {
                if (temp.next != null)
                {
                    temp = temp.next;
                }
                else
                {
                    System.out.println("[INFO] You are already on the LAST record....");
                }
            }
            else if (choice == 'P')
            {
                if (temp.prev != null)
                {
                    temp = temp.prev;
                }
                else
                {
                    System.out.println("[INFO] You are already on the FIRST record....");
                }
            }
            else if (choice == 'E')
            {
                exit = true;
                System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
            }
            else
            {
                System.err.println("[INVALID] Enter a valid navigation option (N/P/E).");
            }
        }
    }

    public void DisplayAllData()
    {
        if(first==null)
        {
            System.err.println("[WARNING] No records found....");
        }
        else
        {
            node temp=first;

            while(temp!=null)
            {
                System.out.println(temp.data);
                temp=temp.next;
            }
            System.out.println("+-------------------------------------------------------------------------------------------------------------------+");
            temp=null;
        }
    }

}