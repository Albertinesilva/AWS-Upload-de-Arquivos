package com.albertsilva.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albertsilva.dscatalog.entities.User;

/**
 * Repositório responsável pelo acesso a dados da entidade {@link User}.
 *
 * <p>
 * Além das operações padrão do {@link JpaRepository},
 * define método específico para autenticação via e-mail.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Busca um usuário pelo e-mail.
   *
   * <p>
   * Método utilizado principalmente pelo Spring Security
   * durante o processo de autenticação.
   * </p>
   *
   * @param email e-mail do usuário
   * @return usuário correspondente ou null caso não exista
   */
  User findByEmail(String email);
}
