package com.albertsilva.dscatalog.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation personalizada para validação de inserção de usuário.
 *
 * <p>
 * Essa anotação é aplicada em nível de classe (TYPE) e está associada
 * à implementação {@link UserInsertValidator}, responsável por
 * executar as regras de validação customizadas.
 * </p>
 *
 * <p>
 * Geralmente utilizada em conjunto com DTOs de criação
 * (ex: UserInsertDTO), permitindo validações que envolvem
 * múltiplos campos, como:
 * </p>
 *
 * <ul>
 * <li>Verificação se o e-mail já está cadastrado</li>
 * <li>Validações cruzadas entre campos</li>
 * </ul>
 *
 * <p>
 * Integra-se ao Bean Validation (JSR-380), funcionando em conjunto
 * com anotações como {@code @Valid}.
 * </p>
 */
@Constraint(validatedBy = UserInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInsertValid {

	/**
	 * Mensagem padrão retornada quando a validação falha.
	 *
	 * @return mensagem de erro
	 */
	String message() default "Validation error";

	/**
	 * Permite agrupar validações.
	 *
	 * @return grupos de validação
	 */
	Class<?>[] groups() default {};

	/**
	 * Permite associar metadados adicionais à validação.
	 *
	 * @return payloads da validação
	 */
	Class<? extends Payload>[] payload() default {};
}