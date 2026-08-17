package DataStructure;

public class CustomPriorityQueue
{
    private CustomQueue[] queues;

    public CustomPriorityQueue()
    {
        queues = new CustomQueue[5];

        for (int i = 1; i <= 4; i++)
        {
            queues[i] = new CustomQueue();
        }
    }

    public void PREnqueue(String value, int priority)
    {
        if (priority < 1)
        {
            priority = 1;
        }

        if (priority > 4)
        {
            priority = 4;
        }

        queues[priority].Enqueue(value);
    }

    // Overload for compatibility with standard add
    public void Enqueue(String data)
    {
        int priority = determinePriority(data);

        PREnqueue(data, priority);
    }

    private int determinePriority(String details)
    {
        String data = details.toLowerCase();

        if (data.contains("terrorism") || data.contains("espionage") || data.contains("treason") || data.contains("hijacking") || data.contains("cyber attack") || data.contains("smuggling"))
        {
            return 1; // National / Central Level Priority (Highest)
        }
        else if (data.contains("murder") || data.contains("homicide") || data.contains("kill") || data.contains("assault") || data.contains("attack"))
        {
            return 2; // High Priority
        }
        else if (data.contains("robbery") || data.contains("theft") || data.contains("steal") || data.contains("burglary") || data.contains("fraud") || data.contains("kidnap"))
        {
            return 3; // Medium Priority
        }
        else
        {
            return 4; // Low Priority
        }
    }

    public String Dequeue()
    {
        for (int i = 1; i <= 4; i++)
        {
            if (!queues[i].isEmpty())
            {
                return queues[i].Dequeue();
            }
        }
        return null;
    }

    public String peek()
    {
        for (int i = 1; i <= 4; i++)
        {
            if (!queues[i].isEmpty())
            {
                return queues[i].peek();
            }
        }
        return null;
    }

    public boolean isEmpty()
    {
        for (int i = 1; i <= 4; i++)
        {
            if (!queues[i].isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public void clear()
    {
        for (int i = 1; i <= 4; i++)
        {
            queues[i].clear();
        }
    }
}
