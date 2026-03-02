package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

/**
 * Componente responsável por customizar o conteúdo do token JWT.
 * <p>
 * Implementa a interface {@link TokenEnhancer} do Spring Security OAuth2,
 * permitindo adicionar informações extras (claims) ao token gerado
 * durante o processo de autenticação.
 * </p>
 *
 * <p>
 * Neste caso, são adicionadas informações básicas do usuário autenticado,
 * como o primeiro nome e o identificador, possibilitando que esses dados
 * sejam acessados no lado cliente sem a necessidade de nova consulta ao
 * servidor.
 * </p>
 *
 * @author
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	/**
	 * Repositório utilizado para buscar os dados completos do usuário
	 * com base no e-mail autenticado.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Método responsável por adicionar informações adicionais ao token JWT.
	 *
	 * @param accessToken    token de acesso gerado pelo servidor de autorização
	 * @param authentication objeto que contém os dados da autenticação
	 * @return token de acesso com informações adicionais incluídas
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		/**
		 * Recupera o usuário autenticado a partir do e-mail
		 * fornecido pelo objeto de autenticação.
		 */
		User user = userRepository.findByEmail(authentication.getName());

		/**
		 * Mapa contendo as informações adicionais que serão
		 * inseridas como claims no token JWT.
		 */
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());

		/**
		 * Converte o token para DefaultOAuth2AccessToken
		 * para permitir a inclusão das informações extras.
		 */
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);

		return accessToken;
	}
}