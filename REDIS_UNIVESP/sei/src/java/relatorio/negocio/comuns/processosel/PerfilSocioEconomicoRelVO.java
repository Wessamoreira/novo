/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Otimize-TI
 */
public class PerfilSocioEconomicoRelVO {
    protected String unidadeEnsino;
    protected List sexo;
    protected List rendaMensalFamiliar;
    protected List rendaMensalCandidato;
    protected List comoPretendeManterCurso;
    protected List grauEscolaridadePai;
    protected List grauEscolaridadeMae;
    protected List escolaCursouEnsinoMedio;
    protected List quantidadeFaculdadePrestVestibular;
    protected List conhecimentoProcSeletivo;

    public PerfilSocioEconomicoRelVO(){
    
    }
    
    public void inicializarDados(){
        setComoPretendeManterCurso(new ArrayList(0));
        setSexo(new ArrayList(0));
        setRendaMensalCandidato(new ArrayList(0));
        setRendaMensalFamiliar(new ArrayList(0));
        setGrauEscolaridadeMae(new ArrayList(0));
        setGrauEscolaridadePai(new ArrayList(0));
        setEscolaCursouEnsinoMedio(new ArrayList(0));
        setQuantidadeFaculdadePrestVestibular(new ArrayList(0));
        setConhecimentoProcSeletivo(new ArrayList(0));
        setUnidadeEnsino("");
    }
    
    public List getComoPretendeManterCurso() {
        return comoPretendeManterCurso;
    }

    public void setComoPretendeManterCurso(List comoPretendeManterCurso) {
        this.comoPretendeManterCurso = comoPretendeManterCurso;
    }

    public List getConhecimentoProcSeletivo() {
        return conhecimentoProcSeletivo;
    }

    public void setConhecimentoProcSeletivo(List conhecimentoProcSeletivo) {
        this.conhecimentoProcSeletivo = conhecimentoProcSeletivo;
    }

    public List getEscolaCursouEnsinoMedio() {
        return escolaCursouEnsinoMedio;
    }

    public void setEscolaCursouEnsinoMedio(List escolaCursouEnsinoMedio) {
        this.escolaCursouEnsinoMedio = escolaCursouEnsinoMedio;
    }

    public List getGrauEscolaridadeMae() {
        return grauEscolaridadeMae;
    }

    public void setGrauEscolaridadeMae(List grauEscolaridadeMae) {
        this.grauEscolaridadeMae = grauEscolaridadeMae;
    }

    public List getGrauEscolaridadePai() {
        return grauEscolaridadePai;
    }

    public void setGrauEscolaridadePai(List grauEscolaridadePai) {
        this.grauEscolaridadePai = grauEscolaridadePai;
    }

    public List getQuantidadeFaculdadePrestVestibular() {
        return quantidadeFaculdadePrestVestibular;
    }

    public void setQuantidadeFaculdadePrestVestibular(List quantidadeFaculdadePrestVestibular) {
        this.quantidadeFaculdadePrestVestibular = quantidadeFaculdadePrestVestibular;
    }

    public List getRendaMensalCandidato() {
        return rendaMensalCandidato;
    }

    public void setRendaMensalCandidato(List rendaMensalCandidato) {
        this.rendaMensalCandidato = rendaMensalCandidato;
    }

    public List getRendaMensalFamiliar() {
        return rendaMensalFamiliar;
    }

    public void setRendaMensalFamiliar(List rendaMensalFamiliar) {
        this.rendaMensalFamiliar = rendaMensalFamiliar;
    }

    public List getSexo() {
        return sexo;
    }

    public void setSexo(List sexo) {
        this.sexo = sexo;
    }

    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
    
    public JRDataSource getListaSexo(){
        JRDataSource jr = new JRBeanArrayDataSource(getSexo().toArray());
        return jr;
    }
    
    public JRDataSource getListaRendaMensaFamiliar(){
        JRDataSource jr = new JRBeanArrayDataSource(getRendaMensalFamiliar().toArray());
        return jr;
    }
    
    public JRDataSource getListaRendaMensaCandidato(){
        JRDataSource jr = new JRBeanArrayDataSource(getRendaMensalCandidato().toArray());
        return jr;
    }
    
    public JRDataSource getListaComoPretendeManterCurso(){
        JRDataSource jr = new JRBeanArrayDataSource(getComoPretendeManterCurso().toArray());
        return jr;
    }
    
    public JRDataSource getListaConhecimentoProcSeletivo(){
        JRDataSource jr = new JRBeanArrayDataSource(getConhecimentoProcSeletivo().toArray());
        return jr;
    }
    
    public JRDataSource getListaEscolaCursouEnsinoMedio(){
        JRDataSource jr = new JRBeanArrayDataSource(getEscolaCursouEnsinoMedio().toArray());
        return jr;
    }
    
    public JRDataSource getListaGrauEscolaridadeMae(){
        JRDataSource jr = new JRBeanArrayDataSource(getGrauEscolaridadeMae().toArray());
        return jr;
    }
    
    public JRDataSource getListaGrauEscolaridadePai(){
        JRDataSource jr = new JRBeanArrayDataSource(getGrauEscolaridadePai().toArray());
        return jr;
    }
    
    public JRDataSource getListaQuantidadeFaculdadePrestVestibular(){
        JRDataSource jr = new JRBeanArrayDataSource(getQuantidadeFaculdadePrestVestibular().toArray());
        return jr;
    }
    

    
}
