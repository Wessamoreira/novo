package webservice.aws.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;


/**
 * 
 * Classe com o objetivo de suprir as necessidades de armazenamento e distribuicao de arquivos <br>
 * utilizando o servico da Amazon AWS S3
 * 
 * @author gilberto.nery
 *
 */
public class ServidorArquivoOnlineS3RS implements Serializable {

	private static final long serialVersionUID = 8852122376217670742L;
	private AmazonS3 s3;
	
	// Nome padrao do bucket SEI seguindo padrao aws: https://docs.aws.amazon.com/pt_br/AmazonS3/latest/dev/BucketRestrictions.html
	private String bucket;
	
	//Por padrao a regiao escolhida sera a de Sao Paulo - America do Sul
	private final Regions regions = Regions.SA_EAST_1;
	
	public ServidorArquivoOnlineS3RS(String chaveDeAcesso, String senhaDeAcesso, String bucket) {
		setBucket(realizarAdequacaoDoNomeAoPadraoAmazonS3(bucket));
		AWSCredentials initialCredentials = new BasicAWSCredentials(chaveDeAcesso, senhaDeAcesso);
		this.s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(initialCredentials)).withRegion(regions).build();
	}


	/**
	 * o nome do bucket nao pode conter caracteres maiusculos
	 * 
	 * @param bucket
	 * @return
	 */
	private String realizarAdequacaoDoNomeAoPadraoAmazonS3(String bucket) {
		return bucket.toLowerCase();
	}
	
	
	/**
	 * Recupera o bucket 
	 * 
	 * @return
	 */
	private String getBucket() {

		List<Bucket> buckets = s3.listBuckets();
		for (Bucket b : buckets) {			
			if (b.getName().equals(this.bucket)) {
				return b.getName();
			}
		}
		return createBucket().getName();
	}

	
	/**
	 * Cria novo Bucket caso ele nao exista
	 * 
	 * @return
	 */
	protected Bucket createBucket() {
		return s3.createBucket(this.bucket);
	}

	
	/**
	 * Realiza upload do arquivo para Amazon AWS S3 no bucket padrao
	 * 
	 * @param nomeArquivo
	 * @param arquivo
	 * @throws ConsistirException
	 */
	public boolean consultarSeObjetoExiste(String nomeArquivo) throws ConsistirException {
		try {
		    return this.s3.doesObjectExist(getBucket(), nomeArquivo); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(e.getMessage());
		}
	}
	/**
	 * Realiza upload do arquivo para Amazon AWS S3 no bucket padrao
	 * 
	 * @param nomeArquivo
	 * @param arquivo
	 * @throws ConsistirException
	 */
	public void uploadObjeto(String nomeArquivo, File arquivo, boolean isDeletarArquivoExistente) throws ConsistirException {
		
		try {
			Boolean objetoExiste = this.s3.doesObjectExist(getBucket(), nomeArquivo);
			if (objetoExiste && !isDeletarArquivoExistente) {
				throw new Exception("Existe um arquivo com mesmo nome no diretório, favor renomear e postar novamente!");
			}else if (objetoExiste && isDeletarArquivoExistente) {
				deletarArquivo(nomeArquivo);
			}
			//System.out.println(" AMAZON S3 upload = ");			
			this.s3.putObject(getBucket(), nomeArquivo, arquivo);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(" AMAZON S3 ERRO 01 = " + e.getMessage());
			if (e.getMessage().contains("Existe um arquivo")) {
				throw new ConsistirException(e.getMessage());
			} else {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroAoSalvarArquivo") + " (" + e.getMessage().substring(0, e.getMessage().indexOf("(")) + ")");
			}
		}
	}
	
	
	/**
	 * Realiza upload do arquivo para Amazon AWS S3 no bucket padrao
	 * 
	 * @param nomeArquivo
	 * @param caminhoArquivo
	 * @throws ConsistirException
	 */
	public void uploadObjeto(String nomeArquivo, String caminhoArquivo) throws ConsistirException {
		
		File arquivo = new File(caminhoArquivo);
		try {
			Boolean objetoExiste = this.s3.doesObjectExist(getBucket(), nomeArquivo);
		    if (objetoExiste) {
		    	throw new Exception("Existe um arquivo com mesmo nome no diretorio, favor renomear e postar novamente!");
		    }			
		    this.s3.putObject(getBucket(), nomeArquivo, arquivo);
		    
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroUploadArquivo"));
		}
	}
	
	
	/**
	 * Download do arquivo @param (nomeArquivo) no local selecionado (@param caminhoParaSalvarArquivo)
	 * @param nomeArquivo
	 * @param caminhoParaSalvarArquivo
	 * @throws ConsistirException
	 */
	public void downloadObjeto(String nomeArquivo, String caminhoParaSalvarArquivo) throws ConsistirException {
		try {
			Boolean objetoExiste = this.s3.doesObjectExist(getBucket(), nomeArquivo);
			if(!objetoExiste) {
				throw new Exception("Arquivo não encontrado no servidor!");
			}
		    S3Object o = this.s3.getObject(this.bucket, nomeArquivo);
		    S3ObjectInputStream s3is = o.getObjectContent();
		    FileOutputStream fos = new FileOutputStream(new File(caminhoParaSalvarArquivo));
		    byte[] read_buf = new byte[1024];
		    int read_len = 0;
		    while ((read_len = s3is.read(read_buf)) > 0) {
		        fos.write(read_buf, 0, read_len);
		    }
		    s3is.close();
		    fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroDownloadArquivo"));
		}
	}
	
	
	/**
	 * Gera URL para download do arquivo @param (nomeArquivo)
	 * 
	 * @param nomeArquivo
	 * @return
	 * @throws ConsistirException
	 */
	public String getUrlParaDownloadDoArquivo(String nomeArquivo) throws ConsistirException {
		try {
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.bucket, nomeArquivo);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3.generatePresignedUrl(generatePresignedUrlRequest); 

			return url.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroGerarUrlDownloadArquivo"));
		}
	}
	
	public void deletarArquivoPorAlteracao(String nomeArquivo) throws ConsistirException {
		try {
		    deletarArquivo(nomeArquivo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroAoSalvarArquivo"));
		}
	}
	
	public void alterarEstruturaDeDiretorioArquivo(String origem, String destino, String nomeArquivo, PastaBaseArquivoEnum pastaBase, boolean isExcluirDiretorioOrigem) throws ConsistirException {
		try {
			origem+= File.separator + nomeArquivo;
			destino+= File.separator + nomeArquivo;
			if (!this.s3.doesObjectExist(getBucket(), origem)) {
				throw new StreamSeiException(pastaBase.name().replaceAll("TMP", "") +": Não foi localizado a origem do arquivo "+nomeArquivo+" para ser copiado! AWS S3.");
			}
			this.s3.copyObject(this.bucket, origem, this.bucket, destino);
			if(isExcluirDiretorioOrigem) {
				deletarArquivo(origem);	
			}
		} catch (StreamSeiException sse) {
			sse.printStackTrace();
			throw sse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroAoSalvarArquivo"));
		}
	}
	
	
	
	/**
	 * Deletar arquivo na Amazon AWS S3
	 * 
	 * @param nomeArquivo
	 * @throws ConsistirException
	 */
	public void deletarArquivo(String nomeArquivo) throws ConsistirException {
		try {
			this.s3.deleteObject(new DeleteObjectRequest(this.bucket, nomeArquivo));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ServidorArquivoAWS_erroGerarUrlDownloadArquivo"));
		}
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
} 