package webservice.servicos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.faces.model.SelectItem;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.JakartaServletDiskFileUpload;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
//import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
//import jobs.JobProvaProcessoSeletivo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.FiliacaoVO;
//import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
//import negocio.comuns.academico.HorarioProfessorDiaVO;
//import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EnderecoVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
//import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
//import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
//import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
//import negocio.comuns.financeiro.ContaReceberVO;
//import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
//import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
//import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
//import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
//import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
//import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.QuestionarioVO;
//import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PlataformaEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
//import negocio.facade.jdbc.processosel.DisciplinasGrupoDisciplinaProcSeletivo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
//import relatorio.controle.financeiro.ComprovanteRecebimentoRelControle;
//import relatorio.controle.processosel.ComprovanteInscricaoRelControle;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
//import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import webservice.AlwaysListTypeAdapterFactory;
import webservice.arquitetura.ControleConsultaRS;
//import webservice.aws.s3.ServidorArquivoOnlineS3RS;
//import webservice.pix.comuns.PixRSVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;
import webservice.servicos.excepetion.WebServiceException;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.AvaliacaoInstitucionalRSVO;
import webservice.servicos.objetos.DataEventosRSVO;
import webservice.servicos.objetos.DisciplinaRSVO;
import webservice.servicos.objetos.DocumentacaoMatriculaRSVO;
import webservice.servicos.objetos.LancamentoNotaRSVO;
import webservice.servicos.objetos.ProcessoSeletivoRSVO;
import webservice.servicos.objetos.QuantidadeObjetoExistentesRSVO;
import webservice.servicos.objetos.RequerimentoTurmaReposicaoRSVO;
import webservice.servicos.objetos.SelectComboRSVO;
import webservice.servicos.objetos.UrlRSVO;


/**
 * @author Victor Hugo de Paula Costa - 21 de set de 2016
 *
 */
@Service
@Path("/aplicativoSEISV")
public class AplicativoSEISV extends SuperControle implements Serializable {
	/**
	 * @author Victor Hugo de Paula Costa - 21 de set de 2016
	 */
	private static final long serialVersionUID = 1L;
	@Context
	private HttpServletRequest request;
	public static final String AUTHENTICATION_HEADER = "Authorization";
	public static final String NOME_SEI_SIGNATURE = "SEI_SIGNATURE";
	
//	private RenovarMatriculaControle renovarMatriculaControle;
//	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/realizarAutenticacaoLoginSEI")
	public Response realizarAutenticacaoLoginSEI( UsuarioVO usuarioLogin) {
		try {
			UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(usuarioLogin.getUsername(), usuarioLogin.getSenha(), true, Uteis.NIVELMONTARDADOS_DADOSLOGIN);
			Uteis.checkState((Uteis.isAtributoPreenchido(usuarioLogin.getNomeAplicativo()) && usuarioLogin.getNomeAplicativo().equals(NOME_SEI_SIGNATURE)
					&& !usuarioLogin.getVersaoAplicativo().equals(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarVesaoSeiSignature())),"A versão do SEI SIGNATURE está desatualizada, acesse o SEI e baixe novamente o aplicativo.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioVO), "Usuário e/ou senha incorretos!");
			usuarioVO.setIpMaquinaLogada(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarTokenWebServicePadraoSistema());
			usuarioVO.setSenha("");
			usuarioVO.setPerfilAcesso(new PerfilAcessoVO());
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/realizarLoginSEI/{token}/{device}")
	public Response realizarLoginSEI(@PathParam("token") final String token, @PathParam("device") final String device) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			definirVisaoLogar(usuarioVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario(usuarioVO.getPessoa().getCodigo(), false, usuarioVO, configuracaoGeralSistemaVO);
			if (matriculaVOs.isEmpty()) {
				throw new Exception("Não Foi Encontrada Uma Matrícula Para Este Usuário");
			} else {
				montarDadosMatriculaAcessoAluno(usuarioVO, matriculaVOs.get(0));
				Iterator<MatriculaVO> i = matriculaVOs.iterator();
				while (i.hasNext()) {
					MatriculaVO matriculaVO = (MatriculaVO) i.next();
					matriculaVO.setSituacao(matriculaVO.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo());
					if (!matriculaVO.getUnidadeEnsino().getCodigo().equals(0)) {
						matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
						if (!matriculaVO.getUnidadeEnsino().getConfiguracoes().getCodigo().equals(0)) {
							usuarioVO.setUnidadeEnsinoLogado(matriculaVO.getUnidadeEnsino());
							usuarioVO.setUrlLogoUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().renderizarLogoAplicativo(configuracaoGeralSistemaVO, matriculaVO.getUnidadeEnsino().getCaminhoBaseLogoAplicativo(), matriculaVO.getUnidadeEnsino().getNomeArquivoLogoAplicativo()));
						}
					}
					break;
			}
			}
			usuarioVO.getMatriculaVOs().addAll(matriculaVOs);
			if(!matriculaVOs.isEmpty()) {
				realizarDefinicoesUsuarioLogado(usuarioVO, matriculaVOs.get(0), configuracaoGeralSistemaVO);
			}
			usuarioVO.getMatriculaVOs().forEach(m -> {
				m.setConsultor(null);
				m.setFormacaoAcademica(null);
				m.setAutorizacaoCurso(null);
				m.setUsuario(null);
				UtilReflexao.removerCamposChamadaAPI(m.getCurso(), "codigo", "nome", "nivelEducacional", "periodicidade");
				UtilReflexao.removerCamposChamadaAPI(m.getUnidadeEnsino(), "codigo", "nome");				
				UtilReflexao.removerCamposChamadaAPI(m.getTurno(), "codigo", "nome");				
			});
			if (!token.equals("null") && !device.equals("undefined")) {
				usuarioVO.setTokenAplicativo(token);
				usuarioVO.setCelular(PlataformaEnum.valueOf(device));
				getFacadeFactory().getUsuarioFacade().alterarTokenEPlataformaAplicativo(usuarioVO);
			}
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(usuarioVO);
			return Response.status(Status.OK).entity(json).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	private void realizarDefinicoesUsuarioLogado(UsuarioVO usuarioVO, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		usuarioVO.setCaminhoFotoUsuarioAplicativo(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(usuarioVO.getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));		
		if(configuracaoGeralSistemaVO.getApresentarMensagemAlertaAlunoNaoAssinouContrato() && !matriculaVO.getMatricula().isEmpty()) {	
			matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(configuracaoGeralSistemaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
			matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(matriculaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula().replaceAll("\\r\\n|\\r|\\n", " "));
		}
		matriculaVO.setAlunoNaoAssinouContratoMatricula(!getFacadeFactory().getDocumetacaoMatriculaFacade().validaAlunoAssinouContratoMatricula(matriculaVO.getMatricula()));
		consultarDocumentoPendenteAssinatura(matriculaVO, usuarioVO);
		if (usuarioVO.getVisaoLogar().equals("aluno")) {
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO), matriculaVO, matriculaVO.getAlunoNaoAssinouContratoMatricula(), usuarioVO ));
		} else {
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoPais(getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO), usuarioVO,matriculaVO));
		}
		
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoVizualizarMateriaisPeriodoConcluido", usuarioVO);
			usuarioVO.setPermitirAlunoVizualizarMateriaisPeriodoConcluido(true);				
		} catch (Exception e) {
			usuarioVO.setPermitirAlunoVizualizarMateriaisPeriodoConcluido(false);
		}
		usuarioVO.setPermissaoAcessoMenuVO(getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(usuarioVO.getPerfilAcesso()));
		usuarioVO.setConfiguracaoGeralSistemaVO((ConfiguracaoGeralSistemaVO)configuracaoGeralSistemaVO.clone());
