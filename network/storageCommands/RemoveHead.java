package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "remove_head"
 */
public class RemoveHead extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  remove_head : вывести первый элемент коллекции и удалить его\n";

    public RemoveHead() {
        super("remove_head");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.remove_head();
    }
}
