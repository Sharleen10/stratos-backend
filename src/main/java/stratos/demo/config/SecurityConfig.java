package stratos.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Authentication endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // User management
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("MANAGER")

                        // Project endpoints
                        .requestMatchers(HttpMethod.POST, "/api/projects").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/projects/**/team-members/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole("MANAGER", "TEAM_MEMBER")

                        // Task endpoints
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("MANAGER", "TEAM_MEMBER")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("MANAGER", "TEAM_MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/tasks/**/log-time").hasAnyRole("MANAGER", "TEAM_MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole("MANAGER", "TEAM_MEMBER")

                        // Dashboard endpoints
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/manager").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/team-member").hasAnyRole("MANAGER", "TEAM_MEMBER")

                        // AI Analysis endpoints
                        .requestMatchers(HttpMethod.POST, "/api/ai/analyze").hasAnyRole("MANAGER", "TEAM_MEMBER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",          // React development
                "https://your-production-domain.com"  // Production frontend
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}