package negocio.comuns.utilitarias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;

import javax.crypto.Cipher;

import com.google.gson.annotations.JsonAdapter;

public class EncriptaDecriptaRSA {
	public static final String ALGORITHM = "RSA";
	  
	  /**
	   * Local da chave privada no sistema de arquivos.
	   */
	  public static final String PATH_CHAVE_PRIVADA = "D:/chave/private.key";
	  
	  /**
	   * Local da chave pública no sistema de arquivos.
	   */
	  public static final String PATH_CHAVE_PUBLICA = "D:/chave/public.key";
	  
	  /**
	   * Gera a chave que contém um par de chave Privada e Pública usando 1025 bytes.
	   * Armazena o conjunto de chaves nos arquivos private.key e public.key
	   */
	  public static void geraChave() {
	    try {
	      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	      keyGen.initialize(1024);
	      final KeyPair key = keyGen.generateKeyPair();
	  
	      File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
	      File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);
	  
	      // Cria os arquivos para armazenar a chave Privada e a chave Publica
	      if (chavePrivadaFile.getParentFile() != null) {
	        chavePrivadaFile.getParentFile().mkdirs();
	      }
	       
	      chavePrivadaFile.createNewFile();
	  
	      if (chavePublicaFile.getParentFile() != null) {
	        chavePublicaFile.getParentFile().mkdirs();
	      }
	       
	      chavePublicaFile.createNewFile();
	  
	      // Salva a Chave Pública no arquivo
	      ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
	          new FileOutputStream(chavePublicaFile));
	      chavePublicaOS.writeObject(key.getPublic());
	      chavePublicaOS.close();
	  
	      // Salva a Chave Privada no arquivo
	      ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
	          new FileOutputStream(chavePrivadaFile));
	      chavePrivadaOS.writeObject(key.getPrivate());
	      chavePrivadaOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  
	  }
	  
	  /**
	   * Verifica se o par de chaves Pública e Privada já foram geradas.
	   */
	  public static boolean verificaSeExisteChavesNoSO() {
	  
	    File chavePrivada = new File(PATH_CHAVE_PRIVADA);
	    File chavePublica = new File(PATH_CHAVE_PUBLICA);
	  
	    if (chavePrivada.exists() && chavePublica.exists()) {
	      return true;
	    }
	     
	    return false;
	  }
	  
	  /**
	   * Criptografa o texto puro usando chave pública.
	   */
	  public static byte[] criptografa(String texto, PublicKey chave) {
	    byte[] cipherText = null;
	     
	    try {
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // Criptografa o texto puro usando a chave Púlica
	      cipher.init(Cipher.ENCRYPT_MODE, chave);
	      cipherText = cipher.doFinal(texto.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	     
	    return cipherText;
	  }
	  
	  /**
	   * Decriptografa o texto puro usando chave privada.
	   */
	  public static String decriptografa(byte[] texto, PrivateKey chave) {
	    byte[] dectyptedText = null;
	     
	    try {
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // Decriptografa o texto puro usando a chave Privada
	      cipher.init(Cipher.DECRYPT_MODE, chave);
	      dectyptedText = cipher.doFinal(texto);
	  
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  
	    return new String(dectyptedText);
	  }
	  
	  /**
	   * Testa o Algoritmo
	   */
	  public static void main(String[] args) {
	  
	    try {
	  
	      // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
	      if (!verificaSeExisteChavesNoSO()) {
	       // Método responsável por gerar um par de chaves usando o algoritmo RSA e
	       // armazena as chaves nos seus respectivos arquivos.
	        geraChave();
	      }
	      
	      final String msgOriginal = "0000000000000001";
	      ObjectInputStream inputStream = null;
	  
	      // Criptografa a Mensagem usando a Chave Pública
	      inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA));
	      final PublicKey chavePublica = (PublicKey) inputStream.readObject();
	      final byte[] textoCriptografadoByte = criptografa(msgOriginal, chavePublica);
	      final String textoCriptografado = new Decoder.BASE64Encoder().encode(textoCriptografadoByte);
//	      final String textoCriptografado = "NftD0C9p9QVusGLlSTssvNDAHPTvtKtbffW2GGuAbOGbP9lOYMnSqzY2qG35oPSZaUQJOG0/gl1kaHvjsvDvm2Ko1d8LlPVh/wxgPBCcxl+8c8Nvq/9EHTm5itwxLT22drZbgbbBriPcEJhEsvQ6dhAcUH5GM/zF3cl8BxDvkfo=";
	      byte[] b = new Decoder.BASE64Decoder().decodeBuffer(textoCriptografado);
	  
	      // Decriptografa a Mensagem usando a Chave Pirvada
	      inputStream = new ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA));
	      final PrivateKey chavePrivada = (PrivateKey) inputStream.readObject();
	      final String textoPuro = decriptografa(b, chavePrivada);
	  
	      // Imprime o texto original, o texto criptografado e 
	      // o texto descriptografado.
	      System.out.println("Mensagem Original: " + msgOriginal);
	      System.out.println("Mensagem Criptografada: " +textoCriptografado.toString());
	      System.out.println("Mensagem Decriptografada: " + textoPuro);
	  
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
