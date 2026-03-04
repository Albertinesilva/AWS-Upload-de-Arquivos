package com.albertsilva.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albertsilva.dscatalog.entities.Role;

/**
 * Repositório responsável pelo acesso a dados da entidade {@link Role}.
 *
 * <p>
 * Utilizado principalmente para:
 * </p>
 * <ul>
 * <li>Buscar perfis de usuário</li>
 * <li>Associar roles a usuários</li>
 * </ul>
 *
 * <p>
 * Herda todas as operações básicas de {@link JpaRepository}.
 * </p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}