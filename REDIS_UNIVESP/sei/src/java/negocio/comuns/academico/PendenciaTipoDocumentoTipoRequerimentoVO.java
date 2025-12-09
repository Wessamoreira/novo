package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PendenciaTipoDocumentoTipoRequerimentoVO extends SuperVO {

    private Integer codigo;
    private TipoDocumentoVO tipoDocumento;
    private TipoRequerimentoVO tipoRequerimento;
    
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public TipoDocumentoVO getTipoDocumento() {
		if(tipoDocumento == null) {
			tipoDocumento = new TipoDocumentoVO();
		}
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoRequerimentoVO getTipoRequerimento() {
		if(tipoRequerimento == null) {
			tipoRequerimento = new TipoRequerimentoVO();
		}
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimentoVO tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

}
