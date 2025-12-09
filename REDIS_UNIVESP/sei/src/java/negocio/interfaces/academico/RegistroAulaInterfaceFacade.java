package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TransferenciaEntradaRegistroAulaFrequenciaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.job.NotificacaoRegistroAulaNaoLancadaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.AlunoComFrequenciaBaixaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface RegistroAulaInterfaceFacade {

	public RegistroAulaVO novo() throws Exception;

	public void incluir(RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, UsuarioVO usuario, Boolean validarCalendarioRegistroAula) throws Exception;

	public void incluir(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, Integer perfilPadraoProfessor, UsuarioVO usuario, Boolean validarCalendarioRegistroAula) throws Exception;

	public void alterar(RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, UsuarioVO usuario) throws Exception;

	public void alterar(final RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura, Integer perfilPadraoProfessor, UsuarioVO usuario) throws Exception;

	public void excluir(RegistroAulaVO obj, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception;
	
	public boolean existeRegistroAula(TurmaVO turma, DisciplinaVO disciplina);

	public RegistroAulaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorTurmaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCursoProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultarPorIdentificadorTurmaProfessorDisciplina(TurmaVO turmaVO, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeDisciplinaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDataProfessor(Date prmIni, Date prmFim, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void carregarCargaHoraria(RegistroAulaVO registroAulaVO, HorarioTurmaVO obj, UsuarioVO usuario) throws Exception;

	public List<PessoaVO> montarListaProfessorRegistroAula(HorarioTurmaVO obj, RegistroAulaVO registroAulaVO, UsuarioVO usuario);

	public List<PessoaVO> carregarCargaHorariaEmontarListaProfessorRegistroAula(RegistroAulaVO registroAulaVO, UsuarioVO usuario) throws Exception;

	public void montarAlunosTurma(RegistroAulaVO registroAulaVO, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception;

	public Integer consultarSomaCargaHorarioDisciplina(Integer codigoTurma, String semestre, String ano, Integer codigoDisciplina, boolean b, UsuarioVO usuario) throws Exception;
	
	public Integer consultarSomaCargaHorarioDisciplina(Integer codigoTurma, String semestre, String ano, Integer codigoDisciplina, boolean b, UsuarioVO usuario, boolean validarConfiguracaoCurso) throws Exception;

	public RegistroAulaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void carregarDados(RegistroAulaVO obj, UsuarioVO usuario) throws Exception;

	public RegistroAulaVO consultarPorChavePrimaria(Integer codRegistroAula, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultarPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(Integer codigoTurma, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(Integer codigoTurma, Boolean turmaAgrupada, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, Boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

//	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaEReposicao(Integer codigoTurma, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario) throws Exception;

	public void montarRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, Boolean isLancadoRegistro, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario, boolean trazerAlunosPendenteFinanceiramente, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public void montarRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario, Boolean controlarAbonoFaltaNovosRegistrosAula, Boolean isLancadoRegistro, boolean trazerAlunosPendenteFinanceiramente, boolean trazerAlunosTransferenciaMatriz, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public RegistroAulaVO consultarPorDataTurmaProfessor(Date dataAula, TurmaVO turma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultarPorHorarioTurmaDia(HorarioProfessorDiaVO horarioProfessorDia, Integer codigoTurma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> montarRegistrosAulaComBaseHorarioTurma(HorarioTurmaDiaVO horarioTurmaDia, DisciplinaVO disciplina, PessoaVO professor, TurnoVO turno, UsuarioVO responsavelRegistroAula, TurmaVO turma, List<HistoricoVO> historicoVOs, String ano, String semestre, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	// public void montarRegistrosAulaComBaseHorarioTurma(HorarioTurmaDiaVO
	// horarioTurmaDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO
	// disciplina, PessoaVO professor, Integer cargaHoraria, UsuarioVO
	// responsavelRegistroAula, TurmaVO turma,
	// List<MatriculaPeriodoTurmaDisciplinaVO>
	// listaMatriculaPeriodoTurmaDisciplina, ConfiguracaoFinanceiroVO
	// confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void adicionarMatriculasRegistrosAula(List<String> listaHorario, HorarioProfessorDiaVO horarioProfessorDia, List<RegistroAulaVO> listaRegistroAulaVO, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, Integer cargaHoraria, UsuarioVO responsavelRegistroAula, String tipoAula, TurmaVO turma, String conteudo, boolean filtroVisaoProfessor, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> executarMontagemDadosRegistroAulaENota(Integer unidadeEnsino, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, PessoaVO pessoaVO, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean trazerAlunoPendenteFinanceiramente, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public void excluirComBaseNaProgramacaoAula(Integer turma, Integer disciplina, String semestre, String ano, Date dataAula, String nrAula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public Boolean consultarRegistroAulaExistente(String horario, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date data, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario) throws Exception;

//	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVerso(Integer codigoTurma, Boolean turmaAgrupada, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> executarMontagemDadosRegistroAulaENota(Integer unidadeEnsino, TurmaVO turma, DisciplinaVO disciplina, String semestre, String ano, PessoaVO pessoaVO, boolean coordenador, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean trazerAlunoPendenteFinanceiramente, ConfiguracaoAcademicoVO configuracaoAcademicoVO, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception;

	public void excluirComBaseNaProgramacaoAulaPorDia(Integer turma, String semestre, String ano, Date dataAula, Integer professor, UsuarioVO usuarioLogado) throws Exception;

	public void excluirComBaseNaProgramacaoAulaPorAnoSemestreProfessor(Integer turma, String semestre, String ano, Integer professor, UsuarioVO usuarioLogado) throws Exception;

	public RegistroAulaVO consultaRapidaPorTurmaDataDiaSemanaDisciplinaHorario(Integer codigoTurma, Date dataAula, String diaSemana, Integer disciplina, String semestre, String ano, String horario, boolean referenteAulaComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void validarDadosRegistroAulaNotaTurma(Integer turma, Integer professor, Integer disciplina, ConfiguracaoAcademicoVO configuracaoAcademicoVO, String periodicidade, String ano, String semestre) throws Exception;

	public boolean realizarVerificacaoPorDataTurmaProfessorDisciplina(Date dataAula, TurmaVO turma, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorHorarioTurmaDia(HorarioProfessorDiaVO horarioProfessorDia, TurmaVO turmaVO, Integer codigoDisciplina, Integer codigoProfessor, String ano, String semestre, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiro, boolean trazerAlunosTransferenciaMatriz) throws Exception;

	public Integer consultarSomaCargaHorarioDisciplinaComposta(Integer turma, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaFaltasAluno(String matriculaAluno, Integer codigoDisciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<RegistroAulaVO> consultaRapidaFaltasAlunoQuantidade(Integer turma, String matriculaAluno, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Boolean consultarExistenciaRegistroAula(String matricula, Integer turma, Integer disciplina, Integer professor, String semestre, String ano) throws Exception;

	public List<RegistroAulaVO> consultaRapidaFaltasAlunoTurma(Integer turma, String matriculaAluno, Integer codigoDisciplina, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorTurmaProfessorDisciplinaEntreDatas(String identificadorTurma, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Map<String, List<AlunoComFrequenciaBaixaRelVO>> consultarMatriculaComFrequenciaBaixaEmailAutomatico();

	public Boolean existeRegistroAulaCarregadoListaDiaAulaNaoRegistrada(String horario, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date data, List<RegistroAulaVO> listaRegistroAulaVOs, UsuarioVO usuarioVO);

	List<NotificacaoRegistroAulaNaoLancadaVO> consultarDadosNotificacaoNaoLancamentoRegistroAula();

	public void carregarDadosVisaoAdministrativa(RegistroAulaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception;

//	public List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaVisaoAdministrativa(Integer codigoTurma, Boolean turmaAgrupada, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupada(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferido, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVerso(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, String tipoLayout, Boolean trazerAlunoTransferido, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaEReposicao(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferencia, boolean permitirRealizarLancamentoAlunosPreMatriculados, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaVisaoAdministrativa(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professor, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, UsuarioVO usuario, Boolean trazerAlunoTransferencia, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception;

	List<RegistroAulaVO> consultaRapidaPorTurmaProfessorDisciplinaConsiderandoTurmaAgrupadaRelVersoVisaoAdministrativa(TurmaVO turmaVO, String semestre, String ano, List<ProfessorTitularDisciplinaTurmaVO> professores, Integer disciplina, Integer unidadeEnsino, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, String filtroTipoCursoAluno, String tipoAluno, boolean controlarAcesso, NivelMontarDados nivelMontarDados, Boolean liberarRegistroAulaEntrePeriodo, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroAcademicoVO, String mes, String anoMes, Integer configuracaoAcademico, Boolean trazerAlunoTransferido, String tipoFiltroPeriodo, Date dataInicio, Date dataFim) throws Exception;

	public Integer consultarSomaCargaHorarioDisciplinaMinistradaPorOutroProfessor(Integer turma, Integer professor, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void realizarCriacaoRegistroAulaAPartirTransferenciaEntrada(String matricula, List<TransferenciaEntradaRegistroAulaFrequenciaVO> transferenciaEntradaRegistroAulaFrequenciaVOs, UsuarioVO usuarioVO) throws Exception;

	public Integer consultarCargaHorariaTotal(RegistroAulaVO registroAula, TurmaVO turmaVO, String semestre, String ano) throws Exception;

	public void verificarVinculoRegistroAula(Integer turma, Integer disciplina, Integer professor, Date dataAula) throws Exception;

	public List<RegistroAulaVO> montarRegistrosAulaComBaseHorarioTurmaDataNaoConstaBase(HorarioTurmaDiaVO horarioTurmaDia, DisciplinaVO disciplina, PessoaVO professor, TurnoVO turno, UsuarioVO responsavelRegistroAula, TurmaVO turma, List<FrequenciaAulaVO> frequenciaAulaVOs, String ano, String semestre, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	Integer consultaRapidaFaltasAlunoBoletimAcademico(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<RegistroAulaVO> consultarRegistroAulaPorTurmaDisciplinaPeriodo(Integer turma, Integer disciplina, Date dataInicio, Date dataTermino, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception;

	void excluir(List<RegistroAulaVO> listaRegistrosAula, String acaoLog, UsuarioVO usuario, Boolean atividadeComplementar) throws Exception;

	void persistir(List<RegistroAulaVO> registroAulaVOs, String conteudo, String tipoAula, Boolean permiteLancamentoAulaFutura, String idEntidade, String operacao, UsuarioVO usuarioVO, Boolean validarCalendarioRegistroAula) throws Exception;

	public String consultarRegistroAulaPorDisciplinaMatricula(String matricula, Integer disciplina, UsuarioVO usuarioVO);

	Map<BimestreEnum, Integer> consultarQuantidadeFaltasAlunoBimestre(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FrequenciaAulaVO</code> ao List <code>frequenciaAulaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @author Wellington Rodrigues - 06/04/2015
	 * @param obj
	 * @param frequenciaAulaVOs
	 * @throws Exception
	 */
	void adicionarFrequenciaAulaVOs(FrequenciaAulaVO obj, List<FrequenciaAulaVO> frequenciaAulaVOs) throws Exception;

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>FrequenciaAulaVO</code> no List <code>frequenciaAulaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @author Wellington Rodrigues - 06/04/2015
	 * @param matricula
	 * @param frequenciaAulaVOs
	 * @throws Exception
	 */
	void removerFrequenciaAulaVOs(String matricula, List<FrequenciaAulaVO> frequenciaAulaVOs) throws Exception;

	/** 
	 * @author Wellington Rodrigues - 2 de jul de 2015 
	 * @param matricula
	 * @param disciplina
	 * @param semestre
	 * @param ano
	 * @param turma
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	Integer consultarTotalRegistroAula(String matricula, Integer disciplina, String semestre, String ano, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 17 de out de 2015 
	 * @param turma
	 * @param semestre
	 * @param ano
	 * @param disciplina
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	public Integer consultarSomaCargaHorariaDisciplinaConsiderantoTurmaTeoricaETurmaPratica(Integer turmaPratica, Integer turmaTeorica, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception ;
	
	public PersonalizacaoMensagemAutomaticaVO carregarDadosMensagemPersonalizada(UsuarioVO usuarioVO, Integer unidadeEnsino) throws Exception;
	
	public void executarEnvioComunicadoInternoRegistroAula(ComunicacaoInternaVO comunicacaoInternaVO, String dataRegistro, String conteudo, UsuarioVO usuarioVO,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,Boolean enviarSms,Boolean enviarEmail) throws Exception;
	public String realizarSubstituicaoTagMensagem(String conteudo, UsuarioVO usuario, String dataRegistro, final String mensagemTemplate);
	public String realizarSubstituicaoTagMensagemSms(String conteudo, UsuarioVO usuario, String dataRegistro, final String mensagemTemplate);

	List<RegistroAulaVO> realizarGeracaoRegistroAulaPeloHorarioProfessorDia(HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, PessoaVO professorVO, String ano, String semestre, Boolean trazerAlunosTransferenciaMatriz) throws Exception;

	Boolean verificarPermissaoVisualizarMatriculaTR_CA(UsuarioVO usuarioVO);

	void atualizarNovaDisciplinaRegistroAulaPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception;

	public void validarQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAulaVisaoProfessor(Date data, TurmaVO turma, DisciplinaVO disciplina, PessoaVO professor, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	void carregarDados(RegistroAulaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario,
			Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor,
			String filtroTipoCursoAluno, String tipoLayout, String tipoAluno, Boolean turmaAgrupada,
			Boolean liberarRegistroAulaEntrePeriodo, Boolean permitirRealizarLancamentoAlunosPreMatriculados)
			throws Exception;
	
	Integer consultarQtdeRegistroAulaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO);
}
