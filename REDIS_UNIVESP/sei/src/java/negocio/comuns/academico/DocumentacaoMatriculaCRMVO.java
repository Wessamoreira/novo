package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade DocumetacaoMatricula. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
public class DocumentacaoMatriculaCRMVO extends SuperVO {

    private Integer codigo;
    private Boolean entregue;
    private Date dataEntrega;
    private UsuarioVO usuario;
    private TipoDocumentoVO tipoDeDocumentoVO;
    private Integer matriculaCRM;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DocumetacaoMatricula</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DocumentacaoMatriculaCRMVO() {
        super();
    }

    public Boolean getEntregue() {
        if (entregue == null) {
            entregue = Boolean.FALSE;
        }
        return (entregue);
    }

    public Boolean isEntregue() {
        return (entregue);
    }

    public void setEntregue(Boolean entregue) {
        this.entregue = entregue;
    }

    public Integer getMatriculaCRM() {
        if (matriculaCRM == null) {
            matriculaCRM = 0;
        }
        return (matriculaCRM);
    }

    public void setMatriculaCRM(Integer matriculaCRM) {
        this.matriculaCRM = matriculaCRM;
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

    public TipoDocumentoVO getTipoDeDocumentoVO() {
        if (tipoDeDocumentoVO == null) {
            tipoDeDocumentoVO = new TipoDocumentoVO();
        }
        return tipoDeDocumentoVO;
    }

    public void setTipoDeDocumentoVO(TipoDocumentoVO tipoDeDocumentoVO) {
        this.tipoDeDocumentoVO = tipoDeDocumentoVO;
    }

    public Date getDataEntrega() {
        if (dataEntrega == null) {
            dataEntrega = new Date();
        }
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }
}
