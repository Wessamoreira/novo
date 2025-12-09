package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TransferenciaEntradaDisciplinasAproveitadasInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 */
	public TransferenciaEntradaDisciplinasAproveitadasVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(TransferenciaEntradaDisciplinasAproveitadasVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>descricao</code> da classe <code>TransferenciaEntrada</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoTransferenciaEntrada(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>Double cargaHoraria</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCargaHoraria(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>Double frequencia</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorFrequencia(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>Double nota</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNota(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>Integer disciplina</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDisciplinaETransferencia(Integer valorConsulta, Integer transferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaEntradaDisciplinasAproveitadas</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>.
	 * @param <code>transferenciaEntrada</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada) throws Exception;

	public void excluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada, String periodicidadeCurso, List objetos) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirTransferenciaEntradaDisciplinasAproveitadass</code> e <code>incluirTransferenciaEntradaDisciplinasAproveitadass</code> disponíveis na classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntrada, String periodicidadeCurso, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>academico.TransferenciaEntrada</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirTransferenciaEntradaDisciplinasAproveitadass(Integer transferenciaEntradaPrm, String periodicidadeCurso, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TransferenciaEntradaDisciplinasAproveitadasVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);
	
	public void alterarTransferenciaEntradaDisciplinasAproveitadasAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception;

}