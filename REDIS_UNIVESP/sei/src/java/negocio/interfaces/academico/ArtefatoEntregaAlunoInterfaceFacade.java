package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ArtefatoEntregaAlunoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>.
	 */
	public ArtefatoEntregaAlunoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>artefato</code> através do
	 * valor do atributo <code>nome</code> da classe
	 * <code>ArtefatoEntregaAlunoVO</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ArtefatoEntregaAlunoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ArtefatoEntregaAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>artefato</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>artefato</code> através do
	 * valor do atributo <code>unidadeEnsino</code> da classe
	 * <code>ArtefatoEntregaAlunoVO</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarArtefatosAtivosPorUnidadeEnsino(Integer codigoFuncionario, UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
	
	/**
	 * Responsável por realizar uma consulta de <code>artefato</code> através do
	 * valor do atributo <code>unidadeEnsino</code> da classe
	 * <code>ArtefatoEntregaAlunoVO</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ArtefatoEntregaAlunoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarArtefatosAtivos(Integer codigoFuncionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
	
	public void validarDados(ArtefatoEntregaAlunoVO obj) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String aIdEntidade);

}