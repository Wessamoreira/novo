package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;

public interface UnidadeEnsinoContaCorrenteInterfaceFacade {

	public abstract UnidadeEnsinoContaCorrenteVO novo() throws Exception;

	public abstract void incluir(final UnidadeEnsinoContaCorrenteVO obj)
			throws Exception;

	public abstract void alterar(final UnidadeEnsinoContaCorrenteVO obj)
			throws Exception;

	public abstract void excluir(UnidadeEnsinoContaCorrenteVO obj) throws Exception;

	public abstract List<UnidadeEnsinoContaCorrenteVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract List<UnidadeEnsinoContaCorrenteVO> consultarPorContaCorrente(Integer codigoContaCorrente, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public abstract void excluirUnidadeEnsinoContaCorrentes(List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs)
			throws Exception;

	public abstract void alterarUnidadeEnsinoContaCorrentes(Integer codigoContaCorrente,
			List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs)
			throws Exception;

	public abstract void incluirUnidadeEnsinoContaCorrentes(Integer codigoContaCorrente,
			List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs)
			throws Exception;

	public abstract void setIdEntidade(String idEntidade);
	
	public void adicionarTodasUnidadesEnsinoContaCorrente(ContaCorrenteVO contaCorrenteVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

}