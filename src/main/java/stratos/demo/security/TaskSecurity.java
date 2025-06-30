package stratos.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import stratos.demo.model.User;
import stratos.demo.repository.TaskRepository;

@Component
@RequiredArgsConstructor
public class TaskSecurity {
    private final TaskRepository taskRepository;

    public boolean isTaskAssignee(Long taskId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return taskRepository.existsByIdAndAssignedUserId(taskId, user.getId());
    }

    public boolean isProjectMember(Long taskId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return taskRepository.existsByIdAndProjectTeamMembersId(taskId, user.getId());
    }
}