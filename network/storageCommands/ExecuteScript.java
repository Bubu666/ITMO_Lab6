package network.storageCommands;

import network.storage.Storage;

/**
 * Команда "execute_script"
 */
public class ExecuteScript extends AbstractCommand implements Command {

    /**
     * Имя файла
     */
    private String fileName;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  execute_script file_name : считать и исполнить скрипт из указанного файла. \n";

    /**
     * Принимает имя файла
     * @param fileName Имя файла
     */
    public ExecuteScript(String fileName) {
        super("execute_script");
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.execute_script(fileName);
    }
}
