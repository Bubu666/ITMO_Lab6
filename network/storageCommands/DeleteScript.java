package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteScript extends AbstractCommand {
    private String name;

    public static final String helpInfo = "  delete_script script_name : удалить указанный скрипт из каталога клиента\n";

    public DeleteScript(String name) {
        super("delete_script");
        this.name = name;
    }

    @Override
    public void execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        storage.delete_script(name);
    }
}
