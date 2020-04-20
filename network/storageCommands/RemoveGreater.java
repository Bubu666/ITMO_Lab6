package network.storageCommands;

import network.human.HumanBeing;
import network.storage.Storage;

/**
 * Команда "remove_greater"
 */
public class RemoveGreater extends AbstractCommand implements Command {

    /**
     * Введенный пользователем объект
     */
    private HumanBeing humanBeing;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  remove_greater : удалить из коллекции все элементы, превышающие заданный\n  remove_greater name_of_human\n  remove_greater random : (*)\n";

    /**
     * Принимает объект {@code HumanBeing}
     * @param humanBeing Объект
     */
    public RemoveGreater(HumanBeing humanBeing) {
        super("remove_greater");
        this.humanBeing = humanBeing;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.remove_greater(humanBeing);
    }
}
