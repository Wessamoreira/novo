package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.RegraCalculoDisciplinaCompostaEnum;
import negocio.comuns.academico.enumeradores.TipoCalculoCargaHorariaFrequencia;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.academico.enumeradores.TipoUsoConfiguracaoAcademicoEnum;
import negocio.comuns.academico.enumeradores.VariaveisNotaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoAcademicoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConfiguracaoAcademicoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ConfiguracaoAcademicoVO</code>. Encapsula toda a interação com
 * o banco de dados.
 * 
 * @see ConfiguracaoAcademicoVO
 * @see ControleAcesso
 */	
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoAcademico extends ControleAcesso implements ConfiguracaoAcademicoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4571130270749429557L;
	protected static String idEntidade;

	public ConfiguracaoAcademico() throws Exception {
		super();
		setIdEntidade("ConfiguracaoAcademico");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>.
	 */
	public ConfiguracaoAcademicoVO novo() throws Exception {
		ConfiguracaoAcademico.incluir(getIdEntidade());
		ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAcademicoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception {
		try {			
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO ConfiguracaoAcademico( nome, formulaCalculoMediaFinal, nota1, utilizarNota1, tituloNota1, formulaCalculoNota1, " // 1-6
					+ "formulaUsoNota1, nota2, utilizarNota2, tituloNota2, formulaCalculoNota2, formulaUsoNota2, nota3, utilizarNota3, tituloNota3, " // 7
					+ "formulaCalculoNota3, formulaUsoNota3, nota4, utilizarNota4, tituloNota4, formulaCalculoNota4, formulaUsoNota4, nota5, utilizarNota5, " // 16
					+ "tituloNota5, formulaCalculoNota5, formulaUsoNota5, nota6, utilizarNota6, tituloNota6, formulaCalculoNota6, formulaUsoNota6, nota7, " // 25
					+ "utilizarNota7, tituloNota7, formulaCalculoNota7, formulaUsoNota7, nota8, utilizarNota8, tituloNota8, formulaCalculoNota8, formulaUsoNota8, " // 34
					+ "nota9, utilizarNota9, tituloNota9, formulaCalculoNota9, formulaUsoNota9, nota10, utilizarNota10, tituloNota10, formulaCalculoNota10, " // 43
					+ "formulaUsoNota10, percentualFrequenciaAprovacao, mascaraPadraoGeracaoMatricula, nota1MediaFinal, nota2MediaFinal, nota3MediaFinal, " // 52
					+ "nota4MediaFinal, nota5MediaFinal, nota6MediaFinal, nota7MediaFinal, nota8MediaFinal, nota9MediaFinal, nota10MediaFinal, configuracoes, " // 58
					+ "numeroDisciplinaConsiderarReprovadoPeriodoLetivo, reprovadoMatricularDisciplinaPeriodoLetivo, permiteEvoluirPeriodoLetivoCasoReprovado, " // 66
					+ "diasmaximoreativacaomatricula, renovacaomatriculasequencial, cursardisciplinaeprerequisito, apresentarNota1, apresentarNota2, apresentarNota3, " // 69
					+ "apresentarNota4, apresentarNota5, apresentarNota6, apresentarNota7, apresentarNota8 ,apresentarNota9, apresentarNota10, " // 75
					+ "notasDeCincoEmCincoDecimos, limitarDiasDownload, qtdeDiasLimiteDownload, notasDeCincoEmCincoDecimosApenasMedia, " // 82
					+ "nota11, utilizarNota11, tituloNota11, formulaCalculoNota11, formulaUsoNota11, nota11MediaFinal, apresentarNota11, " // 86
					+ "nota12, utilizarNota12, tituloNota12, formulaCalculoNota12, formulaUsoNota12, nota12MediaFinal, apresentarNota12, " // 93
					+ "nota13, utilizarNota13, tituloNota13, formulaCalculoNota13, formulaUsoNota13, nota13MediaFinal, apresentarNota13, enviarMensagemNotaAbaixoMedia, apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico, " // 100-106
					+ "pesoMediaNotaMeritoAcademico, pesoMediaFrequenciaMeritoAcademico, usarSituacaoAprovadoAproveitamentoTransferenciaGrade, considerarCampoNuloNotaZerada, apresentardisciplinasemaulaprogramadaminhasnotasvisaoaluno, " + "liberarPreRequisitoDisciplinaConcomitancia, apresentarTextoSemNotaCampoNuloHistorico, " + "utilizarNota1PorConceito, utilizarNota2PorConceito, utilizarNota3PorConceito, utilizarNota4PorConceito, utilizarNota5PorConceito, " + "utilizarNota6PorConceito, utilizarNota7PorConceito, utilizarNota8PorConceito, utilizarNota9PorConceito, utilizarNota10PorConceito, " + "utilizarNota11PorConceito, utilizarNota12PorConceito, utilizarNota13PorConceito, " + "utilizarComoSubstitutiva1, utilizarComoSubstitutiva2, utilizarComoSubstitutiva3, utilizarComoSubstitutiva4, "
					+ "utilizarComoSubstitutiva5, utilizarComoSubstitutiva6, utilizarComoSubstitutiva7, utilizarComoSubstitutiva8, "
					+ "utilizarComoSubstitutiva9, utilizarComoSubstitutiva10, utilizarComoSubstitutiva11, utilizarComoSubstitutiva12, " + "utilizarComoSubstitutiva13, politicaSubstitutiva1, politicaSubstitutiva2, politicaSubstitutiva3, " + "politicaSubstitutiva4, politicaSubstitutiva5, politicaSubstitutiva6, politicaSubstitutiva7, politicaSubstitutiva8, " + "politicaSubstitutiva9, politicaSubstitutiva10, politicaSubstitutiva11, politicaSubstitutiva12, politicaSubstitutiva13, " + "utilizarArredondamentoMediaParaMais, tipoCalculoCargaHorariaFrequencia, permiteRegistrarAulaFutura,"
					+ "nota14, utilizarNota14, tituloNota14, formulaCalculoNota14, formulaUsoNota14, nota14MediaFinal, apresentarNota14, politicaSubstitutiva14, utilizarNota14PorConceito, utilizarComoSubstitutiva14, bimestreNota14, "
					+ "nota15, utilizarNota15, tituloNota15, formulaCalculoNota15, formulaUsoNota15, nota15MediaFinal, apresentarNota15, politicaSubstitutiva15, utilizarNota15PorConceito, utilizarComoSubstitutiva15, bimestreNota15, " + "nota16, utilizarNota16, tituloNota16, formulaCalculoNota16, formulaUsoNota16, nota16MediaFinal, apresentarNota16, politicaSubstitutiva16, utilizarNota16PorConceito, utilizarComoSubstitutiva16, bimestreNota16, " + "nota17, utilizarNota17, tituloNota17, formulaCalculoNota17, formulaUsoNota17, nota17MediaFinal, apresentarNota17, politicaSubstitutiva17, utilizarNota17PorConceito, utilizarComoSubstitutiva17, bimestreNota17, "
					+ "nota18, utilizarNota18, tituloNota18, formulaCalculoNota18, formulaUsoNota18, nota18MediaFinal, apresentarNota18, politicaSubstitutiva18, utilizarNota18PorConceito, utilizarComoSubstitutiva18, bimestreNota18, "
					+ "nota19, utilizarNota19, tituloNota19, formulaCalculoNota19, formulaUsoNota19, nota19MediaFinal, apresentarNota19, politicaSubstitutiva19, utilizarNota19PorConceito, utilizarComoSubstitutiva19, bimestreNota19, " + "nota20, utilizarNota20, tituloNota20, formulaCalculoNota20, formulaUsoNota20, nota20MediaFinal, apresentarNota20, politicaSubstitutiva20, utilizarNota20PorConceito, utilizarComoSubstitutiva20, bimestreNota20, " + "nota21, utilizarNota21, tituloNota21, formulaCalculoNota21, formulaUsoNota21, nota21MediaFinal, apresentarNota21, politicaSubstitutiva21, utilizarNota21PorConceito, utilizarComoSubstitutiva21, bimestreNota21, "
					+ "nota22, utilizarNota22, tituloNota22, formulaCalculoNota22, formulaUsoNota22, nota22MediaFinal, apresentarNota22, politicaSubstitutiva22, utilizarNota22PorConceito, utilizarComoSubstitutiva22, bimestreNota22, "
					+ "nota23, utilizarNota23, tituloNota23, formulaCalculoNota23, formulaUsoNota23, nota23MediaFinal, apresentarNota23, politicaSubstitutiva23, utilizarNota23PorConceito, utilizarComoSubstitutiva23, bimestreNota23, " + "nota24, utilizarNota24, tituloNota24, formulaCalculoNota24, formulaUsoNota24, nota24MediaFinal, apresentarNota24, politicaSubstitutiva24, utilizarNota24PorConceito, utilizarComoSubstitutiva24, bimestreNota24, " + "nota25, utilizarNota25, tituloNota25, formulaCalculoNota25, formulaUsoNota25, nota25MediaFinal, apresentarNota25, politicaSubstitutiva25, utilizarNota25PorConceito, utilizarComoSubstitutiva25, bimestreNota25, "
					+ "nota26, utilizarNota26, tituloNota26, formulaCalculoNota26, formulaUsoNota26, nota26MediaFinal, apresentarNota26, politicaSubstitutiva26, utilizarNota26PorConceito, utilizarComoSubstitutiva26, bimestreNota26, "
					+ "nota27, utilizarNota27, tituloNota27, formulaCalculoNota27, formulaUsoNota27, nota27MediaFinal, apresentarNota27, politicaSubstitutiva27, utilizarNota27PorConceito, utilizarComoSubstitutiva27, bimestreNota27, " + "nota28, utilizarNota28, tituloNota28, formulaCalculoNota28, formulaUsoNota28, nota28MediaFinal, apresentarNota28, politicaSubstitutiva28, utilizarNota28PorConceito, utilizarComoSubstitutiva28, bimestreNota28, " + "nota29, utilizarNota29, tituloNota29, formulaCalculoNota29, formulaUsoNota29, nota29MediaFinal, apresentarNota29, politicaSubstitutiva29, utilizarNota29PorConceito, utilizarComoSubstitutiva29, bimestreNota29, "
					+ "nota30, utilizarNota30, tituloNota30, formulaCalculoNota30, formulaUsoNota30, nota30MediaFinal, apresentarNota30, politicaSubstitutiva30, utilizarNota30PorConceito, utilizarComoSubstitutiva30, bimestreNota30, "
					
					+ "bimestreNota1, bimestreNota2, bimestreNota3, bimestreNota4, bimestreNota5, bimestreNota6, bimestreNota7, bimestreNota8, bimestreNota9, bimestreNota10, bimestreNota11, bimestreNota12, bimestreNota13, " + "tituloNotaApresentar1, tituloNotaApresentar2, tituloNotaApresentar3, tituloNotaApresentar4, tituloNotaApresentar5, tituloNotaApresentar6, tituloNotaApresentar7, tituloNotaApresentar8, tituloNotaApresentar9, tituloNotaApresentar10, " + "tituloNotaApresentar11, tituloNotaApresentar12, tituloNotaApresentar13, tituloNotaApresentar14, tituloNotaApresentar15, tituloNotaApresentar16, tituloNotaApresentar17, tituloNotaApresentar18, tituloNotaApresentar19, tituloNotaApresentar20, "
					+ "tituloNotaApresentar21, tituloNotaApresentar22, tituloNotaApresentar23, tituloNotaApresentar24, tituloNotaApresentar25, tituloNotaApresentar26, tituloNotaApresentar27, tituloNotaApresentar28, tituloNotaApresentar29, tituloNotaApresentar30, "
					+ "controlarAvancoPeriodoPorCreditoOuCH, tipoControleAvancoPeriodoPorCreditoOuCH, percCumprirPeriodoAnteriorRenovarProximoPerLetivo, percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo, " + "controlarInclusaoDisciplinaPorNrMaxCreditoOuCH, tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH, acumularCreditosOuCHPeriodosAnterioresNaoCumpridos, " + "permitirInclusaoDiscipDependenciaComChoqueHorario, qtdPermitirInclusaoDiscipDependenciaComChoqueHorario, permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta, "
					+ "faixaNota1Maior, faixaNota1Menor, faixaNota2Maior, faixaNota2Menor, faixaNota3Maior, faixaNota3Menor, faixaNota4Maior, faixaNota4Menor, faixaNota5Maior, faixaNota5Menor, faixaNota6Maior, faixaNota6Menor, faixaNota7Maior, faixaNota7Menor, faixaNota8Maior, faixaNota8Menor, faixaNota9Maior, faixaNota9Menor, faixaNota10Maior, faixaNota10Menor, "
					+ "faixaNota11Maior, faixaNota11Menor, faixaNota12Maior, faixaNota12Menor, faixaNota13Maior, faixaNota13Menor, faixaNota14Maior, faixaNota14Menor, faixaNota15Maior, faixaNota15Menor, faixaNota16Maior, faixaNota16Menor, faixaNota17Maior, faixaNota17Menor, faixaNota18Maior, faixaNota18Menor, faixaNota19Maior, faixaNota19Menor, " + "faixaNota20Maior, faixaNota20Menor, faixaNota21Maior, faixaNota21Menor, faixaNota22Maior, faixaNota22Menor, faixaNota23Maior, faixaNota23Menor, faixaNota24Maior, faixaNota24Menor, faixaNota25Maior, faixaNota25Menor, faixaNota26Maior, faixaNota26Menor, faixaNota27Maior, faixaNota27Menor, faixaNota28Maior, faixaNota28Menor, " + "faixaNota29Maior, faixaNota29Menor, faixaNota30Maior, faixaNota30Menor, quantidadeCasasDecimaisPermitirAposVirgula, "
					+ "apresentarSiglaConcessaoCredito, percMinimoCargaHorariaDisciplinaParaAproveitamento, tipoApresentarFrequenciaVisaoAluno, "
					+ "regraArredondamentoNota1, regraArredondamentoNota2, regraArredondamentoNota3, regraArredondamentoNota4, regraArredondamentoNota5, regraArredondamentoNota6, regraArredondamentoNota7, regraArredondamentoNota8, regraArredondamentoNota9, regraArredondamentoNota10, " + "regraArredondamentoNota11, regraArredondamentoNota12, regraArredondamentoNota13, regraArredondamentoNota14, regraArredondamentoNota15, regraArredondamentoNota16, regraArredondamentoNota17, regraArredondamentoNota18, regraArredondamentoNota19, regraArredondamentoNota20, " + "regraArredondamentoNota21, regraArredondamentoNota22, regraArredondamentoNota23, regraArredondamentoNota24, regraArredondamentoNota25, regraArredondamentoNota26, regraArredondamentoNota27, regraArredondamentoNota28, regraArredondamentoNota29, regraArredondamentoNota30, "
					+ "apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico, obrigaInformarFormaIngressoMatricula, obrigaInformarOrigemFormaIngressoMatricula, bloquearRegistroAulaAnteriorDataMatricula, "
					+ "ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao, situacaoDiscQueFazParteComposicaoControladaDiscPrincipal, calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes, "
					+ " habilitarControleInclusaoDisciplinaPeriodoFuturo, numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina, bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular, bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular, "
					+ " habilitarControleInclusaoObrigatoriaDisciplinaDependencia, porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia, removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline, habilitarDistribuicaoTurmaPraticaTeoricaRenovacao, distribuirTurmaPraticaTeoricaComAulaProgramada, removerDisciplinaTurmaPraticaTeoricaComChoqueHorario, "
					+ " considerarRegularAlunoDependenciaOptativa, utilizarApoioEADParaDisciplinasModalidadePresencial, considerarPortadoDiplomaTransEntradaAlunoIrregular, matricularApenasDisciplinaAulaProgramada, "
					
					+ "nota31, utilizarNota31, tituloNota31, formulaCalculoNota31, formulaUsoNota31, nota31MediaFinal, apresentarNota31, politicaSubstitutiva31, utilizarNota31PorConceito, utilizarComoSubstitutiva31, bimestreNota31, "
					+ "nota32, utilizarNota32, tituloNota32, formulaCalculoNota32, formulaUsoNota32, nota32MediaFinal, apresentarNota32, politicaSubstitutiva32, utilizarNota32PorConceito, utilizarComoSubstitutiva32, bimestreNota32, "
					+ "nota33, utilizarNota33, tituloNota33, formulaCalculoNota33, formulaUsoNota33, nota33MediaFinal, apresentarNota33, politicaSubstitutiva33, utilizarNota33PorConceito, utilizarComoSubstitutiva33, bimestreNota33, "
					+ "nota34, utilizarNota34, tituloNota34, formulaCalculoNota34, formulaUsoNota34, nota34MediaFinal, apresentarNota34, politicaSubstitutiva34, utilizarNota34PorConceito, utilizarComoSubstitutiva34, bimestreNota34, "
					+ "nota35, utilizarNota35, tituloNota35, formulaCalculoNota35, formulaUsoNota35, nota35MediaFinal, apresentarNota35, politicaSubstitutiva35, utilizarNota35PorConceito, utilizarComoSubstitutiva35, bimestreNota35, "
					+ "nota36, utilizarNota36, tituloNota36, formulaCalculoNota36, formulaUsoNota36, nota36MediaFinal, apresentarNota36, politicaSubstitutiva36, utilizarNota36PorConceito, utilizarComoSubstitutiva36, bimestreNota36, "
					+ "nota37, utilizarNota37, tituloNota37, formulaCalculoNota37, formulaUsoNota37, nota37MediaFinal, apresentarNota37, politicaSubstitutiva37, utilizarNota37PorConceito, utilizarComoSubstitutiva37, bimestreNota37, "
					+ "nota38, utilizarNota38, tituloNota38, formulaCalculoNota38, formulaUsoNota38, nota38MediaFinal, apresentarNota38, politicaSubstitutiva38, utilizarNota38PorConceito, utilizarComoSubstitutiva38, bimestreNota38, "
					+ "nota39, utilizarNota39, tituloNota39, formulaCalculoNota39, formulaUsoNota39, nota39MediaFinal, apresentarNota39, politicaSubstitutiva39, utilizarNota39PorConceito, utilizarComoSubstitutiva39, bimestreNota39, "
					+ "nota40, utilizarNota40, tituloNota40, formulaCalculoNota40, formulaUsoNota40, nota40MediaFinal, apresentarNota40, politicaSubstitutiva40, utilizarNota40PorConceito, utilizarComoSubstitutiva40, bimestreNota40, "
					+ "tituloNotaApresentar31, tituloNotaApresentar32, tituloNotaApresentar33, tituloNotaApresentar34, tituloNotaApresentar35, tituloNotaApresentar36, tituloNotaApresentar37, tituloNotaApresentar38, tituloNotaApresentar39, tituloNotaApresentar40, "
					+ "faixaNota31Maior, faixaNota31Menor, faixaNota32Maior, faixaNota32Menor, faixaNota33Maior, faixaNota33Menor, faixaNota34Maior, faixaNota34Menor, faixaNota35Maior, faixaNota35Menor, faixaNota36Maior, faixaNota36Menor, faixaNota37Maior, faixaNota37Menor, faixaNota38Maior, faixaNota38Menor, faixaNota39Maior, faixaNota39Menor, faixaNota40Maior, faixaNota40Menor, "
					+ "regraArredondamentoNota31, regraArredondamentoNota32, regraArredondamentoNota33, regraArredondamentoNota34, regraArredondamentoNota35, regraArredondamentoNota36, regraArredondamentoNota37, regraArredondamentoNota38, regraArredondamentoNota39, regraArredondamentoNota40, "
					+ "quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula, incluirAutomaticamenteDisciplinaGrupoOptativa, permitirAlunoRegularIncluirDisciplinaGrupoOptativa, bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa, considerarDisciplinasReprovadasPeriodosLetivosAnteriores  , criarDigitoMascaraMatricula, formulaCriarDigitoMascaraMatricula, "
					+ "reprovarFaltaDiscCompostaCasoReprovadoFaltaDiscFilha, ocultarFrequenciaDisciplinaComposta, tipoUsoConfiguracaoAcademico, regraCalculoDisciplinaComposta, ocultarMediaFinalDisciplinaCasoReprovado, obrigarAceiteAlunoTermoParaEditarRenovacao, "
					+ "validarChoqueHorarioOutraMatriculaAluno, validarDadosEnadeCensoMatricularAluno, usarFormulaCalculoFrequencia, formulaCalculoFrequencia, formulaCoeficienteRendimento, quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula ,mascaraNumeroProcessoExpedicaoDiploma, casasDecimaisCoeficienteRendimento, permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual, registrarComoFaltaAulasRealizadasAposDataMatricula, "
					+ "alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo, habilitarDistribuicaoDisciplinaDependenciaAutomatica  , habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares, mascaraNumeroRegistroDiploma , permitiraproveitamentodisciplinasoptativas "
					+ ") VALUES ( " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 20
					
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 40
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 60
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 80
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 100
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 120
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 140
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 160
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 180
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 200
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 20
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 40
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 60
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 80
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 100
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 120
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 140
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 160
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 180
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 200
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 30
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, "
					
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ? "
					+ ") returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario); // 72-165

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 1;
					sqlInserir.setString(i++, obj.getNome());
					sqlInserir.setString(i++, obj.getFormulaCalculoMediaFinal());
					sqlInserir.setDouble(i++, obj.getNota1().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota1().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota1());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota1());
					sqlInserir.setString(i++, obj.getFormulaUsoNota1());
					sqlInserir.setDouble(i++, obj.getNota2().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota2().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota2());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota2());
					sqlInserir.setString(i++, obj.getFormulaUsoNota2());
					sqlInserir.setDouble(i++, obj.getNota3().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota3().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota3());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota3());
					sqlInserir.setString(i++, obj.getFormulaUsoNota3());
					sqlInserir.setDouble(i++, obj.getNota4().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota4().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota4());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota4());
					sqlInserir.setString(i++, obj.getFormulaUsoNota4());
					sqlInserir.setDouble(i++, obj.getNota5().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota5().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota5());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota5());
					sqlInserir.setString(i++, obj.getFormulaUsoNota5());
					sqlInserir.setDouble(i++, obj.getNota6().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota6().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota6());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota6());
					sqlInserir.setString(i++, obj.getFormulaUsoNota6());
					sqlInserir.setDouble(i++, obj.getNota7().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota7().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota7());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota7());
					sqlInserir.setString(i++, obj.getFormulaUsoNota7());
					sqlInserir.setDouble(i++, obj.getNota8().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota8().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota8());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota8());
					sqlInserir.setString(i++, obj.getFormulaUsoNota8());
					sqlInserir.setDouble(i++, obj.getNota9().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota9().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota9());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota9());
					sqlInserir.setString(i++, obj.getFormulaUsoNota9());
					sqlInserir.setDouble(i++, obj.getNota10().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota10().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota10());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota10());
					sqlInserir.setString(i++, obj.getFormulaUsoNota10());
					sqlInserir.setDouble(i++, obj.getPercentualFrequenciaAprovacao().doubleValue());
					sqlInserir.setString(i++, obj.getMascaraPadraoGeracaoMatricula().trim());
					sqlInserir.setBoolean(i++, obj.getNota1MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota2MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota3MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota4MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota5MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota6MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota7MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota8MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota9MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getNota10MediaFinal().booleanValue());
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(i++, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					sqlInserir.setInt(i++, obj.getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo());
					sqlInserir.setBoolean(i++, obj.getReprovadoMatricularDisciplinaPeriodoLetivo());
					sqlInserir.setBoolean(i++, obj.getPermiteEvoluirPeriodoLetivoCasoReprovado());
					sqlInserir.setInt(i++, obj.getDiasMaximoReativacaoMatricula());
					sqlInserir.setBoolean(i++, obj.getRenovacaoMatriculaSequencial());
					sqlInserir.setBoolean(i++, obj.getPermiteCursarDisciplinaEPreRequisito());
					sqlInserir.setBoolean(i++, obj.getApresentarNota1());
					sqlInserir.setBoolean(i++, obj.getApresentarNota2());
					sqlInserir.setBoolean(i++, obj.getApresentarNota3());
					sqlInserir.setBoolean(i++, obj.getApresentarNota4());
					sqlInserir.setBoolean(i++, obj.getApresentarNota5());
					sqlInserir.setBoolean(i++, obj.getApresentarNota6());
					sqlInserir.setBoolean(i++, obj.getApresentarNota7());
					sqlInserir.setBoolean(i++, obj.getApresentarNota8());
					sqlInserir.setBoolean(i++, obj.getApresentarNota9());
					sqlInserir.setBoolean(i++, obj.getApresentarNota10());
					sqlInserir.setBoolean(i++, obj.getNotasDeCincoEmCincoDecimos());
					sqlInserir.setBoolean(i++, obj.getLimitarQtdeDiasMaxDownload());
					sqlInserir.setInt(i++, obj.getQtdeMaxDiasDownload());
					sqlInserir.setBoolean(i++, obj.getNotasDeCincoEmCincoDecimosApenasMedia());
					sqlInserir.setDouble(i++, obj.getNota11().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota11().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota11());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota11());
					sqlInserir.setString(i++, obj.getFormulaUsoNota11());
					sqlInserir.setBoolean(i++, obj.getNota11MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota11());
					sqlInserir.setDouble(i++, obj.getNota12().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota12().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota12());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota12());
					sqlInserir.setString(i++, obj.getFormulaUsoNota12());
					sqlInserir.setBoolean(i++, obj.getNota12MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota12());
					sqlInserir.setDouble(i++, obj.getNota13().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota13().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota13());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota13());
					sqlInserir.setString(i++, obj.getFormulaUsoNota13());
					sqlInserir.setBoolean(i++, obj.getNota13MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota13());
					sqlInserir.setBoolean(i++, obj.getEnviarMensagemNotaAbaixoMedia());
					sqlInserir.setBoolean(i++, obj.getApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico());
					sqlInserir.setDouble(i++, obj.getPesoMediaNotaMeritoAcademico());
					sqlInserir.setDouble(i++, obj.getPesoMediaFrequenciaMeritoAcademico());
					sqlInserir.setBoolean(i++, obj.getUsarSituacaoAprovadoAproveitamentoTransferenciaGrade());
					sqlInserir.setBoolean(i++, obj.getConsiderarCampoNuloNotaZerada());
					sqlInserir.setBoolean(i++, obj.getApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno());
					sqlInserir.setBoolean(i++, obj.getLiberarPreRequisitoDisciplinaConcomitancia());
					sqlInserir.setBoolean(i++, obj.getApresentarTextoSemNotaCampoNuloHistorico());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota1PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota2PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota3PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota4PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota5PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota6PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota7PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota8PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota9PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota10PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota11PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota12PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota13PorConceito());

					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva1());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva2());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva3());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva4());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva5());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva6());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva7());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva8());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva9());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva10());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva11());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva12());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva13());

					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva1());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva2());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva3());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva4());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva5());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva6());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva7());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva8());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva9());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva10());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva11());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva12());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva13());
					sqlInserir.setBoolean(i++, obj.getUtilizarArredondamentoMediaParaMais());
					sqlInserir.setString(i++, obj.getTipoCalculoCargaHorariaFrequencia().name());
					sqlInserir.setBoolean(i++, obj.getPermiteRegistrarAulaFutura());

					// **********NOTA14*********
					sqlInserir.setDouble(i++, obj.getNota14().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota14().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota14());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota14());
					sqlInserir.setString(i++, obj.getFormulaUsoNota14());
					sqlInserir.setBoolean(i++, obj.getNota14MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota14());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva14());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota14PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva14());
					sqlInserir.setString(i++, obj.getBimestreNota14().toString());
					// *********************
					// **********NOTA15*********
					sqlInserir.setDouble(i++, obj.getNota15().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota15().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota15());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota15());
					sqlInserir.setString(i++, obj.getFormulaUsoNota15());
					sqlInserir.setBoolean(i++, obj.getNota15MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota15());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva15());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota15PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva15());
					sqlInserir.setString(i++, obj.getBimestreNota15().toString());
					// *********************
					// **********NOTA16*********
					sqlInserir.setDouble(i++, obj.getNota16().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota16().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota16());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota16());
					sqlInserir.setString(i++, obj.getFormulaUsoNota16());
					sqlInserir.setBoolean(i++, obj.getNota16MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota16());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva16());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota16PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva16());
					sqlInserir.setString(i++, obj.getBimestreNota16().toString());
					// *********************
					// **********NOTA17*********
					sqlInserir.setDouble(i++, obj.getNota17().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota17().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota17());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota17());
					sqlInserir.setString(i++, obj.getFormulaUsoNota17());
					sqlInserir.setBoolean(i++, obj.getNota17MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota17());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva17());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota17PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva17());
					sqlInserir.setString(i++, obj.getBimestreNota17().toString());
					// *********************
					// **********NOTA18*********
					sqlInserir.setDouble(i++, obj.getNota18().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota18().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota18());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota18());
					sqlInserir.setString(i++, obj.getFormulaUsoNota18());
					sqlInserir.setBoolean(i++, obj.getNota18MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota18());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva18());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota18PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva18());
					sqlInserir.setString(i++, obj.getBimestreNota18().toString());
					// *********************
					// **********NOTA19*********
					sqlInserir.setDouble(i++, obj.getNota19().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota19().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota19());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota19());
					sqlInserir.setString(i++, obj.getFormulaUsoNota19());
					sqlInserir.setBoolean(i++, obj.getNota19MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota19());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva19());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota19PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva19());
					sqlInserir.setString(i++, obj.getBimestreNota19().toString());
					// *********************
					// **********NOTA20*********
					sqlInserir.setDouble(i++, obj.getNota20().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota20().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota20());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota20());
					sqlInserir.setString(i++, obj.getFormulaUsoNota20());
					sqlInserir.setBoolean(i++, obj.getNota20MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota20());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva20());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota20PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva20());
					sqlInserir.setString(i++, obj.getBimestreNota20().toString());
					// *********************
					// **********NOTA21*********
					sqlInserir.setDouble(i++, obj.getNota21().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota21().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota21());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota21());
					sqlInserir.setString(i++, obj.getFormulaUsoNota21());
					sqlInserir.setBoolean(i++, obj.getNota21MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota21());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva21());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota21PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva21());
					sqlInserir.setString(i++, obj.getBimestreNota21().toString());
					// *********************
					// **********NOTA22*********
					sqlInserir.setDouble(i++, obj.getNota22().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota22().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota22());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota22());
					sqlInserir.setString(i++, obj.getFormulaUsoNota22());
					sqlInserir.setBoolean(i++, obj.getNota22MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota22());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva22());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota22PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva22());
					sqlInserir.setString(i++, obj.getBimestreNota22().toString());
					// *********************
					// **********NOTA23*********
					sqlInserir.setDouble(i++, obj.getNota23().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota23().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota23());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota23());
					sqlInserir.setString(i++, obj.getFormulaUsoNota23());
					sqlInserir.setBoolean(i++, obj.getNota23MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota23());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva23());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota23PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva23());
					sqlInserir.setString(i++, obj.getBimestreNota23().toString());
					// *********************
					// **********NOTA24*********
					sqlInserir.setDouble(i++, obj.getNota24().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota24().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota24());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota24());
					sqlInserir.setString(i++, obj.getFormulaUsoNota24());
					sqlInserir.setBoolean(i++, obj.getNota24MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota24());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva24());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota24PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva24());
					sqlInserir.setString(i++, obj.getBimestreNota24().toString());
					// *********************
					// **********NOTA25*********
					sqlInserir.setDouble(i++, obj.getNota25().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota25().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota25());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota25());
					sqlInserir.setString(i++, obj.getFormulaUsoNota25());
					sqlInserir.setBoolean(i++, obj.getNota25MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota25());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva25());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota25PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva25());
					sqlInserir.setString(i++, obj.getBimestreNota25().toString());
					// *********************
					// **********NOTA26*********
					sqlInserir.setDouble(i++, obj.getNota26().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota26().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota26());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota26());
					sqlInserir.setString(i++, obj.getFormulaUsoNota26());
					sqlInserir.setBoolean(i++, obj.getNota26MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota26());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva26());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota26PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva26());
					sqlInserir.setString(i++, obj.getBimestreNota26().toString());
					// *********************
					// **********NOTA27*********
					sqlInserir.setDouble(i++, obj.getNota27().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota27().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota27());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota27());
					sqlInserir.setString(i++, obj.getFormulaUsoNota27());
					sqlInserir.setBoolean(i++, obj.getNota27MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota27());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva27());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota27PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva27());
					sqlInserir.setString(i++, obj.getBimestreNota27().toString());
					// *********************
					// **********NOTA28*********
					sqlInserir.setDouble(i++, obj.getNota28().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota28().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota28());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota28());
					sqlInserir.setString(i++, obj.getFormulaUsoNota28());
					sqlInserir.setBoolean(i++, obj.getNota28MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota28());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva28());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota28PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva28());
					sqlInserir.setString(i++, obj.getBimestreNota28().toString());
					// *********************
					// **********NOTA29*********
					sqlInserir.setDouble(i++, obj.getNota29().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota29().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota29());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota29());
					sqlInserir.setString(i++, obj.getFormulaUsoNota29());
					sqlInserir.setBoolean(i++, obj.getNota29MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota29());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva29());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota29PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva29());
					sqlInserir.setString(i++, obj.getBimestreNota29().toString());
					// *********************
					// **********NOTA30*********
					sqlInserir.setDouble(i++, obj.getNota30().doubleValue());
					sqlInserir.setBoolean(i++, obj.isUtilizarNota30().booleanValue());
					sqlInserir.setString(i++, obj.getTituloNota30());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota30());
					sqlInserir.setString(i++, obj.getFormulaUsoNota30());
					sqlInserir.setBoolean(i++, obj.getNota30MediaFinal().booleanValue());
					sqlInserir.setBoolean(i++, obj.getApresentarNota30());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva30());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota30PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva30());
					sqlInserir.setString(i++, obj.getBimestreNota30().toString());
					// *********************
					// **********BIMESTRAIS 1 A 13 *********
					sqlInserir.setString(i++, obj.getBimestreNota1().toString());
					sqlInserir.setString(i++, obj.getBimestreNota2().toString());
					sqlInserir.setString(i++, obj.getBimestreNota3().toString());
					sqlInserir.setString(i++, obj.getBimestreNota4().toString());
					sqlInserir.setString(i++, obj.getBimestreNota5().toString());
					sqlInserir.setString(i++, obj.getBimestreNota6().toString());
					sqlInserir.setString(i++, obj.getBimestreNota7().toString());
					sqlInserir.setString(i++, obj.getBimestreNota8().toString());
					sqlInserir.setString(i++, obj.getBimestreNota9().toString());
					sqlInserir.setString(i++, obj.getBimestreNota10().toString());
					sqlInserir.setString(i++, obj.getBimestreNota11().toString());
					sqlInserir.setString(i++, obj.getBimestreNota12().toString());
					sqlInserir.setString(i++, obj.getBimestreNota13().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar1().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar2().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar3().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar4().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar5().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar6().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar7().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar8().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar9().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar10().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar11().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar12().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar13().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar14().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar15().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar16().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar17().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar18().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar19().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar20().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar21().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar22().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar23().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar24().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar25().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar26().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar27().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar28().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar29().toString());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar30().toString());

					sqlInserir.setBoolean(i++, obj.getControlarAvancoPeriodoPorCreditoOuCH());
					sqlInserir.setString(i++, obj.getTipoControleAvancoPeriodoPorCreditoOuCH());
					sqlInserir.setInt(i++, obj.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo());
					sqlInserir.setInt(i++, obj.getPercCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo());
					sqlInserir.setBoolean(i++, obj.getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH());
					sqlInserir.setString(i++, obj.getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH());
					sqlInserir.setBoolean(i++, obj.getAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos());
					sqlInserir.setBoolean(i++, obj.getPermitirInclusaoDiscipDependenciaComChoqueHorario());
					sqlInserir.setInt(i++, obj.getQtdPermitirInclusaoDiscipDependenciaComChoqueHorario());
					sqlInserir.setBoolean(i++, obj.getPermitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta());

					sqlInserir.setDouble(i++, obj.getFaixaNota1Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota1Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota2Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota2Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota3Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota3Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota4Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota4Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota5Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota5Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota6Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota6Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota7Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota7Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota8Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota8Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota9Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota9Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota10Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota10Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota11Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota11Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota12Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota12Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota13Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota13Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota14Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota14Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota15Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota15Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota16Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota16Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota17Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota17Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota18Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota18Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota19Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota19Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota20Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota20Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota21Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota21Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota22Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota22Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota23Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota23Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota24Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota24Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota25Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota25Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota26Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota26Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota27Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota27Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota28Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota28Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota29Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota29Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota30Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota30Menor());
					sqlInserir.setInt(i++, obj.getQuantidadeCasasDecimaisPermitirAposVirgula());
					sqlInserir.setBoolean(i++, obj.getApresentarSiglaConcessaoCredito());
					sqlInserir.setInt(i++, obj.getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
					sqlInserir.setString(i++, obj.getTipoApresentarFrequenciaVisaoAluno());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota1());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota2());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota3());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota4());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota5());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota6());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota7());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota8());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota9());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota10());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota11());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota12());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota13());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota14());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota15());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota16());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota17());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota18());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota19());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota20());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota21());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota22());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota23());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota24());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota25());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota26());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota27());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota28());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota29());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota30());
					sqlInserir.setBoolean(i++, obj.getApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico());
					sqlInserir.setBoolean(i++, obj.getObrigaInformarFormaIngressoMatricula());
					sqlInserir.setBoolean(i++, obj.getObrigaInformarOrigemFormaIngressoMatricula());
					sqlInserir.setBoolean(i++, obj.getBloquearRegistroAulaAnteriorDataMatricula());
					sqlInserir.setBoolean(i++, obj.getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao());
					sqlInserir.setBoolean(i++, obj.getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal());
					sqlInserir.setBoolean(i++, obj.getCalcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes());
					sqlInserir.setBoolean(i++, obj.getHabilitarControleInclusaoDisciplinaPeriodoFuturo());
					sqlInserir.setInt(i++, obj.getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina());
					sqlInserir.setBoolean(i++, obj.getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular());
					sqlInserir.setBoolean(i++, obj.getBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular());
					sqlInserir.setBoolean(i++, obj.getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia());
					sqlInserir.setInt(i++, obj.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia());
					sqlInserir.setBoolean(i++, obj.getRemoverAutomaticamenteDisciplinaSemVagaRenovacaoOnline());
					sqlInserir.setBoolean(i++, obj.getHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao());
					sqlInserir.setBoolean(i++, obj.getDistribuirTurmaPraticaTeoricaComAulaProgramada());
					sqlInserir.setBoolean(i++, obj.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario());
					sqlInserir.setBoolean(i++, obj.getConsiderarRegularAlunoDependenciaOptativa());
					sqlInserir.setBoolean(i++, obj.getUtilizarApoioEADParaDisciplinasModalidadePresencial());
					sqlInserir.setBoolean(i++, obj.getConsiderarPortadoDiplomaTransEntradaAlunoIrregular());
					sqlInserir.setBoolean(i++, obj.getMatricularApenasDisciplinaAulaProgramada());
					
					sqlInserir.setDouble(i++, obj.getNota31());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota31());
					sqlInserir.setString(i++, obj.getTituloNota31());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota31());
					sqlInserir.setString(i++, obj.getFormulaUsoNota31());
					sqlInserir.setBoolean(i++, obj.getNota31MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota31());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva31());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota31PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva31());
					sqlInserir.setString(i++, obj.getBimestreNota31().toString());
					
					sqlInserir.setDouble(i++, obj.getNota32());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota32());
					sqlInserir.setString(i++, obj.getTituloNota32());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota32());
					sqlInserir.setString(i++, obj.getFormulaUsoNota32());
					sqlInserir.setBoolean(i++, obj.getNota32MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota32());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva32());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota32PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva32());
					sqlInserir.setString(i++, obj.getBimestreNota32().toString());
					
					sqlInserir.setDouble(i++, obj.getNota33());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota33());
					sqlInserir.setString(i++, obj.getTituloNota33());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota33());
					sqlInserir.setString(i++, obj.getFormulaUsoNota33());
					sqlInserir.setBoolean(i++, obj.getNota33MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota33());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva33());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota33PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva33());
					sqlInserir.setString(i++, obj.getBimestreNota33().toString());
					
					sqlInserir.setDouble(i++, obj.getNota34());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota34());
					sqlInserir.setString(i++, obj.getTituloNota34());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota34());
					sqlInserir.setString(i++, obj.getFormulaUsoNota34());
					sqlInserir.setBoolean(i++, obj.getNota34MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota34());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva34());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota34PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva34());
					sqlInserir.setString(i++, obj.getBimestreNota34().toString());
					
					sqlInserir.setDouble(i++, obj.getNota35());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota35());
					sqlInserir.setString(i++, obj.getTituloNota35());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota35());
					sqlInserir.setString(i++, obj.getFormulaUsoNota35());
					sqlInserir.setBoolean(i++, obj.getNota35MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota35());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva35());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota35PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva35());
					sqlInserir.setString(i++, obj.getBimestreNota35().toString());					
					
					sqlInserir.setDouble(i++, obj.getNota36());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota36());
					sqlInserir.setString(i++, obj.getTituloNota36());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota36());
					sqlInserir.setString(i++, obj.getFormulaUsoNota36());
					sqlInserir.setBoolean(i++, obj.getNota36MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota36());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva36());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota36PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva36());
					sqlInserir.setString(i++, obj.getBimestreNota36().toString());
					
					sqlInserir.setDouble(i++, obj.getNota37());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota37());
					sqlInserir.setString(i++, obj.getTituloNota37());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota37());
					sqlInserir.setString(i++, obj.getFormulaUsoNota37());
					sqlInserir.setBoolean(i++, obj.getNota37MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota37());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva37());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota37PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva37());
					sqlInserir.setString(i++, obj.getBimestreNota37().toString());
					
					sqlInserir.setDouble(i++, obj.getNota38());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota38());
					sqlInserir.setString(i++, obj.getTituloNota38());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota38());
					sqlInserir.setString(i++, obj.getFormulaUsoNota38());
					sqlInserir.setBoolean(i++, obj.getNota38MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota38());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva38());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota38PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva38());
					sqlInserir.setString(i++, obj.getBimestreNota38().toString());
					
					sqlInserir.setDouble(i++, obj.getNota39());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota39());
					sqlInserir.setString(i++, obj.getTituloNota39());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota39());
					sqlInserir.setString(i++, obj.getFormulaUsoNota39());
					sqlInserir.setBoolean(i++, obj.getNota39MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota39());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva39());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota39PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva39());
					sqlInserir.setString(i++, obj.getBimestreNota39().toString());
					
					sqlInserir.setDouble(i++, obj.getNota40());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota40());
					sqlInserir.setString(i++, obj.getTituloNota40());
					sqlInserir.setString(i++, obj.getFormulaCalculoNota40());
					sqlInserir.setString(i++, obj.getFormulaUsoNota40());
					sqlInserir.setBoolean(i++, obj.getNota40MediaFinal());
					sqlInserir.setBoolean(i++, obj.getApresentarNota40());
					sqlInserir.setString(i++, obj.getPoliticaSubstitutiva40());
					sqlInserir.setBoolean(i++, obj.getUtilizarNota40PorConceito());
					sqlInserir.setBoolean(i++, obj.getUtilizarComoSubstitutiva40());
					sqlInserir.setString(i++, obj.getBimestreNota40().toString());
					
					sqlInserir.setString(i++, obj.getTituloNotaApresentar31());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar32());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar33());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar34());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar35());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar36());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar37());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar38());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar39());
					sqlInserir.setString(i++, obj.getTituloNotaApresentar40());
					
					sqlInserir.setDouble(i++, obj.getFaixaNota31Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota31Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota32Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota32Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota33Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota33Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota34Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota34Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota35Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota35Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota36Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota36Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota37Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota37Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota38Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota38Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota39Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota39Menor());
					sqlInserir.setDouble(i++, obj.getFaixaNota40Maior());
					sqlInserir.setDouble(i++, obj.getFaixaNota40Menor());
					
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota31());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota32());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota32());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota34());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota35());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota36());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota37());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota38());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota39());
					sqlInserir.setString(i++, obj.getRegraArredondamentoNota40());
					
					sqlInserir.setInt(i++, obj.getQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula());
					sqlInserir.setBoolean(i++, obj.getIncluirAutomaticamenteDisciplinaGrupoOptativa());
					sqlInserir.setBoolean(i++, obj.getPermitirAlunoRegularIncluirDisciplinaGrupoOptativa());
					sqlInserir.setBoolean(i++, obj.getBloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa());
					sqlInserir.setBoolean(i++, obj.getConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores());
					sqlInserir.setBoolean(i++, obj.getCriarDigitoMascaraMatricula());
					sqlInserir.setString(i++, obj.getFormulaCriarDigitoMascaraMatricula());
					sqlInserir.setBoolean(i++, obj.getReprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha());
					sqlInserir.setBoolean(i++, obj.getOcultarFrequenciaDisciplinaComposta());
					sqlInserir.setString(i++, obj.getTipoUsoConfiguracaoAcademico().name());
					sqlInserir.setString(i++, obj.getRegraCalculoDisciplinaComposta().name());
					sqlInserir.setBoolean(i++, obj.getOcultarMediaFinalDisciplinaCasoReprovado());
					sqlInserir.setBoolean(i++, obj.getObrigarAceiteAlunoTermoParaEditarRenovacao().booleanValue());
					sqlInserir.setBoolean(i++, obj.getValidarChoqueHorarioOutraMatriculaAluno());
					sqlInserir.setBoolean(i++, obj.getValidarDadosEnadeCensoMatricularAluno());
					sqlInserir.setBoolean(i++, obj.getUsarFormulaCalculoFrequencia());
					sqlInserir.setString(i++, obj.getFormulaCalculoFrequencia());
					sqlInserir.setString(i++, obj.getFormulaCoeficienteRendimento());
					sqlInserir.setInt(i++, obj.getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula());
					sqlInserir.setString(i++, obj.getMascaraNumeroProcessoExpedicaoDiploma());
					sqlInserir.setInt(i++, obj.getCasasDecimaisCoeficienteRendimento());
					sqlInserir.setBoolean(i++, obj.getPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual());
					sqlInserir.setBoolean(i++, obj.getRegistrarComoFaltaAulasRealizadasAposDataMatricula());
					sqlInserir.setBoolean(i++, obj.isAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo());
					sqlInserir.setBoolean(i++, obj.isHabilitarDistribuicaoDisciplinaDependenciaAutomatica());
					sqlInserir.setBoolean(i++, obj.isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares());
					sqlInserir.setString(i++, obj.getMascaraNumeroRegistroDiploma());
					sqlInserir.setBoolean(i++, obj.getPermitirAproveitamentoDisciplinasOptativas());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().incluirConfiguracaoAcademicoNotaConceito(obj);
			getFacadeFactory().getConfiguracaoAcademicoNotaFacade().incluirConfiguracaoAcademicoNotaVOs(obj);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			////System.out.println(e.getMessage());
			throw e;
		}
	}

	public void incluir(List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs, UsuarioVO usuario) throws Exception {
		Iterator<ConfiguracaoAcademicoVO> i = configuracaoAcademicoVOs.iterator();
		while (i.hasNext()) {
			incluir(i.next(), usuario);
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAcademicoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception {
		try {			
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ConfiguracaoAcademico set nome=?, formulaCalculoMediaFinal=?, nota1=?, utilizarNota1=?, tituloNota1=?, " // 1-5
					+ "formulaCalculoNota1=?, formulaUsoNota1=?, nota2=?, utilizarNota2=?, tituloNota2=?, formulaCalculoNota2=?, formulaUsoNota2=?, " // 6
					+ "nota3=?, utilizarNota3=?, tituloNota3=?, formulaCalculoNota3=?, formulaUsoNota3=?, nota4=?, utilizarNota4=?, tituloNota4=?, " // 13
					+ "formulaCalculoNota4=?, formulaUsoNota4=?, nota5=?, utilizarNota5=?, tituloNota5=?, formulaCalculoNota5=?, formulaUsoNota5=?, nota6=?, " // 21
					+ "utilizarNota6=?, tituloNota6=?, formulaCalculoNota6=?, formulaUsoNota6=?, nota7=?, utilizarNota7=?, tituloNota7=?, formulaCalculoNota7=?, " // 29
					+ "formulaUsoNota7=?, nota8=?, utilizarNota8=?, tituloNota8=?, formulaCalculoNota8=?, formulaUsoNota8=?, nota9=?, utilizarNota9=?, tituloNota9=?, " // 37
					+ "formulaCalculoNota9=?, formulaUsoNota9=?, nota10=?, utilizarNota10=?, tituloNota10=?, formulaCalculoNota10=?, formulaUsoNota10=?, " // 46
					+ "percentualFrequenciaAprovacao=?, mascaraPadraoGeracaoMatricula=?, nota1MediaFinal=?, nota2MediaFinal=?, nota3MediaFinal=?, nota4MediaFinal=?, " // 53
					+ "nota5MediaFinal=?, nota6MediaFinal=?, nota7MediaFinal=?, nota8MediaFinal=?, nota9MediaFinal=?, nota10MediaFinal=?, configuracoes=?, " // 59
					+ "numeroDisciplinaConsiderarReprovadoPeriodoLetivo=?, reprovadoMatricularDisciplinaPeriodoLetivo=?, permiteEvoluirPeriodoLetivoCasoReprovado=?, " // 66
					+ "diasmaximoreativacaomatricula=?, renovacaomatriculasequencial=?, cursardisciplinaeprerequisito=?, apresentarNota1=?, apresentarNota2=?, " // 69
					+ "apresentarNota3=?, apresentarNota4=?, apresentarNota5=?, apresentarNota6=?, apresentarNota7=?, apresentarNota8=?, apresentarNota9=?, " // 74
					+ "apresentarNota10=?, notasDeCincoEmCincoDecimos=?, limitarDiasDownload=?, qtdeDiasLimiteDownload=?, notasDeCincoEmCincoDecimosApenasMedia=?, " // 81
					+ "nota11=?, utilizarNota11=?, tituloNota11=?, formulaCalculoNota11=?, formulaUsoNota11=?, nota11MediaFinal=?, apresentarNota11=?, " // 86
					+ "nota12=?, utilizarNota12=?, tituloNota12=?, formulaCalculoNota12=?, formulaUsoNota12=?, nota12MediaFinal=?, apresentarNota12=?, " // 93
					+ "nota13=?, utilizarNota13=?, tituloNota13=?, formulaCalculoNota13=?, formulaUsoNota13=?, nota13MediaFinal=?, apresentarNota13=?, enviarMensagemNotaAbaixoMedia=?, apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico=?, " // 100
					+ "pesoMediaNotaMeritoAcademico=?, pesoMediaFrequenciaMeritoAcademico=?, usarSituacaoAprovadoAproveitamentoTransferenciaGrade=?, considerarCampoNuloNotaZerada=?, apresentardisciplinasemaulaprogramadaminhasnotasvisaoaluno=?, " + "liberarPreRequisitoDisciplinaConcomitancia=?, apresentarTextoSemNotaCampoNuloHistorico=?, " + "utilizarNota1PorConceito=?, utilizarNota2PorConceito=?, utilizarNota3PorConceito=?, utilizarNota4PorConceito=?, utilizarNota5PorConceito=?, " + "utilizarNota6PorConceito=?, utilizarNota7PorConceito=?, utilizarNota8PorConceito=?, utilizarNota9PorConceito=?, utilizarNota10PorConceito=?, " + "utilizarNota11PorConceito=?, utilizarNota12PorConceito=?, utilizarNota13PorConceito=?, " + "utilizarComoSubstitutiva1=?, utilizarComoSubstitutiva2=?, utilizarComoSubstitutiva3=?, utilizarComoSubstitutiva4=?, "
					+ "utilizarComoSubstitutiva5=?, utilizarComoSubstitutiva6=?, utilizarComoSubstitutiva7=?, utilizarComoSubstitutiva8=?, "
					+ "utilizarComoSubstitutiva9=?, utilizarComoSubstitutiva10=?, utilizarComoSubstitutiva11=?, utilizarComoSubstitutiva12=?, " + "utilizarComoSubstitutiva13=?, politicaSubstitutiva1=?, politicaSubstitutiva2=?, politicaSubstitutiva3=?, " + "politicaSubstitutiva4=?, politicaSubstitutiva5=?, politicaSubstitutiva6=?, politicaSubstitutiva7=?, politicaSubstitutiva8=?, " + "politicaSubstitutiva9=?, politicaSubstitutiva10=?, politicaSubstitutiva11=?, politicaSubstitutiva12=?, politicaSubstitutiva13=?, utilizarArredondamentoMediaParaMais = ?, " + "tipoCalculoCargaHorariaFrequencia=?, permiteRegistrarAulaFutura=?, "
					+ "nota14=?, utilizarNota14=?, tituloNota14=?, formulaCalculoNota14=?, formulaUsoNota14=?, nota14MediaFinal=?, apresentarNota14=?, politicaSubstitutiva14=?, utilizarNota14PorConceito=?, utilizarComoSubstitutiva14=?, bimestreNota14=?, "
					+ "nota15=?, utilizarNota15=?, tituloNota15=?, formulaCalculoNota15=?, formulaUsoNota15=?, nota15MediaFinal=?, apresentarNota15=?, politicaSubstitutiva15=?, utilizarNota15PorConceito=?, utilizarComoSubstitutiva15=?, bimestreNota15=?, " + "nota16=?, utilizarNota16=?, tituloNota16=?, formulaCalculoNota16=?, formulaUsoNota16=?, nota16MediaFinal=?, apresentarNota16=?, politicaSubstitutiva16=?, utilizarNota16PorConceito=?, utilizarComoSubstitutiva16=?, bimestreNota16=?, " + "nota17=?, utilizarNota17=?, tituloNota17=?, formulaCalculoNota17=?, formulaUsoNota17=?, nota17MediaFinal=?, apresentarNota17=?, politicaSubstitutiva17=?, utilizarNota17PorConceito=?, utilizarComoSubstitutiva17=?, bimestreNota17=?, "
					+ "nota18=?, utilizarNota18=?, tituloNota18=?, formulaCalculoNota18=?, formulaUsoNota18=?, nota18MediaFinal=?, apresentarNota18=?, politicaSubstitutiva18=?, utilizarNota18PorConceito=?, utilizarComoSubstitutiva18=?, bimestreNota18=?, "
					+ "nota19=?, utilizarNota19=?, tituloNota19=?, formulaCalculoNota19=?, formulaUsoNota19=?, nota19MediaFinal=?, apresentarNota19=?, politicaSubstitutiva19=?, utilizarNota19PorConceito=?, utilizarComoSubstitutiva19=?, bimestreNota19=?, " + "nota20=?, utilizarNota20=?, tituloNota20=?, formulaCalculoNota20=?, formulaUsoNota20=?, nota20MediaFinal=?, apresentarNota20=?, politicaSubstitutiva20=?, utilizarNota20PorConceito=?, utilizarComoSubstitutiva20=?, bimestreNota20=?, " + "nota21=?, utilizarNota21=?, tituloNota21=?, formulaCalculoNota21=?, formulaUsoNota21=?, nota21MediaFinal=?, apresentarNota21=?, politicaSubstitutiva21=?, utilizarNota21PorConceito=?, utilizarComoSubstitutiva21=?, bimestreNota21=?, "
					+ "nota22=?, utilizarNota22=?, tituloNota22=?, formulaCalculoNota22=?, formulaUsoNota22=?, nota22MediaFinal=?, apresentarNota22=?, politicaSubstitutiva22=?, utilizarNota22PorConceito=?, utilizarComoSubstitutiva22=?, bimestreNota22=?, "
					+ "nota23=?, utilizarNota23=?, tituloNota23=?, formulaCalculoNota23=?, formulaUsoNota23=?, nota23MediaFinal=?, apresentarNota23=?, politicaSubstitutiva23=?, utilizarNota23PorConceito=?, utilizarComoSubstitutiva23=?, bimestreNota23=?, " + "nota24=?, utilizarNota24=?, tituloNota24=?, formulaCalculoNota24=?, formulaUsoNota24=?, nota24MediaFinal=?, apresentarNota24=?, politicaSubstitutiva24=?, utilizarNota24PorConceito=?, utilizarComoSubstitutiva24=?, bimestreNota24=?, " + "nota25=?, utilizarNota25=?, tituloNota25=?, formulaCalculoNota25=?, formulaUsoNota25=?, nota25MediaFinal=?, apresentarNota25=?, politicaSubstitutiva25=?, utilizarNota25PorConceito=?, utilizarComoSubstitutiva25=?, bimestreNota25=?, "
					+ "nota26=?, utilizarNota26=?, tituloNota26=?, formulaCalculoNota26=?, formulaUsoNota26=?, nota26MediaFinal=?, apresentarNota26=?, politicaSubstitutiva26=?, utilizarNota26PorConceito=?, utilizarComoSubstitutiva26=?, bimestreNota26=?, "
					+ "nota27=?, utilizarNota27=?, tituloNota27=?, formulaCalculoNota27=?, formulaUsoNota27=?, nota27MediaFinal=?, apresentarNota27=?, politicaSubstitutiva27=?, utilizarNota27PorConceito=?, utilizarComoSubstitutiva27=?, bimestreNota27=?, " + "nota28=?, utilizarNota28=?, tituloNota28=?, formulaCalculoNota28=?, formulaUsoNota28=?, nota28MediaFinal=?, apresentarNota28=?, politicaSubstitutiva28=?, utilizarNota28PorConceito=?, utilizarComoSubstitutiva28=?, bimestreNota28=?, " + "nota29=?, utilizarNota29=?, tituloNota29=?, formulaCalculoNota29=?, formulaUsoNota29=?, nota29MediaFinal=?, apresentarNota29=?, politicaSubstitutiva29=?, utilizarNota29PorConceito=?, utilizarComoSubstitutiva29=?, bimestreNota29=?, "
					+ "nota30=?, utilizarNota30=?, tituloNota30=?, formulaCalculoNota30=?, formulaUsoNota30=?, nota30MediaFinal=?, apresentarNota30=?, politicaSubstitutiva30=?, utilizarNota30PorConceito=?, utilizarComoSubstitutiva30=?, bimestreNota30=?, "
					+ "bimestreNota1=?, bimestreNota2=?, bimestreNota3=?, bimestreNota4=?, bimestreNota5=?, bimestreNota6=?, bimestreNota7=?, bimestreNota8=?, bimestreNota9=?, bimestreNota10=?, bimestreNota11=?, bimestreNota12=?, bimestreNota13=?, " + "tituloNotaApresentar1=?, tituloNotaApresentar2=?, tituloNotaApresentar3=?, tituloNotaApresentar4=?, tituloNotaApresentar5=?, tituloNotaApresentar6=?, tituloNotaApresentar7=?, tituloNotaApresentar8=?, tituloNotaApresentar9=?, tituloNotaApresentar10=?, "
					+ "tituloNotaApresentar11=?, tituloNotaApresentar12=?, tituloNotaApresentar13=?, tituloNotaApresentar14=?, tituloNotaApresentar15=?, tituloNotaApresentar16=?, tituloNotaApresentar17=?, tituloNotaApresentar18=?, tituloNotaApresentar19=?, tituloNotaApresentar20=?, "
					+ "tituloNotaApresentar21=?, tituloNotaApresentar22=?, tituloNotaApresentar23=?, tituloNotaApresentar24=?, tituloNotaApresentar25=?, tituloNotaApresentar26=?, tituloNotaApresentar27=?, tituloNotaApresentar28=?, tituloNotaApresentar29=?, tituloNotaApresentar30=?, " + "controlarAvancoPeriodoPorCreditoOuCH=?, tipoControleAvancoPeriodoPorCreditoOuCH=?, percCumprirPeriodoAnteriorRenovarProximoPerLetivo=?, percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo=?, " + "controlarInclusaoDisciplinaPorNrMaxCreditoOuCH=?, tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH=?, acumularCreditosOuCHPeriodosAnterioresNaoCumpridos=?, "
					+ "permitirInclusaoDiscipDependenciaComChoqueHorario=?, qtdPermitirInclusaoDiscipDependenciaComChoqueHorario=?, permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta=?, "
					+ "faixaNota1Maior=?, faixaNota1Menor=?, faixaNota2Maior=?, faixaNota2Menor=?, faixaNota3Maior=?, faixaNota3Menor=?, faixaNota4Maior=?, faixaNota4Menor=?, faixaNota5Maior=?, faixaNota5Menor=?, faixaNota6Maior=?, faixaNota6Menor=?, faixaNota7Maior=?, faixaNota7Menor=?, " + "faixaNota8Maior=?, faixaNota8Menor=?, faixaNota9Maior=?, faixaNota9Menor=?, faixaNota10Maior=?, faixaNota10Menor=?, faixaNota11Maior=?, faixaNota11Menor=?, faixaNota12Maior=?, faixaNota12Menor=?, faixaNota13Maior=?, faixaNota13Menor=?, faixaNota14Maior=?, faixaNota14Menor=?, "
					+ "faixaNota15Maior=?, faixaNota15Menor=?, faixaNota16Maior=?, faixaNota16Menor=?, faixaNota17Maior=?, faixaNota17Menor=?, faixaNota18Maior=?, faixaNota18Menor=?, faixaNota19Maior=?, faixaNota19Menor=?, faixaNota20Maior=?, faixaNota20Menor=?, faixaNota21Maior=?, faixaNota21Menor=?, "
					+ "faixaNota22Maior=?, faixaNota22Menor=?, faixaNota23Maior=?, faixaNota23Menor=?, faixaNota24Maior=?, faixaNota24Menor=?, faixaNota25Maior=?, faixaNota25Menor=?, faixaNota26Maior=?, faixaNota26Menor=?, faixaNota27Maior=?, faixaNota27Menor=?, faixaNota28Maior=?, faixaNota28Menor=?, " + "faixaNota29Maior=?, faixaNota29Menor=?, faixaNota30Maior=?, faixaNota30Menor=?, quantidadeCasasDecimaisPermitirAposVirgula=?, " + "apresentarSiglaConcessaoCredito=?, percMinimoCargaHorariaDisciplinaParaAproveitamento=?, tipoApresentarFrequenciaVisaoAluno=?, "
					+ "regraArredondamentoNota1=?, regraArredondamentoNota2=?, regraArredondamentoNota3=?, regraArredondamentoNota4=?, regraArredondamentoNota5=?, regraArredondamentoNota6=?, regraArredondamentoNota7=?, regraArredondamentoNota8=?, regraArredondamentoNota9=?, regraArredondamentoNota10=?, "
					+ "regraArredondamentoNota11=?, regraArredondamentoNota12=?, regraArredondamentoNota13=?, regraArredondamentoNota14=?, regraArredondamentoNota15=?, regraArredondamentoNota16=?, regraArredondamentoNota17=?, regraArredondamentoNota18=?, regraArredondamentoNota19=?, regraArredondamentoNota20=?, " + "regraArredondamentoNota21=?, regraArredondamentoNota22=?, regraArredondamentoNota23=?, regraArredondamentoNota24=?, regraArredondamentoNota25=?, regraArredondamentoNota26=?, regraArredondamentoNota27=?, regraArredondamentoNota28=?, regraArredondamentoNota29=?, regraArredondamentoNota30=?, "
					+ "apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico=?, obrigaInformarFormaIngressoMatricula=?, obrigaInformarOrigemFormaIngressoMatricula=?, bloquearRegistroAulaAnteriorDataMatricula=?, "
					+ "ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao=?, situacaoDiscQueFazParteComposicaoControladaDiscPrincipal=?, calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes=?, "
					+ " habilitarControleInclusaoDisciplinaPeriodoFuturo = ?, numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina = ?, bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = ?, bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = ?, "
					+ " habilitarControleInclusaoObrigatoriaDisciplinaDependencia = ?, porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia = ?, removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline = ?, habilitarDistribuicaoTurmaPraticaTeoricaRenovacao = ?, distribuirTurmaPraticaTeoricaComAulaProgramada = ?, removerDisciplinaTurmaPraticaTeoricaComChoqueHorario = ?, considerarRegularAlunoDependenciaOptativa = ?, utilizarApoioEADParaDisciplinasModalidadePresencial=?, considerarPortadoDiplomaTransEntradaAlunoIrregular =?, matricularApenasDisciplinaAulaProgramada = ?,  "
					+ "nota31=?, utilizarNota31=?, tituloNota31=?, formulaCalculoNota31=?, formulaUsoNota31=?, nota31MediaFinal=?, apresentarNota31=?, politicaSubstitutiva31=?, utilizarNota31PorConceito=?, utilizarComoSubstitutiva31=?, bimestreNota31=?, "
					+ "nota32=?, utilizarNota32=?, tituloNota32=?, formulaCalculoNota32=?, formulaUsoNota32=?, nota32MediaFinal=?, apresentarNota32=?, politicaSubstitutiva32=?, utilizarNota32PorConceito=?, utilizarComoSubstitutiva32=?, bimestreNota32=?, "
					+ "nota33=?, utilizarNota33=?, tituloNota33=?, formulaCalculoNota33=?, formulaUsoNota33=?, nota33MediaFinal=?, apresentarNota33=?, politicaSubstitutiva33=?, utilizarNota33PorConceito=?, utilizarComoSubstitutiva33=?, bimestreNota33=?, "
					+ "nota34=?, utilizarNota34=?, tituloNota34=?, formulaCalculoNota34=?, formulaUsoNota34=?, nota34MediaFinal=?, apresentarNota34=?, politicaSubstitutiva34=?, utilizarNota34PorConceito=?, utilizarComoSubstitutiva34=?, bimestreNota34=?, "
					+ "nota35=?, utilizarNota35=?, tituloNota35=?, formulaCalculoNota35=?, formulaUsoNota35=?, nota35MediaFinal=?, apresentarNota35=?, politicaSubstitutiva35=?, utilizarNota35PorConceito=?, utilizarComoSubstitutiva35=?, bimestreNota35=?, "
					+ "nota36=?, utilizarNota36=?, tituloNota36=?, formulaCalculoNota36=?, formulaUsoNota36=?, nota36MediaFinal=?, apresentarNota36=?, politicaSubstitutiva36=?, utilizarNota36PorConceito=?, utilizarComoSubstitutiva36=?, bimestreNota36=?, "
					+ "nota37=?, utilizarNota37=?, tituloNota37=?, formulaCalculoNota37=?, formulaUsoNota37=?, nota37MediaFinal=?, apresentarNota37=?, politicaSubstitutiva37=?, utilizarNota37PorConceito=?, utilizarComoSubstitutiva37=?, bimestreNota37=?, "
					+ "nota38=?, utilizarNota38=?, tituloNota38=?, formulaCalculoNota38=?, formulaUsoNota38=?, nota38MediaFinal=?, apresentarNota38=?, politicaSubstitutiva38=?, utilizarNota38PorConceito=?, utilizarComoSubstitutiva38=?, bimestreNota38=?, "
					+ "nota39=?, utilizarNota39=?, tituloNota39=?, formulaCalculoNota39=?, formulaUsoNota39=?, nota39MediaFinal=?, apresentarNota39=?, politicaSubstitutiva39=?, utilizarNota39PorConceito=?, utilizarComoSubstitutiva39=?, bimestreNota39=?, "
					+ "nota40=?, utilizarNota40=?, tituloNota40=?, formulaCalculoNota40=?, formulaUsoNota40=?, nota40MediaFinal=?, apresentarNota40=?, politicaSubstitutiva40=?, utilizarNota40PorConceito=?, utilizarComoSubstitutiva40=?, bimestreNota40=?, "
					+ "tituloNotaApresentar31=?, tituloNotaApresentar32=?, tituloNotaApresentar33=?, tituloNotaApresentar34=?, tituloNotaApresentar35=?, tituloNotaApresentar36=?, tituloNotaApresentar37=?, tituloNotaApresentar38=?, tituloNotaApresentar39=?, tituloNotaApresentar40=?, "
					+ "faixaNota31Maior=?, faixaNota31Menor=?, faixaNota32Maior=?, faixaNota32Menor=?, faixaNota33Maior=?, faixaNota33Menor=?, faixaNota34Maior=?, faixaNota34Menor=?, faixaNota35Maior=?, faixaNota35Menor=?, faixaNota36Maior=?, faixaNota36Menor=?, faixaNota37Maior=?, faixaNota37Menor=?, faixaNota38Maior=?, faixaNota38Menor=?, faixaNota39Maior=?, faixaNota39Menor=?, faixaNota40Maior=?, faixaNota40Menor=?, "
					+ "regraArredondamentoNota31=?, regraArredondamentoNota32=?, regraArredondamentoNota33=?, regraArredondamentoNota34=?, regraArredondamentoNota35=?, regraArredondamentoNota36=?, regraArredondamentoNota37=?, regraArredondamentoNota38=?, regraArredondamentoNota39=?, regraArredondamentoNota40=?, "
					+ " quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula=?, incluirAutomaticamenteDisciplinaGrupoOptativa= ?, permitirAlunoRegularIncluirDisciplinaGrupoOptativa = ?, bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa = ?, considerarDisciplinasReprovadasPeriodosLetivosAnteriores=? , criarDigitoMascaraMatricula=?, formulaCriarDigitoMascaraMatricula=?, "
					+ " reprovarFaltaDiscCompostaCasoReprovadoFaltaDiscFilha=?, ocultarFrequenciaDisciplinaComposta=?, tipoUsoConfiguracaoAcademico=?, regraCalculoDisciplinaComposta =?, ocultarMediaFinalDisciplinaCasoReprovado=?, obrigarAceiteAlunoTermoParaEditarRenovacao=?, "
					+ " validarChoqueHorarioOutraMatriculaAluno = ?, validarDadosEnadeCensoMatricularAluno=?, usarFormulaCalculoFrequencia = ?, formulaCalculoFrequencia = ?, formulaCoeficienteRendimento = ?, quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula =? , mascaraNumeroProcessoExpedicaoDiploma = ?, casasDecimaisCoeficienteRendimento = ?, permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual = ?, registrarComoFaltaAulasRealizadasAposDataMatricula = ?, "
					+ " alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo = ?, habilitarDistribuicaoDisciplinaDependenciaAutomatica=?, habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares=?  , mascaraNumeroRegistroDiploma=?, permitiraproveitamentodisciplinasoptativas =? "
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getFormulaCalculoMediaFinal());
					sqlAlterar.setDouble(3, obj.getNota1().doubleValue());
					sqlAlterar.setBoolean(4, obj.isUtilizarNota1().booleanValue());
					sqlAlterar.setString(5, obj.getTituloNota1());
					sqlAlterar.setString(6, obj.getFormulaCalculoNota1());
					sqlAlterar.setString(7, obj.getFormulaUsoNota1());
					sqlAlterar.setDouble(8, obj.getNota2().doubleValue());
					sqlAlterar.setBoolean(9, obj.isUtilizarNota2().booleanValue());
					sqlAlterar.setString(10, obj.getTituloNota2());
					sqlAlterar.setString(11, obj.getFormulaCalculoNota2());
					sqlAlterar.setString(12, obj.getFormulaUsoNota2());
					sqlAlterar.setDouble(13, obj.getNota3().doubleValue());
					sqlAlterar.setBoolean(14, obj.isUtilizarNota3().booleanValue());
					sqlAlterar.setString(15, obj.getTituloNota3());
					sqlAlterar.setString(16, obj.getFormulaCalculoNota3());
					sqlAlterar.setString(17, obj.getFormulaUsoNota3());
					sqlAlterar.setDouble(18, obj.getNota4().doubleValue());
					sqlAlterar.setBoolean(19, obj.isUtilizarNota4().booleanValue());
					sqlAlterar.setString(20, obj.getTituloNota4());
					sqlAlterar.setString(21, obj.getFormulaCalculoNota4());
					sqlAlterar.setString(22, obj.getFormulaUsoNota4());
					sqlAlterar.setDouble(23, obj.getNota5().doubleValue());
					sqlAlterar.setBoolean(24, obj.isUtilizarNota5().booleanValue());
					sqlAlterar.setString(25, obj.getTituloNota5());
					sqlAlterar.setString(26, obj.getFormulaCalculoNota5());
					sqlAlterar.setString(27, obj.getFormulaUsoNota5());
					sqlAlterar.setDouble(28, obj.getNota6().doubleValue());
					sqlAlterar.setBoolean(29, obj.isUtilizarNota6().booleanValue());
					sqlAlterar.setString(30, obj.getTituloNota6());
					sqlAlterar.setString(31, obj.getFormulaCalculoNota6());
					sqlAlterar.setString(32, obj.getFormulaUsoNota6());
					sqlAlterar.setDouble(33, obj.getNota7().doubleValue());
					sqlAlterar.setBoolean(34, obj.isUtilizarNota7().booleanValue());
					sqlAlterar.setString(35, obj.getTituloNota7());
					sqlAlterar.setString(36, obj.getFormulaCalculoNota7());
					sqlAlterar.setString(37, obj.getFormulaUsoNota7());
					sqlAlterar.setDouble(38, obj.getNota8().doubleValue());
					sqlAlterar.setBoolean(39, obj.isUtilizarNota8().booleanValue());
					sqlAlterar.setString(40, obj.getTituloNota8());
					sqlAlterar.setString(41, obj.getFormulaCalculoNota8());
					sqlAlterar.setString(42, obj.getFormulaUsoNota8());
					sqlAlterar.setDouble(43, obj.getNota9().doubleValue());
					sqlAlterar.setBoolean(44, obj.isUtilizarNota9().booleanValue());
					sqlAlterar.setString(45, obj.getTituloNota9());
					sqlAlterar.setString(46, obj.getFormulaCalculoNota9());
					sqlAlterar.setString(47, obj.getFormulaUsoNota9());
					sqlAlterar.setDouble(48, obj.getNota10().doubleValue());
					sqlAlterar.setBoolean(49, obj.isUtilizarNota10().booleanValue());
					sqlAlterar.setString(50, obj.getTituloNota10());
					sqlAlterar.setString(51, obj.getFormulaCalculoNota10());
					sqlAlterar.setString(52, obj.getFormulaUsoNota10());
					sqlAlterar.setDouble(53, obj.getPercentualFrequenciaAprovacao().doubleValue());
					sqlAlterar.setString(54, obj.getMascaraPadraoGeracaoMatricula().trim());
					sqlAlterar.setBoolean(55, obj.getNota1MediaFinal().booleanValue());
					sqlAlterar.setBoolean(56, obj.getNota2MediaFinal().booleanValue());
					sqlAlterar.setBoolean(57, obj.getNota3MediaFinal().booleanValue());
					sqlAlterar.setBoolean(58, obj.getNota4MediaFinal().booleanValue());
					sqlAlterar.setBoolean(59, obj.getNota5MediaFinal().booleanValue());
					sqlAlterar.setBoolean(60, obj.getNota6MediaFinal().booleanValue());
					sqlAlterar.setBoolean(61, obj.getNota7MediaFinal().booleanValue());
					sqlAlterar.setBoolean(62, obj.getNota8MediaFinal().booleanValue());
					sqlAlterar.setBoolean(63, obj.getNota9MediaFinal().booleanValue());
					sqlAlterar.setBoolean(64, obj.getNota10MediaFinal().booleanValue());
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(65, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlAlterar.setNull(65, 0);
					}
					sqlAlterar.setInt(66, obj.getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo());
					sqlAlterar.setBoolean(67, obj.getReprovadoMatricularDisciplinaPeriodoLetivo());
					sqlAlterar.setBoolean(68, obj.getPermiteEvoluirPeriodoLetivoCasoReprovado());
					sqlAlterar.setInt(69, obj.getDiasMaximoReativacaoMatricula());
					sqlAlterar.setBoolean(70, obj.getRenovacaoMatriculaSequencial());
					sqlAlterar.setBoolean(71, obj.getPermiteCursarDisciplinaEPreRequisito());
					sqlAlterar.setBoolean(72, obj.getApresentarNota1());
					sqlAlterar.setBoolean(73, obj.getApresentarNota2());
					sqlAlterar.setBoolean(74, obj.getApresentarNota3());
					sqlAlterar.setBoolean(75, obj.getApresentarNota4());
					sqlAlterar.setBoolean(76, obj.getApresentarNota5());
					sqlAlterar.setBoolean(77, obj.getApresentarNota6());
					sqlAlterar.setBoolean(78, obj.getApresentarNota7());
					sqlAlterar.setBoolean(79, obj.getApresentarNota8());
					sqlAlterar.setBoolean(80, obj.getApresentarNota9());
					sqlAlterar.setBoolean(81, obj.getApresentarNota10());
					sqlAlterar.setBoolean(82, obj.getNotasDeCincoEmCincoDecimos());
					sqlAlterar.setBoolean(83, obj.getLimitarQtdeDiasMaxDownload());
					sqlAlterar.setInt(84, obj.getQtdeMaxDiasDownload());
					sqlAlterar.setBoolean(85, obj.getNotasDeCincoEmCincoDecimosApenasMedia());
					sqlAlterar.setDouble(86, obj.getNota11().doubleValue());
					sqlAlterar.setBoolean(87, obj.isUtilizarNota11().booleanValue());
					sqlAlterar.setString(88, obj.getTituloNota11());
					sqlAlterar.setString(89, obj.getFormulaCalculoNota11());
					sqlAlterar.setString(90, obj.getFormulaUsoNota11());
					sqlAlterar.setBoolean(91, obj.getNota11MediaFinal().booleanValue());
					sqlAlterar.setBoolean(92, obj.getApresentarNota11());
					sqlAlterar.setDouble(93, obj.getNota12().doubleValue());
					sqlAlterar.setBoolean(94, obj.isUtilizarNota12().booleanValue());
					sqlAlterar.setString(95, obj.getTituloNota12());
					sqlAlterar.setString(96, obj.getFormulaCalculoNota12());
					sqlAlterar.setString(97, obj.getFormulaUsoNota12());
					sqlAlterar.setBoolean(98, obj.getNota12MediaFinal().booleanValue());
					sqlAlterar.setBoolean(99, obj.getApresentarNota12());
					sqlAlterar.setDouble(100, obj.getNota13().doubleValue());
					sqlAlterar.setBoolean(101, obj.isUtilizarNota13().booleanValue());
					sqlAlterar.setString(102, obj.getTituloNota13());
					sqlAlterar.setString(103, obj.getFormulaCalculoNota13());
					sqlAlterar.setString(104, obj.getFormulaUsoNota13());
					sqlAlterar.setBoolean(105, obj.getNota13MediaFinal().booleanValue());
					sqlAlterar.setBoolean(106, obj.getApresentarNota13());
					sqlAlterar.setBoolean(107, obj.getEnviarMensagemNotaAbaixoMedia());
					sqlAlterar.setBoolean(108, obj.getApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico());
					sqlAlterar.setDouble(109, obj.getPesoMediaNotaMeritoAcademico());
					sqlAlterar.setDouble(110, obj.getPesoMediaFrequenciaMeritoAcademico());
					sqlAlterar.setBoolean(111, obj.getUsarSituacaoAprovadoAproveitamentoTransferenciaGrade());
					sqlAlterar.setBoolean(112, obj.getConsiderarCampoNuloNotaZerada());
					sqlAlterar.setBoolean(113, obj.getApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno());
					sqlAlterar.setBoolean(114, obj.getLiberarPreRequisitoDisciplinaConcomitancia());
					sqlAlterar.setBoolean(115, obj.getApresentarTextoSemNotaCampoNuloHistorico());
					sqlAlterar.setBoolean(116, obj.getUtilizarNota1PorConceito());
					sqlAlterar.setBoolean(117, obj.getUtilizarNota2PorConceito());
					sqlAlterar.setBoolean(118, obj.getUtilizarNota3PorConceito());
					sqlAlterar.setBoolean(119, obj.getUtilizarNota4PorConceito());
					sqlAlterar.setBoolean(120, obj.getUtilizarNota5PorConceito());
					sqlAlterar.setBoolean(121, obj.getUtilizarNota6PorConceito());
					sqlAlterar.setBoolean(122, obj.getUtilizarNota7PorConceito());
					sqlAlterar.setBoolean(123, obj.getUtilizarNota8PorConceito());
					sqlAlterar.setBoolean(124, obj.getUtilizarNota9PorConceito());
					sqlAlterar.setBoolean(125, obj.getUtilizarNota10PorConceito());
					sqlAlterar.setBoolean(126, obj.getUtilizarNota11PorConceito());
					sqlAlterar.setBoolean(127, obj.getUtilizarNota12PorConceito());
					sqlAlterar.setBoolean(128, obj.getUtilizarNota13PorConceito());
					int i = 129;
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva1());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva2());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva3());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva4());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva5());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva6());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva7());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva8());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva9());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva10());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva11());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva12());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva13());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva1());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva2());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva3());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva4());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva5());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva6());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva7());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva8());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva9());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva10());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva11());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva12());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva13());
					sqlAlterar.setBoolean(i++, obj.getUtilizarArredondamentoMediaParaMais());
					sqlAlterar.setString(i++, obj.getTipoCalculoCargaHorariaFrequencia().name());
					sqlAlterar.setBoolean(i++, obj.getPermiteRegistrarAulaFutura());

					// **********NOTA14*********
					sqlAlterar.setDouble(i++, obj.getNota14().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota14().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota14());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota14());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota14());
					sqlAlterar.setBoolean(i++, obj.getNota14MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota14());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva14());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota14PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva14());
					sqlAlterar.setString(i++, obj.getBimestreNota14().toString());
					// *********************
					// **********NOTA15*********
					sqlAlterar.setDouble(i++, obj.getNota15().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota15().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota15());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota15());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota15());
					sqlAlterar.setBoolean(i++, obj.getNota15MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota15());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva15());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota15PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva15());
					sqlAlterar.setString(i++, obj.getBimestreNota15().toString());
					// *********************
					// **********NOTA16*********
					sqlAlterar.setDouble(i++, obj.getNota16().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota16().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota16());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota16());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota16());
					sqlAlterar.setBoolean(i++, obj.getNota16MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota16());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva16());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota16PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva16());
					sqlAlterar.setString(i++, obj.getBimestreNota16().toString());
					// *********************
					// **********NOTA17*********
					sqlAlterar.setDouble(i++, obj.getNota17().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota17().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota17());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota17());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota17());
					sqlAlterar.setBoolean(i++, obj.getNota17MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota17());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva17());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota17PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva17());
					sqlAlterar.setString(i++, obj.getBimestreNota17().toString());
					// *********************
					// **********NOTA18*********
					sqlAlterar.setDouble(i++, obj.getNota18().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota18().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota18());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota18());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota18());
					sqlAlterar.setBoolean(i++, obj.getNota18MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota18());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva18());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota18PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva18());
					sqlAlterar.setString(i++, obj.getBimestreNota18().toString());
					// *********************
					// **********NOTA19*********
					sqlAlterar.setDouble(i++, obj.getNota19().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota19().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota19());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota19());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota19());
					sqlAlterar.setBoolean(i++, obj.getNota19MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota19());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva19());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota19PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva19());
					sqlAlterar.setString(i++, obj.getBimestreNota19().toString());
					// *********************
					// **********NOTA20*********
					sqlAlterar.setDouble(i++, obj.getNota20().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota20().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota20());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota20());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota20());
					sqlAlterar.setBoolean(i++, obj.getNota20MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota20());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva20());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota20PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva20());
					sqlAlterar.setString(i++, obj.getBimestreNota20().toString());
					// *********************
					// **********NOTA21*********
					sqlAlterar.setDouble(i++, obj.getNota21().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota21().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota21());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota21());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota21());
					sqlAlterar.setBoolean(i++, obj.getNota21MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota21());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva21());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota21PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva21());
					sqlAlterar.setString(i++, obj.getBimestreNota21().toString());
					// *********************
					// **********NOTA22*********
					sqlAlterar.setDouble(i++, obj.getNota22().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota22().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota22());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota22());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota22());
					sqlAlterar.setBoolean(i++, obj.getNota22MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota22());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva22());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota22PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva22());
					sqlAlterar.setString(i++, obj.getBimestreNota22().toString());
					// *********************
					// **********NOTA23*********
					sqlAlterar.setDouble(i++, obj.getNota23().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota23().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota23());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota23());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota23());
					sqlAlterar.setBoolean(i++, obj.getNota23MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota23());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva23());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota23PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva23());
					sqlAlterar.setString(i++, obj.getBimestreNota23().toString());
					// *********************
					// **********NOTA24*********
					sqlAlterar.setDouble(i++, obj.getNota24().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota24().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota24());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota24());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota24());
					sqlAlterar.setBoolean(i++, obj.getNota24MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota24());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva24());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota24PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva24());
					sqlAlterar.setString(i++, obj.getBimestreNota24().toString());
					// *********************
					// **********NOTA25*********
					sqlAlterar.setDouble(i++, obj.getNota25().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota25().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota25());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota25());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota25());
					sqlAlterar.setBoolean(i++, obj.getNota25MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota25());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva25());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota25PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva25());
					sqlAlterar.setString(i++, obj.getBimestreNota25().toString());
					// *********************
					// **********NOTA26*********
					sqlAlterar.setDouble(i++, obj.getNota26().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota26().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota26());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota26());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota26());
					sqlAlterar.setBoolean(i++, obj.getNota26MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota26());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva26());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota26PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva26());
					sqlAlterar.setString(i++, obj.getBimestreNota26().toString());
					// *********************
					// **********NOTA27*********
					sqlAlterar.setDouble(i++, obj.getNota27().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota27().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota27());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota27());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota27());
					sqlAlterar.setBoolean(i++, obj.getNota27MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota27());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva27());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota27PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva27());
					sqlAlterar.setString(i++, obj.getBimestreNota27().toString());
					// *********************
					// **********NOTA28*********
					sqlAlterar.setDouble(i++, obj.getNota28().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota28().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota28());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota28());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota28());
					sqlAlterar.setBoolean(i++, obj.getNota28MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota28());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva28());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota28PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva28());
					sqlAlterar.setString(i++, obj.getBimestreNota28().toString());
					// *********************
					// **********NOTA29*********
					sqlAlterar.setDouble(i++, obj.getNota29().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota29().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota29());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota29());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota29());
					sqlAlterar.setBoolean(i++, obj.getNota29MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota29());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva29());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota29PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva29());
					sqlAlterar.setString(i++, obj.getBimestreNota29().toString());
					// *********************
					// **********NOTA30*********
					sqlAlterar.setDouble(i++, obj.getNota30().doubleValue());
					sqlAlterar.setBoolean(i++, obj.isUtilizarNota30().booleanValue());
					sqlAlterar.setString(i++, obj.getTituloNota30());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota30());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota30());
					sqlAlterar.setBoolean(i++, obj.getNota30MediaFinal().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota30());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva30());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota30PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva30());
					sqlAlterar.setString(i++, obj.getBimestreNota30().toString());
					// *********************
					// **********BIMESTRAIS 1 A 13 *********
					sqlAlterar.setString(i++, obj.getBimestreNota1().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota2().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota3().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota4().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota5().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota6().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota7().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota8().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota9().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota10().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota11().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota12().toString());
					sqlAlterar.setString(i++, obj.getBimestreNota13().toString());

					sqlAlterar.setString(i++, obj.getTituloNotaApresentar1().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar2().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar3().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar4().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar5().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar6().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar7().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar8().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar9().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar10().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar11().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar12().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar13().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar14().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar15().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar16().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar17().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar18().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar19().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar20().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar21().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar22().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar23().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar24().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar25().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar26().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar27().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar28().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar29().toString());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar30().toString());

					sqlAlterar.setBoolean(i++, obj.getControlarAvancoPeriodoPorCreditoOuCH());
					sqlAlterar.setString(i++, obj.getTipoControleAvancoPeriodoPorCreditoOuCH());
					sqlAlterar.setInt(i++, obj.getPercCumprirPeriodoAnteriorRenovarProximoPerLetivo());
					sqlAlterar.setInt(i++, obj.getPercCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo());
					sqlAlterar.setBoolean(i++, obj.getControlarInclusaoDisciplinaPorNrMaxCreditoOuCH());
					sqlAlterar.setString(i++, obj.getTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH());
					sqlAlterar.setBoolean(i++, obj.getAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos());
					sqlAlterar.setBoolean(i++, obj.getPermitirInclusaoDiscipDependenciaComChoqueHorario());
					sqlAlterar.setInt(i++, obj.getQtdPermitirInclusaoDiscipDependenciaComChoqueHorario());
					sqlAlterar.setBoolean(i++, obj.getPermitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta());

					sqlAlterar.setDouble(i++, obj.getFaixaNota1Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota1Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota2Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota2Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota3Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota3Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota4Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota4Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota5Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota5Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota6Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota6Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota7Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota7Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota8Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota8Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota9Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota9Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota10Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota10Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota11Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota11Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota12Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota12Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota13Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota13Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota14Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota14Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota15Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota15Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota16Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota16Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota17Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota17Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota18Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota18Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota19Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota19Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota20Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota20Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota21Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota21Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota22Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota22Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota23Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota23Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota24Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota24Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota25Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota25Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota26Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota26Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota27Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota27Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota28Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota28Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota29Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota29Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota30Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota30Menor());
					sqlAlterar.setInt(i++, obj.getQuantidadeCasasDecimaisPermitirAposVirgula());
					sqlAlterar.setBoolean(i++, obj.getApresentarSiglaConcessaoCredito());
					sqlAlterar.setInt(i++, obj.getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
					sqlAlterar.setString(i++, obj.getTipoApresentarFrequenciaVisaoAluno());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota1());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota2());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota3());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota4());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota5());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota6());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota7());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota8());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota9());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota10());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota11());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota12());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota13());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota14());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota15());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota16());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota17());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota18());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota19());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota20());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota21());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota22());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota23());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota24());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota25());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota26());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota27());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota28());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota29());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota30());
					sqlAlterar.setBoolean(i++, obj.getApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico());
					sqlAlterar.setBoolean(i++, obj.getObrigaInformarFormaIngressoMatricula());
					sqlAlterar.setBoolean(i++, obj.getObrigaInformarOrigemFormaIngressoMatricula());
					sqlAlterar.setBoolean(i++, obj.getBloquearRegistroAulaAnteriorDataMatricula());
					sqlAlterar.setBoolean(i++, obj.getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao());
					sqlAlterar.setBoolean(i++, obj.getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal());
					sqlAlterar.setBoolean(i++, obj.getCalcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes());
					sqlAlterar.setBoolean(i++, obj.getHabilitarControleInclusaoDisciplinaPeriodoFuturo());
					sqlAlterar.setInt(i++, obj.getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina());
					sqlAlterar.setBoolean(i++, obj.getBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular());
					sqlAlterar.setBoolean(i++, obj.getBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular());
					sqlAlterar.setBoolean(i++, obj.getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia());
					sqlAlterar.setInt(i++, obj.getPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia());
					sqlAlterar.setBoolean(i++, obj.getRemoverAutomaticamenteDisciplinaSemVagaRenovacaoOnline());
					sqlAlterar.setBoolean(i++, obj.getHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao());
					sqlAlterar.setBoolean(i++, obj.getDistribuirTurmaPraticaTeoricaComAulaProgramada());
					sqlAlterar.setBoolean(i++, obj.getRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario());
					sqlAlterar.setBoolean(i++, obj.getConsiderarRegularAlunoDependenciaOptativa());
					sqlAlterar.setBoolean(i++, obj.getUtilizarApoioEADParaDisciplinasModalidadePresencial());
					sqlAlterar.setBoolean(i++, obj.getConsiderarPortadoDiplomaTransEntradaAlunoIrregular());
					sqlAlterar.setBoolean(i++, obj.getMatricularApenasDisciplinaAulaProgramada());
					
					
