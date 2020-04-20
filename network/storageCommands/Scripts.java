package network.storageCommands;

import network.storage.Storage;

public class Scripts extends AbstractCommand {
    public static final String helpInfo = "  scripts : выводит список скриптов клиента.\n";

    public Scripts() {
        super("scripts");
    }

    @Override
    public void execute(Storage storage) {
        storage.scripts();
    }
}