//		usuarioVO.setParceiro(null);			
		matriculaVO.getMatriculaPeriodoVO().setMatriculaVO(null);
		matriculaVO.setMatriculaPeriodoVOs(null);
		UtilReflexao.removerCamposChamadaAPI(usuarioVO.getPessoa(), "codigo", "nome", "arquivoImagem", "cpf", "celular", "email", "urlFotoAluno", "possuiAcessoVisaoPais", "aluno");
		UtilReflexao.removerCamposChamadaAPI(usuarioVO.getUnidadeEnsinoLogado(), "codigo", "nome");
		usuarioVO.setPerfilAcesso((PerfilAcessoVO)usuarioVO.getPerfilAcesso().clone());
		UtilReflexao.removerCamposChamadaAPI(usuarioVO.getPerfilAcesso(), "codigo", "nome");
		UtilReflexao.removerCamposChamadaAPI(usuarioVO.getConfiguracaoGeralSistemaVO(), "codigo", "nome", "layoutIdentificacaoEstudantil", "quantidadeCaracteresMinimoSenhaUsuario", "nivelSegurancaNumero", "nivelSegurancaLetra", "nivelSegurancaLetraMaiuscula" , "nivelSegurancaCaracterEspecial", "nivelcontrolealteracaosenha", "primeiroAcessoAlunoResetarSenha", "primeiroAcessoProfessorResetarSenha");
		UtilReflexao.removerCamposChamadaAPI(usuarioVO.getPermissaoAcessoMenuVO(), "requerimento", "listaExercicioAluno", "comunicacaoInterna", "disciplina", "entregaDocumento", "meusHorarios", "minhasNotas", "downloadArquivo", "documentosDigitais", "minhasContasPagar", "meusContratos", "identificacaoEstudantil", "configuracoesAlteracaoSenha", "configuracoesAlteracaoFoto", "advertencia", "permitirRegistrarAulaRetroativo", "permitirLancarNotaRetroativo", "tarefa", "documentosDigitais");
		
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/realizarLoginSEIProfessor/{token}/{device}/{tipoPessoa}")
	public Response realizarLoginSEIProfessor(@PathParam("token") final String token, @PathParam("device") final String device, @PathParam("tipoPessoa") final String tipoPessoa) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			if (!usuarioVO.getPessoa().getProfessor()) {
				throw new Exception("Apenas Professores Podem Acessar Este Aplicativo.");
			}
			if (Uteis.isAtributoPreenchido(tipoPessoa) && tipoPessoa.equals("professor")) {
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().definirPerfilAcessoParaAlunoProfessorECandidato("professor", configuracaoGeralSistemaVO));
				PermissaoAcessoMenuVO permissaoAcessoMenuVO = getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(usuarioVO.getPerfilAcesso());
				usuarioVO.setPermitirLancarNotaRetroativoMobile(permissaoAcessoMenuVO.getPermitirLancarNotaRetroativo());
				usuarioVO.setPermitirRegistrarAulaRetroativoMobile(permissaoAcessoMenuVO.getPermitirRegistrarAulaRetroativo());
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				//Utilizando atributo ipmaquina temporariamente para armazenar matrícula até obter autorização para criação de novo atributo no usuarioVO.
				usuarioVO.setIpMaquinaLogada(funcionarioVO.getMatricula());
			} 
			usuarioVO.setCaminhoFotoUsuarioAplicativo(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(usuarioVO.getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(unidadeEnsinoVO, NivelMontarDados.TODOS, usuarioVO);
			}
			usuarioVO.setUnidadeEnsinoLogado(unidadeEnsinoVO);
			usuarioVO.setUrlLogoUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().renderizarLogoAplicativo(configuracaoGeralSistemaVO, unidadeEnsinoVO.getCaminhoBaseLogoAplicativo(), unidadeEnsinoVO.getNomeArquivoLogoAplicativo()));
			if (!token.equals("null") && !device.equals("undefined")) {
				usuarioVO.setTokenAplicativo(token);
				usuarioVO.setCelular(PlataformaEnum.valueOf(device));
				getFacadeFactory().getUsuarioFacade().alterarTokenEPlataformaAplicativo(usuarioVO);
			}
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deslogar/{token}/{device}")
	public Response deslogar(@PathParam("token") final String token, @PathParam("device") final String device) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			if (!token.equals("null") && !device.equals("undefined")) {
				usuarioVO.setTokenAplicativo(null);
				usuarioVO.setCelular(null);
				getFacadeFactory().getUsuarioFacade().alterarTokenEPlataformaAplicativo(usuarioVO);
			}
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/trocarMatriculaAluno/{matricula}")
	public Response trocarMatriculaAlunoDepreciado(@PathParam("matricula") final String matricula) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
			montarDadosMatriculaAcessoAluno(usuarioVO, matriculaVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, usuarioVO.getCodigoUnidadeEnsinoMatriculaLogado());
			
			if(configuracaoGeralSistemaVO.getApresentarMensagemAlertaAlunoNaoAssinouContrato()) {	
				matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(configuracaoGeralSistemaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
				matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(matriculaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula().replaceAll("\\r\\n|\\r|\\n", " "));
			}
			
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula())) {
				matriculaVO.setAlunoNaoAssinouContratoMatricula(getFacadeFactory().getDocumetacaoMatriculaFacade().validaAlunoAssinouContratoMatricula(matriculaVO.getMatricula()));
			}
			
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(configuracaoGeralSistemaVO, matriculaVO, matriculaVO.getAlunoNaoAssinouContratoMatricula(), usuarioVO));
			if (!matriculaVO.getUnidadeEnsino().getCodigo().equals(0)) {
				matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
				if (!matriculaVO.getUnidadeEnsino().getConfiguracoes().getCodigo().equals(0)) {
					usuarioVO.setUnidadeEnsinoLogado(matriculaVO.getUnidadeEnsino());
					usuarioVO.setUrlLogoUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().renderizarLogoAplicativo(configuracaoGeralSistemaVO, matriculaVO.getUnidadeEnsino().getCaminhoBaseLogoAplicativo(), matriculaVO.getUnidadeEnsino().getNomeArquivoLogoAplicativo()));
				}
			}
			usuarioVO.setCaminhoFotoUsuarioAplicativo(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(usuarioVO.getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarMinhasNotasAluno/{matricula}/{codigoPerfilAcesso}")
	public Response carregarMinhasNotasAluno(@PathParam("matricula") final String matricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matricula, false, usuarioVO));
			matriculaVO.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoPorMatriculaCurso(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaBasicaHistoricoAplicativo(matriculaVO, usuarioVO);
			if(matriculaVO.getCurso().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				Ordenacao.ordenarLista(historicoVOs, "dataPrimeiraAula");				
			} else {
				Ordenacao.ordenarListaDecrescente(historicoVOs, "ordenacaoMinhasNotasAplicativo");				
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<HistoricoVO>>(historicoVOs) {}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarEventosCalendarioAluno/{matricula}/{codigoPerfilAcesso}/{codigoUnidadeEnsinoMatricula}")
	public Response carregarEventosCalendarioAluno(@PathParam("matricula") final String matricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(codigoUnidadeEnsinoMatricula);
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaAtiva(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
			List<DataEventosRSVO> dataEventosRSVOs = new ArrayList<>();
//			if (!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
//				dataEventosRSVOs = getFacadeFactory().getHorarioAlunoFacade().consultarDatasMeusHorariosAlunoEspecificoAplicativo(matriculaPeriodoTurmaDisciplinaVOs, null, null, true, null, usuarioVO);
//				dataEventosRSVOs.addAll(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosEADAplicativo(matriculaPeriodoTurmaDisciplinaVOs));
//			}
			for (DataEventosRSVO dataEventosRSVO : dataEventosRSVOs) {
				if (Uteis.isAtributoPreenchido(dataEventosRSVO.getData())) {
					dataEventosRSVO.setData(Uteis.getDataAdicionadaEmHoras(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataEventosRSVO.getData()), 12));
				}
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<DataEventosRSVO>>(dataEventosRSVOs) {}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarAgendaAlunoDia/{matricula}/{data}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response carregarAgendaAlunoDia(@PathParam("matricula") final String matricula, @PathParam("data") final Date data, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO.setCodigo(codigoUnidadeEnsinoMatricula);
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaAtiva(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
//			List<AgendaAlunoRSVO> agendaAlunoRSVOs = getFacadeFactory().getHorarioAlunoFacade().consultarAgendaAlunoDia(matriculaPeriodoTurmaDisciplinaVOs, data, unidadeEnsinoVO, usuarioVO);
//			return Response.status(Status.OK).entity(new GenericEntity<List<AgendaAlunoRSVO>>(agendaAlunoRSVOs) {}).build();
			return Response.status(Status.OK).entity(new GenericEntity<List<AgendaAlunoRSVO>>(new ArrayList<AgendaAlunoRSVO>()) {}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarDetalhesMinhasNotasAluno/{matricula}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}/{codigoHistorico}")
	public Response carregarDetalhesMinhasNotasAluno(@PathParam("matricula") final String matricula, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoHistorico") final Integer codigoHistorico) throws Exception {
		try {
			if(!Uteis.isAtributoPreenchido(codigoHistorico)){
				throw new Exception("Código histórico não informado.");
			}
			if(!Uteis.isAtributoPreenchido(codigoPerfilAcesso)){
				throw new Exception("Código perfil acesso não informado.");
			}
			if(!Uteis.isAtributoPreenchido(matricula)){
				throw new Exception("Matrícula não informado.");
			}
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			matriculaVO.getCurso().setNivelEducacional(tipoNivelEducacional);
			matriculaVO.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoPorMatriculaCurso(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().executarMontagemListaHistoricoAlunoPorCodigoHistorico(codigoHistorico, matriculaVO, null, matriculaVO.getCurso().getConfiguracaoAcademico(), matriculaVO.getCurso(), null, "", true, true, false , usuarioVO);
			HistoricoVO historicoVO = new HistoricoVO();
//			if(historicoVOs != null && !historicoVOs.isEmpty()){
//				CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
//				historicoVOs.stream().filter(HistoricoVO::getHistoricoDisciplinaAproveitada).forEach(h -> h.setCalendarioLancamentoNotaVO(calendarioLancamentoNotaVO));
//				historicoVO = historicoVOs.get(0);
//				List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs = getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().consultarPorHistorico(historicoVO, usuarioVO);
//				if(Uteis.isAtributoPreenchido(historicoVO.getCalendarioLancamentoNotaVO())
//						&& !historicoVO.getCalendarioLancamentoNotaVO().getApresentarCalculoMediaFinalVisaoAluno()) {
//					historicoVO.setMediaFinal(null);
//					historicoVO.setMediaFinalConceito(null);
//					historicoVO.setMediaFinalTexto("");						
//				}
//				
//				if (historicoVO.getConfiguracaoAcademico().getOcultarMediaFinalDisciplinaCasoReprovado()) {
//					if (historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor())) {
//						historicoVO.setMediaFinal(null);
//						historicoVO.setMediaFinalConceito(null);
//						historicoVO.setMediaFinalTexto("");
//					}
//				}
//				
//				if((Uteis.isAtributoPreenchido(historicoVO.getCalendarioLancamentoNotaVO())
//						&& !historicoVO.getCalendarioLancamentoNotaVO().getApresentarCalculoMediaFinalVisaoAluno()) 
//						|| !historicoVO.getApresentarSituacaoAplicandoRegraConfiguracaoAcademica()) {
//					historicoVO.setSituacao(" ");
//				}
//				boolean permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_APRESENTAR_TODAS_NOTAS_PARAMETRIZADAS_CONFIGURACAO_ACADEMICA.getValor(), 
//						usuarioVO);
//				historicoVO.montaListaNotas(false, turmaDisciplinaNotaTituloVOs, permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica);
//			}
			return Response.status(Status.OK).entity(historicoVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/co/{codigoPessoa}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{limit}/{offset}/{codigoPerfilAcesso}")
	public Response carregarComunicadosAluno(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("limit") final Integer limit, @PathParam("offset") final Integer offset, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("aluno");
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, codigoUnidadeEnsinoMatricula);
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			List<ComunicacaoInternaVO> objs = new ArrayList<>();
			objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorSituacaoEntradaLimitOffset(codigoPessoa, "entrada", null, true, null, null, true, Uteis.NIVELMONTARDADOS_TODOS, limit, offset, usuarioVO);
			for (ComunicacaoInternaVO comunicacaoInternaVO : objs) {
				comunicacaoInternaVO.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().redimensionarTextoHTMLAplicativo(comunicacaoInternaVO, getCaminhoWeb(usuarioVO), comunicacaoInternaVO.getMensagem()));
				comunicacaoInternaVO.getResponsavel().setUrlFotoAluno(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(comunicacaoInternaVO.getResponsavel().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));
				comunicacaoInternaVO.getResponsavel().setCelular("(00)00000-0000");
				comunicacaoInternaVO.getResponsavel().setCPF("00000000000");
				comunicacaoInternaVO.getResponsavel().setEmail("suporte@sei.com");
				comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagem().replaceAll("\\r\\n|\\r|\\n", " "));
				comunicacaoInternaVO.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(comunicacaoInternaVO.getMensagem(), usuarioVO.getPessoa()));
				comunicacaoInternaVO.setAssunto(comunicacaoInternaVO.getAssunto().replaceAll("\\r\\n|\\r|\\n", " "));
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<ComunicacaoInternaVO>>(objs) {}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarQuantidadeComunicadosExistentesAluno/{codigoPessoa}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response consultarQuantidadeComunicadosExistentesAluno(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("aluno");
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			QuantidadeObjetoExistentesRSVO quantidadeExistentes = new QuantidadeObjetoExistentesRSVO();
			quantidadeExistentes.setValor(getFacadeFactory().getComunicacaoInternaFacade().consultaQuantidadeRapidaPorSituacaoEntrada(codigoPessoa, "entrada", null, true, null, null, usuarioVO));
			return Response.status(Status.OK).entity(quantidadeExistentes).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/consultarRequerimentoAluno/{codigoPessoa}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response consultarRequerimentoV2(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		return consultarRequerimento(codigoPessoa, tipoNivelEducacional, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso, "V2");
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarRequerimentoAluno/{codigoPessoa}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response consultarRequerimentoV1(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		return consultarRequerimento(codigoPessoa, tipoNivelEducacional, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso, "V1");
	}
	public Response consultarRequerimento(Integer codigoPessoa, String tipoNivelEducacional, Integer codigoUnidadeEnsinoMatricula, Integer codigoPerfilAcesso, String versao) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("aluno");
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, codigoUnidadeEnsinoMatricula);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsinoMatricula, null);
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			List<RequerimentoVO> requerimentoVOs = new ArrayList<>(0);
			requerimentoVOs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorCodigoAluno(0, codigoPessoa, codigoUnidadeEnsinoMatricula, true, usuarioVO, configuracaoGeralSistemaVO);
//			for (RequerimentoVO requerimentoVO : requerimentoVOs) {
//				requerimentoVO.setAptoImpressaoVisaoAluno(getFacadeFactory().getRequerimentoFacade().validarApresentarBotaoImprimirVisaoAluno(requerimentoVO));
//				requerimentoVO.setEdicao(true);
//				if (Uteis.isAtributoPreenchido(requerimentoVO.getContaReceberVO().getCodigo())) {
//					requerimentoVO.setContaReceberVO(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(requerimentoVO.getContaReceberVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS,  usuarioVO));
//					List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>();
//					Map<Integer,ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs = new HashMap<Integer, ConfiguracaoRecebimentoCartaoOnlineVO>();
//					contaReceberVOs.add(requerimentoVO.getContaReceberVO());
//					configuracaoRecebimentoCartaoOnlineVOs.putAll(getFacadeFactory().getContaReceberFacade().realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(requerimentoVO.getMatricula().getMatricula(), contaReceberVOs, usuarioVO));
//					getFacadeFactory().getContaReceberFacade().validarTipoImpressao(contaReceberVOs, usuarioVO);
//					requerimentoVO.setContaReceberVO(contaReceberVOs.get(0));
//				}
//			}
			if(versao.equals("V1")) {
				return Response.status(Status.OK).entity(new GenericEntity<List<RequerimentoVO>>(requerimentoVOs) {}).build();
			}else {
				for(RequerimentoVO requerimentoVO: requerimentoVOs) {
					RequerimentoVO.removerCamposNaoUsadoAPI(requerimentoVO);
				}
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				String json =	gson.toJson(requerimentoVOs);
				return Response.status(Status.OK).entity(json).build();
			}
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	public UsuarioVO autenticarConexao() throws Exception {
//		Enumeration<String> headerNames = request.getHeaderNames();
//		if (headerNames != null) {
//			while (headerNames.hasMoreElements()) {
//				System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
//			}
//		}
		String credenciais = "";
		if (request.getHeader(AUTHENTICATION_HEADER) != null) {
			credenciais = request.getHeader(AUTHENTICATION_HEADER);
		} else if (request.getHeader("usuario") != null) {
			credenciais = request.getHeader("usuario");
		}
		AutenticadorWS autenticadorWS = new AutenticadorWS();
		UsuarioVO usuarioVO = autenticadorWS.autenticar(credenciais);
		if (usuarioVO == null) {
			throw new Exception(UteisJSF.internacionalizar("msg_AutenticacaoAplicativo_naoPossuiAutorizacao"));
		}
		return usuarioVO;
	}
	
	public String getCaminhoWeb(UsuarioVO usuarioVO) throws Exception {
		String diretorioPastaWeb = "";
		//diretorioPastaWeb = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("")) + ""+"/";
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
		diretorioPastaWeb = configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao();
		
		if (configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao().endsWith("/")) {
			return diretorioPastaWeb;
		} else {
			return diretorioPastaWeb + "/";
		}
	}
	
	public UsuarioVO montarDadosMatriculaAcessoAluno(UsuarioVO usuarioVO, MatriculaVO matriculaVO) {
		usuarioVO.setMatricula(matriculaVO.getMatricula());
		usuarioVO.setCodigoCursoLogado(matriculaVO.getCurso().getCodigo());
		usuarioVO.setNomeCursoLogado(matriculaVO.getCurso().getNome());
		usuarioVO.setCodigoUnidadeEnsinoMatriculaLogado(matriculaVO.getUnidadeEnsino().getCodigo());
		usuarioVO.setTipoNivelEducacional(matriculaVO.getCurso().getNivelEducacional());
		usuarioVO.setPeriodicidadeCurso(matriculaVO.getCurso().getPeriodicidade());
		return usuarioVO;
	}
	
	public PerfilAcessoVO consultarPerfilAcessoUtilizar(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String tipoNivelEducacional, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(usuario.getMatricula(), null, NivelMontarDados.BASICO, usuario);
		
		if(configuracaoGeralSistemaVO.getApresentarMensagemAlertaAlunoNaoAssinouContrato()) {			
			matriculaVO.setAlunoNaoAssinouContratoMatricula(getFacadeFactory().getDocumetacaoMatriculaFacade().validaAlunoAssinouContratoMatricula(matriculaVO.getMatricula()));
			matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(configuracaoGeralSistemaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
			matriculaVO.setMensagemAlertaAlunoNaoAssinouContratoMatricula(matriculaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula().replaceAll("\\r\\n|\\r|\\n", " "));
		}
		if(usuario.getTipoUsuario().equals("RL")) {
			PerfilAcessoVO perfilAcessoVO = getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoPais(configuracaoGeralSistemaVO,  usuario, matriculaVO);
			if(!perfilAcessoVO.getCodigo().equals(0)) {
				perfilAcessoVO = getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(perfilAcessoVO.getCodigo(), usuario);
			}
			return perfilAcessoVO;
		}else {
			PerfilAcessoVO perfilAcessoVO = getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(configuracaoGeralSistemaVO, matriculaVO, matriculaVO.getAlunoNaoAssinouContratoMatricula(), usuario);
			if(!perfilAcessoVO.getCodigo().equals(0)) {
				perfilAcessoVO = getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(perfilAcessoVO.getCodigo(), usuario);
			}
			return perfilAcessoVO;
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarTurmasProfessor/{ano}/{semestre}/{unidadeEnsino}/{codigoPerfilAcesso}/{telaOrigem}")
	public Response consultarTurmasProfessor(@PathParam("ano") final String ano, @PathParam("semestre") final String semestre, @PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("telaOrigem") final String telaOrigem) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			PermissaoAcessoMenuVO permissaoAcessoMenuVO = getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(usuarioVO.getPerfilAcesso());
			usuarioVO.setPermitirLancarNotaRetroativoMobile(permissaoAcessoMenuVO.getPermitirLancarNotaRetroativo());
			usuarioVO.setPermitirRegistrarAulaRetroativoMobile(permissaoAcessoMenuVO.getPermitirRegistrarAulaRetroativo());
			List<TurmaVO> turmaVOs = new ArrayList<>();
			turmaVOs.addAll(getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(usuarioVO.getPessoa().getCodigo(),   semestre, ano, telaOrigem.equals("LN") ? usuarioVO.getPermitirLancarNotaRetroativoMobile() : usuarioVO.getPermitirRegistrarAulaRetroativoMobile(),  "AT", unidadeEnsino, 0, true, true, true, telaOrigem.equals("LN"), null,false, null));			
			for (TurmaVO turmaVO : turmaVOs) {
				getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, usuarioVO);
			}
//			Ordenacao.ordenarListaDecrescente(historicoVOs, "anoSemestreOrdenacao");
//			System.out.println("MOBILE INFO: RETORNANDO " + historicoVOs.toString());
			return Response.status(Status.OK).entity(new GenericEntity<List<TurmaVO>>(turmaVOs) {}).build(); 
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarQuantidadeComunicadosExistentesProfessor/{codigoPessoa}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response consultarQuantidadeComunicadosExistentesProfessor(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("professor");
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			QuantidadeObjetoExistentesRSVO quantidadeExistentes = new QuantidadeObjetoExistentesRSVO();
			quantidadeExistentes.setValor(getFacadeFactory().getComunicacaoInternaFacade().consultaQuantidadeRapidaPorSituacaoEntradaProfessor(codigoPessoa, "entrada", null, true, null, null, usuarioVO));
			return Response.status(Status.OK).entity(quantidadeExistentes).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
	
		
		
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gravarRegistroAula")
	public void gravarRegistroAula(final String listaRegistrosAula) {
		try {

			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("professor");
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().definirPerfilAcessoParaAlunoProfessorECandidato("professor", configuracaoGeralSistemaVO));
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			List<RegistroAulaVO> registroAulaVOs = new ArrayList<>();
			Type type = new TypeToken<List<RegistroAulaVO>>() {
			}.getType();
			registroAulaVOs = gson.fromJson(listaRegistrosAula, type);
			if (Uteis.isAtributoPreenchido(registroAulaVOs)) {
				for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
					registroAulaVO.setData(Uteis.getDateSemHora(registroAulaVO.getData()));
					registroAulaVO.setResponsavelRegistroAula(usuarioVO);
					if (Uteis.isAtributoPreenchido(registroAulaVO.getCodigo())) {
						registroAulaVO.setNovoObj(false);
					}
					
				}
				
				getFacadeFactory().getRegistroAulaFacade().persistir(registroAulaVOs, registroAulaVOs.get(0).getConteudo(), registroAulaVOs.get(0).getTipoAula(), false, "RegistroAula", "Inserção pela Aplicativo do Professor", usuarioVO, true);
			}
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarDisciplinaLancamentoNota/{codigoPessoa}/{codigoTurma}/{turmaAgrupada}/{ano}/{semestre}/{codigoPerfilAcesso}")
		public Response consultarDisciplinaLancamentoNota(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoTurma") 
		final Integer codigoTurma, @PathParam("turmaAgrupada") final Boolean turmaAgrupada, @PathParam("ano") final Integer ano, @PathParam("semestre") 
		final Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			Boolean permiteLancarNotaDisciplinaComposta;
			try {
				permiteLancarNotaDisciplinaComposta = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("PermiteLancarNotaDisciplinaComposta", usuarioVO);
			} catch (Exception e) {
				permiteLancarNotaDisciplinaComposta = false;
			}
			TurmaVO turmaVO = new TurmaVO();
			turmaVO.setCodigo(codigoTurma);
			getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, usuarioVO);
			List<DisciplinaVO> listaConsultas = new ArrayList<>(0);
			CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoProfessorExcluisoLancamentoNota(0, codigoTurma, turmaAgrupada, codigoPessoa, 0, 0, "", String.valueOf(ano), String.valueOf(semestre), false, usuarioVO);
			boolean professorExclusiso = getFacadeFactory().getHistoricoFacade().professorExclusivoLancamentoNota(calendarioLancamentoNotaVO, usuarioVO);
			if (Uteis.isAtributoPreenchido(turmaVO)) {
				if (turmaVO.getIntegral()) {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(codigoPessoa, turmaVO.getUnidadeEnsino().getCodigo(), null, turmaVO.getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, permiteLancarNotaDisciplinaComposta, usuarioVO, professorExclusiso);
				} else {
					listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(codigoPessoa, turmaVO.getUnidadeEnsino().getCodigo(), null, turmaVO.getCodigo(), semestre.toString(), ano.toString(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, permiteLancarNotaDisciplinaComposta, usuarioVO, professorExclusiso);
				}
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<DisciplinaVO>>(listaConsultas) {}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarConfiguracaoNotaLancamentoNota/{codigoPessoa}/{codigoTurma}/{codigoDisciplina}/{ano}/{semestre}/{codigoPerfilAcesso}")
		public Response consultarConfiguracaoNotaLancamentoNota(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoTurma") 
		final Integer codigoTurma, @PathParam("codigoDisciplina") final Integer codigoDisciplina, @PathParam("ano") final Integer ano, @PathParam("semestre") 
		final Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			TurmaVO turmaVO = new TurmaVO();
			turmaVO.setCodigo(codigoTurma);
			getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, usuarioVO);
			DisciplinaVO disciplinaVO = new DisciplinaVO();
			disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(codigoDisciplina, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicaLancamentoNotaMobile(turmaVO, disciplinaVO, ano.toString(), semestre.toString(), usuarioVO);
			Date dataBase = new Date();
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoRetornoVOs =  new ArrayList<ConfiguracaoAcademicoVO>(0);
			for(ConfiguracaoAcademicoVO configuracaoAcademicoVO: configuracaoAcademicoVOs){
				configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracaoAcademicoVO.getCodigo(), usuarioVO);
				getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(turmaVO, disciplinaVO, ano.toString(), semestre.toString(), configuracaoAcademicoVO, usuarioVO);
				CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
				calendarioLancamentoNotaVO = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(turmaVO.getUnidadeEnsino().getCodigo(), turmaVO.getCodigo(), turmaVO.getTurmaAgrupada(), usuarioVO.getPessoa().getCodigo(), codigoDisciplina, configuracaoAcademicoVO.getCodigo(), turmaVO.getPeriodicidade(), ano.toString(), semestre.toString(), false, usuarioVO);
				
				
				if (calendarioLancamentoNotaVO != null && !calendarioLancamentoNotaVO.getCodigo().equals(0)) {
					
					if ((calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal() == null ||
							calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal().before(new Date()) ||
							Uteis.getData(calendarioLancamentoNotaVO.getDataInicioCalculoMediaFinal()).equals(Uteis.getData(new Date())))
						&&
						(calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal() == null ||
								calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal().after(new Date()) ||
							Uteis.getData(calendarioLancamentoNotaVO.getDataTerminoCalculoMediaFinal()).equals(Uteis.getData(new Date())))) {
						configuracaoAcademicoVO.setOcultarBotaoCalcularMedia(false);
					} else {
						configuracaoAcademicoVO.setOcultarBotaoCalcularMedia(true);
					}
				} else {
					configuracaoAcademicoVO.setOcultarBotaoCalcularMedia(false);
				}
				
					for (Iterator<ConfiguracaoAcademicaNotaVO> iterator = configuracaoAcademicoVO.getConfiguracaoAcademicaNotaUtilizarVOs().iterator(); iterator.hasNext();) {
						ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) iterator.next();
						Date dataInicio = Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO) ? (Date)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataInicioNota"+configuracaoAcademicaNotaVO.getNota().getNumeroNota()) : null;
						if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO) && dataInicio != null && !Uteis.getData(dataInicio).equals(Uteis.getData(dataBase)) && dataInicio.compareTo(dataBase) > 0){
							iterator.remove();
						}else{
							Date dataTermino = Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO) ? (Date)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataTerminoNota"+configuracaoAcademicaNotaVO.getNota().getNumeroNota()) : null;
							if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO) && dataTermino != null							
									&& !Uteis.getData(dataTermino).equals(Uteis.getData(dataBase))
									&& dataTermino.compareTo(dataBase) < 0){
								configuracaoAcademicaNotaVO.setBloquearLancamentoNota(true);
							}
							if(!configuracaoAcademicaNotaVO.getUtilizarComoSubstitutiva() 
								&& (!configuracaoAcademicaNotaVO.getFormulaCalculo().trim().isEmpty()
								|| configuracaoAcademicoVO.getBloquearNotaComposta(configuracaoAcademicaNotaVO.getNota().getNumeroNota()))){
								configuracaoAcademicaNotaVO.setBloquearLancamentoNota(true);
							}
							
							configuracaoAcademicaNotaVO.setTituloApresentar(((TurmaDisciplinaNotaTituloVO)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, 
									"turmaDisciplinaNotaTitulo"+configuracaoAcademicaNotaVO.getNota().getNumeroNota())).getTituloNotaApresentar());
						}						
					}
					configuracaoAcademicoRetornoVOs.add(configuracaoAcademicoVO);
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<ConfiguracaoAcademicoVO>>(configuracaoAcademicoRetornoVOs) {}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarAlunosLancamentoNota/{codigoPessoa}/{codigoTurma}/{codigoDisciplina}/{ano}/{semestre}/{codigoPerfilAcesso}/{configuracaoAcademico}")
		public Response consultarAlunosLancamentoNota(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoTurma") 
		final Integer codigoTurma, @PathParam("codigoDisciplina") final Integer codigoDisciplina, @PathParam("ano") final Integer ano, @PathParam("semestre") 
		final Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("configuracaoAcademico") final Integer configuracaoAcademico) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			ConfiguracaoAcademicoVO configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracaoAcademico, usuarioVO);
			TurmaVO turmaVO = new TurmaVO();
			turmaVO.setCodigo(codigoTurma);
			getFacadeFactory().getTurmaFacade().carregarDados(turmaVO, usuarioVO);
			DisciplinaVO disciplinaVO = new DisciplinaVO();
			disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(codigoDisciplina, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			String anoParametro = "";
			String semestreParametro = "";
			if (ano != 0) {
				anoParametro = ano.toString();
			}
			if (semestre != 0) {
				semestreParametro = semestre.toString();
			}
			List<HistoricoVO> lista = getFacadeFactory().getHistoricoFacade().consultarHistoricoLancamentoNotaMobile(turmaVO, disciplinaVO, anoParametro, semestreParametro, configuracaoAcademicoVO, usuarioVO);
//			Ordenacao.ordenarLista(configuracaoAcademicoVO.getConfiguracaoAcademicaNotaUtilizarVOs(), "nota.numeroNota");
//			for(HistoricoVO historicoVO : lista){
//				for(ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO: configuracaoAcademicoVO.getConfiguracaoAcademicaNotaUtilizarVOs()){
//					HistoricoNotaVO historicoNotaVO = new HistoricoNotaVO();
//					historicoNotaVO.setAgrupamentoNota(configuracaoAcademicaNotaVO.getAgrupamentoNota());
//					historicoNotaVO.setTipoNota(configuracaoAcademicaNotaVO.getNota());
//					historicoNotaVO.setNotaLancada((Double)UtilReflexao.invocarMetodoGet(historicoVO, "nota"+configuracaoAcademicaNotaVO.getNota().getNumeroNota()));
//					historicoNotaVO.setConfiguracaoAcademicoNotaConceito((ConfiguracaoAcademicoNotaConceitoVO)UtilReflexao.invocarMetodoGet(historicoVO, "nota"+configuracaoAcademicaNotaVO.getNota().getNumeroNota()+"Conceito"));
//					historicoVO.getHistoricoNotaMobileVOs().add(historicoNotaVO);
//				}
////				Ordenacao.ordenarLista(historicoVO.getHistoricoNotaMobileVOs(), "tipoNota.numeroNota");
//			}
			return Response.status(Status.OK).entity(new GenericEntity<List<HistoricoVO>>(lista) {}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}	
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarVerificacaoBloqueioNotaDisciplinaComposta")
		public Response realizarVerificacaoBloqueioNotaDisciplinaComposta(final String paramObject) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("professor");
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
				Type type = new TypeToken<LancamentoNotaRSVO>() {
				}.getType();
				lancamentoNotaRSVO = gson.fromJson(paramObject, type);
				getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(lancamentoNotaRSVO.getListaHistoricos(), lancamentoNotaRSVO.getConfiguracaoAcademico(), usuarioVO);
				for (Iterator<ConfiguracaoAcademicaNotaVO> iterator = lancamentoNotaRSVO.getConfiguracaoAcademico().getConfiguracaoAcademicaNotaUtilizarVOs().iterator(); iterator.hasNext();) {
					ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) iterator.next();										
					if(lancamentoNotaRSVO.getConfiguracaoAcademico().getBloquearNotaComposta(configuracaoAcademicaNotaVO.getNota().getNumeroNota())){
						configuracaoAcademicaNotaVO.setBloquearLancamentoNota(true);
					}																
				}				
				return Response.status(Status.OK).entity(lancamentoNotaRSVO.getConfiguracaoAcademico()).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}	
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarCalculoMedia")
		public Response realizarCalculoMedia(final String paramObject) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("professor");
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
				Type type = new TypeToken<LancamentoNotaRSVO>() {
				}.getType();
				lancamentoNotaRSVO = gson.fromJson(paramObject, type);
				lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
				usuarioVO.getPerfilAcesso().setCodigo(lancamentoNotaRSVO.getCodigoPerfilAcesso());
					for(HistoricoVO historicoVO: lancamentoNotaRSVO.getListaHistoricos()){
						//realizarCopiaHistoricoNotaParaNotaHistorico(historicoVO);
						historicoVO.setConfiguracaoAcademico(lancamentoNotaRSVO.getConfiguracaoAcademico());
						getFacadeFactory().getHistoricoFacade().calcularMedia(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), lancamentoNotaRSVO.getConfiguracaoAcademico(), lancamentoNotaRSVO.getTurmaVO().getCodigo(), TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, true, usuarioVO);
						//realizarCopiaNotaHistoricoParaHistoricoNota(historicoVO);
						historicoVO.setHistoricoAlterado(true);
						historicoVO.setHistoricoCalculado(Boolean.TRUE);
					}		
					return Response.status(Status.OK).entity(new GenericEntity<List<HistoricoVO>>(lancamentoNotaRSVO.getListaHistoricos()) {}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
//		private void realizarCopiaHistoricoNotaParaNotaHistorico(HistoricoVO historicoVO){
//			for(HistoricoNotaVO historicoNotaVO: historicoVO.getHistoricoNotaMobileVOs()){
//				if(historicoNotaVO.getNotaLancada() == null){
//					UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota"+historicoNotaVO.getTipoNota().getNumeroNota());
//				}else{
//					UtilReflexao.invocarMetodo(historicoVO, "setNota"+historicoNotaVO.getTipoNota().getNumeroNota(), historicoNotaVO.getNotaLancada());
//				}
//				if(Uteis.isAtributoPreenchido(historicoNotaVO.getConfiguracaoAcademicoNotaConceito())){
//					UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota"+historicoNotaVO.getTipoNota().getNumeroNota()+"Conceito");
//				}else{
//					UtilReflexao.invocarMetodo(historicoVO, "setNota"+historicoNotaVO.getTipoNota().getNumeroNota()+"Conceito", historicoNotaVO.getConfiguracaoAcademicoNotaConceito());
//				}
//			}
//		}
//		private void realizarCopiaNotaHistoricoParaHistoricoNota(HistoricoVO historicoVO){
//			for(HistoricoNotaVO historicoNotaVO: historicoVO.getHistoricoNotaMobileVOs()){
//				historicoNotaVO.setNotaLancada((Double)UtilReflexao.invocarMetodoGet(historicoVO, "nota"+historicoNotaVO.getTipoNota().getNumeroNota()));
//				historicoNotaVO.setConfiguracaoAcademicoNotaConceito((ConfiguracaoAcademicoNotaConceitoVO)UtilReflexao.invocarMetodoGet(historicoVO, "nota"+historicoNotaVO.getTipoNota().getNumeroNota()+"Conceito"));				
//			}
//		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarCalculoMediaIndividual")
		public Response realizarCalculoMediaIndividual(final String paramObject) {
			try {	
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("professor");
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
				Type type = new TypeToken<LancamentoNotaRSVO>() {
				}.getType();
				lancamentoNotaRSVO = gson.fromJson(paramObject, type);
				usuarioVO.getPerfilAcesso().setCodigo(lancamentoNotaRSVO.getCodigoPerfilAcesso());
				lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
				lancamentoNotaRSVO.getHistorico().setConfiguracaoAcademico(lancamentoNotaRSVO.getConfiguracaoAcademico());
				getFacadeFactory().getHistoricoFacade().calcularMedia(lancamentoNotaRSVO.getHistorico(), lancamentoNotaRSVO.getHistorico().getHistoricoDisciplinaFilhaComposicaoVOs(), lancamentoNotaRSVO.getConfiguracaoAcademico(), lancamentoNotaRSVO.getTurmaVO().getCodigo(), TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, true, usuarioVO);
				lancamentoNotaRSVO.getHistorico().setHistoricoAlterado(true);
				lancamentoNotaRSVO.getHistorico().setHistoricoCalculado(Boolean.TRUE);
				return Response.status(Status.OK).entity(lancamentoNotaRSVO.getHistorico()).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}	
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarLancamentoNota")
		public Response gravarLancamentoNota(final String paramObject) throws Exception {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("professor");
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
				Type type = new TypeToken<LancamentoNotaRSVO>() {
				}.getType();
				lancamentoNotaRSVO = gson.fromJson(paramObject, type);
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getCodigoPerfilAcesso(), usuarioVO));
				lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
				CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(
						lancamentoNotaRSVO.getTurmaVO().getUnidadeEnsino().getCodigo(), lancamentoNotaRSVO.getTurmaVO().getCodigo(), lancamentoNotaRSVO.getTurmaVO().getTurmaAgrupada(), usuarioVO.getPessoa().getCodigo(), lancamentoNotaRSVO.getHistorico().getDisciplina().getCodigo(), 0, lancamentoNotaRSVO.getTurmaVO().getPeriodicidade(), lancamentoNotaRSVO.getHistorico().getAnoHistorico(), lancamentoNotaRSVO.getHistorico().getSemestreHistorico(), false, usuarioVO);
				getFacadeFactory().getHistoricoFacade().incluirListaHistoricoVisaoProfessor(lancamentoNotaRSVO.getListaHistoricos(), "LancamentoNota", usuarioVO, "Visão do Professor", true, calendarioLancamentoNotaVO, true);
				return Response.status(Status.OK).entity(new GenericEntity<List<HistoricoVO>>(lancamentoNotaRSVO.getListaHistoricos()) {}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarLancamentoNotaIndividual")
		public Response gravarLancamentoNotaIndividual(final String paramObject) throws Exception {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("professor");
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
				Type type = new TypeToken<LancamentoNotaRSVO>() {
				}.getType();
				lancamentoNotaRSVO = gson.fromJson(paramObject, type);
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getCodigoPerfilAcesso(), usuarioVO));
				lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
				getFacadeFactory().getHistoricoFacade().gravarLancamentoNota(lancamentoNotaRSVO.getHistorico(), true, "Professor", TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, usuarioVO);
				return Response.status(Status.OK).entity(lancamentoNotaRSVO.getHistorico()).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarValidacaoNotaLancada/")
		public Response realizarValidacaoNotaLancada(final String paramObject) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("professor");
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
			Type type = new TypeToken<LancamentoNotaRSVO>() {
			}.getType();
			lancamentoNotaRSVO = gson.fromJson(paramObject, type);
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getCodigoPerfilAcesso(), usuarioVO));
			lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO)); 
			for (HistoricoVO historicoVO : lancamentoNotaRSVO.getListaHistoricos()) {
				for(int x = 1; x<=40; x++) {
					getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoVO, historicoVO.getHistoricoDisciplinaFilhaComposicaoVOs(), lancamentoNotaRSVO.getConfiguracaoAcademico(), x);
				}
			}
			return Response.status(Status.OK).entity(lancamentoNotaRSVO.getHistorico()).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}	
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarValidacaoNotaLancadaIndividual/")
		public Response realizarValidacaoNotaLancadaIndividual(final String paramObject) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar("professor");
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			LancamentoNotaRSVO lancamentoNotaRSVO = new LancamentoNotaRSVO();
			Type type = new TypeToken<LancamentoNotaRSVO>() {
			}.getType();
			lancamentoNotaRSVO = gson.fromJson(paramObject, type);
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getCodigoPerfilAcesso(), usuarioVO));
			lancamentoNotaRSVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(lancamentoNotaRSVO.getConfiguracaoAcademico().getCodigo(), usuarioVO)); 
			for(int x = 1; x<=40; x++) {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(lancamentoNotaRSVO.getHistorico(), lancamentoNotaRSVO.getHistorico().getHistoricoDisciplinaFilhaComposicaoVOs(), lancamentoNotaRSVO.getConfiguracaoAcademico(), x);
			}
			return Response.status(Status.OK).entity(lancamentoNotaRSVO.getHistorico()).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}	

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/verificarPermissaoConsultarRegistroAula/{codigoPerfilAcesso}")
		public Response verificarPermissaoConsultarRegistroAula(@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			getFacadeFactory().getControleAcessoFacade().consultar("RegistroAula", true, usuarioVO);
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}	
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/verificarPermissaoConsultarLancamentoNota/{codigoPerfilAcesso}")
		public Response verificarPermissaoConsultarLancamentoNota(@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("professor");
			getFacadeFactory().getControleAcessoFacade().consultar("LancamentoNota", true, usuarioVO);
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}	
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarDisciplinasAluno/{unidadeEnsino}/{matricula}/{ano}/{semestre}/{codigoPerfilAcesso}")
		public Response consultarDisciplinasAluno(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("matricula") final String matricula, @PathParam("ano") Integer ano, @PathParam("semestre") Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("aluno");
				MatriculaVO matriculaVO = new MatriculaVO();
				matriculaVO.setMatricula(matricula);
				matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
				List<DisciplinaVO> disciplinaVOs = new ArrayList<>();
				if (!Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre) && !matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
					String anoSemestre = getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(matriculaVO.getMatricula(), new ArrayList<>());
					String anoPrm = montarAnoAlunoDownloadMaterial(anoSemestre);
					ano = Integer.parseInt(anoPrm);
					String semestrePrm = montarSemestreAlunoDownloadMaterial(anoSemestre);
					semestre = Integer.parseInt(semestrePrm);
				}
				disciplinaVOs.addAll(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaAluno(unidadeEnsino, matriculaVO.getCurso().getPeriodicidade(), matricula, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, ano.toString(), semestre.toString()));
				return Response.status(Status.OK).entity(new GenericEntity<List<DisciplinaVO>>(disciplinaVOs) {
				}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarArquivosDownloadMaterial/{unidadeEnsino}/{matricula}/{ano}/{semestre}/{codigoPerfilAcesso}/{disciplina}/{tipoArquivo}")
		public Response consultarArquivosDownloadMaterial(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("matricula") final String matricula, @PathParam("ano") Integer ano, @PathParam("semestre") Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("disciplina") final Integer disciplina, @PathParam("tipoArquivo") final String tipoArquivo) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("aluno");
				getFacadeFactory().getControleAcessoFacade().consultar("DownloadArquivo", true, usuarioVO);
				List<ArquivoVO> arquivosConsultados = new ArrayList<>();
				List<ArquivoVO> arquivoVOs = new ArrayList<>();
				Boolean dentroDataMaximaLimiteDownload;
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
				if (!Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre) && !matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
					String anoSemestre = getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(matriculaVO.getMatricula(), new ArrayList<>());
					String anoPrm = montarAnoAlunoDownloadMaterial(anoSemestre);
					ano = Integer.parseInt(anoPrm);
					String semestrePrm = montarSemestreAlunoDownloadMaterial(anoSemestre);
					semestre = Integer.parseInt(semestrePrm);
				}
				if (tipoArquivo.equals("instituicao")) {
					arquivoVOs.addAll(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(matricula, disciplina, ano.toString(), semestre.toString(), OrigemArquivo.INSTITUICAO.getValor()));
				} else if (tipoArquivo.equals("professor")) {
					arquivosConsultados.addAll(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(matricula, disciplina, ano.toString(), semestre.toString(), OrigemArquivo.PROFESSOR.getValor()));
					Map<String, ArquivoVO> mapArquivoAgrupamentoDisciplinaTurmaProfessor = new HashMap<String, ArquivoVO>(0);
					mapArquivoAgrupamentoDisciplinaTurmaProfessor = getFacadeFactory().getArquivoFacade().montarDadosArquivoIndice(arquivosConsultados, usuarioVO);
					arquivoVOs.addAll(mapArquivoAgrupamentoDisciplinaTurmaProfessor.values());
				}
				if (Uteis.isAtributoPreenchido(arquivoVOs)) {
					for (ArquivoVO arquivoVO : arquivoVOs) {
						if (Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
							arquivoVO.setExtensao(ArquivoHelper.getExtensaoArquivo(arquivoVO.getNome()));	
							continue;
						}
						for (ArquivoVO arquivoVO2 : arquivoVO.getArquivoFilhoVOs()) {
							if (Uteis.isAtributoPreenchido(arquivoVO2.getNome())) {
								arquivoVO2.setExtensao(ArquivoHelper.getExtensaoArquivo(arquivoVO2.getNome()));	
								continue;
							}
							
							for (ArquivoVO arquivoVO3 : arquivoVO2.getArquivoFilhoVOs()) {
								if (Uteis.isAtributoPreenchido(arquivoVO3.getNome())) {
									arquivoVO3.setExtensao(ArquivoHelper.getExtensaoArquivo(arquivoVO3.getNome()));	
									continue;
								}
							}
						}
						
//						arquivoVO.setExtensao(ArquivoHelper.getExtensaoArquivo(arquivoVO.getNome()));
					}				
				}
				dentroDataMaximaLimiteDownload = verificarQuantidadeDiasMaximoLimiteDownload(matriculaVO, arquivoVOs);
				return Response.status(Status.OK).entity(new GenericEntity<List<ArquivoVO>>(arquivoVOs) {
				}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		public Boolean verificarQuantidadeDiasMaximoLimiteDownload(MatriculaVO matriculaVO, List<ArquivoVO> arquivoVOs) throws Exception {
			try {
				Boolean dentroDataMaximaLimiteDownload;
				matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), false, getUsuarioLogado()));
				if ((!arquivoVOs.isEmpty()) && matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
					if (!matriculaVO.getCurso().getLimitarQtdeDiasMaxDownload()) {
						matriculaVO.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
					}
					if (matriculaVO.getCurso().getLimitarQtdeDiasMaxDownload() || matriculaVO.getCurso().getConfiguracaoAcademico().getLimitarQtdeDiasMaxDownload()) {
						dentroDataMaximaLimiteDownload = true;
						// dentroDataMaximaLimiteDownload =
						// getFacadeFactory().getArquivoFacade().verificarDataDownloadDentroQuantidadeDiasMaximoLimite(matriculaVO.getMatricula(),
						// matriculaVO.getCurso(), getArquivoVO().getTurma(),
						// getArquivoVO().getDisciplina());
					} else {
						dentroDataMaximaLimiteDownload = true;
					}
					if (!dentroDataMaximaLimiteDownload) {
						throw new ConsistirException("Data limite para download de material foi ultrapassada. Favor entrar em contato com a Instituição de Ensino.");
					} else {
						setMensagemID("");
						setMensagem("");
					}
				} else {
					dentroDataMaximaLimiteDownload = true;
					setMensagemID("");
					setMensagem("");
				}
				return dentroDataMaximaLimiteDownload;
			} catch (Exception e) {
				throw e;
			}
		}
		
		public String montarAnoAlunoDownloadMaterial(String anoSemestre) {
			try {
				if (!anoSemestre.trim().isEmpty()) {
					return anoSemestre.substring(0, 4);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
		
		public String montarSemestreAlunoDownloadMaterial(String anoSemestre) {
			try {
				if (!anoSemestre.trim().isEmpty() && anoSemestre.length() > 5) {
					return anoSemestre.substring(5, 6);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
					
		@GET
		@Produces({ MediaType.TEXT_PLAIN })
		@Path("/registrarDownload/{codigoArquivo}/{codigoPerfilAcesso}/{codigoUnidadeEnsinoMatricula}/{matricula}")
		public Response registrarDownload(@PathParam("codigoArquivo") final Integer codigoArquivo, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("matricula") final String matricula) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("aluno");
				ArquivoVO arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(codigoArquivo, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
//				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsinoMatricula, null);
				MatriculaVO matriculaVO = new MatriculaVO();
				matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula);
				getFacadeFactory().getDownloadFacade().registrarDownload(arquivoVO, usuarioVO, new MatriculaPeriodoVO(), matriculaVO);
				return Response.ok("").build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarListaAvalicoesInstitucionais/{matricula}/{codigoPerfilAcesso}")
		public Response consultarListaAvalicoesInstitucionais(@PathParam("matricula") final String matricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("aluno");
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
				List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional = new ArrayList<AvaliacaoInstitucionalVO>();
				listaAvaliacaoInstitucional.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUsuarioLogado(usuarioVO, matriculaVO));
				return Response.status(Status.OK).entity(new GenericEntity<List<AvaliacaoInstitucionalVO>>(listaAvaliacaoInstitucional) {
				}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarListaAvalicoesInstitucionaisProfessor/{codigoPerfilAcesso}")
		public Response consultarListaAvalicoesInstitucionaisProfessor(@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("professor");
				List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional = new ArrayList<AvaliacaoInstitucionalVO>();
				listaAvaliacaoInstitucional.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUsuarioLogado(usuarioVO, null));
				return Response.status(Status.OK).entity(new GenericEntity<List<AvaliacaoInstitucionalVO>>(listaAvaliacaoInstitucional) {
				}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/carregarDadosAvaliacaoInstitucional/{matricula}/{codigoPerfilAcesso}/{codigoAvaliacaoInstitucional}")
		public Response carregarDadosAvaliacaoInstitucional(@PathParam("matricula") final String matricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoAvaliacaoInstitucional") final Integer codigoAvaliacaoInstitucional) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("aluno");
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
				avaliacaoInstitucionalVO.setCodigo(codigoAvaliacaoInstitucional);
				avaliacaoInstitucionalVO = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(avaliacaoInstitucionalVO, matriculaVO, usuarioVO);
				avaliacaoInstitucionalVO.setMensagem(avaliacaoInstitucionalVO.getMensagem().replaceAll("\\r\\n|\\r|\\n", " "));
				return Response.status(Status.OK).entity(avaliacaoInstitucionalVO).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/carregarDadosAvaliacaoInstitucionalProfessor/{codigoPerfilAcesso}/{codigoAvaliacaoInstitucional}")
		public Response carregarDadosAvaliacaoInstitucionalProfessor(@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoAvaliacaoInstitucional") final Integer codigoAvaliacaoInstitucional) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				usuarioVO.setVisaoLogar("professor");
				AvaliacaoInstitucionalVO avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
				avaliacaoInstitucionalVO.setCodigo(codigoAvaliacaoInstitucional);
				avaliacaoInstitucionalVO = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(avaliacaoInstitucionalVO, null, usuarioVO);
				return Response.status(Status.OK).entity(avaliacaoInstitucionalVO).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarValidacaoImportanciaPerguntaSelecionado/")
		public Response realizarValidacaoImportanciaPerguntaSelecionado(final String paramObject) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				AvaliacaoInstitucionalRSVO avaliacaoInstitucionalRSVO = new AvaliacaoInstitucionalRSVO();
				Type type = new TypeToken<AvaliacaoInstitucionalRSVO>() {
				}.getType();
				avaliacaoInstitucionalRSVO = gson.fromJson(paramObject, type);
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(avaliacaoInstitucionalRSVO.getCodigoPerfilAcesso(), usuarioVO));
				usuarioVO.setVisaoLogar("aluno");
				getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(avaliacaoInstitucionalRSVO.getAvaliacaoInstitucionalVO(), avaliacaoInstitucionalRSVO.getQuestionarioVO(), avaliacaoInstitucionalRSVO.getPerguntaQuestionarioVO(), avaliacaoInstitucionalRSVO.getPerguntaQuestionarioVO().getPergunta().getPeso(), usuarioVO);
				return Response.status(Status.OK).entity(avaliacaoInstitucionalRSVO.getPerguntaQuestionarioVO()).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarValidacaoRespostaQuestionario/")
		public Response realizarValidacaoRespostaQuestionario(final String paramObject) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			AvaliacaoInstitucionalRSVO avaliacaoInstitucionalRSVO = new AvaliacaoInstitucionalRSVO();
			Type type = new TypeToken<AvaliacaoInstitucionalRSVO>() {
			}.getType();
			avaliacaoInstitucionalRSVO = gson.fromJson(paramObject, type);
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(avaliacaoInstitucionalRSVO.getCodigoPerfilAcesso(), usuarioVO));
			usuarioVO.setVisaoLogar("aluno");
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoRespostaQuestionario(avaliacaoInstitucionalRSVO.getAvaliacaoInstitucionalVO(), avaliacaoInstitucionalRSVO.getQuestionarioVO(), avaliacaoInstitucionalRSVO.getPerguntaQuestionarioVO(), avaliacaoInstitucionalRSVO.getRespostaPerguntaVO(), usuarioVO);
			return Response.status(Status.OK).entity(avaliacaoInstitucionalRSVO.getPerguntaQuestionarioVO()).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}

	}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarAvaliacaoInstitucional/")
		public Response gravarAvaliacaoInstitucional(final String paramObject) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			AvaliacaoInstitucionalRSVO avaliacaoInstitucionalRSVO = new AvaliacaoInstitucionalRSVO();
			Type type = new TypeToken<AvaliacaoInstitucionalRSVO>() {
			}.getType();
			avaliacaoInstitucionalRSVO = gson.fromJson(paramObject, type);
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(avaliacaoInstitucionalRSVO.getCodigoPerfilAcesso(), usuarioVO));
//			usuarioVO.setVisaoLogar("aluno");
			avaliacaoInstitucionalRSVO.setListaAvaliacaoInstitucional(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().persistir(avaliacaoInstitucionalRSVO.getListaAvaliacaoInstitucional(), avaliacaoInstitucionalRSVO.getAvaliacaoInstitucionalVO(), usuarioVO));
			return Response.status(Status.OK).entity(new GenericEntity<List<AvaliacaoInstitucionalVO>>(avaliacaoInstitucionalRSVO.getListaAvaliacaoInstitucional()) {}).build(); 
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}

	}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarComunicadoInternoMural/{codigoPessoa}/{codigoPerfilAcesso}")
		public Response consultarComunicadoInternoMural(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			List<ComunicacaoInternaVO> comunicadoInternoVOs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteMarketingLeituraObrigatoria(codigoPessoa, 1000, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			for (ComunicacaoInternaVO comunicacaoInternaVO : comunicadoInternoVOs) {
				comunicacaoInternaVO.getResponsavel().setCelular("(00)00000-0000");
				comunicacaoInternaVO.getResponsavel().setCPF("00000000000");
				comunicacaoInternaVO.getResponsavel().setEmail("suporte@sei.com");
				comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagem().replaceAll("\\r\\n|\\r|\\n", " "));
				comunicacaoInternaVO.setAssunto(comunicacaoInternaVO.getAssunto().replaceAll("\\r\\n|\\r|\\n", " "));
			}
			return Response.status(Status.OK).entity(new GenericEntity<List<ComunicacaoInternaVO>>(comunicadoInternoVOs) {}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/marcarComunicadoInternoComoLido/{codigoComunicacaoInterna}/{codigoPessoa}/{codigoPerfilAcesso}/{tipoPessoa}")
		public Response marcarComunicadoInternoComoLido(@PathParam("codigoComunicacaoInterna") final Integer codigoComunicacaoInterna, @PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("tipoPessoa") final String tipoPessoa) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar(tipoPessoa);
			ComunicacaoInternaVO comunicacaoInternaVO = getFacadeFactory().getComunicacaoInternaFacade().consultarPorChavePrimaria(codigoComunicacaoInterna, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			getFacadeFactory().getComunicacaoInternaFacade().registrarLeituraComunicadoInterno(comunicacaoInternaVO, usuarioVO.getPessoa(), usuarioVO);
			return Response.status(Status.OK).entity(comunicacaoInternaVO).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarEntregaDocumentos/{matricula}/{codigoPerfilAcesso}")
		public Response consultarEntregaDocumentosV1(@PathParam("matricula") final String matricula,
				@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			try {
				return Response.status(Status.OK).entity(new GenericEntity<List<DocumetacaoMatriculaVO>>(
						consultarEntregaDocumentos(matricula, codigoPerfilAcesso)) {
				}).build();

			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarEntregaDocumentos/{matricula}/{codigoPerfilAcesso}")
		public Response consultarEntregaDocumentosV2(@PathParam("matricula") final String matricula,
				@PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			try {
				return DocumetacaoMatriculaVO
						.removerCamposChamadaAPI(consultarEntregaDocumentos(matricula, codigoPerfilAcesso));
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		public List<DocumetacaoMatriculaVO> consultarEntregaDocumentos(String matricula, Integer codigoPerfilAcesso)
				throws Exception {
			UsuarioVO usuarioVO = obterUsuarioWebService();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = (getFacadeFactory().getDocumetacaoMatriculaFacade()
					.consultarDocumetacaoMatriculas(matricula, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuarioVO));
			if (!getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("EntregaDocumentoPermiteAnexarArquivo",
					usuarioVO)) {
				documetacaoMatriculaVOs.stream()
						.forEach(d -> d.getTipoDeDocumentoVO().setPermitirPostagemPortalAluno(false));
			}
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
	        getFacadeFactory().getDocumetacaoMatriculaFacade().realizarPreencherCaminhoAnexoImagemDocumentacaoMatricula(documetacaoMatriculaVOs, configuracaoGeralSistemaVO);	
	        return documetacaoMatriculaVOs;

		}

		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarUploadArquivo")
		public Response realizarUploadArquivoV1(@Context HttpServletRequest request) {
			try {
				return Response.status(Status.OK).entity(realizarUploadArquivo(request)).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/realizarUploadArquivo")
		public Response realizarUploadArquivoV2(@Context HttpServletRequest request) {
			try {
				DocumetacaoMatriculaVO documetacaoMatriculaVO = realizarUploadArquivo(request);
				return DocumetacaoMatriculaVO.removerCamposChamadaAPI(documetacaoMatriculaVO);
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		private DocumetacaoMatriculaVO realizarUploadArquivo(HttpServletRequest request) throws Exception {

			UsuarioVO usuarioVO = obterUsuarioWebService();
			request.getHeaderNames();
			String documento = request.getHeader("objeto") != null ? request.getHeader("objeto") : "";
			Boolean frente = request.getHeader("frente") != null ? Boolean.valueOf(request.getHeader("frente")) : null;
			Boolean gravarDocumento = request.getHeader("gravarDocumento") != null ? Boolean.valueOf(request.getHeader("gravarDocumento")) : null;
			DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
			JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>(factory);
			List<DiskFileItem> items = upload.parseRequest(request);
			
			if (!Uteis.isAtributoPreenchido(documento)) {
				if (items != null && items.stream().anyMatch(t -> t.getFieldName().equals("objeto"))) {
					documento = items.stream().filter(t -> t.getFieldName().equals("objeto")).findFirst().get().getString();
				}
			}

			if (frente == null) {
				if (items != null && items.stream().anyMatch(t -> t.getFieldName().equals("frente"))) {
					frente = Boolean.valueOf(
							items.stream().filter(t -> t.getFieldName().equals("frente")).findFirst().get().getString());
				}else {
					frente =  true;
				}
			}
			if (gravarDocumento == null) {
				if (items != null && items.stream().anyMatch(t -> t.getFieldName().equals("gravarDocumento"))) {
					gravarDocumento = Boolean.valueOf(
							items.stream().filter(t -> t.getFieldName().equals("gravarDocumento")).findFirst().get().getString());
				}else {
					gravarDocumento =  false;
				}
			}

			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy())
					.setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			DocumetacaoMatriculaVO documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
			Type type = new TypeToken<DocumetacaoMatriculaVO>() {
			}.getType();
			documetacaoMatriculaVO = gson.fromJson(documento, type);
			if(!Uteis.isAtributoPreenchido(documetacaoMatriculaVO)) {
				throw new Exception("Documentação Matrícula não cadastrado.");
			}
			if(!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getTipoDeDocumentoVO())) {
				throw new Exception("O TIPO DE DOCUMENTO deve ser informado.");
			}
			
			documetacaoMatriculaVO.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade()
					.consultarPorChavePrimaria(documetacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			if(!documetacaoMatriculaVO.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno()) {
				throw new Exception("O TIPO DE DOCUMENTO "+documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome()+" não está habilitado a postagem pelo aluno/responsável.");
			}
			if((documetacaoMatriculaVO.getDocumetacaoMatriculaAprovado()) && (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED()))) {
				throw new Exception("O TIPO DE DOCUMENTO "+documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome()+" já foi aprovado pela IES e não é possível enviar um novo documento.");
			}
			if(gravarDocumento && documetacaoMatriculaVO.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && ((!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso().getNome()) && frente) || (!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO().getNome()) && !frente))) {
				throw new Exception("Para GRAVAR a entrega do documento "+documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome()+" é necessário enviar o arquivo frente e verso.");
			}
			if(!documetacaoMatriculaVO.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && !frente) {
				throw new Exception("O TIPO DE DOCUMENTO "+documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome()+" não está habilitado o arquivo verso.");
			}
			if (items.stream().anyMatch(i -> !i.isFormField())) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
						documetacaoMatriculaVO.getMatricula(), null, NivelMontarDados.BASICO, usuarioVO);
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory()
						.getConfiguracaoGeralSistemaFacade()
						.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);					
				if (items != null) {
					Iterator<DiskFileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem item = iter.next();
						if (!item.isFormField() && item.getSize() > 0) {						
								if (gravarDocumento && 
									((Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO().getCodigo()) && documetacaoMatriculaVO.getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS))
											|| (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOVerso().getCodigo()) && documetacaoMatriculaVO.getArquivoVOVerso().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS))
											|| (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado().getCodigo()) && documetacaoMatriculaVO.getArquivoVOAssinado().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DOCUMENTOS))
									)) {
									getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(
											documetacaoMatriculaVO, configuracaoGeralSistemaVO, false, usuarioVO);
									documetacaoMatriculaVO.getArquivoVO().setCpfAlunoDocumentacao(matriculaVO.getAluno().getCPF());
								}
//								getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(null, item, documetacaoMatriculaVO, matriculaVO.getAluno(), frente, false, null, usuarioVO,"Aplicativo Aluno");
//								getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(null, item, documetacaoMatriculaVO, matriculaVO.getAluno(), false, false, null, usuarioVO, "Documentação Matrícula");

								if (frente) {
									String caminhoImagemAnexo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()
											+ File.separator + documetacaoMatriculaVO.getArquivoVO().getPastaBaseArquivo()
											+ File.separator + documetacaoMatriculaVO.getArquivoVO().getNome();
									documetacaoMatriculaVO.getArquivoVO().setCaminhoImagemAnexo(caminhoImagemAnexo);

								} else if (!frente) {
									String caminhoImagemAnexo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo()
											+ File.separator
											+ documetacaoMatriculaVO.getArquivoVOVerso().getPastaBaseArquivo()
											+ File.separator + documetacaoMatriculaVO.getArquivoVOVerso().getNome();
									documetacaoMatriculaVO.getArquivoVOVerso().setCaminhoImagemAnexo(caminhoImagemAnexo);
								}
								if(gravarDocumento) {
									getFacadeFactory().getDocumetacaoMatriculaFacade().alterar(documetacaoMatriculaVO, matriculaVO.getAluno(), usuarioVO, configuracaoGeralSistemaVO);
								}

							
						}
					}
				}
			}
			return documetacaoMatriculaVO;
		}
		
		
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/realizarUploadArquivoEspecificoValidandoSessaoNavegador")
		public Response realizarUploadArquivoEspecificoValidandoSessaoNavegadorV1(@Context HttpServletRequest request) {
			return Response.status(Status.OK).entity(realizarUploadArquivoEspecificoValidandoSessaoNavegador(request)).build();						
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/realizarUploadArquivoEspecificoValidandoSessaoNavegador")
		public Response realizarUploadArquivoEspecificoValidandoSessaoNavegadorV2(@Context HttpServletRequest request) {
			
			DocumentacaoMatriculaRSVO documentacaoMatriculaRSVO = realizarUploadArquivoEspecificoValidandoSessaoNavegador(request);
			try {
				DocumetacaoMatriculaVO.removerCamposChamadaAPI(documentacaoMatriculaRSVO.getDocumentacaoMatriculaVO());
			for(DocumetacaoMatriculaVO documetacaoMatriculaVO: documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO()) {
				DocumetacaoMatriculaVO.removerCamposNaoUsadoAPI(documetacaoMatriculaVO);
			}
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			String json =	gson.toJson(documentacaoMatriculaRSVO);
			return Response.status(Status.OK).entity(json).build();	
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
				
		}
		
		public DocumentacaoMatriculaRSVO realizarUploadArquivoEspecificoValidandoSessaoNavegador(HttpServletRequest request) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			try {
			
			UsuarioVO usuarioVO = obterUsuarioWebService();
			request.getHeaderNames();
			String documento = request.getHeader("objeto") != null ? request.getHeader("objeto") : "";
			Boolean frente = request.getHeader("frente") != null ? Boolean.valueOf(request.getHeader("frente")) : null;
			
			DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
			JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>(factory);
			List<DiskFileItem> items = upload.parseRequest(request);

			if (!Uteis.isAtributoPreenchido(documento)) {
				if (items != null && items.stream().anyMatch(t -> t.getFieldName().equals("objeto"))) {
					documento = items.stream().filter(t -> t.getFieldName().equals("objeto")).findFirst().get().getString();
				}
			}

			if (frente == null) {
				if (items != null && items.stream().anyMatch(t -> t.getFieldName().equals("frente"))) {
					frente = Boolean.valueOf(
							items.stream().filter(t -> t.getFieldName().equals("frente")).findFirst().get().getString());
				}else {
					frente =  true;
				}
			}
			
			
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			
			DocumentacaoMatriculaRSVO documetacaoMatriculaRSVO = new DocumentacaoMatriculaRSVO();
			Type type = new TypeToken<DocumentacaoMatriculaRSVO>() {}.getType();
			documetacaoMatriculaRSVO = gson.fromJson(documento, type);			
			//documetacaoMatriculaVO = documetacaoMatriculaRSVO.getDocumentacaoMatriculaVO();
			inscricaoVO = getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(documetacaoMatriculaRSVO.getCodigoInscricao() ,documetacaoMatriculaRSVO.getCodigoAutenticacaoNavegador(), documetacaoMatriculaRSVO.getNavegadorAcesso(), false ,"ENTREGADOCUMENTO", usuarioVO);
			documetacaoMatriculaRSVO.setCodigoAutenticacaoNavegador(inscricaoVO.getCodigoAutenticacaoNavegador());			
			DocumetacaoMatriculaVO documetacaoMatriculaVO =  getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(documetacaoMatriculaRSVO.getCodigoDocumentacaoMatricula(), 	Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			//documetacaoMatriculaVO.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(documetacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		    
			if (items.stream().anyMatch(i -> !i.isFormField())) {
				String cpfAluno = getFacadeFactory().getMatriculaFacade()
						.consultarCPFalunoPreencherDocumentacaoMatriculaParaMatriculaOnline(
								documetacaoMatriculaVO.getMatricula());
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory()
						.getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(
								Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				if (frente) {
					documetacaoMatriculaVO.getArquivoVO().setCpfAlunoDocumentacao(cpfAluno);
				} else if (!frente) {
					documetacaoMatriculaVO.getArquivoVOVerso().setCpfAlunoDocumentacao(cpfAluno);
				}
		    	documetacaoMatriculaVO.getUsuario().setCodigo(usuarioVO.getCodigo());
		    	documetacaoMatriculaVO.getUsuario().setNome(usuarioVO.getNome());
		    	documetacaoMatriculaVO.setDataEntrega(new Date());
		    	documetacaoMatriculaVO.setLocalUpload("Aluno");
				if (!configuracaoGeralSistemaVO.getControlaAprovacaoDocEntregue()) {
					documetacaoMatriculaVO.setArquivoAprovadoPeloDep(Boolean.TRUE);
					documetacaoMatriculaVO.setRespAprovacaoDocDep(usuarioVO);
					documetacaoMatriculaVO.setEntregue(true);
				} else {
					documetacaoMatriculaVO.setArquivoAprovadoPeloDep(Boolean.FALSE);
					documetacaoMatriculaVO.setRespAprovacaoDocDep(null);
					documetacaoMatriculaVO.setEntregue(false);
				}		
				if (!documetacaoMatriculaVO.getEntregue()) {
					documetacaoMatriculaVO.setDataNegarDocDep(null);
					documetacaoMatriculaVO.setRespNegarDocDep(null);
					documetacaoMatriculaVO.setJustificativaNegacao(null);
				}			   
			    if (items != null) {
			        Iterator<DiskFileItem> iter = items.iterator();
			        while (iter.hasNext()) {
			            FileItem item = iter.next();
			            if (!item.isFormField() && item.getSize() > 0) {
				            try {
				            	if (item.getName().endsWith(".pdf") || item.getName().endsWith(".PDF")) {
									ByteArrayInputStream inpuStream = ArquivoHelper.criarByteArrayInputStream(item.getInputStream());
									try {
										getFacadeFactory().getArquivoHelper().validarConformidadeArquivoPdf(frente ? documetacaoMatriculaVO.getArquivoVO() : documetacaoMatriculaVO.getArquivoVOVerso(), inpuStream);
										ArquivoHelper.validarArquivoPdfCriptografado(inpuStream);
									} finally {
										if (Objects.nonNull(inpuStream)) {
											inpuStream.close();
											inpuStream = null;
										}
									}
				            	}
				            	if (frente) {
									if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO().getCodigo())) {
										getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(
												documetacaoMatriculaVO, configuracaoGeralSistemaVO, false, usuarioVO);
										documetacaoMatriculaVO.getArquivoVO().setCpfAlunoDocumentacao(cpfAluno);
									}

									getFacadeFactory().getArquivoHelper()
											.upLoadDocumentacaoMatriculaRequerimentoAplicativo(item,
													documetacaoMatriculaVO.getArquivoVO(),
													documetacaoMatriculaVO.getArquivoVO().getCpfAlunoDocumentacao()
															+ documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(),
													configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP,
													usuarioVO);
									String caminhoImagemAnexo = configuracaoGeralSistemaVO
											.getUrlExternoDownloadArquivo() + File.separator
											+ documetacaoMatriculaVO.getArquivoVO().getPastaBaseArquivo()
											+ File.separator + documetacaoMatriculaVO.getArquivoVO().getNome();
									documetacaoMatriculaVO.getArquivoVO().setCaminhoImagemAnexo(caminhoImagemAnexo);

								} else if (!frente) {
									getFacadeFactory().getArquivoHelper()
											.upLoadDocumentacaoMatriculaRequerimentoAplicativo(item,
													documetacaoMatriculaVO.getArquivoVOVerso(),
													documetacaoMatriculaVO.getArquivoVOVerso().getCpfAlunoDocumentacao()
															+ documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome(),
													configuracaoGeralSistemaVO, PastaBaseArquivoEnum.DOCUMENTOS_TMP,
													usuarioVO);
									String caminhoImagemAnexo = configuracaoGeralSistemaVO
											.getUrlExternoDownloadArquivo() + File.separator
											+ documetacaoMatriculaVO.getArquivoVOVerso().getPastaBaseArquivo()
											+ File.separator + documetacaoMatriculaVO.getArquivoVOVerso().getNome();
									documetacaoMatriculaVO.getArquivoVOVerso()
											.setCaminhoImagemAnexo(caminhoImagemAnexo);
								}
				            	
				            } catch (Exception e) {
				            	e.printStackTrace();
					            if (Uteis.isAtributoPreenchido(e.getMessage()) 
										&& (e.getMessage().equals("PDF válido, porém criptografado (senha necessária).") 
												|| e.getMessage().equals("Arquivo não é um PDF válido ou está corrompido.")
												|| e.getMessage().equals("O arquivo é identificado como PDF/A, mas não está em conformidade com os padrões requeridos. Existem irregularidades técnicas que impedem sua utilização")
												|| e.getMessage().equals("O arquivo PDF apresenta inconsistências em sua formatação interna que impedem a validação. Recomendamos gerar um novo arquivo ou solicitar uma versão corrigida do documento."))) {
				            		throw e;
				            	}
				           }
			            }
			        }
			    }
			}
		    getFacadeFactory().getDocumetacaoMatriculaFacade().removerVinculoMotivoIndeferimentoDocumentoAluno(documetacaoMatriculaVO, usuarioVO);
		    documetacaoMatriculaRSVO.setDocumentacaoMatriculaVO(documetacaoMatriculaVO);
		    if (Uteis.isAtributoPreenchido(inscricaoVO)) {
		    	getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO,usuarioVO);	
		    }
		    return documetacaoMatriculaRSVO;
			} catch (Exception e) {				
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				if (!Uteis.isAtributoPreenchido(inscricaoVO.getCodigoAutenticacaoNavegador())) {
					errorInfoRSVO.setStatusCode(Status.CONFLICT.getStatusCode());
					errorInfoRSVO.setCodigo(Status.CONFLICT.name());
					errorInfoRSVO.setCampo(Status.CONFLICT.name());
					errorInfoRSVO.setMensagemErro(Status.CONFLICT.name());
				}else {
				   errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				}
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		
	}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarDocumentacaoMatricula")
		public Response gravarDocumentacaoMatriculaV1(final String paramObject) {
			try {
				return Response.status(Status.OK).entity(new GenericEntity<List<DocumetacaoMatriculaVO>>(
						gravarDocumentacaoMatricula(paramObject)) {
				}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}		
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/gravarDocumentacaoMatricula")
		public Response gravarDocumentacaoMatriculaV2(final String paramObject) {
			try {
				return DocumetacaoMatriculaVO.removerCamposChamadaAPI(gravarDocumentacaoMatricula(paramObject));
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
			
		public List<DocumetacaoMatriculaVO> gravarDocumentacaoMatricula(final String paramObject) throws Exception {
			
//				System.out.println(paramObject);
				UsuarioVO usuarioVO = obterUsuarioWebService();
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory()
						.getConfiguracaoGeralSistemaFacade()
						.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy())
						.setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				DocumentacaoMatriculaRSVO documentacaoMatriculaRSVO = new DocumentacaoMatriculaRSVO();
				Type type = new TypeToken<DocumentacaoMatriculaRSVO>() {
				}.getType();
				documentacaoMatriculaRSVO = gson.fromJson(paramObject, type);
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
						documentacaoMatriculaRSVO.getMatricula(), null, NivelMontarDados.BASICO, usuarioVO);
				if (Uteis.isAtributoPreenchido(documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO())) {
					for (DocumetacaoMatriculaVO documetacaoMatriculaVO : documentacaoMatriculaRSVO
							.getListaDocumentacaoMatriculaVO()) {
						if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getCodigo())) {
							documetacaoMatriculaVO.setNovoObj(false);
						}
					}
					getFacadeFactory().getDocumetacaoMatriculaFacade().gravarDocumentacaoMatriculaVisaoAluno(
							documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO(), matriculaVO, usuarioVO,
							configuracaoGeralSistemaVO, Boolean.FALSE);
					realizarPreencherCaminhoAnexoImagem(documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO(),
							configuracaoGeralSistemaVO);
				}
				return documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO();
			
		}
		
		
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarDocumentacaoMatriculaEspecifico")
		public Response gravarDocumentacaoMatriculaEspecificoV1(final String paramObject) {
			try {

				return Response.status(Status.OK).entity(
						new GenericEntity<DocumentacaoMatriculaRSVO>(gravarDocumentacaoMatriculaEspecifico(paramObject)) {
						}).build();
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}

		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/gravarDocumentacaoMatriculaEspecifico")
		public Response gravarDocumentacaoMatriculaEspecificoV2(final String paramObject) {
			try {
				DocumentacaoMatriculaRSVO documentacaoMatriculaRSVO = gravarDocumentacaoMatriculaEspecifico(paramObject); 
				DocumetacaoMatriculaVO.removerCamposNaoUsadoAPI(documentacaoMatriculaRSVO.getDocumentacaoMatriculaVO());
				for(DocumetacaoMatriculaVO documetacaoMatriculaVO: documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO()) {
					DocumetacaoMatriculaVO.removerCamposNaoUsadoAPI(documetacaoMatriculaVO);
				}
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
				String json =	gson.toJson(documentacaoMatriculaRSVO);
				return Response.status(Status.OK).entity(json).build();			
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		public DocumentacaoMatriculaRSVO gravarDocumentacaoMatriculaEspecifico(final String paramObject) throws Exception {

			// System.out.println(paramObject);
			UsuarioVO usuarioVO = obterUsuarioWebService();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy())
					.setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			DocumentacaoMatriculaRSVO documentacaoMatriculaRSVO = new DocumentacaoMatriculaRSVO();

			Type type = new TypeToken<DocumentacaoMatriculaRSVO>() {
			}.getType();
			documentacaoMatriculaRSVO = gson.fromJson(paramObject, type);
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade()
					.validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(
							documentacaoMatriculaRSVO.getCodigoInscricao(),
							documentacaoMatriculaRSVO.getCodigoAutenticacaoNavegador(),
							documentacaoMatriculaRSVO.getNavegadorAcesso(), false, "ENTREGADOCUMENTO", usuarioVO);
			documentacaoMatriculaRSVO.setCodigoAutenticacaoNavegador(inscricaoVO.getCodigoAutenticacaoNavegador());
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					documentacaoMatriculaRSVO.getMatricula(), null, NivelMontarDados.BASICO, usuarioVO);

			if (!documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO().isEmpty()) {
				documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO().get(0).setNovoObj(false);
				getFacadeFactory().getDocumetacaoMatriculaFacade().gravarDocumentacaoMatriculaVisaoAluno(
						documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO(), matriculaVO, usuarioVO,
						configuracaoGeralSistemaVO, Boolean.FALSE);
				realizarPreencherCaminhoAnexoImagem(documentacaoMatriculaRSVO.getListaDocumentacaoMatriculaVO(),
						configuracaoGeralSistemaVO);
			}
			if (Uteis.isAtributoPreenchido(inscricaoVO)) {
				getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO, usuarioVO);
			}
			return documentacaoMatriculaRSVO;
		}
		

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarTipoRequerimentoAluno/{codigoPessoa}/{matricula}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
		public Response consultarTipoRequerimentoAlunoV2(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("matricula") final String matricula, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			List<TipoRequerimentoVO> tipoRequerimentoVOs = consultarTipoRequerimentoAluno(codigoPessoa, matricula, tipoNivelEducacional, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso);
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json =	gson.toJson(tipoRequerimentoVOs);
			return Response.status(Status.OK).entity(json).build();
		}
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarTipoRequerimentoAluno/{codigoPessoa}/{matricula}/{tipoNivelEducacional}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
		public List<TipoRequerimentoVO> consultarTipoRequerimentoAlunoV1(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("matricula") final String matricula, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
			return consultarTipoRequerimentoAluno(codigoPessoa, matricula, tipoNivelEducacional, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso);
		}
		
		public List<TipoRequerimentoVO> consultarTipoRequerimentoAluno(Integer codigoPessoa, String matricula, String tipoNivelEducacional, Integer codigoUnidadeEnsinoMatricula, Integer codigoPerfilAcesso) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("aluno");
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
				List<TipoRequerimentoVO> tipoRequerimentoVOs = new ArrayList<>(0);
				tipoRequerimentoVOs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoAluno(Boolean.TRUE, "AT", codigoUnidadeEnsinoMatricula, matriculaVO.getCurso().getCodigo(), matricula,  Uteis.NIVELMONTARDADOS_DADOSBASICOS,  false, usuarioVO);
				for (Iterator iterator = tipoRequerimentoVOs.iterator(); iterator.hasNext();) {
					TipoRequerimentoVO tipoRequerimentoVO = (TipoRequerimentoVO) iterator.next();
					            			
					if(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getNivelEducacional())) {
						if(!tipoRequerimentoVO.getNivelEducacional().equals(matriculaVO.getCurso().getNivelEducacional())) {
							iterator.remove();
						}
					}
				}
				return tipoRequerimentoVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/novoRequerimentoAluno/{codigoPessoa}/{matricula}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}/{codigoTipoRequerimento}")
		public Response novoRequerimentoAlunoV2(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("matricula") final String matricula, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoTipoRequerimento") final Integer codigoTipoRequerimento) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(novoRequerimentoAluno(codigoPessoa, matricula, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso, codigoTipoRequerimento));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/novoRequerimentoAluno/{codigoPessoa}/{matricula}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}/{codigoTipoRequerimento}")
		public RequerimentoVO novoRequerimentoAlunoV1(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("matricula") final String matricula, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoTipoRequerimento") final Integer codigoTipoRequerimento) {
			return novoRequerimentoAluno(codigoPessoa, matricula, codigoUnidadeEnsinoMatricula, codigoPerfilAcesso, codigoTipoRequerimento);
		}
			public RequerimentoVO novoRequerimentoAluno(@PathParam("codigoPessoa") final Integer codigoPessoa, @PathParam("matricula") final String matricula, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("codigoTipoRequerimento") final Integer codigoTipoRequerimento) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				usuarioVO.setVisaoLogar("aluno");
				usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
				RequerimentoVO requerimentoVO = new RequerimentoVO();
				TipoRequerimentoVO tipoRe = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(codigoTipoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
				requerimentoVO.setMatricula(matriculaVO);
				requerimentoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(requerimentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, null));
				requerimentoVO = getFacadeFactory().getRequerimentoFacade().inicializarRequerimentoConformeTipoRequerimento(tipoRe, requerimentoVO, true);
				requerimentoVO.setPessoa(matriculaVO.getAluno());
				getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(requerimentoVO, usuarioVO);
				if(requerimentoVO.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
					requerimentoVO.getListaDisciplina().addAll(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasAptasAproveitamentoDisciplina(requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, null, requerimentoVO.getTipoRequerimento()));
				}else {					
					requerimentoVO.getListaDisciplina().addAll(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorMatriculaAptoVincularRequerimento(requerimentoVO.getMatricula().getMatricula(), "", "", tipoRe, null));
				}
				if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna()) {
					requerimentoVO.setListaUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(null, false, true, usuarioVO));
				}
