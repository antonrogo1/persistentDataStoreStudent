package utd.persistentDataStore.datastoreServer.commands;

import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import java.io.IOException;

/**
 * Created by Anton on 4/17/2016.
 */
public class WriteCommand extends ServerCommand{

    private static Logger logger = Logger.getLogger(ReadCommand.class);
    @Override
    public void run() throws IOException, ServerException
    {
        // Read message
        String inDatasetName = StreamUtil.readLine(inputStream);
        String inDatasetLength = StreamUtil.readLine(inputStream);
        String inDatasetData = StreamUtil.readLine(inputStream);

        logger.debug("inDatasetName: " + inDatasetName);
        logger.debug("inDatasetLength: " + inDatasetLength);
        logger.debug("inDatasetData: " + inDatasetData);

        byte[] data = new byte[Integer.parseInt(inDatasetLength)];
        data = inDatasetData.getBytes();

        this.writeData(inDatasetName, data);

        // Write response
        String outMessage = inDatasetName + "\n";
        StreamUtil.writeLine(outMessage, outputStream);
        logger.debug("Finished writing message");

    }

    private void writeData(String name, byte[] data) throws ServerException, IOException {
        FileUtil.writeData(name, data);
    }
}
