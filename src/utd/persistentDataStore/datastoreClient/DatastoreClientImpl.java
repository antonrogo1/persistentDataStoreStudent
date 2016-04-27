package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException
	{
		logger.debug("Executing Write Operation");

		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Sending Request");
			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(Integer.toString(data.length), outputStream);
			StreamUtil.writeData(data, outputStream);

			logger.debug("Reading Response");
			String responseCode = StreamUtil.readLine(inputStream);
			logger.debug("Response Code " + responseCode);
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException
	{
		byte[] readData;
		logger.debug("Executing Read Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Sending Request");
			StreamUtil.writeLine("read", outputStream);
			StreamUtil.writeLine(name, outputStream);


			logger.debug("Reading Response");
			String responseCode = StreamUtil.readLine(inputStream);
			if(responseCode.equalsIgnoreCase("ok"))
			{
				logger.debug("Response Code " + responseCode);
				String dataLength = StreamUtil.readLine(inputStream);
				logger.debug("Read Length " + dataLength);
				readData = StreamUtil.readData(Integer.parseInt(dataLength),inputStream);
				logger.debug("Read Data " + new String(readData) );
			}
			else
			{
				//String errorMessage = StreamUtil.readLine(inputStream);
				throw new ClientException(responseCode);
			}

		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		return readData;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException
	{
		logger.debug("Executing Delete Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Sending Request");
			StreamUtil.writeLine("delete", outputStream);
			StreamUtil.writeLine(name, outputStream);

			logger.debug("Reading Response");
			String responseCode = StreamUtil.readLine(inputStream);

			if(responseCode.equalsIgnoreCase("ok")) {
				logger.debug("Response Code " + responseCode);
			}
			else
			{
				throw new ClientException(responseCode);
			}
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}

	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException
	{
		List<String> fileNameList = new ArrayList<String>();
		logger.debug("Executing Directory Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Sending Request");
			StreamUtil.writeLine("directory", outputStream);

			logger.debug("Reading Response");
			String responseCode = StreamUtil.readLine(inputStream);
			logger.debug("Response Code " + responseCode);
			String numberOfFileNames = StreamUtil.readLine(inputStream);
			logger.debug("Number of File names to expect " + numberOfFileNames);
			for(int i = 0; i < Integer.parseInt(numberOfFileNames); i++ )
			{
				String fileName = StreamUtil.readLine(inputStream);
				fileNameList.add(fileName);
				logger.debug("File name " + fileName);
			}

		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		return fileNameList;
	}

	//Test Main method for debugging/testing
//	public static void main(String args[]) throws ClientException {
//		try {
//			//local
//			InetAddress address = InetAddress.getLocalHost();
//			DatastoreClientImpl exampleClient = new DatastoreClientImpl(address,10023);
//
//			//amazon
////			InetAddress address = InetAddress.getByName("ec2-52-38-188-85.us-west-2.compute.amazonaws.com");
////			DatastoreClient exampleClient = new DatastoreClientImpl(address, 10023);
//
//			byte[] data = "Anton".getBytes();
//			byte[] data2 = "Anton3".getBytes();
//
//			exampleClient.write("Test", data);
//			exampleClient.write("Test2", data2);
//
//			exampleClient.read("Test2");
//			exampleClient.read("Test3");
//
//			exampleClient.directory();
//			exampleClient.delete("Test");
//			exampleClient.delete("Test3");
//			exampleClient.delete("Test2");
//			exampleClient.directory();
//
//		}
//		catch (IOException ex) {
//			logger.error(ex);
//		}
//	}
}
