package com.albertsilva.dscatalog.components;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import com.albertsilva.dscatalog.security.CustomUserDetails;

/**
 * Customiza o JWT (JSON Web Token) de acesso adicionando claims específicas do
 * domínio da aplicação.
 * 
 * <p>
 * Esta customização inclui:
 * <ul>
 * <li>Adição de informações do usuário (ID e nome)</li>
 * <li>Inclusão das roles do usuário</li>
 * </ul>
 * Observações:
 * <ul>
 * <li>Aplica-se somente a tokens do tipo
 * {@link OAuth2TokenType#ACCESS_TOKEN}</li>
 * <li>Ignora o fluxo de {@link AuthorizationGrantType#CLIENT_CREDENTIALS}</li>
 * <li>Não realiza consultas adicionais ao banco de dados; utiliza apenas os
 * dados carregados em {@link CustomUserDetails}</li>
 * </ul>
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
@Component
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

  private static final String CLAIM_USER_ID = "userId";
  private static final String CLAIM_FIRST_NAME = "firstName";
  private static final String CLAIM_ROLES = "roles";

  /**
   * Personaliza o token JWT de acesso.
   * 
   * <p>
   * Adiciona claims customizadas contendo:
   * <ul>
   * <li>userId - ID do usuário</li>
   * <li>firstName - primeiro nome do usuário</li>
   * <li>roles - lista de roles do usuário</li>
   * </ul>
   * </p>
   *
   * @param context Contexto do JWT sendo gerado, contém informações do principal
   *                e do tipo de token.
   * @throws IllegalArgumentException Se o principal não estiver presente ou não
   *                                  for do tipo {@link CustomUserDetails}.
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public void customize(JwtEncodingContext context) {

    // Aplica somente para access_token
    if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
      return;
    }

    // Ignora fluxo client_credentials
    if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {
      return;
    }

    Authentication authentication = context.getPrincipal();
    if (authentication == null) {
      return;
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof CustomUserDetails user)) {
      return;
    }

    // Extrai roles já carregadas (sem consulta ao banco)
    var roles = user.getAuthorities()
        .stream()
        .map(auth -> auth.getAuthority())
        .collect(Collectors.toList());

    // Adiciona claims personalizadas ao JWT
    context.getClaims()
        .claim(CLAIM_USER_ID, user.getId())
        .claim(CLAIM_FIRST_NAME, user.getFirstName())
        .claim(CLAIM_ROLES, roles);
  }
}