package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.compras.CondicaoPagamento;

/**
 * Reponsável por manter os dados da entidade ParcelaCondicaoPagamento. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see CondicaoPagamento
 */
public class ParcelaCondicaoPagamentoVO extends SuperVO {

    private Integer codigo;
    private Integer numeroParcela;
    private Double percentualValor;
    private Integer intervalo;
    private Integer condicaoPagamento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ParcelaCondicaoPagamento</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ParcelaCondicaoPagamentoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ParcelaCondicaoPagamentoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ParcelaCondicaoPagamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNumeroParcela().intValue() == 0) {
            throw new ConsistirException("O campo NR. PARCELA (Parcelas Condicão de Pagamento) deve ser informado.");
        }
        if (obj.getPercentualValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR(%) (Parcelas Condicão de Pagamento) deve ser informado.");
        }
        if (obj.getIntervalo().intValue() == 0 && obj.getNumeroParcela().intValue() > 1) {
            throw new ConsistirException("O campo INTERVALO(DIAS) (Parcelas Condicão de Pagamento) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNumeroParcela(0);
        setPercentualValor(0.0);
        setIntervalo(0);
    }

    public Integer getCondicaoPagamento() {
        return (condicaoPagamento);
    }

    public void setCondicaoPagamento(Integer condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public Integer getIntervalo() {
        return (intervalo);
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public Double getPercentualValor() {
        return (percentualValor);
    }

    public void setPercentualValor(Double percentualValor) {
        this.percentualValor = percentualValor;
    }

    public Integer getNumeroParcela() {
        return (numeroParcela);
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
