package negocio.comuns.compras;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CreditoFornecedor. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CreditoFornecedorVO extends SuperVO {

    private Integer codigo;
    private Double valor;
    private Double valorUtilizado;
    private Double saldo;
    private Date data;
    private Integer codigoOrigem;
    private String origem;
    private String situacao;
    private Double valorDevolucao;
    private Date dataDevolucao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavelCadastro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Fornecedor </code>.
     */
    private FornecedorVO fornecedor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavelDevolucao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCorrente;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CreditoFornecedor</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CreditoFornecedorVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CreditoFornecedorVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CreditoFornecedorVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getValor() == null) {
            throw new ConsistirException("O campo VALOR (CreditoFornecedor) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (CreditoFornecedor) deve ser informado.");
        }
        if ((obj.getResponsavelCadastro() == null) || (obj.getResponsavelCadastro().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL CADASTRO (CreditoFornecedor) deve ser informado.");
        }
        if (obj.getCodigoOrigem().intValue() == 0) {
            throw new ConsistirException("O campo CÓDIGO ORIGEM (CreditoFornecedor) deve ser informado.");
        }
        if (obj.getOrigem().equals("")) {
            throw new ConsistirException("O campo ORIGEM (CreditoFornecedor) deve ser informado.");
        }
        if ((obj.getFornecedor() == null) || (obj.getFornecedor().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FORNECEDOR (CreditoFornecedor) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (CreditoFornecedor) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setOrigem(getOrigem().toUpperCase());
        setSituacao(getSituacao().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setValor(0.0);
        setValorUtilizado(0.0);
        setSaldo(0.0);
        setData(new Date());
        setCodigoOrigem(0);
        setOrigem("");
        setSituacao("");
        setValorDevolucao(0.0);
        setDataDevolucao(new Date());
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return (contaCorrente);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public void setContaCorrente(ContaCorrenteVO obj) {
        this.contaCorrente = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public UsuarioVO getResponsavelDevolucao() {
        if (responsavelDevolucao == null) {
            responsavelDevolucao = new UsuarioVO();
        }
        return (responsavelDevolucao);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public void setResponsavelDevolucao(UsuarioVO obj) {
        this.responsavelDevolucao = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return (fornecedor);
    }

    /**
     * Define o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public void setFornecedor(FornecedorVO obj) {
        this.fornecedor = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public UsuarioVO getResponsavelCadastro() {
        if (responsavelCadastro == null) {
            responsavelCadastro = new UsuarioVO();
        }
        return (responsavelCadastro);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>CreditoFornecedor</code>).
     */
    public void setResponsavelCadastro(UsuarioVO obj) {
        this.responsavelCadastro = obj;
    }

    public Date getDataDevolucao() {
        if (dataDevolucao == null) {
            dataDevolucao = new Date();
        }
        return (dataDevolucao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataDevolucao_Apresentar() {
        return (Uteis.getData(dataDevolucao));
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Double getValorDevolucao() {
        if (valorDevolucao == null) {
            valorDevolucao = 0.0;
        }
        return (valorDevolucao);
    }

    public void setValorDevolucao(Double valorDevolucao) {
        this.valorDevolucao = valorDevolucao;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getOrigem() {
        if (origem == null) {
            origem = "";
        }
        return (origem);
    }

    public void setOrigem(String origem) {
        this.origem = origem;
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
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getSaldo() {
        if (saldo == null) {
            saldo = 0.0;
        }
        return (saldo);
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getValorUtilizado() {
        if (valorUtilizado == null) {
            valorUtilizado = 0.0;
        }
        return (valorUtilizado);
    }

    public void setValorUtilizado(Double valorUtilizado) {
        this.valorUtilizado = valorUtilizado;
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
