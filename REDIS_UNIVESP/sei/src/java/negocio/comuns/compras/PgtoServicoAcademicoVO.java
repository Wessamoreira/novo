package negocio.comuns.compras;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PgtoServicoAcademico. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PgtoServicoAcademicoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String formaPagamento;
    private Double valorPagamento;
    private String nrDocumento;
    private String situacao;
    private String tipoDestinatario;
    private String codigoDestinatario;
    private String bancoDestinatario;
    private String agenciaDestinatario;
    private String contaDestinatario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>SolicitacaoPgtoServicoAcademico </code>.
     */
    private SolicitacaoPgtoServicoAcademicoVO solicitacaoPgtoServicoAcademico;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>CentroCusto </code>.
     */
    private CategoriaDespesaVO centroDespesa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCorrente;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PgtoServicoAcademico</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PgtoServicoAcademicoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PgtoServicoAcademicoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PgtoServicoAcademicoVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getValorPagamento().intValue() == 0) {
            throw new ConsistirException("O campo VALOR PAGAMENTO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getNrDocumento().equals("")) {
            throw new ConsistirException("O campo NÚMERO DOCUMENTO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getSolicitacaoPgtoServicoAcademico() == null)
                || (obj.getSolicitacaoPgtoServicoAcademico().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo SOLICITAÇÃO PAGAMENTO SERVIÇO ACADÊMICO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getCentroDespesa() == null) || (obj.getCentroDespesa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CENTRO DE CUSTO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getTipoDestinatario().equals("")) {
            throw new ConsistirException("O campo TIPO DESTINATÁRIO (Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getContaCorrente() == null) || (obj.getContaCorrente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CORRENTE (Pagamento Serviço Acadêmico) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setFormaPagamento("");
        setValorPagamento(0.0);
        setNrDocumento("");
        setSituacao("");
        setTipoDestinatario("");
        setCodigoDestinatario("");
        setBancoDestinatario("");
        setAgenciaDestinatario("");
        setContaDestinatario("");
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>PgtoServicoAcademico</code>).
     */
    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return (contaCorrente);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>PgtoServicoAcademico</code>).
     */
    public void setContaCorrente(ContaCorrenteVO obj) {
        this.contaCorrente = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PgtoServicoAcademico</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PgtoServicoAcademico</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public CategoriaDespesaVO getCentroDespesa() {
        if (centroDespesa == null) {
            centroDespesa = new CategoriaDespesaVO();
        }
        return centroDespesa;
    }

    public void setCentroDespesa(CategoriaDespesaVO centroDespesa) {
        this.centroDespesa = centroDespesa;
    }

    /**
     * Retorna o objeto da classe <code>SolicitacaoPgtoServicoAcademico</code>
     * relacionado com (<code>PgtoServicoAcademico</code>).
     */
    public SolicitacaoPgtoServicoAcademicoVO getSolicitacaoPgtoServicoAcademico() {
        if (solicitacaoPgtoServicoAcademico == null) {
            solicitacaoPgtoServicoAcademico = new SolicitacaoPgtoServicoAcademicoVO();
        }
        return (solicitacaoPgtoServicoAcademico);
    }

    /**
     * Define o objeto da classe <code>SolicitacaoPgtoServicoAcademico</code>
     * relacionado com (<code>PgtoServicoAcademico</code>).
     */
    public void setSolicitacaoPgtoServicoAcademico(SolicitacaoPgtoServicoAcademicoVO obj) {
        this.solicitacaoPgtoServicoAcademico = obj;
    }

    public String getContaDestinatario() {
        return (contaDestinatario);
    }

    public void setContaDestinatario(String contaDestinatario) {
        this.contaDestinatario = contaDestinatario;
    }

    public String getAgenciaDestinatario() {
        return (agenciaDestinatario);
    }

    public void setAgenciaDestinatario(String agenciaDestinatario) {
        this.agenciaDestinatario = agenciaDestinatario;
    }

    public String getBancoDestinatario() {
        return (bancoDestinatario);
    }

    public void setBancoDestinatario(String bancoDestinatario) {
        this.bancoDestinatario = bancoDestinatario;
    }

    public String getCodigoDestinatario() {
        return (codigoDestinatario);
    }

    public void setCodigoDestinatario(String codigoDestinatario) {
        this.codigoDestinatario = codigoDestinatario;
    }

    public String getTipoDestinatario() {
        return (tipoDestinatario);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDestinatario_Apresentar() {
        if (tipoDestinatario.equals("PR")) {
            return "Professor";
        }
        if (tipoDestinatario.equals("AL")) {
            return "Aluno";
        }
        if (tipoDestinatario.equals("MC")) {
            return "Menbro Comunidade";
        }
        if (tipoDestinatario.equals("FU")) {
            return "Funcionário";
        }
        return (tipoDestinatario);
    }

    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("QI")) {
            return "Quitado";
        }
        if (situacao.equals("AB")) {
            return "Em Aberto";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getNrDocumento() {
        return (nrDocumento);
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Double getValorPagamento() {
        return (valorPagamento);
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public String getFormaPagamento() {
        return (formaPagamento);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getFormaPagamento_Apresentar() {
        if (formaPagamento.equals("DB")) {
            return "Deposito Bancário";
        }
        if (formaPagamento.equals("ED")) {
            return "Em Dinheiro";
        }
        if (formaPagamento.equals("CH")) {
            return "Cheque";
        }
        return (formaPagamento);
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Date getData() {
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

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
