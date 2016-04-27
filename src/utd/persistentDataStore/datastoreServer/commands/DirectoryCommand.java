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
public class DirectoryCommand extends ServerCommand {
    private static Logger logger = Logger.getLogger(ReadCommand.class);

    @Override
    public void run() throws IOException, ServerException {

        // Getting directory listing
        List<String> directoryList = this.directory();

        // Write response
        String outMessage = "ok" + "\n";
        StreamUtil.writeLine(outMessage, outputStream);
        StreamUtil.writeLine(Integer.toString(directoryList.size()), outputStream);
        for(String item : directoryList)
        {
            StreamUtil.writeLine(item, outputStream);
        }

        logger.debug("Finished writing message");
    }

    private List<String> directory() throws ServerException, IOException {
        List<String> directoryList = FileUtil.directory();
        return directoryList;
    }
}
