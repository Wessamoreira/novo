package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;

public interface ContaPagarAdiantamentoInterfaceFacade {

	

	public void incluir(ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(ContaPagarAdiantamentoVO obj, UsuarioVO usuario) throws Exception;

	public List<ContaPagarAdiantamentoVO> consultarContaPagarUtilizadaPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirContaPagarAdiantamentos(Integer contaPagar, UsuarioVO usuario) throws Exception;

	public void alterarContaPagarAdiantamentos(ContaPagarVO contaPagar, List<ContaPagarAdiantamentoVO> objetos, UsuarioVO usuario) throws Exception;

	public void incluirContaPagarAdiantamentos(ContaPagarVO contaPagarPrm, List<ContaPagarAdiantamentoVO> objetos, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	List<ContaPagarAdiantamentoVO> consultarContaPagarPorCodigoContaPagarUtilizada(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}