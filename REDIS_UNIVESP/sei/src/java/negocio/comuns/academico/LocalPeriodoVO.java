/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class LocalPeriodoVO {

    private String local;
    private Date periodoInicio;
    private Date periodoFim;

    public String getLocal() {
        if (local == null) {
            local = "";
        }
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }
    
    public String getPeriodoInicio_Apresentar() {
        if (periodoInicio != null) {
            return Uteis.getData(periodoInicio);
        }
        return "";
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFim() {
        return periodoFim;
    }

    public String getPeriodoFim_Apresentar() {
        if (periodoFim != null) {
            return Uteis.getData(periodoFim);
        }
        return "";
    }

    public void setPeriodoFim(Date periodoFim) {
        this.periodoFim = periodoFim;
    }
}
