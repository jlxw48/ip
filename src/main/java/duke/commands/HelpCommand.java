package duke.commands;

import duke.tasks.TaskList;
import duke.utils.Storage;
import duke.utils.Ui;

public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";

    public HelpCommand(TaskList taskList, Ui ui, Storage storage) {
        super(taskList, ui, storage);
    }

    @Override
    public void execute() {
        this.ui.showHelp();
    }
}