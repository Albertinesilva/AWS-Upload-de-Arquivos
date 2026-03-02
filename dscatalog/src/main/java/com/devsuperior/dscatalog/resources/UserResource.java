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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.services.UserService;

/**
 * Resource responsável por expor os endpoints REST
 * relacionados à entidade Usuário.
 *
 * <p>
 * Disponibiliza operações de:
 * <ul>
 * <li>Busca paginada de usuários</li>
 * <li>Busca por ID</li>
 * <li>Inserção com validação</li>
 * <li>Atualização com validação</li>
 * <li>Remoção</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/users")
public class UserResource {

	/**
	 * Serviço responsável pelas regras de negócio da entidade Usuário.
	 */
	@Autowired
	private UserService service;

	/**
	 * Retorna uma lista paginada de usuários.
	 *
	 * @param pageable informações de paginação (página, tamanho e ordenação)
	 * @return ResponseEntity contendo a página de UserDTO
	 */
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		Page<UserDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	/**
	 * Busca um usuário pelo seu identificador.
	 *
	 * @param id identificador do usuário
	 * @return ResponseEntity contendo o UserDTO correspondente
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		UserDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * Insere um novo usuário no sistema.
	 *
	 * <p>
	 * Utiliza {@link Valid} para aplicar as validações definidas
	 * na classe UserInsertDTO.
	 * </p>
	 *
	 * @param dto objeto contendo os dados para inserção do usuário
	 * @return ResponseEntity contendo o usuário criado e o URI do recurso
	 */
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserInsertDTO dto) {
		UserDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}

	/**
	 * Atualiza os dados de um usuário existente.
	 *
	 * <p>
	 * Utiliza {@link Valid} para aplicar as validações
	 * definidas na classe UserUpdateDTO.
	 * </p>
	 *
	 * @param id  identificador do usuário a ser atualizado
	 * @param dto objeto contendo os novos dados do usuário
	 * @return ResponseEntity contendo o usuário atualizado
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
		UserDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);
	}

	/**
	 * Remove um usuário pelo seu identificador.
	 *
	 * @param id identificador do usuário a ser removido
	 * @return ResponseEntity sem conteúdo (204 No Content)
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}