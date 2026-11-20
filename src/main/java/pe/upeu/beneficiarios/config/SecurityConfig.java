package pe.upeu.beneficiarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import pe.upeu.beneficiarios.security.JwtAuthenticationFilter;
import pe.upeu.beneficiarios.security.JwtService;
import pe.upeu.beneficiarios.security.RestAccessDeniedHandler;
import pe.upeu.beneficiarios.security.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] RUTAS_PUBLICAS = {
            "/api/v1/auth/login",
            "/api/health",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            CorsConfigurationSource corsConfigurationSource,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RUTAS_PUBLICAS).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/facultades/**").hasAuthority("FACULTAD_LEER")
                        .requestMatchers("/api/v1/facultades/**").hasAuthority("FACULTAD_GESTIONAR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/proyectos/**").hasAuthority("PROYECTO_LEER")
                        .requestMatchers("/api/v1/proyectos/**").hasAuthority("PROYECTO_GESTIONAR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/talleres/**").hasAuthority("TALLER_LEER")
                        .requestMatchers("/api/v1/talleres/**").hasAuthority("TALLER_GESTIONAR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/beneficiarios/**").hasAuthority("BENEFICIARIO_LEER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/beneficiarios").hasAuthority("BENEFICIARIO_CREAR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/beneficiarios/**").hasAuthority("BENEFICIARIO_EDITAR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/beneficiarios/**").hasAuthority("BENEFICIARIO_ELIMINAR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/asistencias/**").hasAuthority("ASISTENCIA_LEER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/asistencias/**").hasAuthority("ASISTENCIA_REGISTRAR")

                        .requestMatchers("/api/v1/certificados/**").hasAuthority("CERTIFICADO_EMITIR")

                        .requestMatchers("/api/v1/auditorias/**").hasAuthority("AUDITORIA_LEER")

                        .requestMatchers("/api/v1/usuarios/me").authenticated()
                        .requestMatchers("/api/v1/usuarios/**").hasAuthority("USUARIO_GESTIONAR")

                        .anyRequest().authenticated())
                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
