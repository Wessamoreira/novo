package negocio.comuns.financeiro;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;

/**
 * Reponsï¿½vel por manter os dados da entidade ChqRLog. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os mï¿½todos de acesso a estes atributos. Classe utilizada para apresentar
 * e manter em memï¿½ria os dados desta entidade.
 *
 * @see SuperVO
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ChequeVO extends SuperVO {

    private Integer codigo;
    private String sacado;
    private Boolean emitentePessoaJuridica;
    private String cpf;
    private String cnpj;
	private String numero;
    // Nao persistir esse campo, usado apenas a nivel de tela ( negociacaoRecebimentoForm.jsp )
    private String numeroFinal;
    private Double valor;
    private Double valorUsadoRecebimento;
    private Date dataEmissao;
    private Date dataPrevisao;
    private Boolean pago;
    private Boolean chequeProprio;
    private String situacao;
    private Integer recebimento;
    private Integer pagamento;
    private UnidadeEnsinoVO unidadeEnsino;
    private String matriculaAluno;
    private String nomeAluno;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>pessoa </code>.
     */
    private PessoaVO pessoa;
    // /** Atributo responsï¿½vel por manter o objeto relacionado da classe
    // <code>Banco </code>.*/
    // private BancoVO banco;
    // /** Atributo responsï¿½vel por manter o objeto relacionado da classe
    // <code>Agencia </code>.*/
    // private AgenciaVO agencia;
    private String banco;
    private String agencia;
    private String numeroContaCorrente;
    private ContaCorrenteVO contaCorrente;
    private ContaCorrenteVO localizacaoCheque;
    private Double tarifaAntecipacao;
    private Double valorDescontoAntecipacao;
    private Date dataAntecipacao;
    private Date dataBaixa;
    private UsuarioVO responsavelBaixa;
    private ParceiroVO parceiro;
    private FornecedorVO fornecedor;
    //esse campo não deve ser persistido, usado apenas para a impressão de cheques / NegociacaoPagamentoControle.java
    private String valorPorExtenso;
    /**
     * Transiente
     */
    private Boolean selecionado;
    private String situacaoChequeApresentarAluno;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrï¿½o da classe <code>ChqRLog</code>. Cria uma nova
     * instï¿½ncia desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ChequeVO() {
        super();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>CensoVO</code>.
     */
    public static void validarUnicidade(List<ChequeVO> lista, ChequeVO obj) throws ConsistirException {
        for (ChequeVO repetido : lista) {
            if (repetido.getBanco().equals(obj.getBanco())) {
                if (repetido.getAgencia().equals(obj.getAgencia())) {
                    if (repetido.getNumeroContaCorrente().equals(obj.getNumeroContaCorrente())) {
                        if (repetido.getNumero().equals(obj.getNumero())) {
                            throw new ConsistirException("O cheque N° " + repetido.getNumero() + " já está cadastrado!");
                        }
                    }
                }
            }
        }
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por validar os dados de um objeto da classe
     * <code>ChqRLogVO</code>. Todos os tipos de consistï¿½ncia de dados sï¿½o e
     * devem ser implementadas neste mï¿½todo. Sï¿½o validaï¿½ï¿½es tï¿½picas:
     * verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de valores
     * vï¿½lidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistï¿½ncia for encontrada aumaticamente ï¿½
     *                gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ChequeVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (!obj.getChequeProprio()) {
            if (((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0))
                    && ((obj.getParceiro() == null) || (obj.getParceiro().getCodigo().intValue() == 0))
                    && ((obj.getFornecedor() == null) || (obj.getFornecedor().getCodigo().intValue() == 0))) {
                throw new ConsistirException("O campo PESSOA (Cheque) deve ser informado.");
            }
            if ((obj.getBanco() == null) || (obj.getBanco().equals(""))) {
                throw new ConsistirException("O campo BANCO (Cheque) deve ser informado.");
            }
            if ((obj.getAgencia() == null) || (obj.getAgencia().equals(""))) {
                throw new ConsistirException("O campo AGÊNCIA (Cheque) deve ser informado.");
            }
            if ((obj.getNumeroContaCorrente() == null) || (obj.getNumeroContaCorrente().equals(""))) {
                throw new ConsistirException("O campo CONTA CORRENTE (Cheque) deve ser informado.");
            }
            if (obj.getSacado().equals("")) {
                throw new ConsistirException("O campo SACADO (Cheque) deve ser informado.");
            }
        }
        if (obj.getChequeProprio()) {
            if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
                throw new ConsistirException("O campo CONTA CORRENTE (Cheque) deve ser informado.");
            }
        }
        if (obj.getNumero().equals("")) {
            throw new ConsistirException("O campo NÚMERO (Cheque) deve ser informado.");
        }
        if (obj.getDataEmissao() == null) {
            throw new ConsistirException("O campo DATA EMISSÃO (Cheque) deve ser informado.");
        }
        if (obj.getDataPrevisao() == null) {
            throw new ConsistirException("O campo DATA PREVISÃO (Cheque) deve ser informado.");
        }
        if (obj.getValor() == null || obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Cheque) deve ser informado.");
        }
        obj.setValorUsadoRecebimento(obj.getValor());
    }

    public MapaLancamentoFuturoVO criarMapaLancamentoFuturo(Integer codigoOrigem, String tipoOrigem, String tipoCheque, UsuarioVO responsavel) throws Exception {
        if (getCodigo().intValue() == 0) {
            throw new ConsistirException("O código do cheque deve ser incluido antes de criar a pendencia.");
        }
        MapaLancamentoFuturoVO mapaLancamentoFuturoVO = new MapaLancamentoFuturoVO();
        mapaLancamentoFuturoVO.setBanco(getBanco());
        mapaLancamentoFuturoVO.setCodigoCheque(getCodigo());
        mapaLancamentoFuturoVO.setCodigoOrigem(codigoOrigem);
        mapaLancamentoFuturoVO.setDataEmissao(getDataEmissao());
        mapaLancamentoFuturoVO.setDataPrevisao(getDataPrevisao());
        mapaLancamentoFuturoVO.setNumeroCheque(getNumero());
        mapaLancamentoFuturoVO.setResponsavel(responsavel);
        mapaLancamentoFuturoVO.setSacado(getSacado());
        mapaLancamentoFuturoVO.setTipoOrigem(tipoOrigem);
        mapaLancamentoFuturoVO.setTipoMapaLancamentoFuturo(tipoCheque);
        mapaLancamentoFuturoVO.setValor(getValor());
        return mapaLancamentoFuturoVO;
    }

    public void preencherDadosDoBanco() throws Exception {
        if (getContaCorrente() == null || getContaCorrente().getCodigo() == 0) {
            throw new ConsistirException("Desenvolvedor, ainda não foi informado conta corrente para o cheque.");
        }
        setBanco(getContaCorrente().getAgencia().getBanco().getNome());
        setAgencia(getContaCorrente().getAgencia().getNumeroAgencia() + "-"
                + getContaCorrente().getAgencia().getDigito());
        setNumeroContaCorrente(getContaCorrente().getNumero());
    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setSacado(sacado.toUpperCase());
        setNumero(numero.toUpperCase());
        setSituacao(situacao.toUpperCase());
        setBanco(banco.toUpperCase());
        setAgencia(agencia.toUpperCase());
        setNumeroContaCorrente(numeroContaCorrente.toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>pessoa</code> relacionado com (
     * <code>ChqRLog</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>pessoa</code> relacionado com (
     * <code>ChqRLog</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    public Integer getPagamento() {
        if (pagamento == null) {
            pagamento = 0;
        }
        return pagamento;
    }

    public void setPagamento(Integer pagamento) {
        this.pagamento = pagamento;
    }

    public Integer getRecebimento() {
        if (recebimento == null) {
            recebimento = 0;
        }
        return recebimento;
    }

    public void setRecebimento(Integer recebimento) {
        this.recebimento = recebimento;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = null;
        }
        return (situacao);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar o valor de apresentaï¿½ï¿½o de um
     * atributo com um domï¿½nio especï¿½fico. Com base no valor de
     * armazenamento do atributo esta funï¿½ï¿½o ï¿½ capaz de retornar o de
     * apresentaï¿½ï¿½o correspondente. ï¿½til para campos como sexo,
     * escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        return SituacaoCheque.getDescricao(situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getChequeProprio() {
        if (chequeProprio == null) {
            chequeProprio = Boolean.TRUE;
        }
        return (chequeProprio);
    }

    public void setChequeProprio(Boolean chequeProprio) {
        this.chequeProprio = chequeProprio;
    }

    public Boolean getPago() {
        if (pago == null) {
            pago = Boolean.FALSE;
        }
        return (pago);
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Date getDataPrevisao() {
        if (dataPrevisao == null) {
            dataPrevisao = new Date();
        }
        return (dataPrevisao);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar um atributo do tipo data no
     * formato padrï¿½o dd/mm/aaaa.
     */
    public String getDataPrevisao_Apresentar() {
        return (Uteis.getData(dataPrevisao));
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public Date getDataEmissao() {
        if (dataEmissao == null) {
            dataEmissao = new Date();
        }
        return (dataEmissao);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar um atributo do tipo data no
     * formato padrï¿½o dd/mm/aaaa.
     */
    public String getDataEmissao_Apresentar() {
        return (Uteis.getData(dataEmissao));
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNumero() {
        if (numero == null) {
            numero = "";
        }
        return (numero);
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSacado() {
        if (sacado == null) {
            sacado = "";
        }
        return (sacado);
    }

    public void setSacado(String sacado) {
        this.sacado = sacado;
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

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public ContaCorrenteVO getLocalizacaoCheque() {
        if (localizacaoCheque == null) {
            localizacaoCheque = new ContaCorrenteVO();
        }
        return localizacaoCheque;
    }

    public void setLocalizacaoCheque(ContaCorrenteVO localizacaoCheque) {
        this.localizacaoCheque = localizacaoCheque;
    }

    public String getAgencia() {
        if (agencia == null) {
            agencia = "";
        }
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getBanco() {
        if (banco == null) {
            banco = "";
        }
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Double getValorUsadoRecebimento() {
        if (valorUsadoRecebimento == null) {
            valorUsadoRecebimento = 0.0;
        }
        return valorUsadoRecebimento;
    }

    public void setValorUsadoRecebimento(Double valorUsadoRecebimento) {
        this.valorUsadoRecebimento = valorUsadoRecebimento;
    }

    public String getNumeroContaCorrente() {
        if (numeroContaCorrente == null) {
            numeroContaCorrente = "";
        }
        return numeroContaCorrente;
    }

    public void setNumeroContaCorrente(String numeroContaCorrente) {
        this.numeroContaCorrente = numeroContaCorrente;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the tarifaAntecipacao
     */
    public Double getTarifaAntecipacao() {
        if (tarifaAntecipacao == null) {
            tarifaAntecipacao = 0.0;
        }
        return tarifaAntecipacao;
    }

    /**
     * @param tarifaAntecipacao
     *            the tarifaAntecipacao to set
     */
    public void setTarifaAntecipacao(Double tarifaAntecipacao) {
        this.tarifaAntecipacao = tarifaAntecipacao;
    }

    /**
     * @return the valorDescontoAntecipacao
     */
    public Double getValorDescontoAntecipacao() {
        if (valorDescontoAntecipacao == null) {
            valorDescontoAntecipacao = 0.0;
        }
        return valorDescontoAntecipacao;
    }

    /**
     * @param valorDescontoAntecipacao
     *            the valorDescontoAntecipacao to set
     */
    public void setValorDescontoAntecipacao(Double valorDescontoAntecipacao) {
        this.valorDescontoAntecipacao = valorDescontoAntecipacao;
    }

    /**
     * @return the dataAntecipacao
     */
    public Date getDataAntecipacao() {
        if (dataAntecipacao == null) {
            dataAntecipacao = new Date();
        }
        return dataAntecipacao;
    }

    /**
     * @param dataAntecipacao
     *            the dataAntecipacao to set
     */
    public void setDataAntecipacao(Date dataAntecipacao) {
        this.dataAntecipacao = dataAntecipacao;
    }

    /**
     * @return the dataBaixa
     */
    public Date getDataBaixa() {
        if (dataBaixa == null) {
            dataBaixa = new Date();
        }
        return dataBaixa;
    }

    /**
     * @param dataBaixa
     *            the dataBaixa to set
     */
    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    /**
     * @return the responsavelBaixa
     */
    public UsuarioVO getResponsavelBaixa() {
        if (responsavelBaixa == null) {
            responsavelBaixa = new UsuarioVO();
        }
        return responsavelBaixa;
    }

    /**
     * @param responsavelBaixa
     *            the responsavelBaixa to set
     */
    public void setResponsavelBaixa(UsuarioVO responsavelBaixa) {
        this.responsavelBaixa = responsavelBaixa;
    }

    /**
     * @return the parceiro
     */
    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    /**
     * @param parceiro
     *            the parceiro to set
     */
    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    /**
     * @return the numeroFinal
     */
    public String getNumeroFinal() {
        if (numeroFinal == null) {
            numeroFinal = "";
        }
        return numeroFinal;
    }

    /**
     * @param numeroFinal the numeroFinal to set
     */
    public void setNumeroFinal(String numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

	public FornecedorVO getFornecedor() {
		if(fornecedor == null){
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
    public Boolean getEmitentePessoaJuridica() {
    	if (emitentePessoaJuridica == null) {
    		emitentePessoaJuridica = false;
    	}
		return emitentePessoaJuridica;
	}

	public void setEmitentePessoaJuridica(Boolean emitentePessoaJuridica) {
		this.emitentePessoaJuridica = emitentePessoaJuridica;
	}

	public String getCnpj() {
		if (cnpj == null) {
			cnpj = "";
		}
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getValorPorExtenso() {
		if(valorPorExtenso == null){
			valorPorExtenso = "";
		}
		return valorPorExtenso;
	}

	public void setValorPorExtenso(String valorPorExtenso) {
		this.valorPorExtenso = valorPorExtenso;
	}
	
	public String getSituacaoChequeApresentarAluno() {
		if(situacaoChequeApresentarAluno == null){
			situacaoChequeApresentarAluno = "";
		}
		return situacaoChequeApresentarAluno;
	}

	public void setSituacaoChequeApresentarAluno(String situacaoChequeApresentarAluno) {
		this.situacaoChequeApresentarAluno = situacaoChequeApresentarAluno;
	}
    
    
}
