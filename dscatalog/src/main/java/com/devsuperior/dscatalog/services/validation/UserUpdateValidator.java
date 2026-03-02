package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

/**
 * Implementação da validação personalizada para atualização de usuário.
 *
 * <p>
 * Esta classe é responsável por implementar as regras definidas
 * pela annotation {@link UserUpdateValid}.
 * </p>
 *
 * <p>
 * A principal responsabilidade deste validador é impedir que dois
 * usuários diferentes possuam o mesmo e-mail, permitindo que o próprio
 * usuário mantenha seu e-mail atual durante a atualização.
 * </p>
 *
 * <p>
 * Para isso, é necessário capturar o ID do usuário presente na URL
 * da requisição HTTP.
 * </p>
 */
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	/**
	 * Permite acesso à requisição HTTP atual para capturar
	 * variáveis da URL (path variables).
	 */
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserRepository repository;

	/**
	 * Método executado na inicialização do validador.
	 *
	 * @param ann annotation associada
	 */
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	/**
	 * Executa a lógica de validação para atualização de usuário.
	 *
	 * <p>
	 * Fluxo da validação:
	 * </p>
	 * <ol>
	 * <li>Captura o ID da URL da requisição</li>
	 * <li>Busca usuário existente com o e-mail informado</li>
	 * <li>Verifica se o e-mail pertence a outro usuário</li>
	 * <li>Adiciona erro caso exista conflito</li>
	 * </ol>
	 *
	 * @param dto     objeto a ser validado
	 * @param context contexto de validação
	 * @return true se válido, false caso contrário
	 */
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

		@SuppressWarnings("unchecked")
		Map<String, String> uriVars = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		long userId = Long.parseLong(uriVars.get("id"));

		List<FieldMessage> list = new ArrayList<>();

		// Verifica se já existe usuário com o e-mail informado
		User user = repository.findByEmail(dto.getEmail());

		// Se existir e for diferente do usuário atual, gera erro
		if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		// Adiciona erros customizados ao contexto
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
					.addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}

		// Retorna verdadeiro se não houver erros
		return list.isEmpty();
	}
}