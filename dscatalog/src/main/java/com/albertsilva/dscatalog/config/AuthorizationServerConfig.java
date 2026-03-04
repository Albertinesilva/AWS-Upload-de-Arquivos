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
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

  @Value("${security.oauth2.client.client-id}")
  private String clientId;

  @Value("${security.oauth2.client.client-secret}")
  private String clientSecret;

  @Value("${jwt.duration}")
  private Integer jwtDuration;

  // 1️⃣ Authorization Server Security
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

  // 2️⃣ Registered Client
  @Bean
  public RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder) {
    RegisteredClient client = RegisteredClient
        .withId(UUID.randomUUID().toString())
        .clientId(clientId)
        .clientSecret(encoder.encode(clientSecret))
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .scope("read")
        .scope("write")
        .tokenSettings(TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofSeconds(jwtDuration))
            .build())
        .build();
    return new InMemoryRegisteredClientRepository(client);
  }

  // 3️⃣ Token Generator com JwtGenerator (RSA)
  @Bean
  public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
    // JwtEncoder para RSA
    var jwtEncoder = new NimbusJwtEncoder(jwkSource);

    // JwtGenerator usa o encoder
    var jwtGenerator = new JwtGenerator(jwtEncoder);

    return new DelegatingOAuth2TokenGenerator(jwtGenerator);
  }

  // 4️⃣ JwtDecoder (RSA)
  // @Bean
  // public JwtDecoder jwtDecoder() throws Exception {
  //   RSAPublicKey publicKey = PemUtils.readPublicKey("public_key.pem");
  //   return NimbusJwtDecoder.withPublicKey(publicKey).build();
  // }

  // 5️⃣ Authorization Server Settings
  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
        .issuer("http://localhost:8080")
        .build();
  }
}