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