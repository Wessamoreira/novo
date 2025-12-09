package controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OpcaoAlunoCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.enumerador.LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.controle.financeiro.ComprovanteRecebimentoRelControle;
import relatorio.controle.financeiro.TermoReconhecimentoDividaRelControle;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

@Controller("RenegociacaoContaReceberAlunoControle")
@Scope("viewScope")
@Lazy
public class RenegociacaoContaReceberAlunoControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1654227044371543146L;
	private List<ContaReceberVO> contaReceberVOs;
    private NegociacaoContaReceberVO negociacaoContaReceberVO;
    private Boolean apresentarDadosRenegociacao;
    private Boolean apresentarDadosOpcaoRenegociacao;
    private Boolean apresentarDadosGravados;
    private List<SelectItem> listaSelectItemOpcaoRenegociacao;
    private List<SelectItem> listaSelectItemItemCondicaoRenegociacao;
    protected List<SelectItem> listaSelectItemUnidadeEnsino;
    protected ContaReceberVO contaReceberVisaoAluno;
    private Boolean alunoConcordouComTermosRenegociacaoOnLine;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
    
    private TipoCartaoOperadoraCartaoEnum tipoCartao;

    @PostConstruct
    public void carregarDadosIniciais() {
        try {
            setAlunoConcordouComTermosRenegociacaoOnLine(false);
            setApresentarDadosOpcaoRenegociacao(false);
            setApresentarDadosGravados(false);
            VisaoAlunoControle vs = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");            
            if (vs != null && vs.getMatricula() != null) {
            	setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(vs.getMatricula().getUnidadeEnsino().getCodigo()));
                getFacadeFactory().getMatriculaFacade().carregarDados(vs.getMatricula(), NivelMontarDados.TODOS, getUsuarioLogado());
                realizarInicializacaoDadosRenegociacaoContaReceber(vs);
                if(Uteis.removeHTML(getConfiguracaoFinanceiroVO().getRecomendacaoRenegociacaoVisaoAluno()).replaceAll("&nbsp;", "").trim().isEmpty()) {
                	setAlunoConcordouComTermosRenegociacaoOnLine(true);
                }
                if(Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroVO().getIndiceReajustePadraoContasPorAtrasoVO())) {
                	getConfiguracaoFinanceiroVO().setIndiceReajustePadraoContasPorAtrasoVO(getFacadeFactory().getIndiceReajusteFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroVO().getIndiceReajustePadraoContasPorAtrasoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                }
            } else{
                setMensagem(UteisJSF.internacionalizar("msg_nao_existe_matricula_selecionada").replace("NOME_ALUNO", getUsuario().getNome().toUpperCase()));                
                setApresentarDadosRenegociacao(false);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            setApresentarDadosRenegociacao(false);
        }
    }
    
    public void realizarInicializacaoDadosRenegociacaoContaReceber(VisaoAlunoControle vs) throws Exception {
    	List<ContaReceberVO> listaTemp = new ArrayList<>();
    	getListaSelectItemUnidadeEnsino().clear();
    	getContaReceberVOs().clear();
		getNegociacaoContaReceberVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		 if(getIsApresentarVisaoPais()){
             getFacadeFactory().getNegociacaoContaReceberFacade().realizarInicializacaoDadosRenegociacaoContaReceber(getNegociacaoContaReceberVO(), vs.getMatricula(), TipoPessoa.RESPONSAVEL_FINANCEIRO, getUsuarioLogado());
         }else{
         	getFacadeFactory().getNegociacaoContaReceberFacade().realizarInicializacaoDadosRenegociacaoContaReceber(getNegociacaoContaReceberVO(), vs.getMatricula(), TipoPessoa.ALUNO, getUsuarioLogado());
         }
		 listaTemp = getFacadeFactory().getContaReceberFacade().consultarUnidadeEnsinoFinanceiraDaContaReceberPorMatriculaOuResponsavelFinanceiro(Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroVO().getTipoParcelaNegociar()) ? getConfiguracaoFinanceiroVO().getTipoParcelaNegociar().getSigla() : "VE", getNegociacaoContaReceberVO().getMatricula(), getNegociacaoContaReceberVO().getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado());
		 if (!listaTemp.isEmpty()) {
			 listaTemp.stream().forEach(
					 p->{
							getListaSelectItemUnidadeEnsino().add(new SelectItem(p.getUnidadeEnsinoFinanceira().getCodigo(), p.getUnidadeEnsinoFinanceira().getNome()));
							if(!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getUnidadeEnsino())){
								getNegociacaoContaReceberVO().setUnidadeEnsino(p.getUnidadeEnsinoFinanceira());
								atualizarContaReceberPorUnidadeEnsinoFinanceira();
							}
						});
			
			 if(getContaReceberVOs().isEmpty()) {
				 	setMensagem(UteisJSF.internacionalizar("msg_nao_existe_contaReceber_vencidas").replace("NOME_ALUNO", getNegociacaoContaReceberVO().getPessoa().getNome().toUpperCase()).replace("NOME_CURSO", getNegociacaoContaReceberVO().getMatriculaAluno().getCurso().getNome().toUpperCase()));                    
				 	setApresentarDadosRenegociacao(false);
                 return;
			 	}
			
             setMensagemID("", "");
             setApresentarDadosRenegociacao(true);                
         }else {
             setMensagem(UteisJSF.internacionalizar("msg_nao_existe_contaReceber_vencidas").replace("NOME_ALUNO", getNegociacaoContaReceberVO().getPessoa().getNome().toUpperCase()).replace("NOME_CURSO", getNegociacaoContaReceberVO().getMatriculaAluno().getCurso().getNome().toUpperCase()));                    
             setApresentarDadosRenegociacao(false);
         }
	}
    public void atualizarContaReceberPorUnidadeEnsinoFinanceira(){
        try{        	
        	if(getIsApresentarVisaoPais()){
        		setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarContaReceberRenegociacaoResponsavelFinanceiro(Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroVO().getTipoParcelaNegociar()) ? getConfiguracaoFinanceiroVO().getTipoParcelaNegociar().getSigla() : "VE", getNegociacaoContaReceberVO().getPessoa().getCodigo(), getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado()));
            }else{
            	setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarPorMatriculaEUnidadeEnsino(Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroVO().getTipoParcelaNegociar()) ? getConfiguracaoFinanceiroVO().getTipoParcelaNegociar().getSigla() : "VE", getNegociacaoContaReceberVO().getMatricula(),getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado()));
            }
        	getFacadeFactory().getContaReceberFacade().verificarPermissaoNegociacaoContaReceber(getContaReceberVOs(), getUsuarioLogado());
        	setContaReceberVOs(getFacadeFactory().getContaReceberFacade().executarCalculoValorFinalASerPago(getContaReceberVOs(), getUsuarioLogado(), getConfiguracaoFinanceiroVO(), new Date()));
	    }catch (Exception e) {
	        setApresentarDadosGravados(false);
	        setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	    }
    }
    
    public void persistir(){
        try{
        	executarValidacaoSimulacaoVisaoAluno();
            getFacadeFactory().getNegociacaoContaReceberFacade().incluirRenegociacaoAluno(getNegociacaoContaReceberVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            setApresentarDadosOpcaoRenegociacao(false);            
            setApresentarDadosGravados(true);
			consultarContaReceberAlunoMatriculaOnline();
			if(getPermitirRecebimentoCartaoCreditoOnline() && getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getQtdeDiasEntrada() == 0) {
				setModalPagamentoOnline("RichFaces.$('panelPagamento').show()");					
			}else{
				setModalPagamentoOnline("");
			}
			setMensagemID("msg_renegociacao_gravada", Uteis.SUCESSO);
        }catch (Exception e) {
        	e.printStackTrace();
            setApresentarDadosGravados(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarConfirmacaoSelecaoContaReceber(){
        try {
            getListaSelectItemOpcaoRenegociacao().clear();
            getFacadeFactory().getNegociacaoContaReceberFacade().realizarInicializacaoDadosOpcaoRenegociacao(getNegociacaoContaReceberVO(), getContaReceberVOs(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getNegociacaoContaReceberVO().getUnidadeEnsino().getCodigo()), false, getUsuarioLogado());            
            for(OpcaoAlunoCondicaoRenegociacaoVO opcao:getNegociacaoContaReceberVO().getOpcaoAlunoCondicaoRenegociacaoVOs()){
                getListaSelectItemOpcaoRenegociacao().add(new SelectItem(opcao.getNumeroParcela(), opcao.getDescricao()));
            }
            montarComboBoxItemCondicaoRenegociacao();            
            
            setApresentarDadosOpcaoRenegociacao(true);
            setApresentarDadosRenegociacao(false);
            setApresentarDadosGravados(false);
            setMensagemID("", "");
            setMensagem("");
            setMensagemDetalhada("", "", "");
            setSucesso(false);
        } catch (Exception e) {
        	e.printStackTrace();
            setApresentarDadosOpcaoRenegociacao(false);
            setApresentarDadosGravados(false);
            setApresentarDadosRenegociacao(true);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void atualizarParcelasNegociacao() {
    	try {    		
    		getFacadeFactory().getNegociacaoContaReceberFacade().realizarAtualizacaoOpcoesPagamentoConformeItemCondicaoNegociacao(getNegociacaoContaReceberVO());
	    	getListaSelectItemOpcaoRenegociacao().clear();
	    	for(OpcaoAlunoCondicaoRenegociacaoVO opcao: getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs()){
	    			getListaSelectItemOpcaoRenegociacao().add(new SelectItem(opcao.getNumeroParcela(), opcao.getDescricao()));
	        }
	    	limparMensagem();
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public void atualizarOpcaoAlunoCondicaoRenegociacao() {
    	try {
    		for(OpcaoAlunoCondicaoRenegociacaoVO opcao: getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getOpcaoAlunoCondicaoRenegociacaoVOs()){
	            getListaSelectItemOpcaoRenegociacao().add(new SelectItem(opcao.getNumeroParcela(), opcao.getDescricao()));
	        }
    		
    		limparMensagem();
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public boolean getApresentarValorFinalRenegociado() {
    	return getNegociacaoContaReceberVO().getValorDescontoIsencaoJuroMultaIndiceReajuste() > 0 
    			|| getNegociacaoContaReceberVO().getValorAcrescimoCondicaoRenegociacao() > 0.0
    			|| getNegociacaoContaReceberVO().getValorDescontoCondicaoRenegociacao() > 0.0;
    }
    
    
    public void realizarConfirmacaoNegociacao() {
    	try {
    		setOncompleteModal("");
    		if (Uteis.isAtributoPreenchido(getListaSelectItemOpcaoRenegociacao()) && getListaSelectItemOpcaoRenegociacao().size() == 1) {
    			setDescricaoConfirmacao(getListaSelectItemOpcaoRenegociacao().get(0).getLabel().toUpperCase());
    			
    		}    		
    		getFacadeFactory().getNegociacaoContaReceberFacade().validarValorEntrada(getNegociacaoContaReceberVO());
    		 if(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorTotal()) == Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorEntrada())) {
    			 setDescricaoConfirmacao("Pagamento de 100% na Entrada");
    		 }
    		 boolean validarNumeroParcela = true;
    		 if (getNegociacaoContaReceberVO().getNrParcela() == 0) {
    			 validarNumeroParcela = false;
    		 }else {
    			 if(getListaSelectItemOpcaoRenegociacao().stream().filter(t -> t.getValue().equals(getNegociacaoContaReceberVO().getNrParcela())).findFirst().isPresent()){    				 
    				 setDescricaoConfirmacao(getListaSelectItemOpcaoRenegociacao().stream().filter(t -> t.getValue().equals(getNegociacaoContaReceberVO().getNrParcela())).findFirst().get().getLabel());    				     				
    			 }
    		 }
    		 
    		 
    		if (!Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getNrParcela()) && validarNumeroParcela) {
    			throw new Exception("Nenhuma Condição de pagamento foi selecionada.");
    		}
    		limparMensagem();
    		setOncompleteModal("RichFaces.$('panelConfirmacao').show();");
    	} catch (Exception e) {
    		setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
    		setOncompleteModal("RichFaces.$('panelConfirmacao').hide();");
		}
    }
    
    public void validarValorEntrada() {
    	try {
    		getFacadeFactory().getNegociacaoContaReceberFacade().validarValorEntrada(getNegociacaoContaReceberVO());
    		limparMensagem();
    	} catch (Exception e) {
    		getNegociacaoContaReceberVO().setValorEntrada(0.0);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			atualizarParcelasNegociacao();			
		}
    }

	public void montarComboBoxItemCondicaoRenegociacao() {
		try {
			setListaSelectItemItemCondicaoRenegociacao(new ArrayList<>());			
			getNegociacaoContaReceberVO().getItemCondicaoRenegociacaoVOs().sort((p1, p2) -> p1.getFaixaEntradaInicial().compareTo(p2.getFaixaEntradaInicial()));
			for(ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO : getNegociacaoContaReceberVO().getItemCondicaoRenegociacaoVOs()) {
//				itemCondicaoRenegociacaoVO = getFacadeFactory().getItemCondicaoRenegociacaoFacade().consultarPorChavePrimaria(itemCondicaoRenegociacaoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//				itemCondicaoRenegociacaoVO.setCondicaoRenegociacao(getFacadeFactory().getCondicaoRenegociacaoFacade().consultarPorChavePrimaria(itemCondicaoRenegociacaoVO.getCondicaoRenegociacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getListaSelectItemItemCondicaoRenegociacao().add(new SelectItem(itemCondicaoRenegociacaoVO.getCodigo(), itemCondicaoRenegociacaoVO.getDescricao()));				
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
    
    public Double getValorTotalContaReceberVisaoAluno() {
    	if(getAlunoConcordouComTermosRenegociacaoOnLine()){
    		return getContaReceberVOs().stream().filter(p-> p.getSelecionado()).map(p -> p.getValorReceberCalculado()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));	
    	}else{
    		return getContaReceberVOs().stream().map(p -> p.getValorReceberCalculado()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
    	}
    }
    
    public NegociacaoContaReceberVO getNegociacaoContaReceberVO() {
        if(negociacaoContaReceberVO == null){
            negociacaoContaReceberVO = new NegociacaoContaReceberVO();
        }
        return negociacaoContaReceberVO;
    }

    public void setNegociacaoContaReceberVO(NegociacaoContaReceberVO negociacaoContaReceberVO) {
        this.negociacaoContaReceberVO = negociacaoContaReceberVO;
    }

    public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        }
        return contaReceberVOs;
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    public Boolean getApresentarDadosRenegociacao() {
        if (apresentarDadosRenegociacao == null) {
            apresentarDadosRenegociacao = false;
        }
        return apresentarDadosRenegociacao;
    }

    public void setApresentarDadosRenegociacao(Boolean apresentarDadosRenegociacao) {
        this.apresentarDadosRenegociacao = apresentarDadosRenegociacao;
    }
    
    public Boolean getApresentarMensagemGeral(){
        return !getApresentarDadosRenegociacao() && !getApresentarDadosOpcaoRenegociacao();
    }

    
    public Boolean getApresentarDadosOpcaoRenegociacao() {
        if(apresentarDadosOpcaoRenegociacao == null){
            apresentarDadosOpcaoRenegociacao = false;
        }
        return apresentarDadosOpcaoRenegociacao;
    }

    
    public void setApresentarDadosOpcaoRenegociacao(Boolean apresentarDadosOpcaoRenegociacao) {
        this.apresentarDadosOpcaoRenegociacao = apresentarDadosOpcaoRenegociacao;
    }

    
    public List<SelectItem> getListaSelectItemOpcaoRenegociacao() {
        if(listaSelectItemOpcaoRenegociacao == null){
            listaSelectItemOpcaoRenegociacao = new ArrayList<SelectItem>(0);
        }        
        return listaSelectItemOpcaoRenegociacao;
    }

    
    public void setListaSelectItemOpcaoRenegociacao(List<SelectItem> listaSelectItemOpcaoRenegociacao) {
        this.listaSelectItemOpcaoRenegociacao = listaSelectItemOpcaoRenegociacao;
    }
    
    public void voltarSelecaoContaReceber(){
    	getNegociacaoContaReceberVO().setValorTotalJuro(0.0);
    	getNegociacaoContaReceberVO().setValorTotalMulta(0.0);
    	getNegociacaoContaReceberVO().setValorIndiceReajusteDesconto(0.0);
    	getNegociacaoContaReceberVO().setNrParcela(0);
    	negociacaoContaReceberVO.setCondicaoRenegociacao(new CondicaoRenegociacaoVO());
        setApresentarDadosRenegociacao(true);
        setApresentarDadosOpcaoRenegociacao(false);
        setMensagemID("", "");
        setMensagem("");
        setMensagemDetalhada("", "", "");
        setSucesso(false);
    }

    
    public Boolean getApresentarDadosGravados() {
        if(apresentarDadosGravados == null){
            apresentarDadosGravados = false;
        }
        return apresentarDadosGravados;
    }

    
    public void setApresentarDadosGravados(Boolean apresentarDadosGravados) {
        this.apresentarDadosGravados = apresentarDadosGravados;
    }
    
    public String getBoleto() {
        if (getApresentarImprimirBoleto()) {
            return "abrirPopup('../BoletoBancarioSV?codigoContaReceber=" + getContaReceberVisaoAluno().getCodigo() + "&titulo=matricula', 'boletoMatricula', 780, 585)";
        } else {
            return "";
        }
    }
    
    public Boolean getApresentarImprimirBoleto() {
        if (getContaReceberVisaoAluno().getSituacao().equalsIgnoreCase("AR")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    
    public ContaReceberVO getContaReceberVisaoAluno() {
        if(contaReceberVisaoAluno == null){
            contaReceberVisaoAluno = new ContaReceberVO();
        }
        return contaReceberVisaoAluno;
    }

    
    public void setContaReceberVisaoAluno(ContaReceberVO contaReceberVisaoAluno) {
        this.contaReceberVisaoAluno = contaReceberVisaoAluno;
    }
    
    
    public void confirmarConcordaComTermosRenegociacaoContaReceber() {
        setAlunoConcordouComTermosRenegociacaoOnLine(Boolean.TRUE);
    }

    /**
     * @return the alunoConcordouComTermosRenegociacaoOnLine
     */
    public Boolean getAlunoConcordouComTermosRenegociacaoOnLine() {
        if (alunoConcordouComTermosRenegociacaoOnLine == null) {
            alunoConcordouComTermosRenegociacaoOnLine = Boolean.FALSE;
        }
        return alunoConcordouComTermosRenegociacaoOnLine;
    }

    /**
     * @param alunoConcordouComTermosRenegociacaoOnLine the alunoConcordouComTermosRenegociacaoOnLine to set
     */
    public void setAlunoConcordouComTermosRenegociacaoOnLine(Boolean alunoConcordouComTermosRenegociacaoOnLine) {
        this.alunoConcordouComTermosRenegociacaoOnLine = alunoConcordouComTermosRenegociacaoOnLine;
    }
    

	private String modalPagamentoOnline;
	private Boolean contaRecebida;

	public String getModalPagamentoOnline() {
		if(modalPagamentoOnline == null) {
			modalPagamentoOnline = "";
		}
		return modalPagamentoOnline;
	}

	public void setModalPagamentoOnline(String modalPagamentoOnline) {
		this.modalPagamentoOnline = modalPagamentoOnline;
	}
	
	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private Integer quantidadeCartao;
	private Boolean desativarBotaoCartaoCredito;
	
	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
		if(formaPagamentoNegociacaoRecebimentoVOs == null) {
			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
	}

	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
		if(negociacaoRecebimentoVO == null) {
			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		}
		return negociacaoRecebimentoVO;
	}

	
	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
	}

	public void adicionarNovoCartaoCredito() {
		try {
			ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
			setQuantidadeCartao(getQuantidadeCartao() + 1);
			int parcelas = 1;
			if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
				Date menorDataVencimento = null;
				for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
					if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
							(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
						menorDataVencimento = c.getContaReceber().getDataVencimento();
					}
				}
				parcelas = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getNegociacaoRecebimentoVO().getResiduo(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
			}
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().adicionarNovoCartaoCredito(getNegociacaoRecebimentoVO(), configuracaoFinanceiroCartaoVO, parcelas, getQuantidadeCartao(), getUsuarioLogado());
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
			}
			setMensagemID("msg_PagamentoOnline_cartaoAdicionadoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerCartaoCredito() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().removerCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, getNegociacaoRecebimentoVO(), getUsuarioLogado());
			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void calcularTotalPago() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		try {
			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().calcularTotalPago(getNegociacaoRecebimentoVO(), formaPagamentoNegociacaoRecebimentoVO, getUsuarioLogado());
			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
			inicializarMensagemVazia();
		} catch (Exception e) {
			formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(0.0);
			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Integer getQuantidadeCartao() {
		if(quantidadeCartao == null) {
			quantidadeCartao = 0;
		}
		return quantidadeCartao;
	}

	public void setQuantidadeCartao(Integer quantidadeCartao) {
		this.quantidadeCartao = quantidadeCartao;
	}

	public void consultarContaReceberAlunoMatriculaOnline() {
		try {
			setNegociacaoRecebimentoVO(new NegociacaoRecebimentoVO());
			setDesativarBotaoCartaoCredito(false);
			getNegociacaoRecebimentoVO().setMatricula(getVisaoAlunoControle().getMatricula().getMatricula());
			Boolean possuiPermissaoEmitirBoletoVencido = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("ImprimirBoletoVencidoVisaoAluno", getUsuarioLogado());
			for (ContaReceberVO obj : getNegociacaoContaReceberVO().getNovaContaReceber()) {
				ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
				getFacadeFactory().getContaReceberFacade().validarTipoImpressaoPorContaReceber(obj, possuiPermissaoEmitirBoletoVencido, getUsuarioLogado());
				contaReceberNegociacaoRecebimentoVO.setContaReceber(obj);
				contaReceberNegociacaoRecebimentoVO.setValorTotal(obj.getCalcularValorFinal(getConfiguracaoFinanceiroVO(), getUsuarioLogado()));
				getNegociacaoRecebimentoVO().adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
				if (Uteis.isAtributoPreenchido(getNegociacaoContaReceberVO().getValorEntrada())) {					
					getNegociacaoRecebimentoVO().setValorTotal(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorEntrada()));
				} else {
					getNegociacaoRecebimentoVO().setValorTotal(Uteis.arrendondarForcando2CadasDecimais(getNegociacaoRecebimentoVO().getValorTotal() + obj.getValorReceberCalculado()));
				}
				getNegociacaoRecebimentoVO().setTipoPessoa(obj.getTipoPessoa());
			}
			setPermitirRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroVO().getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "usarrenegociacaoonline"));
			if(getPermitirRecebimentoCartaoCreditoOnline() && getNegociacaoRecebimentoVO().getTipoAluno()) {
				setMatriculaPeriodoVO(new MatriculaPeriodoVO());
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(getNegociacaoRecebimentoVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado()));
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroVO(), getUsuarioLogado()));
				setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
				setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(getMatriculaPeriodoVO().getTurma().getCodigo(), getMatriculaPeriodoVO().getTurma().getCurso().getCodigo(), TipoNivelEducacional.getEnum(getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNivelEducacional()).getValor(), getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				verificarContasRecbimentoOnline();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getDesativarBotaoCartaoCredito() {
		if(desativarBotaoCartaoCredito == null) {
			desativarBotaoCartaoCredito = false;
		}
		return desativarBotaoCartaoCredito;
	}

	public void setDesativarBotaoCartaoCredito(Boolean desativarBotaoCartaoCredito) {
		this.desativarBotaoCartaoCredito = desativarBotaoCartaoCredito;
	}
	
	public void validarNumeroCartaoCredito() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			formaPagamentoNegociacaoRecebimentoVO.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorCodigoConfiguracaoFinanceiroCartao(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getNome())) {
				throw new Exception(UteisJSF.internacionalizar("msg_NumeroCartaoCreditoInvalido"));
			} else {
				setMensagemID("msg_NumeroCartaoCreditoValido");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarRecebimentoCartaoCredito() {
		List<FormaPagamentoNegociacaoRecebimentoVO> listaCartoesAntesAlteracao = getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().stream().collect(Collectors.toList());
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getNegociacaoRecebimentoFacade().realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(getNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO().getMatricula(), getConfiguracaoFinanceiroVO(), getUsuarioLogado());
			imprimirComprovanteRecebimento();
			setModalPagamentoOnline("RichFaces.$('panelPagamento').hide()");
			setContaRecebida(true);
			setMensagemDetalhada("msg_RenovarMatriculaControle_pagamentoRealizadoComSucesso", getNegociacaoRecebimentoVO().getMensagemPagamentoCartaoCredito(), Uteis.SUCESSO);
		} catch (Exception e) {
			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().addAll(listaCartoesAntesAlteracao);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}	
	}	
	
	public void imprimirComprovanteRecebimento() {
		try {
			comprovanteRecebimentoRelControle = null;
			comprovanteRecebimentoRelControle = (ComprovanteRecebimentoRelControle) context().getExternalContext().getSessionMap().get(ComprovanteRecebimentoRelControle.class.getSimpleName());
			if (comprovanteRecebimentoRelControle == null) {
				comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
				context().getExternalContext().getSessionMap().put(ComprovanteRecebimentoRelControle.class.getSimpleName(), comprovanteRecebimentoRelControle);
			}
			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
				getComprovanteRecebimentoRelControle().setNegociacaoRecebimentoVO((NegociacaoRecebimentoVO) getNegociacaoRecebimentoVO().clone());
				getComprovanteRecebimentoRelControle().imprimirPDFRecebimentoCartaoCredito();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public void realizarRecebimentoBoletoBancario () {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			List<BoletoBancarioRelVO> boletoBancarioRelVOs = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, null, getVisaoAlunoControle().getMatricula().getMatricula(), "", "", "", 0, 0, null, null, 0, "aluno", 0, getUsuarioLogado(), "boletoAluno", getNegociacaoContaReceberVO().getCodigo(), getConfiguracaoFinanceiroVO(), getUsuarioLogado().getPessoa().getCodigo(), false, null, null);
			getFacadeFactory().getBoletoBancarioRelFacade().realizarImpressaoPDF(boletoBancarioRelVOs, getSuperParametroRelVO(), getVersaoSistema(), "boleto", getUsuarioLogado());
			realizarImpressaoRelatorio(getSuperParametroRelVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private Boolean permitirRecebimentoCartaoCreditoOnline;
	private ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;

	public Boolean getPermitirRecebimentoCartaoCreditoOnline() {
		if(permitirRecebimentoCartaoCreditoOnline == null) {
			permitirRecebimentoCartaoCreditoOnline = true;
		}
		return permitirRecebimentoCartaoCreditoOnline;
	}

	public void setPermitirRecebimentoCartaoCreditoOnline(Boolean permitirRecebimentoCartaoCreditoOnline) {
		this.permitirRecebimentoCartaoCreditoOnline = permitirRecebimentoCartaoCreditoOnline;
	}

	public ComprovanteRecebimentoRelControle getComprovanteRecebimentoRelControle() {
		return comprovanteRecebimentoRelControle;
	}

	public void setComprovanteRecebimentoRelControle(ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle) {
		this.comprovanteRecebimentoRelControle = comprovanteRecebimentoRelControle;
	}
	
	public void consultarConfiguracaoFinanceiroCartao() {
		try {
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
			if(!formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));				
			} else {
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getContaRecebida() {
		if(contaRecebida == null) {
			contaRecebida = false;
		}
		return contaRecebida;
	}

	public void setContaRecebida(Boolean contaRecebida) {
		this.contaRecebida = contaRecebida;
	}
	
	public Boolean getIsApresentarResiduoPagamentoOnline() {
		return getNegociacaoRecebimentoVO().getResiduo() != 0.0;
	}
	
	private String modalConfirmacaoPagamento;

	public String getModalConfirmacaoPagamento() {
		if(modalConfirmacaoPagamento == null) {
			modalConfirmacaoPagamento = "";
		}
		return modalConfirmacaoPagamento;
	}

	public void setModalConfirmacaoPagamento(String modalConfirmacaoPagamento) {
		this.modalConfirmacaoPagamento = modalConfirmacaoPagamento;
	}
	
	private TermoReconhecimentoDividaRelControle termoReconhecimentoDividaRelControle;
	
	/**
	 * Responsável por executar a emissão do termo de reconhecimento de dívida na visão do aluno, no qual, só é possível emitir tal termo se existir
	 * um LayoutPadraoTermoReconhecimentoDivida vinculado à condição de renegociação.
	 * 
	 * @author Wellington - 9 de dez de 2015
	 */
	public void emitirTermoReconhecimentoDivida() {
		try {
			setTermoReconhecimentoDividaRelControle(null);
			setTermoReconhecimentoDividaRelControle((TermoReconhecimentoDividaRelControle) context().getExternalContext().getSessionMap().get(TermoReconhecimentoDividaRelControle.class.getSimpleName()));
			if (getTermoReconhecimentoDividaRelControle() == null) {
				setTermoReconhecimentoDividaRelControle(new TermoReconhecimentoDividaRelControle());
				context().getExternalContext().getSessionMap().put(TermoReconhecimentoDividaRelControle.class.getSimpleName(), getTermoReconhecimentoDividaRelControle());
			}
			if (!getNegociacaoContaReceberVO().getCodigo().equals(0)) {
				getTermoReconhecimentoDividaRelControle().setNegociacaoContaReceberVO(getNegociacaoContaReceberVO());
				getTermoReconhecimentoDividaRelControle().setObservacaoHistorico("");
				getTermoReconhecimentoDividaRelControle().setAlunoVO(getNegociacaoContaReceberVO().getMatriculaAluno());
				String tipoLayout = "";
				if (getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.LAYOUT_1)) {
					tipoLayout = "TermoReconhecimentoDividaRel";
				} else if (getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.LAYOUT_2)) {
					tipoLayout = "TermoReconhecimentoDividaLayout3Rel";
				} else if (getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.TEXTO_PADRAO)) {
					tipoLayout = "TextoPadrao";
					getTermoReconhecimentoDividaRelControle().setTextoPadraoDeclaracao(getNegociacaoContaReceberVO().getCondicaoRenegociacao().getTextoPadraoDeclaracaoVO().getCodigo());
				}
				getTermoReconhecimentoDividaRelControle().setTipoLayout(tipoLayout);
				getTermoReconhecimentoDividaRelControle().imprimirPDF();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
	/**
	 * Responsável por exibir o termo de reconhecimento de dívida do tipo TEXTO_PADRAO.
	 * 
	 * @author Wellington - 9 de dez de 2015
	 * @return
	 */
	public String getVisualizarRelatorio() {
		if (getTermoReconhecimentoDividaRelControle() != null) {
			if (getTermoReconhecimentoDividaRelControle().getFazerDownload()) {
				return getTermoReconhecimentoDividaRelControle().getDownload();
			} else {
				return "abrirPopup('../VisualizarContrato', 'RelatorioContrato', 730, 545);";
			}
		}
		return "";
	}

	public TermoReconhecimentoDividaRelControle getTermoReconhecimentoDividaRelControle() {
		return termoReconhecimentoDividaRelControle;
	}

	public void setTermoReconhecimentoDividaRelControle(TermoReconhecimentoDividaRelControle termoReconhecimentoDividaRelControle) {
		this.termoReconhecimentoDividaRelControle = termoReconhecimentoDividaRelControle;
	}
	
	public boolean getIsApresentarBotaoTermoReconhecimentoDividaTextoPadrao() {
		return getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida() != null && getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.TEXTO_PADRAO);
	}
	
	public boolean getIsApresentarBotaoTermoReconhecimentoDivida() {
		return getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida() != null && (getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.LAYOUT_1) || getNegociacaoContaReceberVO().getCondicaoRenegociacao().getLayoutPadraoTermoReconhecimentoDivida().equals(LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum.LAYOUT_2));
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 * 23/02/2016 15:12
	 */
	private List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs;

	public List<ConfiguracaoFinanceiroCartaoVO> getConfiguracaoFinanceiroCartaoVOs() {
		if(configuracaoFinanceiroCartaoVOs == null) {
			configuracaoFinanceiroCartaoVOs = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
		}
		return configuracaoFinanceiroCartaoVOs;
	}

	public void setConfiguracaoFinanceiroCartaoVOs(List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs) {
		this.configuracaoFinanceiroCartaoVOs = configuracaoFinanceiroCartaoVOs;
	}
	
	public int getRetornarTamanhoListaConfiguracaoFinanceiroCartaoVOs() {
		if(!getConfiguracaoFinanceiroCartaoVOs().isEmpty())
			return getConfiguracaoFinanceiroCartaoVOs().size();
		return 0;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 18/03/2016 08:29
	 */
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;
	
	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if(configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}
	
	public void verificarContasRecbimentoOnline() {
		try {
			if(!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
				ConsistirException consistirException = new ConsistirException();
				getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().verificarContasRecebimentoOnline(getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs(), getConfiguracaoRecebimentoCartaoOnlineVO(), consistirException, false, false, false, false, false, true, getUsuarioLogado());
				if(!consistirException.getListaMensagemErro().isEmpty() || !getNegociacaoContaReceberVO().getPermitirPagamentoCartaoCreditoVisaoAluno()) {
					setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
					return;
				}
				consultarCartoesParaRecebimentoOnline();
			} else {
				setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarCartoesParaRecebimentoOnline() {
		try {
			ConfiguracaoFinanceiroVO confFinanceiro = Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO()) ? getConfiguracaoRecebimentoCartaoOnlineVO().getConfiguracaoFinanceiroVO() : getConfiguracaoFinanceiroVO();
			//setPermitirRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(confFinanceiro.getCodigo()));
			//(getPermitirRecebimentoCartaoCreditoOnline() && getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().realizarVerificacaoRecebimentoOnlinePermitePorParcelasValorAReceber(getConfiguracaoRecebimentoCartaoOnlineVO(), getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs().size(), getNegociacaoRecebimentoVO().getValorTotal(), getUsuarioLogado())) {
				getConfiguracaoFinanceiroCartaoVOs().clear();
				getConfiguracaoFinanceiroCartaoVOs().addAll(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroVO().getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "", getTipoCartao().name(), getUsuarioLogado()));
				getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
				ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
				configuracaoFinanceiroCartaoVO.setBoletoBancario(true);
				getConfiguracaoFinanceiroCartaoVOs().add(configuracaoFinanceiroCartaoVO);				
			//} else {
				//setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
			//}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void apresentarDicaCVCartaoCredito() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(true);
	}

	public void esconderDicaCVCartaoCredito() {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(false);
	}
	
	public String getMensagemFaixaEntradaApresentar() {
		return UteisJSF.internacionalizar("msg_ItemRenociacaoContaReceber_informeUmValorEntre")
				.replace("{0}", " " + Uteis.formatarDoubleParaMoeda(getNegociacaoContaReceberVO().getValorMinimoEntrada()))
				.replace("{1}", " " + Uteis.formatarDoubleParaMoeda(getNegociacaoContaReceberVO().getValorMaximoEntrada()));
	}

	public String dataVencimentoApresentar() {
		return UteisData.getDataAplicandoFormatacao(UteisData.adicionarDiasEmData(new Date(), getNegociacaoContaReceberVO().getItemCondicaoRenegociacao().getQtdeDiasEntrada()), "dd/MM/yyyy");
	}

	public String getDataVencimentoApresentar() {
		return "";
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private MatriculaPeriodoVO matriculaPeriodoVO;

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	
	
	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}	
	
	 public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void editarContaReceberVisaoAluno() {
		 try {
	        ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaAPagarItens");
	        setContaReceberVisaoAluno(obj);
		 } catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getIsExibirScrollCartoes() {
		return getConfiguracaoFinanceiroCartaoVOs().size() > 7;
	}
	
	private String descricaoConfirmacao;

	public String getDescricaoConfirmacao() {
		if(descricaoConfirmacao == null){
			descricaoConfirmacao = "";
		}
		return descricaoConfirmacao;
	}

	public void setDescricaoConfirmacao(String descricaoConfirmacao) {
		this.descricaoConfirmacao = descricaoConfirmacao;
	}
	
	public void selecionarCondicaoRenegociacao() {
		for(SelectItem item: getListaSelectItemOpcaoRenegociacao()) {
			if(item.getValue().equals(getNegociacaoContaReceberVO().getNrParcela())) {
				setDescricaoConfirmacao(item.getLabel().toUpperCase());
				break;
			}
		}
	}

	public TipoCartaoOperadoraCartaoEnum getTipoCartao() {
		if (tipoCartao == null) {
			if (Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO()) &&
					PermitirCartaoEnum.DEBITO.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao())) {
				tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
			} else {
				tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
			}
		}
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public List<SelectItem> getListaSelectItemItemCondicaoRenegociacao() {
		if (listaSelectItemItemCondicaoRenegociacao == null)  {
			listaSelectItemItemCondicaoRenegociacao = new ArrayList<>();
		}
		return listaSelectItemItemCondicaoRenegociacao;
	}

	public void setListaSelectItemItemCondicaoRenegociacao(List<SelectItem> listaSelectItemItemCondicaoRenegociacao) {
		this.listaSelectItemItemCondicaoRenegociacao = listaSelectItemItemCondicaoRenegociacao;
	}

	public Boolean getApresentarBotaoConfirmarNegociacao() {
		return !getListaSelectItemOpcaoRenegociacao().isEmpty() || (getNegociacaoContaReceberVO().getValorEntrada() > 0 && Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorTotal()) == Uteis.arrendondarForcando2CadasDecimais(getNegociacaoContaReceberVO().getValorEntrada())); 
	}
	
	public Boolean selecionarTodasContas;
		
	public Boolean getSelecionarTodasContas() {
		if(selecionarTodasContas == null) {
			selecionarTodasContas = true;
		}
		return selecionarTodasContas;
	}

	public void setSelecionarTodasContas(Boolean selecionarTodasContas) {
		this.selecionarTodasContas = selecionarTodasContas;
	}

	public void realizarSelecaoContaReceber() {
		getContaReceberVOs().forEach(t -> t.setSelecionado(getSelecionarTodasContas()));
	}
	
}
