package negocio.comuns.utilitarias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.financeiro.ContaPagarVO;



public class UteisFTP {

	private String servidor;
	private String usuario;
	private String senha;
	private FTPClient ftpClient;
	private InputStream input;
			
	/**
	 * Método construtor da classe TdvFTP.
	 * @param servidorFTP String - IP do servidor FTP.
	 * @param usuario String - Usuário do servidor FTP.
	 * @param senha String - Senha do servidor FTP.
	 * @param FTPClient FTPClient - FTPClient.
	 *
	 * */	
	
	public UteisFTP(String servidorFTP, String usuario, String senha, FTPClient ftpClient) {		
		this.servidor = servidorFTP;
		this.usuario = usuario;
		this.senha = senha;
		this.ftpClient = ftpClient;
	}
	
	/**
	 * Método que realiza a conexão com o FTP.
	 * @return boolean - Retorna true se a conexão foi realizada com sucesso e false caso contrário.
	 * @throws Exception 
	 * 
	 * */	
	public void  connect() throws Exception {
		try {
			 ftpClient.connect(this.servidor);
		} catch (Exception e) {
			disconnectFTP();
			throw new Exception("Não foi possivel  conectar ao Servidor FTP . Endereço Servidor Incorreto ."+e.getMessage());
		}
			
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			try {
				ftpClient.login(this.usuario, this.senha);			
			} catch (Exception e) {
				disconnectFTP();
				throw new Exception("Conexão recusada verifique os dados de Conexão . ");
			}
			
			if (!new Integer(ftpClient.getReplyCode()).equals(FTPReply.USER_LOGGED_IN)) {
				disconnectFTP();
				throw new Exception("Não foi possivel fazer autenticação no FTP  . Usuario ou senha incorretos ");
			}
		} else {
			disconnectFTP();
			throw new Exception("Conexão recusada verifique os dados de Conexão . ");
		}
   
	}
	
	
	/**
	 * Método que retorna apenas uma lista com o nome dos diretórios e arquivos do FTP.
	 * @param  diretorio String - Nome do diretório a ser listado.
	 * @return String[] - Retorna uma lista de Strings com o nome dos arquivos e diretórios contidos no diretório informado.
	 * @throws Exception 
	 * 
	 * */	
	public String[] getNameDirs(String diretorio) throws Exception {
	    String[] nameDirs = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();  
		    ftpClient.changeWorkingDirectory(diretorio);  
			nameDirs = ftpClient.listNames();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			disconnectFTP();
		}		
		return nameDirs;
	}
	
	/**
	 * 
	 * Método que devolve diversas propriedades dos arquivos e diretórios do FTP tais como: permissões, tamanho dos arquivos e diretórios e etc... É mais completo que o getNameDirs.
	 * @param  diretorio String - Nome do diretório a ser listado.
	 * @throws SocketException
	 * @throws IOException
	 * @return FTPFile[] - Retorna uma lista do tipo FTPFile com o nome dos arquivos e diretórios contidos no diretório informado.
	 * @throws Exception 
	 * 
	 * */		
	public FTPFile[] getConfigFTPFiles(String diretorio) throws Exception {				
		FTPFile[] filesConfig = null;
	     try {
	    	connect(); 
	    	ftpClient.enterLocalPassiveMode();   
			ftpClient.changeWorkingDirectory(diretorio);
			filesConfig = ftpClient.listFiles();  
	     } catch (IOException e) {
		    	e.printStackTrace(); 
		}finally {
			disconnectFTP();
		}		 		
		return filesConfig;
	}
	
	
	/**
	 * Envia um arquivo para o servidor FTP.	
	 * @description Envia um arquivo para o servidor FTP.
	 * @param caminhoArquivo String - Caminho aonde está localizado o arquivo a ser enviado.
	 * @param  arquivo String - Nome do arquivo.
	 * @return boolean - Retorna true se o arquivo foi enviado e false caso contrário.
	 * @throws Exception 
	 * 
	 * */
	public synchronized void realizarEnvioArquivoFtp(ConfiguracaoGeralSistemaVO conSistemaVO, ArquivoVO arquivo )throws Exception {	 	
		
		StringBuilder file = new StringBuilder().append(conSistemaVO.getLocalUploadArquivoFixo()).append(File.separator).append(arquivo.getPastaBaseArquivo()).append(File.separator).append(arquivo.getNome());
		FileInputStream	arqEnviar = new FileInputStream(file.toString());	
		ftpClient.enterLocalPassiveMode();		
		if (ftpClient.storeFile(arquivo.getNome(), arqEnviar)) {
			arqEnviar.close();					
		}else {
			arqEnviar.close();          
			throw new ConsistirException("Erro ao enviar arquivo verifique as permissões de acesso ao FTP .");
		}     
	}

	
	
	
	/**
	 * Obtém um arquivo do servidor FTP.	
	 * @description Obtém um arquivo do servidor FTP.
	 * @param  arquivo String - Nome do arquivo a ser baixado.
	 * @return void 
	 * @throws Exception 
	 * 
	 * */
	public void getFile(String arquivo) throws Exception {
		try {
			connect();
			ftpClient.enterLocalPassiveMode(); 
			ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE );
			OutputStream os = new FileOutputStream(arquivo);  
			ftpClient.retrieveFile(arquivo, os );  
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			disconnectFTP();
		}   		
	}
	
	/**
	 * Fecha a conexão com o FTP.	 
	 * @description Faz a desconexão com o FTP.
	 * @return void 
	 * 
	 * */
	public void disconnectFTP() {
		try {
			this.ftpClient.logout();
			this.ftpClient.disconnect();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeDirectory(String directory) {		
		try {
			connect();
			this.ftpClient.removeDirectory(directory);
			this.ftpClient.deleteFile(directory);
			disconnectFTP();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	
	public boolean sendFTPFile(String conSistemaVO, String arquivoMarc21VO)
			throws Exception {
		FileInputStream arqEnviar = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();

			arqEnviar = new FileInputStream(conSistemaVO);			
			boolean retorno = ftpClient.changeWorkingDirectory("/full/ArquivosEbsco");
			//boolean retornoUpdate = ftpClient.changeWorkingDirectory("update/");
			if (!retorno) {
				throw new Exception("Diretorio não encontrado . ");
			}
			if (ftpClient.storeFile(arquivoMarc21VO, arqEnviar)) {
				arqEnviar.close();
				disconnectFTP();
				return true;
			}else {
				arqEnviar.close();
				disconnectFTP();
				throw new ConsistirException("Erro ao enviar arquivo  .");
			 
			}
		} catch (UnknownHostException ex) {
			throw new ConsistirException("Erro ao conectar Servidor Verifique sua Internet .");
		} catch (Exception e) {
			throw e;
		}
		   
			
	
      
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}
	
	
}
