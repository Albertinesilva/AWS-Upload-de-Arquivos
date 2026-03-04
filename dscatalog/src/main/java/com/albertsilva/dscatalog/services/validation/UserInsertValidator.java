package com.albertsilva.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.albertsilva.dscatalog.dto.UserInsertDTO;
import com.albertsilva.dscatalog.entities.User;
import com.albertsilva.dscatalog.repositories.UserRepository;
import com.albertsilva.dscatalog.resources.exceptions.FieldMessage;

/**
 * Implementação da validação personalizada para inserção de usuário.
 *
 * <p>
 * Esta classe é responsável por implementar as regras definidas
 * pela annotation {@link UserInsertValid}.
 * </p>
 *
 * <p>
 * A validação ocorre em nível de objeto (classe), permitindo
 * verificar regras que dependem de múltiplos campos ou que
 * exigem acesso ao banco de dados.
 * </p>
 *
 * <p>
 * Neste caso, a regra implementada verifica se já existe
 * um usuário cadastrado com o mesmo e-mail.
 * </p>
 */
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Autowired
	private UserRepository repository;

	/**
	 * Método executado na inicialização do validador.
	 * Pode ser utilizado para acessar atributos da annotation.
	 *
	 * @param ann annotation associada à validação
	 */
	@Override
	public void initialize(UserInsertValid ann) {
	}

	/**
	 * Executa a lógica de validação.
	 *
	 * @param dto     objeto a ser validado
	 * @param context contexto da validação
	 * @return true se for válido, false caso contrário
	 */
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		// Verifica se já existe usuário com o e-mail informado
		User user = repository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		// Adiciona os erros personalizados ao contexto de validação
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