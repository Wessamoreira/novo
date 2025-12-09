package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;

/**
 * @author Victor Hugo de Paula Costa - 27 de abr de 2016
 *
 */
public interface ContaReceberNegociacaoRecebimentoDCCInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param negociacaoRecebimentoPrm
	 * @param objetos
	 * @param configuracaoFinanceiro
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirContaReceberNegociacaoRecebimentos(Integer negociacaoRecebimentoPrm, List<ContaReceberNegociacaoRecebimentoVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param obj
	 * @param configuracaoFinanceiro
	 * @param usuario
	 * @throws Exception 
	 */
	void incluir(ContaReceberNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param codigoNegociacaoRecebimento
	 * @param nilveMontarDados
	 * @param configuracaoFinanceiro
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<ContaReceberNegociacaoRecebimentoVO> consultarPorNegociacaoRecebimentoDCC(Integer codigoNegociacaoRecebimento, int nilveMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de mai de 2016 
	 * @param codigoContaReceber
	 * @param nilveMontarDados
	 * @param configuracaoFinanceiro
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<ContaReceberNegociacaoRecebimentoVO> consultarPorCodigoContaReceber(Integer codigoContaReceber, int nilveMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 17 de mai de 2016 
	 * @param nilveMontarDados
	 * @return
	 * @throws Exception 
	 */
	List<ContaReceberNegociacaoRecebimentoVO> consultarContasAReceberNegociacaoRecebimentoQuePossuemContasAReceberDCC(int nilveMontarDados) throws Exception;
}
