package ice_breaker_server.adam4.com;

import java.nio.file.FileSystems;

public class IceBreakerServer
{
	private static String runFilePath = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "IceBreakerServer.run";
	public final static int clientPort = 9995;
	static DatabaseHandler dbHandler;

	public static void main(String[] args) throws Exception
	{
		handleCLI(args);
		BlockOnRunFile block = new BlockOnRunFile(runFilePath);
        block.block();
        dbHandler.close();
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
					dbHandler = new DatabaseHandler(new DatabaseConnectionInfo(
							args[++i].split(";")[0], "", "", 2));
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
