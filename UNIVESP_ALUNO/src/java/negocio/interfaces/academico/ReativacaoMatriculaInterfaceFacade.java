package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ReativacaoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ReativacaoMatriculaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ReativacaoMatriculaVO</code>.
	 */
	public ReativacaoMatriculaVO novo() throws Exception;

	public void alterarSituacaoAcademicaMatriculaPeriodo(ReativacaoMatriculaVO reativacaoMatriculaVO,  UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ReativacaoMatriculaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ReativacaoMatriculaVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(ReativacaoMatriculaVO obj, UsuarioVO usuarioVO, Boolean permiteReativacaoMatriculaSemRequerimento) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ReativacaoMatriculaVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ReativacaoMatriculaVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(ReativacaoMatriculaVO obj, UsuarioVO usuarioVO, Boolean permiteReativacaoMatriculaSemRequerimento) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ReativacaoMatriculaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ReativacaoMatriculaVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ReativacaoMatriculaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void deferirRequerimento(ReativacaoMatriculaVO reativacaoMatriculaVO,  UsuarioVO usuario) throws Exception;

	public void indeferirRequerimento(ReativacaoMatriculaVO reativacaoMatriculaVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>Requerimento</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>String descricao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>CanceReativacaoMatriculalamento</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;

	public List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ReativacaoMatricula</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ReativacaoMatriculaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ReativacaoMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

    public List consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Integer codigoUltimaReativacaoPorMatricula (String matricula) throws Exception;

	List consultarPorRegistroAcademicoAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
	
	public List consultarReativacaoMatricula(String valorConsulta, Date prmIni, Date prmFim, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}