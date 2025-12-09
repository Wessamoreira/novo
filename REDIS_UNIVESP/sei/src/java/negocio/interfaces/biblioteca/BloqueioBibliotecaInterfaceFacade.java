package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BloqueioBibliotecaVO;

public interface BloqueioBibliotecaInterfaceFacade {

	public void inicializarDadosBloqueioBibliotecaNovo(BloqueioBibliotecaVO bloqueioBibliotecaVO, UsuarioVO usuario) throws Exception;

	public BloqueioBibliotecaVO novo() throws Exception;

	/**
	 * Verifica se a pessoa tem um bloqueio na biblioteca, o que impossibilita
	 * de realizar algumas ações.
	 * 
	 * @param codPessoa
	 * @throws Exception
	 */
	public void verificarBloqueioBiblioteca(Integer codPessoa, Integer biblioteca, String tipoPessoa, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Verifica se a pessoa já tem um bloqueio na biblioteca.
	 * 
	 * @param pessoa
	 * @throws Exception
	 */
	public Boolean verificarBloqueioExistente(Integer pessoa) throws Exception;

	public void incluir(BloqueioBibliotecaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(BloqueioBibliotecaVO obj, UsuarioVO usuarioVO) throws Exception;

	public List<BloqueioBibliotecaVO> consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<BloqueioBibliotecaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<BloqueioBibliotecaVO> consultarPorNomePessoa(String nome, Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<BloqueioBibliotecaVO> consultarPorMatricula(String matricula, Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	void persistir(BloqueioBibliotecaVO obj, UsuarioVO usuarioVO) throws Exception;

	List<BloqueioBibliotecaVO> consultarPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso,	int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<BloqueioBibliotecaVO> consultar(Integer pessoaSolicitante, Integer biblioteca, Date dataInicioDataBloqueio, Date dataFimDataBloqueio, Date dataInicioDataLimiteBloqueio, Date dataFimDataLimiteBloqueio, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) throws Exception;
	
	public Integer consultarQuantidadeRegistros(Integer pessoaSolicitante, Integer biblioteca, Date dataInicioDataBloqueio, Date dataFimDataBloqueio, Date dataInicioDataLimiteBloqueio, Date dataFimDataLimiteBloqueio, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
}