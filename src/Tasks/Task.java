package Tasks;

import Tasks.enums.Status;
import Tasks.enums.TypeOfTask;

import java.util.Objects;

public class Task {
    protected int id;
    protected TypeOfTask type;
    protected String name;
    protected Status status;
    protected String description;

    public Task(String name, TypeOfTask type, Status status, String description) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TypeOfTask getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && status == task.status && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description);
    }

}