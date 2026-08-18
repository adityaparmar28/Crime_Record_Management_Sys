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
                long startTimeMs = APIs.TimeStamp.getLastActivityTime();
                long durationMs = APIs.TimeStamp.getDurationAndReset();
                long endTimeMs = APIs.TimeStamp.getCurrentTime();

                String durationStr = formatDuration(durationMs);

                String uId = Profile.Login_SignUpPage.getLoggedUserID();
                String role = Profile.Login_SignUpPage.getLoggedUserRole();

                if (uId == null || uId.equals(""))
                {
                    uId = "Guest";
                    role = "Citizen";

                    if (value.startsWith("Signed up new user: "))
                    {
                        uId = value.replace("Signed up new user: ", "");
                    }
                }

                Timestamp startTime = new Timestamp(startTimeMs);
                Timestamp endTime = new Timestamp(endTimeMs);

                String sql = "INSERT INTO ActivityLog (Time, UserID, Role, Activity, ActivityEndTime, ActivityDuration) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = DataBase.Database.getConnection().prepareStatement(sql);
                
                ps.setTimestamp(1, startTime);
                ps.setString(2, uId);
                ps.setString(3, role);
                ps.setString(4, value);
                ps.setTimestamp(5, endTime);
                ps.setString(6, durationStr);
                
                ps.executeUpdate();
                ps.close();
            }
        }
        catch (Exception e)
        {
            System.err.println("[WARNING] Failed to save activity log: " + e.getMessage());
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
                 String uId = Profile.Login_SignUpPage.getLoggedUserID();
                 if (uId == null || uId.isEmpty())
                 {
                     System.out.println("[INFO] No logged in user....Cannot retrieve history....");
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
