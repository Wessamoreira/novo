package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class CarteirinhaEstudantilVO implements Serializable{

    private List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilVOs;
    private List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilAuxVOs;
    private String versoCarteirinhaEstudantil;    

    public List<IdentificacaoEstudantilVO> getListaIdentificacaoEstudantilVOs() {
        if (listaIdentificacaoEstudantilVOs == null) {
            listaIdentificacaoEstudantilVOs = new ArrayList<IdentificacaoEstudantilVO>(0);
        }
        return listaIdentificacaoEstudantilVOs;
    }

    public void setListaIdentificacaoEstudantilVOs(List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilVOs) {
        this.listaIdentificacaoEstudantilVOs = listaIdentificacaoEstudantilVOs;
    }

    public List<IdentificacaoEstudantilVO> getListaIdentificacaoEstudantilAuxVOs() {
        if (listaIdentificacaoEstudantilAuxVOs == null) {
            listaIdentificacaoEstudantilAuxVOs = new ArrayList<IdentificacaoEstudantilVO>(0);
        }
        return listaIdentificacaoEstudantilAuxVOs;
    }

    public void setListaIdentificacaoEstudantilAuxVOs(List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilAuxVOs) {
        this.listaIdentificacaoEstudantilAuxVOs = listaIdentificacaoEstudantilAuxVOs;
    }

    public JRDataSource getListaiIdentificacaoEstudantilVOsJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaIdentificacaoEstudantilVOs().toArray());
        return jr;
    }

    public JRDataSource getListaiIdentificacaoEstudantilVOsAuxJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaIdentificacaoEstudantilAuxVOs().toArray());
        return jr;
    }

    public String getVersoCarteirinhaEstudantil() {
        if (versoCarteirinhaEstudantil == null) {
            versoCarteirinhaEstudantil = "";
        }
        return versoCarteirinhaEstudantil;
    }

    public void setVersoCarteirinhaEstudantil(String versoCarteirinhaEstudantil) {
        this.versoCarteirinhaEstudantil = versoCarteirinhaEstudantil;
    }
}
