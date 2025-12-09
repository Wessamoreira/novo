package controle.financeiro;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.boleto.GeradorDeLinhaDigitavelOuCodigoBarra;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Controller("ControleRemessaContaPagarControle")
@Scope("viewScope")
@Lazy
public class ControleRemessaContaPagarControle extends SuperControleRelatorio implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2709427323713027806L;
	private ControleRemessaContaPagarVO controleRemessaContaPagarVO;
	private ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO;
    private List listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsino;
    private FormaPagamentoVO formaPagamento;
    private List listaSelectItemContaCorrente;
    private List listaSelectItemBanco;
    private List listaSelectItemSituacao;
    private List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs;
    private List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaSemBancoVOs;
    private List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaOutroBancoVOs;
    private List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaErroVOs;
    private List<ContaPagarControleRemessaContaPagarVO> listaEstornos;
    private ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarEstorno; 
    private Integer qtdRegistros;
    private Integer qtdRegistrosSemBanco;
    private Integer qtdRegistrosOutroBanco;
    private Integer qtdRegistrosErro;
    private Integer qtdRegistrosAlteracao;
    private Double vlrTotalRegistrosAlteracao;
    private Double vlrTotalRegistros;
    private Double vlrTotalRegistrosSemBanco;
    private Double vlrTotalRegistrosOutroBanco;
    private String situacaoControleRemessa;
    private String motivoEstorno;
    private Boolean estornoContaUnica;
    private ComunicacaoInternaVO comunicacaoEnviar;
    private String oncompleteModal = "RichFaces.$('panelContaPagarControleRemessaContaPagar').show();";
	private List<SelectItem> listaSelectItemBancoRecebimento;
	private List<SelectItem> comboTipoLancamentoContaPagar;
	private List<SelectItem> comboTipoServicoContaPagar;
	private List<SelectItem> comboFormaPagamento;
	private List<BancoVO> listaBancosConsulta;
	private List<SelectItem> listaSelectItemBancoRemessa;
	private Boolean agruparContasMesmoFornecedor ; 
	private Boolean apresentarCodigoAgrupador ;
	private List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsContasAgrupadas; 
	private Integer qtdRegistrosAgrupado ;
	private Double  vlrTotalRegistrosAgrupado;
	private Boolean agrupouContaPagar;
	private Boolean apresentarFormaPagamento;
	private Boolean apresentarBotaoGerar;
	private Boolean apresentarBotaoDownloadArquivo ;
	private Boolean apresentarBotaoEstornar;
	private Boolean contasAgrupadas;
	private Boolean contasDesagrupadas;
	
	

    public ControleRemessaContaPagarControle()  {
        novo();
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("contaCorrente");
        getControleConsulta().setDataIni(new Date());
        getControleConsulta().setDataFim(Uteis.obterDataFutura(new Date(), 30));
        getControleRemessaContaPagarVO().setSituacaoControleRemessa(SituacaoControleRemessaEnum.TODOS);
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() {
        //removerObjetoMemoria(this);
        try {
            //montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemLayoutsBancos();
            montarListaSelectItemSituacao();
            //montarListaSelectItemFormaPagamento();
            setControleRemessaContaPagarVO(null);
            setQtdRegistros(0);
            setQtdRegistrosAlteracao(0);
            setVlrTotalRegistrosAlteracao(0.0);
            setVlrTotalRegistrosAlteracao(0.0);
            setQtdRegistrosErro(0);
            setVlrTotalRegistros(0.0);
            getControleRemessaContaPagarVO().setResponsavel(getUsuarioLogadoClone());
            setQtdRegistrosSemBanco(0);
            setVlrTotalRegistrosSemBanco(0.0);
            setQtdRegistrosOutroBanco(0);
            setVlrTotalRegistrosOutroBanco(0.0);
            getControleRemessaContaPagarVO().setDataInicio(new Date());
            getControleRemessaContaPagarVO().setDataFim(Uteis.obterDataFutura(new Date(), 30));
            setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO());
            setMarcarTodosTipoOrigemContaPagar(true);
            getFiltroRelatorioFinanceiroVO().realizarSelecaoTodasOrigensContaPagar(getMarcarTodosTipoOrigemContaPagar());
            setListaDadosRemessaErroVOs(null);
            setListaDadosRemessaVOs(null);
            setListaDadosRemessaOutroBancoVOs(null);
            setListaDadosRemessaSemBancoVOs(null);
            setApresentarBotaoGerar(Boolean.FALSE);
            setAgruparContasMesmoFornecedor(Boolean.TRUE);
            setContasAgrupadas(Boolean.FALSE);
            setContasDesagrupadas(Boolean.FALSE);
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaContaPagarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String editar() {
        try {
            setQtdRegistrosErro(0);
            setQtdRegistros(0);
            setVlrTotalRegistros(0.0);
            setQtdRegistrosSemBanco(0);
            setVlrTotalRegistrosSemBanco(0.0);
            setQtdRegistrosOutroBanco(0);
            setVlrTotalRegistrosOutroBanco(0.0);
            setQtdRegistrosAlteracao(0);
            setVlrTotalRegistrosAlteracao(0.0);            
            ControleRemessaContaPagarVO obj = (ControleRemessaContaPagarVO) context().getExternalContext().getRequestMap().get("controleRemessaItens");
            setControleRemessaContaPagarVO(getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getControleRemessaContaPagarVO().setNovoObj(Boolean.FALSE);
            //montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemLayoutsBancos();
            montarListaSelectItemContaCorrente();
            montarListaSelectItemFormaPagamento();
            setListaDadosRemessaVOs(getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleRemessaContaPagarVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogado()));
            processarListaErro();
            contabilizarContas();
            contabilizarContasAlteracao();           
            validarApresentacaoBotoesGerarArquivoDownloadEstorno(getListaDadosRemessaVOs());
            setApresentarBotaoDownloadArquivo(Boolean.TRUE);
            getControleRemessaContaPagarVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getControleRemessaContaPagarVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaContaPagarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void gerarArquivo() {
        try {
        	getListaEstornos().clear();
            for (ContaPagarControleRemessaContaPagarVO obj : getListaDadosRemessaVOs()) {
                if (obj.getApresentarArquivoRemessa()) { 
                	if(obj.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO)) {                		
                		getListaEstornos().add(obj);
                	}
                }
            }
            String caminhoPasta = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp();
            
            if (getAgruparContasMesmoFornecedor()) {
            	getFacadeFactory().getControleRemessaContaPagarFacade().realizarValidarExistenciaContaComFormaPagamentoBoletoCodigoBarrasIgualSemAgrupador(getListaEstornos());
            }
            getFacadeFactory().getControleRemessaContaPagarFacade().executarGeracaoArquivoRemessaContaPagar(getListaEstornos() ,getAgruparContasMesmoFornecedor(), 
            		getControleRemessaContaPagarVO(), caminhoPasta, 
            		getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleRemessaContaPagarVO().getUnidadeEnsinoVO().getCodigo()), 
            		getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getControleRemessaContaPagarVO().getUnidadeEnsinoVO().getCodigo()),
            		getUsuarioLogado(), 
            		getContaPagarControleRemessaContaPagarVO().getContaPagar());
                    setApresentarBotaoGerar(Boolean.FALSE);
                    setApresentarBotaoDownloadArquivo(Boolean.TRUE);
                    setApresentarBotaoEstornar(Boolean.TRUE);
            setMensagemID("msg_arquivoGerado");
        } catch (Exception e) {
        	getControleRemessaContaPagarVO().setNovoObj(true);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void visualizarRemessa() {
        try {
            getListaDadosRemessaVOs().clear();
//            if(!Uteis.isAtributoPreenchido(getControleRemessaContaPagarVO().getUnidadeEnsinoVO())){
//            	throw new Exception("O campo Unidade Ensino deve ser informado.");
//            }
            if(!Uteis.isAtributoPreenchido(getControleRemessaContaPagarVO().getBancoVO())){
            	throw new Exception("O campo Banco deve ser informado.");
            }
            if(!Uteis.isAtributoPreenchido(getControleRemessaContaPagarVO().getContaCorrenteVO())){
            	throw new Exception("O campo Conta Corrente deve ser informado.");
            }
    		setListaDadosRemessaVOs(getFacadeFactory().getContaPagarFacade().consultaRapidaContasArquivoRemessaEntreDatas(getControleRemessaContaPagarVO(), new ConfiguracaoFinanceiroVO(), getUnidadeEnsinoVOs(), getFormaPagamentoVOs(), false, false, getFiltroRelatorioFinanceiroVO(), getUsuarioLogado()));
    		setListaDadosRemessaOutroBancoVOs(getFacadeFactory().getContaPagarFacade().consultaRapidaContasArquivoRemessaEntreDatas(getControleRemessaContaPagarVO(), new ConfiguracaoFinanceiroVO(), getUnidadeEnsinoVOs(), getFormaPagamentoVOs(), true, false, getFiltroRelatorioFinanceiroVO(), getUsuarioLogado()));
    		setListaDadosRemessaSemBancoVOs(getFacadeFactory().getContaPagarFacade().consultaRapidaContasArquivoRemessaEntreDatas(getControleRemessaContaPagarVO(), new ConfiguracaoFinanceiroVO(), getUnidadeEnsinoVOs(), getFormaPagamentoVOs(), false, true, getFiltroRelatorioFinanceiroVO(), getUsuarioLogado()));
    		
            processarListaErro();  
            realizarAgruparContasPagar();
            contabilizarContas();
            
           
            if (getListaDadosRemessaVOs().isEmpty() && getListaDadosRemessaErroVOs().isEmpty()) {
            	setMensagemID("msg_dados_nenhum_registro");
            } else {
            	setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void editarContaPagarControleRemessaContaPagarVO(){
    	try {
    		ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context().getExternalContext().getRequestMap().get("contaEstornarItens");
    		obj.setBancoRemessaPagar(getControleRemessaContaPagarVO().getBancoVO());
    		obj.setContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		obj.getContaPagar().setBanco(getControleRemessaContaPagarVO().getBancoVO());
    		setContaPagarControleRemessaContaPagarVO(obj);
    		montarListaSelectItemFormaPagamento();
    		if (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo().intValue() > 0) {
    			getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		}
    		//limparCamposRemessaBancaria();
    		limparMensagem();
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}    	
    }
    
    public void carregarContaPagarControleRemessaContaPagarVO(){
    	try {
    		ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context().getExternalContext().getRequestMap().get("contaEstornarItens");
    		if (Uteis.isAtributoPreenchido(obj.getBancoRecebimento())) {
    			obj.setBancoRecebimento(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBancoRecebimento().getCodigo(),
    					false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
    		}
    		setContaPagarControleRemessaContaPagarVO(obj);
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    /**
     * Apresenta os dados da {@link ContaPagarVO} caso a {@link SituacaoControleRemessaContaReceberEnum} não esteja
     * como estornado.
     * 
     * @return
     */
    public boolean apresentarContaPagarHabilitado() {
    	if (Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO())) {
    		return getContaPagarControleRemessaContaPagarVO().getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO) ? true : false;
    	} else {
    		return false;
    	}
    }
    
    public void addContaPagarControleRemessaContaPagarVO(){
    	try {
    		
    		if (!Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO())) {
				setOncompleteModal("RichFaces.$('panelContaPagarControleRemessaContaPagar').show();");
				throw new Exception("O campo forma de pagamento deve ser informado.");
			}
			
    		
    		if (!getControleRemessaContaPagarVO().getBancoVO().getNrBanco().equals(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getNrBanco())) {
    			throw new Exception ("Não foi possível adicionar a conta a pagar a remessa! Deve ser selecionado para a conta a pagar o mesmo banco (" + getControleRemessaContaPagarVO().getBancoVO().getNome() + ") da remessa!");
    		}
    		getContaPagarControleRemessaContaPagarVO().getContaPagar().setNumeroAgenciaRecebimento(getContaPagarControleRemessaContaPagarVO().getNumeroAgenciaRecebimento());
    		getContaPagarControleRemessaContaPagarVO().getContaPagar().setContaCorrenteRecebimento(getContaPagarControleRemessaContaPagarVO().getContaCorrenteRecebimento());
    		getContaPagarControleRemessaContaPagarVO().getContaPagar().setDigitoAgenciaRecebimento(getContaPagarControleRemessaContaPagarVO().getDigitoAgenciaRecebimento());
    		getContaPagarControleRemessaContaPagarVO().getContaPagar().setDigitoCorrenteRecebimento(getContaPagarControleRemessaContaPagarVO().getDigitoCorrenteRecebimento());
    		getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoContaEnum(getContaPagarControleRemessaContaPagarVO().getTipoContaEnum());
    	
    		getFacadeFactory().getContaPagarFacade().verificarPossiveisErrosRemessa(getContaPagarControleRemessaContaPagarVO(), getControleRemessaContaPagarVO());
    		if (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO() == null || (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE) 
					|| getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEPOSITO)
					|| getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO))) {
	    		if (getContaPagarControleRemessaContaPagarVO().getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO)) {
	    			throw new Exception ("Não foi possível adicionar a conta a pagar a remessa! " + getContaPagarControleRemessaContaPagarVO().getMotivoErro() + "!");
	    		}
			}
    		getFacadeFactory().getControleRemessaContaPagarFacade().addContaPagarControleRemessaContaPagarVO(getListaDadosRemessaVOs(), getListaDadosRemessaSemBancoVOs(), getListaDadosRemessaOutroBancoVOs(), getContaPagarControleRemessaContaPagarVO());
    		getFacadeFactory().getContaPagarFacade().alterarDadosRemessa(getContaPagarControleRemessaContaPagarVO().getContaPagar(), false, getUsuarioLogado());
    		realizarAgruparContaPagarVindoTelaEdicao();	
    		setContaPagarControleRemessaContaPagarVO(new ContaPagarControleRemessaContaPagarVO());
    		processarListaErro();
    		realizarAgruparContasPagar();
    		contabilizarContas();
    		setOncompleteModal("RichFaces.$('panelContaPagarControleRemessaContaPagar').hide();");
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}    	
    }
		

    public void contabilizarContas() {
    	setQtdRegistros(0);
    	setVlrTotalRegistros(0.0);
        for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {
            if (dadosRemessaVO.getApresentarArquivoRemessa().booleanValue()) {
            	if (!dadosRemessaVO.getContaRemetidaComAlteracao()) {
	            	setQtdRegistros(getQtdRegistros() + 1);
	            	setVlrTotalRegistros(getVlrTotalRegistros() + dadosRemessaVO.getContaPagar().getPrevisaoValorPago());
            	}
            }
        }
        setQtdRegistrosSemBanco(0);
        setVlrTotalRegistrosSemBanco(0.0);
        for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaSemBancoVOs()) {
			setQtdRegistrosSemBanco(getQtdRegistrosSemBanco() + 1);
			setVlrTotalRegistrosSemBanco(getVlrTotalRegistrosSemBanco() + dadosRemessaVO.getValor());
        }
        setQtdRegistrosOutroBanco(0);
        setVlrTotalRegistrosOutroBanco(0.0);
        for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaOutroBancoVOs()) {
			setQtdRegistrosOutroBanco(getQtdRegistrosOutroBanco() + 1);
			setVlrTotalRegistrosOutroBanco(getVlrTotalRegistrosOutroBanco() + dadosRemessaVO.getValor());
        }
       
    }
    public void contabilizarContasAlteracao() {
    	setQtdRegistrosAlteracao(0);
    	setVlrTotalRegistrosAlteracao(0.0);
    	for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {    		
    		if (dadosRemessaVO.getApresentarArquivoRemessa().booleanValue()) {
    			if (dadosRemessaVO.getContaRemetidaComAlteracao()) {
    				setQtdRegistrosAlteracao(getQtdRegistrosAlteracao() + 1);
    				setVlrTotalRegistrosAlteracao(getVlrTotalRegistrosAlteracao());
    			}
    		}
    	}
    }
    
    public void contabilizarContasAgrupadas() {
    	setQtdRegistrosAgrupados(0);
    	setVlrTotalRegistrosAgrupado(0.0);
    	for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOsContasAgrupadas()) {    		
    		if (dadosRemessaVO.getApresentarArquivoRemessa().booleanValue()) {    			
    				setQtdRegistrosAgrupados(getQtdRegistrosAgrupado() + 1);
    				setVlrTotalRegistrosAgrupado(getVlrTotalRegistrosAgrupado() + dadosRemessaVO.getPrevisaoValorPagoDescontosMultas());
    			
    		}
    	}
    }

    public void preencherTodasContas() {
        for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {
        	if (!dadosRemessaVO.getContaRemetidaComAlteracao()) {
        		dadosRemessaVO.setApresentarArquivoRemessa(Boolean.TRUE);
        	}
        }
        realizarAgruparContasPagar();
    	contabilizarContas();
    }

    public void desmarcarTodasContas() {
        for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {
        	if (!dadosRemessaVO.getContaRemetidaComAlteracao()) {
        		dadosRemessaVO.setApresentarArquivoRemessa(Boolean.FALSE);
        	}
        }
        realizarDesagruparContasPagar();
    	contabilizarContas();
    }

    public void preencherTodasContasAlteracao() {
    	for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {
    		if (dadosRemessaVO.getContaRemetidaComAlteracao()) {
    			dadosRemessaVO.setApresentarArquivoRemessa(Boolean.TRUE);
    		}
    	}
    	contabilizarContasAlteracao();
    }
    
    public void desmarcarTodasContasAlteracao() {
    	for (ContaPagarControleRemessaContaPagarVO dadosRemessaVO : getListaDadosRemessaVOs()) {
    		if (dadosRemessaVO.getContaRemetidaComAlteracao()) {
    			dadosRemessaVO.setApresentarArquivoRemessa(Boolean.FALSE);
    		}
    	}
    	contabilizarContasAlteracao();
    }
    
    public void estornarContaPagar() {
        try {
        	if (getContaPagarControleRemessaContaPagarEstorno().getMotivoEstorno().equals("")) {
        		throw new Exception("Informe o motivo do estorno!");
        	}
        	if (!getEstornoContaUnica()) {
		    	for (ContaPagarControleRemessaContaPagarVO obj : getListaDadosRemessaVOs()) {
		    		if (!obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())) {
			    		obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
			    		obj.setUsuarioEstorno(getUsuarioLogadoClone());
			    		obj.setDataEstorno(new Date());
			    		obj.setMotivoEstorno(getContaPagarControleRemessaContaPagarEstorno().getMotivoEstorno());
			    		obj.setContaPagar(new ContaPagarVO());
			            //getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), getUsuarioLogado());
			            getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().realizarEstorno(obj, getUsuarioLogado());
		    		}
		        }
		    	getControleRemessaContaPagarVO().setSituacaoControleRemessa(SituacaoControleRemessaEnum.ESTORNADO);
	            getFacadeFactory().getControleRemessaContaPagarFacade().realizarEstorno(getControleRemessaContaPagarVO(), getUsuarioLogado());
	           	           
            } else {
	    		if (!getContaPagarControleRemessaContaPagarEstorno().getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())) {
	            	getContaPagarControleRemessaContaPagarEstorno().setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
		            //getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(getContaPagarControleRemessaContaPagarEstorno().getContaPagar(), getUsuarioLogado());
		            getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().realizarEstorno(getContaPagarControleRemessaContaPagarEstorno(), getUsuarioLogado());
	    		}
	    		
	    		
            }
        	 setListaDadosRemessaVOs(getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(getControleRemessaContaPagarVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleRemessaContaPagarVO().getUnidadeEnsinoVO().getCodigo()), getUsuarioLogado()));
        	 setApresentarBotaoGerar(Boolean.TRUE);
        	 validarApresentacaoBotoesGerarArquivoDownloadEstorno(getListaDadosRemessaVOs());   
        	 setMensagemID("msg_estornoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void verificarEstorno() {
        try {
        	setMotivoEstorno("");
        	setEstornoContaUnica(Boolean.TRUE);
        	ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context().getExternalContext().getRequestMap().get("contaEstornarItens");
            if (!obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())) {
	            obj.setUsuarioEstorno(getUsuarioLogadoClone());
	            obj.setDataEstorno(new Date());
            }
            setContaPagarControleRemessaContaPagarEstorno(obj);
           
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void estornarTodasContas() {
        try {
        	getContaPagarControleRemessaContaPagarEstorno().setUsuarioEstorno(getUsuarioLogadoClone());
        	getContaPagarControleRemessaContaPagarEstorno().setDataEstorno(new Date());
        	getContaPagarControleRemessaContaPagarEstorno().setMotivoEstorno("");
        	getContaPagarControleRemessaContaPagarEstorno().setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO);
        	setEstornoContaUnica(Boolean.FALSE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public ComunicacaoInternaVO getComunicacaoEnviar() {
		if (comunicacaoEnviar == null) {
			comunicacaoEnviar = new ComunicacaoInternaVO();
		}
		return comunicacaoEnviar;
	}

	public void setComunicacaoEnviar(ComunicacaoInternaVO comunicacaoEnviar) {
		this.comunicacaoEnviar = comunicacaoEnviar;
	}
	
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("contaCorrente")) {
            	objs = getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorContaCorrente(getControleConsulta().getValorConsulta(), getControleRemessaContaPagarVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				 int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
            	objs = getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorNossoNumero(valorInt, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeSacado")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}            	
            	objs = getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorNomeSacado(getControleConsulta().getValorConsulta(), getControleRemessaContaPagarVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            
            if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
                objs = getFacadeFactory().getControleRemessaContaPagarFacade().consultarPorDataGeracao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaContaPagarCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaContaPagarCons.xhtml");
        }
    }

    public void montarListaSelectItemLayoutsBancos() {
        try {
            List listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            Iterator j = listaBancoVOs.iterator();
            while (j.hasNext()) {
                BancoVO item = (BancoVO) j.next();
	            BancoEnum banco = BancoEnum.getEnum("CNAB240", item.getNrBanco());
	            if (banco != null && banco.getPossuiRemessaContaPagar()) {
	            	objs.add(new SelectItem(item.getCodigo(), item.getNome()));
	            }
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemBanco(objs);
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

	public void montarListaSelectItemSituacao() {
		List obj = UtilSelectItem.getListaSelectItemEnum(SituacaoControleRemessaEnum.values(), Obrigatorio.SIM);		
		setListaSelectItemSituacao(obj);
	}

    
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("contaCorrente", "Conta Corrente"));
        itens.add(new SelectItem("nomeSacado", "Nome Sacado"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public boolean isCampoData() {
        if (!getControleConsulta().getCampoConsulta().equals("codigo")) {
            return true;
        }
        return false;
    }   

    public boolean isCampoCPF() {
    	if (getControleConsulta().getCampoConsulta().equals("cpfSacado")) {
    		return true;
    	}
    	return false;
    }   

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void selecionarContaCorrente() {
    	getListaDadosRemessaErroVOs().clear();
    	getListaDadosRemessaVOs().clear();
    }
    
	public void montarListaSelectItemContaCorrente() {
        try {
        	getListaDadosRemessaErroVOs().clear();
        	getListaDadosRemessaVOs().clear();
            if (getControleRemessaContaPagarVO().getBancoVO().getCodigo() != 0) {
            	getControleRemessaContaPagarVO().setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getControleRemessaContaPagarVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            	List listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultaRapidaPorBancoControleRemessaNivelComboBox(getControleRemessaContaPagarVO().getBancoVO().getCodigo(), getControleRemessaContaPagarVO().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                List objs = new ArrayList(0);
                objs.add(new SelectItem(0, ""));
                Iterator j = listaContaCorrenteVOs.iterator();
                while (j.hasNext()) {
                    ContaCorrenteVO item = (ContaCorrenteVO) j.next();
                    BancoEnum banco = BancoEnum.getEnum(item.getAgencia().getBanco().getNrBanco());
                    if (banco != null && !item.getContaCaixa() && banco.getPossuiRemessaContaPagar()) {
                    	if(Uteis.isAtributoPreenchido(item.getNomeApresentacaoSistema())){
                    	   objs.add(new SelectItem(item.getCodigo(), item.getNomeApresentacaoSistema()));
                    	}else{
                    		objs.add(new SelectItem(item.getCodigo(), item.getNumero() + " - " + item.getCarteira()));
                    	}
                    }
                }
                SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
                Collections.sort((List) objs, ordenador);
                setListaSelectItemContaCorrente(objs);
            }
            montarComboTipoServicoContaPagar();
            montarComboTipoLancamentoContaPagar();
            limparMensagem();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

    public Boolean getApresentaContaCorrente() {
        return getControleRemessaContaPagarVO().getBancoVO().getCodigo() != 0;
    }

    public String getMascaraConsulta() {
        return "";
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        novo();
        getControleRemessaContaPagarVO().setSituacaoControleRemessa(SituacaoControleRemessaEnum.TODOS);
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("contaCorrente");
        getControleConsulta().setDataIni(new Date());
        getControleConsulta().setDataFim(Uteis.obterDataFutura(new Date(), 30));
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaContaPagarCons.xhtml");
    }    

    
    
    public String realizarDownloadArquivo(){
    	try {
    		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    		controleRemessaContaPagarVO.getArquivoRemessaContaPagar().setPastaBaseArquivo(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlFisicoAcessoArquivo(controleRemessaContaPagarVO.getArquivoRemessaContaPagar(), PastaBaseArquivoEnum.REMESSA_PG, getConfiguracaoGeralPadraoSistema()).replace(controleRemessaContaPagarVO.getArquivoRemessaContaPagar().getNome(), ""));
    		request.setAttribute("arquivoVO",  controleRemessaContaPagarVO.getArquivoRemessaContaPagar());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
    	} catch (IOException e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    	return "";
    }
    
	
	
	
    public boolean isApresentarBotaoDownload() {
        return (!getControleRemessaContaPagarVO().isNovoObj());
    }
   
    public void setControleRemessaContaPagarVO(ControleRemessaContaPagarVO controleRemessaContaPagarVO) {
        this.controleRemessaContaPagarVO = controleRemessaContaPagarVO;
    }

    public ControleRemessaContaPagarVO getControleRemessaContaPagarVO() {
        if (controleRemessaContaPagarVO == null) {
            controleRemessaContaPagarVO = new ControleRemessaContaPagarVO();
        }
        return controleRemessaContaPagarVO;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public List getListaSelectItemBanco() {
        if (listaSelectItemBanco == null) {
            listaSelectItemBanco = new ArrayList(0);
        }
        return listaSelectItemBanco;
    }

    public void setListaSelectItemBanco(List listaSelectItemBanco) {
        this.listaSelectItemBanco = listaSelectItemBanco;
    }

    public List<ContaPagarControleRemessaContaPagarVO> getListaDadosRemessaVOs() {
        if (listaDadosRemessaVOs == null) {
            listaDadosRemessaVOs = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
        }
        return listaDadosRemessaVOs;
    }   

    public void setListaDadosRemessaVOs(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
        this.listaDadosRemessaVOs = listaDadosRemessaVOs;
    }
    
    public void setListaDadosRemessaVOsContasAgrupadas(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsContasAgrupadas) {
        this.listaDadosRemessaVOsContasAgrupadas = listaDadosRemessaVOsContasAgrupadas;
    }
    
    public List<ContaPagarControleRemessaContaPagarVO> getListaDadosRemessaVOsContasAgrupadas() {
        if (listaDadosRemessaVOsContasAgrupadas == null) {
            listaDadosRemessaVOsContasAgrupadas = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
        }
        return listaDadosRemessaVOsContasAgrupadas;
    }

    public Boolean getApresentarBotaoGerarArquivo() {
        if (((!getListaDadosRemessaOutroBancoVOs().isEmpty() || !getListaDadosRemessaSemBancoVOs().isEmpty() || !getListaDadosRemessaErroVOs().isEmpty() || !getListaDadosRemessaVOs().isEmpty()) && getControleRemessaContaPagarVO().isNovoObj() ) ) {
            return Boolean.TRUE; 
        }
        return Boolean.FALSE;
    }

    public List<ContaPagarControleRemessaContaPagarVO> getListaEstornos() {
        if (listaEstornos == null) {
            listaEstornos = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
        }
        return listaEstornos;
    }

    public void setListaEstornos(List<ContaPagarControleRemessaContaPagarVO> listaEstornos) {
        this.listaEstornos = listaEstornos;
    }

	public Integer getQtdRegistros() {
		if (qtdRegistros == null) {
			qtdRegistros = 0;
		}
		return qtdRegistros;
	}

	public void setQtdRegistros(Integer qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}

	public Double getVlrTotalRegistros() {
		if (vlrTotalRegistros == null) {
			vlrTotalRegistros = 0.0;
		}
		return vlrTotalRegistros;
	}

	public void setVlrTotalRegistros(Double vlrTotalRegistros) {
		this.vlrTotalRegistros = vlrTotalRegistros;
	}

	public Integer getQtdRegistrosSemBanco() {
		if (qtdRegistrosSemBanco == null) {
			qtdRegistrosSemBanco = 0;
		}
		return qtdRegistrosSemBanco;
	}
	
	public void setQtdRegistrosSemBanco(Integer qtdRegistrosSemBanco) {
		this.qtdRegistrosSemBanco = qtdRegistrosSemBanco;
	}
	
	public Double getVlrTotalRegistrosSemBanco() {
		if (vlrTotalRegistrosSemBanco == null) {
			vlrTotalRegistrosSemBanco = 0.0;
		}
		return vlrTotalRegistrosSemBanco;
	}
	
	public void setVlrTotalRegistrosSemBanco(Double vlrTotalRegistrosSemBanco) {
		this.vlrTotalRegistrosSemBanco = vlrTotalRegistrosSemBanco;
	}
	
	public Integer getQtdRegistrosOutroBanco() {
		if (qtdRegistrosOutroBanco == null) {
			qtdRegistrosOutroBanco = 0;
		}
		return qtdRegistrosOutroBanco;
	}
	
	public void setQtdRegistrosOutroBanco(Integer qtdRegistrosOutroBanco) {
		this.qtdRegistrosOutroBanco = qtdRegistrosOutroBanco;
	}
	
	public Double getVlrTotalRegistrosOutroBanco() {
		if (vlrTotalRegistrosOutroBanco == null) {
			vlrTotalRegistrosOutroBanco = 0.0;
		}
		return vlrTotalRegistrosOutroBanco;
	}
	
	public void setVlrTotalRegistrosOutroBanco(Double vlrTotalRegistrosOutroBanco) {
		this.vlrTotalRegistrosOutroBanco = vlrTotalRegistrosOutroBanco;
	}
	
    public List getTipoRemessaCombo() {
    	List itens = new ArrayList(0);
    	itens.add(new SelectItem("RE", "Registro de Conta"));
    	//itens.add(new SelectItem("AU", "Atualização de Conta"));
    	return itens;
    }
	
    public Boolean getApresentaTipoRemessa() {
    	if (getControleRemessaContaPagarVO().getBancoVO().getCodigo().intValue() > 0) {
    		try {
    			getControleRemessaContaPagarVO().setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getControleRemessaContaPagarVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    			return getControleRemessaContaPagarVO().getBancoVO().getNrBanco().equals("033");
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	return false;
    }

	public List getListaSelectItemSituacao() {
		if(listaSelectItemSituacao == null){
			listaSelectItemSituacao = new ArrayList(0);
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getSituacaoControleRemessa() {
		if (situacaoControleRemessa == null) {
			situacaoControleRemessa = "";
		}
		return situacaoControleRemessa;
	}

	public void setSituacaoControleRemessa(String situacaoControleRemessa) {
		this.situacaoControleRemessa = situacaoControleRemessa;
	}

	public String getMotivoEstorno() {
		if (motivoEstorno == null) {
			motivoEstorno = "";
		}
		return motivoEstorno;
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public Boolean getEstornoContaUnica() {
		if (estornoContaUnica == null) {
			estornoContaUnica = Boolean.FALSE;
		}
		return estornoContaUnica;
	}

	public void setEstornoContaUnica(Boolean estornoContaUnica) {
		this.estornoContaUnica = estornoContaUnica;
	}

	public ContaPagarControleRemessaContaPagarVO getContaPagarControleRemessaContaPagarEstorno() {
		if (contaPagarControleRemessaContaPagarEstorno == null) {
			contaPagarControleRemessaContaPagarEstorno = new ContaPagarControleRemessaContaPagarVO();
		}
		return contaPagarControleRemessaContaPagarEstorno;
	}

	public void setContaPagarControleRemessaContaPagarEstorno(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarEstorno) {
		this.contaPagarControleRemessaContaPagarEstorno = contaPagarControleRemessaContaPagarEstorno;
	}

	public List<ContaPagarControleRemessaContaPagarVO> getListaDadosRemessaErroVOs() {
		if (listaDadosRemessaErroVOs == null) {
			listaDadosRemessaErroVOs = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
		}
		return listaDadosRemessaErroVOs;
	}

	public void setListaDadosRemessaErroVOs(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaErroVOs) {
		this.listaDadosRemessaErroVOs = listaDadosRemessaErroVOs;
	}

	public void processarListaErro() {
		getListaDadosRemessaErroVOs().clear();
		setQtdRegistrosErro(null);
    	if (!listaDadosRemessaVOs.isEmpty()) {
    		Iterator i = listaDadosRemessaVOs.iterator();
    		while (i.hasNext()) {
    			ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO)i.next();
    			if (obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO.getValor())) {
    				getListaDadosRemessaErroVOs().add(obj);
    			}
    		}
    		if (!getListaDadosRemessaErroVOs().isEmpty()) {
    			setQtdRegistrosErro(getListaDadosRemessaErroVOs().size());
    			List lista = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
    			Iterator j = listaDadosRemessaVOs.iterator();
        		while (j.hasNext()) {
        			ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO)j.next();
        			if (!obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO.getValor())) {
        				lista.add(obj);
        			}
        		}
        		listaDadosRemessaVOs = lista;
    		}
    	}	        
	}

	public String getLabelRemessaErro() {
		return "Registros Remessa c/ Erro (" + getQtdRegistrosErro() + ")";
	}
	
	public Integer getQtdRegistrosErro() {
		if (qtdRegistrosErro == null) {
			qtdRegistrosErro = 0;
		}
		return qtdRegistrosErro;
	}

	public void setQtdRegistrosErro(Integer qtdRegistrosErro) {
		this.qtdRegistrosErro = qtdRegistrosErro;
	}
	
	public Integer getQtdRegistrosAlteracao() {
		if (qtdRegistrosAlteracao == null) {
			qtdRegistrosAlteracao = 0;
		}
		return qtdRegistrosAlteracao;
	}
	
	public void setQtdRegistrosAlteracao(Integer qtdRegistrosAlteracao) {
		this.qtdRegistrosAlteracao = qtdRegistrosAlteracao;
	}
	
	public Double getVlrTotalRegistrosAlteracao() {
		if (vlrTotalRegistrosAlteracao == null) {
			vlrTotalRegistrosAlteracao = 0.0;
		}
		return vlrTotalRegistrosAlteracao;
	}
	
	public void setVlrTotalRegistrosAlteracao(Double vlrTotalRegistrosAlteracao) {
		this.vlrTotalRegistrosAlteracao = vlrTotalRegistrosAlteracao;
	}	
	

	public Integer getQtdRegistrosAgrupado() {
		if (qtdRegistrosAgrupado == null) {
			qtdRegistrosAgrupado = 0;
		}
		return qtdRegistrosAgrupado;
	}
	
	public void setQtdRegistrosAgrupados(Integer qtdRegistrosAgrupado) {
		this.qtdRegistrosAgrupado = qtdRegistrosAgrupado;
	}
	
	public Double getVlrTotalRegistrosAgrupado() {   
		if (vlrTotalRegistrosAgrupado == null) {
			vlrTotalRegistrosAgrupado = 0.0;
		}
		return vlrTotalRegistrosAgrupado;
	}
	
	public void setVlrTotalRegistrosAgrupado(Double vlrTotalRegistrosAgrupado) {
		this.vlrTotalRegistrosAgrupado = vlrTotalRegistrosAgrupado;
	}

	public ContaPagarControleRemessaContaPagarVO getContaPagarControleRemessaContaPagarVO() {
		if(contaPagarControleRemessaContaPagarVO == null){
			contaPagarControleRemessaContaPagarVO = new ContaPagarControleRemessaContaPagarVO();
		}
		return contaPagarControleRemessaContaPagarVO;
	}

	public void setContaPagarControleRemessaContaPagarVO(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO) {
		this.contaPagarControleRemessaContaPagarVO = contaPagarControleRemessaContaPagarVO;
	}
	
	public void limparCamposRemessaBancaria() {
		try {
			if(Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getCodigo())){
				getContaPagarControleRemessaContaPagarVO().setBancoRemessaPagar(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setBancoRemessaPagar(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar());
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(null);
				getContaPagarControleRemessaContaPagarVO().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarControleRemessaContaPagarVO().setFinalidadeDocEnum(null);
				getContaPagarControleRemessaContaPagarVO().setFinalidadeTedEnum(null);
				setComboTipoLancamentoContaPagar(null);
				setComboTipoServicoContaPagar(null);
				selecionarFormaPagamentoContaPagar();
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}
	
	public void selecionarFormaPagamentoContaPagar() {
		try {
			if (getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getCodigo().intValue() > 0) {
				getContaPagarControleRemessaContaPagarVO().setBancoRemessaPagar(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if ((getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("033") || getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("756")) && (Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo()))) {
				preencherDadosBancoSantanderParaFormaPagamentoEscolhida();				
			} else if ((getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("237")) && (Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo()))) {
				preencherDadosBancoBradescoParaFormaPagamentoEscolhida();	
			} else if (getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar().getNrBanco().equals("104") && Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo())) {
				preencherDadosBancoCaixaEconomicaParaFormaPagamentoEscolhida();
			} else if ((getContaPagarControleRemessaContaPagarVO().getContaPagar().getBancoRemessaPagar().getNrBanco().equals("341")) && (Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo()))) {
				preencherDadosBancoItauParaFormaPagamentoEscolhida();	
			} else {
				if (Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo())) {
					getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else {
					getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(null);
				}
				preencherCamposTipoLancamento();
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(null);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(null);
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(null);
				getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(null);
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void preencherDadosBancoBradescoParaFormaPagamentoEscolhida() throws Exception {
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		switch (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
		case BOLETO_BANCARIO:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_FATURA);
			getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_FATURA);
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
			break;
		case DEBITO_EM_CONTA_CORRENTE:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
			getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
			break;
		case DEPOSITO:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
			getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
			break;
		default:
			break;
		}
		preencherCamposTipoLancamento();
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);	
		getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
	}

	private void preencherDadosBancoSantanderParaFormaPagamentoEscolhida() throws Exception {
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			switch (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
			case BOLETO_BANCARIO:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
				break;
			case DEBITO_EM_CONTA_CORRENTE:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
				break;
			case DEPOSITO:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
				break;
			default:
				break;
			}
			preencherCamposTipoLancamento();
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);				
			getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
	}	
	
	public void preencherCamposTipoLancamento() { 
		try {
			if(Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar())){
				
				if(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isTransferencia()){
					if (getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isTransferenciaTed()) {
						getContaPagarControleRemessaContaPagarVO().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.TED);
						getContaPagarControleRemessaContaPagarVO().getContaPagar().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.TED);
						getContaPagarControleRemessaContaPagarVO().setFinalidadeTedEnum(FinalidadeTedEnum.OUTROS);
						getContaPagarControleRemessaContaPagarVO().getContaPagar().setFinalidadeTedEnum(FinalidadeTedEnum.OUTROS);
					} else if (getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isTransferenciaDoc()) {
						getContaPagarControleRemessaContaPagarVO().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.DOC);
						getContaPagarControleRemessaContaPagarVO().getContaPagar().setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.DOC);
						getContaPagarControleRemessaContaPagarVO().setFinalidadeDocEnum(FinalidadeDocEnum.OUTROS);
						getContaPagarControleRemessaContaPagarVO().getContaPagar().setFinalidadeDocEnum(FinalidadeDocEnum.OUTROS);
					}
					
				}else if (getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra() ||
						  getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra() ||
						  getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isGpsSemCodigoBarra()){
					
					      getContaPagarControleRemessaContaPagarVO().setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.CPF);
					      getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.CPF);
					
				}else if(
						(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isTransferencia() || 
						 getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isCreditoContaCorrente())
						){
					if (getContaPagarControleRemessaContaPagarVO().getContaPagar().isTipoSacadoFornecedor() && Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor()) && getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getPermiteenviarremessa()) {
						getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarControleRemessaContaPagarVO().getContaPagar(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getNumeroBancoRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getNumeroAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getDigitoAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getContaCorrenteRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getDigitoCorrenteRecebimento(), 
								getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getChaveEnderecamentoPix() ,getContaPagarControleRemessaContaPagarVO().getContaPagar().getFornecedor().getTipoIdentificacaoChavePixEnum() , getUsuarioLogado());
					} else if (getContaPagarControleRemessaContaPagarVO().getContaPagar().isTipoSacadoParceiro() && Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro()) && getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getPermiteenviarremessa()) {
						getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarControleRemessaContaPagarVO().getContaPagar(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getNumeroBancoRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getNumeroAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getDigitoAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getContaCorrenteRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getDigitoCorrenteRecebimento(),
								getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getChaveEnderecamentoPix() ,getContaPagarControleRemessaContaPagarVO().getContaPagar().getParceiro().getTipoIdentificacaoChavePixEnum() ,getUsuarioLogado());
					} else if (getContaPagarControleRemessaContaPagarVO().getContaPagar().isTipoSacadoFuncionario() && Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario())) {
						getFacadeFactory().getContaPagarFacade().preencherDadosRemessaContaPagar(getContaPagarControleRemessaContaPagarVO().getContaPagar(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getNumeroBancoRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getNumeroAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getDigitoAgenciaRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getContaCorrenteRecebimento(), getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getDigitoCorrenteRecebimento(),
								getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getChaveEnderecamentoPix() ,getContaPagarControleRemessaContaPagarVO().getContaPagar().getFuncionario().getTipoIdentificacaoChavePixEnum() ,getUsuarioLogado());
					}
					getContaPagarControleRemessaContaPagarVO().setBancoRecebimento(getContaPagarControleRemessaContaPagarVO().getContaPagar().getBancoRecebimento());
			    	getContaPagarControleRemessaContaPagarVO().setNumeroAgenciaRecebimento(getContaPagarControleRemessaContaPagarVO().getContaPagar().getNumeroAgenciaRecebimento());
			    	getContaPagarControleRemessaContaPagarVO().setContaCorrenteRecebimento(getContaPagarControleRemessaContaPagarVO().getContaPagar().getContaCorrenteRecebimento());
			    	getContaPagarControleRemessaContaPagarVO().setDigitoAgenciaRecebimento(getContaPagarControleRemessaContaPagarVO().getContaPagar().getDigitoAgenciaRecebimento());
			    	getContaPagarControleRemessaContaPagarVO().setDigitoCorrenteRecebimento(getContaPagarControleRemessaContaPagarVO().getContaPagar().getDigitoCorrenteRecebimento());
				}
			}else{
				getContaPagarControleRemessaContaPagarVO().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setModalidadeTransferenciaBancariaEnum(null);
				getContaPagarControleRemessaContaPagarVO().setFinalidadeDocEnum(null);
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setFinalidadeDocEnum(null);
				getContaPagarControleRemessaContaPagarVO().setFinalidadeTedEnum(null);
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setFinalidadeTedEnum(null);
				getContaPagarControleRemessaContaPagarVO().setFinalidadeTedEnum(null);
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel1("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel1("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel2("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel2("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel3("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel3("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel4("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel4("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel5("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel5("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel6("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel6("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel7("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel7("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setLinhaDigitavel8("");
//				getContaPagarControleRemessaContaPagarVO().setLinhaDigitavel8("");
//				getContaPagarControleRemessaContaPagarVO().getContaPagar().setCodigoBarra("");
//				getContaPagarControleRemessaContaPagarVO().setCodigoBarra("");
				
				
//				getContaPagarControleRemessaContaPagarVO().setBancoRecebimento(null);
//		    	getContaPagarControleRemessaContaPagarVO().setNumeroAgenciaRecebimento("");
//		    	getContaPagarControleRemessaContaPagarVO().setContaCorrenteRecebimento("");
//		    	getContaPagarControleRemessaContaPagarVO().setDigitoAgenciaRecebimento("");
//		    	getContaPagarControleRemessaContaPagarVO().setDigitoCorrenteRecebimento("");
		    	
		    	getContaPagarControleRemessaContaPagarVO().setTipoIdentificacaoContribuinte(null);
		    	getContaPagarControleRemessaContaPagarVO().setCodigoReceitaTributo("");
		    	getContaPagarControleRemessaContaPagarVO().setIdentificacaoContribuinte("");
		    	getContaPagarControleRemessaContaPagarVO().setNumeroReferencia("");
		    	getContaPagarControleRemessaContaPagarVO().setValorReceitaBrutaAcumulada(0.0);
		    	getContaPagarControleRemessaContaPagarVO().setPercentualReceitaBrutaAcumulada(0.0);
			}
		} catch (Exception ex) {
			//setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		
	}
	
	public void converterCodigoBarraParaLinhaDigitavel() {
		try {
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setBancoRemessaPagar(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar());
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar());
//			GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
//			gd.geraLinhaDigitavelApartirCodigoBarra(getContaPagarControleRemessaContaPagarVO().getContaPagar());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void converterLinhaDigitavelParaCodigoBarra() {
		try {
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setBancoRemessaPagar(getContaPagarControleRemessaContaPagarVO().getBancoRemessaPagar());
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar());
			GeradorDeLinhaDigitavelOuCodigoBarra gd = new GeradorDeLinhaDigitavelOuCodigoBarra();
			if((getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
					&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel1())
					&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel2())
					&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel3())
					&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel4()))
					|| 
					(!getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel1())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel2())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel3())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel4())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel5())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel6())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel7())
							&& Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getContaPagar().getLinhaDigitavel8()))){
				gd.geraCodigoBarraApartirLinhaDigitavel(getContaPagarControleRemessaContaPagarVO().getContaPagar());
			}
			limparMensagem();
		} catch (Exception ex) {
			String mensagem = ex.getMessage();
			if (ex.getMessage().contains("index ")) {
				mensagem = "Linha digitável inválida!";
			}
			setMensagemDetalhada("msg_erro", mensagem);
		}
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public List<SelectItem> getListaSelectItemBancoRecebimento() {
		if (listaSelectItemBancoRecebimento == null) {
			listaSelectItemBancoRecebimento = new ArrayList<>();
			try {
				listaSelectItemBancoRecebimento.add(new SelectItem(0, ""));
				getListaBancosConsulta().stream()
					.map(b -> new SelectItem(b.getCodigo(), b.getNome()))
					.forEach(listaSelectItemBancoRecebimento::add);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemBancoRecebimento;
	}
	
	public List<SelectItem> getListaSelectItemBancoRemessa() {
		if (listaSelectItemBancoRemessa == null) {
			try {
				listaSelectItemBancoRemessa = new ArrayList<>();
				listaSelectItemBancoRemessa.add(new SelectItem(0, ""));
				getListaBancosConsulta().stream().filter(b -> {
					BancoEnum banco = BancoEnum.getEnum("CNAB240", b.getNrBanco());
					return Uteis.isAtributoPreenchido(banco) && banco.getPossuiRemessaContaPagar();
				}).map(b -> new SelectItem(b.getCodigo(), b.getNome()))
				.forEach(listaSelectItemBancoRemessa::add);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemBancoRemessa;
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<BancoVO> consultarBancoPorNome(String prm) throws Exception {
		return getFacadeFactory().getBancoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}
	
	public List<BancoVO> getListaBancosConsulta() {
		if (listaBancosConsulta == null) {
			listaBancosConsulta = new ArrayList<>();
			try {
				listaBancosConsulta = consultarBancoPorNome("");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaBancosConsulta;
	}

	private void montarComboTipoServicoContaPagar() {
		getComboTipoServicoContaPagar().clear();
		Bancos layout = Bancos.getEnum(getControleRemessaContaPagarVO().getBancoVO().getNrBanco());
		if (layout != null) {
			for (TipoServicoContaPagarEnum tipoServicoContaPagarEnum : TipoServicoContaPagarEnum.values()) {
				if (tipoServicoContaPagarEnum.isTipoServicoPorNrBanco(layout.getNumeroBanco())
						|| (tipoServicoContaPagarEnum.isTipoServicoPorNrBanco("033") && layout.getNumeroBanco().equals("756"))) {
					if (getComboTipoServicoContaPagar().isEmpty() && !Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getTipoServicoContaPagar())) {
						getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(tipoServicoContaPagarEnum);
					}
					getComboTipoServicoContaPagar().add(new SelectItem(tipoServicoContaPagarEnum, tipoServicoContaPagarEnum.getDescricao()));
				}
			}
			getComboTipoServicoContaPagar().sort(Comparator.comparing(SelectItem::getLabel));
		}
	}
	
	public List<SelectItem> getComboTipoServicoContaPagar() {
		if (comboTipoServicoContaPagar == null) {
			comboTipoServicoContaPagar = new ArrayList<>();
		}
		return comboTipoServicoContaPagar;
	}

	public void setComboTipoServicoContaPagar(List<SelectItem> comboTipoServicoContaPagar) {
		this.comboTipoServicoContaPagar = comboTipoServicoContaPagar;
	}

	private void montarComboTipoLancamentoContaPagar() {
		getComboTipoLancamentoContaPagar().clear();
		Bancos layout = Bancos.getEnum(getControleRemessaContaPagarVO().getBancoVO().getNrBanco());
		if (layout != null) {
			for (TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum : TipoLancamentoContaPagarEnum.values()) {
				if (tipoLancamentoContaPagarEnum.isTipoLancamentoPorNrBanco(layout.getNumeroBanco())) {
					if(getComboTipoLancamentoContaPagar().isEmpty() && !Uteis.isAtributoPreenchido(getContaPagarControleRemessaContaPagarVO().getTipoLancamentoContaPagar())){
						getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(tipoLancamentoContaPagarEnum);	
					}
					getComboTipoLancamentoContaPagar().add(new SelectItem(tipoLancamentoContaPagarEnum, tipoLancamentoContaPagarEnum.getDescricao()));
				}
			}
		}
	}
	
	public List<SelectItem> getComboTipoLancamentoContaPagar() {
		if (comboTipoLancamentoContaPagar == null) {
			comboTipoLancamentoContaPagar = new ArrayList<>();
		}
		return comboTipoLancamentoContaPagar;
	}

	public void setComboTipoLancamentoContaPagar(List<SelectItem> comboTipoLancamentoContaPagar) {
		this.comboTipoLancamentoContaPagar = comboTipoLancamentoContaPagar;
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemFormaPagamento() throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				if(obj.isBoletoBancario() || obj.isDebitoEmConta() || obj.isDeposito()){
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));	
				}
			}
			setComboFormaPagamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<SelectItem> getComboFormaPagamento() {
		if (comboFormaPagamento == null) {
			comboFormaPagamento = new ArrayList<SelectItem>();
		}
		return comboFormaPagamento;
	}

	public void setComboFormaPagamento(List<SelectItem> comboFormaPagamento) {
		this.comboFormaPagamento = comboFormaPagamento;
	}
	
		
	public Boolean getIsApresentarBotaoSelecionarUnidadeEnsino() {
		return Boolean.TRUE;
	}	

	public Boolean getIsApresentarBotaoSelecionarFormaPagamento() {
		return Boolean.TRUE;
	}	
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ControleRemessaContaPagar");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void consultarFormaPagamento() {
		try {
			consultarFormaPagamentoFiltroRelatorio("ControleRemessaContaPagar");
			//verificarTodasFormaPagamentoSelecionados();
			verificarTodasFormaPagamentosSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsino().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsino().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsino().setNome(unidade.toString());
			}
		}		
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
	
	public void verificarTodasFormaPagamentosSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getFormaPagamentoVOs().size() > 1) {
			for (FormaPagamentoVO obj : getFormaPagamentoVOs()) {
				if (obj.getFiltrarFormaPagamento()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getFormaPagamento().setNome(unidade.toString());
		} else {
			if (!getFormaPagamentoVOs().isEmpty()) {
				if (getFormaPagamentoVOs().get(0).getFiltrarFormaPagamento()) {
					getFormaPagamento().setNome(getFormaPagamentoVOs().get(0).getNome());
				}
			} else {
				getFormaPagamento().setNome(unidade.toString());
			}
		}		
	}
	
	public void marcarTodasFormaPagamentosAction() {
		for (FormaPagamentoVO unidade : getFormaPagamentoVOs()) {
			if (getMarcarTodasFormaPagamento()) {
				unidade.setFiltrarFormaPagamento(Boolean.TRUE);
			} else {
				unidade.setFiltrarFormaPagamento(Boolean.FALSE);
			}
		}
		verificarTodasFormaPagamentosSelecionadas();
	}

	
	
	
	public void realizarDesagruparContasPagar() {
		setContasAgrupadas(Boolean.FALSE);
		setContasDesagrupadas(Boolean.TRUE);
		getFacadeFactory().getControleRemessaContaPagarFacade()
				.realizarDesagruparContasPagar(getListaDadosRemessaVOs());
		setApresentarCodigoAgrupador(Boolean.FALSE);

	}

	public void realizarAgruparContasPagar() {
		if (getAgruparContasMesmoFornecedor()) {
			setContasAgrupadas(Boolean.TRUE);
			setContasDesagrupadas(Boolean.FALSE);
			getFacadeFactory().getControleRemessaContaPagarFacade()
					.realizarAgrupamentoContasPagar(getListaDadosRemessaVOs());
			montarDadosObservacaoAgrupadorContaPagar(getListaDadosRemessaVOs());
			if (!getListaDadosRemessaVOs().isEmpty()) {
				setApresentarCodigoAgrupador(Boolean.TRUE);
				setAgrupouContaPagar(Boolean.TRUE);
				setApresentarBotaoGerar(Boolean.TRUE);
			}
			validarApresentacaoCamposAgrupamentoContas();
			contabilizarContas();
		}else {
			setApresentarBotaoGerar(Boolean.TRUE);
		}
		
	}

	private void montarDadosObservacaoAgrupadorContaPagar(
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
		try {
			Map<String, List<ContaPagarControleRemessaContaPagarVO>> map = getFacadeFactory()
					.getControleRemessaContaPagarFacade().realizarSepararListaContaPagarAgrupadas(listaDadosRemessaVOs);
			for (String chaves : map.keySet()) {
				Double valorTotal = 0.0;
				int contadorTotal = 0;
				List<ContaPagarControleRemessaContaPagarVO> listaRemessaVO = map.get(chaves);
				for (ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO : listaRemessaVO) {
					valorTotal = valorTotal + contaPagarControleRemessaContaPagarVO.getPrevisaoValorPagoDescontosMultas();
					contadorTotal++;
				}
				for (ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO : listaRemessaVO) {
					contaPagarControleRemessaContaPagarVO.setVlrTotalRegistrosAgrupado(Uteis.arrendondarForcando2CasasDecimais(valorTotal));
					contaPagarControleRemessaContaPagarVO.preencherVlrTotalRegistrosAgrupadoApresentar();
					contaPagarControleRemessaContaPagarVO.setQtdRegistrosAgrupados(contadorTotal);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarRemoverAgrupamentoContasPagar() {
		try {
			if (getAgruparContasMesmoFornecedor()) {
				ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context()
						.getExternalContext().getRequestMap().get("contaEstornarItens");
				String codigoAgrupamento = obj.getCodigoAgrupamentoContasPagar();
				getFacadeFactory().getControleRemessaContaPagarFacade().realizarRemoverAgrupamentoContasPagar(obj,
						getListaDadosRemessaVOs());
				Boolean apresentar = getListaDadosRemessaVOs().stream()
						.anyMatch(p -> Uteis.isAtributoPreenchido(p.getCodigoAgrupamentoContasPagar()));
				setApresentarBotaoGerar(apresentar);
				setContasAgrupadas(apresentar);
				setContasDesagrupadas(Boolean.TRUE);
				realizarVizualizarContasMesmoAgrupador(codigoAgrupamento);
				montarDadosObservacaoAgrupadorContaPagar(getListaDadosRemessaVOs());

			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarAdicionarAgrupamentoContasPagar() {
		try {
			ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context()
					.getExternalContext().getRequestMap().get("contaEstornarItens");
			if (getAgruparContasMesmoFornecedor()) {				
				if (obj.getApresentarArquivoRemessa()) {
					getFacadeFactory().getControleRemessaContaPagarFacade().realizarAdicionarAgrupamentoContasPagar(obj,
							getListaDadosRemessaVOs());
					montarDadosObservacaoAgrupadorContaPagar(getListaDadosRemessaVOs());
					setApresentarBotaoGerar(Boolean.TRUE);
					setApresentarCodigoAgrupador(Boolean.TRUE);
					Boolean apresentar = getListaDadosRemessaVOs().stream()
							.allMatch(p -> Uteis.isAtributoPreenchido(p.getCodigoAgrupamentoContasPagar()));
					if(apresentar) {
						setContasAgrupadas(Boolean.TRUE);
						setContasDesagrupadas(Boolean.FALSE);
					}
					
				} else {
					throw new Exception("E necessario  marcar  opção  apresentar Arquivo remessa.");
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarVizualizarContasMesmoAgrupador() {
		realizarVizualizarContasMesmoAgrupador("");

	}

	public void realizarVizualizarContasMesmoAgrupador(String codigoAgrupamento) {
		setListaDadosRemessaVOsContasAgrupadas(new ArrayList<ContaPagarControleRemessaContaPagarVO>(0));
		if (!Uteis.isAtributoPreenchido(codigoAgrupamento)) {
			ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context()
					.getExternalContext().getRequestMap().get("contaEstornarItens");
			codigoAgrupamento = obj.getCodigoAgrupamentoContasPagar();
		}
		setListaDadosRemessaVOsContasAgrupadas(getFacadeFactory().getControleRemessaContaPagarFacade()
				.realizarVizualizarContasMesmoAgrupador(codigoAgrupamento, getListaDadosRemessaVOs()));
		contabilizarContasAgrupadas();
	}

	public void realizarVerificarAgruparContasMesmoFornecedor() {
		if (!getAgruparContasMesmoFornecedor()) {
			realizarDesagruparContasPagar();
		} else {
			setApresentarCodigoAgrupador(Boolean.TRUE);
			
		}
	}

	public void realizarValidarApresentarArquivoRemessaEAgruparContaPagar() {		
		if(getAgruparContasMesmoFornecedor()) {			
			 ContaPagarControleRemessaContaPagarVO obj = (ContaPagarControleRemessaContaPagarVO) context()
						.getExternalContext().getRequestMap().get("contaEstornarItens");
			 if(obj.getApresentarArquivoRemessa()) {
				 realizarAdicionarAgrupamentoContasPagar();				
			 }else {
				 realizarRemoverAgrupamentoContasPagar();
			 }			
		}		
		contabilizarContas();
	}
	
	
	public void realizarAgruparContaPagarVindoTelaEdicao() {
		getFacadeFactory().getControleRemessaContaPagarFacade().realizarAdicionarAgrupamentoContasPagar(
				getContaPagarControleRemessaContaPagarVO(), getListaDadosRemessaVOs());
	}

	public void validarApresentacaoCamposAgrupamentoContas() {		
		Boolean apresentarFormaPagamento = getListaDadosRemessaVOs().stream().anyMatch(p -> Uteis.isAtributoPreenchido(p.getContaPagar()));
		Boolean apresentarCodigoAgrupador = getListaDadosRemessaVOs().stream().anyMatch(p -> Uteis.isAtributoPreenchido(p.getCodigoAgrupamentoContasPagar()));
		setApresentarFormaPagamento(apresentarFormaPagamento);
		setApresentarCodigoAgrupador(apresentarCodigoAgrupador);
	}
	
	public void validarApresentacaoBotoesGerarArquivoDownloadEstorno(
			List<ContaPagarControleRemessaContaPagarVO> listaRemessa) {

		Boolean todasContasEstornadas = listaRemessa.stream().allMatch(p -> p.getSituacaoControleRemessaContaReceber()
				.equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO));
		if (todasContasEstornadas) {
			if (getControleRemessaContaPagarVO().getSituacaoControleRemessa()
					.equals(SituacaoControleRemessaEnum.ESTORNADO)) {
				setApresentarBotaoDownloadArquivo(Boolean.FALSE);
				setApresentarBotaoEstornar(Boolean.FALSE);
				setApresentarBotaoGerar(Boolean.FALSE);
			} else {
				setApresentarBotaoEstornar(Boolean.TRUE);
				setApresentarBotaoGerar(Boolean.FALSE);
			}
		} else {
			setApresentarBotaoEstornar(Boolean.TRUE);
			setApresentarBotaoDownloadArquivo(Boolean.FALSE);

		}
		validarApresentacaoCamposAgrupamentoContas();
		montarDadosObservacaoAgrupadorContaPagar(listaRemessa);
	}


	public Boolean getApresentarFormaPagamento() {
		if (apresentarFormaPagamento == null) {
			apresentarFormaPagamento = Boolean.TRUE;
		}
		return apresentarFormaPagamento;
	}

	public void setApresentarFormaPagamento(Boolean apresentarFormaPagamento) {
		this.apresentarFormaPagamento = apresentarFormaPagamento;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<ContaPagarControleRemessaContaPagarVO> getListaDadosRemessaSemBancoVOs() {
		if (listaDadosRemessaSemBancoVOs == null) {
			listaDadosRemessaSemBancoVOs = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
		}
		return listaDadosRemessaSemBancoVOs;
	}

	public void setListaDadosRemessaSemBancoVOs(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaSemBancoVOs) {
		this.listaDadosRemessaSemBancoVOs = listaDadosRemessaSemBancoVOs;
	}

	public List<ContaPagarControleRemessaContaPagarVO> getListaDadosRemessaOutroBancoVOs() {
		if (listaDadosRemessaOutroBancoVOs == null) {
			listaDadosRemessaOutroBancoVOs = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
		}
		return listaDadosRemessaOutroBancoVOs;
	}

	public void setListaDadosRemessaOutroBancoVOs(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaOutroBancoVOs) {
		this.listaDadosRemessaOutroBancoVOs = listaDadosRemessaOutroBancoVOs;
	}

	public Boolean getAgruparContasMesmoFornecedor() {
		if (agruparContasMesmoFornecedor == null) {
			agruparContasMesmoFornecedor = Boolean.TRUE;
		}
		return agruparContasMesmoFornecedor;
	}

	public void setAgruparContasMesmoFornecedor(Boolean agruparContasMesmoFornecedor) {
		this.agruparContasMesmoFornecedor = agruparContasMesmoFornecedor;
	}

	public Boolean getApresentarCodigoAgrupador() {
		if (apresentarCodigoAgrupador == null) {
			apresentarCodigoAgrupador = Boolean.FALSE;
		}
		return apresentarCodigoAgrupador;
	}

	public void setApresentarCodigoAgrupador(Boolean apresentarCodigoAgrupador) {
		this.apresentarCodigoAgrupador = apresentarCodigoAgrupador;
	}

	public Boolean getAgrupouContaPagar() {
		if (agrupouContaPagar == null) {
			agrupouContaPagar = Boolean.FALSE;
		}
		return agrupouContaPagar;
	}

	public void setAgrupouContaPagar(Boolean agrupouContaPagar) {
		this.agrupouContaPagar = agrupouContaPagar;
	}
	
	public String getOncompleteModal() {
		if (oncompleteModal == null) {
			oncompleteModal = "";
		}
		return oncompleteModal;
	}

	public void setOncompleteModal(String oncompleteModal) {
		this.oncompleteModal = oncompleteModal;
	}
	
	private void preencherDadosBancoCaixaEconomicaParaFormaPagamentoEscolhida() throws Exception {
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		switch (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
		case BOLETO_BANCARIO:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
			break;
		case DEBITO_EM_CONTA_CORRENTE:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
			break;
		case DEPOSITO:
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
			getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE);
			break;
		default:
			break;
		}
		preencherCamposTipoLancamento();
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);				
		getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
	}
	public Boolean getApresentarBotaoGerar() {
		if(apresentarBotaoGerar== null ) {
			apresentarBotaoGerar = Boolean.FALSE;
		}
		return apresentarBotaoGerar;
	}

	public void setApresentarBotaoGerar(Boolean apresentarBotaoGerar) {
		this.apresentarBotaoGerar = apresentarBotaoGerar;
	}
	
	public Boolean getApresentarBotaoDownloadArquivo() {
		if(apresentarBotaoDownloadArquivo == null ) {
			apresentarBotaoDownloadArquivo = Boolean.FALSE;
		}
		return apresentarBotaoDownloadArquivo;
	}

	public void setApresentarBotaoDownloadArquivo(Boolean apresentarBotaoDownloadArquivo) {
		this.apresentarBotaoDownloadArquivo = apresentarBotaoDownloadArquivo;
	}

	public Boolean getApresentarBotaoEstornar() {
		if(apresentarBotaoEstornar == null ) {
			apresentarBotaoEstornar = Boolean.FALSE;
		}
		return apresentarBotaoEstornar;
	}

	public void setApresentarBotaoEstornar(Boolean apresentarBotaoEstornar) {
		this.apresentarBotaoEstornar = apresentarBotaoEstornar;
	}

	public Boolean getContasAgrupadas() {
        if(contasAgrupadas == null) {
        	contasAgrupadas = Boolean.FALSE;
        }		
		return contasAgrupadas;
	}

	public void setContasAgrupadas(Boolean contasAgrupadas) {
		this.contasAgrupadas = contasAgrupadas;
	}

	public Boolean getContasDesagrupadas() {
        if(contasDesagrupadas == null) {
        	contasDesagrupadas = Boolean.FALSE;
        }
		return contasDesagrupadas;
	}

	public void setContasDesagrupadas(Boolean contasDesagrupadas) {
		this.contasDesagrupadas = contasDesagrupadas;
	}	
	
	public String realizarDownloadArquivoPix(){
    	try {
    		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    		controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().setPastaBaseArquivo(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlFisicoAcessoArquivo(controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar(), PastaBaseArquivoEnum.REMESSA_PG, getConfiguracaoGeralPadraoSistema()).replace(controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().getNome(), ""));
    		request.setAttribute("arquivoVO",  controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();			
    	} catch (IOException e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    	return "";
    }
	
	private void preencherDadosBancoItauParaFormaPagamentoEscolhida() throws Exception {
		getContaPagarControleRemessaContaPagarVO().getContaPagar().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			switch (getContaPagarControleRemessaContaPagarVO().getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum()) {
			case BOLETO_BANCARIO:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO);
				break;
			case DEBITO_EM_CONTA_CORRENTE:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE);
				break;
			case DEPOSITO:
				getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
				getContaPagarControleRemessaContaPagarVO().setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
				break;
			default:
				break;
			}
			preencherCamposTipoLancamento();
			getContaPagarControleRemessaContaPagarVO().getContaPagar().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);				
			getContaPagarControleRemessaContaPagarVO().setTipoServicoContaPagar(TipoServicoContaPagarEnum.PAGAMENTO_DIVERSOS);
	}
	
	
}
