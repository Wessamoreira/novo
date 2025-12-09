package negocio.comuns.financeiro;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class AgenteNegativacaoCobrancaUnidadeEnsinoVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5934866409072730618L;
	private Integer codigo;
    private AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO;
    private UnidadeEnsinoVO unidadeEnsino;
	
    public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
    	return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
    
    
    
	
}
