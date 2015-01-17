package ice_breaker_server.adam4.com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientListener implements Runnable
{
	
	private static final int ENDCHECKFREQUENCY = 1000;
	static boolean acceptingNewClients = false;
    ServerSocket serverSocket = null;

    ClientListener()
    {
        try
        {
            serverSocket = new ServerSocket();
        }
        catch (Exception e)
        {
        	System.out.println(e.getMessage());
        }
    }

    @Override
    public void run()
    {
    	acceptingNewClients = true;
        while (acceptingNewClients)
        {
            try
            {
                serverSocket.setSoTimeout(ENDCHECKFREQUENCY);
                Socket clientSocket = serverSocket.accept();
                if (acceptingNewClients)
                {
                	new ClientHandler(clientSocket);
                }
                else
                {

                }
            }
            catch (SocketException e)
            {
                ;
                // do nothing; this is likely an exit due to SoTimeout such that it
                // can check that if it should still be listening or not
            }
            catch (IOException e)
            {
            	System.out.println(e.getMessage());
            }
        }
    }

	public static void close()
	{
		acceptingNewClients = false;
	}

}
