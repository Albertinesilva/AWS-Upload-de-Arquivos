package com.albertsilva.dscatalog.services.exceptions;

/**
 * Exceção personalizada lançada quando um recurso solicitado
 * não é encontrado na base de dados.
 *
 * <p>
 * É utilizada principalmente na camada de serviço para indicar que
 * uma entidade não existe para o identificador informado.
 * </p>
 *
 * <p>
 * Essa exceção normalmente é capturada por um manipulador global
 * de exceções (por exemplo, usando {@code @ControllerAdvice}),
 * que converte a exceção em uma resposta HTTP adequada
 * (geralmente status 404 - NOT FOUND).
 * </p>
 *
 * <p>
 * Por estender {@link RuntimeException}, não exige tratamento
 * obrigatório (checked exception).
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe a mensagem descritiva do erro.
	 *
	 * @param msg mensagem detalhando o motivo pelo qual o recurso não foi
	 *            encontrado
	 */
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}