package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarNegociadoVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;

public interface ContaPagarNegociadoInterfaceFacade {

	public void incluir(final ContaPagarNegociadoVO obj, UsuarioVO usuario) throws Exception;		
	public List<ContaPagarNegociadoVO> consultarPorCodigoNegociacaoContaPagar(Integer valorConsulta, int nivelMontarDados,  UsuarioVO usuario) throws Exception;	
	public void excluirContaPagarNegociados(NegociacaoContaPagarVO negociacaoContaPagarVO, UsuarioVO usuario) throws Exception;	
	public void incluirContaPagarNegociados(NegociacaoContaPagarVO negociacaoContaPagarVO, UsuarioVO usuario) throws Exception;
	public ContaPagarNegociadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;


}