package stratos.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import stratos.demo.dto.TaskDTO;
import stratos.demo.model.*;
import stratos.demo.repository.ProjectRepository;
import stratos.demo.repository.TaskRepository;
import stratos.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignedUser = null;
        if (taskDTO.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(taskDTO.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Task task = new Task();
        task.setProject(project);
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        task.setAssignedUser(assignedUser);

        return taskRepository.save(task);
    }

    public Page<Task> searchTasks(TaskFilterDTO filter, Pageable pageable) {
        Specification<Task> spec = Specification.where(null);

        if (filter.getProjectId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("project").get("id"), filter.getProjectId()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getPriority() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("priority"), filter.getPriority()));
        }

        if (filter.getAssignedUserId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("assignedUser").get("id"), filter.getAssignedUserId()));
        }

        if (filter.getOverdue() != null && filter.getOverdue()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("dueDate"), LocalDate.now()));
        }

        return taskRepository.findAll(spec, pageable);
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (taskDTO.getName() != null) {
            task.setName(taskDTO.getName());
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }
        if (taskDTO.getPriority() != null) {
            task.setPriority(taskDTO.getPriority());
        }
        if (taskDTO.getDueDate() != null) {
            task.setDueDate(taskDTO.getDueDate());
        }
        if (taskDTO.getEstimatedHours() != null) {
            task.setEstimatedHours(taskDTO.getEstimatedHours());
        }
        if (taskDTO.getActualHours() != null) {
            task.setActualHours(taskDTO.getActualHours());
        }
        if (taskDTO.getAssignedUserId() != null) {
            User user = userRepository.findById(taskDTO.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedUser(user);
        }

        return taskRepository.save(task);
    }

    public void logTime(Long taskId, Double hours) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getActualHours() == null) {
            task.setActualHours(0.0);
        }
        task.setActualHours(task.getActualHours() + hours);
        taskRepository.save(task);
    }

}