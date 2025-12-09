package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;

public interface UnidadeEnsinoBibliotecaInterfaceFacade {

	public UnidadeEnsinoBibliotecaVO novo() throws Exception;

	public void incluir(final UnidadeEnsinoBibliotecaVO obj) throws Exception;

	public void alterar(final UnidadeEnsinoBibliotecaVO obj) throws Exception;

	public void excluir(UnidadeEnsinoBibliotecaVO obj) throws Exception;

	public List<UnidadeEnsinoBibliotecaVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UnidadeEnsinoBibliotecaVO> consultarPorBiblioteca(Integer codigoContaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirUnidadeEnsinoBiblioteca(List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception;

	public void alterarUnidadeEnsinoBiblioteca(Integer codigoBiblioteca, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception;

	public void incluirUnidadeEnsinoBiblioteca(Integer codigoContaCorrente, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs) throws Exception;

	public abstract void setIdEntidade(String idEntidade);

}