package duke.utils;

import duke.commands.ByeCommand;
import duke.commands.Command;
import duke.commands.DeadlineCommand;
import duke.commands.DeleteCommand;
import duke.commands.DoneCommand;
import duke.commands.EventCommand;
import duke.commands.HelpCommand;
import duke.commands.ListCommand;
import duke.commands.ToDoCommand;
import duke.dukeexceptions.EmptyArgumentException;
import duke.dukeexceptions.InvalidDateTimeException;
import duke.dukeexceptions.InvalidIndexInputException;
import duke.tasks.TaskList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern checkNum = Pattern.compile("^[0-9]$");
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d/M/yyyy HHmm][d MMM yy HHmm]"
            + "[dd-MM-yy HHmm]");

    private final TaskList taskList;
    private final Ui ui;
    private final Storage storage;

    public Parser(TaskList taskList, Ui ui, Storage storage) {
        this.taskList = taskList;
        this.ui = ui;
        this.storage = storage;
    }

    public Command parse(String input) throws NumberFormatException, EmptyArgumentException, InvalidDateTimeException,
            InvalidIndexInputException {
        String[] commandAndInput = input.split(" ", 2);
        String command = commandAndInput[0];

        switch (command) {
            case ToDoCommand.COMMAND_WORD:
                return prepareToDo(commandAndInput);

            case DeadlineCommand.COMMAND_WORD:
                return prepareDeadline(commandAndInput);

            case EventCommand.COMMAND_WORD:
                return prepareEvent(commandAndInput);

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

    private Command prepareToDo(String[] arguments) {
        return new ToDoCommand(this.taskList, this.ui, this.storage, arguments[0]);
    }

    private Command prepareDeadline(String[] arguments) throws EmptyArgumentException, InvalidDateTimeException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please input a valid task description!");
        } else {
            String description = arguments[1];
            String[] taskInputAndDate = description.split("/", 2);

            taskInputAndDate[0] = taskInputAndDate[0].trim();
            taskInputAndDate[1] = taskInputAndDate[1].trim();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), formatter);
                return new DeadlineCommand(this.taskList, this.ui, this.storage, taskInputAndDate[0], dateTime);
            } catch (DateTimeParseException e) {
                throw new InvalidDateTimeException();
            }
        }
    }

    private Command prepareEvent(String[] arguments) throws EmptyArgumentException, InvalidDateTimeException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please input a valid task description!");
        } else {
            String description = arguments[1];
            String[] taskInputAndDate = description.split("/", 2);

            taskInputAndDate[0] = taskInputAndDate[0].trim();
            taskInputAndDate[1] = taskInputAndDate[1].trim();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(taskInputAndDate[1].substring(3), formatter);
                return new EventCommand(this.taskList, this.ui, this.storage, taskInputAndDate[0], dateTime);
            } catch (DateTimeParseException e) {
                throw new InvalidDateTimeException();
            }
        }
    }

    private int calcListPos(String taskIndex, String command) throws InvalidIndexInputException {
        Matcher m = checkNum.matcher(taskIndex);
        if (m.find()) {
            return Integer.parseInt(taskIndex) - 1;
        } else {
            throw new InvalidIndexInputException("'" + command + "' is command word; please pass a numerical index or "
                    + "start your task with another word!");
        }
    }

    private Command prepareDone(String[] arguments) throws InvalidIndexInputException, EmptyArgumentException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please pass an index after the 'done' command!");
        } else {
            int position = calcListPos(arguments[1], arguments[0]);

            if (this.taskList.getList().size() == 0){
                throw new InvalidIndexInputException("You have already done all tasks!");
            } else if (position >= this.taskList.getList().size() || position < 0) {
                throw new InvalidIndexInputException("Please input an index from 1 to "
                        + this.taskList.getList().size() + "!");
            } else {
                return new DoneCommand(this.taskList, this.ui, this.storage, position);
            }
        }
    }

    private Command prepareDelete(String[] arguments) throws InvalidIndexInputException, EmptyArgumentException {
        if (arguments.length == 1) {
            throw new EmptyArgumentException("Please pass an index after the 'delete' command!");
        } else {
            int position = calcListPos(arguments[1], arguments[0]);

            if (this.taskList.getList().size() == 0){
                throw new InvalidIndexInputException("There are no tasks to delete!");
            } else if (position >= this.taskList.getList().size() || position < 0) {
                throw new InvalidIndexInputException("Please input an index from 1 to "
                        + this.taskList.getList().size() + "!");
            } else {
                return new DeleteCommand(this.taskList, this.ui, this.storage, position);
            }
        }
    }

    private Command prepareList() {
        return new ListCommand(this.taskList, this.ui, this.storage);
    }

    private Command prepareExit() {
        return new ByeCommand(this.taskList, this.ui, this.storage);
    }

    private Command prepareHelp() {
        return new HelpCommand(this.taskList, this.ui, this.storage);
    }
}
