/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 *  
 * @author MarcoTulio
 */
public class EtapaWorkflowVO extends SuperVO {

    protected Integer codigo;
    protected WorkflowVO workflow;
    protected String nome;
    protected String script;
    protected String motivo;
    protected String cor;
    protected String corFonte;
    protected Long tempoMinimo;
    protected Long tempoMaximo;
    protected Boolean permitirFinalizarDessaEtapa;
    protected Boolean obrigatorioInformarObservacao;
    protected Integer nivelApresentacao;
    private String StringConcatenadaNomeNivelApresentacao;
    protected SituacaoProspectWorkflowVO situacaoDefinirProspectFinal;
    private List<EtapaWorkflowAntecedenteVO> etapaWorkflowAntecedenteVOs;
    private List<CursoEtapaWorkflowVO> cursoEtapaWorkflowVOs;
    private List<ArquivoEtapaWorkflowVO> arquivoEtapaWorkflowVOs;

    /**
     * Construtor padrão da classe <code>EtapaWorkflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public EtapaWorkflowVO() {
        super();
    }

    public Integer getNivelApresentacao() {
        if (nivelApresentacao == null) {
            nivelApresentacao = 0;
        }
        return (nivelApresentacao);
    }

    public void setNivelApresentacao(Integer nivelApresentacao) {
        this.nivelApresentacao = nivelApresentacao;
    }

    public Boolean getObrigatorioInformarObservacao() {
        if (obrigatorioInformarObservacao == null) {
            obrigatorioInformarObservacao = Boolean.FALSE;
        }
        return (obrigatorioInformarObservacao);
    }

    public Boolean isObrigatorioInformarObservacao() {
        if (obrigatorioInformarObservacao == null) {
            obrigatorioInformarObservacao = Boolean.FALSE;
        }
        return (obrigatorioInformarObservacao);
    }

    public void setObrigatorioInformarObservacao(Boolean obrigatorioInformarObservacao) {
        this.obrigatorioInformarObservacao = obrigatorioInformarObservacao;
    }

    public Boolean getPermitirFinalizarDessaEtapa() {
        if (permitirFinalizarDessaEtapa == null) {
            permitirFinalizarDessaEtapa = Boolean.FALSE;
        }
        return (permitirFinalizarDessaEtapa);
    }

    public Boolean isPermitirFinalizarDessaEtapa() {
        if (permitirFinalizarDessaEtapa == null) {
            permitirFinalizarDessaEtapa = Boolean.FALSE;
        }
        return (permitirFinalizarDessaEtapa);
    }

    public void setPermitirFinalizarDessaEtapa(Boolean permitirFinalizarDessaEtapa) {
        this.permitirFinalizarDessaEtapa = permitirFinalizarDessaEtapa;
    }

    public Long getTempoMaximo() {
        if (tempoMaximo == null) {
            tempoMaximo = 0L;
        }
        return (tempoMaximo);
    }

    public void setTempoMaximo(Long tempoMaximo) {
        this.tempoMaximo = tempoMaximo;
    }

    public Long getTempoMinimo() {
        if (tempoMinimo == null) {
            tempoMinimo = 0L;
        }
        return (tempoMinimo);
    }

    public void setTempoMinimo(Long tempoMinimo) {
        this.tempoMinimo = tempoMinimo;
    }

    public String getCor() {
        if (cor == null) {
            cor = "#ffffff";
        }
        return (cor);
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCorFonte() {
        if (corFonte == null) {
            corFonte = "#000000";
        }
        return (corFonte);
    }

    public void setCorFonte(String corFonte) {
        this.corFonte = corFonte;
    }

    public String getScript() {
        if (script == null) {
            return "";
        }
        return (script);
    }

    public String getScript_Apresentar() {
        String script_Apresentar = "";
        if (getScript().contains("<body>")) {
            script_Apresentar = script.substring(script.indexOf("<body>"), script.indexOf("</body>"));
            script_Apresentar = script_Apresentar.replace("<body>", "");
            script_Apresentar = script_Apresentar.replace("</body>", "");
            if (script_Apresentar.contains("<p>")) {
                script_Apresentar = script_Apresentar.replace("<p>", "");
                script_Apresentar = script_Apresentar.replace("</p>", "");
            }
        } else {
            return getScript();
        }
        return (script_Apresentar);
    }

    public void setScript(String script) {
        this.script = script;
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

    public WorkflowVO getWorkflow() {
        if (workflow == null) {
            workflow = new WorkflowVO();
        }
        return (workflow);
    }

    public void setWorkflow(WorkflowVO workflow) {
        this.workflow = workflow;
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

    public List<ArquivoEtapaWorkflowVO> getArquivoEtapaWorkflowVOs() {
        if (arquivoEtapaWorkflowVOs == null) {
            arquivoEtapaWorkflowVOs = new ArrayList(0);
        }
        return arquivoEtapaWorkflowVOs;
    }

    public void setArquivoEtapaWorkflowVOs(List<ArquivoEtapaWorkflowVO> arquivoEtapaWorkflowVOs) {
        this.arquivoEtapaWorkflowVOs = arquivoEtapaWorkflowVOs;
    }

    public List<CursoEtapaWorkflowVO> getCursoEtapaWorkflowVOs() {
        if (cursoEtapaWorkflowVOs == null) {
            cursoEtapaWorkflowVOs = new ArrayList(0);
        }
        return cursoEtapaWorkflowVOs;
    }

    public void setCursoEtapaWorkflowVOs(List<CursoEtapaWorkflowVO> cursoEtapaWorkflowVOs) {
        this.cursoEtapaWorkflowVOs = cursoEtapaWorkflowVOs;
    }

    public SituacaoProspectWorkflowVO getSituacaoDefinirProspectFinal() {
        if (situacaoDefinirProspectFinal == null) {
            situacaoDefinirProspectFinal = new SituacaoProspectWorkflowVO();
        }
        return situacaoDefinirProspectFinal;
    }

    public void setSituacaoDefinirProspectFinal(SituacaoProspectWorkflowVO situacaoDefinirProspectFinal) {
        this.situacaoDefinirProspectFinal = situacaoDefinirProspectFinal;
    }

    public List<EtapaWorkflowAntecedenteVO> getEtapaWorkflowAntecedenteVOs() {
        if (etapaWorkflowAntecedenteVOs == null) {
            etapaWorkflowAntecedenteVOs = new ArrayList<EtapaWorkflowAntecedenteVO>();
        }
        return etapaWorkflowAntecedenteVOs;
    }

    public void setEtapaWorkflowAntecedenteVOs(List<EtapaWorkflowAntecedenteVO> etapaWorkflowAntecedenteVOs) {
        this.etapaWorkflowAntecedenteVOs = etapaWorkflowAntecedenteVOs;
    }

    public String getStringConcatenadaNomeNivelApresentacao() {
        if (StringConcatenadaNomeNivelApresentacao == null) {
            StringConcatenadaNomeNivelApresentacao = "";
        }
        return StringConcatenadaNomeNivelApresentacao;
    }

    public void setStringConcatenadaNomeNivelApresentacao(String StringConcatenadaNomeNivelApresentacao) {
        this.StringConcatenadaNomeNivelApresentacao = StringConcatenadaNomeNivelApresentacao;
    }

    
    public String getMotivo() {
        if(motivo ==  null){
            motivo = "";
        }
        return motivo;
    }

    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void verificarEtapaExisteEAdicionarAntecedenteVOs(EtapaWorkflowAntecedenteVO obj) {
        for (EtapaWorkflowAntecedenteVO etapasAnteriories : this.getEtapaWorkflowAntecedenteVOs()) {
            if (etapasAnteriories.getEtapaAntecedente().getCodigo().equals(obj.getEtapaAntecedente().getCodigo())) {
                return;
            }
        }
        this.getEtapaWorkflowAntecedenteVOs().add(obj);
    }
    
    
}
