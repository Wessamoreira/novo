package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.MovimentacaoFinanceira;

/**
 * Reponsável por manter os dados da entidade MovimentacaoFinanceiraItem. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see MovimentacaoFinanceira
 */
public class MovimentacaoFinanceiraItemVO extends SuperVO {

    private Integer codigo;
    private Integer movimentacaoFinanceira;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ChqRLog </code>.
     */
    private ChequeVO cheque;
    private Double valor;
    private FormaPagamentoVO formaPagamento;
    public static final long serialVersionUID = 1L;
    private boolean contaReceberRecebidaOuNegociada = false;

    /**
     * Construtor padrão da classe <code>MovimentacaoFinanceiraItem</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public MovimentacaoFinanceiraItemVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MovimentacaoFinanceiraItemVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MovimentacaoFinanceiraItemVO obj) throws ConsistirException {

        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getFormaPagamento() == null || obj.getFormaPagamento().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo TIPO MOVIMENTAÇÃO FINANCEIRA (Item Movimentação Financeira) deve ser informado.");
        }
        if (obj.getFormaPagamento().getTipo().equals("CH")) {
            if ((obj.getCheque() == null) || (obj.getCheque().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo CHEQUE (Item Movimentação Financeira) deve ser informado.");
            }
        }
        if ((obj.getValor() == null) || (obj.getValor().doubleValue() == 0)) {
            throw new ConsistirException("O campo VALOR (Item Movimentação Financeira) deve ser informado.");
        }

    }

    public Boolean getDinheiro() {
        if (getFormaPagamento().getTipo().equals("DI")) {
            return true;
        }
        return false;
    }

    public Boolean getExisteCheque() {
        if (getFormaPagamento().getTipo().equals("CH")) {
            return true;
        }
        return false;
    }

    /**
     * Retorna o objeto da classe <code>ChqRLog</code> relacionado com (
     * <code>MovimentacaoFinanceiraItem</code>).
     */
    public ChequeVO getCheque() {
        if (cheque == null) {
            cheque = new ChequeVO();
        }
        return (cheque);
    }

    /**
     * Define o objeto da classe <code>ChqRLog</code> relacionado com (
     * <code>MovimentacaoFinanceiraItem</code>).
     */
    public void setCheque(ChequeVO cheque) {
        this.cheque = cheque;
    }

    public Integer getMovimentacaoFinanceira() {
        if (movimentacaoFinanceira == null) {
            movimentacaoFinanceira = 0;
        }
        return movimentacaoFinanceira;
    }

    public void setMovimentacaoFinanceira(Integer movimentacaoFinanceira) {
        this.movimentacaoFinanceira = movimentacaoFinanceira;
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

    public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public FormaPagamentoVO getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = new FormaPagamentoVO();
        }
        return formaPagamento;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

	public boolean isContaReceberRecebidaOuNegociada() {
		return contaReceberRecebidaOuNegociada;
	}

	public void setContaReceberRecebidaOuNegociada(boolean contaReceberRecebidaOuNegociada) {
		this.contaReceberRecebidaOuNegociada = contaReceberRecebidaOuNegociada;
	}
}
