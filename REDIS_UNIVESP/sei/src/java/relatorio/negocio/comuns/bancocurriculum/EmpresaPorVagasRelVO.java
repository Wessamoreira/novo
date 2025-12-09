/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.financeiro.ContatoParceiroVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author rogerio.gomes
 */
public class EmpresaPorVagasRelVO {

    private String estado;
    private String cidade;
    private String telefoneEmpresa;
    private String nomeParceiro;
    private Integer vagasAbertas;
    private Integer vagasExpiradas;
    private Integer vagasEncerradas;
    private Integer contratados;
    private Integer total;
    private Integer codigoParceiro;
    private Boolean apresentarContatos;
    private List<ContatoParceiroVO> contatoParceiroVOs;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomeParceiro() {
        return nomeParceiro;
    }

    public void setNomeParceiro(String nomeParceiro) {
        this.nomeParceiro = nomeParceiro;
    }

    public Integer getTotal() {
        return getVagasAbertas() + getVagasEncerradas() + getVagasExpiradas();
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getVagasAbertas() {
        if (vagasAbertas == null) {
            vagasAbertas = 0;
        }
        return vagasAbertas;
    }

    public void setVagasAbertas(Integer vagasAbertas) {
        this.vagasAbertas = vagasAbertas;
    }

    public Integer getVagasEncerradas() {
        if (vagasEncerradas == null) {
            vagasEncerradas = 0;
        }
        return vagasEncerradas;
    }

    public void setVagasEncerradas(Integer vagasEncerradas) {
        this.vagasEncerradas = vagasEncerradas;
    }

    public Integer getVagasExpiradas() {
        if (vagasExpiradas == null) {
            vagasExpiradas = 0;
        }
        return vagasExpiradas;
    }

    public void setVagasExpiradas(Integer vagasExpiradas) {
        this.vagasExpiradas = vagasExpiradas;
    }

    public Integer getCodigoParceiro() {
        return codigoParceiro;
    }

    public void setCodigoParceiro(Integer codigoParceiro) {
        this.codigoParceiro = codigoParceiro;
    }

    public List<ContatoParceiroVO> getContatoParceiroVOs() {
        if (contatoParceiroVOs == null) {
            contatoParceiroVOs = new ArrayList<ContatoParceiroVO>(0);
        }
        return contatoParceiroVOs;
    }

    public JRDataSource getListaContatoParceiroVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getContatoParceiroVOs().toArray());
        return jr;
    }

    public void setContatoParceiroVOs(List<ContatoParceiroVO> getContatoParceiroVOs) {
        this.contatoParceiroVOs = getContatoParceiroVOs;
    }

    public Integer getContratados() {
        if (contratados == null) {
            contratados = 0;
        }
        return contratados;
    }

    public void setContratados(Integer contratados) {
        this.contratados = contratados;
    }

    public Boolean getApresentarContatos() {
        if (apresentarContatos == null) {
            apresentarContatos = false;
        }
        return apresentarContatos;
    }

    public void setApresentarContatos(Boolean apresentarContatos) {
        this.apresentarContatos = apresentarContatos;
    }

    public String getTelefoneEmpresa() {
        if (telefoneEmpresa == null) {
            telefoneEmpresa = "";
        }
        return telefoneEmpresa;
    }

    public void setTelefoneEmpresa(String telefoneEmpresa) {
        this.telefoneEmpresa = telefoneEmpresa;
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
}
