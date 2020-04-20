package network.storageCommands;

import network.storage.Storage;

import java.io.Serializable;
import java.util.Objects;

/**
 * Абстрактный суперкласс для всех команд
 */
public abstract class AbstractCommand implements Command, Serializable {

    /**
     * Имя команды в пользовательском представлении
     */
    protected final String name;

    /**
     * Принимает название команды
     * @param name Название команды
     */
    protected AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCommand)) return false;
        AbstractCommand that = (AbstractCommand) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
