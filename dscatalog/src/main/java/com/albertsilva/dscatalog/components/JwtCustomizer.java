package com.albertsilva.dscatalog.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.stereotype.Component;

import com.albertsilva.dscatalog.entities.User;
import com.albertsilva.dscatalog.repositories.UserRepository;

@Component
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void customize(JwtEncodingContext context) {
    if ("access_token".equals(context.getTokenType().getValue())) {
      var email = context.getPrincipal().getName();
      if (email != null) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
          context.getClaims().claim("userFirstName", user.getFirstName());
          context.getClaims().claim("userId", user.getId());
        }
      }
    }
  }
}