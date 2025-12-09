package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;

public interface HorarioTurmaDiaInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>HorarioTurmaDiaVO</code>.
     */
    public HorarioTurmaDiaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>HorarioTurmaDiaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>HorarioTurmaDiaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(HorarioTurmaDiaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>HorarioTurmaDiaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>HorarioTurmaDiaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(HorarioTurmaDiaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>HorarioTurmaDiaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>HorarioTurmaDiaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(HorarioTurmaDiaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>HorarioTurmaDia</code> através do valor do atributo
     * <code>codigo</code> da classe <code>HorarioTuma</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoHorarioTuma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    

    /**
     * Responsável por realizar uma consulta de <code>HorarioTurmaDia</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>HorarioTurmaDiaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>HorarioTurmaDia</code>.
     * @param <code>horarioTurma</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirHorarioTurmaDias(Integer horarioTurma, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>HorarioTurmaDiaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirHorarioTurmaDias</code> e <code>incluirHorarioTurmaDias</code> disponíveis na classe <code>HorarioTurmaDia</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarHorarioTurmaDias(Integer horarioTurma, TurnoVO turno, List<HorarioTurmaDiaVO> objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>HorarioTurmaDiaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.HorarioTuma</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirHorarioTurmaDias(Integer horarioTurmaPrm, TurnoVO turno, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>HorarioTurmaDiaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public HorarioTurmaDiaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public List consultarHorarioTurmaDias(Integer horarioTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

    public List<HorarioTurmaDiaVO> consultarPorHorarioTurmaDiaContendoDisciplina(Integer codigoDisciplina) throws Exception;

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurmaVO);

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurmaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs);

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO obj, HorarioTurmaDiaVO horarioTurmaDiaVO);

    public HorarioTurmaDiaVO consultarHorarioTurmaPorDiaPorDia(HorarioTurmaVO horarioTurmaVO, Date date);

    public Boolean executarVerificarDisponibilidadeProfessorEDisciplinaAula(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer nrAula, String horario, DisciplinaVO disciplina, PessoaVO novoProfessor, boolean retornarExcecao) throws Exception;

    public List consultarHorarioTurmaDias(HorarioTurmaVO horarioTurma, String mes, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public boolean executarVerificarSeHaAulaNaTurmaParaDeterminadoProfessor(Date data, Integer numeroAula, Integer codigoDisciplina, Integer codigoProfessor, int codigoTurma) throws Exception;

    public List<HorarioTurmaDiaVO> consultarPorTurmaDisciplinaPeriodoProfessor(Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, boolean limitarAteDataAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Date consultarUltimaDataAulaPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre, Integer professor) throws Exception;

    public Date consultarUltimaDataAulaPorTurmaAnteriorDataAtual(Integer turma) throws Exception;

    public Date consultarUltimaDataAulaPorTurma(Integer turma, String ano, String semestre) throws Exception;

    public Date consultarPrimeiraDataAulaPorTurma(Integer turma, String ano, String semestre) throws Exception;

    public Boolean consultarExistenciaProgramacaoAulaPorCursoTurmaProfessorData(Integer unidadeEnsino, Integer curso, Integer turma, Integer professor, Boolean graduacao, Boolean posGraduacao, String ano, String semestre, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Date consultarPrimeiraDataAulaPorTurmaAgrupada(Integer turma, String ano, String semestre) throws Exception;

    public Date consultarUltimaDataAulaPorTurmaAgrupada(Integer turma, String ano, String semestre) throws Exception;

    public Date consultarUltimaDataAulaPorMatricula(String matricula) throws Exception;

    public Date consultarPrimeiraDataAulaPorMatricula(String matricula) throws Exception;

    public Date consultarPrimeiraDataAulaPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre) throws Exception;

    public Date consultarUltimaDataAulaProgramadaMenorDataAtual(Integer turma) throws Exception;

    public List<DisciplinaVO> consultarDisciplinaUltimaDataAulaProgramadaMenorDataAtual(Integer turma, Integer qtd ) throws Exception;

    public Integer consultarQtdAulaRealizadaAteDataAtual(Integer turma) throws Exception;

    public Boolean consultarExistenciaProgramacaoAulaPorTurmaDisciplinaData(Integer turma, Integer disciplina, Date dataVerificacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(String matricula) throws Exception;

    public Integer consultarNrModuloDisciplina(Integer turma, Integer disciplina) throws Exception;

    public String consultarDataAulaTurmaDisciplina(Integer turma, Integer disciplina) throws Exception;

    public List<HorarioTurmaDiaVO> consultarPorTurmaPeriodoUnidadeAnoSemestre(String identificadorTurma, Date dataInicio, Date dataFim, String ano, String semestre,Integer disciplina,  Integer unidadeEnsino, boolean controlarAcesso,  int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<HorarioTurmaDiaVO> consultarPorTurmaDisciplinaPeriodoProfessorNaoConstaRegistroAula(Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, boolean limitarAteDataAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    HorarioTurmaDiaVO montarDadosConsultaCompletaHorarioTurmaDia(SqlRowSet rs, UsuarioVO usuario) throws Exception;

	HorarioTurmaDiaItemVO montarDadosConsultaCompletaHorarioTurmaDiaItem(SqlRowSet rs, UsuarioVO usuario) throws Exception;

	List<HorarioTurmaDiaVO> consultarHorarioTurmaDias(Integer horarioTurma, Integer turma, String identificadorTurma, String ano, String semestre, Integer professor, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/09/2015
	 * @param obj
	 * @param usuario
	 * @throws Exception
	 */
	void alterarOcultarAula(HorarioTurmaDiaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 10/09/2015
	 * @param horarioTurmaDiaVOs
	 * @param usuario
	 * @throws Exception
	 */
	void realizarCarregamentoDados(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, UsuarioVO usuario) throws Exception;

	void excluirAulaProgramadaComBaseFeriado(FeriadoVO feriado, UsuarioVO usuarioVO) throws Exception;

	Date consultarUltimaAulaAlunoPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception;

	Date consultarPrimeiraDataAulaPorMatriculaPorDisciplina(String matricula,
			Integer disciplina) throws Exception;

	Date consultarUltimaDataAulaPorMatriculaPorDisciplina(String matricula,
			Integer disciplina) throws Exception;
	
	public HorarioTurmaDiaItemVO montarDadosConsultaCompletaHorarioTurmaDiaItem(SqlRowSet rs, UsuarioVO usuario, FuncionarioCargoVO funcionarioCargoVO) throws Exception;
	
}
