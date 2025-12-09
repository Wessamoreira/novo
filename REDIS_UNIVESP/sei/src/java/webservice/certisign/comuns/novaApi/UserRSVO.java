package webservice.certisign.comuns.novaApi;

import com.google.gson.annotations.SerializedName;

public class UserRSVO {

    @SerializedName("id")
    private Integer codigo;

    @SerializedName("individualIdentificationCode")
    private String cpf;

    @SerializedName("name")
    private String nome;

    @SerializedName("email")
    private String email;

    private Boolean excluido;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
