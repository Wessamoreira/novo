package controle.patrimonio;

/**
 * 
 * @author Leonardo Riciolle
 */

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.EstadoBemEnum;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("PatrimonioControle")
@Scope("viewScope")
@Lazy
public class PatrimonioControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -8857992915198731809L;
	private PatrimonioVO patrimonioVO;
	private PatrimonioUnidadeVO patrimonioUnidadeVO;
	private List<FornecedorVO> fornecedorVOs;
	private List<TipoPatrimonioVO> tipoPatrimonioVOs;
	private DataModelo controleConsultaOtimizadoLocalArmazenamento;
	private List<SelectItem> tipoConsultaComboLocalArmazenamando;
	private TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor;
	private String codigoBarraInicial;
	private Integer quantidadePatrimonioUnidadeGerar;
	private List<SelectItem> listaSelectItemTextoPadraoPatrimonio;
	private List<LocalArmazenamentoVO> localArmazenamentoVOs ;
	private TextoPadraoPatrimonioVO textoPadraoPatrimonioVO;

	public PatrimonioControle() throws Exception {

	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioCons");
	}

	public String novo() throws Exception {
		try {
			setPatrimonioVO(new PatrimonioVO());
			setPatrimonioUnidadeVO(new PatrimonioUnidadeVO());
			removerObjetoMemoria(this);
			setControleConsulta(new ControleConsulta());
			getPatrimonioVO().setResponsavel(getUsuarioLogadoClone());
			setCodigoBarraInicial(getFacadeFactory().getPatrimonioUnidadeFacade().consultarSugestaoCodigoBarra(getPatrimonioVO()));
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioForm");

	}

	public String gravar() {
		try {
			getFacadeFactory().getPatrimonioFacade().persistir(getPatrimonioVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioForm");
	}

	public String excluir() {
		try {
			getFacadeFactory().getPatrimonioFacade().excluir(getPatrimonioVO(), false, getUsuarioLogado());
			setPatrimonioVO(new PatrimonioVO());
			setPatrimonioUnidadeVO(new PatrimonioUnidadeVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioForm");
	}

	public String editar() throws Exception {
		try {
			PatrimonioVO obj = ((PatrimonioVO) context().getExternalContext().getRequestMap().get("patrimonioItens"));
			setPatrimonioVO(getFacadeFactory().getPatrimonioFacade().consultarPorChavePrimaria(obj.getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			setCodigoBarraInicial(getFacadeFactory().getPatrimonioUnidadeFacade().consultarSugestaoCodigoBarra(getPatrimonioVO()));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioForm");
	}

	public void realizarImpressaoTermoRecebimentoPatrimonio() {
		try {

		} catch (Exception e) {

		}
	}

	public String consultar() throws Exception {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPatrimonioFacade().consultar(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getCampoConsulta(), true, getUsuarioLogado(), NivelMontarDados.BASICO, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPatrimonioFacade().consultarTotalRegistro(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getCampoConsulta()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("patrimonioCons");
	}

	public void consultarDadosPaginador(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarDados();
	}

	public void adicionarPatrimonioUnidadeVOs() {
		try {
			getFacadeFactory().getPatrimonioFacade().adicionarPatrimonioUnidadeVOs(getPatrimonioVO(), getPatrimonioUnidadeVO(), getQuantidadePatrimonioUnidadeGerar(), getCodigoBarraInicial());
			setCodigoBarraInicial(getFacadeFactory().getPatrimonioUnidadeFacade().consultarSugestaoCodigoBarra(getPatrimonioVO()));
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			if (getControleConsulta().getValorConsulta().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			} else if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				setFornecedorVOs(getFacadeFactory().getFornecedorFacade().consultarPorNome(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("cpf")) {
				setFornecedorVOs(getFacadeFactory().getFornecedorFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
			getControleConsultaOtimizadoLocalArmazenamento().getListaConsulta().clear();
			getControleConsultaOtimizadoLocalArmazenamento().setLimitePorPagina(5);
			getControleConsultaOtimizadoLocalArmazenamento().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarLocalArmazenamentoPor(), getControleConsultaOtimizadoLocalArmazenamento().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaOtimizadoLocalArmazenamento().getLimitePorPagina(), getControleConsultaOtimizadoLocalArmazenamento().getOffset(), false));			
			getControleConsultaOtimizadoLocalArmazenamento().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarLocalArmazenamentoPor(), getControleConsultaOtimizadoLocalArmazenamento().getValorConsulta(),  getUnidadeEnsinoLogado(), false));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDadosLocalArmazenamentoPaginador(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizadoLocalArmazenamento().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizadoLocalArmazenamento().setPage(DataScrollEvent.getPage());
		consultarLocalArmazenamento();
	}

	public void selecionarFornecedor() throws Exception {
		getPatrimonioVO().setFornecedorVO((FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens"));
	}

	public void selecionarTipoPatrimonio() throws Exception {
		getPatrimonioVO().setTipoPatrimonioVO((TipoPatrimonioVO) context().getExternalContext().getRequestMap().get("tipoPatrimonioItens"));
	}

	public void selecionarLocalArmazenamento() throws Exception {
		getPatrimonioUnidadeVO().setLocalArmazenamento((LocalArmazenamentoVO) context().getExternalContext().getRequestMap().get("localArmazenamentoItens"));
		setControleConsultaOtimizadoLocalArmazenamento(new DataModelo());
	}

	public void selecionarCompra() throws Exception {
		getPatrimonioVO().setCompraVO((CompraVO) context().getExternalContext().getRequestMap().get("compra"));
		getPatrimonioVO().getFornecedorVO().setCodigo(getPatrimonioVO().getFornecedorVO().getCodigo());
		getPatrimonioVO().getFornecedorVO().setNome(getPatrimonioVO().getFornecedorVO().getNome());
		setMensagemID("");
	}

	public void selecionarPatrimonioUnidade() {
		setPatrimonioUnidadeVO((PatrimonioUnidadeVO) context().getExternalContext().getRequestMap().get("patrimonioUnidade"));
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarDados();
	}

	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("descricao", "Patrimônio"));
			tipoConsultaCombo.add(new SelectItem("codigoBarra", "Código Barra"));
			tipoConsultaCombo.add(new SelectItem("fornecedor", "Fornecedor"));
			tipoConsultaCombo.add(new SelectItem("tipopatrimonio", "Tipo Patrimônio"));
			tipoConsultaCombo.add(new SelectItem("notafiscal", "Nota Fiscal"));
			tipoConsultaCombo.add(new SelectItem("local", "Local de Armazenamento"));
			tipoConsultaCombo.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));			
		}
		return tipoConsultaCombo;
	}

	public List<SelectItem> tipoFornecedorCombo;

	public List<SelectItem> getTipoFornecedorCombo() {
		if (tipoFornecedorCombo == null) {
			tipoFornecedorCombo = new ArrayList<SelectItem>(0);
			tipoFornecedorCombo.add(new SelectItem("descricao", "Descrição"));
//			tipoFornecedorCombo.add(new Sele'ctItem("cpf", "CPF"));
		}
		return tipoFornecedorCombo;
	}

	public List<SelectItem> tipoPatrimonioCombo;

	public List<SelectItem> getTipoPatrimonioCombo() {
		if (tipoPatrimonioCombo == null) {
			tipoPatrimonioCombo = new ArrayList<SelectItem>(0);
			tipoPatrimonioCombo.add(new SelectItem("descricao", "Descrição"));
		}
		return tipoPatrimonioCombo;
	}

	public List<SelectItem> tipoCompraCombo;

	public List<SelectItem> getTipoCompraCombo() {
		if (tipoCompraCombo == null) {
			tipoCompraCombo = new ArrayList<SelectItem>(0);
			tipoCompraCombo.add(new SelectItem("fornecedor", "Fornecedor"));
			tipoCompraCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoCompraCombo;
	}

	public void limparFornecedor() throws Exception {
		setFornecedorVOs(null);
		getPatrimonioVO().setFornecedorVO(null);
	}

	public void limparTipoPatrimonio() throws Exception {
		setTipoPatrimonioVOs(null);
		getPatrimonioVO().setTipoPatrimonioVO(null);
	}

	public void removerPatrimonioUnidade() {
		try {
			PatrimonioUnidadeVO obj = (PatrimonioUnidadeVO) context().getExternalContext().getRequestMap().get("patrimonioUnidadeItens");
			getFacadeFactory().getPatrimonioFacade().removerPatrimonioUnidadeVOs(getPatrimonioVO(), obj);
			setCodigoBarraInicial(getFacadeFactory().getPatrimonioUnidadeFacade().consultarSugestaoCodigoBarra(getPatrimonioVO()));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public List<FornecedorVO> getFornecedorVOs() {
		if (fornecedorVOs == null) {
			fornecedorVOs = new ArrayList<FornecedorVO>(0);
		}
		return fornecedorVOs;
	}

	public void setFornecedorVOs(List<FornecedorVO> fornecedorVOs) {
		this.fornecedorVOs = fornecedorVOs;
	}

	public List<TipoPatrimonioVO> getTipoPatrimonioVOs() {
		if (tipoPatrimonioVOs == null) {
			tipoPatrimonioVOs = new ArrayList<TipoPatrimonioVO>(0);
		}
		return tipoPatrimonioVOs;
	}

	public void setTipoPatrimonioVOs(List<TipoPatrimonioVO> tipoPatrimonioVOs) {
		this.tipoPatrimonioVOs = tipoPatrimonioVOs;
	}

	public Integer getQuantidadePatrimonioUnidadeGerar() {
		if (quantidadePatrimonioUnidadeGerar == null) {
			quantidadePatrimonioUnidadeGerar = 1;
		}
		return quantidadePatrimonioUnidadeGerar;
	}

	public void setQuantidadePatrimonioUnidadeGerar(Integer quantidadePatrimonioUnidadeGerar) {
		this.quantidadePatrimonioUnidadeGerar = quantidadePatrimonioUnidadeGerar;
	}

	public PatrimonioUnidadeVO getPatrimonioUnidadeVO() {
		if (patrimonioUnidadeVO == null) {
			patrimonioUnidadeVO = new PatrimonioUnidadeVO();
		}
		return patrimonioUnidadeVO;
	}

	public void setPatrimonioUnidadeVO(PatrimonioUnidadeVO patrimonioUnidadeVO) {
		this.patrimonioUnidadeVO = patrimonioUnidadeVO;
	}

	public void realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade() {
		try {
			PatrimonioUnidadeVO obj = (PatrimonioUnidadeVO) context().getExternalContext().getRequestMap().get("patrimonioUnidadeItens");
			getFacadeFactory().getPatrimonioFacade().realizarValidacaoUnicidadeNumeroSeriePatrimonioUnidade(getPatrimonioVO(), obj);
			setMensagemID("msg_dados_validados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade() {
		try {
			PatrimonioUnidadeVO obj = (PatrimonioUnidadeVO) context().getExternalContext().getRequestMap().get("patrimonioUnidadeItens");
			getFacadeFactory().getPatrimonioFacade().realizarValidacaoUnicidadeCodigoBarraPatrimonioUnidade(getPatrimonioVO(), obj);
			setMensagemID("msg_dados_validados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarCampoDataCompra() {
		return getControleConsulta().getCampoConsulta().equals("dataCompra");
	}

	public DataModelo getControleConsultaOtimizadoLocalArmazenamento() {
		if (controleConsultaOtimizadoLocalArmazenamento == null) {
			controleConsultaOtimizadoLocalArmazenamento = new DataModelo();
		}
		return controleConsultaOtimizadoLocalArmazenamento;
	}

	public void setControleConsultaOtimizadoLocalArmazenamento(DataModelo controleConsultaOtimizadoLocalArmazenamento) {
		this.controleConsultaOtimizadoLocalArmazenamento = controleConsultaOtimizadoLocalArmazenamento;
	}

	public List<SelectItem> getTipoConsultaComboLocalArmazenamando() {
		if (tipoConsultaComboLocalArmazenamando == null) {
			tipoConsultaComboLocalArmazenamando = UtilSelectItem.getListaSelectItemEnum(TipoConsultaLocalArmazenamentoEnum.values(), Obrigatorio.SIM);
		}
		return tipoConsultaComboLocalArmazenamando;
	}

	public void setTipoConsultaComboLocalArmazenamando(List<SelectItem> tipoConsultaComboLocalArmazenamando) {
		this.tipoConsultaComboLocalArmazenamando = tipoConsultaComboLocalArmazenamando;
	}

	public TipoConsultaLocalArmazenamentoEnum getConsultarLocalArmazenamentoPor() {
		if (consultarLocalArmazenamentoPor == null) {
			consultarLocalArmazenamentoPor = TipoConsultaLocalArmazenamentoEnum.LOCAL;
		}
		return consultarLocalArmazenamentoPor;
	}

	public void setConsultarLocalArmazenamentoPor(TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor) {
		this.consultarLocalArmazenamentoPor = consultarLocalArmazenamentoPor;
	}

	public String getCodigoBarraInicial() {
		if (codigoBarraInicial == null) {
			codigoBarraInicial = "";
		}
		return codigoBarraInicial;
	}

	public void setCodigoBarraInicial(String codigoBarraInicial) {
		this.codigoBarraInicial = codigoBarraInicial;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoPatrimonio() {
		if (listaSelectItemTextoPadraoPatrimonio == null) {
			montarListaSelectItemTextoPadraoPatrimonio();
		}
		return listaSelectItemTextoPadraoPatrimonio;
	}

	public void setListaSelectItemTextoPadraoPatrimonio(List<SelectItem> listaSelectItemTextoPadraoPatrimonio) {
		this.listaSelectItemTextoPadraoPatrimonio = listaSelectItemTextoPadraoPatrimonio;
	}
	
	public void montarListaSelectItemTextoPadraoPatrimonio(){
		try{
			List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = getFacadeFactory().getTextoPadraoPatrimonioFacade().consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum.ATIVO, TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO);
			setListaSelectItemTextoPadraoPatrimonio(UtilSelectItem.getListaSelectItem(textoPadraoPatrimonioVOs, "codigo", "nome", false));
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarImpressaoTextoPadraoPatrimonio(){
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			LocalArmazenamentoVO localArmazenamentoVO = (LocalArmazenamentoVO) getRequestMap().get("local");			
			this.setCaminhoRelatorio(getFacadeFactory().getTextoPadraoPatrimonioFacade().realizarImpressaoTextoPadraoPatrimonio(getTextoPadraoPatrimonioVO(), getPatrimonioVO(), null, localArmazenamentoVO.getPatrimonioUnidadeVOs(), localArmazenamentoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}
	
	/**
	 * @return the textoPadraoPatrimonioVO
	 */
	public TextoPadraoPatrimonioVO getTextoPadraoPatrimonioVO() {
		if (textoPadraoPatrimonioVO == null) {
			textoPadraoPatrimonioVO = new TextoPadraoPatrimonioVO();
		}
		return textoPadraoPatrimonioVO;
	}

	/**
	 * @param textoPadraoPatrimonioVO the textoPadraoPatrimonioVO to set
	 */
	public void setTextoPadraoPatrimonioVO(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		this.textoPadraoPatrimonioVO = textoPadraoPatrimonioVO;
	}

	/**
	 * @return the localArmazenamentoVOs
	 */
	public List<LocalArmazenamentoVO> getLocalArmazenamentoVOs() {
		if (localArmazenamentoVOs == null) {
			localArmazenamentoVOs = new ArrayList<LocalArmazenamentoVO>(0);
		}
		return localArmazenamentoVOs;
	}

	/**
	 * @param localArmazenamentoVOs the localArmazenamentoVOs to set
	 */
	public void setLocalArmazenamentoVOs(List<LocalArmazenamentoVO> localArmazenamentoVOs) {
		this.localArmazenamentoVOs = localArmazenamentoVOs;
	}
	
	public void realizarSeparacaoPatrimonioUnidadePorLocalArmazenamento(){
		limparMensagem();
		setLocalArmazenamentoVOs(getFacadeFactory().getPatrimonioFacade().realizarSeparacaoPatrimonioUnidadePorLocalArmazenamento(getPatrimonioVO()));
		montarListaSelectItemTextoPadraoPatrimonio();
	}
	
	public List<SelectItem> getListaEstadoBem() {
	/*	List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("otimo", "Ótimo"));
		itens.add(new SelectItem("bom", "Bom"));
		itens.add(new SelectItem("regular", "Regular"));
		itens.add(new SelectItem("ruim", "Ruim"));
		return itens;*/
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(EstadoBemEnum.class);
	}

}
