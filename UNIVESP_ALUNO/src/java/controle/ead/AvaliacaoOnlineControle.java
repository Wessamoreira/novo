package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;


import org.primefaces.event.DragDropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDefinicaoPeriodoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

//import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

import negocio.comuns.utilitarias.dominios.TipoPessoa;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AvaliacaoOnlineControle")
@Scope("viewScope")
public class AvaliacaoOnlineControle extends AvaliacaoOnlineAlunoSuperControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private QuestaoVO questaoVO;
	private DataModelo controleConsultaQuestoes;
	private Boolean apresentarResponsavelAtivacao;
	private Boolean apresentarResponsavelInativacao;
	private String fecharModalPanelQuestoesFixasRandomicas;
	private Integer nivelFacil;
	private Integer nivelMedio;
	private Integer nivelDificil;
	private Integer qualquerNivel;
	private List<SelectItem> listaSelectItemConteudo;
	private List<SelectItem> listaSelectItemDisciplinasProfessor;
	private DisciplinaVO disciplinaVO;
	private List<SelectItem> comboBoxRegraPoliticaSelecaoQuestao;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessor;
	private List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	private List<SelectItem> listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	private Boolean permitirAlteracaoValorNota;
	private String valorConsulta;
	private String campoConsulta;
	private List<SelectItem> listaSelectItemVariavelNotaCfaVOs;
	private List<SelectItem> listaSelectItemDisciplinasPorTurma;
	private List<SelectItem> listaSelectItemTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private ProgressBarVO progressBarVO;
	private String abrirModalAvisoAtivacaoPorTurma;
	private Boolean desabilitarRecursoRandomizacaoQuestaoProfessor;
	private ControleConsulta controleConsultaDisciplina;

	@PostConstruct
	public void init() {
		setAvaliacaoOnlineVO(new AvaliacaoOnlineVO());
		getAvaliacaoOnlineVO().setReposnsavelCriacao(getUsuarioLogadoClone());
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public void montarListaSelectItemDisciplinasProfessor() {
		try {
			setListaSelectItemDisciplinasProfessor(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "descricaoParaCombobox", true, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String novo() {
		setSimulandoAvaliacao(false);
		setPermitirAlteracaoValorNota(true);
		setAvaliacaoOnlineVO(new AvaliacaoOnlineVO());
		getAvaliacaoOnlineVO().setReposnsavelCriacao(getUsuarioLogadoClone());
		setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
		apresentarReponsavel();
		limparMensagem();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemDisciplinasProfessor();
		}
		getControleConsulta().getListaConsulta().clear();
		getControleConsultaQuestoes().getListaConsulta().clear();
		preencherCamposPorTipoUsoAvaliacaoOnline();
		montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
		montarListaDeNotasDaConfiguracaoAcademico();
		verificarPermissaoRecursoRandomizarQuestaoProfessor();
		verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor();
		setOncompleteModal("");
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorForm");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineForm");
	}

	public String voltarTelaConsulta() {
		setAvaliacaoOnlineVO(new AvaliacaoOnlineVO());
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineCons");
	}

	public String gravar() {
		try {
			setSimulandoAvaliacao(false);
			executarValidacaoSimulacaoVisaoProfessor();
			getAvaliacaoOnlineVO().setReposnsavelCriacao(getUsuarioLogadoClone());
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().persistir(getAvaliacaoOnlineVO(), false, getUsuarioLogado());
			apresentarReponsavel();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorForm");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineForm");
	}

	public void validarAvaliacaoOnlineModalAntesGravar() {
		try {
			setSimulandoAvaliacao(false);
			if (getAvaliacaoOnlineVO().getTipoUso().isTurma() && getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().isRandomicoPorComplexidade() && !getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().isQualquerQuestao()) {
				setOncompleteModal("PF('panelAvisoPoliticaSelecaoQuestao').show()");
			} else {
				gravar();
				setOncompleteModal("");
			}
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String ativarAvaliacaoOnline() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			setSimulandoAvaliacao(false);
			Uteis.checkState(getAvaliacaoOnlineVO().getCodigo().equals(0), "Grave a Avaliação Primeiro");
			getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.ATIVO);
			getAvaliacaoOnlineVO().setReposnsavelAtivacao(getUsuarioLogadoClone());
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarSituacaoAvaliacaoOnline(getAvaliacaoOnlineVO(), false, null, getUsuarioLogado());
			apresentarReponsavel();
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void alterarAvaliacaoOnlinePorTurmaAtivada() {
		try {
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarAvaliacaoOnlineJaAtivada(getAvaliacaoOnlineVO(), false, getProgressBarVO(), getProgressBarVO().getUsuarioVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);
		}
	}

	public void ativarAvaliacaoOnlineTipoUsoTurma() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.ATIVO);
			getAvaliacaoOnlineVO().setReposnsavelAtivacao(getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarSituacaoAvaliacaoOnline(getAvaliacaoOnlineVO(), false, getProgressBarVO(), getProgressBarVO().getUsuarioVO());
			apresentarReponsavel();
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);
		}
	}

	public void realizarInicioAtivacaoPorProgressBar() {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			Integer tam = getAvaliacaoOnlineVO().getListaHistoricoAluno().size() == 0 ? 1 : getAvaliacaoOnlineVO().getListaHistoricoAluno().size();
			getProgressBarVO().iniciar(0l, tam, "Iniciando Processamento do(s) Calendarios da Avaliação Online.", true, this, "ativarAvaliacaoOnlineTipoUsoTurma");
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
			setOncompleteModal("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarInicioAlteracaoPorTurmaAtivadaPorProgressBar() {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			Integer tam = getAvaliacaoOnlineVO().getListaHistoricoAluno().size() == 0 ? 1 : getAvaliacaoOnlineVO().getListaHistoricoAluno().size();
			getProgressBarVO().iniciar(0l, tam, "Iniciando Processamento do(s) Calendarios da Avaliação Online.", true, this, "alterarAvaliacaoOnlinePorTurmaAtivada");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			setOncompleteModal("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina copiado do registro de nota professor que fica em
	 * HistoricoTurmaControle
	 */
	public void prepararDadosParaAtivacaoPorTurma() {
		try {
			setAbrirModalAvisoAtivacaoPorTurma("");
			executarValidacaoSimulacaoVisaoProfessor();
			Uteis.checkState(getAvaliacaoOnlineVO().getCodigo().equals(0), "Grave a Avaliação Primeiro");
			boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getAvaliacaoOnlineVO().getTurmaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogadoClone());
			boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA();
			boolean permiteLancarNotaDisciplinaComposta = verificarPermiteLancarNotaDisciplinaComposta();
			boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			boolean visaoProfessor = false;
			Integer codigoProfessor = 0;
			if (getAvaliacaoOnlineVO().getUsoExclusivoProfessor() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				visaoProfessor = true;
				if (getAvaliacaoOnlineVO().getUsoExclusivoProfessor() && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getProfessor())) {
					codigoProfessor = getAvaliacaoOnlineVO().getProfessor().getCodigo();
				} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					codigoProfessor = getUsuarioLogado().getPessoa().getCodigo();
				}
			}
			getAvaliacaoOnlineVO().setListaHistoricoAluno(getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(0, 0, getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), getAvaliacaoOnlineVO().getTurmaVO(), getAvaliacaoOnlineVO().getAno(), getAvaliacaoOnlineVO().getSemestre(), trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, visaoProfessor, codigoProfessor, false, null, permitiVisualizarAlunoTR_CA, permiteLancarNotaDisciplinaComposta, Uteis.NIVELMONTARDADOS_TODOS, null, null, getUsuarioLogadoClone(), permitirRealizarLancamentoAlunosPreMatriculados));
			setAbrirModalAvisoAtivacaoPorTurma("PF('panelAvisoAtivacaoPorTurma').show()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inativarAvaliacaoOnline() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.INATIVO);
			getAvaliacaoOnlineVO().setReposnsavelInativacao(getUsuarioLogadoClone());
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarSituacaoAvaliacaoOnline(getAvaliacaoOnlineVO(), false, null, getUsuarioLogadoClone());
			apresentarReponsavel();			
			setOncompleteModal("");
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.ATIVO);			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorForm");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineForm");
	}
	
	private String mensagemInativacao;
	
	public String getMensagemInativacao() {
		return mensagemInativacao;
	}

	public void setMensagemInativacao(String mensagemInativacao) {
		this.mensagemInativacao = mensagemInativacao;
	}

	public void validarAvaliacaoOnlineModalAntesInativar() {
		try {
			limparMensagem();
			setOncompleteModal("PF('panelAvisoInativarPorTurma').show()");
			if (!getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
				setMensagemInativacao(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlineEstaVinculada(getAvaliacaoOnlineVO().getCodigo()).toString());
			} 
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String excluir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().excluir(getAvaliacaoOnlineVO(), false, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorForm");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineForm");
	}

	public String editar() {
		try {
			setSimulandoAvaliacao(false);
			AvaliacaoOnlineVO avaliacaoOnlineVO = (AvaliacaoOnlineVO) context().getExternalContext().getRequestMap().get("avaliacaoItens");
			setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(avaliacaoOnlineVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			apresentarReponsavel();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
//				if (getAvaliacaoOnlineVO().getUsoExclusivoProfessor()) {
					montarListaSelectItemDisciplinasProfessor();
//				}
			}
			preencherCamposPorTipoUsoAvaliacaoOnline();
			montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
			montarListaDeNotasDaConfiguracaoAcademico();
			consultarTemaAssunto();
			setOncompleteModal("");
			verificarAvaliacaoOnlineMatriculaExistente(getAvaliacaoOnlineVO().getCodigo());
			getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().forEach(t -> t.setMaximixado(false));
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
			somarNotaAvaliacao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineProfessorForm");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineForm");

	}

	public String consultar() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), 0, getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			if (getControleConsultaDisciplina().getCampoConsulta().equals("codigo")) {
				getControleConsultaDisciplina().getListaConsulta().clear();
				if (getControleConsultaDisciplina().getCampoConsulta().equals("")) {
					getControleConsultaDisciplina().setValorConsulta("0");
				}
				if (getControleConsultaDisciplina().getValorConsulta().trim() != null || !getControleConsultaDisciplina().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsultaDisciplina().getValorConsulta().trim());
				}
				DisciplinaVO obj = new DisciplinaVO();
				int valorInt = Integer.parseInt(getControleConsultaDisciplina().getValorConsulta());
				obj = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(valorInt, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getControleConsultaDisciplina().getListaConsulta().add(obj);

			}
			if (getControleConsultaDisciplina().getCampoConsulta().equals("nome")) {
				getControleConsultaDisciplina().getListaConsulta().clear();
				getControleConsultaDisciplina().setListaConsulta(getFacadeFactory().getDisciplinaFacade().consultarPorNome(getControleConsultaDisciplina().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())); // =
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsultaDisciplina().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gerarQuestoesRandomicamente() {
		try {
			getAvaliacaoOnlineVO().setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO);
			getAvaliacaoOnlineVO().setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(getAvaliacaoOnlineVO(), getNivelFacil(), getNivelMedio(), getNivelDificil(), getQualquerNivel(), getMatriculaPeriodoTurmaDisciplinaVO(), getAvaliacaoOnlineVO().getIsPermiteInformarTemaAssunto() ? getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs() : null, getUsuarioLogado()));
			if (getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_nenhumaQuestaoEncontrada"));
			}
			getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().forEach(t -> t.setMaximixado(false));
			setFecharModalPanelQuestoesFixasRandomicas("PF('panelQuestoesFixasRandomicamente').hide()");
			somarNotaAvaliacao();
		} catch (Exception e) {
			setFecharModalPanelQuestoesFixasRandomicas("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally {
			getAvaliacaoOnlineVO().setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.NENHUM);
		}
	}

	public void somarNotaAvaliacao() {
		if (getIsRenderizarCamposRandomizacaoQuestaoTipoUsoDisciplinaOuGeral()) {
			getAvaliacaoOnlineVO().setNotaPorQuestaoQualquerNivel(
					getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() 
					&& getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() ?  getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil()
					: getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() 
					&& getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() ? getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio()
							: getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() 
									&& getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() >= getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() ? getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil()
											: getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil());
			getAvaliacaoOnlineVO().setNotaMaximaAvaliacao((getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil()) 
					+ (getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio()) 
					+ (getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil()) 
					+ (getAvaliacaoOnlineVO().getNotaPorQuestaoQualquerNivel() * getAvaliacaoOnlineVO().getQuantidadeQualquerNivelQuestao()));
		} else {
			getAvaliacaoOnlineVO().setNotaMaximaAvaliacao((getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil()) + (getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio()) + (getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() * getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil()));
		}
	}

	public void consultarQuestao() {
		try {
			if (getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo().equals(0)) {
				throw new Exception("Informe Uma Disciplina");
			}
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
			getControleConsultaOtimizado().setLimitePorPagina(5);
			List<QuestaoVO> objs = new ArrayList<>();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() && !getLoginControle().getPermissaoAcessoMenuVO().getAlterarQuestaoOnlineOutroProfessor()) {
				if (getCampoConsulta().equals("codigo")) {
					QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getValorConsulta()));
					objs.add(questaoVO);
					getControleConsultaQuestoes().setListaConsulta(objs);
					getControleConsultaQuestoes().setTotalRegistrosEncontrados(1);
				} else {
					getControleConsultaQuestoes().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultarQuestoesPorUsuario(getQuestaoVO().getEnunciado(), getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), false, "", getUsuarioLogado(), getControleConsultaQuestoes().getLimitePorPagina(), getControleConsultaQuestoes().getOffset(), getAvaliacaoOnlineVO().getConteudoVO().getCodigo()));
					getControleConsultaQuestoes().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistroPorUsuario(getQuestaoVO().getEnunciado(), getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), getAvaliacaoOnlineVO().getConteudoVO().getCodigo(), getUsuarioLogado()));
				}
			} else {
				if (getCampoConsulta().equals("codigo")) {
					QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getValorConsulta()));
					objs.add(questaoVO);
					getControleConsultaQuestoes().setListaConsulta(objs);
					getControleConsultaQuestoes().setTotalRegistrosEncontrados(1);
				} else {
					getControleConsultaQuestoes().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoVO().getEnunciado(), getAvaliacaoOnlineVO().getUnidadeConteudoVO().getTemaAssuntoVO(), getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), false, "", getUsuarioLogado(), getControleConsultaQuestoes().getLimitePorPagina(), getControleConsultaQuestoes().getOffset(), getAvaliacaoOnlineVO().getConteudoVO().getCodigo(), getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum(), false, null, null, false));
					getControleConsultaQuestoes().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoVO().getEnunciado(), getAvaliacaoOnlineVO().getUnidadeConteudoVO().getTemaAssuntoVO(), getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), SituacaoQuestaoEnum.ATIVA, true, false, false, false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), getAvaliacaoOnlineVO().getConteudoVO().getCodigo(), getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum(), false, null, null, getUsuarioLogado(), false));
				}
			}
			marcarQuestaoJaSelecionada();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public void adicionarQuestao() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = new AvaliacaoOnlineQuestaoVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem");
			avaliacaoOnlineQuestaoVO.setQuestaoVO(questaoVO);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().adicionarQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO);
			questaoVO.setSelecionado(true);
			somarNotaAvaliacao();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestao() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = new AvaliacaoOnlineQuestaoVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItem");
			avaliacaoOnlineQuestaoVO.setQuestaoVO(questaoVO);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().removerQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO, getUsuarioLogado());
			questaoVO.setSelecionado(false);
			somarNotaAvaliacao();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestaoAvaliacaoOnline() {
		try {
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().removerQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens"), getUsuarioLogado());
			somarNotaAvaliacao();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void paginarQuestao() {
		
		consultarQuestao();
	}

	public void subirQuestaoAvaliacaoOnline() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (avaliacaoOnlineQuestaoVO.getOrdemApresentacao() > 1) {
				AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 = getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().get(avaliacaoOnlineQuestaoVO.getOrdemApresentacao() - 2);
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarOrdemApresentacaoQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO, avaliacaoOnlineQuestaoVO2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerQuestaoAvaliacaoOnline() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().size() >= avaliacaoOnlineQuestaoVO.getOrdemApresentacao()) {
				AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 = getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().get(avaliacaoOnlineQuestaoVO.getOrdemApresentacao());
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarOrdemApresentacaoQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO, avaliacaoOnlineQuestaoVO2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public void marcarQuestaoJaSelecionada() {
		for (QuestaoVO questaoVO : (List<QuestaoVO>) getControleConsultaQuestoes().getListaConsulta()) {
			for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO : getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs()) {
				if (questaoVO.getCodigo().equals(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo())) {
					questaoVO.setSelecionado(true);
				}
			}
		}
	}

	public void alterarOrdemApresentacaoQuestaoDragDrop(DragDropEvent<?> dropEvent) {
		try {
			if (dropEvent.getData() instanceof AvaliacaoOnlineQuestaoVO && dropEvent.getData() instanceof AvaliacaoOnlineQuestaoVO) {
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarOrdemApresentacaoQuestaoAvaliacaoOnline(getAvaliacaoOnlineVO(), (AvaliacaoOnlineQuestaoVO) dropEvent.getData(), (AvaliacaoOnlineQuestaoVO) dropEvent.getData());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDisciplina() {
		if (getSimulandoAvaliacao()) {
			montarListaSelectItemConteudos();
			consultarTemaAssunto();
			consultarDefinicoesTutoriaOnlineTurmaDisciplina();
		} else {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getDisciplinaVO()) && !getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty() && !getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo().equals(obj.getCodigo())) {
				setMensagemDetalhada("msg_erro", "Já foram adicionadas questões à Avaliação, portanto não é possível alterar a Disciplina.", Uteis.ERRO);
				return;
			}
			getAvaliacaoOnlineVO().setDisciplinaVO(obj);
//			getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(obj);
			getControleConsultaDisciplina().getListaConsulta().clear();
			montarListaSelectItemConteudos();
			consultarTemaAssunto();
		}
	}

	public void limparDadosDisciplina() {
		if (getSimulandoAvaliacao()) {
			getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
			getListaSelectItemConteudo().clear();
		} else {
			if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getDisciplinaVO()) && !getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
				setMensagemDetalhada("msg_erro", "Já foram adicionadas questões à Avaliação, portanto não é possível remover a Disciplina.", Uteis.ERRO);
				return;
			}
			removerObjetoMemoria(getAvaliacaoOnlineVO().getDisciplinaVO());
			limparCamposQuestoesQuandoConteudoOuDisciplinaAlterado();
			getListaSelectItemConteudo().clear();
		}
	}

	public String inicializarAvaliacaoOnline() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineCons");
	}

	public List<SelectItem> getListaSelectItemCampoConsulta() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("nome", "Nome"));
		objs.add(new SelectItem("disciplina", "Disciplina"));
		objs.add(new SelectItem("turma", "Turma"));
		return objs;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoQuestaoConsulta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoQuestaoEnum.UNICA_ESCOLHA, TipoQuestaoEnum.UNICA_ESCOLHA.getValorApresentar()));
		itens.add(new SelectItem(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.MULTIPLA_ESCOLHA.getValorApresentar()));

		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoUso() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoUsoEnum enumerador : TipoUsoEnum.values()) {
			if (enumerador.isRea() && !getIsNaoPermitirAlterarQuandoAtivado()) {
				continue;
			}
			lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
		}
		return lista;
	}

	public List<SelectItem> getListaSelectItemTipoGeracaoProvaOnline() {
		List<SelectItem> comboBoxTipoGeracaoProvaOnlineEnum = new ArrayList<SelectItem>();
		comboBoxTipoGeracaoProvaOnlineEnum = new ArrayList<SelectItem>();
		comboBoxTipoGeracaoProvaOnlineEnum.add(new SelectItem(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE, TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE.getValorApresentar()));
		comboBoxTipoGeracaoProvaOnlineEnum.add(new SelectItem(TipoGeracaoProvaOnlineEnum.FIXO, TipoGeracaoProvaOnlineEnum.FIXO.getValorApresentar()));

		return comboBoxTipoGeracaoProvaOnlineEnum;

	}

	public void apresentarReponsavel() {
		if (getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.INATIVO)) {
			setApresentarResponsavelInativacao(true);
			setApresentarResponsavelAtivacao(true);
		} else if (getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.ATIVO)) {
			setApresentarResponsavelInativacao(false);
			setApresentarResponsavelAtivacao(true);
		} else if (getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.EM_CONSTRUCAO)) {
			setApresentarResponsavelInativacao(false);
			setApresentarResponsavelAtivacao(false);
		}
	}

	public void preencherCamposPorTipoUsoAvaliacaoOnline() {
		try {
			setSimulandoAvaliacao(false);
			verficarTipoGeracaoAvaliacaoOnline();
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				limparCamposQuestoesQuandoConteudoOuDisciplinaAlterado();
			}
			getListaSelectItemTurma().clear();
			getListaSelectItemDisciplinasPorTurma().clear();
			if (getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
				if (!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getAno())) {
					getAvaliacaoOnlineVO().setAno(Uteis.getAnoDataAtual());
				}
				if (!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getSemestre())) {
					getAvaliacaoOnlineVO().setSemestre(Uteis.getSemestreAtual());
				}
				montarListaSelectItemTurma(false);
				getAvaliacaoOnlineVO().setRegraDefinicaoPeriodoAvaliacaoOnline(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_DIA_FIXO);
			} else {
				montarListaSelectItemConteudos();
				if (getAvaliacaoOnlineVO().getCodigo().equals(0) && getAvaliacaoOnlineVO().getSituacao().isEmElaboracao()) {
					getAvaliacaoOnlineVO().setTurmaVO(new TurmaVO());
				}
				montarListaSelectItemTurma(false);
			}
			if (getAvaliacaoOnlineVO().getCodigo().equals(0) && getAvaliacaoOnlineVO().getSituacao().isEmElaboracao()) {
				getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().clear();
				getAvaliacaoOnlineVO().setDisciplinaVO(new DisciplinaVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemConteudos() {
		try {			
			List<ConteudoVO> resultado = new ArrayList<ConteudoVO>();
			
			if(getSimulandoAvaliacao() && !Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getConteudoVO().getCodigo())) {
				Integer conteudo = getFacadeFactory().getConteudoFacade().consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				if(Uteis.isAtributoPreenchido(conteudo)) {
					resultado.add(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(conteudo, NivelMontarDados.COMBOBOX, false, getUsuarioLogado()));
				}
				
			}else if (!getSimulandoAvaliacao() && getAvaliacaoOnlineVO().getTipoUso().isDisciplina() && !getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo().equals(0) && (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE) || getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO))) {
				resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
			} else if (!getSimulandoAvaliacao() && getAvaliacaoOnlineVO().getTipoUso().isDisciplina() && getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo().equals(0)) {
				resultado.clear();
			} else if (!getSimulandoAvaliacao() && getAvaliacaoOnlineVO().getTipoUso().isGeral()) {
				resultado = getFacadeFactory().getConteudoFacade().consultarConteudosDiferentesDeInativos(getAvaliacaoOnlineVO().getProfessor().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (!getAvaliacaoOnlineVO().getCodigo().equals(0) && !getAvaliacaoOnlineVO().getConteudoVO().getCodigo().equals(0)) {
				ConteudoVO conteudo = getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getAvaliacaoOnlineVO().getConteudoVO().getCodigo(), NivelMontarDados.COMBOBOX, false, getUsuarioLogado());
				resultado.add(conteudo);
			}
			getListaSelectItemConteudo().clear();
			setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricaoCombobox", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposQuestoesQuandoConteudoOuDisciplinaAlterado() {
		setSimulandoAvaliacao(false);
		if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
			if (getAvaliacaoOnlineVO().getSituacao().isEmElaboracao() && getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
				getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoDificil(0);
				getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoFacil(0);
				getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoMedio(0);
				getAvaliacaoOnlineVO().setNotaMaximaAvaliacao(0.0);
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} else {
			getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().clear();
			consultarTemaAssunto();
		}
	}

	// Getters and Setters
	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if (avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}

	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}

	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}

	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}

	public DataModelo getControleConsultaQuestoes() {
		if (controleConsultaQuestoes == null) {
			controleConsultaQuestoes = new DataModelo();
		}
		return controleConsultaQuestoes;
	}

	public void setControleConsultaQuestoes(DataModelo controleConsultaQuestoes) {
		this.controleConsultaQuestoes = controleConsultaQuestoes;
	}

	public Boolean getApresentarResponsavelAtivacao() {
		if (apresentarResponsavelAtivacao == null) {
			apresentarResponsavelAtivacao = false;
		}
		return apresentarResponsavelAtivacao;
	}

	public void setApresentarResponsavelAtivacao(Boolean apresentarResponsavelAtivacao) {
		this.apresentarResponsavelAtivacao = apresentarResponsavelAtivacao;
	}

	public Boolean getApresentarResponsavelInativacao() {
		if (apresentarResponsavelInativacao == null) {
			apresentarResponsavelInativacao = false;
		}
		return apresentarResponsavelInativacao;
	}

	public void setApresentarResponsavelInativacao(Boolean apresentarResponsavelInativacao) {
		this.apresentarResponsavelInativacao = apresentarResponsavelInativacao;
	}

	public String getFecharModalPanelQuestoesFixasRandomicas() {
		if (fecharModalPanelQuestoesFixasRandomicas == null) {
			fecharModalPanelQuestoesFixasRandomicas = "";
		}
		return fecharModalPanelQuestoesFixasRandomicas;
	}

	public void setFecharModalPanelQuestoesFixasRandomicas(String fecharModalPanelQuestoesFixasRandomicas) {
		this.fecharModalPanelQuestoesFixasRandomicas = fecharModalPanelQuestoesFixasRandomicas;
	}

	public Integer getNivelFacil() {
		if (nivelFacil == null) {
			nivelFacil = 0;
		}
		return nivelFacil;
	}

	public void setNivelFacil(Integer nivelFacil) {
		this.nivelFacil = nivelFacil;
	}

	public Integer getNivelMedio() {
		if (nivelMedio == null) {
			nivelMedio = 0;
		}
		return nivelMedio;
	}

	public void setNivelMedio(Integer nivelMedio) {
		this.nivelMedio = nivelMedio;
	}

	public Integer getNivelDificil() {
		if (nivelDificil == null) {
			nivelDificil = 0;
		}
		return nivelDificil;
	}

	public void setNivelDificil(Integer nivelDificil) {
		this.nivelDificil = nivelDificil;
	}

	public Integer getQualquerNivel() {
		if (qualquerNivel == null) {
			qualquerNivel = 0;
		}
		return qualquerNivel;
	}

	public void setQualquerNivel(Integer qualquerNivel) {
		this.qualquerNivel = qualquerNivel;
	}

	public List<SelectItem> getListaSelectItemConteudo() {
		if (listaSelectItemConteudo == null) {
			listaSelectItemConteudo = new ArrayList<SelectItem>();
		}
		return listaSelectItemConteudo;
	}

	public void setListaSelectItemConteudo(List<SelectItem> listaSelectItemConteudo) {
		this.listaSelectItemConteudo = listaSelectItemConteudo;
	}

	public List<SelectItem> getListaSelectItemDisciplinasProfessor() {
		if (listaSelectItemDisciplinasProfessor == null) {
			listaSelectItemDisciplinasProfessor = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasProfessor;
	}

	public void setListaSelectItemDisciplinasProfessor(List<SelectItem> listaSelectItemDisciplinasProfessor) {
		this.listaSelectItemDisciplinasProfessor = listaSelectItemDisciplinasProfessor;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			getListaSelectItemVariavelNotaCfaVOs().clear();
			getListaSelectItemVariavelNotaCfaVOs().addAll(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarVariavelTituloConfiguracaoAcademicoEAvaliacaoOnline(getAvaliacaoOnlineVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemVariavelNotaCfaVOs() {
		if (listaSelectItemVariavelNotaCfaVOs == null) {
			listaSelectItemVariavelNotaCfaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNotaCfaVOs;
	}

	public void setListaSelectItemVariavelNotaCfaVOs(List<SelectItem> listaSelectItemVariavelNotaCfaVOs) {
		this.listaSelectItemVariavelNotaCfaVOs = listaSelectItemVariavelNotaCfaVOs;
	}

	public void preencherDadosTurma() {
		try {
			if (Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO())) {
				getAvaliacaoOnlineVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getAvaliacaoOnlineVO().getTurmaVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
				getIsApresentarAnoVisaoProfessorCoordenador();
				getIsApresentarSemestreVisaoProfessorCoordenador();
				montarListaDisciplinaTurmaVisaoProfessor();
			} else {
				getListaSelectItemDisciplinasPorTurma().clear();
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma(Boolean simulandoAvaliacao) {
		setSimulandoAvaliacao(simulandoAvaliacao);
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		try {
			getListaSelectItemDisciplinasPorTurma().clear();			
			List<TurmaVO> listaResultado = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			listaResultado.stream().forEach(p -> {
				if (!mapAuxiliarSelectItem.contains(p.getCodigo())) {
					getListaSelectItemTurma().add(new SelectItem(p.getCodigo(), p.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(p.getCodigo());
				}
			});
			montarListaDisciplinaTurmaVisaoProfessor();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if(getSimulandoAvaliacao()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), false, "AT", 0, 0, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, false, true, null, false, getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
		}
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getAvaliacaoOnlineVO().getSemestre(), getAvaliacaoOnlineVO().getAno(), true, "AT", getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, true, null, false, null);
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			List<DisciplinaVO> resultadoConsulta = consultarDisciplinaProfessorTurma();
			getListaSelectItemDisciplinasPorTurma().clear();
			setListaSelectItemDisciplinasPorTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricaoParaCombobox"));
			
		} catch (Exception e) {
			getListaSelectItemDisciplinasPorTurma().clear();
		}
	}

	
	
	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		List<DisciplinaVO> listaConsultas = new ArrayList<>();
		if ((Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO()) && !getSimulandoAvaliacao()) || (getSimulandoAvaliacao() && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma()))) {
			TurmaVO turmaVO = getSimulandoAvaliacao() ? getMatriculaPeriodoTurmaDisciplinaVO().getTurma() : getAvaliacaoOnlineVO().getTurmaVO();
			String ano = getSimulandoAvaliacao() ? getMatriculaPeriodoTurmaDisciplinaVO().getAno() : getAvaliacaoOnlineVO().getAno();
			String semestre = getSimulandoAvaliacao() ? getMatriculaPeriodoTurmaDisciplinaVO().getSemestre() : getAvaliacaoOnlineVO().getSemestre();
			if (turmaVO.getIntegral() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, turmaVO.getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogadoClone());
			} else if (!turmaVO.getIntegral() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, turmaVO.getCodigo(), semestre, ano, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogadoClone());
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(turmaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
		}
		return listaConsultas;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if ((getUsuarioLogado().getIsApresentarVisaoAdministrativa() || getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO())) {
			if (getAvaliacaoOnlineVO().getTurmaVO().getSemestral() || getAvaliacaoOnlineVO().getTurmaVO().getAnual()) {
					if (!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getAno())) {
						getAvaliacaoOnlineVO().setAno(Uteis.getAnoDataAtual());
					}
				return true;				
				}
			getAvaliacaoOnlineVO().setAno("");
			return false;
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if ((getUsuarioLogado().getIsApresentarVisaoAdministrativa() || getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO())) {
			if (getAvaliacaoOnlineVO().getTurmaVO().getSemestral()) {
					if (!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getSemestre())) {
						getAvaliacaoOnlineVO().setSemestre(Uteis.getSemestreAtual());
					}
				return true;				
				}
			getAvaliacaoOnlineVO().setSemestre("");
			return false;
		}
		return true;
	}

	public void limparTurma() {
		try {
			if(getSimulandoAvaliacao()) {
				getMatriculaPeriodoTurmaDisciplinaVO().setTurma(new TurmaVO());
				if(getAvaliacaoOnlineVO().getTipoUso().isGeral()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
					getAvaliacaoOnlineTemaAssuntoSimulacaoVOs().clear();
				}
				if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getConteudoVO())) {
					getMatriculaPeriodoTurmaDisciplinaVO().setConteudo(new ConteudoVO());
				}
				setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
			}else {
				if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO()) && !getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
					setMensagemDetalhada("msg_erro", "Já foram adicionadas questões à Avaliação, portanto não é remover alterar a Turma.", Uteis.ERRO);
					return;
				}
				getAvaliacaoOnlineVO().setTurmaVO(new TurmaVO());
				limparDadosDisciplina();
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = null;
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
				getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
			}else {
				if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma())) {
					getFacadeFactory().getTurmaFacade().carregarDados(getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), NivelMontarDados.TODOS, getUsuarioLogado());					
				}else {
					getMatriculaPeriodoTurmaDisciplinaVO().setTurma(new TurmaVO());
					if(getAvaliacaoOnlineVO().getTipoUso().isGeral()){
						getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
						getListaSelectItemDisciplinasPorTurma().clear();
					}
					if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getConteudoVO())) {
						getMatriculaPeriodoTurmaDisciplinaVO().setConteudo(new ConteudoVO());
						getListaSelectItemConteudo().clear();										
					}	
				}
				obj = getMatriculaPeriodoTurmaDisciplinaVO().getTurma(); 
			}
			if (getSimulandoAvaliacao()) {				
				getMatriculaPeriodoTurmaDisciplinaVO().setTurma(obj);
				if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma())) {
				if(!obj.getIntegral() && getMatriculaPeriodoTurmaDisciplinaVO().getAno().isEmpty()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno(Uteis.getAnoDataAtual());
				}
				if(obj.getSemestral() && getMatriculaPeriodoTurmaDisciplinaVO().getSemestre().isEmpty()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(Uteis.getSemestreAtual());
				}
				if(obj.getIntegral()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno("");
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre("");
				}
				if(getAvaliacaoOnlineVO().getTipoUso().isGeral()){
					getMatriculaPeriodoTurmaDisciplinaVO().setDisciplina(new DisciplinaVO());
					montarListaDisciplinaTurmaVisaoProfessor();
				}
				if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getConteudoVO())) {
					getMatriculaPeriodoTurmaDisciplinaVO().setConteudo(new ConteudoVO());
					montarListaSelectItemConteudos();					
				}				
				consultarDefinicoesTutoriaOnlineTurmaDisciplina();
				}
			} else {
				if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO()) && !getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty() && !getAvaliacaoOnlineVO().getTurmaVO().getCodigo().equals(obj.getCodigo())) {
					setMensagemDetalhada("msg_erro", "Já foram adicionadas questões à Avaliação, portanto não é possível alterar a Turma.", Uteis.ERRO);
					return;
				}
				getAvaliacaoOnlineVO().setTurmaVO(obj);
				montarListaDisciplinaTurmaVisaoProfessor();
			}
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			if(getSimulandoAvaliacao()) {
				if(getAvaliacaoOnlineVO().getTipoUso().isGeral()) {
					setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				}else {
					setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoDisciplina(getValorConsultaTurma(), 0, getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
				}
			}else {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public String getCampoConsultaTurma() {
		campoConsultaTurma = Optional.ofNullable(campoConsultaTurma).orElse("");
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		valorConsultaTurma = Optional.ofNullable(valorConsultaTurma).orElse("");
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		listaConsultaTurma = Optional.ofNullable(listaConsultaTurma).orElse(new ArrayList<>());
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<SelectItem> getListaSelectItemDisciplinasPorTurma() {
		if (listaSelectItemDisciplinasPorTurma == null) {
			listaSelectItemDisciplinasPorTurma = new ArrayList<>();
		}
		return listaSelectItemDisciplinasPorTurma;
	}

	public void setListaSelectItemDisciplinasPorTurma(List<SelectItem> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinasPorTurma = listaSelectItemDisciplinas;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * Não será permitido qualquer alteração na avaliação on-line após a mesma
	 * estiver ativa ou inativa.
	 * 
	 * @return
	 */
	public boolean getIsNaoPermitirAlterarQuandoAtivado() {
		return getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.ATIVO) || getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.INATIVO);
	}

	/**
	 * A combo box tipo geração avaliação on-line tanto na visão administrativa
	 * quanto na visão do professor será desabilitado caso o tipo uso seja GERAL.
	 * 
	 * @return
	 */
	public boolean getIsDesativarComboBoxTipoGeracaoAvaliacaoOnline() {
		return getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.GERAL);
	}

	/**
	 * A renderazição dos campos que sejam tipo geração RANDOMICO_POR_COMPLEXIDADE
	 * será permitada caso o tipo uso seja GERAl ou o tipo uso seja DISCIPLINA e o
	 * tipo geração seja RANDOMICO_POR_COMPLEXIDADE.
	 * 
	 * @return
	 */
	public boolean getIsRenderizarCamposRandomizacaoQuestaoTipoUsoDisciplinaOuGeral() {
		return (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE));
	}

	/**
	 * Renderização dos campos para permitir o cadastro tipo uso DISCIPLINA e tipo
	 * geração FIXO
	 * 
	 * @return
	 */
	public boolean getIsRenderizarCamposQuestoesTipoUsoDisciplina() {
		return getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.DISCIPLINA) && getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO);
	}

	/**
	 * Independente do tipo uso, a combo box da Política de Seleção de Questão será
	 * renderizado caso o tipo geração seja RANDOMICO_POR_COMPLEXIDADE.
	 * 
	 * @return
	 */
	public boolean getIsRenderizarComboBoxPoliticaSelecaoDeQuestao() {
		return getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE);
	}

	/**
	 * No mesmo caso do getIsRenderizarComboBoxPoliticaSelecaoDeQuestao(), porém ele
	 * será renderizado também se a Política de Seleção de Questão seja
	 * QUESTOES_TODOS_ASSUNTOS_CONTEUDO ou QUESTOES_ASSUNTO_UNIDADE
	 * 
	 * @return
	 */
	public boolean getIsApresentarComboBoxRegraDistribuicaoQuestao() {
		return getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE) && (getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO) || getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE));
	}

	public List<SelectItem> getComboBoxRegraPoliticaSelecaoQuestao() {
		if (comboBoxRegraPoliticaSelecaoQuestao == null) {
			comboBoxRegraPoliticaSelecaoQuestao = new ArrayList<SelectItem>();
			for (PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum : PoliticaSelecaoQuestaoEnum.values()) {
				if (!politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.NENHUM) && !politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS)) {
					comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(politicaSelecaoQuestaoEnum, politicaSelecaoQuestaoEnum.getValorApresentar()));
				}
			}
		}
		return comboBoxRegraPoliticaSelecaoQuestao;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<SelectItem> getTipoConsultaComboBoxProfessor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			getAvaliacaoOnlineVO().setProfessor(obj);
			montarListaSelectItemDisciplinasProfessor();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {
			setListaConsultaProfessor(new ArrayList<PessoaVO>());
			setListaConsultaProfessor(getFacadeFactory().getPessoaFacade().consultar(getCampoConsultaProfessor(), getValorConsultaProfessor(), TipoPessoa.PROFESSOR, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>();
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public Boolean getIsApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return true;
		}
		return false;
	}

	public void limparDadosProfessor() {
		getAvaliacaoOnlineVO().setProfessor(new PessoaVO());
	}

	public void clonar() {
		try {
			setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().clonar(getAvaliacaoOnlineVO()));
			setApresentarResponsavelInativacao(false);
			setApresentarResponsavelAtivacao(false);
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		if (listaSelectItemParametrosMonitoramentoAvaliacaoOnline == null) {
			listaSelectItemParametrosMonitoramentoAvaliacaoOnline = new ArrayList<SelectItem>();
		}
		return listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}

	public void setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline) {
		this.listaSelectItemParametrosMonitoramentoAvaliacaoOnline = listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}

	public void montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		try {
			setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(UtilSelectItem.getListaSelectItem(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultarTodosParametros(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo", "descricao", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void verficarTipoGeracaoAvaliacaoOnline() {
		if (getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.GERAL) && getAvaliacaoOnlineVO().getCodigo().intValue() == 0) {
			getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().clear();
			getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoDificil(0);
			getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoFacil(0);
			getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoMedio(0);
			getAvaliacaoOnlineVO().setNotaPorQuestaoNivelDificil(0.0);
			getAvaliacaoOnlineVO().setNotaPorQuestaoNivelFacil(0.0);
			getAvaliacaoOnlineVO().setNotaPorQuestaoNivelMedio(0.0);
			getAvaliacaoOnlineVO().setNotaMaximaAvaliacao(0.0);
			getAvaliacaoOnlineVO().setTipoGeracaoProvaOnline(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE);
		}
	}

	public void inicializarConsultaQuestao() {
		setControleConsultaQuestoes(null);
		limparMensagem();
	}

	public String getAbrirModalConsultarQuestoes() {
		inicializarConsultaQuestao();
		return "PF('panelQuestao').show()";
	}

	public String getAbrirModalSelecionarQuestoesRandomicamente() {
		inicializarConsultaQuestao();
		return "PF('panelQuestoesFixasRandomicamente').show()";
	}

	public List<SelectItem> getListaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline() {
		if (listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline == null) {
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline = new ArrayList<SelectItem>(0);
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA.getValorApresentar()));
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.NUMERO_DIA_ESPECIFICO, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.NUMERO_DIA_ESPECIFICO.getValorApresentar()));
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA.getValorApresentar()));
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_DIA_FIXO, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_DIA_FIXO.getValorApresentar()));
		}
		return listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	}

	public void setListaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline(List<SelectItem> listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline) {
		this.listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline = listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	}

	public Boolean getPermitirAlteracaoValorNota() {
		if (permitirAlteracaoValorNota == null) {
			permitirAlteracaoValorNota = true;
		}
		return permitirAlteracaoValorNota;
	}

	public void setPermitirAlteracaoValorNota(Boolean permitirAlteracaoValorNota) {
		this.permitirAlteracaoValorNota = permitirAlteracaoValorNota;
	}

	public void verificarAvaliacaoOnlineMatriculaExistente(Integer codigoAvaliacaoOnline) {
		try {
			setPermitirAlteracaoValorNota(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().verificarAvaliacaoOnlineMatriculaExistente(codigoAvaliacaoOnline));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("questao", "Questão"));
		return itens;
	}

	public void limparCamposBusca() {
		getControleConsultaQuestoes().setListaConsulta(new ArrayList<>());
		setValorConsulta("");
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "questao";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}

	public ProgressBarVO getProgressBarVO() {
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	private boolean verificarPermissaoVisualizarAlunoTR_CA() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNotaa_VisualizarMatriculaTR_CA", getUsuarioLogadoClone());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean verificarPermiteLancarNotaDisciplinaComposta() {
		try {
			return getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("PermiteLancarNotaDisciplinaComposta", getUsuarioLogadoClone());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getIsFixarOpcaoEscolhaRegraDistribuicao() {
		return getIsNaoPermitirAlterarQuandoAtivado() ? getIsNaoPermitirAlterarQuandoAtivado() : getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE);
	}

	public void validarSelecaoPoliticaSelecaoQuestaoEnum() {	
		setSimulandoAvaliacao(false);
		if (getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)) {
			getAvaliacaoOnlineVO().setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO);
		}
		if (getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)) {
			getAvaliacaoOnlineVO().getRegraDistribuicaoQuestaoEnum().equals(RegraDistribuicaoQuestaoEnum.NENHUM);
			consultarTemaAssunto();
		}
		
	}

	public boolean getBloquearAlteracaoConformeRegraQuestoesAdicionadas() {
		return ((getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO) && !getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) && (getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.TURMA) || getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.DISCIPLINA)) && (Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getDisciplinaVO()) || Uteis.isAtributoPreenchido(getAvaliacaoOnlineVO().getTurmaVO())));
	}

	public String getAbrirModalAvisoAtivacaoPorTurma() {
		if (abrirModalAvisoAtivacaoPorTurma == null) {
			abrirModalAvisoAtivacaoPorTurma = "";
		}
		return abrirModalAvisoAtivacaoPorTurma;
	}

	public void setAbrirModalAvisoAtivacaoPorTurma(String abrirModalAvisoAtivacaoPorTurma) {
		this.abrirModalAvisoAtivacaoPorTurma = abrirModalAvisoAtivacaoPorTurma;
	}

	private void verificarPermissaoRecursoRandomizarQuestaoProfessor() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
			getAvaliacaoOnlineVO().setRandomizarApenasQuestoesCadastradasPeloProfessor(true);
		} catch (Exception e) {

		}
	}

	private void verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(true);
		} catch (Exception e) {
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(false);
		}
	}

	public Boolean getDesabilitarRecursoRandomizacaoQuestaoProfessor() {
		if (desabilitarRecursoRandomizacaoQuestaoProfessor == null) {
			desabilitarRecursoRandomizacaoQuestaoProfessor = false;
		}
		return desabilitarRecursoRandomizacaoQuestaoProfessor;
	}

	public void setDesabilitarRecursoRandomizacaoQuestaoProfessor(Boolean desabilitarRecursoRandomizacaoQuestaoProfessor) {
		this.desabilitarRecursoRandomizacaoQuestaoProfessor = desabilitarRecursoRandomizacaoQuestaoProfessor;
	}

	public void inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno() {
		try {
			setOncompleteModal("");
			limparMensagem();
			setSimulandoAvaliacao(true);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno(getAvaliacaoOnlineVO(), getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado());
			if(getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
				realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno();
			}else {										
				if(getUsuarioLogado().getIsApresentarVisaoProfessor() && !getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno(Uteis.getAnoDataAtual());
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(Uteis.getSemestreAtual());
					montarListaSelectItemTurma(true);										
				}
				consultarTemaAssunto();
				if(getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
					montarListaSelectItemConteudos();
				}
				setOncompleteModal("PF('panelSimularAvaliacao').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Boolean simulandoAvaliacao;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	

	public Boolean getSimulandoAvaliacao() {
		if (simulandoAvaliacao == null) {
			simulandoAvaliacao = false;
		}
		return simulandoAvaliacao;
	}

	public void setSimulandoAvaliacao(Boolean simulandoAvaliacao) {
		this.simulandoAvaliacao = simulandoAvaliacao;
	}
	
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	private List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoSimulacaoVOs;

	public List<AvaliacaoOnlineTemaAssuntoVO> getAvaliacaoOnlineTemaAssuntoSimulacaoVOs() {
		if (avaliacaoOnlineTemaAssuntoSimulacaoVOs == null) {
			avaliacaoOnlineTemaAssuntoSimulacaoVOs = new ArrayList<AvaliacaoOnlineTemaAssuntoVO>(0);
		}
		return avaliacaoOnlineTemaAssuntoSimulacaoVOs;
	}

	public void setAvaliacaoOnlineTemaAssuntoSimulacaoVOs(List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoSimulacaoVOs) {
		this.avaliacaoOnlineTemaAssuntoSimulacaoVOs = avaliacaoOnlineTemaAssuntoSimulacaoVOs;
	}

	public void realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno() {
		try {
			setMostrarGabarito(false);
			limparMensagem();
			setOncompleteModal("");
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(getAvaliacaoOnlineVO(), getMatriculaPeriodoTurmaDisciplinaVO(), getAvaliacaoOnlineTemaAssuntoSimulacaoVOs(), getUsuarioLogado(), getSimulandoAvaliacao()));
			setApresentarResultadoAvaliacaoOnline(false);			
			setOncompleteModal("PF('panelAvaliacaoOnlineMatricula').show();PF('panelSimularAvaliacao').hide()");
		} catch (ConsistirException e) {
			setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	private List<SelectItem> listaSelectItemProfessorSimularAvaliacao;
	
	public List<SelectItem> getListaSelectItemProfessorSimularAvaliacao() {
		if(listaSelectItemProfessorSimularAvaliacao == null) {
			listaSelectItemProfessorSimularAvaliacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessorSimularAvaliacao;
	}

	public void setListaSelectItemProfessorSimularAvaliacao(List<SelectItem> listaSelectItemProfessorSimularAvaliacao) {
		this.listaSelectItemProfessorSimularAvaliacao = listaSelectItemProfessorSimularAvaliacao;
	}

	public void consultarDefinicoesTutoriaOnlineTurmaDisciplina() {
		getListaSelectItemProfessorSimularAvaliacao().clear();
		if(getIsApresentarOpcaoSelecaoProfessorSimulacaoAvaliacao()){
			try {
				getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setCodigo(0);
				getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setNome("");
				DefinicoesTutoriaOnlineEnum definicoes = getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				if (definicoes == null || !definicoes.isProgramacaoAula()) {
					ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarProgramacaoTutoriaOnlinePorTurmaDisciplinaAnoSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getUsuarioLogado());
					if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO)) {
						List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs =  getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						for(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO: programacaoTutoriaOnlineProfessorVOs) {
							getListaSelectItemProfessorSimularAvaliacao().add(new SelectItem(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), programacaoTutoriaOnlineProfessorVO.getProfessor().getNome()));
							if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getProfessor())) {
								getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setCodigo(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
								getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setNome(programacaoTutoriaOnlineProfessorVO.getProfessor().getNome());
							}
						}
					}
				}
			} catch (Exception e) {
				
			}
		}
		if(getSimulandoAvaliacao() && !getAvaliacaoOnlineVO().getTipoUso().isTurma() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurma(true);
		}
	}
	
	public Boolean getIsApresentarOpcaoSelecaoProfessorSimulacaoAvaliacao() {
			
			return (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma()) 
			&& Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina()) 
			&& ((getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getAnual() && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getAno()))
				||(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getSemestral() && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getAno()) && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getSemestre()))
				|| (getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIntegral()))
			&& !getAvaliacaoOnlineVO().getUsoExclusivoProfessor() && getAvaliacaoOnlineVO().getRandomizarApenasQuestoesCadastradasPeloProfessor()
			&& !getUsuarioLogado().getIsApresentarVisaoProfessor());
		
	} 
	

	public void consultarTemaAssunto() {
		try {
			if(!getSimulandoAvaliacao()) {
				getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs().clear();
			}
			getAvaliacaoOnlineTemaAssuntoSimulacaoVOs().clear();
			if (getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().isRandomicoPorComplexidade() 
					&& ((getSimulandoAvaliacao() && (getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS) 
							|| getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)
							|| getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)))) 
					|| (!getSimulandoAvaliacao() && getAvaliacaoOnlineVO().getIsPermiteInformarTemaAssunto())) {
				if (getSimulandoAvaliacao()) {
					if (getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs().stream().anyMatch(t -> t.getSelecionado())) {
						getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs().forEach(t -> {
							if(t.getSelecionado()) {
								try {
									getAvaliacaoOnlineTemaAssuntoSimulacaoVOs().add((AvaliacaoOnlineTemaAssuntoVO)t.clone());
								} catch (CloneNotSupportedException e) {									
									e.printStackTrace();
								}
							}
						});
					} else {
						setAvaliacaoOnlineTemaAssuntoSimulacaoVOs(getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().consultarAvaliacaoOnlineTemaAssuntoAptoParaAvaliacaoOnline(getAvaliacaoOnlineVO(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getMatriculaPeriodoTurmaDisciplinaVO().getConteudo(), getUsuarioLogado()));
					}
				} else {
					getAvaliacaoOnlineVO().setAvaliacaoOnlineTemaAssuntoVOs(getFacadeFactory().getAvaliacaoOnlineTemaAssuntoInterfaceFacade().consultarAvaliacaoOnlineTemaAssuntoAptoParaAvaliacaoOnline(getAvaliacaoOnlineVO(), getAvaliacaoOnlineVO().getDisciplinaVO(), getAvaliacaoOnlineVO().getConteudoVO(), getUsuarioLogado()));
				}
			}
		} catch (Exception e) {

		}
	}
	

	public ControleConsulta getControleConsultaDisciplina() {
		if(controleConsultaDisciplina == null) {
			controleConsultaDisciplina =  new ControleConsulta();
			controleConsultaDisciplina.setCampoConsulta("nome");
		}
		return controleConsultaDisciplina;
	}

	public void setControleConsultaDisciplina(ControleConsulta controleConsultaDisciplina) {
		this.controleConsultaDisciplina = controleConsultaDisciplina;
	}
	
	


	public String getAbrirModalFinalizacaoAvaliacaoOnline() {
		if (getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino().compareTo(new Date()) <= 0 && getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
			return "PF('panelFinalizacao').show()";
		}
		return "";
	}

	public String getTempoApresentar() {
		return "Faltam " + Uteis.pegarTempoEntreDuasDatas(getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino(), new Date());
	}

	public boolean isHabilitarTempo() {
		return getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO) && getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual() > 0;
	}

	public Long tempoRestanteAvaliacaoOnline;
	public Long getTempoRestanteAvaliacaoOnline() {
		if (tempoRestanteAvaliacaoOnline == null) {
			tempoRestanteAvaliacaoOnline = getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual();
		}
		return tempoRestanteAvaliacaoOnline;
	}

	public void setTempoRestanteAvaliacaoOnline(Long tempoRestanteAvaliacaoOnline) {
		this.tempoRestanteAvaliacaoOnline = tempoRestanteAvaliacaoOnline;
	}
	
	public long getDataAtual() {
		return new Date().getTime();
	}
	
	public boolean getIsApresentarNDiasEntreAvaliacaoOnlineEVezesPodeRepetir() {
		return getAvaliacaoOnlineVO().getTipoUso().isTurma();
	}

	public boolean getIsDesativarCamposSeAtivo() {
		return getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.ATIVO) || getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.INATIVO);
	}

}
