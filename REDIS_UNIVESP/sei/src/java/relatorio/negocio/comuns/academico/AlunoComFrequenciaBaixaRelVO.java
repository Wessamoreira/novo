package relatorio.negocio.comuns.academico;

import negocio.comuns.basico.PessoaVO;


public class AlunoComFrequenciaBaixaRelVO {
    private String matricula;
    private PessoaVO aluno;
    private String unidadeEnsino;
    private String curso;
    private String disciplina;
    private Integer matriculaPeriodoDisciplina;
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public PessoaVO getAluno() {
        if(aluno == null){
            aluno = new PessoaVO();
        }
        return aluno;
    }
    
    public void setAluno(PessoaVO aluno) {
        this.aluno = aluno;
    }
    
    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }
    
    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
    
    public String getCurso() {
        return curso;
    }
    
    public void setCurso(String curso) {
        this.curso = curso;
    }
    
    public String getDisciplina() {
        return disciplina;
    }
    
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
    
    public Integer getMatriculaPeriodoDisciplina() {
        return matriculaPeriodoDisciplina;
    }
    
    public void setMatriculaPeriodoDisciplina(Integer matriculaPeriodoDisciplina) {
        this.matriculaPeriodoDisciplina = matriculaPeriodoDisciplina;
    }
    
    
}
