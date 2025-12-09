package negocio.comuns.contabil;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade DRE. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class DREVO extends SuperVO {

    protected Integer codigo;
    protected Integer ordem;
    protected Double valor;
    protected String sinal;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Empresa </code>.*/
    protected UnidadeEnsinoVO unidadeEnsino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PlanoConta </code>.*/
    protected PlanoContaVO planoConta;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DRE</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public DREVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>DREVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(DREVO obj) throws ConsistirException {
        if ((obj.getPlanoConta() == null)
                || (obj.getPlanoConta().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PLANO DE CONTAS (DRE) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
//    public void realizarUpperCaseDados() {
//        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
//            return;
//        }
//    }
    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setSinal("DE");
    }

    /**
     * Retorna o objeto da classe <code>PlanoConta</code> relacionado com (<code>DRE</code>).
     */
    public PlanoContaVO getPlanoConta() {
        if (planoConta == null) {
            planoConta = new PlanoContaVO();
        }
        return (planoConta);
    }

    /**
     * Define o objeto da classe <code>PlanoConta</code> relacionado com (<code>DRE</code>).
     */
    public void setPlanoConta(PlanoContaVO obj) {
        this.planoConta = obj;
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

    public Integer getOrdem() {
        if (ordem == null) {
            ordem = 0;
        }
        return (ordem);
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
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

    public String getSinal() {
        if (sinal == null) {
            return "";
        }
        return sinal;
    }

    public void setSinal(String sinal) {
        this.sinal = sinal;
    }

    public Double getValor() {
        if (valor == null) {
            valor = new Double(0.0);
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