//				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo(), null);
//				requerimentoVO.setCentroReceita(configuracaoFinanceiroVO.getCentroReceitaRequerimentoPadrao());
//				requerimentoVO.getContaCorrenteVO().setCodigo(configuracaoFinanceiroVO.getContaCorrentePadraoRequerimento());
				try {
					if (getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario(
							"RequerimentoPermitirRequerenteAnexarArquivo", usuarioVO)) {
						requerimentoVO.setRequerimentoPermitirRequerenteAnexarArquivo(Boolean.TRUE);
					}
				} catch (Exception e) {
					requerimentoVO.setRequerimentoPermitirRequerenteAnexarArquivo(Boolean.FALSE);
				}
				return requerimentoVO;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarCurso/{unidadeEnsino}/{nivelEducacional}/{nomeCurso}")
		public Response consultarCursoV2(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("nivelEducacional") final String nivelEducacional, @PathParam("nomeCurso") final String nomeCurso) {
			Gson gson =  new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();			
			return Response.status(Status.OK).entity(gson.toJson(consultarCurso(unidadeEnsino, nivelEducacional, "%%"))).build();
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarCurso/{unidadeEnsino}/{nivelEducacional}/{nomeCurso}")
		public List<UnidadeEnsinoCursoVO> consultarCursoV1(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("nivelEducacional") final String nivelEducacional, @PathParam("nomeCurso") final String nomeCurso) {
			return consultarCurso(unidadeEnsino, nivelEducacional, nomeCurso);
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarCurso/{unidadeEnsino}/{nivelEducacional}")
		public List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsinoNivelEducacionalV1(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("nivelEducacional") final String nivelEducacional) {
			return consultarCurso(unidadeEnsino, nivelEducacional, "%%");
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarCurso/{unidadeEnsino}/{nivelEducacional}")
		public Response consultarCursoPorUnidadeEnsinoNivelEducacionalV2(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("nivelEducacional") final String nivelEducacional) {
			Gson gson =  new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();			
			return Response.status(Status.OK).entity(gson.toJson(consultarCurso(unidadeEnsino, nivelEducacional, "%%"))).build();
		}
		
		public List<UnidadeEnsinoCursoVO> consultarCurso(Integer unidadeEnsino, String nivelEducacional,String nomeCurso) {
			try {
				UsuarioVO usuarioVO = null;
				try {
					autenticarTokenCliente();
				}catch (Exception e) {
					usuarioVO = autenticarConexao();
					usuarioVO.setVisaoLogar("aluno");
				}
				List<UnidadeEnsinoCursoVO> cursoVOs = new ArrayList<UnidadeEnsinoCursoVO>();
				cursoVOs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsinoNivelEducacional(nomeCurso, unidadeEnsino, nivelEducacional, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO, "");
				return cursoVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarEndereco/{cep}")
		public EnderecoVO consultarEndereco(@PathParam("cep") final String cep) {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				EnderecoVO enderecoVO = new EnderecoVO();
				List<EnderecoVO> listaEndereco = getFacadeFactory().getEnderecoFacade().consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
		        if (listaEndereco.size() > 0) {
		            enderecoVO = listaEndereco.get(0);
		        }
				return enderecoVO;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarEstado")
		public List<EstadoVO> consultarEstado() {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				List<EstadoVO> estadoVOs = new ArrayList<EstadoVO>(0);
				estadoVOs= getFacadeFactory().getEstadoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				estadoVOs.add(0, new EstadoVO());
				return estadoVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarCidade/{siglaEstado}/{nomeCidade}")
		public List<CidadeVO> consultarCidade(@PathParam("siglaEstado") final String siglaEstado, @PathParam("nomeCidade") final String nomeCidade) {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
				List<CidadeVO> cidadeVOs = new ArrayList<CidadeVO>();
				
				cidadeVOs.addAll( getFacadeFactory().getCidadeFacade().consultarPorNomeSiglaEstado(nomeCidade, siglaEstado, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
				
				return cidadeVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarTurmaReposicao")
		public Response consultarTurmaReposicaoV2(final String paramObject) {
				Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				String json =	gson.toJson(consultarTurmaReposicao(paramObject));
				return Response.status(Status.OK).entity(json).build();					
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarTurmaReposicao")
		public List<TurmaVO> consultarTurmaReposicaoV1(final String paramObject) {
			return consultarTurmaReposicao(paramObject);
		}
		public List<TurmaVO> consultarTurmaReposicao(final String paramObject) {
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>();
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			Map<Integer, List<TurmaVO>> mapTurmas = new HashMap<Integer, List<TurmaVO>>();
			Type type = new TypeToken<RequerimentoVO>() {
			}.getType();
			requerimentoVO = gson.fromJson(paramObject, type);
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				if (Uteis.isAtributoPreenchido(requerimentoVO.getDisciplina())) {
					requerimentoVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(
							requerimentoVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,
							usuarioVO));
				}
				requerimentoVO.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));	
				if (requerimentoVO.getTipoRequerimento().getIsTipoReposicao()
						&& Uteis.isAtributoPreenchido(requerimentoVO.getDisciplina())) {
//					getFacadeFactory().getRequerimentoFacade().realizarValidacaoCobrancaRequerimento(requerimentoVO);
					requerimentoVO.setCargaHorariaDisciplina(getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaDisciplinaPorDisciplinaETurma(requerimentoVO.getDisciplina().getCodigo(), requerimentoVO.getMatricula().getMatricula(), usuarioVO));
					if (!Uteis.isAtributoPreenchido(requerimentoVO.getUnidadeEnsino().getCodigo())) {
						requerimentoVO.setUnidadeEnsino(requerimentoVO.getMatricula().getUnidadeEnsino());
					}
					getFacadeFactory().getRequerimentoFacade().montarListaSelectItemTurmaAdicionar(requerimentoVO, turmaVOs, usuarioVO, mapTurmas);
				}
				if (!Uteis.isAtributoPreenchido(turmaVOs)) {
					throw new Exception("Nenhuma Turma Encontrada Para Reposição");
				}
				return turmaVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
				
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/consultarMapaEquivalenciaDisciplina/")
		public Response consultarMapaEquivalenciaDisciplinaV2(final String paramObject) {
			Gson gson =  new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();			
			return Response.status(Status.OK).entity(gson.toJson(consultarMapaEquivalenciaDisciplina(paramObject))).build();
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/consultarMapaEquivalenciaDisciplina/")
		public List<MapaEquivalenciaDisciplinaVO> consultarMapaEquivalenciaDisciplinaV1(final String paramObject) {
			return consultarMapaEquivalenciaDisciplina(paramObject);
		}
		public List<MapaEquivalenciaDisciplinaVO> consultarMapaEquivalenciaDisciplina(final String paramObject) {
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs = new ArrayList<MapaEquivalenciaDisciplinaVO>();
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			Type type = new TypeToken<RequerimentoVO>() {
			}.getType();
			requerimentoVO = gson.fromJson(paramObject, type);
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				mapaEquivalenciaDisciplinaVOs = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(requerimentoVO.getMatricula().getGradeCurricularAtual().getCodigo(), requerimentoVO.getDisciplina().getCodigo(), NivelMontarDados.TODOS, true);
				if (mapaEquivalenciaDisciplinaVOs.isEmpty()) {
					throw new Exception("Não Existem Equivalências Disponíveis para Esta Disciplina.");
				}
				return mapaEquivalenciaDisciplinaVOs;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/validarTurmaReposicao/")
		public Response validarTurmaReposicaoV2(final String paramObject) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(realizarValidacaoTurmaReposicao(paramObject));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);			
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/validarTurmaReposicao/")
		public RequerimentoVO validarTurmaReposicao(final String paramObject) {
			return realizarValidacaoTurmaReposicao(paramObject);
		}
		
		private RequerimentoVO realizarValidacaoTurmaReposicao(final String paramObject) {
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs = new ArrayList<MapaEquivalenciaDisciplinaVO>();
			Map<Integer, List<TurmaVO>> mapTurmas = new HashMap<Integer, List<TurmaVO>>();
			List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>();
			RequerimentoTurmaReposicaoRSVO requerimentoTurmaReposicaoRSVO = new RequerimentoTurmaReposicaoRSVO();
			Type type = new TypeToken<RequerimentoTurmaReposicaoRSVO>() {
			}.getType();
			requerimentoTurmaReposicaoRSVO = gson.fromJson(paramObject, type);
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				TurmaVO turmaConsiderarVagasReposicaoTurmaAgrupada = null;
				requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				Integer disciplinaConsultarTurma = requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getDisciplina().getCodigo();
				if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getDisciplinaPorEquivalencia()) {	
					disciplinaConsultarTurma = requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getDisciplinaVO().getCodigo();
				}
				if (!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getUnidadeEnsino().getCodigo())) {
					requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setUnidadeEnsino(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getUnidadeEnsino());
				}
				if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao()
						&& Uteis.isAtributoPreenchido(disciplinaConsultarTurma)) {
//					getFacadeFactory().getRequerimentoFacade().realizarValidacaoCobrancaRequerimento(requerimentoTurmaReposicaoRSVO.getRequerimentoVO());
					requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setCargaHorariaDisciplina(getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaDisciplinaPorDisciplinaETurma(disciplinaConsultarTurma, requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatricula(), usuarioVO));
					getFacadeFactory().getRequerimentoFacade().montarListaSelectItemTurmaAdicionar(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), turmaVOs, usuarioVO, mapTurmas);
				}
				
				if (Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getTurmaVO().getCodigo())) {
					getFacadeFactory().getTurmaFacade().carregarDados(requerimentoTurmaReposicaoRSVO.getTurmaVO(), NivelMontarDados.BASICO, usuarioVO);
					
					getFacadeFactory().getRequerimentoFacade().definirTurmaBaseRequerimentosTurmaAgrupada(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), turmaVOs, usuarioVO, mapTurmas, requerimentoTurmaReposicaoRSVO.getTurmaVO());
				}
				
				requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatriculaComHistoricoAlunoVO().setNivelMontarDados(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS);
//				getFacadeFactory().getRequerimentoFacade().realizarValidacaoChoqueHorarioEVagaTurmaRequerimentoReposicao(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getUnidadeEnsino().getCodigo()), usuarioVO);
				return requerimentoTurmaReposicaoRSVO.getRequerimentoVO();
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST		
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/uploadArquivoRequerimento")
		public Response uploadArquivoRequerimentoV2(@Context HttpServletRequest request) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(realizarUploadArquivoRequerimento(request));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				System.out.println("MOBILE ERROR: " + e.getMessage());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST		
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/uploadArquivoRequerimento")
		public RequerimentoVO uploadArquivoRequerimento(@Context HttpServletRequest request) {
			return realizarUploadArquivoRequerimento(request);
		}
		
		public RequerimentoVO realizarUploadArquivoRequerimento(@Context HttpServletRequest request) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			request.getHeaderNames();
			String requerimento = request.getHeader("objeto") != null ? request.getHeader("objeto") : "";
			DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
			JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>(factory);
			List<DiskFileItem> items = upload.parseRequest(request);	    
			if(!Uteis.isAtributoPreenchido(requerimento)) {
				if (items != null) {
					requerimento = items.stream().filter(t -> t.getFieldName().equals("requerimento")).findFirst().get().getString();		        
				}
			}
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			Type type = new TypeToken<RequerimentoVO>() {
			}.getType();
			requerimentoVO = gson.fromJson(requerimento, type);
			String tipoArquivo = request.getHeader("tipoArquivo") != null ? request.getHeader("tipoArquivo") : "";
			if(!Uteis.isAtributoPreenchido(tipoArquivo)) {
				if (items != null) {
					tipoArquivo = items.stream().filter(t -> t.getFieldName().equals("tipoArquivo")).findFirst().get().getString();		        
				}
			}
			String nomeArquivo = request.getHeader("nomeArquivo") != null ? request.getHeader("nomeArquivo") : "";
			if(!Uteis.isAtributoPreenchido(nomeArquivo)) {
				if (items != null) {
					nomeArquivo = items.stream().filter(t -> t.getFieldName().equals("nomeArquivo")).findFirst().get().getString();		        
				}
			}
			String descricaoArquivo = request.getHeader("descricaoArquivo") != null ? request.getHeader("descricaoArquivo") : "";
			if(!Uteis.isAtributoPreenchido(descricaoArquivo)) {
				if (items != null) {
					descricaoArquivo = items.stream().filter(t -> t.getFieldName().equals("descricaoArquivo")).findFirst().get().getString();		        
				}
			}
			if(!Uteis.isAtributoPreenchido(descricaoArquivo)) {
				descricaoArquivo =  nomeArquivo;
			}		
			Boolean gravarArquivoAnexo = request.getHeader("gravarArquivoAnexo") != null ? Boolean.parseBoolean(request.getHeader("gravarArquivoAnexo")) : null;
			if(gravarArquivoAnexo == null) {
				if (items != null) {
					String gravar = items.stream().filter(t -> t.getFieldName().equals("gravarArquivoAnexo")).findFirst().get().getString();
					gravarArquivoAnexo = Uteis.isAtributoPreenchido(gravar) ? Boolean.parseBoolean(gravar) : Uteis.isAtributoPreenchido(requerimentoVO.getCodigo());
				}
			}
			if (items.stream().anyMatch(i -> !i.isFormField())) {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory()
						.getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(
								Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				    if (items != null) {
				        Iterator<DiskFileItem> iter = items.iterator();
//				        if (items.isEmpty() || iter.forEachRemaining( t -> !t.isFormField())) {
//				        	throw new Exception("Arquivo não está presente.");
//				        }
				        Boolean possuiAnexo  = false;
				        while (iter.hasNext()) {
				            FileItem item = iter.next();
				            if (!item.isFormField() && item.getSize() > 0) {
				            	possuiAnexo =  true;
					            try {
					            	requerimentoVO.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
					            	getFacadeFactory().getPessoaFacade().carregarDados(requerimentoVO.getPessoa(), usuarioVO);
//					            	if (Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO())) {
//					            		requerimentoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(requerimentoVO.getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(requerimentoVO.getUnidadeEnsino().getCodigo()), usuarioVO));
//					    			}
					            	if (tipoArquivo.equals("ARQUIVO_ANEXO")) {
					            		MaterialRequerimentoVO materialRequerimentoVO = new MaterialRequerimentoVO();
					            		materialRequerimentoVO.getArquivoVO().setCpfRequerimento(requerimentoVO.getPessoa().getCPF());
					            		materialRequerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
					            		materialRequerimentoVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
					            		materialRequerimentoVO.setUsuarioDisponibilizouArquivo(usuarioVO);
					            		if (Uteis.isAtributoPreenchido(descricaoArquivo)) {
					            			materialRequerimentoVO.setDescricao(descricaoArquivo);		            			
					            		} else {
					            			materialRequerimentoVO.setDescricao(item.getName());
					            		}
					            		getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(item, materialRequerimentoVO.getArquivoVO(), materialRequerimentoVO.getDescricao(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.REQUERIMENTOS_TMP, usuarioVO);
					            		if (!materialRequerimentoVO.getArquivoVO().getCpfRequerimento().equals("")) {
					            			materialRequerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + materialRequerimentoVO.getArquivoVO().getCpfRequerimento());
					    				} 
					            		adicionarMaterialRequerimento(requerimentoVO, materialRequerimentoVO, usuarioVO, gravarArquivoAnexo);
					            	} else if (tipoArquivo.equals("ARQUIVO_SOLICITACAO_ISENCAO_TAXA")) {
					            		requerimentoVO.getComprovanteSolicitacaoIsencao().setCpfRequerimento(requerimentoVO.getPessoa().getCPF());
					            		requerimentoVO.getComprovanteSolicitacaoIsencao().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
					            		requerimentoVO.getComprovanteSolicitacaoIsencao().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
					            		requerimentoVO.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA);
					        			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(item, requerimentoVO.getComprovanteSolicitacaoIsencao(), requerimentoVO.getComprovanteSolicitacaoIsencao().getCpfRequerimento() + requerimentoVO.getTipoRequerimento().getNome(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.REQUERIMENTOS_TMP, usuarioVO);
					        			if (!requerimentoVO.getComprovanteSolicitacaoIsencao().getCpfRequerimento().equals("")) {
					            			requerimentoVO.getComprovanteSolicitacaoIsencao().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + requerimentoVO.getComprovanteSolicitacaoIsencao().getCpfRequerimento());
					    				} 
					            	} else if (tipoArquivo.equals("ARQUIVO_APROVEITAMENTO_DISCIPLINA")) {
					            		requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setCpfRequerimento(requerimentoVO.getPessoa().getCPF());
					            		requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
					            		requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
					            		requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setDescricao(descricaoArquivo);
					            		getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(item, requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino(), descricaoArquivo, configuracaoGeralSistemaVO, PastaBaseArquivoEnum.REQUERIMENTOS_TMP, usuarioVO);
					        			if (!requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getCpfRequerimento().equals("")) {
					            			requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + requerimentoVO.getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getCpfRequerimento());
					    				} 
					            	} else if (tipoArquivo.equals("ARQUIVO_ABERTURA")) {
					            		requerimentoVO.getArquivoVO().setCpfRequerimento(requerimentoVO.getPessoa().getCPF());
					            		getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(item, requerimentoVO.getArquivoVO(), requerimentoVO.getArquivoVO().getCpfRequerimento() + requerimentoVO.getTipoRequerimento().getNome(), configuracaoGeralSistemaVO, PastaBaseArquivoEnum.REQUERIMENTOS_TMP, usuarioVO);
					        			registrarAtividadeUsuario(usuarioVO, "RequerimentoControle", "Iniciando Adicionar Arquivo Requerimento", "Uploading");
					        			Uteis.checkState(!Uteis.isAtributoPreenchido(requerimentoVO.getArquivoVO().getNome()), "Arquivo nao Encontrado por favor verificar as extensões ou local do arquivo");
					        			requerimentoVO.getArquivoVO().setResponsavelUpload(usuarioVO);
					        			requerimentoVO.getArquivoVO().setDataUpload(new Date());
					        			requerimentoVO.getArquivoVO().setManterDisponibilizacao(true);
					        			requerimentoVO.getArquivoVO().setDataDisponibilizacao(requerimentoVO.getArquivoVO().getDataUpload());
					        			requerimentoVO.getArquivoVO().setDataIndisponibilizacao(null);
					        			requerimentoVO.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
					        			requerimentoVO.getArquivoVO().setOrigem(OrigemArquivo.REQUERIMENTO.getValor());
					        			if (!requerimentoVO.getArquivoVO().getCpfRequerimento().equals("")) {
					        				requerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + requerimentoVO.getArquivoVO().getCpfRequerimento());
					    				} else {
					    					requerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
					    				}
					        			requerimentoVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
					        			requerimentoVO.setExcluirArquivo(false);
					        			if (Uteis.isAtributoPreenchido(requerimentoVO.getCodigo())) {
					        				getFacadeFactory().getRequerimentoFacade().carregarDadoCampoServidorOnlineArquivo(requerimentoVO, usuarioVO);
					        				if (!requerimentoVO.getArquivoVO().getNome().equals("")) {
					        					if (!requerimentoVO.getArquivoVO().getCodigo().equals(0)) {
					        						getFacadeFactory().getArquivoFacade().alterar(requerimentoVO.getArquivoVO(), usuarioVO, getConfiguracaoGeralPadraoSistema());
					        					} else {
					        						getFacadeFactory().getArquivoFacade().incluir(requerimentoVO.getArquivoVO(), usuarioVO, getConfiguracaoGeralPadraoSistema());
					        						getFacadeFactory().getRequerimentoFacade().alterarCodigoArquivo(requerimentoVO, requerimentoVO.getArquivoVO().getCodigo(), usuarioVO);
					        					}
					        				}
					        			}
					            	}
					            } catch (Exception e) {
					            	throw new Exception(e.getMessage());
					           }
				            }
				        }
				        if(!possuiAnexo) {
				        	throw new Exception("Arquivo não está presente.");
				        }
				    } else {
				    	throw new Exception("Arquivo não está presente.");
				    }
				} else {
					throw new Exception("Formato Inválido.");
				}
			 
			 
		    return requerimentoVO;
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
	
		
		public void adicionarMaterialRequerimento(RequerimentoVO requerimentoVO, MaterialRequerimentoVO materialRequerimentoVO, UsuarioVO usuarioVO, Boolean gravarArquivoAnexo) throws Exception {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			RequerimentoHistoricoVO requerimentoHistoricoAtual = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
			if (materialRequerimentoVO.getDescricao() == null || materialRequerimentoVO.getDescricao().equals("")) {
				throw new Exception("O campo DESCRIÇÃO deve ser informado.");
			}
			if (materialRequerimentoVO.getArquivoVO().getNome() == null || materialRequerimentoVO.getArquivoVO().getNome().equals("")) {
				throw new Exception("O campo ARQUIVO deve ser informado.");
			}
			materialRequerimentoVO.setDataDisponibilizacaoArquivo(new Date());
			materialRequerimentoVO.setUsuarioDisponibilizouArquivo(usuarioVO);
			if(!requerimentoHistoricoAtual.getCodigo().equals(0)) {				
				materialRequerimentoVO.setRequerimentoHistorico(requerimentoHistoricoAtual);
			}
			materialRequerimentoVO.setDisponibilizarParaRequerente(true);
			requerimentoVO.getMaterialRequerimentoVOs().add(materialRequerimentoVO);
			if (gravarArquivoAnexo) {
				getFacadeFactory().getMaterialRequerimentoFacade().incluirMaterialRequerimentos(requerimentoVO.getCodigo(), requerimentoVO.getMaterialRequerimentoVOs(), usuarioVO, configuracaoGeralSistemaVO);
				getFacadeFactory().getRequerimentoFacade().alterarDataUltimaAlteracao(requerimentoVO.getCodigo());
			}
			if(!requerimentoVO.getNovoObj() && gravarArquivoAnexo) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, usuarioVO);
			}
		}
		
		
		
		
		
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/consultarQuestionarioRequerimentoParaResponder/")
	public Response consultarQuestionarioRequerimentoParaResponderV2(final String paramObject) {
		try {
			return RequerimentoVO.removerCamposChamadaAPI(consultarQuestionarioRequerimentoParaResponder(paramObject));
		} catch (WebServiceException e) {
			throw e;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarQuestionarioRequerimentoParaResponder/")
	public RequerimentoVO consultarQuestionarioRequerimentoParaResponderV1(final String paramObject) {
		return consultarQuestionarioRequerimentoParaResponder(paramObject);
	}
	
	public RequerimentoVO consultarQuestionarioRequerimentoParaResponder(final String paramObject) {
		Gson gson = new GsonBuilder()
				.setDateFormat("MM-dd-yyyy HH:mm:ss")
				.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
				.create();
		RequerimentoVO requerimentoVO = new RequerimentoVO();
		Type type = new TypeToken<RequerimentoVO>() {
		}.getType();
		requerimentoVO = gson.fromJson(paramObject, type);
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			if (requerimentoVO.getTipoRequerimento().getQuestionario().getCodigo() > 0) {
				if (requerimentoVO.getTipoRequerimento().getQuestionario().getCodigo() != 0 && requerimentoVO.getCodigo().equals(0)) {
//					if (requerimentoVO.getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
						requerimentoVO.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//					}
				} else if (requerimentoVO.getQuestionarioVO().getCodigo() != 0 && !requerimentoVO.getCodigo().equals(0)) {
//					if (requerimentoVO.getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
						requerimentoVO.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(requerimentoVO.getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
						getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimento(requerimentoVO.getCodigo().intValue(), requerimentoVO.getQuestionarioVO());
//					}
				}
			}
			return requerimentoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/realizarEmissaoArquivoRequerimento/")
	public String realizarEmissaoArquivoRequerimento(final String paramObject) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			RequerimentoTurmaReposicaoRSVO requerimentoTurmaReposicaoRSVO = new RequerimentoTurmaReposicaoRSVO();
			Type type = new TypeToken<RequerimentoTurmaReposicaoRSVO>() {
			}.getType();
			requerimentoTurmaReposicaoRSVO = gson.fromJson(paramObject, type);
			requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setEdicao(true);
			String caminhoArquivo = "";
			String formatoCertificadoSelecionado = requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getFormatoCertificadoSelecionado();
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
			if (Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo())) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), usuarioVO);
				if (!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getFormatoCertificadoSelecionado())) {
					requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setFormatoCertificadoSelecionado(formatoCertificadoSelecionado);
				}
			}
			if (Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatriculaPeriodoVO())) {
				requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			if (!getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao().endsWith("/")) {
				getConfiguracaoGeralSistemaVO().setUrlAcessoExternoAplicacao(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao() + "/");
			}
			if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("VISUALIZACAO_CERTIFICADO")) {
				if(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("EC")) {
					//toDo if(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTipo().equals("EC") && requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getNovoObj()) {
					getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), getUsuarioLogado());
					if(Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
						DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
						if (Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo())) {
							documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatricula(), requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO.name(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
						}
						Boolean gerarNovoArquivoAssinado = (!Uteis.isAtributoPreenchido(documentoAssinadoVO)) && Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo());
						Boolean persistirDocumentoAssinado = Uteis.isAtributoPreenchido(documentoAssinadoVO) && Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo());
						List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
						caminhoArquivo = montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs, requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), textoPadraoDeclaracaoVO, gerarNovoArquivoAssinado, persistirDocumentoAssinado, usuarioVO, caminhoArquivo);
					}
				}
				
			} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("CERTIFICADO")) {
				if (!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getSituacao().equals("FD")) {
					throw new Exception("Esse requerimento não está finalizado/deferido");
				}
				if (!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getPermitirImpressaoCertificado() || !Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTextoPadrao())) {
					throw new Exception("Esse requerimento não está apto a ser impresso");
				}
				if(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital()) {
					if(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getIsDeferido()) {
						Boolean gerarNovoArquivoAssinado = false;
						List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
						caminhoArquivo = montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs, requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), textoPadraoDeclaracaoVO, gerarNovoArquivoAssinado, true, usuarioVO, caminhoArquivo);
						if(!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getDataUltimaImpressao())) {
							getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo());
						}
					}
				}
			} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("DECLARACAO")) {
				if (!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getPermitirImpressaoDeclaracao()) {
					throw new Exception("Esse requerimento não está apto a ser impresso");
				}
				getFacadeFactory().getMatriculaFacade().carregarDados(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula(), usuarioVO);
				List<MatriculaPeriodoVO> lista = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatricula(), requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getUnidadeEnsino().getCodigo(), false, new DataModelo(), usuarioVO);
				requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatriculaPeriodoVOs().addAll(lista);
				ImpressaoContratoVO contratoGravar = new ImpressaoContratoVO();
				contratoGravar.setMatriculaVO(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula());
				contratoGravar.setGerarNovoArquivoAssinado(false);
				contratoGravar.setImpressaoContratoExistente(true);
				if (!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo()) && Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getCodigo())) {
					requerimentoTurmaReposicaoRSVO.getRequerimentoVO().setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario()));
				}
				TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				caminhoArquivo = getFacadeFactory().getRequerimentoFacade().realizarImpressaoDeclaracaoRequerimento(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), contratoGravar, contratoGravar, configuracaoGeralSistemaVO,  texto, true, usuarioVO);
			} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("COMPROVANTE")) {
				String tipoLayout = "";
				String professorMinistrouAula = "";
				SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
				tipoLayout = "LAYOUT_3";
				if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getIsPermiteInformarDisciplina() && Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getDisciplina())) {
					professorMinistrouAula = getFacadeFactory().getRequerimentoFacade().executarVerificacaoProfessorMinistrouAula(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getMatricula(), requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getDisciplina().getCodigo(), false, usuarioVO);
				}
				superParametroRelVO.setVersaoSoftware(getVersaoSistema());
				superParametroRelVO.adicionarParametro("isPermitirInformarEnderecoEntrega", requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getTipoRequerimento().getPermitirInformarEnderecoEntrega());
				superParametroRelVO.adicionarParametro("professorMinistrouAula", professorMinistrouAula);
				caminhoArquivo = getFacadeFactory().getRequerimentoFacade().realizarImpressaoComprovanteRequerimento(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), superParametroRelVO, usuarioVO, tipoLayout);
			} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("HISTORICO")) {

			}
			else {
				throw new Exception("O Tipo do Arquivo Não é Válido");
			}
			if (caminhoArquivo.isEmpty()) {
				throw new Exception("O arquivo não foi localizado para download.");
			}
			if(caminhoArquivo.contains(ServidorArquivoOnlineEnum.AMAZON_S3.name())) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
