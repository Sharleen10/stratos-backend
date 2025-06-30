package stratos.demo.dto;

import lombok.Data;
import stratos.demo.model.Task.TaskStatus;
import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long assignedUserId;
}