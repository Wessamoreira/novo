package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProcessoMatriculaCalendarioInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ProcessoMatriculaCalendarioVO</code>.
	 */
	public ProcessoMatriculaCalendarioVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessoMatriculaCalendarioVO</code>. Primeiramente valida os dados
	 * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessoMatriculaCalendarioVO</code>
	 *            que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO obj, UsuarioVO usuario) throws Exception;

	public Integer incluirPeriodoAtivo(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoAtivo, String nivelProcessoMatricula, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessoMatriculaCalendarioVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessoMatriculaCalendarioVO</code>
	 *            que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessoMatriculaCalendarioVO</code>. Sempre localiza o registro a
	 * ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessoMatriculaCalendarioVO</code>
	 *            que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ProcessoMatriculaCalendarioVO obj, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * Responsável por realizar uma consulta de
	 * <code>ProcessoMatriculaCalendario</code> através do valor do atributo
	 * <code>codigo</code> e do atributo <code>unidadeEnsinoCurso</code> da
	 * classe <code>MatriculaPeriodo</code> Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return ProcessoMatriculaCalendarioVO Contendo os dados da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public ProcessoMatriculaCalendarioVO consultarPorMatriculaPeriodoUnidadeEnsinoCurso(Integer codigoMatriculaPeriodo, Integer curso, Integer turno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultarProcessoMatriculaUnidadeEnsinoCursoDentroPrazo(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProcessoMatriculaUnidadeEnsinoCursoForaPrazo(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarProcessoMatriculaUnidadeEnsinoCursoPrazoInclusaoExclusaoDisciplina(Integer curso, Integer turno, Integer unidadeEnsino, Date inicio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ProcessoMatriculaCalendario</code> através do valor do atributo
	 * <code>descricao</code> da classe <code>ProcessoMatricula</code> Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ProcessoMatriculaCalendarioVO</code> resultantes da
	 *         consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoProcessoMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ProcessoMatriculaCalendarioVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe
	 * <code>ProcessoMatriculaCalendario</code>.
	 * 
	 * @param <code>processoMatricula</code> campo chave para exclusão dos
	 *        objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void excluirProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ProcessoMatriculaCalendarioVO</code> contidos em um Hashtable no
	 * BD. Faz uso da operação <code>excluirProcessoMatriculaCalendarios</code>
	 * e <code>incluirProcessoMatriculaCalendarios</code> disponíveis na classe
	 * <code>ProcessoMatriculaCalendario</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void alterarProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List<ProcessoMatriculaCalendarioVO> objetos, List<ProcessoMatriculaCalendarioVO> listaProcessoMatriculaExcluirPeriodoLetivo, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da
	 * <code>ProcessoMatriculaCalendarioVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal
	 * <code>academico.ProcessoMatricula</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List<ProcessoMatriculaCalendarioVO> objetos, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProcessoMatriculaCalendarioVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ProcessoMatriculaCalendarioVO consultarPorChavePrimaria(Integer cursoPrm, Integer turno, Integer processoMatriculaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public void inicializarDadosCalendario(ProcessoMatriculaVO processoMatricula, ProcessoMatriculaCalendarioVO obj);

	public void excluirObjProcessoMatriculaCalendarioVOs(Integer curso, Integer turno, List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs) throws Exception;

	public void validarDadosCursoExisteGradeCurricularAtiva(Integer curso, UsuarioVO usuario) throws Exception;

	public Boolean consultarSeAindaExisteProcessoMatriculaCalendarioPorPeriodoLetivoAtivoDistintosComMesmoProcessoMatricula(Integer periodoLetivoAtivoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ProcessoMatriculaCalendarioVO consultarPorPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarProcessoMatriculaCalendarios(ProcessoMatriculaVO processoMatriculaVO, List objetos, UsuarioVO usuario) throws Exception;

	boolean executarVerificarProcessoMatriculaCalendarioTransferenciaTurno(Integer cursoPrm, Integer turno, Integer processoMatriculaPrm, UsuarioVO usuario) throws Exception;
		
	public void realizarAlteracaoPeriodoCalendario(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarPeriodoProcessoMatriculaCalendarioPorProcessoMatricula(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 13 de jan de 2016 
	 * @param unidadeEnsinoCurso
	 * @param ano
	 * @param semestre
	 * @param nivelMontarDados
	 * @param controlarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	ProcessoMatriculaCalendarioVO consultarPorUnidadeEnsinoCursoAnoSemestre(Integer unidadeEnsinoCurso, String ano, String semestre, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception;
	
	public void desvincularPoliticaDivulgacaoMatriculaOnline(Integer politicaDivulgacaoMatriculaOnline, UsuarioVO usuario) throws Exception;
	
	public Boolean verificarProcessoMatriculaCalendarioExistenteUnidadeEnsinoCurso(Integer unidadeEnsinoCodigo,Integer turnoCodigo,Integer cursoCodigo) throws Exception ;
}