//				ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
				ArquivoVO arquivo = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(Integer.parseInt(caminhoArquivo.replace(ServidorArquivoOnlineEnum.AMAZON_S3.name(), "")), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
//				getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(arquivo, servidorExternoAmazon, config, true);
				return new ObjectMapper().writeValueAsString(configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/relatorio/" + arquivo.getNome());
			}else {
				return new ObjectMapper().writeValueAsString(configuracaoGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/relatorio/" + caminhoArquivo);
			}
			 
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
	public String montarRelatorioPorLayoutTextoPadrao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, RequerimentoVO requerimentoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, Boolean gerarNovoArquivoAssinado, Boolean persistirDocumentoAssinado, UsuarioVO usuarioVO, String caminhoArquivo) throws Exception{
		DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
		if(Uteis.isAtributoPreenchido(requerimentoVO)) {
			if (requerimentoVO.getIsFormatoCertificadoSelecionadoImpresso()) {
				gerarNovoArquivoAssinado = false;	
			} else {
				documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getCodigo(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO.name(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				gerarNovoArquivoAssinado = !Uteis.isAtributoPreenchido(documentoAssinadoVO);
			}
		}else {
			gerarNovoArquivoAssinado = false;
		}
        List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>(0);
        List<File> listaArquivos = new ArrayList<File>();
        CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO= new CertificadoCursoExtensaoRelVO();
		listaMatricula.add(requerimentoVO.getMatricula());
    	certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(listaMatricula.get(0).getUnidadeEnsino());
    	if (Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO().getCodigo())) {
    		getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(requerimentoVO.getMatriculaPeriodoVO(), null, usuarioVO);
    	}
    	certificadoCursoExtensaoRelVO.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(requerimentoVO.getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
    	certificadoCursoExtensaoRelVOs = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().executarConsultaParametrizadaLayout2(listaMatricula, certificadoCursoExtensaoRelVO, "TextoPadrao", false, null, null, false);
		if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
			if(Uteis.isAtributoPreenchido(requerimentoVO.getTipoRequerimento().getTextoPadrao()) && (requerimentoVO.getFormatoCertificadoSelecionado().equals("") || requerimentoVO.getIsFormatoCertificadoSelecionadoDigital())) {
				textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);									
			} else if (Uteis.isAtributoPreenchido(requerimentoVO.getTipoRequerimento().getCertificadoImpresso()) && (requerimentoVO.getFormatoCertificadoSelecionado().equals("") || requerimentoVO.getIsFormatoCertificadoSelecionadoImpresso())) {
				textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCertificadoImpresso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);									
			}
			HashMap<String, Object> hashMap = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().realizarMontagemRelatorioPorTextoPadrao(certificadoCursoExtensaoRelVOs, listaArquivos, textoPadraoDeclaracaoVO, null, new GradeDisciplinaVO(),  requerimentoVO,  getConfiguracaoGeralPadraoSistema(), gerarNovoArquivoAssinado, Uteis.isAtributoPreenchido(requerimentoVO.getCodigo()) ? true : false, usuarioVO);
			if(textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isPdf() && listaArquivos.size() == 1){
				caminhoArquivo = listaArquivos.get(0).getName();
			}
		} 
		return caminhoArquivo;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/carregarValorRequerimento/")
	public Response carregarValorRequerimentoV2(final String paramObject) {
		try {			
			return RequerimentoVO.removerCamposChamadaAPI(carregarValorRequerimento(paramObject));
		} catch (WebServiceException e) {
			throw e;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarValorRequerimento/")
	public RequerimentoVO carregarValorRequerimentoV1(final String paramObject) {
		return carregarValorRequerimento(paramObject);
	}
	
	public RequerimentoVO carregarValorRequerimento(final String paramObject) {
		Gson gson = new GsonBuilder()
				.setDateFormat("MM-dd-yyyy HH:mm:ss")
				.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
				.create();
		RequerimentoVO requerimentoVO = new RequerimentoVO();
		Type type = new TypeToken<RequerimentoVO>() {
		}.getType();
		requerimentoVO = gson.fromJson(paramObject, type);
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			getFacadeFactory().getRequerimentoFacade().atualizarValorRequerimento(requerimentoVO, true, usuarioVO);
			return requerimentoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/gravarRequerimento/")
	public Response gravarRequerimentoV2(final String paramObject) {
		try {			
			return RequerimentoVO.removerCamposChamadaAPI(gravarRequerimento(paramObject));
		} catch (WebServiceException e) {
			throw e;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gravarRequerimento/")
	public RequerimentoVO gravarRequerimentoV1(final String paramObject) {
		return gravarRequerimento(paramObject);
	}
		public RequerimentoVO gravarRequerimento(final String paramObject) {
		Gson gson = new GsonBuilder()
				.setDateFormat("MM-dd-yyyy HH:mm:ss")
				.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
				.create();
		RequerimentoVO requerimentoVO = new RequerimentoVO();
		Map<Integer, List<TurmaVO>> mapTurmas = new HashMap<Integer, List<TurmaVO>>();
		List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>();
		Type type = new TypeToken<RequerimentoVO>() {
		}.getType();
		requerimentoVO = gson.fromJson(paramObject, type);
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			if(!usuarioVO.getTipoUsuario().equals("RL")) {
				usuarioVO.setTipoUsuario("AL");
			}
			RequerimentoHistoricoVO requerimentoHistoricoVO = new RequerimentoHistoricoVO();			
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			obterPerfilAcesso(usuarioVO, requerimentoVO, configuracaoGeralSistemaVO);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo(), null);
			requerimentoVO.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			if (Uteis.isAtributoPreenchido(requerimentoVO.getMatriculaPeriodoVO())) {
				requerimentoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(requerimentoVO.getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,  usuarioVO));
			}
			requerimentoVO.getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(requerimentoVO.getTipoRequerimento().getCodigo(), false, usuarioVO));
			if(requerimentoVO.getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso() && requerimentoVO.getTipoRequerimento().getIsEmissaoCertificado() && requerimentoVO.getIsFormatoCertificadoSelecionadoImpresso()) {
				getFacadeFactory().getRequerimentoFacade().realizarDefinicaoVencimentoContaReceberRequerimento(requerimentoVO, true);
				requerimentoVO.setValor(requerimentoVO.getTipoRequerimento().getValor());
//				getFacadeFactory().getRequerimentoFacade().realizarValidacaoCobrancaRequerimento(requerimentoVO);
				requerimentoVO.setExigePagamento(Boolean.TRUE);
			}
			if (Uteis.isAtributoPreenchido(requerimentoVO.getArquivoVO().getNome()) && !Uteis.isAtributoPreenchido(requerimentoVO.getArquivoVO().getPastaBaseArquivo()) && Uteis.isAtributoPreenchido(requerimentoVO.getArquivoVO().getPastaBaseArquivoEnum()) && requerimentoVO.getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
				if (!requerimentoVO.getArquivoVO().getCpfRequerimento().equals("")) {
    				requerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + requerimentoVO.getArquivoVO().getCpfRequerimento());
				} else {
					requerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
				}
			}
			if (Uteis.isAtributoPreenchido(requerimentoVO.getComprovanteSolicitacaoIsencao().getNome()) && !Uteis.isAtributoPreenchido(requerimentoVO.getComprovanteSolicitacaoIsencao().getPastaBaseArquivo())) {
				if (!requerimentoVO.getComprovanteSolicitacaoIsencao().getCpfRequerimento().equals("")) {
					requerimentoVO.getComprovanteSolicitacaoIsencao().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + requerimentoVO.getComprovanteSolicitacaoIsencao().getCpfRequerimento());
				} else {
					requerimentoVO.getComprovanteSolicitacaoIsencao().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
				}
			}
			for (MaterialRequerimentoVO materialRequerimentoVO : requerimentoVO.getMaterialRequerimentoVOs()) {
				materialRequerimentoVO.setUsuarioDisponibilizouArquivo(usuarioVO);
				if (Uteis.isAtributoPreenchido(materialRequerimentoVO.getArquivoVO().getNome()) && !Uteis.isAtributoPreenchido(materialRequerimentoVO.getArquivoVO().getPastaBaseArquivo())) {
					if (!materialRequerimentoVO.getArquivoVO().getCpfRequerimento().equals("")) {
						materialRequerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + materialRequerimentoVO.getArquivoVO().getCpfRequerimento());
					} else {
						materialRequerimentoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue());
					}
				}
			}
			if (Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getCodigo()) && requerimentoVO.getTipoRequerimento().getIsTipoReposicao()) {
				getFacadeFactory().getTurmaFacade().carregarDados(requerimentoVO.getTurmaReposicao(), usuarioVO);
				if (Uteis.isAtributoPreenchido(requerimentoVO.getCodigo())) {
					requerimentoVO.setNovoObj(false);
				}
				DisciplinaVO disciplina = requerimentoVO.getDisciplinaPorEquivalencia() ? requerimentoVO.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO() : requerimentoVO.getDisciplina();
//				if(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(requerimentoVO.getTurmaReposicao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO).getTurmaAgrupada()) {
//					throw new Exception("Houve um erro na vinculação da turma de reposição, entre em contato com a secretaria acadêmica e informe o código da turma " + requerimentoVO.getTurmaReposicao().getCodigo()+" e disciplina "+disciplina.getCodigo());
//				}				
				if(!Uteis.isAtributoPreenchido(getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(requerimentoVO.getTurmaReposicao().getCodigo(),  disciplina.getCodigo(), false, usuarioVO))){
					disciplina =  getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					if(!requerimentoVO.getTurmaReposicao().getIdentificadorTurmaBase().isEmpty()) {
						throw new Exception("Não foi encontado a oferta para a disciplina "+disciplina.getNome()+" na turma "+requerimentoVO.getTurmaReposicao().getIdentificadorTurmaBase()+".");
					}else {
						throw new Exception("Não foi encontado a oferta para a disciplina "+disciplina.getNome()+" na turma "+requerimentoVO.getTurmaReposicao().getIdentificadorTurma()+".");
					}
				}
				
				if (requerimentoVO.getTurmaReposicao().getTurmaAgrupada()) {
					getFacadeFactory().getRequerimentoFacade().definirTurmaBaseRequerimentosTurmaAgrupada(requerimentoVO, turmaVOs, usuarioVO, mapTurmas, requerimentoVO.getTurmaReposicao());
				}
			}
