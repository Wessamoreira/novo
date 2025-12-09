package negocio.comuns.crm;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.TipoMetaEnum;

/**
 * Reponsável por manter os dados da entidade Meta. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
*/

public class MetaVO extends SuperVO {
	
    protected Integer codigo;
    protected String descricao;
    protected TipoMetaEnum tipoMeta;    
    protected Boolean considerarSabado;
    protected Boolean metaParaCampanhaCaptacao;
    private List<MetaItemVO> listaMetaItem;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Cargo </code>.*/
    protected CargoVO cargo;
    

    
	
    /**
     * Construtor padrão da classe <code>Meta</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
    */
    public MetaVO() {
        super();
    }
     
	
	

    /**
     * Retorna o objeto da classe <code>Cargo</code> relacionado com (<code>Meta</code>).
    */
    public CargoVO getCargo() {
        if (cargo == null) {
            cargo = new CargoVO();
        }
        return (cargo);
    }
     
    /**
     * Define o objeto da classe <code>Cargo</code> relacionado com (<code>Meta</code>).
    */
    public void setCargo( CargoVO obj) {
        this.cargo = obj;
    }

    

    public Boolean getMetaParaCampanhaCaptacao() {
        if (metaParaCampanhaCaptacao == null) {
            metaParaCampanhaCaptacao = Boolean.FALSE;
        }
        return (metaParaCampanhaCaptacao);
    }
    
    public Boolean isMetaParaCampanhaCaptacao() {
        if (metaParaCampanhaCaptacao == null) {
            metaParaCampanhaCaptacao = Boolean.FALSE;
        }
        return (metaParaCampanhaCaptacao);
    }
     
    public void setMetaParaCampanhaCaptacao( Boolean metaParaCampanhaCaptacao ) {
        this.metaParaCampanhaCaptacao = metaParaCampanhaCaptacao;
    }


    public Boolean getConsiderarSabado() {
        if (considerarSabado == null) {
            considerarSabado = Boolean.FALSE;
        }
        return (considerarSabado);
    }
    
    public Boolean isConsiderarSabado() {
        if (considerarSabado == null) {
            considerarSabado = Boolean.FALSE;
        }
        return (considerarSabado);
    }
     
    public void setConsiderarSabado( Boolean considerarSabado ) {
        this.considerarSabado = considerarSabado;
    }

    public TipoMetaEnum getTipoMeta() {
        return (tipoMeta);
    }
     
    public void setTipoMeta( TipoMetaEnum tipoMeta ) {
        this.tipoMeta = tipoMeta;
    }

    public String getDescricao() {
        if (descricao == null) {
            return "";
        }
        return (descricao);
    }
     
    public void setDescricao( String descricao ) {
        this.descricao = descricao;
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

    public List<MetaItemVO> getListaMetaItem() {
        if (listaMetaItem == null) {
            listaMetaItem = new ArrayList(0);
        }
        return listaMetaItem;
    }

    public void setListaMetaItem(List<MetaItemVO> listaMetaItem) {
        this.listaMetaItem = listaMetaItem;
    }
}