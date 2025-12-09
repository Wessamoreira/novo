package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public class PlanoFinanceiroReposicaoVO extends SuperVO {
    private Integer codigo;
    private String descricao;
    private TextoPadraoVO textoPadraoContratoVO;
    private Integer qtdeParcela;
    private Double valor;
    private String situacao;
    private Date dataAtivacao;
    private Date dataInativacao;
    private UsuarioVO responsavelAtivacao;
    private UsuarioVO responsavelInativacao;


    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TextoPadraoVO getTextoPadraoContratoVO() {
        if (textoPadraoContratoVO == null) {
            textoPadraoContratoVO = new TextoPadraoVO();
        }
        return textoPadraoContratoVO;
    }

    public void setTextoPadraoContratoVO(TextoPadraoVO textoPadraoContratoVO) {
        this.textoPadraoContratoVO = textoPadraoContratoVO;
    }

    public Integer getQtdeParcela() {
        if (qtdeParcela == null) {
            qtdeParcela = 0;
        }
        return qtdeParcela;
    }

    public void setQtdeParcela(Integer qtdeParcela) {
        this.qtdeParcela = qtdeParcela;
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
            situacao = "EL";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (situacao == null) {
            return "EM ELABORAÇÃO";
        }
        if (situacao.equals("EL")) {
            return "EM ELABORAÇÃO";
        }
        if (situacao.equals("AT")) {
            return "ATIVO";
        }
        if (situacao.equals("IN")) {
            return "INATIVO";
        }
        return "";
    }


    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataAtivacao() {
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
