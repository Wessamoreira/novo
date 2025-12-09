package negocio.comuns.academico;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade SetranspAluno. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class SetranspAlunoVO extends SuperVO {

    private Integer codigo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Setransp </code>.
     */
    private SetranspVO setransp;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaPeriodoVO matriculaPeriodo;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>SetranspAluno</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public SetranspAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>SetranspAlunoVO</code>.
     */
    public static void validarUnicidade(List<SetranspAlunoVO> lista, SetranspAlunoVO obj) throws ConsistirException {
        for (SetranspAlunoVO repetido : lista) {
            if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
                throw new ConsistirException("");
            }
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>SetranspAlunoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(SetranspAlunoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
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
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>SetranspAluno</code>).
     */
    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return (matriculaPeriodo);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>SetranspAluno</code>).
     */
    public void setMatriculaPeriodo(MatriculaPeriodoVO obj) {
        this.matriculaPeriodo = obj;
    }

    /**
     * Retorna o objeto da classe <code>Setransp</code> relacionado com (
     * <code>SetranspAluno</code>).
     */
    public SetranspVO getSetransp() {
        if (setransp == null) {
            setransp = new SetranspVO();
        }
        return (setransp);
    }

    /**
     * Define o objeto da classe <code>Setransp</code> relacionado com (
     * <code>SetranspAluno</code>).
     */
    public void setSetransp(SetranspVO obj) {
        this.setransp = obj;
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
