/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class LogTurmaVO {

    private Integer codigo;
    private TurmaVO turma;
    private PeriodoLetivoVO periodoLetivo;
    private GradeCurricularVO gradeCurricular;
    private CursoVO curso;
    private ContaCorrenteVO contaCorrente;
    private Date dataAlteracao;
    private UsuarioVO usuarioResponsavel;
    private Date dataBaseGeracaoParcelas;
    private String acao;

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public PeriodoLetivoVO getPeriodoLetivo() {
        if (periodoLetivo == null) {
            periodoLetivo = new PeriodoLetivoVO();
        }
        return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivoVO periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public GradeCurricularVO getGradeCurricular() {
        if (gradeCurricular == null) {
            gradeCurricular = new GradeCurricularVO();
        }
        return gradeCurricular;
    }

    public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
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

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Date getDataAlteracao() {
        if (dataAlteracao == null) {
            dataAlteracao = new Date();
        }
        return dataAlteracao;
    }

    public String getDataAlteracao_Apresentar() {
        return Uteis.getDataComHora(getDataAlteracao());
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public UsuarioVO getUsuarioResponsavel() {
        if (usuarioResponsavel == null) {
            usuarioResponsavel = new UsuarioVO();
        }
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
    

    public Date getDataBaseGeracaoParcelas() {
		return dataBaseGeracaoParcelas;
	}

	public void setDataBaseGeracaoParcelas(Date dataBaseGeracaoParcelas) {
		this.dataBaseGeracaoParcelas = dataBaseGeracaoParcelas;
	}

	public String getAcao() {
        if (acao == null) {
            acao = "";
        }
        return acao;
    }

    public String getAcao_Apresentar() {
        if (getAcao().equals("inclusao")) {
            return "Inclusão";
        } else if (getAcao().equals("exclusao")) {
            return "Exclusão";
        } else if (getAcao().equals("alteracao")) {
            return "Alteração";
        } else if (getAcao().equals("atualizacaoDisciplinasAlunos")) {
            return "Atualização das Disciplinas dos Aluno";
        }
        return "Atualização da Lista de Disciplinas";
    }

    public void setAcao(String acao) {
        this.acao = acao;
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
}
