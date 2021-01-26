package duke.commands;

import duke.tasks.TaskList;
import duke.utils.Storage;
import duke.utils.Ui;

import java.io.IOException;

public class ByeCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    public ByeCommand(TaskList taskList, Ui ui, Storage storage) {
        super(taskList, ui, storage);
    }

    /**
     * Saves all Tasks in taskList to local file, and then prints exit message.
     */
    @Override
    public void execute() {
        try {
            this.storage.writeToFile(taskList);
            String endMessage = "Bye. Hope to see you again soon!";
            this.ui.showMsg(endMessage);
        } catch (IOException e) {
            this.ui.showError(e.getMessage());
        }
    }

    /**
     * Returns signal indicating to exit the program.
     * @return boolean signal indicating to exit the program.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
