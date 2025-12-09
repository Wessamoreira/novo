package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public class PlanoDescontoInclusaoDisciplinaVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private Double valor;
    private Date dataAtivacao;
    private Date dataInativacao;
    private UsuarioVO responsavelAtivacao;
    private UsuarioVO responsavelInativacao;
    private String situacao;
    public static final long serialVersionUID = 1L;

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "CO";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao_Apresentar() {
        if (getSituacao().equals("CO")) {
            return "Em construção";
        }
        if (getSituacao().equals("AT")) {
            return "Ativo";
        }
        if (getSituacao().equals("FI")) {
            return "Finalizado";
        }
        return (getSituacao());
    }

    public Date getDataAtivacao() {
        if (dataAtivacao == null) {
            dataAtivacao = new Date();
        }
        return dataAtivacao;
    }

    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public Date getDataInativacao() {
        return dataInativacao;
    }

    public void setDataInativacao(Date dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public UsuarioVO getResponsavelAtivacao() {
        if (responsavelAtivacao == null) {
            responsavelAtivacao = new UsuarioVO();
        }
        return responsavelAtivacao;
    }

    public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
        this.responsavelAtivacao = responsavelAtivacao;
    }

    public UsuarioVO getResponsavelInativacao() {
        if (responsavelInativacao == null) {
            responsavelInativacao = new UsuarioVO();
        }
        return responsavelInativacao;
    }

    public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
        this.responsavelInativacao = responsavelInativacao;
    }
}
