package com.albertsilva.dscatalog.services;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.albertsilva.dscatalog.dto.CategoryDTO;
import com.albertsilva.dscatalog.dto.ProductDTO;
import com.albertsilva.dscatalog.dto.UriDTO;
import com.albertsilva.dscatalog.entities.Category;
import com.albertsilva.dscatalog.entities.Product;
import com.albertsilva.dscatalog.repositories.CategoryRepository;
import com.albertsilva.dscatalog.repositories.ProductRepository;
import com.albertsilva.dscatalog.services.exceptions.DatabaseException;
import com.albertsilva.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * Serviço responsável pelas regras de negócio da entidade {@code Product}.
 *
 * <p>
 * Atua como camada intermediária entre o controller e os repositórios,
 * sendo responsável por:
 * </p>
 *
 * <ul>
 * <li>Consulta paginada com filtros</li>
 * <li>Busca por ID</li>
 * <li>Inserção e atualização</li>
 * <li>Remoção com tratamento de exceções</li>
 * <li>Upload de imagens para armazenamento externo (AWS S3)</li>
 * </ul>
 */
@Service
public class ProductService {

	/**
	 * Repositório responsável pelo acesso aos dados de produtos.
	 */
	@Autowired
	private ProductRepository repository;

	/**
	 * Repositório responsável pelo acesso aos dados de categorias.
	 */
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Serviço responsável pelo upload de arquivos para o Amazon S3.
	 */
	@Autowired
	private S3Service s3Service;

	/**
	 * Retorna uma página de produtos com filtro opcional por categoria e nome.
	 *
	 * @param categoryId identificador da categoria (0 para ignorar filtro)
	 * @param name       filtro opcional por nome do produto
	 * @param pageable   informações de paginação e ordenação
	 * @return página contendo {@link ProductDTO}
	 */
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {

		List<Category> categories = (categoryId == 0)
				? null
				: Arrays.asList(categoryRepository.getReferenceById(categoryId));

		Page<Product> page = repository.find(categories, name, pageable);

		// Evita problema de N+1 carregando categorias associadas
		repository.findProductsWithCategories(page.getContent());

		return page.map(x -> new ProductDTO(x, x.getCategories()));
	}

	/**
	 * Busca um produto pelo seu identificador.
	 *
	 * @param id identificador do produto
	 * @return {@link ProductDTO} correspondente
	 * @throws ResourceNotFoundException caso o produto não exista
	 */
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	/**
	 * Insere um novo produto.
	 *
	 * @param dto dados do produto a ser criado
	 * @return {@link ProductDTO} do produto persistido
	 */
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	/**
	 * Atualiza um produto existente.
	 *
	 * @param id  identificador do produto
	 * @param dto novos dados do produto
	 * @return {@link ProductDTO} atualizado
	 * @throws ResourceNotFoundException caso o ID não exista
	 */
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	/**
	 * Remove um produto pelo seu identificador.
	 *
	 * @param id identificador do produto
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

	/**
	 * Copia os dados do DTO para a entidade.
	 *
	 * <p>
	 * Responsável por mapear os atributos simples e
	 * também gerenciar o relacionamento com categorias.
	 * </p>
	 *
	 * @param dto    objeto de transferência com os dados
	 * @param entity entidade que será atualizada
	 */
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();

		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);
		}
	}

	/**
	 * Realiza o upload de um arquivo para o Amazon S3.
	 *
	 * @param file arquivo enviado na requisição
	 * @return {@link UriDTO} contendo a URL pública do arquivo armazenado
	 */
	public UriDTO uploadFile(MultipartFile file) {
		URL url = s3Service.uploadFile(file);
		return new UriDTO(url.toString());
	}
}