package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe responsável pela configuração principal do Spring Security.
 * <p>
 * Define como a autenticação será realizada, qual serviço carregará
 * os usuários do sistema e quais endpoints devem ser ignorados
 * pela camada de segurança.
 * </p>
 *
 * <p>
 * Também expõe o {@link AuthenticationManager} como bean,
 * permitindo sua utilização no fluxo de autenticação OAuth2.
 * </p>
 * 
 * @author
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Codificador de senha baseado no algoritmo BCrypt.
	 */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Serviço responsável por carregar os dados do usuário
	 * durante o processo de autenticação.
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Configura o mecanismo de autenticação.
	 * <p>
	 * Define que os usuários serão carregados por meio do
	 * {@link UserDetailsService} e que as senhas serão verificadas
	 * utilizando o {@link BCryptPasswordEncoder}.
	 * </p>
	 *
	 * @param auth objeto de configuração de autenticação
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
	}

	/**
	 * Define recursos que devem ser ignorados pela segurança.
	 * <p>
	 * Neste caso, endpoints relacionados ao Actuator não passam
	 * pelo filtro de autenticação.
	 * </p>
	 *
	 * @param web objeto de configuração de segurança web
	 * @throws Exception em caso de erro na configuração
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**");
	}

	/**
	 * Expõe o {@link AuthenticationManager} como bean da aplicação.
	 * <p>
	 * Necessário para que o Authorization Server possa utilizar
	 * o gerenciador de autenticação no fluxo OAuth2.
	 * </p>
	 *
	 * @return instância de {@link AuthenticationManager}
	 * @throws Exception em caso de erro
	 */
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}