package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "show"
 */
public class Show extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  show : вывести все элементы коллекции в строковом представлении\n";

    public Show() {
        super("show");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.show();
    }
}
