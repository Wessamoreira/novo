package negocio.comuns.crm;
import java.util.Date;

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.crm.RegistroEntrada;

/**
 * Reponsável por manter os dados da entidade RegistroEntradaProspects. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see RegistroEntrada
*/

public class RegistroEntradaProspectsVO extends SuperVO {
	
    protected Integer codigo;
    protected RegistroEntradaVO registroEntrada;
    protected Boolean existeProspects;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Prospects </code>.*/
    protected ProspectsVO prospects;
    private ProspectsVO indicadoPeloProspect;
    private CampanhaVO campanha;
    private UsuarioVO vendedor;
    private Date dataIndicacaoProspect;
	
    /**
     * Construtor padrão da classe <code>RegistroEntradaProspects</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public RegistroEntradaProspectsVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>Prospects</code> relacionado com (<code>RegistroEntradaProspects</code>).
    */
    public ProspectsVO getProspects() {
        if (prospects == null) {
            prospects = new ProspectsVO();
        }
        return (prospects);
    }
     
    /**
     * Define o objeto da classe <code>Prospects</code> relacionado com (<code>RegistroEntradaProspects</code>).
    */
    public void setProspects( ProspectsVO obj) {
        this.prospects = obj;
    }

    public Boolean getExisteProspects() {
        if (existeProspects == null) {
            existeProspects = Boolean.FALSE;
        }
        return (existeProspects);
    }

    public String getExisteProspects_Apresentar(){
        if(existeProspects == null || existeProspects.equals(false)){
            return "Não registrado";
        }
        else
            return"Já registrado";
    }
    
    public Boolean isExisteProspects() {
        if (existeProspects == null) {
            existeProspects = Boolean.FALSE;
        }
        return (existeProspects);
    }
     
    public void setExisteProspects( Boolean existeProspects ) {
        this.existeProspects = existeProspects;
    }

    public RegistroEntradaVO getRegistroEntrada() {
        if (registroEntrada == null) {
            registroEntrada = new RegistroEntradaVO();
        }
        return (registroEntrada);
    }
     
    public void setRegistroEntrada( RegistroEntradaVO registroEntrada ) {
        this.registroEntrada = registroEntrada;
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

    public ProspectsVO getIndicadoPeloProspect() {
        if (indicadoPeloProspect == null) {
            indicadoPeloProspect = new ProspectsVO();
        }
        return indicadoPeloProspect;
    }

    public void setIndicadoPeloProspect(ProspectsVO indicadoPeloProspect) {
        this.indicadoPeloProspect = indicadoPeloProspect;
    }

    public CampanhaVO getCampanha() {
        if (campanha == null) {
            campanha = new CampanhaVO();
        }
        return campanha;
    }

    public void setCampanha(CampanhaVO campanha) {
        this.campanha = campanha;
    }

    public UsuarioVO getVendedor() {
        if (vendedor == null) {
            vendedor = new UsuarioVO();
        }
        return vendedor;
    }

    public void setVendedor(UsuarioVO vendedor) {
        this.vendedor = vendedor;
    }

    public Date getDataIndicacaoProspect() {
        if (dataIndicacaoProspect == null) {
            dataIndicacaoProspect = new Date();
        }
        return dataIndicacaoProspect;
    }

    public void setDataIndicacaoProspect(Date dataIndicacaoProspect) {
        this.dataIndicacaoProspect = dataIndicacaoProspect;
    }
    
	public boolean equalsNomeEmail(RegistroEntradaProspectsVO obj){
		if (getProspects().getNome().equals(obj.getProspects().getNome())) {
			if (getProspects().getEmailPrincipal().equals(obj.getProspects().getEmailPrincipal())) {
				return true;
			}
		}
		return false;
	}
}