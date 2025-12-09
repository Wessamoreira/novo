package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ChamadaCandidatoAprovadoRelVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;
import relatorio.negocio.jdbc.processosel.ChamadaCandidatoAprovadoRel;
import relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRel;

@SuppressWarnings("unchecked")
@Controller("ChamadaCandidatoAprovadoRelControle")
@Scope("viewScope")
@Lazy
public class ChamadaCandidatoAprovadoRelControle extends SuperControleRelatorio {

	private ProcSeletivoVO procSeletivoVO;
	private ProcSeletivoCursoVO procSeletivoCursoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer nrCandidatoMatriculado;
	private Integer nrCandidatoConvocado;
	private Integer qtdeCandidatoChamar;
	private Integer qtdeCandidatoAprovadoNaoMatriculado;
	private List<ChamadaCandidatoAprovadoRelVO> listaChamadaCandidatoAprovadoRelVOs;
	private List listaSelectItemProcSeletivo;
	private List listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String tipoChamada;
	private List<SelectItem> listaSelectItemDataProva;
	private List<SelectItem> listaSelectItemNumeroChamada;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private Integer numeroChamada;	
	private ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO ;
	private Boolean utilizarVagasEixoCurso;
	private static final long serialVersionUID = 1L;

	public ChamadaCandidatoAprovadoRelControle() {
		inicializarListasSelectItemTodosComboBox();
	}

