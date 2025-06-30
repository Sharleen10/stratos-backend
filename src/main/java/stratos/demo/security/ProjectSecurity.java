package stratos.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import stratos.demo.model.User;
import stratos.demo.repository.ProjectRepository;

@Component
@RequiredArgsConstructor
public class ProjectSecurity {
    private final ProjectRepository projectRepository;

    public boolean isProjectCreator(Long projectId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return projectRepository.existsByIdAndCreatedByUserId(projectId, user.getId());
    }

    public boolean isTeamMember(Long projectId, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return projectRepository.existsByIdAndTeamMembersId(projectId, user.getId());
    }
}