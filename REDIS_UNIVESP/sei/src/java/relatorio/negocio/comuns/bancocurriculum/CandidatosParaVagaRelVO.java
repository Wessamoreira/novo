/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author PEDRO
 */
public class CandidatosParaVagaRelVO {

    private Integer codigoEmpresa;
    private String empresa;
    private Integer codigoVaga;
    private String vaga;
    private Integer numeroVagas;
    private String situacaoVaga;
    private List<CandidatosParaVagaRelVOSub> listaCandidatosParaVagaRelVOSub;

    public Integer getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(Integer codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSituacaoVaga() {
        if (situacaoVaga.equals("CA")) {
            return "Cancelado";
        }
        if (situacaoVaga.equals("AT")) {
            return "Ativado";
        }
        if (situacaoVaga.equals("EX")) {
            return "Expirada";
        }
        if (situacaoVaga.equals("EN")) {
            return "Encerrada";
        }
        return "Em Construção";
    }

    public void setSituacaoVaga(String situacaoVaga) {
        this.situacaoVaga = situacaoVaga;
    }

    public String getVaga() {
        return vaga;
    }

    public void setVaga(String vaga) {
        this.vaga = vaga;
    }

    public List<CandidatosParaVagaRelVOSub> getListaCandidatosParaVagaRelVOSub() {
        if (listaCandidatosParaVagaRelVOSub == null) {
            listaCandidatosParaVagaRelVOSub = new ArrayList<CandidatosParaVagaRelVOSub>(0);
        }
        return listaCandidatosParaVagaRelVOSub;
    }

    public void setListaCandidatosParaVagaRelVOSub(List<CandidatosParaVagaRelVOSub> listaCandidatosParaVagaRelVOSub) {
        this.listaCandidatosParaVagaRelVOSub = listaCandidatosParaVagaRelVOSub;
    }

    public JRDataSource getListaCandidatosParaVagaRelVOSubJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaCandidatosParaVagaRelVOSub().toArray());
        return jr;
    }

    public Integer getCodigoVaga() {
        if (codigoVaga == null) {
            codigoVaga = 0;
        }
        return codigoVaga;
    }

    public void setCodigoVaga(Integer codigoVaga) {
        this.codigoVaga = codigoVaga;
    }

    
    public Integer getNumeroVagas() {
        return numeroVagas;
    }

    
    public void setNumeroVagas(Integer numerovagas) {
        this.numeroVagas = numerovagas;
    }
    
    
}
