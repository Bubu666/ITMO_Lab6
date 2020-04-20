package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "help"
 */
public class Help extends AbstractCommand implements Command {

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  help : вывести справку по доступным командам\n";

    public Help() {
        super("help");
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.help();
    }
}
