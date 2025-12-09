/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
import negocio.comuns.crm.enumerador.TempoContatoMetaEnum;

/**
 *
 * @author RODRIGO
 */
public class DadosGeraisCampanhaPorFuncionarioVO {
    
    private Integer codigoUsuario;    
    private String nomeUsuario;    
    private NivelExperienciaCargoEnum nivelExperiencia; 
    private String diaCampanha;
    
    private Integer quantidadeContatoDia;
    private Integer quantidadeContatoDiaAtual;
    private Integer quantidadeTotalContato;
    private Integer quantidadeTotalContatoEsperado;
    
    private Integer quantidadeContatoSucessoDia;
    private Integer quantidadeContatoSucessoDiaAtual;
    private Integer quantidadeTotalContatoSucesso;    
    private Integer quantidadeTotalContatoSucessoEsperado;    
    
    private Integer quantidadeCaptacaoProspectDia;
    private Integer quantidadeCaptacaoProspectDiaAtual;
    private Integer quantidadeTotalCaptacaoProspect;
    private Integer quantidadeTotalCaptacaoProspectEsperado;
    
    public TempoContatoMetaEnum getTempoContatoMetaEnumGeral(){
        if(getQuantidadeTotalContatoEsperado() > getQuantidadeTotalContato()){
            return TempoContatoMetaEnum.ABAIXO_MEDIA;
        }
        if(getQuantidadeTotalContatoEsperado() < getQuantidadeTotalContato()){
            return TempoContatoMetaEnum.ACIMA_MEDIA;
        }        
        return TempoContatoMetaEnum.NA_MEDIA;        
    }
    public TempoContatoMetaEnum getTempoContatoMetaEnumDia(){
        if(getQuantidadeContatoDia() > getQuantidadeContatoDiaAtual()){
            return TempoContatoMetaEnum.ABAIXO_MEDIA;
        }
        if(getQuantidadeContatoDia() < getQuantidadeContatoDiaAtual()){
            return TempoContatoMetaEnum.ACIMA_MEDIA;
        }        
        return TempoContatoMetaEnum.NA_MEDIA;        
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Integer getQuantidadeCaptacaoProspectDia() {
        return quantidadeCaptacaoProspectDia;
    }

    public void setQuantidadeCaptacaoProspectDia(Integer quantidadeCaptacaoProspectDia) {
        this.quantidadeCaptacaoProspectDia = quantidadeCaptacaoProspectDia;
    }

    public Integer getQuantidadeCaptacaoProspectDiaAtual() {
        return quantidadeCaptacaoProspectDiaAtual;
    }

    public void setQuantidadeCaptacaoProspectDiaAtual(Integer quantidadeCaptacaoProspectDiaAtual) {
        this.quantidadeCaptacaoProspectDiaAtual = quantidadeCaptacaoProspectDiaAtual;
    }

    public Integer getQuantidadeContatoDia() {
        return quantidadeContatoDia;
    }

    public void setQuantidadeContatoDia(Integer quantidadeContatoDia) {
        this.quantidadeContatoDia = quantidadeContatoDia;
    }

    public Integer getQuantidadeContatoDiaAtual() {
        return quantidadeContatoDiaAtual;
    }

    public void setQuantidadeContatoDiaAtual(Integer quantidadeContatoDiaAtual) {
        this.quantidadeContatoDiaAtual = quantidadeContatoDiaAtual;
    }

    public Integer getQuantidadeContatoSucessoDia() {
        return quantidadeContatoSucessoDia;
    }

    public void setQuantidadeContatoSucessoDia(Integer quantidadeContatoSucessoDia) {
        this.quantidadeContatoSucessoDia = quantidadeContatoSucessoDia;
    }

    public Integer getQuantidadeContatoSucessoDiaAtual() {
        return quantidadeContatoSucessoDiaAtual;
    }

    public void setQuantidadeContatoSucessoDiaAtual(Integer quantidadeContatoSucessoDiaAtual) {
        this.quantidadeContatoSucessoDiaAtual = quantidadeContatoSucessoDiaAtual;
    }

    public Integer getQuantidadeTotalCaptacaoProspect() {
        return quantidadeTotalCaptacaoProspect;
    }

    public void setQuantidadeTotalCaptacaoProspect(Integer quantidadeTotalCaptacaoProspect) {
        this.quantidadeTotalCaptacaoProspect = quantidadeTotalCaptacaoProspect;
    }

    public Integer getQuantidadeTotalCaptacaoProspectEsperado() {
        return quantidadeTotalCaptacaoProspectEsperado;
    }

    public void setQuantidadeTotalCaptacaoProspectEsperado(Integer quantidadeTotalCaptacaoProspectEsperado) {
        this.quantidadeTotalCaptacaoProspectEsperado = quantidadeTotalCaptacaoProspectEsperado;
    }

    public Integer getQuantidadeTotalContato() {
        return quantidadeTotalContato;
    }

    public void setQuantidadeTotalContato(Integer quantidadeTotalContato) {
        this.quantidadeTotalContato = quantidadeTotalContato;
    }

    public Integer getQuantidadeTotalContatoEsperado() {
        return quantidadeTotalContatoEsperado;
    }

    public void setQuantidadeTotalContatoEsperado(Integer quantidadeTotalContatoEsperado) {
        this.quantidadeTotalContatoEsperado = quantidadeTotalContatoEsperado;
    }

    public Integer getQuantidadeTotalContatoSucesso() {
        return quantidadeTotalContatoSucesso;
    }

    public void setQuantidadeTotalContatoSucesso(Integer quantidadeTotalContatoSucesso) {
        this.quantidadeTotalContatoSucesso = quantidadeTotalContatoSucesso;
    }

    public Integer getQuantidadeTotalContatoSucessoEsperado() {
        return quantidadeTotalContatoSucessoEsperado;
    }

    public void setQuantidadeTotalContatoSucessoEsperado(Integer quantidadeTotalContatoSucessoEsperado) {
        this.quantidadeTotalContatoSucessoEsperado = quantidadeTotalContatoSucessoEsperado;
    }

   

  
    public NivelExperienciaCargoEnum getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(NivelExperienciaCargoEnum nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public String getDiaCampanha() {
        return diaCampanha;
    }

    public void setDiaCampanha(String diaCampanha) {
        this.diaCampanha = diaCampanha;
    }
    
    
    
}
