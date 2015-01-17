package ice_breaker_server.adam4.com;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// to be run on ec2-54-191-73-91.us-west-2.compute.amazonaws.com

public class IceBreakerServer
{
	private static String runFilePath = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "IceBreakerServer.run";
	public final static int clientPort = 9995;
	public static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<ClientHandler>());

	public static void main(String[] args) throws Exception
	{
		handleCLI(args);
		new Thread(new ClientListener()).start();
		BlockOnRunFile block = new BlockOnRunFile(runFilePath);
		block.block();
		ClientListener.close();
		DatabaseHandler.close(); // close database after disconnecting clients to enable last-second saves
	}

	private static boolean handleCLI(String[] args) throws Exception
	{
		// return false and end program if invalid input is provided
		for (int i = 0; i < args.length; ++i)
		{
			switch (args[i].toLowerCase())
			{
				case "-db":
				case "-database":
					DatabaseHandler.setConnection(new DatabaseConnectionInfo(args[++i], 2));
					break;
				case "-h":
				case "-help":
				default:
					System.out.print("Error: help not yet created");
					return false;
			}
		}
		return true;
	}
}
