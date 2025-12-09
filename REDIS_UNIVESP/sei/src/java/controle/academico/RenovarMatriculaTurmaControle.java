package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * matriculaForm.jsp matriculaCons.jsp) com as funcionalidades da classe <code>Matricula</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Matricula
 * @see MatriculaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaMatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.RenovacaoMatriculaTurma;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("RenovarMatriculaTurmaControle")
@Scope("viewScope")
@Lazy
public class RenovarMatriculaTurmaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO;

	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;

	private List<SelectItem> listaSelectItemUnidadeEnsino;	
//	private List<SelectItem> listaSelectItemPlanoFinanceiroCursoAtual;
//	private List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual;

	private List<SelectItem> listaSelectItemPeriodoLetivoMatricula;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemProcessoMatricula;
	private List<SelectItem> listaSelectItemGradeCurricular;
//	private List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar;

	private String apresentarRichModal;
	private Boolean selecionarTudo;
	private Boolean apresentarModalConsultarMatriculaPeriodo;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	private boolean permitirRealizarMatriculaDisciplinaPreRequisito = false;
	private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;
	private List<GradeDisciplinaVO> gradeDisciplinaVOs;
	private GradeDisciplinaVO gradeDisciplinaSelecionadaVO;
	private boolean abrirPainelGradeDisciplina = false;
	private boolean abrirPainelGradeDisciplinaComposta = false;
	private ProgressBarVO progressBarConsulta;
	private ProgressBarVO progressBarRenovacao;
	
	public RenovarMatriculaTurmaControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Matricula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setApresentarModalConsultarMatriculaPeriodo(false);
		setRenovacaoMatriculaTurmaVO(new RenovacaoMatriculaTurmaVO());
		getRenovacaoMatriculaTurmaVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
		getRenovacaoMatriculaTurmaVO().getResponsavel().setNome(getUsuarioLogado().getNome());
		getRenovacaoMatriculaTurmaVO().setData(new Date());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaTurmaForm.xhtml");
	}

	//@PostConstruct
	public String navegarConsultar() {
		setControleConsultaOtimizado(new DataModelo());
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setOffset(0);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultarPorSituacao(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), 10, 0));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultarTotalRegistroPorSituacao(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaTurmaCons.xhtml");
	}

	public void consultarRenovacaoMatriculaTurma() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));			
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultarTotalRegistro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarRenovacaoMatriculaTurma();
	}

	public String editar() throws Exception {
		setRenovacaoMatriculaTurmaVO((RenovacaoMatriculaTurmaVO) context().getExternalContext().getRequestMap().get("renovacaoMatriculaTurmaItens"));
		realizarEdicaoDadosRenovacaoTurma();
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaTurmaForm.xhtml");
	}

	public void realizarEdicaoDadosRenovacaoTurma() throws Exception {
		setApresentarModalConsultarMatriculaPeriodo(false);
		setApresentarRichModal("");
		getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), 0, 0));
		getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), 0, 0));
		getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), 0, 0));		
		getListaSelectItemUnidadeEnsino().clear();
//		getListaSelectItemPlanoFinanceiroCursoAtual().clear();
//		getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual().clear();
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemProcessoMatricula().clear();
//		getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().clear();
		getListaSelectItemTurma().clear();

		getListaSelectItemUnidadeEnsino().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getNome()));
//		getListaSelectItemPlanoFinanceiroCursoAtual().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getPlanoFinanceiroCursoAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getPlanoFinanceiroCursoAtual().getDescricao()));
//		getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getCondicaoPagamentoPlanoFinanceiroCursoAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getCondicaoPagamentoPlanoFinanceiroCursoAtual().getDescricao()));
		getListaSelectItemGradeCurricular().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getNome()));
