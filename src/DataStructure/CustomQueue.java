package DataStructure;

/**Creating Custom Queue by LinkList....**/

public class CustomQueue
{
    public class node
    {
        public String data;
        public node next;

        public node(String value)
        {
            data = value;
            next = null;
        }
    }

    public node front = null;
    public node rear = null;

    public void Enqueue(String value)
    {
        node n = new node(value);

        if (front == null)
        {
            front = n;
            rear = n;
        }
        else
        {
            rear.next = n;
            rear = n;
        }
    }

    public String Dequeue()
    {
        if (front == null)
        {
            return null;
        }
        String val = front.data;
        front = front.next;
        if (front == null)
        {
            rear = null;
        }
        return val;
    }

    public String peek()
    {
        if (front == null)
        {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty()
    {
        return front == null;
    }

    public void clear()
    {
        front = null;
        rear = null;
    }

    public void display()
    {
        if (front == null)
        {
            System.out.println("Queue is Empty....");
        }
        else
        {
            node temp = front;
            while (temp != null)
            {
                System.out.print(temp.data + " -> ");
                temp = temp.next;
            }
            System.out.println("NULL");
        }
    }
}
