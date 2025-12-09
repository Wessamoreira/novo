package negocio.comuns.financeiro;

import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.SuperVO;

public class TipoDocumentoPendenciaAgenteCobrancaVO extends SuperVO {

	 	private Integer codigo;
	    private TipoDocumentoVO tipoDocumento;
	    private AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO;
	    
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

		public AgenteNegativacaoCobrancaContaReceberVO getAgenteNegativacaoCobrancaContaReceberVO() {
			if (agenteNegativacaoCobrancaContaReceberVO == null) {
				agenteNegativacaoCobrancaContaReceberVO = new AgenteNegativacaoCobrancaContaReceberVO();
			}
			return agenteNegativacaoCobrancaContaReceberVO;
		}

		public void setAgenteNegativacaoCobrancaContaReceberVO(
				AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO) {
			this.agenteNegativacaoCobrancaContaReceberVO = agenteNegativacaoCobrancaContaReceberVO;
		}

		

	
	
}
