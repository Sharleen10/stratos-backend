package stratos.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stratos.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTeamMembersId(Long userId);

    @Query("SELECT p FROM Project p WHERE " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Project> findWithFilters(
            @Param("status") Project.ProjectStatus status,
            @Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.teamMembers tm WHERE " +
            "(:teamMemberId IS NULL OR tm.id = :teamMemberId) AND " +
            "(p.dueDate BETWEEN :startDate AND :endDate)")
    List<Project> findProjectsByTeamMemberAndDateRange(
            @Param("teamMemberId") Long teamMemberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}