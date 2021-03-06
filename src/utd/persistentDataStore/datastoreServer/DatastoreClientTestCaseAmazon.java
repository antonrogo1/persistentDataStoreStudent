package utd.persistentDataStore.datastoreServer;

import org.junit.Test;
import utd.persistentDataStore.datastoreClient.ClientException;
import utd.persistentDataStore.datastoreClient.DatastoreClient;
import utd.persistentDataStore.datastoreClient.DatastoreClientImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatastoreClientTestCaseAmazon
{
	String serverAddress = "ec2-52-38-188-85.us-west-2.compute.amazonaws.com";
	int port = 10023;

	@Test
	public void testWrite() throws Exception
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);

		byte data[] = generateData(100);
		dsClient.write("testData", data);
	}

	@Test
	public void testRead() throws Exception
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);

		byte dataOut[] = generateData(100);
		dsClient.write("testData", dataOut);

		byte dataIn[] = dsClient.read("testData");
		assertEquals(100, dataIn.length);

		Checksum dataOutChecksum = new CRC32();
		dataOutChecksum.update(dataOut, 0, dataOut.length);
		long checksumOut = dataOutChecksum.getValue();

		Checksum dataInChecksum = new CRC32();
		dataInChecksum.update(dataIn, 0, dataIn.length);
		long checksumIn = dataInChecksum.getValue();
		
		assertEquals(checksumOut, checksumIn);
	}

	/**
	 * Attempt to read named data that does not exist on the server. 
	 * Expect a DatastoreClientException
	 * @throws UnknownHostException 
	 * @throws Exception 
	 */
	@Test (expected=ClientException.class)
	public void testReadBroken() throws Exception 
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);
		
		byte dataIn[] = dsClient.read("missingData");
	}
	
	@Test
	public void testDelete() throws Exception
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);
		
		byte data[] = generateData(10);
		dsClient.write("testData", data);

		dsClient.delete("testData");
	}

	/**
	 * Attempt to delete named data that does not exist on the server. 
	 * Expect a DatastoreClientException
	 */
	@Test (expected=ClientException.class)
	public void testDeleteBroken() throws Exception
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);
		
		dsClient.delete("missingData");
	}

	@Test
	public void testDirectory() throws Exception
	{
		InetAddress address = InetAddress.getByName(serverAddress);
		DatastoreClient dsClient = new DatastoreClientImpl(address, port);
		
		byte data[] = generateData(10);
		dsClient.write("testData", data);

		List<String> names = dsClient.directory();
		assertTrue(names.size() > 0);
		for(String name : names) {
			System.out.println(name);
		}
	}

	private byte[] generateData(int size)
	{
		byte data[] = new byte[size];
		Random random = new Random();
		random.nextBytes(data);
		return data;
	}
}
