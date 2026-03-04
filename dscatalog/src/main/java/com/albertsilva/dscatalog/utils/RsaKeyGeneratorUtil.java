package com.albertsilva.dscatalog.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class RsaKeyGeneratorUtil {

  public static void generateRsaKeyPair(String privateKeyPath, String publicKeyPath) throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();

    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

    writePemFile(privateKeyPath, "PRIVATE KEY", privateKey.getEncoded());
    writePemFile(publicKeyPath, "PUBLIC KEY", publicKey.getEncoded());

    System.out.println("Chaves RSA geradas com sucesso!");
    System.out.println("Private key: " + privateKeyPath);
    System.out.println("Public key: " + publicKeyPath);
  }

  private static void writePemFile(String path, String description, byte[] bytes) throws IOException {
    String encoded = Base64.getEncoder().encodeToString(bytes);
    String pem = "-----BEGIN " + description + "-----\n"
        + chunkString(encoded, 64)
        + "-----END " + description + "-----\n";

    Path filePath = Paths.get(path);
    if (filePath.getParent() != null) {
      Files.createDirectories(filePath.getParent());
    }
    Files.writeString(filePath, pem); // escreve como UTF-8 por padrão
  }

  private static String chunkString(String str, int chunkSize) {
    StringBuilder sb = new StringBuilder();
    int index = 0;
    while (index < str.length()) {
      sb.append(str, index, Math.min(index + chunkSize, str.length()));
      sb.append("\n");
      index += chunkSize;
    }
    return sb.toString();
  }

  public static void main(String[] args) throws Exception {
    // Gera dentro da pasta raiz do projeto dscatalog
    String privateKeyPath = "D:\\DEVSUPERIOR\\BOOTCAMP-SPRING-BOOT\\COLECAO-DE-CONTEUDOS\\upload-de-arquivos\\dscatalog\\private_key.pem";
    String publicKeyPath = "D:\\DEVSUPERIOR\\BOOTCAMP-SPRING-BOOT\\COLECAO-DE-CONTEUDOS\\upload-de-arquivos\\dscatalog\\public_key.pem";

    // Só gera se ainda não existir
    if (!Files.exists(Paths.get(privateKeyPath)) || !Files.exists(Paths.get(publicKeyPath))) {
      RsaKeyGeneratorUtil.generateRsaKeyPair(privateKeyPath, publicKeyPath);
    }
  }
}