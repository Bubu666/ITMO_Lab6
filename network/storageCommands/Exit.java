package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "exit"
 */
public class Exit extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  exit : завершить программу\n";

    /**
     * Принимает ссылку на хранилище
     */
    public Exit() {
        super("exit");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.exit();
    }
}
