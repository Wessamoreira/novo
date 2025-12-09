package negocio.interfaces.blackboard;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.OperacaoImportacaoSalaBlackboardEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardGrupoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.SugestaoFacilitadorBlackboardVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface SalaAulaBlackboardInterfaceFacade {

	public StringBuilder getSqlConsultaDeVerificacaoSeMatriculaAptaEstagio(String ano, String semestre);
	
	public StringBuilder getSqlConsultaDeVerificacaoSeMatriculaAptaTcc(String ano, String semestre, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, boolean isAlunoNaoEnsalado);
	
	SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaDesensalarEstagio() throws Exception;
	
	SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaDesensalarTccAmbientacao() throws Exception;
	
	SqlRowSet realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardTccAmbientacao(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	List<MatriculaPeriodoVO> realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio(List<Integer> listaCurso, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	void realizarVerificacaoSalaAulaBlackboardPorGradeCurricularEstagio(EstagioVO estagioVO, UsuarioVO usuarioVO) throws Exception;

	void realizarVerificacaoSalaAulaBlackboardPorModuloTccAmbientacao(MatriculaVO matriculaVO , MatriculaPeriodoVO ultimaMatriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;


	void realizarVerificacaoSalaAulaBlackboardPorModuloEstagio(MatriculaVO matriculaVO , MatriculaPeriodoVO ultimaMatriculaPeriodoVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuarioVO) throws Exception;

	SalaAulaBlackboardVO realizarGeracaoSalaAulaBlackboardPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO pto, SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;

	SalaAulaBlackboardVO realizarGeracaoSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;
	
	SalaAulaBlackboardVO realizarVerificacaoDadosSeiComDadosBlackboard(Integer codigoSalaAula , UsuarioVO usuarioVO) throws Exception;

	SalaAulaBlackboardVO realizarRevisaoSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarRevisaoSalaAulaBlackboardPorTurma(TurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarRevisaoSalaAulaBlackboardPorTurmaPorDisciplinaPorAnoPorSemestrePorProfessor(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, CursoVO curso, TurmaVO turma, DisciplinaVO displina, String ano, String semestre, Integer bimestre, Integer nrSala, Integer professor, UsuarioVO usuarioVO) throws Exception;

	void realizarExclusaoSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarExclusaoSalaAulaBlackboardEad(SalaAulaBlackboardVO objFiltro, UsuarioVO usuarioVO) throws Exception;

	void realizarBuscaAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj, ConfiguracaoGeralSistemaVO confGeral, UsuarioVO usuarioVO) throws Exception;

	void realizarEnvioConviteAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(SalaAulaBlackboardVO obj,  SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, OperacaoEnsalacaoBlackboardEnum operacao, UsuarioVO usuarioVO) throws Exception;

	HttpResponse<JsonNode> realizarAtualizacaoAlunoSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public SalaAulaBlackboardVO realizarConsultaSalaAulaBlackboardConteudoMaster(String idSalaAulaBlackboard,  UsuarioVO usuarioVO) throws Exception;
	
	List<SalaAulaBlackboardVO> realizarConsultaSalaAulaBlackboardConteudoMaster(UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoSalaAulaBlackboardPorHorarioTurma(HorarioTurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	HttpResponse<JsonNode> realizarProcessamentoLoteSalaAulaBlackboard(UsuarioVO usuarioVO) throws Exception;

	HttpResponse<JsonNode> realizarProcessamentoLoteSalaAulaBlackboardPorHorarioTurma(HorarioTurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarSalaAulaBlackboardOperacao(UsuarioVO usuarioVO) throws Exception;

	List<SalaAulaBlackboardVO> consultarSalaAulaBlackboard(SalaAulaBlackboardVO salaAulaBlackboardVOFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<SalaAulaBlackboardVO> consultarSeExisteSalaAulaBlackboardComVagaGrupo(Integer curso, Integer agrupamentoUnidadeEnsino, Integer disciplina, String ano, String semestre, Integer bimestre, Integer vagaPorSala, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardEad(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer bimestre, Integer programacaoTutoriaOnline, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	SalaAulaBlackboardVO consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, Integer bimestre, Integer nrSala, Integer professorEad, UsuarioVO usuairo);

	void realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception;

	Integer consultarProximoNrGrupoSalaAulaBlackboard(String id, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardGrupoEnum) throws Exception;
	
	Integer consultarQtdeGrupoExistenteSalaAulaBlackboard(String id, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardGrupoEnum) throws Exception;

	Integer consultarProximoNrSalaAulaBlackboard(Integer curso, Integer agrupamentoUnidadeEnsino,Integer disciplina, String ano, String semestre, Integer bimestre, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) throws Exception;

	Boolean consultarSeExisteAlgumSalaAulaBlackboardTutoriaOnline(PessoaVO pessoa, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	Boolean consultarSeExisteAgrupamentoUnidadeEnsino(Integer agrupamentoUnidadeEnsino, boolean isLevantarExcecao, UsuarioVO usuarioVO) throws Exception;

	List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardTutoriaOnline(PessoaVO pessoa, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	Boolean consultarSeExisteSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(String matricula, TipoSalaAulaBlackboardEnum  tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO);

	SalaAulaBlackboardVO consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(String matricula, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO);

	void visualizarTelaFechamentoGrupoSalaAulaBlackboard(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, List<SalaAulaBlackboardGrupoVO> listaFechamento, ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum,  DisciplinaVO discipinaVO, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;
	
	public List<SalaAulaBlackboardGrupoVO> consultarSalaAulaMontagemSalaTccGrupo(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, DisciplinaVO discipinaVO, String ano, String semestre, String nivelEducacional, UsuarioVO usuarioVO) throws Exception;
	
	public List<SalaAulaBlackboardGrupoVO> consultarSalaAulaMontagemSalaProjetoIntegrador(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO, Integer disciplina, String ano, String semestre, Integer bimestre, String nivelEducacional) throws Exception;
	
	public List<SalaAulaBlackboardVO> consultarSalaAulaMontagemSalaEstagio(CursoVO curso, String ano, String semestre, String nivelEducacional, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosTccPorSalaAulaBlackboardGrupo(SalaAulaBlackboardGrupoVO salaAulaBlackboardGrupo, String acao) throws Exception;
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosProjetoIntegradorPorSalaAulaBlackboardGrupo(SalaAulaBlackboardGrupoVO salaAulaBlackboardGrupo, String acao) throws Exception;
	
	void realizarImportacaoSalaAulaBlackboard(ConfiguracaoSeiBlackboardVO configSeiBlackboardVO, UsuarioVO usuarioVO) throws Exception;

	void consultarSalaAulaImportada(DataModelo controleConsulta, String nomeSala, String nomeGrupo, UsuarioVO usuarioVO) throws Exception;

	void persistir(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;

	void consultar(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuario, DataModelo controleConsulta, String nivelEducacionalApresentar) throws Exception;

	List<SugestaoFacilitadorBlackboardVO> consultarSugestaoFacilitadores(String ano, String semestre, UsuarioVO usuario);

	void sugerirFacilitadores(List<SugestaoFacilitadorBlackboardVO> listaSugestaoFacilitadores, Integer quantidadeFacilitadoresPorSala, String ano, String semestre, UsuarioVO usuario) throws Exception;	

	void consultarSalaAulaBlackboardGrupoTcc(String matricula, GradeCurricularVO gradeCurricularVO, String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception;
	
	void consultarSalaAulaBlackboardGrupoProjetoIntegrador(String matricula, Integer unidadeEnsino, Integer disciplina,  String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception; 

	void consultarSalaAulaBlackboardGrupoPorMatriculaPorTipoSalaAulaBlackboardEnum(MatriculaVO matricula, Integer disciplina, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum,  String ano, String semestre, DataModelo controleConsulta, UsuarioVO usuarioVO) throws Exception;

	SalaAulaBlackboardVO consultarPorIdSalaAulaBlackboardPorTipoSalaAulaBlackboardEnum(String idSalaAulaBlackboard, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum);
	
	SalaAulaBlackboardVO consultarPorChavePrimaria(Integer codigo);

	File realizarGeracaoExcelSalas(List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean exportarSala, Boolean exportarAlunos, Boolean exportarNotas, Boolean exportarProfessores, Boolean exportarFacilitadores, Boolean exportarSupervisores, Integer qtdLinhasExportacaoPorSalaAulaBlackboard, UsuarioVO usuario) throws Exception;

	void upLoadArquivoImportado(InputStream inputStream, List<SalaAulaBlackboardPessoaVO> salaAulaBlackboardPessoaVOs, UsuarioVO usuario) throws Exception;

	void realizarImportacao(List<SalaAulaBlackboardPessoaVO> salaAulaBlackboardPessoaVOs, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoProfessor, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoFacilitador, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoSupervisor, OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoNota, UsuarioVO usuario) throws Exception;

	void excluir(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) throws Exception;

	void realizarAdicaoPessoaSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarRemocaoPessoaSalaAulaBlackboard(SalaAulaBlackboardVO obj, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception;

	List<SalaAulaBlackboardVO> consultarGeracaoSalaAulaBlackboard(Integer disciplina, String ano, String semestre,
			Integer bimestre, Integer situacaoAlunos, String nivelEducacionalApresentar) throws Exception;

	SalaAulaBlackboardVO consultarSalaAulaBlackboardPorMatriculaPeriodoTurmaDisciplina(
			Integer matriculaPeriodoTurmaDisciplina, UsuarioVO usuario);

	void realizarGeracaoSalaAulaBlackboard(ConfiguracaoSeiBlackboardVO configSeiBlackboardVO, UsuarioVO usuarioVO)
			throws Exception;

//	void apurarNotas(ProgressBarVO progressBarVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean realizarCalculoMediaApuracaoNotas, UsuarioVO usuarioLogado) throws Exception;
	
	public void preencherNotaHistoricoSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO sabPessoaNota, Map<Integer, ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOMap, HistoricoVO historicoVO, boolean considerarAuditoria, UsuarioVO usuarioLogado, StringBuilder log ) throws Exception; 
	
	public void preencherHistoricoNotaBlackboardNaoLocalizado(SalaAulaBlackboardNotaVO obj, SalaAulaBlackboardPessoaNotaVO salaAulaBlackboardPessoaNotaVO,  UsuarioVO usuario) throws Exception;
	
	public void executarApuracaoDeNotaSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj,  boolean considerarAuditoria, UsuarioVO usuario) throws Exception;
	
	public void executarConsolidacaoDeNotasApuradasNoSeiPorSalaAulaBlackboard(SalaAulaBlackboardNotaVO obj, UsuarioVO usuario) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(String listaAlunos, DisciplinaVO disciplinaVO, String ano, String semestre)
			throws Exception;

	List<SalaAulaBlackboardVO> consultarSalaAulasExistentes(String salaAulaExistentes, UsuarioVO usuarioLogado) throws Exception;

	List<SalaAulaBlackboardVO> consultarSalaAulaBlackboardAgrupada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardCopiaConteudo(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, String nivelEducacionalApresentar) throws Exception;
	
	List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardApurarNota(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, String nivelEducacionalApresentar) throws Exception;

	List<SalaAulaBlackboardVO> consultarGruposPorFacilitador(Integer codigoPessoaFacilitador) throws Exception;

	public SalaAulaBlackboardVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados);

	public File realizarGeracaoExcelLogNotas(DataModelo dataModelo, UsuarioVO usuarioLogado) throws Exception;

	List<PessoaVO> consultarFacilitadorProfessorSupervisorPorCPF(String valorConsultaPessoa, String ano, String semestre) throws Exception;

	List<PessoaVO> consultarFacilitadorProfessorSupervisorPorNome(String valorConsultaPessoa, String ano, String semestre) throws Exception;

	//List<SalaAulaBlackboardVO> consultaPadraoSalaAulaBlackBoardRealizarCalculoMediaApuracaoNotas(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;

	

}
