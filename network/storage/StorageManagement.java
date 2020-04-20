package network.storage;

import network.human.HumanBeing;
import network.storageCommands.Command;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Содержит методы для работы с коллекциией объектов класса {@code HumanBeing}
 */
public interface StorageManagement {

    /**
     * Вывод в консоль информации о доступных командах
     */
    void help();


    /**
     * Вывод в консоль информации о коллекции объектов класса {@code HumanBeing}
     */
    void info();


    /**
     * Вывод в консоль объектов коллекции объектов класса {@code HumanBeing}
     */
    void show();


    /**
     * Добавление объекта в коллекцию объектов класса {@code HumanBeing}
     *
     * @param humanBeing Добавляемый объект
     */
    void add(HumanBeing humanBeing);


    /**
     * Обновление объекта коллекции объектов класса {@code HumanBeing}
     * по заданному id
     *
     * @param id         Id объекта
     * @param humanBeing Объект
     */
    void update(int id, HumanBeing humanBeing);


    /**
     * Удаление объекта коллекции объектов класса {@code HumanBeing}
     * по заданному id
     *
     * @param id Id удаляемого объекта
     */
    void remove_by_id(int id);


    /**
     * очистка коллеции объектов класса {@code HumanBeing}
     */
    void clear();


    /**
     * сохранение коллекции {объектов класса {@code HumanBeing} в файл
     */
    void save() throws IOException;


    /**
     * Выполнение скрипта из файла по заданному имени
     *
     * @param fileName Имя файла, в котором находится скрипт
     */
    void execute_script(String fileName);


    /**
     * Принудительное завершение программы
     */
    void exit();


    /**
     * Удаление из коллекции объектов класса {@code HumanBeing}
     * последнего добавленного элемента
     */
    void remove_head();


    /**
     * Удаление объектов коллекции объектов класса
     * {@code HumanBeing}, которые привышают заданный элемент
     *
     * @param delimiter Заданный элемент
     */
    void remove_greater(HumanBeing delimiter);


    /**
     * Вывод в консоль последних пяти команд
     */
    void history();


    /**
     * Вывод в консоль объекта коллекции объектов класса {@code HumanBeing},
     * который имеет максимальное значение переменной {@code mood}
     */
    void max_by_mood();


    /**
     * Вывод в консоль количества объектов коллекции объектов класса
     * {@code HumanBeing} с заданным значением поля {@code soundtrackName}
     *
     * @param soundtrackName Заданное значение поля {@code soundtrackName}
     */
    void count_by_soundtrack_name(String soundtrackName);


    /**
     * Вывод в консоль списка {@code soundtrackName} объектов коллекции
     * {@code humanBeings} в порядке убывания
     */
    void print_field_descending_soundtrack_name();

    void create_script(List<Command> script, String scriptName);

    void delete_script(String name);

    void delete_file(String name);

    void delete_all_scripts();

    void delete_all_files();
}