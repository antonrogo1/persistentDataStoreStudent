package utd.persistentDataStore.datastoreServer.commands;

import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Anton on 4/17/2016.
 */
public class ReadCommand extends ServerCommand
{
    private static Logger logger = Logger.getLogger(ReadCommand.class);

    @Override
    public void run() throws IOException, ServerException
    {
        // Read request
        String inDatasetName = StreamUtil.readLine(inputStream);

        try {
            byte[] responseData = this.read(inDatasetName);

            // Write response
            String outMessage = "ok" + "\n";
            StreamUtil.writeLine(outMessage, outputStream);
            StreamUtil.writeLine(Integer.toString(responseData.length), outputStream);
            StreamUtil.writeData(responseData, outputStream);
            logger.debug("Finished writing message");
        }
        catch (ServerException ex)
        {
            // Write response
            String outMessage = "error " + ex.getMessage() +"\n";
            StreamUtil.writeLine(outMessage, outputStream);
            logger.debug("Finished writing error message");
        }
    }

    private byte[] read(String readName) throws ServerException, IOException {
        byte[] readData = FileUtil.readData(readName);
        return readData;
    }
}
