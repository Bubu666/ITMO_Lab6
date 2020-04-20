package network.storageCommands;

import network.human.HumanBeing;
import network.storage.Storage;

import java.util.Arrays;

/**
 * Команда "add"
 */
public class Add extends AbstractCommand implements Command {

    /**
     * Введенный пользователем объект
     */
    private HumanBeing[] people;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  add : добавить новый элемент в коллекцию\n  add name_of_human\n  add random : (*)\n  add random num_of_elements : (*)\n";

    /**
     * Принимает ссылку на новый объект {@code HumanBeing}
     * @param people Новый объект
     */
    public Add(HumanBeing[] people) {
        super("add");
        this.people = people;
    }

    /**
     * Реализация команды
     */
    @Override
    public void execute(Storage storage) {
        Arrays.stream(people).forEach(storage::add);
    }
}