package duke;

import duke.commands.Command;
import duke.dukeexceptions.DukeException;
import duke.dukeexceptions.InvalidTaskTypeException;
import duke.tasks.TaskList;
import duke.utils.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static final String FILE_PATH = "./src/main/java/duke/tasks.txt";
    private static Ui ui;
    private static Storage storage;
    private static TaskList taskList;

    private static void run() {
        ui = new Ui();
        ui.introduction();
        storage = new Storage(FILE_PATH, ui);
        taskList = storage.loadFromFile();

        Scanner scannerInput = new Scanner(System.in);
        ui.showMsg("What can I do for you?");
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = scannerInput.nextLine();
                Parser p = new Parser(taskList, ui, storage);
                Command c = p.parse(input);
                c.execute();
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
        scannerInput.close();
    }

    public static void main(String[] args) {
        run();
    }
}