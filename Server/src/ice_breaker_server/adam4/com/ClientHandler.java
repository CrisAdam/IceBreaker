package ice_breaker_server.adam4.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable
{

	private Socket clientSocket;
	public ClientHandler(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		new Thread(this).start();
	}

	@Override
	public void run()
	{

		BufferedReader input = null; // not sure if I need the buffering, but
										// having the getLine() is nice
		String message = "";

		try
		{
			input = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream(),
					StandardCharsets.UTF_8.newDecoder()));
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		while (!clientSocket.isClosed())
		{
			try
			{
				message = input.readLine();
				if (message.isEmpty())
				{
					continue;
				}
			}
			catch (IOException e)
			{
				message = "error";
				try
				{
					clientSocket.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				System.out.println(e.getMessage());
			}

			char switchChar = message.charAt(0);
			switch (switchChar)
			{
				case 'c': // connect
				{
					connect(message);
					break;
				}
				case 'd': // disconnect
				{
					disconnect();
					break;
				}
				case 'j': // join (new account)
				{
					join(message);
					break;
				}


				default:
				{
					System.out.println(message);
					break;
				}
			}
		}
	}



	private void connect(String message)
	{
		// TODO Auto-generated method stub
		
	}

	private void disconnect()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void join(String message)
	{
		// TODO Auto-generated method stub
		
	}

}
