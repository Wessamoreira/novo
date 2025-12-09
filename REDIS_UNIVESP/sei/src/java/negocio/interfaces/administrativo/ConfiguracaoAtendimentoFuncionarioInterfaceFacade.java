package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoAtendimentoFuncionarioInterfaceFacade {

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ItemTitulacaoCursoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>ItemTitulacaoCurso</code>
	 * .
	 * 
	 * @param <code>titulacaoCurso</code> campo chave para exclusão dos objetos
	 *        no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	void excluirConfiguracaoAtendimentoFuncionarioVO(Integer configuracaoAtendimento, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ItemTitulacaoCursoVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirConfiguracaoAtendimentoFuncionarioVO</code> e
	 * <code>incluirConfiguracaoAtendimentoFuncionarioVO</code> disponíveis na
	 * classe <code>ItemTitulacaoCurso</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	void alterarConfiguracaoAtendimentoFuncionarioVO(Integer configuracaoAtendimento, List objetos, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Operação responsável por incluir objetos da
	 * <code>ItemTitulacaoCursoVO</code> no BD. Garantindo o relacionamento com
	 * a entidade principal <code>academico.TitulacaoCurso</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	void incluirConfiguracaoAtendimentoFuncionarioVO(Integer configuracaoAtendimento, List<ConfiguracaoAtendimentoFuncionarioVO> objetos, UsuarioVO usuarioLogado) throws Exception;

	void validarDados(ConfiguracaoAtendimentoFuncionarioVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ItemTitulacaoCurso</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>TitulacaoCurso</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemTitulacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List consultarPorCodigoConfiguracaoAtendimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ConfiguracaoAtendimentoFuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	

}