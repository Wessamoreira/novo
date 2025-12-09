package negocio.comuns.financeiro;

import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.Convenio;

/**
 * Reponsável por manter os dados da entidade ConvenioTurno. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see Convenio
 */
public class ConvenioTurnoVO extends SuperVO {

    private Integer convenio;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ConvenioTurno</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ConvenioTurnoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConvenioTurnoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConvenioTurnoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Turnos Cobertos no Convênio) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>ConvenioTurno</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>ConvenioTurno</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    public Integer getConvenio() {
        if (convenio == null) {
            convenio = 0;
        }
        return (convenio);
    }

    public void setConvenio(Integer convenio) {
        this.convenio = convenio;
    }
}
