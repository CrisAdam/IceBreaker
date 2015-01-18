package ice_breaker_server.adam4.com;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseRequestHandler
{
    public static boolean userExists(String userName)
    {
        try
        {
            Connection con = DatabaseHandler.getConnection();
            java.sql.Statement existsQuery;
            existsQuery = con.createStatement();

            ResultSet existsResult = existsQuery.executeQuery("SELECT COUNT(*) from icebreaker.user_table where username=\"" + userName + "\";");
            return (existsResult.getInt(1) == 1);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

	public static boolean connect(String username, String password)
	{
		if (userExists(username))
		{
			// todo: check that password is valid
			return true;
		}
		return true;
		
	}

	public static boolean join(String username, String password)
	{
		return !userExists(username);
		
	}
}
