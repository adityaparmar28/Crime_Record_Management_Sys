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
package Quries;

import DataBase.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class OODataQueries
{
    Scanner sc=new Scanner(System.in);
    Database db=new Database();

    public Object SQLDType2JDType(String ColummName, String TName) throws Exception
    {
        String Datatype = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        PreparedStatement DT = Database.getConnection().prepareStatement(Datatype);
        DT.setString(1, TName);
        DT.setString(2, ColummName);
        ResultSet DTrs = DT.executeQuery();

        String Data_Type = "VARCHAR";
        if (DTrs.next())
        {
            Data_Type = DTrs.getString(1);
        }
        DTrs.close();
        DT.close();
        
        Data_Type = Data_Type.toUpperCase();

        switch (Data_Type)
        {
            case "VARCHAR":
            case "TEXT":
            case "LONGTEXT":
            {
                String UData = "";
                while (UData.isEmpty())
                {
                    UData = sc.nextLine().trim();
                }
                return UData;
            }

            case "INT":
            case "INTEGER":
            case "YEAR":
            {
                int UData = sc.nextInt();
                return UData;
            }

            case "DATE":
            {
                String UData = sc.next();
                return UData;
            }

            case "DECIMAL":
            case "DOUBLE":
            case "FLOAT":
            {
                double UData = sc.nextDouble();
                return UData;
            }

            default:
            {
                System.err.println("[WARNING] Column DataType of " + ColummName + " in Table " + TName + " is: " + Data_Type + " which is not fully supported, defaulting to String.");
                String UData = "";
                while (UData.isEmpty())
                {
                    UData = sc.nextLine().trim();
                }
                return UData;
            }
        }
    }
}