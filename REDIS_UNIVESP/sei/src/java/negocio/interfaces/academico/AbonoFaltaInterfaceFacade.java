package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.AbonoFaltaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface AbonoFaltaInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>AbonoFaltaVO</code>.
	 */
	public AbonoFaltaVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TurmaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TurmaVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception;

	public Integer obterUnidadeEnsinoCurso(AbonoFaltaVO obj, UsuarioVO usuario);

	public String obterSemestre(AbonoFaltaVO obj);

	public String obterAno(AbonoFaltaVO obj);

	public void alterarDisciplinasAbono(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TurmaVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List consultarDisciplinaComFalta(String matricula, Date dataInicio, Date dataFim, String situacaoAula, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<AbonoFaltaVO> consultarAbonoFaltaPorPeriodoEMatricula(String matricula, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TurmaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public AbonoFaltaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public void carregarDados(AbonoFaltaVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(AbonoFaltaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<AbonoFaltaVO> consultaRapidaPorMatricula(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<AbonoFaltaVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void consultarECarregarAbonoFaltaParaFrequenciasDoRegistroAula(RegistroAulaVO registroAula) throws Exception;

	boolean verificarAbonoRegistroAulaTransferenciaTurma(Integer pessoa, String matricula, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	void executarGeracaoAbonoRegistroAulaTransferenciaTurma(MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplina, Integer qtdeRegistroAbonar, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	int verificarQtdeHorasRegistroAulaTransferenciaTurma(String matricula, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington Rodrigues - 21 de ago de 2015 
	 * @param abonoFaltaVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void persistir(AbonoFaltaVO abonoFaltaVO, UsuarioVO usuarioVO) throws Exception;

}