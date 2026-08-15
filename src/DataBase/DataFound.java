package DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataFound
{
    Database db=new Database();

    public boolean isCaseFound(String cid) throws Exception
    {
        String FCID="select count(*) from case_details where CaseID=?";
        PreparedStatement QFCID=db.getConnection().prepareStatement(FCID);
        QFCID.setString(1, cid);
        ResultSet FCIDrs = QFCID.executeQuery();
        if (FCIDrs.next())
        {
            boolean res = FCIDrs.getInt(1) > 0;
            FCIDrs.close();
            QFCID.close();
            return res;
        }
        QFCID.close();
        return false;
    }

    public boolean isOffFound(String OID) throws Exception
    {
        String FOID="select count(*) from officer_details where OfficerID=?";
        PreparedStatement QFOID=db.getConnection().prepareStatement(FOID);
        QFOID.setString(1,OID);
        ResultSet found=QFOID.executeQuery();
        if (found.next())
        {
            boolean res = found.getInt(1) > 0;
            found.close();
            QFOID.close();
            return res;
        }
        QFOID.close();
        return false;
    }

    public boolean isUSer(String uId) throws Exception
    {
        String isUser = "SELECT COUNT(*) FROM users WHERE UserID = ? or EmailID=?";
        PreparedStatement QiU = db.getConnection().prepareStatement(isUser);
        QiU.setString(1, uId);
        QiU.setString(2,uId);

        ResultSet isU_rs = QiU.executeQuery();

        isU_rs.next();

        int count = isU_rs.getInt(1);

        if (count > 0)
        {
            isU_rs.close();
            QiU.close();
            return true;
        }
        else
        {
            System.err.println("[NOT FOUND] User Not Found....");
            isU_rs.close();
            QiU.close();
            return false;
        }
    }

    public boolean isCriFound(String CriID) throws Exception
    {
        String isCriminal="select count(*) from criminal_details where CriminalID=?";
        PreparedStatement QiCri=db.getConnection().prepareStatement(isCriminal);
        QiCri.setString(1,CriID);

        ResultSet isCri_rs = QiCri.executeQuery();

        isCri_rs.next();

        int count = isCri_rs.getInt(1);

        if (count > 0)
        {
            isCri_rs.close();
            QiCri.close();
            return true;
        }
        else
        {
            System.err.println("[NOT FOUND] Criminal Not Found....");
            isCri_rs.close();
            QiCri.close();
            return false;
        }
    }
}
