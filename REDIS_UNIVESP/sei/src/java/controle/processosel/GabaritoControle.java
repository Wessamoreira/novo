package controle.processosel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoCalculoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.ColunaGabaritoVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("GabaritoControle")
@Scope("viewScope")
@Lazy
public class GabaritoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private GabaritoVO gabaritoVO;
	private GabaritoRespostaVO gabaritoRespostaVO;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private BigDecimal valorNotaPorQuestao;
	private List<CursoVO> listaConsultaCurso;

	protected List<SelectItem> listaSelectItemGradeCurricular;
	protected List<SelectItem> listaSelectItemPeriodoLetivo;
	protected List<SelectItem> listaSelectItemConfiguracaoAcademico;
	protected List<SelectItem> listaSelectItemVariavelNota;
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<SelectItem> listaSelectItemAreaConhecimento;	
	private List<SelectItem> listaSelectItemTurnoCurso;
	private List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo;
	private List<SelectItem> listaSelectItemGrupoDisciplinaProcessoSeletivo;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> listaSelectItemTipoGabarito;
	private List<SelectItem> listaSelectItemTipoRespostaGabarito;
	private List<SelectItem> listaSelectItemTipoCalculoGabarito;
	private List<SelectItem> tipoConsultaComboTurno;
	
	private String historicoAnulacao;

	public GabaritoControle() {
		inicializarDadosComboBox();
	}

	public void inicializarDadosComboBox() {
		inicializarDadosListaSelectItemUnidadelEnsino();
		montarListaSelectItemConfiguracaoAcademico();
		montarListaSelectItemGrupoDisciplinaProcessoSeletivo();
		montarListaSelectItemDisciplinaProcessoSeletivo();
	}

	public String novo() {
		setGabaritoVO(new GabaritoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoCons.xhtml");
	}

	public void inicializarDadosListaSelectItemUnidadelEnsino() {
		try {
			List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
			setMensagem("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String persistir() {
		try {
			getFacadeFactory().getGabaritoFacade().persistir(getGabaritoVO(), getUsuarioLogado());
			
			setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(getGabaritoVO().getCodigo(), getUsuarioLogado()));
			getFacadeFactory().getGabaritoFacade().editarColunaGabaritoResposta(getGabaritoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		}
	}

	public String clonar() {
		try {
			setGabaritoVO(getGabaritoVO().getClone());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getGabaritoFacade().excluir(getGabaritoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
		}
	}

	public void adicionarGabaritoResposta() {
		getFacadeFactory().getGabaritoFacade().adicionarGabaritoResposta(getGabaritoVO(), getValorNotaPorQuestao(), getUsuarioLogado());
		setMensagemID("msg_dados_adicionados");
	}

	public String editar() throws Exception {
		GabaritoVO obj = (GabaritoVO) context().getExternalContext().getRequestMap().get("gabaritoItens");
		setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), getUsuarioLogado()));
		getFacadeFactory().getGabaritoFacade().editarColunaGabaritoResposta(getGabaritoVO(), getUsuarioLogado());
		inicializarDadosComboBox();
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemPeriodoLetivo();
		montarListaSelectItemTurnoCurso();
		montarListaSelectItemVariavelNota();
		montarListaSelectItemTipoRespostaGabaritoDisciplina();
		montarListaSelectItemTipoRespostaGabaritoAreaConhecimento();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoForm.xhtml");
	}

	public String consultar() {
		try {
			setListaConsulta(getFacadeFactory().getGabaritoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("gabaritoCons.xhtml");
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getGabaritoVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getGabaritoVO().setCursoVO(obj);
			montarListaSelectItemGradeCurricular();
			montarListaSelectItemTurnoCurso();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			getGabaritoVO().setCursoVO(new CursoVO());
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemPeriodoLetivo().clear();
			getGabaritoVO().setGradeCurricularVO(null);
			getGabaritoVO().setPeriodoLetivoVO(null);
			setListaSelectItemTurnoCurso(new ArrayList<SelectItem>(0));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosProvaPresencial() {
		try {
			limparCurso();
			getGabaritoVO().setTurnoVO(null);
			getGabaritoVO().setConfiguracaoAcademicoVO(null);
			getListaSelectItemVariavelNota().clear();
			getGabaritoVO().setRealizarCalculoMediaLancamentoNota(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Valida se existe resultado processado processo seletivo prova presencial pelo gabarito selecionado.
	 * 
	 * @return true ou false
	 */
	public boolean existeResultadoProcessamentoRespostaPorGabarito() {
		try {
			if (Uteis.isAtributoPreenchido(getGabaritoVO())) {
				return getFacadeFactory().getProcessarResultadoProvaPresencialFacade().existeResultadoProcessamentoRespostaPorGabarito(getGabaritoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Boolean.FALSE;
	}

	/**
	 * Valida se existe resultado processado processo seletivo prova presencial pelo gabarito selecionado.
	 * 
	 * @return true ou false
	 */
	public boolean existeResultadoProcessamentoSeletivoPorGabarito() {
		try {
			if (Uteis.isAtributoPreenchido(getGabaritoVO())) {
				return getFacadeFactory().getInscricaoFacade().existeResultadoProcessamentoRespostaPorGabarito(getGabaritoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Boolean.FALSE;
	}

	/**
	 * Metodo responsavel por anular a questão do {@link GabaritoRespostaVO}.
	 * 
	 * @param gabaritoResposta
	 * @param anular
	 */
	public void anularQuestao(GabaritoRespostaVO gabaritoResposta ,boolean anular) {
		setGabaritoRespostaVO(gabaritoResposta);
		getGabaritoRespostaVO().setAnulado(anular);
		String historico = gabaritoResposta.getHistoricoAnulado() + 
					(Uteis.isAtributoPreenchido(gabaritoResposta.getHistoricoAnulado()) ? ";" : "" ) +
					getUsuarioLogado().getNome_Apresentar() + " - Data Alteração: " + UteisData.getDataComHoraAtual() + ";";
		getGabaritoRespostaVO().setHistoricoAnulado(historico);
	}

	/**
	 * Carrega o objeto {@link GabaritoRespostaVO} para visualização do
	 * historico dos funcionário que realizaram a alteração no Gabarito.
	 * 
	 * @param gabaritoResposta
	 */
	public void visualizarHistorico(GabaritoRespostaVO gabaritoResposta) {
		StringBuilder sb = new StringBuilder("");
		String[] teste = gabaritoResposta.getHistoricoAnulado().split(";");
		for (String valor : teste) {
			sb.append(valor + "<br />");
		}
		setGabaritoRespostaVO(gabaritoResposta);
		
		setHistoricoAnulacao(sb.toString());
	}
	
	public void confirmarAnularQuestao() {
		int indice = getGabaritoVO().getGabaritoRespostaVOs().indexOf(getGabaritoRespostaVO());
		getGabaritoVO().getGabaritoRespostaVOs().set(indice, getGabaritoRespostaVO());

		persistir();
	}

	public void selecionarTurno() throws Exception {
		try {
			TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turno");
			getGabaritoVO().setTurnoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparTurno() {
		getGabaritoVO().setTurnoVO(null);
	}

	public void selecionarGradeCurricular() {
		getGabaritoVO().setPeriodoLetivoVO(null);
		montarListaSelectItemPeriodoLetivo();
		montarComboBoxDeAcordoComTipoRespostaGabarito();
	}

	public void montarComboBoxDeAcordoComTipoRespostaGabarito() {
		if (!getGabaritoVO().getGradeCurricularVO().getCodigo().equals(0)) {
			if (getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {
				montarListaSelectItemTipoRespostaGabaritoDisciplina();
			} else {
				montarListaSelectItemTipoRespostaGabaritoAreaConhecimento();
			}
		}
	}

	public void montarListaSelectItemGradeCurricular() {
		try {
			List<GradeCurricularVO> resultadoConsulta = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getGabaritoVO().getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemPeriodoLetivo() {
		try {
			List<PeriodoLetivoVO> resultadoConsulta = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(getGabaritoVO().getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemPeriodoLetivo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(false, getUsuarioLogado());
			setListaSelectItemConfiguracaoAcademico(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemTipoRespostaGabaritoDisciplina() {
		try {
			List<DisciplinaVO> resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasPorGradeCurricularPeriodoLetivo(getGabaritoVO().getGradeCurricularVO().getCodigo(), getGabaritoVO().getPeriodoLetivoVO().getCodigo(), getUsuarioLogado());
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeDisciplinaPeriodoLetivo", true));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemTipoRespostaGabaritoAreaConhecimento() {
		try {
			List<AreaConhecimentoVO> resultadoConsulta = getFacadeFactory().getAreaConhecimentoFacade().consultarDisciplinasPorGradeCurricularPeriodoLetivo(getGabaritoVO().getGradeCurricularVO().getCodigo(), getGabaritoVO().getPeriodoLetivoVO().getCodigo(), getUsuarioLogado());
			setListaSelectItemAreaConhecimento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemVariavelNota() {
		try {
			List<String> resultadoConsulta = null;
			if (!getGabaritoVO().getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelNotaPorConfiguracaoAcademico(getGabaritoVO().getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado());
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				Iterator<String> j = resultadoConsulta.iterator();
				while (j.hasNext()) {
					String item = (String) j.next();
					objs.add(new SelectItem(item.toUpperCase().toString(), item.toUpperCase().toString()));
				}
				Collections.sort(objs, new SelectItemOrdemValor());
				setListaSelectItemVariavelNota(objs);
			} else {
				getListaSelectItemVariavelNota().clear();
			}

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public GabaritoVO getGabaritoVO() {
		if (gabaritoVO == null) {
			gabaritoVO = new GabaritoVO();
		}
		return gabaritoVO;
	}

	public void setGabaritoVO(GabaritoVO gabaritoVO) {
		this.gabaritoVO = gabaritoVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public Boolean getApresentarDadosProvaPresencial() {
		return getGabaritoVO().getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name());
	}

	public Boolean getApresentarDadosTipoRespostaGabaritoDisciplina() {
		return getGabaritoVO().getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name()) && getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name());
	}

	public Boolean getApresentarDadosTipoRespostaGabaritoAreaConhecimento() {
		return getGabaritoVO().getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name()) && getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.AREA_CONHECIMENTO.name());
	}

	public List<SelectItem> getListaSelectItemTipoGabarito() {
		if (listaSelectItemTipoGabarito == null) {
			listaSelectItemTipoGabarito = new ArrayList<SelectItem>(0);
			listaSelectItemTipoGabarito.add(new SelectItem(TipoGabaritoEnum.PROCESSO_SELETIVO, "Processo Seletivo"));
			listaSelectItemTipoGabarito.add(new SelectItem(TipoGabaritoEnum.PROVA_PRESENCIAL, "Prova Presencial"));

		}
		return listaSelectItemTipoGabarito;
	}

	public List<SelectItem> getListaSelectItemTipoRespostaGabarito() {
		if (listaSelectItemTipoRespostaGabarito == null) {
			listaSelectItemTipoRespostaGabarito = new ArrayList<SelectItem>(0);
			listaSelectItemTipoRespostaGabarito.add(new SelectItem(TipoRespostaGabaritoEnum.DISCIPLINA, "Disciplina"));
			listaSelectItemTipoRespostaGabarito.add(new SelectItem(TipoRespostaGabaritoEnum.AREA_CONHECIMENTO, "Área de Conhecimento"));

		}
		return listaSelectItemTipoRespostaGabarito;
	}

	public List<SelectItem> getListaSelectItemTipoCalculoGabarito() {
		listaSelectItemTipoCalculoGabarito = new ArrayList<SelectItem>(0);
		listaSelectItemTipoCalculoGabarito.add(new SelectItem(TipoCalculoGabaritoEnum.GERAL, "Geral"));
		if (getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {
			listaSelectItemTipoCalculoGabarito.add(new SelectItem(TipoCalculoGabaritoEnum.INDIVIDUAL_DISCIPLINA, "Individual por Disciplina"));
		} else {
			listaSelectItemTipoCalculoGabarito.add(new SelectItem(TipoCalculoGabaritoEnum.INDIVIDUAL_AREA_CONHECIMENTO, "Individual por Área de Conhecimento"));
		}

		return listaSelectItemTipoCalculoGabarito;
	}

	public List<SelectItem> getTipoConsultaComboTurno() {
		if (tipoConsultaComboTurno == null) {
			tipoConsultaComboTurno = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurno.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboTurno;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemConfiguracaoAcademico);
	}
	
	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public List<SelectItem> getListaSelectItemVariavelNota() {
		if (listaSelectItemVariavelNota == null) {
			listaSelectItemVariavelNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNota;
	}

	public void setListaSelectItemVariavelNota(List<SelectItem> listaSelectItemVariavelNota) {
		this.listaSelectItemVariavelNota = listaSelectItemVariavelNota;
	}

	public Boolean getDesabilitarTipoGabarito() {
		return !getGabaritoVO().getCodigo().equals(0);
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<SelectItem> getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List<SelectItem> listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public Boolean getApresentarAno() {
		return getGabaritoVO().getCursoVO().getPeriodicidade().equals("SE") || getGabaritoVO().getCursoVO().getPeriodicidade().equals("AN");
	}

	public Boolean getApresentarSemestre() {
		return getGabaritoVO().getCursoVO().getPeriodicidade().equals("SE");
	}

	public BigDecimal getValorNotaPorQuestao() {
		if (valorNotaPorQuestao == null) {
			valorNotaPorQuestao = BigDecimal.ZERO;
		}
		return valorNotaPorQuestao;
	}

	public void setValorNotaPorQuestao(BigDecimal valorNotaPorQuestao) {
		this.valorNotaPorQuestao = valorNotaPorQuestao;
	}

	public List<SelectItem> getListaSelectItemTurnoCurso() {
		if (listaSelectItemTurnoCurso == null) {
			listaSelectItemTurnoCurso = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurnoCurso;
	}

	public void setListaSelectItemTurnoCurso(List<SelectItem> listaSelectItemTurnoCurso) {
		this.listaSelectItemTurnoCurso = listaSelectItemTurnoCurso;
	}

	public void montarListaSelectItemTurnoCurso() {
		try {
			List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorCodigoCurso(getGabaritoVO().getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemTurnoCurso().add(new SelectItem(0, ""));
			setListaSelectItemTurnoCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemGrupoDisciplinaProcessoSeletivo() {
		try {
			List<GrupoDisciplinaProcSeletivoVO> grupoDisciplinaProcSeletivoVOs = getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGrupoDisciplinaProcessoSeletivo(null);
			if (getGabaritoVO().getNovoObj()) {
				setListaSelectItemDisciplinaProcessoSeletivo(null);
				getGabaritoVO().setGrupoDisciplinaProcSeletivoVO(null);
				for (ColunaGabaritoVO colunaGabaritoVO : getGabaritoVO().getColunaGabaritoVOs()) {
					for (GabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getGabaritoRespostaVOs()) {
						gabaritoRespostaVO.setDisciplinasProcSeletivoVO(null);
					}
				}
			}
			for (GrupoDisciplinaProcSeletivoVO obj : grupoDisciplinaProcSeletivoVOs) {
				getListaSelectItemGrupoDisciplinaProcessoSeletivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				if (!Uteis.isAtributoPreenchido(getGabaritoVO().getGrupoDisciplinaProcSeletivoVO())) {
					getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().setCodigo(obj.getCodigo());
					montarListaSelectItemDisciplinaProcessoSeletivo("");
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinaProcessoSeletivo() {
		try {
			montarListaSelectItemDisciplinaProcessoSeletivo("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarListaSelectItemDisciplinaProcessoSeletivo(String string) throws Exception {
		List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs = getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarDisciplinasGrupoDisciplinaProcSeletivos(getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), getUsuarioLogado());
		setListaSelectItemDisciplinaProcessoSeletivo(null);
		for (DisciplinasGrupoDisciplinaProcSeletivoVO obj : disciplinasGrupoDisciplinaProcSeletivoVOs) {
			getListaSelectItemDisciplinaProcessoSeletivo().add(new SelectItem(obj.getDisciplinasProcSeletivo().getCodigo(), obj.getDisciplinasProcSeletivo().getNome()));
		}
	}

	public List<SelectItem> getListaSelectItemDisciplinaProcessoSeletivo() {
		if (listaSelectItemDisciplinaProcessoSeletivo == null) {
			listaSelectItemDisciplinaProcessoSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaProcessoSeletivo;
	}

	public void setListaSelectItemDisciplinaProcessoSeletivo(List<SelectItem> listaSelectItemDisciplinaProcessoSeletivo) {
		this.listaSelectItemDisciplinaProcessoSeletivo = listaSelectItemDisciplinaProcessoSeletivo;
	}

	public List<SelectItem> getListaSelectItemGrupoDisciplinaProcessoSeletivo() {
		if (listaSelectItemGrupoDisciplinaProcessoSeletivo == null) {
			listaSelectItemGrupoDisciplinaProcessoSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGrupoDisciplinaProcessoSeletivo;
	}

	public void setListaSelectItemGrupoDisciplinaProcessoSeletivo(List<SelectItem> listaSelectItemGrupoDisciplinaProcessoSeletivo) {
		this.listaSelectItemGrupoDisciplinaProcessoSeletivo = listaSelectItemGrupoDisciplinaProcessoSeletivo;
	}

	public void limparDadosProcessoSeletivo() {
		getGabaritoVO().setControlarGabaritoPorDisciplina(false);
		if (getGabaritoVO().getTipoGabaritoEnum().equals(TipoGabaritoEnum.PROVA_PRESENCIAL)) {
			setListaSelectItemGrupoDisciplinaProcessoSeletivo(null);
			setListaSelectItemDisciplinaProcessoSeletivo(null);
			getGabaritoVO().setGrupoDisciplinaProcSeletivoVO(null);
			for (ColunaGabaritoVO colunaGabaritoVO : getGabaritoVO().getColunaGabaritoVOs()) {
				for (GabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getGabaritoRespostaVOs()) {
					gabaritoRespostaVO.setDisciplinasProcSeletivoVO(null);
				}
			}
		}
	}

	public GabaritoRespostaVO getGabaritoRespostaVO() {
		if (gabaritoRespostaVO == null) {
			gabaritoRespostaVO = new GabaritoRespostaVO();
		}
		return gabaritoRespostaVO;
	}

	public void setGabaritoRespostaVO(GabaritoRespostaVO gabaritoRespostaVO) {
		this.gabaritoRespostaVO = gabaritoRespostaVO;
	}

	public String getHistoricoAnulacao() {
		if (historicoAnulacao == null) {
			historicoAnulacao = "";
		}
		return historicoAnulacao;
	}

	public void setHistoricoAnulacao(String historicoAnulacao) {
		this.historicoAnulacao = historicoAnulacao;
	}	
}
