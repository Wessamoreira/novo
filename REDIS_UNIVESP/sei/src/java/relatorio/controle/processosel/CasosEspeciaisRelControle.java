package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.ProcSeletivoCurso;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.processosel.CasosEspeciaisRel;
import relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRel;

@Controller("CasosEspeciaisRelControle")
@Scope("viewScope")
@Lazy
public class CasosEspeciaisRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -6225529176168266279L;
	
	private ProcSeletivoVO procSeletivoVO;
	private List<SelectItem> listaSelectItemDataProva;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	protected String valorConsultaProcSeletivo;
	protected String campoConsultaProcSeletivo;
	protected List listaSelectItemUnidadeEnsino;
	protected List listaConsultaProcSeletivo;
	protected List listaSelectItemProcessoSeletivo;
	protected List listaSelectItemCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private List<SelectItem> listaSelectItemSala;
	private Integer sala;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	List<SelectItem> tipoConsultaComboCurso;
	private Boolean canhoto;
	private Boolean gravida;
	private Boolean necessidadeEspecial;
	
	public CasosEspeciaisRelControle() throws Exception {
		setValorConsultaProcSeletivo("");
		setCampoConsultaProcSeletivo("");
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public void imprimirObjetoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getCasosEspeciaisRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getCanhoto(), getGravida(), getNecessidadeEspecial(), getItemProcSeletivoDataProvaVO(), getSala() );
			nomeRelatorio = CasosEspeciaisRel.getIdEntidade();
			titulo = "Relatório de Casos Especiais";
			design = getFacadeFactory().getCasosEspeciaisRelFacade().getDesignIReportRelatorio();
			caminho = getFacadeFactory().getCasosEspeciaisRelFacade().getCaminhoBaseRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				StringBuilder filtros = new StringBuilder("");
				if(getCanhoto()) {
					filtros.append("Canhoto, ");
				}
				if(getGravida()) {
					filtros.append("Gravida, ");
				}
				if(getNecessidadeEspecial()) {
					filtros.append("Necessidades Especiais, ");
				}
				getSuperParametroRelVO().setFiltros(filtros.toString());
				getSuperParametroRelVO().setProcessoSeletivo(getProcSeletivoVO().getDescricao());
				
				if (getUnidadeEnsinoCursoVO().getCurso().getNome().equals("")) {
					getSuperParametroRelVO().setCurso("Todos");
				} else {
					getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				}
				getSuperParametroRelVO().setDataProva(Uteis.getData(getItemProcSeletivoDataProvaVO().getDataProva(), "dd/MM/yyyy HH:MM"));
				if (getSala() == -1) {
					getSuperParametroRelVO().setSala("Todas");
				} else if (getSala() == 0) {
					getSuperParametroRelVO().setSala("Sem Sala");
				} else {
					getSuperParametroRelVO().setSala(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala()).getSala());
				}
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setValorConsultaProcSeletivo("");
				setCampoConsultaProcSeletivo("");
				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFazerDownload(false);
		} finally {
			caminho = null;
			design = null;
			titulo = null;
			nomeRelatorio = null;
			listaRegistro = null;
		}
	}
	
	public void imprimirExcel() {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);
		try {
			listaRegistro = getFacadeFactory().getCasosEspeciaisRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getCanhoto(), getGravida(), getNecessidadeEspecial(), getItemProcSeletivoDataProvaVO(), getSala());
			nomeRelatorio = CasosEspeciaisRel.getIdEntidade();
			titulo = "Relatório de Casos Especiais";
			design = getFacadeFactory().getCasosEspeciaisRelFacade().getDesignIReportRelatorio();
			caminho = getFacadeFactory().getCasosEspeciaisRelFacade().getCaminhoBaseRelatorio();
			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				StringBuilder filtros = new StringBuilder("");
				if(getCanhoto()) {
					filtros.append("Canhoto, ");
				}
				if(getGravida()) {
					filtros.append("Gravida, ");
				}
				if(getNecessidadeEspecial()) {
					filtros.append("Necessidades Especiais, ");
				}
				getSuperParametroRelVO().setFiltros(filtros.toString());
				getSuperParametroRelVO().setProcessoSeletivo(getProcSeletivoVO().getDescricao());
				
				if (getUnidadeEnsinoCursoVO().getCurso().getNome().equals("")) {
					getSuperParametroRelVO().setCurso("Todos");
				} else {
					getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				}
				getSuperParametroRelVO().setDataProva(Uteis.getData(getItemProcSeletivoDataProvaVO().getDataProva(), "dd/MM/yyyy HH:MM"));
				if (getSala() == -1) {
					getSuperParametroRelVO().setSala("Todas");
				} else if (getSala() == 0) {
					getSuperParametroRelVO().setSala("Sem Sala");
				} else {
					getSuperParametroRelVO().setSala(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala()).getSala());
				}				
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setValorConsultaProcSeletivo("");
				setCampoConsultaProcSeletivo("");
				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			caminho = null;
			design = null;
			titulo = null;
			nomeRelatorio = null;
			listaRegistro = null;
		}
	}
	
	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(),  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void montarListaUltimosProcSeletivos() {
		try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getProcSeletivoInscricoesRelFacade().consultarCurso(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getProcSeletivoVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
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
		montarListaSelectItemDataProva();
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
		try {
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
				ProcSeletivoUnidadeEnsino.montarDadosUnidadeEnsino(proc, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				objs.add(new SelectItem(proc.getUnidadeEnsino().getCodigo(), proc.getUnidadeEnsino().getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List consultarProcessoSeletivoUnidadeEnsino() throws Exception {
		List lista = ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}
	
	public List consultarProcessoSeletivoUnidadeEnsinoCurso() throws Exception {
		ProcSeletivoUnidadeEnsinoVO obj = getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		List lista = ProcSeletivoCurso.consultarProcSeletivoCursos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}
	
	public void montarListaSelectItemSala() {
		setSala(-1);
		List<SalaLocalAulaVO> salaVOs = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivo(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getUsuarioLogado());
		getListaSelectItemSala().clear();
		getListaSelectItemSala().add(new SelectItem(-1, "Todas"));
		getListaSelectItemSala().add(new SelectItem(0, "Sem Sala"));		
		for (SalaLocalAulaVO sala : salaVOs) {					
			getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getSala()));			
		}
	}
	
	public void limparDadosCurso() {
		getUnidadeEnsinoCursoVO().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getUnidadeEnsinoCursoVO().getTurno().setNome("");
		montarListaSelectItemSala();
	}
	
	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setUnidadeEnsinoCursoVO(obj);
			montarListaSelectItemSala();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}
	
	public boolean getIsApresentraUnidadeEnsinoCurso() {
		return getProcSeletivoVO().getCodigo() != 0;
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

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public String getCampoConsultaProcSeletivo() {
		if(campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public List getListaConsultaProcSeletivo() {
		if(listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}

	public List getListaSelectItemProcessoSeletivo() {
		if(listaSelectItemProcessoSeletivo == null) {
			listaSelectItemProcessoSeletivo = new ArrayList(0);
		}
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public Boolean getCanhoto() {
		if (canhoto == null) {
			canhoto = Boolean.FALSE;
		}
		return canhoto;
	}

	public void setCanhoto(Boolean canhoto) {
		this.canhoto = canhoto;
	}

	public Boolean getGravida() {
		if (gravida == null) {
			gravida = Boolean.FALSE;
		}
		return gravida;
	}

	public void setGravida(Boolean gravida) {
		this.gravida = gravida;
	}

	public Boolean getNecessidadeEspecial() {
		if (necessidadeEspecial == null) {
			necessidadeEspecial = Boolean.FALSE;
		}
		return necessidadeEspecial;
	}

	public void setNecessidadeEspecial(Boolean necessidadeEspecial) {
		this.necessidadeEspecial = necessidadeEspecial;
	}
	

}
