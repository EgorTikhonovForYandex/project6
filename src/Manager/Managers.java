package Manager;


import Manager.inFile.FileBackedTasksManager;
import Manager.interfaces.HistoryManager;
import Manager.inMemory.InMemoryHistoryManager;
import Manager.interfaces.TaskManager;

import java.io.File;

public final class Managers {
    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    static {
        historyManager = new InMemoryHistoryManager();
        taskManager = new FileBackedTasksManager(new File("src/Manager/InFile/saveNotes.csv"));
    }

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

}
