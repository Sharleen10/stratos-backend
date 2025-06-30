package stratos.demo.dto;

import lombok.Data;
import stratos.demo.model.Project.ProjectStatus;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate dueDate;
    private Set<Long> teamMemberIds;
}