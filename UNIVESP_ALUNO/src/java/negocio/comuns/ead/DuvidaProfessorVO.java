package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoDuvidaProfessorEnum;


public class DuvidaProfessorVO extends SuperVO {
    
    private Integer codigo;
    private Date dataCadastro;
    private String matricula;
    private PessoaVO aluno;
    private DisciplinaVO disciplina;
    private TurmaVO turma;
    private Boolean permitePublicarDuvida;
    private Boolean duvidaFrequente;    
    private Date dataAlteracao;
    private String duvida;
    private SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor;
    private List<DuvidaProfessorInteracaoVO> duvidaProfessorInteracaoVOs;
    
    public Date getDataCadastro() {
        if(dataCadastro == null){
            dataCadastro = new Date();
        }
        return dataCadastro;
    }
    
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
  
    
    public DisciplinaVO getDisciplina() {
        if(disciplina == null){
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }
    
    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }
    
    public TurmaVO getTurma() {
        if(turma == null){
            turma = new TurmaVO();
        }
        return turma;
    }
    
    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }
    
    public Boolean getPermitePublicarDuvida() {
        if(permitePublicarDuvida == null){
            permitePublicarDuvida = true;
        }
        return permitePublicarDuvida;
    }
    
    public void setPermitePublicarDuvida(Boolean permitePublicarDuvida) {
        this.permitePublicarDuvida = permitePublicarDuvida;
    }
    
    public Date getDataAlteracao() {
        return dataAlteracao;
    }
    
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
    
    public String getDuvida() {
        if(duvida == null){
            duvida = "";
        }
        return duvida;
    }
    
    public void setDuvida(String duvida) {
        this.duvida = duvida;
    }
    
    public SituacaoDuvidaProfessorEnum getSituacaoDuvidaProfessor() {
        if(situacaoDuvidaProfessor == null){
            situacaoDuvidaProfessor = SituacaoDuvidaProfessorEnum.NOVA;
        }
        return situacaoDuvidaProfessor;
    }
    
    public void setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor) {
        this.situacaoDuvidaProfessor = situacaoDuvidaProfessor;
    }

    
    public Boolean getDuvidaFrequente() {
        if(duvidaFrequente == null){
            duvidaFrequente = false;
        }
        return duvidaFrequente;
    }

    
    public void setDuvidaFrequente(Boolean duvidaFrequente) {
        this.duvidaFrequente = duvidaFrequente;
    }

    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }

    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    
    public String getMatricula() {
        if(matricula == null){
            matricula = "";
        }
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

    
    public List<DuvidaProfessorInteracaoVO> getDuvidaProfessorInteracaoVOs() {
        if(duvidaProfessorInteracaoVOs == null){
            duvidaProfessorInteracaoVOs = new ArrayList<DuvidaProfessorInteracaoVO>(0);
        }
        return duvidaProfessorInteracaoVOs;
    }

    
    public void setDuvidaProfessorInteracaoVOs(List<DuvidaProfessorInteracaoVO> duvidaProfessorInteracaoVOs) {
        this.duvidaProfessorInteracaoVOs = duvidaProfessorInteracaoVOs;
    }
    
    
    

}
