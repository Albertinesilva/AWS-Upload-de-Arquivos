package com.albertsilva.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.albertsilva.dscatalog.entities.Category;

/**
 * Repositório responsável pelas operações de acesso a dados
 * da entidade {@link Category}.
 *
 * <p>
 * Estende {@link JpaRepository}, herdando automaticamente
 * operações CRUD padrão como:
 * </p>
 *
 * <ul>
 * <li>save()</li>
 * <li>findById()</li>
 * <li>findAll()</li>
 * <li>deleteById()</li>
 * </ul>
 *
 * <p>
 * O Spring Data JPA gera automaticamente a implementação
 * em tempo de execução.
 * </p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}