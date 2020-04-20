package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteAllFiles extends AbstractCommand {
    public static final String helpInfo = "  delete_all_files : удаляет из каталога клиента все файлы кроме текущего\n";

    public DeleteAllFiles() {
        super("delete_all_files");
    }

    @Override
    public void execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        storage.delete_all_files();
    }
}
