package com.devsuperior.dscatalog.entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entidade que representa um Perfil (Role) de autorização no sistema.
 *
 * <p>
 * Mapeada para a tabela {@code tb_role}.
 * </p>
 *
 * <p>
 * Uma Role define permissões que podem ser atribuídas a usuários.
 * Exemplos comuns:
 * </p>
 *
 * <ul>
 * <li>ROLE_ADMIN</li>
 * <li>ROLE_OPERATOR</li>
 * <li>ROLE_CLIENT</li>
 * </ul>
 *
 * <p>
 * Esta entidade é utilizada pelo Spring Security para
 * controle de acesso baseado em papéis (Role-Based Access Control - RBAC).
 * </p>
 */
@Entity
@Table(name = "tb_role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único da role.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Nome da autoridade/perfil.
	 *
	 * <p>
	 * Deve seguir o padrão do Spring Security:
	 * geralmente prefixado com "ROLE_".
	 * </p>
	 */
	private String authority;

	public Role() {
	}

	public Role(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}

	// Getters

	public Long getId() {
		return id;
	}

	public String getAuthority() {
		return authority;
	}

	// Setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * Implementação de hashCode baseada no ID.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Implementação de equals baseada no ID.
	 *
	 * <p>
	 * Entidades JPA devem comparar igualdade
	 * com base na identidade persistida.
	 * </p>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}