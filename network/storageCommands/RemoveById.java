package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "remove_by_id"
 */
public class RemoveById extends AbstractCommand implements Command {

    /**
     * id объекта
     */
    private int id;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  remove_by_id id : удалить элемент из коллекции по его id\n";

    /**
     * Принимает id объекта
     * @param id Id объекта
     */
    public RemoveById(int id) {
        super("remove_by_id");
        this.id = id;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.remove_by_id(id);
    }
}
