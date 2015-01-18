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
	public String username;

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
			case 'a': // answer invite
			{ 	// username;T|F
				invite(message);
				break;
			}
			case 'c': // connect
			{// username;password
				connect(message);
				break;
			}
			case 'd': // disconnect
			{ // ;disc
				disconnect();
				break;
			}
			case 'i': // invite
			{ // username
				invite(message);
				break;
			}
			case 'j': // join (new account)
			{ // username;password
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
		String[] params = message.split(Common.SEPARATOR);
		if (Common.isGoodUserName(params[1]) && DatabaseRequestHandler.connect(params[1], params[2]))
		{
			username = params[1];
			sendMessage("a" + Common.SEPARATOR + "connected successfully");
		}
		else
		{
			sendMessage("e" + Common.SEPARATOR + "unable to connect");
		}

	}

	private void disconnect()
	{
		sendMessage("d" + Common.SEPARATOR + "disconnect");
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void join(String message)
	{
		String[] params = message.split(Common.SEPARATOR);
		if (DatabaseRequestHandler.join(params[1], params[2]))
		{
			sendMessage("j" + Common.SEPARATOR + "joined successfully");
		}
		else
		{
			sendMessage("e" + Common.SEPARATOR + "unable to join");
		}
	}

	private boolean invite(String message)
	{
		String[] params = message.split(Common.SEPARATOR);
		if (params[1].equals(username)) // can't invite self
		{
			sendMessage("e" + Common.SEPARATOR + "can't invite self");
			return false;
		}
		
		for (ClientHandler c : IceBreakerServer.clients)
		{
			if (c.username.equals(params[1]))
			{
				c.sendInvite(username);
			}
		}
		return true;
		
		// send invite will call answerInvite with an answer

	}

	public void sendInvite(String username2)
	{
		sendMessage("i" + Common.SEPARATOR + username2);
	}
	
	public void answerInvite()
	{
		
	}

	private void photo(String message)
	{
		// TODO Auto-generated method stub

	}

	private void sendMessage(String message)
	{
		// messages to send
		// guest
		// host
		// ack
		// nac (|uname|pw)
		// invite
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
