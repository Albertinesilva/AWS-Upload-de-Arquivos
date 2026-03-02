package com.devsuperior.dscatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.devsuperior.dscatalog.services.S3Service;

/**
 * Classe principal da aplicação Spring Boot.
 *
 * <p>
 * Responsável por inicializar o contexto da aplicação e executar
 * rotinas de teste após o carregamento completo do Spring.
 * </p>
 *
 * <p>
 * Esta classe implementa {@link CommandLineRunner}, permitindo
 * a execução de código automaticamente assim que a aplicação
 * é iniciada.
 * </p>
 *
 * <p>
 * No cenário atual, a aplicação realiza um teste de upload
 * de arquivo para o Amazon S3 utilizando o {@link S3Service}.
 * </p>
 *
 * <p>
 * <b>Importante:</b> Esta abordagem é adequada apenas para
 * testes ou fins didáticos. Em ambientes reais, o upload
 * normalmente é disparado por requisições HTTP (REST API),
 * eventos ou integrações externas.
 * </p>
 *
 * @author
 */
@SpringBootApplication
public class DscatalogApplication implements CommandLineRunner {

	/**
	 * Serviço responsável pela comunicação com o Amazon S3.
	 */
	@Autowired
	private S3Service s3Service;

	/**
	 * Método principal responsável por inicializar a aplicação
	 * Spring Boot.
	 *
	 * @param args argumentos passados via linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

	/**
	 * Método executado automaticamente após a inicialização
	 * do contexto Spring.
	 *
	 * <p>
	 * Neste exemplo, é realizado o upload de um arquivo local
	 * específico para o bucket S3 configurado.
	 * </p>
	 *
	 * <p>
	 * O caminho informado deve existir no sistema de arquivos
	 * da máquina onde a aplicação está sendo executada.
	 * </p>
	 *
	 * @param args argumentos de inicialização
	 * @throws Exception caso ocorra falha durante a execução
	 */
	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile(
				"D:\\DEVSUPERIOR\\BOOTCAMP-SPRING-BOOT\\COLECAO-DE-CONTEUDOS\\upload-de-arquivos\\print.jpg");
	}
}