/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

//Usado para que imprima mais de uma folha em papel a4 como desejado
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaRelVOSubReport {

    private String nomeResponsavel;
    private Date dataAcordoExtenso;
    private String nomeAtendente;
    private String nomeEmpresa;
    private String mantenedora;
    private String ufEmpresa;
    private String cidadeEmpresa;
    private List<TermoReconhecimentoDividaRelTituloTextoFixoVO> termoReconhecimentoDividaRelTituloTextoFixoVOs;

    public String getMantenedora() {
        if (mantenedora == null) {
            mantenedora = "";
        }
        return mantenedora;
    }

    public void setMantenedora(String mantenedora) {
        this.mantenedora = mantenedora;
    }

    public String getNomeAtendente() {
        if (nomeAtendente == null) {
            nomeAtendente = "";
        }
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
    }

    public Date getDataAcordoExtenso() {
        if (dataAcordoExtenso == null) {
            dataAcordoExtenso = new Date();
        }
        return dataAcordoExtenso;
    }

    public void setDataAcordoExtenso(Date dataAcordoExtenso) {
        this.dataAcordoExtenso = dataAcordoExtenso;
    }

    public String getNomeResponsavel() {
        if (nomeResponsavel == null) {
            nomeResponsavel = "";
        }
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
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

    public String getUfEmpresa() {
        if (ufEmpresa == null) {
            ufEmpresa = "";
        }
        return ufEmpresa;
    }

    public void setUfEmpresa(String ufEmpresa) {
        this.ufEmpresa = ufEmpresa;
    }

    public String getCidadeEmpresa() {
        if (cidadeEmpresa == null) {
            cidadeEmpresa = "";
        }
        return cidadeEmpresa;
    }

    public void setCidadeEmpresa(String cidadeEmpresa) {
        this.cidadeEmpresa = cidadeEmpresa;
    }

    public List<TermoReconhecimentoDividaRelTituloTextoFixoVO> getTermoReconhecimentoDividaRelTituloTextoFixoVOs() {
        if (termoReconhecimentoDividaRelTituloTextoFixoVOs == null) {
            termoReconhecimentoDividaRelTituloTextoFixoVOs = new ArrayList<TermoReconhecimentoDividaRelTituloTextoFixoVO>(0);
        }
        return termoReconhecimentoDividaRelTituloTextoFixoVOs;
    }

    public void setTermoReconhecimentoDividaRelTituloTextoFixoVOs(List<TermoReconhecimentoDividaRelTituloTextoFixoVO> termoReconhecimentoDividaRelTituloTextoFixoVOs) {
        this.termoReconhecimentoDividaRelTituloTextoFixoVOs = termoReconhecimentoDividaRelTituloTextoFixoVOs;
    }

    public JRDataSource getTermoReconhecimentoDividaRelTituloTextoFixoVOsJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getTermoReconhecimentoDividaRelTituloTextoFixoVOs().toArray());
        return jr;
    }
}
