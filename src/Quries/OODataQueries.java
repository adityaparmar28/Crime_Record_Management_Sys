package Quries;

import DataBase.DataBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class OODataQueries
{
    Scanner sc=new Scanner(System.in);
    DataBase db=new DataBase();

    public Object SQLDType2JDType(String ColummName, String TName) throws Exception
    {
        String Datatype = "Select ColummDataType(?,?)";
        PreparedStatement DT = db.getConnection().prepareStatement(Datatype);
        DT.setString(1, ColummName);
        DT.setString(2, TName);
        ResultSet DTrs = DT.executeQuery();

        DTrs.next();
        //System.out.println("[INFO] Column DataType of "+ColummName+" in Table "+TName+" is: "+DTrs.getString(1));
        String Data_Type = DTrs.getString(1);
        Data_Type = Data_Type.toUpperCase();

        switch (Data_Type)
        {
            case "VARCHAR":
            {
                String UData = sc.next();
                return UData;
                //break;
            }

            case "INT":
            {
                int UData = sc.nextInt();
                return UData;
                //break;
            }

            case "DATE":
            {
                String UData = sc.next();
                return UData;
                //break;
            }

            case "DECIMAL":
            {
                double UData = sc.nextDouble();
                return UData;

            }

            default:
            {
                System.err.println("[WARNING] Column DataType of " + ColummName + " in Database " + TName + " is: " + DTrs.getString(1));
                System.err.println(" which is not supported for updating in Database....");
                return null;
            }
        }
    }
}