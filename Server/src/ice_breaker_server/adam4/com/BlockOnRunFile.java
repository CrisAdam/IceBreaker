package ice_breaker_server.adam4.com;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class BlockOnRunFile
{
    private File watchedFile;
    private Path path;
    private WatchService watcher;

    public void end()
    {
        watchedFile.delete();
    }

    public BlockOnRunFile(String runFilePath) throws IOException
    {
        watcher = FileSystems.getDefault().newWatchService();
        path = FileSystems.getDefault().getPath(runFilePath.substring(0, runFilePath.lastIndexOf(FileSystems.getDefault().getSeparator())));
        watchedFile = new File(runFilePath);
        watchedFile.createNewFile();
    }

    public void block()
    {
        try
        {
            WatchKey key;
            key = path.register(watcher, StandardWatchEventKinds.ENTRY_DELETE);
            // stall until the game is supposed to end
            // reset key to allow new events to be detected
            while (key.reset())
            {
                try
                {
                    for (WatchEvent<?> event : key.pollEvents())
                    {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW)
                        {
                            System.out.println("File watcher overflow");
                            if (!watchedFile.exists())
                            {
                                // do nothing
                                ;
                            }
                            break;
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE)
                        {
                            @SuppressWarnings("unchecked")
                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path filename = ev.context();
                            if (filename.toAbsolutePath().toString().equals(watchedFile.getAbsolutePath().toString()))
                            {
                                watcher.close();
                                System.out.println("watchedFile file has been deleted");
                                break;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    watcher.close();
                    continue;
                }
            }// end while loop
        }
        catch (IOException e1)
        {

        }
    }
}