//					A PARTIR NOTA 31 
					
					sqlAlterar.setDouble(i++, obj.getNota31());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota31());
					sqlAlterar.setString(i++, obj.getTituloNota31());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota31());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota31());
					sqlAlterar.setBoolean(i++, obj.getNota31MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota31());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva31());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota31PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva31());
					sqlAlterar.setString(i++, obj.getBimestreNota31().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota32());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota32());
					sqlAlterar.setString(i++, obj.getTituloNota32());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota32());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota32());
					sqlAlterar.setBoolean(i++, obj.getNota32MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota32());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva32());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota32PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva32());
					sqlAlterar.setString(i++, obj.getBimestreNota32().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota33());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota33());
					sqlAlterar.setString(i++, obj.getTituloNota33());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota33());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota33());
					sqlAlterar.setBoolean(i++, obj.getNota33MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota33());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva33());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota33PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva33());
					sqlAlterar.setString(i++, obj.getBimestreNota33().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota34());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota34());
					sqlAlterar.setString(i++, obj.getTituloNota34());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota34());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota34());
					sqlAlterar.setBoolean(i++, obj.getNota34MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota34());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva34());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota34PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva34());
					sqlAlterar.setString(i++, obj.getBimestreNota34().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota35());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota35());
					sqlAlterar.setString(i++, obj.getTituloNota35());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota35());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota35());
					sqlAlterar.setBoolean(i++, obj.getNota35MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota35());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva35());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota35PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva35());
					sqlAlterar.setString(i++, obj.getBimestreNota35().toString());					
					
					sqlAlterar.setDouble(i++, obj.getNota36());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota36());
					sqlAlterar.setString(i++, obj.getTituloNota36());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota36());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota36());
					sqlAlterar.setBoolean(i++, obj.getNota36MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota36());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva36());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota36PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva36());
					sqlAlterar.setString(i++, obj.getBimestreNota36().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota37());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota37());
					sqlAlterar.setString(i++, obj.getTituloNota37());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota37());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota37());
					sqlAlterar.setBoolean(i++, obj.getNota37MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota37());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva37());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota37PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva37());
					sqlAlterar.setString(i++, obj.getBimestreNota37().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota38());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota38());
					sqlAlterar.setString(i++, obj.getTituloNota38());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota38());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota38());
					sqlAlterar.setBoolean(i++, obj.getNota38MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota38());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva38());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota38PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva38());
					sqlAlterar.setString(i++, obj.getBimestreNota38().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota39());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota39());
					sqlAlterar.setString(i++, obj.getTituloNota39());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota39());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota39());
					sqlAlterar.setBoolean(i++, obj.getNota39MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota39());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva39());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota39PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva39());
					sqlAlterar.setString(i++, obj.getBimestreNota39().toString());
					
					sqlAlterar.setDouble(i++, obj.getNota40());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota40());
					sqlAlterar.setString(i++, obj.getTituloNota40());
					sqlAlterar.setString(i++, obj.getFormulaCalculoNota40());
					sqlAlterar.setString(i++, obj.getFormulaUsoNota40());
					sqlAlterar.setBoolean(i++, obj.getNota40MediaFinal());
					sqlAlterar.setBoolean(i++, obj.getApresentarNota40());
					sqlAlterar.setString(i++, obj.getPoliticaSubstitutiva40());
					sqlAlterar.setBoolean(i++, obj.getUtilizarNota40PorConceito());
					sqlAlterar.setBoolean(i++, obj.getUtilizarComoSubstitutiva40());
					sqlAlterar.setString(i++, obj.getBimestreNota40().toString());
					
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar31());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar32());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar33());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar34());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar35());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar36());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar37());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar38());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar39());
					sqlAlterar.setString(i++, obj.getTituloNotaApresentar40());
					
					sqlAlterar.setDouble(i++, obj.getFaixaNota31Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota31Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota32Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota32Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota33Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota33Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota34Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota34Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota35Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota35Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota36Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota36Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota37Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota37Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota38Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota38Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota39Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota39Menor());
					sqlAlterar.setDouble(i++, obj.getFaixaNota40Maior());
					sqlAlterar.setDouble(i++, obj.getFaixaNota40Menor());
					
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota31());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota32());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota32());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota34());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota35());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota36());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota37());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota38());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota39());
					sqlAlterar.setString(i++, obj.getRegraArredondamentoNota40());
					
					sqlAlterar.setInt(i++, obj.getQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula());
					sqlAlterar.setBoolean(i++, obj.getIncluirAutomaticamenteDisciplinaGrupoOptativa());
					sqlAlterar.setBoolean(i++, obj.getPermitirAlunoRegularIncluirDisciplinaGrupoOptativa());
					sqlAlterar.setBoolean(i++, obj.getBloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa());
					sqlAlterar.setBoolean(i++, obj.getConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores());
					sqlAlterar.setBoolean(i++, obj.getCriarDigitoMascaraMatricula());
					sqlAlterar.setString(i++, obj.getFormulaCriarDigitoMascaraMatricula());
					sqlAlterar.setBoolean(i++, obj.getReprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha());
					sqlAlterar.setBoolean(i++, obj.getOcultarFrequenciaDisciplinaComposta());
					sqlAlterar.setString(i++, obj.getTipoUsoConfiguracaoAcademico().name());
					sqlAlterar.setString(i++, obj.getRegraCalculoDisciplinaComposta().name());
					sqlAlterar.setBoolean(i++, obj.getOcultarMediaFinalDisciplinaCasoReprovado());
					sqlAlterar.setBoolean(i++, obj.getObrigarAceiteAlunoTermoParaEditarRenovacao());
					sqlAlterar.setBoolean(i++, obj.getValidarChoqueHorarioOutraMatriculaAluno());
					sqlAlterar.setBoolean(i++, obj.getValidarDadosEnadeCensoMatricularAluno());
					sqlAlterar.setBoolean(i++, obj.getUsarFormulaCalculoFrequencia());
					sqlAlterar.setString(i++, obj.getFormulaCalculoFrequencia());
					sqlAlterar.setString(i++, obj.getFormulaCoeficienteRendimento());
					sqlAlterar.setInt(i++, obj.getQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula());
					sqlAlterar.setString(i++, obj.getMascaraNumeroProcessoExpedicaoDiploma());
					sqlAlterar.setInt(i++, obj.getCasasDecimaisCoeficienteRendimento());
					sqlAlterar.setBoolean(i++, obj.getPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual());
					sqlAlterar.setBoolean(i++, obj.getRegistrarComoFaltaAulasRealizadasAposDataMatricula());
					sqlAlterar.setBoolean(i++, obj.isAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo());
					sqlAlterar.setBoolean(i++, obj.isHabilitarDistribuicaoDisciplinaDependenciaAutomatica());
					sqlAlterar.setBoolean(i++, obj.isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares());
					sqlAlterar.setString(i++, obj.getMascaraNumeroRegistroDiploma());
					sqlAlterar.setBoolean(i++, obj.getPermitirAproveitamentoDisciplinasOptativas());
					sqlAlterar.setInt(i++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().alterarConfiguracaoAcademicoNotaConceito(obj);
			getFacadeFactory().getConfiguracaoAcademicoNotaFacade().alterarConfiguracaoAcademicoNotaVOs(obj);
			getAplicacaoControle().removerConfiguracaoAcademica(obj.getCodigo());
			getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(null, null, false, true);
		} catch (Exception e) {
			throw e;
		}
	}

	public void alterar(List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs, UsuarioVO usuario) throws Exception {
		Iterator<ConfiguracaoAcademicoVO> i = configuracaoAcademicoVOs.iterator();
		while (i.hasNext()) {
			alterar(i.next(), usuario);
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAcademicoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoAcademicoVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM ConfiguracaoAcademico WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<ConfiguracaoAcademicoVO> consultarConfiguracoesASeremUsadas(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = new ArrayList<>(0);
		ConfiguracoesVO configuracoesPadrao = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoPadrao(controlarAcesso, nivelMontarDados, usuario);
		try {
			if (Uteis.isAtributoPreenchido(usuario) && Uteis.isAtributoPreenchido(usuario.getTipoUsuario()) && usuario.getTipoUsuario().equals("DM")) {
				return configuracaoAcademicoVOs = this.consultarTodasConfiguracaoAcademica(Uteis.NIVELMONTARDADOS_TODOS,controlarAcesso, usuario);
			}
		} catch (Exception e) {
			return configuracaoAcademicoVOs = this.consultarPorCodigoConfiguracoes(configuracoesPadrao.getCodigo(), false, usuario);
		}
		
		ConfiguracoesVO configuracoesUnidadeLogada = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_TODOS, usuario, null);
		
		if (Uteis.isAtributoPreenchido(configuracoesUnidadeLogada) && this.executarVerificarConfiguracaoAcademicaVinculadaUnidadeEnsinoLogada(configuracoesUnidadeLogada)) {
			configuracaoAcademicoVOs.addAll(consultarPorCodigoConfiguracoes(configuracoesUnidadeLogada.getCodigo(), false, usuario));    
			List<ConfiguracaoAcademicoVO> listaConfiguracaoAcademicaPadrao = consultarPorCodigoConfiguracoes(configuracoesPadrao.getCodigo(), false, usuario);	
			
			if (Uteis.isAtributoPreenchido(listaConfiguracaoAcademicaPadrao)) {
				List<Integer> listaCodigoConfiguracaoAcademico = new ArrayList<>(0);
				Iterator<ConfiguracaoAcademicoVO> i = configuracaoAcademicoVOs.iterator();
				while (i.hasNext()) {
					ConfiguracaoAcademicoVO obj = i.next();
					listaCodigoConfiguracaoAcademico.add(obj.getCodigo());
				}
				for (ConfiguracaoAcademicoVO configuracaoAcademica : listaConfiguracaoAcademicaPadrao) {
					if (!listaCodigoConfiguracaoAcademico.contains(configuracaoAcademica.getCodigo())) {
						configuracaoAcademicoVOs.add(configuracaoAcademica);
					}
				}
				listaCodigoConfiguracaoAcademico = null;
			}
			listaConfiguracaoAcademicaPadrao = null;

		}else {		 
			configuracaoAcademicoVOs.addAll(consultarPorCodigoConfiguracoes(configuracoesPadrao.getCodigo(), false, usuario));
		}
		return configuracaoAcademicoVOs;
	}

	public List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select configuracaoacademico.codigo, (configuracoes.nome || ' - ' || configuracaoacademico.nome) AS nome from configuracaoacademico ");
		sb.append(" inner join configuracoes on configuracoes.codigo = configuracaoacademico.configuracoes ");
		sb.append(" order by configuracoes.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ConfiguracaoAcademicoVO> listaConfiguracaoAcademico = new ArrayList<ConfiguracaoAcademicoVO>();
		while (tabelaResultado.next()) {
			ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaConfiguracaoAcademico.add(obj);
		}
		return listaConfiguracaoAcademico;
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoAcademico</code> através do valor do atributo
	 * <code>String nome</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoAcademicoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Deprecated
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoAcademico WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoAcademico</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoAcademicoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoAcademico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public List<ConfiguracaoAcademicoVO> consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM configuracaoacademico where configuracoes = ? order by nome ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoConfiguracoes });
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoAcademicoVO</code> resultantes da consulta.
	 */
	public  List<ConfiguracaoAcademicoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConfiguracaoAcademicoVO> vetResultado = new ArrayList<ConfiguracaoAcademicoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code>.
	 * 
	 * @return O objeto da classe <code>ConfiguracaoAcademicoVO</code> com os
	 *         dados devidamente montados.
	 */
	public  ConfiguracaoAcademicoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setEnviarMensagemNotaAbaixoMedia(dadosSQL.getBoolean("enviarMensagemNotaAbaixoMedia"));
		obj.setUtilizarArredondamentoMediaParaMais(dadosSQL.getBoolean("utilizarArredondamentoMediaParaMais"));
		obj.setApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico(dadosSQL.getBoolean("apresentarPeriodoLetivoMatriculaPeriodoAtualHistorico"));
		obj.setFormulaCalculoMediaFinal(dadosSQL.getString("formulaCalculoMediaFinal"));
		obj.setPercMinimoCargaHorariaDisciplinaParaAproveitamento(dadosSQL.getInt("percMinimoCargaHorariaDisciplinaParaAproveitamento"));
		obj.setNota1((dadosSQL.getDouble("nota1")));
		obj.setUtilizarNota1((dadosSQL.getBoolean("utilizarNota1")));
		obj.setTituloNota1(dadosSQL.getString("tituloNota1"));
		obj.setFormulaCalculoNota1(dadosSQL.getString("formulaCalculoNota1"));
		obj.setFormulaUsoNota1(dadosSQL.getString("formulaUsoNota1"));
		obj.setNota2((dadosSQL.getDouble("nota2")));
		obj.setUtilizarNota2((dadosSQL.getBoolean("utilizarNota2")));
		obj.setTituloNota2(dadosSQL.getString("tituloNota2"));
		obj.setFormulaCalculoNota2(dadosSQL.getString("formulaCalculoNota2"));
		obj.setFormulaUsoNota2(dadosSQL.getString("formulaUsoNota2"));
		obj.setNota3((dadosSQL.getDouble("nota3")));
		obj.setUtilizarNota3((dadosSQL.getBoolean("utilizarNota3")));
		obj.setTituloNota3(dadosSQL.getString("tituloNota3"));
		obj.setFormulaCalculoNota3(dadosSQL.getString("formulaCalculoNota3"));
		obj.setFormulaUsoNota3(dadosSQL.getString("formulaUsoNota3"));
		obj.setNota4((dadosSQL.getDouble("nota4")));
		obj.setUtilizarNota4((dadosSQL.getBoolean("utilizarNota4")));
		obj.setTituloNota4(dadosSQL.getString("tituloNota4"));
		obj.setFormulaCalculoNota4(dadosSQL.getString("formulaCalculoNota4"));
		obj.setFormulaUsoNota4(dadosSQL.getString("formulaUsoNota4"));
		obj.setNota5((dadosSQL.getDouble("nota5")));
		obj.setUtilizarNota5((dadosSQL.getBoolean("utilizarNota5")));
		obj.setTituloNota5(dadosSQL.getString("tituloNota5"));
		obj.setFormulaCalculoNota5(dadosSQL.getString("formulaCalculoNota5"));
		obj.setFormulaUsoNota5(dadosSQL.getString("formulaUsoNota5"));
		obj.setNota6((dadosSQL.getDouble("nota6")));
		obj.setUtilizarNota6((dadosSQL.getBoolean("utilizarNota6")));
		obj.setTituloNota6(dadosSQL.getString("tituloNota6"));
		obj.setFormulaCalculoNota6(dadosSQL.getString("formulaCalculoNota6"));
		obj.setFormulaUsoNota6(dadosSQL.getString("formulaUsoNota6"));
		obj.setNota7((dadosSQL.getDouble("nota7")));
		obj.setUtilizarNota7((dadosSQL.getBoolean("utilizarNota7")));
		obj.setTituloNota7(dadosSQL.getString("tituloNota7"));
		obj.setFormulaCalculoNota7(dadosSQL.getString("formulaCalculoNota7"));
		obj.setFormulaUsoNota7(dadosSQL.getString("formulaUsoNota7"));
		obj.setNota8((dadosSQL.getDouble("nota8")));
		obj.setUtilizarNota8((dadosSQL.getBoolean("utilizarNota8")));
		obj.setTituloNota8(dadosSQL.getString("tituloNota8"));
		obj.setFormulaCalculoNota8(dadosSQL.getString("formulaCalculoNota8"));
		obj.setFormulaUsoNota8(dadosSQL.getString("formulaUsoNota8"));
		obj.setNota9((dadosSQL.getDouble("nota9")));
		obj.setUtilizarNota9((dadosSQL.getBoolean("utilizarNota9")));
		obj.setTituloNota9(dadosSQL.getString("tituloNota9"));
		obj.setFormulaCalculoNota9(dadosSQL.getString("formulaCalculoNota9"));
		obj.setFormulaUsoNota9(dadosSQL.getString("formulaUsoNota9"));
		obj.setNota10((dadosSQL.getDouble("nota10")));
		obj.setUtilizarNota10((dadosSQL.getBoolean("utilizarNota10")));
		obj.setTituloNota10(dadosSQL.getString("tituloNota10"));
		obj.setFormulaCalculoNota10(dadosSQL.getString("formulaCalculoNota10"));
		obj.setFormulaUsoNota10(dadosSQL.getString("formulaUsoNota10"));
		obj.setNota11((dadosSQL.getDouble("nota11")));
		obj.setUtilizarNota11((dadosSQL.getBoolean("utilizarNota11")));
		obj.setTituloNota11(dadosSQL.getString("tituloNota11"));
		obj.setFormulaCalculoNota11(dadosSQL.getString("formulaCalculoNota11"));
		obj.setFormulaUsoNota11(dadosSQL.getString("formulaUsoNota11"));
		obj.setNota12((dadosSQL.getDouble("nota12")));
		obj.setUtilizarNota12((dadosSQL.getBoolean("utilizarNota12")));
		obj.setTituloNota12(dadosSQL.getString("tituloNota12"));
		obj.setFormulaCalculoNota12(dadosSQL.getString("formulaCalculoNota12"));
		obj.setFormulaUsoNota12(dadosSQL.getString("formulaUsoNota12"));
		obj.setNota13((dadosSQL.getDouble("nota13")));
		obj.setUtilizarNota13((dadosSQL.getBoolean("utilizarNota13")));
		obj.setTituloNota13(dadosSQL.getString("tituloNota13"));
		obj.setFormulaCalculoNota13(dadosSQL.getString("formulaCalculoNota13"));
		obj.setFormulaUsoNota13(dadosSQL.getString("formulaUsoNota13"));
		obj.setPesoMediaNotaMeritoAcademico((dadosSQL.getDouble("pesoMediaNotaMeritoAcademico")));
		obj.setPesoMediaFrequenciaMeritoAcademico((dadosSQL.getDouble("pesoMediaFrequenciaMeritoAcademico")));
		obj.setPercentualFrequenciaAprovacao((dadosSQL.getDouble("percentualFrequenciaAprovacao")));
		obj.setMascaraPadraoGeracaoMatricula(dadosSQL.getString("mascaraPadraoGeracaoMatricula"));
		obj.setNota1MediaFinal((dadosSQL.getBoolean("nota1MediaFinal")));
		obj.setNota2MediaFinal((dadosSQL.getBoolean("nota2MediaFinal")));
		obj.setNota3MediaFinal((dadosSQL.getBoolean("nota3MediaFinal")));
		obj.setNota4MediaFinal((dadosSQL.getBoolean("nota4MediaFinal")));
		obj.setNota5MediaFinal((dadosSQL.getBoolean("nota5MediaFinal")));
		obj.setNota6MediaFinal((dadosSQL.getBoolean("nota6MediaFinal")));
		obj.setNota7MediaFinal((dadosSQL.getBoolean("nota7MediaFinal")));
		obj.setNota8MediaFinal((dadosSQL.getBoolean("nota8MediaFinal")));
		obj.setNota9MediaFinal((dadosSQL.getBoolean("nota9MediaFinal")));
		obj.setNota10MediaFinal((dadosSQL.getBoolean("nota10MediaFinal")));
		obj.setNota11MediaFinal((dadosSQL.getBoolean("nota11MediaFinal")));
		obj.setNota12MediaFinal((dadosSQL.getBoolean("nota12MediaFinal")));
		obj.setNota13MediaFinal((dadosSQL.getBoolean("nota13MediaFinal")));
		obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("configuracoes"));
		obj.setNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(dadosSQL.getInt("numeroDisciplinaConsiderarReprovadoPeriodoLetivo"));
		obj.setReprovadoMatricularDisciplinaPeriodoLetivo(dadosSQL.getBoolean("reprovadoMatricularDisciplinaPeriodoLetivo"));
		obj.setPermiteEvoluirPeriodoLetivoCasoReprovado(dadosSQL.getBoolean("permiteEvoluirPeriodoLetivoCasoReprovado"));
		obj.setDiasMaximoReativacaoMatricula(dadosSQL.getInt("diasmaximoreativacaomatricula"));
		obj.setRenovacaoMatriculaSequencial(dadosSQL.getBoolean("renovacaomatriculasequencial"));
		obj.setObrigarAceiteAlunoTermoParaEditarRenovacao(dadosSQL.getBoolean("obrigarAceiteAlunoTermoParaEditarRenovacao"));
		obj.setPermiteCursarDisciplinaEPreRequisito(dadosSQL.getBoolean("cursardisciplinaeprerequisito"));
		obj.setApresentarNota1(dadosSQL.getBoolean("apresentarNota1"));
		obj.setApresentarNota2(dadosSQL.getBoolean("apresentarNota2"));
		obj.setApresentarNota3(dadosSQL.getBoolean("apresentarNota3"));
		obj.setApresentarNota4(dadosSQL.getBoolean("apresentarNota4"));
		obj.setApresentarNota5(dadosSQL.getBoolean("apresentarNota5"));
		obj.setApresentarNota6(dadosSQL.getBoolean("apresentarNota6"));
		obj.setApresentarNota7(dadosSQL.getBoolean("apresentarNota7"));
		obj.setApresentarNota8(dadosSQL.getBoolean("apresentarNota8"));
		obj.setApresentarNota9(dadosSQL.getBoolean("apresentarNota9"));
		obj.setApresentarNota10(dadosSQL.getBoolean("apresentarNota10"));
		obj.setApresentarNota11(dadosSQL.getBoolean("apresentarNota11"));
		obj.setApresentarNota12(dadosSQL.getBoolean("apresentarNota12"));
		obj.setApresentarNota13(dadosSQL.getBoolean("apresentarNota13"));

		obj.setUtilizarNota1PorConceito(dadosSQL.getBoolean("utilizarNota1PorConceito"));
		obj.setUtilizarNota2PorConceito(dadosSQL.getBoolean("utilizarNota2PorConceito"));
		obj.setUtilizarNota3PorConceito(dadosSQL.getBoolean("utilizarNota3PorConceito"));
		obj.setUtilizarNota4PorConceito(dadosSQL.getBoolean("utilizarNota4PorConceito"));
		obj.setUtilizarNota5PorConceito(dadosSQL.getBoolean("utilizarNota5PorConceito"));
		obj.setUtilizarNota6PorConceito(dadosSQL.getBoolean("utilizarNota6PorConceito"));
		obj.setUtilizarNota7PorConceito(dadosSQL.getBoolean("utilizarNota7PorConceito"));
		obj.setUtilizarNota8PorConceito(dadosSQL.getBoolean("utilizarNota8PorConceito"));
		obj.setUtilizarNota9PorConceito(dadosSQL.getBoolean("utilizarNota9PorConceito"));
		obj.setUtilizarNota10PorConceito(dadosSQL.getBoolean("utilizarNota10PorConceito"));
		obj.setUtilizarNota11PorConceito(dadosSQL.getBoolean("utilizarNota11PorConceito"));
		obj.setUtilizarNota12PorConceito(dadosSQL.getBoolean("utilizarNota12PorConceito"));
		obj.setUtilizarNota13PorConceito(dadosSQL.getBoolean("utilizarNota13PorConceito"));
		obj.setUtilizarComoSubstitutiva1(dadosSQL.getBoolean("utilizarComoSubstitutiva1"));
		obj.setUtilizarComoSubstitutiva2(dadosSQL.getBoolean("utilizarComoSubstitutiva2"));
		obj.setUtilizarComoSubstitutiva3(dadosSQL.getBoolean("utilizarComoSubstitutiva3"));
		obj.setUtilizarComoSubstitutiva4(dadosSQL.getBoolean("utilizarComoSubstitutiva4"));
		obj.setUtilizarComoSubstitutiva5(dadosSQL.getBoolean("utilizarComoSubstitutiva5"));
		obj.setUtilizarComoSubstitutiva6(dadosSQL.getBoolean("utilizarComoSubstitutiva6"));
		obj.setUtilizarComoSubstitutiva7(dadosSQL.getBoolean("utilizarComoSubstitutiva7"));
		obj.setUtilizarComoSubstitutiva8(dadosSQL.getBoolean("utilizarComoSubstitutiva8"));
		obj.setUtilizarComoSubstitutiva9(dadosSQL.getBoolean("utilizarComoSubstitutiva9"));
		obj.setUtilizarComoSubstitutiva10(dadosSQL.getBoolean("utilizarComoSubstitutiva10"));
		obj.setUtilizarComoSubstitutiva11(dadosSQL.getBoolean("utilizarComoSubstitutiva11"));
		obj.setUtilizarComoSubstitutiva12(dadosSQL.getBoolean("utilizarComoSubstitutiva12"));
		obj.setUtilizarComoSubstitutiva13(dadosSQL.getBoolean("utilizarComoSubstitutiva13"));
		obj.setPoliticaSubstitutiva1(dadosSQL.getString("politicaSubstitutiva1"));
		obj.setPoliticaSubstitutiva2(dadosSQL.getString("politicaSubstitutiva2"));
		obj.setPoliticaSubstitutiva3(dadosSQL.getString("politicaSubstitutiva3"));
		obj.setPoliticaSubstitutiva4(dadosSQL.getString("politicaSubstitutiva4"));
		obj.setPoliticaSubstitutiva5(dadosSQL.getString("politicaSubstitutiva5"));
		obj.setPoliticaSubstitutiva6(dadosSQL.getString("politicaSubstitutiva6"));
		obj.setPoliticaSubstitutiva7(dadosSQL.getString("politicaSubstitutiva7"));
		obj.setPoliticaSubstitutiva8(dadosSQL.getString("politicaSubstitutiva8"));
		obj.setPoliticaSubstitutiva9(dadosSQL.getString("politicaSubstitutiva9"));
		obj.setPoliticaSubstitutiva10(dadosSQL.getString("politicaSubstitutiva10"));
		obj.setPoliticaSubstitutiva11(dadosSQL.getString("politicaSubstitutiva11"));
		obj.setPoliticaSubstitutiva12(dadosSQL.getString("politicaSubstitutiva12"));
		obj.setPoliticaSubstitutiva13(dadosSQL.getString("politicaSubstitutiva13"));
		obj.setNotasDeCincoEmCincoDecimos(dadosSQL.getBoolean("notasDeCincoEmCincoDecimos"));
		obj.setNotasDeCincoEmCincoDecimosApenasMedia(dadosSQL.getBoolean("notasDeCincoEmCincoDecimosApenasMedia"));
		obj.setLimitarQtdeDiasMaxDownload(dadosSQL.getBoolean("limitarDiasDownload"));
		obj.setQtdeMaxDiasDownload(dadosSQL.getInt("qtdeDiasLimiteDownload"));
		obj.setUsarSituacaoAprovadoAproveitamentoTransferenciaGrade(dadosSQL.getBoolean("usarSituacaoAprovadoAproveitamentoTransferenciaGrade"));
		obj.setConsiderarCampoNuloNotaZerada(dadosSQL.getBoolean("considerarCampoNuloNotaZerada"));
		obj.setApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno(dadosSQL.getBoolean("apresentardisciplinasemaulaprogramadaminhasnotasvisaoaluno"));
		obj.setLiberarPreRequisitoDisciplinaConcomitancia(dadosSQL.getBoolean("liberarPreRequisitoDisciplinaConcomitancia"));
		obj.setApresentarTextoSemNotaCampoNuloHistorico(dadosSQL.getBoolean("apresentarTextoSemNotaCampoNuloHistorico"));
		obj.setTipoCalculoCargaHorariaFrequencia(TipoCalculoCargaHorariaFrequencia.valueOf(dadosSQL.getString("tipoCalculoCargaHorariaFrequencia")));
		obj.setPermiteRegistrarAulaFutura(dadosSQL.getBoolean("permiteRegistrarAulaFutura"));

		// **********NOTA14*********
		obj.setNota14(dadosSQL.getDouble("nota14"));
		obj.setUtilizarNota14(dadosSQL.getBoolean("utilizarNota14"));
		obj.setTituloNota14(dadosSQL.getString("tituloNota14"));
		obj.setFormulaCalculoNota14(dadosSQL.getString("formulaCalculoNota14"));
		obj.setFormulaUsoNota14(dadosSQL.getString("formulaUsoNota14"));
		obj.setNota14MediaFinal(dadosSQL.getBoolean("nota14MediaFinal"));
		obj.setApresentarNota14(dadosSQL.getBoolean("apresentarNota14"));
		obj.setPoliticaSubstitutiva14(dadosSQL.getString("politicaSubstitutiva14"));
		obj.setUtilizarNota14PorConceito(dadosSQL.getBoolean("utilizarNota14PorConceito"));
		obj.setUtilizarComoSubstitutiva14(dadosSQL.getBoolean("utilizarComoSubstitutiva14"));
		obj.setBimestreNota14(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota14")));
		// *********************
		// **********NOTA15*********
		obj.setNota15(dadosSQL.getDouble("nota15"));
		obj.setUtilizarNota15(dadosSQL.getBoolean("utilizarNota15"));
		obj.setTituloNota15(dadosSQL.getString("tituloNota15"));
		obj.setFormulaCalculoNota15(dadosSQL.getString("formulaCalculoNota15"));
		obj.setFormulaUsoNota15(dadosSQL.getString("formulaUsoNota15"));
		obj.setNota15MediaFinal(dadosSQL.getBoolean("nota15MediaFinal"));
		obj.setApresentarNota15(dadosSQL.getBoolean("apresentarNota15"));
		obj.setPoliticaSubstitutiva15(dadosSQL.getString("politicaSubstitutiva15"));
		obj.setUtilizarNota15PorConceito(dadosSQL.getBoolean("utilizarNota15PorConceito"));
		obj.setUtilizarComoSubstitutiva15(dadosSQL.getBoolean("utilizarComoSubstitutiva15"));
		obj.setBimestreNota15(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota15")));
		// *********************
		// **********NOTA16*********
		obj.setNota16(dadosSQL.getDouble("nota16"));
		obj.setUtilizarNota16(dadosSQL.getBoolean("utilizarNota16"));
		obj.setTituloNota16(dadosSQL.getString("tituloNota16"));
		obj.setFormulaCalculoNota16(dadosSQL.getString("formulaCalculoNota16"));
		obj.setFormulaUsoNota16(dadosSQL.getString("formulaUsoNota16"));
		obj.setNota16MediaFinal(dadosSQL.getBoolean("nota16MediaFinal"));
		obj.setApresentarNota16(dadosSQL.getBoolean("apresentarNota16"));
		obj.setPoliticaSubstitutiva16(dadosSQL.getString("politicaSubstitutiva16"));
		obj.setUtilizarNota16PorConceito(dadosSQL.getBoolean("utilizarNota16PorConceito"));
		obj.setUtilizarComoSubstitutiva16(dadosSQL.getBoolean("utilizarComoSubstitutiva16"));
		obj.setBimestreNota16(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota16")));
		// *********************
		// **********NOTA17*********
		obj.setNota17(dadosSQL.getDouble("nota17"));
		obj.setUtilizarNota17(dadosSQL.getBoolean("utilizarNota17"));
		obj.setTituloNota17(dadosSQL.getString("tituloNota17"));
		obj.setFormulaCalculoNota17(dadosSQL.getString("formulaCalculoNota17"));
		obj.setFormulaUsoNota17(dadosSQL.getString("formulaUsoNota17"));
		obj.setNota17MediaFinal(dadosSQL.getBoolean("nota17MediaFinal"));
		obj.setApresentarNota17(dadosSQL.getBoolean("apresentarNota17"));
		obj.setPoliticaSubstitutiva17(dadosSQL.getString("politicaSubstitutiva17"));
		obj.setUtilizarNota17PorConceito(dadosSQL.getBoolean("utilizarNota17PorConceito"));
		obj.setUtilizarComoSubstitutiva17(dadosSQL.getBoolean("utilizarComoSubstitutiva17"));
		obj.setBimestreNota17(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota17")));
		// *********************
		// **********NOTA18*********
		obj.setNota18(dadosSQL.getDouble("nota18"));
		obj.setUtilizarNota18(dadosSQL.getBoolean("utilizarNota18"));
		obj.setTituloNota18(dadosSQL.getString("tituloNota18"));
		obj.setFormulaCalculoNota18(dadosSQL.getString("formulaCalculoNota18"));
		obj.setFormulaUsoNota18(dadosSQL.getString("formulaUsoNota18"));
		obj.setNota18MediaFinal(dadosSQL.getBoolean("nota18MediaFinal"));
		obj.setApresentarNota18(dadosSQL.getBoolean("apresentarNota18"));
		obj.setPoliticaSubstitutiva18(dadosSQL.getString("politicaSubstitutiva18"));
		obj.setUtilizarNota18PorConceito(dadosSQL.getBoolean("utilizarNota18PorConceito"));
		obj.setUtilizarComoSubstitutiva18(dadosSQL.getBoolean("utilizarComoSubstitutiva18"));
		obj.setBimestreNota18(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota18")));
		// *********************
		// **********NOTA19*********
		obj.setNota19(dadosSQL.getDouble("nota19"));
		obj.setUtilizarNota19(dadosSQL.getBoolean("utilizarNota19"));
		obj.setTituloNota19(dadosSQL.getString("tituloNota19"));
		obj.setFormulaCalculoNota19(dadosSQL.getString("formulaCalculoNota19"));
		obj.setFormulaUsoNota19(dadosSQL.getString("formulaUsoNota19"));
		obj.setNota19MediaFinal(dadosSQL.getBoolean("nota19MediaFinal"));
		obj.setApresentarNota19(dadosSQL.getBoolean("apresentarNota19"));
		obj.setPoliticaSubstitutiva19(dadosSQL.getString("politicaSubstitutiva19"));
		obj.setUtilizarNota19PorConceito(dadosSQL.getBoolean("utilizarNota19PorConceito"));
		obj.setUtilizarComoSubstitutiva19(dadosSQL.getBoolean("utilizarComoSubstitutiva19"));
		obj.setBimestreNota19(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota19")));
		// *********************
		// **********NOTA20*********
		obj.setNota20(dadosSQL.getDouble("nota20"));
		obj.setUtilizarNota20(dadosSQL.getBoolean("utilizarNota20"));
		obj.setTituloNota20(dadosSQL.getString("tituloNota20"));
		obj.setFormulaCalculoNota20(dadosSQL.getString("formulaCalculoNota20"));
		obj.setFormulaUsoNota20(dadosSQL.getString("formulaUsoNota20"));
		obj.setNota20MediaFinal(dadosSQL.getBoolean("nota20MediaFinal"));
		obj.setApresentarNota20(dadosSQL.getBoolean("apresentarNota20"));
		obj.setPoliticaSubstitutiva20(dadosSQL.getString("politicaSubstitutiva20"));
		obj.setUtilizarNota20PorConceito(dadosSQL.getBoolean("utilizarNota20PorConceito"));
		obj.setUtilizarComoSubstitutiva20(dadosSQL.getBoolean("utilizarComoSubstitutiva20"));
		obj.setBimestreNota20(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota20")));
		// *********************
		// **********NOTA21*********
		obj.setNota21(dadosSQL.getDouble("nota21"));
		obj.setUtilizarNota21(dadosSQL.getBoolean("utilizarNota21"));
		obj.setTituloNota21(dadosSQL.getString("tituloNota21"));
		obj.setFormulaCalculoNota21(dadosSQL.getString("formulaCalculoNota21"));
		obj.setFormulaUsoNota21(dadosSQL.getString("formulaUsoNota21"));
		obj.setNota21MediaFinal(dadosSQL.getBoolean("nota21MediaFinal"));
		obj.setApresentarNota21(dadosSQL.getBoolean("apresentarNota21"));
		obj.setPoliticaSubstitutiva21(dadosSQL.getString("politicaSubstitutiva21"));
		obj.setUtilizarNota21PorConceito(dadosSQL.getBoolean("utilizarNota21PorConceito"));
		obj.setUtilizarComoSubstitutiva21(dadosSQL.getBoolean("utilizarComoSubstitutiva21"));
		obj.setBimestreNota21(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota21")));
		// *********************
		// **********NOTA22*********
		obj.setNota22(dadosSQL.getDouble("nota22"));
		obj.setUtilizarNota22(dadosSQL.getBoolean("utilizarNota22"));
		obj.setTituloNota22(dadosSQL.getString("tituloNota22"));
		obj.setFormulaCalculoNota22(dadosSQL.getString("formulaCalculoNota22"));
		obj.setFormulaUsoNota22(dadosSQL.getString("formulaUsoNota22"));
		obj.setNota22MediaFinal(dadosSQL.getBoolean("nota22MediaFinal"));
		obj.setApresentarNota22(dadosSQL.getBoolean("apresentarNota22"));
		obj.setPoliticaSubstitutiva22(dadosSQL.getString("politicaSubstitutiva22"));
		obj.setUtilizarNota22PorConceito(dadosSQL.getBoolean("utilizarNota22PorConceito"));
		obj.setUtilizarComoSubstitutiva22(dadosSQL.getBoolean("utilizarComoSubstitutiva22"));
		obj.setBimestreNota22(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota22")));
		// *********************
		// **********NOTA23*********
		obj.setNota23(dadosSQL.getDouble("nota23"));
		obj.setUtilizarNota23(dadosSQL.getBoolean("utilizarNota23"));
		obj.setTituloNota23(dadosSQL.getString("tituloNota23"));
		obj.setFormulaCalculoNota23(dadosSQL.getString("formulaCalculoNota23"));
		obj.setFormulaUsoNota23(dadosSQL.getString("formulaUsoNota23"));
		obj.setNota23MediaFinal(dadosSQL.getBoolean("nota23MediaFinal"));
		obj.setApresentarNota23(dadosSQL.getBoolean("apresentarNota23"));
		obj.setPoliticaSubstitutiva23(dadosSQL.getString("politicaSubstitutiva23"));
		obj.setUtilizarNota23PorConceito(dadosSQL.getBoolean("utilizarNota23PorConceito"));
		obj.setUtilizarComoSubstitutiva23(dadosSQL.getBoolean("utilizarComoSubstitutiva23"));
		obj.setBimestreNota23(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota23")));
		// *********************
		// **********NOTA24*********
		obj.setNota24(dadosSQL.getDouble("nota24"));
		obj.setUtilizarNota24(dadosSQL.getBoolean("utilizarNota24"));
		obj.setTituloNota24(dadosSQL.getString("tituloNota24"));
		obj.setFormulaCalculoNota24(dadosSQL.getString("formulaCalculoNota24"));
		obj.setFormulaUsoNota24(dadosSQL.getString("formulaUsoNota24"));
		obj.setNota24MediaFinal(dadosSQL.getBoolean("nota24MediaFinal"));
		obj.setApresentarNota24(dadosSQL.getBoolean("apresentarNota24"));
		obj.setPoliticaSubstitutiva24(dadosSQL.getString("politicaSubstitutiva24"));
		obj.setUtilizarNota24PorConceito(dadosSQL.getBoolean("utilizarNota24PorConceito"));
		obj.setUtilizarComoSubstitutiva24(dadosSQL.getBoolean("utilizarComoSubstitutiva24"));
		obj.setBimestreNota24(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota24")));
		// *********************
		// **********NOTA25*********
		obj.setNota25(dadosSQL.getDouble("nota25"));
		obj.setUtilizarNota25(dadosSQL.getBoolean("utilizarNota25"));
		obj.setTituloNota25(dadosSQL.getString("tituloNota25"));
		obj.setFormulaCalculoNota25(dadosSQL.getString("formulaCalculoNota25"));
		obj.setFormulaUsoNota25(dadosSQL.getString("formulaUsoNota25"));
		obj.setNota25MediaFinal(dadosSQL.getBoolean("nota25MediaFinal"));
		obj.setApresentarNota25(dadosSQL.getBoolean("apresentarNota25"));
		obj.setPoliticaSubstitutiva25(dadosSQL.getString("politicaSubstitutiva25"));
		obj.setUtilizarNota25PorConceito(dadosSQL.getBoolean("utilizarNota25PorConceito"));
		obj.setUtilizarComoSubstitutiva25(dadosSQL.getBoolean("utilizarComoSubstitutiva25"));
		obj.setBimestreNota25(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota25")));
		// *********************
		// **********NOTA26*********
		obj.setNota26(dadosSQL.getDouble("nota26"));
		obj.setUtilizarNota26(dadosSQL.getBoolean("utilizarNota26"));
		obj.setTituloNota26(dadosSQL.getString("tituloNota26"));
		obj.setFormulaCalculoNota26(dadosSQL.getString("formulaCalculoNota26"));
		obj.setFormulaUsoNota26(dadosSQL.getString("formulaUsoNota26"));
		obj.setNota26MediaFinal(dadosSQL.getBoolean("nota26MediaFinal"));
		obj.setApresentarNota26(dadosSQL.getBoolean("apresentarNota26"));
		obj.setPoliticaSubstitutiva26(dadosSQL.getString("politicaSubstitutiva26"));
		obj.setUtilizarNota26PorConceito(dadosSQL.getBoolean("utilizarNota26PorConceito"));
		obj.setUtilizarComoSubstitutiva26(dadosSQL.getBoolean("utilizarComoSubstitutiva26"));
		obj.setBimestreNota26(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota26")));
		// *********************
		// **********NOTA27*********
		obj.setNota27(dadosSQL.getDouble("nota27"));
		obj.setUtilizarNota27(dadosSQL.getBoolean("utilizarNota27"));
		obj.setTituloNota27(dadosSQL.getString("tituloNota27"));
		obj.setFormulaCalculoNota27(dadosSQL.getString("formulaCalculoNota27"));
		obj.setFormulaUsoNota27(dadosSQL.getString("formulaUsoNota27"));
		obj.setNota27MediaFinal(dadosSQL.getBoolean("nota27MediaFinal"));
		obj.setApresentarNota27(dadosSQL.getBoolean("apresentarNota27"));
		obj.setPoliticaSubstitutiva27(dadosSQL.getString("politicaSubstitutiva27"));
		obj.setUtilizarNota27PorConceito(dadosSQL.getBoolean("utilizarNota27PorConceito"));
		obj.setUtilizarComoSubstitutiva27(dadosSQL.getBoolean("utilizarComoSubstitutiva27"));
		obj.setBimestreNota27(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota27")));
		// *********************
		// **********NOTA28*********
		obj.setNota28(dadosSQL.getDouble("nota28"));
		obj.setUtilizarNota28(dadosSQL.getBoolean("utilizarNota28"));
		obj.setTituloNota28(dadosSQL.getString("tituloNota28"));
		obj.setFormulaCalculoNota28(dadosSQL.getString("formulaCalculoNota28"));
		obj.setFormulaUsoNota28(dadosSQL.getString("formulaUsoNota28"));
		obj.setNota28MediaFinal(dadosSQL.getBoolean("nota28MediaFinal"));
		obj.setApresentarNota28(dadosSQL.getBoolean("apresentarNota28"));
		obj.setPoliticaSubstitutiva28(dadosSQL.getString("politicaSubstitutiva28"));
		obj.setUtilizarNota28PorConceito(dadosSQL.getBoolean("utilizarNota28PorConceito"));
		obj.setUtilizarComoSubstitutiva28(dadosSQL.getBoolean("utilizarComoSubstitutiva28"));
		obj.setBimestreNota28(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota28")));
		// *********************
		// **********NOTA29*********
		obj.setNota29(dadosSQL.getDouble("nota29"));
		obj.setUtilizarNota29(dadosSQL.getBoolean("utilizarNota29"));
		obj.setTituloNota29(dadosSQL.getString("tituloNota29"));
		obj.setFormulaCalculoNota29(dadosSQL.getString("formulaCalculoNota29"));
		obj.setFormulaUsoNota29(dadosSQL.getString("formulaUsoNota29"));
		obj.setNota29MediaFinal(dadosSQL.getBoolean("nota29MediaFinal"));
		obj.setApresentarNota29(dadosSQL.getBoolean("apresentarNota29"));
		obj.setPoliticaSubstitutiva29(dadosSQL.getString("politicaSubstitutiva29"));
		obj.setUtilizarNota29PorConceito(dadosSQL.getBoolean("utilizarNota29PorConceito"));
		obj.setUtilizarComoSubstitutiva29(dadosSQL.getBoolean("utilizarComoSubstitutiva29"));
		obj.setBimestreNota29(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota29")));
		// *********************
		// **********NOTA30*********
		obj.setNota30(dadosSQL.getDouble("nota30"));
		obj.setUtilizarNota30(dadosSQL.getBoolean("utilizarNota30"));
		obj.setTituloNota30(dadosSQL.getString("tituloNota30"));
		obj.setFormulaCalculoNota30(dadosSQL.getString("formulaCalculoNota30"));
		obj.setFormulaUsoNota30(dadosSQL.getString("formulaUsoNota30"));
		obj.setNota30MediaFinal(dadosSQL.getBoolean("nota30MediaFinal"));
		obj.setApresentarNota30(dadosSQL.getBoolean("apresentarNota30"));
		obj.setPoliticaSubstitutiva30(dadosSQL.getString("politicaSubstitutiva30"));
		obj.setUtilizarNota30PorConceito(dadosSQL.getBoolean("utilizarNota30PorConceito"));
		obj.setUtilizarComoSubstitutiva30(dadosSQL.getBoolean("utilizarComoSubstitutiva30"));
		obj.setBimestreNota30(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota30")));
		// *********************
		// **********BIMESTRAIS 1 A 13 *********
		obj.setBimestreNota1(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota1")));
		obj.setBimestreNota2(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota2")));
		obj.setBimestreNota3(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota3")));
		obj.setBimestreNota4(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota4")));
		obj.setBimestreNota5(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota5")));
		obj.setBimestreNota6(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota6")));
		obj.setBimestreNota7(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota7")));
		obj.setBimestreNota8(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota8")));
		obj.setBimestreNota9(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota9")));
		obj.setBimestreNota10(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota10")));
		obj.setBimestreNota11(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota11")));
		obj.setBimestreNota12(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota12")));
		obj.setBimestreNota13(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota13")));

		obj.setTituloNotaApresentar1(dadosSQL.getString("tituloNotaApresentar1"));
		obj.setTituloNotaApresentar2(dadosSQL.getString("tituloNotaApresentar2"));
		obj.setTituloNotaApresentar3(dadosSQL.getString("tituloNotaApresentar3"));
		obj.setTituloNotaApresentar4(dadosSQL.getString("tituloNotaApresentar4"));
		obj.setTituloNotaApresentar5(dadosSQL.getString("tituloNotaApresentar5"));
		obj.setTituloNotaApresentar6(dadosSQL.getString("tituloNotaApresentar6"));
		obj.setTituloNotaApresentar7(dadosSQL.getString("tituloNotaApresentar7"));
		obj.setTituloNotaApresentar8(dadosSQL.getString("tituloNotaApresentar8"));
		obj.setTituloNotaApresentar9(dadosSQL.getString("tituloNotaApresentar9"));
		obj.setTituloNotaApresentar10(dadosSQL.getString("tituloNotaApresentar10"));
		obj.setTituloNotaApresentar11(dadosSQL.getString("tituloNotaApresentar11"));
		obj.setTituloNotaApresentar12(dadosSQL.getString("tituloNotaApresentar12"));
		obj.setTituloNotaApresentar13(dadosSQL.getString("tituloNotaApresentar13"));
		obj.setTituloNotaApresentar14(dadosSQL.getString("tituloNotaApresentar14"));
		obj.setTituloNotaApresentar15(dadosSQL.getString("tituloNotaApresentar15"));
		obj.setTituloNotaApresentar16(dadosSQL.getString("tituloNotaApresentar16"));
		obj.setTituloNotaApresentar17(dadosSQL.getString("tituloNotaApresentar17"));
		obj.setTituloNotaApresentar18(dadosSQL.getString("tituloNotaApresentar18"));
		obj.setTituloNotaApresentar19(dadosSQL.getString("tituloNotaApresentar19"));
		obj.setTituloNotaApresentar20(dadosSQL.getString("tituloNotaApresentar20"));
		obj.setTituloNotaApresentar21(dadosSQL.getString("tituloNotaApresentar21"));
		obj.setTituloNotaApresentar22(dadosSQL.getString("tituloNotaApresentar22"));
		obj.setTituloNotaApresentar23(dadosSQL.getString("tituloNotaApresentar23"));
		obj.setTituloNotaApresentar24(dadosSQL.getString("tituloNotaApresentar24"));
		obj.setTituloNotaApresentar25(dadosSQL.getString("tituloNotaApresentar25"));
		obj.setTituloNotaApresentar26(dadosSQL.getString("tituloNotaApresentar26"));
		obj.setTituloNotaApresentar27(dadosSQL.getString("tituloNotaApresentar27"));
		obj.setTituloNotaApresentar28(dadosSQL.getString("tituloNotaApresentar28"));
		obj.setTituloNotaApresentar29(dadosSQL.getString("tituloNotaApresentar29"));
		obj.setTituloNotaApresentar30(dadosSQL.getString("tituloNotaApresentar30"));
		
		obj.setTituloNotaApresentar31(dadosSQL.getString("tituloNotaApresentar31"));
		obj.setTituloNotaApresentar32(dadosSQL.getString("tituloNotaApresentar32"));
		obj.setTituloNotaApresentar33(dadosSQL.getString("tituloNotaApresentar33"));
		obj.setTituloNotaApresentar34(dadosSQL.getString("tituloNotaApresentar34"));
		obj.setTituloNotaApresentar35(dadosSQL.getString("tituloNotaApresentar35"));
		obj.setTituloNotaApresentar36(dadosSQL.getString("tituloNotaApresentar36"));
		obj.setTituloNotaApresentar37(dadosSQL.getString("tituloNotaApresentar37"));
		obj.setTituloNotaApresentar38(dadosSQL.getString("tituloNotaApresentar38"));
		obj.setTituloNotaApresentar39(dadosSQL.getString("tituloNotaApresentar39"));
		obj.setTituloNotaApresentar40(dadosSQL.getString("tituloNotaApresentar40"));

		obj.setControlarAvancoPeriodoPorCreditoOuCH(dadosSQL.getBoolean("controlarAvancoPeriodoPorCreditoOuCH"));
		obj.setTipoControleAvancoPeriodoPorCreditoOuCH(dadosSQL.getString("tipoControleAvancoPeriodoPorCreditoOuCH"));
		obj.setPercCumprirPeriodoAnteriorRenovarProximoPerLetivo(dadosSQL.getInt("percCumprirPeriodoAnteriorRenovarProximoPerLetivo"));
		obj.setPercCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo(dadosSQL.getInt("percCumprirPrimeiroPeriodoAtePenultimoPeriodoLetivo"));
		obj.setControlarInclusaoDisciplinaPorNrMaxCreditoOuCH(dadosSQL.getBoolean("controlarInclusaoDisciplinaPorNrMaxCreditoOuCH"));
		obj.setTipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH(dadosSQL.getString("tipoControleInclusaoDisciplinaPorNrMaxCreditoOuCH"));
		obj.setAcumularCreditosOuCHPeriodosAnterioresNaoCumpridos(dadosSQL.getBoolean("acumularCreditosOuCHPeriodosAnterioresNaoCumpridos"));
		obj.setPermitirInclusaoDiscipDependenciaComChoqueHorario(dadosSQL.getBoolean("permitirInclusaoDiscipDependenciaComChoqueHorario"));
		obj.setQtdPermitirInclusaoDiscipDependenciaComChoqueHorario(dadosSQL.getInt("qtdPermitirInclusaoDiscipDependenciaComChoqueHorario"));
		obj.setPermitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta(dadosSQL.getBoolean("permitirInclusaoComChoqueHorDiscipNaoReprovadasPorFalta"));

		obj.setFaixaNota1Maior(dadosSQL.getDouble("faixaNota1Maior"));
		obj.setFaixaNota1Menor(dadosSQL.getDouble("faixaNota1Menor"));
		obj.setFaixaNota2Maior(dadosSQL.getDouble("faixaNota2Maior"));
		obj.setFaixaNota2Menor(dadosSQL.getDouble("faixaNota2Menor"));
		obj.setFaixaNota3Maior(dadosSQL.getDouble("faixaNota3Maior"));
		obj.setFaixaNota3Menor(dadosSQL.getDouble("faixaNota3Menor"));
		obj.setFaixaNota4Maior(dadosSQL.getDouble("faixaNota4Maior"));
		obj.setFaixaNota4Menor(dadosSQL.getDouble("faixaNota4Menor"));
		obj.setFaixaNota5Maior(dadosSQL.getDouble("faixaNota5Maior"));
		obj.setFaixaNota5Menor(dadosSQL.getDouble("faixaNota5Menor"));
		obj.setFaixaNota6Maior(dadosSQL.getDouble("faixaNota6Maior"));
		obj.setFaixaNota6Menor(dadosSQL.getDouble("faixaNota6Menor"));
		obj.setFaixaNota7Maior(dadosSQL.getDouble("faixaNota7Maior"));
		obj.setFaixaNota7Menor(dadosSQL.getDouble("faixaNota7Menor"));
		obj.setFaixaNota8Maior(dadosSQL.getDouble("faixaNota8Maior"));
		obj.setFaixaNota8Menor(dadosSQL.getDouble("faixaNota8Menor"));
		obj.setFaixaNota9Maior(dadosSQL.getDouble("faixaNota9Maior"));
		obj.setFaixaNota9Menor(dadosSQL.getDouble("faixaNota9Menor"));
		obj.setFaixaNota10Maior(dadosSQL.getDouble("faixaNota10Maior"));
		obj.setFaixaNota10Menor(dadosSQL.getDouble("faixaNota10Menor"));
		obj.setFaixaNota11Maior(dadosSQL.getDouble("faixaNota11Maior"));
		obj.setFaixaNota11Menor(dadosSQL.getDouble("faixaNota11Menor"));
		obj.setFaixaNota12Maior(dadosSQL.getDouble("faixaNota12Maior"));
		obj.setFaixaNota12Menor(dadosSQL.getDouble("faixaNota12Menor"));
		obj.setFaixaNota13Maior(dadosSQL.getDouble("faixaNota13Maior"));
		obj.setFaixaNota13Menor(dadosSQL.getDouble("faixaNota13Menor"));
		obj.setFaixaNota14Maior(dadosSQL.getDouble("faixaNota14Maior"));
		obj.setFaixaNota14Menor(dadosSQL.getDouble("faixaNota14Menor"));
		obj.setFaixaNota15Maior(dadosSQL.getDouble("faixaNota15Maior"));
		obj.setFaixaNota15Menor(dadosSQL.getDouble("faixaNota15Menor"));
		obj.setFaixaNota16Maior(dadosSQL.getDouble("faixaNota16Maior"));
		obj.setFaixaNota16Menor(dadosSQL.getDouble("faixaNota16Menor"));
		obj.setFaixaNota17Maior(dadosSQL.getDouble("faixaNota17Maior"));
		obj.setFaixaNota17Menor(dadosSQL.getDouble("faixaNota17Menor"));
		obj.setFaixaNota18Maior(dadosSQL.getDouble("faixaNota18Maior"));
		obj.setFaixaNota18Menor(dadosSQL.getDouble("faixaNota18Menor"));
		obj.setFaixaNota19Maior(dadosSQL.getDouble("faixaNota19Maior"));
		obj.setFaixaNota19Menor(dadosSQL.getDouble("faixaNota19Menor"));
		obj.setFaixaNota20Maior(dadosSQL.getDouble("faixaNota20Maior"));
		obj.setFaixaNota20Menor(dadosSQL.getDouble("faixaNota20Menor"));
		obj.setFaixaNota21Maior(dadosSQL.getDouble("faixaNota21Maior"));
		obj.setFaixaNota21Menor(dadosSQL.getDouble("faixaNota21Menor"));
		obj.setFaixaNota22Maior(dadosSQL.getDouble("faixaNota22Maior"));
		obj.setFaixaNota22Menor(dadosSQL.getDouble("faixaNota22Menor"));
		obj.setFaixaNota23Maior(dadosSQL.getDouble("faixaNota23Maior"));
		obj.setFaixaNota23Menor(dadosSQL.getDouble("faixaNota23Menor"));
		obj.setFaixaNota24Maior(dadosSQL.getDouble("faixaNota24Maior"));
		obj.setFaixaNota24Menor(dadosSQL.getDouble("faixaNota24Menor"));
		obj.setFaixaNota25Maior(dadosSQL.getDouble("faixaNota25Maior"));
		obj.setFaixaNota25Menor(dadosSQL.getDouble("faixaNota25Menor"));
		obj.setFaixaNota26Maior(dadosSQL.getDouble("faixaNota26Maior"));
		obj.setFaixaNota26Menor(dadosSQL.getDouble("faixaNota26Menor"));
		obj.setFaixaNota27Maior(dadosSQL.getDouble("faixaNota27Maior"));
		obj.setFaixaNota27Menor(dadosSQL.getDouble("faixaNota27Menor"));
		obj.setFaixaNota28Maior(dadosSQL.getDouble("faixaNota28Maior"));
		obj.setFaixaNota28Menor(dadosSQL.getDouble("faixaNota28Menor"));
		obj.setFaixaNota29Maior(dadosSQL.getDouble("faixaNota29Maior"));
		obj.setFaixaNota29Menor(dadosSQL.getDouble("faixaNota29Menor"));
		obj.setFaixaNota30Maior(dadosSQL.getDouble("faixaNota30Maior"));
		obj.setFaixaNota30Menor(dadosSQL.getDouble("faixaNota30Menor"));
		
		obj.setFaixaNota31Maior(dadosSQL.getDouble("faixaNota31Maior"));
		obj.setFaixaNota31Menor(dadosSQL.getDouble("faixaNota31Menor"));
		
		obj.setFaixaNota32Maior(dadosSQL.getDouble("faixaNota32Maior"));
		obj.setFaixaNota32Menor(dadosSQL.getDouble("faixaNota32Menor"));
		
		obj.setFaixaNota33Maior(dadosSQL.getDouble("faixaNota33Maior"));
		obj.setFaixaNota33Menor(dadosSQL.getDouble("faixaNota33Menor"));
		
		obj.setFaixaNota34Maior(dadosSQL.getDouble("faixaNota34Maior"));
		obj.setFaixaNota34Menor(dadosSQL.getDouble("faixaNota34Menor"));
		
		obj.setFaixaNota35Maior(dadosSQL.getDouble("faixaNota35Maior"));
		obj.setFaixaNota35Menor(dadosSQL.getDouble("faixaNota35Menor"));
		
		obj.setFaixaNota36Maior(dadosSQL.getDouble("faixaNota36Maior"));
		obj.setFaixaNota36Menor(dadosSQL.getDouble("faixaNota36Menor"));
		
		obj.setFaixaNota37Maior(dadosSQL.getDouble("faixaNota37Maior"));
		obj.setFaixaNota37Menor(dadosSQL.getDouble("faixaNota37Menor"));
		
		obj.setFaixaNota38Maior(dadosSQL.getDouble("faixaNota38Maior"));
		obj.setFaixaNota38Menor(dadosSQL.getDouble("faixaNota38Menor"));
		
		obj.setFaixaNota39Maior(dadosSQL.getDouble("faixaNota39Maior"));
		obj.setFaixaNota39Menor(dadosSQL.getDouble("faixaNota39Menor"));
		
		obj.setFaixaNota40Maior(dadosSQL.getDouble("faixaNota40Maior"));
		obj.setFaixaNota40Menor(dadosSQL.getDouble("faixaNota40Menor"));
		
		obj.setQuantidadeCasasDecimaisPermitirAposVirgula(dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula"));

		obj.setApresentarSiglaConcessaoCredito(dadosSQL.getBoolean("apresentarSiglaConcessaoCredito"));
		obj.setTipoApresentarFrequenciaVisaoAluno(dadosSQL.getString("tipoApresentarFrequenciaVisaoAluno"));
		obj.setRegraArredondamentoNota1(dadosSQL.getString("regraArredondamentoNota1"));
		obj.setRegraArredondamentoNota2(dadosSQL.getString("regraArredondamentoNota2"));
		obj.setRegraArredondamentoNota3(dadosSQL.getString("regraArredondamentoNota3"));
		obj.setRegraArredondamentoNota4(dadosSQL.getString("regraArredondamentoNota4"));
		obj.setRegraArredondamentoNota5(dadosSQL.getString("regraArredondamentoNota5"));
		obj.setRegraArredondamentoNota6(dadosSQL.getString("regraArredondamentoNota6"));
		obj.setRegraArredondamentoNota7(dadosSQL.getString("regraArredondamentoNota7"));
		obj.setRegraArredondamentoNota8(dadosSQL.getString("regraArredondamentoNota8"));
		obj.setRegraArredondamentoNota9(dadosSQL.getString("regraArredondamentoNota9"));
		obj.setRegraArredondamentoNota10(dadosSQL.getString("regraArredondamentoNota10"));
		obj.setRegraArredondamentoNota11(dadosSQL.getString("regraArredondamentoNota11"));
		obj.setRegraArredondamentoNota12(dadosSQL.getString("regraArredondamentoNota12"));
		obj.setRegraArredondamentoNota13(dadosSQL.getString("regraArredondamentoNota13"));
		obj.setRegraArredondamentoNota14(dadosSQL.getString("regraArredondamentoNota14"));
		obj.setRegraArredondamentoNota15(dadosSQL.getString("regraArredondamentoNota15"));
		obj.setRegraArredondamentoNota16(dadosSQL.getString("regraArredondamentoNota16"));
		obj.setRegraArredondamentoNota17(dadosSQL.getString("regraArredondamentoNota17"));
		obj.setRegraArredondamentoNota18(dadosSQL.getString("regraArredondamentoNota18"));
		obj.setRegraArredondamentoNota19(dadosSQL.getString("regraArredondamentoNota19"));
		obj.setRegraArredondamentoNota20(dadosSQL.getString("regraArredondamentoNota20"));
		obj.setRegraArredondamentoNota21(dadosSQL.getString("regraArredondamentoNota21"));
		obj.setRegraArredondamentoNota22(dadosSQL.getString("regraArredondamentoNota22"));
		obj.setRegraArredondamentoNota23(dadosSQL.getString("regraArredondamentoNota23"));
		obj.setRegraArredondamentoNota24(dadosSQL.getString("regraArredondamentoNota24"));
		obj.setRegraArredondamentoNota25(dadosSQL.getString("regraArredondamentoNota25"));
		obj.setRegraArredondamentoNota26(dadosSQL.getString("regraArredondamentoNota26"));
		obj.setRegraArredondamentoNota27(dadosSQL.getString("regraArredondamentoNota27"));
		obj.setRegraArredondamentoNota28(dadosSQL.getString("regraArredondamentoNota28"));
		obj.setRegraArredondamentoNota29(dadosSQL.getString("regraArredondamentoNota29"));
		obj.setRegraArredondamentoNota30(dadosSQL.getString("regraArredondamentoNota30"));
		
		obj.setRegraArredondamentoNota31(dadosSQL.getString("regraArredondamentoNota31"));
		obj.setRegraArredondamentoNota32(dadosSQL.getString("regraArredondamentoNota32"));
		obj.setRegraArredondamentoNota33(dadosSQL.getString("regraArredondamentoNota33"));
		obj.setRegraArredondamentoNota34(dadosSQL.getString("regraArredondamentoNota34"));
		obj.setRegraArredondamentoNota35(dadosSQL.getString("regraArredondamentoNota35"));
		obj.setRegraArredondamentoNota36(dadosSQL.getString("regraArredondamentoNota36"));
		obj.setRegraArredondamentoNota37(dadosSQL.getString("regraArredondamentoNota37"));
		obj.setRegraArredondamentoNota38(dadosSQL.getString("regraArredondamentoNota38"));
		obj.setRegraArredondamentoNota39(dadosSQL.getString("regraArredondamentoNota39"));
		obj.setRegraArredondamentoNota40(dadosSQL.getString("regraArredondamentoNota40"));
		
		obj.setQuantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula(dadosSQL.getInt("quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula"));
		
		obj.setUtilizarApoioEADParaDisciplinasModalidadePresencial(dadosSQL.getBoolean("utilizarApoioEADParaDisciplinasModalidadePresencial"));
		getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorConfiguracaoAcademico(obj);
		getFacadeFactory().getConfiguracaoAcademicoNotaFacade().carregarDadosConfiguracaoAcademicoNotaVOs(obj);
		montarDadosConfiguracoes(obj, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		obj.setApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico(dadosSQL.getBoolean("apresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico"));
		obj.setObrigaInformarFormaIngressoMatricula(dadosSQL.getBoolean("obrigaInformarFormaIngressoMatricula"));
		obj.setObrigaInformarOrigemFormaIngressoMatricula(dadosSQL.getBoolean("obrigaInformarOrigemFormaIngressoMatricula"));
		obj.setBloquearRegistroAulaAnteriorDataMatricula(dadosSQL.getBoolean("bloquearRegistroAulaAnteriorDataMatricula"));
		obj.setOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao(dadosSQL.getBoolean("ocultarSituacaoHistoricoDisciplinaQueFazParteComposicao"));
		obj.setSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal(dadosSQL.getBoolean("situacaoDiscQueFazParteComposicaoControladaDiscPrincipal"));
		obj.setCalcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes(dadosSQL.getBoolean("calcularMediaFinalDisciplinaCompostaAposCalculoTodasComposicoes"));
		obj.setHabilitarControleInclusaoDisciplinaPeriodoFuturo(dadosSQL.getBoolean("habilitarControleInclusaoDisciplinaPeriodoFuturo"));
		obj.setBloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(dadosSQL.getBoolean("bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular"));
		obj.setBloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(dadosSQL.getBoolean("bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular"));
		obj.setHabilitarControleInclusaoObrigatoriaDisciplinaDependencia(dadosSQL.getBoolean("habilitarControleInclusaoObrigatoriaDisciplinaDependencia"));
		obj.setRemoverAutomaticamenteDisciplinaSemVagaRenovacaoOnline(dadosSQL.getBoolean("removerAutomaticamenteDisciplinaSemVagaRenovacaoOnline"));
		obj.setHabilitarDistribuicaoTurmaPraticaTeoricaRenovacao(dadosSQL.getBoolean("habilitarDistribuicaoTurmaPraticaTeoricaRenovacao"));
		obj.setHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares(dadosSQL.getBoolean("habilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares"));
		obj.setAlunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo(dadosSQL.getBoolean("alunoApenasComDisciplinasDependenciaRealizarOperacaoForaDoPrazo"));
		obj.setHabilitarDistribuicaoDisciplinaDependenciaAutomatica(dadosSQL.getBoolean("habilitarDistribuicaoDisciplinaDependenciaAutomatica"));
		obj.setDistribuirTurmaPraticaTeoricaComAulaProgramada(dadosSQL.getBoolean("distribuirTurmaPraticaTeoricaComAulaProgramada"));
		obj.setNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina(dadosSQL.getInt("numeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina"));
		obj.setPorcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia(dadosSQL.getInt("porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia"));
		obj.setRemoverDisciplinaTurmaPraticaTeoricaComChoqueHorario(dadosSQL.getBoolean("removerDisciplinaTurmaPraticaTeoricaComChoqueHorario"));
		obj.setConsiderarRegularAlunoDependenciaOptativa(dadosSQL.getBoolean("considerarRegularAlunoDependenciaOptativa"));
		obj.setConsiderarPortadoDiplomaTransEntradaAlunoIrregular(dadosSQL.getBoolean("considerarPortadoDiplomaTransEntradaAlunoIrregular"));
		obj.setMatricularApenasDisciplinaAulaProgramada(dadosSQL.getBoolean("matricularApenasDisciplinaAulaProgramada"));
		
//		A PARTIR NOTA 31
		
		obj.setNota31(dadosSQL.getDouble("nota31"));
		obj.setUtilizarNota31(dadosSQL.getBoolean("utilizarNota31")); 
	 	obj.setTituloNota31(dadosSQL.getString("tituloNota31"));
		obj.setFormulaCalculoNota31(dadosSQL.getString("formulaCalculoNota31"));
		obj.setFormulaUsoNota31(dadosSQL.getString("formulaUsoNota31"));
		obj.setNota31MediaFinal(dadosSQL.getBoolean("nota31MediaFinal")); 
		obj.setApresentarNota31(dadosSQL.getBoolean("apresentarNota31")); 
		obj.setPoliticaSubstitutiva31(dadosSQL.getString("politicaSubstitutiva31"));  
		obj.setUtilizarNota31PorConceito(dadosSQL.getBoolean("utilizarNota31PorConceito")); 
		obj.setUtilizarComoSubstitutiva31(dadosSQL.getBoolean("utilizarComoSubstitutiva31"));  
		obj.setBimestreNota31(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota31"))); 
		
		obj.setNota32(dadosSQL.getDouble("nota32"));
		obj.setUtilizarNota32(dadosSQL.getBoolean("utilizarNota32")); 
	 	obj.setTituloNota32(dadosSQL.getString("tituloNota32"));
		obj.setFormulaCalculoNota32(dadosSQL.getString("formulaCalculoNota32"));
		obj.setFormulaUsoNota32(dadosSQL.getString("formulaUsoNota32"));
		obj.setNota32MediaFinal(dadosSQL.getBoolean("nota32MediaFinal")); 
		obj.setApresentarNota32(dadosSQL.getBoolean("apresentarNota32")); 
		obj.setPoliticaSubstitutiva32(dadosSQL.getString("politicaSubstitutiva32"));  
		obj.setUtilizarNota32PorConceito(dadosSQL.getBoolean("utilizarNota32PorConceito")); 
		obj.setUtilizarComoSubstitutiva32(dadosSQL.getBoolean("utilizarComoSubstitutiva32"));  
		obj.setBimestreNota32(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota32"))); 
		
		obj.setNota33(dadosSQL.getDouble("nota33"));
		obj.setUtilizarNota33(dadosSQL.getBoolean("utilizarNota33")); 
	 	obj.setTituloNota33(dadosSQL.getString("tituloNota33"));
		obj.setFormulaCalculoNota33(dadosSQL.getString("formulaCalculoNota33"));
		obj.setFormulaUsoNota33(dadosSQL.getString("formulaUsoNota33"));
		obj.setNota33MediaFinal(dadosSQL.getBoolean("nota33MediaFinal")); 
		obj.setApresentarNota33(dadosSQL.getBoolean("apresentarNota33")); 
		obj.setPoliticaSubstitutiva33(dadosSQL.getString("politicaSubstitutiva33"));  
		obj.setUtilizarNota33PorConceito(dadosSQL.getBoolean("utilizarNota33PorConceito")); 
		obj.setUtilizarComoSubstitutiva33(dadosSQL.getBoolean("utilizarComoSubstitutiva33"));  
		obj.setBimestreNota33(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota33"))); 
		
		obj.setNota34(dadosSQL.getDouble("nota34"));
		obj.setUtilizarNota34(dadosSQL.getBoolean("utilizarNota34")); 
	 	obj.setTituloNota34(dadosSQL.getString("tituloNota34"));
		obj.setFormulaCalculoNota34(dadosSQL.getString("formulaCalculoNota34"));
		obj.setFormulaUsoNota34(dadosSQL.getString("formulaUsoNota34"));
		obj.setNota34MediaFinal(dadosSQL.getBoolean("nota34MediaFinal")); 
		obj.setApresentarNota34(dadosSQL.getBoolean("apresentarNota34")); 
		obj.setPoliticaSubstitutiva34(dadosSQL.getString("politicaSubstitutiva34"));  
		obj.setUtilizarNota34PorConceito(dadosSQL.getBoolean("utilizarNota34PorConceito")); 
		obj.setUtilizarComoSubstitutiva34(dadosSQL.getBoolean("utilizarComoSubstitutiva34"));  
		obj.setBimestreNota34(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota34"))); 
		
		obj.setNota35(dadosSQL.getDouble("nota35"));
		obj.setUtilizarNota35(dadosSQL.getBoolean("utilizarNota35")); 
	 	obj.setTituloNota35(dadosSQL.getString("tituloNota35"));
		obj.setFormulaCalculoNota35(dadosSQL.getString("formulaCalculoNota35"));
		obj.setFormulaUsoNota35(dadosSQL.getString("formulaUsoNota35"));
		obj.setNota35MediaFinal(dadosSQL.getBoolean("nota35MediaFinal")); 
		obj.setApresentarNota35(dadosSQL.getBoolean("apresentarNota35")); 
		obj.setPoliticaSubstitutiva35(dadosSQL.getString("politicaSubstitutiva35"));  
		obj.setUtilizarNota35PorConceito(dadosSQL.getBoolean("utilizarNota35PorConceito")); 
		obj.setUtilizarComoSubstitutiva35(dadosSQL.getBoolean("utilizarComoSubstitutiva35"));  
		obj.setBimestreNota35(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota35"))); 					
		
		obj.setNota36(dadosSQL.getDouble("nota36"));
		obj.setUtilizarNota36(dadosSQL.getBoolean("utilizarNota36")); 
	 	obj.setTituloNota36(dadosSQL.getString("tituloNota36"));
		obj.setFormulaCalculoNota36(dadosSQL.getString("formulaCalculoNota36"));
		obj.setFormulaUsoNota36(dadosSQL.getString("formulaUsoNota36"));
		obj.setNota36MediaFinal(dadosSQL.getBoolean("nota36MediaFinal")); 
		obj.setApresentarNota36(dadosSQL.getBoolean("apresentarNota36")); 
		obj.setPoliticaSubstitutiva36(dadosSQL.getString("politicaSubstitutiva36"));  
		obj.setUtilizarNota36PorConceito(dadosSQL.getBoolean("utilizarNota36PorConceito")); 
		obj.setUtilizarComoSubstitutiva36(dadosSQL.getBoolean("utilizarComoSubstitutiva36"));  
		obj.setBimestreNota36(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota36"))); 
		
		obj.setNota37(dadosSQL.getDouble("nota37"));
		obj.setUtilizarNota37(dadosSQL.getBoolean("utilizarNota37")); 
	 	obj.setTituloNota37(dadosSQL.getString("tituloNota37"));
		obj.setFormulaCalculoNota37(dadosSQL.getString("formulaCalculoNota37"));
		obj.setFormulaUsoNota37(dadosSQL.getString("formulaUsoNota37"));
		obj.setNota37MediaFinal(dadosSQL.getBoolean("nota37MediaFinal")); 
		obj.setApresentarNota37(dadosSQL.getBoolean("apresentarNota37")); 
		obj.setPoliticaSubstitutiva37(dadosSQL.getString("politicaSubstitutiva37"));  
		obj.setUtilizarNota37PorConceito(dadosSQL.getBoolean("utilizarNota37PorConceito")); 
		obj.setUtilizarComoSubstitutiva37(dadosSQL.getBoolean("utilizarComoSubstitutiva37"));  
		obj.setBimestreNota37(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota37"))); 
		
		obj.setNota38(dadosSQL.getDouble("nota38"));
		obj.setUtilizarNota38(dadosSQL.getBoolean("utilizarNota38")); 
	 	obj.setTituloNota38(dadosSQL.getString("tituloNota38"));
		obj.setFormulaCalculoNota38(dadosSQL.getString("formulaCalculoNota38"));
		obj.setFormulaUsoNota38(dadosSQL.getString("formulaUsoNota38"));
		obj.setNota38MediaFinal(dadosSQL.getBoolean("nota38MediaFinal")); 
		obj.setApresentarNota38(dadosSQL.getBoolean("apresentarNota38")); 
		obj.setPoliticaSubstitutiva38(dadosSQL.getString("politicaSubstitutiva38"));  
		obj.setUtilizarNota38PorConceito(dadosSQL.getBoolean("utilizarNota38PorConceito")); 
		obj.setUtilizarComoSubstitutiva38(dadosSQL.getBoolean("utilizarComoSubstitutiva38"));  
		obj.setBimestreNota38(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota38"))); 
		
		obj.setNota39(dadosSQL.getDouble("nota39"));
		obj.setUtilizarNota39(dadosSQL.getBoolean("utilizarNota39")); 
	 	obj.setTituloNota39(dadosSQL.getString("tituloNota39"));
		obj.setFormulaCalculoNota39(dadosSQL.getString("formulaCalculoNota39"));
		obj.setFormulaUsoNota39(dadosSQL.getString("formulaUsoNota39"));
		obj.setNota39MediaFinal(dadosSQL.getBoolean("nota39MediaFinal")); 
		obj.setApresentarNota39(dadosSQL.getBoolean("apresentarNota39")); 
		obj.setPoliticaSubstitutiva39(dadosSQL.getString("politicaSubstitutiva39"));  
		obj.setUtilizarNota39PorConceito(dadosSQL.getBoolean("utilizarNota39PorConceito")); 
		obj.setUtilizarComoSubstitutiva39(dadosSQL.getBoolean("utilizarComoSubstitutiva39"));  
		obj.setBimestreNota39(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota39"))); 
		
		obj.setNota40(dadosSQL.getDouble("nota40"));
		obj.setUtilizarNota40(dadosSQL.getBoolean("utilizarNota40")); 
	 	obj.setTituloNota40(dadosSQL.getString("tituloNota40"));
		obj.setFormulaCalculoNota40(dadosSQL.getString("formulaCalculoNota40"));
		obj.setFormulaUsoNota40(dadosSQL.getString("formulaUsoNota40"));
		obj.setNota40MediaFinal(dadosSQL.getBoolean("nota40MediaFinal")); 
		obj.setApresentarNota40(dadosSQL.getBoolean("apresentarNota40")); 
		obj.setPoliticaSubstitutiva40(dadosSQL.getString("politicaSubstitutiva40"));  
		obj.setUtilizarNota40PorConceito(dadosSQL.getBoolean("utilizarNota40PorConceito")); 
		obj.setUtilizarComoSubstitutiva40(dadosSQL.getBoolean("utilizarComoSubstitutiva40"));  
		obj.setBimestreNota40(BimestreEnum.valueOf(dadosSQL.getString("bimestreNota40"))); 
		
		obj.setTituloNotaApresentar31(dadosSQL.getString("tituloNotaApresentar31"));
		obj.setTituloNotaApresentar32(dadosSQL.getString("tituloNotaApresentar32"));
		obj.setTituloNotaApresentar33(dadosSQL.getString("tituloNotaApresentar33"));
		obj.setTituloNotaApresentar34(dadosSQL.getString("tituloNotaApresentar34"));
		obj.setTituloNotaApresentar35(dadosSQL.getString("tituloNotaApresentar35"));
		obj.setTituloNotaApresentar36(dadosSQL.getString("tituloNotaApresentar36"));
		obj.setTituloNotaApresentar37(dadosSQL.getString("tituloNotaApresentar37"));
		obj.setTituloNotaApresentar38(dadosSQL.getString("tituloNotaApresentar38"));
		obj.setTituloNotaApresentar39(dadosSQL.getString("tituloNotaApresentar39"));
		obj.setTituloNotaApresentar40(dadosSQL.getString("tituloNotaApresentar40"));
		
		obj.setFaixaNota31Maior(dadosSQL.getDouble("faixaNota31Maior"));
		obj.setFaixaNota31Menor(dadosSQL.getDouble("faixaNota31Menor"));
		obj.setFaixaNota32Maior(dadosSQL.getDouble("faixaNota32Maior"));
		obj.setFaixaNota32Menor(dadosSQL.getDouble("faixaNota32Menor"));
		obj.setFaixaNota33Maior(dadosSQL.getDouble("faixaNota33Maior"));
		obj.setFaixaNota33Menor(dadosSQL.getDouble("faixaNota33Menor"));
		obj.setFaixaNota34Maior(dadosSQL.getDouble("faixaNota34Maior"));
		obj.setFaixaNota34Menor(dadosSQL.getDouble("faixaNota34Menor"));
		obj.setFaixaNota35Maior(dadosSQL.getDouble("faixaNota35Maior"));
		obj.setFaixaNota35Menor(dadosSQL.getDouble("faixaNota35Menor"));
		obj.setFaixaNota36Maior(dadosSQL.getDouble("faixaNota36Maior"));
		obj.setFaixaNota36Menor(dadosSQL.getDouble("faixaNota36Menor"));
		obj.setFaixaNota37Maior(dadosSQL.getDouble("faixaNota37Maior"));
		obj.setFaixaNota37Menor(dadosSQL.getDouble("faixaNota37Menor"));
		obj.setFaixaNota38Maior(dadosSQL.getDouble("faixaNota38Maior"));
		obj.setFaixaNota38Menor(dadosSQL.getDouble("faixaNota38Menor"));
		obj.setFaixaNota39Maior(dadosSQL.getDouble("faixaNota39Maior"));
		obj.setFaixaNota39Menor(dadosSQL.getDouble("faixaNota39Menor"));
		obj.setFaixaNota40Maior(dadosSQL.getDouble("faixaNota40Maior"));
		obj.setFaixaNota40Menor(dadosSQL.getDouble("faixaNota40Menor"));
		
		obj.setRegraArredondamentoNota31(dadosSQL.getString("regraArredondamentoNota31"));
		obj.setRegraArredondamentoNota32(dadosSQL.getString("regraArredondamentoNota32"));
		obj.setRegraArredondamentoNota33(dadosSQL.getString("regraArredondamentoNota33"));
		obj.setRegraArredondamentoNota34(dadosSQL.getString("regraArredondamentoNota34"));
		obj.setRegraArredondamentoNota35(dadosSQL.getString("regraArredondamentoNota35"));
		obj.setRegraArredondamentoNota36(dadosSQL.getString("regraArredondamentoNota36"));
		obj.setRegraArredondamentoNota37(dadosSQL.getString("regraArredondamentoNota37"));
		obj.setRegraArredondamentoNota38(dadosSQL.getString("regraArredondamentoNota38"));
		obj.setRegraArredondamentoNota39(dadosSQL.getString("regraArredondamentoNota39"));
		obj.setRegraArredondamentoNota40(dadosSQL.getString("regraArredondamentoNota40"));
		
		obj.setQuantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula(dadosSQL.getInt("quantidadeMaximaPeriodoTrancadoPermiteReativacaoMatricula"));
		obj.setIncluirAutomaticamenteDisciplinaGrupoOptativa(dadosSQL.getBoolean("incluirAutomaticamenteDisciplinaGrupoOptativa"));
		obj.setBloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa(dadosSQL.getBoolean("bloquearRenovAlunoRegSemIncluirQtdeExigidaDiscGrupoOptativa"));
		obj.setPermitirAlunoRegularIncluirDisciplinaGrupoOptativa(dadosSQL.getBoolean("permitirAlunoRegularIncluirDisciplinaGrupoOptativa"));
		obj.setConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores(dadosSQL.getBoolean("considerarDisciplinasReprovadasPeriodosLetivosAnteriores"));
		obj.setCriarDigitoMascaraMatricula(dadosSQL.getBoolean("criarDigitoMascaraMatricula"));
		obj.setFormulaCriarDigitoMascaraMatricula(dadosSQL.getString("formulaCriarDigitoMascaraMatricula"));
		obj.setOcultarFrequenciaDisciplinaComposta(dadosSQL.getBoolean("ocultarFrequenciaDisciplinaComposta"));
		obj.setReprovarFaltaDisciplinaCompostaCasoReprovadoFaltaDisciplinaFilha(dadosSQL.getBoolean("reprovarFaltaDiscCompostaCasoReprovadoFaltaDiscFilha"));
		obj.setTipoUsoConfiguracaoAcademico(TipoUsoConfiguracaoAcademicoEnum.valueOf(dadosSQL.getString("tipoUsoConfiguracaoAcademico")));
		obj.setRegraCalculoDisciplinaComposta(RegraCalculoDisciplinaCompostaEnum.valueOf(dadosSQL.getString("regraCalculoDisciplinaComposta")));
		obj.setOcultarMediaFinalDisciplinaCasoReprovado(dadosSQL.getBoolean("ocultarMediaFinalDisciplinaCasoReprovado"));
		obj.setValidarChoqueHorarioOutraMatriculaAluno(dadosSQL.getBoolean("validarChoqueHorarioOutraMatriculaAluno"));
		obj.setValidarDadosEnadeCensoMatricularAluno(dadosSQL.getBoolean("validardadosenadecensomatricularaluno"));
		obj.setUsarFormulaCalculoFrequencia(dadosSQL.getBoolean("usarFormulaCalculoFrequencia"));
		obj.setFormulaCalculoFrequencia(dadosSQL.getString("formulaCalculoFrequencia"));
		obj.setFormulaCoeficienteRendimento(dadosSQL.getString("formulaCoeficienteRendimento"));
	    obj.setMascaraNumeroProcessoExpedicaoDiploma(dadosSQL.getString("mascaraNumeroProcessoExpedicaoDiploma"));
	    obj.setMascaraNumeroRegistroDiploma(dadosSQL.getString("mascaraNumeroRegistroDiploma"));
	    obj.setCasasDecimaisCoeficienteRendimento(dadosSQL.getInt("casasDecimaisCoeficienteRendimento"));
	    obj.setPermitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual(dadosSQL.getBoolean("permitirAlunoIrregularRemoverDisciplinaPeriodoLetivoAtual"));
	    obj.setRegistrarComoFaltaAulasRealizadasAposDataMatricula(dadosSQL.getBoolean("registrarComoFaltaAulasRealizadasAposDataMatricula"));
	    obj.setPermitirAproveitamentoDisciplinasOptativas(dadosSQL.getBoolean("permitiraproveitamentodisciplinasoptativas"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public  void montarDadosConfiguracoes(ConfiguracaoAcademicoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
			obj.setConfiguracoesVO(new ConfiguracoesVO());
			return;
		}
		obj.setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(obj.getConfiguracoesVO().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ConfiguracaoAcademicoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ConfiguracaoAcademicoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().carregarDadosConfiguracaoAcademica(codigoPrm);		
	}
	
	@Override
	public ConfiguracaoAcademicoVO consultarPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception {		
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConfiguracaoAcademico WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new Exception("Dados Não Encontrados (Configuração Acadêmico).");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public ConfiguracaoAcademicoVO consultarPorDisciplinaMatriculaPeriodoAlunoVinculadoGradeDisciplina(Integer disciplina, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct configuracaoacademico.* from configuracaoacademico ");
		sb.append(" inner join gradedisciplina on gradedisciplina.configuracaoAcademico = configuracaoAcademico.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.periodoletivomatricula = gradedisciplina.periodoletivo ");
		sb.append(" where gradedisciplina.disciplina = ").append(disciplina);
		sb.append(" and matriculaperiodo.codigo = ").append(matriculaPeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoAcademicoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public ConfiguracaoAcademicoVO consultarPorCodigoCurso(Integer codigoCurso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct configuracaoacademico.* from configuracaoacademico ");
		sb.append(" inner join curso on curso.configuracaoacademico = configuracaoacademico.codigo ");
		sb.append(" where curso.codigo = ").append(codigoCurso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoAcademicoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public Boolean consultarPorMatriculaCurso(String matricula, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select configuracaoacademico.usarsituacaoaprovadoaproveitamentotransferenciagrade from configuracaoacademico ");
		sb.append(" inner join curso on curso.configuracaoacademico = configuracaoacademico.codigo ");
		sb.append(" inner join matricula on matricula.curso = curso.codigo ");
		sb.append(" where matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("usarsituacaoaprovadoaproveitamentotransferenciagrade");
		}
		return Boolean.FALSE;
	}

	public Boolean consultarPermissaoBloquearRegistroAulaAnteriorDataMatricula(Integer codigo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select bloquearRegistroAulaAnteriorDataMatricula from configuracaoacademico  where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("bloquearRegistroAulaAnteriorDataMatricula");
		}
		return Boolean.FALSE;
	}

	public Boolean consultarPermissaoCursarDisciplinaPreRequisito(Integer codigo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select cursardisciplinaeprerequisito from configuracaoacademico  where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("cursardisciplinaeprerequisito");
		}
		return Boolean.FALSE;
	}	

	public Boolean consultarPermissaoPreRequisitoDisciplinaConcomitante(Integer codigo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select liberarPreRequisitoDisciplinaConcomitancia from configuracaoacademico  where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("liberarPreRequisitoDisciplinaConcomitancia");
		}
		return Boolean.FALSE;
	}
	
	public Integer verificarPeriodoReativacaoMatricula(Integer maximoDias, Date dataTrancamento, Date dataReativacao, UsuarioVO usuario) {
		Integer dias = 0;
		dias = maximoDias - Uteis.getIntervaloEntreDatas(dataTrancamento, dataReativacao);
		return dias;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoAcademico.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoAcademico.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirConfiguracaoAcademico(List listaObjetos, Integer configuracoes) {
		String sql = "DELETE FROM configuracaoAcademico WHERE (configuracoes = ?)";
		Iterator i = listaObjetos.iterator();
		while (i.hasNext()) {
			ConfiguracaoAcademicoVO obj = (ConfiguracaoAcademicoVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and (codigo != " + obj.getCodigo() + ") ";
			}
		}
		getConexao().getJdbcTemplate().update(sql, new Object[] { configuracoes });

	}

	@Override
	public void adicionarConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO, ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO, TipoNotaConceitoEnum tipoNotaConceito, Boolean mediaFinal) throws Exception {
		List<ConfiguracaoAcademicoNotaConceitoVO> conf = null;
		switch (tipoNotaConceito) {
		case NOTA_1:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota1ConceitoVOs();
			break;
		case NOTA_2:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota2ConceitoVOs();
			break;
		case NOTA_3:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota3ConceitoVOs();
			break;
		case NOTA_4:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota4ConceitoVOs();
			break;
		case NOTA_5:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota5ConceitoVOs();
			break;
		case NOTA_6:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota6ConceitoVOs();
			break;
		case NOTA_7:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota7ConceitoVOs();
			break;
		case NOTA_8:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota8ConceitoVOs();
			break;
		case NOTA_9:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota9ConceitoVOs();
			break;
		case NOTA_10:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota10ConceitoVOs();
			break;
		case NOTA_11:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota11ConceitoVOs();
			break;
		case NOTA_12:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota12ConceitoVOs();
			break;
		case NOTA_13:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota13ConceitoVOs();
			break;
		case NOTA_14:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota14ConceitoVOs();
			break;
		case NOTA_15:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota15ConceitoVOs();
			break;
		case NOTA_16:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota16ConceitoVOs();
			break;
		case NOTA_17:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota17ConceitoVOs();
			break;
		case NOTA_18:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota18ConceitoVOs();
			break;
		case NOTA_19:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota19ConceitoVOs();
			break;
		case NOTA_20:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota20ConceitoVOs();
			break;
		case NOTA_21:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota21ConceitoVOs();
			break;
		case NOTA_22:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota22ConceitoVOs();
			break;
		case NOTA_23:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota23ConceitoVOs();
			break;
		case NOTA_24:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota24ConceitoVOs();
			break;
		case NOTA_25:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota25ConceitoVOs();
			break;
		case NOTA_26:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota26ConceitoVOs();
			break;
		case NOTA_27:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota27ConceitoVOs();
			break;
		case NOTA_28:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota28ConceitoVOs();
			break;
		case NOTA_29:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota29ConceitoVOs();
			break;
		case NOTA_30:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota30ConceitoVOs();
			break;
		case NOTA_31:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota31ConceitoVOs();
			break;
		case NOTA_32:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota32ConceitoVOs();
			break;
		case NOTA_33:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota33ConceitoVOs();
			break;
		case NOTA_34:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota34ConceitoVOs();
			break;
		case NOTA_35:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota35ConceitoVOs();
			break;
		case NOTA_36:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota36ConceitoVOs();
			break;
		case NOTA_37:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota37ConceitoVOs();
			break;
		case NOTA_38:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota38ConceitoVOs();
			break;
		case NOTA_39:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota39ConceitoVOs();
			break;
		case NOTA_40:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota40ConceitoVOs();
			break;
		default:
			break;
		}
		configuracaoAcademicoNotaConceitoVO.setTipoNotaConceito(tipoNotaConceito);
		// if (!mediaFinal) {
		// configuracaoAcademicoNotaConceitoVO.setFaixaNota2(configuracaoAcademicoNotaConceitoVO.getFaixaNota1());
		// }
		getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().validarDados(configuracaoAcademicoNotaConceitoVO, mediaFinal);
		for (ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO2 : conf) {
			if (configuracaoAcademicoNotaConceitoVO2.getConceitoNota().trim().equalsIgnoreCase(configuracaoAcademicoNotaConceitoVO.getConceitoNota())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_conceitoNotaDuplicado"));
			}
			if (configuracaoAcademicoNotaConceitoVO2.getAbreviaturaConceitoNota().trim().equalsIgnoreCase(configuracaoAcademicoNotaConceitoVO.getAbreviaturaConceitoNota())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_abreviaturaNotaDuplicado"));
			}
			if ((configuracaoAcademicoNotaConceitoVO2.getFaixaNota1() >= configuracaoAcademicoNotaConceitoVO.getFaixaNota1() && configuracaoAcademicoNotaConceitoVO2.getFaixaNota2() <= configuracaoAcademicoNotaConceitoVO.getFaixaNota1()) || (configuracaoAcademicoNotaConceitoVO2.getFaixaNota1() >= configuracaoAcademicoNotaConceitoVO.getFaixaNota2() && configuracaoAcademicoNotaConceitoVO2.getFaixaNota2() <= configuracaoAcademicoNotaConceitoVO.getFaixaNota2())) {
				if (mediaFinal) {
					throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNotaMediaDuplicado"));
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNotaDuplicado"));
				}
			}

		}

		conf.add(configuracaoAcademicoNotaConceitoVO);
	}

	@Override
	public void removerConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO, ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO, TipoNotaConceitoEnum tipoNotaConceito) throws Exception {
		List<ConfiguracaoAcademicoNotaConceitoVO> conf = null;
		switch (tipoNotaConceito) {
		case NOTA_1:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota1ConceitoVOs();
			break;
		case NOTA_2:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota2ConceitoVOs();
			break;
		case NOTA_3:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota3ConceitoVOs();
			break;
		case NOTA_4:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota4ConceitoVOs();
			break;
		case NOTA_5:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota5ConceitoVOs();
			break;
		case NOTA_6:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota6ConceitoVOs();
			break;
		case NOTA_7:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota7ConceitoVOs();
			break;
		case NOTA_8:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota8ConceitoVOs();
			break;
		case NOTA_9:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota9ConceitoVOs();
			break;
		case NOTA_10:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota10ConceitoVOs();
			break;
		case NOTA_11:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota11ConceitoVOs();
			break;
		case NOTA_12:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota12ConceitoVOs();
			break;
		case NOTA_13:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota13ConceitoVOs();
			break;
		case NOTA_14:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota14ConceitoVOs();
			break;
		case NOTA_15:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota15ConceitoVOs();
			break;
		case NOTA_16:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota16ConceitoVOs();
			break;
		case NOTA_17:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota17ConceitoVOs();
			break;
		case NOTA_18:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota18ConceitoVOs();
			break;
		case NOTA_19:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota19ConceitoVOs();
			break;
		case NOTA_20:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota20ConceitoVOs();
			break;
		case NOTA_21:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota21ConceitoVOs();
			break;
		case NOTA_22:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota22ConceitoVOs();
			break;
		case NOTA_23:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota23ConceitoVOs();
			break;
		case NOTA_24:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota24ConceitoVOs();
			break;
		case NOTA_25:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota25ConceitoVOs();
			break;
		case NOTA_26:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota26ConceitoVOs();
			break;
		case NOTA_27:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota27ConceitoVOs();
			break;
		case NOTA_28:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota28ConceitoVOs();
			break;
		case NOTA_29:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota29ConceitoVOs();
			break;
		case NOTA_30:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota30ConceitoVOs();
			break;
		case NOTA_31:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota31ConceitoVOs();
			break;
		case NOTA_32:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota32ConceitoVOs();
			break;
		case NOTA_33:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota33ConceitoVOs();
			break;
		case NOTA_34:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota34ConceitoVOs();
			break;
		case NOTA_35:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota35ConceitoVOs();
			break;
		case NOTA_36:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota36ConceitoVOs();
			break;
		case NOTA_37:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota37ConceitoVOs();
			break;
		case NOTA_38:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota38ConceitoVOs();
			break;
		case NOTA_39:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota39ConceitoVOs();
			break;
		case NOTA_40:
			conf = configuracaoAcademicoVO.getConfiguracaoAcademicoNota40ConceitoVOs();
			break;

		default:
			break;
		}
		int index = 0;
		for (ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO2 : conf) {
			if (configuracaoAcademicoNotaConceitoVO2.getConceitoNota().trim().equalsIgnoreCase(configuracaoAcademicoNotaConceitoVO.getConceitoNota().trim())) {
				conf.remove(index);
				return;
			}
			index++;
		}

	}

	public ConfiguracaoAcademicoVO obterConfiguracaoAcademicoPorHistoricoTurmaDisciplinaCurso(HistoricoVO historicoVO, TurmaVO turmaVO, GradeDisciplinaVO gradeDisciplinaVO, CursoVO cursoVO, UsuarioVO usuarioVO) {
		if (historicoVO != null && !historicoVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
			return historicoVO.getConfiguracaoAcademico();
		}
		// if (turmaVO != null &&
		// !turmaVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
		// return turmaVO.getConfiguracaoAcademico();
		// }
		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaVO.getTurmaDisciplinaVOs()) {
			if (historicoVO.getDisciplina().getCodigo().equals(turmaDisciplinaVO.getDisciplina().getCodigo())) {
				if (turmaDisciplinaVO.getConfiguracaoAcademicoVO() != null && !turmaDisciplinaVO.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
					return turmaDisciplinaVO.getConfiguracaoAcademicoVO();
				}
			}
		}
		if (gradeDisciplinaVO != null && !gradeDisciplinaVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
			return gradeDisciplinaVO.getConfiguracaoAcademico();
		}
		return cursoVO.getConfiguracaoAcademico();
	}

	/**
	 * Este metodo retorna o codigo da configuração acadêmica que deve ser
	 * utilizado no histórico quando já existe matriculaperiodo e
	 * matriculaperiodoturmadisciplina gravado na base, este segue a seguinte
	 * regra de definição: 1 - A coluna ordem do sql define a ordem de
	 * prioridade da origem da configuração (TurmaDisciplina, GradeDisciplina,
	 * GrupoOptativa, Curso)
	 * 
	 */
	@Override
	public Integer consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatriculaPeriodoEDisciplina(Integer matriculaPeriodo, Integer disciplina) throws Exception {
		if (matriculaPeriodo != null && matriculaPeriodo > 0 && disciplina != null && disciplina > 0) {
			StringBuilder sql = new StringBuilder("");
			// Este busca a configuração acadêmica na disciplina
			sql.append(" select 4 as ordem, curso.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso where matriculaperiodo.codigo = ").append(matriculaPeriodo);
			sql.append(" union ");
			// Este busca a configuração acadêmica na grade disciplina
			sql.append(" select 3 as ordem, gradedisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso ");
			sql.append(" inner join gradecurricular on gradecurricular.curso = curso.codigo ");
			sql.append(" inner join periodoletivo on gradecurricular.codigo = periodoletivo.gradecurricular");
			sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = ").append(disciplina);
			sql.append(" and gradecurricular.codigo = matriculaperiodo.gradecurricular ");
			sql.append(" and matriculaperiodo.codigo = ").append(matriculaPeriodo);
			sql.append(" and gradedisciplina.configuracaoAcademico is not null ");
			sql.append(" union ");
			// Este busca a configuração acadêmica na grade disciplina validando
			// se
			// a disciplina é composta
			sql.append(" select 3 as ordem, gradedisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso ");
			sql.append(" inner join gradecurricular on gradecurricular.curso = curso.codigo ");
			sql.append(" inner join periodoletivo on gradecurricular.codigo = periodoletivo.gradecurricular ");
			sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
			sql.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = gradedisciplina.disciplina and disciplinacomposta.composta = ").append(disciplina);
			sql.append(" and gradecurricular.codigo = matriculaperiodo.gradecurricular ");
			sql.append(" and matriculaperiodo.codigo = ").append(matriculaPeriodo);
			sql.append(" and gradedisciplina.configuracaoAcademico is not null ");
			// Este busca a configuração acadêmica do grupo de disciplina
			// optativa
			sql.append(" union ");
			sql.append(" select 2 as ordem, gradecurriculargrupooptativadisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso ");
			sql.append(" inner join gradecurricular on gradecurricular.curso = curso.codigo ");
			sql.append(" inner join gradecurriculargrupooptativa on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
			sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sql.append(" and gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
			sql.append(" and gradecurricular.codigo = matriculaperiodo.gradecurricular ");
			sql.append(" where matriculaperiodo.codigo = ").append(matriculaPeriodo);
			sql.append(" and gradecurriculargrupooptativadisciplina.configuracaoAcademico is not null ");
			// Este busca a configuração acadêmica do grupo de disciplina
			// optativa
			// verificando se a disciplina é composta
			sql.append(" union ");
			sql.append(" select 2 as ordem, gradecurriculargrupooptativadisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sql.append(" inner join curso on curso.codigo = matricula.curso ");
			sql.append(" inner join gradecurricular on gradecurricular.curso = curso.codigo ");
			sql.append(" inner join gradecurriculargrupooptativa on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
			sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
			sql.append(" and gradecurricular.codigo = matriculaperiodo.gradecurricular ");
			sql.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = gradecurriculargrupooptativadisciplina.disciplina and disciplinacomposta.composta = ").append(disciplina);
			sql.append(" where matriculaperiodo.codigo = ").append(matriculaPeriodo);
			sql.append(" and gradecurriculargrupooptativadisciplina.configuracaoAcademico is not null ");
			sql.append(" union");
			// Este busca a configuração acadêmica da turma disciplina
			sql.append(" select 1 as ordem, turmadisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
			sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
			sql.append(" and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina ");
			sql.append(" where matriculaperiodo.codigo =  ").append(matriculaPeriodo);
			sql.append(" and turmadisciplina.configuracaoAcademico is not null ");
			
			// Este busca a configuração acadêmica da turma disciplina
			// verificando
			// se a disciplina é composta
			sql.append(" union");
			sql.append(" select 0 as ordem, turmadisciplina.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
			sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo  ");
			sql.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina and gradedisciplina.disciplinacomposta ");
			sql.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina and gradedisciplinacomposta.disciplina ").append(disciplina);
			sql.append(" and gradedisciplinacomposta.disciplina = matriculaperiodoturmadisciplina.disciplina ");
			sql.append(" where matriculaperiodo.codigo =  ").append(matriculaPeriodo);
			sql.append(" union");
			sql.append(" select 0 as ordem, gradedisciplinacomposta.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
			sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo  ");
			sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina and gradecurriculargrupooptativadisciplina.disciplinacomposta ");
			sql.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina and gradedisciplinacomposta.disciplina ").append(disciplina);
			sql.append(" and gradedisciplinacomposta.disciplina = matriculaperiodoturmadisciplina.disciplina ");
			sql.append(" where matriculaperiodo.codigo =  ").append(matriculaPeriodo);
			sql.append(" and gradedisciplinacomposta.configuracaoAcademico is not null ");			
			sql.append(" union");
			sql.append(" select -1 as ordem, turmadisciplinacomposta.configuracaoAcademico from matriculaperiodo ");
			sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
			sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
			sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
			sql.append(" inner join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
			sql.append(" inner join gradedisciplinacomposta on turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo and gradedisciplinacomposta.disciplina = matriculaperiodoturmadisciplina.disciplina  ");
			sql.append(" and gradedisciplinacomposta.disciplina = ").append(disciplina);
			sql.append(" where matriculaperiodo.codigo =  ").append(matriculaPeriodo);
			sql.append(" and turmadisciplinacomposta.configuracaoAcademico is not null ");
			
			sql.append(" order by ordem limit 1 ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (rs.next()) {
				return rs.getInt("configuracaoAcademico");
			}
		}
		return null;
	}

	@Override
	public Integer consultarCodigoConfiguracaoAcademicaUtilizarHistoricoPorMatrizDisciplinaTurmaCurso(Integer matrizCurricular, Integer disciplina, Integer turma, Integer curso) throws Exception {
		if (curso != null && curso > 0) {
			StringBuilder sql = new StringBuilder("");
			// Este busca a configuração acadêmica na disciplina
			sql.append(" select 4 as ordem, curso.configuracaoAcademico from curso ");
			sql.append(" where codigo = ").append(curso);
			if (disciplina != null && disciplina > 0) {
				if (matrizCurricular != null && matrizCurricular > 0) {
					// Este busca a configuração acadêmica na grade disciplina
					sql.append(" union ");
					sql.append(" select 3 as ordem, gradedisciplina.configuracaoAcademico from gradecurricular ");
					sql.append(" inner join periodoletivo on gradecurricular.codigo = periodoletivo.gradecurricular");
					sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo and gradedisciplina.disciplina = ").append(disciplina);
					sql.append(" and gradecurricular.codigo = ").append(matrizCurricular);
					// Este busca a configuração acadêmica na grade disciplina
					// validando
					// se
					// a disciplina é composta
					sql.append(" union ");
					sql.append(" select 3 as ordem, gradedisciplina.configuracaoAcademico from gradecurricular ");
					sql.append(" inner join periodoletivo on gradecurricular.codigo = periodoletivo.gradecurricular");
					sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
					sql.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = gradedisciplina.disciplina and disciplinacomposta.composta = ").append(disciplina);
					sql.append(" and gradecurricular.codigo = ").append(matrizCurricular);
					// Este busca a configuração acadêmica do grupo de
					// disciplina
					// optativa
					sql.append(" union ");
					sql.append(" select 2 as ordem, gradecurriculargrupooptativadisciplina.configuracaoAcademico from gradecurricular ");
					sql.append(" inner join gradecurriculargrupooptativa on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
					sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
					sql.append(" and gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
					sql.append(" and gradecurricular.codigo = ").append(matrizCurricular);
					// Este busca a configuração acadêmica do grupo de
					// disciplina
					// optativa
					sql.append(" union ");
					sql.append(" select 2 as ordem, gradecurriculargrupooptativadisciplina.configuracaoAcademico from gradecurricular ");
					sql.append(" inner join gradecurriculargrupooptativa on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular ");
					sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
					sql.append(" and gradecurricular.codigo = ").append(matrizCurricular);
					sql.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = gradecurriculargrupooptativadisciplina.disciplina and disciplinacomposta.composta = ").append(disciplina);
				}
				if (turma != null && turma > 0) {
					// Este busca a configuração acadêmica da turma disciplina
					sql.append(" union");
					sql.append(" select 1 as ordem, turmadisciplina.configuracaoAcademico from turma ");
					sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
					sql.append(" and turmadisciplina.disciplina = ").append(disciplina);
					sql.append(" where turma.codigo =  ").append(turma);
					// Este busca a configuração acadêmica da turma disciplina
					// verificando se a disciplina é composta
					sql.append(" union");
					sql.append(" select 1 as ordem, turmadisciplina.configuracaoAcademico from turma ");
					sql.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
					sql.append(" inner join disciplinacomposta on disciplinacomposta.disciplina = turmadisciplina.disciplina and disciplinacomposta.composta = ").append(disciplina);
					sql.append(" where turma.codigo =  ").append(turma);
				}
			}
			sql.append(" order by ordem limit 1 ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (rs.next()) {
				return rs.getInt("configuracaoAcademico");
			}
		}
		return null;
	}

        /**
         * Método responsavel por substituir as variaveis padroes do SEI (VariaveisNotaEnum)
         * na formula de calculo, quando as mesmas sao utilizadas. Deixando assim a formula
         * resolvida do ponto de vista de variáveis.
         * @param historico
         * @throws Exception 
         */
        public void prepararVariaveisNotaParaSubstituicaoFormulaNota(
                ConfiguracaoAcademicoVO cfgAcademico, 
                HistoricoVO historico,
                UsuarioVO usuario) throws Exception {
            boolean utilizaVariaveisRelativasAdvertencia = false;
            for (int y = 1; y <= 40; y++) {
                if ((Boolean) UtilReflexao.invocarMetodoGet(cfgAcademico, "utilizarNota" + y)) {
                    String formulaCalculoNota = (String) UtilReflexao.invocarMetodoGet(cfgAcademico, "formulaCalculoNota" + y); 
                    if (!formulaCalculoNota.equals("")) {
                        // verificar se há a presenca de alguma variável, para justificar montar
                        // as informacoes referentes a esta variavel.
                        if ((formulaCalculoNota.indexOf(VariaveisNotaEnum.ADVERB1.getValor()) != -1) ||
                            (formulaCalculoNota.indexOf(VariaveisNotaEnum.ADVERB2.getValor()) != -1) ||
                            (formulaCalculoNota.indexOf(VariaveisNotaEnum.ADVERB3.getValor()) != -1) ||
                            (formulaCalculoNota.indexOf(VariaveisNotaEnum.ADVERB4.getValor()) != -1) ||
                            (formulaCalculoNota.indexOf(VariaveisNotaEnum.ADVERPER.getValor()) != -1)) {
                            utilizaVariaveisRelativasAdvertencia = true;
                            break;
                        }
                    }
                }
            }
            if (utilizaVariaveisRelativasAdvertencia) {
                Map<String, Integer> listaAvertenciasPorVariavel = 
                        getFacadeFactory().getAdvertenciaFacade().consultarPorMatriculaNrAdvertenciasPeriodo(
                            historico.getMatricula().getMatricula(), historico.getAnoHistorico(), historico.getSemestreHistorico(), usuario);
                historico.setNrAdvertencias1Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.ADVERB1.getValor()));
                historico.setNrAdvertencias2Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.ADVERB2.getValor()));
                historico.setNrAdvertencias3Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.ADVERB3.getValor()));
                historico.setNrAdvertencias4Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.ADVERB4.getValor()));
                historico.setNrAdvertenciasPeriodoLetivo((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.ADVERPER.getValor()));

                historico.setNrSuspensoes1Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.SUSPENB1.getValor()));
                historico.setNrSuspensoes2Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.SUSPENB2.getValor()));
                historico.setNrSuspensoes3Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.SUSPENB3.getValor()));
                historico.setNrSuspensoes4Bimestre((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.SUSPENB4.getValor()));
                historico.setNrSuspensoesPeriodoLetivo((Integer)listaAvertenciasPorVariavel.get(VariaveisNotaEnum.SUSPENPER.getValor()));
            }
        }
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorUnidadeEnsinoNivelCombobox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct ca.codigo, ca.nome FROM configuracaoacademico ca ");
		sqlStr.append("INNER JOIN configuracoes on configuracoes.codigo = ca.configuracoes ");
		sqlStr.append("INNER JOIN unidadeEnsino on unidadeEnsino.configuracoes = configuracoes.codigo ");
		sqlStr.append("where (unidadeEnsino.codigo = ?) or (configuracoes.padrao) order by nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { unidadeEnsino });
		List<ConfiguracaoAcademicoVO> objs = new ArrayList<ConfiguracaoAcademicoVO>(0);
		while (tabelaResultado.next()) {
			ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			objs.add(obj);
		}
		return objs;
	}

	@Override
	public List<ConfiguracaoAcademicoVO> consultarExistenciaMaisDeUmaConfiguracaoAcademicoHistoricoDiario(TurmaVO turmaVO, String semestre, String ano, Integer disciplina, String filtroTipoCursoAluno, String tipoAluno, String tipoLayout, Boolean apenasAlunosAtivos, Boolean trazerAlunosPendentesFinanceiramente, Boolean filtroVisaoProfessor, boolean trazerAlunoTransferenciaMatriz,  boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List<ConfiguracaoAcademicoVO> objs = new ArrayList<ConfiguracaoAcademicoVO>(0);
		if(!Uteis.isAtributoPreenchido(turmaVO) && Uteis.isAtributoPreenchido(disciplina)){
			return objs;
		}
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ca.codigo, ca.nome FROM historico ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append(" INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");	
		sqlStr.append(" INNER JOIN disciplina ON matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" INNER JOIN curso on matricula.curso = curso.codigo ");
		sqlStr.append(" INNER JOIN configuracaoacademico ca ON ca.codigo = historico.configuracaoacademico ");
		sqlStr.append(" WHERE ");
		if(turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)){
			sqlStr.append(" matriculaperiodoturmadisciplina.turmapratica = ").append(turmaVO.getCodigo());
		}else if(turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)){
			sqlStr.append(" matriculaperiodoturmadisciplina.turmateorica = ").append(turmaVO.getCodigo());
		}else if(turmaVO.getTurmaAgrupada()){
			sqlStr.append(" matriculaperiodoturmadisciplina.turma in (select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo()).append(") ");
		}else{
			sqlStr.append(" matriculaperiodoturmadisciplina.turma = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if(!turmaVO.getIntegral()){
			sqlStr.append(" AND historico.anohistorico = '").append(ano).append("'");
		}
		if(turmaVO.getSemestral()){
			sqlStr.append(" AND historico.semestrehistorico = '").append(semestre).append("'");
		}
		if(turmaVO.getTurmaAgrupada()){
			sqlStr.append(" AND ((historico.disciplina = ").append(disciplina);
			sqlStr.append(" or (historico.disciplina in (select equivalente from disciplinaequivalente where disciplina =  ").append(disciplina).append(")) ");
			sqlStr.append(" or (historico.disciplina in (select disciplina from disciplinaequivalente where equivalente =  ").append(disciplina).append(")) ");
			sqlStr.append(" )) ");
		}else{
			sqlStr.append(" AND historico.disciplina = ").append(disciplina);
		}
		sqlStr.append(" and CASE WHEN (MatriculaPeriodoTurmaDisciplina.codigo is not null) then Historico.situacao   not in ( 'AA', 'CC', 'CH', 'IS', 'AB')  else (1=1) end  ");
		sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo in ( 'AT', 'FI', 'FO' ) ");
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo <> 'PR' ");
		}
//		if(!trazerAlunoTransferenciaMatriz){
//			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
//		}
		if (filtroVisaoProfessor == null || !filtroVisaoProfessor) {
			if (apenasAlunosAtivos != null && apenasAlunosAtivos) {
				sqlStr.append(" and matricula.situacao = 'AT' ");
			}
		}
		if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados || (filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and matriculaPeriodo.situacaomatriculaperiodo <> 'PR' ");
		}

		if (Uteis.isAtributoPreenchido(trazerAlunosPendentesFinanceiramente) && !trazerAlunosPendentesFinanceiramente) {
			sqlStr.append(" and matriculaPeriodo.situacao <> 'PF' ");
		}
		if (filtroTipoCursoAluno != null && !filtroTipoCursoAluno.equals("") && !filtroTipoCursoAluno.equals("todos")) {
			if (filtroTipoCursoAluno.equals("posGraduacao")) {
				sqlStr.append(" AND matricula.tipomatricula = 'NO'");
			} else if (filtroTipoCursoAluno.equals("extensao")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'EX' ");
			} else if (filtroTipoCursoAluno.equals("modular")) {
				sqlStr.append(" AND matricula.tipoMatricula = 'MO' ");
			}
		}
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma");
		} else if (tipoAluno.equals("reposicao")) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
//		if (filtroVisaoProfessor != null && filtroVisaoProfessor) {
//			
//			if (tipoAluno.equals("normal")) {
//				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
//			}
//			if (tipoAluno.equals("reposicao")) {
//				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
//			}
//		} else {			
//			if (tipoAluno.equals("normal")) {
//				sqlStr.append(" and matriculaperiodo.turma = turma.codigo ");
//			}
//			if (tipoAluno.equals("reposicao")) {
//				sqlStr.append(" and regAula.turma <> matriculaperiodo.turma ");
//			}
//		}
//		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
//		} else {
//			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = disciplina.codigo and MatriculaPeriodoTurmaDisciplina.matricula = matricula.matricula ");
//			if (tipoAluno.equals("reposicao")) {
//				sqlStr.append(" and matriculaperiodoturmadisciplina.turma = turma.codigo ");
//			}
//		}
		if ((filtroVisaoProfessor != null && filtroVisaoProfessor) || ((filtroVisaoProfessor == null || !filtroVisaoProfessor) && (tipoLayout.equals("DiarioModRetratoRel") || tipoLayout.equals("DiarioRel") || ((apenasAlunosAtivos != null && apenasAlunosAtivos) && (trazerAlunosPendentesFinanceiramente == null || !trazerAlunosPendentesFinanceiramente))))) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		}
		sqlStr.append(" and case when curso.niveleducacional = 'PR' and curso.liberarRegistroAulaEntrePeriodo then " + turmaVO.getCodigo() + " else MatriculaPeriodoTurmaDisciplina.turma end = MatriculaPeriodoTurmaDisciplina.turma ");
		sqlStr.append(" ORDER BY ca.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		while (tabelaResultado.next()) {
			ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			objs.add(obj);
		}
		return objs;
	}
	
	public List<String> consultarVariavelNotaPorConfiguracaoAcademico(Integer configuracaoAcademico, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct * from (");
		sb.append(" select titulonota1 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota1 is null or formulacalculonota1 = '' or utilizarcomosubstitutiva1) and utilizarnota1 ");
		sb.append(" union ");
		sb.append(" select titulonota2 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota2 is null or formulacalculonota2 = '' or utilizarcomosubstitutiva2) and utilizarnota2 ");
		sb.append(" union ");
		sb.append(" select titulonota3 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota3 is null or formulacalculonota3 = '' or utilizarcomosubstitutiva3) and utilizarnota3 ");
		sb.append(" union ");
		sb.append(" select titulonota4 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota4 is null or formulacalculonota4 = '' or utilizarcomosubstitutiva4) and utilizarnota4 ");
		sb.append(" union ");
		sb.append(" select titulonota5 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota5 is null or formulacalculonota5 = '' or utilizarcomosubstitutiva5) and utilizarnota5 ");
		sb.append(" union ");
		sb.append(" select titulonota6 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota6 is null or formulacalculonota6 = '' or utilizarcomosubstitutiva6) and utilizarnota6 ");
		sb.append(" union ");
		sb.append(" select titulonota7 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota7 is null or formulacalculonota7 = '' or utilizarcomosubstitutiva7) and utilizarnota7 ");
		sb.append(" union ");
		sb.append(" select titulonota8 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota8 is null or formulacalculonota8 = '' or utilizarcomosubstitutiva8) and utilizarnota8 ");
		sb.append(" union ");
		sb.append(" select titulonota9 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota9 is null or formulacalculonota9 = '' or utilizarcomosubstitutiva9) and utilizarnota9 ");
		sb.append(" union ");
		sb.append(" select titulonota10 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota10 is null or formulacalculonota10 = '' or utilizarcomosubstitutiva10) and utilizarnota10 ");
		sb.append(" union ");
		sb.append(" select titulonota11 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota11 is null or formulacalculonota11 = '' or utilizarcomosubstitutiva11) and utilizarnota11 ");
		sb.append(" union ");
		sb.append(" select titulonota12 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota12 is null or formulacalculonota12 = '' or utilizarcomosubstitutiva12) and utilizarnota12 ");
		sb.append(" union ");
		sb.append(" select titulonota13 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota13 is null or formulacalculonota13 = '' or utilizarcomosubstitutiva13) and utilizarnota13 ");
		sb.append(" union ");
		sb.append(" select titulonota14 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota14 is null or formulacalculonota14 = '' or utilizarcomosubstitutiva14) and utilizarnota14 ");
		sb.append(" union ");
		sb.append(" select titulonota15 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota15 is null or formulacalculonota15 = '' or utilizarcomosubstitutiva15) and utilizarnota15 ");
		sb.append(" union ");
		sb.append(" select titulonota16 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota16 is null or formulacalculonota16 = '' or utilizarcomosubstitutiva16) and utilizarnota16 ");
		sb.append(" union ");
		sb.append(" select titulonota17 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota17 is null or formulacalculonota17 = '' or utilizarcomosubstitutiva17) and utilizarnota17 ");
		sb.append(" union ");
		sb.append(" select titulonota18 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota18 is null or formulacalculonota18 = '' or utilizarcomosubstitutiva18) and utilizarnota18 ");
		sb.append(" union ");
		sb.append(" select titulonota19 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota19 is null or formulacalculonota19 = '' or utilizarcomosubstitutiva19) and utilizarnota19 ");
		sb.append(" union ");
		sb.append(" select titulonota20 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota20 is null or formulacalculonota20 = '' or utilizarcomosubstitutiva20) and utilizarnota20 ");
		sb.append(" union ");
		sb.append(" select titulonota21 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota21 is null or formulacalculonota21 = '' or utilizarcomosubstitutiva21) and utilizarnota21 ");
		sb.append(" union ");
		sb.append(" select titulonota22 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota22 is null or formulacalculonota22 = '' or utilizarcomosubstitutiva22) and utilizarnota22 ");
		sb.append(" union ");
		sb.append(" select titulonota23 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota23 is null or formulacalculonota23 = '' or utilizarcomosubstitutiva23) and utilizarnota23 ");
		sb.append(" union ");
		sb.append(" select titulonota24 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota24 is null or formulacalculonota24 = '' or utilizarcomosubstitutiva24) and utilizarnota24 ");
		sb.append(" union ");
		sb.append(" select titulonota25 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota25 is null or formulacalculonota25 = '' or utilizarcomosubstitutiva25) and utilizarnota25 ");
		sb.append(" union ");
		sb.append(" select titulonota26 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota26 is null or formulacalculonota26 = '' or utilizarcomosubstitutiva26) and utilizarnota26 ");
		sb.append(" union ");
		sb.append(" select titulonota27 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota27 is null or formulacalculonota27 = '' or utilizarcomosubstitutiva27) and utilizarnota27 ");
		sb.append(" union ");
		sb.append(" select titulonota28 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota28 is null or formulacalculonota28 = '' or utilizarcomosubstitutiva28) and utilizarnota28 ");
		sb.append(" union  ");
		sb.append(" select titulonota29 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota29 is null or formulacalculonota29 = '' or utilizarcomosubstitutiva29) and utilizarnota29 ");
		sb.append(" union ");
		sb.append(" select titulonota30 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota30 is null or formulacalculonota30 = '' or utilizarcomosubstitutiva30) and utilizarnota30 ");
		
		sb.append(" union ");
		sb.append(" select titulonota31 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota31 is null or formulacalculonota31 = '' or utilizarcomosubstitutiva31) and utilizarnota31 ");
		sb.append(" union ");
		sb.append(" select titulonota32 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota32 is null or formulacalculonota32 = '' or utilizarcomosubstitutiva32) and utilizarnota32 ");
		sb.append(" union ");
		sb.append(" select titulonota33 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota33 is null or formulacalculonota33 = '' or utilizarcomosubstitutiva33) and utilizarnota33 ");
		sb.append(" union ");
		sb.append(" select titulonota34 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota34 is null or formulacalculonota34 = '' or utilizarcomosubstitutiva34) and utilizarnota34 ");
		sb.append(" union ");
		sb.append(" select titulonota35 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota35 is null or formulacalculonota35 = '' or utilizarcomosubstitutiva35) and utilizarnota35 ");
		sb.append(" union ");
		sb.append(" select titulonota36 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota36 is null or formulacalculonota36 = '' or utilizarcomosubstitutiva36) and utilizarnota36 ");
		sb.append(" union ");
		sb.append(" select titulonota37 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota37 is null or formulacalculonota37 = '' or utilizarcomosubstitutiva37) and utilizarnota37 ");
		sb.append(" union ");
		sb.append(" select titulonota38 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota38 is null or formulacalculonota38 = '' or utilizarcomosubstitutiva38) and utilizarnota38 ");
		sb.append(" union ");
		sb.append(" select titulonota39 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota39 is null or formulacalculonota39 = '' or utilizarcomosubstitutiva39) and utilizarnota39 ");
		sb.append(" union ");
		sb.append(" select titulonota40 AS tituloNota from configuracaoacademico where codigo = ").append(configuracaoAcademico).append(" and (formulacalculonota40 is null or formulacalculonota40 = '' or utilizarcomosubstitutiva40) and utilizarnota40 ");
		sb.append(" ) as t where tituloNota is not null and tituloNota != ''");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<String> listaTituloNotaVOs = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			listaTituloNotaVOs.add(tabelaResultado.getString("tituloNota"));
		}
		return listaTituloNotaVOs;
	}
	
	public String consultarNotaUtilizarPorConfiguracaoAcademicoTituloNota(Integer configuracaoAcademico, String tituloNota, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" case ");
		sb.append(" when titulonota1 = '").append(tituloNota).append("' then 'nota1' ");
		sb.append(" when titulonota2 = '").append(tituloNota).append("' then 'nota2' ");
		sb.append(" when titulonota3 = '").append(tituloNota).append("' then 'nota3' ");
		sb.append(" when titulonota4 = '").append(tituloNota).append("' then 'nota4' ");
		sb.append(" when titulonota5 = '").append(tituloNota).append("' then 'nota5' ");
		sb.append(" when titulonota6 = '").append(tituloNota).append("' then 'nota6' ");
		sb.append(" when titulonota7 = '").append(tituloNota).append("' then 'nota7' ");
		sb.append(" when titulonota8 = '").append(tituloNota).append("' then 'nota8' ");
		sb.append(" when titulonota9 = '").append(tituloNota).append("' then 'nota9' ");
		sb.append(" when titulonota10 = '").append(tituloNota).append("' then 'nota10' ");
		sb.append(" when titulonota11 = '").append(tituloNota).append("' then 'nota11' ");
		sb.append(" when titulonota12 = '").append(tituloNota).append("' then 'nota12' ");
		sb.append(" when titulonota13 = '").append(tituloNota).append("' then 'nota13' ");
		sb.append(" when titulonota14 = '").append(tituloNota).append("' then 'nota14' ");
		sb.append(" when titulonota15 = '").append(tituloNota).append("' then 'nota15' ");
		sb.append(" when titulonota16 = '").append(tituloNota).append("' then 'nota16' ");
		sb.append(" when titulonota17 = '").append(tituloNota).append("' then 'nota17' ");
		sb.append(" when titulonota18 = '").append(tituloNota).append("' then 'nota18' ");
		sb.append(" when titulonota19 = '").append(tituloNota).append("' then 'nota19' ");
		sb.append(" when titulonota20 = '").append(tituloNota).append("' then 'nota20' ");
		sb.append(" when titulonota21 = '").append(tituloNota).append("' then 'nota21' ");
		sb.append(" when titulonota22 = '").append(tituloNota).append("' then 'nota22' ");
		sb.append(" when titulonota23 = '").append(tituloNota).append("' then 'nota23' ");
		sb.append(" when titulonota24 = '").append(tituloNota).append("' then 'nota24' ");
		sb.append(" when titulonota25 = '").append(tituloNota).append("' then 'nota25' ");
		sb.append(" when titulonota26 = '").append(tituloNota).append("' then 'nota26' ");
		sb.append(" when titulonota27 = '").append(tituloNota).append("' then 'nota27' ");
		sb.append(" when titulonota28 = '").append(tituloNota).append("' then 'nota28' ");
		sb.append(" when titulonota29 = '").append(tituloNota).append("' then 'nota29' ");
		sb.append(" when titulonota30 = '").append(tituloNota).append("' then 'nota30' ");
		
		sb.append(" when titulonota31 = '").append(tituloNota).append("' then 'nota31' ");
		sb.append(" when titulonota32 = '").append(tituloNota).append("' then 'nota32' ");
		sb.append(" when titulonota33 = '").append(tituloNota).append("' then 'nota33' ");
		sb.append(" when titulonota34 = '").append(tituloNota).append("' then 'nota34' ");
		sb.append(" when titulonota35 = '").append(tituloNota).append("' then 'nota35' ");
		sb.append(" when titulonota36 = '").append(tituloNota).append("' then 'nota36' ");
		sb.append(" when titulonota37 = '").append(tituloNota).append("' then 'nota37' ");
		sb.append(" when titulonota38 = '").append(tituloNota).append("' then 'nota38' ");
		sb.append(" when titulonota39 = '").append(tituloNota).append("' then 'nota39' ");
		sb.append(" when titulonota30 = '").append(tituloNota).append("' then 'nota40' ");
		
		sb.append(" end AS nota ");
		sb.append(" from configuracaoacademico ");
		sb.append(" where codigo = ").append(configuracaoAcademico);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("nota");
		}
		throw new Exception("Variável de nota "+tituloNota.toUpperCase()+" não foi encontrada, favor verificar na Configuração Academico.");
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
//		sqlStr.append("inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
//		sqlStr.append("where gradedisciplina.disciplinacomposta = false ");
//		sqlStr.append("and turmadisciplina.turma = ").append(turma);
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
//		sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradedisciplinacomposta.configuracaoAcademico ");
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
//		sqlStr.append("inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradecurriculargrupooptativadisciplina.configuracaoAcademico ");
//		sqlStr.append("where turmadisciplina.turma = ").append(turma).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
//		sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradedisciplinacomposta.configuracaoAcademico ");
//		sqlStr.append("and turmadisciplina.turma = ").append(turma);
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join turma on turma.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join curso on curso.codigo = gradecurricular.curso ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
//		sqlStr.append("and turma.codigo = ").append(turma);
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from turmadisciplina ");
//		sqlStr.append("inner join turmadisciplinacomposta on turmadisciplina.codigo = turmadisciplinacomposta.turmadisciplina ");		
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplinacomposta.configuracaoAcademico ");
//		sqlStr.append("where turmadisciplina.turma = ").append(turma);

		sqlStr.append(" select ConfiguracaoAcademico.* from ConfiguracaoAcademico ");
		sqlStr.append(" where exists ( ");
		sqlStr.append(" select historico.codigo from historico  ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turma).append("");
		sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico ");
		sqlStr.append(" union all ");
		sqlStr.append(" select historico.codigo from historico  ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turmapratica = ").append(turma);
		sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico ");
		sqlStr.append(" union all ");
		sqlStr.append(" select historico.codigo from historico  ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.turmateorica = ").append(turma);		
		sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico ");
		sqlStr.append(" union all ");
		sqlStr.append(" select historico.codigo from historico  ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append(" and  exists(select turmaagrupada.turma from turmaagrupada where matriculaperiodoturmadisciplina.turma = turmaagrupada.turma and turmaagrupada.turmaorigem =").append(turma).append(" limit 1) ");		
		sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico ");		
		sqlStr.append(" limit 1) ");
		sqlStr.append(" order by ConfiguracaoAcademico.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorDisciplinaPorTurma(Integer disciplina, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
//		sqlStr.append("inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
//		sqlStr.append("where gradedisciplina.disciplinacomposta = false ");
//		sqlStr.append("and turmadisciplina.disciplina = ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}
//		sqlStr.append(" union ");		
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
//		sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append("inner join turmadisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ");
//		}
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
//		sqlStr.append("where disciplina.codigo = ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
//		sqlStr.append("inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
//		sqlStr.append("where turmadisciplina.disciplina = ").append(disciplina).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false");
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
//		sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append("inner join turmadisciplina on turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
//		}
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
//		sqlStr.append("where disciplina.codigo = ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
//		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
//		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
//		sqlStr.append("inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo ");
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append("inner join turmadisciplina on turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
//		}
//		sqlStr.append("inner join curso on gradecurricular.curso = curso.codigo ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
//		sqlStr.append("where disciplina.codigo = ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from turmadisciplina ");
//		sqlStr.append("inner join turmadisciplinacomposta on turmadisciplina.codigo = turmadisciplinacomposta.turmadisciplina ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplinacomposta.configuracaoAcademico ");
//		sqlStr.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}		
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from turma ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
//		sqlStr.append("inner join turmadisciplinacomposta on turmadisciplina.codigo = turmadisciplinacomposta.turmadisciplina ");
//		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta ");
//		sqlStr.append("inner join curso on curso.codigo = turma.curso ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
//		sqlStr.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}	
//		sqlStr.append(" union ");
//		sqlStr.append("select distinct ConfiguracaoAcademico.* from turma ");
//		sqlStr.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
//		sqlStr.append("inner join curso on curso.codigo = turma.curso ");
//		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
//		sqlStr.append(" where turmadisciplina.disciplina =  ").append(disciplina);
//		if (Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
//		}	
		
		sqlStr.append(" select ConfiguracaoAcademico.* from ConfiguracaoAcademico ");
		
		
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" where exists ( ");
			sqlStr.append(" select historico.codigo from historico  ");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turma).append("");
			sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico and historico.disciplina =  ").append(disciplina).append("");
			sqlStr.append(" union all ");
			sqlStr.append(" select historico.codigo from historico  ");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sqlStr.append(" and matriculaperiodoturmadisciplina.turmapratica = ").append(turma);
			sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico and historico.disciplina =  ").append(disciplina).append("");
			sqlStr.append(" union all ");
			sqlStr.append(" select historico.codigo from historico  ");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sqlStr.append(" and matriculaperiodoturmadisciplina.turmateorica = ").append(turma);		
			sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico and historico.disciplina =  ").append(disciplina).append("");
			sqlStr.append(" union all ");
			sqlStr.append(" select historico.codigo from historico  ");
			sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = historico.matricula and  matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sqlStr.append(" and  exists(select turmaagrupada.turma from turmaagrupada where matriculaperiodoturmadisciplina.turma = turmaagrupada.turma and turmaagrupada.turmaorigem =").append(turma).append(" limit 1) ");		
			sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico and historico.disciplina =  ").append(disciplina).append("");	
			sqlStr.append(" limit 1) ");
		}else {
		sqlStr.append(" where exists (select historico.codigo from historico  ");
			sqlStr.append(" where ConfiguracaoAcademico.codigo = historico.configuracaoAcademico and historico.disciplina =  ").append(disciplina).append(" limit 1) ");
		}
		sqlStr.append(" order by ConfiguracaoAcademico.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct ConfiguracaoAcademico.codigo, ConfiguracaoAcademico.nome, ConfiguracaoAcademico.permitiraproveitamentodisciplinasoptativas from curso ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
		sqlStr.append("where curso.codigo = ").append(curso);
		sqlStr.append(" union ");
		sqlStr.append("select distinct ConfiguracaoAcademico.codigo, ConfiguracaoAcademico.nome, ConfiguracaoAcademico.permitiraproveitamentodisciplinasoptativas from gradecurricular ");
		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradedisciplina.configuracaoAcademico ");
		sqlStr.append("where gradedisciplina.disciplinacomposta = false");
		sqlStr.append(" and gradecurricular.curso = ").append(curso);
		sqlStr.append(" union ");
		sqlStr.append("select distinct ConfiguracaoAcademico.codigo, ConfiguracaoAcademico.nome, ConfiguracaoAcademico.permitiraproveitamentodisciplinasoptativas from gradecurricular ");
		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradedisciplinacomposta.configuracaoAcademico ");
		sqlStr.append("where gradecurricular.curso = ").append(curso);
		sqlStr.append(" union ");
		sqlStr.append("select distinct ConfiguracaoAcademico.codigo, ConfiguracaoAcademico.nome, ConfiguracaoAcademico.permitiraproveitamentodisciplinasoptativas from gradecurricular ");
		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradecurriculargrupooptativadisciplina.configuracaoAcademico ");
		sqlStr.append("where gradecurricular.curso = ").append(curso).append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta = false ");
		sqlStr.append(" union ");
		sqlStr.append("select distinct ConfiguracaoAcademico.codigo, ConfiguracaoAcademico.nome, ConfiguracaoAcademico.permitiraproveitamentodisciplinasoptativas from gradecurricular ");
		sqlStr.append("inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sqlStr.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradecurriculargrupooptativadisciplina.configuracaoAcademico ");
		sqlStr.append("where gradecurricular.curso = ").append(curso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ConfiguracaoAcademico.* from ConfiguracaoAcademico ");
		sqlStr.append("INNER JOIN UnidadeEnsino on UnidadeEnsino.configuracoes = ConfiguracaoAcademico.configuracoes ");
		sqlStr.append("WHERE UnidadeEnsino.codigo = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<SelectItem> montarListaSelectItemOpcoesDeNotas(ConfiguracaoAcademicoVO ca, boolean somenteNotaTipoLancamento) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		if (ca == null) {
			return lista;
		}
		if (ca.getCodigo().intValue() != 0) {
			for (int i = 1; i <= 40; i++) {
				ConfiguracaoAcademicaNotaVO confNota = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(ca, "configuracaoAcademicaNota" + i+"VO");

				confNota.setUtilizarNota((Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarNota" + i));
				confNota.setApresentarNota((Boolean) UtilReflexao.invocarMetodoGet(ca, "apresentarNota" + i));
				confNota.setTituloApresentar((String) UtilReflexao.invocarMetodoGet(ca, "tituloNotaApresentar" + i));
				confNota.setTitulo((String) UtilReflexao.invocarMetodoGet(ca, "tituloNota" + i));
				confNota.setUtilizarComoSubstitutiva((Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarComoSubstitutiva" + i));
				confNota.setFormulaCalculo((String) UtilReflexao.invocarMetodoGet(ca, "formulaCalculoNota" + i));
				confNota.setUtilizarNotaPorConceito((Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarNota" + i +"PorConceito"));
				confNota.setConfiguracaoAcademicoNotaConceitoVOs((ArrayList) UtilReflexao.invocarMetodoGet(ca, "configuracaoAcademicoNota" + i +"ConceitoVOs"));
				confNota.setVariavel(String.valueOf(i));

//				if (confNota.getUtilizarNota() && confNota.getApresentarNota() && (tipoUsoNota == null || confNota.getTipoUsoNota().equals(tipoUsoNota)) && ((confNota.getNotaTipoLancamento() && somenteNotaTipoLancamento) || !somenteNotaTipoLancamento)) {
//					lista.add(new SelectItem(confNota.getVariavel(), confNota.getTituloApresentar()));
//					ca.getMapaConfigNotas().put(confNota.getVariavel(), confNota);
//				}
			}
		}
		return lista;
	}
	
	/**
	 * Adicionar os dados básicos que desejarem
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Override
	public ConfiguracaoAcademicoVO consultarPorChavePrimariaDadosMinimos(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT codigo, nome, utilizarApoioEADParaDisciplinasModalidadePresencial FROM ConfiguracaoAcademico WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new Exception("Dados Não Encontrados (Configuração Acadêmico).");
		}
		return (montarDadosMinimos(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public ConfiguracaoAcademicoVO montarDadosMinimos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoAcademicoVO obj = new ConfiguracaoAcademicoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUtilizarApoioEADParaDisciplinasModalidadePresencial(dadosSQL.getBoolean("utilizarApoioEADParaDisciplinasModalidadePresencial"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoASerUsadaLancamentoNota(Integer codigoDisciplina, Integer codigoTurma, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT ConfiguracaoAcademico.*, 1 as ordem FROM ConfiguracaoAcademico");
		sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.configuracaoacademico = configuracaoacademico.codigo");
		sqlStr.append(" WHERE turmadisciplina.turma = ").append(codigoTurma);
		sqlStr.append(" AND turmadisciplina.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" union ");
		sqlStr.append(" select distinct ConfiguracaoAcademico.*, 2 as ordem from turmadisciplina ");
		sqlStr.append(" inner join turmadisciplinacomposta on turmadisciplina.codigo = turmadisciplinacomposta.turmadisciplina ");
		sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta ");
		sqlStr.append(" inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplinacomposta.configuracaoAcademico ");
		sqlStr.append(" where gradedisciplinacomposta.disciplina =  ").append(codigoDisciplina);
		sqlStr.append(" and turmadisciplina.turma = ").append(codigoTurma);				
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT ConfiguracaoAcademico.*, 3 as ordem FROM ConfiguracaoAcademico");
		sqlStr.append(" INNER JOIN turma ON turma.configuracaoacademico = configuracaoacademico.codigo");
		sqlStr.append(" WHERE turma.codigo = ").append(codigoTurma);
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT ConfiguracaoAcademico.*, 4 as ordem FROM ConfiguracaoAcademico");
		sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.disciplina = ").append(codigoDisciplina).append(" AND turmadisciplina.turma = ").append(codigoTurma);
		sqlStr.append(" INNER JOIN gradedisciplina ON turmadisciplina.gradedisciplina = gradedisciplina.codigo AND gradedisciplina.configuracaoacademico = configuracaoacademico.codigo");
		sqlStr.append(" WHERE gradedisciplina.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT ConfiguracaoAcademico.*, 4 as ordem FROM ConfiguracaoAcademico");
		sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.disciplina = ").append(codigoDisciplina).append(" AND turmadisciplina.turma = ").append(codigoTurma);
		sqlStr.append(" INNER JOIN gradecurriculargrupooptativadisciplina ON turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo AND gradecurriculargrupooptativadisciplina.configuracaoacademico = configuracaoacademico.codigo");
		sqlStr.append(" WHERE gradecurriculargrupooptativadisciplina.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT ConfiguracaoAcademico.*, 6 as ordem FROM ConfiguracaoAcademico");
		sqlStr.append(" INNER JOIN curso ON curso.configuracaoacademico = configuracaoacademico.codigo");
		sqlStr.append(" WHERE curso.codigo = ").append(codigoCurso);
		sqlStr.append(" ORDER BY ordem LIMIT 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoAcademicoVO configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		if (rs.next()) {
			configuracaoAcademicoVO = montarDados(rs, nivelMontarDados, usuarioVO);
		}
		return configuracaoAcademicoVO;
	}
	

	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoPorMatriculaCurso(String matricula, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("select configuracaoacademico.codigo, configuracaoacademico.apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno from configuracaoacademico ");
		sb.append(" inner join curso on curso.configuracaoacademico = configuracaoacademico.codigo ");
		sb.append(" inner join matricula on matricula.curso = curso.codigo ");
		sb.append(" where matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		ConfiguracaoAcademicoVO configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		if (tabelaResultado.next()) {
			configuracaoAcademicoVO.setCodigo(tabelaResultado.getInt("codigo"));
			configuracaoAcademicoVO.setApresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno(tabelaResultado.getBoolean("apresentarDisciplinaSemAulaProgramadaMinhasNotasVisaoAluno"));
		}
		return configuracaoAcademicoVO;
	}
	
	public boolean executarVerificarConfiguracaoAcademicaVinculadaUnidadeEnsinoLogada(ConfiguracoesVO configuracoesVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (!Uteis.isAtributoPreenchido(configuracoesVO)) {
			return false;
		}
		sqlStr.append("SELECT configuracaoacademico.codigo::text FROM configuracaoacademico ");
		sqlStr.append("WHERE configuracaoacademico.configuracoes = ").append(configuracoesVO.getCodigo().intValue()).append(";");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}
	
	public List<ConfiguracaoAcademicoVO> consultarTodasConfiguracaoAcademica(int nivelMontarDados,boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT configuracaoacademico.* FROM configuracaoacademico ");
		sqlStr.append("INNER JOIN configuracoes ON configuracoes.codigo = configuracaoacademico.configuracoes  ORDER BY configuracaoacademico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessor(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, String situacaoMatricula, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean trazerAlunoPendenteFinanceiramente, boolean permitiVisualizarAlunoTR_CA , boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder str =  new StringBuilder();
		str.append(" SELECT DISTINCT configuracaoAcademico.codigo, configuracaoAcademico.nome ");
		str.append(" FROM historico AS historico ");

		str.append(" INNER JOIN matricula on matricula.matricula = historico.matricula ");
		str.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		str.append(" LEFT JOIN matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = historico.matriculaPeriodoTurmaDisciplina ");		
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			if (turmaVO.getSubturma()) {
				if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					str.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					str.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					str.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				str.append("INNER JOIN Turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
				if (!turmaVO.getTurmaAgrupada()) {
					str.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			}
		} else {
			str.append("INNER JOIN Turma ON turma.codigo = case when matriculaPeriodoTurmaDisciplina.turma is not null then matriculaPeriodoTurmaDisciplina.turma else matriculaPeriodo.turma end ");
		}
		str.append(" LEFT JOIN gradeCurricular on historico.matrizCurricular = gradeCurricular.codigo ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = historico.disciplina ");
		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno ");
		str.append(" LEFT JOIN periodoLetivo on historico.periodoLetivoMatrizCurricular = periodoLetivo.codigo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.codigo = historico.gradeDisciplina ");
		str.append(" LEFT JOIN gradedisciplinacomposta on gradedisciplinacomposta.codigo = historico.gradedisciplinacomposta ");
		str.append(" LEFT JOIN gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = historico.gradeCurricularGrupoOptativaDisciplina ");
		str.append(" LEFT JOIN usuario on usuario.codigo = historico.responsavel ");

		str.append(" LEFT JOIN turno on matricula.turno = turno.codigo ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");

		str.append(" LEFT JOIN periodoLetivo as periodoLetivoMatrizCurricular on historico.periodoLetivoMatrizCurricular = periodoLetivoMatrizCurricular.codigo ");
		str.append(" LEFT JOIN periodoLetivo as periodoLetivoCursada on historico.periodoLetivoCursada = periodoLetivoCursada.codigo ");
		str.append(" LEFT JOIN gradeCurricular as matrizCurricular on matricula.gradecurricularatual = matrizCurricular.codigo ");
		str.append(" inner JOIN configuracaoAcademico on configuracaoAcademico.codigo = historico.configuracaoAcademico ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			str.append(" WHERE ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			str.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			str.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			str.append(") ");
		} else {
			str.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			str.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (Uteis.isAtributoPreenchido(curso) && !Uteis.isAtributoPreenchido(turmaVO)) {
			str.append(" and curso.codigo = ").append(curso);
		}

		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			str.append(" and turma.unidadeensino = ");
			str.append(unidadeEnsino);
		}
		if (verificarDisciplina) {
			if (disciplina != null && !disciplina.equals(0)) {
				str.append(" and (historico.disciplina = ");
				str.append(disciplina);
				if (turmaVO.getTurmaAgrupada()) {
					str.append(" or historico.disciplina in(select distinct disciplinaEquivalenteTurmaAgrupada from turmadisciplina where turmadisciplina.turma = ");
					str.append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina).append(") ");
					str.append(" or historico.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
					str.append(" or historico.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
				}
				str.append(" )");
			}
		} else {
			str.append(" and (historico.disciplina = ");
			str.append(disciplina);
			if (turmaVO.getTurmaAgrupada()) {
				str.append(" or historico.disciplina in(select distinct disciplinaEquivalenteTurmaAgrupada from turmadisciplina where turmadisciplina.turma = ");
				str.append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina).append(") ");
				str.append(" or historico.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
				str.append(" or historico.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
			}
			str.append(" )");
		}
		if (!semestre.equals("") && turmaVO.getSemestral()) {
			str.append(" and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!ano.equals("") && !turmaVO.getIntegral()) {
			str.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!situacaoMatricula.equals("")) {
			str.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		}
		if (!situacaoHistorico.equals("")) {
			str.append(" and historico.situacao not in (").append(situacaoHistorico).append(") ");
		}
		/*
		 * Condição OR adicionada para filtrar os historicos cujo a situação =
		 * 'AP',situacaoMatriculaPeriodo = 'TR' e a periodicidade do curso =
		 * 'IN'.O aluno fez um trancamento,em seguida ativou a matricula. A
		 * disciplina estava aprovada,sendo assim não foi gerado um novo
		 * historico e a matriculaPeriodo trancada ficou vinculada a este
		 * histórico.Se considerarmos apenas a regra and ((curso.periodicidade
		 * in ('AN', 'SE','IN') and matriculaperiodo.situacaoMatriculaPeriodo
		 * not in ('PR', 'TR', 'PC')) ,o histórico não é filtrado.
		 */
		if (filtroVisaoProfessor) {
			if (!trazerAlunoPendenteFinanceiramente) {
				str.append(" and matriculaPeriodo.situacao <> 'PF' ");
			}
			str.append(" and ((curso.periodicidade in ('AN', 'SE')");
			if(permitiVisualizarAlunoTR_CA) {
				str.append(" and matriculaperiodo.situacaoMatriculaPeriodo not in ('PR','PC'))");
			}else {
				str.append(" and matriculaperiodo.situacaoMatriculaPeriodo not in ('PR', 'TR', 'PC'))");	
			}
			str.append(" or (curso.periodicidade = 'IN' and matriculaperiodo.situacaoMatriculaPeriodo not in ('PR', 'PC'))) ");
			str.append(Historico.getWhereAlunoCursaDisciplinaTurmaProfessor(usuario.getPessoa().getCodigo(), "historico", "MatriculaPeriodoTurmaDisciplina", false));										
		}
		if(permitiVisualizarAlunoTR_CA) {
			str.append(" and (matricula.situacao <> 'CA' and Matricula.situacao <> 'CF')");
		}else {
			str.append(" and (matricula.situacao <> 'TR' and matricula.situacao <> 'CA' and Matricula.situacao <> 'CF')");	
		}
		// Garante que só será alterado o histórico atual do aluno
		// sqlStr.append(" and matricula.gradecurricularatual = historico.matrizcurricular ");
		// Garante que o histórico cursado por equivalencia não seja alterado
		// diretamente, pois a nota deve ser lançado no histórico equivalente
		str.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		// Garante que o histórico de disciplina composta não seja alterado
		// diretamente, pois a nota deve ser lançado no histórico de uma
		// disciplina que faz parte da composicao
		str.append(" and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false) ");

		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
//		str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));


		str.append(" group BY configuracaoAcademico.codigo, configuracaoAcademico.nome");
		str.append(" ORDER BY configuracaoAcademico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		// if(!tabelaResultado.next()){
		// throw new
		// Exception("Não foi encontrado nenhum resultado com os parâmetros passados.");
		// }
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = new ArrayList<ConfiguracaoAcademicoVO>(0);
		while(tabelaResultado.next()){
			ConfiguracaoAcademicoVO conf = new ConfiguracaoAcademicoVO();
			conf.setCodigo(tabelaResultado.getInt("codigo"));
			conf.setNome(tabelaResultado.getString("nome"));
			configuracaoAcademicoVOs.add(conf);
		}
		return configuracaoAcademicoVOs;
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(Integer unidadeEnsino, Integer curso, Integer disciplina, TurmaVO turmaVO, String ano, String semestre, Boolean trazerAlunoPendenteFinanceiramente, String situacaoHistorico, boolean verificarDisciplina, boolean filtroVisaoProfessor, boolean controlarAcesso, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permitiVisualizarAlunoTR_CA, Boolean trazerDisciplinaComposta,  boolean permitirRealizarLancamentoAlunosPreMatriculados, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder str =  new StringBuilder();
		str.append(" SELECT DISTINCT configuracaoAcademico.codigo, configuracaoAcademico.nome ");
		for(int x = 1;x<=40;x++){
			str.append(", tituloNota").append(x).append(", tituloNotaApresentar").append(x).append(", utilizarNota").append(x).append(", apresentarNota").append(x);
		}
		str.append(" FROM historico AS historico ");

		str.append(" INNER JOIN matricula on matricula.matricula = historico.matricula ");
		str.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		str.append(" LEFT JOIN matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = historico.matriculaPeriodoTurmaDisciplina ");
		
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			if (turmaVO.getSubturma()) {
				if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					str.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					str.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					str.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				str.append("INNER JOIN Turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
				if (!turmaVO.getTurmaAgrupada()) {
					str.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			}
		} else {
			str.append("INNER JOIN Turma ON turma.codigo = case when matriculaPeriodoTurmaDisciplina.turma is not null then matriculaPeriodoTurmaDisciplina.turma else matriculaPeriodo.turma end ");
		}
		str.append(" LEFT JOIN gradeCurricular on historico.matrizCurricular = gradeCurricular.codigo ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = historico.disciplina ");
		str.append(" LEFT JOIN pessoa AS \"aluno\" on aluno.codigo = matricula.aluno ");
		str.append(" LEFT JOIN periodoLetivo on historico.periodoLetivoMatrizCurricular = periodoLetivo.codigo ");
		str.append(" LEFT JOIN gradedisciplina on gradedisciplina.codigo = historico.gradeDisciplina ");
		str.append(" LEFT JOIN gradedisciplinacomposta on gradedisciplinacomposta.codigo = historico.gradedisciplinacomposta ");
		str.append(" LEFT JOIN gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = historico.gradeCurricularGrupoOptativaDisciplina ");
		str.append(" LEFT JOIN usuario on usuario.codigo = historico.responsavel ");

		str.append(" LEFT JOIN turno on matricula.turno = turno.codigo ");
		str.append(" LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		str.append(" LEFT JOIN curso on curso.codigo = matricula.curso ");

		str.append(" LEFT JOIN periodoLetivo as periodoLetivoMatrizCurricular on historico.periodoLetivoMatrizCurricular = periodoLetivoMatrizCurricular.codigo ");
		str.append(" LEFT JOIN periodoLetivo as periodoLetivoCursada on historico.periodoLetivoCursada = periodoLetivoCursada.codigo ");
		str.append(" LEFT JOIN gradeCurricular as matrizCurricular on matricula.gradecurricularatual = matrizCurricular.codigo ");
		str.append(" inner JOIN configuracaoAcademico on configuracaoAcademico.codigo = historico.configuracaoAcademico ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			str.append(" WHERE ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			str.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			str.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			str.append(") ");
		} else {
			str.append(" WHERE Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			str.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */		
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			str.append(" and unidadeensino.codigo = ");
			str.append(unidadeEnsino);
		}
		
			if (disciplina != null && !disciplina.equals(0)) {
				str.append(" and (historico.disciplina = ");
				str.append(disciplina);
				if (turmaVO.getTurmaAgrupada()) {
					str.append(" or historico.disciplina in(select distinct disciplinaEquivalenteTurmaAgrupada from turmadisciplina where turmadisciplina.turma = ");
					str.append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina).append(") ");
					str.append(" or historico.disciplina in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
					str.append(" or historico.disciplina in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
				}
				str.append(" )");
			}
		if (!semestre.equals("") && turmaVO.getSemestral()) {
			str.append(" and matriculaPeriodoTurmaDisciplina.semestre = '").append(semestre).append("' ");
		}
		if (!ano.equals("") && !turmaVO.getIntegral()) {
			str.append(" and matriculaPeriodoTurmaDisciplina.ano = '").append(ano).append("' ");
		}
		if (!trazerAlunoPendenteFinanceiramente) {
			str.append(" and matricula.situacao <> '").append("PF").append("' ");
			if (!permitiVisualizarAlunoTR_CA) {
				str.append(" and matricula.situacao <> '").append("CA").append("' ");
				str.append(" and matricula.situacao <> '").append("TR").append("' ");
			}
		} else {
			if (!permitiVisualizarAlunoTR_CA) {
				str.append(" and matricula.situacao <> '").append("CA").append("' ");
				str.append(" and matricula.situacao <> '").append("TR").append("' ");
			}
		}
		if (!situacaoHistorico.equals("")) {
			str.append(" and historico.situacao not in (").append(situacaoHistorico).append(") ");
		}
		// sqlStr.append(" and historico.situacao not in ('TR', 'AA', 'CH', 'CC', 'IS') ");
		if (filtroVisaoProfessor) {
			if(!permitirRealizarLancamentoAlunosPreMatriculados){
				str.append(" and matriculaperiodo.situacaoMatriculaPeriodo not in('PR', 'PC') ");
			}else{
				str.append(" and matriculaperiodo.situacaoMatriculaPeriodo != 'PC' ");
			}
			str.append(Historico.getWhereAlunoCursaDisciplinaTurmaProfessor(usuario.getPessoa().getCodigo(), "historico", "MatriculaPeriodoTurmaDisciplina", false));					
		}
		if (configuracaoAcademicoVO != null && !configuracaoAcademicoVO.getCodigo().equals(0)) {
			str.append(" and historico.configuracaoAcademico = ").append(configuracaoAcademicoVO.getCodigo());
		}
//		str.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));



		// Garante que só será alterado o histórico atual do aluno
		// sqlStr.append(" and matricula.gradecurricularatual = historico.matrizcurricular ");
		// Garante que o histórico cursado por equivalencia não seja alterado
		// diretamente, pois a nota deve ser lançado no histórico equivalente
		str.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		// Garante que o histórico de disciplina composta não seja alterado
		// diretamente, pois a nota deve ser lançado no histórico de uma
		// disciplina que faz parte da composicao
		if (!trazerDisciplinaComposta) {
			str.append(" and (historico.historicodisciplinacomposta is null or historico.historicodisciplinacomposta = false) ");
		}
		str.append(" group BY configuracaoAcademico.codigo, configuracaoAcademico.nome");
		for(int x = 1;x<=40;x++){
			str.append(", tituloNota").append(x).append(", tituloNotaApresentar").append(x).append(", utilizarNota").append(x).append(", apresentarNota").append(x);
		}
		str.append(" ORDER BY configuracaoAcademico.nome");
		//System.out.println(str.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		// if(!tabelaResultado.next()){
		// throw new
		// Exception("Não foi encontrado nenhum resultado com os parâmetros passados.");
		// }
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = new ArrayList<ConfiguracaoAcademicoVO>(0);
		while(tabelaResultado.next()){
			ConfiguracaoAcademicoVO conf = new ConfiguracaoAcademicoVO();
			conf.setCodigo(tabelaResultado.getInt("codigo"));
			conf.setNome(tabelaResultado.getString("nome"));
			for(int x = 1;x<=40;x++){
				UtilReflexao.invocarMetodo(conf, "setTituloNota"+x, tabelaResultado.getString("tituloNota"+x));
				UtilReflexao.invocarMetodo(conf, "setTituloNotaApresentar"+x, tabelaResultado.getString("tituloNotaApresentar"+x));
				UtilReflexao.invocarMetodo(conf, "setUtilizarNota"+x, tabelaResultado.getBoolean("utilizarNota"+x));
				UtilReflexao.invocarMetodo(conf, "setApresentarNota"+x, tabelaResultado.getBoolean("apresentarNota"+x));				
			}
			configuracaoAcademicoVOs.add(conf);
		}
		return configuracaoAcademicoVOs;
	}
	
	@Override
	public ConfiguracaoAcademicoVO realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(CursoVO cursoVO, GradeDisciplinaVO gradeDisciplinaVO, 
			GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO)) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), ano, semestre);
			if(ofertaDisciplinaVO != null && Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return ofertaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO = getFacadeFactory().getTurmaDisciplinaCompostaFacade().consultarPorTurmaGradeDisciplinaComposta(turmaVO.getCodigo(), gradeDisciplinaCompostaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuario);
			if(Uteis.isAtributoPreenchido(turmaDisciplinaCompostaVO) && Uteis.isAtributoPreenchido(turmaDisciplinaCompostaVO.getConfiguracaoAcademicoVO())) {
				return turmaDisciplinaCompostaVO.getConfiguracaoAcademicoVO();
			}
			if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getConfiguracaoAcademico())){
				return gradeDisciplinaCompostaVO.getConfiguracaoAcademico();
			}
			TurmaDisciplinaVO turmaDisciplinaVO = null;
			if(turmaVO.getTurmaDisciplinaVOs().stream().anyMatch(t -> t.getDisciplina().getCodigo().equals(gradeDisciplinaCompostaVO.getDisciplinaMaeComposicao().getCodigo()))) {
				turmaDisciplinaVO = turmaVO.getTurmaDisciplinaVOs().stream().filter(t -> t.getDisciplina().getCodigo().equals( gradeDisciplinaCompostaVO.getDisciplinaMaeComposicao().getCodigo())).findFirst().get();
			}else {
				turmaDisciplinaVO =  getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaVO.getCodigo(), gradeDisciplinaCompostaVO.getDisciplinaMaeComposicao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			}				
			if(Uteis.isAtributoPreenchido(turmaDisciplinaVO) && Uteis.isAtributoPreenchido(turmaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return turmaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeDisciplina())) {
				if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeDisciplina().getConfiguracaoAcademico())) {
					return gradeDisciplinaCompostaVO.getGradeDisciplina().getConfiguracaoAcademico();
				}
				GradeDisciplinaVO gradeDisciplinaVO2 = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCompostaVO.getGradeDisciplina().getCodigo(), usuario);
				if(Uteis.isAtributoPreenchido(gradeDisciplinaVO2.getConfiguracaoAcademico())) {
					return gradeDisciplinaVO2.getConfiguracaoAcademico();
				}
			}
			if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeCurricularGrupoOptativaDisciplina())) {
				if(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeCurricularGrupoOptativaDisciplina().getConfiguracaoAcademico())) {
					return gradeDisciplinaCompostaVO.getGradeCurricularGrupoOptativaDisciplina().getConfiguracaoAcademico();
				}
				GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO2 = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCompostaVO.getGradeCurricularGrupoOptativaDisciplina().getCodigo(), usuario);
				if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO2.getConfiguracaoAcademico())) {
					return gradeCurricularGrupoOptativaDisciplinaVO2.getConfiguracaoAcademico();
				}
			}
		} else if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO)) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), ano, semestre);
			if(ofertaDisciplinaVO != null && Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return ofertaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			TurmaDisciplinaVO turmaDisciplinaVO = null;
			if(turmaVO.getTurmaDisciplinaVOs().stream().anyMatch(t -> t.getDisciplina().getCodigo().equals(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()))) {
				turmaDisciplinaVO = turmaVO.getTurmaDisciplinaVOs().stream().filter(t -> t.getDisciplina().getCodigo().equals( gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo())).findFirst().get();
			}else {
				turmaDisciplinaVO =  getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaVO.getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			}			
			if(Uteis.isAtributoPreenchido(turmaDisciplinaVO) && Uteis.isAtributoPreenchido(turmaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return turmaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico())) {
				return gradeCurricularGrupoOptativaDisciplinaVO.getConfiguracaoAcademico();
			}
		} else if(Uteis.isAtributoPreenchido(gradeDisciplinaVO)) {
			OfertaDisciplinaVO ofertaDisciplinaVO = getFacadeFactory().getOfertaDisciplinaFacade().consultarConfiguracaoAcademicoPorDisciplinaAnoSemestre(gradeDisciplinaVO.getDisciplina().getCodigo(), ano, semestre);
			if(ofertaDisciplinaVO != null && Uteis.isAtributoPreenchido(ofertaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return ofertaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			TurmaDisciplinaVO turmaDisciplinaVO = null;
			if(turmaVO.getTurmaDisciplinaVOs().stream().anyMatch(t -> t.getDisciplina().getCodigo().equals(gradeDisciplinaVO.getDisciplina().getCodigo()))) {
				turmaDisciplinaVO = turmaVO.getTurmaDisciplinaVOs().stream().filter(t -> t.getDisciplina().getCodigo().equals( gradeDisciplinaVO.getDisciplina().getCodigo())).findFirst().get();
			}else {
				turmaDisciplinaVO =  getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaVO.getCodigo(), gradeDisciplinaVO.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			}			
			if(Uteis.isAtributoPreenchido(turmaDisciplinaVO) && Uteis.isAtributoPreenchido(turmaDisciplinaVO.getConfiguracaoAcademicoVO())) {
				return turmaDisciplinaVO.getConfiguracaoAcademicoVO();
			}
			if(Uteis.isAtributoPreenchido(gradeDisciplinaVO.getConfiguracaoAcademico())) {
				return gradeDisciplinaVO.getConfiguracaoAcademico();
			}
		}		
		return cursoVO.getConfiguracaoAcademico();
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicaLancamentoNotaMobile(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, UsuarioVO usuarioVO) throws Exception{
		
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, turmaVO.getUnidadeEnsino().getCodigo());
		boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		boolean trazerAlunoPendenteFinanceiramente = usuarioVO.getIsApresentarVisaoProfessor() ? 
				configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoProfessor()
				: usuarioVO.getIsApresentarVisaoCoordenador() ? configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoCoordenador() : true;
		boolean permitiVisualizarAlunoTR_CA = getFacadeFactory().getRegistroAulaFacade().verificarPermissaoVisualizarMatriculaTR_CA(usuarioVO);
		
		
		return consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, disciplinaVO.getCodigo(), turmaVO, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'IS', 'CC', 'CH', 'AB'", false, true, false, null, permitiVisualizarAlunoTR_CA, true, permitirRealizarLancamentoAlunosPreMatriculados, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
	}
	
	@Override
	public Integer consultarQuantidadeCasasDecimaisConfiguracaoAcademico(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "select quantidadecasasdecimaispermitiraposvirgula from configuracaoacademico where codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new Exception("Dados Não Encontrados (Configuração Acadêmico).");
		}
		return tabelaResultado.getInt("quantidadecasasdecimaispermitiraposvirgula");
	}
	
	@Override
	public List<SelectItem> consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(List<SelectItem> listaSelectItemTipoInformarNota, ForumVO forum, TurmaVO turma, String ano, String semestre,  UsuarioVO usuarioVO) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista = getFacadeFactory().getForumRegistrarNotaFacade().consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(listaSelectItemTipoInformarNota, forum, turma, ano, semestre, usuarioVO);
		return lista;
	}
	
	@Override
	public List<ConfiguracaoAcademicoVO> consultarPorDisciplinaPorTurmaConfiguracaoTurmaDisciplinaOuGradeDisciplina(Integer disciplina, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = turmadisciplina.configuracaoAcademico ");
		sqlStr.append("where gradedisciplina.disciplinacomposta = false ");
		sqlStr.append("and turmadisciplina.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
		}
		sqlStr.append(" union ");
		sqlStr.append("select distinct ConfiguracaoAcademico.* from gradecurricular ");
		sqlStr.append("inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sqlStr.append("inner join turmadisciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = gradedisciplina.configuracaoAcademico ");
		sqlStr.append("where turmadisciplina.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turmadisciplina.turma = ").append(turma);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoVinculadaCursoPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct configuracaoAcademico.* from turma ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
		sqlStr.append(" inner join configuracaoAcademico on configuracaoAcademico.codigo = curso.configuracaoAcademico ");
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" where turma.codigo = ").append(turma);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoAcademicoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
	}
	
	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorTurma(Integer turma) throws Exception {
		StringBuilder sql  = new StringBuilder(" ");
		sql.append(" select case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoacademico ");
		sql.append(" from turma ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = turma.periodoletivo");
		sql.append(" inner join curso on curso.codigo = turma.curso");
		sql.append(" where (subturma is null or  subturma = false) and (turmaagrupada is null or turmaagrupada = false)");
		sql.append(" and turma.codigo = ? ");
		sql.append(" union all ");
		sql.append(" select case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoacademico ");
		sql.append(" from turma");
		sql.append(" inner join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = turmaprincipal.periodoletivo");
		sql.append(" inner join curso on curso.codigo = turmaprincipal.curso");
		sql.append(" where turma.subturma and (turma.turmaagrupada is null or turma.turmaagrupada = false)");
		sql.append(" and turma.codigo = ? ");
		sql.append(" union all ");
		sql.append(" select case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoacademico ");
		sql.append(" from turma");
		sql.append(" inner join turmaagrupada on turmaagrupada.turma = turma.codigo");
		sql.append(" inner join turma as turmaprincipal on turmaprincipal.codigo = turmaagrupada.turmaorigem");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = turmaprincipal.periodoletivo");
		sql.append(" inner join curso on curso.codigo = turmaprincipal.curso");
		sql.append(" where turma.turmaagrupada");
		sql.append(" and turma.codigo = ? ");
		sql.append(" limit 1 ");
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), turma, turma, turma);
		if(rs.next()) {
			return getAplicacaoControle().carregarDadosConfiguracaoAcademica(rs.getInt("configuracaoacademico"));
		}
		return new ConfiguracaoAcademicoVO();				
	}
	
	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception {
		StringBuilder sql  = new StringBuilder(" ");
		sql.append(" select case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoacademico ");
		sql.append(" from matricula ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaPeriodo.periodoletivomatricula");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" where matriculaPeriodo.codigo = ? ");		
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaPeriodo);
		if(rs.next()) {
			return getAplicacaoControle().carregarDadosConfiguracaoAcademica(rs.getInt("configuracaoacademico"));
		}
		return new ConfiguracaoAcademicoVO();				
	}
	
	
	@Override
	public ConfiguracaoAcademicoVO consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(String matricula, String ano, String semestre) throws Exception {
		StringBuilder sql  = new StringBuilder(" ");
		sql.append(" select MatriculaPeriodo.ano, MatriculaPeriodo.semestre,  matriculaperiodo.situacaoMatriculaPeriodo, matriculaperiodo.codigo, case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoacademico ");
		sql.append(" from matricula ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaPeriodo.periodoletivomatricula");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" where matricula.matricula = ? and matriculaperiodo.situacaoMatriculaPeriodo != 'PC' ");		
		sql.append(" and ((curso.periodicidade = 'AN' and matriculaPeriodo.ano = ? ) ");
		sql.append(" or  (curso.periodicidade = 'SE' and matriculaPeriodo.ano = ? and matriculaPeriodo.semestre = ? ) ");
		sql.append(" or  (curso.periodicidade = 'IN')) ");		
		sql.append(" order by (MatriculaPeriodo.ano || '/' || MatriculaPeriodo.semestre) desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1");
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, ano, ano, semestre);
		if(rs.next()) {
			return getAplicacaoControle().carregarDadosConfiguracaoAcademica(rs.getInt("configuracaoacademico"));
		}
		return new ConfiguracaoAcademicoVO();				
	}
	
	public Double realizarCalculoCoeficienteRendimento(String formulaBase, List<HistoricoVO> historicoVOs) throws Exception{
		formulaBase = formulaBase.replace(" ", "");
		String[] formulaSomas = formulaBase.split("SOMA\\[");
		for(String soma: formulaSomas) {
			if (Uteis.isAtributoPreenchido(soma)) {
				Double valorSomado = 0.0;
			for(HistoricoVO historicoVO: historicoVOs) {
				valorSomado += substituirValoresCoeficienteRendimento(soma.substring(0, soma.lastIndexOf("]")), historicoVO);
			}
			String operador = soma.contains("]") ? soma.substring(soma.lastIndexOf("]") +1 ) : "";
				formulaBase = formulaBase.replace("SOMA[", "");
				if (!operador.isEmpty()) {
					if (operador.equals("/") || operador.equals("+") || operador.equals("-")|| operador.equals("*")) {
						formulaBase = formulaBase.replace(soma, valorSomado.toString() + operador);		
					}
				} else {
					formulaBase = formulaBase.replace(soma, valorSomado.toString());
					
				}
			}
		}		
		formulaBase = "isNaN(" + formulaBase + ") ? 0.0 : " + formulaBase;
		return Uteis.realizarCalculoFormula(formulaBase);
	}
	
	public Double substituirValoresCoeficienteRendimento(String formulaCoeficienteRendimento, HistoricoVO historicoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(formulaCoeficienteRendimento)) {
			if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO().getNrCreditos())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CR",	historicoVO.getGradeDisciplinaVO().getNrCreditos().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CR",	"0");
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getCargaHorariaDisciplina())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CH",	historicoVO.getCargaHorariaDisciplina().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_CH",	"0");
			}			
			formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("NR_DIS",	"1");			
			if (Uteis.isAtributoPreenchido(historicoVO.getMediaFinal())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("MEDIA_FINAL", historicoVO.getMediaFinal().toString());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("MEDIA_FINAL", "0.0");
			}
			if (Uteis.isAtributoPreenchido(historicoVO.getSituacao())) {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("SIT_HIS",	historicoVO.getSituacao());
			} else {
				formulaCoeficienteRendimento = formulaCoeficienteRendimento.replaceAll("SIT_HIS",	"");
			}	
		}
		return Uteis.realizarCalculoFormula(formulaCoeficienteRendimento);
	}
	
	
	public ConfiguracaoAcademicoVO consultarPorCodigoTurmaAgrupada(Integer codigoTurmaAgrupada, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct configuracaoacademico. * from turmaagrupada ");
		sb.append(" inner join turma on turmaagrupada.turma = turma.codigo ");
		sb.append(" inner join turma origem on turmaagrupada.turmaorigem = origem.codigo ");
		sb.append(" inner join curso on turma.curso = curso.codigo ");
		sb.append(" left join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico ");
		sb.append(" where turmaagrupada.turmaorigem = ").append(codigoTurmaAgrupada);
		sb.append(" order by quantidadediasprofessorpoderaregistraraulaaposultimaaula desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new ConfiguracaoAcademicoVO();
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}
	
	@Override
	public List<SelectItem> consultarVariavelTituloConfiguracaoAcademicoPorTipoUsoNota( UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct variavel, titulo from configuracaoacademiconota where tipousonota = ? and utilizarnota and utilizarcomomediafinal = false and length(formulacalculo) = 0 ");
		sb.append(" order by titulo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			lista.add(new SelectItem(tabelaResultado.getString("variavel"), tabelaResultado.getString("titulo")));
        }
		return lista;
	}

	@Override       
	public ConfiguracaoAcademicoVO  consultarMascaraNumeroRegistroDiplomaENumeroProcessoExpedicaoDiplomaPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select  mascaraNumeroRegistroDiploma  , mascaraNumeroProcessoExpedicaoDiploma from curso ");
		sqlStr.append("inner join ConfiguracaoAcademico on ConfiguracaoAcademico.codigo = curso.configuracaoAcademico ");
		sqlStr.append("where curso.codigo = ").append(curso);
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!rs.next()) {
			throw new Exception("Dados Não Encontrados (Configuração Acadêmico).");
		}
		ConfiguracaoAcademicoVO confAcad = new ConfiguracaoAcademicoVO();
		confAcad.setMascaraNumeroProcessoExpedicaoDiploma(rs.getString("mascaraNumeroProcessoExpedicaoDiploma"));
		confAcad.setMascaraNumeroRegistroDiploma(rs.getString("mascaraNumeroRegistroDiploma"));
		return confAcad ;	
		
	}


	
	
}

