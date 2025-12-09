package webservice.moodle;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.RegistroWebserviceVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServicoWebserviceEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

@Service
@Path("/ava")
@SuppressWarnings("rawtypes")
public class IntegracaoMoodleRS extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public IntegracaoMoodleRS() throws Exception {
		super();
	}

	
	/**
	 * Responsavel por incluir pre inscricao(PreInscricaoVO) de uma lead(LeadRSVO) recebida pelo WS do RD Station
	 *  
	 * @param request
	 * @return
	 */
	@GET
	@Path("/pessoas/{horas}")
	@Produces({ "application/json" })
	public Response consultarPessoasAlteradasDentroIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {
		
		try {
			if (horas == 99) {
				//Realiza alteração em dados para processamento
				getFacadeFactory().getPessoaFacade().alterarPessoasSetandoDataAlteracaoInicial();
				return Response.status(Response.Status.OK).build();
			} else {
				Response invalido = invalidarIntegracaoMoodle(request, horas);
				if(invalido != null) {
					return invalido;
				}
				
				List<Object> listaPessoasVOs = realizarConsultaPessoas(horas);
				
				return enviarListaDeDadosParaMoodle(listaPessoasVOs);
			}
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}

	@GET
	@Path("/login/{token}")
	@Produces({ "application/json" })
	public Response consultarLoginToken(@Context final HttpServletRequest request, @PathParam("token") String token) {
		
		try {
			
			Response invalido = invalidarIntegracaoMoodle(request, 0);
			if(invalido != null) {
				return invalido;
			}
			
			Object pessoaVO = realizarPessoaLogin(token);
			
			return enviarDadosParaMoodle(pessoaVO);
			
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}


	@GET
	@Path("/cursos/{horas}")
	@Produces({ "application/json" })
	public Response consultarCursosAlteradasDentroIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {

		try {
			if (horas == 99) {
				//Realiza alteração em dados para processamento
				getFacadeFactory().getHorarioTurmaFacade().alterarCursosSetandoDataAlteracaoInicial();
				return Response.status(Response.Status.OK).build();
			} else {
				Response invalido = invalidarIntegracaoMoodle(request, horas);
				if(invalido != null) {
					return invalido;
				}
				
				List<Object> listaCursosVOs = realizarConsultaCursosTurmasDisciplinasModulos(horas);
				
				return enviarListaDeDadosParaMoodle(listaCursosVOs);
			}
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}

	@GET
	@Path("/materiais/{horas}")
	@Produces({ "application/json" })
	public Response consultarMateriaisAlteradasDentroIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {
		
		try {
			
			Response invalido = invalidarIntegracaoMoodle(request, horas);
			if(invalido != null) {
				return invalido;
			}
			
			List<Object> listaCursosVOs = realizarConsultaMateriaisTurmasDisciplinasModulos(horas);
			
			return enviarListaDeDadosParaMoodle(listaCursosVOs);
			
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}

	@GET
	@Path("/materiaisRemovidos/{horas}")
	@Produces({ "application/json" })
	public Response consultarMateriaisRemovidosDentroIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {
		
		try {
			
			Response invalido = invalidarIntegracaoMoodle(request, horas);
			if(invalido != null) {
				return invalido;
			}
			
			List<Object> listaCursosVOs = realizarConsultaMateriaisRemovido(horas);
			
			return enviarListaDeDadosParaMoodle(listaCursosVOs);
			
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}
	
	@GET
	@Path("/material/{material_codigo}")
	@Produces({ "application/json" })
	public Response consultarMaterialPorCodigo(@Context final HttpServletRequest request, @PathParam("material_codigo") Long material_codigo) {
		
		try {
			
			Response invalido = invalidarIntegracaoMoodle(request, 1);
			if(invalido != null) {
				return invalido;
			}
			
			ArquivoVO arquivo = realizarConsultaMaterialPorCodigo(material_codigo);
			ConfiguracaoGeralSistemaVO config = null;
			try {
				config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(arquivo == null || arquivo.getCodigo() == 0) {
				return Response.status(Response.Status.NO_CONTENT).build();
			}
			
			String urlAcesso = "";
			if (arquivo.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
				urlAcesso = servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivo.recuperarNomeArquivoServidorExterno(arquivo.getPastaBaseArquivo(), config.getLocalUploadArquivoTemp(), arquivo.getDescricao()));
				servidorArquivoOnlineS3RS = null;			
			} else {
				urlAcesso = config.getUrlExternoDownloadArquivo() + "/" + arquivo.recuperarNomeArquivoServidorExterno(arquivo.getPastaBaseArquivo(), config.getLocalUploadArquivoTemp(), arquivo.getNome());
			}
			
			config = null;
			
//			String imagemBase64 = Uteis.encodeFileToBase64Binary(arquivo.getPastaBaseArquivo() + File.separator + arquivo.getNome());
//			imagemBase64 = imagemBase64.replaceAll("\n", " ");
			Gson gson = new Gson();
//			String leadJson= gson.toJson(imagemBase64);
			String leadJson= gson.toJson(urlAcesso);
			
			return Response.status(Response.Status.OK).entity(leadJson).build();
			
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}
	
	@GET
	@Path("/vinculacoes/alunos/{horas}")
	@Produces({ "application/json" })
	public Response consultarMatriculaAlunoAlteradoDentroDoIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {

		try {
			if (horas == 99) {
				//Realiza alteração em dados para processamento
				getFacadeFactory().getHorarioTurmaFacade().alterarMatriculasSetandoDataAlteracaoInicial();
				return Response.status(Response.Status.OK).build();
			} else {
				Response invalido = invalidarIntegracaoMoodle(request, horas);
				if(invalido != null) {
					return invalido;
				}
				
				List<Object> listaCursosVOs = realizarConsultaVinculacaoAluno(horas);
				
				return enviarListaDeDadosParaMoodle(listaCursosVOs);
			}
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}
	
	@GET
	@Path("/vinculacoes/professores/{horas}")
	@Produces({ "application/json" })
	public Response consultarProfessoresAlteradoDentroDoIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {

		try {
			if (horas == 99) {
				//Realiza alteração em dados para processamento
				getFacadeFactory().getHorarioTurmaFacade().alterarProfessoresSetandoDataAlteracaoInicial();
				return Response.status(Response.Status.OK).build();
			} else {
				Response invalido = invalidarIntegracaoMoodle(request, horas);
				if(invalido != null) {
					return invalido;
				}
				
				List<Object> listaCursosVOs = realizarConsultaVinculacaoProfessor(horas);
				
				return enviarListaDeDadosParaMoodle(listaCursosVOs);
			}
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}
	
	@GET
	@Path("/vinculacoes/coordenadores/{horas}")
	@Produces({ "application/json" })
	public Response consultarCordenadoresAlteradoDentroDoIntervaloDeHoras(@Context final HttpServletRequest request, @PathParam("horas") Integer horas) {

		try {
			if (horas == 99) {
				//Realiza alteração em dados para processamento
				return Response.status(Response.Status.OK).build();
			} else {
				Response invalido = invalidarIntegracaoMoodle(request, horas);
				if(invalido != null) {
					return invalido;
				}
				
				List<Object> listaCursosVOs = realizarConsultaVinculacaoCoordenador(horas);
				
				return enviarListaDeDadosParaMoodle(listaCursosVOs);
			}
		} catch (Exception e) {
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/vinculacoes/notas")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Response atualizarNotaDosAlunos(@Context final HttpServletRequest request) {
		
		try {
			
			Response invalido = validarTokenIntegracaoMoodle(request);
			if(invalido != null) {
				return invalido;
			}
			RegistroWebserviceVO registroWebserviceVO =  new RegistroWebserviceVO(ServicoWebserviceEnum.VINCULACOES_NOTA);			
			registroWebserviceVO.setIp(request.getLocalAddr());
			registroWebserviceVO.setUsuario(request.getHeader("Authorization"));
			NotasRSVO notas = (NotasRSVO) retornarObjetoPreenchidoPeloJSON(request, NotasRSVO.class, "UTF-8", registroWebserviceVO);			
			Response response = enviarListaDeDadosParaMoodle(inserirNotasDosAlunos(notas), registroWebserviceVO);			
			getFacadeFactory().getRegistroWebserviceFacade().incluir(registroWebserviceVO);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/mensagens")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Response mensagemAlunos(@Context final HttpServletRequest request) {
		try {
			Response invalido = validarTokenIntegracaoMoodle(request);
			if (invalido != null) {
				return invalido;
			}
			MensagensRSVO mensagensRSVO = (MensagensRSVO) retornarObjetoMensagemPreenchidoPeloJSON(request, MensagensRSVO.class, "UTF-8");
			mensagensRSVO = realizarEnvioMensagemMoodle(mensagensRSVO);
			if (mensagensRSVO == null) {
				return Response.status(Response.Status.NO_CONTENT).build();
			}
			String leadJson = new Gson().toJson(mensagensRSVO);
			return Response.status(Response.Status.OK).entity(leadJson).build();
		} catch (Exception e) {
			e.printStackTrace();
			registrarExceptionSentry(e, null, null, null, Boolean.TRUE);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage() != null ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.").build();
		}
	}
	

	/**
	 * 
	 * @param request
	 * @param classe
	 * @param charset
	 * @return JSON polimorfico populado 
	 * @throws IOException
	 */
	public Object retornarObjetoPreenchidoPeloJSON(final HttpServletRequest request, Class<NotasRSVO> classe, String charset, RegistroWebserviceVO registroWebserviceVO) throws IOException {
		
		if(charset == null)
			charset = "UTF-8";
		
		List<String> lines = IOUtils.readLines(request.getInputStream(), Charset.forName(charset).toString());
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line);
		}

		Gson gson = new Gson();
		//IntegracaoMoodleRSLog integracaoMoodleRSLog = new IntegracaoMoodleRSLog();
		//integracaoMoodleRSLog.incluir("Entrada", "Notas", builder.toString());
		registroWebserviceVO.setJsonRecebido(builder.toString());
		return gson.fromJson(builder.toString(), classe);
	}

	public Object retornarObjetoMensagemPreenchidoPeloJSON(final HttpServletRequest request, Class<MensagensRSVO> classe, String charset) throws IOException {
		
		if(charset == null)
			charset = "UTF-8";
		
		List<String> lines = IOUtils.readLines(request.getInputStream(), Charset.forName(charset).toString());
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line);
		}
		
		Gson gson = new Gson();
		return gson.fromJson(builder.toString(), classe);
	}
	
	public Response enviarListaDeDadosParaMoodle(List<Object> listaObjetosVO) throws Exception {
		
		if(listaObjetosVO == null || listaObjetosVO.isEmpty()) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		
		Gson gson = new Gson();
		String leadJson= gson.toJson(listaObjetosVO);
		
		return Response.status(Response.Status.OK).entity(leadJson).build();
	}
	
	public Response enviarListaDeDadosParaMoodle(List<Object> listaObjetosVO, RegistroWebserviceVO registroWebserviceVO) throws Exception {
		
		if(listaObjetosVO == null || listaObjetosVO.isEmpty()) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		
		Gson gson = new Gson();
		String leadJson= gson.toJson(listaObjetosVO);
		registroWebserviceVO.setJsonRetorno(leadJson);
		return Response.status(Response.Status.OK).entity(leadJson).build();
	}

	public Response enviarDadosParaMoodle(Object objetoVO) {
		
		if(objetoVO == null) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		
		Gson gson = new Gson();
		String leadJson= gson.toJson(objetoVO);
		
		return Response.status(Response.Status.OK).entity(leadJson).build();
	}	
	
	/**
	 * Invalida operacao caso: <br>
	 *  token nao esteja preenchido <br> 
	 *  token seja invalido <br>
	 *  variavel horas nao preenchida ou seu valor e menor que zero <br>
	 * 
	 * @param request
	 * @param horas
	 * 
	 * @return
	 * Reponse = resposta invalida <br>
	 * null = dados sao validos <br> 
	 */
	private Response invalidarIntegracaoMoodle(final HttpServletRequest request, Integer horas) {
		
		Response response = validarTokenIntegracaoMoodle(request);
		if(response != null)
			return response;
		
		if(horas == null || horas < 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return null;
	}
	
	private Response validarTokenIntegracaoMoodle(final HttpServletRequest request) {
		
		String token = buscarTokenWebService();
		
		//Erro interno no servidor - Token Integracao Moodle nao informado
		if(token.isEmpty())
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
					
		//Valida se o token foi passado no Header da consulta
		if(request.getHeader("Authorization") == null || !token.equals(request.getHeader("Authorization"))) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return null;
	}
	
	private String buscarTokenWebService() {
		
		ConfiguracaoGeralSistemaVO config = null;
		try {
			config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(config == null || config.getTokenWebService().trim().isEmpty()) {
			return "";
		}
		
		return config.getTokenWebService();
	}


	public List<Object> realizarConsultaPessoas(Integer horas) {
		SqlRowSet tabelaResultado = getFacadeFactory().getPessoaFacade().consultarPessoasQueSofreramAlteracao(horas);
		
		List<Object> listaFilhosVOs = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			PessoasItemRSVO obj = new PessoasItemRSVO();
			obj.setCodigo(tabelaResultado.getString("codigo") != null ? tabelaResultado.getString("codigo") : "");
			obj.setNome(tabelaResultado.getString("nome") != null ? tabelaResultado.getString("nome") : "");
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setEmail(tabelaResultado.getString("email") != null ? tabelaResultado.getString("email") : "");
			obj.setSenha(tabelaResultado.getString("senha") != null ? tabelaResultado.getString("senha") : "");
			
			listaFilhosVOs.add(obj);
		}
		return listaFilhosVOs;
	}

	private Object realizarPessoaLogin(String token) {
		SqlRowSet tabelaResultado = getFacadeFactory().getMatriculaFacade().consultarPessoaLoginToken(token);
		
		if (tabelaResultado.next()) {
			PessoasItemRSVO obj = new PessoasItemRSVO();
			obj.setNome(tabelaResultado.getString("nome") != null ? tabelaResultado.getString("nome") : "");
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setEmail(tabelaResultado.getString("email") != null ? tabelaResultado.getString("email") : "");
			obj.setSenha(tabelaResultado.getString("senha") != null ? tabelaResultado.getString("senha") : "");
			
			return obj;
		}
		return null;
	}
	
	public List<Object> realizarConsultaCursosTurmasDisciplinasModulos(Integer horas) throws Exception {
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().consultarCursosQueSofreramAlteracao(horas);
		
		CursosItemRSVO cursosRSVO;
		List<Object> listaFilhosVOs = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			
			CursoItemRSVO cursoRSVO = new CursoItemRSVO();
			cursoRSVO.setCodigo(tabelaResultado.getInt("curso_codigo"));
			cursoRSVO.setNome(tabelaResultado.getString("curso_nome") != null ? tabelaResultado.getString("curso_nome") : "");
			cursoRSVO.setCodigoModalidade(tabelaResultado.getInt("modalidade_codigo"));
			cursoRSVO.setNomeModalidade(tabelaResultado.getString("modalidade_nome") != null ? tabelaResultado.getString("modalidade_nome") : "");
			if (cursoRSVO.getCodigoModalidade().intValue() == 7) {
				if (Uteis.isAtributoPreenchido(tabelaResultado.getString("modalidadecurso")) && tabelaResultado.getString("modalidadecurso").equals("ON_LINE")) {
					cursoRSVO.setCodigoModalidade(12);
					cursoRSVO.setNomeModalidade(cursoRSVO.getNomeModalidade() + " EAD");
				} else if (Uteis.isAtributoPreenchido(tabelaResultado.getString("modalidadecurso")) && tabelaResultado.getString("modalidadecurso").equals("HIBRIDO")) {
					cursoRSVO.setCodigoModalidade(14);
					cursoRSVO.setNomeModalidade("Blended");
				}

			} else if (cursoRSVO.getCodigoModalidade().intValue() == 10) {
				if (Uteis.isAtributoPreenchido(tabelaResultado.getString("modalidadecurso")) && tabelaResultado.getString("modalidadecurso").equals("ON_LINE")) {
					cursoRSVO.setCodigoModalidade(13);
					cursoRSVO.setNomeModalidade(cursoRSVO.getNomeModalidade() + " EAD");
				}
			}
			
			TurmaItemRSVO turmaRSVO = new TurmaItemRSVO();
			turmaRSVO.setCodigo(tabelaResultado.getInt("turma_codigo"));
			turmaRSVO.setNome(tabelaResultado.getString("turma_nome") != null ? tabelaResultado.getString("turma_nome") : "");
			turmaRSVO.setDataInicio(tabelaResultado.getTimestamp("turma_inicio") != null ? tabelaResultado.getTimestamp("turma_inicio") : null);
			turmaRSVO.setDataFim(tabelaResultado.getTimestamp("turma_fim") != null ? tabelaResultado.getTimestamp("turma_fim") : null);
			
			turmaRSVO.setInicio(turmaRSVO.getDataInicio() != null ? turmaRSVO.getDataInicio().getTime() : 0l);
			turmaRSVO.setFim(turmaRSVO.getDataFim() != null ? Uteis.getDataComHoraSetadaParaUltimoMinutoDia(turmaRSVO.getDataFim()).getTime() : 0l);
			turmaRSVO.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo_periodoletivo"));
			
			ModuloItemRSVO moduloRSVO = new ModuloItemRSVO();
			moduloRSVO.setCodigo(tabelaResultado.getInt("modulo_codigo"));
			moduloRSVO.setNome(tabelaResultado.getString("modulo_nome") != null ? tabelaResultado.getString("modulo_nome") : "");
			Integer codigoModalidade = 1;
			String situacaoModalidade = "Presencial";
			if (tabelaResultado.getString("modalidade_disciplina") != null && tabelaResultado.getString("modalidade_disciplina").equals("PRESENCIAL") && tabelaResultado.getBoolean("tipomodalidade_disciplina")) {
				codigoModalidade = 2;
				situacaoModalidade = "Semi Presencial";
				
			} else if (tabelaResultado.getString("modalidade_disciplina") != null && tabelaResultado.getString("modalidade_disciplina").equals("ON_LINE")) { 
				codigoModalidade = 3;
				situacaoModalidade = "Online";
			}
			moduloRSVO.setModulo_modalidade_codigo(codigoModalidade);
			moduloRSVO.setModulo_modalidade_nome(situacaoModalidade);
			Integer codigoTutoriaOnline = tabelaResultado.getInt("tutoriaonline");
			if (codigoTutoriaOnline != null && codigoTutoriaOnline > 0) {
				moduloRSVO.setDataInicio(tabelaResultado.getTimestamp("datainicioaula_ead") != null ? tabelaResultado.getTimestamp("datainicioaula_ead") : null);
				moduloRSVO.setDataFim(tabelaResultado.getTimestamp("dataterminoaula_ead") != null ? tabelaResultado.getTimestamp("dataterminoaula_ead") : null);
				
			} else {
				moduloRSVO.setDataInicio(tabelaResultado.getTimestamp("modulo_inicio") != null ? tabelaResultado.getTimestamp("modulo_inicio") : null);
				moduloRSVO.setDataFim(tabelaResultado.getTimestamp("modulo_fim") != null ? tabelaResultado.getTimestamp("modulo_fim") : null);
			}
			moduloRSVO.setInicio(moduloRSVO.getDataInicio() != null ? moduloRSVO.getDataInicio().getTime() : 0l);
			moduloRSVO.setFim(moduloRSVO.getDataFim() != null ? Uteis.getDataComHoraSetadaParaUltimoMinutoDia(moduloRSVO.getDataFim()).getTime() : 0l);
			moduloRSVO.setAno(tabelaResultado.getString("modulo_ano"));
			moduloRSVO.setSemestre(tabelaResultado.getString("modulo_semestre"));
			
			cursosRSVO = new CursosItemRSVO();
			cursosRSVO.setCursoRSVO(cursoRSVO);
			cursosRSVO.setTurmaRSVO(turmaRSVO);
			cursosRSVO.setModuloRSVO(moduloRSVO);
			
			listaFilhosVOs.add(cursosRSVO);
		}
		
		return listaFilhosVOs;
	}

	public List<Object> realizarConsultaMateriaisTurmasDisciplinasModulos(Integer horas) {
		SqlRowSet tabelaResultado = getFacadeFactory().getArquivoFacade().consultarMateriaisAlunosQueSofreramAlteracao(horas);
		//System.out.println(" Integração MOODLE ");
		List<Object> listaArquivos = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			
			MateriaisItemRSVO obj = new MateriaisItemRSVO();
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setCurso_codigo(tabelaResultado.getInt("curso_codigo"));
			obj.setTurma_codigo(tabelaResultado.getInt("turma_codigo"));
			obj.setModulo_codigo(tabelaResultado.getInt("modulo_codigo"));
			
			//obj.setMaterial_codigo(tabelaResultado.getInt("material_codigo"));
			String valorConcac = "" + tabelaResultado.getInt("material_codigo") + tabelaResultado.getInt("turma_codigo") + tabelaResultado.getInt("modulo_codigo"); 
			obj.setMaterial_codigo(Long.parseLong(valorConcac));
			obj.setCodigo_download(tabelaResultado.getInt("material_codigo"));
			
			obj.setMaterial_titulo(tabelaResultado.getString("material_titulo"));
			obj.setMaterial_descricao(tabelaResultado.getString("material_descricao"));
			obj.setMaterial_extensao(tabelaResultado.getString("material_extensao"));
			obj.setMaterial_datacriacao(tabelaResultado.getTimestamp("material_datacriacao") != null ? tabelaResultado.getTimestamp("material_datacriacao").getTime() : 0L);
			obj.setMaterial_dataatualizacao(tabelaResultado.getTimestamp("material_dataatualizacao") != null ? tabelaResultado.getTimestamp("material_dataatualizacao").getTime() : 0L);
//			obj.setMaterial_dataremocao(tabelaResultado.getTimestamp("material_dataremocao") != null ? tabelaResultado.getTimestamp("material_dataremocao").getTime() : 0L);
//			System.out.println(" --------------------------- " );
//			System.out.println(" CPF: " + tabelaResultado.getString("cpf"));
//			System.out.println(" dataCriacao: " + tabelaResultado.getTimestamp("material_datacriacao"));
//			System.out.println(" dataCriacao: " + tabelaResultado.getTimestamp("material_datacriacao").getTime());
//			System.out.println(" dataAtualizacao: " + tabelaResultado.getTimestamp("material_dataatualizacao"));
//			System.out.println(" dataAtualizacao: " + tabelaResultado.getTimestamp("material_dataatualizacao").getTime());
//			System.out.println(" --------------------------- " );
			listaArquivos.add(obj);
		}
		
		return listaArquivos;
	}

	public List<Object> realizarConsultaMateriaisRemovido(Integer horas) {
		SqlRowSet tabelaResultado = getFacadeFactory().getArquivoFacade().consultarArquivosQueForamExcluidos(horas);
		//System.out.println(" Integração MOODLE ");
		List<Object> listaArquivos = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			
			MateriaisItemRSVO obj = new MateriaisItemRSVO();
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setCurso_codigo(tabelaResultado.getInt("curso_codigo"));
			obj.setTurma_codigo(tabelaResultado.getInt("turma_codigo"));
			obj.setModulo_codigo(tabelaResultado.getInt("modulo_codigo"));
			
			//obj.setMaterial_codigo(tabelaResultado.getInt("material_codigo"));
			String valorConcac = "" + tabelaResultado.getInt("material_codigo") + tabelaResultado.getInt("turma_codigo") + tabelaResultado.getInt("modulo_codigo"); 
			obj.setMaterial_codigo(Long.parseLong(valorConcac));
			obj.setCodigo_download(tabelaResultado.getInt("material_codigo"));
			
			obj.setMaterial_titulo(tabelaResultado.getString("material_titulo"));
			obj.setMaterial_descricao(tabelaResultado.getString("material_descricao"));
			obj.setMaterial_extensao(tabelaResultado.getString("material_extensao"));
			obj.setMaterial_datacriacao(tabelaResultado.getTimestamp("material_datacriacao") != null ? tabelaResultado.getTimestamp("material_datacriacao").getTime() : 0L);
			obj.setMaterial_dataatualizacao(tabelaResultado.getTimestamp("material_dataatualizacao") != null ? tabelaResultado.getTimestamp("material_dataatualizacao").getTime() : 0L);
			listaArquivos.add(obj);
		}
		
		return listaArquivos;
	}

	private ArquivoVO realizarConsultaMaterialPorCodigo(Long material_codigo) {
		try {
			return getFacadeFactory().getArquivoFacade().consultarPorCodigoIntegracaoMoodle(material_codigo, Uteis.NIVELMONTARDADOS_TODOS, null);
		} catch (Exception e) {
			IntegracaoMoodleRSLog integracaoMoodleRSLog = new IntegracaoMoodleRSLog();
			integracaoMoodleRSLog.incluir("Saida", "Material", material_codigo.toString() + " - " + e.getMessage());
			return new ArquivoVO();
		}
	}

	
	 
	public List<Object> realizarConsultaVinculacaoAluno(Integer horas) throws Exception {
		
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().consultarVinculacaoAlunoAlterado(horas);
		
		List<Object> listaAluno = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			VinculosItemRSVO obj = new VinculosItemRSVO();
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setCodigoCurso(tabelaResultado.getInt("curso_codigo"));
			obj.setCodigoTurma(tabelaResultado.getInt("turma_codigo"));
			obj.setCodigoModulo(tabelaResultado.getInt("modulo_codigo"));
			obj.setDataInicio(tabelaResultado.getTimestamp("vinculo_inicio"));
			obj.setDataConfirmacao(tabelaResultado.getTimestamp("data_confirmacao") != null ? tabelaResultado.getTimestamp("data_confirmacao") : null);
			obj.setDataFim(tabelaResultado.getTimestamp("vinculo_fim") != null ? tabelaResultado.getTimestamp("vinculo_fim") : null);
			
			obj.setVinculoInicio(obj.getDataInicio().getTime());
			obj.setConfirmacaoMatricula(obj.getDataConfirmacao().getTime());
			obj.setVinculoFim(obj.getDataFim() != null ? Uteis.getDataComHoraSetadaParaUltimoMinutoDia(obj.getDataFim()).getTime() : 0l);
			
			obj.setCodigoSituacao(tabelaResultado.getInt("codigoSituacao"));
			obj.setSituacaoMatricula(tabelaResultado.getString("situacaoMatricula"));
			
			obj.setCursoCodigoOrigem(tabelaResultado.getInt("curso_codigoorigem"));
			obj.setCodigoReposicao(tabelaResultado.getInt("codigo_reposicao"));
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo_periodoletivo"));
			obj.setAno(tabelaResultado.getString("vinculo_ano"));
			obj.setSemestre(tabelaResultado.getString("vinculo_semestre"));
			
			listaAluno.add(obj);
		}
		return listaAluno;
	}
	
	private List<Object> realizarConsultaVinculacaoProfessor(Integer horas) throws Exception {
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().consultarVinculacaoProfessorAlterado(horas);
		
		List<Object> listaProfessor = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			VinculosItemRSVO obj = new VinculosItemRSVO();
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setCodigoCurso(tabelaResultado.getInt("curso_codigo"));
			obj.setCodigoTurma(tabelaResultado.getInt("turma_codigo"));
			obj.setCodigoModulo(tabelaResultado.getInt("modulo_codigo"));
			obj.setDataInicio(tabelaResultado.getTimestamp("vinculo_inicio"));
			obj.setDataFim(tabelaResultado.getTimestamp("vinculo_fim") != null ? tabelaResultado.getTimestamp("vinculo_fim") : null);
			obj.setVinculoInicio(obj.getDataInicio().getTime());
			obj.setVinculoFim(obj.getDataFim() != null ? Uteis.getDataComHoraSetadaParaUltimoMinutoDia(obj.getDataFim()).getTime() : 0l);			
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo_periodoletivo"));
			obj.setAno(tabelaResultado.getString("vinculo_ano"));
			obj.setSemestre(tabelaResultado.getString("vinculo_semestre"));
			
			listaProfessor.add(obj);
		}

		return listaProfessor;
	}
	
	public List<Object> realizarConsultaVinculacaoCoordenador(Integer horas) throws Exception {
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().consultarVinculacaoCoordenador(horas);
		
		List<Object> listaCoordenador = new ArrayList<Object>(0);
		while (tabelaResultado.next()) {
			VinculosItemRSVO obj = new VinculosItemRSVO();
			obj.setCpf(tabelaResultado.getString("cpf") != null ? Uteis.retirarMascaraCPF(tabelaResultado.getString("cpf")) : "");
			obj.setCodigoCurso(tabelaResultado.getInt("curso_codigo"));
			obj.setCodigoTurma(tabelaResultado.getInt("turma_codigo"));
			obj.setCodigoModulo(tabelaResultado.getInt("modulo_codigo"));
			obj.setDataInicio(tabelaResultado.getTimestamp("vinculo_inicio"));
			obj.setDataFim(tabelaResultado.getTimestamp("vinculo_fim") != null ? tabelaResultado.getTimestamp("vinculo_fim") : null);
			obj.setVinculoInicio(obj.getDataInicio().getTime());
			obj.setVinculoFim(obj.getDataFim() != null ? Uteis.getDataComHoraSetadaParaUltimoMinutoDia(obj.getDataFim()).getTime() : 0l);			
			obj.setPeriodoLetivo(tabelaResultado.getInt("periodoletivo_periodoletivo"));
			obj.setAno(tabelaResultado.getString("vinculo_ano"));
			obj.setSemestre(tabelaResultado.getString("vinculo_semestre"));
			listaCoordenador.add(obj);
		}
		return listaCoordenador;
	}
	
	/**
	 * Metodo responsavel por atualizar as notas e a frequencia do aluno com dado nos campos passado via REST pelo Moodle
	 * @param notas
	 * @return
	 * @throws Exception
	 */
	private List<Object> inserirNotasDosAlunos(NotasRSVO notas) throws Exception {
		List<Object> notasResposta = null;
		NotasRSVO resultadoNotas = null;
		ConfiguracaoGeralSistemaVO config = null;
		UsuarioVO usuarioVO = null;
		Uteis.checkState(!(Objects.nonNull(notas) && Uteis.isAtributoPreenchido(notas.getNotas())), "A lista de notas a serem inseridas devem ser informadas");
		try {
			config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().incluirOperacaoMoodleBaseNotasRSVO(notas, usuarioVO);
			notasResposta = new ArrayList<>();
			resultadoNotas = new NotasRSVO();
			resultadoNotas.setNotas(null);
			resultadoNotas.setNotasInseridas(notas.getNotas());
			resultadoNotas.setNotasNaoInseridas(null);
			notasResposta.add(resultadoNotas);
			return notasResposta;
		} finally {
			if (Objects.nonNull(notasResposta)) {
				notasResposta = null;
			}
			if (Objects.nonNull(resultadoNotas)) {
				resultadoNotas = null;
			}
			if (Objects.nonNull(config)) {
				config = null;
			}
			if (Objects.nonNull(usuarioVO)) {
				usuarioVO = null;
			}
			if (Objects.nonNull(notas)) {
				notas = null;
			}
		}
	}

	
	public MensagensRSVO realizarEnvioMensagemMoodle(MensagensRSVO mensagensRSVO) throws Exception {
		if (mensagensRSVO == null) {
			return mensagensRSVO;
		}
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
		int i = 1;
		int quantidadeEmailsLocalizados = 0;
		try {
			mensagensRSVO.inicializarDadosRetornoEnvioMensagens();
			for (MensagensItemRSVO mensagensItemRSVO : mensagensRSVO.getMensagens()) {
				validarDadosMensagemMoodle(mensagensItemRSVO);
				List<String> emailsLocalizados = Stream.of(mensagensItemRSVO.getEmailDestinatarios().replace(" ", "").split(",")).collect(Collectors.toList());
				if (Uteis.isAtributoPreenchido(emailsLocalizados)) {
					quantidadeEmailsLocalizados = quantidadeEmailsLocalizados + emailsLocalizados.size();
				}
				i++;
			}
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().incluirOperacaoMoodleBaseMensagensRSVO(mensagensRSVO, usuarioVO);
			mensagensRSVO.setNumeroMensagensEnviadas(quantidadeEmailsLocalizados);
			mensagensRSVO.setEmailsNaoLocalizados(new ArrayList<>(0));
			return mensagensRSVO.getRetornoWebService();
		} catch (Exception e) {
			if (Uteis.isAtributoPreenchido(e.getMessage())) {
				throw new Exception("Erro no envio da " + i + "ª mensagem." + e.getMessage());
			}
			throw e;
		}
	}



	private void validarDadosMensagemMoodle(MensagensItemRSVO mensagensItemRSVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(mensagensItemRSVO.getAssunto())) {
			throw new Exception("Deve ser informado o campo Assunto.");
		}
		if (!Uteis.isAtributoPreenchido(mensagensItemRSVO.getMensagem())) {
			throw new Exception("Deve ser informado o campo Mensagem.");
		}
		if (!Uteis.isAtributoPreenchido(mensagensItemRSVO.getEmailRemetente())) {
			throw new Exception("Deve ser informado o campo Email Remetente.");
		}
		if (!Uteis.isAtributoPreenchido(mensagensItemRSVO.getEmailDestinatarios())) {
			throw new Exception("Deve ser informado o campo Email Destinatários.");
		}
		if (mensagensItemRSVO.getEnviarSms() && !Uteis.isAtributoPreenchido(mensagensItemRSVO.getSms())) {
			throw new Exception("Deve ser informado o campo Mensagem SMS.");
		}
	}


	public List<PessoaVO> obterListaPessoaDestinatarios(String emailDestinatarios, String tipoPessoa, UsuarioVO usuarioVO) throws Exception {
		List<PessoaVO> listDestinatario = new ArrayList<>();
		String[] emails = emailDestinatarios.replace(" ", "").split(",");
		for (String email : emails) {
			if (Uteis.isAtributoPreenchido(email) && Uteis.getValidaEmail(email)) {
				PessoaVO pessoaVO = null;
				if (Uteis.isAtributoPreenchido(pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorEmail(email, tipoPessoa, true, false, 
						Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuarioVO)) && listDestinatario.add(pessoaVO));
			}
		}
		return listDestinatario;
	}

	public List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(List<PessoaVO> pessoaVOs, UsuarioVO usuario) {
		List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<>();
		for (PessoaVO pessoaVO : pessoaVOs) {
			ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
			destinatario.setCiJaLida(Boolean.FALSE);
			destinatario.setDestinatario(pessoaVO);
			destinatario.setEmail(pessoaVO.getEmail());
			destinatario.setNome(pessoaVO.getNome());
			destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			listDestinatario.add(destinatario);
		}
		return listDestinatario;
	}
	
	public String obterMensagemFormatadaMensagemConvocacaoEnade(PessoaVO pessoaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
//		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), pessoaVO.getNome());
		return mensagemTexto;
	}

}