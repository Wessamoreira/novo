package negocio.comuns.contabil;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author PedroOtimize
 *
 */
public class IntegracaoContabilLoteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4971152446652401995L;
	private Integer codigo;
	private IntegracaoContabilVO integracaoContabilVO;
	private LancamentoContabilVO lancamentoContabilVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public IntegracaoContabilVO getIntegracaoContabilVO() {
		if (integracaoContabilVO == null) {
			integracaoContabilVO = new IntegracaoContabilVO();
		}
		return integracaoContabilVO;
	}

	public void setIntegracaoContabilVO(IntegracaoContabilVO integracaoContabilVO) {
		this.integracaoContabilVO = integracaoContabilVO;
	}

	public LancamentoContabilVO getLancamentoContabilVO() {
		if (lancamentoContabilVO == null) {
			lancamentoContabilVO = new LancamentoContabilVO();
		}
		return lancamentoContabilVO;
	}

	public void setLancamentoContabilVO(LancamentoContabilVO lancamentoContabilVO) {
		this.lancamentoContabilVO = lancamentoContabilVO;
	}

}
