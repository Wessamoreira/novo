package negocio.comuns.financeiro;

import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

public class RegistroNegativacaoCobrancaContaReceberRelVO extends SuperParametroRelVO {

	private static final long serialVersionUID = 1L;
	private RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceber;
	
	public RegistroNegativacaoCobrancaContaReceberVO getRegistroNegativacaoCobrancaContaReceber() {
		return registroNegativacaoCobrancaContaReceber;
	}
	
	public void setRegistroNegativacaoCobrancaContaReceber(RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceber) {
		this.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber;
	}
	
}