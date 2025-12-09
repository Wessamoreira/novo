package negocio.comuns.utilitarias;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;



public class CertificadoDigital {
	
	private static final String PROVIDER="BC";

//	public static ArquivoVO  realizarGeracaoCertificadoUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBase, UsuarioVO usuarioVO) throws Exception{
//		Uteis.checkState(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCNPJ()), "Não foi localizado o CNPJ para a instituição " + unidadeEnsinoVO.getRazaoSocial()+ ". Por favor verificar o Cadastro.");
//		PessoaVO pessoaVO = new PessoaVO();
//		pessoaVO.setCodigo(unidadeEnsinoVO.getCodigo());
//		pessoaVO.setNome(unidadeEnsinoVO.getRazaoSocial());
//		pessoaVO.setCPF(unidadeEnsinoVO.getCNPJ());
//		pessoaVO.setCidade(unidadeEnsinoVO.getCidade());
//		pessoaVO.setEndereco(unidadeEnsinoVO.getEndereco());
//		pessoaVO.setNumero(unidadeEnsinoVO.getNumero());
//		pessoaVO.setSetor(unidadeEnsinoVO.getSetor());
//		pessoaVO.setEmail(unidadeEnsinoVO.getEmail());
//		pessoaVO.setCEP(unidadeEnsinoVO.getCEP());
//		return realizarGeracaoCertificado(pessoaVO, unidadeEnsinoVO, caminhoBase, OrigemArquivo.CERTIFICADO_DIGITAL_INSTITUICAO, usuarioVO, "", "");
//	}
//	
//	public static ArquivoVO  realizarGeracaoCertificadoPessoa(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBase, UsuarioVO usuarioVO, String caminhoUnidadeCertificadora, String senhaUnidadeCertificadora) throws Exception{
//		Uteis.checkState(!Uteis.isAtributoPreenchido(pessoaVO.getCPF()), "Não foi localizado o Cpf para a pessoa " + pessoaVO.getNome()+ ". Por favor verificar o Cadastro.");
//		Uteis.checkState(!Uteis.verificaCPF(pessoaVO.getCPF()), "O cpf informado para a pessoa " + pessoaVO.getNome()+ " não é um cpf valido. Por favor verificar o Cadastro.");
//		return realizarGeracaoCertificado(pessoaVO, unidadeEnsinoVO, caminhoBase, OrigemArquivo.CERTIFICADO_DIGITAL_PESSOA, usuarioVO, caminhoUnidadeCertificadora, senhaUnidadeCertificadora);
//	}
	
//	private synchronized static ArquivoVO  realizarGeracaoCertificado(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBase, OrigemArquivo origemArquivo, UsuarioVO usuarioVO, String caminhoUnidadeCertificadora, String senhaUnidadeCertificadora) throws Exception{				 
//		String caminhoCompletoCertificado = caminhoBase+File.separator+PastaBaseArquivoEnum.CERTIFICADO.getValue()+File.separator+Uteis.removerMascara(pessoaVO.getCPF())+File.separator+Uteis.removerMascara(unidadeEnsinoVO.getCNPJ());
//		 File certificado = new File(caminhoCompletoCertificado);
//		 if(certificado.exists()) {
//			 caminhoCompletoCertificado += File.separator+Uteis.removerMascara(pessoaVO.getCPF());
//			 certificado = new File(caminhoCompletoCertificado+".cer");
//			 if(certificado.exists()) {
//				 certificado.delete();
//			 }
//			 certificado = new File(caminhoCompletoCertificado+".jks");
//			 if(certificado.exists()) {
//				 certificado.delete();
//			 }
//		 }else {
//			 certificado.mkdirs();
//			 caminhoCompletoCertificado += File.separator+Uteis.removerMascara(pessoaVO.getCPF());
//		 }
//		 Security.addProvider(new BouncyCastleProvider());//para tomar el algoritmo de encriptamiento de BC
//		 Security.insertProviderAt(new BouncyCastleProvider(), 10);//para tomar el algoritmo de encriptamiento de BC
//		
//		 KeyPair rootCAKeyPair = generateKeyPair(Uteis.removerMascara(pessoaVO.getCPF()));		
//		 
//		 X509Certificate unidadeCertificadora = null;
//		 PrivateKey privateKey =  null;
//		 if(Uteis.isAtributoPreenchido(caminhoUnidadeCertificadora)) {
//			 privateKey = obterChavePrivada(caminhoUnidadeCertificadora, senhaUnidadeCertificadora);		
//			 if(caminhoUnidadeCertificadora.endsWith(".pfx")) {
//				 KeyPair rootKeyPair = generateKeyPair(senhaUnidadeCertificadora);
//				 PessoaVO pessoaVO2 = new PessoaVO();
//				 pessoaVO2.setCodigo(unidadeEnsinoVO.getCodigo());
//				 pessoaVO2.setNome(unidadeEnsinoVO.getRazaoSocial());
//				 pessoaVO2.setCPF(unidadeEnsinoVO.getCNPJ());
//				 pessoaVO2.setCidade(unidadeEnsinoVO.getCidade());
//				 pessoaVO2.setEndereco(unidadeEnsinoVO.getEndereco());
//				 pessoaVO2.setNumero(unidadeEnsinoVO.getNumero());
//				 pessoaVO2.setSetor(unidadeEnsinoVO.getSetor());
//				 pessoaVO2.setEmail(unidadeEnsinoVO.getEmail());
//				 pessoaVO2.setCEP(unidadeEnsinoVO.getCEP());
//				 X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(  
//						 obterEmissor(pessoaVO2, unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial()+":"+Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), null), 
//				         new BigInteger(Uteis.removerMascara(pessoaVO.getCPF())), 
//				         new Date(), // start of validity  
//				         UteisData.adicionarDiasEmData(new Date(), 73000), //end of certificate validity  
//				         obterSujeito(pessoaVO2,unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial()+":"+Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), null), // subject name of certificate  
//				         rootKeyPair.getPublic()); // public key of certificate
//				 builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign));  
//			     builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));  
//			     unidadeCertificadora = new JcaX509CertificateConverter().getCertificate(builder  
//							 .build(new JcaContentSignerBuilder("SHA1withRSA").setProvider(PROVIDER). 	        		 
//									 build(rootKeyPair.getPrivate()))); 			 
//			 }else {
//			 unidadeCertificadora = carregarCertificadoRaiz(caminhoUnidadeCertificadora, senhaUnidadeCertificadora);			 			 
//		 }
//		 }
//		 
//		 X509v3CertificateBuilder builder = null;
//		 if(unidadeCertificadora == null) {
//			 builder = new JcaX509v3CertificateBuilder(  
//	         obterEmissor(pessoaVO, unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial()+":"+Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), unidadeCertificadora), //emisor 
//	         new BigInteger(Uteis.removerMascara(pessoaVO.getCPF())), 
//	         new Date(), // start of validity  
//	         UteisData.adicionarDiasEmData(new Date(), 73000), //end of certificate validity  
//	         obterSujeito(pessoaVO, unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial(), 
//	        		 unidadeCertificadora), // subject name of certificate  
//	         rootCAKeyPair.getPublic()); // public key of certificate  
//		 }else {
//			 builder = new JcaX509v3CertificateBuilder(  
//					 unidadeCertificadora, //emisor 
//			         new BigInteger(Uteis.removerMascara(pessoaVO.getCPF())), 
//			         new Date(), // start of validity  
//			         UteisData.adicionarDiasEmData(new Date(), 73000), //end of certificate validity  
//			         obterSujeito(pessoaVO, unidadeEnsinoVO.getRazaoSocial(), unidadeEnsinoVO.getRazaoSocial(), 
//			        		 unidadeCertificadora), // subject name of certificate  
//			         rootCAKeyPair.getPublic()); // public key of certificate
//		 }
//		 
//		 
//		 X509Certificate certificadoPessoa = null;
//		 
//		 if(unidadeCertificadora != null) {	
//			 JcaX509ExtensionUtils jcaX509ExtensionUtils = new JcaX509ExtensionUtils();			 
//			 builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
//			 builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
//			 builder.addExtension(Extension.authorityKeyIdentifier, false,  jcaX509ExtensionUtils.createAuthorityKeyIdentifier(unidadeCertificadora));			 
//		 	 builder.addExtension(Extension.subjectKeyIdentifier, false, jcaX509ExtensionUtils.createSubjectKeyIdentifier(rootCAKeyPair.getPublic()));
//			 
//		 	 
//			 certificadoPessoa = new JcaX509CertificateConverter().getCertificate(builder  
//					 .build(new JcaContentSignerBuilder("SHA1withRSA").setProvider(PROVIDER). 	        		 
//							 build(rootCAKeyPair.getPrivate()))); 			 
//			 saveToFile(certificadoPessoa.getEncoded(), caminhoCompletoCertificado+".cer");	   
//			 gerarArquivoJKS(pessoaVO, caminhoCompletoCertificado, rootCAKeyPair.getPrivate(), certificadoPessoa);
//
//		 }else {
//			 builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign));
//			 builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));
//			 certificadoPessoa = new JcaX509CertificateConverter().getCertificate(builder  
//					 .build(new JcaContentSignerBuilder("SHA1withRSA").setProvider(PROVIDER). 	        		 
//							 build(rootCAKeyPair.getPrivate()))); 			 
//			 saveToFile(certificadoPessoa.getEncoded(), caminhoCompletoCertificado+".cer");	   
//			 gerarArquivoJKS(pessoaVO, caminhoCompletoCertificado, rootCAKeyPair.getPrivate(), certificadoPessoa);
//		 }
//	      
//	     validarCertificado(caminhoCompletoCertificado+".jks", Uteis.removerMascara(pessoaVO.getCPF()));
//	     saveToFiletxt(caminhoCompletoCertificado+".txt", Uteis.removerMascara(pessoaVO.getCPF()));
//	     
//	     return  criarArquivoVO(pessoaVO, new File(caminhoCompletoCertificado+".jks"), origemArquivo,unidadeEnsinoVO, usuarioVO);
//	}
	
