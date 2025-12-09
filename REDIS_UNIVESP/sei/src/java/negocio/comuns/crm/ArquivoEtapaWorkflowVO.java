package negocio.comuns.crm;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.crm.Workflow;

/**
 * Reponsável por manter os dados da entidade ArquivoWorkflow. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see Workflow
 */
public class ArquivoEtapaWorkflowVO extends SuperVO {

    protected Integer codigo;
    protected EtapaWorkflowVO etapaWorkflow;
    protected ArquivoVO arquivo;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;

    /**
     * Construtor padrão da classe <code>ArquivoWorkflow</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ArquivoEtapaWorkflowVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>ArquivoWorkflow</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>ArquivoWorkflow</code>).
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

    public ArquivoVO getArquivo() {
        if (arquivo == null) {
            arquivo = new ArquivoVO();
        }
        return arquivo;
    }

    public void setArquivo(ArquivoVO arquivo) {
        this.arquivo = arquivo;
    }
}