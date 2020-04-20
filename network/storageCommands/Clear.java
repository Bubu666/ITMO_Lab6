package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "clear"
 */
public class Clear extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  clear : очистить коллекцию\n";

    public Clear() {
        super("clear");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.clear();
    }
}
