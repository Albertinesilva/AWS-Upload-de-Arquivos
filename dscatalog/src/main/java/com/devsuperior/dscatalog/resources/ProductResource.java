package com.devsuperior.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.UriDTO;
import com.devsuperior.dscatalog.services.ProductService;

/**
 * Controller REST responsável pelo gerenciamento de produtos.
 *
 * <p>
 * Disponibiliza endpoints para:
 * </p>
 * <ul>
 * <li>Listagem paginada de produtos</li>
 * <li>Busca por ID</li>
 * <li>Inserção de novo produto</li>
 * <li>Atualização de produto</li>
 * <li>Remoção de produto</li>
 * <li>Upload de imagem associada ao produto</li>
 * </ul>
 *
 * <p>
 * Atua como camada de entrada (API), delegando as regras de negócio
 * para a camada de serviço {@link ProductService}.
 * </p>
 *
 * Endpoint base: <b>/products</b>
 *
 * @author Albert
 */
@RestController
@RequestMapping(value = "/products")
public class ProductResource {

	/**
	 * Serviço responsável pela lógica de negócio relacionada a produtos.
	 */
	@Autowired
	private ProductService service;

	/**
	 * Retorna uma listagem paginada de produtos, podendo aplicar filtros por:
	 * <ul>
	 * <li>ID da categoria</li>
	 * <li>Nome do produto (busca parcial)</li>
	 * </ul>
	 *
	 * @param categoryId ID da categoria para filtro (0 = sem filtro)
	 * @param name       termo de busca para o nome do produto
	 * @param pageable   informações de paginação (page, size, sort)
	 * @return página contendo os produtos filtrados
	 */
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			Pageable pageable) {

		Page<ProductDTO> list = service.findAllPaged(categoryId, name.trim(), pageable);
		return ResponseEntity.ok().body(list);
	}

	/**
	 * Busca um produto pelo seu ID.
	 *
	 * @param id identificador do produto
	 * @return produto correspondente ao ID informado
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * Insere um novo produto no sistema.
	 *
	 * <p>
	 * O DTO é validado utilizando as anotações de validação
	 * presentes na classe {@link ProductDTO}.
	 * </p>
	 *
	 * @param dto dados do produto a ser criado
	 * @return produto criado com status HTTP 201 (Created)
	 */
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
		dto = service.insert(dto);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(dto.getId())
				.toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	/**
	 * Realiza o upload de uma imagem associada a um produto.
	 *
	 * <p>
	 * O arquivo deve ser enviado no formato <b>multipart/form-data</b>
	 * com o parâmetro "file".
	 * </p>
	 *
	 * @param file arquivo de imagem enviado na requisição
	 * @return URI pública da imagem armazenada
	 */
	@PostMapping(value = "/image")
	public ResponseEntity<UriDTO> uploadImage(@RequestParam("file") MultipartFile file) {
		UriDTO uriDTO = service.uploadFile(file);
		return ResponseEntity.ok().body(uriDTO);
	}

	/**
	 * Atualiza os dados de um produto existente.
	 *
	 * @param id  identificador do produto
	 * @param dto novos dados do produto
	 * @return produto atualizado
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * Remove um produto pelo seu ID.
	 *
	 * @param id identificador do produto
	 * @return resposta HTTP 204 (No Content) em caso de sucesso
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}