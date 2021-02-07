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
            InvalidIndexInputException, EmptyListException {
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
            return prepareList();

        case ByeCommand.COMMAND_WORD:
            return prepareExit();

        default:
            return prepareHelp();
        }
    }

    private Command prepareToDo(String[] commandAndInput) {
        assert commandAndInput.length == 2;

        return new ToDoCommand(this.taskList, this.storage, commandAndInput[1]);
    }

<<<<<<< HEAD
    private Command prepareDeadline(String[] arguments) throws EmptyArgumentException, InvalidDateTimeException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please input a valid task description!");
        }

        String description = arguments[1];
        String[] taskInputAndDate = description.split("/", 2);

        taskInputAndDate[0] = taskInputAndDate[0].trim();
        taskInputAndDate[1] = taskInputAndDate[1].trim();

        try {
            LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
            return new DeadlineCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException();
        }
    }

    private Command prepareEvent(String[] arguments) throws EmptyArgumentException, InvalidDateTimeException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please input a valid task description!");
        }

        String description = arguments[1];
        String[] taskInputAndDate = description.split("/", 2);

        taskInputAndDate[0] = taskInputAndDate[0].trim();
        taskInputAndDate[1] = taskInputAndDate[1].trim();

        try {
            LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
            return new EventCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException();
=======
    private Command prepareDeadline(String[] commandAndInput) throws EmptyArgumentException, InvalidDateTimeException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException(INVALID_TASK_MSG);
        } else {
            assert commandAndInput.length == 2;

            String description = commandAndInput[1];
            String[] taskInputAndDate = description.split("/", 2);

            assert taskInputAndDate.length == 2;

            taskInputAndDate[0] = taskInputAndDate[0].trim();
            taskInputAndDate[1] = taskInputAndDate[1].trim();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
                return new DeadlineCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
            } catch (DateTimeParseException e) {
                throw new InvalidDateTimeException();
            }
        }
    }

    private Command prepareEvent(String[] commandAndInput) throws EmptyArgumentException, InvalidDateTimeException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException(INVALID_TASK_MSG);
        } else {
            assert commandAndInput.length == 2;

            String description = commandAndInput[1];
            String[] taskInputAndDate = description.split("/", 2);

            assert taskInputAndDate.length == 2;

            taskInputAndDate[0] = taskInputAndDate[0].trim();
            taskInputAndDate[1] = taskInputAndDate[1].trim();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), FORMATTER);
                return new EventCommand(this.taskList, this.storage, taskInputAndDate[0], dateTime);
            } catch (DateTimeParseException e) {
                throw new InvalidDateTimeException();
            }
>>>>>>> master
        }
    }

    private Command prepareFind(String[] commandAndInput) throws EmptyArgumentException, EmptyListException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException("Please pass a word after the 'find' command!");
<<<<<<< HEAD
=======
        } else {
            if (this.taskList.getList().size() == 0) {
                throw new EmptyListException();
            } else {
                assert commandAndInput.length == 2;

                return new FindCommand(this.taskList, this.storage, commandAndInput[1]);
            }
>>>>>>> master
        }

        if (this.taskList.getList().size() == 0) {
            throw new EmptyListException();
        }

        return new FindCommand(this.taskList, this.storage, arguments[1]);
    }

    private int calcListPos(String taskIndex, String command) throws InvalidIndexInputException {
<<<<<<< HEAD
        Matcher matcher = checkNum.matcher(taskIndex);
        if (!matcher.find()) {
=======
        Matcher matcher = REGEX_CHECK_NUMBER.matcher(taskIndex);
        if (matcher.find()) {
            return Integer.parseInt(taskIndex) - 1;
        } else {
>>>>>>> master
            throw new InvalidIndexInputException("'" + command + "' is command word; please pass a numerical index or "
                    + "start your task with another word!");
        }
        return Integer.parseInt(taskIndex) - 1;
    }

    private Command prepareDone(String[] commandAndInput) throws InvalidIndexInputException, EmptyArgumentException {
        if (commandAndInput.length == 1) {
            throw new EmptyArgumentException("Please pass an index after the 'done' command!");
<<<<<<< HEAD
=======
        } else {
            assert commandAndInput.length == 2;

            int position = calcListPos(commandAndInput[1], commandAndInput[0]);

            if (this.taskList.getList().size() == 0) {
                throw new InvalidIndexInputException("You have already done all tasks!");
            } else if (position >= this.taskList.getList().size() || position < 0) {
                throw new InvalidIndexInputException("Please input an index from 1 to "
                        + this.taskList.getList().size() + "!");
            } else {
                return new DoneCommand(this.taskList, this.storage, position);
            }
>>>>>>> master
        }

        int position = calcListPos(arguments[1], arguments[0]);

        if (this.taskList.getList().size() == 0) {
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
<<<<<<< HEAD
=======
        } else {
            assert commandAndInput.length == 2;

            int position = calcListPos(commandAndInput[1], commandAndInput[0]);

            if (this.taskList.getList().size() == 0) {
                throw new InvalidIndexInputException("There are no tasks to delete!");
            } else if (position >= this.taskList.getList().size() || position < 0) {
                throw new InvalidIndexInputException("Please input an index from 1 to "
                        + this.taskList.getList().size() + "!");
            } else {
                return new DeleteCommand(this.taskList, this.storage, position);
            }
>>>>>>> master
        }

        int position = calcListPos(arguments[1], arguments[0]);

        if (this.taskList.getList().size() == 0) {
            throw new InvalidIndexInputException("There are no tasks to delete!");
        } else if (position >= this.taskList.getList().size() || position < 0) {
            throw new InvalidIndexInputException("Please input an index from 1 to "
                    + this.taskList.getList().size() + "!");
        }

        return new DeleteCommand(this.taskList, this.storage, position);
    }

    private Command prepareList() {
        return new ListCommand(this.taskList, this.storage);
    }

    private Command prepareExit() {
        return new ByeCommand(this.taskList, this.storage);
    }

    private Command prepareHelp() {
        return new HelpCommand(this.taskList, this.storage);
    }
}
