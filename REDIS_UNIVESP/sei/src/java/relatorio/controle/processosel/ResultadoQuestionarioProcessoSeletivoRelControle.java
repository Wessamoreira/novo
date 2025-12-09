package relatorio.controle.processosel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas avaliacaoInstitucionalForm.jsp avaliacaoInstitucionalCons.jsp) com as
 * funcionalidades da classe <code>AvaliacaoInstitucional</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see AvaliacaoInstitucional
 * @see AvaliacaoInstitucionalVO
 */

@Controller("ResultadoQuestionarioProcessoSeletivoRelControle")
@Scope("viewScope")
@Lazy
public class ResultadoQuestionarioProcessoSeletivoRelControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7999399119220608239L;
	private ProcSeletivoVO procSeletivoVO;
	private List<SelectItem> listaSelectItemDataProva;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;

	protected String valorConsultaProcSeletivo;
	protected String campoConsultaProcSeletivo;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<SelectItem> listaSelectItemOpcaoConsultaCurso;

	private Integer sala;
	protected List<ProcSeletivoVO> listaConsultaProcSeletivo;
	private List<SelectItem> listaSelectItemSalaProcSeletivo;
	private List<SelectItem> tipoConsultaComboProcSeletivo;
	private QuestionarioRelVO questionarioRelVO;
	private Boolean abrirFiltroConsulta;
	private Boolean trazerSomenteCandidatosConfirmados;

	public void consultarDadosGeracaoRelatorioGrafico() {
		try {
			setQuestionarioRelVO(getFacadeFactory().getResultadoQuestionarioProcessoSeletivoRelFacade().consultarDadosGeracaoRelatorioGrafico(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoCurso().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getTrazerSomenteCandidatosConfirmados(),  getUsuarioLogado()));
			if (getQuestionarioRelVO().getCodigo() == 0) {
				setAbrirFiltroConsulta(true);
				setMensagemID("msg_relatorio_sem_dados", Uteis.ALERTA);
			} else {
				setAbrirFiltroConsulta(false);
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}

		} catch (Exception e) {
			setAbrirFiltroConsulta(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarProcSeletivo() {
		try {
			ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
			setProcSeletivoVO(obj);
			montarListaSelectItemDataProva();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparProcessoSeletivo() {
		setProcSeletivoVO(null);
		getListaSelectItemDataProva().clear();
		getListaSelectItemSalaProcSeletivo().clear();
		setUnidadeEnsinoCurso(null);
	}

	public void montarListaSelectItemDataProva() throws Exception {

		List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		getListaSelectItemDataProva().clear();
		getListaSelectItemSalaProcSeletivo().clear();
		setSala(0);
		getListaSelectItemDataProva().add(new SelectItem(0, ""));
		for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
			getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
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

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DisciplinaProcSeletivo</code>.
	 */
	public void montarListaSelectItemSalaProcSeletivo() throws Exception {
		setSala(0);
		List<SalaLocalAulaVO> resultadoConsulta = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivoEDataAula(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo());
		if (!resultadoConsulta.isEmpty()) {
			getListaSelectItemSalaProcSeletivo().clear();
			getListaSelectItemSalaProcSeletivo().add(new SelectItem(0, "Todas as Sala"));
			for (SalaLocalAulaVO sala : resultadoConsulta) {
				getListaSelectItemSalaProcSeletivo().add(new SelectItem(sala.getCodigo(), sala.getLocalAula().getLocal() + " - " + sala.getSala()));
			}
		} else {
			getListaSelectItemSalaProcSeletivo().clear();
			getListaSelectItemSalaProcSeletivo().add(new SelectItem(0, "Sem Sala Cadastrada"));
		}
	}

	public void imprimirObjetoPDF() throws Exception {
		imprimirObjeto(TipoRelatorioEnum.PDF);
	}

	public void imprimirObjeto(TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
		String caminho = "";
		String design = "";
		String titulo = "";

		@SuppressWarnings("rawtypes")
		List listaRegistro = new ArrayList(0);
		try {

			// design =
			// getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().getDesignIReportRelatorio(getTipoRelatorio());

			caminho = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio();

			if (!listaRegistro.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("processoSeletivo", getProcSeletivoVO().getDescricao());
				if (getUnidadeEnsinoCurso().getCodigo() > 0) {
					getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCurso", getUnidadeEnsinoCurso().getNomeUnidadeEnsino() + " - " + getUnidadeEnsinoCurso().getCurso().getNome() + "/" + getUnidadeEnsinoCurso().getTurno().getNome());
				} else {
					getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCurso", "");
				}
				if (getItemProcSeletivoDataProvaVO().getCodigo() != null && getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
					setItemProcSeletivoDataProvaVO(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("dataProva", getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());
				} else {
					getSuperParametroRelVO().adicionarParametro("dataProva", "TODAS");
				}
				if (getSala() != null && getSala() > 0) {
					SalaLocalAulaVO salaLocalAulaVO = getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala());
					salaLocalAulaVO.setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(salaLocalAulaVO.getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("sala", salaLocalAulaVO.getSala());
					getSuperParametroRelVO().adicionarParametro("local", salaLocalAulaVO.getLocalAula().getLocal());
				} else if (getSala() != null && getSala() < 0) {
					getSuperParametroRelVO().adicionarParametro("sala", "Sem Sala");
					getSuperParametroRelVO().adicionarParametro("local", "Sem Local");
				} else {

					getSuperParametroRelVO().adicionarParametro("sala", "TODAS");
					getSuperParametroRelVO().adicionarParametro("local", "TODOS");
				}

				realizarImpressaoRelatorio();
				setValorConsultaProcSeletivo("");
				setCampoConsultaProcSeletivo("");
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

	public void imprimirObjetoExcel() throws Exception {
		imprimirObjeto(TipoRelatorioEnum.EXCEL);
	}

	public void consultarCurso() {
		try {
			getUnidadeEnsinoCursoVOs().clear();
			setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoProcSeletivo(getValorConsultaCurso(), getProcSeletivoVO().getCodigo(), "", "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		setUnidadeEnsinoCurso((UnidadeEnsinoCursoVO) getRequestMap().get("cursoItens"));
	}

	public void limparCurso() {
		setUnidadeEnsinoCurso(null);
	}

	public void consultarProcSeletivo() {
		try {
			List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'this.id','99/99/9999',event);";
		}
		return "";
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

	public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
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

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public List<SelectItem> getTipoConsultaComboProcSeletivo() {
		if (tipoConsultaComboProcSeletivo == null) {
			tipoConsultaComboProcSeletivo = new ArrayList<SelectItem>(0);
			tipoConsultaComboProcSeletivo.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboProcSeletivo.add(new SelectItem("dataInicio", "Data Início"));
			tipoConsultaComboProcSeletivo.add(new SelectItem("dataFim", "Data Fim"));

		}
		return tipoConsultaComboProcSeletivo;
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

	public List<SelectItem> getListaSelectItemSalaProcSeletivo() {
		if (listaSelectItemSalaProcSeletivo == null) {
			listaSelectItemSalaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSalaProcSeletivo;
	}

	public void setListaSelectItemSalaProcSeletivo(List<SelectItem> listaSelectItemSalaProcSeletivo) {
		this.listaSelectItemSalaProcSeletivo = listaSelectItemSalaProcSeletivo;
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "curso";
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

	public List<SelectItem> getListaSelectItemOpcaoConsultaCurso() {
		if (listaSelectItemOpcaoConsultaCurso == null) {
			listaSelectItemOpcaoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("curso", "Curso"));
		}
		return listaSelectItemOpcaoConsultaCurso;
	}

	public void setListaSelectItemOpcaoConsultaCurso(List<SelectItem> listaSelectItemOpcaoConsultaCurso) {
		this.listaSelectItemOpcaoConsultaCurso = listaSelectItemOpcaoConsultaCurso;
	}

	public void setTipoConsultaComboProcSeletivo(List<SelectItem> tipoConsultaComboProcSeletivo) {
		this.tipoConsultaComboProcSeletivo = tipoConsultaComboProcSeletivo;
	}

	public QuestionarioRelVO getQuestionarioRelVO() {
		if (questionarioRelVO == null) {
			questionarioRelVO = new QuestionarioRelVO();
		}
		return questionarioRelVO;
	}

	public void setQuestionarioRelVO(QuestionarioRelVO questionarioRelVO) {
		this.questionarioRelVO = questionarioRelVO;
	}

	public Boolean getAbrirFiltroConsulta() {
		if (abrirFiltroConsulta == null) {
			abrirFiltroConsulta = true;
		}
		return abrirFiltroConsulta;
	}

	public void setAbrirFiltroConsulta(Boolean abrirFiltroConsulta) {
		this.abrirFiltroConsulta = abrirFiltroConsulta;
	}
	
	public void gerarRelatorioPDF() throws Exception {
		gerarRelatorio(TipoRelatorioEnum.PDF);
	}

	public void gerarRelatorioExcel() throws Exception {
		gerarRelatorio(TipoRelatorioEnum.EXCEL);
	}

	public void gerarRelatorio(TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
		try {
			AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs = getFacadeFactory().getResultadoQuestionarioProcessoSeletivoRelFacade().consultarDadosGeracaoRelatorio(getProcSeletivoVO().getCodigo(), getUnidadeEnsinoCurso().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getTrazerSomenteCandidatosConfirmados(),  getUsuarioLogado());
			if (avaliacaoInstucionalRelVOs != null && !avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().isEmpty()) {
				List<AvaliacaoInstucionalRelVO> lista = new ArrayList<AvaliacaoInstucionalRelVO>(0);
				lista.add(avaliacaoInstucionalRelVOs);
				getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGrafico());
				getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Resultado Questionário Proc. Seletivo");
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getEmpresasRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_gerado");
			}else {
				setMensagemID("msg_relatorio_vazio");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
		}
	}

	public void imprimirRespostasTextuais() {
		try {
			PerguntaRelVO obj = (PerguntaRelVO) context().getExternalContext().getRequestMap().get("perguntaItens");
			if (!obj.getRespostaTexto().isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRelImpressaoRespostasTextuais.jrxml");
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.DOC);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(obj.getRespostaTexto());
				getSuperParametroRelVO().setTituloRelatorio(obj.getNome());
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setUsarTargetBlank("");
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception ex) {
			setUsarTargetBlank("");
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public Boolean getTrazerSomenteCandidatosConfirmados() {
		if(trazerSomenteCandidatosConfirmados == null){
			trazerSomenteCandidatosConfirmados = true;
		}
		return trazerSomenteCandidatosConfirmados;
	}

	public void setTrazerSomenteCandidatosConfirmados(Boolean trazerSomenteCandidatosConfirmados) {
		this.trazerSomenteCandidatosConfirmados = trazerSomenteCandidatosConfirmados;
	}
	
	

}
