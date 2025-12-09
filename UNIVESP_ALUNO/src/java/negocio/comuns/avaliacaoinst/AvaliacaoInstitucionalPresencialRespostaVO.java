package negocio.comuns.avaliacaoinst;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade AvaliacaoInstitucional. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AvaliacaoInstitucionalPresencialRespostaVO extends SuperVO {

    private Integer codigo;
    private Date dataCriacao;
    private Date dataAlteracao;
    private AvaliacaoInstitucionalVO avaliacaoInstitucional;
    private UnidadeEnsinoVO unidadeEnsino;
    private CursoVO curso;
    private TurmaVO turma;
    private DisciplinaVO disciplina;
    private PessoaVO professor;
    private UsuarioVO responsavel;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>AvaliacaoInstitucional</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public AvaliacaoInstitucionalPresencialRespostaVO() {
        super();
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

    public Date getDataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = new Date();
        }
        return (dataCriacao);
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataCriacao_Apresentar() {
        return (Uteis.getData(getDataCriacao()));
    }

    public Date getDataAlteracao() {
        if (dataAlteracao == null) {
            dataAlteracao = new Date();
        }
        return (dataAlteracao);
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getDataAlteracao_Apresentar() {
        return (Uteis.getData(getDataAlteracao()));
    }

    public AvaliacaoInstitucionalVO getAvaliacaoInstitucional() {
        if (avaliacaoInstitucional == null) {
            avaliacaoInstitucional = new AvaliacaoInstitucionalVO();
        }
        return avaliacaoInstitucional;
    }

    public void setAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucional) {
        this.avaliacaoInstitucional = avaliacaoInstitucional;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }
}
