package duke.utils;

import duke.tasks.*;
import duke.dukeexceptions.InvalidTaskTypeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileTaskStringConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d/M/yyyy HHmm][d MMM yy HHmm][dd-MM-yy HHmm]");

    public static List<String> allTaskToAllString(List<Task> list) {
        List<String> result = new ArrayList<>();
        for (Task t : list) {
            result.add(taskToString(t));
        }
        return result;
    }

    public static List<Task> allStringToAllTask(List<String> list) throws InvalidTaskTypeException {
        List<Task> result = new ArrayList<>();
        for (String s : list) {
            result.add(stringToTask(s));
        }
        return result;
    }

    private static String taskToString(Task task) {
        String done = task.isDone() ? "1" : "0";
        if (task instanceof ToDo) {
            return "T | " + done + " | " + task.getDescription();
        } else if (task instanceof Event) {
            return "E | " + done + " | " + task.getDescription() + " | " + ((Event) task).getDateToStore();
        } else {
            return "D | " + done + " | " + task.getDescription() + " | " + ((Deadline) task).getDateToStore();
        }
    }

    private static Task stringToTask(String input) throws InvalidTaskTypeException {
        String[] separated = input.split(" \\| ");
        char taskType = separated[0].charAt(0);
        if (taskType == 'T') {
            ToDo t = new ToDo(separated[2]);
            if (separated[1].equals("1")) {
                t.markAsDone();
            }
            return t;
        } else if (taskType == 'D') {
            Deadline d = new Deadline(separated[2], LocalDateTime.parse(separated[3], formatter));
            if (separated[1].equals("1")) {
                d.markAsDone();
            }
            return d;
        } else if (taskType == 'E') {
            Event e = new Event(separated[2], LocalDateTime.parse(separated[3], formatter));
            if (separated[1].equals("1")) {
                e.markAsDone();
            }
            return e;
        } else {
            throw new InvalidTaskTypeException();
        }
    }
}
