package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;

public interface LogEstornoMovimentacaoFinanceiraItemInterfaceFacade {

	public void preencherLogEstornoMovimentacaoFinanceira(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItem, UsuarioVO usuario) throws Exception;
}
