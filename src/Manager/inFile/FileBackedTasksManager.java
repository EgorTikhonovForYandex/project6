package Manager.inFile;

import Manager.inMemory.InMemoryTaskManager;
import Manager.interfaces.HistoryManager;
import Manager.interfaces.TaskManager;
import Tasks.Task;
import Tasks.enums.Status;
import Tasks.enums.TypeOfTask;
import Tasks.sub.Epic;
import Tasks.sub.SubTask;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import Exception.ManagerSaveException;

import java.io.Writer;
import java.io.File;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) throws ArrayIndexOutOfBoundsException{

        File fileForExample = new File("src/Manager/inFile/savedNotes.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForExample);
        fileBackedTasksManager.createTask(new Task("Таск 1", TypeOfTask.TASK, Status.DONE, "Доделать тз"));

        fileBackedTasksManager.createTask(new Task("Таск 2", TypeOfTask.TASK, Status.DONE, "попытаться сдать тз"));


        fileBackedTasksManager.getTaskByID(1);
        fileBackedTasksManager.getTaskByID(2);
        fileBackedTasksManager.createTask(new Task("Таск 3", TypeOfTask.TASK, Status.NEW, " Посмотреть футбол"));
        fileBackedTasksManager.createTask(new Task("Таск 4", TypeOfTask.TASK, Status.NEW, "Посмотреть сериал"));

        fileBackedTasksManager.getTaskByID(3);
        fileBackedTasksManager.getTaskByID(4);


        fileBackedTasksManager.createEpic(new Epic("Эпик 1", TypeOfTask.EPIC, Status.NEW, "Почитать книгу"));
        fileBackedTasksManager.createEpic(new Epic("Эпик 2", TypeOfTask.EPIC, Status.NEW, "поесть"));
        File fileForExmaple2 = new File("src/Manager/inFile/savedNotes2.csv");
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileForExmaple2);
        System.out.println(fileBackedTasksManager2);
    }

    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static List<Integer> historyFromString(String value) { // для сохранения и восстановления менеджера истории из CSV.
        List<Integer> history = new ArrayList<>();
        if (value.isEmpty()) {
            return history;
        }
        String[] line = value.split(",");
        for (String str : line) {
            history.add(Integer.parseInt(str));
        }
        return history;
    }

    public Task fromString(String value) { // метод создания задачи из строки
        String name;
        String description;
        Status status;
        TypeOfTask type;
        int id;

        String[] line = value.split(",");
        name = line[2];
        description = line[4];
        id = Integer.parseInt(line[0]);
        status = Status.valueOf(line[3]);
        type = TypeOfTask.valueOf(line[1]);
        switch (type) {
            case EPIC:
                Epic epic = new Epic(name, type, status, description);
                epic.setId(id);
                return epic;
            case SUB_TASK:
                SubTask subTask = new SubTask(name, type, status, description, Integer.parseInt(line[5]));
                subTask.setId(id);
                return subTask;
            case TASK:
                Task task = new Task(name, type, status, description);
                task.setId(id);
                return task;
        }
        return null;
    }

    public String toStringFormat(Task task)   {
        return String.format(
                "%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                null);
    }

    public void save() throws ManagerSaveException{ // сохранять текущее состояние менеджера в указанный файл
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            Collection<Task> tasks = super.getTasks();
            Collection<Epic> epics = super.getEpics();
            Collection<SubTask> subTasks = super.getAllSubtasks();

            for (Task task : tasks) {
                writer.write(toStringFormat(task));
            }
            for (Epic epic : epics) {
                writer.write(toStringFormat(epic));
            }
            for (SubTask sub : subTasks) {
                writer.write(toStringFormat(sub));
            }
            writer.write("\n");
            writer.write(toString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static String toString(HistoryManager manager) throws ManagerSaveException{ // для сохранения и восстановления менеджера истории из CSV.
        StringBuilder line = new StringBuilder();
        if (manager.getHistory().size() != 0) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
        }
        return line.toString();
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException, ArrayIndexOutOfBoundsException { // восстанавливать данные менеджера из файла при запуске программы
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String s = Files.readString(Path.of(String.valueOf(file)));
            if (s.isEmpty())
                return fileBackedTasksManager;
            String[] lines = s.split("\n");
            int i = 1;
            while (!lines[i].isEmpty()) {
                Task task = fileBackedTasksManager.fromString(lines[i]);
                ++i;
                fileBackedTasksManager.createTask(task);

                if (lines.length == i)
                    break;
            }
            if (i == lines.length) {
                return fileBackedTasksManager;
            } else {
                List<Integer> history = historyFromString(lines[lines.length - 1]);
                for (Integer idHistory : history) {
                    if (fileBackedTasksManager.getEpicByID(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getEpicByID(idHistory));

                    }
                    if (fileBackedTasksManager.getSubtaskByID(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getSubtaskByID(idHistory));

                    }
                    if (fileBackedTasksManager.getTaskByID(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getTaskByID(idHistory));
                    }
                    return fileBackedTasksManager;
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task)  {
        super.createTask(task);
        save();
    }

    @Override
    public Task getTaskByID(int taskId) {
        Task task = super.getTaskByID(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int epicId) {
        Epic task = super.getEpicByID(epicId);
        save();
        return task;
    }

    @Override
    public SubTask getSubtaskByID(int subtaskId) {
        SubTask task = super.getSubtaskByID(subtaskId);
        save();
        return task;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }



}
