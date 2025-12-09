package webservice.servicos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.security.Provider;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;  
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.fileupload.MultipartStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.lacuna.InitializeRequest;
//import negocio.comuns.lacuna.RespostaAssinaturaInicializaLacunaVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.academico.DocumentoAssinado;
import negocio.interfaces.academico.DocumentoAssinadoInterfaceFacade;
import webservice.AlwaysListTypeAdapterFactory;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;
import webservice.servicos.objetos.DocumentoAssinadoRSVO;
import webservice.servicos.objetos.TipoOrigemDocumentoAssinadoRSVO;


@Path("/documentoAssinadoRS")
public class DocumentoAssinadoRS extends SuperControle  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6328581634804098461L;
	
	@Context 
	private HttpServletRequest request;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private Boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso; 
	
	public DocumentoAssinadoRS() {
		super();
		consultarConfiguracaoGeralSistemaUtilizar(null);
	}
	
	public void consultarConfiguracaoGeralSistemaUtilizar(Integer unidadeEnsino) {
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, null, unidadeEnsino));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(UsuarioVO usuario) {
		if(permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso == null){
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidade("PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso", usuario);
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = true;
			} catch (Exception e) {
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = false;
			}
			
		}
		return permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	}
	
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/consultarNumeroArquivo/{numero}")
	public List<DocumentoAssinadoVO> consultarPessoaSincronizar(@PathParam("numero") final Integer numero)  {
		List<DocumentoAssinadoVO> lista = new ArrayList<DocumentoAssinadoVO>();
		try {
			lista = (getDocumentoAssinadoFacade().consultarArquivoAssinado("codigo", numero.toString(), 1, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, null, null, null, true, true));
			if(!Uteis.isAtributoPreenchido(lista)){		 	
				 throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
			}	
		} catch (Exception e) {
			if(e.getMessage() == null || e.getMessage().equals("null")){
				throw new ExceptionRS(Status.NOT_FOUND, "Por favor entre em contato com administrador");
			}else{
				throw new ExceptionRS(Status.NOT_FOUND, Uteis.trocarLetraAcentuadaPorCodigoHtml(e.getMessage()));
			}
		}
		return lista;
	}
	
	@GET
	@Produces({ "application/xml", "application/json" })
	@Path("/consultarDiploma/{tipoDocumento}/{dados}")
	public Response consultarDiplomaCertificado(@PathParam("tipoDocumento") final String tipoDocumento,@PathParam("dados") final String dados, @Context UriInfo ui) {
		try {
			String baseURL = String.valueOf(ui.getBaseUri());	
			URI redirectURUri = UriBuilder.fromUri(baseURL.replace("webservice/", "")).queryParam("tipoDoc", tipoDocumento).queryParam("dados", dados).path("/visaoAdministrativo/academico/documentoAssinado.xhtml").build();
			return Response.seeOther(redirectURUri).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
		}
	}
	
	public DocumentoAssinadoInterfaceFacade getDocumentoAssinadoFacade() {
		return getSpringUtil().getApplicationContext().getBean(DocumentoAssinadoInterfaceFacade.class);
	}
	
	
	
	@GET
	@Path("/status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response status() {
		ErrorInfoRSVO errorInfoRSVO;
		try {
			errorInfoRSVO = new ErrorInfoRSVO(Status.OK, "WebService conectado com sucesso.");
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
		return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
	}
	
	
	
	
	@GET
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILTRO_TIPO_ORIGEM_DOCUMENTO)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarTipoOrigemDocumentoAssinado(@Context final HttpServletRequest request) {
		try {
			Gson gson = inicializaGson();
			List<TipoOrigemDocumentoAssinadoRSVO> lista = new ArrayList<TipoOrigemDocumentoAssinadoRSVO>();
			for (TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum : TipoOrigemDocumentoAssinadoEnum.values()) {
				if (Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnum) && !tipoOrigemDocumentoAssinadoEnum.isXmlMec()) {
					TipoOrigemDocumentoAssinadoRSVO tipo = new TipoOrigemDocumentoAssinadoRSVO();
					tipo.setName(tipoOrigemDocumentoAssinadoEnum.name());
					tipo.setDescricao(tipoOrigemDocumentoAssinadoEnum.getDescricao() + (tipoOrigemDocumentoAssinadoEnum.name().equals("DIPLOMA_DIGITAL") || tipoOrigemDocumentoAssinadoEnum.name().equals("HISTORICO_DIGITAL") || tipoOrigemDocumentoAssinadoEnum.name().equals("CURRICULO_ESCOLAR_DIGITAL") ? " (XML)" : ""));
					if (tipo.getName() != "NENHUM") {
						lista.add(tipo);
					}
				}
			}
			Ordenacao.ordenarLista(lista, "descricao");
			return Response.status(Status.OK).entity(gson.toJson(lista)).build();
		} catch (Exception e) {
			System.out.println("Sei Signature: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			tratarMensagemErro(errorInfoRSVO);
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_CONSULTAR)	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarDocumentos(@Context final HttpServletRequest request, final DocumentoAssinadoRSVO obj) {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			validarUsuarioLogado(request.getParameter(UteisWebServiceUrl.ul));
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario.getPessoa()), "O Usuário informado não pode realizar nenhum tipo de assinatura. Por isso não é permitdo a listagem de nenhum documento assinado.");
			obj.setListaDocumentoPendente(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorSeiSignature(Uteis.NIVELMONTARDADOS_TODOS, usuario, obj.getTipoDocumentoAssinado(), obj.getOrdemAssinaturaFiltrar()));
			for (DocumentoAssinadoVO documentoAssinadoVO : obj.getListaDocumentoPendente()) {
				if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
					for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
						documentoAssinadoPessoaVO.setNomeAssinanteApresentar(documentoAssinadoPessoaVO.obterNomeAssinanteApresentar());
					}
				}
			}
			executarConsultaDocumentosAssinados(obj, usuario);
			Gson gson = inicializaGson();
			return Response.status(Status.OK).entity(gson.toJson(obj)).build();
		} catch (Exception e) {
			System.out.println("Sei Signature: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			tratarMensagemErro(errorInfoRSVO);
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void validarUsuarioLogado(String codigoUsuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(codigoUsuario)) {
			throw new Exception("Prezado usuário, houve uma atualização do SEI Signature, é necessário que acesse o SEI e baixe esta nova versão.");
		}
	}
	
	public void tratarMensagemErro(InfoWSVO errorInfoRSVO) {
		if (!Uteis.isAtributoPreenchido(errorInfoRSVO.getMessage())) {
			errorInfoRSVO.setMessage("Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
			errorInfoRSVO.setMensagem("Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
		}
	}
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_CONSULTAR_ASSINADO)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarDocumentosAssinados(@Context final HttpServletRequest request, final DocumentoAssinadoRSVO documentoAssinadoRSVO) {
		try {			
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario.getPessoa()), "O Usuário informado não pode realizar nenhum tipo de assinatura. Por isso não é permitdo a listagem de nenhum documento assinado.");
			executarConsultaDocumentosAssinados(documentoAssinadoRSVO, usuario);
			Gson gson = inicializaGson();
			return Response.status(Status.OK).entity(gson.toJson(documentoAssinadoRSVO)).build();
		} catch (Exception e) {
			System.out.println("Sei Signatue: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	private void executarConsultaDocumentosAssinados(final DocumentoAssinadoRSVO obj, UsuarioVO usuario) throws Exception {
		TipoOrigemDocumentoAssinadoEnum tipoOrigem = obj.getFitroTipoOrigemDocumentoAssinadoEnum()!= null && !obj.getFitroTipoOrigemDocumentoAssinadoEnum().getName().isEmpty() ? TipoOrigemDocumentoAssinadoEnum.valueOf(obj.getFitroTipoOrigemDocumentoAssinadoEnum().getName()) : TipoOrigemDocumentoAssinadoEnum.NENHUM;
		DataModelo listaDocumentosAssinados = new DataModelo();
		listaDocumentosAssinados.setPaginaAtual(obj.getPaginaAtual());
		listaDocumentosAssinados.setLimitePorPagina(obj.getLimitePorPagina());
		getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(listaDocumentosAssinados, new UnidadeEnsinoVO(), new CursoVO(), new TurmaVO(), new DisciplinaVO(), "", "", new MatriculaVO(), tipoOrigem, obj.getFiltroDataRegistroInicio(), obj.getFiltroDataRegistroFim(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuario.getPessoa(), usuario, listaDocumentosAssinados.getLimitePorPagina(), listaDocumentosAssinados.getOffset());
		obj.setListaDocumentoAssinado((List<DocumentoAssinadoVO>) listaDocumentosAssinados.getListaConsulta());
		obj.setTotalRegistrosEncontrados(listaDocumentosAssinados.getTotalRegistrosEncontrados());
		obj.setTotalPaginas(listaDocumentosAssinados.getTotalPaginas());
		obj.setPaginaAtual(listaDocumentosAssinados.getPaginaAtual());
	}
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE_EXISTE)	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadFileExiste(@Context final HttpServletRequest request, final DocumentoAssinadoVO doc) throws Exception {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}			
			File file = new File(getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo() + File.separator + doc.getArquivo().getNome());
			Uteis.checkState(!file.exists(), "Não foi localizado o Documento para realizar a Download");
			ResponseBuilder response = Response.ok();
			return response.build();
		} catch (Exception e) {
			System.out.println("Sei Signatue: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	   
	}
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE)	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	public Response downloadFile(@Context final HttpServletRequest request, final DocumentoAssinadoVO doc) throws Exception {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());	
			}			
			File file = new File(getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + doc.getArquivo().getPastaBaseArquivo() + File.separator + doc.getArquivo().getNome());
			Uteis.checkState(!file.exists(), "Não foi localizado o Documento informada para realizar a assinatura");
			ResponseBuilder response = Response.ok((Object) file);						
			String userAgent = request.getHeader("User-Agent");
  			if(Uteis.ClienteMovel(userAgent)){
  				response.header("Content-Disposition", "inline;filename=" + doc.getArquivo().getNome());
  			}else {
  				response.header("Content-Disposition", "attachment;filename=" + doc.getArquivo().getNome());
  			}
			return response.build();
		} catch (Exception e) {
			System.out.println("Sei Signatue: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE_UPLOAD + "/{cod}/{ordemAssinatura}/{provedorAssinatura}/{dataAssinaturaSeiSignature}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@Context final HttpServletRequest request, InputStream uploadedInputStream, @PathParam("cod") String cod, @PathParam("ordemAssinatura") String ordemAssinatura, @PathParam("provedorAssinatura") String provedorAssinatura, @PathParam("dataAssinaturaSeiSignature") String dataAssinaturaSeiSignature) throws Exception {
		MultipartStream multipartStream = null;
		ByteArrayOutputStream data = null;
		InputStream isXml = null;
		InputStream is = null;
		InputStream input = null;
		DocumentoAssinadoVO documentoAssinado = new DocumentoAssinadoVO();
		try {
			String contentType = request.getContentType();
			request.getHeaderNames();
			Boolean assinarPorCNPJ = Boolean.valueOf(request.getHeader("assinarPorCNPJ"));
			String tamanhoArquivo = String.valueOf(request.getHeader("tamanhoArquivo"));
			Boolean realizarEscritaDebug = Boolean.valueOf(request.getHeader("realizarEscritaDebug"));
			data = new ByteArrayOutputStream();
			int boundaryIndex = contentType.indexOf("boundary=");
			if (Uteis.isAtributoPreenchido(boundaryIndex)) {
				byte[] boundary = (contentType.substring(boundaryIndex + 9)).getBytes();
				multipartStream = new MultipartStream(uploadedInputStream, boundary);
				boolean nextPart = multipartStream.skipPreamble();
				while (nextPart) {
					String headers = multipartStream.readHeaders();
					multipartStream.readBodyData(data);
					uploadedInputStream = new ByteArrayInputStream(data.toByteArray());
					nextPart = multipartStream.readBoundary();
					is = new ByteArrayInputStream(data.toByteArray());
					isXml = new ByteArrayInputStream(data.toByteArray());
					input = new ByteArrayInputStream(data.toByteArray());
				}
			}
			if (data != null) {
				data.close();
				data = null;
			}
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			validarUsuarioLogado(request.getHeader("ul"));
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getHeader(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			documentoAssinado = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(Integer.parseInt(cod), Uteis.NIVELMONTARDADOS_TODOS, null);
			StringBuilder escritaDebug = new StringBuilder("");
			escritaDebug.append(" (PARAMETROS REQUISIÇÃO) ");
			escritaDebug.append("  [CODIGO DOCUMENTO: " + cod).append("]  ");
			escritaDebug.append("  [ORDEM ASSINATURA: " + ordemAssinatura).append("]  ");
			escritaDebug.append("  [PROVEDOR ASSINATURA: " + provedorAssinatura).append("]  ");
			escritaDebug.append("  [DATA ASSINATURA: " + dataAssinaturaSeiSignature).append("]  ");
			escritaDebug.append("  [ASSINAR POR CNPJ: " + assinarPorCNPJ).append("]  ");
			escritaDebug.append("  [CÓDIGO USUARIO: " + Integer.parseInt(request.getHeader(UteisWebServiceUrl.ul))).append("]  ");
			if (Uteis.isAtributoPreenchido(usuario)) {
				escritaDebug.append("  [CÓDIGO PESSOA USUARIO: " + usuario.getPessoa().getCodigo()).append("]  ");
			}
			realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, escritaDebug.toString());
			int qtdAssinados = Uteis.isAtributoPreenchido(documentoAssinado) && !documentoAssinado.getListaDocumentoAssinadoPessoa().isEmpty() ? documentoAssinado.getListaDocumentoAssinadoPessoa().stream().filter(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO)).collect(Collectors.toList()).size() : 0;
			for (DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO : documentoAssinado.getListaDocumentoAssinadoPessoa()) {
				if (documentoAssinadoPessoaVO.getOrdemAssinatura().equals(Integer.parseInt(ordemAssinatura))) {
					assinarPorCNPJ = documentoAssinadoPessoaVO.getAssinarPorCNPJ();
					if (Uteis.isAtributoPreenchido(dataAssinaturaSeiSignature)) {
						Date dataAssinaturaSeiSign = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataAssinaturaSeiSignature);
						documentoAssinadoPessoaVO.setDataAssinatura(dataAssinaturaSeiSign);
						qtdAssinados++;
					}
					break;
				}
			}
			if (Uteis.isAtributoPreenchido(boundaryIndex) && documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)) {
				validarAssinaturasPDF(is, tamanhoArquivo, documentoAssinado, realizarEscritaDebug);
			} else if (Uteis.isAtributoPreenchido(boundaryIndex) && documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
				validarQtdAssinaturaDocumentoXML(is, input, tamanhoArquivo, documentoAssinado, qtdAssinados, realizarEscritaDebug);
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (input != null) {
				input.close();
				input = null;
			}
			Integer quantidadeRegistrosAlterados = getFacadeFactory().getDocumentoAssinadoFacade().executarAtualizacaoDocumentoAssinadoPorSeiSignature(documentoAssinado, uploadedInputStream, null, Boolean.FALSE, documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) ? tamanhoArquivo : Constantes.EMPTY, getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(usuario), true, getConfiguracaoGeralSistemaVO(), usuario, assinarPorCNPJ, Integer.parseInt(ordemAssinatura), provedorAssinatura, true, realizarEscritaDebug);
			realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(12) Foi alterado " + quantidadeRegistrosAlterados + " registros");
			return Response.status(Status.OK).entity(quantidadeRegistrosAlterados.toString()).build();
		} catch (Exception e) {
			if (!Uteis.isAtributoPreenchido(documentoAssinado)) {
				documentoAssinado = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(Integer.parseInt(cod), Uteis.NIVELMONTARDADOS_TODOS, null);
			}
			if (isXml != null && (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL))) {
				uploadXmlDiplomaDigitalErro(documentoAssinado, isXml);
			}
			if (isXml != null) {
				isXml.close();
				isXml = null;
			}
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SEI_SIGNATURE, "SEI SIGNATURE: \"" + "ERRO uploalFile, UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE_UPLOAD " + "(Documento Assinado: " + Integer.parseInt(cod) + ")" + "\"" + e.getMessage());
			System.out.println("Sei Signature: " + e.getMessage());
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
		} finally {
			multipartStream = null;
			if (data != null) {
				data.close();
				data = null;
			}
			if (uploadedInputStream != null) {
				uploadedInputStream.close();
				uploadedInputStream = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (isXml != null) {
				isXml.close();
				isXml = null;
			}
			if (input != null) {
				input.close();
				input = null;
			}
		}
	}
	
	@POST
	@Path(UteisWebServiceUrl.URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_REJEITAR)	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response realizarRejeicaoAssinaturaDocumentos(@Context final HttpServletRequest request, final DocumentoAssinadoRSVO documentoAssinadoRSVO) {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			validarUsuarioLogado(request.getParameter(UteisWebServiceUrl.ul));
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(Integer.parseInt(request.getParameter(UteisWebServiceUrl.ul)), Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			for (DocumentoAssinadoVO doc : documentoAssinadoRSVO.getListaDocumentoAssinar()) {
				for (DocumentoAssinadoPessoaVO dap : doc.getListaDocumentoAssinadoPessoa()) {
					if ((dap.getPessoaVO().getCodigo().equals(usuario.getPessoa().getCodigo()) && dap.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) || (doc.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && dap.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))) {
						dap.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
						dap.setDataRejeicao(new Date());
						dap.setMotivoRejeicao(documentoAssinadoRSVO.getMotivoRejeicao());
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(dap);
					}
				}
				if (doc.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
					getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(doc.getCodigo(), Boolean.TRUE, documentoAssinadoRSVO.getMotivoRejeicao(), usuario);
				}
			}
			return Response.status(Status.OK).entity(documentoAssinadoRSVO).build();
		} catch (Exception e) {
			System.out.println("Sei Signature: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			tratarMensagemErro(errorInfoRSVO);
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}
	}
	

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	private void realizarEscritaDebugSeiSignature(DocumentoAssinadoVO documentoAssinado, Boolean realizarEscritaDebug, String mensagem) {
		if (realizarEscritaDebug && Uteis.isAtributoPreenchido(documentoAssinado) && (documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA))) {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SEI_SIGNATURE, "Documento Assinado: " + documentoAssinado.getCodigo() + " " + documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().name() +  " (" + mensagem + ")");
		}
	}
	
	private void validarAssinaturasPDF(InputStream uploadedInputStream, String tamanhoArquivo, DocumentoAssinadoVO documentoAssinado, Boolean realizarEscritaDebug) throws Exception {
		byte[] buffer = null;
		File fileTemp = new File(getCaminhoPastaWeb() + File.separator + "relatorio");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		File pdfDiploma = new File(fileTemp + File.separator + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) + "_" + documentoAssinado.getArquivo().getNome());
		buffer = new byte[uploadedInputStream.available()];
		uploadedInputStream.read(buffer);
		Files.write(buffer, pdfDiploma);
		if (!(pdfDiploma != null && pdfDiploma.exists())) {
			throw new Exception("Erro ao receber arquivo do SeiSignature, arquivo recebido do SeiSignature foi corrompido ou perdido no envio.");
		}
		realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "Tamanho arquivo assinado: " + tamanhoArquivo + " Tamanho arquivo validado: " + pdfDiploma.length());
		if (pdfDiploma.length() <= Long.valueOf(tamanhoArquivo)) {
			throw new Exception("Erro ao atualizar documento assinado pelo SEI, o tamanho do arquivo recebido do SEI SIGNATURE não é compatível com o tamanho do arquivo que acabou de ser assinado.");
		}
		
		List<String> mensagem = new ArrayList<>(0);
		PdfReader reader = new PdfReader(pdfDiploma.getPath());
		int quantidadeEncontrada = 0;
		Provider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		AcroFields af = reader.getAcroFields();
		ArrayList<String> names = af.getSignatureNames();
		for (String name : names) {
			PdfPKCS7 pk = af.verifySignature(name, provider.getName());
			if (pk.getSigningCertificate() != null && !pk.getSigningCertificate().toString().trim().isEmpty()) {
				mensagem.add("ASSINATURA: " + (quantidadeEncontrada+1) + " CERTIFICADO: " + pk.getSigningCertificate().getSubjectDN().toString());
				quantidadeEncontrada++;
			}
			af.removeField(name);
		}
		if (mensagem.isEmpty()) {
			throw new Exception("Não foi encontrado nenhuma assinatura no PDF recebido do SeiSignature.");
		} else {
			mensagem.stream().forEach(m -> realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, m.trim()));
		}
		reader.close();
		reader = null;
		pdfDiploma.delete();
		uploadedInputStream.close();
		uploadedInputStream = null;
		buffer = null;
	}
	
	private void validarQtdAssinaturaDocumentoXML(InputStream uploadedInputStream, InputStream is, String tamanhoArquivo, DocumentoAssinadoVO documentoAssinado, int quantidadePessoasAssinaram, Boolean realizarEscritaDebug) throws Exception {
		realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(2) validação de assinatura do xml.");
		byte[] buffer = null;
		File xmlDiploma = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + documentoAssinado.getArquivo().getNome());
		buffer = new byte[is.available()];
		is.read(buffer);
		Files.write(buffer, xmlDiploma);
		realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(2.1) validação no tamanho do arquivo recebido do SeiSignature [Arquivo LACUNAS: " + Long.valueOf(tamanhoArquivo) + ", Arquivo SeiSignature: " + xmlDiploma.length());
		if (xmlDiploma.exists()) {
			if (xmlDiploma.length() <= Long.valueOf(tamanhoArquivo)) {
				realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(2.2) erro na validação das assinaturas (O tamanho do arquivo assinado da LACUNAS e menor ou igual ao original / Arquivo LACUNAS: " + tamanhoArquivo + ", Arquivo original: " + xmlDiploma.length() + ").");
				if (is != null) {
					is.close();
					is = null;
				}
				if (buffer != null) {
					buffer = null;
				}
				String mensagemErro = "Erro na validação das assinaturas (O tamanho do arquivo assinado da LACUNAS e menor ou igual ao arquivo original / Arquivo LACUNAS: " + tamanhoArquivo + ", Arquivo recebido original: " + xmlDiploma.length() + ").";
				xmlDiploma.delete();
				throw new Exception(mensagemErro);
			}
			xmlDiploma.delete();
		}
		ByteArrayOutputStream baosInput = null;
		DocumentBuilderFactory docFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			String dadosDiploma = documentoAssinado.getDecisaoJudicial() ? "DadosDiplomaPorDecisaoJudicial" : "DadosDiploma";
			String dadosRegistro = documentoAssinado.getDecisaoJudicial() ? "DadosRegistroPorDecisaoJudicial" : "DadosRegistro";
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			baosInput = new ByteArrayOutputStream();
			byte[] bufferInput = new byte[1024];
			int lenInput;
			while ((lenInput = uploadedInputStream.read(bufferInput)) > 0) {
				baosInput.write(bufferInput, 0, lenInput);
			}
			baosInput.flush();
			doc = docBuilder.parse(new ByteArrayInputStream(baosInput.toByteArray()));
			doc.getDocumentElement().normalize();

			// Verificando assinaturas em DadosDiploma
			Node nodeDadosDiploma = doc.getElementsByTagName(dadosDiploma).item(0);
			NodeList nodeDadosDiplomaChildNodes = nodeDadosDiploma.getChildNodes();
			List<String> assinantes = new ArrayList<>(0);
			int totalAssinaturas = 0;
			x: for (int i = 0; i < nodeDadosDiplomaChildNodes.getLength(); i++) {
				Node node = nodeDadosDiplomaChildNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
					totalAssinaturas++;
					NodeList node1ChildNodes = node.getChildNodes();
					for (int j = 0; j < node1ChildNodes.getLength(); j++) {
						Node node2 = node1ChildNodes.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
							NodeList node2ChildNodes = node2.getChildNodes();
							for (int x = 0; x < node2ChildNodes.getLength(); x++) {
								Node node3 = node2ChildNodes.item(x);
								if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
									NodeList node3ChildNodes = node3.getChildNodes();
									for (int y = 0; y < node3ChildNodes.getLength(); y++) {
										Node node4 = node3ChildNodes.item(y);
										if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
											assinantes.add(node4.getTextContent());
											continue x;
										}
									}
								}
							}
						}
					}
				}
			}

			// Verificando assinaturas em DadosRegistro
			Node nodeRegistro = doc.getElementsByTagName(dadosRegistro).item(0);
			NodeList nodeRegistroChildNodes = nodeRegistro.getChildNodes();
			x: for (int i = 0; i < nodeRegistroChildNodes.getLength(); i++) {
				Node node = nodeRegistroChildNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
					totalAssinaturas++;
					NodeList node1ChildNodes = node.getChildNodes();
					for (int j = 0; j < node1ChildNodes.getLength(); j++) {
						Node node2 = node1ChildNodes.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
							NodeList node2ChildNodes = node2.getChildNodes();
							for (int x = 0; x < node2ChildNodes.getLength(); x++) {
								Node node3 = node2ChildNodes.item(x);
								if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
									NodeList node3ChildNodes = node3.getChildNodes();
									for (int y = 0; y < node3ChildNodes.getLength(); y++) {
										Node node4 = node3ChildNodes.item(y);
										if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
											assinantes.add(node4.getTextContent());
											continue x;
										}
									}
								}
							}
						}
					}
				}
			}

			// Verificando assinaturas em Diploma
			Node nodeDiploma = doc.getElementsByTagName("Diploma").item(0);
			NodeList nodeDiplomaChildNodes = nodeDiploma.getChildNodes();
			x: for (int i = 0; i < nodeDiplomaChildNodes.getLength(); i++) {
				Node node = nodeDiplomaChildNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:Signature", node.getNodeName()) || Objects.equals("Signature", node.getNodeName()))) {
					totalAssinaturas++;
					NodeList node1ChildNodes = node.getChildNodes();
					for (int j = 0; j < node1ChildNodes.getLength(); j++) {
						Node node2 = node1ChildNodes.item(j);
						if (node2.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:KeyInfo", node2.getNodeName()) || Objects.equals("KeyInfo", node2.getNodeName()))) {
							NodeList node2ChildNodes = node2.getChildNodes();
							for (int x = 0; x < node2ChildNodes.getLength(); x++) {
								Node node3 = node2ChildNodes.item(x);
								if (node3.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509Data", node3.getNodeName())) || Objects.equals("X509Data", node3.getNodeName())) {
									NodeList node3ChildNodes = node3.getChildNodes();
									for (int y = 0; y < node3ChildNodes.getLength(); y++) {
										Node node4 = node3ChildNodes.item(y);
										if (node4.getNodeType() == Node.ELEMENT_NODE && (Objects.equals("ds:X509SubjectName", node4.getNodeName()) || Objects.equals("X509SubjectName", node4.getNodeName()))) {
											assinantes.add(node4.getTextContent());
											continue x;
										}
									}
								}
							}
						}
					}
				}
			}
			realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(3) final validação de assinatura do xml [Total assinaturas: " + totalAssinaturas + ", Quantidade pessoas assinaram: " + quantidadePessoasAssinaram + "]");
			if (Uteis.isAtributoPreenchido(assinantes)) {
				realizarEscritaDebugSeiSignature(documentoAssinado, realizarEscritaDebug, "(3.1) final validação de assinatura do xml [Assinantes: " + assinantes.stream().map(a -> a.toString()).collect(Collectors.joining(" | ")) + "]");
			}
			if (totalAssinaturas == 0) {
				throw new Exception("Houve um erro ao verificar a assinatura do documento. Não foi localizado a assinatura no documento.");
			}
			if (totalAssinaturas > quantidadePessoasAssinaram) {
				throw new Exception("Houve um erro ao verificar a assinatura do documento. Foram localizadas assinaturas excedentes no documento.");
			}
			if ((totalAssinaturas < quantidadePessoasAssinaram)) {
				throw new Exception("Houve um erro ao verificar a assinatura do documento. Foram localizadas assinaturas faltantes no documento.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (baosInput != null) {
				baosInput.close();
				baosInput = null;
			}
			docFactory = null;
			docBuilder = null;
			doc = null;
			if (is != null) {
				is.close();
				is = null;
			}
			if (buffer != null) {
				buffer = null;
			}
			xmlDiploma.delete();
		}
	}
	
	private void uploadXmlDiplomaDigitalErro(DocumentoAssinadoVO documentoAssinado, InputStream uploadedInputStream) throws Exception {
		byte[] buffer = null;
		try {
			File fileTemp = new File(getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + "xmlDiploma" + File.separator + documentoAssinado.getCodigo());
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}
			File xmlDiploma = new File(fileTemp + File.separator + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) + "_" +  documentoAssinado.getArquivo().getNome());
			buffer = new byte[uploadedInputStream.available()];
			uploadedInputStream.read(buffer);
			Files.write(buffer, xmlDiploma);
			realizarEscritaDebugSeiSignature(documentoAssinado, Boolean.TRUE, "(ERRO) Xml assinado foi armazenado no APACHE " + xmlDiploma.getPath());
		} catch (Exception e) {
			throw e;
		} finally {
			if (uploadedInputStream != null) {
				uploadedInputStream.close();
				uploadedInputStream = null;
			}
			if (buffer != null) {
				buffer = null;
			}
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/consultarDiplomaCodigoValidacao/{codigoValidacao}")
	public Response consultarDiplomaCodigoValidacao(@PathParam("codigoValidacao") final String codigoValidacao) {
		try {
			java.net.URI location = new java.net.URI("../visaoAdministrativo/academico/documentoAssinado/diploma.xhtml?tipoDoc=DIPLOMA_DIGITAL&dados=" + codigoValidacao + "&idControlador=" + codigoValidacao);
			return Response.temporaryRedirect(location).build();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().equals("null")) {
				throw new ExceptionRS(Status.NOT_FOUND, "Por favor entre em contato com administrador");
			} else {
				throw new ExceptionRS(Status.NOT_FOUND, Uteis.trocarLetraAcentuadaPorCodigoHtml(e.getMessage()));
			}
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/consultarHistoricoCodigoValidacao/{codigoValidacao}")
	public Response consultarHistoricoCodigoValidacao(@PathParam("codigoValidacao") final String codigoValidacao) {
		try {
			java.net.URI location = new java.net.URI("../visaoAdministrativo/academico/documentoAssinado/historico.xhtml?tipoDoc=HISTORICO_DIGITAL&dados=" + codigoValidacao + "&idControlador=" + codigoValidacao);
			return Response.temporaryRedirect(location).build();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().equals("null")) {
				throw new ExceptionRS(Status.NOT_FOUND, "Por favor entre em contato com administrador");
			} else {
				throw new ExceptionRS(Status.NOT_FOUND, Uteis.trocarLetraAcentuadaPorCodigoHtml(e.getMessage()));
			}
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/consultarCurriculoCodigoValidacao/{codigoValidacao}")
	public Response consultarCurriculoCodigoValidacao(@PathParam("codigoValidacao") final String codigoValidacao) {
		try {
			java.net.URI location = new java.net.URI("../visaoAdministrativo/academico/documentoAssinado.xhtml?tipoDoc=CURRICULO_ESCOLAR_DIGITAL&dados=" + codigoValidacao + "&idControlador=" + codigoValidacao);
			return Response.temporaryRedirect(location).build();
		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().equals("null")) {
				throw new ExceptionRS(Status.NOT_FOUND, "Por favor entre em contato com administrador");
			} else {
				throw new ExceptionRS(Status.NOT_FOUND, Uteis.trocarLetraAcentuadaPorCodigoHtml(e.getMessage()));
			}
		}
	}
	
	@POST
	@Path(UteisWebServiceUrl.REALIZAR_ESCRITA_DEBUG)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response realizarEscritaDebug(@Context final HttpServletRequest request, final List<String> listaMensagem) throws Exception {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() != Status.OK.getStatusCode()) {
				throw new StreamSeiException(infoWSVO.getMensagem());
			}
			if (Uteis.isAtributoPreenchido(listaMensagem)) {
				for (String mensagem : listaMensagem) {
					AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SEI_SIGNATURE, mensagem);
				}
			}
			return Response.status(Status.OK).entity("200").build();
		} catch (Exception e) {
			System.out.println("Sei Signature: " + e.getMessage());
			InfoWSVO errorInfoRSVO = new InfoWSVO();
			errorInfoRSVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			errorInfoRSVO.setMessage(e.getMessage());
			errorInfoRSVO.setMensagem(e.getMessage());
			tratarMensagemErro(errorInfoRSVO);
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SEI_SIGNATURE, "SEI SIGNATURE: \"" + errorInfoRSVO.getMensagem() + "\"");
			throw new WebServiceException(errorInfoRSVO, Status.INTERNAL_SERVER_ERROR);
		}

	}
	
