/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author RODRIGO
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
	 propOrder = {"codigoEtapa", "nomeEtapa", "nivelEtapa", 
         "cor", "tempoMedioAtendimento","tempoTotalAtendimento", 
         "totalPessoasAtendidas", "etapasSucessoras"}	        
	)
public class EstatisticaPorEtapaCampanhaVO implements Serializable {
    
       private static final long serialVersionUID = 1L;
    
    private Integer codigoEtapa;
    private String nomeEtapa;
    private String tempoMedioAtendimento;
    private String tempoTotalAtendimento;
    private String cor;
    private Integer totalPessoasAtendidas;
    private Integer nivelEtapa;           
    private Integer[] etapasSucessoras;
    

    public Integer[] getEtapasSucessoras() {
        return etapasSucessoras;
    }

    public void setEtapasSucessoras(Integer[] etapasSucessoras) {
        this.etapasSucessoras = etapasSucessoras;
    }

    public Integer getCodigoEtapa() {
        return codigoEtapa;
    }

    public void setCodigoEtapa(Integer codigoEtapa) {
        this.codigoEtapa = codigoEtapa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getNivelEtapa() {
        return nivelEtapa;
    }

    public void setNivelEtapa(Integer nivelEtapa) {
        this.nivelEtapa = nivelEtapa;
    }

    public String getNomeEtapa() {
        return nomeEtapa;
    }

    public void setNomeEtapa(String nomeEtapa) {
        this.nomeEtapa = nomeEtapa;
    }

    public String getTempoMedioAtendimento() {
        return tempoMedioAtendimento;
    }

    public void setTempoMedioAtendimento(String tempoMedioAtendimento) {
        this.tempoMedioAtendimento = tempoMedioAtendimento;
    }

    public String getTempoTotalAtendimento() {
        return tempoTotalAtendimento;
    }

    public void setTempoTotalAtendimento(String tempoTotalAtendimento) {
        this.tempoTotalAtendimento = tempoTotalAtendimento;
    }

    public Integer getTotalPessoasAtendidas() {
        return totalPessoasAtendidas;
    }

    public void setTotalPessoasAtendidas(Integer totalPessoasAtendidas) {
        this.totalPessoasAtendidas = totalPessoasAtendidas;
    }

   
    
    public String getEstatisticaTempo(){
        return getNomeEtapa()+" - "+getTempoMedioAtendimento()+"m";
    }
    
    public String getEstatisticaQuantidade(){
        return getNomeEtapa()+" - "+getTotalPessoasAtendidas();
    }
    
    public String getEstatisticaTempoQuantidade(){
        return getNomeEtapa()+" - "+getTempoMedioAtendimento()+"m ("+getTotalPessoasAtendidas()+")";
    }

   
    
}
