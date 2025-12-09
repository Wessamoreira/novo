package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.PortadorDiplomaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface PortadorDiplomaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>PortadorDiplomaVO</code>.
	 */
	PortadorDiplomaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>PortadorDiplomaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PortadorDiplomaVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	void incluir(PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>PortadorDiplomaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
	 * da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PortadorDiplomaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	void alterar(PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>PortadorDiplomaVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PortadorDiplomaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	void excluir(PortadorDiplomaVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>nome</code> da classe
	 * <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>Integer codigoRequerimento</code>.
	 * Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>String descricao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>PortadorDiploma</code> através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	List<PortadorDiplomaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>PortadorDiplomaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	PortadorDiplomaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	void setIdEntidade(String idEntidade);

	void alterarMatricula(Integer codigo, String matricula) throws Exception;

	/**
	 * @author Wellington - 26 de out de 2015
	 * @param obj
	 * @throws ConsistirException
	 */
	void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException;

	/**
	 * @author Wellington - 26 de out de 2015
	 * @param obj
	 * @param usuario
	 * @throws Exception
	 */
	void persistir(PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 11/11/2015
	 * @param matricula
	 * @return
	 * @throws Exception
	 */
	PortadorDiplomaVO consultarInstitiucaoEnsinoIngressoPorMatricula(String matricula, UsuarioVO usuario) throws Exception;
	
	public List<PortadorDiplomaVO> consultarPorNomeMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;		
}
