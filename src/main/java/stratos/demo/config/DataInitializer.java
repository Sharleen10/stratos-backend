package stratos.demo.config;
import stratos.demo.model.Role;
import stratos.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // Create default roles if they don't exist
        if (roleRepository.findByName(Role.RoleType.ROLE_MANAGER).isEmpty()) {
            roleRepository.save(new Role(null, Role.RoleType.ROLE_MANAGER));
        }
        if (roleRepository.findByName(Role.RoleType.ROLE_TEAM_MEMBER).isEmpty()) {
            roleRepository.save(new Role(null, Role.RoleType.ROLE_TEAM_MEMBER));
        }
    }
}
