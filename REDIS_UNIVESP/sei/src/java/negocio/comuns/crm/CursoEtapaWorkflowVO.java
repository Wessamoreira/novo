package negocio.comuns.crm;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.crm.Workflow;

/**
 * Reponsável por manter os dados da entidade CursoWorkflow. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see Workflow
 */
public class CursoEtapaWorkflowVO extends SuperVO {

    protected Integer codigo;
    protected EtapaWorkflowVO etapaWorkflow;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;
    private String script;

    /**
     * Construtor padrão da classe <code>CursoWorkflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public CursoEtapaWorkflowVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>CursoWorkflow</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>CursoWorkflow</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    public EtapaWorkflowVO getEtapaWorkflow() {
        if (etapaWorkflow == null) {
            etapaWorkflow = new EtapaWorkflowVO();
        }
        return (etapaWorkflow);
    }

    public void setEtapaWorkflow(EtapaWorkflowVO workflow) {
        this.etapaWorkflow = workflow;
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

    public String getScript() {
        if (script == null) {
            script =  "";
        }
        return script;
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
}
