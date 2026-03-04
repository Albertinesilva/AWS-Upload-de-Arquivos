package com.albertsilva.dscatalog.services.exceptions;

/**
 * Exceção personalizada lançada quando ocorre uma violação de integridade
 * ou outro erro relacionado à persistência de dados no banco.
 *
 * <p>
 * Essa exceção é normalmente utilizada na camada de serviço para encapsular
 * exceções específicas da camada de acesso a dados (como
 * DataIntegrityViolationException), desacoplando o domínio da aplicação
 * das exceções técnicas do framework.
 * </p>
 *
 * <p>
 * Por ser uma {@link RuntimeException}, não exige tratamento obrigatório
 * (checked exception), permitindo que seja interceptada globalmente por um
 * handler de exceções (ex: @ControllerAdvice).
 * </p>
 */
public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe a mensagem descritiva do erro.
	 *
	 * @param msg mensagem detalhando a causa da exceção
	 */
	public DatabaseException(String msg) {
		super(msg);
	}
}