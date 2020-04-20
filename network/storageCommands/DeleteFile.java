package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteFile extends AbstractCommand {
    private String name;

    public static final String helpInfo = "  delete_file file_name : удалить указанный файл из каталога клиента\n";

    public DeleteFile(String name) {
        super("delete_file");
        this.name = name;
    }

    @Override
    public void execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        storage.delete_file(name);
    }
}
