package network.storageCommands;

import network.storage.Storage;

import java.util.List;

public class CreateScript extends AbstractCommand {
    public final static String helpInfo = "  create_script script_file_name : создать скрипт с указанным для него названием файла.\n";
    private List<Command> script;
    private String scriptFileName;

    public CreateScript(String scriptFileName, List<Command> script) {
        super("create_script");
        this.scriptFileName = scriptFileName;
        this.script = script;
    }

    @Override
    public void execute(Storage storage) {
        storage.create_script(script, scriptFileName);
    }
}
