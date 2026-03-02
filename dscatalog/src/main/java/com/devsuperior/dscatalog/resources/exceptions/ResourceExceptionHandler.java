package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * Manipulador global de exceções da aplicação.
 *
 * <p>
 * Classe responsável por interceptar exceções lançadas pelos
 * controllers e services, convertendo-as em respostas HTTP
 * padronizadas para o cliente.
 * </p>
 *
 * <p>
 * Utiliza a anotação {@link ControllerAdvice} para aplicar
 * tratamento global a todos os endpoints REST da aplicação.
 * </p>
 *
 * <p>
 * Cada método anotado com {@link ExceptionHandler} define
 * como uma exceção específica deve ser transformada em
 * resposta HTTP.
 * </p>
 */
@ControllerAdvice
public class ResourceExceptionHandler {

	/**
	 * Trata exceções de recurso não encontrado.
	 *
	 * @param e       exceção lançada
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 404 com estrutura padronizada de erro
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(
			ResourceNotFoundException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Trata exceções relacionadas a integridade ou regras de banco de dados.
	 *
	 * @param e       exceção lançada
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 400 com detalhes do erro
	 */
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(
			DatabaseException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Trata erros de validação de argumentos anotados com {@code @Valid}.
	 *
	 * <p>
	 * Retorna todos os erros de campo encontrados na requisição.
	 * </p>
	 *
	 * @param e       exceção de validação
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 422 contendo lista detalhada de erros de campo
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(
			MethodArgumentNotValidException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Validation exception");
		err.setMessage("Validation error");
		err.setPath(request.getRequestURI());

		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Trata exceções retornadas pelo serviço da AWS.
	 *
	 * @param e       exceção do serviço AWS
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 400 com detalhes do erro
	 */
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(
			AmazonServiceException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("AWS Service exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Trata exceções relacionadas ao cliente AWS.
	 *
	 * @param e       exceção do cliente AWS
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 400 com detalhes do erro
	 */
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(
			AmazonClientException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("AWS Client exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Trata exceções de argumentos inválidos.
	 *
	 * @param e       exceção lançada
	 * @param request requisição HTTP atual
	 * @return resposta HTTP 400 indicando requisição inválida
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardError> illegalArgument(
			IllegalArgumentException e,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Bad request");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}
}