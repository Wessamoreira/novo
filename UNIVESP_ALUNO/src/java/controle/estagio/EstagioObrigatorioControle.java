package controle.estagio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;

import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.QuestionarioRespostaControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEstagioEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.estagio.ConcedenteVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.estagio.TipoConcedenteVO;
import negocio.comuns.estagio.TotalizadorEstagioSituacaoVO;
import negocio.comuns.estagio.enumeradores.RegrasSubstituicaoGrupoPessoaItemEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.estagio.enumeradores.TipoConsultaComboSituacaoAproveitamentoEnum;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("EstagioObrigatorioControle")
@Scope("viewScope")
@Lazy
public class EstagioObrigatorioControle extends QuestionarioRespostaControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6451817926953347637L;
	private static final String TELA_FORM = "estagioAluno.xhtml";
	private static final String TELA_mapaEstagioFacilitador_FORM = "mapaEstagioFacilitador.xhtml";
	private static final String TELA_estagioAlunoFormulario_FORM = "estagioAlunoFormulario.xhtml";
	
	private List<EstagioVO> listaEstagio;
	private DashboardEstagioVO dashboardEstagioVO;
	private DashboardEstagioVO dashboardMapaEstagioVO;
	private MatriculaVO matricula;
	private EstagioVO estagioVO;
	private EstagioVO estagioFiltro;
	private GrupoPessoaItemVO grupoPessoaItemAtual; 
	private GrupoPessoaItemVO grupoPessoaItemEspecifico;
	private GrupoPessoaItemVO grupoPessoaItemFiltro;
	private RegrasSubstituicaoGrupoPessoaItemEnum regrasSubstituicaoGrupoPessoaItemEnum;
	private boolean inativarGrupoPessoaItemGrupoParticipante = false;
	private boolean desabilitarGrupoPessoaItemAtual = false;
	private ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO;
	private String ano;
	private String semestre;
	private String caminhoPreview;
	private List<SelectItem> listaSelectItemGrupoPessoaItem;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemGradeCurricularEstagio;
	private List<SelectItem> listaSelectItemTipoConcedente;
	private List<SelectItem> listaSelectItemEstado;
	private String valorConsultaMatricula;
	private String campoConsultaMatricula;
	private List<MatriculaVO> matriculaVOs;
	private String tipoEstagioCidade;
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	private List<CidadeVO> listaConsultaCidade;	
	private String oncompleteModalEstagio;
	private String textoApresentacaoEstornoEstagio;	
	private String textoApresentacaoDeferimentoEstagio;	
	private boolean apresentarEstagioCadastro = false;
	private boolean desabilitarRespostaChecklist = true;
	private boolean permitirDeferirEstagio = false;
	private boolean permitirIndeferirEstagio = false;
	private boolean permitirCorrigirFormularioEstagio = false;
	private boolean permitirEstornoIndeferimentoEstagio = false;
	private boolean permitirEstornoDeferimentoEstagio = false;
	private boolean permitirEstornoCorrecaoEstagio = false;
	private DataModelo dataModeloGrupoPessoaItem;
	private Boolean permiteAtualizarEmailNotificarAssinaturaConcedente;
	private String  emailEnviarNotificacaoAssinaturaConcedente;
	private DocumentoAssinadoPessoaVO documentoAssinadoConcedente;
	private String oncompleteModalEnviarNotificacaoAssinaturaConcedente;
	private EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente;
	
	private List<EstagioVO> listaEstagioParaOperacaoLote;
	private ProgressBarVO progressBarVO;
	private OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum;
	private String motivo;
	private Boolean considerarEstagioAnalise;
	private Boolean considerarEstagioCorrecaoAluno;
	private List<GrupoPessoaVO> listaGrupoFacilitadoresRedistribuir;
	private GrupoPessoaVO grupoFacilitadoresRedistribuir;
	private String campoConsultaConcedente;
	private String valorConsultaConcedente;
	private Boolean permiteIncluirEstagioAluno;
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;

	@PostConstruct
	public void postConstructInicializarMenuEstagio() {
		try {
			MatriculaVO mat = null;
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) getControlador("VisaoAlunoControle");
			if (visaoAlunoControle != null) {
				mat = visaoAlunoControle.getMatricula();
			}
			inicializarMenuEstagio(mat);
			setPermiteIncluirEstagioAluno(getFacadeFactory().getControleAcessoFacade().verificarPermissaoOperacao("EstagioAluno", 1, getUsuarioLogado()));
		} catch (Exception e) {
			
		}
	}
	
	public void inicializarMenuEstagio(MatriculaVO mat) {
		try {
			if (mat != null && !mat.getMatricula().isEmpty()) {
				setMatricula((MatriculaVO) Uteis.clonar(mat));
				if(Uteis.isAtributoPreenchido(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO())) {
					getMatricula().setGradeCurricularAtual(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
				}else {
					getMatricula().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogadoClone()));
				}				
				realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			}
			setConfiguracaoEstagioObrigatorioVO(getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone()));
			setEstagioFiltro(new EstagioVO());
			setDashboardEstagioVO(new DashboardEstagioVO());
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);			
			setDashboardMapaEstagioVO(new DashboardEstagioVO());
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			getControleConsultaOtimizado().setPaginaAtual(1);
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa() || getUsuarioLogado().isUsuarioFacilitador()) {				
				montarListaSelectItemGrupoPessoaItem();
				if(getAplicacaoControle().getDashboardEstagioFacilitadorCache().containsKey(mat.getMatricula())) {
					setDashboardMapaEstagioVO(getAplicacaoControle().getDashboardEstagioFacilitadorCache().get(mat.getMatricula()));
				}else {
					getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagio(getDashboardMapaEstagioVO(), getEstagioFiltro(), getAno(), getSemestre(), getUsuarioLogado());
				}
				if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					realizarInicializacaoPermissoesParaTela();
					montarListaSelectItemUnidadeEnsino();	
				}
			} 
			if(getUsuarioLogado().getIsApresentarVisaoAluno()) {
				if(getAplicacaoControle().getDashboardEstagioAlunoCache().containsKey(mat.getMatricula())) {
					setDashboardEstagioVO(getAplicacaoControle().getDashboardEstagioAlunoCache().get(mat.getMatricula()));
				}else {
					getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(getDashboardEstagioVO(), getMatricula().getMatricula(), getUsuarioLogado());
				}
			}
			setCaminhoPreview(Constantes.EMPTY);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		} 
	}
	
	public void inicializarMenuEstagioNaoLiberado(MatriculaVO mat) {
		try {
			if (mat != null && !mat.getMatricula().isEmpty()) {
				setMatricula((MatriculaVO) Uteis.clonar(mat));
				if(Uteis.isAtributoPreenchido(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO())) {
					getMatricula().setGradeCurricularAtual(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
				}else {
					getMatricula().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogadoClone()));
				}
				realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			}			
			setEstagioFiltro(new EstagioVO());
			setDashboardEstagioVO(new DashboardEstagioVO());
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);			
			setDashboardMapaEstagioVO(new DashboardEstagioVO());
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			setCaminhoPreview(Constantes.EMPTY);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		} 
	}

	private void realizarInicializacaoPermissoesParaTela() {
		isPermitirDeferimentoEstagio();
		isPermitirEstornoIndeferimentoEstagio();
		isPermiteEstornoCorrecaoEstagio();
		isPermiteEstornoDeferimentoEstagio();
		isPermiteEstornoIndeferimentoEstagio();
		isPermiteCorrigirFormularioEstagio();
		isPermitirIndeferimentoEstagio();
		isVerificarPermissaoDesabilitarRespostaChecklist();
	}

	public void consultarDados() {
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioAguardandoAssinatura() {
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);			
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarVisualizacaoEstagioAguardandoAssinaturaTotal() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(obj.getSituacaoAdicionalEstagioEnum());
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioRealizando() {
		try {
			
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.REALIZANDO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmAnalise() {
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmCorrecao() {
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}

	public void realizarVisualizacaoEstagioDeferido() {		
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.DEFERIDO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioIndeferido() {
		try {
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.INDEFERIDO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);		
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}

	public void realizarVisualizacaoEstagioEmAnaliseNoPrazo() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(true);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmAnaliseAtrasado() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(true);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmAnaliseTotal() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmCorrecaoNoPrazo() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(true);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmCorrecaoAtrasado() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(true);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioEmCorrecaoTotal() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioDeferidoTotal() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.DEFERIDO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoEstagioIndeferidoTotal() {
		try {
			TotalizadorEstagioSituacaoVO obj = (TotalizadorEstagioSituacaoVO) getRequestMap().get(Constantes.totalizadorItens);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.INDEFERIDO);
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setTipoEstagioEnum(obj.getTipoEstagioEnum());
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, obj.getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarAtualizacaoPorLimitePorPagina() {
		try {
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, getDashboardMapaEstagioVO().getTipoEstagioEnum());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	private void carregarDadosEstagioAposOperacao(int nivelMontarDados, TipoEstagioEnum tipoEstagioEnum) throws Exception {
		getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagio(getDashboardMapaEstagioVO(), getEstagioFiltro(), getAno(), getSemestre(), getUsuarioLogadoClone());
		if(!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina())) {
			getControleConsultaOtimizado().setLimitePorPagina(10);	
		}
		setListaEstagio(getFacadeFactory().getEstagioFacade().consultarEstagio(getEstagioFiltro(), getDashboardMapaEstagioVO(), tipoEstagioEnum, getAno(), getSemestre(), nivelMontarDados, getControleConsultaOtimizado(), getUsuarioLogadoClone()));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setListaConsulta(getListaEstagio());
		realizarValidacaoLiberarNotificarConcedenteAssinaturaPendente();
		setCaminhoPreview(Constantes.EMPTY);
		inicializarMensagemVazia();
	}
	
	public void scrollListener() {
		
		
		try {
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, getDashboardMapaEstagioVO().getTipoEstagioEnum());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}

	}
	
	@SuppressWarnings("unchecked")
	public void inicializarOperacaoEstagioComDocumentoAssinadoEmLote() {
		try {	
			setMotivo(Constantes.EMPTY);
			setOperacaoDeVinculoEstagioEnum(OperacaoDeVinculoEstagioEnum.INDEFERIR);
			setListaEstagioParaOperacaoLote(((List<EstagioVO>) getControleConsultaOtimizado().getListaConsulta()).stream().filter(p-> p.getSelecionado()).collect(Collectors.toList()));
			Uteis.checkState(getListaEstagioParaOperacaoLote().size() == 0, "Não foi selecionado nenhum Estágio para ser processado.");
			setOncompleteModal("PF('panelOperacoesEstagioEmLote').show();");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setListaEstagioParaOperacaoLote(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarInicioProgressBarOperacaoEstagioLote() {
		try {
			setOncompleteModal("PF('panelOperacoesEstagioEmLote').hide();");
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(getOperacaoDeVinculoEstagioEnum().isIndeferir() && !Uteis.isAtributoPreenchido(getMotivo()), "O campo Motivo do Indeferimento deve ser informado.");
			Integer qtdContasSelecionadas = getListaEstagioParaOperacaoLote().size();
			Uteis.checkState(qtdContasSelecionadas == 0, "Não foi selecionado nenhum Estágio para ser processado.");
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (qtdContasSelecionadas.intValue()), "Iniciando Processamento da(s) operações.", true, this, "executarOperacaoEstagioEmLoteSelecionados");			
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);	
		} 
	}
	
	public void executarOperacaoEstagioEmLoteSelecionados() {
		try {
			ConsistirException consistirException = new ConsistirException();
			for (EstagioVO obj : getListaEstagioParaOperacaoLote()) {
				try {
					getProgressBarVO().setStatus(getProgressBarVO().getPreencherStatusProgressBarVO(getProgressBarVO(), "Estágio", obj.getCodigo().toString()));
					getFacadeFactory().getEstagioFacade().executarOperacaoEstagioEmLoteSelecionados(obj, getOperacaoDeVinculoEstagioEnum(), getMotivo(), getProgressBarVO().getUsuarioVO(), getConfiguracaoEstagioObrigatorioVO());
				} catch (Exception e) {
					consistirException.adicionarListaMensagemErro("Log Estágio de código:"+obj.getCodigo()+" descrição -"+ e.getMessage());
				} finally {
					getProgressBarVO().incrementar();
				}
			}	
			realizarAtualizacaoPorLimitePorPagina();
			if (consistirException.existeErroListaMensagemErro()) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), Uteis.ERRO);
				setListaMensagemErro(consistirException.getListaMensagemErro());
				setOncompleteModal("PF('panelOperacoesEstagioEmLoteLogs').show();");
			} else {
				setMensagemID(MSG_TELA.msg_dados_operacao.name());
				setOncompleteModal("PF('panelOperacoesEstagioEmLote').hide();");
			}
		} catch (Exception e) {
			setOncompleteModal("PF('panelOperacoesEstagioEmLoteLogs').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void selecionarTodosEstagio() {		
		((List<EstagioVO>) getControleConsultaOtimizado().getListaConsulta()).stream().forEach(p -> p.setSelecionado(true));		
	}
	
	@SuppressWarnings("unchecked")
	public void desmarcarTodosEstagio() {
		((List<EstagioVO>) getControleConsultaOtimizado().getListaConsulta()).stream().forEach(p -> p.setSelecionado(false));	
	}

	public void realizarVisualizacaoTermoEstagio() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO((EstagioVO) Uteis.clonar(obj));
			if (getEstagioVO().getSituacaoAdicionalEstagioEnum().isPendenteSolicitacaoAssinatura() || (!Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO()) && getEstagioVO().getSituacaoEstagioEnum().isIndeferido())) {
				setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getFacadeFactory().getEstagioFacade().realizarVisualizacaoTermoEstagio(getEstagioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone()));
			}else if (Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO())
					&& obj.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorTechCert(obj.getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone());
				setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getEstagioVO().getDocumentoAssinadoVO().getArquivo().getNome());
			}
			else if (Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO())) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorCertisign(obj.getDocumentoAssinadoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), false, true, false, getUsuarioLogadoClone());
				setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getEstagioVO().getDocumentoAssinadoVO().getArquivo().getNome());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoRelatorioFinalEstagio() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO((EstagioVO) Uteis.clonar(obj));
			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioUltimaVersaoPorEstagio(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherPerguntaChecklistOrigemVO(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao(), getUsuarioLogadoClone());
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherMotivosPadroesEstagioVO(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao(), getEstagioVO().getTipoEstagio(), true, false, getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarPersitenciaRelatorioFinalEstagio() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarSolicitacaoCorrecaoRelatorioFinalEstagio() {
		try {
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioEmCorrecao(getEstagioVO(), getConfiguracaoEstagioObrigatorioVO(), getUsuarioLogadoClone());
			realizarVisualizacaoEstagioEmCorrecao();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	
	public void realizarIndefericaoRelatorioFinalEstagio() {
		SituacaoEstagioEnum situacaoAnterior = getEstagioVO().getSituacaoEstagioEnum();
		SituacaoAdicionalEstagioEnum situacaoAdicionalAnterior = getEstagioVO().getSituacaoAdicionalEstagioEnum();
		try {
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(getEstagioVO(), SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR ,true , getUsuarioLogadoClone());
			realizarVisualizacaoEstagioIndeferido();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());			
		} catch (Exception e) {
			getEstagioVO().setSituacaoEstagioEnum(situacaoAnterior);
			getEstagioVO().setSituacaoAdicionalEstagioEnum(situacaoAdicionalAnterior);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarIndefericaoFormularioAluno() {
		SituacaoEstagioEnum situacaoAnterior = getEstagioVO().getSituacaoEstagioEnum();
		SituacaoAdicionalEstagioEnum situacaoAdicionalAnterior = getEstagioVO().getSituacaoAdicionalEstagioEnum();
		try {
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(getEstagioVO(),  SituacaoAdicionalEstagioEnum.INDEFERIDO_ALUNO,  true ,getUsuarioLogadoClone());
			realizarVisualizacaoEstagioIndeferidoAluno();
			realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());			
		} catch (Exception e) {
			getEstagioVO().setSituacaoEstagioEnum(situacaoAnterior);
			getEstagioVO().setSituacaoAdicionalEstagioEnum(situacaoAdicionalAnterior);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarDefericaoRelatorioFinalEstagio() {
		try {
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioDeferido(getEstagioVO(), getConfiguracaoEstagioObrigatorioVO(), getUsuarioLogadoClone());
			realizarVisualizacaoEstagioDeferido();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setOncompleteModalEstagio(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void fecharRelatorioFinalEstagio() {
		try {
			if(Uteis.isAtributoPreenchido(getEstagioVO())) {
				setEstagioVO(new EstagioVO());
				if ((getUsuarioLogado().getIsApresentarVisaoAdministrativa() || getUsuarioLogado().isUsuarioFacilitador())) {
					getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagio(getDashboardMapaEstagioVO(), getEstagioFiltro(), getAno(), getSemestre(), getUsuarioLogadoClone());
				} 
				if (getUsuarioLogado().getIsApresentarVisaoAluno() && Uteis.isAtributoPreenchido(getMatricula().getMatricula())){
					getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(getDashboardEstagioVO(), getMatricula().getMatricula(), getUsuarioLogadoClone());
					setListaEstagio(getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(getMatricula().getMatricula(), getDashboardEstagioVO().getSituacaoEstagioEnum(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
			}
			inicializarMensagemVazia();			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String fecharFormularioFinalEstagio() {
		try {
			setPage(1);
			fecharRelatorioFinalEstagio();
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}

	public void realizarEstornoFaseEstagio() {
		try {
			getFacadeFactory().getEstagioFacade().realizarEstornoFaseEstagio(getEstagioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getEstagioVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone());
			getDashboardMapaEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setSituacaoEstagioEnum(getEstagioVO().getSituacaoEstagioEnum());
			getDashboardMapaEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardMapaEstagioVO().setEstagioNoPrazo(false);
			getDashboardMapaEstagioVO().setEstagioAtrasado(false);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			carregarDadosEstagioAposOperacao(Uteis.NIVELMONTARDADOS_TODOS, TipoEstagioEnum.NENHUM);
			setEstagioVO(new EstagioVO());
		} catch (Exception e) {
			setOncompleteModalEstagio(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarInicializacaoDeferimentoEstagio() {
		try {
			switch (getEstagioVO().getTipoEstagio()) {
			case OBRIGATORIO:
				setTextoApresentacaoDeferimentoEstagio("Prezado, "+getUsuarioLogado().getNome()+" você confirma estar de acordo com o formulário de Relatório Final entregue pelo aluno(a) "+getEstagioVO().getMatriculaVO().getAluno().getNome()+"?");
				break;
			case OBRIGATORIO_APROVEITAMENTO:
				setTextoApresentacaoDeferimentoEstagio("Prezado, "+getUsuarioLogado().getNome()+" você confirma estar de acordo com o formulário de Aproveitamento entregue pelo aluno(a) "+getEstagioVO().getMatriculaVO().getAluno().getNome()+"?");
				break;
			case OBRIGATORIO_EQUIVALENCIA:
				setTextoApresentacaoDeferimentoEstagio("Prezado, "+getUsuarioLogado().getNome()+" você confirma estar de acordo com o formulário de Equivalência entregue pelo aluno(a) "+getEstagioVO().getMatriculaVO().getAluno().getNome()+"?");
				break;
			default:
				setTextoApresentacaoDeferimentoEstagio(Constantes.EMPTY);
				break;
			}
		} catch (Exception e) {
			setOncompleteModalEstagio(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarInicializacaoEstornoIndefericaoEstagio() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO((EstagioVO) Uteis.clonar(obj));
			Uteis.checkState(getEstagioVO().getSituacaoEstagioEnum().isIndeferido() && !getFacadeFactory().getEstagioFacade().realizarVerificacaoEstornoEstagioPorCargaHorario(getEstagioVO().getCodigo(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), getEstagioVO().getMatriculaVO().getMatricula()), "Não é permitido o estorno do estágio, pois o mesmo irá ultrapassar a carga horária definida na grade curricular do estágio.");
			if(getEstagioVO().getSituacaoEstagioEnum().isEmCorrecao() 
					|| getEstagioVO().getSituacaoEstagioEnum().isDeferido()
					|| (getEstagioVO().getSituacaoEstagioEnum().isIndeferido() 
							&& Uteis.isAtributoPreenchido(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao()) 
							&& !getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().getSituacaoQuestionarioRespostaOrigemEnum().isEmPreenchimento())) {
				getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setMotivo(Constantes.EMPTY);
				getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_ANALISE);
				getEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
				getEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL);
				setTextoApresentacaoEstornoEstagio("Ao realizar esta operação, esta solicitação irá retornar para o status de Aguardando Análise. Gostaria de Realizar Esta Operação?");
			}else if(getEstagioVO().getSituacaoEstagioEnum().isIndeferido() 
					&& ((Uteis.isAtributoPreenchido(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao()) && getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().getSituacaoQuestionarioRespostaOrigemEnum().isEmPreenchimento())
							|| (!Uteis.isAtributoPreenchido(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao()) && Uteis.isAtributoPreenchido(getEstagioVO().getDocumentoAssinadoVO()) 
									&& getEstagioVO().getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().allMatch(p-> p.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado())))) {
				getEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.REALIZANDO);
				getEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL);
				setTextoApresentacaoEstornoEstagio("Ao realizar esta operação, esta solicitação irá retornar para o status de Realizando. Gostaria de Realizar Esta Operação?");
			}else if(getEstagioVO().getSituacaoEstagioEnum().isIndeferido()
					&& getEstagioVO().getDocumentoAssinadoVO().isDocumentoAssinadoInvalido()) {
				getEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
				getEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE);
				getEstagioVO().setDataEnvioAssinaturaPendente(null);
				setTextoApresentacaoEstornoEstagio("Ao realizar esta operação, esta solicitação irá retornar para o status de Aguardando Assinatura Pendentes. Gostaria de Realizar Esta Operação?");
				
			}else if(getEstagioVO().getSituacaoEstagioEnum().isIndeferido()) {
				getEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
				getEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA);
				getEstagioVO().setDataEnvioAssinaturaPendente(null);
				setTextoApresentacaoEstornoEstagio("Ao realizar esta operação, esta solicitação irá retornar para o status de Aguardando Assinatura. Gostaria de Realizar Esta Operação?");
			}
			setOncompleteModalEstagio("PF('panelEstornoEstagio').show(); ");
		} catch (Exception e) {
			setOncompleteModalEstagio(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoQuestionarioRespostaOrigemVersoes() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO((EstagioVO) Uteis.clonar(obj));
			getEstagioVO().setListaQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioEstagio(getEstagioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(new QuestionarioRespostaOrigemVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarVisualizacaoFormularioVersao() {
		try {
			QuestionarioRespostaOrigemVO obj = (QuestionarioRespostaOrigemVO) getRequestMap().get(Constantes.questionarioRespostaOrigemItens);
			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getEstagioFiltro().getMatriculaVO().getMatricula(), getUsuarioLogado().getCodigoUnidadeEnsinoMatriculaLogado() , NivelMontarDados.BASICO, getUsuarioLogado());
			Uteis.checkState(!Uteis.isAtributoPreenchido(objAluno.getMatricula()), "Aluno de matrícula " + getEstagioFiltro().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			getEstagioFiltro().setMatriculaVO(objAluno);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaMatricula().equals(Constantes.EMPTY)) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaMatricula().equals(Constantes.matricula)) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaMatricula(), getEstagioFiltro().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals(Constantes.nomePessoa)) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), getEstagioFiltro().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals(Constantes.nomeCurso)) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaMatricula(), getEstagioFiltro().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals(Constantes.registroAcademico)) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaMatricula(), getEstagioFiltro().getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			setMatriculaVOs(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void selecionarAluno() {
		try {
			getEstagioFiltro().setMatriculaVO((MatriculaVO) getRequestMap().get(Constantes.matriculaItem));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void limparMatricula() {
		try {
			getEstagioFiltro().setMatriculaVO(new MatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}

	}	
	

	public void consultarDadosCurso() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals(Constantes.nome)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals(Constantes.nrRegistroInterno)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals(Constantes.nomeAreaConhecimento)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals(Constantes.nivelEducacional)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals(Constantes.unidadeEnsino)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get(Constantes.cursoItem);
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			getEstagioFiltro().getMatriculaVO().setCurso(obj);
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void limparDadosCurso() {
		try {
			getEstagioFiltro().getMatriculaVO().setCurso(new CursoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}

	}
	public void consultarDadosGradeCurricularEstagio() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals(Constantes.nome)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getGradeCurricularEstagioFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals(Constantes.curso)) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getGradeCurricularEstagioFacade().consultaRapidaPorCurso(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarGradeCurricularEstagioItem() {
		try {
			GradeCurricularEstagioVO obj = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get(Constantes.greItem);
			getEstagioFiltro().setGradeCurricularEstagioVO(obj);
			getControleConsulta().getListaConsulta().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparDadosGradeCurricularEstagio() {
		try {
			getEstagioFiltro().setGradeCurricularEstagioVO(new GradeCurricularEstagioVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public List<SelectItem> tipoConsultaComboGradeCurricularEstagio;

	public List<SelectItem> getTipoConsultaComboGradeCurricularEstagio() {
		if (tipoConsultaComboGradeCurricularEstagio == null) {
			tipoConsultaComboGradeCurricularEstagio = new ArrayList<SelectItem>(0);
			tipoConsultaComboGradeCurricularEstagio.add(new SelectItem(Constantes.nome, Constantes.Nome));
			tipoConsultaComboGradeCurricularEstagio.add(new SelectItem(Constantes.curso, Constantes.Curso));
		}
		return tipoConsultaComboGradeCurricularEstagio;
	}

	public void montarListaSelectItemGrupoPessoaItem() {
		try {
			Integer codPessoa = 0;
			getListaSelectItemGrupoPessoaItem().clear();
			if(getUsuarioLogado().isUsuarioFacilitador() && !getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				codPessoa = getUsuarioLogado().getPessoa().getCodigo();
				getEstagioFiltro().getGrupoPessoaItemVO().getPessoaVO().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
				getListaSelectItemGrupoPessoaItem().add(new SelectItem(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getPessoa().getNome()));
				return;
			}
			List<PessoaVO> lista = getFacadeFactory().getGrupoPessoaItemFacade().consultarPorPessoaGrupoPessoaItemVOExistenteEstagio(codPessoa, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			if(!getUsuarioLogado().isUsuarioFacilitador() || getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getListaSelectItemGrupoPessoaItem().add(new SelectItem(0, Constantes.EMPTY));
				getListaSelectItemGrupoPessoaItem().add(new SelectItem(1, "Todos Inativos"));
			}
			for (PessoaVO p : lista) {
				getListaSelectItemGrupoPessoaItem().add(new SelectItem(p.getCodigo(), p.getNome()));	
			}
		} catch (Exception e) {
			getListaSelectItemGrupoPessoaItem().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
//			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
//				setListaSelectItemUnidadeEnsino(new ArrayList<>());
//				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
//				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
//				getEstagioFiltro().getMatriculaVO().setUnidadeEnsino(obj);
//				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome_CNPJ()));
//				return;
//			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, Constantes.codigo, Constantes.nome_CNPJ));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void inicializarConsultaGrupoPessoaItem() {
		try {
			setEstagioVO(new EstagioVO());
			setDesabilitarGrupoPessoaItemAtual(false);
			setGrupoPessoaItemEspecifico(new GrupoPessoaItemVO());
			inicializarConsultaGrupoPessoaItemEspecifico();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void inicializarConsultaGrupoPessoaItemEspecifico() {
		try {		
			setGrupoPessoaItemFiltro(new GrupoPessoaItemVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void inicializarConsultaGrupoPessoaItemPorEstagio() {
		try {
			EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get(Constantes.estagioItens);
			setEstagioVO(getFacadeFactory().getEstagioFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			setGrupoPessoaItemAtual(new GrupoPessoaItemVO());
			setGrupoPessoaItemEspecifico(new GrupoPessoaItemVO());
			setGrupoPessoaItemFiltro(new GrupoPessoaItemVO());
			setGrupoPessoaItemAtual(getEstagioVO().getGrupoPessoaItemVO());
			setRegrasSubstituicaoGrupoPessoaItemEnum(RegrasSubstituicaoGrupoPessoaItemEnum.FACILITADOR_ESPECIFICO);
			setDesabilitarGrupoPessoaItemAtual(true);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void consultarGrupoPessoaItem() {
		try {
			super.consultar();
			getDataModeloGrupoPessoaItem().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getGrupoPessoaItemFacade().consultar(getDataModeloGrupoPessoaItem(), getGrupoPessoaItemFiltro());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getDataModeloGrupoPessoaItem().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void scrollerListenerPessoaItem() {
		try {
			
			consultarGrupoPessoaItem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	
	public void selecionarGrupoPessoaItemEspecifico() {
		try {
			GrupoPessoaItemVO obj = (GrupoPessoaItemVO) context().getExternalContext().getRequestMap().get(Constantes.grupoPessoaItens);
			setGrupoPessoaItemEspecifico(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	
	public void persistirSubstituicaoGrupoPessoaItem() {
		try {
			List<Integer> codigoFacilitadosInativados = new ArrayList<>();
			if (getGrupoPessoaItemAtual().getPessoaVO().getCodigo().equals(1)) {
				codigoFacilitadosInativados = getFacadeFactory().getEstagioFacade().consultarFacilitadosInativos();
				for(Integer grupoPessoaItemInativo : codigoFacilitadosInativados) {
					setGrupoPessoaItemAtual(getFacadeFactory().getGrupoPessoaItemFacade().consultarPorChavePrimaria(grupoPessoaItemInativo, 0, getUsuario()));
					getFacadeFactory().getEstagioFacade().persistirSubstituicaoGrupoPessoaItem(getGrupoPessoaItemAtual(), getGrupoPessoaItemEspecifico(), getRegrasSubstituicaoGrupoPessoaItemEnum(), getEstagioVO(), isInativarGrupoPessoaItemGrupoParticipante(), getUsuarioLogadoClone());	
				}
			} else {
			getFacadeFactory().getEstagioFacade().persistirSubstituicaoGrupoPessoaItem(getGrupoPessoaItemAtual(), getGrupoPessoaItemEspecifico(), getRegrasSubstituicaoGrupoPessoaItemEnum(), getEstagioVO(), isInativarGrupoPessoaItemGrupoParticipante(), getUsuarioLogadoClone());
			}
			setEstagioVO(new EstagioVO());
			setGrupoPessoaItemAtual(new GrupoPessoaItemVO());
			setGrupoPessoaItemEspecifico(new GrupoPessoaItemVO());
			montarListaSelectItemGrupoPessoaItem();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}		
	}
	
	public void limparGrupoPessoaItemEspecifico() {
		try {
			setGrupoPessoaItemEspecifico(new GrupoPessoaItemVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	/**
	 * Visao Aluno
	 */
	
	public String novoEstagioAluno() {
		try {
			novoEstagio();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public String realizarNavegacaoEstagioAguardandoAssinaturaAluno() {
		try {
			realizarVisualizacaoEstagioAguardandoAssinaturaAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void realizarVisualizacaoEstagioAguardandoAssinaturaAluno() {
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);			
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarNavegacaoEstagioRealizandoAluno() {
		try {
			realizarVisualizacaoEstagioRealizandoAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void realizarVisualizacaoEstagioRealizandoAluno() {
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.REALIZANDO);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarNavegacaoEstagioEmAnaliseAluno() {
		try {
			realizarVisualizacaoEstagioEmAnaliseAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void realizarVisualizacaoEstagioEmAnaliseAluno() {
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarNavegacaoEstagioEmCorrecaoAluno() {
		try {
			realizarVisualizacaoEstagioEmCorrecaoAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void realizarVisualizacaoEstagioEmCorrecaoAluno() {
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarNavegacaoEstagioDeferidoAluno() {
		try {
			realizarVisualizacaoEstagioDeferidoAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void realizarVisualizacaoEstagioDeferidoAluno() {		
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.DEFERIDO);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarNavegacaoEstagioIndeferidoAluno() {
		try {
			realizarVisualizacaoEstagioIndeferidoAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void realizarVisualizacaoEstagioIndeferidoAluno() {
		try {
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.INDEFERIDO);
			getDashboardEstagioVO().setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.NENHUM);
			getDashboardEstagioVO().setTipoEstagioEnum(TipoEstagioEnum.NENHUM);
			getDashboardEstagioVO().setEstagioNoPrazo(false);
			getDashboardEstagioVO().setEstagioAtrasado(false);
			setApresentarEstagioCadastro(false);
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarVisualizacaoEstagioEmAnaliseFacilitador() {
		try {
			setMatricula(new MatriculaVO());
			realizarVisualizacaoEstagioEmAnalise();
			realizarInicializacaoPermissoesParaTela();
			montarListaSelectItemGrupoPessoaItem();
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}	
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_mapaEstagioFacilitador_FORM);
	}

	public String realizarVisualizacaoEstagioEmCorrecaoFacilitador() {
		try {
			setMatricula(new MatriculaVO());
			realizarVisualizacaoEstagioEmCorrecao();
			realizarInicializacaoPermissoesParaTela();
			montarListaSelectItemGrupoPessoaItem();
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_mapaEstagioFacilitador_FORM);
	}

	public String realizarVisualizacaoEstagioDeferidoFacilitador() {
		try {			
			setMatricula(new MatriculaVO());
			realizarVisualizacaoEstagioDeferido();	
			realizarInicializacaoPermissoesParaTela();
			montarListaSelectItemGrupoPessoaItem();
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_mapaEstagioFacilitador_FORM);
	}

	public String realizarVisualizacaoEstagioIndeferidoFacilitador() {
		try {			
			setMatricula(new MatriculaVO());
			realizarVisualizacaoEstagioIndeferido();
			realizarInicializacaoPermissoesParaTela();
			montarListaSelectItemGrupoPessoaItem();
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_mapaEstagioFacilitador_FORM);
	}
	
	public void persistirEstagio() {
		try {			
			executarValidacaoSimulacaoVisaoAluno();		
			getFacadeFactory().getEstagioFacade().persistir(getEstagioVO(), false, getUsuarioLogadoClone());			
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
			realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			setApresentarEstagioCadastro(false);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);			
		}
	}

	public String persistirEstagioAprovEquiv() {
		try {			
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioEmAnaliseAprovEquiv(getEstagioVO(), getConfiguracaoEstagioObrigatorioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getEstagioVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone());
			realizarVisualizacaoEstagioEmAnaliseAluno();
			realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			setApresentarEstagioCadastro(false);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}

	
	
	public void realizarConfirmacaoEstagio() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioAssinaturaPendente(getEstagioVO(), true, getConfiguracaoEstagioObrigatorioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
			realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			setApresentarEstagioCadastro(false);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void novoEstagio() {
		try {
			setApresentarEstagioCadastro(true);
			setCaminhoPreview(Constantes.EMPTY);
			setOncompleteModal(Constantes.EMPTY);
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			setEstagioVO(new EstagioVO());
			getEstagioVO().setMatriculaVO((MatriculaVO) Uteis.clonar(getMatricula()));
			getEstagioVO().setTipoEstagio(TipoEstagioEnum.OBRIGATORIO);			
			montarlistaSelectItemGradeCurricularEstagio();
			montarListaSelectItemTipoConcedente();
			montarListaSelectItemEstado();
			selecionarGradeCurricularEstagio();
			inicializarMensagemVazia();
			getFacadeFactory().getEstagioFacade().carregarUltimoBeneficiarioEstagio(getEstagioVO());
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void alterarEstagio() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO((EstagioVO) Uteis.clonar(obj));
			setApresentarEstagioCadastro(true);
			getDashboardEstagioVO().setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			montarlistaSelectItemGradeCurricularEstagio();
			montarListaSelectItemTipoConcedente();
			montarListaSelectItemEstado();
			//selecionarGradeCurricularEstagio();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarSolicitacaoCancelarEstagio() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setEstagioVO(obj);
			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioUltimaVersaoPorEstagio(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEstagioVO(getEstagioVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}	
		
	
	public void realizarPreVisualizacaoTermoEstagio() {
		try {
			getFacadeFactory().getEstagioFacade().validarDados(getEstagioVO());
			getEstagioVO().setTipoEstagio(TipoEstagioEnum.OBRIGATORIO);
			setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + getFacadeFactory().getEstagioFacade().realizarVisualizacaoTermoEstagio(getEstagioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getEstagioVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone()));
			setOncompleteModalEstagio("PF('panelVisualizarTermoEstagio').show(); ");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setOncompleteModalEstagio(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public String realizarSolicitacaoAproveitamento() {
		try {
			GradeCurricularEstagioVO gce = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get(Constantes.gradeCurricularEstagioItens);
			if(!gce.isPermiteHorasFragmentadas()) {
				boolean existeExisteEstagio = getFacadeFactory().getEstagioFacade().realizarVerificacaoSeExisteEstagioAproveitamentoOuEquivalencia(getMatricula().getMatricula(), gce.getCodigo(), TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO);
				Uteis.checkState(existeExisteEstagio, "Já existe um estágio utilizando o Aproveitamento disponível para esse componente.");
			}
			setEstagioVO(new EstagioVO());
			getEstagioVO().setGradeCurricularEstagioVO(gce);
			getEstagioVO().setTipoSituacaoAproveitamentoEnum(TipoConsultaComboSituacaoAproveitamentoEnum.NENHUM);
			getEstagioVO().setTipoEstagio(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO);			
			getEstagioVO().setMatriculaVO((MatriculaVO) Uteis.clonar(getMatricula()));
			Integer cargaHorarioExistente = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionada(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);
			Integer cargaHorarioEspecifica = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionadaAproveitamentoOuEquivalencia(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);
			getEstagioVO().setCargaHoraria(getEstagioVO().getGradeCurricularEstagioVO().getObterCargaHorariaAproveitamentoOuEquivalencia(cargaHorarioExistente, cargaHorarioEspecifica));
			getEstagioVO().setCargaHorariaDeferida(getEstagioVO().getCargaHoraria());
			Uteis.checkState(getEstagioVO().getCargaHoraria() <= 0, "Não existe mais Carga Horária disponível para ser solicitado no Aproveitamento ou a quantidade de Horas Máximas de Aproveitamento não foi informado para o componente de estágio.");
			inicializarMensagemVazia();
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_estagioAlunoFormulario_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}
	
	public String realizarSolicitacaoEquivalencia() {
		try {
			GradeCurricularEstagioVO gce = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get(Constantes.gradeCurricularEstagioItens);
			if(!gce.isPermiteHorasFragmentadas()) {
				boolean existeExisteEstagio = getFacadeFactory().getEstagioFacade().realizarVerificacaoSeExisteEstagioAproveitamentoOuEquivalencia(getMatricula().getMatricula(), gce.getCodigo(), TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA);
				Uteis.checkState(existeExisteEstagio, "Já existe um estágio utilizando a Equivalência disponível para esse componente.");	
			}
			setEstagioVO(new EstagioVO());
			getEstagioVO().setGradeCurricularEstagioVO(gce);
			getEstagioVO().setTipoSituacaoAproveitamentoEnum(TipoConsultaComboSituacaoAproveitamentoEnum.NENHUM);
			getEstagioVO().setTipoEstagio(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA);			
			getEstagioVO().setMatriculaVO((MatriculaVO) Uteis.clonar(getMatricula()));
			Integer cargaHorarioExistente = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionada(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);
			Integer cargaHorarioEspecifica = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionadaAproveitamentoOuEquivalencia(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);
			getEstagioVO().setCargaHoraria(getEstagioVO().getGradeCurricularEstagioVO().getObterCargaHorariaAproveitamentoOuEquivalencia(cargaHorarioExistente, cargaHorarioEspecifica));
			getEstagioVO().setCargaHorariaDeferida(getEstagioVO().getCargaHoraria());
			Uteis.checkState(getEstagioVO().getCargaHoraria() <= 0, "Não existe mais Carga Horária disponível para ser solicitado na Equivalência ou a quantidade de Horas Máximas de Equivalência não foi informado para o componente de estágio.");
//			QuestionarioVO questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getEstagioVO().getGradeCurricularEstagioVO().getQuestionarioEquivalencia().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
//			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, getUsuarioLogadoClone()));
//			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEscopo(EscopoPerguntaEnum.ESTAGIO);
			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO);
			Ordenacao.ordenarLista(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().getPerguntaRespostaOrigemVOs(), Constantes.ordem);
			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEstagioVO(getEstagioVO());
			inicializarMensagemVazia();
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_estagioAlunoFormulario_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}
	
	public void realizarAssintaturaTermoEstagioPorAluno() {
		try {
			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
			setOncompleteModalEstagio("navegacaoIde('"+obj.getDocumentoAssinadoVO().getUrlAssinaturaUsuario(getUsuarioLogadoClone())+"')");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
//	public String realizarVisualizacaoFormularioAluno() {
//		try {
//			EstagioVO obj = (EstagioVO) getRequestMap().get(Constantes.estagioItens);
//			setEstagioVO(obj);
//			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioUltimaVersaoPorEstagio(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
//			if(!Uteis.isAtributoPreenchido(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao())) {
//				QuestionarioVO questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getGradeCurricularEstagioVO().getQuestionarioRelatorioFinal().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
//				getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, getUsuarioLogadoClone()));
//				getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEscopo(EscopoPerguntaEnum.ESTAGIO);
//				getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO);
//				Ordenacao.ordenarLista(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().getPerguntaRespostaOrigemVOs(), Constantes.ordem); 
//			}
//			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEstagioVO(getEstagioVO());
//			inicializarMensagemVazia();
//			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_estagioAlunoFormulario_FORM);
//		} catch (Exception e) {
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
//			return "";
//		}
//	}
	
	public String realizarPersitenciaFormularioAluno() {
		try {			
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getEstagioFacade().realizarVerificacaoPersitenciaRelatorioFinalEstagio(getEstagioVO(), getConfiguracaoEstagioObrigatorioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getEstagioVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
			return "";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}
	
	public String realizarConfirmacaoRelatorioFinalEstagio() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioEmAnalise(getEstagioVO(), getConfiguracaoEstagioObrigatorioVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getEstagioVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogadoClone());
			realizarVisualizacaoEstagioEmAnaliseAluno();
			realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio();
			setEstagioVO(new EstagioVO());
			setPage(1);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
			return "";
		}
	}
	
	public void selecionarGradeCurricularEstagio() {
		try {
			for (GradeCurricularEstagioVO p : getMatricula().getGradeCurricularAtual().getListaGradeCurricularEstagioVO()) {
				if (p.getCodigo().equals(getEstagioVO().getGradeCurricularEstagioVO().getCodigo())) {
					getEstagioVO().setGradeCurricularEstagioVO((GradeCurricularEstagioVO) Uteis.clonar(p));
					Integer cargaHorarioExistente = 0;
					if(!Uteis.isAtributoPreenchido(getEstagioVO())) {
						cargaHorarioExistente = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionada(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);							
					}else if(Uteis.isAtributoPreenchido(getEstagioVO())) {
						cargaHorarioExistente = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionada(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), getEstagioVO().getCodigo());
					}
					getEstagioVO().setCargaHoraria(getEstagioVO().getGradeCurricularEstagioVO().getCargaHorarioObrigatorio() - cargaHorarioExistente);
					getEstagioVO().getGradeCurricularEstagioVO().setInformarSupervisorEstagio(p.getInformarSupervisorEstagio());
				}
			}
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

//	public void selecionarTipoSituacaoAproveitamento() {
//		try {
//			getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioUltimaVersaoPorEstagio(getEstagioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
//			if (!Uteis.isAtributoPreenchido(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao())) {
//				Integer codigoPrm = 0;
//				if (!getEstagioVO().getTipoSituacaoAproveitamentoEnum().isNenhum()) {
//					codigoPrm = (getEstagioVO().getTipoSituacaoAproveitamentoEnum().isLicenciadoOutroCurso()) ? getEstagioVO().getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorLicenciatura().getCodigo() : getEstagioVO().getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorDocenteRegular().getCodigo();
//				}
//				if(Uteis.isAtributoPreenchido(codigoPrm)) {
//					QuestionarioVO questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(codigoPrm, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
//					getEstagioVO().setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().preencherQuestionarioRespostaOrigem(questionarioVO, getUsuarioLogadoClone()));
//					getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEscopo(EscopoPerguntaEnum.ESTAGIO);
//					getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO);
//					Ordenacao.ordenarLista(getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().getPerguntaRespostaOrigemVOs(), Constantes.ordem);	
//				}
//			}
//			getEstagioVO().getQuestionarioRespostaOrigemUltimaVersao().setEstagioVO(getEstagioVO());
//		} catch (Exception e) {
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
//		}
//	}

	public void montarlistaSelectItemGradeCurricularEstagio() {
		try {
			getListaSelectItemGradeCurricularEstagio().clear();
			if (Uteis.isAtributoPreenchido(getEstagioVO()) && Uteis.isAtributoPreenchido(getEstagioVO().getGradeCurricularEstagioVO())) {
				getListaSelectItemGradeCurricularEstagio().add(new SelectItem(getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), getEstagioVO().getGradeCurricularEstagioVO().getNome()));
			}
			for (GradeCurricularEstagioVO objExistente : getMatricula().getGradeCurricularAtual().getListaGradeCurricularEstagioVO()) {
				if ((!getDashboardEstagioVO().getSituacaoEstagioEnum().isExigido()
						|| (getDashboardEstagioVO().getSituacaoEstagioEnum().isExigido() 
								&& !getFacadeFactory().getEstagioFacade().realizarVerificacaoCargaHorarioDeferidaCompleta(getMatricula().getMatricula(), objExistente.getCodigo())))
						&& getListaSelectItemGradeCurricularEstagio().stream().noneMatch(p -> p.getValue().equals(objExistente.getCodigo()))) {
					if (getDashboardEstagioVO().getSituacaoEstagioEnum().isExigido() && !Uteis.isAtributoPreenchido(getEstagioVO().getGradeCurricularEstagioVO())) {
						getEstagioVO().setGradeCurricularEstagioVO((GradeCurricularEstagioVO) Uteis.clonar(objExistente));
						Integer cargaHorarioExistente = getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioFracionada(getMatricula().getMatricula(), getEstagioVO().getGradeCurricularEstagioVO().getCodigo(), null);
						getEstagioVO().setCargaHoraria(getEstagioVO().getGradeCurricularEstagioVO().getCargaHorarioObrigatorio() - cargaHorarioExistente);
					}
					getListaSelectItemGradeCurricularEstagio().add(new SelectItem(objExistente.getCodigo(), objExistente.getNome()));
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}

	}
	
	public void selecionarTipoConcedente() {
		try {
			if (Uteis.isAtributoPreenchido(getEstagioVO().getTipoConcedenteVO())) {
				getEstagioVO().setTipoConcedenteVO(getFacadeFactory().getTipoConcedenteFacade().consultarPorChavePrimaria(getEstagioVO().getTipoConcedenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			} else {
				getEstagioVO().setTipoConcedenteVO(new TipoConcedenteVO());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoConcedente() {
		try {
			DataModelo dataModelo = (new DataModelo(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			dataModelo.setLimitePorPagina(0);
			getFacadeFactory().getTipoConcedenteFacade().consultar(dataModelo, new TipoConcedenteVO());
			getListaSelectItemTipoConcedente().clear();
			getListaSelectItemTipoConcedente().add(new SelectItem(0, Constantes.EMPTY));
			((List<TipoConcedenteVO>) dataModelo.getListaConsulta()).stream().forEach(p -> getListaSelectItemTipoConcedente().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			getListaSelectItemTipoConcedente().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}	

	public void consultarConcedentePorCnpj() {
		try {
			if(Uteis.isAtributoPreenchido(getEstagioVO().getCnpj())) {
				ConcedenteVO obj  = getFacadeFactory().getConcedenteFacade().consultarPorCnpj(getEstagioVO().getCnpj(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
				if(Uteis.isAtributoPreenchido(obj)) {
					getEstagioVO().carregarConcedente((ConcedenteVO) Uteis.clonar(obj));	
				}else {
					obj.setTipoConcedenteVO(getEstagioVO().getTipoConcedenteVO());
					obj.setCnpj(getEstagioVO().getCnpj());
					getEstagioVO().carregarConcedente((ConcedenteVO) Uteis.clonar(obj));
				}
			}else {
				getEstagioVO().limparConcedente();
				throw new Exception("Dados não encontrado");
			}
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}	
	
	public void montarListaSelectItemEstado() {
		try {
			List<EstadoVO> lista = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemEstado().clear();
			getListaSelectItemEstado().add(new SelectItem(0, Constantes.EMPTY));
			lista.stream().forEach(p->{
				getListaSelectItemEstado().add(new SelectItem(p.getCodigo(), p.getSigla()));
			});
		} catch (Exception e) {
			getListaSelectItemTipoConcedente().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarConsultaEndereco() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoEstagio(getEstagioVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarConsultaEnderecoBeneficiario() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoEstagioBeneficiario(getEstagioVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void consultarCidade() {
		try {
			getListaConsultaCidade().clear();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getValorConsultaCidade()), getMensagemInternalizacao("msg_ParametroConsulta_informeUmParametro"));
			if (getCampoConsultaCidade().equals(Constantes.codigo)) {
				if (getValorConsultaCidade().equals(Constantes.EMPTY)) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				getListaConsultaCidade().addAll(getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado()));
			}
			if (getCampoConsultaCidade().equals(Constantes.nome)) {
				Uteis.checkState(getValorConsultaCidade().length() < 2, getMensagemInternalizacao("msg_Autor_valorConsultaVazio"));
				getListaConsultaCidade().addAll(getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado()));
			}			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaCidade().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get(Constantes.cidadeItens);
		switch (getTipoEstagioCidade()) {
		case "BENEFICIARIO":
			getEstagioVO().setCidadeBeneficiario(obj.getNome());
			getEstagioVO().setEstadoBeneficiario(obj.getEstado().getSigla());
			break;
		default:
			getEstagioVO().setCidade(obj.getNome());	
			break;
		}
		getListaConsultaCidade().clear();
		setValorConsultaCidade(Constantes.EMPTY);
		setCampoConsultaCidade(Constantes.EMPTY);
		setTipoEstagioCidade(Constantes.EMPTY);
	}
	
	public void inicializarConsultarCidadeConcedente() {
		try {
			getListaConsultaCidade().clear();
			setValorConsultaCidade(Constantes.EMPTY);
			setCampoConsultaCidade(Constantes.nome);
			setTipoEstagioCidade(Constantes.CONCEDENTE);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void inicializarConsultarCidadeBeneficiario() {
		try {
			getListaConsultaCidade().clear();
			setValorConsultaCidade(Constantes.EMPTY);
			setCampoConsultaCidade(Constantes.nome);
			setTipoEstagioCidade(Constantes.BENEFICIARIO);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void limparConsultaCidadeConcedente() {
		getEstagioVO().setCidade(Constantes.EMPTY);
	}
	
	public void limparConsultaCidadeBeneficiario() {
		getEstagioVO().setCidadeBeneficiario(Constantes.EMPTY);
		getEstagioVO().setEstadoBeneficiario(Constantes.EMPTY);
	}
	
	public boolean isCampoConsultaCidadeSomenteNumero(){
		return getCampoConsultaCidade().equals("codigo") || getCampoConsultaCidade().equals(Constantes.CODIGO) ? true: false;
	}
	
	public String getMontarScriptParaCampoConsultaCidadeSomenteNumero(){
		return isCampoConsultaCidadeSomenteNumero() ? "return somenteNumero1(event);":"";
	}
	
	
	private void carregarDadosEstagioAlunoAposOperacao(int nivelMontarDados) throws Exception {
		getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(getDashboardEstagioVO(), getMatricula().getMatricula(), getUsuarioLogadoClone());
		setListaEstagio(getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(getMatricula().getMatricula(), getDashboardEstagioVO().getSituacaoEstagioEnum(), nivelMontarDados, getUsuarioLogado()));
		setEstagioVO(new EstagioVO());
		setCaminhoPreview(Constantes.EMPTY);		
		montarlistaSelectItemGradeCurricularEstagio();
		montarListaSelectItemTipoConcedente();
		montarListaSelectItemEstado();
		inicializarMensagemVazia();
		realizarValidacaoLiberarNotificarConcedenteAssinaturaPendente();
	}
	
	
	public void realizarNavegacaoSalaAulaBlackboardPorGradeCurricularEstagio() {
		try {
			GradeCurricularEstagioVO gce = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get(Constantes.gradeCurricularEstagioItens);
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			if(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));	
			}else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));	
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(gce.getSalaAulaBlackboardVO(), sabAluno, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarNavegacaoSalaAulaBlackboardPorEstagio() {
		try {
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			if(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));	
			}else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));	
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(getMatricula().getSalaAulaBlackboardVO(), sabAluno, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	private void realizarAtualizacaoCargaHorariaDeferidaGradecurricularEstagio() throws Exception {
		if(!getMatricula().getGradeCurricularAtual().getListaGradeCurricularEstagioVO().isEmpty()) {
			Map<Integer, Integer> estagios = null;
			if(getAplicacaoControle().getMatriculaEstagioDeferidoCache().containsKey(getMatricula().getMatricula())) {
				estagios =  getAplicacaoControle().getMatriculaEstagioDeferidoCache().get(getMatricula().getMatricula());
			}else {
				estagios = getFacadeFactory().getGradeCurricularEstagioFacade().consultarCargahorariaDeferidaEstagio(getMatricula().getMatricula());		
			}
			for (GradeCurricularEstagioVO obj :getMatricula().getGradeCurricularAtual().getListaGradeCurricularEstagioVO()) {
				if(estagios.containsKey(obj.getCodigo())) {
					obj.setTotalCargaHorarioEstagio(estagios.get(obj.getCodigo()));
				}
			}
		}
	}

	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem(Constantes.nomePessoa, Constantes.Aluno));
			tipoConsultaComboAluno.add(new SelectItem(Constantes.matricula, Constantes.Matricula));
			tipoConsultaComboAluno.add(new SelectItem(Constantes.registroAcademico, Constantes.Registro_Academico));
			tipoConsultaComboAluno.add(new SelectItem(Constantes.nomeCurso, Constantes.Curso));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem(Constantes.nome, Constantes.Nome));
			tipoConsultaComboCurso.add(new SelectItem(Constantes.unidadeEnsino, Constantes.Unidade_Ensino));
		}
		return tipoConsultaComboCurso;
	}

	public String getValorConsultaMatricula() {
		if (valorConsultaMatricula == null) {
			valorConsultaMatricula = Constantes.EMPTY;
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public String getCampoConsultaMatricula() {
		if (campoConsultaMatricula == null) {
			campoConsultaMatricula = Constantes.nomePessoa;
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public List<EstagioVO> getListaEstagio() {
		if (listaEstagio == null) {
			listaEstagio = new ArrayList<>();
		}
		return listaEstagio;
	}

	public void setListaEstagio(List<EstagioVO> listaEstagio) {
		this.listaEstagio = listaEstagio;
	}

	public DashboardEstagioVO getDashboardEstagioVO() {
		if (dashboardEstagioVO == null) {
			dashboardEstagioVO = new DashboardEstagioVO();
		}
		return dashboardEstagioVO;
	}

	public void setDashboardEstagioVO(DashboardEstagioVO dashboardEstagioVO) {
		this.dashboardEstagioVO = dashboardEstagioVO;
	}

	public DashboardEstagioVO getDashboardMapaEstagioVO() {
		if (dashboardMapaEstagioVO == null) {
			dashboardMapaEstagioVO = new DashboardEstagioVO();
		}
		return dashboardMapaEstagioVO;
	}

	public void setDashboardMapaEstagioVO(DashboardEstagioVO dashboardMapaEstagioVO) {
		this.dashboardMapaEstagioVO = dashboardMapaEstagioVO;
	}

	public List<SelectItem> getListaSelectItemGrupoPessoaItem() {
		if (listaSelectItemGrupoPessoaItem == null) {
			listaSelectItemGrupoPessoaItem = new ArrayList<>();
		}
		return listaSelectItemGrupoPessoaItem;
	}

	public void setListaSelectItemGrupoPessoaItem(List<SelectItem> listaSelectItemGrupoPessoaItem) {
		this.listaSelectItemGrupoPessoaItem = listaSelectItemGrupoPessoaItem;
	}

	public EstagioVO getEstagioFiltro() {
		if (estagioFiltro == null) {
			estagioFiltro = new EstagioVO();
		}
		return estagioFiltro;
	}

	public void setEstagioFiltro(EstagioVO estagioFiltro) {
		this.estagioFiltro = estagioFiltro;
	}

	public EstagioVO getEstagioVO() {
		if (estagioVO == null) {
			estagioVO = new EstagioVO();
		}
		return estagioVO;
	}

	public void setEstagioVO(EstagioVO estagioVO) {
		this.estagioVO = estagioVO;
	}
	

	public GrupoPessoaItemVO getGrupoPessoaItemAtual() {
		if (grupoPessoaItemAtual == null) {
			grupoPessoaItemAtual = new GrupoPessoaItemVO();
		}
		return grupoPessoaItemAtual;
	}

	public void setGrupoPessoaItemAtual(GrupoPessoaItemVO grupoPessoaItemAtual) {
		this.grupoPessoaItemAtual = grupoPessoaItemAtual;
	}

	public GrupoPessoaItemVO getGrupoPessoaItemEspecifico() {
		if (grupoPessoaItemEspecifico == null) {
			grupoPessoaItemEspecifico = new GrupoPessoaItemVO();
		}
		return grupoPessoaItemEspecifico;
	}

	public void setGrupoPessoaItemEspecifico(GrupoPessoaItemVO grupoPessoaItemEspecifico) {
		this.grupoPessoaItemEspecifico = grupoPessoaItemEspecifico;
	}

	public GrupoPessoaItemVO getGrupoPessoaItemFiltro() {
		if (grupoPessoaItemFiltro == null) {
			grupoPessoaItemFiltro = new GrupoPessoaItemVO();
		}
		return grupoPessoaItemFiltro;
	}

	public void setGrupoPessoaItemFiltro(GrupoPessoaItemVO grupoPessoaItemFiltro) {
		this.grupoPessoaItemFiltro = grupoPessoaItemFiltro;
	}

	public boolean isInativarGrupoPessoaItemGrupoParticipante() {		
		return inativarGrupoPessoaItemGrupoParticipante;
	}

	public void setInativarGrupoPessoaItemGrupoParticipante(boolean inativarGrupoPessoaItemGrupoParticipante) {
		this.inativarGrupoPessoaItemGrupoParticipante = inativarGrupoPessoaItemGrupoParticipante;
	}

	public boolean isDesabilitarGrupoPessoaItemAtual() {		
		return desabilitarGrupoPessoaItemAtual;
	}

	public void setDesabilitarGrupoPessoaItemAtual(boolean desabilitarGrupoPessoaItemAtual) {
		this.desabilitarGrupoPessoaItemAtual = desabilitarGrupoPessoaItemAtual;
	}

	public RegrasSubstituicaoGrupoPessoaItemEnum getRegrasSubstituicaoGrupoPessoaItemEnum() {
		if (regrasSubstituicaoGrupoPessoaItemEnum == null) {
			regrasSubstituicaoGrupoPessoaItemEnum = RegrasSubstituicaoGrupoPessoaItemEnum.FACILITADOR_ESPECIFICO;
		}
		return regrasSubstituicaoGrupoPessoaItemEnum;
	}

	public void setRegrasSubstituicaoGrupoPessoaItemEnum(RegrasSubstituicaoGrupoPessoaItemEnum regrasSubstituicaoGrupoPessoaItemEnum) {
		this.regrasSubstituicaoGrupoPessoaItemEnum = regrasSubstituicaoGrupoPessoaItemEnum;
	}
	

	public DataModelo getDataModeloGrupoPessoaItem() {
		if (dataModeloGrupoPessoaItem == null) {
			dataModeloGrupoPessoaItem = new DataModelo();
		}
		return dataModeloGrupoPessoaItem;
	}

	public void setDataModeloGrupoPessoaItem(DataModelo dataModeloGrupoPessoaItem) {
		this.dataModeloGrupoPessoaItem = dataModeloGrupoPessoaItem;
	}

	public String getAno() {
		if (ano == null) {
			ano = Constantes.EMPTY;
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Constantes.EMPTY;
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public ConfiguracaoEstagioObrigatorioVO getConfiguracaoEstagioObrigatorioVO() {
		if (configuracaoEstagioObrigatorioVO == null) {
			configuracaoEstagioObrigatorioVO = new ConfiguracaoEstagioObrigatorioVO();
		}
		return configuracaoEstagioObrigatorioVO;
	}

	public void setConfiguracaoEstagioObrigatorioVO(ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO) {
		this.configuracaoEstagioObrigatorioVO = configuracaoEstagioObrigatorioVO;
	}

	public String getCaminhoPreview() {
		if (caminhoPreview == null) {
			caminhoPreview = Constantes.EMPTY;
		}
		return caminhoPreview;
	}

	public void setCaminhoPreview(String caminhoPreview) {
		this.caminhoPreview = caminhoPreview;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getTextoApresentacaoDeferimentoEstagio() {
		if (textoApresentacaoDeferimentoEstagio == null) {
			textoApresentacaoDeferimentoEstagio = Constantes.EMPTY;
		}
		return textoApresentacaoDeferimentoEstagio;
	}

	public void setTextoApresentacaoDeferimentoEstagio(String textoApresentacaoDeferimentoEstagio) {
		this.textoApresentacaoDeferimentoEstagio = textoApresentacaoDeferimentoEstagio;
	}

	public String getTextoApresentacaoEstornoEstagio() {
		if (textoApresentacaoEstornoEstagio == null) {
			textoApresentacaoEstornoEstagio = Constantes.EMPTY;
		}
		return textoApresentacaoEstornoEstagio;
	}

	public void setTextoApresentacaoEstornoEstagio(String textoApresentacaoEstornoEstagio) {
		this.textoApresentacaoEstornoEstagio = textoApresentacaoEstornoEstagio;
	}

	public String getCssDashboardSituacoes() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa() ? "col-md-2" : "col-md-3";
	}

	public void isVerificarPermissaoDesabilitarRespostaChecklist() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()){
				setDesabilitarRespostaChecklist(false);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_RESPONDER_CHECKLIST_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setDesabilitarRespostaChecklist(false);
			}
		} catch (Exception e) {
			setDesabilitarRespostaChecklist(true);
		}
	}

	public boolean isDesabilitarRespostaChecklist() {		
		return desabilitarRespostaChecklist;
	}

	public void setDesabilitarRespostaChecklist(boolean desabilitarRespostaChecklist) {
		this.desabilitarRespostaChecklist = desabilitarRespostaChecklist;
	}

	public void isPermiteEstornoIndeferimentoEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirEstornoIndeferimentoEstagio(true);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_ESTORNAR_INDEFERIMENTO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirEstornoIndeferimentoEstagio(true);
			}
		} catch (Exception e) {
			setPermitirEstornoIndeferimentoEstagio(false);
		}
	}

	public boolean isPermitirEstornoIndeferimentoEstagio() {
		return permitirEstornoIndeferimentoEstagio;
	}

	public void setPermitirEstornoIndeferimentoEstagio(boolean permitirEstornoIndeferimentoEstagio) {
		this.permitirEstornoIndeferimentoEstagio = permitirEstornoIndeferimentoEstagio;
	}

	public void isPermiteEstornoDeferimentoEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirEstornoDeferimentoEstagio(true);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_ESTORNAR_DEFERIMENTO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirEstornoDeferimentoEstagio(true);
			}
		} catch (Exception e) {
			setPermitirEstornoDeferimentoEstagio(false);
		}
	}

	public boolean isPermitirEstornoDeferimentoEstagio() {
		return permitirEstornoDeferimentoEstagio;
	}

	public void setPermitirEstornoDeferimentoEstagio(boolean permitirEstornoDeferimentoEstagio) {
		this.permitirEstornoDeferimentoEstagio = permitirEstornoDeferimentoEstagio;
	}

	public void isPermiteEstornoCorrecaoEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirEstornoCorrecaoEstagio(true);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_ESTORNAR_CORRECAO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirEstornoCorrecaoEstagio(true);
			}
		} catch (Exception e) {
			setPermitirEstornoCorrecaoEstagio(false);
		}
	}

	public boolean isPermitirEstornoCorrecaoEstagio() {
		return permitirEstornoCorrecaoEstagio;
	}

	public void setPermitirEstornoCorrecaoEstagio(boolean permitirEstornoCorrecaoEstagio) {
		this.permitirEstornoCorrecaoEstagio = permitirEstornoCorrecaoEstagio;
	}

	public void isPermitirDeferimentoEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirDeferirEstagio(true);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_DEFERIMENTO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirDeferirEstagio(true);	
			}
		} catch (Exception e) {
			setPermitirDeferirEstagio(false);
		}
	}

	public boolean isPermitirDeferirEstagio() {
		return permitirDeferirEstagio;
	}

	public void setPermitirDeferirEstagio(boolean permitirDeferirEstagio) {
		this.permitirDeferirEstagio = permitirDeferirEstagio;
	}

	public void isPermitirIndeferimentoEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirIndeferirEstagio(false);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_INDEFERIMENTO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirIndeferirEstagio(true);	
			}
		} catch (Exception e) {
			setPermitirIndeferirEstagio(false);
		}
	}

	public boolean isPermitirIndeferirEstagio() {
		return permitirIndeferirEstagio;
	}

	public void setPermitirIndeferirEstagio(boolean permitirIndeferirEstagio) {
		this.permitirIndeferirEstagio = permitirIndeferirEstagio;
	}

	public void isPermiteCorrigirFormularioEstagio() {
		try {
			if(getUsuarioLogado().isUsuarioFacilitador()) {
				setPermitirCorrigirFormularioEstagio(true);
			}else {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEstagioEnum.PERMITIR_CORRIGIR_FORMULARIO_ESTAGIO_OBRIGATORIO, getUsuarioLogado());
				setPermitirCorrigirFormularioEstagio(true);
			}
		} catch (Exception e) {
			setPermitirCorrigirFormularioEstagio(false);
		}
	}

	public boolean isPermitirCorrigirFormularioEstagio() {
		return permitirCorrigirFormularioEstagio;
	}

	public void setPermitirCorrigirFormularioEstagio(boolean permitirCorrigirFormularioEstagio) {
		this.permitirCorrigirFormularioEstagio = permitirCorrigirFormularioEstagio;
	}

	public String getOncompleteModalEstagio() {
		if (oncompleteModalEstagio == null) {
			oncompleteModalEstagio = Constantes.EMPTY;
		}
		return oncompleteModalEstagio;
	}

	public void setOncompleteModalEstagio(String oncompleteModalEstagio) {
		this.oncompleteModalEstagio = oncompleteModalEstagio;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public boolean isApresentarEstagioCadastro() {
		return apresentarEstagioCadastro;
	}

	public void setApresentarEstagioCadastro(boolean apresentarEstagioCadastro) {
		this.apresentarEstagioCadastro = apresentarEstagioCadastro;
	}

	public List<SelectItem> getListaSelectItemGradeCurricularEstagio() {
		if (listaSelectItemGradeCurricularEstagio == null) {
			listaSelectItemGradeCurricularEstagio = new ArrayList<>();
		}
		return listaSelectItemGradeCurricularEstagio;
	}

	public void setListaSelectItemGradeCurricularEstagio(List<SelectItem> listaSelectItemGradeCurricularEstagio) {
		this.listaSelectItemGradeCurricularEstagio = listaSelectItemGradeCurricularEstagio;
	}

	public List<SelectItem> getListaSelectItemTipoConcedente() {
		if (listaSelectItemTipoConcedente == null) {
			listaSelectItemTipoConcedente = new ArrayList<>();
		}
		return listaSelectItemTipoConcedente;
	}

	public void setListaSelectItemTipoConcedente(List<SelectItem> listaSelectItemTipoConcedente) {
		this.listaSelectItemTipoConcedente = listaSelectItemTipoConcedente;
	}

	public List<SelectItem> getListaSelectItemEstado() {
		if (listaSelectItemEstado == null) {
			listaSelectItemEstado = new ArrayList<>();
		}
		return listaSelectItemEstado;
	}

	public void setListaSelectItemEstado(List<SelectItem> listaSelectItemEstado) {
		this.listaSelectItemEstado = listaSelectItemEstado;
	}
	
	public String getTipoEstagioCidade() {
		if (tipoEstagioCidade == null) {
			tipoEstagioCidade = Constantes.EMPTY;
		}
		return tipoEstagioCidade;
	}

	public void setTipoEstagioCidade(String tipoEstagioCidade) {
		this.tipoEstagioCidade = tipoEstagioCidade;
	}

	public String getCampoConsultaCidade() {
		if(campoConsultaCidade == null) {
			campoConsultaCidade = Constantes.EMPTY;
		}
		return campoConsultaCidade;
	}
	
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}
	
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}


	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	
	public List<CidadeVO> getListaConsultaCidade() {
		if(listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>();
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}


	public List<EstagioVO> getListaEstagioParaOperacaoLote() {
		if(listaEstagioParaOperacaoLote == null) {
			listaEstagioParaOperacaoLote = new ArrayList<>();
		}
		return listaEstagioParaOperacaoLote;
	}

	public void setListaEstagioParaOperacaoLote(List<EstagioVO> listaEstagioParaOperacaoLote) {
		this.listaEstagioParaOperacaoLote = listaEstagioParaOperacaoLote;
	}

	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public OperacaoDeVinculoEstagioEnum getOperacaoDeVinculoEstagioEnum() {
		if(operacaoDeVinculoEstagioEnum == null) {
			operacaoDeVinculoEstagioEnum = OperacaoDeVinculoEstagioEnum.INDEFERIR;
		}
		return operacaoDeVinculoEstagioEnum;
	}

	public void setOperacaoDeVinculoEstagioEnum(OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum) {
		this.operacaoDeVinculoEstagioEnum = operacaoDeVinculoEstagioEnum;
	}

	public String getMotivo() {
		if(motivo == null) {
			motivo = Constantes.EMPTY;
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Boolean getPermiteAtualizarEmailNotificarAssinaturaConcedente() {
		if(permiteAtualizarEmailNotificarAssinaturaConcedente == null ) {
			permiteAtualizarEmailNotificarAssinaturaConcedente = Boolean.FALSE;
		}
		return permiteAtualizarEmailNotificarAssinaturaConcedente;
	}

	public void setPermiteAtualizarEmailNotificarAssinaturaConcedente(
			Boolean permiteAtualizarEmailNotificarAssinaturaConcedente) {
		this.permiteAtualizarEmailNotificarAssinaturaConcedente = permiteAtualizarEmailNotificarAssinaturaConcedente;
	}
	
	
	
	

	public String getEmailEnviarNotificacaoAssinaturaConcedente() {
		if(emailEnviarNotificacaoAssinaturaConcedente == null ) {
			emailEnviarNotificacaoAssinaturaConcedente =Constantes.EMPTY;
		}
		return emailEnviarNotificacaoAssinaturaConcedente;
	}

	public void setEmailEnviarNotificacaoAssinaturaConcedente(String emailEnviarNotificacaoAssinaturaConcedente) {
		this.emailEnviarNotificacaoAssinaturaConcedente = emailEnviarNotificacaoAssinaturaConcedente;
	}
	
	public String getOncompleteModalEnviarNotificacaoAssinaturaConcedente() {
		if(oncompleteModalEnviarNotificacaoAssinaturaConcedente == null ) {
			oncompleteModalEnviarNotificacaoAssinaturaConcedente =Constantes.EMPTY;
		}
		return oncompleteModalEnviarNotificacaoAssinaturaConcedente;
	}
	
	public void setOncompleteModalEnviarNotificacaoAssinaturaConcedente(String oncompleteModalEnviarNotificacaoAssinaturaConcedente) {
		this.oncompleteModalEnviarNotificacaoAssinaturaConcedente = oncompleteModalEnviarNotificacaoAssinaturaConcedente;
	}
	
	
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoConcedente() {
		if(documentoAssinadoConcedente == null ) {
			documentoAssinadoConcedente = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoConcedente;
	}

	public void setDocumentoAssinadoConcedente(DocumentoAssinadoPessoaVO documentoAssinadoConcedente) {
		this.documentoAssinadoConcedente = documentoAssinadoConcedente;
	}
	
	
	
	public void  inicializarDadosModalNotificarAssinaturaConcedente() {
		try {
			   setOncompleteModalEnviarNotificacaoAssinaturaConcedente(Constantes.EMPTY);		
			   EstagioVO objEstagio = (EstagioVO) context().getExternalContext().getRequestMap().get(Constantes.estagioItens);
			   setEstagioConcedenteAlteracaoEmailNotificacaoPendente(objEstagio);
			   DocumentoAssinadoPessoaVO obj = (DocumentoAssinadoPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.documentoAssinadoPessoaItens);
			   setEmailEnviarNotificacaoAssinaturaConcedente(Constantes.EMPTY);	
			   setPermiteAtualizarEmailNotificarAssinaturaConcedente(Boolean.FALSE);		
			   setDocumentoAssinadoConcedente(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			   getDocumentoAssinadoConcedente().setDocumentoAssinadoVO(obj.getDocumentoAssinadoVO());
			   setEmailEnviarNotificacaoAssinaturaConcedente(obj.getEmailPessoa());	
			   inicializarMensagemVazia();
			   setOncompleteModalEnviarNotificacaoAssinaturaConcedente("PF('panelNotificarConcedenteAssinaturaPendente').show(); ");
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarValidacaoLiberarNotificarConcedenteAssinaturaPendente() {		
		Iterator<EstagioVO> i = getListaEstagio().iterator();
		while (i.hasNext()) {
			EstagioVO estagio = (EstagioVO) i.next();
			Iterator<DocumentoAssinadoPessoaVO> j  = estagio.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().iterator();
			while (j.hasNext()) {
				DocumentoAssinadoPessoaVO documentoAssinadoPessoa = (DocumentoAssinadoPessoaVO)  j.next(); 
				if(documentoAssinadoPessoa.getTipoPessoa().isMembroComunidade()  && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
					//	&& documentoAssinadoPessoa.getCpfPessoa().equals(estagio.getCpfResponsavelConcedente())
						){
				documentoAssinadoPessoa.setTipoPessoaConcedente(Boolean.TRUE) ;					
				}				
			}			
		}
		
	}
	
	
	public void realizarNotificarPendenciaAssinatura() {
		try {			
			ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getDocumentoAssinadoConcedente().getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), false,getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(getDocumentoAssinadoConcedente().getDocumentoAssinadoVO(), configGedVO );	
			setOncompleteModalEnviarNotificacaoAssinaturaConcedente("PF('panelNotificarConcedenteAssinaturaPendente').hide(); ");
			setMensagemID("msg_mensagem_enviada", Uteis.SUCESSO);		
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarProcessoAlteracaoEmailNotificacaoAssinatura() {
		try {
			if (getDocumentoAssinadoConcedente().getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarProcessoAlteracaoEmailNotificacaoAssinaturaCertSign(getDocumentoAssinadoConcedente(), getEstagioConcedenteAlteracaoEmailNotificacaoPendente(), getEmailEnviarNotificacaoAssinaturaConcedente(), getUsuarioLogado());
			}
			if (getDocumentoAssinadoConcedente().getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarProcessoAlteracaoEmailNotificacaoAssinaturaTechCert(getDocumentoAssinadoConcedente(), getEstagioConcedenteAlteracaoEmailNotificacaoPendente(), getEmailEnviarNotificacaoAssinaturaConcedente(), getUsuarioLogado());
			}
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
			setOncompleteModalEnviarNotificacaoAssinaturaConcedente("PF('panelNotificarConcedenteAssinaturaPendente').hide(); ");
			setMensagemID("msg_mensagem_enviada", Uteis.SUCESSO);			
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public void realizarProcessoAlteracaoEmailNotificacaoAssinaturaTechCert() {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarProcessoAlteracaoEmailNotificacaoAssinaturaTechCert(getDocumentoAssinadoConcedente(), getEstagioConcedenteAlteracaoEmailNotificacaoPendente(), getEmailEnviarNotificacaoAssinaturaConcedente(), getUsuarioLogado());
			carregarDadosEstagioAlunoAposOperacao(Uteis.NIVELMONTARDADOS_TODOS);
			setOncompleteModalEnviarNotificacaoAssinaturaConcedente("PF('panelNotificarConcedenteAssinaturaPendente').hide(); ");
			setMensagemID("msg_mensagem_enviada", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	
	public void realizarAtualizarEmailNotificarAssinaturaConcedente() {
		setPermiteAtualizarEmailNotificarAssinaturaConcedente(Boolean.TRUE);
		setEmailEnviarNotificacaoAssinaturaConcedente(Constantes.EMPTY);
		
	}

	public EstagioVO getEstagioConcedenteAlteracaoEmailNotificacaoPendente() {
		if(estagioConcedenteAlteracaoEmailNotificacaoPendente == null ) {
			estagioConcedenteAlteracaoEmailNotificacaoPendente = new EstagioVO();
		}
		return estagioConcedenteAlteracaoEmailNotificacaoPendente;
	}

	public void setEstagioConcedenteAlteracaoEmailNotificacaoPendente(EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente) {
		this.estagioConcedenteAlteracaoEmailNotificacaoPendente = estagioConcedenteAlteracaoEmailNotificacaoPendente;
	}

	public Boolean getConsiderarEstagioAnalise() {
		if(considerarEstagioAnalise == null ) {
			considerarEstagioAnalise = Boolean.TRUE;
		}
		return considerarEstagioAnalise;
	}

	public void setConsiderarEstagioAnalise(Boolean considerarEstagioAnalise) {
		this.considerarEstagioAnalise = considerarEstagioAnalise;
	}

	public Boolean getConsiderarEstagioCorrecaoAluno() {
		if(considerarEstagioCorrecaoAluno == null ) {
			considerarEstagioCorrecaoAluno = Boolean.TRUE;
		}
		return considerarEstagioCorrecaoAluno;
	}

	public void setConsiderarEstagioCorrecaoAluno(Boolean considerarEstagioCorrecaoAluno) {
		this.considerarEstagioCorrecaoAluno = considerarEstagioCorrecaoAluno;
	}
	
	
	public void inicializarConsultaGrupoPessoaParaRedistribuicaoFacilitadores() {
		try {
			setListaGrupoFacilitadoresRedistribuir(new ArrayList<GrupoPessoaVO>(0));		
			if(!getConsiderarEstagioAnalise() && !getConsiderarEstagioCorrecaoAluno()) {
				throw new Exception("É necessário selecionar um dos filtros para realizar a consulta dos grupos de Estágios.");

			}
			List<String> situacaoEstagio = new ArrayList<String>(0);
			if(getConsiderarEstagioCorrecaoAluno()) {
				situacaoEstagio.add("'EM_CORRECAO'");
			}
			if(getConsiderarEstagioAnalise()) {
				situacaoEstagio.add("'EM_ANALISE'");
			}		
			setListaGrupoFacilitadoresRedistribuir(getFacadeFactory().getGrupoPessoaFacade().consultarGrupoPessoaAtivoAgrupandoFacilitadorQtdeEstagiosParaRedistribuicaoFacilitadoresPorSituacaoEstagio(situacaoEstagio , false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado()));
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public List<GrupoPessoaVO> getListaGrupoFacilitadoresRedistribuir() {
		if(listaGrupoFacilitadoresRedistribuir == null ) {
			listaGrupoFacilitadoresRedistribuir = new ArrayList<GrupoPessoaVO>(0);
		}
		return listaGrupoFacilitadoresRedistribuir;
	}

	public void setListaGrupoFacilitadoresRedistribuir(List<GrupoPessoaVO> listaGrupoFacilitadoresRedistribuir) {
		this.listaGrupoFacilitadoresRedistribuir = listaGrupoFacilitadoresRedistribuir;
	}
	
	public GrupoPessoaVO getGrupoFacilitadoresRedistribuir() {
		if(grupoFacilitadoresRedistribuir == null ) {
			grupoFacilitadoresRedistribuir = new GrupoPessoaVO();
		}
		return grupoFacilitadoresRedistribuir;
	}
	
	public void setGrupoFacilitadoresRedistribuir(GrupoPessoaVO grupoFacilitadoresRedistribuir) {
		this.grupoFacilitadoresRedistribuir = grupoFacilitadoresRedistribuir;
	}
	
	public void realizarRedistribuicaoGrupoFacilitadores() {
		try {
		   GrupoPessoaVO objGrupoPessoaVO = (GrupoPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.grupoFacilitadoresItens);		
		   getFacadeFactory().getGrupoPessoaFacade().realizarRedistribuicaoGrupoFacilitadores(objGrupoPessoaVO , false,  getUsuarioLogado());		
		   inicializarConsultaGrupoPessoaParaRedistribuicaoFacilitadores();
			setMensagemID("msg_dados_operacao", Uteis.SUCESSO,  true);				
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void realizarRedistribuicaoTodosGruposFacilitadores() {
		try {
		   for(GrupoPessoaVO objGrupoPessoaVO : getListaGrupoFacilitadoresRedistribuir()) {
				getFacadeFactory().getGrupoPessoaFacade().realizarRedistribuicaoGrupoFacilitadores(objGrupoPessoaVO , false,  getUsuarioLogado());		
			}
			inicializarConsultaGrupoPessoaParaRedistribuicaoFacilitadores();	
			setMensagemID("msg_dados_operacao", Uteis.SUCESSO,  true);	
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}

	public Boolean getApresentarOpcaoRedistribuirFacilitadores() {		
		return Uteis.isAtributoPreenchido(getDashboardMapaEstagioVO().getCargaHorariaEmAnalise()) || 
		Uteis.isAtributoPreenchido(getDashboardMapaEstagioVO().getCargaHorariaEmCorrecaoAluna());	
		
	}

	public void inicializarConsultarConcedente() {
		try {
			getListaConsultaCidade().clear();
			setValorConsultaCidade(Constantes.EMPTY);
			setCampoConsultaCidade(Constantes.nome);
			setTipoEstagioCidade(Constantes.BENEFICIARIO);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public List<SelectItem> getTipoConsultaConcedente() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		return itens;
	}
	
	public String getMascaraConsulta() {
		if (getCampoConsultaConcedente().equals("CNPJ")) {
			return "return mascara(this.form,'formConcedente:valorConsulta','99.999.999/9999-99',event)";
		}
		return "";
	}

	public String getCampoConsultaConcedente() {
		if(campoConsultaConcedente == null) {
			campoConsultaConcedente = "";
		}
		return campoConsultaConcedente;
	}

	public void setCampoConsultaConcedente(String campoConsultaConcedente) {
		this.campoConsultaConcedente = campoConsultaConcedente;
	}

	public String getValorConsultaConcedente() {
		if(valorConsultaConcedente == null) {
			valorConsultaConcedente = "";
		}
		return valorConsultaConcedente;
	}

	public void setValorConsultaConcedente(String valorConsultaConcedente) {
		this.valorConsultaConcedente = valorConsultaConcedente;
	}
	
	public void consultarConcedente() {
		try {
			List<ConcedenteVO> listaConsultaConcedente = new ArrayList<ConcedenteVO>();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getValorConsultaConcedente()), getMensagemInternalizacao("msg_ParametroConsulta_informeUmParametro"));
			if (getCampoConsultaConcedente().equals("CNPJ")) {
				ConcedenteVO concedenteVO = getFacadeFactory().getConcedenteFacade().consultarPorCnpj(getValorConsultaConcedente(), getEstagioVO().getTipoConcedenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
				if(Uteis.isAtributoPreenchido(concedenteVO)) {
					listaConsultaConcedente.add(concedenteVO);
					getControleConsultaOtimizado().setListaConsulta(listaConsultaConcedente);
				} else {
					throw new Exception("Dados não encontrado");
				}
			}
			if (getCampoConsultaConcedente().equals("nome")) {
				Uteis.checkState(getValorConsultaConcedente().length() < 2, getMensagemInternalizacao("msg_Autor_valorConsultaVazio"));
				listaConsultaConcedente = getFacadeFactory().getConcedenteFacade().consultarPorNome(getValorConsultaConcedente(), getEstagioVO().getTipoConcedenteVO().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getControleConsultaOtimizado(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
				getControleConsultaOtimizado().setListaConsulta(listaConsultaConcedente);
			}			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {                                                                                    
			getListaConsultaCidade().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), e, getUsuarioLogado(), "", "", true);
		}
	}
	
	public void scrollerListener() throws Exception {
        
                
        consultarConcedente();
    }
	
	public void selecionarConcedente() {
		ConcedenteVO obj = (ConcedenteVO) context().getExternalContext().getRequestMap().get("concedenteItens");
		if(Uteis.isAtributoPreenchido(obj)) {
			getEstagioVO().carregarConcedente((ConcedenteVO) Uteis.clonar(obj));	
		}else {
			obj.setTipoConcedenteVO(getEstagioVO().getTipoConcedenteVO());
			obj.setCnpj(getEstagioVO().getCnpj());
			getEstagioVO().carregarConcedente((ConcedenteVO) Uteis.clonar(obj));
		}
		getControleConsultaOtimizado().getListaConsulta().clear();
		setValorConsultaConcedente(Constantes.EMPTY);
		setCampoConsultaConcedente(Constantes.EMPTY);
	}

	public Boolean getPermiteIncluirEstagioAluno() {
		if (permiteIncluirEstagioAluno == null) {
			permiteIncluirEstagioAluno = false;
		}
		return permiteIncluirEstagioAluno;
	}

	public void setPermiteIncluirEstagioAluno(Boolean permiteIncluirEstagioAluno) {
		this.permiteIncluirEstagioAluno = permiteIncluirEstagioAluno;
	}

	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		if (provedorDeAssinaturaEnum == null) {
			provedorDeAssinaturaEnum = ProvedorDeAssinaturaEnum.SEI;
		}
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}
}
