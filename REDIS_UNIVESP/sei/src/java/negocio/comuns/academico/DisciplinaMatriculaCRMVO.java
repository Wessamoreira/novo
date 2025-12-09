package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade DocumetacaoMatricula. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
public class DisciplinaMatriculaCRMVO extends SuperVO {

    private Integer codigo;
    private DisciplinaVO disciplinaVO;
    private Integer matriculaCRM;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DocumetacaoMatricula</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinaMatriculaCRMVO() {
        super();
    }

    public Integer getMatriculaCRM() {
        if (matriculaCRM == null) {
            matriculaCRM = 0;
        }
        return (matriculaCRM);
    }

    public void setMatriculaCRM(Integer matriculaCRM) {
        this.matriculaCRM = matriculaCRM;
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

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

}
