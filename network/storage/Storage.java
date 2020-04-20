package network.storage;

import network.human.*;
import network.storageCommands.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Хранит коллекцию объектов класса {@code HumanBeing},
 * а также управляет этой коллекцией.
 */
public class Storage implements StorageManagement, Serializable {

    /**
     * Список объектов класса {@code HumanBeing}
     */
    private LinkedList<HumanBeing> humanBeings;

    /**
     * Список вводимых команд {@code Command}
     */
    private LinkedList<Command> commandList;

    /**
     * Файл, из которого считываются объекты класса {@code HumanBeing} при инициализации {@code humanBeings}
     */
    private File objectsFile;

    /**
     * Дата инициализации коллекции
     */
    private Date dateOfInitialization;

    private Path clientFilesPath;

    private Path clientScriptsPath;

    private transient StringWriter stringWriter = new StringWriter();

    /**
     * Поток вывода информации
     */
    private transient PrintWriter output = new PrintWriter(stringWriter);

    /**
     * Содержит всю информацию о командах
     */
    private static final String helpInfo;

    static {
        StringBuilder info = new StringBuilder();
        for (Method m : Storage.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(HelpInfo.class)) {
                info.append(m.getAnnotation(HelpInfo.class).info());
            }
        }
        helpInfo = info.toString();
    }

    {
        humanBeings = new LinkedList<>();
        commandList = new LinkedList<>();
        dateOfInitialization = new Date();
    }

    /**
     * @param objectsFileName Файл, из которого считываются объекты для инициализации
     */
    public Storage(Path clientPath, String objectsFileName) throws IOException {
        clientFilesPath = Paths.get(clientPath + "/files");
        if (!Files.exists(clientFilesPath)) Files.createDirectory(clientFilesPath);

        clientScriptsPath = Paths.get(clientPath + "/scripts");
        if (!Files.exists(clientScriptsPath)) Files.createDirectory(clientScriptsPath);

        Path objectsFilePath = Paths.get(clientFilesPath + "/" + objectsFileName);
        if (!Files.exists(objectsFilePath)) Files.createFile(objectsFilePath);

        this.objectsFile = objectsFilePath.toFile();

        if (!initCollection()) {
            output.println("  Данные в файле " + objectsFileName + " записаны в неверном формате или повреждены, объекты могут быть инициализированы не в полном объеме.");
            output.println(
                    "  Для ввода нового файла используйте \"enter_new_init_file\". Для получения справки используйте \"help\".");
        }
    }

    /**
     * Возвращает рабочий файл с объектами
     *
     * @return Файл
     */
    public File getObjectsFile() {
        return objectsFile;
    }

    /**
     * Выполняет команду
     *
     * @param command Принимаемая команда
     */
    public void apply(Command command, boolean needToBeSaved) {
        try {
            command.execute(this);
            if (needToBeSaved) commandList.addFirst(command);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Инициализация коллекции {@code humanBeings} из файла {@code objectsFile}
     *
     * @return {@code false} если возникли ошибки, иначе {@code true}
     */
    private boolean initCollection() {
        humanBeings.clear();
        boolean success = true;
        HashMap<String, Field> nameToField = new HashMap<>();

        for (Field f : HumanBeing.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(Stored.class)) {
                nameToField.put(f.getName(), f);
            }
        }

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(objectsFile)));

            if (!input.ready()) {
                return true;
            }

            String[] fields = input.readLine().trim().split(";");

            if (fields.length != nameToField.size()) {
                success = false;
            }

            for (String f : fields) {
                if (nameToField.get(f) == null) {
                    success = false;
                }
            }

            ArrayList<String[]> props = (ArrayList<String[]>) input.lines().map(l -> l.trim().split(";"))
                    .collect(Collectors.toList());

            for (int i = 0; i < props.size(); ++i) {

                if (props.get(i).length != fields.length) {
                    success = false;
                    continue;
                }

                Coordinates coordinates;
                String[] inputCoords = props.get(i)[2].split(" ");
                if (inputCoords.length != 2) {
                    success = false;
                    continue;
                }

                try {
                    coordinates = new Coordinates(Integer.parseInt(inputCoords[0]), Double.parseDouble(inputCoords[1]));
                    humanBeings.add(
                            new HumanBeing(Integer.parseInt(props.get(i)[0]),
                                    props.get(i)[1],
                                    coordinates,
                                    Boolean.parseBoolean(props.get(i)[3]),
                                    Boolean.parseBoolean(props.get(i)[4]),
                                    Double.parseDouble(props.get(i)[5]),
                                    props.get(i)[6],
                                    Integer.parseInt(props.get(i)[7]),
                                    Mood.of(props.get(i)[8]),
                                    new Car(Boolean.parseBoolean(props.get(i)[9])),
                                    Instant.parse(props.get(i)[10])
                            ));
                } catch (NumberFormatException | DateTimeParseException e) {
                    success = false;
                }
            }

        } catch (IOException e) {
            return false;
        }
        return success;
    }

    @HelpInfo(info = network.storageCommands.Files.helpInfo)
    public void files() {
        try {
            Files.list(clientFilesPath).map(Path::getFileName).forEachOrdered(f -> output.println("  " + f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @HelpInfo(info = Scripts.helpInfo)
    public void scripts() {
        try {
            Files.list(clientScriptsPath).map(Path::getFileName).forEachOrdered(f -> output.println("  " + f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @HelpInfo(info = DeleteScript.helpInfo)
    public void delete_script(String name) {
        try {
            if (Files.deleteIfExists(Paths.get(clientScriptsPath + "/" + name)))
                output.println("  скрипт " + name + " удален");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @HelpInfo(info = DeleteFile.helpInfo)
    public void delete_file(String name) {
        try {
            if (Files.deleteIfExists(Paths.get(clientFilesPath + "/" + name)))
                output.println("  файл " + name + " удален");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @HelpInfo(info = DeleteAllScripts.helpInfo)
    public void delete_all_scripts() {
        try {
            Files.list(clientScriptsPath)
                    .map(f -> f.getFileName().toString())
                    .forEachOrdered(this::delete_script);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @HelpInfo(info = DeleteAllFiles.helpInfo)
    public void delete_all_files() {
        try {
            Files.list(clientFilesPath)
                    .map(f -> f.getFileName().toString())
                    .filter(f -> !f.equals(objectsFile.getName()))
                    .forEachOrdered(this::delete_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вывод в консоль информации о доступных командах
     */
    @Override
    @HelpInfo(info = Help.helpInfo)
    public void help() {
        output.println("  Допустимые команды:\n" + helpInfo + "  * Некоторые команды поддерживают ввод случайно сгенерированного объекта (random)");
    }

    /**
     * Вывод в консоль информации о коллекции {@code humanBeings}
     */
    @Override
    @HelpInfo(info = Info.helpInfo)
    public void info() {
        output.println("  Тип коллекции: " + humanBeings.getClass().getName() + "\n  Дата инициализации: " + dateOfInitialization + "\n  Количество элементов: " + humanBeings
                .size() + "\n  Файл: " + objectsFile.getName());
    }

    /**
     * Вывод в консоль объектов коллекции {@code humanBeings}
     */
    @Override
    @HelpInfo(info = Show.helpInfo)
    public void show() {
        if (humanBeings.isEmpty()) {
            output.println("  Коллекция пуста");
        } else {
            sortCollection();
            humanBeings.forEach(output::println);
        }
    }

    /**
     * Добавление объекта в коллекцию {@code humanBeings}
     *
     * @param humanBeing Добавляемый объект
     */
    @Override
    @HelpInfo(info = Add.helpInfo)
    public void add(HumanBeing humanBeing) {
        humanBeing.init();

        if (humanBeings.add(humanBeing))
            output.println("  " + humanBeing.getName() + " добавлен в коллекцию!");
        else
            output.println("  " + humanBeing.getName() + " не добавлен в коллекцию.");
    }

    /**
     * Обновление объекта коллекции {@code humanBeings} по заданному id
     *
     * @param id         Id объекта
     * @param humanBeing Объект
     */
    @Override
    @HelpInfo(info = Update.helpInfo)
    public void update(int id, HumanBeing humanBeing) {
        humanBeing.init();

        if (humanBeings.size() == (humanBeings = humanBeings.stream().filter(hb -> hb.getId() != id)
                .collect(Collectors.toCollection(LinkedList::new))).size()) {
            output.println("  В коллекции нет элемента с данным id");
        } else {
            humanBeings.add(humanBeing);
            output.println("  Коллекция обновлена");
        }
    }

    /**
     * Удаление объекта коллекции {@code humanBeings} по заданному id
     *
     * @param id Id удаляемого объекта
     */
    @Override
    @HelpInfo(info = RemoveById.helpInfo)
    public void remove_by_id(int id) {
        if (humanBeings.size() == (humanBeings = humanBeings.stream().filter(hb -> hb.getId() != id)
                .collect(Collectors.toCollection(LinkedList::new))).size()) {
            output.println("  Элемент не удален");
        } else {
            output.println("  Элемент удален");
        }
    }

    /**
     * очистка коллеции {@code humanBeings}
     */
    @Override
    @HelpInfo(info = Clear.helpInfo)
    public void clear() {
        if (humanBeings.size() > 0) {
            humanBeings.clear();
            output.println("  Все элементы удалены");
        } else {
            output.println("  Коллекция пуста");
        }
    }

    /**
     * сохранение коллекции {@code humanBeings} в файл
     */
    @Override
    public void save() throws IOException {
        try (FileWriter fw = new FileWriter(objectsFile)) {
            fw.write(HumanBeing.fieldHeadersCSVString() + "\n");

            for (HumanBeing hb : humanBeings) {
                fw.write(hb.toCSVFormatString() + "\n");
            }
        }
    }

    /**
     * Выполнение скрипта из файла по заданному имени
     *
     * @param fileName Имя файла, в котором находится скрипт
     */
    @Override
    @HelpInfo(info = ExecuteScript.helpInfo)
    public void execute_script(String fileName) {

        Path scriptPath = Paths.get(clientScriptsPath + "/" + fileName);

        if (!Files.exists(scriptPath)) {
            output.println("  Скрипт " + fileName + " не существует.\n  Для создания скрипта используйте create_script");
            return;
        }

        LinkedList<Command> script = new LinkedList<>();

        File scriptFile = scriptPath.toFile();

        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(scriptFile))) {

            while (true) {
                final Command command = (Command) objIn.readObject();

                if (command instanceof ExecuteScript) {
                    if (((ExecuteScript) command).getFileName().equals(fileName)) {
                        output.println("  Скрипт " + fileName + " является зацикленным и не может быть исполнен");
                        return;
                    }
                }

                script.add(command);
            }

        } catch (ClassNotFoundException e) {
            output.println("  Скрипт " + fileName + " не корректен");
            return;
        } catch (IOException ignored) {
        }

        script.forEach(c -> this.apply(c, false));
    }

    /**
     * Принудительное завершение программы
     */
    @Override
    @HelpInfo(info = Exit.helpInfo)
    public void exit() {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Удаление из коллекции {@code humanBeings} последнего добавленного элемента
     */
    @Override
    @HelpInfo(info = RemoveHead.helpInfo)
    public void remove_head() {

        if (humanBeings.size() > 0) {
            output.println(humanBeings.poll());
            output.println("  Элемент удален");
        } else {
            output.println("  Коллекция пуста");
        }
    }

    /**
     * Удаление объектов коллекции {@code humanBeings},
     * которые привышают заданный элемент
     *
     * @param delimiter Заданный элемент
     */
    @Override
    @HelpInfo(info = RemoveGreater.helpInfo)
    public void remove_greater(HumanBeing delimiter) {
        delimiter.init();

        int prevSize = humanBeings.size();

        humanBeings = humanBeings.stream().filter(hb -> hb.compareTo(delimiter) > 0)
                .collect(Collectors.toCollection(LinkedList::new));

        output.println("  Удалено " + (prevSize - humanBeings.size()) + " элементов");
    }

    /**
     * Вывод в консоль последних пяти команд {@code commandList}
     */
    @Override
    @HelpInfo(info = History.helpInfo)
    public void history() {
        if (commandList.size() > 0) {
            output.println(" Последние команды:");
            commandList.stream().limit(5).forEachOrdered(c -> output.println("   " + c));
        } else {
            output.println("  Нет последних команд");
        }
    }

    /**
     * Вывод в консоль объекта коллекции {@code humanBeings}, который имеет максимальное значение переменной {@code mood}
     */
    @Override
    @HelpInfo(info = MaxByMood.helpInfo)
    public void max_by_mood() {
        humanBeings.stream().max(Comparator.comparing(HumanBeing::getMood))
                .ifPresent(output::println);
    }

    /**
     * Вывод в консоль количества объектов коллекции {@code humanBeings}
     * с заданным значением поля {@code soundtrackName}
     *
     * @param soundtrackName Заданное значение поля {@code soundtrackName}
     */
    @Override
    @HelpInfo(info = CountBySoundtrackName.helpInfo)
    public void count_by_soundtrack_name(String soundtrackName) {
        output.println("  " + humanBeings.stream()
                .filter(h -> h.getSoundtrackName().equals(soundtrackName)).count());
    }

    /**
     * Вывод в консоль значений поля {@code soundtrackName} объектов коллекции {@code humanBeings} в порядке убывания
     */
    @Override
    @HelpInfo(info = PrintFieldDescendingSoundtrackName.helpInfo)
    public void print_field_descending_soundtrack_name() {
        if (humanBeings.size() > 0) {
            humanBeings.stream().map(HumanBeing::getSoundtrackName).sorted(Comparator.reverseOrder())
                    .forEachOrdered(output::println);
        } else {
            output.println("  Коллекция пуста!");
        }
    }

    @Override
    @HelpInfo(info = CreateScript.helpInfo)
    public void create_script(List<Command> script, String scriptName) {
        try {
            Path scriptFilePath = Paths.get(clientScriptsPath + "/" + scriptName);
            if (!Files.exists(scriptFilePath)) Files.createFile(scriptFilePath);

            File scriptFile = scriptFilePath.toFile();

            try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(scriptFile))) {
                for (Command command : script) {
                    objOut.writeObject(command);
                }
            }

            output.println("  Скрипт создан");
        } catch (IOException e) {
            output.println("  Скрипт не создан: " + e.getMessage());
        }
    }

    /**
     * Инициализация коллекции из нового файла
     *
     * @param fileName Имя нового файла
     */
    @HelpInfo(info = ChangeFile.helpInfo)
    public void change_file(String fileName) {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File file = Paths.get(clientFilesPath + "/" + fileName).toFile();

        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
                output.println("  Не удалось создать файл");
                return;
            }
        }

        objectsFile = file;

        if (!initCollection()) {
            output.println("  Данные в файле " + objectsFile + " записаны в неверном формате или повреждены, объекты могут быть инициализированы не в полном объеме.");
            output.println("  Для ввода нового файла используйте \"enter_new_init_file\". Для получения справки используйте \"help\".");

        }
        output.println("  Коллекция обновлена");
    }

    /**
     * Сортировка коллекции {@code humanBeings}
     */
    public void sortCollection() {
        humanBeings.sort(HumanBeing::compareTo);
    }

    public StringBuffer getInputBuffer() {
        return stringWriter.getBuffer();
    }
}