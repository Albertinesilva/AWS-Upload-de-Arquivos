package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.devsuperior.dscatalog.components.JwtTokenEnhancer;

/**
 * Classe responsável pela configuração do Authorization Server.
 * <p>
 * Define as regras de autenticação, configuração do cliente OAuth2,
 * geração de tokens JWT e integração com o Spring Security.
 * </p>
 *
 * <p>
 * Utiliza armazenamento em memória para o cliente OAuth2
 * e geração de tokens baseados em JWT assinados com chave secreta.
 * </p>
 * 
 * @author
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	/**
	 * Identificador do cliente OAuth2.
	 */
	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	/**
	 * Segredo do cliente OAuth2.
	 */
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	/**
	 * Tempo de validade do token JWT em segundos.
	 */
	@Value("${jwt.duration}")
	private Integer jwtDuration;

	/**
	 * Bean responsável pela criptografia de senha utilizando BCrypt.
	 */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Conversor responsável por assinar e converter tokens JWT.
	 */
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	/**
	 * Armazenamento de tokens baseado em JWT.
	 */
	@Autowired
	private JwtTokenStore tokenStore;

	/**
	 * Gerenciador de autenticação utilizado no fluxo de login.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * Componente responsável por adicionar informações extras ao token JWT.
	 */
	@Autowired
	private JwtTokenEnhancer tokenEnhancer;

	/**
	 * Configura as permissões de acesso aos endpoints do Authorization Server.
	 *
	 * @param security configuração de segurança do servidor de autorização
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	/**
	 * Configura os detalhes do cliente OAuth2.
	 * <p>
	 * O cliente é armazenado em memória e utiliza grant type "password".
	 * </p>
	 *
	 * @param clients configurador de clientes
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(clientId)
				.secret(passwordEncoder.encode(clientSecret))
				.scopes("read", "write")
				.authorizedGrantTypes("password")
				.accessTokenValiditySeconds(jwtDuration);
	}

	/**
	 * Configura os endpoints do Authorization Server.
	 * <p>
	 * Define o AuthenticationManager, armazenamento do token,
	 * conversor JWT e a cadeia de TokenEnhancers.
	 * </p>
	 *
	 * @param endpoints configurador de endpoints
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		/**
		 * Cadeia de customização do token,
		 * permitindo adicionar claims personalizadas.
		 */
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer));

		endpoints.authenticationManager(authenticationManager)
				.tokenStore(tokenStore)
				.accessTokenConverter(accessTokenConverter)
				.tokenEnhancer(chain);
	}
}