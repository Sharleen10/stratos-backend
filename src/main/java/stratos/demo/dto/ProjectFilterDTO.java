package stratos.demo.dto;

import lombok.Data;
import stratos.demo.model.Project.ProjectStatus;
import java.time.LocalDate;

@Data
public class ProjectFilterDTO {
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long teamMemberId;
    private String searchTerm;
}