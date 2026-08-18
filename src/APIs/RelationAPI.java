package APIs;

import DataBase.Database;
import DataStructure.CustomGraph;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class RelationAPI
{
    public static CustomGraph buildGraph() throws Exception
    {
        CustomGraph graph = new CustomGraph();

        // Step 1: Fetch all criminals and their Case IDs in a single query
        String query = "select CaseID, Name from criminal_details where CaseID is not null and CaseID != ''";
        
        try (PreparedStatement ps = Database.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery())
        {
            // Step 2: Group criminal names by Case ID using a HashMap

            HashMap<String,ArrayList<String>> caseGroups = new HashMap<>();

            while (rs.next())
            {
                String caseID = rs.getString("CaseID");
                String name = rs.getString("Name");
                caseGroups.computeIfAbsent(caseID, k -> new ArrayList<>()).add(name);
            }

            // Step 3: Build the graph edges
            for (String caseID : caseGroups.keySet())
            {
                ArrayList<String> names = caseGroups.get(caseID);
                if (names.size() > 1)
                {
                    for (int i = 0; i < names.size(); i++)
                    {
                        for (int j = i + 1; j < names.size(); j++)
                        {
                            graph.connectCriminals(names.get(i), names.get(j), caseID);
                        }
                    }
                }
                else if (names.size() == 1)
                {
                    graph.addCriminal(names.get(0));
                }
            }
        }

        return graph;
    }
}
