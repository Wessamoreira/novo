package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MapaPendenciaMovimentacaoFinanceiraInterfaceFacade {

    public void recusarMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;

    public void finalizarMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 15 de abr de 2016 
	 * @param movimentacaoFinanceiraVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	boolean executarVerificacaoExisteDevolucaoChequeComContaReceberNegociadaOuRecebida(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 15 de abr de 2016 
	 * @param movimentacaoFinanceiraVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void excluirMovimentacaoFinanceiraItemDevolucaoChequeComContaReceberNegociadaOuRecebida(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuarioVO) throws Exception;

	public void autorizarMovimentacaoFinanceiraContaCaixaFechado(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;
}
