package duke.commands;

import java.io.IOException;

import duke.tasks.TaskList;
import duke.utils.Storage;

public class ByeCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    public ByeCommand(TaskList taskList, Storage storage) {
        super(taskList, storage);
    }

    /**
     * Saves all Tasks in taskList to local file, and then prints exit message.
     * @return exit message to be displayed.
     */
    @Override
    public String execute() {
        try {
            this.storage.writeToFile(this.taskList);
            String byeMsg = "Bye. Hope to see you again soon!";
            return byeMsg;
        } catch (IOException e) {
            return e.getMessage();
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
