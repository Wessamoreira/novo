package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;
import negocio.facade.jdbc.financeiro.FluxoCaixa;

/**
 * Reponsável por manter os dados da entidade MovimentacaoCaixa. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see FluxoCaixa
 */
public class MovimentacaoCaixaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private UsuarioVO responsavel;
    private String tipoMovimentacao;
    private Integer codigoOrigem;
    private String tipoOrigem;
    private Double valor;
    private Integer fluxoCaixa;
    private PessoaVO pessoa;
    private FornecedorVO fornecedor;
    private OperadoraCartaoVO operadoraCartaoVO;
    private BancoVO bancoVO;
    private String banco;    
    private String numeroCheque;
    private Integer cheque;
    private String agenciaCheque;
    private String contaCorrenteCheque;
    private String emitenteCheque;
    private String sacadoCheque;
    private String cpfCnpjCheque;
    private Date dataPrevisaoCheque;
    private Boolean pessoaJuridicaCheque;
    private ParceiroVO parceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>FormaPagamento </code>.
     */
    private FormaPagamentoVO formaPagamento;
    private String descricaoMovimentacaoFinanceira;    
    private String situacaoCheque;
    public static final long serialVersionUID = 1L;
    
    
    /**
     * Construtor padrão da classe <code>MovimentacaoCaixa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public MovimentacaoCaixaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MovimentacaoCaixaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MovimentacaoCaixaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Movimentação do Caixa) deve ser informado.");
        }
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo RESPONSÁVEL (Movimentação do Caixa) deve ser informado.");
        }
        if (obj.getTipoMovimentacao().equals("")) {
            throw new ConsistirException("O campo TIPO MOVIMENTAÇÃO (Movimentação do Caixa) deve ser informado.");
        }
        if (obj.getCodigoOrigem().intValue() == 0) {
            throw new ConsistirException("O campo CÓDIGO ORIGEM (Movimentação do Caixa) deve ser informado.");
        }
        if (obj.getTipoOrigem().equals("")) {
            throw new ConsistirException("O campo TIPO ORIGEM (Movimentação do Caixa) deve ser informado.");
        }
        if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FORMA PAGAMENTO (Movimentação do Caixa) deve ser informado.");
        }
        if (obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Movimentação do Caixa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setTipoMovimentacao(tipoMovimentacao.toUpperCase());
        setTipoOrigem(tipoOrigem.toUpperCase());
    }

    public String getSacado() {
        if (getFornecedor().getCodigo().intValue() != 0) {
            return getFornecedor().getNome();
        }
        if (getPessoa().getCodigo().intValue() != 0) {
            return getPessoa().getNome();
        }
        if (getParceiro().getCodigo().intValue() != 0) {
            return getParceiro().getNome();
        }
        if (getOperadoraCartaoVO().getCodigo().intValue() != 0) {
            return getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar();
        }
        if (getBancoVO().getCodigo().intValue() != 0) {
            return getBancoVO().getNome();
        }
        return "";
    }

    /**
     * Retorna o objeto da classe <code>FormaPagamento</code> relacionado com (
     * <code>MovimentacaoCaixa</code>).
     */
    public FormaPagamentoVO getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = new FormaPagamentoVO();
        }
        return (formaPagamento);
    }

    /**
     * Define o objeto da classe <code>FormaPagamento</code> relacionado com (
     * <code>MovimentacaoCaixa</code>).
     */
    public void setFormaPagamento(FormaPagamentoVO obj) {
        this.formaPagamento = obj;
    }

    public Integer getFluxoCaixa() {
        if (fluxoCaixa == null) {
            fluxoCaixa = 0;
        }
        return (fluxoCaixa);
    }

    public void setFluxoCaixa(Integer fluxoCaixa) {
        this.fluxoCaixa = fluxoCaixa;
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

    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return (tipoOrigem);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoOrigem_Apresentar() {
        return TipoOrigemMovimentacaoCaixa.getDescricao(tipoOrigem);
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public Integer getCodigoOrigem() {
        if (codigoOrigem == null) {
            codigoOrigem = 0;
        }
        return (codigoOrigem);
    }

    public void setCodigoOrigem(Integer codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    public String getTipoMovimentacao() {
        if (tipoMovimentacao == null) {
            tipoMovimentacao = "";
        }
        return (tipoMovimentacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoMovimentacao_Apresentar() {
        if (tipoMovimentacao.equals("SA")) {
            return "Saída";
        }
        if (tipoMovimentacao.equals("EN")) {
            return "Entrada";
        }
        return (tipoMovimentacao);
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
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
        return (Uteis.getDataComHora(data));
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

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return fornecedor;
    }

    public void setFornecedor(FornecedorVO fornecedor) {
        this.fornecedor = fornecedor;
    }

    

    /**
     * @return the descricaoMovimentacaoFinanceira
     */
    public boolean getApresentarDescricaoMovimentacaoFinanceira() {
        if (descricaoMovimentacaoFinanceira == null || descricaoMovimentacaoFinanceira.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public String getDescricaoMovimentacaoFinanceira() {
        if (descricaoMovimentacaoFinanceira == null) {
            return "";
        }
        return descricaoMovimentacaoFinanceira;
    }

    /**
     * @param descricaoMovimentacaoFinanceira the descricaoMovimentacaoFinanceira to set
     */
    public void setDescricaoMovimentacaoFinanceira(String descricaoMovimentacaoFinanceira) {
        this.descricaoMovimentacaoFinanceira = descricaoMovimentacaoFinanceira;
    }

    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }
    
    
    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
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

    public String getNumeroCheque() {
        if (numeroCheque == null) {
            numeroCheque = "";
        }
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

	public Integer getCheque() {		
		return cheque;
	}

	public void setCheque(Integer cheque) {
		this.cheque = cheque;
	}

	public String getAgenciaCheque() {
		if (agenciaCheque == null) {
			agenciaCheque = "";
		}
		return agenciaCheque;
	}

	public void setAgenciaCheque(String agenciaCheque) {
		this.agenciaCheque = agenciaCheque;
	}

	public String getContaCorrenteCheque() {
		if (contaCorrenteCheque == null) {
			contaCorrenteCheque = "";
		}
		return contaCorrenteCheque;
	}

	public void setContaCorrenteCheque(String contaCorrenteCheque) {
		this.contaCorrenteCheque = contaCorrenteCheque;
	}

	public String getEmitenteCheque() {
		if (emitenteCheque == null) {
			emitenteCheque = "";
		}
		return emitenteCheque;
	}

	public void setEmitenteCheque(String emitenteCheque) {
		this.emitenteCheque = emitenteCheque;
	}

	public String getSacadoCheque() {
		if (sacadoCheque == null) {
			sacadoCheque = "";
		}
		return sacadoCheque;
	}

	public void setSacadoCheque(String sacadoCheque) {
		this.sacadoCheque = sacadoCheque;
	}

	public Date getDataPrevisaoCheque() {		
		return dataPrevisaoCheque;
	}

	public void setDataPrevisaoCheque(Date dataPrevisaoCheque) {
		this.dataPrevisaoCheque = dataPrevisaoCheque;
	}

	public String getCpfCnpjCheque() {
		if (cpfCnpjCheque == null) {
			cpfCnpjCheque = "";
		}
		return cpfCnpjCheque;
	}

	public void setCpfCnpjCheque(String cpfCnpjCheque) {
		this.cpfCnpjCheque = cpfCnpjCheque;
	}

	public Boolean getPessoaJuridicaCheque() {
		if (pessoaJuridicaCheque == null) {
			pessoaJuridicaCheque = false;
		}
		return pessoaJuridicaCheque;
	}

	public void setPessoaJuridicaCheque(Boolean pessoaJuridicaCheque) {
		this.pessoaJuridicaCheque = pessoaJuridicaCheque;
	}
    
		
	public String getSituacaoCheque() {
		if (situacaoCheque == null) {
			situacaoCheque = "";
		}
		return situacaoCheque;
	}

	public void setSituacaoCheque(String situacaoCheque) {
		this.situacaoCheque = situacaoCheque;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}
    
}
