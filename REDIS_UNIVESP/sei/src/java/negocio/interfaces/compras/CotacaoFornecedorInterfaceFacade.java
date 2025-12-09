package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoVO;

public interface CotacaoFornecedorInterfaceFacade {

	public void incluir(CotacaoFornecedorVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(CotacaoFornecedorVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarCotacaoFornecedors(CotacaoVO cotacao, List<CotacaoFornecedorVO> objetos, UsuarioVO usuario) throws Exception;

	public void incluirCotacaoFornecedors(CotacaoVO cotacao, List<CotacaoFornecedorVO> objetos, UsuarioVO usuario) throws Exception;

	public CotacaoFornecedorVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	List<CotacaoFornecedorVO> consultarCotacaoFornecedors(Integer cotacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}