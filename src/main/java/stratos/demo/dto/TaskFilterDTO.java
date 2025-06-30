package stratos.demo.dto;

import lombok.Data;
import stratos.demo.model.Task.TaskPriority;
import stratos.demo.model.Task.TaskStatus;
import java.time.LocalDate;

@Data
public class TaskFilterDTO {
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private Long assignedUserId;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;
    private Boolean overdue;
}