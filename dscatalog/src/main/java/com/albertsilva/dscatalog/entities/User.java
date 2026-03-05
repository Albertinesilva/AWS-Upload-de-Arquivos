package com.albertsilva.dscatalog.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entidade que representa um usuário do sistema.
 *
 * <p>
 * Mapeada para a tabela <strong>tb_user</strong> no banco de dados e
 * implementa {@link UserDetails} para integração direta com o Spring Security
 * para autenticação e autorização.
 * </p>
 *
 * <p>
 * Cada usuário pode possuir múltiplos perfis ({@link Role}) em um
 * relacionamento muitos-para-muitos.
 * </p>
 * 
 * @author Albert
 * @since 2026-03-05
 */
@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único do usuário.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Primeiro nome do usuário.
	 */
	private String firstName;

	/**
	 * Sobrenome do usuário.
	 */
	private String lastName;

	/**
	 * Endereço de e-mail do usuário.
	 * Deve ser único no sistema.
	 */
	@Column(unique = true)
	private String email;

	/**
	 * Senha criptografada do usuário.
	 */
	private String password;

	/**
	 * Conjunto de perfis (roles) associados ao usuário.
	 * Relacionamento muitos-para-muitos com carregamento imediato (EAGER),
	 * pois as permissões são necessárias durante a autenticação.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	/**
	 * Construtor padrão exigido pela JPA.
	 */
	public User() {
	}

	/**
	 * Construtor com dados básicos do usuário.
	 *
	 * @param id        identificador do usuário
	 * @param firstName primeiro nome
	 * @param lastName  sobrenome
	 * @param email     e-mail do usuário
	 * @param password  senha criptografada
	 */
	public User(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Retorna o e-mail do usuário (login).
	 *
	 * @return e-mail utilizado como username
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Retorna a senha criptografada do usuário.
	 *
	 * @return senha criptografada
	 */
	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Retorna os perfis (roles) associados ao usuário.
	 *
	 * @return conjunto de roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * Gera hashCode baseado no identificador da entidade.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Compara usuários com base no identificador.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Retorna as autoridades (permissões) do usuário, convertendo cada
	 * {@link Role} em {@link SimpleGrantedAuthority}.
	 *
	 * @return coleção de autoridades utilizadas pelo Spring Security
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getAuthority()))
				.collect(Collectors.toList());
	}

	/**
	 * Retorna o username utilizado para autenticação (neste sistema, o e-mail).
	 *
	 * @return e-mail do usuário
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * Indica se a conta do usuário não está expirada.
	 *
	 * @return true se a conta está ativa
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indica se a conta do usuário não está bloqueada.
	 *
	 * @return true se a conta não está bloqueada
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indica se as credenciais não estão expiradas.
	 *
	 * @return true se as credenciais são válidas
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indica se o usuário está habilitado.
	 *
	 * @return true se o usuário está habilitado
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}