//			getFacadeFactory().getRequerimentoFacade().persistirRequerimento(requerimentoVO, requerimentoHistoricoVO, Boolean.TRUE, requerimentoVO.getExigePagamento(), configuracaoGeralSistemaVO,  requerimentoVO.getMatricula().getUnidadeEnsino(), usuarioVO);			
			requerimentoVO.setPodeSerIniciadoExecucaoDepartamentoAtual(null);			
			return requerimentoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/editarRequerimento/{codigoRequerimento}/{codigoUnidadeEnsinoMatricula}")
	public Response editarRequerimentoV2(@PathParam("codigoRequerimento") final Integer codigoRequerimento, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula) {		
		try {			
			return RequerimentoVO.removerCamposChamadaAPI(editarRequerimento(codigoRequerimento, codigoUnidadeEnsinoMatricula));
		} catch (WebServiceException e) {
			throw e;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editarRequerimento/{codigoRequerimento}/{codigoUnidadeEnsinoMatricula}")
	public RequerimentoVO editarRequerimentoV1(@PathParam("codigoRequerimento") final Integer codigoRequerimento, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula) {
		return editarRequerimento(codigoRequerimento, codigoUnidadeEnsinoMatricula);
	}
	public RequerimentoVO editarRequerimento(Integer codigoRequerimento, Integer codigoUnidadeEnsinoMatricula) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			String turmaBaseTurmaReposicao = "";
			requerimentoVO.setCodigo(codigoRequerimento);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsinoMatricula, null);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			requerimentoVO = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(codigoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			turmaBaseTurmaReposicao = requerimentoVO.getTurmaReposicao().getIdentificadorTurmaBase();
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(configuracaoGeralSistemaVO, requerimentoVO.getMatricula(), requerimentoVO.getMatricula().getAlunoNaoAssinouContratoMatricula(), usuarioVO ));
			try {
				 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirRequerenteInteragirTramite", usuarioVO)) {
					 requerimentoVO.setPermitirRequerenteInteragirTramite(Boolean.TRUE);
				 }
			} catch (Exception e) {
				requerimentoVO.setPermitirRequerenteInteragirTramite(Boolean.FALSE);
			}
			try {
				 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirRequerenteAnexarArquivo", usuarioVO)) {
					 requerimentoVO.setRequerimentoPermitirRequerenteAnexarArquivo(Boolean.TRUE);
				 }
			} catch (Exception e) {
				requerimentoVO.setRequerimentoPermitirRequerenteAnexarArquivo(Boolean.FALSE);
			}
			if (Uteis.isAtributoPreenchido(requerimentoVO.getTurmaReposicao().getCodigo())) {
				requerimentoVO.setTurmaReposicao(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(requerimentoVO.getTurmaReposicao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
				requerimentoVO.getTurmaReposicao().setIdentificadorTurmaBase(turmaBaseTurmaReposicao);
			}
			if(!Uteis.isAtributoPreenchido(requerimentoVO.getTipoTrabalhoConclusaoCurso()) && requerimentoVO.getTipoRequerimento().getTipo().equals("TC")) {
				requerimentoVO.setTipoTrabalhoConclusaoCurso("AR");
			}
			requerimentoVO.getArquivoVO().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
			for (MaterialRequerimentoVO materialRequerimentoVO : requerimentoVO.getMaterialRequerimentoVOs()) {
				materialRequerimentoVO.getArquivoVO().setPastaBaseArquivoWeb(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
			}
			requerimentoVO.setAptoImpressaoVisaoAluno(getFacadeFactory().getRequerimentoFacade().validarApresentarBotaoImprimirVisaoAluno(requerimentoVO));
			requerimentoVO.setEdicao(true);
			return requerimentoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/excluirRequerimento/{codigoRequerimento}/{codigoUnidadeEnsinoMatricula}")
	public Response excluirRequerimentoV2(@PathParam("codigoRequerimento") final Integer codigoRequerimento, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula) {		
		return excluirRequerimento(codigoRequerimento, codigoUnidadeEnsinoMatricula, "V2");
	}
	
		@DELETE
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/excluirRequerimento/{codigoRequerimento}/{codigoUnidadeEnsinoMatricula}")
		public Response excluirRequerimentoV1(@PathParam("codigoRequerimento") final Integer codigoRequerimento, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula) {
			return excluirRequerimento(codigoRequerimento, codigoUnidadeEnsinoMatricula, "V1");
		}
		public Response excluirRequerimento(Integer codigoRequerimento, Integer codigoUnidadeEnsinoMatricula, String versao) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();			
			usuarioVO.setTipoUsuario("AL");
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			obterPerfilAcesso(usuarioVO, requerimentoVO, configuracaoGeralSistemaVO);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsinoMatricula, null);
			requerimentoVO = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(codigoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			getFacadeFactory().getRequerimentoFacade().excluir(requerimentoVO,  usuarioVO, configuracaoGeralSistemaVO, true);			
			requerimentoVO = novoRequerimentoAluno(requerimentoVO.getPessoa().getCodigo(), requerimentoVO.getMatricula().getMatricula(), requerimentoVO.getUnidadeEnsino().getCodigo(), usuarioVO.getPerfilAcesso().getCodigo(), requerimentoVO.getTipoRequerimento().getCodigo());
			if(versao.equals("V1")) {
				return Response.status(Status.OK).entity(requerimentoVO).build();
			}else {
				return RequerimentoVO.removerCamposChamadaAPI(requerimentoVO);
			}
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
		public void obterPerfilAcesso(UsuarioVO usuarioVO, RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
			try {
				if (usuarioVO.getTipoUsuario().equals("AL")) {
					usuarioVO.setVisaoLogar("aluno");
				} else if (usuarioVO.getTipoUsuario().equals("RL")) {
					usuarioVO.setVisaoLogar("pais");
				} else if (usuarioVO.getTipoUsuario().equals("PR")) {
					if(!Uteis.isAtributoPreenchido(requerimentoVO.getCodigo()) && Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
						usuarioVO.setTipoUsuario("AL");
						usuarioVO.setVisaoLogar("aluno");
					}else {
						usuarioVO.setVisaoLogar("professor");
					}			
				}else {
					if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula()) 
							&& Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getAluno())
							&& requerimentoVO.getMatricula().getAluno().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
						usuarioVO.setTipoUsuario("AL");
						usuarioVO.setVisaoLogar("aluno");
					}else {
						usuarioVO.setTipoUsuario("RL");
						usuarioVO.setVisaoLogar("pais");
					}
				}
				if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())
						&& (!Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getCurso())
								|| !Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getUnidadeEnsino())
								|| !Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getAluno()))) {
					getFacadeFactory().getMatriculaFacade().carregarDados(requerimentoVO.getMatricula(), NivelMontarDados.BASICO, usuarioVO);
				}
				if(Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getCurso()) && !Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getCurso().getNivelEducacional())) {
					getFacadeFactory().getCursoFacade().carregarDados(requerimentoVO.getMatricula().getCurso(), NivelMontarDados.BASICO, usuarioVO);
				}
				if (!Uteis.isAtributoPreenchido(usuarioVO.getMatricula())) {
					usuarioVO.setMatricula(requerimentoVO.getMatricula().getMatricula());
				}
				if (!Uteis.isAtributoPreenchido(usuarioVO.getPerfilAcesso().getCodigo())) {
					PerfilAcessoVO perfilAcessoVO = consultarPerfilAcessoUtilizar(configuracaoGeralSistemaVO, requerimentoVO.getMatricula().getCurso().getNivelEducacional(), usuarioVO);
					usuarioVO.getPerfilAcesso().setCodigo(perfilAcessoVO.getCodigo());
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
		
	
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/gravarInteracaoTramiteRequerimento/")
		public Response gravarInteracaoTramiteRequerimentoV2(final String paramObject) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(gravarInteracaoTramiteRequerimento(paramObject));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}		
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarInteracaoTramiteRequerimento/")
		public RequerimentoVO gravarInteracaoTramiteRequerimentoV1(final String paramObject) {
			return gravarInteracaoTramiteRequerimento(paramObject);
		}
		
		public RequerimentoVO gravarInteracaoTramiteRequerimento(final String paramObject) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				Gson gson = new GsonBuilder()
						.setDateFormat("MM-dd-yyyy HH:mm:ss")
						.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
						.create();
				RequerimentoTurmaReposicaoRSVO requerimentoTurmaReposicaoRSVO = new RequerimentoTurmaReposicaoRSVO();
				Type type = new TypeToken<RequerimentoTurmaReposicaoRSVO>() {
				}.getType();
				requerimentoTurmaReposicaoRSVO = gson.fromJson(paramObject, type);
				InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistorico = new InteracaoRequerimentoHistoricoVO();
