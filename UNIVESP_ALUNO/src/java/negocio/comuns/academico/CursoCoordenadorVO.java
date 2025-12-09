package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;

/**
 * Reponsável por manter os dados da entidade UnidadeEnsinoCurso. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see UnidadeEnsino
 */
public class CursoCoordenadorVO extends SuperVO {

    private Integer codigo;
    private Double porcentagemComissao;
    private Double valorFixoComissao;
    private Boolean valorPorAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    private TurmaVO turma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino</code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO funcionario;
    private TipoCoordenadorCursoEnum tipoCoordenadorCurso;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>UnidadeEnsinoCurso</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public CursoCoordenadorVO() {
        super();
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    public Double getPorcentagemComissao() {
        if (porcentagemComissao == null) {
            porcentagemComissao = 0.0;
        }
        return porcentagemComissao;
    }

    public void setPorcentagemComissao(Double porcentagemComissao) {
        this.porcentagemComissao = porcentagemComissao;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Double getValorFixoComissao() {
        if (valorFixoComissao == null) {
            valorFixoComissao = 0.0;
        }
        return valorFixoComissao;
    }

    public void setValorFixoComissao(Double valorFixoComissao) {
        this.valorFixoComissao = valorFixoComissao;
    }

    public Boolean getValorPorAluno() {
        if (valorPorAluno == null) {
            valorPorAluno = false;
        }
        return valorPorAluno;
    }

    public void setValorPorAluno(Boolean valorPorAluno) {
        this.valorPorAluno = valorPorAluno;
    }

	public TipoCoordenadorCursoEnum getTipoCoordenadorCurso() {
		if(tipoCoordenadorCurso == null){
			tipoCoordenadorCurso = TipoCoordenadorCursoEnum.GERAL;
		}
		return tipoCoordenadorCurso;
	}

	public void setTipoCoordenadorCurso(TipoCoordenadorCursoEnum tipoCoordenadorCurso) {
		this.tipoCoordenadorCurso = tipoCoordenadorCurso;
	}
    
    
}
