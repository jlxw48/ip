package duke.commands;

import duke.tasks.TaskList;
import duke.utils.Storage;

public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";

    public ListCommand(TaskList taskList, Storage storage) {
        super(taskList, storage);
    }

    /**
     * Prints all tasks in taskList.
     * @return String representation of all tasks in taskList.
     */
    @Override
    public String execute() {
        return this.taskList.getListInString();
    }
}
