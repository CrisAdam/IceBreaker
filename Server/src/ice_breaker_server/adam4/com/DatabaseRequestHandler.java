package ice_breaker_server.adam4.com;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseRequestHandler
{
    public boolean playerExists(String playerName)
    {
        try
        {
            Connection con = DatabaseHandler.getConnection();
            java.sql.Statement existsQuery;
            existsQuery = con.createStatement();

            ResultSet existsResult = existsQuery.executeQuery("SELECT COUNT(*) from SFASchema.UsersTable where UserName=\"" + playerName + "\";");
            return (existsResult.getInt(1) == 1);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
