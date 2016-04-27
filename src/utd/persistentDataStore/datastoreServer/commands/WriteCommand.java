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
        // Read request
        String fileName = StreamUtil.readLine(inputStream);
        String fileDataLength = StreamUtil.readLine(inputStream);
        byte[] fileData = StreamUtil.readData(Integer.parseInt(fileDataLength),inputStream);

        this.writeData(fileName, fileData);

        // Write response
        String outMessage = "ok" + "\n";
        StreamUtil.writeLine(outMessage, outputStream);

        logger.debug("Finished writing message");

    }

    private void writeData(String name, byte[] data) throws ServerException, IOException {
        FileUtil.writeData(name, data);
    }
}
