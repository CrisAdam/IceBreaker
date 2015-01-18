package ice_breaker_server.adam4.com;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

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
			return true;
		}
		return true;

	}

	public static boolean join(String username, String password)
	{
		if (userExists(username))
		{
			return false;
		}
		/*
		 * INSERT INTO `icebreaker`.`user_table` (`iduser`, `username`, `email`, `salt`, `hashedpw`, `lastlogin`) VALUES (<{iduser: }>, <{username: }>, <{email: }>, <{salt: }>, <{hashedpw: }>,
		 * <{lastlogin: }>);
		 */
		PreparedStatement pstmt = null;
		try
		{
			Connection con = DatabaseHandler.getConnection();
			String query = "INSERT INTO `icebreaker`.`user_table` (`username`, `salt`, `hashedpw`) VALUES(?, ?, ?)";

			final Random r = new SecureRandom();
			byte[] salt = new byte[32];
			r.nextBytes(salt);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, r.toString());
			pstmt.setString(3, Common.hashPassword(password, r.toString()));
			pstmt.executeUpdate();
			pstmt.close();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

	}
}
