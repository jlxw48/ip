package duke.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import duke.commands.ByeCommand;
import duke.commands.Command;
import duke.commands.DeadlineCommand;
import duke.commands.DeleteCommand;
import duke.commands.DoneCommand;
import duke.commands.EventCommand;
import duke.commands.FindCommand;
import duke.commands.HelpCommand;
import duke.commands.ListCommand;
import duke.commands.ToDoCommand;
import duke.dukeexceptions.EmptyArgumentException;
import duke.dukeexceptions.EmptyListException;
import duke.dukeexceptions.InvalidCommandException;
import duke.dukeexceptions.InvalidDateTimeException;
import duke.dukeexceptions.InvalidIndexInputException;
import duke.tasks.TaskList;

public class Parser {
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("[d/M/yyyy HHmm][d MMM yy HHmm]"
            + "[dd-MM-yy HHmm]");
    private static final Pattern REGEX_CHECK_NUMBER = Pattern.compile("^[0-9]$");
    private static final String INVALID_TASK_MSG = "Please input a valid task description!";

    private final TaskList taskList;
    private final Storage storage;

    /**
     * Constructors a Parser object, responsible for parsing input from the user.
     *
     * @param taskList the list of tasks.
     * @param storage the object in charge of writing to the local storage file.
     */
    public Parser(TaskList taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Returns command associated with the command line input from user.
     *
     * @param input command line input from user.
     * @return command associated with input from user.
     * @throws EmptyArgumentException when only a 1 word command is passed without any following input.
     * @throws InvalidDateTimeException when date entered by user is not a valid date or not an acceptable date format.
     * @throws InvalidIndexInputException when index entered by user is not a number or not within range of 1 to
     *     the size of the TaskList.
     * @throws EmptyListException when trying to find by keyword but TaskList is empty.
     */
    public Command parse(String input) throws EmptyArgumentException, InvalidDateTimeException,
            InvalidIndexInputException, EmptyListException, InvalidCommandException {
        String[] commandAndInput = input.split(" ", 2);
        String command = commandAndInput[0];

        switch (command) {
        case ToDoCommand.COMMAND_WORD:
            return prepareToDo(commandAndInput);

        case DeadlineCommand.COMMAND_WORD:
            return prepareDeadline(commandAndInput);

        case EventCommand.COMMAND_WORD:
            return prepareEvent(commandAndInput);

        case FindCommand.COMMAND_WORD:
            return prepareFind(commandAndInput);

        case DoneCommand.COMMAND_WORD:
            return prepareDone(commandAndInput);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(commandAndInput);

        case ListCommand.COMMAND_WORD:
            return prepareList(commandAndInput);

        case ByeCommand.COMMAND_WORD:
            return prepareExit(commandAndInput);

        case HelpCommand.COMMAND_WORD:
            return prepareHelp(commandAndInput);

        default:
            throw new InvalidCommandException();
        }
    }

    private Command prepareToDo(String[] commandAndInput) throws EmptyArgumentException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException(INVALID_TASK_MSG);
        }

        assert commandAndInput.length == 2;

        return new ToDoCommand(this.taskList, this.storage, commandAndInput[1]);
    }

    private Command prepareDeadline(String[] commandAndInput) throws EmptyArgumentException, InvalidDateTimeException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException(INVALID_TASK_MSG);
        }

        assert commandAndInput.length == 2;

        String description = commandAndInput[1];
        String[] taskInputAndDate = description.split("/", 2);

        assert taskInputAndDate.length == 2;

        trimInputsInArray(taskInputAndDate);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
            return new DeadlineCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException();
        }
    }

    private Command prepareEvent(String[] commandAndInput) throws EmptyArgumentException, InvalidDateTimeException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException(INVALID_TASK_MSG);
        }

        assert commandAndInput.length == 2;

        String description = commandAndInput[1];
        String[] taskInputAndDate = description.split("/", 2);

        assert taskInputAndDate.length == 2;

        trimInputsInArray(taskInputAndDate);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
            return new EventCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException();
        }
    }

    private Command prepareFind(String[] commandAndInput) throws EmptyArgumentException, EmptyListException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException("Please pass a word after the 'find' command!");
        }

        if (this.taskList.isEmpty()) {
            throw new EmptyListException();
        }

        assert commandAndInput.length == 2;

        return new FindCommand(this.taskList, this.storage, commandAndInput[1]);
    }

    private Command prepareDone(String[] commandAndInput) throws InvalidIndexInputException, EmptyArgumentException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException("Please pass an index after the 'done' command!");
        }

        assert commandAndInput.length == 2;

        int position = calcListPos(commandAndInput);

        if (this.taskList.isEmpty()) {
            throw new InvalidIndexInputException("You have already done all tasks!");
        } else if (position >= this.taskList.getList().size() || position < 0) {
            throw new InvalidIndexInputException("Please input an index from 1 to "
                    + this.taskList.getList().size() + "!");
        }

        return new DoneCommand(this.taskList, this.storage, position);
    }

    private Command prepareDelete(String[] commandAndInput) throws InvalidIndexInputException, EmptyArgumentException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException("Please pass an index after the 'delete' command!");
        }

        assert commandAndInput.length == 2;

        int position = calcListPos(commandAndInput);

        if (this.taskList.isEmpty()) {
            throw new InvalidIndexInputException("There are no tasks to delete!");
        } else if (position >= this.taskList.getList().size() || position < 0) {
            throw new InvalidIndexInputException("Please input an index from 1 to "
                    + this.taskList.getList().size() + "!");
        }

        return new DeleteCommand(this.taskList, this.storage, position);
    }

    private Command prepareList(String[] commandAndInput) throws InvalidCommandException {
        if (commandAndInput.length > 1) {
            throw new InvalidCommandException();
        }

        return new ListCommand(this.taskList, this.storage);
    }

    private Command prepareExit(String[] commandAndInput) throws InvalidCommandException {
        if (commandAndInput.length > 1) {
            throw new InvalidCommandException();
        }

        return new ByeCommand(this.taskList, this.storage);
    }

    private Command prepareHelp(String[] commandAndInput) {
        assert commandAndInput.length >= 1 && commandAndInput.length <= 2;

        if (commandAndInput.length == 1) {
            return new HelpCommand(this.taskList, this.storage, null);
        }

        return new HelpCommand(this.taskList, this.storage, commandAndInput[1]);
    }

    private void trimInputsInArray(String[] taskInputAndDate) {
        taskInputAndDate[0] = taskInputAndDate[0].trim();
        taskInputAndDate[1] = taskInputAndDate[1].trim();
    }

    private int calcListPos(String[] commandAndInput) throws InvalidIndexInputException {
        String taskIndex = commandAndInput[1];
        String command = commandAndInput[0];

        Matcher matcher = REGEX_CHECK_NUMBER.matcher(taskIndex);
        if (!matcher.find()) {
            throw new InvalidIndexInputException("'" + command + "' is command word; please pass a numerical index or "
                    + "start your task with another word!");
        }

        return Integer.parseInt(taskIndex) - 1;
    }
}
