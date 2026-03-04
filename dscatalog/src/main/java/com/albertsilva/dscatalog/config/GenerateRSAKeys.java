package com.albertsilva.dscatalog.config;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class GenerateRSAKeys {

  public static void main(String[] args) throws Exception {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair keyPair = keyGen.generateKeyPair();

    // Salva chave privada
    try (PemWriter pemWriter = new PemWriter(new FileWriter("private_key.pem"))) {
      PemObject pemObject = new PemObject("PRIVATE KEY", keyPair.getPrivate().getEncoded());
      pemWriter.writeObject(pemObject);
    }

    // Salva chave pública
    try (PemWriter pemWriter = new PemWriter(new FileWriter("public_key.pem"))) {
      PemObject pemObject = new PemObject("PUBLIC KEY", keyPair.getPublic().getEncoded());
      pemWriter.writeObject(pemObject);
    }

    System.out.println("Chaves RSA geradas em PEM!");
  }
}