/**
 * 
 */
package controle.patrimonio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * @author Rodrigo Wind
 *
 */
@Controller("MapaPatrimonioSeparadoDescarteControle")
@Scope("viewScope")
@Lazy
public class MapaPatrimonioSeparadoDescarteControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7565705263575337908L;
	private List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;
	private List<PatrimonioUnidadeVO> patrimonioUnidadeSelecionadosVOs;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<TipoPatrimonioVO> tipoPatrimonioVOs;
	private DataModelo controleConsultaLocalArmazenamento;
	private List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento;
	private TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor;
	private UnidadeEnsinoVO unidadeEnsino;
	private LocalArmazenamentoVO localArmazenamentoVO;
	private LocalArmazenamentoVO localArmazenamentoDestinoVO;
	private TipoPatrimonioVO tipoPatrimonioVO;
	private String codigoBarra;
	private String observacao;
	private Boolean selecionarTodos;
	private Boolean incluirSubLocal;
	private TextoPadraoPatrimonioVO textoPadraoPatrimonioVO;
	private List<SelectItem> listaSelectItemTextoPadraoPatrimonio;

	public void consultarUnidadePatrimonio() {
		try {
			setPatrimonioUnidadeVOs(getFacadeFactory().getMapaPatrimonioSeparadoDescarteFacade().consultarPatrimonioUnidadeVOs(getUnidadeEnsino().getCodigo(), getLocalArmazenamentoVO(), getIncluirSubLocal(), getTipoPatrimonioVO(), getCodigoBarra(), true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getPatrimonioUnidadeVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarUnidadePatrimonio() {
		getPatrimonioUnidadeSelecionadosVOs().clear();
		getPatrimonioUnidadeSelecionadosVOs().add((PatrimonioUnidadeVO) getRequestMap().get("patrimonioUnidadeVOItens"));
	}

	public void realizarMarcarDesmarcarTodosPatrimonioUnidade() {
		getFacadeFactory().getMapaPatrimonioSeparadoDescarteFacade().realizarMarcarDesmarcarTodosPatrimonioUnidade(getPatrimonioUnidadeVOs(), getSelecionarTodos());
	}

	public void realizarSeparacaoPatrimonioUnidadeSelecionado() {
		try {
			getPatrimonioUnidadeSelecionadosVOs().clear();
			setPatrimonioUnidadeSelecionadosVOs(getFacadeFactory().getMapaPatrimonioSeparadoDescarteFacade().realizarSeparacaoPatrimonioUnidadeSelecionado(getPatrimonioUnidadeVOs()));
			limparMensagem();
		} catch (Exception e) {
			getPatrimonioUnidadeVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void novo(){
		getPatrimonioUnidadeSelecionadosVOs().clear();
		getPatrimonioUnidadeVOs().clear();
		limparLocalArmazenamento();
		limparMensagem();
		limparTipoPatrimonio();
		setCodigoBarra("");
		setIncluirSubLocal(false);
	}
	
	public String getAbrirModalConfirmacao(){
		if(!getPatrimonioUnidadeSelecionadosVOs().isEmpty()){
			return "RichFaces.$('panelFinalizarOcorrencia').show();";
		}
		return "";
	}

	public void persistir() {
		try {
			getFacadeFactory().getMapaPatrimonioSeparadoDescarteFacade().persistir(getPatrimonioUnidadeSelecionadosVOs(), getObservacao(), getLocalArmazenamentoDestinoVO(), true, getUsuarioLogado());
			for(PatrimonioUnidadeVO item: getPatrimonioUnidadeSelecionadosVOs()){
				getPatrimonioUnidadeVOs().remove(item);
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTipoPatrimonio() throws Exception {
		setTipoPatrimonioVO((TipoPatrimonioVO) context().getExternalContext().getRequestMap().get("tipoPatrimonioItens"));
	}

	public void limparTipoPatrimonio() {
		setTipoPatrimonioVO(null);
	}

	public void selecionarLocalArmazenamento() {
		setLocalArmazenamentoVO((LocalArmazenamentoVO) getRequestMap().get("localArmazenamentoVOItens"));
		setControleConsultaLocalArmazenamento(null);
	}
	
	public void selecionarLocalArmazenamentoDestino() {
		setLocalArmazenamentoDestinoVO((LocalArmazenamentoVO) getRequestMap().get("localArmazenamentoVOItens"));
		setControleConsultaLocalArmazenamento(null);
	}

	public void limparLocalArmazenamento() {
		setLocalArmazenamentoVO(null);
	}
	
	public void limparLocalArmazenamentoDestino() {
		setLocalArmazenamentoDestinoVO(null);
	}

	public void consultarTipoPatrimonio() {
		try {
			if (getControleConsulta().getValorConsulta().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			} else if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				setTipoPatrimonioVOs(getFacadeFactory().getTipoPatrimonioFacede().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarLocalArmazenamento() {
		try {
		    	getControleConsultaLocalArmazenamento().setLimitePorPagina(5);
			getControleConsultaLocalArmazenamento().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsino(), getControleConsultaLocalArmazenamento().getLimitePorPagina(), getControleConsultaLocalArmazenamento().getOffset(), false));
			getControleConsultaLocalArmazenamento().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), getUnidadeEnsino(), false));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDadosLocalArmazenamentoPaginador(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaLocalArmazenamento().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaLocalArmazenamento().setPage(dataScrollerEvent.getPage());
		consultarLocalArmazenamento();
	}

	public List<SelectItem> tipoPatrimonioCombo;

	public List<SelectItem> getTipoPatrimonioCombo() {
		if (tipoPatrimonioCombo == null) {
			tipoPatrimonioCombo = new ArrayList<SelectItem>(0);
			tipoPatrimonioCombo.add(new SelectItem("descricao", "Descrição"));
		}
		return tipoPatrimonioCombo;
	}

	/**
	 * @return the patrimonioUnidadeVOs
	 */
	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>();
		}
		return patrimonioUnidadeVOs;
	}

	/**
	 * @param patrimonioUnidadeVOs
	 *            the patrimonioUnidadeVOs to set
	 */
	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino = UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the tipoPatrimonioVOs
	 */
	public List<TipoPatrimonioVO> getTipoPatrimonioVOs() {
		if (tipoPatrimonioVOs == null) {
			tipoPatrimonioVOs = new ArrayList<TipoPatrimonioVO>(0);
		}
		return tipoPatrimonioVOs;
	}

	/**
	 * @param tipoPatrimonioVOs
	 *            the tipoPatrimonioVOs to set
	 */
	public void setTipoPatrimonioVOs(List<TipoPatrimonioVO> tipoPatrimonioVOs) {
		this.tipoPatrimonioVOs = tipoPatrimonioVOs;
	}

	/**
	 * @return the controleConsultaLocalArmazenamento
	 */
	public DataModelo getControleConsultaLocalArmazenamento() {
		if (controleConsultaLocalArmazenamento == null) {
			controleConsultaLocalArmazenamento = new DataModelo();
		}
		return controleConsultaLocalArmazenamento;
	}

	/**
	 * @param controleConsultaLocalArmazenamento
	 *            the controleConsultaLocalArmazenamento to set
	 */
	public void setControleConsultaLocalArmazenamento(DataModelo controleConsultaLocalArmazenamento) {
		this.controleConsultaLocalArmazenamento = controleConsultaLocalArmazenamento;
	}

	/**
	 * @return the listaSelectItemOpcaoConsultaLocalArmazenamento
	 */
	public List<SelectItem> getListaSelectItemOpcaoConsultaLocalArmazenamento() {
		if (listaSelectItemOpcaoConsultaLocalArmazenamento == null) {
			listaSelectItemOpcaoConsultaLocalArmazenamento = UtilSelectItem.getListaSelectItemEnum(TipoConsultaLocalArmazenamentoEnum.values(), Obrigatorio.SIM);
			;
		}
		return listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	/**
	 * @param listaSelectItemOpcaoConsultaLocalArmazenamento
	 *            the listaSelectItemOpcaoConsultaLocalArmazenamento to set
	 */
	public void setListaSelectItemOpcaoConsultaLocalArmazenamento(List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento) {
		this.listaSelectItemOpcaoConsultaLocalArmazenamento = listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	/**
	 * @return the consultarLocalArmazenamentoPor
	 */
	public TipoConsultaLocalArmazenamentoEnum getConsultarLocalArmazenamentoPor() {
		if (consultarLocalArmazenamentoPor == null) {
			consultarLocalArmazenamentoPor = TipoConsultaLocalArmazenamentoEnum.LOCAL;
		}
		return consultarLocalArmazenamentoPor;
	}

	/**
	 * @param consultarLocalArmazenamentoPor
	 *            the consultarLocalArmazenamentoPor to set
	 */
	public void setConsultarLocalArmazenamentoPor(TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor) {
		this.consultarLocalArmazenamentoPor = consultarLocalArmazenamentoPor;
	}

	/**
	 * @return the codigoBarra
	 */
	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	/**
	 * @param codigoBarra
	 *            the codigoBarra to set
	 */
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	/**
	 * @param observacao
	 *            the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/**
	 * @return the localArmazenamentoVO
	 */
	public LocalArmazenamentoVO getLocalArmazenamentoVO() {
		if (localArmazenamentoVO == null) {
			localArmazenamentoVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoVO;
	}

	/**
	 * @param localArmazenamentoVO
	 *            the localArmazenamentoVO to set
	 */
	public void setLocalArmazenamentoVO(LocalArmazenamentoVO localArmazenamentoVO) {
		this.localArmazenamentoVO = localArmazenamentoVO;
	}

	/**
	 * @return the tipoPatrimonioVO
	 */
	public TipoPatrimonioVO getTipoPatrimonioVO() {
		if (tipoPatrimonioVO == null) {
			tipoPatrimonioVO = new TipoPatrimonioVO();
		}
		return tipoPatrimonioVO;
	}

	/**
	 * @param tipoPatrimonioVO
	 *            the tipoPatrimonioVO to set
	 */
	public void setTipoPatrimonioVO(TipoPatrimonioVO tipoPatrimonioVO) {
		this.tipoPatrimonioVO = tipoPatrimonioVO;
	}

	/**
	 * @return the qtdePatrimonioSelecionado
	 */
	public Integer getQtdePatrimonioSelecionado() {
		return getPatrimonioUnidadeSelecionadosVOs().size();
	}

	/**
	 * @return the patrimonioUnidadeSelecionadosVOs
	 */
	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeSelecionadosVOs() {
		if (patrimonioUnidadeSelecionadosVOs == null) {
			patrimonioUnidadeSelecionadosVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeSelecionadosVOs;
	}

	/**
	 * @param patrimonioUnidadeSelecionadosVOs
	 *            the patrimonioUnidadeSelecionadosVOs to set
	 */
	public void setPatrimonioUnidadeSelecionadosVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeSelecionadosVOs) {
		this.patrimonioUnidadeSelecionadosVOs = patrimonioUnidadeSelecionadosVOs;
	}

	/**
	 * @return the incluirSubLocal
	 */
	public Boolean getIncluirSubLocal() {
		if (incluirSubLocal == null) {
			incluirSubLocal = false;
		}
		return incluirSubLocal;
	}

	/**
	 * @param incluirSubLocal
	 *            the incluirSubLocal to set
	 */
	public void setIncluirSubLocal(Boolean incluirSubLocal) {
		this.incluirSubLocal = incluirSubLocal;
	}

	/**
	 * @return the selecionarTodos
	 */
	public Boolean getSelecionarTodos() {
		if (selecionarTodos == null) {
			selecionarTodos = true;
		}
		return selecionarTodos;
	}

	/**
	 * @param selecionarTodos
	 *            the selecionarTodos to set
	 */
	public void setSelecionarTodos(Boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
	}

	/**
	 * @return the localArmazenamentoDestinoVO
	 */
	public LocalArmazenamentoVO getLocalArmazenamentoDestinoVO() {
		if (localArmazenamentoDestinoVO == null) {
			localArmazenamentoDestinoVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoDestinoVO;
	}

	/**
	 * @param localArmazenamentoDestinoVO the localArmazenamentoDestinoVO to set
	 */
	public void setLocalArmazenamentoDestinoVO(LocalArmazenamentoVO localArmazenamentoDestinoVO) {
		this.localArmazenamentoDestinoVO = localArmazenamentoDestinoVO;
	}
	
	public TextoPadraoPatrimonioVO getTextoPadraoPatrimonioVO() {
		if(textoPadraoPatrimonioVO == null){
			textoPadraoPatrimonioVO = new TextoPadraoPatrimonioVO();
		}
		return textoPadraoPatrimonioVO;
	}

	public void setTextoPadraoPatrimonioVO(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		this.textoPadraoPatrimonioVO = textoPadraoPatrimonioVO;
	}
	
	@PostConstruct
	public void montarListaSelectItemTextoPadraoPatrimonio() {
		try {
			List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = getFacadeFactory().getTextoPadraoPatrimonioFacade().consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum.ATIVO, TipoUsoTextoPadraoPatrimonioEnum.MAPA_DESCARTE);
			setListaSelectItemTextoPadraoPatrimonio(UtilSelectItem.getListaSelectItem(textoPadraoPatrimonioVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the listaSelectItemTextoPadraoPatrimonio
	 */
	public List<SelectItem> getListaSelectItemTextoPadraoPatrimonio() {
		if (listaSelectItemTextoPadraoPatrimonio == null) {
			listaSelectItemTextoPadraoPatrimonio = new ArrayList<SelectItem>();
		}
		return listaSelectItemTextoPadraoPatrimonio;
	}

	/**
	 * @param listaSelectItemTextoPadraoPatrimonio
	 *            the listaSelectItemTextoPadraoPatrimonio to set
	 */
	public void setListaSelectItemTextoPadraoPatrimonio(List<SelectItem> listaSelectItemTextoPadraoPatrimonio) {
		this.listaSelectItemTextoPadraoPatrimonio = listaSelectItemTextoPadraoPatrimonio;
	}
	
	public void realizarImpressaoTextoPadrao() {
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");		
			OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
			ocorrenciaPatrimonioVO.setObservacao(observacao);
			ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setCodigo(getUsuarioLogado().getCodigo());
			ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setNome(getUsuarioLogado().getNome());
			ocorrenciaPatrimonioVO.setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum.DESCARTE);
			ocorrenciaPatrimonioVO.setDataOcorrencia(new Date());
			ocorrenciaPatrimonioVO.setLocalArmazenamentoDestino(getLocalArmazenamentoDestinoVO());
			this.setCaminhoRelatorio(getFacadeFactory().getTextoPadraoPatrimonioFacade().realizarImpressaoTextoPadraoPatrimonio(getTextoPadraoPatrimonioVO(), ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO(), ocorrenciaPatrimonioVO, getPatrimonioUnidadeSelecionadosVOs(), null, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}
	
	

}
