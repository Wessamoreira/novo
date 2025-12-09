/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.TipoSituacaoWorkflowEnum;

/**
 *
 * @author MarcoTulio
 */
public class WorkflowVO extends SuperVO {

    protected Integer codigo;
    protected String nome;
    protected TipoSituacaoWorkflowEnum tipoSituacaoWorkflow;
    protected String objetivo;
    protected Integer tempoMedioGerarAgenda;
    private Integer numeroSegundosAlertarUsuarioTempoMaximoInteracao;
    /** Atributo responsável por manter os objetos da classe <code>EtapaWorkflow</code>. */
    private List<EtapaWorkflowVO> etapaWorkflowVOs;
    private List<SituacaoProspectWorkflowVO> situacaoProspectWorkflowVOs;
    private Boolean livreAcessoEtapas;

    /**
     * Construtor padrão da classe <code>Workflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public WorkflowVO() {
        super();
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>EtapaWorkflow</code>. */
    public List<EtapaWorkflowVO> getEtapaWorkflowVOs() {
        if (etapaWorkflowVOs == null) {
            etapaWorkflowVOs = new ArrayList(0);
        }
        return (etapaWorkflowVOs);
    }

    /** Define Atributo responsável por manter os objetos da classe <code>EtapaWorkflow</code>. */
    public void setEtapaWorkflowVOs(List<EtapaWorkflowVO> etapaWorkflowVOs) {
        this.etapaWorkflowVOs = etapaWorkflowVOs;
    }

    public Integer getTempoMedioGerarAgenda() {
        if (tempoMedioGerarAgenda == null) {
            tempoMedioGerarAgenda = 0;
        }
        return (tempoMedioGerarAgenda);
    }

    public void setTempoMedioGerarAgenda(Integer tempoMedioGerarAgenda) {
        this.tempoMedioGerarAgenda = tempoMedioGerarAgenda;
    }

    public String getObjetivo() {
        if (objetivo == null) {
            return "";
        }
        return (objetivo);
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getTipoSituacaoWorkflow_Apresentar() {
         if (getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.EM_CONSTRUCAO)) {
            return "Em construção";
        } else if (getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.ATIVO)) {
            return "Ativo";
        }
        return "Inativo";
    }

    public TipoSituacaoWorkflowEnum getTipoSituacaoWorkflow() {
         if (tipoSituacaoWorkflow == null) {
            return TipoSituacaoWorkflowEnum.EM_CONSTRUCAO;
        }
        return tipoSituacaoWorkflow;
    }

    public void setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum tipoSituacaoWorkflow) {
        this.tipoSituacaoWorkflow = tipoSituacaoWorkflow;
    }

    

    public String getNome() {
        if (nome == null) {
            return "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public List<SituacaoProspectWorkflowVO> getSituacaoProspectWorkflowVOs() {
        if (situacaoProspectWorkflowVOs == null) {
            situacaoProspectWorkflowVOs = new ArrayList(0);
        }
        return situacaoProspectWorkflowVOs;
    }

    public void setSituacaoProspectWorkflowVOs(List<SituacaoProspectWorkflowVO> situacaoProspectWorkflowVOs) {
        this.situacaoProspectWorkflowVOs = situacaoProspectWorkflowVOs;
    }

    public Integer getNumeroSegundosAlertarUsuarioTempoMaximoInteracao() {
        if (numeroSegundosAlertarUsuarioTempoMaximoInteracao == null) {
            numeroSegundosAlertarUsuarioTempoMaximoInteracao = 0;
        }
        return numeroSegundosAlertarUsuarioTempoMaximoInteracao;
    }

    public void setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(Integer numeroSegundosAlertarUsuarioTempoMaximoInteracao) {
        this.numeroSegundosAlertarUsuarioTempoMaximoInteracao = numeroSegundosAlertarUsuarioTempoMaximoInteracao;
    }

    public Boolean getLivreAcessoEtapas() {
        if (livreAcessoEtapas == null) {
            livreAcessoEtapas = Boolean.FALSE;
        }
        return livreAcessoEtapas;
    }

    public void setLivreAcessoEtapas(Boolean livreAcessoEtapas) {
        this.livreAcessoEtapas = livreAcessoEtapas;
    }
}
