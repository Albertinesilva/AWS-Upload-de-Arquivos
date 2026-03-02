package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Entidade que representa um Produto do catálogo.
 *
 * <p>
 * Mapeada para a tabela {@code tb_product}.
 * </p>
 *
 * <p>
 * Um produto pode pertencer a uma ou mais categorias,
 * sendo o lado proprietário do relacionamento Many-to-Many
 * com {@link Category}.
 * </p>
 *
 * <p>
 * A associação é realizada através da tabela intermediária
 * {@code tb_product_category}.
 * </p>
 *
 * <p>
 * O campo {@code description} é armazenado como TEXT no banco,
 * permitindo descrições extensas.
 * </p>
 */
@Entity
@Table(name = "tb_product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único do produto.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Nome do produto.
	 */
	private String name;

	/**
	 * Descrição detalhada do produto.
	 */
	@Column(columnDefinition = "TEXT")
	private String description;

	/**
	 * Preço do produto.
	 */
	private Double price;

	/**
	 * URL da imagem representativa do produto.
	 */
	private String imgUrl;

	/**
	 * Data associada ao produto (ex: data de cadastro ou publicação).
	 */
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant date;

	/**
	 * Categorias associadas ao produto.
	 *
	 * <p>
	 * Relacionamento Many-to-Many com tabela de junção.
	 * Este é o lado proprietário da associação.
	 * </p>
	 */
	@ManyToMany
	@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	public Product() {
	}

	public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	// Getters

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	// Setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setDate(Instant date) {
		this.date = date;
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
	 * Entidades JPA devem comparar igualdade com base
	 * na identidade persistida.
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}