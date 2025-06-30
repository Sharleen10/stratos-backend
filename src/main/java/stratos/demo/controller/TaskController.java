package stratos.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import stratos.demo.dto.TaskDTO;
import stratos.demo.model.Task;
import stratos.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Task>> searchTasks(
            @RequestBody TaskFilterDTO filter,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(taskService.searchTasks(filter, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @PostMapping("/{taskId}/log-time")
    public ResponseEntity<Void> logTime(
            @PathVariable Long taskId,
            @RequestParam Double hours) {
        taskService.logTime(taskId, hours);
        return ResponseEntity.ok().build();
    }
}