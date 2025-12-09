package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

public class NrAlunosMatriculadosVO {

    private String nomeTurma;
    private String nomeDisciplina;
    private Integer nrAlunos;
    //private List<AlunosPorDiciplinaDadosAlunosRelVO> listaAlunosPorDiciplinaDadosAlunos;
    
    private String nomeAluno;
    private String telefoneFixo;
    private String telefoneCelular;
    private String email;
    private String cpf;
    private String obs;

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTelefoneFixo() {
        if (telefoneFixo == null) {
            telefoneFixo = "";
        }
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getTelefoneCelular() {
        if (telefoneCelular == null) {
            telefoneCelular = "";
        }
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getObs() {
        if (obs == null) {
            obs = "";
        }
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public NrAlunosMatriculadosVO() {

    }

    public String getNomeTurma() {
        if (nomeTurma == null) {
            nomeTurma = "";
        }
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getNomeDisciplina() {
        if (nomeDisciplina == null) {
            nomeDisciplina = "";
        }
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Integer getNrAlunos() {
        return nrAlunos;
    }

    public void setNrAlunos(Integer nrAlunos) {
        this.nrAlunos = nrAlunos;
    }

//    public List<AlunosPorDiciplinaDadosAlunosRelVO> getListaAlunosPorDiciplinaDadosAlunos() {
//        if (listaAlunosPorDiciplinaDadosAlunos == null) {
//            listaAlunosPorDiciplinaDadosAlunos = new ArrayList<AlunosPorDiciplinaDadosAlunosRelVO>();
//        }
//        return listaAlunosPorDiciplinaDadosAlunos;
//    }
//
//    public void setListaAlunosPorDiciplinaDadosAlunos(List<AlunosPorDiciplinaDadosAlunosRelVO> listaAlunosPorDiciplinaDadosAlunos) {
//        this.listaAlunosPorDiciplinaDadosAlunos = listaAlunosPorDiciplinaDadosAlunos;
//    }

}