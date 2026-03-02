package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

/**
 * DTO responsável por encapsular uma URI retornada pela aplicação.
 * <p>
 * Geralmente utilizado como resposta de operações que geram ou
 * disponibilizam recursos externos, como uploads de arquivos
 * para serviços de armazenamento (ex: Amazon S3).
 * </p>
 * 
 * Implementa {@link Serializable} para permitir serialização do objeto
 * quando necessário (ex: cache distribuído, transporte em rede ou logs
 * estruturados).
 * 
 * @author Albert
 */
public class UriDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * URI do recurso gerado ou disponibilizado.
   * <p>
   * Pode representar, por exemplo, o endereço público de um arquivo
   * armazenado em um serviço externo.
   * </p>
   */
  private String uri;

  /**
   * Construtor padrão.
   */
  public UriDTO() {
  }

  /**
   * Construtor que inicializa o objeto com a URI informada.
   *
   * @param uri endereço do recurso gerado
   */
  public UriDTO(String uri) {
    this.uri = uri;
  }

  /**
   * Retorna a URI do recurso.
   *
   * @return string contendo o endereço do recurso
   */
  public String getUri() {
    return uri;
  }

  /**
   * Define a URI do recurso.
   *
   * @param uri endereço do recurso a ser armazenado
   */
  public void setUri(String uri) {
    this.uri = uri;
  }
}