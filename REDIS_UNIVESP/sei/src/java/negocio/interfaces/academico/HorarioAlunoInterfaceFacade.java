/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.ChoqueHorarioAlunoDetalheVO;
import negocio.comuns.academico.ChoqueHorarioAlunoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioAlunoDiaVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.HorarioAlunoVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 *
 * @author Otimize-Not
 */
public interface HorarioAlunoInterfaceFacade {

    void adicionarDisponibilidadeHorarioAlunoVOs(HorarioAlunoTurnoVO horarioAlunoTurnoVO, Integer nrAula, String horario, DiaSemana diaSemana, DisciplinaVO disciplina, TurmaVO turma, Integer professor) throws Exception;

    void adicionarHorarioAlunoDia(HorarioAlunoTurnoVO horarioAlunoTurnoVO, List<HorarioAlunoDiaVO> horarioAlunoDiaVOs) throws Exception;

    void adicionarHorarioAlunoDia(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioAlunoDiaVO obj) throws Exception;

    void adicionarHorarioAlunoDiaItem(HorarioAlunoDiaVO obj, HorarioAlunoDiaVO objExistente) throws Exception;

    public void inicializarDadosCalendario(HorarioAlunoTurnoVO horarioAlunoTurnoVO,UsuarioVO usuario ) throws Exception ;

//    void adicionarHorarioAlunoSemanal(HorarioAlunoTurnoVO horarioAlunoTurnoVO, List<HorarioDisciplinaVO> horarioDisciplinaVOs, DisciplinaVO disciplina, TurmaVO turma) throws Exception;

//    List<HorarioDisciplinaVO> consultarDisciplinaSemana(HorarioTurmaVO horarioTurmaVO, Integer codigoDisciplina) throws Exception;

    boolean montarDadosHorarioAlunoDiario(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioTurmaVO horarioTurmaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception;

//    boolean montarDadosHorarioAlunoSemanal(HorarioAlunoTurnoVO horarioAlunoTurnoVO, HorarioTurmaVO horarioTurmaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception;

    boolean montarHorarioAluno(HorarioAlunoVO horarioAlunoVO, List<HorarioTurmaVO> horarioTurmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs,UsuarioVO usuario) throws Exception;

	List<HorarioAlunoTurnoVO> consultarHorarioAlunoPorMatriculaPeriodoDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuario) throws Exception;

	Boolean realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(MatriculaPeriodoVO matriculaPeriodoVO, List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Integer turma, Integer turmaPratica, Integer turmaTeorica, Integer disciplina, UsuarioVO usuarioVO, Boolean retornarExcecao, Boolean validarChoqueComOutrasMatriculaAluno) throws Exception;

	void realizarLimpezaRegistroChoqueHorario(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs);

	List<HorarioAlunoTurnoVO> consultarMeusHorariosAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, Integer turno, boolean visaoMensal, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, boolean validarAulaNaoRegistrada, Integer codigoUnidadeEnsino) throws Exception;

	Date consultarPrimeiroDiaAulaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

	Date consultarUltimoDiaAulaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 21/09/2015
	 * @param turma
	 * @param matricula
	 * @param ano
	 * @param semestre
	 * @return
	 */
	List<ChoqueHorarioAlunoDetalheVO> consultarChoqueHorarioAlunoDetalhePorTurmaAnoSemestre(TurmaVO turma, String matricula, String ano, String semestre, Integer disciplina);

	/**
	 * @author Rodrigo Wind - 21/09/2015
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @return
	 */
	List<ChoqueHorarioAlunoVO> consultarChoqueHorarioAlunoPorTurmaAnoSemestre(TurmaVO turma, String ano, String semestre, Integer disciplina);

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param dataBase
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<AgendaAlunoRSVO> consultarAgendaHorarioAlunoDiaItem(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param dataBase
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<AgendaAlunoRSVO> consultarAgendaCalendariAlunoEad(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param dataBase
	 * @param unidadeEnsinoVO
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<AgendaAlunoRSVO> consultarAgendaFeriadoDiaItem(Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param dataBase
	 * @param unidadeEnsinoVO
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<AgendaAlunoRSVO> consultarAgendaAlunoDia(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param matriculaPeriodoTurmaDisciplinaVOs
	 * @param dataBase
	 * @param turno
	 * @param visaoMensal
	 * @param unidadeEnsinoVO
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<DataEventosRSVO> consultarDatasMeusHorariosAlunoEspecificoAplicativo(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date dataBase, Integer turno, boolean visaoMensal, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception;
	
	List<CronogramaDeAulasRelVO> criarObjetoRelatorio(List<HorarioAlunoTurnoVO> lista, UsuarioVO usuarioVO);

	List<CronogramaDeAulasRelVO> realizarCriacaoHorarioAlunoModeloMatricula(List<HorarioAlunoTurnoVO> lista);
	
	public List<HorarioAlunoTurnoVO> consultarMeusHorariosDisciplinaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, boolean validarAulaNaoRegistrada) throws Exception;
	
	public List<ChoqueHorarioAlunoVO> consultarChoqueHorarioCalendarioAluno(Integer turma, String ano, String semestre, Integer disciplina, Integer professor, Date dataBase, String horarioInicial, String horarioFinal);
	
	CalendarioHorarioAulaVO<DataEventosRSVO> realizarGeracaoCalendarioAluno(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs,String matricula, UnidadeEnsinoVO unidadeEnsinoVO, MesAnoEnum mesAno, Integer ano, UsuarioVO usuarioVO) throws Exception;


}
