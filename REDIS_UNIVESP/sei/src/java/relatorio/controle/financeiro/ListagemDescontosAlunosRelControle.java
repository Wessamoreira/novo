package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ListagemDescontosAlunoUnidadeEnsinoVO;
import relatorio.negocio.jdbc.financeiro.ListagemDescontosAlunosRel;

@Controller("ListagemDescontosAlunosRelControle")
@Scope("viewScope")
@Lazy
public class ListagemDescontosAlunosRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private TurmaVO turmaVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ConvenioVO convenioVO;
	private PlanoDescontoVO planoDescontoVO;
	private DescontoProgressivoVO descontoProgressivoVO;
	private List<CursoVO> listaConsultaCurso;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemDescontoProgresivo;
	private List<SelectItem> listaSelectItemConvenio;
	private List<SelectItem> listaSelectItemPlanoDesconto;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoFiltroPor;
	private String ano;
	private String semestre;
	private Integer parceiro;
	private Integer categoriaDesconto;
	private List<SelectItem> listaSelectItemParceiro;
	private List<SelectItem> listaSelectItemCategoriaDesconto;
	private String tipoLayout;
	private List<SelectItem> listaSelectItemTipoLayout;
	private Boolean apresentarDescontoRateio;
	private Boolean apresentarDescontoAluno;
	private Boolean apresentarDescontoRecebimento;
	private String situacaoConvenio;
	private List<SelectItem> listaSelectItemSituacaoConvenio;
	private Boolean ativoPlanoDesconto;

	public ListagemDescontosAlunosRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void inicializarListaSelectItems(){
		try {
			montarListaSelectItemConvenio();
			montarListaSelectItemDescontoProgressivo();
			montarListaSelectItemPlanoDesconto();
			montarListaSelectItemParceiro();
			montarListaSelectItemCategoriaDesconto();
			consultarFiltroPadrao();
		} catch (Exception e) {
		}
	}

	public void imprimirPDF() throws Exception {
		List<ListagemDescontosAlunoUnidadeEnsinoVO> listagemDescontosAlunoUnidadeEnsinoVOs = null;
		try {			
			listagemDescontosAlunoUnidadeEnsinoVOs = getFacadeFactory().getListagemDescontosAlunosRelFacade().criarObjeto(getUnidadeEnsinoCursoVO(), getTurmaVO(), getCampoFiltroPor(), getAno(), getSemestre(), getDescontoProgressivoVO().getCodigo(), getPlanoDescontoVO().getCodigo(), getConvenioVO().getCodigo(), getUnidadeEnsinoVOs(), getFiltroRelatorioAcademicoVO(), getParceiro(), getCategoriaDesconto(), getTipoLayout(), getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum(), getFiltroRelatorioAcademicoVO().getDataInicio(), getFiltroRelatorioAcademicoVO().getDataTermino(), getUsuarioLogado(), getApresentarDescontoRateio(), getApresentarDescontoAluno(), getApresentarDescontoRecebimento());
			
			if (!listagemDescontosAlunoUnidadeEnsinoVOs.isEmpty()) {				
				getSuperParametroRelVO().setNomeDesignIreport(ListagemDescontosAlunosRel.getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ListagemDescontosAlunosRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Listagem de Descontos Alunos");
				getSuperParametroRelVO().setListaObjetos(listagemDescontosAlunoUnidadeEnsinoVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ListagemDescontosAlunosRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				getSuperParametroRelVO().adicionarParametro("sintetico", tipoLayout.equals("ListagemDescontosAlunosSintetico"));
				if(Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getTurno())) {
					for(SelectItem turno:getListaSelectItemTurno()) {
						if(turno.getValue().equals(getUnidadeEnsinoCursoVO().getTurno().getCodigo())) {
							getSuperParametroRelVO().setTurno(turno.getLabel());
						}
					}
				}
				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.ANUAL)) {
					getSuperParametroRelVO().setPeriodo(getAno());
				}else if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.SEMESTRAL)) {
					getSuperParametroRelVO().setPeriodo(getAno()+"/"+getSemestre());
				}else if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL)) {
					getSuperParametroRelVO().setPeriodo(Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()) +" à "+ Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()));
				}
				getSuperParametroRelVO().adicionarParametro("descontoProgressivo", getDescontoProgressivoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("convenio", getConvenioVO().getDescricao());
				if(Uteis.isAtributoPreenchido(getCategoriaDesconto())) {
					getSuperParametroRelVO().adicionarParametro("categoriaDesconto", getFacadeFactory().getCategoriaDescontoFacade().consultarPorChavePrimaria(getCategoriaDesconto(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}else {
					getSuperParametroRelVO().adicionarParametro("categoriaDesconto", "");
				}
				
				if(Uteis.isAtributoPreenchido(getParceiro())) {
					getSuperParametroRelVO().adicionarParametro("parceiro", getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(getParceiro(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}else {
					getSuperParametroRelVO().adicionarParametro("parceiro", "");
				}
				
				getSuperParametroRelVO().adicionarParametro("planoDesconto", getPlanoDescontoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("situacaoMatricula", getFiltroRelatorioAcademicoVO().getFiltroAcademicoUtilizado());
				persistirFiltroPadrao();
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				consultarFiltroPadrao();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listagemDescontosAlunoUnidadeEnsinoVOs);
			inicializarListaSelectItems();
			setMarcarTodasUnidadeEnsino(true);
			marcarTodasUnidadesEnsinoAction();
		}
	}
	
	public void persistirFiltroPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), ListagemDescontosAlunosRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getAno(), ListagemDescontosAlunosRel.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSemestre(), ListagemDescontosAlunosRel.class.getSimpleName(), "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getTipoLayout(), ListagemDescontosAlunosRel.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void consultarFiltroPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), ListagemDescontosAlunosRel.class.getSimpleName(), getUsuarioLogado());
			Map<String, String> campoValor = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"ano", "semestre", "tipoLayout"}, ListagemDescontosAlunosRel.class.getSimpleName());
			if(campoValor.containsKey("ano") && Uteis.isAtributoPreenchido(campoValor.get("ano"))) {
				setAno(campoValor.get("ano"));
			}
			if(campoValor.containsKey("semestre") && Uteis.isAtributoPreenchido(campoValor.get("semestre"))) {
				setSemestre(campoValor.get("semestre"));
			}
			if(campoValor.containsKey("tipoLayout") && Uteis.isAtributoPreenchido(campoValor.get("tipoLayout"))) {
				setTipoLayout(campoValor.get("tipoLayout"));
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void limparDadosCursoTurma() throws Exception {
		removerObjetoMemoria(getTurmaVO());
		removerObjetoMemoria(getUnidadeEnsinoVO());
		setTurmaVO(new TurmaVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setAno("");
		setSemestre("");
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaSelectItemTurno().clear();
	}

	public void limparIdentificador() {
		removerObjetoMemoria(getTurmaVO());
		setTurmaVO(new TurmaVO());
		getListaConsultaTurma().clear();
	}

	public void limparDadosCurso() {
		removerObjetoMemoria(getUnidadeEnsinoCursoVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		getListaConsultaCurso().clear();
		getListaSelectItemTurno().clear();
	}

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	@SuppressWarnings("unchecked")
	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			boolean excessao = true;
			for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
				if (ue.getFiltrarUnidadeEnsino()) {
					excessao = false;
				}
			}
			if (!excessao) {
				if (getCampoConsultaCurso().equals("codigo")) {
					if (getValorConsultaCurso().equals("")) {
						setValorConsultaCurso("0");
					}
					int valorInt = Integer.parseInt(getValorConsultaCurso());
					for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
						if (ue.getFiltrarUnidadeEnsino()) {
							objs.addAll(getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, ue.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
						}
					}
				}
				if (getCampoConsultaCurso().equals("nome")) {
					for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
						if (ue.getFiltrarUnidadeEnsino()) {
							objs.addAll(getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), ue.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
						}
					}
				}
				setListaConsultaCurso(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			getUnidadeEnsinoCursoVO().setCurso((CursoVO) context().getExternalContext().getRequestMap().get("cursoItem"));
			setMensagemDetalhada("");
			montarListaSelectItemTurno();
			verificarApresentarAnoSemestre(getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
	    }

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			boolean excessao = true;
			for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
				if (ue.getFiltrarUnidadeEnsino()) {
					excessao = false;
				}
			}
			if (!excessao) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
						if (ue.getFiltrarUnidadeEnsino()) {
							objs.addAll(getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(ue.getCodigo(), getValorConsultaTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
						}
					}
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
						if (ue.getFiltrarUnidadeEnsino()) {
							objs.addAll(getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), ue.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
						}
					}
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
		setAno("");
		setSemestre("");
		setValorConsultaTurma("");
		setCampoConsultaTurma("");
		getListaConsultaTurma().clear();		
		verificarApresentarAnoSemestre(obj.getPeriodicidade());
		
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public void montarListaSelectItemTurno() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo().intValue(),"", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		for (UnidadeEnsinoCursoVO obj : resultadoConsulta) {
			getListaSelectItemTurno().add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
		}
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
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

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "unidadeEnsino";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public String getAno() {
		if (ano == null) {
			return "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public Boolean getIsFiltrarPorturma() {
		return getCampoFiltroPor().equals("turma");
	}

	public Boolean getIsFiltrarPorCurso() {
		return getCampoFiltroPor().equals("curso");
	}

	public Boolean getIsFiltrarPorTurno() {
		return getListaSelectItemTurno().size() > 0 && getIsFiltrarPorCurso();
	}

	public ConvenioVO getConvenioVO() {
		if (convenioVO == null) {
			convenioVO = new ConvenioVO();
		}
		return convenioVO;
	}

	public void setConvenioVO(ConvenioVO convenioVO) {
		this.convenioVO = convenioVO;
	}

	public PlanoDescontoVO getPlanoDescontoVO() {
		if (planoDescontoVO == null) {
			planoDescontoVO = new PlanoDescontoVO();
		}
		return planoDescontoVO;
	}

	public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
		this.planoDescontoVO = planoDescontoVO;
	}

	public DescontoProgressivoVO getDescontoProgressivoVO() {
		if (descontoProgressivoVO == null) {
			descontoProgressivoVO = new DescontoProgressivoVO();
		}
		return descontoProgressivoVO;
	}

	public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
		this.descontoProgressivoVO = descontoProgressivoVO;
	}

	public List<SelectItem> getListaSelectItemConvenio() {
		if (listaSelectItemConvenio == null) {
			listaSelectItemConvenio = new ArrayList<SelectItem>();
		}
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List<SelectItem> listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public List<SelectItem> getListaSelectItemDescontoProgresivo() {
		if (listaSelectItemDescontoProgresivo == null) {
			listaSelectItemDescontoProgresivo = new ArrayList<SelectItem>();
		}
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List<SelectItem> listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public List<SelectItem> getListaSelectItemPlanoDesconto() {
		if (listaSelectItemPlanoDesconto == null) {
			listaSelectItemPlanoDesconto = new ArrayList<SelectItem>();
		}
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List<SelectItem> listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemConvenio() throws Exception {
		List<ConvenioVO> convenioVOs = null;
		if (Uteis.isAtributoPreenchido(parceiro)) {
			convenioVOs = getFacadeFactory().getConvenioFacade().consultarPorParceiro(getParceiro(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else {
			convenioVOs = getFacadeFactory().getConvenioFacade().consultarConvenioPorSituacaoNivelCombobox(getSituacaoConvenio(), getUsuarioLogado());
		}
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, "Todos"));
		objs.add(new SelectItem(-1, "Não Apresentar"));
		for(ConvenioVO convenioVO : convenioVOs) {
			objs.add(new SelectItem(convenioVO.getCodigo(), convenioVO.getDescricao()));
		}
		
		setListaSelectItemConvenio(objs);
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemPlanoDesconto() throws Exception {
		List<PlanoDescontoVO> planoDescontoVOs = null;
		if (Uteis.isAtributoPreenchido(categoriaDesconto)) {
			planoDescontoVOs = getFacadeFactory().getPlanoDescontoFacade().consultarPorCategoriaDesconto(getCategoriaDesconto(), false, getUsuarioLogado());
		} else {
			planoDescontoVOs = getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(null, getAtivoPlanoDesconto(), getUsuarioLogado());
		}
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, "Todos"));
		objs.add(new SelectItem(-1, "Não Apresentar"));
		for (PlanoDescontoVO planoDescontoVO : planoDescontoVOs) {
			objs.add(new SelectItem(planoDescontoVO.getCodigo(), planoDescontoVO.getNome()));
		}
		
		setListaSelectItemPlanoDesconto(objs);
	}

	public void montarListaSelectItemDescontoProgressivo() throws Exception {
		List<DescontoProgressivoVO> descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorPlanoFinannceiroAluno(null, false, getUsuarioLogado());
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, "Todos"));
		objs.add(new SelectItem(-1, "Não Apresentar"));
		for (DescontoProgressivoVO descontoProgressivoVO : descontoProgressivoVOs) {
			objs.add(new SelectItem(descontoProgressivoVO.getCodigo(), descontoProgressivoVO.getNome()));
		}
		Ordenacao.ordenarLista(objs, "value");
		setListaSelectItemDescontoProgresivo(objs);
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ListagemDescontosAlunosRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(Collectors.joining("; ")));
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

	public Boolean getApresentarCampoAno() {		
		return !getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL);
	}

	

	public Boolean getApresentarCampoSemestre() {
		return getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.SEMESTRAL);
	}

	

	private void verificarApresentarAnoSemestre(String periodicidade) {
		if (periodicidade.equals("AN")){ 
			getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.ANUAL);
		}else if(periodicidade.equals("SE")) {
			getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.SEMESTRAL);
		} else {
			getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.INTEGRAL);
		}
		
	}

	public Integer getParceiro() {
		if (parceiro == null) {
			parceiro = 0;
		}
		return parceiro;
	}

	public void setParceiro(Integer parceiro) {
		this.parceiro = parceiro;
	}

	public Integer getCategoriaDesconto() {
		if (categoriaDesconto == null) {
			categoriaDesconto = 0;
		}
		return categoriaDesconto;
	}

	public void setCategoriaDesconto(Integer categoriaDesconto) {
		this.categoriaDesconto = categoriaDesconto;
	}

	public List<SelectItem> getListaSelectItemParceiro() {
		if (listaSelectItemParceiro == null) {
			listaSelectItemParceiro = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemParceiro;
	}

	public void setListaSelectItemParceiro(List<SelectItem> listaSelectItemParceiro) {
		this.listaSelectItemParceiro = listaSelectItemParceiro;
	}

	public List<SelectItem> getListaSelectItemCategoriaDesconto() {
		if (listaSelectItemCategoriaDesconto == null) {
			listaSelectItemCategoriaDesconto = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCategoriaDesconto;
	}

	public void setListaSelectItemCategoriaDesconto(List<SelectItem> listaSelectItemCategoriaDesconto) {
		this.listaSelectItemCategoriaDesconto = listaSelectItemCategoriaDesconto;
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemCategoriaDesconto() {
		try {
			List<CategoriaDescontoVO> categoriaDescontoVOs = getFacadeFactory().getCategoriaDescontoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemCategoriaDesconto(UtilSelectItem.getListaSelectItem(categoriaDescontoVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemParceiro() {
		try {
			List<ParceiroVO> parceiroVOs = getFacadeFactory().getParceiroFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemParceiro(UtilSelectItem.getListaSelectItem(parceiroVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "ListagemDescontosAlunosAnalitico";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("ListagemDescontosAlunosAnalitico", "Analítico"));
			listaSelectItemTipoLayout.add(new SelectItem("ListagemDescontosAlunosSintetico", "Sintético"));
		}
		return listaSelectItemTipoLayout;
	}
	
	public boolean getDesabilitarPeriodicidade() {
		return (getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO())) || (getIsFiltrarPorCurso() && Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso()));
	}

	public void limparCamposConformeFiltro() {
		if(getIsFiltrarPorCurso()) {
			setTurmaVO(null);
		}else if(getIsFiltrarPorturma()) {
			setUnidadeEnsinoCursoVO(null);
		}else {
			setTurmaVO(null);
			setUnidadeEnsinoCursoVO(null);
		}
	}

	public Boolean getApresentarDescontoRateio() {
		if (apresentarDescontoRateio == null) {
			apresentarDescontoRateio = true;
		}
		return apresentarDescontoRateio;
	}

	public void setApresentarDescontoRateio(Boolean apresentarDescontoRateio) {
		this.apresentarDescontoRateio = apresentarDescontoRateio;
	}

	public Boolean getApresentarDescontoAluno() {
		if (apresentarDescontoAluno == null) {
			apresentarDescontoAluno = true;
		}
		return apresentarDescontoAluno;
	}

	public void setApresentarDescontoAluno(Boolean apresentarDescontoAluno) {
		this.apresentarDescontoAluno = apresentarDescontoAluno;
	}

	public Boolean getApresentarDescontoRecebimento() {
		if (apresentarDescontoRecebimento == null) {
			apresentarDescontoRecebimento = true;
		}
		return apresentarDescontoRecebimento;
	}

	public void setApresentarDescontoRecebimento(Boolean apresentarDescontoRecebimento) {
		this.apresentarDescontoRecebimento = apresentarDescontoRecebimento;
	}

	public String getSituacaoConvenio() {
		if (situacaoConvenio == null) {
			situacaoConvenio = "AT";
		}
		return situacaoConvenio;
	}

	public void setSituacaoConvenio(String situacaoConvenio) {
		this.situacaoConvenio = situacaoConvenio;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoConvenio() {
		if (listaSelectItemSituacaoConvenio == null) {
			listaSelectItemSituacaoConvenio = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoConvenio.add(new SelectItem("AT", "Ativo"));
			listaSelectItemSituacaoConvenio.add(new SelectItem("IN", "Inativo"));
			listaSelectItemSituacaoConvenio.add(new SelectItem("FI", "Finalizado"));
			listaSelectItemSituacaoConvenio.add(new SelectItem("TO", "Todos"));
		}
		return listaSelectItemSituacaoConvenio;
	}

	public Boolean getAtivoPlanoDesconto() {
		if (ativoPlanoDesconto == null) {
			ativoPlanoDesconto = true;
		}
		return ativoPlanoDesconto;
	}

	public void setAtivoPlanoDesconto(Boolean ativoPlanoDesconto) {
		this.ativoPlanoDesconto = ativoPlanoDesconto;
	}
	
}
