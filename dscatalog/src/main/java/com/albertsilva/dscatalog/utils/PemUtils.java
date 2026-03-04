package com.albertsilva.dscatalog.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class PemUtils {

  public static RSAPrivateKey readPrivateKey(InputStream is) throws Exception {
    try (PemReader pemReader = new PemReader(new InputStreamReader(is))) {
      PemObject pemObject = pemReader.readPemObject();
      if (pemObject == null)
        throw new RuntimeException("Arquivo PEM inválido");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }
  }

  public static RSAPublicKey readPublicKey(InputStream is) throws Exception {
    try (PemReader pemReader = new PemReader(new InputStreamReader(is))) {
      PemObject pemObject = pemReader.readPemObject();
      if (pemObject == null)
        throw new RuntimeException("Arquivo PEM inválido");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) kf.generatePublic(keySpec);
    }
  }

}