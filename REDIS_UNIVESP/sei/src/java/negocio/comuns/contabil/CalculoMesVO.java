package negocio.comuns.contabil;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade calculoMes. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class CalculoMesVO extends SuperVO {

    protected Integer codigo;
    protected Double valorDebito;
    protected Double valorCredito;
    protected Integer mes;
    protected Integer ano;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PlanoConta </code>.*/
    protected PlanoContaVO planoConta;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Empresa </code>.*/
    protected UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>calculoMes</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public CalculoMesVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>CalculoMesVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(CalculoMesVO obj) throws ConsistirException {
//        if (!obj.isValidarDados().booleanValue()) {
//            return;
//            }
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

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
//    public void realizarUpperCaseDados() {
//        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
//            return;
//        }
//    }
    /**
     * Retorna o objeto da classe <code>PlanoConta</code> relacionado com (<code>calculoMes</code>).
     */
    public PlanoContaVO getPlanoConta() {
        if (planoConta == null) {
            planoConta = new PlanoContaVO();
        }
        return (planoConta);
    }

    /**
     * Define o objeto da classe <code>PlanoConta</code> relacionado com (<code>calculoMes</code>).
     */
    public void setPlanoConta(PlanoContaVO obj) {
        this.planoConta = obj;
    }

    public Integer getAno() {
        if (ano == null) {
            ano = 0;
        }
        return (ano);
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        if (mes == null) {
            mes = 0;
        }
        return (mes);
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Double getValorCredito() {
        if (valorCredito == null) {
            valorCredito = 0.0;
        }
        return (valorCredito);
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public Double getValorDebito() {
        if (valorDebito == null) {
            valorDebito = 0.0;
        }
        return (valorDebito);
    }

    public void setValorDebito(Double valorDebito) {
        this.valorDebito = valorDebito;
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
