package utd.persistentDataStore.datastoreServer.commands;

import utd.persistentDataStore.utils.ServerException;

import java.io.IOException;

/**
 * Created by Anton on 4/17/2016.
 */
public class DeleteCommand extends ServerCommand
{
    @Override
    public void run() throws IOException, ServerException {
        System.out.print("Running Delete");
    }
}
