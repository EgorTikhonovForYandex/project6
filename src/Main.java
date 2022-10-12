import Manager.Managers;
import Manager.interfaces.TaskManager;
import Tasks.Task;
import Tasks.enums.Status;
import Tasks.enums.TypeOfTask;
import Tasks.sub.Epic;

public class Main {
    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();


        inMemoryTaskManager.createTask(new Task("Таск 1", TypeOfTask.TASK, Status.DONE, "Доделать тз"));
        inMemoryTaskManager.getTaskByID(1);
        inMemoryTaskManager.createTask(new Task("Таск 2", TypeOfTask.TASK, Status.DONE, "Попытаться сдать тз"));
        inMemoryTaskManager.getTaskByID(2);

        inMemoryTaskManager.getTaskByID(1);
        inMemoryTaskManager.createTask(new Task("Таск 3", TypeOfTask.TASK, Status.NEW, "Посмотреть футбол"));
        inMemoryTaskManager.createTask(new Task("Таск 4", TypeOfTask.TASK, Status.NEW, "посмотреть сериал"));

        inMemoryTaskManager.getTaskByID(4);
        inMemoryTaskManager.getTaskByID(3);
 

        inMemoryTaskManager.createEpic(new Epic("Эпик 1", TypeOfTask.EPIC, Status.NEW, "выбрать яп"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 2", TypeOfTask.EPIC, Status.NEW, " Прочитать книгу"));

 
    }
}

