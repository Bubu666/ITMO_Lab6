package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "change_file"
 */
public class ChangeFile extends AbstractCommand implements Command {

    /**
     * Имя файла
     */
    private String fileName;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  change_file file_name : поменять файл на указанный \n";

    /**
     * Принимает имя файла
     * @param newFileName Имя файла
     */
    public ChangeFile(String newFileName) {
        super("change_file");
        fileName = newFileName;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.change_file(fileName);
    }
}
