package stratos.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import stratos.demo.dto.ProjectDTO;
import stratos.demo.model.Project;
import stratos.demo.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getUserProjects(@PathVariable Long userId) {
        return ResponseEntity.ok(projectService.getProjectsForUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Project>> searchProjects(
            @RequestBody ProjectFilterDTO filter,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(projectService.searchProjects(filter, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }

    @PostMapping("/{projectId}/team-members/{userId}")
    public ResponseEntity<Void> addTeamMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.addTeamMember(projectId, userId);
        return ResponseEntity.ok().build();
    }
}