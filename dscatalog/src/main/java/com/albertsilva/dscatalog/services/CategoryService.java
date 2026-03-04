package com.albertsilva.dscatalog.services;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.albertsilva.dscatalog.dto.CategoryDTO;
import com.albertsilva.dscatalog.entities.Category;
import com.albertsilva.dscatalog.repositories.CategoryRepository;
import com.albertsilva.dscatalog.services.exceptions.DatabaseException;
import com.albertsilva.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * Serviço responsável pelas regras de negócio da entidade {@code Category}.
 *
 * <p>
 * Atua como camada intermediária entre o controller (Resource)
 * e o repositório (Repository), sendo responsável por:
 * </p>
 *
 * <ul>
 * <li>Orquestrar operações de acesso a dados</li>
 * <li>Controlar transações</li>
 * <li>Converter entidades para DTOs</li>
 * <li>Tratar e padronizar exceções da aplicação</li>
 * </ul>
 *
 * <p>
 * Todas as operações seguem o padrão de arquitetura em camadas
 * recomendado para aplicações Spring Boot.
 * </p>
 */
@Service
public class CategoryService {

  /**
   * Repositório responsável pelo acesso aos dados da entidade Category.
   */
  @Autowired
  private CategoryRepository repository;

  /**
   * Retorna uma página de categorias.
   *
   * <p>
   * A consulta é realizada de forma somente leitura,
   * garantindo melhor performance e integridade transacional.
   * </p>
   *
   * @param pageable informações de paginação e ordenação
   * @return página contendo {@link CategoryDTO}
   */
  @Transactional(readOnly = true)
  public Page<CategoryDTO> findAllPaged(Pageable pageable) {
    Page<Category> list = repository.findAll(pageable);
    return list.map(x -> new CategoryDTO(x));
  }

  /**
   * Busca uma categoria pelo seu identificador.
   *
   * @param id identificador da categoria
   * @return {@link CategoryDTO} correspondente
   * @throws ResourceNotFoundException caso a categoria não exista
   */
  @Transactional(readOnly = true)
  public CategoryDTO findById(Long id) {
    Optional<Category> obj = repository.findById(id);
    Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    return new CategoryDTO(entity);
  }

  /**
   * Insere uma nova categoria.
   *
   * <p>
   * Cria uma nova entidade a partir do DTO recebido
   * e persiste no banco de dados.
   * </p>
   *
   * @param dto dados da categoria a ser criada
   * @return {@link CategoryDTO} da entidade persistida
   */
  @Transactional
  public CategoryDTO insert(CategoryDTO dto) {
    Category entity = new Category();
    entity.setName(dto.getName());
    entity = repository.save(entity);
    return new CategoryDTO(entity);
  }

  /**
   * Atualiza uma categoria existente.
   *
   * <p>
   * Utiliza {@code getOne()} para obter uma referência da entidade.
   * Caso o ID não exista, uma exceção será lançada e tratada
   * como {@link ResourceNotFoundException}.
   * </p>
   *
   * @param id  identificador da categoria
   * @param dto novos dados da categoria
   * @return {@link CategoryDTO} atualizado
   * @throws ResourceNotFoundException caso o ID não exista
   */
  @Transactional
  public CategoryDTO update(Long id, CategoryDTO dto) {
    try {
      Category entity = repository.getOne(id);
      entity.setName(dto.getName());
      entity = repository.save(entity);
      return new CategoryDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
  }

  /**
   * Remove uma categoria pelo seu identificador.
   *
   * <p>
   * Realiza tratamento específico para:
   * </p>
   *
   * <ul>
   * <li>ID inexistente → {@link ResourceNotFoundException}</li>
   * <li>Violação de integridade referencial → {@link DatabaseException}</li>
   * </ul>
   *
   * @param id identificador da categoria
   * @throws ResourceNotFoundException caso o ID não exista
   * @throws DatabaseException         caso haja violação de integridade
   */
  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Integrity violation");
    }
  }
}