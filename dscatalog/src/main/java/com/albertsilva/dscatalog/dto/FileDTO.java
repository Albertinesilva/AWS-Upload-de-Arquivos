package com.albertsilva.dscatalog.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO responsável por encapsular um arquivo enviado em uma requisição HTTP
 * do tipo multipart/form-data.
 * <p>
 * Utilizado principalmente em endpoints de upload de arquivos,
 * permitindo que o arquivo seja transportado da camada de controller
 * para a camada de serviço.
 * </p>
 * 
 * Implementa {@link Serializable} para permitir serialização do objeto
 * quando necessário (ex: cache distribuído ou transporte em rede).
 * 
 * @author Albert
 */
public class FileDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Arquivo enviado na requisição.
   * <p>
   * Representa o conteúdo do arquivo recebido via multipart request.
   * Pode ser uma imagem, documento ou qualquer outro tipo de arquivo suportado.
   * </p>
   */
  private MultipartFile file;

  /**
   * Construtor padrão.
   */
  public FileDTO() {
  }

  /**
   * Retorna o arquivo enviado na requisição.
   *
   * @return objeto {@link MultipartFile} contendo os dados do arquivo
   */
  public MultipartFile getFile() {
    return file;
  }

  /**
   * Define o arquivo recebido na requisição.
   *
   * @param file objeto {@link MultipartFile} contendo os dados do upload
   */
  public void setFile(MultipartFile file) {
    this.file = file;
  }
}