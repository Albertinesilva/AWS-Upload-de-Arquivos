package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

/**
 * Repositório responsável pelas operações de acesso a dados
 * da entidade {@link Product}.
 *
 * <p>
 * Além das operações padrão herdadas de {@link JpaRepository},
 * contém consultas personalizadas utilizando JPQL.
 * </p>
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Realiza busca paginada de produtos com filtros opcionais.
	 *
	 * <p>
	 * Filtros disponíveis:
	 * </p>
	 * <ul>
	 * <li>Lista de categorias</li>
	 * <li>Nome parcial (case insensitive)</li>
	 * </ul>
	 *
	 * <p>
	 * A consulta utiliza:
	 * </p>
	 * <ul>
	 * <li>COALESCE para permitir categoria opcional</li>
	 * <li>LOWER + LIKE para busca case insensitive</li>
	 * <li>DISTINCT para evitar duplicidade causada pelo JOIN</li>
	 * </ul>
	 *
	 * @param categories lista de categorias para filtro (pode ser null)
	 * @param name       nome parcial do produto
	 * @param pageable   dados de paginação
	 * @return página de produtos filtrados
	 */
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
			+ "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) ")
	Page<Product> find(List<Category> categories, String name, Pageable pageable);

	/**
	 * Realiza carregamento antecipado (eager fetch) das categorias
	 * associadas a uma lista de produtos.
	 *
	 * <p>
	 * Utiliza JOIN FETCH para evitar o problema de N+1 queries.
	 * </p>
	 *
	 * @param products lista de produtos
	 * @return lista de produtos com categorias já carregadas
	 */
	@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
	List<Product> findProductsWithCategories(List<Product> products);
}