//				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMatricula().getUnidadeEnsino().getCodigo(), null);
				requerimentoTurmaReposicaoRSVO.setRequerimentoVO(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				if (!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getPermitirRequerenteInteragirTramite()) {
					throw new Exception("Este requerimento não permite interações.");
				}
				if (!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getInteracaoRequerimentoHistorico().getCodigo())) {
					interacaoRequerimentoHistorico.setRequerimentoHistorico(interacaoRequerimentoHistorico.getRequerimentoHistorico());
					interacaoRequerimentoHistorico.setUsuarioInteracao(usuarioVO);
					interacaoRequerimentoHistorico.setDataInteracao(new Date());
					interacaoRequerimentoHistorico.setInteracao(requerimentoTurmaReposicaoRSVO.getInteracao());
					requerimentoTurmaReposicaoRSVO.getRequerimentoHistoricoVO().getInteracaoRequerimentoHistoricoVOs().add(interacaoRequerimentoHistorico);
				} else {
					interacaoRequerimentoHistorico.setRequerimentoHistorico(interacaoRequerimentoHistorico.getRequerimentoHistorico());
					interacaoRequerimentoHistorico.setUsuarioInteracao(usuarioVO);
					interacaoRequerimentoHistorico.setDataInteracao(new Date());
					interacaoRequerimentoHistorico.setInteracao(requerimentoTurmaReposicaoRSVO.getInteracao());
					interacaoRequerimentoHistorico.setInteracaoRequerimentoHistoricoPai(requerimentoTurmaReposicaoRSVO.getInteracaoRequerimentoHistorico());
				}
				Iterator<RequerimentoHistoricoVO> i = requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoHistoricoVOs().iterator();
				while (i.hasNext()) {
					RequerimentoHistoricoVO requerimentoHistoricoVO = (RequerimentoHistoricoVO) i.next();
					if (requerimentoHistoricoVO.getCodigo().equals(interacaoRequerimentoHistorico.getRequerimentoHistorico().getCodigo())) {
						requerimentoHistoricoVO = interacaoRequerimentoHistorico.getRequerimentoHistorico();
						break;
					}
				}
				requerimentoTurmaReposicaoRSVO.getRequerimentoHistoricoVO().getInteracaoRequerimentoHistoricoVOs().add(interacaoRequerimentoHistorico);
				requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoHistoricoVOs().add(requerimentoTurmaReposicaoRSVO.getRequerimentoHistoricoVO());
	        	getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().persistir(interacaoRequerimentoHistorico, false, usuarioVO);
				if(Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo())) {
					getFacadeFactory().getRequerimentoFacade().alterarDataUltimaAlteracao(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getCodigo());
					getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoTurmaReposicaoRSVO.getRequerimentoVO(), usuarioVO);
				}
				return requerimentoTurmaReposicaoRSVO.getRequerimentoVO();
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		

		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/excluirArquivoAnexoRequerimento/")
		public Response excluirArquivoAnexoRequerimentoV2(final String paramObject) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(excluirArquivoAnexoRequerimento(paramObject));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/excluirArquivoAnexoRequerimento/")
		public RequerimentoVO excluirArquivoAnexoRequerimentoV1(final String paramObject) {
			return excluirArquivoAnexoRequerimento(paramObject);
		}
		
		public RequerimentoVO excluirArquivoAnexoRequerimento(final String paramObject) {
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				Gson gson = new GsonBuilder()
						.setDateFormat("MM-dd-yyyy HH:mm:ss")
						.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
						.create();
				RequerimentoTurmaReposicaoRSVO requerimentoTurmaReposicaoRSVO = new RequerimentoTurmaReposicaoRSVO();
				Type type = new TypeToken<RequerimentoTurmaReposicaoRSVO>() {
				}.getType();
				requerimentoTurmaReposicaoRSVO = gson.fromJson(paramObject, type);
				final MaterialRequerimentoVO materialRequerimentoVO = requerimentoTurmaReposicaoRSVO.getMaterialRequerimento();
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("ARQUIVO_ABERTURA")) {
					if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO().getCodigo().intValue() != 0) {
						getFacadeFactory().getArquivoFacade().excluir(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO(), usuarioVO, configuracaoGeralSistemaVO);
					}else if(!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO().getCodigo()) &&
							!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO().getNome().trim().isEmpty() &&
							requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
						getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()+File.separator+requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getArquivoVO().getPastaBaseArquivo());
						
					}
				} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("ARQUIVO_SOLICITACAO_ISENCAO_TAXA")) {
					if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao().getCodigo().intValue() != 0) {
						getFacadeFactory().getArquivoFacade().excluir(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao(), usuarioVO, configuracaoGeralSistemaVO);
					}else if(!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao().getCodigo()) &&
							!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao().getNome().trim().isEmpty() &&
							requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
						getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()+File.separator+requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getComprovanteSolicitacaoIsencao().getPastaBaseArquivo());
					}
				} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("ARQUIVO_APROVEITAMENTO_DISCIPLINA")) {
					if(Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getCodigo())){
						getFacadeFactory().getArquivoFacade().excluir(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino(), usuarioVO, configuracaoGeralSistemaVO);
					}else if(!Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getCodigo()) &&
							!requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getNome().trim().isEmpty() &&
							requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
						getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()+File.separator+requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().getPastaBaseArquivo());
					}
				} else if (requerimentoTurmaReposicaoRSVO.getTipoArquivo().equals("ARQUIVO_ANEXO")) {
					if (Uteis.isAtributoPreenchido(requerimentoTurmaReposicaoRSVO.getMaterialRequerimento())) {
						if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMaterialRequerimentoVOs().removeIf(mrvo -> mrvo.getCodigo().equals(materialRequerimentoVO.getCodigo()))) {
							getFacadeFactory().getMaterialRequerimentoFacade().excluir(requerimentoTurmaReposicaoRSVO.getMaterialRequerimento(), usuarioVO, configuracaoGeralSistemaVO);
						}
					} else if (requerimentoTurmaReposicaoRSVO.getMaterialRequerimento() != null){
						if (requerimentoTurmaReposicaoRSVO.getRequerimentoVO().getMaterialRequerimentoVOs().removeIf(mrvo -> mrvo.getDescricao().equals(materialRequerimentoVO.getDescricao()) && mrvo.getArquivoVO().getNome().equals(materialRequerimentoVO.getArquivoVO().getNome()))) {
							String caminhoCompletoArquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + requerimentoTurmaReposicaoRSVO.getMaterialRequerimento().getArquivoVO().getPastaBaseArquivo();
							getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(requerimentoTurmaReposicaoRSVO.getMaterialRequerimento().getArquivoVO(), caminhoCompletoArquivo);
						}
					}
				}
				return requerimentoTurmaReposicaoRSVO.getRequerimentoVO();
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/V2/gravarSolicitacaoIsencaoTaxaRequerimento/")
		public Response gravarSolicitacaoIsencaoTaxaRequerimentoV2(final String paramObject) {
			try {
				return RequerimentoVO.removerCamposChamadaAPI(gravarSolicitacaoIsencaoTaxaRequerimento(paramObject));
			} catch (WebServiceException e) {
				throw e;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}		
			
		}
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/gravarSolicitacaoIsencaoTaxaRequerimento/")
		public RequerimentoVO gravarSolicitacaoIsencaoTaxaRequerimentoV1(final String paramObject) {
			return gravarSolicitacaoIsencaoTaxaRequerimento(paramObject);
		}
		
		public RequerimentoVO gravarSolicitacaoIsencaoTaxaRequerimento(final String paramObject) {
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			String justificativaIsencao;
			Type type = new TypeToken<RequerimentoVO>() {
			}.getType();
			requerimentoVO = gson.fromJson(paramObject, type);
			try {
				UsuarioVO usuarioVO = autenticarConexao();
				justificativaIsencao = requerimentoVO.getJustificativaSolicitacaoIsencao();
//				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, requerimentoVO.getMatricula().getUnidadeEnsino().getCodigo(), null);
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				requerimentoVO.setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//				requerimentoVO = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(requerimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, configuracaoFinanceiroVO);
				if (requerimentoVO.getPermiteSolicitarIsencaoTaxa() && (requerimentoVO.getCodigo() > 0) && !requerimentoVO.getSituacao().equals("FD")) {
					requerimentoVO.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA);
					requerimentoVO.setJustificativaSolicitacaoIsencao(justificativaIsencao);
					getFacadeFactory().getRequerimentoFacade().incluirSolicitacaoIsencaoTaxa(requerimentoVO, configuracaoGeralSistemaVO, usuarioVO);
				} else {
					throw new Exception("Este requerimento não está apto para solicitação de isenção de taxa.");
				}
				return requerimentoVO;
			} catch (Exception e) {
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(e.getMessage());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
		}
		 
		
		
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarProcessoSeletivo/{codigoUnidadeEnsino}/{codigoCurso}/{codigoTurno}/{tipoProcessoSeletivo}")
	public List<ProcSeletivoVO> consultarProcessoSeletivo(@PathParam("codigoUnidadeEnsino") final Integer codigoUnidadeEnsino, @PathParam("codigoCurso") final Integer codigoCurso,
			@PathParam("codigoTurno") final Integer codigoTurno,@PathParam("tipoProcessoSeletivo") final String  tipoProcessoSeletivo) {
		
		try {
			autenticarTokenCliente();
			List<ProcSeletivoVO> procSeletivoVOs = new ArrayList<ProcSeletivoVO>(0);
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			if (codigoUnidadeEnsino != 0) {				  
				procSeletivoVOs =  getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsinoAptoInscricao(codigoUnidadeEnsino, true, false,codigoCurso, codigoTurno,tipoProcessoSeletivo,Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioVO);
				for(ProcSeletivoVO procSeletivo :procSeletivoVOs) {	
					procSeletivo.montarListaTipoProcSeletivo();
				}
			}
			return  procSeletivoVOs ;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	

	
	
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarUnidadeEnsinoProcessoSeletivo/{codigoProcessoSeletivo}")
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoProcessoSeletivo(@PathParam("codigoProcessoSeletivo") final Integer codigoProcessoSeletivo) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			List<UnidadeEnsinoVO> lista = new ArrayList<UnidadeEnsinoVO>(0);
			//lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProcessoSeletivo(codigoProcessoSeletivo, false ,Uteis.NIVELMONTARDADOS_COMBOBOX,usuarioVO );
		    lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoOndeCursoDiferenteDePosGraduacao(Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		  return lista ;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	

	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarCandidatoProcessoSeletivo/{tipoDocumento}/{numeroDocumento}")
	public PessoaVO consultarCandidatoProcessoSeletivo(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			PessoaVO candidato = new  PessoaVO();
			if(!Uteis.isAtributoPreenchido(tipoDocumento) ||  !Uteis.isAtributoPreenchido(numeroDocumento)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_verificarCamposConsultaInscricao"));
			}
			if(tipoDocumento.equals("CPF")) {			
				if (!Uteis.verificaCPF(numeroDocumento)) {					
					throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
				}
				
				candidato = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(numeroDocumento ,0 ,"", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO,usuarioVO);
			}else if(tipoDocumento.equals("CERTIDAO_NASCIMENTO")) {
				candidato = getFacadeFactory().getPessoaFacade().consultarPorCertidaoNascimentoUnico(numeroDocumento  ,"" , false, Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO,usuarioVO );

			}
			  
			return candidato;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarQuestionarioInscricao/{codigoQuestionario}/{codigoInscricao}")
	public QuestionarioVO consultarQuestionarioInscricao(@PathParam("codigoQuestionario") final Integer codigoQuestionario , @PathParam("codigoInscricao") final Integer codigoInscricao) {
		
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			QuestionarioVO questionarioVO = new QuestionarioVO();
			
			questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarQuestionarioInscricao(codigoQuestionario, codigoInscricao, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorInscricao(codigoInscricao, questionarioVO);

			return questionarioVO ;
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gravarCandidato")
	public PessoaVO gravarCandidato(PessoaVO candidatoVO) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			usuarioVO.setVisaoLogar("candidato");			
			candidatoVO.setCandidato(Boolean.TRUE);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");			
			for(FiliacaoVO filiacao : candidatoVO.getFiliacaoVOs()) {
				if(Uteis.isAtributoPreenchido(filiacao.getPais().getCPF()) ) {					
					PessoaVO pessoaFiliacao = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(filiacao.getCPF() ,0 ,"", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioVO);
					if(Uteis.isAtributoPreenchido(pessoaFiliacao)) {
						filiacao.setPais(pessoaFiliacao); 
						candidatoVO.adicionarObjFiliacaoVOs(filiacao);
					}
				}else {
					 candidatoVO.adicionarObjFiliacaoVOs(filiacao);
				}				
				
			}	
			candidatoVO.inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "CANDIDATO");
				if (candidatoVO.getCodigo().equals(0)) {					
					getFacadeFactory().getPessoaFacade().incluir(candidatoVO, false, usuarioVO, getConfiguracaoGeralPadraoSistema(), false, false, true, false, false);
				}else {
					candidatoVO.setCEP(Uteis.adicionarMascaraCEPConformeTamanhoCampo(candidatoVO.getCEP()));
				    candidatoVO.getArquivoImagem().setCodigo(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(candidatoVO.getCodigo(),false,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO).getArquivoImagem().getCodigo());
				    getFacadeFactory().getPessoaFacade().alterar(candidatoVO, false, usuarioVO, getConfiguracaoGeralPadraoSistema(), false, false, true, false, false);
			
			   }	
			   
			   return candidatoVO;
			} catch (Exception e) {			
				setMensagemDetalhada("msg_erro", e.getMessage());				
				System.out.println("MOBILE ERROR: " + getMensagemDetalhada());
				ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
				errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
				errorInfoRSVO.setMensagem(getMensagemDetalhada());
				throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
			}
	}
	

		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarProvaAgendadaCandidatoProcessoSeletivo/{tipoDocumento}/{numeroDocumento}/{tipoRequisicao}")
	public InscricaoVO consultarProvaAgendadaCandidatoProcessoSeletivo(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento,@PathParam("tipoRequisicao") final String tipoRequisicao) {
		InscricaoVO inscricaoVO = new InscricaoVO();		
		try {			
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoaProvaOnlinePorNumeroDocumento(tipoDocumento, numeroDocumento, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			
			getFacadeFactory().getInscricaoFacade().validarInscricaoAptoApresentarProvaProcessoSeletivoResultadoProcessoSeletivo(inscricaoVO,tipoRequisicao);		
			if (Uteis.isAtributoPreenchido(inscricaoVO.getDataAutenticacao())) {
				inscricaoVO.setDataAutenticacao(null);
				inscricaoVO.setCodigoAutenticacao(null);
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
				getFacadeFactory().getInscricaoFacade().enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, null, "", configuracaoGeralSistemaVO,false , false, true, usuarioVO);
			}			
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			if(Uteis.isAtributoPreenchido(inscricaoVO.getMensagemErro())) {
				errorInfoRSVO.setCampo(inscricaoVO.getMensagemErro());	
			}
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo/{codigoInscricao}/{meioAutenticacao}")
	public InscricaoVO enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("meioAutenticacao") final String meioAutenticacao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (inscricaoVO.getSituacao().equals("PF")) {
				throw new Exception("A Inscrição Não Está Confirmada Para Realização da Prova Pois Possui Pendência Financeira.");
			}
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			Random rnd = new Random();
			Integer codigoAutenticacao = 100000 + rnd.nextInt(900000);
			getFacadeFactory().getInscricaoFacade().enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, codigoAutenticacao, meioAutenticacao, configuracaoGeralSistemaVO,false, false, false, usuarioVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo/{codigoInscricao}/{codigoAutenticacao}")
	public InscricaoVO validarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("codigoAutenticacao") final Integer codigoAutenticacao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
			getFacadeFactory().getInscricaoFacade().validarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, codigoAutenticacao, usuarioVO);
			getFacadeFactory().getInscricaoFacade().alterarDataAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, new Date(), usuarioVO);
			inscricaoVO.setDataAutenticacao(new Date());
//			inscricaoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(inscricaoVO, usuarioVO);
			getFacadeFactory().getInscricaoFacade().validarApresentacaoResultadoProcessoSeletivo(inscricaoVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gravarRespostaPergunta")
	public InscricaoVO gravarRespostaPergunta(final String paramObject) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			ProcessoSeletivoRSVO processoSeletivoRSVO = new ProcessoSeletivoRSVO();
			Gson gson = new GsonBuilder()
						.setDateFormat("MM-dd-yyyy HH:mm:ss")
						.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
						.create();
			Type type = new TypeToken<ProcessoSeletivoRSVO>() {
			}.getType();
			processoSeletivoRSVO = gson.fromJson(paramObject, type);			
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(processoSeletivoRSVO.getInscricaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			
			inscricaoVO.setNavegadorAcesso(processoSeletivoRSVO.getInscricaoVO().getNavegadorAcesso());
//			processoSeletivoRSVO.getInscricaoVO().getResultadoProcessoSeletivoVO().setNavegadorAcesso(processoSeletivoRSVO.getInscricaoVO().getNavegadorAcesso());
			getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorProvaOnline(inscricaoVO, processoSeletivoRSVO.getInscricaoVO().getCodigoAutenticacaoNavegador() ,processoSeletivoRSVO.getCodigoAutenticacaoProvaNavegador() ,true ,usuarioVO);
			if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraTermino()) ) {
				throw new Exception("A sua prova já está encerrada");
			}
			processoSeletivoRSVO.getInscricaoVO().setCodigoAutenticacaoNavegador(inscricaoVO.getCodigoAutenticacaoNavegador());
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(processoSeletivoRSVO.getInscricaoVO());
//			if (processoSeletivoRSVO.getTipoPergunta().equals("QUESTAO")) {
//				getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().alterarResultadoProcessoSeletivoProvaRespostaVOs(processoSeletivoRSVO.getInscricaoVO().getResultadoProcessoSeletivoVO(), processoSeletivoRSVO.getQuestaoProvaProcessoSeletivo(), usuarioVO);
//			} else if (processoSeletivoRSVO.getTipoPergunta().equals("REDACAO")) {
//				getFacadeFactory().getResultadoProcessoSeletivoFacade().atualizarRedacaoProvaProcessoSeletivo(processoSeletivoRSVO.getInscricaoVO().getResultadoProcessoSeletivoVO(),usuarioVO);
//			}
			
			return processoSeletivoRSVO.getInscricaoVO();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/finalizarProvaCandidato")
	public InscricaoVO finalizarProvaCandidato(final String paramObject) {
		try {
			autenticarTokenCliente();
		    InscricaoVO inscricaoVO = new InscricaoVO();
			Gson gson = new GsonBuilder()
					.setDateFormat("MM-dd-yyyy HH:mm:ss")
					.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
					.create();
			Type type = new TypeToken<InscricaoVO>() {
			}.getType();
			inscricaoVO = gson.fromJson(paramObject, type);
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVOValidar = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(inscricaoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			inscricaoVOValidar.setNavegadorAcesso(inscricaoVO.getNavegadorAcesso());
			getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorProvaOnline(inscricaoVOValidar, inscricaoVO.getCodigoAutenticacaoNavegador(),inscricaoVO.getCodigoAutenticacaoNavegador(),true ,usuarioVO);
			if (Uteis.isAtributoPreenchido(inscricaoVOValidar.getDataHoraTermino()) ) {
				throw new Exception("A sua prova já está encerrada");
			}
			/*if(UteisData.getDataFuturaAvancandoMinuto(inscricaoVOValidar.getItemProcessoSeletivoDataProva().getDataProva(), inscricaoVOValidar.getProcSeletivo().getTempoRealizacaoProvaOnline()+  5).before(new Date())) {
				throw new Exception("Prazo de finalização de prova esgotada");
				
			}*/
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
//			List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>();
//			inscricaoVO.getResultadoProcessoSeletivoVO().setInscricao(inscricaoVO);
//			inscricaoVO.getResultadoProcessoSeletivoVO().setNavegadorAcesso(inscricaoVO.getNavegadorAcesso());
//			resultadoProcessoSeletivoVOs.add(inscricaoVO.getResultadoProcessoSeletivoVO());
//			getFacadeFactory().getResultadoProcessoSeletivoFacade().gravarLancamentoNotaPorDisciplina(resultadoProcessoSeletivoVOs, usuarioVO);
			inscricaoVO.setDataHoraTermino(Uteis.getDataJDBCTimestamp(new Date()));			
			getFacadeFactory().getInscricaoFacade().alterarDataHoraTerminoProvaProcessoSeletivo(inscricaoVO,usuarioVO);
//			inscricaoVO.setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorChavePrimaria(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			getFacadeFactory().getInscricaoFacade().validarApresentacaoResultadoProcessoSeletivo(inscricaoVO);
			//inscricaoVO.setMensagemErro("A sua prova foi finalizada com sucesso. A liberação do resultado está prevista "+inscricaoVO.getItemProcessoSeletivoDataProva().getDataLiberacaoResultado_Apresentar() +".");
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/apresentarResultadoProvaCandidato/{codigoInscricao}")
	public InscricaoVO apresentarResultadoProvaCandidato(@PathParam("codigoInscricao") final Integer codigoInscricao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
		    InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//			inscricaoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(inscricaoVO, usuarioVO);
			getFacadeFactory().getInscricaoFacade().validarApresentacaoResultadoProcessoSeletivo(inscricaoVO);
//			inscricaoVO.setClassificacao(getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().verificarClassificacaoCandidado(inscricaoVO));
			ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralPadraoSistema();
			inscricaoVO.setOcultarMedia(config.getOcultarMediaProcSeletivo());
		    inscricaoVO.setOcultarClassificacao(config.getOcultarClassificacaoProcSeletivo());
			inscricaoVO.setOcultarChamadaCandidato(config.getOcultarChamadaCandidatoProcSeletivo());
//			if (!inscricaoVO.getOcultarChamadaCandidato()) {
//				if (inscricaoVO.getChamada() != null && inscricaoVO.getChamada().intValue() > 0) {
//					inscricaoVO.getResultadoProcessoSeletivoVO().setDescricaoClassificacaoCand("Convocado em " + inscricaoVO.getChamada() + "º chamada");
//				} else {
//					inscricaoVO.getResultadoProcessoSeletivoVO().setDescricaoClassificacaoCand("Não Convocado (Aguardar Convocação)");
//				}
//			} else {
//				inscricaoVO.getResultadoProcessoSeletivoVO().setDescricaoClassificacaoCand("");
//			}	
			if (!inscricaoVO.getApresentarResultadoProcessoSeletivo()) {
				inscricaoVO.setMensagemErro("Gabarito não pode ser apresentado procure a instituição de ensino para informações da mesma.");
				//throw new Exception("Gabarito não pode ser apresentado procure a instituição de ensino para informações da mesma.");
				return inscricaoVO;
			}
		    getFacadeFactory().getInscricaoFacade().validarDadosProvaProcessoSeletivo(inscricaoVO, inscricaoVO.getApresentarResultadoProcessoSeletivo());
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	

	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/estadoCivil/")
	public List<SelectComboRSVO> consultarSelectItemEstadoCivilPessoa() throws Exception {
		try {
			autenticarTokenCliente();
			List<SelectComboRSVO> objs = new ArrayList<SelectComboRSVO>(0);		
			
			Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
			Enumeration keys = estadoCivils.keys();
			while (keys.hasMoreElements()) {
				SelectComboRSVO selectItemEstadocivil  = new SelectComboRSVO();
				String value = (String) keys.nextElement();
				String label = (String) estadoCivils.get(value);
				selectItemEstadocivil.setChave(value);
				selectItemEstadocivil.setValor(label);			
				objs.add(selectItemEstadocivil);
			}
			return objs;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/nacionalidades/")
	public List<PaizVO> listaNacionalidade() throws Exception {
		try {
			 autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			List<PaizVO> lista = new ArrayList<PaizVO>(0);
			
			lista = getFacadeFactory().getPaizFacade().consultarPorNome("", false, usuarioVO);
			return lista ;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/aceitarTermosAceiteProvaProcessoSeletivo/{codigoInscricao}")
	public InscricaoVO aceitarTermosAceiteProvaProcessoSeletivo(@PathParam("codigoInscricao") final Integer codigoInscricao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
			inscricaoVO.setTermoFoiAceito(true);
			getFacadeFactory().getInscricaoFacade().aceitarTermosAceiteProvaProcessoSeletivo(inscricaoVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarUltimaInscricaoCandidatoProcessoSeletivo/{tipoDocumento}/{numeroDocumento}/{tipoRequisicao}")
	public InscricaoVO consultarUltimaInscricaoCandidatoProcessoSeletivo(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento, @PathParam("tipoRequisicao") final String tipoRequisicao) {
		try {
			autenticarTokenCliente();
			InscricaoVO inscricaoVO =  new InscricaoVO();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();			
			if(tipoRequisicao.equals("CONFIGURACAOCANDIDATO")) {
				inscricaoVO.setConfiguracaoCandidatoProcessoSeletivoVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO.getUnidadeEnsinoLogado().getCodigo()).getConfiguracaoCandidatoProcessoSeletivoVO());
                return inscricaoVO ;
			}
			
			Integer codigoInscricao = 0;
			PessoaVO pessoa = new PessoaVO();           
        	if(tipoDocumento.equals("CODIGO_INSCRICAO")) {
				 codigoInscricao = new Integer(numeroDocumento);
			}else  if(tipoDocumento.equals("CPF")) {    				
				if (!Uteis.verificaCPF(numeroDocumento)) {					
					throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
				}    					
			   pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(numeroDocumento ,0 ,"", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO,usuarioVO);
			   if(!Uteis.isAtributoPreenchido(pessoa)) {
				   pessoa.setCPF(numeroDocumento);
				   inscricaoVO.setCandidato(pessoa);
				   return inscricaoVO ;
			   }
			}          	
        	inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoa(pessoa.getCodigo(), codigoInscricao, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
        	if(tipoDocumento.equals("CPF")) {            		 
        		 inscricaoVO.setCandidato(pessoa);            		
         	}            	 
        	if(tipoDocumento.equals("CODIGO_INSCRICAO") &&  inscricaoVO.getCodigo().equals(0)) {            		 
        		 throw new Exception("Inscrição Não Localizada.");            		
         	}
        
        	if(tipoDocumento.equals("CODIGO_INSCRICAO") && Uteis.isAtributoPreenchido(inscricaoVO.getCandidato().getCodigo())) {
        		 inscricaoVO.setCandidato(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(inscricaoVO.getCandidato().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO, usuarioVO));
            	 pessoa = (PessoaVO) inscricaoVO.getCandidato().clone();
        	} 
        	
           	if(Uteis.isAtributoPreenchido(inscricaoVO)) {
           		/*if(codigoInscricao > 0  && tipoRequisicao.equals("INSCRICAO")) {
           			InscricaoVO  inscricaoVOExistente = getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoa(inscricaoVO.getCandidato().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	            	if(!inscricaoVOExistente.getCodigo().equals(codigoInscricao) && inscricaoVOExistente.getProcSeletivo().getCodigo().equals(inscricaoVO.getProcSeletivo().getCodigo())) {
	            		throw new Exception("Existe uma inscrição mais atual de código "+inscricaoVOExistente.getCodigo()+" para o candidato "+inscricaoVO.getCandidato().getNome()+" no processo seletivo "+inscricaoVO.getProcSeletivo().getDescricao()+", procure a instituição de ensino para informações da mesma.");
	            	}					
           		}*/
           		if(!tipoRequisicao.equals("INSCRICAO")) {
                	getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
                	if(!inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
                		throw new Exception("Esta inscrição não está mais ativa, entre com o código de uma inscrição ativa");
                	}
                }
//                if (tipoRequisicao.equals("INSCRICAO") &&  Uteis.isAtributoPreenchido(inscricaoVO.getCodigo()) &&
//                	(!inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO) ||                	
//                	(Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo()) && Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao())) ||
//                	(Uteis.isAtributoPreenchido(inscricaoVO.getItemProcessoSeletivoDataProva().getDataLimiteApresentarDadosVisaoCandidato()) && inscricaoVO.getItemProcessoSeletivoDataProva().getDataLimiteApresentarDadosVisaoCandidato().compareTo(UteisData.getDataSemHora(new Date())) < 0))) {
//                  
//                	if(tipoDocumento.equals("CPF")) {
//                	InscricaoVO inscricaoExistente =  getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoAtivaDentroPrazoDiferenteAtualSemResultadoPorPessoa(inscricaoVO.getCandidato().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO) ;
//                      if(Uteis.isAtributoPreenchido(inscricaoExistente) ) {
//                    	  inscricaoVO = inscricaoExistente;
//                    	  inscricaoExistente.setCandidato(pessoa);
//                    	  inscricaoVO.getProcSeletivo().montarListaTipoProcSeletivo();
//                    	  return  inscricaoExistente ; 
//                      }                      
//                	}
//                	inscricaoVO = new InscricaoVO();
//                	
//                	if(tipoDocumento.equals("CODIGO_INSCRICAO")) {            		
//                		inscricaoVO.setMensagemErro("Esta inscrição não está mais ativa, realize uma nova inscrição ou entre com o código de uma inscrição ativa");
//                	}
//                	inscricaoVO.setCandidato(pessoa);
//                	return inscricaoVO;
//                }
			 }       
            
            inscricaoVO.getProcSeletivo().montarListaTipoProcSeletivo();
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}

	
	
	
	
	
	
	
	private void validarTokenIntegracao(final HttpServletRequest request) throws Exception {
		
		String token = buscarTokenWebService();
		
		//Erro interno no servidor - Token Integracao Moodle nao informado
		if(token.isEmpty())
			throw new Exception("Token Integração não configurado!");
		
		//Valida se o token foi passado no Header da consulta
		if(request.getHeader("Authorization") == null || !token.equals(request.getHeader("Authorization"))) {
			throw new Exception("Token Integração inválido!");
		}
	}

	private String buscarTokenWebService() throws Exception {
		ConfiguracaoGeralSistemaVO config = null;
		config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		return config.getTokenWebService();
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/validarExistenciaRequerimentoEmAberto/")
	public String validarExistenciaRequerimentoEmAberto(final String paramObject) {
		Gson gson = new GsonBuilder()
				.setDateFormat("MM-dd-yyyy HH:mm:ss")
				.registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>())
				.create();
		RequerimentoVO requerimentoVO = new RequerimentoVO();
		Type type = new TypeToken<RequerimentoVO>() {
		}.getType();
		requerimentoVO = gson.fromJson(paramObject, type);
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			List<RequerimentoVO> listaRequerimento = getFacadeFactory().getRequerimentoFacade().consultarPorTipoRequerimentoAberto(requerimentoVO, usuarioVO);
			if(Uteis.isAtributoPreenchido(listaRequerimento)){
				return new ObjectMapper().writeValueAsString("true");
			}
			return new ObjectMapper().writeValueAsString("false");
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarQuantidadeFinanceiroAluno/{matricula}/{codigoCurso}/{codigoUnidadeEnsinoMatricula}/{tipoNivelEducacional}/{codigoPerfilAcesso}/{visaoLogar}/{situacao}")
	public Response carregarQuantidadeFinanceiroAluno(@PathParam("matricula") final String matricula, @PathParam("codigoCurso") final Integer codigoCurso, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("tipoNivelEducacional") final String tipoNivelEducacional, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("visaoLogar") final String visaoLogar, @PathParam("situacao") final String situacao) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar(visaoLogar);
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsinoMatricula, null);
//			if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira() && configuracaoFinanceiroVO.getUtilizarIntegracaoFinanceira()) {
//				throw new Exception("Prezado, a emissão dos boletos estão indisponíveis temporariamente, tente mais tarde.");
//			}
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, codigoUnidadeEnsinoMatricula);
			QuantidadeObjetoExistentesRSVO quantidadeExistentes = new QuantidadeObjetoExistentesRSVO();			
//			quantidadeExistentes.setValor(getFacadeFactory().getContaReceberFacade().consultarQuantidadeContasAPagarVisaoAluno(configuracaoGeralSistemaVO,  codigoCurso, matricula, situacao, usuarioVO, new HashMap<Integer, ConfiguracaoRecebimentoCartaoOnlineVO>(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, true, null, null));
			return Response.status(Status.OK).entity(quantidadeExistentes).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
		
	
	
	
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/trocarMatriculaAluno/{matricula}/{visaoLogar}")
	public Response trocarMatriculaAluno(@PathParam("matricula") final String matricula, @PathParam("visaoLogar") final String visaoLogar) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.setVisaoLogar(visaoLogar);
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, null, NivelMontarDados.BASICO, usuarioVO);
			montarDadosMatriculaAcessoAluno(usuarioVO, matriculaVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, usuarioVO.getCodigoUnidadeEnsinoMatriculaLogado());
			realizarDefinicoesUsuarioLogado(usuarioVO, matriculaVO, configuracaoGeralSistemaVO);
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(usuarioVO);
			return Response.status(Status.OK).entity(json).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validarSessaoNavegadorProvaOnline/{codigoInscricao}/{codigoautenticacaoNavegador}/{gravarSessao}")
	public InscricaoVO validarSessaoNavegadorProvaOnline(@PathParam("codigoInscricao") final Integer codigoInscricao ,  @PathParam("codigoautenticacaoNavegador") final String codigoautenticacaoNavegador ,  @PathParam("gravarSessao") final Boolean gravarSessao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);			
				inscricaoVO.setCodigoAutenticacaoNavegador(codigoautenticacaoNavegador);
				getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorProvaOnline(inscricaoVO ,codigoautenticacaoNavegador,codigoautenticacaoNavegador, gravarSessao , usuarioVO);		
			return inscricaoVO ;
		
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido/{matricula}")
	public ArquivoVO inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(@PathParam("matricula") final String matricula) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			final String marcadorPrioridade = "";
			List<String> listaAnoSemestre = getFacadeFactory().getHistoricoFacade().consultarAnoSemestreHistoricoPorMatricula(matricula, marcadorPrioridade, usuarioVO);
			ArquivoVO obj = new ArquivoVO();
			obj.setListaAnoSemestreMobile(listaAnoSemestre);
			return obj;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarProvasAgendadaCandidatoProcessoSeletivo/{tipoDocumento}/{numeroDocumento}/{tipoRequisicao}")
	public List<InscricaoVO> consultarProvasAgendadaCandidatoProcessoSeletivo(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento,@PathParam("tipoRequisicao") final String tipoRequisicao) {
		List<InscricaoVO> inscricaoVO = new ArrayList<InscricaoVO>();		
		try {			
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			inscricaoVO.addAll(getFacadeFactory().getInscricaoFacade().consultarInscricaoPessoaProvaOnlinePorNumeroDocumento(tipoDocumento, numeroDocumento, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			if (!Uteis.isAtributoPreenchido(inscricaoVO)) {
        		throw new Exception("Não Foi Encontrado Nenhuma Inscrição Com " + (tipoDocumento.equals("CODIGO_INSCRICAO") ? "Codigo de Inscrição ": "CPF ") +  numeroDocumento);
			}
			inscricaoVO.stream().forEach(inscricao -> {
				try {
					getFacadeFactory().getInscricaoFacade().validarInscricaoAptoApresentarProvaProcessoSeletivoResultadoProcessoSeletivo(inscricao,tipoRequisicao);
				if (Uteis.isAtributoPreenchido(inscricao.getDataAutenticacao())) {
					inscricao.setDataAutenticacao(null);
					inscricao.setCodigoAutenticacao(null);
					ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
					getFacadeFactory().getInscricaoFacade().enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricao, null, "", configuracaoGeralSistemaVO,false , false, true, usuarioVO);
				}			
				} catch (Exception e) {
					e.printStackTrace();
				}		
			});
			inscricaoVO.removeIf(inscricao -> removerItemLista(inscricao , tipoRequisicao));
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();			
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarInscricaoAtivaCandidatoProcessoSeletivo/{tipoDocumento}/{numeroDocumento}/{tipoRequisicao}")
	public List<InscricaoVO> consultarInscricaoAtivaCandidatoProcessoSeletivo(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento, @PathParam("tipoRequisicao") final String tipoRequisicao) {
		try {
			autenticarTokenCliente();
			InscricaoVO inscricaoVO =  new InscricaoVO();
			List<InscricaoVO> listInscricaoVOs =  new ArrayList<InscricaoVO>();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();			
			if(tipoRequisicao.equals("CONFIGURACAOCANDIDATO")) {
				inscricaoVO.setConfiguracaoCandidatoProcessoSeletivoVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO.getUnidadeEnsinoLogado().getCodigo()).getConfiguracaoCandidatoProcessoSeletivoVO());
				listInscricaoVOs.add(inscricaoVO);
				return listInscricaoVOs ;
			}
			
        	if(tipoDocumento.equals("CODIGO_INSCRICAO")) {
        		listInscricaoVOs.add(tipoDocumentoCodigoInscricao(new Integer(numeroDocumento), tipoRequisicao , usuarioVO));
			}else  if(tipoDocumento.equals("CPF")) {    				
				listInscricaoVOs.addAll(tipoDocumentoCPF(numeroDocumento, tipoRequisicao, usuarioVO));
			}
        	if (!Uteis.isAtributoPreenchido(listInscricaoVOs)) {
        		throw new Exception("Não Foi Encontrado Nenhuma Inscrição Com " + (tipoDocumento.equals("CODIGO_INSCRICAO") ? "Codigo de Inscrição ": "CPF ") +  numeroDocumento);
			}
			return listInscricaoVOs;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}

	private List<InscricaoVO> tipoDocumentoCPF(String numeroDocumento, String tipoRequisicao, UsuarioVO usuarioVO)
			throws Exception {
		List<InscricaoVO> listInscricaoVOs = new ArrayList<InscricaoVO>();
		PessoaVO pessoaCandidato = new PessoaVO();
		if (!Uteis.verificaCPF(numeroDocumento)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
		}
		pessoaCandidato = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(numeroDocumento, 0, "", false,Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO, usuarioVO);
		if (!Uteis.isAtributoPreenchido(pessoaCandidato)) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			pessoaCandidato.setCPF(numeroDocumento);
			inscricaoVO.setCandidato(pessoaCandidato);
			inscricaoVO.getProcSeletivo().montarListaTipoProcSeletivo();
			listInscricaoVOs.add(inscricaoVO);
			return listInscricaoVOs;
		}
		listInscricaoVOs = getFacadeFactory().getInscricaoFacade().consultarInscricoesPessoa(pessoaCandidato.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		listInscricaoVOs.stream().filter(inscricao -> removerItemLista(inscricao , tipoRequisicao)).forEach(InscricaoVO::naoEhPossivelAlterarInscricao);
		if (listInscricaoVOs.isEmpty()) {
			InscricaoVO inscricaoVO = new InscricaoVO();
			inscricaoVO.setCandidato(pessoaCandidato);
			listInscricaoVOs.add(inscricaoVO);
			return listInscricaoVOs;
		}
		return listInscricaoVOs;
	}

	private Boolean removerItemLista(InscricaoVO inscricao , String tipoRequisicao) {
		try {
			if (tipoRequisicao.equals("INSCRICAO")) {
				getFacadeFactory().getInscricaoFacade().validarPrazoAlteracaoProcessoSeletivo(inscricao);
			}else {
     			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricao);
			}
			return !inscricao.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO);
		} catch (Exception e) {
			return Boolean.TRUE;
		}
	}

	private InscricaoVO tipoDocumentoCodigoInscricao(Integer codigoInscricao, String tipoRequisicao,UsuarioVO usuarioVO) throws Exception {

		InscricaoVO inscricaoVO = new InscricaoVO();
		PessoaVO pessoaCandidato = new PessoaVO();
		inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoa(0, codigoInscricao, false,Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if (!Uteis.isAtributoPreenchido(inscricaoVO)) {
			throw new Exception("Inscrição Não Localizada.");
		}
		if (Uteis.isAtributoPreenchido(inscricaoVO.getCandidato().getCodigo())) {
			inscricaoVO.setCandidato(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(inscricaoVO.getCandidato().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO,usuarioVO));
			pessoaCandidato = (PessoaVO) inscricaoVO.getCandidato().clone();
		}
		if (!tipoRequisicao.equals("INSCRICAO")) {
			getFacadeFactory().getInscricaoFacade().validarPrazoRealizacaoProvaProcessoSeletivo(inscricaoVO);
			if (!inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO)) {
				throw new Exception("Esta inscrição não está mais ativa, entre com o código de uma inscrição ativa");
			}
		}
//		 if (tipoRequisicao.equals("INSCRICAO") &&  Uteis.isAtributoPreenchido(inscricaoVO.getCodigo()) &&
//             	(!inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO) ||                	
//             	(Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo()) && Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao())) ||
//             	(Uteis.isAtributoPreenchido(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()) && inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva().compareTo(UteisData.getDataSemHora(new Date())) < 0))) {
//             	
//             	inscricaoVO = new InscricaoVO();
//             	inscricaoVO.setMensagemErro("Esta inscrição não está mais ativa, realize uma nova inscrição ou entre com o código de uma inscrição ativa");
//             	inscricaoVO.setCandidato(pessoaCandidato);
//             	return inscricaoVO;
//             }
		inscricaoVO.getProcSeletivo().montarListaTipoProcSeletivo();
		return inscricaoVO;
	}
	

    @GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/enviarCodigoAutenticacaoAcessoInscricao/{codigoInscricao}/{meioAutenticacao}")
	public InscricaoVO enviarCodigoAutenticacaoAcessoInscricao(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("meioAutenticacao") final String meioAutenticacao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			Random rnd = new Random();
			Integer codigoAutenticacao = 100000 + rnd.nextInt(900000);
			Boolean codigoAutenticacaoExpirada = true;
			if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraVencimentoCodigoAutenticacao())) {
				codigoAutenticacaoExpirada = UteisData.validarDataInicialMaiorFinalComHora(new Date() ,inscricaoVO.getDataHoraVencimentoCodigoAutenticacao());
			}
			getFacadeFactory().getInscricaoFacade().enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, codigoAutenticacao, meioAutenticacao, configuracaoGeralSistemaVO,false, codigoAutenticacaoExpirada, false, usuarioVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validarCodigoAutenticacaoAcessoInscricao/{codigoInscricao}/{codigoAutenticacao}")
	public InscricaoVO validarCodigoAutenticacaoAcessoInscricao(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("codigoAutenticacao") final Integer codigoAutenticacao) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			Boolean codigoAutenticacaoExpirada = true;
			if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraVencimentoCodigoAutenticacao())) {
				codigoAutenticacaoExpirada = UteisData.validarDataInicialMaiorFinalComHora(new Date(),inscricaoVO.getDataHoraVencimentoCodigoAutenticacao());
			}
			if (codigoAutenticacaoExpirada) {
				throw new ConsistirException("Código de Autenticação Expirado, Solicite Um Novo Código.");
			}else if (!inscricaoVO.getCodigoAutenticacao().equals(codigoAutenticacao)) {
				throw new ConsistirException("Código de Autenticação Inválido");
			}
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarInscricaoCandidatoProcessoSeletivo/{codigoInscricao}")
	public InscricaoVO consultarInscricaoCandidatoProcessoSeletivo(@PathParam("codigoInscricao") final Integer codigoInscricao) {
		try {
			InscricaoVO inscricaoVO = new InscricaoVO();			
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarInscricaoAptaMatricula/{tipoDocumento}/{numeroDocumento}")
	public InscricaoVO consultarInscricaoAptaMatricula(@PathParam("tipoDocumento") final String tipoDocumento , @PathParam("numeroDocumento") final String numeroDocumento) {
		try {
			autenticarTokenCliente();
			InscricaoVO inscricaoVO =  new InscricaoVO();
			inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarInscricaoAptaMatricula(tipoDocumento, numeroDocumento, false, 0, getUsuario());
			
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/enviarCodigoAutenticacaoAcessoMatricula/{codigoInscricao}/{meioAutenticacao}/{valorValidarCandidato}")
	public InscricaoVO enviarCodigoAutenticacaoAcessoMatricula(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("meioAutenticacao") final String meioAutenticacao , @PathParam("valorValidarCandidato") final String valorValidarCandidato) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			getFacadeFactory().getInscricaoFacade().validarEmailTelefoneCandidato(inscricaoVO, meioAutenticacao, valorValidarCandidato);
			inscricaoVO.getProcSeletivo().setUtilizarAutenticacaoEmail(Boolean.TRUE);
			Random rnd = new Random();
			Integer codigoAutenticacao = 100000 + rnd.nextInt(900000);
			Boolean codigoAutenticacaoExpirada = true;
			if (Uteis.isAtributoPreenchido(inscricaoVO.getDataHoraVencimentoCodigoAutenticacao())) {
				codigoAutenticacaoExpirada = UteisData.validarDataInicialMaiorFinalComHora(new Date() ,inscricaoVO.getDataHoraVencimentoCodigoAutenticacao());
			}
			getFacadeFactory().getInscricaoFacade().enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(inscricaoVO, codigoAutenticacao, meioAutenticacao, configuracaoGeralSistemaVO,false, codigoAutenticacaoExpirada, false, usuarioVO);
			return inscricaoVO;
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	


	
	

	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/alterarPoliticaPrivacidade")
	public Response alterarPoliticaPrivacidade(UsuarioVO usuarioLogin) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			getFacadeFactory().getUsuarioFacade().alterarPoliticaPrivacidade(usuarioLogin);
			return Response.status(Status.OK).entity(usuarioLogin).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarDisciplinasAlunoDownloadMaterial/{unidadeEnsino}/{matricula}/{ano}/{semestre}/{codigoPerfilAcesso}")
	public Response consultarDisciplinasAlunoDownloadMaterial(@PathParam("unidadeEnsino") final Integer unidadeEnsino, @PathParam("matricula") final String matricula, @PathParam("ano") Integer ano, @PathParam("semestre") Integer semestre, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
			List<DisciplinaVO> disciplinaVOs = new ArrayList<>();
			if (!Uteis.isAtributoPreenchido(ano) && !Uteis.isAtributoPreenchido(semestre) && !matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
				String anoSemestre = getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(matriculaVO.getMatricula(), new ArrayList<>());
				String anoPrm = montarAnoAlunoDownloadMaterial(anoSemestre);
				ano = Integer.parseInt(anoPrm);
				String semestrePrm = montarSemestreAlunoDownloadMaterial(anoSemestre);
				semestre = Integer.parseInt(semestrePrm);
			}
			disciplinaVOs.addAll(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaAluno(unidadeEnsino, matriculaVO.getCurso().getPeriodicidade(), matricula, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO, ano.toString(), semestre.toString()));
			return Response.status(Status.OK).entity(new GenericEntity<List<DisciplinaVO>>(disciplinaVOs) {
			}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/obterUrlRedirecionamento/{emailInstitucional}")
	public Response obterUrlRedirecionamento(@PathParam("emailInstitucional") final String emailInstitucional) {
		try {
			ConfiguracaoLdapVO configuracaoLdapVO = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdaps()
					.stream()
					.filter(conf -> emailInstitucional.toLowerCase().endsWith(conf.getDominio().toLowerCase()))
					.findFirst()
					.orElseThrow(() -> new Exception("Configurações de login para o domínio informado não encontrada."));
			String saml = Uteis.obterSaml(configuracaoLdapVO);
			String base64 = Uteis.obterRequisicaoBase64Saml(saml);
			return Response.status(Status.OK).entity(Uteis.obterUrlRedirecionamentoSaml(configuracaoLdapVO, base64, emailInstitucional)).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/realizarLoginSSO/{token}/{device}/{sessao}")
	public Response realizarLoginSSO(@PathParam("token") final String token, @PathParam("device") final String device, @PathParam("sessao") final String sessao) {
		try {
			UsuarioVO usuarioVO = obterUsuarioSessao(sessao);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario(usuarioVO.getPessoa().getCodigo(), false, usuarioVO, configuracaoGeralSistemaVO);
			if (matriculaVOs.isEmpty()) {
				throw new Exception("Não Foi Encontrada Uma Matrícula Para Este Usuário");
			} else {
				montarDadosMatriculaAcessoAluno(usuarioVO, matriculaVOs.get(0));
				Iterator<MatriculaVO> i = matriculaVOs.iterator();
				while (i.hasNext()) {
					MatriculaVO matriculaVO = (MatriculaVO) i.next();
					if (!matriculaVO.getUnidadeEnsino().getCodigo().equals(0)) {
						matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuarioVO));
						if (!matriculaVO.getUnidadeEnsino().getConfiguracoes().getCodigo().equals(0)) {
							usuarioVO.setUnidadeEnsinoLogado(matriculaVO.getUnidadeEnsino());
							usuarioVO.setUrlLogoUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().renderizarLogoAplicativo(configuracaoGeralSistemaVO, matriculaVO.getUnidadeEnsino().getCaminhoBaseLogoAplicativo(), matriculaVO.getUnidadeEnsino().getNomeArquivoLogoAplicativo()));
						}
					}
					break;
			}
			}
			usuarioVO.getMatriculaVOs().addAll(matriculaVOs);
			usuarioVO.setCaminhoFotoUsuarioAplicativo(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(usuarioVO.getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));
			
			if(configuracaoGeralSistemaVO.getApresentarMensagemAlertaAlunoNaoAssinouContrato() && !matriculaVOs.isEmpty()) {	
				matriculaVOs.get(0).setMensagemAlertaAlunoNaoAssinouContratoMatricula(configuracaoGeralSistemaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
				matriculaVOs.get(0).setMensagemAlertaAlunoNaoAssinouContratoMatricula(matriculaVOs.get(0).getMensagemAlertaAlunoNaoAssinouContratoMatricula().replaceAll("\\r\\n|\\r|\\n", " "));
			}
			
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula()) && !matriculaVOs.isEmpty()) {
				matriculaVOs.get(0).setAlunoNaoAssinouContratoMatricula(getFacadeFactory().getDocumetacaoMatriculaFacade().validaAlunoAssinouContratoMatricula(matriculaVOs.get(0).getMatricula()));
			}
						
			usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(configuracaoGeralSistemaVO, matriculaVOs.get(0), matriculaVOs.get(0).getAlunoNaoAssinouContratoMatricula(), usuarioVO ));
			if (!token.equals("null") && !device.equals("undefined")) {
				usuarioVO.setTokenAplicativo(token);
				usuarioVO.setCelular(PlataformaEnum.valueOf(device));
				getFacadeFactory().getUsuarioFacade().alterarTokenEPlataformaAplicativo(usuarioVO);
			}
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoVizualizarMateriaisPeriodoConcluido", usuarioVO);
				usuarioVO.setPermitirAlunoVizualizarMateriaisPeriodoConcluido(true);				
			} catch (Exception e) {
				usuarioVO.setPermitirAlunoVizualizarMateriaisPeriodoConcluido(false);
			}
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
    }
	
	private UsuarioVO obterUsuarioSessao(String sessao) throws Exception {
		Manager manager = getManager(request.getServletContext());
		Session session = manager.findSession(Uteis.converterHexParaString(sessao));
		if (session == null || !session.isValid()) {
			throw new Exception("Usuário e/ou senha incorretos!");
		}
		return (UsuarioVO) session.getSession().getAttribute("usuarioLogado");
	}
	
	private Manager getManager(ServletContext servletContext) {
        try {
            ApplicationContextFacade applicationContextFacade = (ApplicationContextFacade) servletContext;
            Field applicationContextFacadeField = ApplicationContextFacade.class.getDeclaredField("context");
            applicationContextFacadeField.setAccessible(true);
            ApplicationContext appContext = (ApplicationContext) applicationContextFacadeField.get(applicationContextFacade);
            Field applicationContextField = ApplicationContext.class.getDeclaredField("context");
            applicationContextField.setAccessible(true);
            StandardContext stdContext = (StandardContext) applicationContextField.get(appContext);
            return stdContext.getManager();
        } catch (Exception e) {
            //skip this as we can also use Manager as null for metrics
            //"Unable to get Catalina Manager. Cause: {}", e.getMessage() , e;
        }
        return null;
    }
	
	private void realizarPreencherCaminhoAnexoImagem(List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO2) {
		for (DocumetacaoMatriculaVO documento : listaDocumentacaoMatriculaVO) {
			if (Uteis.isAtributoPreenchido(documento.getArquivoVO().getPastaBaseArquivo())
					&& Uteis.isAtributoPreenchido(documento.getArquivoVO().getNome())) {
				String caminhoImagemAnexo = configuracaoGeralSistemaVO2.getUrlExternoDownloadArquivo() + File.separator
						+ documento.getArquivoVO().getPastaBaseArquivo() + File.separator
						+ documento.getArquivoVO().getNome();
				documento.getArquivoVO().setCaminhoImagemAnexo(caminhoImagemAnexo);
			}
			if (Uteis.isAtributoPreenchido(documento.getArquivoVOVerso().getPastaBaseArquivo())
					&& Uteis.isAtributoPreenchido(documento.getArquivoVOVerso().getNome())) {
				String caminhoImagemAnexoVerso = configuracaoGeralSistemaVO2.getUrlExternoDownloadArquivo()
						+ File.separator + documento.getArquivoVOVerso().getPastaBaseArquivo() + File.separator
						+ documento.getArquivoVOVerso().getNome();
				documento.getArquivoVOVerso().setCaminhoImagemAnexo(caminhoImagemAnexoVerso);
			}

		}

	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gerarContratoMatricularAluno")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaRSVO realizarGeracaoContratoMatriculaOnlineExterna(final MatriculaRSVO matriculaRSVO) {
		try {
			autenticarTokenCliente();
			matriculaRSVO.setMensagem(null);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());
			UsuarioVO usuarioVO = getObterUsuarioOperacoesExternas(configuracaoGeralSistema);
			if (!Uteis.isAtributoPreenchido(usuarioVO)) {
				matriculaRSVO.setMensagem("Necessario Informar Um Usuario Operações Externa.");
				return matriculaRSVO;
			}			
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), null);
			getFacadeFactory().getMatriculaFacade().verificarExistenciaPreencherDadosPertinentesMatriculaOnlineRealizadaPendendeAssinaturaContratoEletronicoEntregaDocumento(matriculaRSVO,false, "CONTRATO"  ,usuarioVO, true, configuracaoGeralSistema,  null);		
			return matriculaRSVO;
		} catch (Exception e) {
			e.printStackTrace();
			matriculaRSVO.setMatriculaRealizadaComSucesso(false);
			matriculaRSVO.setMensagem(e.getMessage());
			return matriculaRSVO;
		}
	}
	
	private UsuarioVO getObterUsuarioOperacoesExternas(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioOperacaoExternas())) {
			configuracaoGeralSistemaVO.setUsuarioOperacaoExternas( getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		return configuracaoGeralSistemaVO.getUsuarioOperacaoExternas();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/realizarLoginSSOProfessor/{token}/{device}/{tipoPessoa}/{sessao}")
	public Response realizarLoginSSOProfessor(@PathParam("token") final String token, @PathParam("device") final String device, @PathParam("tipoPessoa") final String tipoPessoa, @PathParam("sessao") final String sessao) throws Exception {
		try {
			UsuarioVO usuarioVO = obterUsuarioSessao(sessao);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			if (!usuarioVO.getPessoa().getProfessor()) {
				throw new Exception("Apenas Professores Podem Acessar Este Aplicativo.");
			}
			if (Uteis.isAtributoPreenchido(tipoPessoa) && tipoPessoa.equals("professor")) {
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().definirPerfilAcessoParaAlunoProfessorECandidato("professor", configuracaoGeralSistemaVO));
				PermissaoAcessoMenuVO permissaoAcessoMenuVO = getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(usuarioVO.getPerfilAcesso());
				usuarioVO.setPermitirLancarNotaRetroativoMobile(permissaoAcessoMenuVO.getPermitirLancarNotaRetroativo());
				usuarioVO.setPermitirRegistrarAulaRetroativoMobile(permissaoAcessoMenuVO.getPermitirRegistrarAulaRetroativo());
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				//Utilizando atributo ipmaquina temporariamente para armazenar matrícula até obter autorização para criação de novo atributo no usuarioVO.
				usuarioVO.setIpMaquinaLogada(funcionarioVO.getMatricula());
			} 
			usuarioVO.setCaminhoFotoUsuarioAplicativo(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(usuarioVO.getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoWeb(usuarioVO), "foto_usuario.jpg", false));
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(unidadeEnsinoVO, NivelMontarDados.TODOS, usuarioVO);
			}
			usuarioVO.setUnidadeEnsinoLogado(unidadeEnsinoVO);
			usuarioVO.setUrlLogoUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().renderizarLogoAplicativo(configuracaoGeralSistemaVO, unidadeEnsinoVO.getCaminhoBaseLogoAplicativo(), unidadeEnsinoVO.getNomeArquivoLogoAplicativo()));
			if (!token.equals("null") && !device.equals("undefined")) {
				usuarioVO.setTokenAplicativo(token);
				usuarioVO.setCelular(PlataformaEnum.valueOf(device));
				getFacadeFactory().getUsuarioFacade().alterarTokenEPlataformaAplicativo(usuarioVO);
			}
			return Response.status(Status.OK).entity(usuarioVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validarSessaoNavegadorHomeCandidato/{codigoInscricao}/{codigoautenticacaoNavegador}/{somentegravarSessao}/{navegador}/{action}")
	public Response validarSessaoNavegadorHomeCandidato(@PathParam("codigoInscricao") final Integer codigoInscricao ,  @PathParam("codigoautenticacaoNavegador") final String codigoautenticacaoNavegador ,  @PathParam("somentegravarSessao") final Boolean somentegravarSessao ,  @PathParam("navegador") final String navegador , @PathParam("action") final String action) {
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();				
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(codigoInscricao ,codigoautenticacaoNavegador,navegador, somentegravarSessao ,action, usuarioVO);
			if (Uteis.isAtributoPreenchido(inscricaoVO)) {
				getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO, usuarioVO);				
			}
			return Response.status(Status.OK).entity(inscricaoVO.getCodigoAutenticacaoNavegador()).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registrarIndeferimentoContratoPorAluno")
	public Response registrarIndeferimentoContratoPorAluno(final MatriculaRSVO matricula) {
		try {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
			documentoAssinadoPessoaVO = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatricula(matricula.getMatricula());
			documentoAssinadoPessoaVO.setMotivoRejeicao(matricula.getMotivoRecusa());
			documentoAssinadoPessoaVO.setDataRejeicao(new Date());
			documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(documentoAssinadoPessoaVO);
			return Response.status(Status.OK).entity(documentoAssinadoPessoaVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarIndeferimentoContratoPorAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			} catch (Exception e1) {
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			}
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registrarAssinaturaContratoPorAluno")
	public Response registrarAssinaturaContratoPorAluno (final MatriculaRSVO matricula) {
		try {
			DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();											
			documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(matricula.getMatricula(), 0, "", TipoOrigemDocumentoAssinadoEnum.CONTRATO, false, 0, getUsuarioLogado());
			documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO)).forEach(documentoAssinadoPessoaAluno-> documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE));
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome(), documentoAssinadoVO.getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(documentoAssinadoVO, fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			return Response.status(Status.OK).entity(documentoAssinadoVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem("Ocorreu um erro na assinatura do seu contrato, para concluir este processo acesse a plataforma educacional com seu login e senha e tente novamente.");
			errorInfoRSVO.setMensagemErro(e.getMessage());
			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarAssinaturaContratoPorAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			} catch (Exception e1) {
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			}
		}
	}
	
	public void definirVisaoLogar(UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO.getPessoa().getPossuiAcessoVisaoPais() && !getFacadeFactory().getMatriculaFacade().verificarPossuiMatriculaAtivaPessoa(usuarioVO.getPessoa().getCodigo(), false, usuarioVO)) {
			usuarioVO.setVisaoLogar("pais");
		} else if (usuarioVO.getPessoa().getCodigo() > 0 && !usuarioVO.getPessoa().getAluno() && usuarioVO.getPessoa().getPossuiAcessoVisaoPais()) {
			usuarioVO.setVisaoLogar("pais");
		}  else {
			usuarioVO.setVisaoLogar("aluno");
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/alterarSenha")
	public Response realizarAlteracaoSenha(String alterarSenha) {
		try {
			UsuarioVO usuarioVO = obterUsuarioWebService();
			usuarioVO.setVisaoLogar("aluno");
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			
			AlterarSenhaRSVO alterarSenhaRSVO = new AlterarSenhaRSVO();
			Type type = new TypeToken<AlterarSenhaRSVO>() {}.getType();
			
			alterarSenhaRSVO = gson.fromJson(alterarSenha, type);			
			usuarioVO.getPerfilAcesso().setCodigo(alterarSenhaRSVO.getPerfilAcesso());
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ConfiguracoesAlteracaoSenha", usuarioVO);
			Uteis.validarSenha(getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), alterarSenhaRSVO.getSenhaNova());
			getFacadeFactory().getUsuarioFacade().alterarSenha( usuarioVO, usuarioVO.getUsername(), alterarSenhaRSVO.getSenhaAtual(),
					 usuarioVO.getUsername(), alterarSenhaRSVO.getSenhaNova(), getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarSeExisteConfiguracaoSeiGsuitePadrao(usuarioVO));
			return Response.status(Status.OK).entity(usuarioVO).build();
		}catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/alterarFoto")
	public Response realizarAlteracaoFoto(@Context HttpServletRequest request) {
		try {
			UsuarioVO usuarioVO = obterUsuarioWebService();
			usuarioVO.setVisaoLogar("aluno");
			DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
			JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);			
			List<DiskFileItem> items = upload.parseRequest(request);
			String visaoLogar = items.stream().anyMatch(i -> i.getFieldName().equals("visaoLogar"))
					? items.stream().filter(i -> i.getFieldName().equals("visaoLogar")).findFirst().get().getString()
					: request.getHeader("visaoLogar");
			if (Uteis.isAtributoPreenchido(visaoLogar)) {
				usuarioVO.setVisaoLogar(visaoLogar);
			}
			String perfilAcesso = items.stream().anyMatch(i -> i.getFieldName().equals("perfilAcesso"))
					? items.stream().filter(i -> i.getFieldName().equals("perfilAcesso")).findFirst().get().getString()
					: request.getHeader("perfilAcesso");
			if (perfilAcesso == null || !Uteis.getIsValorNumerico(perfilAcesso)) {
				throw new Exception(
						"Deve ser informado no body da requisição o código do perfil de acesso (perfilAcesso).");
			}
			Integer pessoa = Integer.valueOf((items.stream().anyMatch(i -> i.getFieldName().equals("pessoa"))
					? items.stream().filter(i -> i.getFieldName().equals("pessoa")).findFirst().get().getString()
					: request.getHeader("pessoa")));
			usuarioVO.getPerfilAcesso().setCodigo(Integer.valueOf(perfilAcesso));
//			getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ConfiguracoesAlteracaoSenha", usuarioVO);
			PessoaVO pessoaVO = null;
			if (Uteis.isAtributoPreenchido(pessoa)) {
				pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoa, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			}
			if (items.stream().anyMatch(i -> !i.isFormField())) {
				if (items != null) {
					Iterator<DiskFileItem> iter = items.iterator();
					while (iter.hasNext()) {
						DiskFileItem item = iter.next();
						if (!item.isFormField() && item.getSize() > 0) {
							File arquivoOriginal = null;
							if (Uteis.isAtributoPreenchido(pessoaVO.getArquivoImagem().getCodigo())) {
								arquivoOriginal = new File(getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo()
										+ File.separator + pessoaVO.getArquivoImagem().getPastaBaseArquivo()
										+ File.separator + pessoaVO.getArquivoImagem().getNome());
							}
							pessoaVO.getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM_TMP);
							pessoaVO.getArquivoImagem().setCpfRequerimento(pessoaVO.getCPF());

							getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimentoAplicativo(
									item, pessoaVO.getArquivoImagem(),
									"foto_" + pessoaVO.getCodigo() + "_" + Uteis.getDataComHora(new Date()),
									getConfiguracaoGeralSistemaVO(), PastaBaseArquivoEnum.IMAGEM_TMP, usuarioVO);
							pessoaVO.inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(
									getConfiguracaoGeralPadraoSistema(), "ALUNO");
							getFacadeFactory().getPessoaFacade().alterarFoto(pessoaVO, usuarioVO,
									getConfiguracaoGeralPadraoSistema());
							pessoaVO.setUrlFotoAluno(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
									pessoaVO.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
									getConfiguracaoGeralSistemaVO(), getCaminhoWeb(usuarioVO), "foto_usuario.jpg",
									false));
							if (arquivoOriginal.exists()) {
								arquivoOriginal.delete();
							}
							break;
						}
					}
				}
			} else {
				throw new Exception("Deve ser enviado a foto para alteração.");
			}
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy())
					.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(pessoaVO.getUrlFotoAluno());
			return Response.status(Status.OK).entity(json).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/V2/carregarMinhasNotasAluno/{matricula}/{codigoPerfilAcesso}/{ano}/{semestre}")
	public Response carregarMinhasNotasAlunoV2(@PathParam("matricula") final String matricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso, @PathParam("ano") final String ano, @PathParam("semestre") final String semestre) throws Exception {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
			usuarioVO.setVisaoLogar("aluno");
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("MinhasNotas", usuarioVO);
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
			matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matricula, false, usuarioVO));
			matriculaVO.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoPorMatriculaCurso(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			MinhasNotasAluno minhasNotasAluno = new MinhasNotasAluno();
			String anoSemestreFilter = (!Uteis.isAtributoPreenchido(ano) || ano.length() != 4 || matriculaVO.getCurso().getIntegral()  ? "" : ano) + (!Uteis.isAtributoPreenchido(semestre) || ano.length() != 4 || matriculaVO.getCurso().getIntegral() || !Uteis.isAtributoPreenchido(ano) || !matriculaVO.getCurso().getSemestral() || (!semestre.equals("1") && !semestre.equals("2")) ? "" : semestre );
			if(!Uteis.isAtributoPreenchido(anoSemestreFilter)) {
				if(matriculaVO.getCurso().getAnual()) {
					anoSemestreFilter =  Uteis.getAnoDataAtual4Digitos();
				}else if(matriculaVO.getCurso().getSemestral()) {
					anoSemestreFilter =  Uteis.getAnoDataAtual4Digitos()+"/"+Uteis.getSemestreAtual();
}
			}else if(matriculaVO.getCurso().getIntegral() && Uteis.isAtributoPreenchido(anoSemestreFilter)) {
				anoSemestreFilter = "";
			}else if(!matriculaVO.getCurso().getIntegral() && Uteis.isAtributoPreenchido(ano) && ano.length() != 4) {
				throw new Exception("O filtro ANO deve ser informado com 4 dígitos");
			}else if(matriculaVO.getCurso().getSemestral() && Uteis.isAtributoPreenchido(semestre) && !semestre.equals("1")&& !semestre.equals("2")) {
				throw new Exception("O filtro SENESTRE deve ser informado 1 ou 2.");
			} 							
			minhasNotasAluno.setModeloApresentacao("BOLETIM");
			List<SelectItem> listaSelectItemAnoSemestre =  new ArrayList<SelectItem>(0);
			String anoSemestreSugerir = getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(matricula,listaSelectItemAnoSemestre);
			listaSelectItemAnoSemestre.forEach(a -> {if(!a.getLabel().isEmpty()) { minhasNotasAluno.getListaAnoSemestre().add(a.getLabel()); }});
			if(Uteis.isAtributoPreenchido(anoSemestreFilter)) {
				anoSemestreSugerir = anoSemestreFilter;
			}
			minhasNotasAluno.setAno(!Uteis.isAtributoPreenchido(anoSemestreSugerir) ? "" : anoSemestreSugerir.contains("/") ? anoSemestreSugerir.substring(0, anoSemestreSugerir.indexOf("/")) :  anoSemestreSugerir);
			minhasNotasAluno.setSemestre(!Uteis.isAtributoPreenchido(anoSemestreSugerir) || !anoSemestreSugerir.contains("/") ? "" : anoSemestreSugerir.substring(anoSemestreSugerir.indexOf("/")+1, anoSemestreSugerir.length()));
			
			minhasNotasAluno.setModeloApresentacao("HISTORICO");
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaBasicaHistoricoAplicativo(matriculaVO, usuarioVO);
			if(matriculaVO.getCurso().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				Ordenacao.ordenarLista(historicoVOs, "dataPrimeiraAula");				
			} else {
				Ordenacao.ordenarListaDecrescente(historicoVOs, "ordenacaoMinhasNotasAplicativo");				
			}
			minhasNotasAluno.setHistoricoVOs(historicoVOs);
			
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(minhasNotasAluno);
			return Response.status(Status.OK).entity(json).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	public class MinhasNotasAluno{
		
		private String modeloApresentacao;
		
		private String urlBoletim;
		
		private String ano;
		private String semestre;
		
		private List<String> listaAnoSemestre;
		
		private List<HistoricoVO> historicoVOs;
		
		public String getModeloApresentacao() {
			return modeloApresentacao;
		}
		public void setModeloApresentacao(String modeloApresentacao) {
			this.modeloApresentacao = modeloApresentacao;
		}
		public String getUrlBoletim() {
			return urlBoletim;
		}
		public void setUrlBoletim(String urlBoletim) {
			this.urlBoletim = urlBoletim;
		}
		public List<HistoricoVO> getHistoricoVOs() {
			return historicoVOs;
		}
		public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
			this.historicoVOs = historicoVOs;
		}
		
		public List<String> getListaAnoSemestre() {
			if(listaAnoSemestre == null) {
				listaAnoSemestre =  new ArrayList<String>(0);
			}
			return listaAnoSemestre;
		}
		public void setListaAnoSemestre(List<String> listaAnoSemestre) {
			this.listaAnoSemestre = listaAnoSemestre;
		}
		public String getAno() {
			return ano;
		}
		public void setAno(String ano) {
			this.ano = ano;
		}
		public String getSemestre() {
			return semestre;
		}
		public void setSemestre(String semestre) {
			this.semestre = semestre;
		}
		
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/carregarAgendaAlunoDia/V2/{matricula}/{data}/{codigoUnidadeEnsinoMatricula}/{codigoPerfilAcesso}")
	public Response carregarAgendaAlunoDiaV2(@PathParam("matricula") final String matricula, @PathParam("data") final String data, @PathParam("codigoUnidadeEnsinoMatricula") final Integer codigoUnidadeEnsinoMatricula, @PathParam("codigoPerfilAcesso") final Integer codigoPerfilAcesso) throws Exception {
//		try {
//			UsuarioVO usuarioVO = autenticarConexao();
//			usuarioVO.getPerfilAcesso().setCodigo(codigoPerfilAcesso);
//			usuarioVO.setVisaoLogar("aluno");
//			UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
//			unidadeEnsinoVO.setCodigo(codigoUnidadeEnsinoMatricula);
//			Date dataPRM = Uteis.getData(data, "yyyy-MM-dd");
//			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaAtiva(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
////			List<AgendaAlunoRSVO> agendaAlunoRSVOs = getFacadeFactory().getHorarioAlunoFacade().consultarAgendaAlunoDia(matriculaPeriodoTurmaDisciplinaVOs, dataPRM, unidadeEnsinoVO, usuarioVO);
//			return Response.status(Status.OK).entity(new GenericEntity<List<AgendaAlunoRSVO>>(agendaAlunoRSVOs) {}).build();
//		} catch (Exception e) {
//			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
//			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
//			errorInfoRSVO.setMensagem(e.getMessage());
//			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
//		}
		
		return Response.status(Status.OK).entity(new GenericEntity<List<AgendaAlunoRSVO>>(new ArrayList<AgendaAlunoRSVO>(0)) {}).build();
	}
	
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/assinarDocumentoDigital")
	public Response assinarDocumentoDigital(final String documentoAssinado, @Context HttpServletRequest request) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			definirVisaoLogar(usuarioVO);
			String latitude = request.getHeader("latitude");
			String longitude = request.getHeader("longitude");
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			DocumentoAssinadoVO documentoAssinadoVO = null;
			Type type = new TypeToken<DocumentoAssinadoVO>() {
			}.getType();
			documentoAssinadoVO = gson.fromJson(documentoAssinado, type);
			String matricula = Uteis.isAtributoPreenchido(request.getHeader("matricula")) ? request.getHeader("matricula") : documentoAssinadoVO.getMatricula().getMatricula();
			if(!Uteis.isAtributoPreenchido(matricula)) {
				throw new Exception("Deve ser enviado a matrícula do aluno.");
			}
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, usuarioVO);
			documentoAssinadoVO.setMatricula(matriculaVO);				
			getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaRejeicaoDocumentoAppAluno(documentoAssinadoVO, usuarioVO, latitude, longitude, true);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
			realizarDefinicoesUsuarioLogado(usuarioVO, matriculaVO, configuracaoGeralSistemaVO);
			gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(usuarioVO);
			return Response.status(Status.OK).entity(json).build();
		}catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/rejeitarDocumentoDigital")
	public Response rejeitarDocumentoDigital(final String documentoAssinado, @Context HttpServletRequest request) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			definirVisaoLogar(usuarioVO);
			String latitude = request.getHeader("latitude");
			String longitude = request.getHeader("longitude");
			String matricula = request.getHeader("matricula");
			if(!Uteis.isAtributoPreenchido(matricula)) {
				throw new Exception("Deve ser enviado a matrícula do aluno.");
			}
			Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();
			DocumentoAssinadoVO documentoAssinadoVO = null;
			Type type = new TypeToken<DocumentoAssinadoVO>() {
			}.getType();
			documentoAssinadoVO = gson.fromJson(documentoAssinado, type);
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, usuarioVO);
			documentoAssinadoVO.setMatricula(matriculaVO);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaRejeicaoDocumentoAppAluno(documentoAssinadoVO, usuarioVO, latitude, longitude, false);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(documentoAssinadoVO.getMatricula().getUnidadeEnsino().getCodigo(), usuarioVO);
			realizarDefinicoesUsuarioLogado(usuarioVO, documentoAssinadoVO.getMatricula(), configuracaoGeralSistemaVO);
			gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(usuarioVO);
			return Response.status(Status.OK).entity(json).build();
		}catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarDocumentoPendenteAssinatura/{matricula}")
	public Response consultarDocumentoPendenteAssinatura(@PathParam("matricula") final String matricula) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			definirVisaoLogar(usuarioVO);
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, usuarioVO);
			consultarDocumentoPendenteAssinatura(matriculaVO, usuarioVO);
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(usuarioVO.getDocumentoPendenteAssinatura());
			return Response.status(Status.OK).entity(json).build();
		}catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	private void consultarDocumentoPendenteAssinatura(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception{
		MatriculaPeriodoVO matriculaPeriodoVO = (getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));		
		getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoDocumentoAssinadoPendenteUsuarioLogado(usuarioVO, matriculaVO, matriculaPeriodoVO, true);
		for(DocumentoAssinadoVO documentoAssinadoVO: usuarioVO.getDocumentoPendenteAssinatura()) {				
			UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getMatricula(), "matricula");
			UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getMatriculaPeriodo(), "codigo");
			UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getCursoVO(), "codigo");
			UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getUnidadeEnsinoVO(), "codigo");
			UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getTurma(), "codigo");
			for(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO: documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoPessoaVO.getPessoaVO(), "codigo", "nome", "cpf", "senhaCertificadoParaDocumento");
			}
			documentoAssinadoVO.setGradecurricular(null);
			documentoAssinadoVO.setDisciplina(null);			
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarDocumentoDigitaisAluno/{matricula}/{perfilAcesso}/{limite}/{pagina}")
	public Response consultarDocumentoDigitaisAluno(@PathParam("matricula") final String matricula, @PathParam("perfilAcesso") final Integer perfilAcesso,@PathParam("limite") final Integer limite, @PathParam("pagina") final Integer pagina) {
		try {
			UsuarioVO usuarioVO = autenticarConexao();
			definirVisaoLogar(usuarioVO);
			if(!Uteis.isAtributoPreenchido(matricula)) {
				throw new Exception("O parâmetro MATRÍCULA deve ser informado");
			}
			if(!Uteis.isAtributoPreenchido(perfilAcesso)) {
				throw new Exception("O parâmetro PERFIL DE ACESSO deve ser informado");
			}
			usuarioVO.getPerfilAcesso().setCodigo(perfilAcesso);
			List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum = getFacadeFactory().getUsuarioFacade().realizarVerificacaoPermissaoTipoOrigemDocumentoAluno(usuarioVO);
			try {
				getFacadeFactory().getControleAcessoFacade().consultar(PerfilAcessoPermissaoVisaoAlunoEnum.MEUS_CONTRATOS.getValor(), true, usuarioVO);		
				listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
			} catch (Exception e) {
				
			}
			DataModelo controleConsultaOtimizado =  new DataModelo();
			controleConsultaOtimizado.setLimitePorPagina(limite);
			controleConsultaOtimizado.setPage(pagina);
			controleConsultaOtimizado.setPaginaAtual(pagina);
			controleConsultaOtimizado.setCampoConsulta("matricula");
			controleConsultaOtimizado.setValorConsulta(matricula);
			if(Uteis.isAtributoPreenchido(listaTipoOrigemDocumentoAssinadoEnum)) {			
				controleConsultaOtimizado.setListaConsulta(getFacadeFactory().getDocumentoAssinadoFacade()
						.consultarArquivoAssinado(controleConsultaOtimizado.getCampoConsulta(),
								controleConsultaOtimizado.getValorConsulta(),
								controleConsultaOtimizado.getLimitePorPagina(), controleConsultaOtimizado.getOffset(),
								false, Uteis.NIVELMONTARDADOS_TODOS, true, listaTipoOrigemDocumentoAssinadoEnum,
								SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioVO, true, false));
				controleConsultaOtimizado.setTotalRegistrosEncontrados(getFacadeFactory().getDocumentoAssinadoFacade()
						.consultarTotalRegistroArquivoAssinados(controleConsultaOtimizado.getCampoConsulta(),
								controleConsultaOtimizado.getValorConsulta(), false,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, listaTipoOrigemDocumentoAssinadoEnum,
								SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, usuarioVO, true, false));
			}
			for(DocumentoAssinadoVO documentoAssinadoVO: (List<DocumentoAssinadoVO>) controleConsultaOtimizado.getListaConsulta()) {
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getMatricula(), "matricula");
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getMatriculaPeriodo(), "codigo");
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getCursoVO(), "codigo");
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getUnidadeEnsinoVO(), "codigo");
				UtilReflexao.removerCamposChamadaAPI(documentoAssinadoVO.getTurma(), "codigo");
				documentoAssinadoVO.setDocumentoAssinadoPessoaVO(null);
				try {
					if(!documentoAssinadoVO.getArquivo().getNome().endsWith(".xml")) {
						documentoAssinadoVO.setCaminhoPreview(getFacadeFactory().getArquivoFacade().realizarVisualizacaoPreview(documentoAssinadoVO.getArquivo()));
					}
//					else if(!documentoAssinadoVO.getArquivo().getNome().endsWith(".xml") && Uteis.isAtributoPreenchido(documentoAssinadoVO.getArquivoVisual())) { 
//						documentoAssinadoVO.setCaminhoPreview(getFacadeFactory().getArquivoFacade().realizarVisualizacaoPreview(documentoAssinadoVO.getArquivoVisual()));
//					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				for(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO: documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
					UtilReflexao.removerCamposChamadaAPI(documentoAssinadoPessoaVO.getPessoaVO(), "codigo", "nome", "cpf");
				}
				
			}
    		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		String json =	gson.toJson(new ControleConsultaRS<DocumentoAssinadoVO>(controleConsultaOtimizado));
    		return Response.status(Status.OK).entity(json).build();				
		}catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	
	
	@GET
	@Produces({ "application/json" })
	@Path("/consultarDadosParaRealizarMatriculaOnline/{codigoInscricao}/{cursoAprovado}/{navegador}")
	public MatriculaRSVO consultarDadosParaRealizarMatriculaOnline(@PathParam("codigoInscricao") final Integer codigoInscricao , @PathParam("cursoAprovado") final Integer cursoAprovado , @PathParam("navegador") final String  navegador) {
		MatriculaRSVO matriculaRSVO = new MatriculaRSVO();
		try {
			autenticarTokenCliente();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();			
			getFacadeFactory().getMatriculaFacade().consultarDadosParaRealizarMatriculaOnline(codigoInscricao, cursoAprovado, navegador, matriculaRSVO,usuarioVO);
			return matriculaRSVO;	
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage() != null) {
				matriculaRSVO.setMensagem(e.getMessage());
			}else {
				matriculaRSVO.setMensagem("Ocorreu um erro interno ao consultar os dados para realização da matrícula online.");
			}
			
		}
		return matriculaRSVO;
	}	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/matricularAluno")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaRSVO realizarMatriculaOnlineExterna(final MatriculaRSVO matriculaRSVO) {
		try {			
			autenticarTokenCliente();			
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());
			UsuarioVO usuarioVO = getObterUsuarioOperacoesExternas(configuracaoGeralSistema);			
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioVO), "Necessário Informar Um Usuário Operações Externa.");
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(matriculaRSVO.getCodigoInscricao() ,matriculaRSVO.getCodigoAutenticacaoNavegador(), matriculaRSVO.getNavegadorAcesso(), false ,"MATRICULA", usuarioVO);
			getFacadeFactory().getMatriculaFacade().realizarCriacaoNovaMatriculaProcessoSeletivo(matriculaRSVO ,configuracaoGeralSistema , usuarioVO);
			if (Uteis.isAtributoPreenchido(inscricaoVO)) {
				getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO,usuarioVO);	
				matriculaRSVO.setCodigoAutenticacaoNavegador(inscricaoVO.getCodigoAutenticacaoNavegador());	
			}
			return matriculaRSVO;
		} catch (Exception e) {
			e.printStackTrace();
			matriculaRSVO.setMatriculaRealizadaComSucesso(false);
			matriculaRSVO.setMensagem(e.getMessage());
			return matriculaRSVO;
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarDisciplinasMatriculaOnline/{unidadeEnsino}/{aluno}/{periodoLetivo}/{condicaoPagamento}/{turma}/{curso}/{turno}/{gradeCurricular}/{processoMatricula}")
	public Response consultarDisciplinasMatriculaOnline(		
			@PathParam("unidadeEnsino") final Integer unidadeEnsino,
			@PathParam("aluno") final Integer aluno, 
			@PathParam("periodoLetivo") Integer periodoLetivo,
			@PathParam("condicaoPagamento") Integer condicaoPagamento, 
			@PathParam("turma") Integer turma,
			@PathParam("curso") Integer curso, 
			@PathParam("turno") final Integer turno,
			@PathParam("gradeCurricular") final Integer gradeCurricular,
			@PathParam("processoMatricula") final Integer processoMatricula) {
		try {			
		    autenticarTokenCliente();			
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());
			UsuarioVO usuarioVO = getObterUsuarioOperacoesExternas(configuracaoGeralSistema);			
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioVO), "Necessário Informar Um Usuário Operações Externa.");			
			
			
		    MatriculaRSVO matriculaRSVO = MatriculaRSVO.converterDadosEmMatriculaRSVO(unidadeEnsino,aluno,periodoLetivo,condicaoPagamento,turma,curso,turno,gradeCurricular,processoMatricula);
		    matriculaRSVO.setValidarAlunoJaMatriculado(false);
		    getFacadeFactory().getMatriculaFacade().realizarMontagenDadosDisciplinasMatriculaOnline(matriculaRSVO, new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0), usuarioVO);			
		    return Response.status(Status.OK).entity(new GenericEntity<List<DisciplinaRSVO>>(matriculaRSVO.getDisciplinasMatricula()) {}).build();
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.UNAUTHORIZED.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			throw new WebServiceException(errorInfoRSVO, Status.UNAUTHORIZED);
		}
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gravarDisciplinasMatricula")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)	
	public MatriculaRSVO gravarDisciplinasMatricula(final MatriculaRSVO matriculaRSVO) {
		try {
			UsuarioVO usuarioVO = obterUsuarioWebService();			
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarGravarDisciplinasMatriculaProcessoSeletivo(matriculaRSVO, usuarioVO);
			return matriculaRSVO;			
		} catch (Exception e) {
			System.out.println("MOBILE ERROR: " + e.getMessage());
			e.printStackTrace();				
			matriculaRSVO.setMensagem(e.getMessage());
			return matriculaRSVO;
		
		}
	}

	
}
