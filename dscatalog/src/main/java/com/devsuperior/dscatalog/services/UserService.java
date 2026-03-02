package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * Serviço responsável pelas operações relacionadas à entidade {@link User}.
 *
 * <p>
 * Contém as regras de negócio para:
 * <ul>
 * <li>Busca paginada e por ID</li>
 * <li>Inserção e atualização de usuários</li>
 * <li>Exclusão com tratamento de integridade</li>
 * <li>Integração com o Spring Security para autenticação</li>
 * </ul>
 * </p>
 *
 * <p>
 * Implementa {@link UserDetailsService} para permitir que o Spring Security
 * carregue os dados do usuário durante o processo de autenticação.
 * </p>
 */
@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Retorna uma página de usuários.
	 *
	 * @param pageable informações de paginação
	 * @return página de {@link UserDTO}
	 */
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	/**
	 * Busca um usuário pelo seu ID.
	 *
	 * @param id identificador do usuário
	 * @return {@link UserDTO} correspondente
	 * @throws ResourceNotFoundException caso o ID não exista
	 */
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	/**
	 * Insere um novo usuário no sistema.
	 *
	 * <p>
	 * A senha informada é criptografada utilizando {@link BCryptPasswordEncoder}
	 * antes de ser persistida.
	 * </p>
	 *
	 * @param dto dados do usuário a ser inserido
	 * @return {@link UserDTO} com os dados persistidos
	 */
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	/**
	 * Atualiza os dados de um usuário existente.
	 *
	 * @param id  identificador do usuário
	 * @param dto dados atualizados
	 * @return {@link UserDTO} atualizado
	 * @throws ResourceNotFoundException caso o ID não exista
	 */
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	/**
	 * Remove um usuário pelo ID.
	 *
	 * @param id identificador do usuário
	 * @throws ResourceNotFoundException caso o ID não exista
	 * @throws DatabaseException         caso ocorra violação de integridade
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
	 * Copia os dados de um DTO para a entidade {@link User}.
	 *
	 * <p>
	 * Também realiza a associação das roles (perfis) ao usuário.
	 * </p>
	 *
	 * @param dto    objeto com os dados de origem
	 * @param entity entidade de destino
	 */
	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	/**
	 * Carrega um usuário pelo e-mail para autenticação no Spring Security.
	 *
	 * @param username e-mail do usuário
	 * @return {@link UserDetails} contendo os dados do usuário autenticável
	 * @throws UsernameNotFoundException caso o e-mail não exista
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
}