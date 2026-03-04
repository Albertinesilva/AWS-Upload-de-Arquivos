package com.albertsilva.dscatalog.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

/**
 * Serviço responsável pelo gerenciamento de uploads de arquivos
 * para o Amazon S3.
 *
 * <p>
 * Implementação voltada para ambiente de produção,
 * garantindo geração de nome único, organização por diretório
 * e validações básicas de segurança.
 * </p>
 */
@Service
public class S3Service {

  private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

  @Autowired
  private AmazonS3 s3Client;

  @Value("${s3.bucket}")
  private String bucketName;

  /**
   * Diretório padrão dentro do bucket.
   * Pode ser alterado conforme regra de negócio.
   */
  private static final String UPLOAD_FOLDER = "images/";

  /**
   * Realiza upload de um arquivo Multipart para o Amazon S3.
   *
   * @param file arquivo recebido via requisição multipart
   * @return URL pública do arquivo armazenado
   */
  public URL uploadFile(MultipartFile file) {

    validateFile(file);

    String originalName = file.getOriginalFilename();
    String extension = FilenameUtils.getExtension(originalName);

    // Geração segura de nome único
    String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

    // Organização por pasta
    String finalFileName = UPLOAD_FOLDER + uniqueFileName;

    try (InputStream inputStream = file.getInputStream()) {

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());
      LOG.info("Starting upload - Bucket: {}, File: {}", bucketName, finalFileName);

      s3Client.putObject(bucketName, finalFileName, inputStream, metadata);
      LOG.info("Upload completed successfully - File: {}", finalFileName);

      return s3Client.getUrl(bucketName, finalFileName);

    } catch (IOException e) {
      LOG.error("Error during file upload", e);
      throw new IllegalArgumentException("Error processing file upload", e);
    }
  }

  /**
   * Valida regras básicas do arquivo antes do upload.
   *
   * @param file arquivo recebido
   */
  private void validateFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File cannot be null or empty");
    }

    if (file.getOriginalFilename() == null) {
      throw new IllegalArgumentException("File must have a valid name");
    }
  }
}
