package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "info"
 */
public class Info extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  info : вывести информацию о коллекции\n";

    public Info() {
        super("info");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.info();
    }
}
