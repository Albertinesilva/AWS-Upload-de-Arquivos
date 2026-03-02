package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Classe responsável pela configuração do Resource Server.
 * <p>
 * Define as regras de autorização para acesso aos recursos da aplicação,
 * utilizando autenticação baseada em JWT.
 * </p>
 *
 * <p>
 * Também configura políticas de CORS e permissões específicas
 * por perfil de usuário.
 * </p>
 * 
 * @author
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/**
	 * Ambiente da aplicação, utilizado para identificar o profile ativo.
	 */
	@Autowired
	private Environment env;

	/**
	 * Armazenamento de tokens JWT.
	 */
	@Autowired
	private JwtTokenStore tokenStore;

	/**
	 * Endpoints públicos que não exigem autenticação.
	 */
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };

	/**
	 * Endpoints acessíveis por usuários com perfil OPERATOR ou ADMIN.
	 */
	private static final String[] OPERATOR_OR_ADMIN = { "/products/**", "/categories/**" };

	/**
	 * Endpoints acessíveis exclusivamente por usuários com perfil ADMIN.
	 */
	private static final String[] ADMIN = { "/users/**" };

	/**
	 * Configura o armazenamento de tokens utilizado pelo Resource Server.
	 *
	 * @param resources configurador de segurança do Resource Server
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	/**
	 * Define as regras de autorização e segurança HTTP.
	 *
	 * @param http objeto de configuração de segurança HTTP
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {

		// Configuração específica para ambiente de teste (H2 Console)
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		http.authorizeRequests()
				.antMatchers(PUBLIC).permitAll()
				.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
				.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
				.antMatchers(ADMIN).hasRole("ADMIN")
				.anyRequest().authenticated();

		http.cors().configurationSource(corsConfigurationSource());
	}

	/**
	 * Define a configuração global de CORS da aplicação.
	 *
	 * @return configuração de CORS aplicada a todos os endpoints
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	/**
	 * Registra o filtro de CORS com a maior precedência,
	 * garantindo que seja aplicado antes de outros filtros.
	 *
	 * @return bean de registro do filtro CORS
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}