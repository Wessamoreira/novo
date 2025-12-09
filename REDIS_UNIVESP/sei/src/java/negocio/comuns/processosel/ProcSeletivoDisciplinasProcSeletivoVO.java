package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Reponsável por manter os dados da entidade
 * ProcSeletivoDisciplinasProcSeletivo. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProcSeletivo
 */
public class ProcSeletivoDisciplinasProcSeletivoVO extends SuperVO {

    private Integer procSeletivo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>DisciplinasProcSeletivo </code>.
     */
    private DisciplinasProcSeletivoVO disciplinasProcSeletivo;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe
     * <code>ProcSeletivoDisciplinasProcSeletivo</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ProcSeletivoDisciplinasProcSeletivoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcSeletivoDisciplinasProcSeletivoVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcSeletivoDisciplinasProcSeletivoVO obj) throws ConsistirException {
        if ((obj.getDisciplinasProcSeletivo() == null)
                || (obj.getDisciplinasProcSeletivo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINAS PROCESSO SELETIVO (Disciplinas Processo Seletivo) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    /**
     * Retorna o objeto da classe <code>DisciplinasProcSeletivo</code>
     * relacionado com (<code>ProcSeletivoDisciplinasProcSeletivo</code>).
     */
    public DisciplinasProcSeletivoVO getDisciplinasProcSeletivo() {
        if (disciplinasProcSeletivo == null) {
            disciplinasProcSeletivo = new DisciplinasProcSeletivoVO();
        }
        return (disciplinasProcSeletivo);
    }

    /**
     * Define o objeto da classe <code>DisciplinasProcSeletivo</code>
     * relacionado com (<code>ProcSeletivoDisciplinasProcSeletivo</code>).
     */
    public void setDisciplinasProcSeletivo(DisciplinasProcSeletivoVO obj) {
        this.disciplinasProcSeletivo = obj;
    }

    public Integer getProcSeletivo() {
        return (procSeletivo);
    }

    public void setProcSeletivo(Integer procSeletivo) {
        this.procSeletivo = procSeletivo;
    }
}
