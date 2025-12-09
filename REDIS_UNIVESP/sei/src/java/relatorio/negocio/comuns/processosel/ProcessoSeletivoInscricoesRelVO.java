/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Rodrigo Araújo
 */
public class ProcessoSeletivoInscricoesRelVO {

    protected Integer codigo;
    protected String descricao;
    protected Date dataInicio;
    protected Date dataFim;
    protected Date dataProva;
    protected String documentacaoObrigatoria;
    protected String requisitosGerais;
    protected String nivelEducacional;
    protected String horarioProva;
    protected Double mediaMinimaAprovacao;

    protected List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> processoSeletivoInscricoes_UnidadeEnsinoRelVO;
    private List processoSeletivoInscricoes_ExtratoRel;

    public ProcessoSeletivoInscricoesRelVO() {
        inicializarDados();
    }

    public void inicializarDados() {
        codigo = 0;
        descricao = "";
        dataInicio = new Date();
        dataFim = new Date();
        dataProva= new Date();
        documentacaoObrigatoria = "";
        requisitosGerais = "";
        nivelEducacional = "";
        mediaMinimaAprovacao = 0.0;
        horarioProva = "";
        processoSeletivoInscricoes_UnidadeEnsinoRelVO = new ArrayList(0);
    }

    public JRDataSource getListaProcessoSeletivoInscricoes_UnidadeEnsinoRelVO() {
        JRDataSource jr = new JRBeanArrayDataSource(getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO().toArray());
        return jr;
    }

    public JRDataSource getListaProcessoSeletivoInscricoes_ExtratoRelVO() {
        JRDataSource jr = new JRBeanArrayDataSource(getProcessoSeletivoInscricoes_ExtratoRel().toArray());
        return jr;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataProva() {
        return dataProva;
    }

    public void setDataProva(Date dataProva) {
        this.dataProva = dataProva;
    }

    public String getDocumentacaoObrigatoria() {
        return documentacaoObrigatoria;
    }

    public void setDocumentacaoObrigatoria(String documentacaoObrigatoria) {
        this.documentacaoObrigatoria = documentacaoObrigatoria;
    }

    public String getHorarioProva() {
        return horarioProva;
    }

    public void setHorarioProva(String horarioProva) {
        this.horarioProva = horarioProva;
    }

    public Double getMediaMinimaAprovacao() {
        return mediaMinimaAprovacao;
    }

    public void setMediaMinimaAprovacao(Double mediaMinimaAprovacao) {
        this.mediaMinimaAprovacao = mediaMinimaAprovacao;
    }

    public String getNivelEducacional_Apresentar() {
        if (nivelEducacional.equals("BA")) {
            return "Ensino Básico";
        }
        if (nivelEducacional.equals("PO")) {
            return "Pós-graduação";
        }
        if (nivelEducacional.equals("SU")) {
            return "Ensino Superior";
        }
        if (nivelEducacional.equals("ME")) {
            return "Ensino Médio";
        }
        if (nivelEducacional.equals("TO")) {
        	return "Todos";
        }		
        return (nivelEducacional);
    }

    public String getNivelEducacional() {
        return nivelEducacional;
    }



    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

    public String getRequisitosGerais() {
        return requisitosGerais;
    }

    public void setRequisitosGerais(String requisitosGerais) {
        this.requisitosGerais = requisitosGerais;
    }

    public List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO() {
        return processoSeletivoInscricoes_UnidadeEnsinoRelVO;
    }

    public void setProcessoSeletivoInscricoes_UnidadeEnsinoRelVO(List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> processoSeletivoInscricoes_UnidadeEnsinoRelVO) {
        this.processoSeletivoInscricoes_UnidadeEnsinoRelVO = processoSeletivoInscricoes_UnidadeEnsinoRelVO;
    }

    /**
     * @return the processoSeletivoInscricoes_ExtratoRel
     */
    public List getProcessoSeletivoInscricoes_ExtratoRel() {
        if(processoSeletivoInscricoes_ExtratoRel == null){
            processoSeletivoInscricoes_ExtratoRel = new ArrayList(0);
        }
        return processoSeletivoInscricoes_ExtratoRel;
    }

    /**
     * @param processoSeletivoInscricoes_ExtratoRel the processoSeletivoInscricoes_ExtratoRel to set
     */
    public void setProcessoSeletivoInscricoes_ExtratoRel(List processoSeletivoInscricoes_ExtratoRel) {
        this.processoSeletivoInscricoes_ExtratoRel = processoSeletivoInscricoes_ExtratoRel;
    }


}