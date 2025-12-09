package relatorio.controle.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoAtaProvaRelVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoRedacaoRelVO;

@Controller("ProcessoSeletivoRedacaoRelControle")
@Scope("viewScope")
@Lazy
public class ProcessoSeletivoRedacaoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected String valorConsultaProcessoSeletivo;
	protected String campoConsultaProcessoSeletivo;
	protected List listaConsultaProcessoSeletivo;
	protected List listaSelectItemProcessoSeletivo;
	protected List listaSelectItemUnidadeEnsino;
	private ProcSeletivoVO procSeletivoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO;
	private ProcSeletivoCursoVO procSeletivoCursoVO;
	private List<SelectItem> listaSelectItemDataProva;
	private List<SelectItem> listaProcSeletivoCurso;	
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private List<SelectItem> listaSelectItemSala;
	private Integer sala;
	private Integer quantidadePagina;
	private ProcessoSeletivoRedacaoRelVO processoSeletivoRedacaoRelVO;
	private Integer qtdeLinhas;
	private List<SelectItem> listaSelectItemLayout;
	private List<SelectItem> listaSelectItemOrdenarPor;
	private String layout;	
	private Boolean ocultarLinha;
	private String textoOrientacaoRodape;
	private String ordenarPor;
	private List<InscricaoVO> listaConsultaInscricao;
	private String campoConsultaInscricao;
	private String valorConsultaInscricao;
	private InscricaoVO inscricaoVO;
	private String descricaoInscricao;
	
	
	public ProcessoSeletivoRedacaoRelControle() throws Exception {
		super();
		setValorConsultaProcessoSeletivo("");
		setCampoConsultaProcessoSeletivo("");
		montarListaSelectItemLayout();				
		montarListaSelectItemOrdenarPor();				
		inicializarListasSelectItemTodosComboBox();
		verificarLayoutPadrao();		
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
			List<ProcessoSeletivoRedacaoRelVO> linhasFin = getFacadeFactory().getInscricaoFacade().consultaInscricaoProcessoSeletivoRedacaoVOs(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(),  getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCurso().getCodigo(),  getSala(), getItemProcSeletivoDataProvaVO(), getQtdeLinhas(), getOrdenarPor(), getOcultarLinha(), getTextoOrientacaoRodape(), getInscricaoVO().getCodigo(), getUsuarioLogado());
			if (!linhasFin.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio("Folha de Redação");
				getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorioRedacao());
				getSuperParametroRelVO().setListaObjetos(linhasFin);	
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
				persistirLayoutRelatorio();
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_gerado");
			} else {
                setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
		}
	}

	private void persistirLayoutRelatorio() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao("textoOrientacaoRodape", "ProcessoSeletivoRedacaoRel", "textoOrientacaoRodape", 0, 0, 0, false, "", "", "", "", getTextoOrientacaoRodape(), getUsuarioLogado(), "", "");		
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getOcultarLinha().toString(), "ProcessoSeletivoRedacaoRel", "ocultarLinha", 0, 0, 0, false, "", "", "", "", "", getUsuarioLogado(), "", "");		
	}
	
	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = null;
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("ProcessoSeletivoRedacaoRel", "textoOrientacaoRodape", false, getUsuarioLogado());
		if (!layoutPadraoVO.getTextoCertidaoEstudo().equals("")) {
			setTextoOrientacaoRodape(layoutPadraoVO.getTextoCertidaoEstudo());
		}
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("ProcessoSeletivoRedacaoRel", "ocultarLinha", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setOcultarLinha(Boolean.valueOf(layoutPadraoVO.getValor()));
		}
	}
	
	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcessoSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcessoSeletivo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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

	public void montarListaSelectItemProcSeletivoCurso() {
		try {
			montarListaSelectItemSala();
			ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVOs = getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());			
			getListaProcSeletivoCurso().clear();
			getListaProcSeletivoCurso().add(new SelectItem(0, ""));
			for (ProcSeletivoCursoVO obj : procSeletivoUnidadeEnsinoVOs.getProcSeletivoCursoVOs()) {
				if(!getListaProcSeletivoCurso().contains(new SelectItem(obj.getUnidadeEnsinoCurso().getCurso().getCodigo(), obj.getUnidadeEnsinoCurso().getCurso().getNome()))){
					getListaProcSeletivoCurso().add(new SelectItem(obj.getUnidadeEnsinoCurso().getCurso().getCodigo(), obj.getUnidadeEnsinoCurso().getCurso().getNome()));
				}	
			}
		} catch (Exception e) {
			getListaProcSeletivoCurso().clear();
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
		limparCursos();
		limparTurnos();
		limparInscricao();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemProcSeletivoCurso();
		montarListaSelectItemSala();
		montarListaSelectItemLayout();		
		montarListaSelectItemOrdenarPor();		
		montarListaSelectItemDataProva();
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

	public void montarListaSelectItemLayout() {
		getListaSelectItemLayout().clear();
		getListaSelectItemLayout().add(new SelectItem("Redacaos", "Layout 1 - Ocorrências"));
		getListaSelectItemLayout().add(new SelectItem("AtaProva", "Layout 2 - Ata de Prova"));
	}
	
	public void montarListaSelectItemOrdenarPor() {
		getListaSelectItemOrdenarPor().clear();
		getListaSelectItemOrdenarPor().add(new SelectItem("candidato", "Candidato"));
		getListaSelectItemOrdenarPor().add(new SelectItem("dataProva", "Data Prova e Candidato"));
		getListaSelectItemOrdenarPor().add(new SelectItem("curso", "Curso e Candidato"));
		getListaSelectItemOrdenarPor().add(new SelectItem("sala", "Sala e Candidato"));
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
		objs.add(new SelectItem(0, ""));
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

	public void consultarInscricao() {
		try {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			List<InscricaoVO> objs = new ArrayList<InscricaoVO>(0);
			if (getCampoConsultaInscricao().equals("codigo")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
				}
				int valorInt = Integer.parseInt(getValorConsultaInscricao());
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("nomePessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("cpfPessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CPF do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCPFPessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaInscricao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarInscricao() {
		try {
			InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
			inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(inscricao.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			setInscricaoVO(inscricao);
			setDescricaoInscricao(getInscricaoVO().getCodigo() + " - " + getInscricaoVO().getCandidato().getNome());
//			getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(inscricao);
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
		} catch (Exception e) {
			setDescricaoInscricao(null);
			setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboInscricao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("cpfPessoa", "CPF do Candidato"));
		itens.add(new SelectItem("nomePessoa", "Nome do Candidato"));
		itens.add(new SelectItem("codigo", "Número da Inscrição"));
		return itens;
	}
	
	public void limparInscricao() {
		try {
			setDescricaoInscricao(null);
			setInscricaoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLayout;
	}
	
	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		if (listaSelectItemOrdenarPor == null) {
			listaSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemOrdenarPor;
	}
	
	public void setListaSelectItemOrdenarPor(List<SelectItem> listaSelectItemOrdenarPor) {
		this.listaSelectItemOrdenarPor = listaSelectItemOrdenarPor;
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

	public String getDesignIReportRelatorioRedacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoRedacaoRel.jrxml");
	}
	
	public String getLayout() {
		if (layout == null) {
			layout = "Redacaos";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
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
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoRedacaoRel.jrxml");
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

	public ProcessoSeletivoRedacaoRelVO getProcessoSeletivoRedacaoRelVO() {
		if (processoSeletivoRedacaoRelVO == null) {
			processoSeletivoRedacaoRelVO = new ProcessoSeletivoRedacaoRelVO(0, false);
		}
		return processoSeletivoRedacaoRelVO;
	}

	public void setProcessoSeletivoRedacaoRelVO(ProcessoSeletivoRedacaoRelVO processoSeletivoRedacaoRelVO) {
		this.processoSeletivoRedacaoRelVO = processoSeletivoRedacaoRelVO;
	}

	public Integer getQtdeLinhas() {
		if (qtdeLinhas == null)
			qtdeLinhas = 30;
		return qtdeLinhas;
	}

	public void setQtdeLinhas(Integer qtdeLinhas) {
		this.qtdeLinhas = qtdeLinhas;
	}

	public Boolean getOcultarLinha() {
		if (ocultarLinha == null) {
			ocultarLinha = Boolean.FALSE;
		}
		return ocultarLinha;
	}

	public void setOcultarLinha(Boolean ocultarLinha) {
		this.ocultarLinha = ocultarLinha;
	}

	public String getTextoOrientacaoRodape() {
		if (textoOrientacaoRodape == null) {			
	        StringBuilder sb = new StringBuilder();
//	        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
	        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	        sb.append("<html><head>");
	        sb.append("<meta charset=\"utf-8\">");
	        sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
	        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
	        sb.append("<title>Untitled document</title></head>");
	        sb.append("<body>");
	        sb.append("<p style=\"text-align: center;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CRIT&Eacute;RIOS DE AVALIA&Ccedil;&Atilde;O (valer&aacute; 2 pontos cada)</p>");
	        sb.append("<table width=\"70%\" style=\"margin-left: 5%;\">");
	        sb.append("<tr><td>____ &nbsp;- Domínio da norma padrão da língua portuguesa.</td></tr>");
	        sb.append("<tr><td>____ &nbsp;- Compreensão da proposta de redação.</td></tr>");
	        sb.append("<tr><td>____ &nbsp;- Seleção e organização das informações.</td></tr>");
	        sb.append("<tr><td>____ &nbsp;- Demonstração de conhecimento da língua necessária para argumentação do texto.</td></tr>");
	        sb.append("<tr><td>____ &nbsp;- Elaboração de uma proposta de solução para os problemas abordados, respeitando os valores e considerando as diversidades socioculturais.</td></tr>");
	        sb.append("</table>");
	        sb.append("<p style=\"text-align: left;\">&nbsp;</p>");
	        sb.append("</body></html>");
	        textoOrientacaoRodape = sb.toString();			
		}
		return textoOrientacaoRodape;
	}

	public void setTextoOrientacaoRodape(String textoOrientacaoRodape) {
		this.textoOrientacaoRodape = textoOrientacaoRodape;
	}

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "sala";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public List<SelectItem> getListaProcSeletivoCurso() {
		if (listaProcSeletivoCurso == null) {
			listaProcSeletivoCurso = new ArrayList<SelectItem>(0);
		}		
		return listaProcSeletivoCurso;
	}

	public void setListaProcSeletivoCurso(List<SelectItem> listaProcSeletivoCurso) {
		this.listaProcSeletivoCurso = listaProcSeletivoCurso;
	}

	public ProcSeletivoCursoVO getProcSeletivoCursoVO() {
		if (procSeletivoCursoVO == null) {
			procSeletivoCursoVO = new ProcSeletivoCursoVO();
		}
		return procSeletivoCursoVO;
	}

	public void setProcSeletivoCursoVO(ProcSeletivoCursoVO procSeletivoCursoVO) {
		this.procSeletivoCursoVO = procSeletivoCursoVO;
	}

	public String getDescricaoInscricao() {
		if (descricaoInscricao == null) {
			descricaoInscricao = "";
		}
		return descricaoInscricao;
	}

	public void setDescricaoInscricao(String descricaoInscricao) {
		this.descricaoInscricao = descricaoInscricao;
	}
	
	public void setListaConsultaInscricao(List<InscricaoVO> listaConsultaInscricao) {
		this.listaConsultaInscricao = listaConsultaInscricao;
	}

	public List<InscricaoVO> getListaConsultaInscricao() {
		if (listaConsultaInscricao == null) {
			listaConsultaInscricao = new ArrayList<InscricaoVO>(0);
		}
		return listaConsultaInscricao;
	}
	
	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}

	public void setCampoConsultaInscricao(String campoConsultaInscricao) {
		this.campoConsultaInscricao = campoConsultaInscricao;
	}

	public String getCampoConsultaInscricao() {
		if (campoConsultaInscricao == null) {
			campoConsultaInscricao = "nomeCandidato";
		}
		return campoConsultaInscricao;
	}

	public void setValorConsultaInscricao(String valorConsultaInscricao) {
		this.valorConsultaInscricao = valorConsultaInscricao;
	}

	public Boolean getDesabilitarInscricao() {
		return getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().intValue() != 0;
	}

	public String getValorConsultaInscricao() {
		return valorConsultaInscricao;
	}
}
