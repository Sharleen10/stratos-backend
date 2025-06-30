package stratos.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stratos.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedUserId(Long userId);

    @Query("SELECT t FROM Task t WHERE " +
            "(:projectId IS NULL OR t.project.id = :projectId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:assignedUserId IS NULL OR t.assignedUser.id = :assignedUserId) AND " +
            "(:overdue IS NULL OR (:overdue = true AND t.dueDate < CURRENT_DATE))")
    List<Task> findWithFilters(
            @Param("projectId") Long projectId,
            @Param("status") Task.TaskStatus status,
            @Param("priority") Task.TaskPriority priority,
            @Param("assignedUserId") Long assignedUserId,
            @Param("overdue") Boolean overdue);

    @Query("SELECT t FROM Task t WHERE " +
            "t.dueDate BETWEEN :from AND :to AND " +
            "t.assignedUser.id = :userId")
    List<Task> findUserTasksInDateRange(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Task t WHERE t.id = :taskId AND t.assignedUser.id = :userId")
    boolean existsByIdAndAssignedUserId(@Param("taskId") Long taskId,
                                        @Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Task t JOIN t.project p JOIN p.teamMembers tm " +
            "WHERE t.id = :taskId AND tm.id = :userId")
    boolean existsByIdAndProjectTeamMembersId(@Param("taskId") Long taskId,
                                              @Param("userId") Long userId);
}