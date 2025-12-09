package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TurmaAgrupadaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TurmaAgrupadaVO</code>.
	 */
	public TurmaAgrupadaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TurmaAgrupadaVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>TurmaAgrupadaVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(TurmaAgrupadaVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TurmaAgrupadaVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>TurmaAgrupadaVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(TurmaAgrupadaVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TurmaAgrupadaVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>TurmaAgrupadaVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(TurmaAgrupadaVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TurmaAgrupada</code> através do valor do atributo
	 * <code>Integer gradeDisciplina</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TurmaAgrupadaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TurmaAgrupadaVO consultarPorCodigoTurmaCodigoTurmaOrigem(Integer turma, Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>TurmaAgrupada</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>TurmaAgrupadaVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirTurmaAgrupadas(Integer turma) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>TurmaAgrupadaVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirTurmaAgrupadas</code> e <code>incluirTurmaAgrupadas</code> disponíveis na classe <code>TurmaAgrupada</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarTurmaAgrupadas(TurmaVO turma, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>TurmaAgrupadaVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>academico.Turma</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirTurmaAgrupadas(TurmaVO turma, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>TurmaAgrupadaVO</code>
	 * através de sua chave primária.
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TurmaAgrupadaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

        public void executarBuscaPorDisciplinasComunsEntreTurmasAgrupadas(TurmaVO turmaAgrupada, UsuarioVO usuario) throws Exception;

        public void carregarDadosTurmaAgrupada(TurmaVO turmaVO, UsuarioVO usuario) throws Exception;

        public Boolean consultarPossuiTurmaAgrupada(Integer turmaOrigem, Integer turma);

}