package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;

/**
 * @author Victor Hugo de Paula Costa - 20 de abr de 2016
 *
 */
public interface FormaPagamentoNegociacaoRecebimentoDCCInterfaceFacade {

	/** 
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016 
	 * @param obj
	 * @param verificarAcesso
	 * @param usuario
	 * @throws Exception 
	 */
	void incluir(FormaPagamentoNegociacaoRecebimentoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	/**
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016 
	 */

	/** 
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016 
	 * @param negociacaoRecebimento
	 * @param controleAcesso
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, boolean controleAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param codigoCondicaoPagamentoNegocicaoRecebimento
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<FormaPagamentoNegociacaoRecebimentoVO> consultarPorCodigoCondicaoPagamentoNegociacaoRecebimento(Integer codigoCondicaoPagamentoNegocicaoRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de jun de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirMotivoCancelamento(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de jun de 2016 
	 * @param codigoPrm
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	FormaPagamentoNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 23 de jun de 2016 
	 * @param codigoContaReceber
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoVOSDCCPorCodigoContaReceber(Integer codigoContaReceber, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 29 de jun de 2016 
	 * @param valorConsulta
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<FormaPagamentoNegociacaoRecebimentoVO> consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
