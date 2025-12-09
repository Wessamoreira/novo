package controle.faturamento.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO.EnumCampoConsultaUnidadeEnsinoCurso;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("ValoresCursoGinfesControle")
@Scope("viewScope")
@Lazy
public class ValoresCursoGinfesControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9072879546830986544L;
	private static final String CONTEXT_PARA_EDICAO = "unidadeEnsinoCursoItem";

	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoFiltros;
	private UnidadeEnsinoCursoValoresGinfesVO unidadeEnsinoCursoValoresGinfesVO;
	private String periodicidadeCurso;
	private Integer anoCompetenciaAtual;
	private Integer semestreCompetenciaAtual;
	private BigDecimal percentualReajustePorCompetencia;
	private boolean reajustePorCompetenciaGeral = true;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> comboboxPeriodicidadeEnum;
	private List<SelectItem> listaSelectItemNumeroPeriodoLetivo;

	public ValoresCursoGinfesControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(UnidadeEnsinoCursoVO.EnumCampoConsultaUnidadeEnsinoCurso.UNIDADEENSINO.name());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setDataFim(null);
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	public void editarUnidadeEnsinoCursoVO() {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO));
			getUnidadeEnsinoCursoVO().setListaUnidadeEnsinoCursoValoresGinfes(getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().consultaRapidaPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getUnidadeEnsinoCursoVO().setEdicaoManual(true);
			setReajustePorCompetenciaGeral(false);
			setPeriodicidadeCurso(getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade());
			montarListaSelectNumeroPeriodoLetivo();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void fecharUnidadeEnsinoCursoValoresGinfes() {
		try {
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			setReajustePorCompetenciaGeral(true);
			setPeriodicidadeCurso("AN");
			consultarDataScrollerPorFiltro();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void persistir() {
		try {
			getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().persistir(getUnidadeEnsinoCursoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void reajustarValoresCursoGinfes() {
		try {
			if(!isReajustePorCompetenciaGeral()) {
				getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().reajustarValoresCursoGinfes(getUnidadeEnsinoCursoVO(), getAnoCompetenciaAtual(), getSemestreCompetenciaAtual(),getPercentualReajustePorCompetencia(), getPeriodicidadeCurso(), isReajustePorCompetenciaGeral(), true, getUsuarioLogado());
			}else {
				getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().reajustarValoresCursoGinfes(getUnidadeEnsinoCursoFiltros(), getAnoCompetenciaAtual(), getSemestreCompetenciaAtual(),getPercentualReajustePorCompetencia(), getPeriodicidadeCurso(), isReajustePorCompetenciaGeral(), true, getUsuarioLogado());	
			}
			
			setAnoCompetenciaAtual(0);
			setSemestreCompetenciaAtual(0);
			setPercentualReajustePorCompetencia(BigDecimal.ZERO);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarUnidadeEnsinoCursoValoresGinfes() {
		try {
			getUnidadeEnsinoCursoValoresGinfesVO().setUnidadeEnsinoCursoVO(getUnidadeEnsinoCursoVO());
			getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().adicionarUnidadeEnsinoCursoValoresGinfes(getUnidadeEnsinoCursoVO().getListaUnidadeEnsinoCursoValoresGinfes(), getUnidadeEnsinoCursoValoresGinfesVO(), getUsuarioLogado());
			setUnidadeEnsinoCursoValoresGinfesVO(new UnidadeEnsinoCursoValoresGinfesVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarUnidadeEnsinoCursoValoresGinfes() {
		try {
			setUnidadeEnsinoCursoValoresGinfesVO((UnidadeEnsinoCursoValoresGinfesVO) context().getExternalContext().getRequestMap().get("valoresCursoGinfesItens"));
			getUnidadeEnsinoCursoValoresGinfesVO().setEdicaoManual(true);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerUnidadeEnsinoCursoValoresGinfes() {
		try {
			UnidadeEnsinoCursoValoresGinfesVO obj = (UnidadeEnsinoCursoValoresGinfesVO) context().getExternalContext().getRequestMap().get("valoresCursoGinfesItens");
			getFacadeFactory().getUnidadeEnsinoCursoValoresGinfesFacade().removerUnidadeEnsinoCursoValoresGinfes(getUnidadeEnsinoCursoVO().getListaUnidadeEnsinoCursoValoresGinfes(), obj, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getControleConsultaOtimizado(), getUnidadeEnsinoCursoFiltros());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDataScrollerPorFiltro() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		consultarDados();
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getUnidadeEnsinoCursoFiltros().setUnidadeEnsino(obj.getCodigo());
				getUnidadeEnsinoCursoFiltros().setNomeUnidadeEnsino(obj.getNome());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoCursoFiltros().getUnidadeEnsino(), getUnidadeEnsinoCursoFiltros().getNomeUnidadeEnsino()));
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectNumeroPeriodoLetivo() {
		try {
			getListaSelectItemNumeroPeriodoLetivo().clear();
			getListaSelectItemNumeroPeriodoLetivo().add(new SelectItem(0, ""));
			Integer maiorNumeroPeriodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarMaiorNumeroPeriodoLetivoPorCurso(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (int i = 1; i <= maiorNumeroPeriodoLetivo; i++) {
				getListaSelectItemNumeroPeriodoLetivo().add(new SelectItem(i, String.valueOf(i)));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboUnidadeEnsinoCurso() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(EnumCampoConsultaUnidadeEnsinoCurso.UNIDADEENSINO.name(), UteisJSF.internacionalizar("enum_EnumCampoConsultaUnidadeEnsinoCurso_UnidadeEnsino")));
		return itens;
	}
	
	public List<SelectItem> getComboboxPeriodicidadeEnum() {
		if (comboboxPeriodicidadeEnum == null) {
			comboboxPeriodicidadeEnum = new ArrayList<>(0);			
			comboboxPeriodicidadeEnum.add(new SelectItem("AN", "Anual"));
			comboboxPeriodicidadeEnum.add(new SelectItem("SE", "Semestral"));
		}
		return comboboxPeriodicidadeEnum;
	}
	
	public void setComboboxPeriodicidadeEnum(List<SelectItem> comboboxPeriodicidadeEnum) {
		this.comboboxPeriodicidadeEnum = comboboxPeriodicidadeEnum;
	}


	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoFiltros() {
		if (unidadeEnsinoCursoFiltros == null) {
			unidadeEnsinoCursoFiltros = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoFiltros;
	}

	public void setUnidadeEnsinoCursoFiltros(UnidadeEnsinoCursoVO unidadeEnsinoCursoFiltros) {
		this.unidadeEnsinoCursoFiltros = unidadeEnsinoCursoFiltros;
	}

	public UnidadeEnsinoCursoValoresGinfesVO getUnidadeEnsinoCursoValoresGinfesVO() {
		if (unidadeEnsinoCursoValoresGinfesVO == null) {
			unidadeEnsinoCursoValoresGinfesVO = new UnidadeEnsinoCursoValoresGinfesVO();
		}
		return unidadeEnsinoCursoValoresGinfesVO;
	}

	public void setUnidadeEnsinoCursoValoresGinfesVO(UnidadeEnsinoCursoValoresGinfesVO unidadeEnsinoCursoValoresGinfesVO) {
		this.unidadeEnsinoCursoValoresGinfesVO = unidadeEnsinoCursoValoresGinfesVO;
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
	
	

	public List<SelectItem> getListaSelectItemNumeroPeriodoLetivo() {
		if (listaSelectItemNumeroPeriodoLetivo == null) {
			listaSelectItemNumeroPeriodoLetivo = new ArrayList<>();
		}
		return listaSelectItemNumeroPeriodoLetivo;
	}

	public void setListaSelectItemNumeroPeriodoLetivo(List<SelectItem> listaSelectItemNumeroPeriodoLetivo) {
		this.listaSelectItemNumeroPeriodoLetivo = listaSelectItemNumeroPeriodoLetivo;
	}

	public Integer getAnoCompetenciaAtual() {
		if (anoCompetenciaAtual == null) {
			anoCompetenciaAtual = 0;
		}
		return anoCompetenciaAtual;
	}

	public void setAnoCompetenciaAtual(Integer anoCompetenciaAtual) {
		this.anoCompetenciaAtual = anoCompetenciaAtual;
	}

	public Integer getSemestreCompetenciaAtual() {
		if (semestreCompetenciaAtual == null) {
			semestreCompetenciaAtual = 0;
		}
		return semestreCompetenciaAtual;
	}

	public void setSemestreCompetenciaAtual(Integer semestreCompetenciaAtual) {
		this.semestreCompetenciaAtual = semestreCompetenciaAtual;
	}

	public BigDecimal getPercentualReajustePorCompetencia() {
		if (percentualReajustePorCompetencia == null) {
			percentualReajustePorCompetencia = BigDecimal.ZERO;
		}
		return percentualReajustePorCompetencia;
	}

	public void setPercentualReajustePorCompetencia(BigDecimal percentualReajustePorCompetencia) {
		this.percentualReajustePorCompetencia = percentualReajustePorCompetencia;
	}

	public boolean isReajustePorCompetenciaGeral() {
		return reajustePorCompetenciaGeral;
	}
	
	public void setReajustePorCompetenciaGeral(boolean reajustePorCompetenciaGeral) {
		this.reajustePorCompetenciaGeral = reajustePorCompetenciaGeral;
	}

	public String getPeriodicidadeCurso() {
		if (periodicidadeCurso == null) {
			periodicidadeCurso = "AN";
		}
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}
	
	public boolean isPeriodicidadeSemestre() {
		return getPeriodicidadeCurso().equals("SE");
	}
	
	
	
	

}
