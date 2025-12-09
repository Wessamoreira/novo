package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import webservice.servicos.objetos.DataEventosRSVO;

public interface HorarioProfessorInterfaceFacade {

    public void incluir(HorarioProfessorVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(HorarioProfessorVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(HorarioProfessorVO obj, UsuarioVO usuario) throws Exception;

//    public HorarioProfessorVO consultarPorProfessorTurno(Integer codigoProfessor, Integer codigoTurno) throws Exception;
    public void executarCriacaoDisponibilidadeHorarioProfessor(HorarioProfessorVO horarioProfessorVO, TurnoVO turnoVO, Boolean marcarIndisponibilidade, UsuarioVO usuario);

    public void verificarDisponibilidadeProfessorApartirDisciplinaHorarioTurma(HorarioProfessorVO horarioProfessorVO, HorarioTurmaVO horarioTurma, Boolean alterarTodasAulas, UsuarioVO usuario) throws ConsistirException, Exception;

    public void executarSubstituicaoDisciplinaTurmaHorarioProfessorDiarioComNrAulaDiaEspecifico(HorarioProfessorVO horarioProfessor, TurmaVO turma, DisciplinaVO disciplinaVO, Date data, int nrAula, SalaLocalAulaVO sala, UsuarioVO usuario) throws Exception;

    public void executarSubstitucaoDisciplinaTurmaHorarioProfessorSemanal(HorarioProfessorVO horarioProfessor, Integer disciplina, Integer turma, DiaSemana diaSemana, Integer nrAula, UsuarioVO usuario) throws Exception;

    public HorarioProfessorDiaVO consultarHorarioProfessorPorDiaPorDia(HorarioProfessorVO horarioProfessorVO, Date date, UsuarioVO usuario);

    public void montarDadosHorarioProfessorDiaItemVOs(HorarioProfessorVO horarioProfessorVO, Date dataInicio, Date dataFim, UsuarioVO usuario);

    public void montarListaProfessorLecionaDisciplina(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario);

    public void montarListaProfessorLecionaTurma(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario);

    public void excluirHorarioProfessor(Integer professor, UsuarioVO usuario) throws Exception;

    public void incluirHorarioProfessor(Integer professorPrm, List objetos, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public HorarioProfessorVO consultarPorChavePrimaria(Integer codigoPrm, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

    public List consultarHorarioProfessor(Integer professor, boolean controlarAcesso, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public QuadroHorarioVO atualizarDadosQuadroHorario(HorarioProfessorVO obj, QuadroHorarioVO quadroHorario, Boolean detalhado, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public List<HorarioProfessorVO> consultarPorHorarioProfessorContendoDisciplina(Integer codigoDisciplina, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public void alterarSometeHorariosSemana(final HorarioProfessorVO obj, UsuarioVO usuario) throws Exception;

    public HorarioProfessorVO executarCriacaoHorarioProfessorTurno(PessoaVO pessoaVO, TurnoVO turnoVO, UsuarioVO usuario) throws Exception;

    public List<HorarioProfessorVO> consultarPorProfessor(Integer codigoProfessor, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public void executarCriacaoDisponibilidadeHorarioProfessorDiaSemana(HorarioProfessorVO horarioProfessorVO, TurnoVO turnoVO, DiaSemana diaSemana, Boolean marcarIndisponibilidade, UsuarioVO usuario);

    public void montarDadosListaQuadroHorarioVO(QuadroHorarioVO quadroHorarioVO, UsuarioVO usuario) throws Exception;

    public void inicializarDadosCalendario(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) throws Exception;

    public HorarioProfessorVO consultarPorProfessorTurno(Integer codigoProfessor, Integer codigoTurno, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public void alterarHorarioProfessorPorTurno(PessoaVO pessoa, Integer turno, UsuarioVO usuario) throws Exception;

    public HorarioProfessorDiaVO consultarHorarioProfessorPorDiaPorDiaComMontagemDiaItems(HorarioProfessorVO horarioProfessorVO, Date date, UsuarioVO usuario);

    public HorarioProfessorVO consultarHorarioProfessor(Integer professor, Integer codigoTurno, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public List<HorarioProfessorVO> consultarTodos(Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public HorarioProfessorVO consultarPorChavePrimaria(Integer codigoPrm, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    public void executarAtualizarHorariosProfessor(PessoaVO pessoa, Date dataInicio, Date dataFim) throws Exception;

    public HorarioProfessorVO consultarPorProfessorTurnoAnoSemestre(Integer codigoProfessor, Integer codigoTurno, String semestre, String ano, Integer turma, UsuarioVO usuario) throws Exception;

    public void montarDadosHorarioProfessorDiaItemVOsRegistroAula(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) throws Exception;
    
    public boolean consultarHorarioProfessorPorTurno(Integer turno,  UsuarioVO usuario) throws Exception;

	void corrigeHorarioProfessorComBaseTurma();

	HorarioProfessorVO consultarRapidaHorarioProfessorTurno(Integer codigoProfessor, Integer codigoTurno, UsuarioVO usuario) throws Exception;

	void realizarExclusaoHorarioProfessor(HorarioTurmaVO horarioTurmaVO, int professor, int disciplina,  Date dataInicio, Date dataTermino, int numeroAula, boolean alterarTodasAulas, List<RegistroAulaVO> registroAulaVOs, UsuarioVO usuario, Boolean liberarAulaAcimaLimite) throws Exception;

	HorarioProfessorVO consultaRapidaPorChavePrimaria(Integer codigoHorarioProfessor, UsuarioVO usuario) throws Exception;
	
	public List<TurmaDisciplinaVO> consultarChoqueHorarioCalendarioProfessor(Integer professor, Date dataBase, String horarioInicial, String horarioFinal);
	
	CalendarioHorarioAulaVO<DataEventosRSVO> realizarGeracaoCalendarioProfessor(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO, MesAnoEnum mesAno, Integer ano, UsuarioVO usuarioVO) throws Exception;
	
    
	void realizarGeracaoItemCalendarioProfessor(DataEventosRSVO dataEventoRSVO, Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Boolean visaoProfessor, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO, UsuarioVO usuarioVO) throws Exception;
    
}