//	@POST
//	@SuppressWarnings("serial")
//	@Path("/xml/inicializaAssinaturaXmlDocumentoAssinado")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response inicializaAssinaturaXmlDocumentoAssinado(@Context final HttpServletRequest request, final String paramObject) throws Exception {
//		DocumentoAssinadoVO documentoAssinado = new DocumentoAssinadoVO();
//		InitializeRequest initializeRequest = new InitializeRequest();
//		RespostaAssinaturaInicializaLacunaVO respostaAssinaturaInicializa = null;
//		Gson gson = null;
//		Type type = null;
//		try {
//			gson = new GsonBuilder()
//					.setDateFormat("MM-dd-yyyy HH:mm:ss")
//					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
//					.create();
//			type = new TypeToken<InitializeRequest>() {}.getType();
//			initializeRequest = gson.fromJson(paramObject, type);
//			if (getAplicacaoControle().getProgressBarAssinarXmlMec().getForcarEncerramento()) {
//				return Response.status(Status.OK).entity(null).build();
//			} else if (Objects.isNull(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) || !getAplicacaoControle().getMapDocumentoAssinadoAssinarXml().containsKey(initializeRequest.getCodigoDocumentoAssinado())) {
//				throw new Exception("Não foi encontrado o documento assinado salvo em aplicação para realizar a assinatura do XML");
//			}
//			documentoAssinado = getAplicacaoControle().getDocumentoAssinadoAssinarXml(initializeRequest.getCodigoDocumentoAssinado());
//			respostaAssinaturaInicializa = new RespostaAssinaturaInicializaLacunaVO();
//			respostaAssinaturaInicializa.setDocumentoAssinado(initializeRequest.getCodigoDocumentoAssinado());
//			respostaAssinaturaInicializa.setOrdemAssinatura(initializeRequest.getOrdemAssinatura());
//			respostaAssinaturaInicializa.setCodigoPessoaLogada(initializeRequest.getCodigoPessoaLogada());
//			respostaAssinaturaInicializa.setThumbprintCertificado(initializeRequest.getThumbprintCertificado());
//			respostaAssinaturaInicializa.setTokenLacuna(getFacadeFactory().getServicoIntegracaoLacunaInterfaceFacade().iniciandoRestPKI(documentoAssinado, initializeRequest.getOrdemAssinatura()));
//			return Response.status(Status.OK).entity(gson.toJson(respostaAssinaturaInicializa)).build();
//		} catch (RestException restException) {
//			if (!Uteis.isAtributoPreenchido(documentoAssinado.getCodigo())) {
//				documentoAssinado.setCodigo(initializeRequest.getCodigoDocumentoAssinado());
//			}
//			documentoAssinado.setErro(Boolean.TRUE);
//			documentoAssinado.setMotivoErro(restException.getMessage());
//			getFacadeFactory().getDocumentoAssinadoFacade().atualizarSituacaoDocumentoAssinadoErro(documentoAssinado);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(restException.getMessage()).build();
//		} catch (Exception e) {
//			if (!Uteis.isAtributoPreenchido(documentoAssinado.getCodigo())) {
//				documentoAssinado.setCodigo(initializeRequest.getCodigoDocumentoAssinado());
//			}
//			documentoAssinado.setErro(Boolean.TRUE);
//			documentoAssinado.setMotivoErro(e.getMessage());
//			getFacadeFactory().getDocumentoAssinadoFacade().atualizarSituacaoDocumentoAssinadoErro(documentoAssinado);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		} finally {
//			getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(LocalDateTime.now());
//			if (Objects.nonNull(documentoAssinado)) {
//				removerObjetoMemoria(documentoAssinado);
//				documentoAssinado = null;
//			}
//			if (Objects.nonNull(initializeRequest)) {
//				initializeRequest = null;
//			}
//			if (Objects.nonNull(respostaAssinaturaInicializa)) {
//				respostaAssinaturaInicializa = null;
//			}
//			if (Objects.nonNull(gson)) {
//				gson = null;
//			}
//			if (Objects.nonNull(type)) {
//				type = null;
//			}
//		}
//	}
	
