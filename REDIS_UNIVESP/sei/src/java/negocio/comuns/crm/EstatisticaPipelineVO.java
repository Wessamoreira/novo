/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.Map;

import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoUtilizacaoCorEnum;

/**
 *
 * @author RODRIGO
 */
public class EstatisticaPipelineVO {
    
    private String nomeSituacao;
    private Integer quantidadeProspect;
    private Double efetivacaoVendaHistorica;
    private Integer projecaoEfetivacao;
    private Map<TipoUtilizacaoCorEnum,String> cores;
    private String corFundo;
    private String corTexto;
    private Integer altura;

    public Double getEfetivacaoVendaHistorica() {
        return efetivacaoVendaHistorica;
    }

    public void setEfetivacaoVendaHistorica(Double efetivacaoVendaHistorica) {
        this.efetivacaoVendaHistorica = efetivacaoVendaHistorica;
    }

    public String getNomeSituacao() {
        return nomeSituacao;
    }

    public void setNomeSituacao(String nomeSituacao) {
        this.nomeSituacao = nomeSituacao;
    }

    public Integer getProjecaoEfetivacao() {
        return projecaoEfetivacao;
    }

    public void setProjecaoEfetivacao(Integer projecaoEfetivacao) {
        this.projecaoEfetivacao = projecaoEfetivacao;
    }

    public Integer getQuantidadeProspect() {
        return quantidadeProspect;
    }

    public void setQuantidadeProspect(Integer quantidadeProspect) {
        this.quantidadeProspect = quantidadeProspect;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public Map<TipoUtilizacaoCorEnum, String> getCores() {
        if(cores == null){
            cores = UteisJSF.obterCoresAleatoriasFundoETexto();
        }
        return cores;
    }

    public void setCores(Map<TipoUtilizacaoCorEnum, String> cores) {
        this.cores = cores;
    }

    public String getCorTexto(){
        if(corTexto == null){
            corTexto = getCores().get(TipoUtilizacaoCorEnum.TEXTO);
        }
        return corTexto;
    }
    
    public String getCorFundo(){
        if(corFundo == null){
        corFundo = getCores().get(TipoUtilizacaoCorEnum.FUNDO);
        }
        return corFundo;
    }

    public void setCorFundo(String corFundo) {
        this.corFundo = corFundo;
    }

    public void setCorTexto(String corTexto) {
        this.corTexto = corTexto;
    }
    
    
}
