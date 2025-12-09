package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MatriculaPeriodoVencimentoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String descricaoPagamento;
    private Integer matriculaPeriodo;
    private SituacaoVencimentoMatriculaPeriodo situacao;
    private ContaReceberVO contaReceber;
    private ControleRemessaContaReceberVO controleRemessaContaReceberVO;
    /**
     * Campo retirado pois foi criado tipoOrigemMatriculaPeriodoVencimento agora nao sendo mais matricula e mensalidade;
     * private Boolean vencimentoReferenteMatricula;
     */
    private TipoOrigemContaReceber tipoOrigemMatriculaPeriodoVencimento;
    private Boolean vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina;
    private Date dataVencimento;
    // Campo criado para gerenciar a data de competência da conta a receber, as regras dessa data, são carregadas do calendário de matricula
    private Date dataCompetencia;
    // Campo criado para controlar separadamento, parcela a parcela, se a mesma é ou não para estar com controle financeiro manual, esse campo será muito utilizado para casos de importação no meio do periodo letivo.
    private Boolean financeiroManual;
    private boolean usaValorPrimeiraParcela = false;
    
    private Double valor;
    private Double valorDesconto;
    private Double valorDescontoInstituicao;
    private Double valorDescontoConvenio;
    private String parcela;
    private String tipoDesconto;
    private String tipoBoleto;
    private boolean validarParcela;
    private Integer diasVariacaoDataVencimento;
    private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
    private Double valorDescontoCalculadoSegundaFaixaDescontos;
    private Double valorDescontoCalculadoTerceiraFaixaDescontos;
    private Double valorDescontoCalculadoQuartaFaixaDescontos;
    
    private Integer geracaoManualParcela;
    private Boolean erroGeracaoParcela;
    private String mensagemErroGeracaoParcela;

    /*
     * TRANSIENT = utilizado para manter dados da matricula periodo
     * relacionada com este objeto. Esta manutencao de dados é necessário
     * para o processamento de geracao de boleto mes a mes.
     */
    private MatriculaPeriodoVO matriculaPeriodoVO;
    
    /**
     * TRANSIENT
     */
    private String controleGeracaoParcelaMesAMes;
    /**
     * TRANSIENT
     */
    private String erroGeracaoParcelaMesAMes;
    private Double acrescimoExtra;
    private String descricaoDesconto;
    private String descricaoParcela;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContaReceber</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public MatriculaPeriodoVencimentoVO() {
        super();
    }

    public static void validarDados(MatriculaPeriodoVencimentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Matrícula Período Vencimento) deve ser informado.");
        }
        if (obj.getParcela().equals("")) {
            throw new ConsistirException("O campo PARCELA (Matrícula Período Vencimento) deve ser informado.");
        }
        if (obj.getValidarParcela() && !obj.getParcela().equals("MA")) {
            validarLeiauteParcela(obj.getParcela());
        }
        if (obj.getDataVencimento() == null) {
            throw new ConsistirException("O campo DATA DE VENCIMENTO (Matrícula Período Vencimento) deve ser informado.");
        }
        if (obj.getTipoDesconto().equals("")) {
            obj.setTipoDesconto("PO");
        }
    }

    public Long getNrDiasAtraso() {
        Long dias = Uteis.nrDiasEntreDatas(new Date(), getDataVencimento());
        if (dias > 0) {
            return dias;
        }
        return 0l;
    }

    public Double getCalcularValorFinal() {
        Double valorFinal = getValor() - getValorDesconto() - getValorDescontoConvenio()
                - getValorDescontoInstituicao();
        return valorFinal;
    }

    public String getTipoDesc() {
        return TipoDescontoAluno.getSimbolo(getTipoDesconto());
    }

