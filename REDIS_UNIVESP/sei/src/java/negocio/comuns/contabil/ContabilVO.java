package negocio.comuns.contabil;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Contabil. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class ContabilVO extends SuperVO implements Cloneable {

    protected Integer codigo;
    protected Integer contraPartida;
    protected UnidadeEnsinoVO unidadeEnsino;
    protected String numeroDocumento;
    protected Date data;
    protected Date dataVencimento;
    protected String historico;
    protected Double valor;
    protected Double juro;
    protected Double desconto;
    protected String sinal;
    protected ContaReceberVO contaReceber;
    protected ContaPagarVO contaPagar;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PlanoConta </code>.*/
    protected PlanoContaVO conta;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Cliente </code>.*/
    protected PessoaVO pessoa;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Fornecedor </code>.*/
    protected FornecedorVO fornecedor;
    protected BancoVO banco;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Contabil</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ContabilVO() {
        super();
        inicializarDados();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ContabilVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(ContabilVO obj) throws ConsistirException {
        if ((obj.getConta() == null)
                || (obj.getConta().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA (Contábil) deve ser informado.");
        }
        if (obj.getNumeroDocumento().equals("")) {
            throw new ConsistirException("O campo NÚMERO DO DOCUMENTO (Contábil) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Contábil) deve ser informado.");
        }
        if (obj.getSinal().equals("")) {
            throw new ConsistirException("O campo SINAL (Contábil) deve ser informado.");
        }
//        if ((obj.getCliente() == null) ||
//            (obj.getCliente().getCodigo().intValue() == 0)) { 
//            throw new ConsistirException("O campo CLIENTE (Contábil) deve ser informado.");
//        }
//        if ((obj.getFornecedor() == null) ||
//            (obj.getFornecedor().getCodigo().intValue() == 0)) { 
//            throw new ConsistirException("O campo FORNECEDOR (Contábil) deve ser informado.");
//        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setContraPartida(0);
        setNumeroDocumento("");
        setData(new Date());
        setDataVencimento(new Date());
        setHistorico("");
        setValor(0.0);
        setSinal("");
    }

    public ContaPagarVO getContaPagar() {
        if (contaPagar == null) {
            contaPagar = new ContaPagarVO();
        }
        return contaPagar;
    }

    public void setContaPagar(ContaPagarVO contaPagar) {
        this.contaPagar = contaPagar;
    }

    public ContaReceberVO getContaReceber() {
        if (contaReceber == null) {
            contaReceber = new ContaReceberVO();
        }
        return contaReceber;
    }

    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }

    /**
     * Retorna o objeto da classe <code>Fornecedor</code> relacionado com (<code>Contabil</code>).
     */
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return (fornecedor);
    }

    /**
     * Define o objeto da classe <code>Fornecedor</code> relacionado com (<code>Contabil</code>).
     */
    public void setFornecedor(FornecedorVO obj) {
        this.fornecedor = obj;
    }

    /**
     * Retorna o objeto da classe <code>PlanoConta</code> relacionado com (<code>Contabil</code>).
     */
    public PlanoContaVO getConta() {
        if (conta == null) {
            conta = new PlanoContaVO();
        }
        return (conta);
    }

    /**
     * Define o objeto da classe <code>PlanoConta</code> relacionado com (<code>Contabil</code>).
     */
    public void setConta(PlanoContaVO obj) {
        this.conta = obj;
    }

    public String getSinal() {
        if (sinal == null) {
            return "";
        }
        return (sinal);
    }

    public void setSinal(String sinal) {
        this.sinal = sinal;
    }

    public Double getValor() {
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getHistorico() {
        if (historico == null) {
            return "";
        }
        return (historico);
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public Date getDataVencimento() {
        return (dataVencimento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
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
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
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

    public String getNumeroDocumento() {
        if (numeroDocumento == null) {
            return "";
        }
        return (numeroDocumento);
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getContraPartida() {
        return contraPartida;
    }

    public void setContraPartida(Integer contraPartida) {
        this.contraPartida = contraPartida;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Double getDesconto() {
        if (desconto == null) {
            desconto = 0.0;
        }
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public BancoVO getBanco() {
        if (banco == null) {
            banco = new BancoVO();
        }
        return banco;
    }

    public void setBanco(BancoVO banco) {
        this.banco = banco;
    }
}
