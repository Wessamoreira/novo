package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Censo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class NotificacaoRegistroAulaNotaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Integer professor;
    private FuncionarioVO professorVO;
    private Integer turma;
    private TurmaVO turmaVO;
    private Integer unidadeEnsino;
    private UsuarioVO usuario;
    private Integer disciplina;
    private DisciplinaVO disciplinaVO;
    public static final long serialVersionUID = 1L;

    public NotificacaoRegistroAulaNotaVO() {
        super();
    }

    
    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(getData()));
    }

    public void setData(Date data) {
        this.data = data;
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

    public Integer getProfessor() {
        if (professor == null) {
            professor = 0;
        }
        return professor;
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    /**
     * @return the data
     */
    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    /**
     * @return the turma
     */
    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    /**
     * @return the disciplina
     */
    public Integer getDisciplina() {
        if (disciplina == null) {
            disciplina = 0;
        }
        return disciplina;
    }

    /**
     * @param disciplina the disciplina to set
     */
    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the unidadeEnsino
     */
    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the usuario
     */
    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the disciplinaVO
     */
    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    /**
     * @param disciplinaVO the disciplinaVO to set
     */
    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    /**
     * @return the professorVO
     */
    public FuncionarioVO getProfessorVO() {
        if (professorVO == null) {
            professorVO = new FuncionarioVO();
        }
        return professorVO;
    }

    /**
     * @param professorVO the professorVO to set
     */
    public void setProfessorVO(FuncionarioVO professorVO) {
        this.professorVO = professorVO;
    }

}
