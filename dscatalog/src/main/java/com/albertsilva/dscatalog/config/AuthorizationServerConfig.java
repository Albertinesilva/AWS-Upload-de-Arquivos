package com.albertsilva.dscatalog.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração do Authorization Server para o projeto.
 *
 * <p>
 * Define os endpoints de autenticação OAuth2, os clientes registrados
 * (com fluxos e escopos), e as configurações de token JWT.
 * </p>
 *
 * <p>
 * Esta configuração permite que o Spring Authorization Server
 * gerencie a emissão de tokens com OAuth2 e JWT, incluindo
 * a utilização do fluxo {@link AuthorizationGrantType#CLIENT_CREDENTIALS}.
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
@Configuration
public class AuthorizationServerConfig {

  @Value("${security.oauth2.client.client-id}")
  private String clientId;

  @Value("${security.oauth2.client.client-secret}")
  private String clientSecret;

  @Value("${jwt.duration}")
  private Integer jwtDuration;

  /**
   * Configura a segurança dos endpoints do Authorization Server.
   *
   * <p>
   * Aplica a segurança apenas aos endpoints do OAuth2 Authorization Server,
   * exige autenticação para qualquer requisição, desabilita CSRF e aplica
   * o configurador específico de OAuth2.
   * </p>
   *
   * @param http instância de {@link HttpSecurity} fornecida pelo Spring
   * @return {@link SecurityFilterChain} configurado para o Authorization Server
   * @throws Exception caso ocorra algum erro de configuração
   * @author Albert
   * @since 2026-03-05
   */
  @Bean
  @Order(1)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
    http
        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .apply(authorizationServerConfigurer);
    return http.build();
  }

  /**
   * Registra um cliente em memória para o Authorization Server.
   *
   * <p>
   * O cliente registrado utiliza o fluxo
   * {@link AuthorizationGrantType#CLIENT_CREDENTIALS},
   * possui escopos "read" e "write", e o token de acesso possui duração
   * configurável
   * via {@code jwt.duration}.
   * </p>
   *
   * @param encoder {@link PasswordEncoder} utilizado para criptografar a senha do
   *                cliente
   * @return {@link RegisteredClientRepository} contendo o cliente registrado
   * @author Albert
   * @since 2026-03-05
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder) {

    RegisteredClient client = RegisteredClient
        .withId(UUID.randomUUID().toString())
        .clientId(clientId)
        .clientSecret(encoder.encode(clientSecret))
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("read")
        .scope("write")
        .tokenSettings(TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofSeconds(jwtDuration)) // access token curto
            .refreshTokenTimeToLive(Duration.ofDays(10)) // refresh token longo
            .reuseRefreshTokens(false) // evita reuso de refresh tokens
            .build())
        .build();

    return new InMemoryRegisteredClientRepository(client);
  }

  /**
   * Configurações gerais do Authorization Server.
   *
   * <p>
   * Define o issuer (emissor) dos tokens e pode ser expandido para
   * customizações adicionais, como endpoints e políticas.
   * </p>
   *
   * @return {@link AuthorizationServerSettings} configurado
   * @author Albert
   * @since 2026-03-05
   */
  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
        .issuer("http://localhost:8080")
        .build();
  }
}