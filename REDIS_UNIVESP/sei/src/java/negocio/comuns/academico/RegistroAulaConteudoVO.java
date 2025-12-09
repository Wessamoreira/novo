/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

/**
 *
 * @author Carlos
 */
public class RegistroAulaConteudoVO extends SuperParametroRelVO {
    private Date data;
    private String cargaHorariaStr;
    private String conteudo;
    private List<RegistroAulaConteudoVO> listaVerso1;
    private List<RegistroAulaConteudoVO> listaVerso2;

    public RegistroAulaConteudoVO() {
    }

    
    public void setListaConteudoVerso1(List<RegistroAulaConteudoVO> listaVerso1) {
        JRDataSource jr = new JRBeanArrayDataSource(listaVerso1.toArray());
        getParametros().put("listaConteudoVerso1", jr);
    }
    
    public void setListaConteudoVerso2(List<RegistroAulaConteudoVO> listaVerso2) {
        JRDataSource jr = new JRBeanArrayDataSource(listaVerso2.toArray());
        getParametros().put("listaConteudoVerso2", jr);
    }
    
    public List<RegistroAulaConteudoVO> getListaVerso1() {
        if (listaVerso1 == null) {
            listaVerso1 = new ArrayList(0);
        }
        return listaVerso1;
    }

    public void setListaVerso1(List<RegistroAulaConteudoVO> listaVerso1) {
        this.listaVerso1 = listaVerso1;
    }

    public List<RegistroAulaConteudoVO> getListaVerso2() {
        if (listaVerso2 == null) {
            listaVerso2 = new ArrayList(0);
        }
        return listaVerso2;
    }

    public void setListaVerso2(List<RegistroAulaConteudoVO> listaVerso2) {
        this.listaVerso2 = listaVerso2;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCargaHorariaStr() {
        if (cargaHorariaStr == null) {
            cargaHorariaStr = "";
        }
        return cargaHorariaStr;
    }

    public void setCargaHorariaStr(String cargaHorariaStr) {
        this.cargaHorariaStr = cargaHorariaStr;
    }

    public String getConteudo() {
        if (conteudo == null) {
            conteudo = "";
        }
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
