package com.albertsilva.dscatalog.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementação personalizada de {@link UserDetails} utilizada
 * pelo Spring Security durante o processo de autenticação.
 *
 * <p>
 * Esta classe encapsula os dados essenciais do usuário autenticado
 * e disponibiliza atributos adicionais do domínio da aplicação
 * que podem ser utilizados na geração do JWT.
 * </p>
 *
 * <p>
 * Ela é fundamental para evitar consultas adicionais ao banco
 * durante a emissão do token, pois os dados necessários já
 * estarão disponíveis no {@code Principal}.
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
public class CustomUserDetails implements UserDetails {

  private final Long id;
  private final String firstName;
  private final String email;
  private final String password;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * Construtor completo do usuário personalizado.
   *
   * @param id          identificador único do usuário
   * @param firstName   primeiro nome do usuário
   * @param email       e-mail utilizado como username
   * @param password    senha criptografada
   * @param enabled     indica se o usuário está ativo
   * @param authorities permissões do usuário
   * @author Albert
   * @since 2026-03-05
   */
  public CustomUserDetails(
      Long id,
      String firstName,
      String email,
      String password,
      boolean enabled,
      Collection<? extends GrantedAuthority> authorities) {

    this.id = id;
    this.firstName = firstName;
    this.email = email;
    this.password = password;
    this.enabled = enabled;
    this.authorities = authorities;
  }

  /**
   * Retorna o ID do usuário.
   *
   * @return ID do usuário
   * @author Albert
   * @since 2026-03-05
   */
  public Long getId() {
    return id;
  }

  /**
   * Retorna o primeiro nome do usuário.
   *
   * @return primeiro nome do usuário
   * @author Albert
   * @since 2026-03-05
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Retorna as permissões concedidas ao usuário.
   *
   * @return coleção de {@link GrantedAuthority} associadas ao usuário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Retorna a senha criptografada do usuário.
   *
   * @return senha do usuário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Retorna o username utilizado para autenticação.
   * Neste caso, o e-mail.
   *
   * @return e-mail do usuário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Indica se a conta do usuário não está expirada.
   * Pode ser adaptado para lógica de domínio.
   *
   * @return sempre true
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indica se a conta não está bloqueada.
   * Pode ser adaptado para lógica de domínio.
   *
   * @return sempre true
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indica se as credenciais não estão expiradas.
   *
   * @return sempre true
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indica se o usuário está habilitado.
   *
   * @return true se o usuário estiver ativo, false caso contrário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Implementação de equals baseada no ID do usuário.
   *
   * @param o objeto a ser comparado
   * @return true se os IDs forem iguais, false caso contrário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof CustomUserDetails that))
      return false;
    return Objects.equals(id, that.id);
  }

  /**
   * Implementação de hashCode baseada no ID do usuário.
   *
   * @return hash do ID do usuário
   * @author Albert
   * @since 2026-03-05
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}