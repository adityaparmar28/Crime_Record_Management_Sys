package DataStructure;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class CustomStack
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

    public node top = null;

    public void push(String value)
    {
        node n = new node(value);
        n.next = top;
        top = n;

        try
        {
            if (DataBase.Database.testConnection())
            {
                long endTimeMs = System.currentTimeMillis();
                long startTimeMs = Profile.Login_SignUpPage.lastActionTime;

                if (startTimeMs <= 0 || startTimeMs > endTimeMs)
                {
                    startTimeMs = endTimeMs;
                }

                long durationMs = endTimeMs - startTimeMs;
                Profile.Login_SignUpPage.lastActionTime = endTimeMs;

                String durationStr = formatDuration(durationMs);

                String uId = Profile.Login_SignUpPage.LoggedUserID;
                String role = Profile.Login_SignUpPage.LoggedUserRole;

                if (uId == null || uId.isEmpty())
                {
                    if (value.startsWith("Signed up new user: "))
                    {
                        uId = value.substring("Signed up new user: ".length()).trim();
                        role = "Citizen";
                    }
                    else if (value.startsWith("Logged in user: "))
                    {
                        int startIndex = "Logged in user: ".length();
                        int endIndex = value.indexOf(" (");
                        if (endIndex != -1)
                        {
                            uId = value.substring(startIndex, endIndex).trim();
                        }
                        else
                        {
                            uId = value.substring(startIndex).trim();
                        }
                        role = Profile.Login_SignUpPage.LoggedUserRole;
                        if (role == null || role.isEmpty())
                        {
                            role = "Citizen";
                        }
                    }
                    else
                    {
                        uId = "Guest";
                        role = "Citizen";
                    }
                }

                Timestamp startTime = new Timestamp(startTimeMs);
                Timestamp endTime = new Timestamp(endTimeMs);

                String sql = "INSERT INTO ActivityLog (Time, UserID, Role, Activity, ActivityEndTime, ActivityDuration) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = DataBase.Database.getConnection().prepareStatement(sql))
                {
                    ps.setTimestamp(1, startTime);
                    ps.setString(2, uId);
                    ps.setString(3, role);
                    ps.setString(4, value);
                    ps.setTimestamp(5, endTime);
                    ps.setString(6, durationStr);
                    ps.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("[WARNING] Failed to save activity log to database: " + e.getMessage());
        }
    }

    public String pop()
    {
        if (top == null)
        {
            return null;
        }
        String val = top.data;
        top = top.next;
        return val;
    }

    public String peek()
    {
        if (top == null)
        {
            return null;
        }
        return top.data;
    }

    public boolean isEmpty()
    {
        return top == null;
    }

    public void display()
    {
        try
        {
            if (DataBase.Database.testConnection())
            {
                 String uId = Profile.Login_SignUpPage.LoggedUserID;
                 if (uId == null || uId.isEmpty())
                 {
                     System.out.println("[INFO] No logged in user. Cannot retrieve logs.");
                     return;
                 }

                String sql = "SELECT Time, UserID, Role, Activity, ActivityEndTime, ActivityDuration FROM ActivityLog WHERE UserID = ? ORDER BY LogID DESC";
                try (PreparedStatement ps = DataBase.Database.getConnection().prepareStatement(sql))
                {
                    ps.setString(1, uId);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        boolean hasLogs = false;
                        while (rs.next())
                        {
                            hasLogs = true;
                            Timestamp time = rs.getTimestamp("Time");
                            String userId = rs.getString("UserID");
                            String role = rs.getString("Role");
                            String activity = rs.getString("Activity");
                            Timestamp endTime = rs.getTimestamp("ActivityEndTime");
                            String duration = rs.getString("ActivityDuration");

                            System.out.printf("[%s] %s - %s | %s | [%s] | %s\n",
                                    time.toString(), userId, role, activity, endTime.toString(), duration);
                        }
                        if (!hasLogs)
                        {
                            System.out.println("[INFO] No activity logs found for user: " + uId);
                        }
                    }
                }
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println("[WARNING] Database query failed, falling back to local memory logs: " + e.getMessage());
        }

        if (top == null)
        {
            System.out.println("[INFO] No activity logs found....");
        }
        else
        {
            node temp = top;
            while (temp != null)
            {
                System.out.println(" - " + temp.data);
                temp = temp.next;
            }
        }
    }

    private String formatDuration(long durationMs)
    {
        long durationSec = durationMs / 1000;
        if (durationSec < 60)
        {
            return durationSec + " seconds";
        }
        long minutes = durationSec / 60;
        long seconds = durationSec % 60;
        if (seconds == 0)
        {
            return minutes + " minutes";
        }
        return minutes + " minutes " + seconds + " seconds";
    }
}
