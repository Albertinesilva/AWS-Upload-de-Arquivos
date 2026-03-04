package com.albertsilva.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma resposta de erro específica para falhas de validação.
 *
 * <p>
 * Estende {@link StandardError}, adicionando uma lista de erros
 * relacionados a campos específicos da requisição.
 * </p>
 *
 * <p>
 * Normalmente utilizada quando ocorre {@code MethodArgumentNotValidException},
 * ou seja, quando validações anotadas com {@code @Valid} falham.
 * </p>
 *
 * <p>
 * Estrutura típica do JSON retornado:
 * 
 * <pre>
 * {
 *   "timestamp": "2026-03-01T18:40:12Z",
 *   "status": 422,
 *   "error": "Validation exception",
 *   "message": "Validation error",
 *   "path": "/users",
 *   "errors": [
 *     {
 *       "fieldName": "email",
 *       "message": "Email must be valid"
 *     },
 *     {
 *       "fieldName": "password",
 *       "message": "Password must have at least 8 characters"
 *     }
 *   ]
 * }
 * </pre>
 * </p>
 */
public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;

	/**
	 * Lista de erros de validação por campo.
	 */
	private List<FieldMessage> errors = new ArrayList<>();

	/**
	 * Retorna a lista de erros de validação.
	 *
	 * @return lista contendo os erros de campo
	 */
	public List<FieldMessage> getErrors() {
		return errors;
	}

	/**
	 * Adiciona um erro de validação à lista.
	 *
	 * @param fieldName nome do campo que apresentou erro
	 * @param message   mensagem descritiva do erro
	 */
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
}