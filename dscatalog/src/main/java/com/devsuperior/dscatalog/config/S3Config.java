package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Classe de configuração responsável por instanciar e configurar o cliente
 * {@link AmazonS3} utilizado para comunicação com o serviço Amazon S3.
 *
 * <p>
 * Esta configuração utiliza credenciais estáticas (Access Key e Secret Key)
 * fornecidas via propriedades da aplicação. O cliente é registrado como
 * um {@link Bean} gerenciado pelo Spring, permitindo sua injeção em outros
 * componentes da aplicação.
 * </p>
 *
 * <p>
 * As propriedades esperadas no arquivo {@code application.properties}
 * ou {@code application.yml} são:
 * </p>
 *
 * <ul>
 * <li><b>aws.access_key_id</b> – Access Key ID do usuário IAM</li>
 * <li><b>aws.secret_access_key</b> – Secret Access Key do usuário IAM</li>
 * <li><b>s3.region</b> – Região AWS onde o bucket está configurado (ex:
 * sa-east-1)</li>
 * </ul>
 *
 * <p>
 * <b>Importante:</b> Para ambientes de produção, recomenda-se utilizar
 * mecanismos mais seguros como variáveis de ambiente, perfis AWS ou
 * IAM Roles ao invés de armazenar credenciais diretamente no arquivo
 * de propriedades.
 * </p>
 *
 * @author
 */
@Configuration
public class S3Config {

  /**
   * Access Key ID do usuário IAM com permissão de acesso ao S3.
   */
  @Value("${aws.access_key_id}")
  private String awsId;

  /**
   * Secret Access Key associada ao Access Key ID.
   */
  @Value("${aws.secret_access_key}")
  private String awsKey;

  /**
   * Região da AWS onde o bucket S3 está provisionado.
   * Exemplo: sa-east-1, us-east-1, etc.
   */
  @Value("${s3.region}")
  private String region;

  /**
   * Cria e configura uma instância do cliente {@link AmazonS3}.
   *
   * <p>
   * O cliente é configurado com:
   * </p>
   * <ul>
   * <li>Credenciais estáticas via {@link BasicAWSCredentials}</li>
   * <li>Região definida via propriedade externa</li>
   * </ul>
   *
   * <p>
   * Este bean poderá ser injetado em serviços responsáveis por:
   * </p>
   * <ul>
   * <li>Upload de arquivos</li>
   * <li>Download de objetos</li>
   * <li>Remoção de arquivos</li>
   * <li>Geração de URLs pré-assinadas</li>
   * </ul>
   *
   * @return instância configurada de {@link AmazonS3}
   */
  @Bean
  public AmazonS3 s3client() {
    BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);

    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.fromName(region))
        .withCredentials(new AWSStaticCredentialsProvider(awsCred))
        .build();
  }
}