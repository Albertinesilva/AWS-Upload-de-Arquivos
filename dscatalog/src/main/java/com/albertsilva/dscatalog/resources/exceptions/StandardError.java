package com.albertsilva.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

/**
 * Representa a estrutura padrão de resposta de erro da API.
 *
 * <p>
 * Essa classe é utilizada para padronizar o retorno de erros
 * nas respostas HTTP, garantindo consistência e previsibilidade
 * para os consumidores da API.
 * </p>
 *
 * <p>
 * Normalmente é instanciada pelo {@code ResourceExceptionHandler}
 * quando uma exceção é capturada.
 * </p>
 *
 * <p>
 * Estrutura padrão do JSON retornado:
 * 
 * <pre>
 * {
 *   "timestamp": "2026-03-01T18:25:43Z",
 *   "status": 404,
 *   "error": "Resource not found",
 *   "message": "Id not found 10",
 *   "path": "/categories/10"
 * }
 * </pre>
 * </p>
 */
public class StandardError implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Momento exato em que o erro ocorreu.
	 */
	private Instant timestamp;

	/**
	 * Código de status HTTP da resposta.
	 * Exemplo: 400, 404, 422, 500.
	 */
	private Integer status;

	/**
	 * Descrição resumida do tipo de erro.
	 * Exemplo: "Resource not found", "Validation exception".
	 */
	private String error;

	/**
	 * Mensagem detalhada da exceção.
	 */
	private String message;

	/**
	 * Caminho (URI) da requisição que gerou o erro.
	 */
	private String path;

	/**
	 * Construtor padrão.
	 */
	public StandardError() {
	}

	/**
	 * Retorna o instante em que o erro ocorreu.
	 *
	 * @return timestamp do erro
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * Define o instante em que o erro ocorreu.
	 *
	 * @param timestamp momento do erro
	 */
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Retorna o código de status HTTP.
	 *
	 * @return status HTTP
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Define o código de status HTTP.
	 *
	 * @param status código HTTP
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Retorna a descrição resumida do erro.
	 *
	 * @return descrição do erro
	 */
	public String getError() {
		return error;
	}

	/**
	 * Define a descrição resumida do erro.
	 *
	 * @param error descrição do erro
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Retorna a mensagem detalhada da exceção.
	 *
	 * @return mensagem detalhada
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Define a mensagem detalhada da exceção.
	 *
	 * @param message mensagem da exceção
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Retorna o caminho da requisição que originou o erro.
	 *
	 * @return URI da requisição
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Define o caminho da requisição que originou o erro.
	 *
	 * @param path URI da requisição
	 */
	public void setPath(String path) {
		this.path = path;
	}
}