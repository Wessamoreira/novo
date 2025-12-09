package relatorio.controle.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoAtaProvaRelVO;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoOcorrenciaRelVO;

@Controller("ProcessoSeletivoOcorrenciaRelControle")
@Scope("viewScope")
@Lazy
public class ProcessoSeletivoOcorrenciaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected String valorConsultaProcessoSeletivo;
	protected String campoConsultaProcessoSeletivo;
	protected List listaConsultaProcessoSeletivo;
	protected List listaSelectItemProcessoSeletivo;
	protected List listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemLayout;	
	private ProcSeletivoVO procSeletivoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO;
	private List<SelectItem> listaSelectItemDataProva;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private List<SelectItem> listaSelectItemSala;
	private Integer sala;
	private Integer quantidadePagina;
	private ProcessoSeletivoOcorrenciaRelVO processoSeletivoOcorrenciaRelVO;
	private Integer qtdeLinhas;
	private String layout;	

	public ProcessoSeletivoOcorrenciaRelControle() throws Exception {
		super();
		setValorConsultaProcessoSeletivo("");
		setCampoConsultaProcessoSeletivo("");
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemLayout();				
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void gerarRelatorioPdf() throws Exception {
		gerarRelatorio(TipoRelatorioEnum.PDF);
	}

	public void gerarRelatorio(TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
		try {
			if (getProcSeletivoVO().getCodigo() == 0) {
				throw new Exception("O Campo PROCESSO SELETIVO deve ser selecionado.");
			}
						
			if (getLayout().equals("Ocorrencias")) {
				List<ProcessoSeletivoOcorrenciaRelVO> linhas = new ArrayList<ProcessoSeletivoOcorrenciaRelVO>(0);
				Integer totalLinhas = getQtdeLinhas() * 32;
				for (int x = 0; x < totalLinhas; x++) {
					linhas.add(new ProcessoSeletivoOcorrenciaRelVO());
				}
				getSuperParametroRelVO().setTituloRelatorio("Ocorrência Processo Seletivo");
				getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorio());
				getSuperParametroRelVO().setListaObjetos(linhas);
			} else {
				List<ProcessoSeletivoAtaProvaRelVO> linhas = getFacadeFactory().getInscricaoFacade().consultaInscricaoProcessoSeletivoAtaProvaVOs(getProcSeletivoVO().getCodigo(), getSala(), getItemProcSeletivoDataProvaVO(), getQtdeLinhas(), "", getUsuarioLogado());
				getSuperParametroRelVO().setTituloRelatorio("Ata de Prova");
				getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorioAtaProva());
				getSuperParametroRelVO().setListaObjetos(linhas);
			}
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros("");
			getSuperParametroRelVO().adicionarParametro("processoSeletivo", getProcSeletivoVO().getDescricao());
			if (getSala() > 0) {
				SalaLocalAulaVO sala = getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala());
				getSuperParametroRelVO().adicionarParametro("sala", sala.getSala());
			} else {
				getSuperParametroRelVO().adicionarParametro("sala", "TODAS");
			}
			if (getUnidadeEnsinoVO().getCodigo() > 0) {
				UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsino", unidadeEnsino.getNome());
				getSuperParametroRelVO().adicionarParametro("endereco", (unidadeEnsino.getEndereco() + ", nº" + unidadeEnsino.getNumero() + ", " + unidadeEnsino.getSetor() + ", " + unidadeEnsino.getCidade().getNome() + "-" + unidadeEnsino.getCidade().getEstado().getSigla() + " - CEP: " + unidadeEnsino.getCEP() + " - Fone: " + unidadeEnsino.getTelComercial1()).toString());
				getSuperParametroRelVO().adicionarParametro("site", unidadeEnsino.getSite());
			} else {
				getSuperParametroRelVO().adicionarParametro("unidadeEnsino", "TODAS");
			}
			if (getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
				ItemProcSeletivoDataProvaVO dataProva = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), 0, getUsuarioLogado());
				getSuperParametroRelVO().adicionarParametro("dataProva", dataProva.getDataProva_Apresentar());
			} else {
				getSuperParametroRelVO().adicionarParametro("dataProva", "TODAS");
			}
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_gerado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
		}
	}

	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcessoSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcessoSeletivo(),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcessoSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcessoSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void montarListaUltimosProcSeletivos() {
		try {
			setListaConsultaProcessoSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcessoSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDataProva() {
		try {
			List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemDataProva().clear();
			getListaSelectItemDataProva().add(new SelectItem(0, ""));
			for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
				getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
			}
		} catch (Exception e) {

		}

	}

	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcessoSeletivo().equals("dataInicio") || getCampoConsultaProcessoSeletivo().equals("dataFim") || getCampoConsultaProcessoSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'this.id','99/99/9999',event);";
		}
		return "";
	}

	public List getTipoConsultaComboProcSeletivo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}

	public void selecionarProcSeletivo() {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		setProcSeletivoVO(obj);
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemSala();
		montarListaSelectItemLayout();		
		montarListaSelectItemDataProva();
	}

	public void montarListaSelectItemLayout() {
		getListaSelectItemLayout().clear();
		getListaSelectItemLayout().add(new SelectItem("Ocorrencias", "Layout 1 - Ocorrências"));
		getListaSelectItemLayout().add(new SelectItem("AtaProva", "Layout 2 - Ata de Prova"));
	}
	
	public void montarListaSelectItemSala() {
		setSala(-1);
		List<SalaLocalAulaVO> salaVOs = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivo(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), 0, getUsuarioLogado());
		getListaSelectItemSala().clear();
		getListaSelectItemSala().add(new SelectItem(-1, "Todas"));
		getListaSelectItemSala().add(new SelectItem(0, "Sem Sala"));
		for (SalaLocalAulaVO sala : salaVOs) {
			getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getSala()));
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		if (getProcSeletivoVO().getCodigo().intValue() == 0) {
			setListaSelectItemUnidadeEnsino(new ArrayList(0));
			return;
		}
		resultadoConsulta = consultarProcessoSeletivoUnidadeEnsino();
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		while (i.hasNext()) {
			ProcSeletivoUnidadeEnsinoVO proc = (ProcSeletivoUnidadeEnsinoVO) i.next();
			ProcSeletivoUnidadeEnsino.montarDadosUnidadeEnsino(proc, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			objs.add(new SelectItem(proc.getUnidadeEnsino().getCodigo(), proc.getUnidadeEnsino().getNome()));
		}
		setListaSelectItemUnidadeEnsino(objs);
	}

	public List consultarProcessoSeletivoUnidadeEnsino() throws Exception {
		List lista = ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemDataProva();
	}

	public void limparDados() {
		getProcSeletivoVO().setCodigo(0);
		getProcSeletivoVO().setDescricao("");
		inicializarListasSelectItemTodosComboBox();
	}

	public String getValorConsultaProcessoSeletivo() {
		if (valorConsultaProcessoSeletivo == null) {
			valorConsultaProcessoSeletivo = "";
		}
		return valorConsultaProcessoSeletivo;
	}

	public void setValorConsultaProcessoSeletivo(String valorConsultaProcessoSeletivo) {
		this.valorConsultaProcessoSeletivo = valorConsultaProcessoSeletivo;
	}

	public String getCampoConsultaProcessoSeletivo() {
		if (campoConsultaProcessoSeletivo == null) {
			campoConsultaProcessoSeletivo = "";
		}
		return campoConsultaProcessoSeletivo;
	}

	public void setCampoConsultaProcessoSeletivo(String campoConsultaProcessoSeletivo) {
		this.campoConsultaProcessoSeletivo = campoConsultaProcessoSeletivo;
	}

	public List getListaConsultaProcessoSeletivo() {
		if (listaConsultaProcessoSeletivo == null) {
			listaConsultaProcessoSeletivo = new ArrayList(0);
		}
		return listaConsultaProcessoSeletivo;
	}

	public void setListaConsultaProcessoSeletivo(List listaConsultaProcessoSeletivo) {
		this.listaConsultaProcessoSeletivo = listaConsultaProcessoSeletivo;
	}

	public List getListaSelectItemProcessoSeletivo() {
		if (listaSelectItemProcessoSeletivo == null) {
			listaSelectItemProcessoSeletivo = new ArrayList(0);
		}
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public List<SelectItem> getListaSelectItemDataProva() {
		if (listaSelectItemDataProva == null) {
			listaSelectItemDataProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDataProva;
	}

	public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
		this.listaSelectItemDataProva = listaSelectItemDataProva;
	}

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	public List<SelectItem> getListaSelectItemSala() {
		if (listaSelectItemSala == null) {
			listaSelectItemSala = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSala;
	}

	public void setListaSelectItemSala(List<SelectItem> listaSelectItemSala) {
		this.listaSelectItemSala = listaSelectItemSala;
	}

	public Integer getSala() {
		if (sala == null) {
			sala = 0;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}

	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoOcorrenciaRel.jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public ProcSeletivoUnidadeEnsinoVO getProcSeletivoUnidadeEnsinoVO() {
		if (procSeletivoUnidadeEnsinoVO == null) {
			procSeletivoUnidadeEnsinoVO = new ProcSeletivoUnidadeEnsinoVO();
		}
		return procSeletivoUnidadeEnsinoVO;
	}

	public void setProcSeletivoUnidadeEnsinoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO) {
		this.procSeletivoUnidadeEnsinoVO = procSeletivoUnidadeEnsinoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Integer getQuantidadePagina() {
		if (quantidadePagina == null) {
			quantidadePagina = 1;
		}
		return quantidadePagina;
	}

	public void setQuantidadePagina(Integer quantidadePagina) {
		this.quantidadePagina = quantidadePagina;
	}

	public ProcessoSeletivoOcorrenciaRelVO getProcessoSeletivoOcorrenciaRelVO() {
		if (processoSeletivoOcorrenciaRelVO == null) {
			processoSeletivoOcorrenciaRelVO = new ProcessoSeletivoOcorrenciaRelVO();
		}
		return processoSeletivoOcorrenciaRelVO;
	}

	public void setProcessoSeletivoOcorrenciaRelVO(ProcessoSeletivoOcorrenciaRelVO processoSeletivoOcorrenciaRelVO) {
		this.processoSeletivoOcorrenciaRelVO = processoSeletivoOcorrenciaRelVO;
	}

	public Integer getQtdeLinhas() {
		if (qtdeLinhas == null)
			qtdeLinhas = 1;
		return qtdeLinhas;
	}

	public void setQtdeLinhas(Integer qtdeLinhas) {
		this.qtdeLinhas = qtdeLinhas;
	}
	
	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLayout;
	}
	
	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}
	
	public String getDesignIReportRelatorioAtaProva() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoAtaProvaRel.jrxml");
	}

	public String getLayout() {
		if (layout == null) {
			layout = "Ocorrencias";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
}
