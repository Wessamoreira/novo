package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DocumentacaoCursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface DocumentacaoCursoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>DocumentacaoCursoVO</code>.
	 */
	public DocumentacaoCursoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>DocumentacaoCursoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>DocumentacaoCursoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>DocumentacaoCursoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>DocumentacaoCursoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>DocumentacaoCursoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>DocumentacaoCursoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DocumentacaoCurso</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Curso</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>DocumentacaoCursoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DocumentacaoCurso</code> através do valor do atributo 
	 * <code>String tipoDocumento</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>DocumentacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */

	public List consultarPorTipoDeDocumento(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>DocumentacaoCurso</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>DocumentacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>DocumentacaoCursoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>DocumentacaoCurso</code>.
	 * @param <code>curso</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirDocumentacaoCursos(Integer curso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>DocumentacaoCursoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirDocumentacaoCursos</code> e <code>incluirDocumentacaoCursos</code> disponíveis na classe <code>DocumentacaoCurso</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarDocumentacaoCursos(Integer curso, List objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>DocumentacaoCursoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>academico.Curso</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirDocumentacaoCursos(Integer cursoPrm, List objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>DocumentacaoCursoVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public DocumentacaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

	/** 
	 * @author Wellington - 19 de fev de 2016 
	 * @param curso
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<DocumentacaoCursoVO> consultarPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	List<DocumentacaoCursoVO> consultarDocumentacaoCursos(MatriculaVO matricula, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception;

}