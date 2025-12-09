/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;
import java.util.Date;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.utilitarias.Uteis;

public class PainelGestorContaPagarMesAnoVO implements Serializable {

    private String mesAno;
    private Double provisaoPagarMes;
    private Double pagoDoMes;
    private Double pagoDoMesNoMes;
    private Double pagoNoMes;
    private Double saldoPagarMes;
    private Double totalVencidoMes;
    private Double pagoAtrazadoDeOutroMesNoMes;
    private Double pagoAdiantadoDeOutroMesNoMes;
    private String mesAnoApresentar;
    public static final long serialVersionUID = 1L;

    public String getMesAnoApresentar() {
        if ((mesAnoApresentar == null || mesAnoApresentar.isEmpty()) && !getMesAno().isEmpty()) {
            mesAnoApresentar = MesAnoEnum.getEnum(mesAno.substring(0, mesAno.indexOf("/"))).getMesAbreviado() + "/" + getMesAno().substring(getMesAno().length() - 2, getMesAno().length());
        }
        return mesAnoApresentar;
    }

    public Double getPagoAdiantadoDeOutroMesNoMes() {
        if (pagoAdiantadoDeOutroMesNoMes == null) {
            pagoAdiantadoDeOutroMesNoMes = 0.0;
        }
        return pagoAdiantadoDeOutroMesNoMes;
    }

    public void setPagoAdiantadoDeOutroMesNoMes(Double pagoAdiantadoDeOutroMesNoMes) {
        this.pagoAdiantadoDeOutroMesNoMes = pagoAdiantadoDeOutroMesNoMes;
    }

    public Double getPagoAtrazadoDeOutroMesNoMes() {
        if (pagoAtrazadoDeOutroMesNoMes == null) {
            pagoAtrazadoDeOutroMesNoMes = 0.0;
        }
        return pagoAtrazadoDeOutroMesNoMes;
    }

    public void setPagoAtrazadoDeOutroMesNoMes(Double pagoAtrazadoDeOutroMesNoMes) {
        this.pagoAtrazadoDeOutroMesNoMes = pagoAtrazadoDeOutroMesNoMes;
    }

    public Double getTaxaInadimplenciaNoMes() {
        /**
         * Este Valida a taxa de inadimplência do mês que já passou
         */
        if (getPagoDoMesNoMes() > 0
                && (Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) < Uteis.getMesData(new Date())
                && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) <= Uteis.getAnoData(new Date()))
                || Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) < Uteis.getAnoData(new Date())) {
            return Uteis.arrendondarForcando2CadasDecimais(100 - (getPagoDoMesNoMes() * 100 / getProvisaoPagarMes()));
        }
        /**
         * Este Valida a taxa de inadimplência do mês atual, portanto ele valida com base no total já vencido do mês
         */
        if (getTotalVencidoMes() > 0
                && Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) == Uteis.getMesData(new Date())
                && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) == Uteis.getAnoData(new Date())) {
            return Uteis.arrendondarForcando2CadasDecimais((getTotalVencidoMes() * 100 / getProvisaoPagarMes()));
        }
        return 0.0;
    }

    /**
     * Método responsável em retornar a taxa de inadimplecia no mês em questão nos dias atuais
     *
     * @return
     */
    public Double getTaxaInadimplenciaDoMes() {
        if (getTotalVencidoMes() > 0
                && (Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) <= Uteis.getMesData(new Date())
                && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) <= Uteis.getAnoData(new Date()))
                || Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) < Uteis.getAnoData(new Date())) {
            return Uteis.arrendondarForcando2CadasDecimais(getTotalVencidoMes() * 100 / getProvisaoPagarMes());
        }
        return 0.0;
    }

    public Double getPagoDoMesNoMes() {
        if (pagoDoMesNoMes == null) {
            pagoDoMesNoMes = 0.0;
        }
        return pagoDoMesNoMes;
    }

    public void setPagoDoMesNoMes(Double pagoDoMesNoMes) {
        this.pagoDoMesNoMes = pagoDoMesNoMes;
    }

    public String getMesAno() {
        if (mesAno == null) {
            mesAno = "";
        }
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public Double getPagoDoMes() {
        if (pagoDoMes == null) {
            pagoDoMes = 0.0;
        }
        return pagoDoMes;
    }

    public void setPagoDoMes(Double pagoDoMes) {
        this.pagoDoMes = pagoDoMes;
    }

    public Double getPagoNoMes() {
        if (pagoNoMes == null) {
            pagoNoMes = 0.0;
        }
        return pagoNoMes;
    }

    public void setPagoNoMes(Double pagoNoMes) {
        this.pagoNoMes = pagoNoMes;
    }

    public Double getProvisaoPagarMes() {
        if (provisaoPagarMes == null) {
            provisaoPagarMes = 0.0;
        }
        return provisaoPagarMes;
    }

    public void setProvisaoPagarMes(Double provisaoPagarMes) {
        this.provisaoPagarMes = provisaoPagarMes;
    }

    public Double getSaldoPagarMes() {
        if (saldoPagarMes == null) {
            saldoPagarMes = 0.0;
        }
        return saldoPagarMes;
    }

    public void setSaldoPagarMes(Double saldoPagarMes) {
        this.saldoPagarMes = saldoPagarMes;
    }

    public Double getTotalVencidoMes() {
        if (totalVencidoMes == null) {
            totalVencidoMes = 0.0;
        }
        return totalVencidoMes;
    }

    public void setTotalVencidoMes(Double totalVencidoMes) {
        this.totalVencidoMes = totalVencidoMes;
    }
}
