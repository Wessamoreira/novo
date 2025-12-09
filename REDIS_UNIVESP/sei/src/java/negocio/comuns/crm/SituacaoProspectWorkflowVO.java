package negocio.comuns.crm;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;

/**
 * Reponsável por manter os dados da entidade SituacaoProspectWorkflow. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see Workflow
*/

public class SituacaoProspectWorkflowVO extends SuperVO {
	
    protected Integer codigo;
    protected WorkflowVO workflow;
    /** Atributo responsável por manter o objeto relacionado da classe <code>SituacaoProspectPipeline </code>.*/
    protected SituacaoProspectPipelineVO situacaoProspectPipeline;
    private Double efetivacaoVendaHistorica;
	
    /**
     * Construtor padrão da classe <code>SituacaoProspectWorkflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public SituacaoProspectWorkflowVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>SituacaoProspectPipeline</code> relacionado com (<code>SituacaoProspectWorkflow</code>).
    */
    public SituacaoProspectPipelineVO getSituacaoProspectPipeline() {
        if (situacaoProspectPipeline == null) {
            situacaoProspectPipeline = new SituacaoProspectPipelineVO();
        }
        return (situacaoProspectPipeline);
    }
     
    /**
     * Define o objeto da classe <code>SituacaoProspectPipeline</code> relacionado com (<code>SituacaoProspectWorkflow</code>).
    */
    public void setSituacaoProspectPipeline( SituacaoProspectPipelineVO obj) {
        this.situacaoProspectPipeline = obj;
    }

    public WorkflowVO getWorkflow() {
        if (workflow == null) {
            workflow = new WorkflowVO();
        }
        return (workflow);
    }
     
    public void setWorkflow( WorkflowVO workflow ) {
        this.workflow = workflow;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }
     
    public void setCodigo( Integer codigo ) {
        this.codigo = codigo;
    }

    public Double getEfetivacaoVendaHistorica() {
        if (efetivacaoVendaHistorica == null) {
            efetivacaoVendaHistorica = 0.0;
        }
        return efetivacaoVendaHistorica;
    }

    public void setEfetivacaoVendaHistorica(Double efetivacaoVendaHistorica) {
        this.efetivacaoVendaHistorica = efetivacaoVendaHistorica;
    }
    
    public Integer getOrdenacao(){
    	return getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL) ? 1 :
    		getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO) ? 2 :
    			getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO) ? 3: 4;
    }
}