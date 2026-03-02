package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Classe de configuração geral da aplicação.
 * <p>
 * Responsável por definir beans relacionados à segurança,
 * incluindo codificação de senha e configuração de JWT
 * para autenticação baseada em token.
 * </p>
 *
 * <p>
 * A chave secreta utilizada na assinatura do token é
 * carregada a partir do arquivo de propriedades da aplicação
 * por meio da propriedade <strong>jwt.secret</strong>.
 * </p>
 *
 * @author
 */
@Configuration
public class AppConfig {

	/**
	 * Chave secreta utilizada para assinatura do token JWT.
	 * <p>
	 * O valor é injetado a partir da propriedade
	 * definida no arquivo application.properties ou application.yml.
	 * </p>
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Define o bean responsável pela criptografia de senhas
	 * utilizando o algoritmo BCrypt.
	 *
	 * @return instância de {@link BCryptPasswordEncoder}
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Define o bean responsável por converter e assinar
	 * tokens JWT.
	 * <p>
	 * A chave secreta configurada é utilizada para
	 * assinar digitalmente o token.
	 * </p>
	 *
	 * @return instância de {@link JwtAccessTokenConverter}
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtSecret);
		return tokenConverter;
	}

	/**
	 * Define o armazenamento de tokens baseado em JWT.
	 * <p>
	 * Como o JWT é auto-contido, não há necessidade
	 * de persistência em banco de dados.
	 * </p>
	 *
	 * @return instância de {@link JwtTokenStore}
	 */
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}