	private static void gerarArquivoJKS(PessoaVO pessoaVO, String caminhoCompletoCertificado, PrivateKey privateKey, X509Certificate certificadoPessoa) throws Exception {
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	     KeyStore ks = KeyStore.getInstance("PKCS12");
	     ks.load(null);
	     ks.setKeyEntry(Uteis.removerCaracteresEspeciais3(Uteis.removerAcentos(pessoaVO.getNome())).toUpperCase()+" - "+Uteis.removerMascara(pessoaVO.getCPF()), (Key)privateKey, Uteis.removerMascara(pessoaVO.getCPF()).toCharArray(), new java.security.cert.Certificate[]{certificadoPessoa});
	     ks.store(bos, Uteis.removerMascara(pessoaVO.getCPF()).toCharArray());
	     bos.close();
	     saveToFile(bos.toByteArray(), caminhoCompletoCertificado+".jks");	
	}
	
	private static ArquivoVO criarArquivoVO(PessoaVO pessoaVO, File certificado, OrigemArquivo origemArquivo, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		ArquivoVO arquivoVO =  new ArquivoVO();
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CERTIFICADO);
		arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.CERTIFICADO.getValue()+File.separator+Uteis.removerMascara(pessoaVO.getCPF())+File.separator+Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()));
		arquivoVO.setArquivoExisteHD(true);
		arquivoVO.setArquivoEstaNoDiretorioFixo(true);
		arquivoVO.setNome(certificado.getName());
		arquivoVO.setDescricao(Uteis.removerCaracteresEspeciais3(Uteis.removerAcentos(pessoaVO.getNome())).toUpperCase()+" - "+Uteis.removerMascara(pessoaVO.getCPF())+".jks");
		arquivoVO.setSituacao(SituacaoArquivo.ATIVO.getValor());
		arquivoVO.setCodOrigem(pessoaVO.getCodigo());
		arquivoVO.setDataUpload(new Date());
		arquivoVO.setExtensao(".jks");
		arquivoVO.setManterDisponibilizacao(true);
		arquivoVO.setOrigem(origemArquivo.getValor());
		if(origemArquivo.equals(OrigemArquivo.CERTIFICADO_DIGITAL_PESSOA)) {
			arquivoVO.setPessoaVO(pessoaVO);
		}
		arquivoVO.getResponsavelUpload().setCodigo(usuarioVO.getCodigo());
		arquivoVO.getResponsavelUpload().setNome(usuarioVO.getNome());		
		return arquivoVO;
	}
	
	private static X500Name obterEmissor(PessoaVO pessoaVO, String nomeO, String nomeOu, String cn, X509Certificate root){
		X500NameBuilder issuerBuilder = new X500NameBuilder(X500Name.getDefaultStyle());		
		issuerBuilder.addRDN(BCStyle.OU, nomeO);
		issuerBuilder.addRDN(BCStyle.O, nomeOu);
		issuerBuilder.addRDN(BCStyle.CN, cn);
		issuerBuilder.addRDN(BCStyle.C, "BR");
        return issuerBuilder.build();
	}
	
	private static X500Name obterSujeito(PessoaVO pessoaVO, String nomeO, String nomeOu, X509Certificate root){
		X500NameBuilder issuerBuilder = new X500NameBuilder(X500Name.getDefaultStyle());
		if(root != null) {
			issuerBuilder.addRDN(BCStyle.O, root.getSubjectDN().getName());			
			issuerBuilder.addRDN(BCStyle.OU, root.getSubjectDN().getName());			
		}else {
			issuerBuilder.addRDN(BCStyle.O, nomeO.toUpperCase());
			issuerBuilder.addRDN(BCStyle.OU, nomeOu.toUpperCase());
		}
		issuerBuilder.addRDN(BCStyle.CN, pessoaVO.getNome().toUpperCase()+":"+Uteis.removerMascara(pessoaVO.getCPF()));
		issuerBuilder.addRDN(BCStyle.L, pessoaVO.getCidade().getNome().toUpperCase());
		issuerBuilder.addRDN(BCStyle.ST,pessoaVO.getCidade().getEstado().getSigla().toUpperCase());
		issuerBuilder.addRDN(BCStyle.C, "BR");
		issuerBuilder.addRDN(BCStyle.SERIALNUMBER, "/"+Uteis.removerMascara(pessoaVO.getCPF()));
        return issuerBuilder.build();
	}
	

	private static KeyPair generateKeyPair(String senha) throws NoSuchAlgorithmException, NoSuchProviderException {  
	     KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA",PROVIDER); 
	     kpGen.initialize(2048, new SecureRandom(senha.getBytes()));  //aqui se genera el pass aleatorio ?? no estoy seguro	     
	     return kpGen.generateKeyPair();  
	}  
	
	private static void saveToFile(byte encoded[] , String filePath) throws IOException, CertificateEncodingException {  
	     FileOutputStream fileOutputStream = new FileOutputStream(filePath);  
	     fileOutputStream.write(encoded);  
	     fileOutputStream.flush();  
	     fileOutputStream.close();  
	}
	
	private static void  saveToFiletxt(String caminhoSenha, String senha) {
		
        File archivo = new File(caminhoSenha);
        BufferedWriter bw = null;
        if(!archivo.exists()) {
        	 try {
				bw = new BufferedWriter(new FileWriter(archivo));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
             try {
				bw.write(senha);
				bw.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        }else {
        	System.out.println("El archivo ya se encuentra creado");
        }
        
	}
	
	public static X509Certificate carregarCertificadoRaiz(String caminhoCertificado, String senha) throws Exception {		
		InputStream fs = new FileInputStream(caminhoCertificado);
		KeyStore rep = KeyStore.getInstance("PKCS12");
		rep.load(fs, senha.toCharArray());
		Enumeration<String> aliasesEnum = rep.aliases();
		String aliasChave = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChave = (String) aliasesEnum.nextElement();
			if (rep.isKeyEntry(aliasChave)) {
				break;
			}
		}
		return (X509Certificate) rep.getCertificate(aliasChave);
	}
	
	public static void validarCertificado(String caminhoCertificado, String senha) throws Exception {		
		InputStream fs = new FileInputStream(caminhoCertificado);
		KeyStore rep = KeyStore.getInstance("PKCS12");
		rep.load(fs, senha.toCharArray());
		Enumeration<String> aliasesEnum = rep.aliases();
		String aliasChave = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChave = (String) aliasesEnum.nextElement();
			if (rep.isKeyEntry(aliasChave)) {
				break;
			}
		}		
		X509Certificate cert = (X509Certificate) rep.getCertificate(aliasChave);
		cert.checkValidity(new Date());
		
	}
	
	public static PrivateKey obterChavePrivada(String caminhoCertificado, String senha) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore rep = KeyStore.getInstance("PKCS12");
		InputStream fs = new FileInputStream(caminhoCertificado);
		rep.load(fs, senha.toCharArray());
		Enumeration<String> aliasesEnum = rep.aliases();
		String aliasChave = "";
		while (aliasesEnum.hasMoreElements()) {
			aliasChave = (String) aliasesEnum.nextElement();
			if (rep.isKeyEntry(aliasChave)) {
				Key key = rep.getKey(aliasChave, senha.toCharArray()); 
				if(key != null) {
					return (PrivateKey) key;
				}
			}
		}
		return null;
	}
	
		
}