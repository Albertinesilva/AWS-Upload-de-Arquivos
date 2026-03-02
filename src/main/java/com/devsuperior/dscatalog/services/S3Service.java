package com.devsuperior.dscatalog.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * Serviço responsável por operações de upload de arquivos
 * para um bucket do Amazon S3.
 *
 * <p>
 * Esta implementação utiliza o cliente {@link AmazonS3} configurado
 * na classe de configuração da aplicação. O método disponibilizado
 * realiza o envio de um arquivo local para o bucket configurado.
 * </p>
 *
 * <p>
 * A propriedade esperada no arquivo {@code application.properties}
 * ou {@code application.yml} é:
 * </p>
 *
 * <ul>
 * <li><b>s3.bucket</b> – Nome do bucket de destino no Amazon S3</li>
 * </ul>
 *
 * <p>
 * <b>Observação:</b> Esta implementação utiliza um nome fixo
 * ("test.jpg") como chave do objeto no S3. Em ambientes reais,
 * recomenda-se gerar nomes dinâmicos para evitar sobrescrita
 * de arquivos.
 * </p>
 *
 * @author
 */
@Service
public class S3Service {

  /**
   * Logger responsável pelo registro de eventos do serviço.
   */
  private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

  /**
   * Cliente Amazon S3 injetado pelo Spring.
   * Responsável por executar operações contra o bucket.
   */
  @Autowired
  private AmazonS3 s3client;

  /**
   * Nome do bucket S3 definido nas propriedades da aplicação.
   */
  @Value("${s3.bucket}")
  private String bucketName;

  /**
   * Realiza o upload de um arquivo local para o bucket S3 configurado.
   *
   * <p>
   * O arquivo é enviado utilizando a API
   * {@link AmazonS3#putObject(PutObjectRequest)}.
   * Atualmente, o objeto é armazenado com a chave fixa "test.jpg".
   * </p>
   *
   * <p>
   * Exceções específicas são tratadas separadamente:
   * </p>
   *
   * <ul>
   * <li>{@link AmazonServiceException} – Erros retornados pelo serviço AWS
   * (ex: permissão negada, bucket inexistente, erro 403, etc.)</li>
   * <li>{@link AmazonClientException} – Erros do lado cliente
   * (ex: falha de conexão, timeout, problemas de rede)</li>
   * </ul>
   *
   * @param localFilePath caminho absoluto do arquivo local a ser enviado
   */
  public void uploadFile(String localFilePath) {
    try {
      File file = new File(localFilePath);

      LOG.info("Upload start - File: {}", file.getName());

      s3client.putObject(new PutObjectRequest(bucketName, "test.jpg", file));

      LOG.info("Upload successfully completed.");

    } catch (AmazonServiceException e) {
      LOG.error("AmazonServiceException: {}", e.getErrorMessage());
      LOG.error("HTTP Status Code: {}", e.getStatusCode());
      LOG.error("AWS Error Code: {}", e.getErrorCode());

    } catch (AmazonClientException e) {
      LOG.error("AmazonClientException: {}", e.getMessage());
    }
  }
}