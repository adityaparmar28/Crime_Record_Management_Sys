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

import java.sql.*;
import java.util.Scanner;

public class Database extends Thread
{
    private static final String URL = "jdbc:mysql://localhost:3306/v102";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection con = null;
    private static boolean driverLoaded = false;

    // Load JDBC Driver once
    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("[WARNING] MySQL JDBC Driver not found in classpath. SQL features will be disabled.");
        }
    }

    public static boolean conStatus=testConnection();

    Scanner sc=new Scanner(System.in);

    public static Connection getConnection() throws SQLException, APIs.DBConnectionException
    {
        if (!driverLoaded)
        {
            throw new APIs.DBConnectionException("JDBC Driver was not loaded.");
        }

        // Return active connection or create a new one if closed
        if (con == null || con.isClosed())
        {
            try {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new APIs.DBConnectionException("Could not connect to database: " + e.getMessage());
            }
        }
        return con;
    }

    public static boolean testConnection()
    {
        try
        {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static void closeConnection()
    {
        if (con != null)
        {
            try
            {
                if (!con.isClosed())
                {
                    con.close();
                }
            }
            catch (SQLException e)
            {
                System.err.println("[ERROR] Failed to close database connection: " + e.getMessage());
            }
        }
    }

    public void DBSync()
    {
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|              DATABASE & SYSTEM CONFIGURATION MANAGEMENT              |");
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println();

        if(testConnection())
        {
            System.out.println("Status: ONLINE MODE (Connected to MySQL Database)");
            System.out.println("Connection String: jdbc:mysql://localhost:3306/crms");
            System.out.println();
        }
        else
        {
            System.out.println("Status: OFFLINE MODE (In-memory simulation active)");
            System.out.println("Crime Record Management System could not connect to XAMPP MySQL Database at start...");
            System.out.println("Make sure XAMPP Control Panel is running and MySQL service is started....");
            System.out.println();

            System.out.print("Do you want to test and establish MySQL connection now???");
            char conAns=sc.next().charAt(0);

            if (conAns=='y' || conAns=='Y')
            {
                System.out.println("Attempting to connect to jdbc:mysql://localhost:3306/crms...");
                if (testConnection())
                {
                    System.out.println("Database connection established! System synchronized to live database.");
                    System.out.println("System reconnected and synchronized with live MySQL DB");
                }
                else
                {
                    System.out.println("Reconnection failed....Staying in Offline Mode....");
                }
            }
        }
    }

    public Database()
    {
        conStatus=testConnection();
    }

    @Override
    public void run()
    {
        Status();
    }


    public void Status()
    {
        while (true)
        {
            try
            {
                Thread.sleep(10000); // Check every 10 seconds
                if (testConnection())
                {
                    if (!conStatus)
                    {
                        System.out.println("[INFO] Database connection restored....");
                        conStatus = true;
                    }
                }
                else
                {
                    if (conStatus)
                    {
                        System.out.println("[WARNING] Database connection lost....");
                        conStatus = false;
                    }
                }
            }
            catch (InterruptedException e)
            {
                System.err.println("[ERROR] DBSync thread interrupted: " + e.getMessage());
            }
        }
    }


    private void checkAndMigrateSchema() throws Exception
    {
        boolean needsMigration = false;
        
        // 1. Check if case_details table has CaseID as INT (old schema)
        if (tableExists("case_details"))
        {
            String checkQuery = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'case_details' AND COLUMN_NAME = 'CaseID'";
            PreparedStatement ps = getConnection().prepareStatement(checkQuery);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                String dataType = rs.getString(1);
                if (dataType.equalsIgnoreCase("int") || dataType.equalsIgnoreCase("integer"))
                {
                    needsMigration = true;
                }
            }
            rs.close();
            ps.close();
        }
        
        // 2. Check if officer_details table has old column Officer_ID (old schema)
        if (!needsMigration && tableExists("officer_details"))
        {
            String checkQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'officer_details' AND COLUMN_NAME = 'Officer_ID'";
            PreparedStatement ps = getConnection().prepareStatement(checkQuery);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0)
            {
                needsMigration = true;
            }
            rs.close();
            ps.close();
        }
        
        if (needsMigration)
        {
            System.out.println("[INFO] Old database schema detected (INT IDs). Dropping old tables to recreate upgraded schema...");
            Statement stmt = getConnection().createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("DROP TABLE IF EXISTS case_details");
            stmt.execute("DROP TABLE IF EXISTS criminal_details");
            stmt.execute("DROP TABLE IF EXISTS officer_details");
            stmt.execute("DROP TABLE IF EXISTS crime_records");
            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("DROP TABLE IF EXISTS criminal_pictures");
            stmt.execute("DROP TABLE IF EXISTS Criminal_Pictures");
            stmt.execute("DROP TABLE IF EXISTS ActivityLog");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            stmt.close();
        }
    }

    public void DefaultDataBase() throws Exception
    {
        CreateTable CT = new CreateTable();
        InsertData ITD = new InsertData();

        if (!testConnection())
        {
            System.err.println("[INFO] System running in OFFLINE mode (Database connection or driver not available).");
            return;
        }

        // Run auto-migration check to drop old schema tables
        checkAndMigrateSchema();

        if (!tableExists("officer_details"))
        {
            CT.CTofficer_details();
        }
        if (isTableEmpty("officer_details"))
        {
            ITD.ITOfficer_Details();
        }

        if (!tableExists("criminal_details"))
        {
            CT.CTcriminal_details();
        }
        if (isTableEmpty("criminal_details"))
        {
            ITD.ITCriminal_Details();
        }

        if (!tableExists("case_details"))
        {
            CT.CTcase_details();
        }
        if (isTableEmpty("case_details"))
        {
            ITD.ITCase_Details();
        }

        if (!tableExists("criminal_pictures"))
        {
            CT.CTcriminal_pictures();
        }
        if (isTableEmpty("criminal_pictures"))
        {
            ITD.ITCriminal_Pictures();
        }

        // Apply circular FK from criminal_details to criminal_pictures
        CT.ApplyCircularFKs();

        if (!tableExists("crime_records"))
        {
            CT.CTcrime_records();
        }
        if (isTableEmpty("crime_records"))
        {
            ITD.ITCrime_Records();
        }

        if (!tableExists("users"))
        {
            CT.CTusers();
        }
        if (isTableEmpty("users"))
        {
            ITD.ITUsers();
        }

        if (!tableExists("ActivityLog"))
        {
            CT.CTactivity_log();
        }
        else
        {
            try
            {
                String findConstraint = "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'ActivityLog' AND REFERENCED_TABLE_NAME = 'users'";
                String constraintName = null;
                try (PreparedStatement ps = getConnection().prepareStatement(findConstraint);
                     ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        constraintName = rs.getString(1);
                    }
                }

                if (constraintName != null)
                {
                    try (Statement stmt = getConnection().createStatement())
                    {
                        stmt.execute("ALTER TABLE ActivityLog DROP FOREIGN KEY " + constraintName);
                    }
                }
            }
            catch (Exception e)
            {
                // Ignore if constraint does not exist or cannot be dropped
            }
        }

        if (isTableEmpty("ActivityLog"))
        {
            ITD.ITActivityLog();
        }
    }

    public boolean tableExists(String tableName) throws Exception
    {
        Connection con = getConnection();

        String TableExists = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";

        PreparedStatement QTE = con.prepareStatement(TableExists);
        QTE.setString(1, tableName);

        ResultSet existsT_rs = QTE.executeQuery();

        existsT_rs.next();
        boolean exists = existsT_rs.getInt(1) > 0;

        existsT_rs.close();
        QTE.close();
        return exists;
    }

    public boolean isTableEmpty(String tableName) throws Exception
    {
        Connection con = getConnection();

        String isTValues = "SELECT COUNT(*) FROM " + tableName;
        PreparedStatement QITV = con.prepareStatement(isTValues);
        ResultSet rs = QITV.executeQuery();

        rs.next();
        boolean empty = rs.getInt(1) == 0;

        rs.close();
        QITV.close();
        return empty;
    }
}