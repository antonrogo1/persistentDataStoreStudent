package utd.persistentDataStore.datastoreServer.commands;

import org.apache.log4j.Logger;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Anton on 4/17/2016.
 */
public class DeleteCommand extends ServerCommand
{
    private static Logger logger = Logger.getLogger(ReadCommand.class);
    @Override
    public void run() throws IOException, ServerException {
        // Read request
        String fileToDelete = StreamUtil.readLine(inputStream);

        try
        {
            this.delete(fileToDelete);

            // Write response
            String outMessage = "ok" + "\n";
            StreamUtil.writeLine(outMessage, outputStream);
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

    private void delete(String fileToDelete) throws ServerException, IOException {
        FileUtil.deleteData(fileToDelete);
    }
}
