package Manager.interfaces;
import java.util.List;
import Tasks.Task;

public interface HistoryManager {

    void add(Task task);

    List<Task>getHistory();

    void remove(int id);
}
