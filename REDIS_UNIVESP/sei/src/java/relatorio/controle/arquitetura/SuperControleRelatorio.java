package relatorio.controle.arquitetura;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public abstract class SuperControleRelatorio extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6779076600525663253L;
	protected Integer opcaoOrdenacao;
	protected List<SelectItem> listaSelectItemOrdenacoesRelatorio;
	private SuperParametroRelVO superParametroRelVO;
	protected String usarTargetBlank;
	protected Boolean fazerDownload;
	private GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
	String caminhoRelatorio = null;
	private boolean assinarDigitalmente = false;
	private String corAssinaturaDigitalmente;
	private Float larguraAssinatura;
	private Float alturaAssinatura;
	private List<DocumentoAssinadoVO> listaDocumentoAsssinados;
	private Boolean utilizarCaminhoDownloadArquivoAssinadoDigitalmente;
	

	private Boolean marcarTodasSituacoesFinanceiras;

	public String getCaminhoRelatorio() {
		if (caminhoRelatorio == null) {
			caminhoRelatorio = "";
		}
		return caminhoRelatorio;
	}

	public void setCaminhoRelatorio(String caminhoRelatorio) {
		this.caminhoRelatorio = caminhoRelatorio;
	}

	public void setSuperParametroRelVO(SuperParametroRelVO superParametroRelVO) {
		this.superParametroRelVO = superParametroRelVO;
	}

	public SuperControleRelatorio() {
	}

	public void limparMensagem() {
		// setSucesso(sucesso);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	/*
	 * Rotina responsável por acionar o Servlet de Apresentação de Relatório <code>VisualizadorRelatorio</code>, fornecendo todos os parâmetros e dados necessários para a geração e visualização do mesmo.
	 * 
	 * @param xml Dados a serem visualizados no relatório
	 * 
	 * @param tituloRelatorio Título do relatório
	 * 
	 * @param tipoRelatorio Pode assumir dois valores: HTML ou PDF
	 * 
	 * @param parserBuscaTag Padrão a ser utilizado pelo JasperReport para filtrar quais dados do XML deverão ser processados
	 * 
	 * @param designIReport Nome do arquivo do IReport contendo o design gráfico do relatório
	 * 
	 * @param nomeUsuario Nome do usuário logado para apresentação no relatório
	 */
	public void apresentarRelatorio(String nomeRelatorio, String xml, String tituloRelatorio, String nomeEmpresa, String mensagemRel, String tipoRelatorio, String parserBuscaTags, String designIReport, String nomeUsuario, String filtros) throws Exception {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.setAttribute("xmlRelatorio", xml);
		request.setAttribute("tipoRelatorio", tipoRelatorio);
		request.setAttribute("nomeRelatorio", nomeRelatorio);
		request.setAttribute("nomeEmpresa", nomeEmpresa);
		request.setAttribute("mensagemRel", mensagemRel);
		request.setAttribute("tituloRelatorio", tituloRelatorio);
		request.setAttribute("caminhoParserXML", parserBuscaTags);
		request.setAttribute("nomeDesignIReport", designIReport);
		request.setAttribute("nomeUsuario", nomeUsuario);
		if (filtros.equals("")) {
			filtros = "nenhum";
		}
		request.setAttribute("filtros", filtros);
		context().getExternalContext().dispatch("/VisualizadorRelatorio");
		FacesContext.getCurrentInstance().responseComplete();
	}
	

	@SuppressWarnings("rawtypes")
	public void apresentarRelatorioObjetos(String nomeRelatorio, String tituloRelatorio, String nomeEmpresa, String mensagemRel, String tipoRelatorio, String parserBuscaTags, String designIReport, String nomeUsuario, String filtros, List listaObjetos, String caminhoBaseRelatorio) throws Exception {
		try {

			if (listaObjetos == null || listaObjetos.isEmpty()) {
				throw new ConsistirException("Não há resultados a serem exibidos neste relatório.");
			}
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("tipoRelatorio", tipoRelatorio);
			request.setAttribute("nomeRelatorio", nomeRelatorio);
			request.setAttribute("nomeEmpresa", nomeEmpresa);
			request.setAttribute("mensagemRel", mensagemRel);
			request.setAttribute("tituloRelatorio", tituloRelatorio);
			request.setAttribute("caminhoParserXML", parserBuscaTags);
			request.setAttribute("nomeDesignIReport", designIReport);
			request.setAttribute("nomeUsuario", nomeUsuario);
			request.setAttribute("listaObjetos", listaObjetos);
			request.setAttribute("tipoImplementacao", "OBJETO");
			request.setAttribute("caminhoBaseRelatorio", caminhoBaseRelatorio);
			if (filtros.equals("")) {
				filtros = "nenhum";
			}
			request.setAttribute("filtros", filtros);
			context().getExternalContext().dispatch("/VisualizadorRelatorio");
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	@SuppressWarnings("rawtypes")
	public void apresentarRelatorioObjetosTotalizador(String nomeRelatorio, String tituloRelatorio, String nomeEmpresa, String mensagemRel, String tipoRelatorio, String parserBuscaTags, String designIReport, String nomeUsuario, String filtros, List listaObjetos, Integer total, String caminhoBaseRelatorio) throws Exception {
		try {

			if (listaObjetos == null || listaObjetos.isEmpty()) {
				throw new ConsistirException("Não há resultados a serem exibidos neste relatório.");
			}
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("tipoRelatorio", tipoRelatorio);
			request.setAttribute("nomeRelatorio", nomeRelatorio);
			request.setAttribute("nomeEmpresa", nomeEmpresa);
			request.setAttribute("mensagemRel", mensagemRel);
			request.setAttribute("tituloRelatorio", tituloRelatorio);
			request.setAttribute("caminhoParserXML", parserBuscaTags);
			request.setAttribute("nomeDesignIReport", designIReport);
			request.setAttribute("nomeUsuario", nomeUsuario);
			request.setAttribute("listaObjetos", listaObjetos);
			request.setAttribute("tipoImplementacao", "OBJETO");
			request.setAttribute("caminhoBaseRelatorio", caminhoBaseRelatorio);
			request.setAttribute("total", total);
			if (filtros.equals("")) {
				filtros = "nenhum";
			}
			request.setAttribute("filtros", filtros);
			context().getExternalContext().dispatch("/VisualizadorRelatorio");
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	@SuppressWarnings("rawtypes")
	public void apresentarRelatorioObjetosBoleto(String nomeRelatorio, String tituloRelatorio, String nomeEmpresa, String mensagemRel, String tipoRelatorio, String parserBuscaTags, String designIReport, String nomeUsuario, String filtros, List listaObjetos, String caminhoBaseRelatorio, List<InputStream> imagemBoleto) throws Exception {

		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.setAttribute("imagemBoleto", imagemBoleto);
		apresentarRelatorioObjetos(nomeRelatorio, tituloRelatorio, nomeEmpresa, mensagemRel, tipoRelatorio, parserBuscaTags, designIReport, nomeUsuario, filtros,
				listaObjetos, caminhoBaseRelatorio);
	}

	public void apresentarRelatorio(String nomeRelatorio, String xml, String tituloRelatorio, String nomeEmpresa, String mensagemRel, String tipoRelatorio, String parserBuscaTags, String designIReport, String nomeUsuario, String filtros, String parametro1, String parametro2, String parametro3) throws Exception {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.setAttribute("xmlRelatorio", xml);
		request.setAttribute("tipoRelatorio", tipoRelatorio);
		request.setAttribute("nomeRelatorio", nomeRelatorio);
		request.setAttribute("nomeEmpresa", nomeEmpresa);
		request.setAttribute("mensagemRel", mensagemRel);
		request.setAttribute("tituloRelatorio", tituloRelatorio);
		request.setAttribute("caminhoParserXML", parserBuscaTags);
		request.setAttribute("nomeDesignIReport", designIReport);
		request.setAttribute("nomeUsuario", nomeUsuario);
		request.setAttribute("parametro1", parametro1);
		request.setAttribute("parametro2", parametro2);
		request.setAttribute("parametro3", parametro3);

		if (filtros.equals("")) {
			filtros = "nenhum";
		}
		request.setAttribute("filtros", filtros);
		context().getExternalContext().dispatch("/VisualizadorRelatorio");
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void realizarImpressaoRelatorio() throws Exception {
		try {
			caminhoRelatorio = geradorRelatorio.realizarExportacaoRelatorio(getSuperParametroRelVO());
			// request = (HttpServletRequest) context().getExternalContext().getRequest();
			// request.setAttribute("relatorio", caminhoRelatorio);
			// context().getExternalContext().dispatch("/DownloadRelatorioSV");
			// FacesContext.getCurrentInstance().responseComplete();
			setFazerDownload(true);
		} catch (Exception ex) {
			setFazerDownload(false);
			throw ex;
		} finally {
		}
	}

	public String getDownload() {
		if (getFazerDownload()) {
			setFazerDownload(false);
			try {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return "";
	}

	public String getDownloadMatriculaExterna() {
		if (getFazerDownload()) {
			try {
				return "location.href='../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				setFazerDownload(false);
			}
		}
		return "";
	}

	public Boolean getFazerDownload() {
		if (fazerDownload == null) {
			fazerDownload = false;
		}
		return fazerDownload;
	}

	public void setFazerDownload(Boolean fazerDownload) {
		this.fazerDownload = fazerDownload;
	}

	public void realizarImpressaoRelatorio(SuperParametroRelVO superParametroRe) throws Exception {
		try {
			caminhoRelatorio = geradorRelatorio.realizarExportacaoRelatorio(superParametroRe);
			setFazerDownload(true);
		} catch (Exception ex) {
			setFazerDownload(false);
			throw ex;
		} finally {
		}
	}

	public List<SelectItem> getListaSelectItemOrdenacoesRelatorio() {
		return listaSelectItemOrdenacoesRelatorio;
	}

	public void setListaSelectItemOrdenacoesRelatorio(List<SelectItem> listaSelectItemOrdenacoesRelatorio) {
		this.listaSelectItemOrdenacoesRelatorio = listaSelectItemOrdenacoesRelatorio;
	}

	public Integer getOpcaoOrdenacao() {
		if (opcaoOrdenacao == null) {
			opcaoOrdenacao = 0;
		}
		return opcaoOrdenacao;
	}

	public void setOpcaoOrdenacao(Integer opcaoOrdenacao) {
		this.opcaoOrdenacao = opcaoOrdenacao;
	}

	public class Inicio {
	}

	public SuperParametroRelVO getSuperParametroRelVO() {
		if (superParametroRelVO == null) {
			superParametroRelVO = new SuperParametroRelVO();
		}
		return superParametroRelVO;
	}

	public String getUsarTargetBlank() {
		if (usarTargetBlank == null) {
			usarTargetBlank = "_self";
		}
		return usarTargetBlank;
	}

	public void setUsarTargetBlank(String usarTargetBlank) {
		this.usarTargetBlank = usarTargetBlank;
	}

	public String getLogoPadraoRelatorio() {
		if (FacesContext.getCurrentInstance() != null) {
			LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
			if (loginControle != null && !loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio().trim().isEmpty()) {
				return loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio();
			} else {
				return getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png";
			}
		} else {
			return getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png";
		}
	}

	public String getDownloadComprovanteRecebimentoMatriculaExterna() {
		if (getFazerDownload()) {
			try {
				return "location.href='../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				setFazerDownload(false);
			}
		}
		return "";
	}

	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;
	private Boolean removerEspacoTAGVazia;
	private ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum;

	public Boolean getRemoverEspacoTAGVazia() {
		if (removerEspacoTAGVazia == null) {
			removerEspacoTAGVazia = Boolean.FALSE;
		}
		return removerEspacoTAGVazia;
	}

	public void setRemoverEspacoTAGVazia(Boolean removerEspacoTAGVazia) {
		this.removerEspacoTAGVazia = removerEspacoTAGVazia;
	}

	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
			try {
				List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(getModuloLayoutEtiquetaEnum(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
				for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
					listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemlayoutEtiqueta;
	}

	public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
		this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
	}

	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	public Integer getNumeroCopias() {
		if (numeroCopias == null) {
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
					getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
				}
				for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
					getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the moduloLayoutEtiquetaEnum
	 */
	public ModuloLayoutEtiquetaEnum getModuloLayoutEtiquetaEnum() {
		if (moduloLayoutEtiquetaEnum == null) {
			moduloLayoutEtiquetaEnum = ModuloLayoutEtiquetaEnum.BIBLIOTECA;
		}
		return moduloLayoutEtiquetaEnum;
	}

	/**
	 * @param moduloLayoutEtiquetaEnum
	 *            the moduloLayoutEtiquetaEnum to set
	 */
	public void setModuloLayoutEtiquetaEnum(ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum) {
		this.moduloLayoutEtiquetaEnum = moduloLayoutEtiquetaEnum;
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", filtroRelatorioAcademicoVO.getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado",
				filtroRelatorioAcademicoVO.getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado",
				filtroRelatorioAcademicoVO.getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula",
				filtroRelatorioAcademicoVO.getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada",
				filtroRelatorioAcademicoVO.getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido",
				filtroRelatorioAcademicoVO.getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro",
				filtroRelatorioAcademicoVO.getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaExterna",
				filtroRelatorioAcademicoVO.getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna",
				filtroRelatorioAcademicoVO.getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonado",
				filtroRelatorioAcademicoVO.getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", filtroRelatorioAcademicoVO.getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaAReceber",
				filtroRelatorioAcademicoVO.getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaRecebida",
				filtroRelatorioAcademicoVO.getConfirmado());
		superParametroRelVO.adicionarParametro("filtroAcademicoExcluida",
				filtroRelatorioAcademicoVO.getExcluida());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaDe",
				filtroRelatorioAcademicoVO.getTransferidaDe());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaPara",
				filtroRelatorioAcademicoVO.getTransferidaPara());
	}

	public void imprimirDocumentoAssinadoJaGerada() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItem");
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			getFacadeFactory().getArquivoFacade().carregarArquivoDigitalmenteAssinado(obj.getArquivo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setCaminhoRelatorio(obj.getArquivo().getNome());
			setFazerDownload(true);
			setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<DocumentoAssinadoVO> getListaDocumentoAsssinados() {
		if (listaDocumentoAsssinados == null) {
			listaDocumentoAsssinados = new ArrayList<DocumentoAssinadoVO>();
		}
		return listaDocumentoAsssinados;
	}

	public void setListaDocumentoAsssinados(List<DocumentoAssinadoVO> listaDocumentoAsssinados) {
		this.listaDocumentoAsssinados = listaDocumentoAsssinados;
	}

	public boolean isExibirListaDocumentoAssinados() {
		return !getListaDocumentoAsssinados().isEmpty();
	}

	public boolean isAssinarDigitalmente() {
		return assinarDigitalmente;
	}

	public void setAssinarDigitalmente(boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}

	public String getCorAssinaturaDigitalmente() {
		if (corAssinaturaDigitalmente == null) {
			corAssinaturaDigitalmente = "#000000";
		}
		return corAssinaturaDigitalmente;
	}

	public void setCorAssinaturaDigitalmente(String corAssinaturaDigitalmente) {
		this.corAssinaturaDigitalmente = corAssinaturaDigitalmente;
	}

	public Float getLarguraAssinatura() {
		if (larguraAssinatura == null) {
			larguraAssinatura = 200f;
		}
		return larguraAssinatura;
	}

	public void setLarguraAssinatura(Float larguraAssinatura) {
		this.larguraAssinatura = larguraAssinatura;
	}

	public Float getAlturaAssinatura() {
		if (alturaAssinatura == null) {
			alturaAssinatura = 40f;
		}
		return alturaAssinatura;
	}

	public void setAlturaAssinatura(Float alturaAssinatura) {
		this.alturaAssinatura = alturaAssinatura;
	}

	public String getCertificado() {
		if (getVisualizarCertificado()) {
			return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		}
		return "";
	}

	private Boolean visualizarCertificado;

	public Boolean getVisualizarCertificado() {
		if (visualizarCertificado == null) {
			visualizarCertificado = Boolean.FALSE;
		}
		return visualizarCertificado;
	}

	public void setVisualizarCertificado(Boolean visualizarCertificado) {
		this.visualizarCertificado = visualizarCertificado;
	}
	

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoFinanceira() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			getFiltroRelatorioFinanceiroVO().realizarMarcarTodosTipoOrigem();
		} else {
			getFiltroRelatorioFinanceiroVO().realizarDesmarcarTodosTipoOrigem();
		}
	}

	public Boolean getMarcarTodasSituacoesFinanceiras() {
		if (marcarTodasSituacoesFinanceiras == null) {
			marcarTodasSituacoesFinanceiras = false;
		}
		return marcarTodasSituacoesFinanceiras;
	}

	public void setMarcarTodasSituacoesFinanceiras(Boolean marcarTodasSituacoesFinanceiras) {
		this.marcarTodasSituacoesFinanceiras = marcarTodasSituacoesFinanceiras;
	}

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFinanceiro() {
		if (getMarcarTodasSituacoesFinanceiras()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public void adicionarParametroLogoRelatorio(UnidadeEnsinoVO unidadeEnsinoVO, SuperParametroRelVO superParametroRelVO) throws Exception {
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			unidadeEnsinoVO = (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (!unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().equals("") && !unidadeEnsinoVO.getNomeArquivoLogoRelatorio().equals("")) {
				String caminho = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo();
				if (caminho.contains("/") && !File.separator.equals("/")) {
					caminho = caminho.replace("/", File.separator);
				} else if (caminho.contains("\\") && !File.separator.equals("\\")) {
					caminho = caminho.replace("\\", File.separator);
				}
				if (!caminho.endsWith(File.separator)) {
					caminho += File.separator;
				}
				caminho += unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio();
				if (caminho.contains("/") && !File.separator.equals("/")) {
					caminho = caminho.replace("/", File.separator);
				} else if (caminho.contains("\\") && !File.separator.equals("\\")) {
					caminho = caminho.replace("\\", File.separator);
				}
				superParametroRelVO.adicionarParametro("logoPadraoRelatorio", caminho);
			} else {
				superParametroRelVO.adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
			}
		} else {
			superParametroRelVO.adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
		}
	}

	public void persistirLayoutPadraoSuperRelatorio(String entidade) {
		try {
//			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(String.valueOf(isAssinarDigitalmente()), entidade, "assinarDigitalmente", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLarguraAssinatura().toString(), entidade, "larguraAssinatura", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAlturaAssinatura().toString(), entidade, "alturaAssinatura", getUsuarioLogado());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	public void verificarLayoutPadraoSuperRelatorio(String entidade) {
		Map<String, String> dadosPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "alturaAssinatura", "larguraAssinatura", "assinarDigitalmente" }, entidade);
		for (String key : dadosPadroes.keySet()) {
			if (key.equals("alturaAssinatura")) {
				setAlturaAssinatura(Float.valueOf(dadosPadroes.get(key)));
			} else if (key.equals("larguraAssinatura")) {
				setLarguraAssinatura(Float.valueOf(dadosPadroes.get(key)));
			} 
//			else if (key.equals("assinarDigitalmente")) {
//				setAssinarDigitalmente(Boolean.valueOf(dadosPadroes.get(key)));
//			}
		}
	}
	
	/***
	 * 
	 * Monta COMBOBOX <br> 
	 * 
	 * Enum nao considera opcao de nenhum para fazer o processamento (vide outros montarCombobox)
	 *  
	 * @param enumeradores
	 * @param obrigatorio
	 * @return
	 */
	public List<SelectItem> montarComboboxSemOpcaoDeNenhum(@SuppressWarnings("rawtypes") Enum[] enumeradores, Obrigatorio obrigatorio) {
		return super.montarComboboxSemOpcaoDeNenhum(enumeradores, obrigatorio);
	}
	
	public Boolean getUtilizarCaminhoDownloadArquivoAssinadoDigitalmente() {
		if (utilizarCaminhoDownloadArquivoAssinadoDigitalmente == null) {
			utilizarCaminhoDownloadArquivoAssinadoDigitalmente = false;
		}
		return utilizarCaminhoDownloadArquivoAssinadoDigitalmente;
	}

	public void setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(Boolean utilizarCaminhoDownloadArquivoAssinadoDigitalmente) {
		this.utilizarCaminhoDownloadArquivoAssinadoDigitalmente = utilizarCaminhoDownloadArquivoAssinadoDigitalmente;
	}
	
	public SuperControleRelatorio getSuperControleRelatorio() {
		return this;
	}
	
	public void imprimirArquivoVisualDocumentoAssinadoJaGerada() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItem");
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			getFacadeFactory().getArquivoFacade().carregarArquivoDigitalmenteAssinado(obj.getArquivoVisual(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setCaminhoRelatorio(obj.getArquivoVisual().getNome());
			setFazerDownload(true);
			setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
