package duke.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class TaskList {
    private List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(List<Task> converted) {
        this.taskList = new ArrayList<>(converted);
    }

    /**
     * Marks task at specified position to be done.
     *
     * @param pos position of task to be marked.
     */
    public void setTaskDone(int pos) {
        taskList.get(pos).markAsDone();
    }

    /**
     * Prints the list of tasks.
     * If the list is empty, a statement indicating so will be printed.
     */
    public String getListInString () {
        if (this.taskList.size() == 0) {
            return getEmptyListInString();
        } else {
            return getNonEmptyListInString();
        }
    }

    private String getEmptyListInString() {
        String completedAllTasksMsg = "You have completed all tasks!";
        return completedAllTasksMsg;
    }

    private String getNonEmptyListInString() {
        String allTasks = Stream.iterate(1, index -> index <= this.taskList.size(), index -> index + 1)
                .map(new Function<Integer, StringBuilder>() {
                    @Override
                    public StringBuilder apply(Integer index) {
                        Task task = taskList.get(index - 1);
                        StringBuilder stringBuilder = new StringBuilder("\n");
                        stringBuilder.append(index)
                                .append(". ")
                                .append(task.toString());
                        return stringBuilder;
                    }
                })
                .reduce(new BinaryOperator<StringBuilder>() {
                    @Override
                    public StringBuilder apply(StringBuilder stringBuilder1, StringBuilder stringBuilder2) {
                        stringBuilder1.append(stringBuilder2);
                        return stringBuilder1;
                    }
                })
                .get()
                .toString();

        return allTasks;
    }

    public List<Task> getList() {
        return this.taskList;
    }

    /**
     * Adds a task to the existing list of tasks.
     *
     * @param t task to be added to the list.
     */
    public void addTask(Task t) {
        this.taskList.add(t);
    }

    /**
     * Removes the task at the specified position from the list.
     *
     * @param pos position of the task to be removed.
     */
    public void deleteTask(int pos) {
        this.taskList.remove(pos);
    }
}
