/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.biblioteca;

/**
 *
 * @author OTIMIZE-09
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class AcervoCabecalhoVO implements Serializable {

    private String biblioteca;
    private String secao;
    private String local;
    private String nivelBibliografico;
    private String classificacao;
    private List<AcervoVO> acervos;
    private Integer totalGeral;
    public static final long serialVersionUID = 1L;

    public Integer getTotalGeral() {
        if (totalGeral == null) {
            totalGeral = 0;
        }
        return totalGeral;
    }

    public void setTotalGeral(Integer totalGeral) {
        this.totalGeral = totalGeral;
    }

    public JRDataSource getListaAcervosRelVOJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getAcervos().toArray());
        return jr;
    }

    public List<AcervoVO> getAcervos() {
        if (acervos == null) {
            acervos = new ArrayList<AcervoVO>(0);
        }
        return acervos;
    }

    public void setAcervos(List<AcervoVO> acervos) {
        this.acervos = acervos;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getNivelBibliografico() {
        return nivelBibliografico;
    }

    public void setNivelBibliografico(String nivelBibliografico) {
        this.nivelBibliografico = nivelBibliografico;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }
}
