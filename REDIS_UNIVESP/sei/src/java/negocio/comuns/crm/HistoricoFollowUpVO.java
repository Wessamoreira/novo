package negocio.comuns.crm;
import java.util.Date;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.crm.FollowUp;

/**
 * Reponsável por manter os dados da entidade HistoricoFollowUp. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see FollowUp
*/

public class HistoricoFollowUpVO extends SuperVO {
	
    private Integer codigo;
    protected ProspectsVO prospect;
    private String observacao;
    private DepartamentoVO departamento;
    private Date dataregistro;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Funcionario </code>.*/
    protected UsuarioVO responsavel;
    private TipoContatoVO tipoContato;
	
    /**
     * Construtor padrão da classe <code>HistoricoFollowUp</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public HistoricoFollowUpVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (<code>HistoricoFollowUp</code>).
    */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }
     
    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (<code>HistoricoFollowUp</code>).
    */
    public void setResponsavel( UsuarioVO obj) {
        this.responsavel = obj;
    }

    public Date getDataregistro() {
        if (dataregistro == null) {
            dataregistro = new Date();
        }
        return (dataregistro);
    }
     
    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
    */
    public String getDataregistro_Apresentar() {
        return (Uteis.getDataComHora(getDataregistro()));
    }
     
    public void setDataregistro( Date dataregistro ) {
        this.dataregistro = dataregistro;
    }

    public String getObservacao_Apresentar() {
        if (getObservacao().length() > 20){
            return observacao.substring(0, 20);
        } else {
            return getObservacao();
        }
    }

    public String getObservacao() {
        if (observacao == null) {
            return "";
        }
        return (observacao);
    }
     
    public void setObservacao( String observacao ) {
        this.observacao = observacao;
    }

    /**
     * Retorna o objeto da classe <code>Prospects</code> relacionado com (<code>InteracaoWorkflow</code>).
    */
    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return (prospect);
    }
     
    /**
     * Define o objeto da classe <code>Prospects</code> relacionado com (<code>InteracaoWorkflow</code>).
    */
    public void setProspect( ProspectsVO obj) {
        this.prospect = obj;
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

    /**
     * @return the departamento
     */
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }




	public TipoContatoVO getTipoContato() {
		if(tipoContato == null){
			tipoContato = new TipoContatoVO();
		}
		return tipoContato;
	}




	public void setTipoContato(TipoContatoVO tipoContato) {
		this.tipoContato = tipoContato;
	}
    
    
    
}