//	@POST
//	@SuppressWarnings("serial")
//	@Path("/xml/finalizaAssinaturaXmlDocumentoAssinado")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response finalizaAssinaturaXmlDocumentoAssinado(@Context final HttpServletRequest request, final String paramObject) throws Exception {
//		RespostaAssinaturaInicializaLacunaVO respostaAssinaturaInicializaLacuna = new RespostaAssinaturaInicializaLacunaVO();
//		DocumentoAssinadoVO documentoAssinado = new DocumentoAssinadoVO();
//		FileInputStream fileInputStream = null;
//		String caminhoArquivoOriginal = Constantes.EMPTY;
//		Gson gson = null;
//		Type type = null;
//		try {
//			gson = new GsonBuilder()
//					.setDateFormat("MM-dd-yyyy HH:mm:ss")
//					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
//					.create();
//			type = new TypeToken<RespostaAssinaturaInicializaLacunaVO>() {}.getType();
//			respostaAssinaturaInicializaLacuna = gson.fromJson(paramObject, type);
//			if (Objects.isNull(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) || !getAplicacaoControle().getMapDocumentoAssinadoAssinarXml().containsKey(respostaAssinaturaInicializaLacuna.getDocumentoAssinado())) {
//				throw new Exception("Não foi encontrado o documento assinado salvo em aplicação para realizar a assinatura do XML");
//			}
//			documentoAssinado = getAplicacaoControle().getDocumentoAssinadoAssinarXml(respostaAssinaturaInicializaLacuna.getDocumentoAssinado());
//			File diretorioAssinados = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + "xmlDiploma" + File.separator + documentoAssinado.getCodigo());
//			File xmlAssinado = getFacadeFactory().getServicoIntegracaoLacunaInterfaceFacade().finalizandoRestPKI(documentoAssinado, respostaAssinaturaInicializaLacuna.getOrdemAssinatura(), respostaAssinaturaInicializaLacuna.getTokenLacuna());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(xmlAssinado), "Erro ao assinar o xml pela Lacuna");
//			fileInputStream = new FileInputStream(xmlAssinado);
//			caminhoArquivoOriginal = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + documentoAssinado.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinado.getArquivo().getNome();
//			getFacadeFactory().getArquivoHelper().escreverArquivo(fileInputStream, caminhoArquivoOriginal);
//			Boolean documentoAssinadoPessoaAlterada = Boolean.FALSE;
//			for (DocumentoAssinadoPessoaVO documentoAssinadoPessoa : documentoAssinado.getListaDocumentoAssinadoPessoa()) {
//				if (Uteis.isAtributoPreenchido(documentoAssinadoPessoa) && documentoAssinadoPessoa.getOrdemAssinatura().equals(respostaAssinaturaInicializaLacuna.getOrdemAssinatura()) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(respostaAssinaturaInicializaLacuna.getCodigoPessoaLogada())) {
//					documentoAssinado.setErro(Boolean.FALSE);
//					documentoAssinado.setMotivoErro(Constantes.EMPTY);
//					documentoAssinadoPessoa.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
//					if (!Uteis.isAtributoPreenchido(documentoAssinadoPessoa.getDataAssinatura())) {
//						documentoAssinadoPessoa.setDataAssinatura(new Date());
//					}
//					getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosAssinatura(documentoAssinadoPessoa, respostaAssinaturaInicializaLacuna.getOrdemAssinatura().equals(3) || respostaAssinaturaInicializaLacuna.getOrdemAssinatura().equals(5), respostaAssinaturaInicializaLacuna.getOrdemAssinatura(), "LACUNAS");
//					getFacadeFactory().getDocumentoAssinadoFacade().atualizarSituacaoDocumentoAssinadoErro(documentoAssinado);
//					documentoAssinadoPessoaAlterada = Boolean.TRUE;
//					break;
//				}
//			}
//			if (Uteis.isAtributoPreenchido(xmlAssinado)) {
//				xmlAssinado.delete();
//			}
//			if (Uteis.isAtributoPreenchido(diretorioAssinados)) {
//				diretorioAssinados.delete();
//			}
//			Uteis.checkState(!documentoAssinadoPessoaAlterada, "Não foi encontrado nenhuma pessoa apta para a atualização de sua situação, ordem de assinatura: " + respostaAssinaturaInicializaLacuna.getOrdemAssinatura() + ", código usuário logado: " + respostaAssinaturaInicializaLacuna.getCodigoPessoaLogada());
//			return Response.status(Status.OK).entity(gson.toJson("sucesso")).build();
//		} catch (RestException restException) {
//			if (!Uteis.isAtributoPreenchido(documentoAssinado.getCodigo())) {
//				documentoAssinado.setCodigo(respostaAssinaturaInicializaLacuna.getDocumentoAssinado());
//			}
//			documentoAssinado.setErro(Boolean.TRUE);
//			documentoAssinado.setMotivoErro(restException.getMessage());
//			getFacadeFactory().getDocumentoAssinadoFacade().atualizarSituacaoDocumentoAssinadoErro(documentoAssinado);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(restException.getMessage()).build();
//		} catch (Exception e) {
//			if (!Uteis.isAtributoPreenchido(documentoAssinado.getCodigo())) {
//				documentoAssinado.setCodigo(respostaAssinaturaInicializaLacuna.getDocumentoAssinado());
//			}
//			documentoAssinado.setErro(Boolean.TRUE);
//			documentoAssinado.setMotivoErro(e.getMessage());
//			getFacadeFactory().getDocumentoAssinadoFacade().atualizarSituacaoDocumentoAssinadoErro(documentoAssinado);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		} finally {
//			getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(LocalDateTime.now());
//			if (!Uteis.isAtributoPreenchido(documentoAssinado.getCodigo())) {
//				documentoAssinado.setCodigo(respostaAssinaturaInicializaLacuna.getDocumentoAssinado());
//			}
//			getAplicacaoControle().removerDocumentoAssinadoAssinarXml(documentoAssinado.getCodigo());
//			if (Objects.nonNull(documentoAssinado)) {
//				removerObjetoMemoria(documentoAssinado);
//				documentoAssinado = null;
//			}
//			if (Objects.nonNull(respostaAssinaturaInicializaLacuna)) {
//				removerObjetoMemoria(respostaAssinaturaInicializaLacuna);
//				respostaAssinaturaInicializaLacuna = null;
//			}
//			if (Objects.nonNull(gson)) {
//				gson = null;
//			}
//			if (Objects.nonNull(type)) {
//				type = null;
//			}
//			if (Objects.nonNull(fileInputStream)) {
//				fileInputStream.close();
//				fileInputStream = null;
//			}
//		}
//	}
}
