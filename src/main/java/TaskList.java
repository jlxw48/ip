import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> taskList;

    public TaskList(List<Task> converted) {
        this.taskList = new ArrayList<>(converted);
    }

    public void setTaskDone(int pos) {
        taskList.get(pos).markAsDone();

    }

    public void printList() {
        System.out.println("Here are the tasks in your list:");
        int counter = 1;
        for (Task t : this.taskList) {
            System.out.println(counter + ". " + t);
            counter++;
        }
    }

    public void printTask(int pos) {
        System.out.println(this.taskList.get(pos));
    }

    public List<Task> getList() {
        return this.taskList;
    }

    public void addTask(Task t) {
        this.taskList.add(t);
    }

    public void deleteTask(int pos) {
        this.taskList.remove(pos);
    }


}
