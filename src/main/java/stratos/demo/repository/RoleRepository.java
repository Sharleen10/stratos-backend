package stratos.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stratos.demo.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleType name);
}
