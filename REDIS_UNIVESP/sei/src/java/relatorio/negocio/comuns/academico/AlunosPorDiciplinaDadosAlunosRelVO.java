package relatorio.negocio.comuns.academico;


public class AlunosPorDiciplinaDadosAlunosRelVO {

    public AlunosPorDiciplinaDadosAlunosRelVO() {

    }

    private String nomeAluno;
    private String telefoneFixo;
    private String telefoneCelular;
    private String email;
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

    public String getObs() {
        if (obs == null) {
            obs = "";
        }
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

}
