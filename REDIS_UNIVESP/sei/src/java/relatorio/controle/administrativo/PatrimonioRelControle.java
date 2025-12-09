/**
 * 
 */
package relatorio.controle.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.PatrimonioRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * @author Leonardo Riciolle
 *
 */
@Controller("PatrimonioRelControle")
@Scope("viewScope")
@Lazy
public class PatrimonioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private String tipoRelatorio;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private PatrimonioRelVO patrimonioRelVO;
	private LocalArmazenamentoVO localArmazenamentoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<LocalArmazenamentoVO> armazenamentoVOs;
	private List<SelectItem> tipoConsultaComboLocalArmazenamento;
	private List<SelectItem> listaSelectItemTipoPatrimonio;
	private List<PatrimonioVO> patrimonioVOs;
	private List<SelectItem> tipoConsultaCombo;
	private TipoPatrimonioVO tipoPatrimonioVO;
	private PatrimonioVO patrimonioVO;
	private String campoLocalArmazenamento;
	private String valorConsultaLocalArmazenamento;
	private String layout;
	private String ordenar;
	private Boolean apresentarValor;

	public PatrimonioRelControle() throws Exception {
		inicializarDadosListaSelectItemUnidadelEnsino();
		inicializarDadosListaSelectItemTipoPatrimonio();
		setUnidadeEnsinoVO(null);
		setLocalArmazenamentoVO(null);
		setTipoPatrimonioVO(null);
		setPatrimonioVO(null);
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		List<PatrimonioRelVO> listaObjetos = new ArrayList<PatrimonioRelVO>(0);
		try {
			getFacadeFactory().getPatrimonioRelInterfaceFacade().validarDados(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO());
			if (getLayout().equals("AN")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().criarObjetoAnalitico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioAnalitico());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Patrimônio Analítico");
			} else if (getLayout().equals("SP")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSintetico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPatrimoio());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Por Patrimônio");
			} else if (getLayout().equals("ST")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSintetico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPorTipoPatrimoio());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Por Tipo Patrimônio");
			} else {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSinteticoLocalArmazenamento(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPorLocalArmazenamento());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Local Armazenamento");
			}
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
				getSuperParametroRelVO().adicionarParametro("local", getLocalArmazenamentoVO().getLocalArmazenamento().toString());
				getSuperParametroRelVO().adicionarParametro("tipoPatrimonio", getFacadeFactory().getTipoPatrimonioFacede().consultarPorChavePrimaria(getTipoPatrimonioVO().getCodigo(), false, getUsuarioLogado()).getDescricao());
				getSuperParametroRelVO().adicionarParametro("patrimonio", getPatrimonioVO().getDescricao().toString());
				getSuperParametroRelVO().adicionarParametro("apresentarValor", getApresentarValor());				
				adicionarFiltroSituacaoPatrimonio(getSuperParametroRelVO());
				realizarImpressaoRelatorio();				
				removerObjetoMemoria(this);
				inicializarDadosListaSelectItemUnidadelEnsino();
				inicializarDadosListaSelectItemTipoPatrimonio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void imprimirExcel() {
		List<PatrimonioRelVO> listaObjetos = new ArrayList<PatrimonioRelVO>(0);
		try {
			getFacadeFactory().getPatrimonioRelInterfaceFacade().validarDados(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO());
			if (getLayout().equals("AN")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().criarObjetoAnalitico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioAnalitico());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Patrimônio Analítico");
			} else if (getLayout().equals("SP")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSintetico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPatrimoio());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Por Patrimônio");
			} else if (getLayout().equals("ST")) {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSintetico(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPorTipoPatrimoio());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Por Tipo Patrimônio");
			} else {
				listaObjetos = getFacadeFactory().getPatrimonioRelInterfaceFacade().executarConsultaParametrizadaSinteticoLocalArmazenamento(getPatrimonioRelVO(), getUnidadeEnsinoVO(), getPatrimonioVO(), getTipoPatrimonioVO(), getLocalArmazenamentoVO(), getUsuarioLogado(), getOrdenar());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPatrimonioRelInterfaceFacade().designIReportRelatorioSinteticoPorLocalArmazenamento());
				getSuperParametroRelVO().setTituloRelatorio("Relatorio Sintético Local Armazenamento");
			}
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				getSuperParametroRelVO().adicionarParametro("local", getLocalArmazenamentoVO().getLocalArmazenamento().toString());
				getSuperParametroRelVO().adicionarParametro("tipoPatrimonio", getFacadeFactory().getTipoPatrimonioFacede().consultarPorChavePrimaria(getTipoPatrimonioVO().getCodigo(), false, getUsuarioLogado()).getDescricao());
				getSuperParametroRelVO().adicionarParametro("patrimonio", getPatrimonioVO().getDescricao().toString());
				adicionarFiltroSituacaoPatrimonio(getSuperParametroRelVO());
				realizarImpressaoRelatorio();				
				removerObjetoMemoria(this);
				inicializarDadosListaSelectItemUnidadelEnsino();
				inicializarDadosListaSelectItemTipoPatrimonio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void adicionarFiltroSituacaoPatrimonio(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("emUso", getPatrimonioRelVO().getEmUso());
		superParametroRelVO.adicionarParametro("manutencao", getPatrimonioRelVO().getManutencao());
		superParametroRelVO.adicionarParametro("separadoParaDescarte", getPatrimonioRelVO().getSeparadoParaDescarte());
		superParametroRelVO.adicionarParametro("descartado", getPatrimonioRelVO().getDescartado());
		superParametroRelVO.adicionarParametro("emprestado", getPatrimonioRelVO().getEmprestado());
	}

	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("AN", "Analítico"));
		lista.add(new SelectItem("SP", "Sintético Por Patrimônio"));
		lista.add(new SelectItem("ST", "Sintético Por Tipo Patrimônio"));
		lista.add(new SelectItem("SL", "Sintético Por Local"));
		return lista;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("LO", "Local"));
		lista.add(new SelectItem("PA", "Patrimônio"));
		lista.add(new SelectItem("ST", "Situação"));
		lista.add(new SelectItem("TP", "Tipo Patrimônio"));
		return lista;
	}

	public void inicializarDadosListaSelectItemUnidadelEnsino() {
		try {
			List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosListaSelectItemTipoPatrimonio() {
		try {
			List<TipoPatrimonioVO> listaResultado = getFacadeFactory().getTipoPatrimonioFacede().consultarPorDescricao("", false, getUsuarioLogado());
			setListaSelectItemTipoPatrimonio(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparLocalArmazenamento() throws Exception {
		try {
			setLocalArmazenamentoVO(null);
			setArmazenamentoVOs(null);
		} catch (Exception e) {
		}
	}

	public void limparPatrimonio() throws Exception {
		try {
			setPatrimonioVO(null);
			setPatrimonioVOs(null);
		} catch (Exception e) {
		}
	}

	public void consultarLocalArmazenamento() {
		try {
			if (getCampoLocalArmazenamento().equals("codigo")) {
				if (getValorConsultaLocalArmazenamento().equals("")) {
					setValorConsultaLocalArmazenamento("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaLocalArmazenamento());
				setArmazenamentoVOs(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorChavePrimaria(valorInt, false, getUsuarioLogado()));
			}
			if (getCampoLocalArmazenamento().equals("localArmazenamento")) {
				setArmazenamentoVOs(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorLocalArmazenamento(getValorConsultaLocalArmazenamento(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPatrimonio() {
		try {
			if (getControleConsulta().getValorConsulta().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			} else {
				setPatrimonioVOs(getFacadeFactory().getPatrimonioFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado(), NivelMontarDados.BASICO, 0, 0));
				setMensagemID("msg_dados_consultados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboLocalArmazenamento() {
		if (tipoConsultaComboLocalArmazenamento == null) {
			tipoConsultaComboLocalArmazenamento = new ArrayList<SelectItem>(0);
			tipoConsultaComboLocalArmazenamento.add(new SelectItem("localArmazenamento", "Local Armazenamento"));
			tipoConsultaComboLocalArmazenamento.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboLocalArmazenamento;
	}

	public List<SelectItem> getTipoPatrimonioCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("descricao", "Patrimônio"));
			tipoConsultaCombo.add(new SelectItem("tipopatrimonio", "Tipo Patrimônio"));
			if (getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				tipoConsultaCombo.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
			}
		}
		return tipoConsultaCombo;
	}

	public void selecionarLocalArmazenamento() throws Exception {
		try {
			setLocalArmazenamentoVO((LocalArmazenamentoVO) context().getExternalContext().getRequestMap().get("armazenamentoItens"));
			getUnidadeEnsinoVO().setCodigo(getLocalArmazenamentoVO().getUnidadeEnsinoVO().getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public void selecionarPatrimonio() throws Exception {
		try {
			setPatrimonioVO((PatrimonioVO) context().getExternalContext().getRequestMap().get("patrimonioItens"));
		} catch (Exception e) {
			throw e;
		}
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
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

	public String getCampoLocalArmazenamento() {
		if (campoLocalArmazenamento == null) {
			campoLocalArmazenamento = "";
		}
		return campoLocalArmazenamento;
	}

	public void setCampoLocalArmazenamento(String campoLocalArmazenamento) {
		this.campoLocalArmazenamento = campoLocalArmazenamento;
	}

	public String getValorConsultaLocalArmazenamento() {
		if (valorConsultaLocalArmazenamento == null) {
			valorConsultaLocalArmazenamento = "";
		}
		return valorConsultaLocalArmazenamento;
	}

	public void setValorConsultaLocalArmazenamento(String valorConsultaLocalArmazenamento) {
		this.valorConsultaLocalArmazenamento = valorConsultaLocalArmazenamento;
	}

	public List<LocalArmazenamentoVO> getArmazenamentoVOs() {
		if (armazenamentoVOs == null) {
			armazenamentoVOs = new ArrayList<LocalArmazenamentoVO>(0);
		}
		return armazenamentoVOs;
	}

	public void setArmazenamentoVOs(List<LocalArmazenamentoVO> armazenamentoVOs) {
		this.armazenamentoVOs = armazenamentoVOs;
	}

	public LocalArmazenamentoVO getLocalArmazenamentoVO() {
		if (localArmazenamentoVO == null) {
			localArmazenamentoVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoVO;
	}

	public void setLocalArmazenamentoVO(LocalArmazenamentoVO localArmazenamentoVO) {
		this.localArmazenamentoVO = localArmazenamentoVO;
	}

	public void setTipoConsultaComboLocalArmazenamento(List<SelectItem> tipoConsultaComboLocalArmazenamento) {
		this.tipoConsultaComboLocalArmazenamento = tipoConsultaComboLocalArmazenamento;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<SelectItem> getListaSelectItemTipoPatrimonio() {
		if (listaSelectItemTipoPatrimonio == null) {
			listaSelectItemTipoPatrimonio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoPatrimonio;
	}

	public void setListaSelectItemTipoPatrimonio(List<SelectItem> listaSelectItemTipoPatrimonio) {
		this.listaSelectItemTipoPatrimonio = listaSelectItemTipoPatrimonio;
	}

	public List<PatrimonioVO> getPatrimonioVOs() {
		if (patrimonioVOs == null) {
			patrimonioVOs = new ArrayList<PatrimonioVO>(0);
		}
		return patrimonioVOs;
	}

	public void setPatrimonioVOs(List<PatrimonioVO> patrimonioVOs) {
		this.patrimonioVOs = patrimonioVOs;
	}

	public TipoPatrimonioVO getTipoPatrimonioVO() {
		if (tipoPatrimonioVO == null) {
			tipoPatrimonioVO = new TipoPatrimonioVO();
		}
		return tipoPatrimonioVO;
	}

	public void setTipoPatrimonioVO(TipoPatrimonioVO tipoPatrimonioVO) {
		this.tipoPatrimonioVO = tipoPatrimonioVO;
	}

	public PatrimonioVO getPatrimonioVO() {
		if (patrimonioVO == null) {
			patrimonioVO = new PatrimonioVO();
		}
		return patrimonioVO;
	}

	public void setPatrimonioVO(PatrimonioVO patrimonioVO) {
		this.patrimonioVO = patrimonioVO;
	}

	public String getOrdenar() {
		if (ordenar == null) {
			ordenar = "";
		}
		return ordenar;
	}

	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
	}

	public PatrimonioRelVO getPatrimonioRelVO() {
		if (patrimonioRelVO == null) {
			patrimonioRelVO = new PatrimonioRelVO();
		}
		return patrimonioRelVO;
	}

	public void setPatrimonioRelVO(PatrimonioRelVO patrimonioRelVO) {
		this.patrimonioRelVO = patrimonioRelVO;
	}
	
	public Boolean getApresentarValor() {
		if(apresentarValor == null){
			apresentarValor = getLoginControle().getPermissaoAcessoMenuVO().getPermitirApresentarValorRecursoPatrimonioRel();
		}
		return apresentarValor;
	}

	public void setApresentarValor(Boolean apresentarValor) {
		this.apresentarValor = apresentarValor;
	}

}