	public void alterarProcessoSeletivo() {
		try {
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemDataProva();
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemDataProva() throws Exception {

		List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivoProvaJaRealizada(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if(itemProcSeletivoDataProvaVOs.isEmpty()){
			setMensagemDetalhada("msg_erro", "Nenhuma data de prova realizada neste processo seletivo para a geração deste relatório.", Uteis.SUCESSO);
			getProcSeletivoVO().setCodigo(0);
			getListaSelectItemUnidadeEnsino().clear();
			return;
		}
		getListaSelectItemDataProva().clear();
		getListaSelectItemDataProva().add(new SelectItem(0, ""));
		for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
			getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
		}

	}

	public void imprimirPDF() {
		String caminho = "";
		String design = "";
		String titulo = "";
		String nomeRelatorio = "";
		List listaRegistro = new ArrayList(0);

		try {
			listaRegistro = getListaChamadaCandidatoAprovadoRelVOs();
			nomeRelatorio = ChamadaCandidatoAprovadoRel.getIdEntidade();
			titulo = "Relatório - Chamada Candidatos Aprovados";
			design = getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().getDesignIReportRelatorio();
			caminho = getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().getCaminhoBaseRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
				if (!getProcSeletivoVO().getCodigo().equals(0)) {
					setProcSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().setProcessoSeletivo(getProcSeletivoVO().getDescricao());
				}
				getSuperParametroRelVO().setCurso(getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getNomeCursoTurno());
				getSuperParametroRelVO().adicionarParametro("totalCandidato", listaRegistro.size());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				realizarImpressaoRelatorio();
				getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().alterarSituacaoCandidatoParaConvocado(getListaChamadaCandidatoAprovadoRelVOs(), getNumeroChamada(), getUsuarioLogado());
				getListaChamadaCandidatoAprovadoRelVOs().clear();
				inicializarListasSelectItemTodosComboBox();
				setNumeroChamada(getFacadeFactory().getInscricaoFacade().consultarChamadaProcSeletivo(procSeletivoVO, getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo()));
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

	public void imprimirPDFListaClassificacao() {
		String caminho = "";
		String design = "";
		String titulo = "";

		@SuppressWarnings("rawtypes")
		List listaRegistro = new ArrayList(0);
		try {
			FiltroRelatorioProcessoSeletivoVO filtro= new FiltroRelatorioProcessoSeletivoVO();
			filtro.setAtivo(true);
			
			listaRegistro = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), 0, getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), "", "", filtro, "classificacao", 0, Integer.parseInt(getTipoChamada()), true, getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado(), 0);
			titulo = TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS.getValorApresentar();
			design = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().getDesignIReportRelatorio(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS);
			caminho = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio();

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
				setProcSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarParametro("processoSeletivo", getProcSeletivoVO().getDescricao());
				if (getItemProcSeletivoDataProvaVO().getCodigo() != null && getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
					setItemProcSeletivoDataProvaVO(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("dataProva", getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());
				} else {
					getSuperParametroRelVO().adicionarParametro("dataProva", "TODAS");
				}
				getSuperParametroRelVO().adicionarParametro("sala", "TODAS");
				getSuperParametroRelVO().adicionarParametro("local", "TODOS");
				realizarImpressaoRelatorio();
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

			listaRegistro = null;
		}
	}
	
	public void imprimirPDFListaChamados() {
		String caminho = "";
		String design = "";
		String titulo = "";

		@SuppressWarnings("rawtypes")
		List listaRegistro = new ArrayList(0);
		try {
			FiltroRelatorioProcessoSeletivoVO filtro = new FiltroRelatorioProcessoSeletivoVO();
			filtro.setAtivo(true);
			listaRegistro = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CANDIDATOS_CHAMADOS, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), 0, getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), "", "", filtro, "chamada", 0, Integer.valueOf(getTipoChamada()), true, getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado(), 0);			
			titulo = TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CANDIDATOS_CHAMADOS.getValorApresentar();
			design = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().getDesignIReportRelatorio(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CANDIDATOS_CHAMADOS);
			caminho = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio();

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
				setProcSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarParametro("processoSeletivo", getProcSeletivoVO().getDescricao());
				if (getItemProcSeletivoDataProvaVO().getCodigo() != null && getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
					setItemProcSeletivoDataProvaVO(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("dataProva", getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());
				} else {
					getSuperParametroRelVO().adicionarParametro("dataProva", "TODAS");
				}
				getSuperParametroRelVO().adicionarParametro("sala", "TODAS");
				getSuperParametroRelVO().adicionarParametro("local", "TODOS");
				realizarImpressaoRelatorio();
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
			listaRegistro = null;
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemProcSeletivo();
	}

	public void montarListaSelectItemProcSeletivo() {
		try {
			montarListaSelectItemProcSeletivo("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			setProcSeletivoCursoVO(null);
			getListaChamadaCandidatoAprovadoRelVOs().clear();
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ProcSeletivo</code>.
	 */
	public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
		setListaSelectItemProcSeletivo(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().montarListaSelectItemProcSeletivo(getProcSeletivoVO(), getUnidadeEnsinoLogado(), getUsuarioLogado()));
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		setListaSelectItemUnidadeEnsino(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().montarListaSelectItemUnidadeEnsino(getProcSeletivoVO(), getUnidadeEnsinoLogado(), getUsuarioLogado()));
	}

	public List consultarCursoPorProcessoSeletivoCurso() throws Exception {
		return getFacadeFactory().getProcSeletivoCursoFacade().consultarPorCodigoProcSeletivoUnidadeEnsinoInscicao(getValorConsultaCurso(), getProcSeletivoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo());
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getProcSeletivoCursoFacade().consultarPorCodigoProcSeletivoUnidadeEnsinoInscricao(getValorConsultaCurso(), getProcSeletivoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			ProcSeletivoCursoVO obj = (ProcSeletivoCursoVO) context().getExternalContext().getRequestMap().get("procSeletivoCursoItens");
			setProcSeletivoCursoVO(obj);
			setUtilizarVagasEixoCurso(null);
			if(Uteis.isAtributoPreenchido(getProcSeletivoCursoVO().getProcSeletivoUnidadeEnsino().getCodigo()) && Uteis.isAtributoPreenchido(getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCurso().getEixoCursoVO().getCodigo())) {
				ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCurso =   getFacadeFactory().getProcSeletivoUnidadeEnsinoEixoCursoFacade().consultarPorChavePrimaria(getProcSeletivoCursoVO().getProcSeletivoUnidadeEnsino().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCurso().getEixoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setProcSeletivoUnidadeEnsinoEixoCursoVO(procSeletivoUnidadeEnsinoEixoCurso);
				setUtilizarVagasEixoCurso(Uteis.isAtributoPreenchido(getProcSeletivoUnidadeEnsinoEixoCursoVO()));
			}			
			
			if(getUtilizarVagasEixoCurso()) {		
				setNrCandidatoMatriculado(getFacadeFactory().getProcSeletivoFacade().verificarQuantidadeAlunosMatriculadosPorProcessoSeletivoUnidadeEnsino(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCurso().getEixoCursoVO().getCodigo(),getUsuarioLogado())); 
				setQtdeCandidatoChamar(getVagasRestantesEixoCurso()); 			
			}else {				
				setNrCandidatoMatriculado(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().consultarQtdeCandidatosMatriculadosPorProcSeletivoCurso(getProcSeletivoVO().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
				setQtdeCandidatoChamar(getVagasRestantes());
			}
			setNrCandidatoConvocado(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().consultarQtdeCandidatosConvocadoPorProcSeletivoCurso(getProcSeletivoVO().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
			setQtdeCandidatoAprovadoNaoMatriculado(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().consultarQtdeCandidatosAprovadosNaoMatriculadoPorProcSeletivoCurso(getProcSeletivoVO().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
			setNumeroChamada(getFacadeFactory().getInscricaoFacade().consultarChamadaProcSeletivo(procSeletivoVO, getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo()));
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		setProcSeletivoCursoVO(null);
		setProcSeletivoUnidadeEnsinoEixoCursoVO(null);
		setUtilizarVagasEixoCurso(null);
		getListaChamadaCandidatoAprovadoRelVOs().clear();
		setNrCandidatoMatriculado(0);
		setQtdeCandidatoChamar(0);
		setNrCandidatoConvocado(0);
	}

	public void consultarCandidatosAprovados() {
		try {
			getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().validarDados(getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUsuarioLogado());
			setListaChamadaCandidatoAprovadoRelVOs(getFacadeFactory().getChamadaCandidatoAprovadoRelFacade().consultarCandidatosAprovados(getProcSeletivoVO(), getItemProcSeletivoDataProvaVO().getCodigo(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getProcSeletivoCursoVO().getNumeroVaga(), getNrCandidatoMatriculado(), getQtdeCandidatoChamar(), getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemNumeroChamada() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("0", "Todas"));
		try {
			List<Integer> listaChamada = getFacadeFactory().getInscricaoFacade().consultarNumeroChamada(getProcSeletivoVO(), getProcSeletivoCursoVO().getUnidadeEnsinoCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo());
			for (Integer chamada : listaChamada) {
				itens.add(new SelectItem(chamada, chamada+"º Chamada"));
			}
			setListaSelectItemNumeroChamada(itens);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTipoChamada() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("1", "1º Chamada"));
		itens.add(new SelectItem("2", "2º Chamada"));
		return itens;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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

	public List getListaSelectItemProcSeletivo() {
		if (listaSelectItemProcSeletivo == null) {
			listaSelectItemProcSeletivo = new ArrayList(0);
		}
		return listaSelectItemProcSeletivo;
	}

	public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
		this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
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

	public Integer getNrCandidatoMatriculado() {
		if (nrCandidatoMatriculado == null) {
			nrCandidatoMatriculado = 0;
		}
		return nrCandidatoMatriculado;
	}

	public void setNrCandidatoMatriculado(Integer nrCandidatoMatriculado) {
		this.nrCandidatoMatriculado = nrCandidatoMatriculado;
	}

	public String getTipoChamada() {
		if (tipoChamada == null) {
			tipoChamada = "1";
		}
		return tipoChamada;
	}

	public void setTipoChamada(String tipoChamada) {
		this.tipoChamada = tipoChamada;
	}

	public List<ChamadaCandidatoAprovadoRelVO> getListaChamadaCandidatoAprovadoRelVOs() {
		if (listaChamadaCandidatoAprovadoRelVOs == null) {
			listaChamadaCandidatoAprovadoRelVOs = new ArrayList<ChamadaCandidatoAprovadoRelVO>(0);
		}
		return listaChamadaCandidatoAprovadoRelVOs;
	}

	public void setListaChamadaCandidatoAprovadoRelVOs(List<ChamadaCandidatoAprovadoRelVO> listaChamadaCandidatoAprovadoRelVOs) {
		this.listaChamadaCandidatoAprovadoRelVOs = listaChamadaCandidatoAprovadoRelVOs;
	}

	public Integer getQtdeCandidatoChamar() {
		if (qtdeCandidatoChamar == null) {
			qtdeCandidatoChamar = 0;
		}
		return qtdeCandidatoChamar;
	}

	public void setQtdeCandidatoChamar(Integer qtdeCandidatoChamar) {
		this.qtdeCandidatoChamar = qtdeCandidatoChamar;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public boolean getApresentarDadosRelatorio() {
		return !getProcSeletivoVO().getCodigo().equals(0);
	}

	public Integer getVagasRestantes() {
		return (getProcSeletivoCursoVO().getNumeroVaga() - getNrCandidatoMatriculado()) > 0 ? (getProcSeletivoCursoVO().getNumeroVaga() - getNrCandidatoMatriculado()) : 0 ;
	}

	public Integer getNrCandidatoConvocado() {
		if (nrCandidatoConvocado == null) {
			nrCandidatoConvocado = 0;
		}
		return nrCandidatoConvocado;
	}

	public void setNrCandidatoConvocado(Integer nrCandidatoConvocado) {
		this.nrCandidatoConvocado = nrCandidatoConvocado;
	}

	public Integer getQtdeCandidatoAprovadoNaoMatriculado() {
		if (qtdeCandidatoAprovadoNaoMatriculado == null) {
			qtdeCandidatoAprovadoNaoMatriculado = 0;
		}
		return qtdeCandidatoAprovadoNaoMatriculado;
	}

	public void setQtdeCandidatoAprovadoNaoMatriculado(Integer qtdeCandidatoAprovadoNaoMatriculado) {
		this.qtdeCandidatoAprovadoNaoMatriculado = qtdeCandidatoAprovadoNaoMatriculado;
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

	public List<SelectItem> getListaSelectItemNumeroChamada() {
		if (listaSelectItemNumeroChamada == null) {
			listaSelectItemNumeroChamada = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNumeroChamada;
	}

	public void setListaSelectItemNumeroChamada(List<SelectItem> listaSelectItemNumeroChamada) {
		this.listaSelectItemNumeroChamada = listaSelectItemNumeroChamada;
	}

	public Integer getNumeroChamada() {
		if (numeroChamada == null) {
			numeroChamada = 0;
		}
		return numeroChamada;
	}

	public void setNumeroChamada(Integer numeroChamada) {
		this.numeroChamada = numeroChamada;
	}
	
	public ProcSeletivoUnidadeEnsinoEixoCursoVO getProcSeletivoUnidadeEnsinoEixoCursoVO() {
		if(procSeletivoUnidadeEnsinoEixoCursoVO == null) {
			procSeletivoUnidadeEnsinoEixoCursoVO = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
		}
		return procSeletivoUnidadeEnsinoEixoCursoVO;
	}

	public void setProcSeletivoUnidadeEnsinoEixoCursoVO(
			ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) {
		this.procSeletivoUnidadeEnsinoEixoCursoVO = procSeletivoUnidadeEnsinoEixoCursoVO;
	}
 
	
	public Integer getVagasRestantesEixoCurso() {
		return (getProcSeletivoUnidadeEnsinoEixoCursoVO().getNrVagasEixoCurso()  - getNrCandidatoMatriculado()) > 0 ? (getProcSeletivoUnidadeEnsinoEixoCursoVO().getNrVagasEixoCurso()  - getNrCandidatoMatriculado()) :0;
	}
	public Boolean getUtilizarVagasEixoCurso() {
		if(utilizarVagasEixoCurso == null ) {
			utilizarVagasEixoCurso = Boolean.FALSE;
		}
		return utilizarVagasEixoCurso ;
	}
	
	public void setUtilizarVagasEixoCurso(Boolean utilizarVagasEixoCurso) {
		this.utilizarVagasEixoCurso = utilizarVagasEixoCurso;
	}
}
