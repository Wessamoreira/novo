package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class LogFuncionarioVO extends SuperVO {

    private Integer codigo;
    private UsuarioVO usuario;
    private Date data;
    private String acao;
    private String nome;
    private String CPF;
    private Integer codigoFuncionario;
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

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public String getAcao() {
        if (acao == null) {
            acao = "";
        }
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getCPF() {
        if (CPF == null) {
            CPF = "";
        }
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
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

    public Integer getCodigoFuncionario() {
        if (codigoFuncionario == null) {
            codigoFuncionario = 0;
        }
        return (codigoFuncionario);
    }

    public void setCodigoFuncionario(Integer codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }
}
