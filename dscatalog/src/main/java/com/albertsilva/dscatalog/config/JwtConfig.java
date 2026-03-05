package com.albertsilva.dscatalog.config;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;

import com.albertsilva.dscatalog.utils.PemUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * Configuração do JWT utilizando chaves RSA.
 *
 * <p>
 * Carrega as chaves pública e privada do classpath, cria o JWKSource e
 * configura o {@link JwtDecoder} utilizado pelo Spring Authorization Server.
 * </p>
 * 
 * <p>
 * Permite que os tokens JWT sejam assinados e verificados usando RSA.
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
@Configuration
public class JwtConfig {

  private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

  @Value("${jwt.key-id}")
  private String keyId;

  @Value("${jwt.private-key-path}")
  private String privateKeyPath;

  @Value("${jwt.public-key-path}")
  private String publicKeyPath;

  /**
   * Cria o {@link JWKSource} a partir das chaves RSA.
   *
   * <p>
   * Lê as chaves do classpath usando {@link PemUtils}, cria um {@link RSAKey} e
   * retorna um {@link ImmutableJWKSet} que será usado pelo Spring para
   * emissão e validação de tokens JWT.
   * </p>
   *
   * @return {@link JWKSource} configurado com a chave RSA
   * @throws Exception caso ocorra erro ao carregar ou ler as chaves
   * @author Albert
   * @since 2026-03-05
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource() throws Exception {

    try (InputStream privateKeyStream = new ClassPathResource(privateKeyPath).getInputStream();
        InputStream publicKeyStream = new ClassPathResource(publicKeyPath).getInputStream()) {

      RSAPrivateKey privateKey = PemUtils.readPrivateKey(privateKeyStream);
      RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyStream);

      // Log para verificar se as chaves foram carregadas
      logger.info("==== JWT Keys carregadas ====");
      logger.info("Public Key: {} bits, Alg: {}", publicKey.getModulus().bitLength(), publicKey.getAlgorithm());
      logger.info("============================");

      RSAKey rsaKey = new RSAKey.Builder(publicKey)
          .privateKey(privateKey)
          .keyID(keyId)
          .build();

      return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }
  }

  /**
   * Cria o {@link JwtDecoder} baseado no {@link JWKSource}.
   *
   * <p>
   * O {@link JwtDecoder} é usado pelo Spring Authorization Server para
   * validar tokens JWT assinados com a chave RSA configurada.
   * </p>
   *
   * @param jwkSource fonte de chaves JWK
   * @return {@link JwtDecoder} configurado
   * @author Albert
   * @since 2026-03-05
   */
  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }
}