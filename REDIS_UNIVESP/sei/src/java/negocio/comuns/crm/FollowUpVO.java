package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade FollowUp. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class FollowUpVO extends SuperVO {

    private Integer codigo;
    /** Atributo responsável por manter os objetos da classe <code>HistoricoFollowUp</code>. */
    private List<HistoricoFollowUpVO> historicoFollowUpVOs;
    private List<InteracaoWorkflowVO> interacaoWorkflowVOs;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Prospects </code>.*/
    protected ProspectsVO prospect;
    private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario;

    /**
     * Construtor padrão da classe <code>FollowUp</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public FollowUpVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Prospects</code> relacionado com (<code>FollowUp</code>).
     */
    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return (prospect);
    }

    /**
     * Define o objeto da classe <code>Prospects</code> relacionado com (<code>FollowUp</code>).
     */
    public void setProspect(ProspectsVO obj) {
        this.prospect = obj;
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>HistoricoFollowUp</code>. */
    public List<HistoricoFollowUpVO> getHistoricoFollowUpVOs() {
        if (historicoFollowUpVOs == null) {
            historicoFollowUpVOs = new ArrayList(0);
        }
        return (historicoFollowUpVOs);
    }

    /** Define Atributo responsável por manter os objetos da classe <code>HistoricoFollowUp</code>. */
    public void setHistoricoFollowUpVOs(List<HistoricoFollowUpVO> historicoFollowUpVOs) {
        this.historicoFollowUpVOs = historicoFollowUpVOs;
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

    public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorario() {
        if (compromissoAgendaPessoaHorario == null) {
            compromissoAgendaPessoaHorario = new CompromissoAgendaPessoaHorarioVO();
        }
        return compromissoAgendaPessoaHorario;
    }

    public void setCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario) {
        this.compromissoAgendaPessoaHorario = compromissoAgendaPessoaHorario;
    }

    public String getDataHora_Apresentar() {
        return getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getDia().toString()
                + "/" + getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getMes().toString() + "/"
                + getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAno().toString() + " - "
                + getCompromissoAgendaPessoaHorario().getHora();
    }

    public List<InteracaoWorkflowVO> getInteracaoWorkflowVOs() {
        if (interacaoWorkflowVOs == null) {
            interacaoWorkflowVOs = new ArrayList(0);
        }
        return interacaoWorkflowVOs;
    }

    public void setInteracaoWorkflowVOs(List<InteracaoWorkflowVO> interacaoWorkflowVOs) {
        this.interacaoWorkflowVOs = interacaoWorkflowVOs;
    }
}
