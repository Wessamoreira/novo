package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;

/**
 * Reponsável por manter os dados da entidade MovimentacaoFinanceira. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class MapaLancamentoFuturoVO extends SuperVO {

    private Integer codigo;
    private String sacado;
    private Integer codigoCheque;
    private String matriculaOrigem;
    private Double valorFinalCheque;
    private Double taxaDescontoCheque;
    private Double valorTaxaDescontoCheque;
    private Double valor;
    private String numeroCheque;
    private Date dataPrevisao;
    private Date dataEmissao;
    private Date dataPrevisaoFinal;
    private Date dataEmissaoFinal;
    private Date dataAntecipacao;
    private Date dataBaixa;
    private Integer codigoOrigem;
    private String banco;
    private String tipoOrigem;
    private Date dataAutorizacao;
    private UsuarioVO responsavel;
    private String tipoMapaLancamentoFuturo;
    private boolean baixarCheque;
    private boolean apresentarCamposAntecipacao;
    private Date dataReapresentacaoCheque1;
    private Date dataReapresentacaoCheque2;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MovimentacaoFinanceira</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public MapaLancamentoFuturoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MovimentacaoFinanceiraVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MapaLancamentoFuturoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        // if (obj.getData() == null) {
        // throw new
        // ConsistirException("O campo DATA (Movimentação Financeira) deve ser informado.");
        // }
        // if ((obj.getResponsavel() == null) ||
        // (obj.getResponsavel().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo RESPONSÁVEL (Movimentação Financeira) deve ser informado.");
        // }
        // if ((obj.getContaCorrenteOrigem() == null) ||
        // (obj.getContaCorrenteOrigem().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo CONTA CORRENTE ORIGEM (Movimentação Financeira) deve ser informado.");
        // }
        // if ((obj.getContaCorrenteDestino() == null) ||
        // (obj.getContaCorrenteDestino().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo CONTA CORRENTE DESTINO (Movimentação Financeira) deve ser informado.");
        // }
        // if(obj.getContaCorrenteDestino().getCodigo().intValue() ==
        // obj.getContaCorrenteOrigem().getCodigo()){
        // throw new
        // ConsistirException("O campo CONTA CORRENTE DESTINO (Movimentação Financeira) deve ser diferente de CONTA CORRENTE DESTINO (Movimentação Financeira).");
        // }
        // if(obj.getMovimentacaoFinanceiraItemVOs().isEmpty()){
        // throw new
        // ConsistirException("Movimentação Financeira Item (Movimentação Financeira) deve ser informado.");
        // }
    }

    public Boolean getEdicao() {
        if (getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>MovimentacaoFinanceira</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>MovimentacaoFinanceira</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
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

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAntecipacao_Apresentar() {
        return (Uteis.getData(dataAntecipacao));
    }

    public String getDataPrevisao_Apresentar() {
        return (Uteis.getData(dataPrevisao));
    }

    public String getDataEmissao_Apresentar() {
        return (Uteis.getData(dataEmissao));
    }

    public String getDataAutorizacao_Apresentar() {
        return (Uteis.getDataComHora(dataAutorizacao));
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

    /**
     * @return the sacado
     */
    public String getSacado() {
        if (sacado == null) {
            sacado = "";
        }
        return sacado;
    }

    /**
     * @param sacado
     *            the sacado to set
     */
    public void setSacado(String sacado) {
        this.sacado = sacado;
    }

    /**
     * @return the situacaoCheque
     */
    public Integer getCodigoCheque() {
        if (codigoCheque == null) {
            codigoCheque = 0;
        }
        return codigoCheque;
    }

    /**
     * @param situacaoCheque
     *            the situacaoCheque to set
     */
    public void setCodigoCheque(Integer codigoCheque) {
        this.codigoCheque = codigoCheque;
    }

    /**
     * @return the valorFinalCheque
     */
    public Double getValorFinalCheque() {
        if (valorFinalCheque == null) {
            valorFinalCheque = 0.0;
        }
        return valorFinalCheque;
    }

    /**
     * @param valorFinalCheque
     *            the valorFinalCheque to set
     */
    public void setValorFinalCheque(Double valorFinalCheque) {
        this.valorFinalCheque = valorFinalCheque;
    }

    /**
     * @return the taxaDescontoCheque
     */
    public Double getTaxaDescontoCheque() {
        if (taxaDescontoCheque == null) {
            taxaDescontoCheque = 0.0;
        }
        return taxaDescontoCheque;
    }

    /**
     * @param taxaDescontoCheque
     *            the taxaDescontoCheque to set
     */
    public void setTaxaDescontoCheque(Double taxaDescontoCheque) {
        this.taxaDescontoCheque = taxaDescontoCheque;
    }

    /**
     * @return the numeroCheque
     */
    public String getNumeroCheque() {
        if (numeroCheque == null) {
            numeroCheque = "";
        }
        return numeroCheque;
    }

    /**
     * @param numeroCheque
     *            the numeroCheque to set
     */
    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    /**
     * @return the dataPrevisao
     */
    public Date getDataPrevisao() {
//        if (dataPrevisao == null) {
//            dataPrevisao = new Date();
//        }
        return dataPrevisao;
    }

    /**
     * @param dataPrevisao
     *            the dataPrevisao to set
     */
    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    /**
     * @return the codigoOrigem
     */
    public Integer getCodigoOrigem() {
        if (codigoOrigem == null) {
            codigoOrigem = 0;
        }
        return codigoOrigem;
    }

    /**
     * @param codigoOrigem
     *            the codigoOrigem to set
     */
    public void setCodigoOrigem(Integer codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    /**
     * @return the tipoOrigem
     */
    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    /**
     * @param tipoOrigem
     *            the tipoOrigem to set
     */
    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    /**
     * @return the dataAutorizacao
     */
    public Date getDataAutorizacao() {
        if (dataAutorizacao == null) {
            dataAutorizacao = new Date();
        }
        return dataAutorizacao;
    }

    /**
     * @param dataAutorizacao
     *            the dataAutorizacao to set
     */
    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    /**
     * @return the codigoBanco
     */
    public String getBanco() {
        if (banco == null) {
            banco = "";
        }
        return banco;
    }

    /**
     * @param codigoBanco
     *            the codigoBanco to set
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @return the dataEmissao
     */
    public Date getDataEmissao() {
//        if (dataEmissao == null) {
//            dataEmissao = new Date();
//        }
        return dataEmissao;
    }

    /**
     * @param dataEmissao
     *            the dataEmissao to set
     */
    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    /**
     * @return the dataPrevisaoFinal
     */
    public Date getDataPrevisaoFinal() {
//        if (dataPrevisaoFinal == null) {
//            dataPrevisaoFinal = new Date();
//        }
        return dataPrevisaoFinal;
    }

    /**
     * @param dataPrevisaoFinal
     *            the dataPrevisaoFinal to set
     */
    public void setDataPrevisaoFinal(Date dataPrevisaoFinal) {
        this.dataPrevisaoFinal = dataPrevisaoFinal;
    }

    /**
     * @return the dataEmissaoFinal
     */
    public Date getDataEmissaoFinal() {
//        if (dataEmissaoFinal == null) {
//            dataEmissaoFinal = new Date();
//        }
        return dataEmissaoFinal;
    }

    /**
     * @param dataEmissaoFinal
     *            the dataEmissaoFinal to set
     */
    public void setDataEmissaoFinal(Date dataEmissaoFinal) {
        this.dataEmissaoFinal = dataEmissaoFinal;
    }

    /**
     * @return the baixarCheque
     */
    public boolean getApresentarCamposAntecipacao() {

        if (!this.baixarCheque) {
            return false;
        } else {
            if (this.getDataPrevisao().compareTo(new Date()) > 0) {
                if (!TipoMapaLancamentoFuturo.CHEQUE_A_PAGAR.getValor().equals(getTipoMapaLancamentoFuturo())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean getBaixarCheque() {
        return baixarCheque;
    }

    /**
     * @param baixarCheque
     *            the baixarCheque to set
     */
    public void setBaixarCheque(boolean baixarCheque) {
        this.baixarCheque = baixarCheque;
    }

    /**
     * @param baixarCheque
     *            the baixarCheque to set
     */
    public void isBaixarCheque(boolean baixarCheque) {
        this.baixarCheque = baixarCheque;
    }

    /**
     * @return the valorTaxaDescontoCheque
     */
    public Double getValorTaxaDescontoCheque() {
        if (valorTaxaDescontoCheque == null) {
            valorTaxaDescontoCheque = 0.0;
        }
        return valorTaxaDescontoCheque;
    }

    /**
     * @param valorTaxaDescontoCheque
     *            the valorTaxaDescontoCheque to set
     */
    public void setValorTaxaDescontoCheque(Double valorTaxaDescontoCheque) {
        this.valorTaxaDescontoCheque = valorTaxaDescontoCheque;
    }

    public void atualizarTaxa() {
        this.valorFinalCheque = new Double(this.valor.doubleValue() - this.valorTaxaDescontoCheque.doubleValue());
        this.taxaDescontoCheque = new Double((this.valorTaxaDescontoCheque.doubleValue() * 100)
                / this.valor.doubleValue());
    }

    public void atualizarValorTaxa() {
        this.valorFinalCheque = new Double(this.valor.doubleValue()
                - ((this.taxaDescontoCheque.doubleValue() / 100) * this.valor.doubleValue()));
        this.valorTaxaDescontoCheque = new Double(this.valor.doubleValue() - this.valorFinalCheque.doubleValue());
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
     * @param apresentarCamposAntecipacao
     *            the apresentarCamposAntecipacao to set
     */
    public void setApresentarCamposAntecipacao(boolean apresentarCamposAntecipacao) {
        this.apresentarCamposAntecipacao = apresentarCamposAntecipacao;
    }

    public String getTipoMapaLancamentoFuturo() {
        if (tipoMapaLancamentoFuturo == null) {
            tipoMapaLancamentoFuturo = "";
        }
        return tipoMapaLancamentoFuturo;
    }

    public void setTipoMapaLancamentoFuturo(String tipoMapaLancamentoFuturo) {
        this.tipoMapaLancamentoFuturo = tipoMapaLancamentoFuturo;
    }

    /**
     * @return the matriculaOrigem
     */
    public String getMatriculaOrigem() {
        if (matriculaOrigem == null) {
            matriculaOrigem = "";
        }
        return matriculaOrigem;
    }

    /**
     * @param matriculaOrigem
     *            the matriculaOrigem to set
     */
    public void setMatriculaOrigem(String matriculaOrigem) {
        this.matriculaOrigem = matriculaOrigem;
    }

	public Date getDataBaixa() {
		if (dataBaixa == null) {
			dataBaixa = new Date();
		}
		return dataBaixa;
	}
	
	public String getDataBaixa_Apresentar() {
		return Uteis.getData(dataBaixa);
	}

	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public Date getDataReapresentacaoCheque1() {
		return dataReapresentacaoCheque1;
	}

	public void setDataReapresentacaoCheque1(Date dataReapresentacaoCheque1) {
		this.dataReapresentacaoCheque1 = dataReapresentacaoCheque1;
	}

	public Date getDataReapresentacaoCheque2() {
		return dataReapresentacaoCheque2;
	}

	public void setDataReapresentacaoCheque2(Date dataReapresentacaoCheque2) {
		this.dataReapresentacaoCheque2 = dataReapresentacaoCheque2;
	}
}
