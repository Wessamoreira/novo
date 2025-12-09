package controle.arquitetura;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.faces. application.FacesMessage;
import jakarta.faces. context.FacesContext;
import jakarta.mail.internet.MimeUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
//import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import relatorio.arquitetura.SuperRelatorioSV;


/**
 * Servlet implementation class DownloadSV
 */
@WebServlet(name = "DownloadSV")
public class DownloadSV extends SuperRelatorioSV {

	public static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadSV() {
		super();
	}

	private ArquivoVO pesquisarArquivoVO(int codigo) throws Exception {
		return getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
	}

	private Optional<ArquivoVO> pesquisarArquivoPorNomeUnico(String nome) throws Exception {
		List<ArquivoVO> consultarPorNome = getFacadeFactory().getArquivoFacade().consultarPorNome(nome, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
		return consultarPorNome.stream().findFirst();
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		ByteArrayOutputStream arrayOutputStream = null;
		OutputStream out = null;
		byte buffer[] = new byte[4096];
		File file = null;

		ArquivoVO arquivoVO = null;
		Optional<ArquivoVO> arquivoOptional = Optional.empty();
		LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
		
		/*
		 * Validação para evitar valores nulos.
		 */
		
        Integer codigoArquivo = 0;
        
		if (request.getParameter("tokenAutenticacaoMobileOtm") != null) {
			try {
				if (getFacadeFactory().getUsuarioFacade().validarTokenAplicativo(request.getParameter("tokenAutenticacaoMobileOtm"))) {
					if (request.getParameter("codigoArquivo") != null) {
						codigoArquivo = request.getParameter("codigoArquivo") != null ? Integer.parseInt(request.getParameter("codigoArquivo")) : 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			codigoArquivo = (Integer) request.getAttribute("codigoArquivo") != null ? (Integer) request.getAttribute("codigoArquivo") : 0;
			if (!Uteis.isAtributoPreenchido(codigoArquivo) && request.getParameter("codigoArquivo") != null) {
				codigoArquivo = request.getParameter("codigoArquivo") != null ? Integer.parseInt(request.getParameter("codigoArquivo")) : 0;
			}
		}
		
		
		String nomeArquivo = (String) request.getSession().getAttribute("nomeArquivo") != null ? (String) request.getSession().getAttribute("nomeArquivo") : "";
		String pastaBaseArquivo = (String) request.getSession().getAttribute("pastaBaseArquivo") != null ? (String) request.getSession().getAttribute("pastaBaseArquivo") : "";
		String nomeReal = (String) request.getSession().getAttribute("nomeReal") != null ? (String) request.getSession().getAttribute("nomeReal") : "";
		Boolean deletarArquivo = (Boolean) request.getSession().getAttribute("deletarArquivo") != null ? (Boolean) request.getSession().getAttribute("deletarArquivo") : false;
        byte fileBuffer[] = (byte[]) request.getSession().getAttribute("file");
		try {
			
        	if(fileBuffer != null && nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {
				request.getSession().removeAttribute("file");				        		
        		String userAgent = request.getHeader("User-Agent");
    			if(Uteis.ClienteMovel(userAgent)){
    				response.setHeader("Content-Disposition", "inline;filename=" + nomeArquivo);    			
    			}else {
    				response.setHeader("Content-Disposition", "attachment;filename=" + nomeArquivo);    				
    			}
        		out = response.getOutputStream();
        		out.write(fileBuffer);
                out.flush();
        		return;
        	}
			if (!Strings.isNullOrEmpty(nomeArquivo)) {
				arquivoOptional = this.pesquisarArquivoPorNomeUnico(nomeArquivo);
			}

			if (codigoArquivo > 0) {
				arquivoVO = pesquisarArquivoVO(codigoArquivo);
			} else if (Objects.nonNull(request.getSession().getAttribute("arquivoVO"))) {
				arquivoVO = (ArquivoVO) request.getSession().getAttribute("arquivoVO");
				request.getSession().removeAttribute("arquivoVO");            
			}else if(request.getAttribute("arquivoVO") != null && request.getAttribute("arquivoVO") instanceof ArquivoVO){
            	arquivoVO = (ArquivoVO)request.getAttribute("arquivoVO");
				request.getSession().removeAttribute("arquivoVO");
			} else if (arquivoOptional.isPresent()) {
				arquivoVO = arquivoOptional.get();
			} else {
				arquivoVO = new ArquivoVO();
				arquivoVO.setDescricao(nomeReal);
				arquivoVO.setNome(nomeArquivo);
				request.getSession().removeAttribute("nomeArquivo");
				arquivoVO.setPastaBaseArquivo(pastaBaseArquivo);
				request.getSession().removeAttribute("pastaBaseArquivo");
			}
//			if(arquivoVO != null && !arquivoVO.getPastaBaseArquivo().contains("TMP") && arquivoVO.getServidorArquivoOnline().getValor().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
//				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
//				ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
//				String nomeArquivoUsar = arquivoVO.getDescricao().contains(".") ? arquivoVO.getDescricao() : arquivoVO.getDescricao() + (arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf("."), arquivoVO.getNome().length()));
//				if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
//					response.sendRedirect(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));					
//				}else {
//					nomeArquivoUsar =  arquivoVO.getDescricao();
//					if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
//						response.sendRedirect(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
//					}else {
//						nomeArquivoUsar =  arquivoVO.getDescricao()+"."+arquivoVO.getExtensao().toLowerCase();
//						if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
//							response.sendRedirect(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
//						} else {
//							if((FacesContext.getCurrentInstance()) != null) {
//								FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Não foi encontrado no repositório da AMAZON o aquivo no caminho "+arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)+".");
//								(FacesContext.getCurrentInstance()).addMessage(null, msg);
//								throw new Exception(msg.getDetail());
//							} else {
//								FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Não foi encontrado no repositório da AMAZON o aquivo no caminho "+arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)+".");
//								(FacesContext.getCurrentInstance()).addMessage(null, msg);
//								throw new Exception(msg.getDetail());
//							}
//						}
//					}
//				}
//				servidorArquivoOnlineS3RS = null;
//				configuracaoGeralSistemaVO = null;
//				return;
//			}
			request.getSession().removeAttribute("deletarArquivo");
			String nomeFinal = getNomeFinal(arquivoVO);
			String fileName = request.getHeader("user-agent").contains("MSIE") ? URLEncoder.encode(nomeFinal, "utf-8") : MimeUtility.encodeWord(nomeFinal);
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			setContentTypePorNome(response, nomeFinal);
			arrayOutputStream = new ByteArrayOutputStream();
			FileInputStream fi = null;
			file = new File(arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
			//Validacao para verificar se o arquivo existe no caminho salvo na tabela arquivo pois existe arquivo que nao e salvo o caminho completo sendo assim informado na parametro P pastaBaseArquivo que deve ser usado 
			if(!file.exists() && arquivoVO.getNome().contains(".")){
				String extensao = arquivoVO.getNome().substring(arquivoVO.getNome().indexOf("."));
				String extensaoFinal = extensao.toUpperCase();
				String nomeArquivoFinal = arquivoVO.getNome().replaceAll(extensao, extensaoFinal);
				file = new File(arquivoVO.getPastaBaseArquivo() + File.separator + nomeArquivoFinal);
			}
			if(!file.exists() && Uteis.isAtributoPreenchido(pastaBaseArquivo)){
				file = new File(pastaBaseArquivo + File.separator + arquivoVO.getNome());
			}
			if(!file.exists() && ((!Uteis.isSistemaOperacionalWindows() && !arquivoVO.getPastaBaseArquivo().startsWith("/"))
					|| (Uteis.isSistemaOperacionalWindows() && !arquivoVO.getPastaBaseArquivo().contains(":")))) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
				file = new File(config.getLocalUploadArquivoFixo() + File.separator + arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
			}
			try {
				buffer = new byte[4096];
				int bytesRead = 0;
				fi = new FileInputStream(file);
				while ((bytesRead = fi.read(buffer)) != -1) {
					arrayOutputStream.write(buffer, 0, bytesRead);
				}
				arrayOutputStream.close();
				fi.close();
			} catch (Exception e) {
				throw e;
			} finally {
				fi = null;
				buffer = null;
			}

			arrayOutputStream.flush();
			out = response.getOutputStream();
			out.write(arrayOutputStream.toByteArray());
			out.flush();

		} catch (ConsistirException e) {		
			if(loginControle != null) {
				loginControle.setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			try {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
				response.sendRedirect(configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/" + "paginaErroCustomizada.xhtml");
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} catch (Exception e) {
        	e.getMessage();           	        	
			if(loginControle != null) {
				loginControle.setMensagemDetalhada("msg_erro", "Arquivo não localizado para download.", Uteis.ERRO);				
			}
			try {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
				response.sendRedirect(configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/" + "paginaErroCustomizada.xhtml");
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				arrayOutputStream = null;
				out = null;
				buffer = null;
				if (deletarArquivo != null && deletarArquivo) {
					file.delete();
				}
			} catch (IOException ex) {
				Logger.getLogger(DownloadSV.class.getName()).log(Level.SEVERE, null, ex);
			}

			arrayOutputStream = null;

		}
	}

	private String getNomeFinal(ArquivoVO arquivoVO) {
		String nome = Strings.isNullOrEmpty(arquivoVO.getDescricao()) 
				|| arquivoVO.getDescricao().length() > 500 ||
				arquivoVO.getDescricao().contains("\n") ? arquivoVO.getNome() : arquivoVO.getDescricao();
		if(nome.contains(".")){
			if (Uteis.isAtributoPreenchido(arquivoVO.getNome()) && arquivoVO.getNome().chars().filter(c -> c == 46).count() > 1) {
				String extensao = Uteis.isAtributoPreenchido(arquivoVO.getExtensao()) ? 
						"." + arquivoVO.getExtensao().trim() : arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf("."), arquivoVO.getNome().length());
				nome = Uteis.removeCaractersEspeciais(arquivoVO.getDescricao());
				return  nome.trim() + extensao;
			}
			return  nome.trim();
		}else if(Uteis.isAtributoPreenchido(arquivoVO.getExtensao())){
			return nome.trim() + "." + arquivoVO.getExtensao().trim();
		}else{
			return nome.trim() + arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf("."), arquivoVO.getNome().length());
		}
	}

	private void setContentTypePorNome(HttpServletResponse response, String nome) {
		if (nome.endsWith(".pdf")) {
			response.setContentType("application/pdf");
		} else if (nome.endsWith(".xls")) {
			response.setContentType("application/vnd.ms-excel");
		} else if (nome.endsWith(".xlsx")) {
			response.setContentType("application/vnd.openxmlformats");
		} else if (nome.endsWith(".zip")) {
			response.setContentType("application/x-zip-compressed");
		} else if (nome.endsWith(".png")) {
			response.setContentType("application/png");
		} else if (nome.endsWith(".doc")) {
			response.setContentType("application/msword");
		} else if (nome.endsWith(".txt")) {
			response.setContentType("text/plain");
		} else {
			response.setContentType("application/octet-stream");
		}
	}

	@SuppressWarnings("unused")
	private ByteArrayOutputStream criarArray(File arquivo, String urlArquivo) throws Exception {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		FileInputStream fi = null;
		byte buffer[] = null;
		try {
			buffer = new byte[4096];
			int bytesRead = 0;
			// System.out.print("====================== ARQUIVO AQUI
			// ========================");
			//// System.out.print(arquivo.getAbsolutePath());
			// System.out.print("============================================================");
			fi = new FileInputStream(arquivo.getAbsolutePath());
			while ((bytesRead = fi.read(buffer)) != -1) {
				arrayOutputStream.write(buffer, 0, bytesRead);
			}
			arrayOutputStream.close();
			fi.close();
			return arrayOutputStream;
		} catch (Exception e) {
			throw e;
		} finally {
			fi = null;
			buffer = null;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<String> urlsPermitidas = Arrays.asList("documentoAssinado.xhtml", "diploma.xhtml", "historico.xhtml");
			 LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
			 Boolean isSeiSignature = Boolean.valueOf(request.getParameter("projetoSeiSignature"));
			 String url = ((String) request.getSession().getAttribute("urlTelaAtual"));
			 Boolean utlRestrita = url == null || url.equals("") || urlsPermitidas.stream().noneMatch(u -> url.contains(u));
			 if(utlRestrita && (isSeiSignature == null || !isSeiSignature) && ((loginControle == null || !Uteis.isAtributoPreenchido(loginControle.getUsuario())) && request.getParameter("tokenAutenticacaoMobileOtm") == null)) {
	         	throw new Exception("Você está acessando uma área restrita ou não está autenticado no sistema.");
	         }
			processRequest(request, response);
		} catch (Exception e) {
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			 LoginControle loginControle = (LoginControle) request.getSession().getAttribute("LoginControle");
			 if((loginControle == null || !Uteis.isAtributoPreenchido(loginControle.getUsuario())) && request.getParameter("tokenAutenticacaoMobileOtm") == null) {
	         	throw new Exception("Você está acessando uma área restrita ou não está autenticado no sistema.");
	         }
			processRequest(request, response);
		} catch (Exception ex) {
		}
	}
	
}
