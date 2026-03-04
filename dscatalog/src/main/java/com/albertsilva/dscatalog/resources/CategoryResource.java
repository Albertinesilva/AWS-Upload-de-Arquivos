package com.albertsilva.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.albertsilva.dscatalog.dto.CategoryDTO;
import com.albertsilva.dscatalog.services.CategoryService;

/**
 * Resource responsável por expor os endpoints REST
 * relacionados à entidade Categoria.
 * 
 * <p>
 * Disponibiliza operações de:
 * <ul>
 * <li>Busca paginada de categorias</li>
 * <li>Busca por ID</li>
 * <li>Inserção</li>
 * <li>Atualização</li>
 * <li>Remoção</li>
 * </ul>
 * </p>
 * 
 * @author
 */
@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

  /**
   * Serviço responsável pelas regras de negócio da Categoria.
   */
  @Autowired
  private CategoryService service;

  /**
   * Retorna uma lista paginada de categorias.
   *
   * @param pageable informações de paginação (página, tamanho e ordenação)
   * @return ResponseEntity contendo a página de CategoryDTO
   */
  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
    Page<CategoryDTO> list = service.findAllPaged(pageable);
    return ResponseEntity.ok().body(list);
  }

  /**
   * Busca uma categoria pelo seu identificador.
   *
   * @param id identificador da categoria
   * @return ResponseEntity contendo o CategoryDTO correspondente
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
    CategoryDTO dto = service.findById(id);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * Insere uma nova categoria.
   *
   * @param dto objeto contendo os dados da categoria a ser criada
   * @return ResponseEntity contendo o objeto criado e o URI do recurso
   */
  @PostMapping
  public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
    dto = service.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(dto.getId()).toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  /**
   * Atualiza os dados de uma categoria existente.
   *
   * @param id  identificador da categoria a ser atualizada
   * @param dto objeto contendo os novos dados da categoria
   * @return ResponseEntity contendo o objeto atualizado
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
    dto = service.update(id, dto);
    return ResponseEntity.ok().body(dto);
  }

  /**
   * Remove uma categoria pelo seu identificador.
   *
   * @param id identificador da categoria a ser removida
   * @return ResponseEntity sem conteúdo (204 No Content)
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}