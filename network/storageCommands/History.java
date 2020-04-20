package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "history"
 */
public class History extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  history : вывести последние 5 команд\n";

    public History() {
        super("history");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.history();
    }
}
