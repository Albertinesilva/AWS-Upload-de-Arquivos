package com.albertsilva.dscatalog.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuração de segurança do Resource Server.
 *
 * <p>
 * Define regras de autorização, CORS, CSRF, headers e autenticação via OAuth2
 * JWT.
 * </p>
 * 
 * <p>
 * Inclui:
 * <ul>
 * <li>Regras públicas e restritas por roles</li>
 * <li>Configuração de CORS para permitir chamadas de qualquer origem</li>
 * <li>Form login habilitado para testes</li>
 * <li>Criptografia de senhas usando BCrypt</li>
 * </ul>
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC = { "/h2-console/**" };
    private static final String[] OPERATOR_OR_ADMIN = { "/products/**", "/categories/**" };
    private static final String[] ADMIN = { "/users/**" };

    /**
     * Configura o {@link SecurityFilterChain} do Resource Server.
     *
     * <p>
     * Define regras de autorização baseadas em roles, habilita OAuth2 JWT,
     * form login, desabilita CSRF e configura headers para H2 console.
     * </p>
     *
     * @param http instância de {@link HttpSecurity} fornecida pelo Spring
     * @return {@link SecurityFilterChain} configurado
     * @throws Exception caso ocorra erro na configuração de segurança
     * @author Albert
     * @since 2026-03-05
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC).permitAll()
                        .requestMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
                        .requestMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers(ADMIN).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .formLogin(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    /**
     * Configuração de CORS (Cross-Origin Resource Sharing).
     *
     * <p>
     * Permite chamadas de qualquer origem, com métodos POST, GET, PUT, DELETE e
     * PATCH,
     * e headers Authorization e Content-Type.
     * </p>
     *
     * @return {@link CorsConfigurationSource} configurado
     * @author Albert
     * @since 2026-03-05
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        corsConfig.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "PATCH"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    /**
     * Configura o {@link PasswordEncoder} utilizado pelo Spring Security.
     *
     * <p>
     * Utiliza {@link BCryptPasswordEncoder} para criptografar senhas de usuários
     * antes de persistir no banco.
     * </p>
     *
     * @return {@link PasswordEncoder} configurado
     * @author Albert
     * @since 2026-03-05
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}