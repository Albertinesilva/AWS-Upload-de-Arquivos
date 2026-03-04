package com.albertsilva.dscatalog.config;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.albertsilva.dscatalog.utils.PemUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtConfig {

  @Value("${jwt.key-id}")
  private String keyId;

  @Value("${jwt.private-key-path}")
  private String privateKeyPath;

  @Value("${jwt.public-key-path}")
  private String publicKeyPath;

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws Exception {
    try (InputStream privateKeyStream = new ClassPathResource(privateKeyPath).getInputStream();
        InputStream publicKeyStream = new ClassPathResource(publicKeyPath).getInputStream()) {

      RSAPrivateKey privateKey = PemUtils.readPrivateKey(privateKeyStream);
      RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyStream);

      RSAKey rsaKey = new RSAKey.Builder(publicKey)
          .privateKey(privateKey)
          .keyID(keyId != null ? keyId : UUID.randomUUID().toString())
          .build();

      JWKSet jwkSet = new JWKSet(rsaKey);
      return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }
  }

  @Bean
  public JwtDecoder jwtDecoder() throws Exception {
    try (InputStream publicKeyStream = new ClassPathResource(publicKeyPath).getInputStream()) {
      RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyStream);
      return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
  }
}