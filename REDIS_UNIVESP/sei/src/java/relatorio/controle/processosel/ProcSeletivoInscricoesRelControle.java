package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.processosel.ProcSeletivoCurso;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoesRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO;
import relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRel;

@Controller("ProcSeletivoInscricoesRelControle")
@Scope("viewScope")
@Lazy
public class ProcSeletivoInscricoesRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
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
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;

	private String situacao;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private List<SelectItem> listaSelectItemSala;
	private List<SelectItem> listaSelectItemSituacao;
	private Integer sala;
    private SituacaoInscricaoEnum situacaoInscricao;
    private Boolean filtrarSomenteInscricoesIsentas;
    
    private FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO;
    private Boolean marcarTodasSituacoesInscricao;

	public ProcSeletivoInscricoesRelControle() throws Exception {
		setValorConsultaProcSeletivo("");
		setCampoConsultaProcSeletivo("");
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
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

	public void montarListaUltimosProcSeletivos(){
		try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
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
		montarListaSelectItemSituacao();
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

	public void montarListaSelectItemSituacao() {
		getListaSelectItemSituacao().clear();
		getListaSelectItemSituacao().add(new SelectItem("PF", "Pendente Financeiramente"));
		getListaSelectItemSituacao().add(new SelectItem("CO", "Confirmado"));
		getListaSelectItemSituacao().add(new SelectItem("AM", "Ambos"));
	}
        
        public String getSituacao_Apresentar() {
            if (getSituacao().equals("PF")) {
                return "Pendente Financeiramente";
            }
            if (getSituacao().equals("CO")) {
                return "Confirmado";
            }
            return "";
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
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), getSituacao(), getSituacaoInscricao(), getFiltrarSomenteInscricoesIsentas(), getFiltroRelatorioProcessoSeletivoVO() );
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
				String situacaoStr = getSituacao_Apresentar();
				if (!situacaoStr.equals("")) {
					situacaoStr = situacaoStr + " - ";
				}
				getSuperParametroRelVO().setSituacao(situacaoStr + montaSituacoesSelecionadas(getFiltroRelatorioProcessoSeletivoVO()));
				getSuperParametroRelVO().setSomenteIsentas("");
				if (getFiltrarSomenteInscricoesIsentas()) {
					getSuperParametroRelVO().setSomenteIsentas("Sim");
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

	public void imprimirQuantitativoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorioQuantitativo(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), getSituacao(), getFiltroRelatorioProcessoSeletivoVO(), getFiltrarSomenteInscricoesIsentas());
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
				String situacaoStr = getSituacao_Apresentar();
				if (!situacaoStr.equals("")) {
					situacaoStr = situacaoStr + " - ";
				}
				getSuperParametroRelVO().setSituacao(situacaoStr + montaSituacoesSelecionadas(getFiltroRelatorioProcessoSeletivoVO()));
				getSuperParametroRelVO().setSomenteIsentas("");
				if (getFiltrarSomenteInscricoesIsentas()) {
					getSuperParametroRelVO().setSomenteIsentas("Sim");
				}
				int cont = 0;
				Double totalQtdCandVaga = 0.0;
				Iterator i = listaRegistro.iterator();
				while (i.hasNext()) {
					ProcessoSeletivoInscricoesRelVO obj = (ProcessoSeletivoInscricoesRelVO) i.next();
					Iterator k = obj.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO().iterator();
					while (k.hasNext()) {
						ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO p = (ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO) k.next();
						totalQtdCandVaga += p.getQtdeCandPorVaga();
						cont++;
					}
				}
				totalQtdCandVaga = Uteis.arrendondarForcando2CadasDecimais(totalQtdCandVaga / cont);
				getSuperParametroRelVO().setQtdTurma(totalQtdCandVaga.toString());
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
			setFiltroRelatorioProcessoSeletivoVO(new FiltroRelatorioProcessoSeletivoVO());
		}
	}

	public void imprimirSinteticoPDF() throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorioSintetico(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(),  getSala(), getItemProcSeletivoDataProvaVO(), getSituacao(), getSituacaoInscricao(), getFiltrarSomenteInscricoesIsentas(), getFiltroRelatorioProcessoSeletivoVO());
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
				StringBuilder s = new StringBuilder();
				s.append("Filtros: ");
				if (!getProcSeletivoVO().getCodigo().equals(0)) {
					s.append(getProcSeletivoVO().getDescricao());
				}
				if (!getUnidadeEnsinoCursoVO().getUnidadeEnsino().equals(0)) {
					s.append(" - Unidade: ");
					try {
						UnidadeEnsinoVO unidade = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoCursoVO().getUnidadeEnsino(), false, getUsuarioLogado());
						s.append(unidade.getAbreviatura());
					} catch (Exception e) {
					}
				}
				if (!getUnidadeEnsinoCursoVO().getCodigo().equals(0)) {
					getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getUnidadeEnsinoCursoVO(), getUsuarioLogado());
					s.append(" - Curso: ");
					s.append(getUnidadeEnsinoCursoVO().getCurso().getNome());
					s.append(" - Turno: ");
					s.append(getUnidadeEnsinoCursoVO().getTurno().getNome());
				}
				if (!getItemProcSeletivoDataProvaVO().getCodigo().equals(0)) {
					s.append(" - Data Prova: ");
					s.append(getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());
				}
				if (!getSala().equals(-1)) {
					s.append(" - Sala: ");
					s.append(getSala());
				}
				String situacaoStr = getSituacao_Apresentar();
				s.append(" - Situação: ");
				if (!situacaoStr.equals("")) {
					s.append(situacaoStr);
					s.append(" (");
					s.append(montaSituacoesSelecionadas(getFiltroRelatorioProcessoSeletivoVO()));
					s.append(")");
				}
				getSuperParametroRelVO().setSomenteIsentas("");
				if (getFiltrarSomenteInscricoesIsentas()) {
					s.append(" - Somente Inscrições Isentas");
				}
				getSuperParametroRelVO().setFiltros(s.toString());
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
			listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), getSituacao(), getSituacaoInscricao(), getFiltrarSomenteInscricoesIsentas(), getFiltroRelatorioProcessoSeletivoVO());
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
				String situacaoStr = getSituacao_Apresentar();
				if (!situacaoStr.equals("")) {
					situacaoStr = situacaoStr + " - ";
				}
				getSuperParametroRelVO().setSituacao(situacaoStr + montaSituacoesSelecionadas(getFiltroRelatorioProcessoSeletivoVO()));
				getSuperParametroRelVO().setSomenteIsentas("");
				if (getFiltrarSomenteInscricoesIsentas()) {
					getSuperParametroRelVO().setSomenteIsentas("Sim");
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

	public void imprimirObjetoHTML() throws Exception {
		try {
			List listaRegistro = getFacadeFactory().getProcSeletivoInscricoesRelFacade().emitirRelatorio(getProcSeletivoVO(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCodigo(), getItemProcSeletivoDataProvaVO(), getSala(), getSituacao(), getSituacaoInscricao(), getFiltrarSomenteInscricoesIsentas(), getFiltroRelatorioProcessoSeletivoVO());
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

	// public void montarListaSelectItemUnidadeEnsinoCurso() {
	// try {
	// montarListaSelectItemUnidadeEnsinoCurso("");
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }

	// public void montarListaSelectItemUnidadeEnsinoCurso(String prm) throws
	// Exception {
	// List resultadoConsulta = null;
	// Iterator i = null;
	// try {
	// if ((getProcSeletivoVO() == null) ||
	// (getProcSeletivoVO().getCodigo().intValue() == 0)) {
	// List objs = new ArrayList(0);
	// setListaSelectItemCurso(objs);
	// return;
	// }
	// if ((getUnidadeEnsinoVO() == null) || (getUnidadeEnsinoVO().getCodigo()
	// == 0)) {
	// List objs = new ArrayList(0);
	// setListaSelectItemCurso(objs);
	// return;
	// }
	//
	// resultadoConsulta = consultarProcessoSeletivoUnidadeEnsinoCurso();
	// i = resultadoConsulta.iterator();
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// ProcSeletivoCursoVO proc = (ProcSeletivoCursoVO) i.next();
	// objs.add(new SelectItem(proc.getUnidadeEnsinoCurso().getCodigo(),
	// proc.getUnidadeEnsinoCurso().getCurso().getNome() + " - " +
	// proc.getUnidadeEnsinoCurso().getTurno().getNome()));
	// }
	// setListaSelectItemCurso(objs);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// Uteis.liberarListaMemoria(resultadoConsulta);
	// i = null;
	// }
	// }

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

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AM";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
        
        private List<SelectItem> listaSelectItemSituacaoInscricaoEnum;
        
        public List getListaSelectItemSituacaoInscricaoEnum() throws Exception {
            List objs = new ArrayList(0);
            if(listaSelectItemSituacaoInscricaoEnum == null){
                listaSelectItemSituacaoInscricaoEnum = new ArrayList<SelectItem>(0);
                for(SituacaoInscricaoEnum situacaoInscricaoEnum:SituacaoInscricaoEnum.values()){
                    listaSelectItemSituacaoInscricaoEnum.add(new SelectItem(situacaoInscricaoEnum.name(), situacaoInscricaoEnum.getValorApresentar()));
                }
            }
            return listaSelectItemSituacaoInscricaoEnum;
        }

    /**
     * @return the situacaoInscricao
     */
    public SituacaoInscricaoEnum getSituacaoInscricao() {
        return situacaoInscricao;
    }

    /**
     * @param situacaoInscricao the situacaoInscricao to set
     */
    public void setSituacaoInscricao(SituacaoInscricaoEnum situacaoInscricao) {
        this.situacaoInscricao = situacaoInscricao;
    }

    /**
     * @return the filtrarSomenteInscricoesIsentas
     */
    public Boolean getFiltrarSomenteInscricoesIsentas() {
        if (filtrarSomenteInscricoesIsentas == null) {
             filtrarSomenteInscricoesIsentas = Boolean.FALSE;
        }
        return filtrarSomenteInscricoesIsentas;
    }

    /**
     * @param filtrarSomenteInscricoesIsentas the filtrarSomenteInscricoesIsentas to set
     */
    public void setFiltrarSomenteInscricoesIsentas(Boolean filtrarSomenteInscricoesIsentas) {
        this.filtrarSomenteInscricoesIsentas = filtrarSomenteInscricoesIsentas;
    }
    
    public FiltroRelatorioProcessoSeletivoVO getFiltroRelatorioProcessoSeletivoVO() {
    	if(filtroRelatorioProcessoSeletivoVO == null) {
    		filtroRelatorioProcessoSeletivoVO = new FiltroRelatorioProcessoSeletivoVO();
    	}
		return filtroRelatorioProcessoSeletivoVO;
	}
    
    public void setFiltroRelatorioProcessoSeletivoVO(
			FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) {
		this.filtroRelatorioProcessoSeletivoVO = filtroRelatorioProcessoSeletivoVO;
	}
    
    private StringBuilder montaSituacoesSelecionadas(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) {
    	StringBuilder str = new StringBuilder();
    	str.append(" ");
    	if(filtroRelatorioProcessoSeletivoVO.getAtivo()) {
    		str.append("Ativa, ");
    	}
    	if (filtroRelatorioProcessoSeletivoVO.getCancelado()) {
			str.append("Cancelada, ");
		}
    	if (filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao()) {
			str.append("C.O.I., ");
		}
    	if (filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
			str.append("N Comp.");
		}
    	return str;
    }
    
    public Boolean getMarcarTodasSituacoesInscricao() {
		if(marcarTodasSituacoesInscricao == null){
			marcarTodasSituacoesInscricao = false;
		}
		return marcarTodasSituacoesInscricao;
	}
    
    public void setMarcarTodasSituacoesInscricao(Boolean marcarTodasSituacoesInscricao) {
		this.marcarTodasSituacoesInscricao = marcarTodasSituacoesInscricao;
	}
    
    public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoInscricao() {
		if (getMarcarTodasSituacoesInscricao()) {
			getFiltroRelatorioProcessoSeletivoVO().realizarMarcarTodasSituacoes();
		} else {
			getFiltroRelatorioProcessoSeletivoVO().realizarDesmarcarTodasSituacoes();
		}
	}
    
    public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodos() {
		if (getMarcarTodasSituacoesInscricao()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
}