//	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
//		try {
//			ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//			return conf;
//		} catch (Exception e) {
//			return new ConfiguracaoFinanceiroVO();
//		}
//	}
    public String getParcela() {
        if (parcela == null) {
            parcela = "1/1";
        }
        return (parcela);
    }
    
    public String getParcelaCusteadaConvenio(Integer totalParcelaMensalidade, Integer totalParcelaMateriaDidatico) {
    	if(getParcela().contains("/") && (getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() || getTipoOrigemMatriculaPeriodoVencimento().isMensalidade())){
    		Integer parcela = Integer.parseInt(getParcela().substring(0, getParcela().indexOf("/")));
    		if(getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico()){
    			return parcela+"/"+ (totalParcelaMensalidade+totalParcelaMateriaDidatico);
    		}else{
    			return (parcela+totalParcelaMateriaDidatico)+"/"+ (totalParcelaMensalidade+totalParcelaMateriaDidatico);
    		}
    	}else if(getParcela().contains("MA") || getParcela().contains("EX")){
    		return getParcela();
    	}
    	throw new StreamSeiException("Não foi localizado a parcela da matrícula perido vencimento " + getCodigo());
    }
    
    public Boolean getParcelaReferenteMatricula() {
        if (getParcela().equals("MA")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getDescricaoPagamento() {
        if (descricaoPagamento == null) {
            descricaoPagamento = "";
        }
        return (descricaoPagamento);
    }

    public void setDescricaoPagamento(String descricaoPagamento) {
        this.descricaoPagamento = descricaoPagamento;
    }

    public Double getValorDesconto() {
        if (valorDesconto == null) {
            valorDesconto = 0.0;
        }
        return (valorDesconto);
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public Double getValor_Apresentar() {
        return (Uteis.arredondarDecimal(valor, 2));
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        return (dataVencimento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataVencimento_Apresentar() {
        if (dataVencimento == null) {
            return "";
        }
        return (Uteis.getData(dataVencimento));
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        if (data == null) {
            return "";
        }
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipoDesconto() {
        if (tipoDesconto == null) {
            tipoDesconto = "PO";
        }
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public String getTipoBoleto() {
        if (tipoBoleto == null) {
            tipoBoleto = "";
        }
        return tipoBoleto;
    }

    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public Double getValorDescontoConvenio() {
        if (valorDescontoConvenio == null) {
            valorDescontoConvenio = 0.0;
        }
        return valorDescontoConvenio;
    }

    public void setValorDescontoConvenio(Double valorDescontoConvenio) {
        this.valorDescontoConvenio = Uteis.arrendondarForcando2CadasDecimais(valorDescontoConvenio);
    }

    public Double getValorDescontoInstituicao() {
        if (valorDescontoInstituicao == null) {
            valorDescontoInstituicao = 0.0;
        }
        return valorDescontoInstituicao;
    }

    public void setValorDescontoInstituicao(Double valorDescontoInstituicao) {
        this.valorDescontoInstituicao = Uteis.arrendondarForcando2CadasDecimais(valorDescontoInstituicao);
    }

    /**
     * @return the matriculaPeriodo
     */
    public Integer getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = 0;
        }
        return matriculaPeriodo;
    }

    /**
     * @param matriculaPeriodo
     *            the matriculaPeriodo to set
     */
    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    /**
     * @return the situacao
     */
    public SituacaoVencimentoMatriculaPeriodo getSituacao() {

        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (situacao == null) {
            return "";
        }
        if(getContaReceber().getSituacao().equals("NE")){
        	return SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_RENEGOCIADA.getDescricao();
        }
        if (getContaReceber().getContaEditadaManualmente() && (!getContaReceber().getSituacaoEQuitada())) {
        	return SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_RENEGOCIADA_EDITADA_MANUALMENTE.getDescricao();
        }
        return situacao.getDescricao();
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoVencimentoMatriculaPeriodo situacao) {
        this.situacao = situacao;
    }
    
    

    public TipoOrigemContaReceber getTipoOrigemMatriculaPeriodoVencimento() {
		return tipoOrigemMatriculaPeriodoVencimento;
	}

	public void setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber tipoOrigemMatriculaPeriodoVencimento) {
		this.tipoOrigemMatriculaPeriodoVencimento = tipoOrigemMatriculaPeriodoVencimento;
	}

	/**
     * @return the vencimentoReferenteMatricula
     */
    public Boolean getVencimentoReferenteMatricula() {       
        return Uteis.isAtributoPreenchido(getTipoOrigemMatriculaPeriodoVencimento()) &&  getTipoOrigemMatriculaPeriodoVencimento().isMatricula();
    }

    /**
     * @param vencimentoReferenteMatricula
     *            the vencimentoReferenteMatricula to set
     *//*
    public void setVencimentoReferenteMatricula(Boolean vencimentoReferenteMatricula) {
        this.vencimentoReferenteMatricula = vencimentoReferenteMatricula;
    }*/

    public ContaReceberVO getContaReceber() {
        if (contaReceber == null) {
            contaReceber = new ContaReceberVO();
        }
        return contaReceber;
    }

    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }
    
    

    public ControleRemessaContaReceberVO getControleRemessaContaReceberVO() {
    	if(controleRemessaContaReceberVO == null) {
    		controleRemessaContaReceberVO = new ControleRemessaContaReceberVO();
    	}
		return controleRemessaContaReceberVO;
	}

	public void setControleRemessaContaReceberVO(ControleRemessaContaReceberVO controleRemessaContaReceberVO) {
		this.controleRemessaContaReceberVO = controleRemessaContaReceberVO;
	}

	public String getDescricaoVencimentoVOParaHistorico() {
        return "Valor Base: " + this.getValor() + " (" + this.getTipoDesconto() + ") Desconto: "
                + this.getValorDesconto() + " Valor Desconto Instituicional: " + this.getValorDescontoInstituicao()
                + " Valor Desconto Convênio: " + this.getValorDescontoConvenio() + " Valor Final: "
                + this.getCalcularValorFinal();
    }

    public void atualizarSituacaoMatriculaPeriodoVencimentoDeAcordoContaReceber() {
        if (this.getContaReceber().getCodigo() == 0) {
            return;
        }
        if (this.getContaReceber().getSituacao().equals("RE")) {
            this.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA);
            return;
        }
        if (this.getContaReceber().getContaEditadaManualmente()) {
        	// Quando uma conta é editada manualmente, vamos gerenciar a mesma, da mesma forma que uma
        	// conta renegociada - ou seja vamos considerar como se a mesma esteja paga.
        	// Isso visa garantir que ao repassar pela matricula a parcela editada manualmente, nao será
        	// deletada e regerada pelo SEI. 
        	// Da mesma forma, atualmente no SEI quando uma conta é renegociada o SEI altera a situacaoMatriculaPeriodoVencimento
        	// para gerada e paga (pois a renegociacao assume a gestao do recebimento). 
            this.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA);
        }
    }

    public Boolean verificarDataVctoCorrespondeteMesAnoReferenciaGerar(MatriculaPeriodoVencimentoVO parcelaRegerar) {
        try {
//            Integer mesDataVcto = Uteis.getMesData(this.getDataVencimento());
//            Integer anoDataVcto = Uteis.getAnoData(this.getDataVencimento());
//            String mesReferenciaStr = mesAnoReferenciaGerar.substring(0, mesAnoReferenciaGerar.indexOf("/"));
//            String anoReferenciaStr = mesAnoReferenciaGerar.substring(mesAnoReferenciaGerar.indexOf("/") + 1);
//            Integer mesReferencia = Integer.valueOf(mesReferenciaStr);
//            Integer anoReferencia = Integer.valueOf(anoReferenciaStr);
//            if ((anoReferencia.equals(anoDataVcto))
//                    && (mesReferencia.equals(mesDataVcto))) {
//                return true;
//            }
//            return false;
        	if(parcelaRegerar.getTipoOrigemMatriculaPeriodoVencimento().equals(getTipoOrigemMatriculaPeriodoVencimento())){
        		String parcela = this.getParcela();
    			if (parcela.contains("/")) {
    				parcela = parcela.substring(0, parcela.indexOf("/"));
    			}
    			if (parcela.equals(parcelaRegerar.getParcela())) {
    				return true;
    			}	
        	}
			return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean verificarParcelaCorrespondente(MatriculaPeriodoVencimentoVO parcelaRegerar) {
    	try {
    		if(parcelaRegerar.getTipoOrigemMatriculaPeriodoVencimento().equals(getTipoOrigemMatriculaPeriodoVencimento())){
    			String parcela = this.getParcela();
    			if (parcela.contains("/")) {
    				parcela = parcela.substring(0, parcela.indexOf("/"));
    			}
    			String parcelaTemp = parcelaRegerar.getParcela();
    			if (parcelaTemp.contains("/")) {
    				parcelaTemp = parcelaTemp.substring(0, parcelaTemp.indexOf("/"));
    			}
    			return parcela.equals(parcelaTemp);
    		}
    		return false;
    	} catch (Exception e) {
    		return false;
    	}
    }

    /**
     * @return the vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina
     */
    public Boolean getVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina() {
        if (vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina == null) {
            vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina = Boolean.FALSE;
        }
        return vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina;
    }

    /**
     * @param vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina
     *            the vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina to
     *            set
     */
    public void setVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina(Boolean vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina) {
        this.vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina = vencimentoExtraCobrancaValorDiferencaInclusaoDisciplina;
    }

    public Double getValorTotalDesconto() {
        Double descontoVencimentoVO = Uteis.arrendondarForcando2CadasDecimais(this.getValorDesconto()
                + this.getValorDescontoConvenio() + this.getValorDescontoInstituicao());
        if ((this.getContaReceber() == null) || (this.getContaReceber().getCodigo().equals(0))) {
            return descontoVencimentoVO;
        }
        if (!this.getContaReceber().getSituacaoEQuitada()) {
            return descontoVencimentoVO;
        } else {
            return this.getContaReceber().getValorTotalDesconto();
        }
    }

    public Double getValorPagoVcto() {
        if ((this.getContaReceber() == null) || (this.getContaReceber().getCodigo().equals(0))) {
            return 0.0;
        }
        if (this.getContaReceber().getSituacaoEQuitada()) {
            return this.getContaReceber().getValorRecebido();
        } else {
            return 0.0;
        }
    }

    public Double getValorPagoVctoSemDescontoProgressivo() {
        if ((this.getContaReceber() == null) || (this.getContaReceber().getCodigo().equals(0))) {
            return 0.0;
        }
        if (this.getContaReceber().getSituacaoEQuitada()) {
            return this.getContaReceber().getValorRecebidoSemDescontoProgressivo();
        } else {
            return 0.0;
        }
    }

	public Double getValorReferenciaTituloPago() {
		if (Uteis.isAtributoPreenchido(getContaReceber()) && getContaReceber().getSituacaoEQuitada()) {
			if (!Uteis.isAtributoPreenchido(getContaReceber().getValor()) && !Uteis.isAtributoPreenchido(getContaReceber().getValorDescontoCalculado())) {
				return getContaReceber().getValor() + getContaReceber().getValorCusteadoContaReceber();
			}
			if (Uteis.isAtributoPreenchido(getContaReceber().getValorBaseContaReceber())) {
				return getContaReceber().getValorBaseContaReceber();
			}
			return getContaReceber().getValor() + getContaReceber().getValorCusteadoContaReceber();
		}
		return 0.0;
	}
    
    public boolean getPermitirRecebimentoContaReceber() {
        if (this.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA)) {
            return true;
        }
        return false;
    }

    /**
     * @return the editarMatriculaPeriodoVencimento
     */
    public boolean getEditarMatriculaPeriodoVencimento() {
        if (this.getContaReceber().getSituacaoEQuitada()) {
            return false;
        } else {
            return true;
        }
    }

    public static void validarLeiauteParcela(String parcela) throws ConsistirException {
        int barra = parcela.indexOf("/");

        if (barra > 0) {
            String parcelaAtual = parcela.substring(0, barra);
            String parcelaTotal = parcela.substring(barra + 1, parcela.length());
            try {
                Integer.parseInt(parcelaAtual);
                Integer.parseInt(parcelaTotal);
            } catch (Exception e) {
                throw new ConsistirException("A PARCELA digitada não é válida.");
            }
        } else {
            throw new ConsistirException("A PARCELA digitada não é válida.");
        }
    }

    /**
     * @return the validarParcela
     */
    public boolean getValidarParcela() {
        return validarParcela;
    }

    /**
     * @param validarParcela
     *            the validarParcela to set
     */
    public void setValidarParcela(boolean validarParcela) {
        this.validarParcela = validarParcela;
    }

    public String toString() {
        return "MatriculaPeriodoVencimento: " + this.getCodigo() + " Data Vcto: " + this.getDataVencimento_Apresentar()
                + " Valor: " + this.getValor() + " Parc" + getParcela() +  " Conta a Receber: " + this.getContaReceber().getCodigo();
    }

    /**
     * @return the matriculaPeriodoVO
     */
    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
    	if(matriculaPeriodoVO == null){
    		matriculaPeriodoVO = new MatriculaPeriodoVO();
    	}
        return matriculaPeriodoVO;
    }

    /**
     * @param matriculaPeriodoVO the matriculaPeriodoVO to set
     */
    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    /**
     * @return the controleGeracaoParcelaMesAMes
     */
    public String getControleGeracaoParcelaMesAMes() {
        return controleGeracaoParcelaMesAMes;
    }

    /**
     * @param controleGeracaoParcelaMesAMes the controleGeracaoParcelaMesAMes to set
     */
    public void setControleGeracaoParcelaMesAMes(String controleGeracaoParcelaMesAMes) {
        this.controleGeracaoParcelaMesAMes = controleGeracaoParcelaMesAMes;
    }

    /**
     * @return the erroGeracaoParcelaMesAMes
     */
    public String getErroGeracaoParcelaMesAMes() {
        return erroGeracaoParcelaMesAMes;
    }

    /**
     * @param erroGeracaoParcelaMesAMes the erroGeracaoParcelaMesAMes to set
     */
    public void setErroGeracaoParcelaMesAMes(String erroGeracaoParcelaMesAMes) {
        this.erroGeracaoParcelaMesAMes = erroGeracaoParcelaMesAMes;
    }

    public String getBaseOrdenacao() {
        return this.getErroGeracaoParcelaMesAMes() + this.getDataVencimento_Apresentar();
    }

    public void setDiasVariacaoDataVencimento(Integer diasVariacaoDataVencimento) {
        this.diasVariacaoDataVencimento = diasVariacaoDataVencimento;
    }

    public Integer getDiasVariacaoDataVencimento() {
        if (diasVariacaoDataVencimento == null) {
            diasVariacaoDataVencimento = 0;
        }
        return diasVariacaoDataVencimento;
    }

    public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
        if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
            valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        if (valorDescontoCalculadoPrimeiraFaixaDescontos < 0.0) {
        	valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
        this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public Double getValorDescontoCalculadoSegundaFaixaDescontos() {
        if (valorDescontoCalculadoSegundaFaixaDescontos == null) {
            valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
        }
        if (valorDescontoCalculadoSegundaFaixaDescontos < 0.0) {
        	valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public void setValorDescontoCalculadoSegundaFaixaDescontos(Double valorDescontoCalculadoSegundaFaixaDescontos) {
        this.valorDescontoCalculadoSegundaFaixaDescontos = valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public Double getValorDescontoCalculadoTerceiraFaixaDescontos() {
        if (valorDescontoCalculadoTerceiraFaixaDescontos == null) {
            valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
        }
        if (valorDescontoCalculadoTerceiraFaixaDescontos < 0.0) {
        	valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoTerceiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoTerceiraFaixaDescontos(Double valorDescontoCalculadoTerceiraFaixaDescontos) {
        this.valorDescontoCalculadoTerceiraFaixaDescontos = valorDescontoCalculadoTerceiraFaixaDescontos;
    }

    public Double getValorDescontoCalculadoQuartaFaixaDescontos() {
        if (valorDescontoCalculadoQuartaFaixaDescontos == null) {
            valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
        }
        if (valorDescontoCalculadoQuartaFaixaDescontos < 0.0) {
        	valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoQuartaFaixaDescontos;
    }

    public void setValorDescontoCalculadoQuartaFaixaDescontos(Double valorDescontoCalculadoQuartaFaixaDescontos) {
        this.valorDescontoCalculadoQuartaFaixaDescontos = valorDescontoCalculadoQuartaFaixaDescontos;
    }

	public Date getDataCompetencia() {
		if (dataCompetencia == null) {
			dataCompetencia = new Date();
		}
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public Boolean getFinanceiroManual() {
		if (financeiroManual == null) {
			financeiroManual = Boolean.FALSE;
		}
		return financeiroManual;
	}

	public void setFinanceiroManual(Boolean financeiroManual) {
		this.financeiroManual = financeiroManual;
	}

	public boolean isUsaValorPrimeiraParcela() {
		return usaValorPrimeiraParcela;
	}

	public void setUsaValorPrimeiraParcela(boolean usaValorPrimeiraParcela) {
		this.usaValorPrimeiraParcela = usaValorPrimeiraParcela;
	}

	public Integer getGeracaoManualParcela() {		
		return geracaoManualParcela;
	}

	public void setGeracaoManualParcela(Integer geracaoManualParcela) {
		this.geracaoManualParcela = geracaoManualParcela;
	}

	public Boolean getErroGeracaoParcela() {
		if (erroGeracaoParcela == null) {
			erroGeracaoParcela = false;
		}
		return erroGeracaoParcela;
	}

	public void setErroGeracaoParcela(Boolean erroGeracaoParcela) {
		this.erroGeracaoParcela = erroGeracaoParcela;
	}
	

	public String getDescricaoParcela() {
		return descricaoParcela;
	}

	public void setDescricaoParcela(String descricaoParcela) {
		this.descricaoParcela = descricaoParcela;
	}

	public String getMensagemErroGeracaoParcela() {
		if (mensagemErroGeracaoParcela == null) {
			mensagemErroGeracaoParcela = "";
		}
		return mensagemErroGeracaoParcela;
	}

	public void setMensagemErroGeracaoParcela(String mensagemErroGeracaoParcela) {
		this.mensagemErroGeracaoParcela = mensagemErroGeracaoParcela;
	}

	public Double getAcrescimoExtra() {
		if(acrescimoExtra == null){
			acrescimoExtra = 0.0;
		}
		return acrescimoExtra;
	}

	public void setAcrescimoExtra(Double acrescimoExtra) {
		this.acrescimoExtra = acrescimoExtra;
	}

	public String getDescricaoDesconto() {
		if (descricaoDesconto == null) {
			descricaoDesconto = "";
		}
		return descricaoDesconto;
	}

	public void setDescricaoDesconto(String descricaoDesconto) {
		this.descricaoDesconto = descricaoDesconto;
	}	
	
}
