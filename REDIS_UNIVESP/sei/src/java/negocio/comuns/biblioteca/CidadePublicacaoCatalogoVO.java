package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;

public class CidadePublicacaoCatalogoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String estado;
    private String pais;
    public static final long serialVersionUID = 1L;

    public CidadePublicacaoCatalogoVO() {
        super();
    }

    public String getNomeConcatEstado() {
        return getNome() + " - " + getEstado();
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        if (pais == null) {
            pais = "";
        }
        return pais;
    }
}
