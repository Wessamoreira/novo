package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class LogFechamentoVO extends SuperVO {

    private Integer codigo;
    private String matricula;
    private Date data;
    private String descricao;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
