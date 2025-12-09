package negocio.comuns.academico;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade TipoDocumento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "tipoDocumentoEquivalenteVO")
public class TipoDocumentoEquivalenteVO extends SuperVO {

    private Integer codigo;
    private Integer tipoDocumento;
    private TipoDocumentoVO tipoDocumentoEquivalente;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoDocumento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TipoDocumentoEquivalenteVO() {
        super();
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "tipoDocumento")
    public Integer getTipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = 0;
        }
        return (tipoDocumento);
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the tipoDocumentoEquivalente
     */
    @XmlElement(name = "tipoDocumentoEquivalente")
    public TipoDocumentoVO getTipoDocumentoEquivalente() {
        if (tipoDocumentoEquivalente == null) {
            tipoDocumentoEquivalente = new TipoDocumentoVO();
        }
        return tipoDocumentoEquivalente;
    }

    /**
     * @param tipoDocumentoEquivalente the tipoDocumentoEquivalente to set
     */
    public void setTipoDocumentoEquivalente(TipoDocumentoVO tipoDocumentoEquivalente) {
        this.tipoDocumentoEquivalente = tipoDocumentoEquivalente;
    }

}
