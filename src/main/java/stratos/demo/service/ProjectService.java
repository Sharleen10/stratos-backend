package stratos.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import stratos.demo.dto.ProjectDTO;
import stratos.demo.model.Project;
import stratos.demo.model.User;
import stratos.demo.repository.ProjectRepository;
import stratos.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStatus(projectDTO.getStatus());
        project.setDueDate(projectDTO.getDueDate());

        if (projectDTO.getTeamMemberIds() != null) {
            Set<User> teamMembers = projectDTO.getTeamMemberIds().stream()
                    .map(userId -> userRepository.findById(userId).orElseThrow(
                            () -> new RuntimeException("User not found with id: " + userId)
                    ))
                    .collect(Collectors.toSet());
            project.setTeamMembers(teamMembers);
        }

        return projectRepository.save(project);
    }

    public List<Project> getProjectsForUser(Long userId) {
        return projectRepository.findByTeamMembersId(userId);
    }

    public Page<Project> searchProjects(ProjectFilterDTO filter, Pageable pageable) {
        Specification<Project> spec = Specification.where(null);

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getTeamMemberId() != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Project, User> users = root.join("teamMembers");
                return cb.equal(users.get("id"), filter.getTeamMemberId());
            });
        }

        if (filter.getSearchTerm() != null && !filter.getSearchTerm().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")),
                            "%" + filter.getSearchTerm().toLowerCase() + "%"));
        }

        return projectRepository.findAll(spec, pageable);
    }

    public Project updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (projectDTO.getName() != null) {
            project.setName(projectDTO.getName());
        }
        if (projectDTO.getDescription() != null) {
            project.setDescription(projectDTO.getDescription());
        }
        if (projectDTO.getStatus() != null) {
            project.setStatus(projectDTO.getStatus());
        }
        if (projectDTO.getDueDate() != null) {
            project.setDueDate(projectDTO.getDueDate());
        }

        project.setLastUpdated(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public void addTeamMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getTeamMembers().add(user);
        projectRepository.save(project);
    }
}