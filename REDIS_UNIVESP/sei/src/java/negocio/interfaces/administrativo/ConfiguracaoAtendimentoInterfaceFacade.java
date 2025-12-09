package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.Usuario;

public interface ConfiguracaoAtendimentoInterfaceFacade {

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	void excluir(ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	void persistir(ConfiguracaoAtendimentoVO obj, UsuarioVO usuario) throws Exception;

	void validarDados(ConfiguracaoAtendimentoVO obj) throws Exception;

	List<ConfiguracaoAtendimentoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ConfiguracaoAtendimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ItemTitulacaoCursoVO</code>
	 * ao List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
	 * @param obj    Objeto da classe <code>ItemTitulacaoCursoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	void adicionarConfiguracaoAtendimentoFuncionarioVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoFuncionarioVO obj) throws Exception;

	/**
	 * Operação responsável por excluir um objeto da classe <code>ItemTitulacaoCursoVO</code>
	 * no List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
	 * @param titulacao  Parâmetro para localizar e remover o objeto do List.
	 */
	void excluirConfiguracaoAtendimentoFuncionarioVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoFuncionarioVO obj) throws Exception;

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ItemTitulacaoCursoVO</code>
	 * ao List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
	 * @param obj    Objeto da classe <code>ItemTitulacaoCursoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	void adicionarConfiguracaoAtendimentoUnidadeEnsinoVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoUnidadeEnsinoVO obj) throws Exception;

	/**
	 * Operação responsável por excluir um objeto da classe <code>ItemTitulacaoCursoVO</code>
	 * no List <code>itemTitulacaoCursoVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>ItemTitulacaoCurso</code> - getTitulacao() - como identificador (key) do objeto no List.
	 * @param titulacao  Parâmetro para localizar e remover o objeto do List.
	 */
	void excluirConfiguracaoAtendimentoUnidadeEnsinoVOs(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO, ConfiguracaoAtendimentoUnidadeEnsinoVO obj, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarValidacaoSeExisteConfiguracaoAtendimentoValidaPorUnidadeEnsino(Integer codigoUnidadeEnisno, UsuarioVO usuario) throws Exception;

	ConfiguracaoAtendimentoVO consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}