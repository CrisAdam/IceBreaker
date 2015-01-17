package ice_breaker_server.adam4.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
		IceBreakerServer.clients.add(this);
		BufferedReader input = null; // not sure if I need the buffering, but
										// having the getLine() is nice
		String message = "";

		try
		{
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8.newDecoder()));
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
				{// username|email;password
					connect(message);
					break;
				}
				case 'd': // disconnect
				{ // ;disc
					disconnect();
					break;
				}
				case 'c': // connect
				{// username|email;password
					connect(message);
					break;
				}
				case 'g': // guest
				{// username|email;password
					connect(message);
					break;
				}
				case 'h': // host
				{// username|email;password
					connect(message);
					break;
				}
				case 'i': // invite
				{ // username|email
					invite(message);
					break;
				}
				case 'j': // join (new account)
				{ // username;password;email
					join(message);
					break;
				}
				case 'p': // photo
				{ // ?????
					photo(message);
					break;
				}

				default:
				{
					System.out.println(message);
					break;
				}
			}
		}// end while loop
		IceBreakerServer.clients.remove(this);
	}

	private void connect(String message)
	{
		if (DatabaseRequestHandler.connect(message))
		{
			sendMessage("  ");
		}
		else
		{
			sendMessage("e" + Common.SEPARATOR + "unable to connect");
		}

	}

	private void disconnect()
	{
		// TODO Auto-generated method stub

	}

	private void join(String message)
	{
		// TODO Auto-generated method stub

	}

	private void invite(String message)
	{
		// search for person
		// ask person yay/nay
		// return response

	}

	private void photo(String message)
	{
		// TODO Auto-generated method stub

	}

	private void sendMessage(String message)
	{
		if (message.charAt(message.length() - 1) != '\n')
		{
			message += '\n';
		}
		try
		{
			clientSocket.getOutputStream().write((message.getBytes(Common.ENCODING)));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
