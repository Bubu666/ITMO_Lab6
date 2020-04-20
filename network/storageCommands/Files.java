package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class Files extends AbstractCommand {
    public static final String helpInfo = "  files : выводит список файлов клиента.\n";

    public Files() {
        super("files");
    }

    @Override
    public void execute(Storage storage) {
        storage.files();
    }
}
