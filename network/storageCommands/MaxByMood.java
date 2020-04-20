package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "max_by_mood"
 */
public class MaxByMood extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  max_by_mood : вывести любой объект из коллекции, значение поля mood которого является максимальным\n";

    public MaxByMood() {
        super("max_by_mood");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.max_by_mood();
    }
}
