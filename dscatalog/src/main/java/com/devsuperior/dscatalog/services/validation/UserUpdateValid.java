package com.devsuperior.dscatalog.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation personalizada para validação de atualização de usuário.
 *
 * <p>
 * Esta anotação é aplicada em nível de classe (TYPE) e está associada
 * à implementação {@link UserUpdateValidator}, responsável por
 * executar regras de validação específicas do processo de atualização.
 * </p>
 *
 * <p>
 * Diferentemente da validação de inserção, a atualização normalmente
 * exige regras adicionais, como:
 * </p>
 *
 * <ul>
 * <li>Permitir o mesmo e-mail caso pertença ao próprio usuário</li>
 * <li>Impedir duplicidade de e-mail para usuários diferentes</li>
 * <li>Validações condicionais baseadas no ID</li>
 * </ul>
 *
 * <p>
 * Integra-se ao Bean Validation (JSR-380) e funciona em conjunto com
 * {@code @Valid} nos endpoints da aplicação.
 * </p>
 */
@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUpdateValid {

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