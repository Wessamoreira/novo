/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Rogerio
 */
public class DadosComerciaisVO extends SuperVO {

    private Integer codigo;
    private String nomeEmpresa;
    private String enderecoEmpresa;
    private String cargoPessoaEmpresa;
    private String cepEmpresa;
    private String complementoEmpresa;
    private String setorEmpresa;
    
    private CidadeVO cidadeEmpresa;
    
    private PessoaVO pessoa;
    private String telefoneComer;
    private Boolean empregoAtual;
    private String principaisAtividades;
    private String salario;
    private Date dataAdmissao;
    private Date dataDemissao;
    private String motivoDesligamento;
    public static final long serialVersionUID = 1L;

    public DadosComerciaisVO() {
        super();
    }

    public String getCargoPessoaEmpresa() {
        if (cargoPessoaEmpresa == null) {
            cargoPessoaEmpresa = "";
        }
        return cargoPessoaEmpresa;
    }

    public void setCargoPessoaEmpresa(String cargoPessoaEmpresa) {
        this.cargoPessoaEmpresa = cargoPessoaEmpresa;
    }

    public String getCepEmpresa() {
        if (cepEmpresa == null) {
            cepEmpresa = "";
        }
        return cepEmpresa;
    }

    public void setCepEmpresa(String cepEmpresa) {
        this.cepEmpresa = cepEmpresa;
    }

    public CidadeVO getCidadeEmpresa() {
        if (cidadeEmpresa == null) {
            cidadeEmpresa = new CidadeVO();
        }
        return cidadeEmpresa;
    }

    public void setCidadeEmpresa(CidadeVO cidadeEmpresa) {
        this.cidadeEmpresa = cidadeEmpresa;
    }

    public String getComplementoEmpresa() {
        if (complementoEmpresa == null) {
            complementoEmpresa = "";
        }
        return complementoEmpresa;
    }

    public void setComplementoEmpresa(String complementoEmpresa) {
        this.complementoEmpresa = complementoEmpresa;
    }

    public String getEnderecoEmpresa() {
        if (enderecoEmpresa == null) {
            enderecoEmpresa = "";
        }
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(String enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getNomeEmpresa() {
        if (nomeEmpresa == null) {
            nomeEmpresa = "";
        }
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getSetorEmpresa() {
        if (setorEmpresa == null) {
            setorEmpresa = "";
        }
        return setorEmpresa;
    }

    public void setSetorEmpresa(String setorEmpresa) {
        this.setorEmpresa = setorEmpresa;
    }

    public String getTelefoneComer() {
        if (telefoneComer == null) {
            telefoneComer = "";
        }
        return telefoneComer;
    }

    public void setTelefoneComer(String telefoneComer) {
        this.telefoneComer = telefoneComer;
    }

    public Boolean getEmpregoAtual() {
        if (empregoAtual == null) {
            empregoAtual = false;
        }
        return empregoAtual;
    }

    public String getEmpregoAtual_Apresentar() {
        if (getEmpregoAtual()) {
            return "Sim";
        }
        return "Não";
    }

    public void setEmpregoAtual(Boolean empregoAtual) {
        this.empregoAtual = empregoAtual;
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

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getSalario() {
        if (salario == null) {
            salario = "";
        }
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getPrincipaisAtividades() {
        if (principaisAtividades == null) {
            principaisAtividades = "";
        }
        return principaisAtividades;
    }

    public void setPrincipaisAtividades(String principaisAtividades) {
        this.principaisAtividades = principaisAtividades;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public String getDataAdmissao_Apresentar() {
        if (dataAdmissao == null) {
            return "";
        }
        return Uteis.getDataMesAnoConcatenado(dataAdmissao);
    }

    public String getAnoDataAdmissao_Apresentar() {
        return Uteis.getAnoData(dataAdmissao) + "";
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataDemissao() {
        return dataDemissao;
    }

    public String getDataDemissao_Apresentar() {
        if (dataDemissao == null) {
            return "Atual";
        }
        return Uteis.getDataMesAnoConcatenado(dataDemissao);
    }

    public String getAnoDataDemissao_Apresentar() {
        if (dataDemissao == null) {
            return "Atual";
        }
        return Uteis.getAnoData(dataDemissao) + "";
    }

    public void setDataDemissao(Date dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public Boolean getDesenharDataDemissao() {
        if (getEmpregoAtual()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public String getMotivoDesligamento() {
        if (motivoDesligamento == null) {
            motivoDesligamento = "";
        }
        return motivoDesligamento;
    }

    public void setMotivoDesligamento(String motivoDesligamento) {
        this.motivoDesligamento = motivoDesligamento;
    }
}
