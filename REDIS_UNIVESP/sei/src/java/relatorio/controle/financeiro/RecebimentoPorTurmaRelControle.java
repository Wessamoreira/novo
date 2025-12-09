package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorTurmaRelVO;

@Controller("RecebimentoPorTurmaRelControle")
@Scope("viewScope")
public class RecebimentoPorTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<SelectItem> listaSelectItemOrdenacao;
	private List<SelectItem> listaSelectItemFiltrarPor;
	private Date dataInicio;
	private Date dataFim;
	private TurmaVO turma;
	private Integer ordenacao;
	private String tipoLayout;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean naoTrazerContasIsentas;
	private List<SelectItem> listaSelectItemParcelas;
	private String parcela;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private String filtrarPor;
	private boolean emitirApenasMatricula = false;

	public RecebimentoPorTurmaRelControle()  {
		verificarPermissaoEmitirApenasMatricula();
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("RecebimentoPorTurmaRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTipoReq() {
		try {
			boolean umSelecionad = false;
			for (TipoRequerimentoVO obj : getTipoReqVOs()) {
				if (obj.getFiltrarTipoReq()) {				
					umSelecionad = true;
				}
			}
			if (!umSelecionad) {
				getTipoReqVOs().clear();
				if (getUnidadeEnsinoLogado().getCodigo() > 0) {
					setTipoReqVOs(getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome("%", "AT", getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
					for (TipoRequerimentoVO obj : getTipoReqVOs()) {
						obj.setFiltrarTipoReq(true);
					}
				} else {
					setTipoReqVOs(getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome("%", "AT", getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
				}
				setMarcarTodosTipoReq(Boolean.TRUE);
				marcarTodosTipoReqAction();				
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}	

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void marcarTodosTipoReqAction() {
		for (TipoRequerimentoVO unidade : getTipoReqVOs()) {
			if (getMarcarTodosTipoReq()) {
				unidade.setFiltrarTipoReq(Boolean.TRUE);
			} else {
				unidade.setFiltrarTipoReq(Boolean.FALSE);
			}
		}
		//verificarTodasUnidadesSelecionadas();
	}

	public void verificarPermissaoEmitirApenasMatricula() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RecebimentoTurmaEmitirApenasMatricula", getUsuarioLogado());
			setEmitirApenasMatricula(true);
		} catch (Exception e) {
			setEmitirApenasMatricula(false);
		}
	}

	public void imprimirRelatorioExcel() {
		List<RecebimentoPorTurmaRelVO> listaRecebimento = null;
		try {
//			if (getFiltroRelatorioFinanceiroVO().getFiltrarPorDataCompetencia()) {
			if (getFiltrarPor().equals("dataCompetencia")) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			getTurma().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			listaRecebimento = getFacadeFactory().getRecebimentoPorTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getTipoReqVOs(), getFiltroRelatorioFinanceiroVO(), getDataInicio(), getDataFim(), getTurma(), getOrdenacao(), getTipoLayout(), getNaoTrazerContasIsentas(), getParcela(), getFiltrarPor(), getUsuarioLogado());
			if (!listaRecebimento.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRecebimentoPorTurmaRelFacade().getDesignIReportRelatorioExcel());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRecebimentoPorTurmaRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Recebimento por Turma");
				getSuperParametroRelVO().setListaObjetos(listaRecebimento);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRecebimentoPorTurmaRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));

				if (getTurma().getCodigo() > 0) {
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}

				if (ordenacao.equals(1)) {
					getSuperParametroRelVO().setOrdenadoPor("Data");
				} else {
					getSuperParametroRelVO().setOrdenadoPor("Aluno");
				}
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
//				removerObjetoMemoria(this);
//				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRecebimento);
		}
	}

	public void imprimirPDF() {
		List<RecebimentoPorTurmaRelVO> listaRecebimento = null;
		try {
			//if (getFiltroRelatorioFinanceiroVO().getFiltrarPorDataCompetencia()) {
			if (getFiltrarPor().equals("dataCompetencia")) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			getFacadeFactory().getRecebimentoPorTurmaRelFacade().validarDadosPeriodoRelatorioUnidadeEnsino(getUnidadeEnsinoVOs(), getTurma().getCodigo(), getDataInicio(), getDataFim());
			getTurma().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			listaRecebimento = getFacadeFactory().getRecebimentoPorTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getTipoReqVOs(), getFiltroRelatorioFinanceiroVO(), getDataInicio(), getDataFim(), getTurma(), getOrdenacao(), getTipoLayout(), getNaoTrazerContasIsentas(), getParcela(), getFiltrarPor(), getUsuarioLogado());
			if (!listaRecebimento.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRecebimentoPorTurmaRelFacade().getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRecebimentoPorTurmaRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Recebimento por Turma");
				getSuperParametroRelVO().setListaObjetos(listaRecebimento);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRecebimentoPorTurmaRelFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));

				if (getTurma().getCodigo() > 0) {
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}

				if (ordenacao.equals(1)) {
					getSuperParametroRelVO().setOrdenadoPor("Data");
				} else {
					getSuperParametroRelVO().setOrdenadoPor("Aluno");
				}
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
//				removerObjetoMemoria(this);
//				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRecebimento);
		}
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "recebimento", "designRecebimentoPorTurma", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getFiltrarPor(), "recebimento", "filtrarPor", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getOrdenacao().toString(), "recebimento", "ordenarPor", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getNaoTrazerContasIsentas().toString(), "recebimento", "naoTrazerContaIsenta", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "recebimento", getUsuarioLogado());
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox
	 * denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}

	}

	public void selecionarTurma() throws Exception {
		setTurma((TurmaVO) getRequestMap().get("turmaItens"));
		buscarParcelasParaImpressao();
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
	}

	public void buscarParcelasParaImpressao() {
//		try {
//			setListaSelectItemParcelas(new ArrayList<SelectItem>(0));
//			if (isEmitirApenasMatricula()) {
//				getListaSelectItemParcelas().add(new SelectItem("Matrícula", "Matrícula"));
//				return;
//			}
//			List<String> parcelas = getFacadeFactory().getBoletoBancarioRelFacade().executarConsultaParcelasTurma(getTurma().getCodigo(), getDataInicio(), getDataFim());
//			getListaSelectItemParcelas().add(new SelectItem("", ""));
//			if (Uteis.isAtributoPreenchido(parcelas)) {
//				for (String parcela : parcelas) {
//					getListaSelectItemParcelas().add(new SelectItem(parcela, parcela));
//				}
//			} else {
//				throw new ConsistirException("Não há nenhuma parcela para esse(a) determinado(a) pessoa/parceiro nesse período");
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	/**
	 * Método que ao selecionar uma pessoa para geração do histórico, verifica se já existe uma preferência de layout para determinado relatório.
	 * 
	 * @throws Exception
	 */
	private void verificarLayoutPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "recebimento", getUsuarioLogado());
			LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("recebimento", "designRecebimentoPorTurma", false, getUsuarioLogado());
			if (!layoutPadraoVO.getValor().equals("")) {
				setTipoLayout(layoutPadraoVO.getValor());
			}			
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("recebimento", "filtrarPor", false, getUsuarioLogado());
			if (!layoutPadraoVO.getValor().equals("")) {
				setFiltrarPor(layoutPadraoVO.getValor());
			}			
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("recebimento", "ordenarPor", false, getUsuarioLogado());
			if (!layoutPadraoVO.getValor().equals("")) {
				setOrdenacao(Integer.valueOf(layoutPadraoVO.getValor()));
			}			
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("recebimento", "naoTrazerContaIsenta", false, getUsuarioLogado());
			if (!layoutPadraoVO.getValor().equals("")) {
				setNaoTrazerContasIsentas(Boolean.valueOf(layoutPadraoVO.getValor()));
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void montarListaSelectItemOrdenacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, "Aluno"));
		itens.add(new SelectItem(1, "Data"));
		setListaSelectItemOrdenacao(itens);
	}

	public void montarListaSelectItemFiltrarPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dataRecebimento", "Data Recebimento"));
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		itens.add(new SelectItem("dataRecebimento", "Data Recebimento"));
		itens.add(new SelectItem("dataCompetencia", "Data Competência"));
		itens.add(new SelectItem("dataCompensacao", "Data Compensação"));
		setListaSelectItemFiltrarPor(itens);
	}

	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("RecebimentoPorTurmaRel", "Layout 1 - Sem Observação"));
		itens.add(new SelectItem("RecebimentoPorTurmaRelComObservacao", "Layout 2 - Com Observação"));
		itens.add(new SelectItem("RecebimentoPorTurmaRelNovo", "Layout 3"));
		return itens;
	}

	public void limparDadosTurma() {
		setTurma(new TurmaVO());
		setParcela("");
		setListaSelectItemParcelas(new ArrayList<SelectItem>(0));
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemFiltrarPor();
		consultarUnidadeEnsino();
		verificarLayoutPadrao();
		verificarHabilitarCamposFiltroRelatorioFinanceiro();
	}

	/**
	 * @return the listaConsultaTurma
	 */
	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public Integer getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = 0;
		}
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<SelectItem> getListaSelectItemOrdenacao() {
		if (listaSelectItemOrdenacao == null) {
			listaSelectItemOrdenacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemOrdenacao;
	}

	public void setListaSelectItemOrdenacao(List<SelectItem> listaSelectItemOrdenacao) {
		this.listaSelectItemOrdenacao = listaSelectItemOrdenacao;
	}

	public List<SelectItem> getListaSelectItemFiltrarPor() {
		if (listaSelectItemFiltrarPor == null) {
			listaSelectItemFiltrarPor = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFiltrarPor;
	}
	
	public void setListaSelectItemFiltrarPor(List<SelectItem> listaSelectItemFiltrarPor) {
		this.listaSelectItemFiltrarPor = listaSelectItemFiltrarPor;
	}
	
	public boolean getIsApresentarTipoLayout() {
		return Uteis.isAtributoPreenchido(getTurma());
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public Boolean getNaoTrazerContasIsentas() {
		if (naoTrazerContasIsentas == null) {
			naoTrazerContasIsentas = true;
		}
		return naoTrazerContasIsentas;
	}

	public void setNaoTrazerContasIsentas(Boolean naoTrazerContasIsentas) {
		this.naoTrazerContasIsentas = naoTrazerContasIsentas;
	}

	public List<SelectItem> getListaSelectItemParcelas() {
		if (listaSelectItemParcelas == null) {
			listaSelectItemParcelas = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemParcelas;
	}

	public void setListaSelectItemParcelas(List<SelectItem> listaSelectItemParcelas) {
		this.listaSelectItemParcelas = listaSelectItemParcelas;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}

	public boolean isEmitirApenasMatricula() {
		return emitirApenasMatricula;
	}

	public void setEmitirApenasMatricula(boolean emitirApenasMatricula) {
		this.emitirApenasMatricula = emitirApenasMatricula;
	}

	private void verificarHabilitarCamposFiltroRelatorioFinanceiro() {
		setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()));
		if (isEmitirApenasMatricula()) {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodosTipoOrigem();
			getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(true);
			getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(false);
		}
	}

	public Boolean getApresentarPorCompetencia() {
		if (getFiltrarPor().equals("dataCompetencia")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public String getFiltrarPor() {
		if (filtrarPor == null) {
			filtrarPor = "dataVencimento";
		}
		return filtrarPor;
	}

	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}

}