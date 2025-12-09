package relatorio.negocio.comuns.academico;

/**
 *
 * @author Carlos
 */
public class EnvelopeRelVO {
    private String matricula;
    private String nome;
    private String cidade;
    private String instituicaoChanceladora;
    private String nomeDocumento;
    private Boolean entregue;
    private String endereco;
    private String cep;

    public String getNomeDocumento() {
        if (nomeDocumento == null) {
            nomeDocumento = "";
        }
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public Boolean getEntregue() {
        if (entregue == null) {
            entregue = Boolean.FALSE;
        }
        return entregue;
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getInstituicaoChanceladora() {
        if (instituicaoChanceladora == null) {
            instituicaoChanceladora = "";
        }
        return instituicaoChanceladora;
    }

    public void setInstituicaoChanceladora(String instituicaoChanceladora) {
        this.instituicaoChanceladora = instituicaoChanceladora;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        if (endereco == null){
            endereco = "";
        }
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        if (cep == null){
            cep = "";
        }
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
