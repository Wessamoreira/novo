package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.ProcSeletivoCurso;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRel;

@Controller("ProcSeletivoProvasRelControle")
@Scope("viewScope")
@Lazy
public class ProcSeletivoProvasRelControle  extends SuperControleRelatorio {

	private static final long serialVersionUID = -6225529176168266279L;
	protected String valorConsultaProcSeletivo;
	protected String campoConsultaProcSeletivo;
	protected List listaConsultaProcSeletivo;
	protected List listaSelectItemProcessoSeletivo;
	protected List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemCurso;
	private ProcSeletivoVO procSeletivoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private List<SelectItem> listaSelectItemDataProva;
	private List<SelectItem> listaSelectItemIdiomaProva;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	public ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private List<SelectItem> listaSelectItemSala;
	private Integer sala;
	private Integer provaProcessoSeletivo;
	
	public ProcSeletivoProvasRelControle() throws Exception {
		setValorConsultaProcSeletivo("");
		setCampoConsultaProcSeletivo("");
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),  getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
	
	public void montarListaSelectItemIdiomaProva() throws Exception {
		try {
			List<ProcessoSeletivoProvaDataVO> processoSeletivoProvaDataVOs = getFacadeFactory().getProcessoSeletivoProvaDataFacade().consultarPorItemProcSeletivoDataProva(itemProcSeletivoDataProvaVO.getCodigo());
			getListaSelectItemIdiomaProva().clear();
			
			if(processoSeletivoProvaDataVOs.isEmpty()) {
				throw new Exception("Não existem provas para essa data.");
			}

			for (ProcessoSeletivoProvaDataVO obj : processoSeletivoProvaDataVOs) {
				String idioma = "";
				if (!obj.getDisciplinaIdioma().getNome().equals("")) {
					idioma = " - " + obj.getDisciplinaIdioma().getNome();
				}
				getListaSelectItemIdiomaProva().add(new SelectItem(obj.getProvaProcessoSeletivo().getCodigo() , obj.getProvaProcessoSeletivo().getDescricao() + idioma));
			}
		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void imprimirProva() {
		try {
			setFazerDownload(false);
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoCursoVO.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
			ProvaProcessoSeletivoVO prova = getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(getProvaProcessoSeletivo(), NivelMontarDados.TODOS);
			setCaminhoRelatorio(getFacadeFactory().getProvaProcessoSeletivoFacade().executarDownloadProvaPDF(unidadeEnsino, prova, procSeletivoVO, getConfiguracaoGeralPadraoSistema()));
			setFazerDownload(true);
		} catch (Exception e) {
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

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setUnidadeEnsinoCursoVO(obj);
			montarListaSelectItemSala();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirObjetoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), "AM", SituacaoInscricaoEnum.ATIVO, false, null);
			nomeRelatorio = ProcSeletivoInscricoesRel.getIdEntidade();
			titulo = "Processo Seletivo - Inscrições";
			design = getFacadeFactory().getProcSeletivoInscricoesRelFacade().getDesignIReportRelatorio();
			caminho = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
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

	public void imprimirQuantitativoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), "AM", SituacaoInscricaoEnum.ATIVO, false, null);
			nomeRelatorio = ProcSeletivoInscricoesRel.getIdEntidade();
			titulo = "Processo Seletivo - Inscrições";
			design = getFacadeFactory().getProcSeletivoInscricoesRelFacade().getDesignIReportRelatorioQuantitativo();
			caminho = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
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

	public void imprimirSinteticoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorioSintetico(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(),  getSala(), getItemProcSeletivoDataProvaVO());
			nomeRelatorio = ProcSeletivoInscricoesRel.getIdEntidade();
			titulo = "Processo Seletivo - Inscrições";
			design = getFacadeFactory().getProcSeletivoInscricoesRelFacade().getDesignIReportRelatorioSintetico();
			caminho = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
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

	public void imprimirExcel() {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);
		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), "AM", SituacaoInscricaoEnum.ATIVO, false, null);
			nomeRelatorio = ProcSeletivoInscricoesRel.getIdEntidade();
			titulo = "Processo Seletivo - Inscrições";
			design = getFacadeFactory().getProcSeletivoInscricoesRelFacade().getDesignIReportRelatorio();
			caminho = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio();
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

	public void imprimirObjetoHTML() throws Exception {
		try {
			List listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), "AM", SituacaoInscricaoEnum.ATIVO, false, null);
			String nomeRelatorio = ProcSeletivoInscricoesRel.getIdEntidade();
			String titulo = "Processo Seletivo - Inscrições";
			String design = getFacadeFactory().getProcSeletivoInscricoesRelFacade().getDesignIReportRelatorio();
			apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "", "HTML", "/" + ProcSeletivoInscricoesRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), "", listaRegistro, getFacadeFactory().getProcSeletivoInscricoesRelFacade().getCaminhoBaseRelatorio());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			removerObjetoMemoria(this);
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

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemDataProva();
		// montarListaSelectItemUnidadeEnsinoCurso();
	}

	public void limparDados() {
		getProcSeletivoVO().setCodigo(0);
		getProcSeletivoVO().setDescricao("");
		inicializarListasSelectItemTodosComboBox();
	}

	public void limparDadosCurso() {
		getUnidadeEnsinoCursoVO().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getUnidadeEnsinoCursoVO().getTurno().setNome("");
		montarListaSelectItemSala();
	}

	List<SelectItem> tipoConsultaComboCurso;

	public List getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public String getCampoConsultaProcSeletivo() {
		if (campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public List getListaConsultaProcSeletivo() {
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
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

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		Uteis.liberarListaMemoria(getListaSelectItemUnidadeEnsino());
		valorConsultaProcSeletivo = null;
		campoConsultaProcSeletivo = null;
		Uteis.liberarListaMemoria(listaConsultaProcSeletivo);
	}

	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemProcessoSeletivo() {
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public List getListaSelectItemCurso() {
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
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

	public boolean getIsApresentraUnidadeEnsinoCurso() {
		return getProcSeletivoVO().getCodigo() != 0;
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
			sala = -1;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
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

	public List<SelectItem> getListaSelectItemDataProva() {
		if (listaSelectItemDataProva == null) {
			listaSelectItemDataProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDataProva;
	}

	public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
		this.listaSelectItemDataProva = listaSelectItemDataProva;
	}

	public List<SelectItem> getListaSelectItemIdiomaProva() {
		if(listaSelectItemIdiomaProva == null){
			listaSelectItemIdiomaProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemIdiomaProva;
	}

	public void setListaSelectItemIdiomaProva(List<SelectItem> listaSelectItemIdiomaProva) {
		this.listaSelectItemIdiomaProva = listaSelectItemIdiomaProva;
	}

	public ProcessoSeletivoProvaDataVO getProcessoSeletivoProvaDataVO() {
		if(processoSeletivoProvaDataVO == null) {
			processoSeletivoProvaDataVO = new ProcessoSeletivoProvaDataVO();
		}
		return processoSeletivoProvaDataVO;
	}

	public void setProcessoSeletivoProvaDataVO(ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) {
		this.processoSeletivoProvaDataVO = processoSeletivoProvaDataVO;
	}

	public Integer getProvaProcessoSeletivo() {
		if(provaProcessoSeletivo == null) {
			provaProcessoSeletivo = 0;
		}
		return provaProcessoSeletivo;
	}

	public void setProvaProcessoSeletivo(Integer provaProcessoSeletivo) {
		this.provaProcessoSeletivo = provaProcessoSeletivo;
	}
	
}