//		getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getCodigo(), getRenovacaoMatriculaTurmaVO().getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getDescricao()));
		getListaSelectItemProcessoMatricula().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getProcessoMatriculaRenovar().getCodigo(), getRenovacaoMatriculaTurmaVO().getProcessoMatriculaRenovar().getDescricao()));
		getListaSelectItemTurma().add(new SelectItem(getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getCodigo(), getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getIdentificadorTurma()));
		montarListaSelectItemPeriodoLetivo();
		
		if(getRenovacaoMatriculaTurmaVO().getApresentarAcompanhamentoProcessamento()){
			getProgressBarRenovacao().resetar();
			getProgressBarRenovacao().iniciar(Long.valueOf((getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoGerada()+getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoErro())), getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada(),  getRenovacaoMatriculaTurmaVO().getLabelProcessamento(), false, null, null);
		}
	}

	public void processarSelecionarTudo() {
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : getRenovacaoMatriculaTurmaVO().getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs()) {
			obj.setSelecionado(getSelecionarTudo());
		}
	}

	public void gravar() {
		try {
			getProgressBarRenovacao().resetar();
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarComThrows();
			executarGeracaoGradeDisciplinaCompostaIncluir();
			getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarInicializacaoProcessamento(getRenovacaoMatriculaTurmaVO(), getAplicacaoControle(), true, getUsuarioLogado(), getGradeDisciplinaCompostaVOs());
			getProgressBarRenovacao().iniciar(0l, getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada(), getRenovacaoMatriculaTurmaVO().getLabelProcessamento(), false, null, "");
		} catch (Exception e) {
			setApresentarRichModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public void realizarTerminoProcessamento() {
		try {
			realizarEdicaoDadosRenovacaoTurma();
			setApresentarRichModal("RichFaces.$('modalProcessamento').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setApresentarRichModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}
	
	public void realizarInterrupcaoProcessamento() {
		try {
			getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarInterrupcaoProcessamento(getRenovacaoMatriculaTurmaVO(), getAplicacaoControle(), true, getUsuarioLogado());
			getProgressBarRenovacao().setForcarEncerramento(true);
			realizarEdicaoDadosRenovacaoTurma();
			setApresentarRichModal("RichFaces.$('modalProcessamento').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setApresentarRichModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public void realizarInterrupcaoProcessamentoCons() {
		try {
			getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarInterrupcaoProcessamento((RenovacaoMatriculaTurmaVO) context().getExternalContext().getRequestMap().get("renovacaoMatriculaTurmaItens"), getAplicacaoControle(), true, getUsuarioLogado());
			realizarEdicaoDadosRenovacaoTurma();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public void consultarStatusProcessamento() {
		try {
			if (getRenovacaoMatriculaTurmaVO().getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)) {
				getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarAtualizacaoDadosProcessamento(getRenovacaoMatriculaTurmaVO(), getUsuarioLogado());
				getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado(), 0, 0));
				getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado(), 10, 0));
				getRenovacaoMatriculaTurmaVO().setRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(getRenovacaoMatriculaTurmaVO().getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado(), 10, 0));
				if (getRenovacaoMatriculaTurmaVO().getApresentarAcompanhamentoProcessamento()) {
					setApresentarRichModal("");
					getProgressBarRenovacao().setProgresso(Long.valueOf((getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoGerada()+getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoErro())));
					getProgressBarRenovacao().setStatus(getRenovacaoMatriculaTurmaVO().getLabelProcessamento());
				} else {
					realizarEdicaoDadosRenovacaoTurma();
					getProgressBarRenovacao().setForcarEncerramento(true);
					setApresentarRichModal("RichFaces.$('modalProcessamento').hide()");
				}
			} else {
				getProgressBarRenovacao().setForcarEncerramento(true);
				setApresentarRichModal("RichFaces.$('modalProcessamento').hide()");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void consultarStatusProcessamentoCons(){
		getLabelProcessamentoCons();
	}

	public String getLabelProcessamentoCons() {
		try {
			RenovacaoMatriculaTurmaVO obj = (RenovacaoMatriculaTurmaVO) context().getExternalContext().getRequestMap().get("renovacaoMatriculaTurmaItens");
			if (obj.getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)) {
				getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarAtualizacaoDadosProcessamento(obj, getUsuarioLogado());
				if (obj.getApresentarAcompanhamentoProcessamento()) {					
					return obj.getLabelProcessamento();
				}else{
					obj.setForcarEncerramento(true);
				}
			}else{
				obj.setForcarEncerramento(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void validarDadosInicioProcessamento() {
		try {
			getFacadeFactory().getRenovacaoMatriculaTurmaFacade().validarDadosInicioProcessamento(getRenovacaoMatriculaTurmaVO(), getAplicacaoControle());
			setApresentarRichModal("RichFaces.$('panelSelecionarDados').show();");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setApresentarRichModal("");
		}
	}

	public void montarListaProcessoMatricula() {
		List<ProcessoMatriculaVO> processoMatriculaVOs = null;
		try {
			getListaSelectItemProcessoMatricula().clear();
			String anoBase = getRenovacaoMatriculaTurmaVO().getAno();
			String semestreBase = getRenovacaoMatriculaTurmaVO().getSemestre();
			if (getRenovacaoMatriculaTurmaVO().getSemestre().equals("2")) {
				anoBase = "" + (Integer.valueOf(getRenovacaoMatriculaTurmaVO().getAno()) + 1);
				semestreBase = "1";
			}else if (getRenovacaoMatriculaTurmaVO().getSemestre().equals("1")) {
				semestreBase = "2";
			} 
			Integer unidadeEnsino = getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo():0;
			Integer curso = getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo():0;
			processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(getRenovacaoMatriculaTurmaVO().getTurmaVO().getTurno().getCodigo(), curso, unidadeEnsino, "AT", "PR_AT", anoBase, semestreBase, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), TipoAlunoCalendarioMatriculaEnum.VETERANO);			
			if (Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO()) && !processoMatriculaVOs.isEmpty()) {
				getRenovacaoMatriculaTurmaVO().setProcessoMatriculaRenovar(processoMatriculaVOs.get(0));			
			} else {
				getRenovacaoMatriculaTurmaVO().setProcessoMatriculaRenovar(null);
				getListaSelectItemProcessoMatricula().add(new SelectItem(0, ""));
			}
			processoMatriculaVOs.stream().forEach(p-> {
				getListaSelectItemProcessoMatricula().add(new SelectItem(p.getCodigo(), p.getDescricao()));	
			});
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(processoMatriculaVOs);
		}
		
	}

	public void processarDadosPermitinentesTurmaSelecionada() {
		if (getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getCodigo() > 0) {
			try {
				getRenovacaoMatriculaTurmaVO().setTurmaRenovar(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//				getRenovacaoMatriculaTurmaVO().setPlanoFinanceiroCursoRenovar(getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getPlanoFinanceiroCurso());
//				montarListaSelectItemPlanoFinanceiroCurso();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		} else {
//			getRenovacaoMatriculaTurmaVO().setPlanoFinanceiroCursoRenovar(null);
//			getRenovacaoMatriculaTurmaVO().setCondicaoPagamentoPlanoFinanceiroCursoRenovar(null);
//			getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().clear();
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivo</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemGradeCurricular() {
		List<GradeCurricularVO> resultadoConsulta = null;
		getListaSelectItemGradeCurricular().clear();
		try {
			resultadoConsulta = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtualFiltrarRenovacaoTurmaNivelCombobox(getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getAno(), getRenovacaoMatriculaTurmaVO().getSemestre());
			if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO()) && !resultadoConsulta.isEmpty()) {
				getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().setCodigo(resultadoConsulta.get(0).getCodigo());
			}else {
				getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().setCodigo(0);
				getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
			}
			resultadoConsulta.stream().forEach(p->{
				getListaSelectItemGradeCurricular().add(new SelectItem(p.getCodigo(), p.getNome()));
			});
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}

	}
//
//	public void montarListaSelectItemPlanoFinanceiroCurso() {
//		List<CondicaoPagamentoPlanoFinanceiroCursoVO> resultadoConsulta = null;
//		try {
//			getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().clear();
//			if (getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getCodigo() > 0) {
//				resultadoConsulta = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getPlanoFinanceiroCurso().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());				
//				getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().add(new SelectItem(0, ""));
//				resultadoConsulta.stream().forEach(p->{
//					getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().add(new SelectItem(p.getCodigo(), p.getDescricao()));	
//				});
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//		}
//	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() {
		List<PeriodoLetivoVO> periodoLetivoVOs = null;
		try {
			getListaSelectItemPeriodoLetivoMatricula().clear();
			if(!Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO())) {
				getListaSelectItemPeriodoLetivoMatricula().add(new SelectItem(0, ""));	
			}
			if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo())) {
				periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				for (PeriodoLetivoVO periodoLetivoVO : periodoLetivoVOs) {
					if (periodoLetivoVO.getCodigo().intValue() == getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().getCodigo().intValue()) {
						periodoLetivoVOs.stream().forEach(p->{
							getListaSelectItemPeriodoLetivoMatricula().add(new SelectItem(p.getCodigo(), p.getDescricao()));
						});
						return;
					}
				}
				getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().setCodigo(0);
				if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO())) {
					for (PeriodoLetivoVO periodoLetivoVO : periodoLetivoVOs) {
						if (periodoLetivoVO.getPeriodoLetivo() > getRenovacaoMatriculaTurmaVO().getTurmaVO().getPeridoLetivo().getPeriodoLetivo()) {
							getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().setCodigo(periodoLetivoVO.getCodigo());
							break;
						}
					}
					if (!periodoLetivoVOs.isEmpty() && getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().getCodigo() == 0) {
						getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().setCodigo(periodoLetivoVOs.get(periodoLetivoVOs.size() - 1).getCodigo());
					}	
				}
				periodoLetivoVOs.stream().forEach(p->{
					getListaSelectItemPeriodoLetivoMatricula().add(new SelectItem(p.getCodigo(), p.getDescricao()));
				});				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(periodoLetivoVOs);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turma</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurma() {
		List<TurmaVO> listaConsultaTurma = null;
		try {
			getListaSelectItemTurma().clear();
			if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().getCodigo())) {
				getRenovacaoMatriculaTurmaVO().setPeriodoLetivoRenovar(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
				Integer unidadeEnsino = getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo():0;
				Integer curso = getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo():0;
				listaConsultaTurma = getFacadeFactory().getTurmaFacade().consultaRapidaPorPeriodoLetivoUnidadeEnsinoCursoTurno(getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().getCodigo(), unidadeEnsino, curso, getRenovacaoMatriculaTurmaVO().getTurmaVO().getTurno().getCodigo(), false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO()) && !listaConsultaTurma.isEmpty()) {
					getRenovacaoMatriculaTurmaVO().getTurmaRenovar().setCodigo(listaConsultaTurma.get(0).getCodigo());
					getRenovacaoMatriculaTurmaVO().getTurmaRenovar().setNivelMontarDados(NivelMontarDados.BASICO);
					processarDadosPermitinentesTurmaSelecionada();					
				}else {
//					getRenovacaoMatriculaTurmaVO().setPlanoFinanceiroCursoRenovar(null);
//					getRenovacaoMatriculaTurmaVO().setCondicaoPagamentoPlanoFinanceiroCursoRenovar(null);
//					getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().clear();
				}
				setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(listaConsultaTurma, "codigo", "identificadorTurma", false));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(listaConsultaTurma);
		}
	}

	public void realizarValidacaoDadosConsultarMatriculaPeriodoRenovar() {
		try {
			getProgressBarConsulta().resetar();
			setApresentarRichModal("");
			getRenovacaoMatriculaTurmaVO().setQtdeRenovacaoAGerada(0);
			getRenovacaoMatriculaTurmaVO().setQtdeRenovacaoGerada(0);
			getRenovacaoMatriculaTurmaVO().setQtdeRenovacaoErro(0);
			setApresentarModalConsultarMatriculaPeriodo(false);
			RenovacaoMatriculaTurma.validarDados(getRenovacaoMatriculaTurmaVO());
			executarVerificacaoGradeDisciplinaConfiguradaEstudarQuantidadeComposta();
			if (isAbrirPainelGradeDisciplina()) {
				return;
			}
			realizarInicializacaoDadosAdicionais();
			executarGeracaoGradeDisciplinaCompostaIncluir();
			if(Uteis.isAtributoPreenchido(getRenovacaoMatriculaTurmaVO().getTurmaVO())) {
				getProgressBarConsulta().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarConsulta().iniciar(0l, 10000, "Consultando Alunos...", true, this, "consultarMatriculaPeriodoRenovarPorTurma");			
				setApresentarModalConsultarMatriculaPeriodo(true);
			}else {
				consultarMatriculaPeriodoRenovar();
			}
		} catch (Exception e) {
			//setApresentarRichModal("RichFaces.$('panelConsultarMatriculaPeriodo').hide()");
			setApresentarModalConsultarMatriculaPeriodo(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarInicializacaoDadosAdicionais() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Matricula_PermitirRealizarMatriculaDisciplinaPreRequisito", getUsuarioLogado());
			permitirRealizarMatriculaDisciplinaPreRequisito = Boolean.TRUE;
		} catch (Exception e) {
			permitirRealizarMatriculaDisciplinaPreRequisito = false;
		}
		setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo()));
		setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo()));
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * MatriculaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarMatriculaPeriodoRenovar() {
		try {
			setApresentarRichModal("");
			limparMensagem();
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarConsultarMatriculaPeriodoAptaRenovar(getRenovacaoMatriculaTurmaVO(), getConfiguracaoFinanceiroVO(), getConfiguracaoGeralSistemaVO(), isPermitirRealizarMatriculaDisciplinaPreRequisito(), getProgressBarConsulta().getUsuarioVO(), getGradeDisciplinaCompostaVOs());
			if (getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada() == 0) {
				throw new Exception("Não foram encontradas matrículas aptas a serem renovadas através dos filtros selecionados");
			}
			if (!getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado()
					&& getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() > 0) {
				setApresentarRichModal("RichFaces.$('panelApresentarAvisoAlunoReprovado').show(); ");
				//RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			} else {
				//setApresentarRichModal("RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getProgressBarConsulta().setForcarEncerramento(true);
			//setApresentarRichModal("RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setApresentarModalConsultarMatriculaPeriodo(false);
		}
	}
	
	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * MatriculaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarMatriculaPeriodoRenovarPorTurma() {
		try {
			setApresentarRichModal("");
			limparMensagem();
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarConsultarMatriculaPeriodoAptaRenovarPorTurma(getRenovacaoMatriculaTurmaVO(), getConfiguracaoFinanceiroVO(), getConfiguracaoGeralSistemaVO(), isPermitirRealizarMatriculaDisciplinaPreRequisito(), getProgressBarConsulta().getUsuarioVO(), getGradeDisciplinaCompostaVOs(), getProgressBarConsulta());
			if (getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada() == 0) {
				throw new Exception("Não foram encontradas matrículas aptas a serem renovadas através dos filtros selecionados");
			}
			if (!getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado()
					&& getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() > 0) {
				setApresentarRichModal("RichFaces.$('panelApresentarAvisoAlunoReprovado').show(); ");
				//RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			} else {
				//setApresentarRichModal("RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getProgressBarConsulta().setForcarEncerramento(true);
			//setApresentarRichModal("RichFaces.$('panelConsultarMatriculaPeriodo').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setApresentarModalConsultarMatriculaPeriodo(false);
		}
	}

	public void realizarAlteracaoCondicaoPagamento() {
		try {
			limparMensagem();
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarComThrows();
			executarGeracaoGradeDisciplinaCompostaIncluir();
			RenovacaoMatriculaTurmaMatriculaPeriodoVO obj = (RenovacaoMatriculaTurmaMatriculaPeriodoVO) getRequestMap().get("matriculaPeriodoNaoRealizadoItens");
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().realizarDefinicaoDadosFinanceiroMatriculaPeriodo(getRenovacaoMatriculaTurmaVO(), obj, 
					obj.getMatriculaPeriodoVO().getMatriculaVO(), obj.getNovaMatriculaPeriodoVO(), getConfiguracaoFinanceiroVO(), 
					getConfiguracaoGeralSistemaVO(), isPermitirRealizarMatriculaDisciplinaPreRequisito(),
					getUsuarioLogado(), true, getGradeDisciplinaCompostaVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}
	
	public void consultarDadosCurso() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			getRenovacaoMatriculaTurmaVO().setCursoVO(obj);
			inicializarFiltrosRelacionadoTurmaOrigem();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosCurso() {
		try {
			getRenovacaoMatriculaTurmaVO().setCursoVO(new CursoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void selecionarUnidadeEnsino() {
		try {
			getRenovacaoMatriculaTurmaVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			inicializarFiltrosRelacionadoTurmaOrigem();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome_CNPJ"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
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
	
	public List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		}
		return tipoConsultaComboCurso;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeensino", "Unidade Ensino"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("aluno", "Aluno"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboDebitoFinanceiro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("somenteAdimplentes", "SOMENTE ADIMPLENTES"));
		itens.add(new SelectItem("todos", "TODOS"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAprovadoSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("somenteapro", "SOMENTE APROVADOS"));
		itens.add(new SelectItem("todos", "TODOS"));
		return itens;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List<TurmaVO> consultarTurmaPorIdentificadorTurma(String identificadorTurmaPrm) throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(identificadorTurmaPrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
	}

	@SuppressWarnings("rawtypes")
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
//			novo();
			getRenovacaoMatriculaTurmaVO().setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			consultarTurmaPorChavePrimaria();
		} catch (Exception e) {
			novo();
			inicializarFiltrosRelacionadoTurmaOrigem();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			String identificadorTurma = getRenovacaoMatriculaTurmaVO().getTurmaVO().getIdentificadorTurma();
			//novo();
			getRenovacaoMatriculaTurmaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getRenovacaoMatriculaTurmaVO().getTurmaVO(), identificadorTurma, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getRenovacaoMatriculaTurmaVO().getTurmaVO().getTurmaAgrupada()) {
				throw new Exception("Não é possível realizar a renovação de uma turma agrupada.");
			}
			if (getRenovacaoMatriculaTurmaVO().getTurmaVO().getSubturma()) {
				throw new Exception("Não é possível realizar a renovação de uma subturma.");
			}
			getRenovacaoMatriculaTurmaVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			getRenovacaoMatriculaTurmaVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			inicializarFiltrosRelacionadoTurmaOrigem();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			novo();
			inicializarFiltrosRelacionadoTurmaOrigem();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurma() throws Exception {
		novo();
		inicializarFiltrosRelacionadoTurmaOrigem();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivoMatricula
	 */
	public List<SelectItem> getListaSelectItemPeriodoLetivoMatricula() {
		if (listaSelectItemPeriodoLetivoMatricula == null) {
			listaSelectItemPeriodoLetivoMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivoMatricula;
	}

	/**
	 * @param listaSelectItemPeriodoLetivoMatricula
	 *            the listaSelectItemPeriodoLetivoMatricula to set
	 */
	public void setListaSelectItemPeriodoLetivoMatricula(List<SelectItem> listaSelectItemPeriodoLetivoMatricula) {
		this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the listaSelectItemProcessoMatricula
	 */
	public List<SelectItem> getListaSelectItemProcessoMatricula() {
		if (listaSelectItemProcessoMatricula == null) {
			listaSelectItemProcessoMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProcessoMatricula;
	}

	/**
	 * @param listaSelectItemProcessoMatricula
	 *            the listaSelectItemProcessoMatricula to set
	 */
	public void setListaSelectItemProcessoMatricula(List<SelectItem> listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	/**
	 * @return the listaSelectItemGradeCurricular
	 */
	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	/**
	 * @param listaSelectItemGradeCurricular
	 *            the listaSelectItemGradeCurricular to set
	 */
	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

//	public List<SelectItem> getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar() {
//		if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar == null) {
//			listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar = new ArrayList<SelectItem>(0);
//		}
//		return listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar;
//	}
//
//	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar(List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar) {
//		this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar = listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar;
//	}
//
//	public void limparCondicaoPagamentoRenovar() {
//		getRenovacaoMatriculaTurmaVO().setCondicaoPagamentoPlanoFinanceiroCursoRenovar(null);
//	}

	/**
	 * @return the apresentarRichModal
	 */
	public String getApresentarRichModal() {
		if (apresentarRichModal == null) {
			apresentarRichModal = "";
		}
		return apresentarRichModal;
	}

	/**
	 * @param apresentarRichModal
	 *            the apresentarRichModal to set
	 */
	public void setApresentarRichModal(String apresentarRichModal) {
		this.apresentarRichModal = apresentarRichModal;
	}

	/**
	 * @return the selecionarTudo
	 */
	public Boolean getSelecionarTudo() {
		if (selecionarTudo == null) {
			selecionarTudo = Boolean.TRUE;
		}
		return selecionarTudo;
	}

	/**
	 * @param selecionarTudo
	 *            the selecionarTudo to set
	 */
	public void setSelecionarTudo(Boolean selecionarTudo) {
		this.selecionarTudo = selecionarTudo;
	}

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public Boolean getApresentarCampoSemestre() {
		return getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getPeriodicidade().equals("SE") && !getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo().equals(0)
				|| getRenovacaoMatriculaTurmaVO().getCursoVO().getPeriodicidade().equals("SE") && !getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo().equals(0);
	}

	public Boolean getApresentarCampoAno() {
		return ((getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getPeriodicidade().equals("SE")) && !getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo().equals(0))
				|| ((getRenovacaoMatriculaTurmaVO().getCursoVO().getPeriodicidade().equals("AN") || getRenovacaoMatriculaTurmaVO().getCursoVO().getPeriodicidade().equals("SE")) && !getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo().equals(0));
	}

	public RenovacaoMatriculaTurmaVO getRenovacaoMatriculaTurmaVO() {
		if (renovacaoMatriculaTurmaVO == null) {
			renovacaoMatriculaTurmaVO = new RenovacaoMatriculaTurmaVO();
		}
		return renovacaoMatriculaTurmaVO;
	}

	public void setRenovacaoMatriculaTurmaVO(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO) {
		this.renovacaoMatriculaTurmaVO = renovacaoMatriculaTurmaVO;
	}

	public RenovacaoMatriculaTurmaMatriculaPeriodoVO gerarRenovacaoMatriculaTurmaMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		RenovacaoMatriculaTurmaMatriculaPeriodoVO obj = new RenovacaoMatriculaTurmaMatriculaPeriodoVO();
		obj.setMatriculaPeriodoVO(matriculaPeriodoVO);
		return obj;
	}	

//	public List<SelectItem> getListaSelectItemPlanoFinanceiroCursoAtual() {
//		if (listaSelectItemPlanoFinanceiroCursoAtual == null) {
//			listaSelectItemPlanoFinanceiroCursoAtual = new ArrayList<SelectItem>(0);
//		}
//		return listaSelectItemPlanoFinanceiroCursoAtual;
//	}
//
//	public void setListaSelectItemPlanoFinanceiroCursoAtual(List<SelectItem> listaSelectItemPlanoCursoAtual) {
//		this.listaSelectItemPlanoFinanceiroCursoAtual = listaSelectItemPlanoCursoAtual;
//	}
//
//	public List<SelectItem> getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual() {
//		if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual == null) {
//			listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual = new ArrayList<SelectItem>(0);
//		}
//		return listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual;
//	}
//
//	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual(List<SelectItem> listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual) {
//		this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual = listaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual;
//	}	

//	public void montarListaSelectItemPlanoFinanceiroCursoAtual() {
//		try {
//			Integer unidadeEnsino = getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo():0;
//			Integer curso = getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo():0;
//			List<PlanoFinanceiroCursoVO> planoFinanceiroCursoVOs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(unidadeEnsino, curso, getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getAno(), getRenovacaoMatriculaTurmaVO().getSemestre());
//			setListaSelectItemPlanoFinanceiroCursoAtual(UtilSelectItem.getListaSelectItem(planoFinanceiroCursoVOs, "codigo", "descricao", true));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}
//
//	public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual() {
//		try {
//			Integer unidadeEnsino = getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getUnidadeEnsino().getCodigo():0;
//			Integer curso = getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() : getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo() > 0 ? getRenovacaoMatriculaTurmaVO().getTurmaVO().getCurso().getCodigo():0;
//			List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarCondicaoPagamentoPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(unidadeEnsino, curso, getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo(), getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getPlanoFinanceiroCursoAtual().getCodigo(), getRenovacaoMatriculaTurmaVO().getAno(), getRenovacaoMatriculaTurmaVO().getSemestre());
//			setListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual(UtilSelectItem.getListaSelectItem(condicaoPagamentoPlanoFinanceiroCursoVOs, "codigo", "descricao", true));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//	}

	public void inicializarFiltrosRelacionadoTurmaOrigem() {
		try {
			if (getRenovacaoMatriculaTurmaVO().getUnidadeEnsinoVO().getCodigo() > 0 || getRenovacaoMatriculaTurmaVO().getCursoVO().getCodigo() > 0 || getRenovacaoMatriculaTurmaVO().getTurmaVO().getCodigo() > 0) {
				montarListaSelectItemGradeCurricular();
				montarListaProcessoMatricula();
				montarListaSelectItemTurma();
			} else {
				getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().setCodigo(0);
				getRenovacaoMatriculaTurmaVO().setProcessoMatriculaRenovar(null);
				getListaSelectItemGradeCurricular().clear();
				getListaSelectItemProcessoMatricula().clear();
			}
			inicializarFiltrosRelacionadoGradeCurricularAtual();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void inicializarFiltrosRelacionadoGradeCurricularAtual() {
		try {
			if (getRenovacaoMatriculaTurmaVO().getGradeCurricularAtual().getCodigo() > 0) {
				montarListaSelectItemPeriodoLetivo();
				montarListaSelectItemTurma();
//				montarListaSelectItemPlanoFinanceiroCursoAtual();
//				montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual();
				getRenovacaoMatriculaTurmaVO().setQtdeRenovacaoAGerada(0);
			} else {
				getRenovacaoMatriculaTurmaVO().getPeriodoLetivoRenovar().setCodigo(0);
				getRenovacaoMatriculaTurmaVO().getTurmaRenovar().setCodigo(0);
//				getRenovacaoMatriculaTurmaVO().getPlanoFinanceiroCursoAtual().setCodigo(0);
//				getRenovacaoMatriculaTurmaVO().getCondicaoPagamentoPlanoFinanceiroCursoAtual().setCodigo(0);
				getRenovacaoMatriculaTurmaVO().setQtdeRenovacaoAGerada(0);
				getListaSelectItemPeriodoLetivoMatricula().clear();
//				getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoRenovar().clear();
//				getListaSelectItemCondicaoPagamentoPlanoFinanceiroCursoAtual().clear();
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getPermiteAlterarCampos() {
		return getRenovacaoMatriculaTurmaVO().getNovoObj() && getRenovacaoMatriculaTurmaVO().getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().isEmpty();
	}

	public Boolean getApresentarBotaoRenovarMatricula() {
		return (getRenovacaoMatriculaTurmaVO().getNovoObj() 
				&& getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada() > 0 ) || 
				(!getRenovacaoMatriculaTurmaVO().getNovoObj() 
						&& !getRenovacaoMatriculaTurmaVO().getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().isEmpty() 
						&& (getRenovacaoMatriculaTurmaVO().getSituacao().equals(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO) || getRenovacaoMatriculaTurmaVO().getSituacao().equals(SituacaoRenovacaoTurmaEnum.ERRO_PROCESSAMENTO)));
	}

	public Boolean getApresentarModalConsultarMatriculaPeriodo() {
		if (apresentarModalConsultarMatriculaPeriodo == null) {
			apresentarModalConsultarMatriculaPeriodo = false;
		}
		return apresentarModalConsultarMatriculaPeriodo;
	}

	public void setApresentarModalConsultarMatriculaPeriodo(Boolean apresentarModalConsultarMatriculaPeriodo) {
		this.apresentarModalConsultarMatriculaPeriodo = apresentarModalConsultarMatriculaPeriodo;
	}

	public String getLabelConsultaMatriculaPeriodo() {
		if (getApresentarModalConsultarMatriculaPeriodo()) {
			if (getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada() == 0) {
				return "Consultando Matrículas";
			} else {
				if (getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoGerada() <= getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada()) {
					return "Carregando Matrícula " + getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoGerada() + " de " + getRenovacaoMatriculaTurmaVO().getQtdeRenovacaoAGerada();
				} else {
					return "Finalizando Processamento";
				}
			}
		}
		return "";
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();

		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
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

	public boolean isPermitirRealizarMatriculaDisciplinaPreRequisito() {
		return permitirRealizarMatriculaDisciplinaPreRequisito;
	}

	public void setPermitirRealizarMatriculaDisciplinaPreRequisito(boolean permitirRealizarMatriculaDisciplinaPreRequisito) {
		this.permitirRealizarMatriculaDisciplinaPreRequisito = permitirRealizarMatriculaDisciplinaPreRequisito;
	}	

	public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
		if (gradeDisciplinaCompostaVOs == null) {
			gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
		}
		return gradeDisciplinaCompostaVOs;
	}

	public void setGradeDisciplinaCompostaVOs(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
		this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
	}
	
	public void executarVerificacaoGradeDisciplinaConfiguradaEstudarQuantidadeComposta() throws Exception {
		if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
			for (GradeDisciplinaVO gdVO : getGradeDisciplinaVOs()) {
				try {
					executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(gdVO.getGradeDisciplinaCompostaVOs());
				} catch (Exception e) {
					setAbrirPainelGradeDisciplina(true);
					return;
				}
			}
			setAbrirPainelGradeDisciplina(false);
			return;
		}
		setGradeDisciplinaVOs(getFacadeFactory().getGradeDisciplinaFacade().consultarPorTurmaDisciplinaCompostaEstudarQuantidadeComposta(getRenovacaoMatriculaTurmaVO().getTurmaRenovar().getCodigo(), false, getUsuarioLogado()));
		setAbrirPainelGradeDisciplina(Uteis.isAtributoPreenchido(getGradeDisciplinaVOs()));
	}
	
	public void selecionarGradeDisciplina() {
		setGradeDisciplinaSelecionadaVO((GradeDisciplinaVO) getRequestMap().get("gradeDisciplinaItem"));
		setAbrirPainelGradeDisciplinaComposta(Uteis.isAtributoPreenchido(getGradeDisciplinaSelecionadaVO()));
	}

	public List<GradeDisciplinaVO> getGradeDisciplinaVOs() {
		if (gradeDisciplinaVOs == null) {
			gradeDisciplinaVOs = new ArrayList<GradeDisciplinaVO>(0);
		}
		return gradeDisciplinaVOs;
	}

	public void setGradeDisciplinaVOs(List<GradeDisciplinaVO> gradeDisciplinaVOs) {
		this.gradeDisciplinaVOs = gradeDisciplinaVOs;
	}

	public boolean isAbrirPainelGradeDisciplina() {
		return abrirPainelGradeDisciplina;
	}

	public void setAbrirPainelGradeDisciplina(boolean abrirPainelGradeDisciplina) {
		this.abrirPainelGradeDisciplina = abrirPainelGradeDisciplina;
	}

	public boolean isAbrirPainelGradeDisciplinaComposta() {
		return abrirPainelGradeDisciplinaComposta;
	}

	public void setAbrirPainelGradeDisciplinaComposta(boolean abrirPainelGradeDisciplinaComposta) {
		this.abrirPainelGradeDisciplinaComposta = abrirPainelGradeDisciplinaComposta;
	}

	public GradeDisciplinaVO getGradeDisciplinaSelecionadaVO() {
		if (gradeDisciplinaSelecionadaVO == null) {
			gradeDisciplinaSelecionadaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaSelecionadaVO;
	}

	public void setGradeDisciplinaSelecionadaVO(GradeDisciplinaVO gradeDisciplinaSelecionadaVO) {
		this.gradeDisciplinaSelecionadaVO = gradeDisciplinaSelecionadaVO;
	}
	
	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarGradeDisciplinaSelecionada() {
		try {
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(getGradeDisciplinaSelecionadaVO().getGradeDisciplinaCompostaVOs());
			setAbrirPainelGradeDisciplinaComposta(false);
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setAbrirPainelGradeDisciplinaComposta(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar() {
		try {
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarComThrows();
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setAbrirPainelGradeDisciplina(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarComThrows() throws Exception {
		try {
			for (GradeDisciplinaVO gdVO : getGradeDisciplinaVOs()) {
				executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(gdVO.getGradeDisciplinaCompostaVOs());
			}
			setAbrirPainelGradeDisciplina(false);
		} catch (Exception e) {
			setAbrirPainelGradeDisciplina(true);
			throw e;
		}
	}
	
	private void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVOs)) {
			return;
		}
		Integer numeroMaximoDisciplinaComposicaoEstudar = 0;
		GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCompostaVOs.get(0).getGradeDisciplina().getCodigo(), getUsuarioLogado());
		for (GradeDisciplinaCompostaVO compostaVO : gradeDisciplinaCompostaVOs) {
			numeroMaximoDisciplinaComposicaoEstudar += compostaVO.getSelecionado() ? 1 : 0;
		}
		if ((numeroMaximoDisciplinaComposicaoEstudar < gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar() || numeroMaximoDisciplinaComposicaoEstudar > gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar()) 
				&& TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(gradeDisciplinaVO.getTipoControleComposicao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudar").replace("{0}", 
					gradeDisciplinaVO.getDisciplina().getNome()).replace("{1}", numeroMaximoDisciplinaComposicaoEstudar.toString())
					.replace("{2}", gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar().toString())
					.replace("{3}", gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar().toString()));
		}
	}
	
	private void executarGeracaoGradeDisciplinaCompostaIncluir() throws Exception {
		setGradeDisciplinaCompostaVOs(new ArrayList<GradeDisciplinaCompostaVO>(0));
		for (GradeDisciplinaVO gdVO : getGradeDisciplinaVOs()) {
			for (GradeDisciplinaCompostaVO gdcVO : gdVO.getGradeDisciplinaCompostaVOs()) {
				if (gdcVO.getSelecionado()) {
					getGradeDisciplinaCompostaVOs().add(gdcVO);
				}
			}
		}
	}

	public ProgressBarVO getProgressBarConsulta() {
		if (progressBarConsulta == null) {
			progressBarConsulta = new ProgressBarVO();
		}
		return progressBarConsulta;
	}

	public void setProgressBarConsulta(ProgressBarVO progressBarConsulta) {
		this.progressBarConsulta = progressBarConsulta;
	}

	public ProgressBarVO getProgressBarRenovacao() {
		if (progressBarRenovacao == null) {
			progressBarRenovacao = new ProgressBarVO();
		}
		return progressBarRenovacao;
	}

	public void setProgressBarRenovacao(ProgressBarVO progressBarRenovacao) {
		this.progressBarRenovacao = progressBarRenovacao;
	}
	
	

}
