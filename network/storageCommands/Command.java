package network.storageCommands;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

/**
 * Интерфейс для реализации команд
 */
public interface Command {

    /**
     * Выполняет команду
     *
     * @throws InvocationTargetException _
     * @throws IllegalAccessException _
     */
    void execute(Storage storage) throws InvocationTargetException, IllegalAccessException;
}
