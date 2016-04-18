package utd.persistentDataStore.simpleSocket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.log4j.Logger;

import utd.persistentDataStore.datastoreClient.ClientException;
import utd.persistentDataStore.utils.StreamUtil;

public class ExampleClient
{
	private static Logger logger = Logger.getLogger(ExampleClient.class);
	
	private InetAddress address;
	private int port;

	public ExampleClient(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/**
	 * Sends the given string to the server which will echo it back
	 */
	public String echo(String message) throws ClientException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");
			StreamUtil.writeLine("echo\n", outputStream);
			StreamUtil.writeLine(message, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			
			return result;
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}
		
	/**
	 * Sends the given string to the server which will echo it back
	 */
	public String reverse(String message) throws ClientException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");
			StreamUtil.writeLine("reverse\n", outputStream);
			StreamUtil.writeLine(message, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			
			return result;
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}


	public static void main(String args[]) throws ClientException {
		try {

			//InetAddress address = InetAddress.getByName("78.46.84.100");
			InetAddress address = InetAddress.getLocalHost();
			ExampleClient exampleClient = new ExampleClient(address,10023);
			exampleClient.reverse("Hello Test");
		}
		catch (IOException ex) {
			logger.error(ex);
		}
	}

}
