package com.albertsilva.dscatalog.resources.exceptions;

import java.io.Serializable;

/**
 * Representa uma mensagem de erro associada a um campo específico
 * de uma requisição.
 *
 * <p>
 * Essa classe é utilizada principalmente em cenários de validação
 * (ex: Bean Validation), onde múltiplos erros podem ocorrer em
 * diferentes campos de um formulário ou DTO.
 * </p>
 *
 * <p>
 * Cada instância contém:
 * <ul>
 * <li><b>fieldName</b> – Nome do campo que apresentou erro</li>
 * <li><b>message</b> – Mensagem descritiva do erro</li>
 * </ul>
 * </p>
 *
 * <p>
 * Geralmente utilizada em conjunto com classes de tratamento
 * global de exceções (ex: ResourceExceptionHandler).
 * </p>
 */
public class FieldMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Nome do campo que apresentou erro de validação.
	 */
	private String fieldName;

	/**
	 * Mensagem descritiva do erro associado ao campo.
	 */
	private String message;

	/**
	 * Construtor padrão.
	 */
	public FieldMessage() {
	}

	/**
	 * Construtor que inicializa o nome do campo e a mensagem de erro.
	 *
	 * @param fieldName nome do campo com erro
	 * @param message   mensagem descritiva do erro
	 */
	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

	/**
	 * Retorna o nome do campo que apresentou erro.
	 *
	 * @return nome do campo
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Define o nome do campo que apresentou erro.
	 *
	 * @param fieldName nome do campo
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Retorna a mensagem descritiva do erro.
	 *
	 * @return mensagem de erro
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Define a mensagem descritiva do erro.
	 *
	 * @param message mensagem de erro
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}