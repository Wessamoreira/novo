package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaVO;

public class AlunosProUniRelListaVO {

    private MatriculaPeriodoVO matriculaPeriodo;
    private PessoaVO pessoa;
    private TurmaVO turma;
    
    private String matricula;
    private String nomeAluno;
    private String periodoLetivo;
    private String valorCheioMensalidade;

    public AlunosProUniRelListaVO() {

    }

    public MatriculaPeriodoVO getMatriculaPeriodo() {
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public PessoaVO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public TurmaVO getTurma() {
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    
    public String getMatricula() {
        return matricula;
    }

    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    
    public String getNomeAluno() {
        return nomeAluno;
    }

    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    
    public String getPeriodoLetivo() {
        return periodoLetivo;
    }

    
    public void setPeriodoLetivo(String periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    
    public String getValorCheioMensalidade() {
        return valorCheioMensalidade;
    }

    
    public void setValorCheioMensalidade(String valorCheioMensalidade) {
        this.valorCheioMensalidade = valorCheioMensalidade;
    }

}
