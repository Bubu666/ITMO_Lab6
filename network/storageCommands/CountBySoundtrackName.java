package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "count_by_soundtrack_name"
 */
public class CountBySoundtrackName extends AbstractCommand implements Command {

    /**
     * Название саундтрека
     */
    private String soundtrackName;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  count_by_soundtrack_name soundtrack_name : вывести количество элементов, значение поля soundtrackName которых равно заданному\n  count_by_soundtrack_name random : (*)\n";

    /**
     * Принимает название саундтрека
     * @param soundtrackName Название саундтрека
     */
    public CountBySoundtrackName(String soundtrackName) {
        super("count_by_soundtrack_name");
        this.soundtrackName = soundtrackName;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.count_by_soundtrack_name(soundtrackName);
    }
}
