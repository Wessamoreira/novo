package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;

public interface ContaPagarNegociacaoPagamentoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>.
	 */
	public ContaPagarNegociacaoPagamentoVO novo() throws Exception;

	
	public void incluir(ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

	
	public void alterar(ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

	
	public void excluir(ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

	
	public List consultarPorCodigoNegociacaoPagamento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;


	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	public void excluirContaPagarNegociacaoPagamentos(NegociacaoPagamentoVO negociacaoContaPagar, UsuarioVO usuario) throws Exception;

	
	public void alterarContaPagarNegociacaoPagamentos(Integer negociacaoContaPagar, List objetos, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	
	public void incluirContaPagarNegociacaoPagamentos(Integer negociacaoContaPagarPrm, String tipoSacado, List<ContaPagarNegociacaoPagamentoVO> contaPagarNegociacaoPagamentoVOs, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	
	public ContaPagarNegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	public void setIdEntidade(String idEntidade);

}