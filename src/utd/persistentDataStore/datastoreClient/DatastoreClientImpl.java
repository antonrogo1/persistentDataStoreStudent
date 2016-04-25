package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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

			logger.debug("Writing Message");
			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(Integer.toString(data.length), outputStream);
			StreamUtil.writeLine(Arrays.toString(data), outputStream);

			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
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
		logger.debug("Executing Read Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Writing Message");
			StreamUtil.writeLine("read", outputStream);
			StreamUtil.writeLine(name, outputStream);


			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			String dataLength = StreamUtil.readLine(inputStream);
			logger.debug("Read Length " + dataLength);
			String data = StreamUtil.readLine(inputStream);
			logger.debug("Read Data " + data);

		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		return null;
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

			logger.debug("Writing Message");
			StreamUtil.writeLine("delete", outputStream);
			StreamUtil.writeLine(name, outputStream);


			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
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
		logger.debug("Executing Directory Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			logger.debug("Writing Message");
			StreamUtil.writeLine("directory", outputStream);

			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			String numberOfFileNames = StreamUtil.readLine(inputStream);
			logger.debug("Number of File names to expect " + numberOfFileNames);
			for(int i = 0; i < Integer.parseInt(numberOfFileNames); i++ )
			{
				String data = StreamUtil.readLine(inputStream);
				logger.debug("File name" + data);
			}

		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
		return null;
	}


	public static void main(String args[]) throws ClientException {
		try {

			//InetAddress address = InetAddress.getByName("78.46.84.100");
			InetAddress address = InetAddress.getLocalHost();
			DatastoreClientImpl exampleClient = new DatastoreClientImpl(address,10023);

			byte[] data = "Anton".getBytes();
			byte[] data2 = "Anton2".getBytes();

			//exampleClient.write("Test", data);
			//exampleClient.write("Test2", data2);

			//exampleClient.read("Test2");

			//exampleClient.directory();

			//exampleClient.delete("Test");
		}
		catch (IOException ex) {
			logger.error(ex);
		}
	}

}
