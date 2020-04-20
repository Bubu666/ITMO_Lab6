package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteAllScripts extends AbstractCommand {

    public static final String helpInfo = "  delete_all_scripts : удалить все скрипты в каталоге клиента\n";

    public DeleteAllScripts() {
        super("delete_all_scripts");
    }

    @Override
    public void execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        storage.delete_all_scripts();
    }
}
