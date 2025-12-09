package negocio.comuns.contabil;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade TipoEvendoContabil. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoEventoContabilVO extends SuperVO {

    protected Integer codigo;
    protected String descricao;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PlanoConta </code>. */
    protected PlanoContaVO contaDebito;
    protected PlanoContaVO contaCredito;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Historico </code>. */
    protected HistoricoContabilVO historico;
    protected Boolean selecionado;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoEvendoContabil</code>. Cria uma nova instância desta entidade, inicializando
     * automaticamente seus atributos (Classe VO).
     */
    public TipoEventoContabilVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>TipoEvendoContabilVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos
     * obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public static void validarDados(TipoEventoContabilVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Tipo Evento Contábil) deve ser informado.");
        }
        if ((obj.getContaDebito() == null) || (obj.getContaDebito().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA DE DÉBITO (Tipo Evento Contábil) deve ser informado.");
        }
        if ((obj.getContaCredito() == null) || (obj.getContaCredito().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA DE CRÉDITO (Tipo Evento Contábil) deve ser informado.");
        }
        if ((obj.getHistorico() == null) || (obj.getHistorico().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo HISTÓRICO (Tipo Evento Contábil) deve ser informado.");
        }
        if (obj.getContaCredito().getCodigo().intValue() == obj.getContaDebito().getCodigo().intValue()) {
            throw new ConsistirException("O plano de contas escolhido nos campos Conta de Débito e Conta de Crédito não podem ser iguais.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setContaDebito(new PlanoContaVO());
        setContaCredito(new PlanoContaVO());
        setHistorico(new HistoricoContabilVO());
        setSelecionado(Boolean.FALSE);
    }

    /**
     * Retorna o objeto da classe <code>Historico</code> relacionado com (<code>TipoEvendoContabil</code>).
     */
    public HistoricoContabilVO getHistorico() {
        return (historico);
    }

    /**
     * Define o objeto da classe <code>Historico</code> relacionado com (<code>TipoEvendoContabil</code>).
     */
    public void setHistorico(HistoricoContabilVO obj) {
        this.historico = obj;
    }

    /**
     * Retorna o objeto da classe <code>PlanoConta</code> relacionado com (<code>TipoEvendoContabil</code>).
     */
    public PlanoContaVO getContaDebito() {
        return (contaDebito);
    }

    /**
     * Define o objeto da classe <code>PlanoConta</code> relacionado com (<code>TipoEvendoContabil</code>).
     */
    public void setContaDebito(PlanoContaVO obj) {
        this.contaDebito = obj;
    }

    public String getDescricao() {
        if (descricao == null) {
            return "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public PlanoContaVO getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(PlanoContaVO contaCredito) {
        this.contaCredito = contaCredito;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
}
