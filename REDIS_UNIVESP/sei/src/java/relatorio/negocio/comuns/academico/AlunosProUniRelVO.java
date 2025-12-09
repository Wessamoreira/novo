package relatorio.negocio.comuns.academico;

import java.util.List;

public class AlunosProUniRelVO {

    private List<AlunosProUniRelListaVO> listaAlunosProUniRel;

    private String unidadeEnsino;
    private String curso;
    private String turno;
    private String turma;
    private String semestre;
    private String ano;
    
    private String matricula;
    private String nomeAluno;
    private String periodoLetivo;
    private String valorCheioMensalidade;

    public AlunosProUniRelVO() {

    }

    public List<AlunosProUniRelListaVO> getListaAlunosProUni() {
        return listaAlunosProUniRel;
    }

   // public JRDataSource getListaAlunosProUniJrDataSource() {
     //   JRDataSource jr = new JRBeanArrayDataSource(getListaAlunosProUni().toArray());
      //  return jr;
   // }

    public void setListaAlunosProUni(List<AlunosProUniRelListaVO> listaAlunosProUni) {
        this.listaAlunosProUniRel = listaAlunosProUni;
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

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
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
