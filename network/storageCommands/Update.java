package network.storageCommands;

import network.human.HumanBeing;
import network.storage.Storage;

/**
 * Команда "update"
 */
public class Update extends AbstractCommand implements Command {

    /**
     * Введенный пользователем объект
     */
    private HumanBeing humanBeing;

    /**
     * id объекта
     */
    private int id;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  update id : обновить значение элемента коллекции, id которого равен заданному\n  update id random : (*)\n";

    /**
     * Принимает id объекта и объект {@code HumanBeing}
     * @param id Id
     * @param newHumanBeing Новый объект
     */
    public Update(int id, HumanBeing newHumanBeing) {
        super("update");
        this.id = id;
        this.humanBeing = newHumanBeing;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        storage.update(id, humanBeing);
    }
}
