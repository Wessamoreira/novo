package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.NegociacaoPagamento;

/**
 * Reponsável por manter os dados da entidade ContaPagarNegociacaoPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see NegociacaoPagamento
 */
public class ContaPagarNegociacaoPagamentoVO extends SuperVO {

    private Integer codigo;
    private Integer negociacaoContaPagar;
    private Double valorContaPagar;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaPagar </code>.
     */
    private ContaPagarVO contaPagar;
    public static final long serialVersionUID = 1L;
    
    private Double valorTotal;    
    private String nomeResponsavel;
    private String nomeAluno;
    private String matricula;
    private String turma;
    private Integer codigoNegociacaoPagamento;
    private String valorTotalPagamentoPorExtenso;
    private Double valorTotalPagamento;

    /**
     * Construtor padrão da classe <code>ContaPagarNegociacaoPagamento</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public ContaPagarNegociacaoPagamentoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ContaPagarNegociacaoPagamentoVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ContaPagarNegociacaoPagamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getContaPagar() == null) || (obj.getContaPagar().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA PAGAR (Conta Pagar Negociação) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
    }

    /**
     * Retorna o objeto da classe <code>ContaPagar</code> relacionado com (
     * <code>ContaPagarNegociacaoPagamento</code>).
     */
    public ContaPagarVO getContaPagar() {
        if (contaPagar == null) {
            contaPagar = new ContaPagarVO();
        }
        return (contaPagar);
    }

    /**
     * Define o objeto da classe <code>ContaPagar</code> relacionado com (
     * <code>ContaPagarNegociacaoPagamento</code>).
     */
    public void setContaPagar(ContaPagarVO obj) {
        this.contaPagar = obj;
    }

    public Double getValorContaPagar() {
        if (valorContaPagar == null) {
            valorContaPagar = 0.0;
        }
        return (valorContaPagar);
    }

    public void setValorContaPagar(Double valorContaPagar) {
        this.valorContaPagar = valorContaPagar;
    }

    public Integer getNegociacaoContaPagar() {
        if (negociacaoContaPagar == null) {
            negociacaoContaPagar = 0;
        }
        return (negociacaoContaPagar);
    }

    public void setNegociacaoContaPagar(Integer negociacaoContaPagar) {
        this.negociacaoContaPagar = negociacaoContaPagar;
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

    public Double getValorPrevisaoPagamento() {
        return this.getContaPagar().getValorPrevisaoPagamento();
    }

	public Double getValorTotal() {
		return valorTotal;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getTurma() {
		return turma;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getCodigoNegociacaoPagamento() {
		return codigoNegociacaoPagamento;
	}

	public String getValorTotalPagamentoPorExtenso() {
		return valorTotalPagamentoPorExtenso;
	}

	public Double getValorTotalPagamento() {
		return valorTotalPagamento;
	}

	public void setCodigoNegociacaoPagamento(Integer codigoNegociacaoPagamento) {
		this.codigoNegociacaoPagamento = codigoNegociacaoPagamento;
	}

	public void setValorTotalPagamentoPorExtenso(String valorTotalPagamentoPorExtenso) {
		this.valorTotalPagamentoPorExtenso = valorTotalPagamentoPorExtenso;
	}

	public void setValorTotalPagamento(Double valorTotalPagamento) {
		this.valorTotalPagamento = valorTotalPagamento;
	}

}
