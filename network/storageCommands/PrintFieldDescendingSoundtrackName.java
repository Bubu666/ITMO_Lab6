package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "print_field_descending_soundtrack_name"
 */
public class PrintFieldDescendingSoundtrackName extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  print_field_descending_soundtrack_name : вывести значения поля soundtrackName в порядке убывания\n";

    public PrintFieldDescendingSoundtrackName() {
        super("print_field_descending_soundtrack_name");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.print_field_descending_soundtrack_name();
    }
}
