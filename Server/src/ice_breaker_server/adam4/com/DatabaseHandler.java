package ice_breaker_server.adam4.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHandler
{
	private static DatabaseConnectionInfo conInfo;
	static List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	private static int openConnections = 0;

	public DatabaseHandler()
	{

	}

	private static Connection createConnection()
	{
		Connection con = null;
		if (openConnections < conInfo.connections) // otherwise too many connections have already been opened
		{
			try
			{
				connections.add(DriverManager.getConnection(conInfo.url, conInfo.user, conInfo.password));
				++openConnections;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Error: " + openConnections + " DB connections have already been opened");
		}

		return con;
	}

	public static Connection getConnection()
	{
		if (connections.size() > 0)
		{
			return connections.remove(connections.size() - 1);
		}
		else
		{
			Connection temp = createConnection();
			while (temp == null)
			{
				if (connections.size() > 0)
				{
					return connections.remove(connections.size() - 1);
				}
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			return temp;
		}
	}

	public static void returnConnection(Connection con)
	{
		connections.add(con);
	}

	public static void close()
	{
		while (connections.size() > 0)
		{
			try
			{
				connections.get(0).close();
				--openConnections;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (openConnections > 0)
		{
			try
			{
				Thread.sleep(500); // wait for connections to be returned
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			while (connections.size() > 0)
			{
				try
				{
					connections.get(0).close();
					--openConnections;
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		if (openConnections > 0)
		{
			System.out.println("error: unable to close all db connections");
		}
	}

	public static void setConnection(DatabaseConnectionInfo databaseConnectionInfo)
	{
		conInfo = databaseConnectionInfo;
		connections.add(getConnection());

	}

}
