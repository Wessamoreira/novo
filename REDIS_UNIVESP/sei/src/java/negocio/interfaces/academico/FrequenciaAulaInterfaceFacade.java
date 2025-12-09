package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

public interface FrequenciaAulaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FrequenciaAulaVO</code>.
	 */
	public FrequenciaAulaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FrequenciaAulaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FrequenciaAulaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FrequenciaAulaVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FrequenciaAulaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FrequenciaAulaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FrequenciaAulaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(FrequenciaAulaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FrequenciaAula</code>
	 * através do valor do atributo <code>conteudo</code> da classe
	 * <code>RegistroAula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>FrequenciaAulaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FrequenciaAulaVO> consultarPorConteudoRegistroAula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FrequenciaAula</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>FrequenciaAulaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FrequenciaAulaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>FrequenciaAulaVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>FrequenciaAula</code>.
	 * 
	 * @param <code>registroAula</code> campo chave para exclusão dos objetos no
	 *        BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void excluirFrequenciaAulas(Integer registroAula, UsuarioVO usuario) throws Exception;

	public void persistirFrequenciaAulaVOs(RegistroAulaVO registroAulaVO, Integer perfilPadraoProfessor, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>FrequenciaAulaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public FrequenciaAulaVO consultarPorChavePrimaria(String matriculaPrm, Integer registroAulaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarSomaFrequenciaAlunoEspecifico(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean presente, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAula(Integer turma, Integer disciplina, String semestre, String ano, Date dataAula, String nrAula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAulaPorDia(Integer turma, String semestre, String ano, Date dataAula, Integer professor, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAulaPorAnoSemestreProfessor(Integer turma, String semestre, String ano, Integer professor, UsuarioVO usuarioLogado) throws Exception;

	public Integer consultarSomaFrequenciaAlunoEspecificoDisciplinaComposta(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean presente, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarAbonoFalta(final DisciplinaAbonoVO disciplinaAbonoVO, UsuarioVO usuario) throws Exception;

	void excluirFrequenciaAulaPorMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina, String matricula, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 05/03/2015
	 * @param matricula
	 * @param matriculaPeriodo
	 * @param turma
	 * @param disciplina
	 * @param usuarioVO
	 * @throws Exception
	 */
	void excluirFrequenciaAulaPorMatriculaMatriculaPeriodoTurmaDisciplina(String matricula, Integer matriculaPeriodo, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 19 de out de 2015 
	 * @param matricula
	 * @param semestre
	 * @param ano
	 * @param turma
	 * @param disciplina
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	Integer consultarSomaFrequenciaAlunoEspecificoConsiderantoTurmaTeoricaETurmaPratica(String matricula, String semestre, String ano, Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 21/09/2016
	 * @param registroAula
	 * @param usuarioVO
	 * @return
	 */
	List<FrequenciaAulaVO> consultarMatriculaFrequenciaAulaPorRegistroAula(Integer registroAula, UsuarioVO usuarioVO);
	
	public void incluirFrequenciaFaltaAulasRealizadasAposDataMatricula(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Date dataMatricula, UsuarioVO usuario) throws Exception;
}

