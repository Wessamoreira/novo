package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorTurnoVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.GraduacaoPosGraduacaoEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import webservice.servicos.objetos.DataEventosRSVO;

public interface HorarioProfessorDiaInterfaceFacade {

    public void executarValidacaoProfessorHorarioDisponivelDia(HorarioProfessorDiaVO horarioProfessorDiaVO, Integer nrAula, DisciplinaVO disciplina, TurmaVO novaTurma) throws ConsistirException;

    public void montarDadosHorarioProfessorDiaItemVOs(HorarioProfessorDiaVO horarioProfessorDiaVO, HorarioProfessorVO horarioProfessorVO, TurnoVO obj, Date dataInicio, Date dataFim,  UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por retornar um novo objeto da classe <code>HorarioProfessorDiaVO</code>.
     */
    public HorarioProfessorDiaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>HorarioProfessorDiaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>HorarioProfessorDiaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(HorarioProfessorDiaVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>HorarioProfessorDiaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>HorarioProfessorDiaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(HorarioProfessorDiaVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>HorarioProfessorDiaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>HorarioProfessorDiaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(HorarioProfessorDiaVO obj, UsuarioVO usuarioVO) throws Exception;
    
    public HorarioProfessorDiaVO consultarPorDiaProfessorTurno(Date valorConsulta, Integer professor, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>HorarioProfessorDiaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>HorarioProfessorDia</code>.
     * @param <code>horarioProfessor</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirHorarioProfessorDias(Integer horarioProfessor, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>HorarioProfessorDiaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirHorarioProfessorDias</code> e <code>incluirHorarioProfessorDias</code> disponíveis na classe <code>HorarioProfessorDia</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarHorarioProfessorDias(Integer horarioProfessor, TurnoVO turnoVO, List<HorarioProfessorDiaVO> objetos, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>HorarioProfessorDiaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.HorarioTuma</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirHorarioProfessorDias(Integer horarioProfessorPrm, TurnoVO turnoVO, List objetos, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>HorarioProfessorDiaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public HorarioProfessorDiaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List<HorarioProfessorDiaVO> consultarPorHorarioProfessorDiaContendoDisciplina(Integer codigoDisciplina) throws Exception;

    public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String nivelEducacional, UsuarioVO usuario, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception;

    public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, Boolean visaoCoordenador, String nivelEducacional, UsuarioVO usuario, String ordenacao) throws Exception;

    public void excluirPorDataProfessor(Date dataAula, Integer codigoProfessor, UsuarioVO usuario) throws Exception;

    public List<HorarioProfessorDiaVO> consultarProfessorPorPeriodoTurmaAgrupada(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String ordenacao) throws Exception;

    public void montarDadosHorarioProfessorDiaItemVOsRegistroAula(HorarioProfessorVO horarioProfessorVO, HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO) throws Exception;

	List<HorarioProfessorDiaVO> consultarHorarioProfessorDia(Integer horarioProfessor, Integer turno, Integer professor, Integer disciplina, Integer turma, Date dataInicio, Date dataTermino, DiaSemana[] diaSemanas, List<Date> datas, Integer unidadeEnsino, String ano, String semestre) throws Exception;

	void realizarCriacaoHorarioProfessorDiaItemVOs(HorarioProfessorDiaVO horarioProfessorDiaVO, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuarioVO);

	/**
	 * @author Rodrigo Wind - 15/09/2015
	 * @param datas
	 * @param professor
	 * @return
	 */
	Map<String, Integer> consultarQuantidadeAulaProgramadaProfessorPorData(List<Date> datas, Integer professor);

	List<CronogramaDeAulasRelVO> criarObjetoRelatorioSemanal(List<HorarioProfessorTurnoVO> lista, UsuarioVO usuarioVO);

	List<HorarioProfessorTurnoVO> consultarMeusHorariosProfessor(PessoaVO professorVO, Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception;
	
	List<HorarioProfessorDiaVO> consultarProfessorPorPeriodoTurmaAgrupada(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception;

	List<DataEventosRSVO> consultarRapidaHorarioProfessorDia(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception;
	
	public List<CursoVO> montarCursoVinculadoProfessor(List<HorarioProfessorDiaItemVO> horarioProfessorDiaItemVOs);
	public List<HorarioProfessorDiaVO> consultarHorarioProfessorDiaRegistroAulaAutomaticamente(UsuarioVO usuarioVO) throws Exception;
	
	public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String nivelEducacional, UsuarioVO usuario, String ordenacao) throws Exception;
}
