package duke.dukeexceptions;

public class InvalidCommandException extends DukeException {
    public InvalidCommandException() {
        super("That is not a valid command format! Send 'help' if you need assistance!");
    }
}
