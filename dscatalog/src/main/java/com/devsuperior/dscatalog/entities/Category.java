package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Entidade que representa uma Categoria de produtos no sistema.
 *
 * <p>
 * Mapeada para a tabela {@code tb_category}.
 * </p>
 *
 * <p>
 * Possui relacionamento Many-to-Many com {@link Product},
 * sendo o lado inverso da associação.
 * </p>
 *
 * <p>
 * Contém controle automático de auditoria:
 * </p>
 * <ul>
 * <li>{@code createdAt} → definido automaticamente na criação</li>
 * <li>{@code updatedAt} → definido automaticamente na atualização</li>
 * </ul>
 */
@Entity
@Table(name = "tb_category")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único da categoria.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Nome da categoria.
	 */
	private String name;

	/**
	 * Data de criação do registro.
	 * Definida automaticamente antes da persistência.
	 */
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;

	/**
	 * Data da última atualização do registro.
	 * Definida automaticamente antes da atualização.
	 */
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;

	/**
	 * Produtos associados a esta categoria.
	 *
	 * <p>
	 * Lado inverso do relacionamento Many-to-Many.
	 * </p>
	 */
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products = new HashSet<>();

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Executado automaticamente antes da persistência da entidade.
	 * Define a data de criação.
	 */
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
	}

	/**
	 * Executado automaticamente antes da atualização da entidade.
	 * Define a data de modificação.
	 */
	@PreUpdate
	public void preUpdate() {
		updatedAt = Instant.now();
	}

	// Getters

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Set<Product> getProducts() {
		return products;
	}

	// Setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	// equals e hashCode baseados no ID

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}