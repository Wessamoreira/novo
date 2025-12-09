package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoDisciplinaForaGradeVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PortadorDiplomaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.secretaria.HistoricoGradeMigradaEquivalenteVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TituloCursoPos;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.HistoricoAlunoAtividadeComplementarRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.interfaces.academico.HistoricoAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class HistoricoAlunoRel extends SuperRelatorio implements HistoricoAlunoRelInterfaceFacade {

	private static final long serialVersionUID = -6474105682929885677L;

	public static final String HISTORICO_ALUNO_REL = "HistoricoAlunoRel";

	public static final String HISTORICO_ALUNO_LAYOUT_2_REL = "HistoricoAlunoLayout2Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_3_REL = "HistoricoAlunoLayout3Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_4_REL = "HistoricoAlunoLayout4Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_5_REL = "HistoricoAlunoLayout5Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_6_REL = "HistoricoAlunoLayout6Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_7_REL = "HistoricoAlunoLayout7Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_8_REL = "HistoricoAlunoLayout8Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_9_REL = "HistoricoAlunoLayout9Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_10_REL = "HistoricoAlunoLayout10Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_11_REL = "HistoricoAlunoLayout11Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_REL = "HistoricoAlunoLayout12Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL = "HistoricoAlunoLayout12MedicinaRel";
	public static final String HISTORICO_ALUNO_LAYOUT_13_REL = "HistoricoAlunoLayout13Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_14_REL = "HistoricoAlunoLayout14Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_15_REL = "HistoricoAlunoLayout15Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_16_REL = "HistoricoAlunoLayout16Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_17_REL = "HistoricoAlunoLayout17Rel";

	// Excel
	public static final String HISTORICO_ALUNO_LAYOUT_2_EXCEL_REL = "HistoricoAlunoLayout2ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_3_EXCEL_REL = "HistoricoAlunoLayout3ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_4_EXCEL_REL = "HistoricoAlunoLayout4ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_5_EXCEL_REL = "HistoricoAlunoLayout5ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_6_EXCEL_REL = "HistoricoAlunoLayout6ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_7_EXCEL_REL = "HistoricoAlunoLayout7ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_8_EXCEL_REL = "HistoricoAlunoLayout8ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_9_EXCEL_REL = "HistoricoAlunoLayout9ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_10_EXCEL_REL = "HistoricoAlunoLayout10ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_11_EXCEL_REL = "HistoricoAlunoLayout11ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_EXCEL_REL = "HistoricoAlunoLayout12ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_REL = "HistoricoAlunoLayout12MedicinaExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_13_EXCEL_REL = "HistoricoAlunoLayout13ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_14_EXCEL_REL = "HistoricoAlunoLayout14ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_15_EXCEL_REL = "HistoricoAlunoLayout15ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_16_EXCEL_REL = "HistoricoAlunoLayout16ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_17_EXCEL_REL = "HistoricoAlunoLayout17ExcelRel";
	public static final String HISTORICO_ALUNO_ENSINO_MEDIO = "HistoricoAlunoEnsinoMedio";
	public static final String HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2 = "HistoricoAlunoEnsinoMedioLayout2Rel";
	public static final String HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3 = "HistoricoAlunoEnsinoMedioLayout3Rel";

	public static final String HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC = "HistoricoAlunoLayoutPortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayoutExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC = "HistoricoAlunoLayout2PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_2_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout2ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC = "HistoricoAlunoLayout3PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_3_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout3ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC = "HistoricoAlunoLayout4PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_4_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout4ExcelPortariaMEC";

	public static final String HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC = "HistoricoAlunoLayout5PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_5_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout5ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC = "HistoricoAlunoLayout6PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_6_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout6ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC = "HistoricoAlunoLayout7PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_7_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout7ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC = "HistoricoAlunoLayout8PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_8_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout8ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC = "HistoricoAlunoLayout9PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_9_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout9ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC = "HistoricoAlunoLayout10PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_10_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout10ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC = "HistoricoAlunoLayout11PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_11_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout11ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC = "HistoricoAlunoLayout12PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout12ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC = "HistoricoAlunoLayout13PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_13_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout13ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC = "HistoricoAlunoLayout12MedicinaPortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout12MedicinaExcelPortariaMECRel";
	
	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR = "HistoricoAlunoLayout12MedicinaAdaptacaoMatrizCurricularRel";
	public static final String HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_ADAPTACAO_MATRIZ_CURRICULAR = "HistoricoAlunoLayout12MedicinaExcelAdaptacaoMatrizCurricularRel";

	public static final String HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC = "HistoricoAlunoLayout14PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_14_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout14ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC = "HistoricoAlunoLayout15PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_15_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout15ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC = "HistoricoAlunoLayout16PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_16_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout16ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC = "HistoricoAlunoLayout17PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_17_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout17ExcelPortariaMECRel";

	public static final String HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC = "HistoricoAlunoLayout18PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_18_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout18ExcelPortariaMECRel";
	
	public static final String HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC = "HistoricoAlunoLayout19PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_19_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout19ExcelPortariaMECRel";
	
	
	public static final String HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC = "HistoricoAlunoLayout20PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_20_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout20ExcelPortariaMECRel";
	
	public static final String HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC = "HistoricoAlunoLayout21PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_21_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout21ExcelPortariaMECRel";
	
	public static final String HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC = "HistoricoAlunoLayout22PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout22ExcelPortariaMEC";
	
	public static final String HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC = "HistoricoAlunoLayout23PortariaMECRel";
	public static final String HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC = "HistoricoAlunoLayout23ExcelPortariaMECRel";
	
	public static final String HISTORICO_ALUNO_LAYOUT_24 = "HistoricoAlunoLayout24Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_24_EXCEL = "HistoricoAlunoLayout24ExcelRel";
	public static final String HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC = "HistoricoAlunoLayout24PortariaMecRel";
	public static final String HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC_EXCEL = "HistoricoAlunoLayout24PortariaMecExcelRel";
	
	public static final String HISTORICO_ALUNO_POS_17 = "HistoricoAlunoPos17Rel";
	public static final String HISTORICO_ALUNO_POS_18 = "HistoricoAlunoPos18Rel";
	public static final String HISTORICO_ALUNO_LAYOUT_EXT19 = "HistoricoAlunoLayout19ExtRel";

	

	public HistoricoAlunoRel() throws Exception {
	}

	public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, String campoFiltrarPor, String tipoDesconto, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		if (unidadeEnsinoVO.getCodigo() == 0 && !campoFiltrarPor.equals("programacaoFormatura")) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO(Histórico Aluno) deve ser informado.");
		}
		if (campoFiltrarPor.equals("aluno")) {
			if (matriculaVO == null || matriculaVO.getMatricula().equals("")) {
				throw new ConsistirException("Por Favor informe uma matrícula para a geração do relatório.");
			}
		}
		if (campoFiltrarPor.equals("turma")) {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException("Por Favor informe uma turma para a geração do relatório.");
			}
		}

		if (campoFiltrarPor.equals("desconto")) {
			if (tipoDesconto.equals("")) {
				throw new ConsistirException("O campo TIPO DESCONTO (Histórico Aluno) deve ser informado.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.HistoricoAlunoRelInterfaceFacade#criarObjeto
	 * (relatorio.negocio.comuns.academico .HistoricoAlunoRelVO,
	 * negocio.comuns.academico.MatriculaVO,
	 * negocio.comuns.academico.GradeCurricularVO, int, int)
	 */
	public HistoricoAlunoRelVO criarObjeto(HistoricoAlunoRelVO historicoAlunoRelVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int ordem, String observacaoComplementar, String observacaoComplementarIntegralizado, String tipoConsulta, String tipoLayout, Boolean utilizarCidadeUnidadeMatriz, Date dataExpedicaoDiploma, Boolean apresentarInstituicaoDisciplinaAproveitada, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, Boolean apresentarDisciplinaForaGrade, UsuarioVO usuarioVO, Boolean filtroDisciplinasACursar, Boolean desconsiderarSituacaoMatriculaPeriodo, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, boolean apresentarApenasUltimoHistoricoDisciplina, boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista, boolean apresentarApenasProfessorTitulacaoTurmaOrigem, String regraApresentacaoProfessorDisciplinaAproveitamento, Boolean dataInicioTerminoAlteracoesCadastrais, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) throws Exception {
		return criarObjeto(historicoAlunoRelVO, matriculaVO, gradeCurricularVO, filtroRelatorioAcademicoVO, ordem, observacaoComplementar, observacaoComplementarIntegralizado, tipoConsulta, tipoLayout, utilizarCidadeUnidadeMatriz, dataExpedicaoDiploma, apresentarInstituicaoDisciplinaAproveitada, apresentarDisciplinaAnoSemestreTransferenciaGrade, apresentarDisciplinaForaGrade, usuarioVO, filtroDisciplinasACursar, desconsiderarSituacaoMatriculaPeriodo, apresentarObservacaoTransferenciaMatrizCurricular, observacaoTransferenciaMatrizCurricular, apresentarApenasUltimoHistoricoDisciplina, considerarCargaHorariaCursadaIgualCargaHorariaPrevista, apresentarApenasProfessorTitulacaoTurmaOrigem, regraApresentacaoProfessorDisciplinaAproveitamento, dataInicioTerminoAlteracoesCadastrais, configuracaoLayoutHistoricoVO, Boolean.FALSE);
	}
	
	public HistoricoAlunoRelVO criarObjeto(HistoricoAlunoRelVO historicoAlunoRelVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int ordem, String observacaoComplementar, String observacaoComplementarIntegralizado, String tipoConsulta, String tipoLayout, Boolean utilizarCidadeUnidadeMatriz, Date dataExpedicaoDiploma, Boolean apresentarInstituicaoDisciplinaAproveitada, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, Boolean apresentarDisciplinaForaGrade, UsuarioVO usuarioVO, Boolean filtroDisciplinasACursar, Boolean desconsiderarSituacaoMatriculaPeriodo, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, boolean apresentarApenasUltimoHistoricoDisciplina, boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista, boolean apresentarApenasProfessorTitulacaoTurmaOrigem, String regraApresentacaoProfessorDisciplinaAproveitamento, Boolean dataInicioTerminoAlteracoesCadastrais, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {
		// FiltroSituacaoDisciplina filtroEnum =
		// FiltroSituacaoDisciplina.getEnum(filtro);
		if(Uteis.getIsValorNumerico(tipoLayout) && !Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
			configuracaoLayoutHistoricoVO =  getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorChavePrimaria(Integer.valueOf(tipoLayout), usuarioVO);
		}
		gradeCurricularVO.getPeriodoLetivosVOs().clear();
		gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricularVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
		boolean gradeAtualAluno = matriculaVO.getGradeCurricularAtual().getCodigo().equals(gradeCurricularVO.getCodigo());// getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularUltimaMatriculaPeriodo(matriculaVO.getMatricula(),
																															// gradeCurricularVO.getCodigo(),
		Matricula.montarDadosUnidadeEnsino(matriculaVO, NivelMontarDados.TODOS);
		historicoAlunoRelVO.setMatriculaVO(matriculaVO);																											// usuarioVO);
		
		if (utilizarCidadeUnidadeMatriz) {					
			UnidadeEnsinoVO unidadeEnsinoMatriz = getFacadeFactory().getUnidadeEnsinoFacade()
					.obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			
			obterDadosUnidadeEnsino(unidadeEnsinoMatriz, historicoAlunoRelVO, utilizarCidadeUnidadeMatriz, dataExpedicaoDiploma, usuarioVO);
		}
		else {
			obterDadosUnidadeEnsino(matriculaVO.getUnidadeEnsino(), historicoAlunoRelVO, utilizarCidadeUnidadeMatriz, dataExpedicaoDiploma, usuarioVO);
		}				

		Matricula.montarDadosAluno(matriculaVO, NivelMontarDados.TODOS, usuarioVO);
		// getFacadeFactory().getPessoaFacade().carregarDados(matriculaVO.getAluno(),
		// NivelMontarDados.TODOS);
		if (tipoConsulta.equals("desconto")) {
			gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularDaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		historicoAlunoRelVO.setSituacaoFinal(matriculaVO.getSituacao_Apresentar());

		if (matriculaVO.getTituloMonografia() != null) {
			matriculaVO.setTituloMonografia(matriculaVO.getTituloMonografia().replaceAll("(\n|\r)+", " "));
			historicoAlunoRelVO.setTituloMonografia(matriculaVO.getTituloMonografia());
		} else {
			historicoAlunoRelVO.setTituloMonografia(matriculaVO.getTituloMonografia());
		}
		if (matriculaVO.getNotaMonografia() != null) {
			if (Uteis.isAtributoPreenchido(matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo())) {
				Integer quantidadeCasasDecimais = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarQuantidadeCasasDecimaisConfiguracaoAcademico(matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo(), usuarioVO);
				historicoAlunoRelVO.setNotaMonografia(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(matriculaVO.getNotaMonografia(), quantidadeCasasDecimais).toString());
			} else {
				historicoAlunoRelVO.setNotaMonografia(Uteis.getDoubleFormatado(matriculaVO.getNotaMonografia()));
			}
		}
		if (matriculaVO.getOrientadorMonografia() != null) {
			matriculaVO.setOrientadorMonografia(matriculaVO.getOrientadorMonografia().replaceAll("(\n|\r)+", " "));
			historicoAlunoRelVO.setOrientadorMonografia(matriculaVO.getOrientadorMonografia());
		}

		if (Uteis.isAtributoPreenchido(matriculaVO.getProficienciaLinguaEstrangeira())) {
			historicoAlunoRelVO.setProficienciaLinguaEstrangeira(matriculaVO.getProficienciaLinguaEstrangeira());
		}

		if (Uteis.isAtributoPreenchido(matriculaVO.getSituacaoProficienciaLinguaEstrangeira())) {
			historicoAlunoRelVO.setSituacaoProficienciaLinguaEstrangeira(matriculaVO.getSituacaoProficienciaLinguaEstrangeira());
		}

		if (matriculaVO.getOrientadorMonografia() != null) {
			matriculaVO.setOrientadorMonografia(matriculaVO.getOrientadorMonografia().replaceAll("(\n|\r)+", " "));
			historicoAlunoRelVO.setOrientadorMonografia(matriculaVO.getOrientadorMonografia());
		}
		
		if (matriculaVO.getTitulacaoOrientadorMonografia() != null) {
			matriculaVO.setTitulacaoOrientadorMonografia(matriculaVO.getTitulacaoOrientadorMonografia().replaceAll("(\n|\r)+", " "));
			historicoAlunoRelVO.setTitulacaoOrientadorMonografia(matriculaVO.getTitulacaoOrientadorMonografia());
		}	
		
		if (matriculaVO.getCargaHorariaMonografia() != null) {
			historicoAlunoRelVO.setCargaHorariaMonografia(matriculaVO.getCargaHorariaMonografia());
		}
		

		// if (!matriculaVO.getEnadeVO().getTituloEnade().trim().isEmpty()) {
		// historicoAlunoRelVO.setEnade(matriculaVO.getEnadeVO().getTituloEnade());
		// } else if (matriculaVO.getEnadeVO().getCodigo() > 0) {
		// historicoAlunoRelVO.setEnade(getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(matriculaVO.getEnadeVO().getCodigo(),
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO).getTituloEnade());
		// }
		historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + observacaoComplementar);
		obterDadosAluno(matriculaVO, gradeCurricularVO, gradeAtualAluno, historicoAlunoRelVO, tipoLayout, observacaoComplementarIntegralizado, apresentarObservacaoTransferenciaMatrizCurricular, observacaoTransferenciaMatrizCurricular, utilizarCidadeUnidadeMatriz, apresentarDisciplinaForaGrade, usuarioVO, configuracaoLayoutHistoricoVO);

		Matricula.montarDadosCurso(matriculaVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		Date dataMatricula = new Date();
		if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC)) {
			dataMatricula = matriculaVO.getDataConclusaoCurso();
		}else {
		  dataMatricula = matriculaVO.getData();
		}
		
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoPos15Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || Uteis.getIsValorNumerico(tipoLayout)) {
			dataMatricula = matriculaVO.getDataConclusaoCurso();
			if (utilizarCidadeUnidadeMatriz) {
				String cidade = consultarCidadePorUnidadeEnsinoMatriz(usuarioVO);
				if (cidade.equals("")) {
					cidade = matriculaVO.getUnidadeEnsino().getCidade().getNome();
				}
				historicoAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade, new Date(), true));
			} else {
				historicoAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), true));
			}
		}else if(tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel")) {
			historicoAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(historicoAlunoRelVO.getCidade(), historicoAlunoRelVO.getEstado(), new Date(), true));
		}

		obterDadosCurso(matriculaVO.getCurso(), dataMatricula, matriculaVO.getAutorizacaoCurso().getCodigo(),
				gradeAtualAluno, historicoAlunoRelVO, gradeCurricularVO.getCodigo(), matriculaVO.getMatricula(),
				tipoLayout, matriculaVO.getRenovacaoReconhecimentoVO().getCodigo(), usuarioVO, gradeCurricularVO, configuracaoLayoutHistoricoVO);
		montarDadosCursoEnderecoEstabelecimentoAnterior(matriculaVO.getAluno().getCodigo(), historicoAlunoRelVO,
				usuarioVO);
		montarDadosCursoGraduacaoVindoFormacaoAcademica(matriculaVO.getAluno().getCodigo(), historicoAlunoRelVO,
				usuarioVO);
		if (matriculaVO.getCurso().getCodigo() > 0) {
			if (matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo() == 0) {
				matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuarioVO));
			}
			if (matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo() > 0 && matriculaVO.getCurso().getConfiguracaoAcademico().getNome().trim().isEmpty()) {
				matriculaVO.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo(), usuarioVO));
			}
		}

		// SE NÃO TIVER DATA DE COLAÇÃO NÃO DEVE APARECER O TITULO DO CURSO EM
		// NENHUM RELATÓRIO
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
			if (historicoAlunoRelVO.getColacaoGrau().equals("") || historicoAlunoRelVO.getColacaoGrau() == null) {
				historicoAlunoRelVO.setTituloCurso(null);
			}
		}

		montarUnidadeEnsinoCurso(historicoAlunoRelVO, matriculaVO, usuarioVO);

		if (Uteis.getIsValorNumerico(tipoLayout)  ||   tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayout.equals("DocumentoIntegralizacaoCurricularRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
			// historicoAlunoRelVO.setCargaHorariaObrigatoriaAtividadeComplementar(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorariaObrigatoria(gradeCurricularVO.getCodigo()).toString());
			historicoAlunoRelVO.setCargaHorariaRealizadaAtividadeComplementar(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), false).toString());
			if (Integer.parseInt(historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar()) > Integer.parseInt(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar())) {
				historicoAlunoRelVO.setCargaHorariaRealizadaAtividadeComplementar(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar());
			}
		} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || Uteis.getIsValorNumerico(tipoLayout)) {
			historicoAlunoRelVO.setCargaHorariaRealizadaAtividadeComplementar(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), true).toString());
		}

		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayout.equals("DocumentoIntegralizacaoCurricularRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL) || Uteis.getIsValorNumerico(tipoLayout)) {
			// historicoAlunoRelVO.setCargaHorariaObrigatoriaEstagio(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaObrigatoriaEstagioPorMatrizCurricular(gradeCurricularVO.getCodigo()));
			historicoAlunoRelVO.setCargaHorariaRealizadaEstagio(getFacadeFactory().getEstagioFacade().consultarCargaHorariaRealizadaEstagioMatricula(matriculaVO.getMatricula()));
		}

		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_POS_18) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_EXCEL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC_EXCEL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
			ordem = OrdemHistoricoDisciplina.PERIODO_LETIVO.getValor();
		}
		historicoAlunoRelVO.setMatriculaVO(matriculaVO);
		List<HistoricoAlunoDisciplinaRelVO> listaAlunoDisciplinaRelVOs = montarHistoricoDisciplinas(matriculaVO, gradeAtualAluno, filtroRelatorioAcademicoVO, ordem, gradeCurricularVO, matriculaVO.getCurso().getNivelEducacionalPosGraduacao(), tipoLayout, apresentarInstituicaoDisciplinaAproveitada, apresentarDisciplinaAnoSemestreTransferenciaGrade, apresentarDisciplinaForaGrade, apresentarApenasUltimoHistoricoDisciplina, apresentarApenasProfessorTitulacaoTurmaOrigem, usuarioVO, regraApresentacaoProfessorDisciplinaAproveitamento, historicoAlunoRelVO, dataInicioTerminoAlteracoesCadastrais, configuracaoLayoutHistoricoVO, xmlDiploma);

		if (matriculaVO.getCurso().getNivelEducacional().equals("ME") || matriculaVO.getCurso().getNivelEducacional().equals("BA") || matriculaVO.getCurso().getNivelEducacional().equals("PR")) {

			montarDadosSituacaoFinal(listaAlunoDisciplinaRelVOs, matriculaVO, desconsiderarSituacaoMatriculaPeriodo, tipoLayout);

			List<HistoricoAlunoDisciplinaRelVO> listaFinal = new ArrayList<HistoricoAlunoDisciplinaRelVO>();

			if (filtroRelatorioAcademicoVO.getAprovado() || filtroRelatorioAcademicoVO.getReprovado() || filtroRelatorioAcademicoVO.getTrancamentoHistorico() || filtroRelatorioAcademicoVO.getAbandonoHistorico() || filtroRelatorioAcademicoVO.getCursando() || filtroRelatorioAcademicoVO.getTransferidoHistorico() || filtroRelatorioAcademicoVO.getCanceladoHistorico()) {
				for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : listaAlunoDisciplinaRelVOs) {
					SituacaoHistorico situacaoHistorico = null;
					if (!matriculaVO.getCurso().getNivelEducacional().equals("PR")) {
						situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoFinal());
					} else {
						situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
					}
					if (situacaoHistorico != null) {
						if (filtroRelatorioAcademicoVO.getAprovado() && situacaoHistorico.getHistoricoAprovado()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getReprovado() && situacaoHistorico.getHistoricoReprovado()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getCursando() && situacaoHistorico.getHistoricoCursando()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getTrancamentoHistorico() && situacaoHistorico.isTrancamento()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getCanceladoHistorico() && situacaoHistorico.isCancelado()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getAbandonoHistorico() && situacaoHistorico.isAbandonoCurso()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						} else if (filtroRelatorioAcademicoVO.getTransferidoHistorico() && situacaoHistorico.isTransferido()) {
							listaFinal.add(historicoAlunoDisciplinaRelVO);
						}
					}
				}
			} else {
				listaFinal.addAll(listaAlunoDisciplinaRelVOs);
			}

			listaAlunoDisciplinaRelVOs.clear();
			listaAlunoDisciplinaRelVOs = listaFinal;
			if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayout.equals("HistoricoAlunoNivelTecnico2Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL)					
					&& (Uteis.getIsValorNumerico(tipoLayout) && matriculaVO.getCurso().getNivelEducacional().equals("ME") || matriculaVO.getCurso().getNivelEducacional().equals("BA") || matriculaVO.getCurso().getNivelEducacional().equals("PR"))) {
				if (!filtroDisciplinasACursar) {
					realizarInclusaoPeriodoLetivoNaoCursadoEnsinoMedio(listaAlunoDisciplinaRelVOs, gradeCurricularVO, matriculaVO.getCurso().getNivelEducacional());
				}
			}

			realizarAlteracaoDescricaoPeriodoLetivoEnsinoFundamental(listaAlunoDisciplinaRelVOs, Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAnoMudancaLegislacao()), tipoLayout);

			historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().clear();
			historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().addAll(listaAlunoDisciplinaRelVOs);

			listaAlunoDisciplinaRelVOs.stream().collect(Collectors.groupingBy(h -> h.getAnoSemstre() + h.getNomePeriodo() + h.getInstituicao()))
			.forEach((k, v) -> {
				if (Uteis.isAtributoPreenchido(v)) {
					v.stream().findFirst().ifPresent(historicoAlunoRelVO.getHistoricoAlunoPeriodoRelVOs()::add);
				}
			}
		);
			
			Ordenacao.ordenarLista(historicoAlunoRelVO.getHistoricoAlunoPeriodoRelVOs(), "anoSemstre");
			if (tipoLayout.equals("DocumentoIntegralizacaoCurricularRel")) {
				Ordenacao.ordenarLista(historicoAlunoRelVO.getHistoricoAlunoPeriodoRelVOs(), "nomePeriodo");
			}
		} else {
			historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().clear();
			if (tipoLayout.equals("DocumentoIntegralizacaoCurricularRel")) {
				Ordenacao.ordenarLista(listaAlunoDisciplinaRelVOs, "nomePeriodo");
			}
	
			if(tipoLayout.equals(HISTORICO_ALUNO_POS_18)) {
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().addAll(listaAlunoDisciplinaRelVOs.stream()
						.filter(s -> !s.getDisciplinaTcc())
						.collect(Collectors.toList()));		
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs().addAll(listaAlunoDisciplinaRelVOs.stream()
						.filter(s -> s.getDisciplinaTcc())
						.collect(Collectors.toList()));	
			}else if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().addAll(listaAlunoDisciplinaRelVOs.stream()
						.filter(s -> !s.getDisciplinaTcc() && !s.getDisciplinaEstagio())
						.collect(Collectors.toList()));		
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs().addAll(listaAlunoDisciplinaRelVOs.stream()
						.filter(s -> s.getDisciplinaTcc())
						.collect(Collectors.toList()));	
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs().addAll(listaAlunoDisciplinaRelVOs.stream()
						.filter(s -> s.getDisciplinaEstagio())
						.collect(Collectors.toList()));				
											
			}else {
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().addAll(listaAlunoDisciplinaRelVOs);
			}
		}

		if (Uteis.getIsValorNumerico(tipoLayout)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
			Iterator<HistoricoAlunoDisciplinaRelVO> iterator = historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().iterator();
			while (iterator.hasNext()) {
				HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO = iterator.next();
				// if (
				// (historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("CA") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC") ||
				// (
				// (historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TR") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("AC") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TI") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TS")
				// ) &&
				// !(historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("AP") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("AA") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("IS") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("CH") ||
				// historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("CC")
				// )
				// )
				// ) &&
				// !historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals("IS")
				// ) {
				// iterator.remove();
				// } else
				if (historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade()) {
					historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaForaGradeRelVOs().add(historicoAlunoDisciplinaRelVO);
					if(!Uteis.getIsValorNumerico(tipoLayout)) {
						iterator.remove();
					}
				}
			}
			// for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO :
			// listaAlunoDisciplinaRelVOs) {
			// if (historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade()) {
			// historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaForaGradeRelVOs().add(historicoAlunoDisciplinaRelVO);
			// }
			// }
		}

		if (Uteis.getIsValorNumerico(tipoLayout)  ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
			if (matriculaVO.getDataProcessoSeletivo() != null && (matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()))) {

				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataProcessoSeletivo()));
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataProcessoSeletivo()));
				int mes = Uteis.getMesData(matriculaVO.getDataProcessoSeletivo());
				String mesStr = String.valueOf(mes);
				if (mes < 10) {
					mesStr = "0" + String.valueOf(mes);
				}
				historicoAlunoRelVO.setMesIngresso(mesStr);

			} else if (matriculaVO.getDataInicioCurso() != null && (!matriculaVO.getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor()) && !matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) && !matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()))) {

				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataInicioCurso()));
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataInicioCurso()));
				int mes = Uteis.getMesData(matriculaVO.getDataInicioCurso());
				String mesStr = String.valueOf(mes);
				if (mes < 10) {
					mesStr = "0" + String.valueOf(mes);
				}
				historicoAlunoRelVO.setMesIngresso(mesStr);

			} else {
				// historicoAlunoRelVO.setAnoIngresso(matriculaPeriodoVO.getAno());
				// historicoAlunoRelVO.setSemestreIngresso(matriculaPeriodoVO.getSemestre());
			}
			historicoAlunoRelVO.getListaHistoricoAlunoAtividadeComplementarRelVOs().clear();
			int cargaHorariaRealizadaAtividadeComplementar = 0;
			for (RegistroAtividadeComplementarMatriculaVO registro : getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistroAtividadeComplementarHistoricoPorMatricula(historicoAlunoRelVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO)) {
				HistoricoAlunoAtividadeComplementarRelVO atividade = new HistoricoAlunoAtividadeComplementarRelVO();
				atividade.setTipoAtividadeComplementar(registro.getTipoAtividadeComplementarVO().getNome());
				atividade.setCargaHorariaObrigatoriaAtividadeComplementar(registro.getCargaHorariaEvento().toString());
				if (registro.getCargaHorariaConsiderada() > registro.getCargaHorariaEvento()) {
					atividade.setCargaHorariaRealizadaAtividadeComplementar(registro.getCargaHorariaEvento().toString());
					cargaHorariaRealizadaAtividadeComplementar += registro.getCargaHorariaEvento();
				} else {
					atividade.setCargaHorariaRealizadaAtividadeComplementar(registro.getCargaHorariaConsiderada().toString());
					cargaHorariaRealizadaAtividadeComplementar += registro.getCargaHorariaConsiderada();
				}
				historicoAlunoRelVO.getListaHistoricoAlunoAtividadeComplementarRelVOs().add(atividade);
			}
			historicoAlunoRelVO.setCargaHorariaRealizadaAtividadeComplementar(cargaHorariaRealizadaAtividadeComplementar + "");
		}

		Integer chDisciplina = 0;
		Integer chCumprida = 0;
		Integer cargaHorariaCursada = 0;
		Integer creditoCursado = 0;
		Integer creditoCumprido = 0;
		Integer chDisciplinaForaGrade = 0;
		Integer cargaHorariaCursadaForaGrade = 0;
		Integer cargaHorariaComplementacaoCargaHoraria = 0;
		Integer cargaHorariaCursadaDisciplina = 0;
		Integer cargaHorariaCursadaDisciplinaOptativa = 0;
		Integer chDisciplinaEstagio = 0;
		Double cargaHorariaMatriculado = 0.0;
		Double cargaHorariaReprovado = 0.0;
		Double cargaHorariaAprovado = 0.0;
		
	    Integer cargaHorariaTotalDisciplina = 0;
	    Integer cargaHorariaTotalEstagio = 0;
	    Integer cargaHorariaTotalTcc = 0;
		Integer cargaHorariaAprovadaDisciplina = 0;
		Integer cargaHorariaAprovadaEstagio = 0;
		Integer cargaHorariaAprovadaTcc = 0;
		
		for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : listaAlunoDisciplinaRelVOs) {
			try {
				if (!historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar()) {
					chDisciplinaForaGrade += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
					if (!historicoAlunoDisciplinaRelVO.getCargaHorariaCursada().equals("--")) {
						if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
							if (historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.ISENTO.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CONCESSAO_CREDITO.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.APROVADO_COM_DEPENDENCIA.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.APROVEITAMENTO_BANCA.getDescricao())) {
								cargaHorariaCursadaForaGrade += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
							}
						} else {
							cargaHorariaCursadaForaGrade += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
						}
					}
				}
				if (historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria()) {
					cargaHorariaComplementacaoCargaHoraria += cargaHorariaComplementacaoCargaHoraria += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
				}
			
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals("DocumentoIntegralizacaoCurricularRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
					if (!historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("AC")
							&& !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("CA") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TS") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TI") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta") && !historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade() && !historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar()) {
						if ((!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) || (((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) && !historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()))) {
							chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
							creditoCumprido += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
						}
					} else if ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANCAMENTO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANSFERIDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.ABANDONO_CURSO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CANCELADO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor())
							&& !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta") && !historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria()
							&& !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar()) {

						chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
						creditoCumprido += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());

					} else if (!historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade() && !historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar() && historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals("IS")) {
						if ((!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) || (((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) && !historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()))) {
							chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
							creditoCumprido += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
						}
					}
					
					if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) && historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()) {
						SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
						if (situacaoHistorico != null && situacaoHistorico.getHistoricoAprovado()) {
							chDisciplinaEstagio += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
						}
					}
					
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC)) {
					if (!historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado")
							&& !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("AC")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TR")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("CA")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TS")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TI")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR")
							// && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC")
							&& !historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar() && !historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade()) {
						if ( tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
							chDisciplina += valorCargaHorariaDisciplinaConformeSituacoes(historicoAlunoDisciplinaRelVO, SituacaoHistorico.getSituacoesDeAprovacao());
							creditoCumprido += valorCreditoDisciplinaConformeSituacoes(historicoAlunoDisciplinaRelVO, SituacaoHistorico.getSituacoesDeAprovacao());
						} else {
							chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
							creditoCumprido += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
						}
					} else if (!historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar() && historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals("IS") && !historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade()) {
						if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
							chDisciplina += valorCargaHorariaDisciplinaConformeSituacoes(historicoAlunoDisciplinaRelVO, SituacaoHistorico.getSituacoesDeAprovacao());
							creditoCumprido += valorCreditoDisciplinaConformeSituacoes(historicoAlunoDisciplinaRelVO, SituacaoHistorico.getSituacoesDeAprovacao());				
						} else {
							chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
							creditoCumprido += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
						}
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
					if (!historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RE") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RF") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RP") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("CS") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Cursando") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().contains("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANCAMENTO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANCAMENTO.getDescricao())
							&& !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.ABANDONO_CURSO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.ABANDONO_CURSO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CANCELADO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CANCELADO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANSFERIDO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.TRANSFERIDO.getDescricao())

							&& !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta")) {
						chCumprida += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
					}
				} else if (tipoLayout.equals("HistoricoAlunoPosRel") || tipoLayout.equals("HistoricoAlunoPos3Rel") || tipoLayout.equals("HistoricoAlunoPos2Rel") || tipoLayout.equals("HistoricoAlunoPos14Rel") || tipoLayout.equals("HistoricoAlunoPos15Rel") || tipoLayout.equals("HistoricoAlunoPos17Rel") || tipoLayout.equals("HistoricoAlunoPos18Rel")   || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
					if (!historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("AC") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("CA") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TS") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TI") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RE") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RF") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("RP") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals("CS") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado")
							&& !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Cursando") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().contains("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta")) {
						chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
					}
				} else {
					chDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
				}
				if (considerarCargaHorariaCursadaIgualCargaHorariaPrevista) {
					historicoAlunoDisciplinaRelVO.setCargaHorariaCursada(Integer.parseInt(historicoAlunoDisciplinaRelVO.getChDisciplina()));
				}
				if (!historicoAlunoDisciplinaRelVO.getCargaHorariaCursada().equals("--")) {
					SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
					if (situacaoHistorico != null && situacaoHistorico.getHistoricoAprovado()) {
						cargaHorariaCursada += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
						creditoCursado += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
						if(historicoAlunoDisciplinaRelVO.getDisciplinaReferenteAUmGrupoOptativa() || historicoAlunoDisciplinaRelVO.getHistoricoDisciplinaOptativa()) {
							cargaHorariaCursadaDisciplinaOptativa += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
						}
						else {
							cargaHorariaCursadaDisciplina += Integer.valueOf(historicoAlunoDisciplinaRelVO.getCargaHorariaCursada());
						}
					}
				}
				if(tipoLayout.equals("HistoricoAlunoPos18Rel")|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
					
					SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
					if(situacaoHistorico.getHistoricoCursando()) {
						cargaHorariaMatriculado += Double.parseDouble(historicoAlunoDisciplinaRelVO.getChDisciplina());
					}						
					else if(situacaoHistorico.getHistoricoReprovado()) {
						cargaHorariaReprovado += Double.parseDouble(historicoAlunoDisciplinaRelVO.getChDisciplina());
					}
					else if(situacaoHistorico.getHistoricoAprovado()) {
						cargaHorariaAprovado+= Double.parseDouble(historicoAlunoDisciplinaRelVO.getChDisciplina());
					}
				}
				
				if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
					
					SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());					
					historicoAlunoDisciplinaRelVO.setHistoricoAprovado(situacaoHistorico.getHistoricoAprovado());
					
					if(historicoAlunoDisciplinaRelVO.getHistoricoAprovado()) {
						
						if(!historicoAlunoDisciplinaRelVO.getDisciplinaEstagio() 
								&& !historicoAlunoDisciplinaRelVO.getDisciplinaTcc()
								&& !historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade()) {					
							cargaHorariaAprovadaDisciplina+= Integer.parseInt(historicoAlunoDisciplinaRelVO.getChDisciplina());

						}	
						else if(historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()) {
							cargaHorariaAprovadaEstagio+= Integer.parseInt(historicoAlunoDisciplinaRelVO.getChDisciplina());

						}	
						else if(historicoAlunoDisciplinaRelVO.getDisciplinaTcc()) {						
							cargaHorariaAprovadaTcc+= Integer.parseInt(historicoAlunoDisciplinaRelVO.getChDisciplina());
							
						}						
					}													
				}

			} catch (Exception e) {

			}
		}
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || Uteis.getIsValorNumerico(tipoLayout) || xmlDiploma) {
			historicoAlunoRelVO.setCargaHorariaRealizadaEstagio(consultarCargaHorariaEstagioRealizadoSomadoDisciplinasEstagioGradeDisciplina(matriculaVO.getMatricula(), listaAlunoDisciplinaRelVOs, xmlDiploma));
		}		
		
		if ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC))) {
			historicoAlunoRelVO.setChDisciplina(chCumprida.toString());
		} else {
			historicoAlunoRelVO.setChDisciplina(chDisciplina.toString());
		}
		historicoAlunoRelVO.setCreditoCursado(creditoCursado);
		historicoAlunoRelVO.setCreditoCumprido(creditoCumprido);
		historicoAlunoRelVO.setCargaHorariaCursada(cargaHorariaCursada);
		historicoAlunoRelVO.setCargaHorariaDisciplinaForaGrade(chDisciplinaForaGrade);
		historicoAlunoRelVO.setCargaHorariaComplementacaoCargaHoraria(cargaHorariaComplementacaoCargaHoraria);
		historicoAlunoRelVO.setCargaHorariaCursadaDisciplinaForaGrade(cargaHorariaCursadaForaGrade);
		historicoAlunoRelVO.setCargaHorariaDisciplinaOptativaCursada(cargaHorariaCursadaDisciplinaOptativa);
		historicoAlunoRelVO.setCargaHorariaDisciplinaEstagioRealizada(chDisciplinaEstagio);
		
		if(tipoLayout.equals(HISTORICO_ALUNO_POS_18) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19) ) {
			gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricularVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			historicoAlunoRelVO.setCargaHorariaMatriculado(cargaHorariaMatriculado);
			historicoAlunoRelVO.setCargaHorariaReprovado(cargaHorariaReprovado);
			historicoAlunoRelVO.setCargaHorariaAprovado(cargaHorariaAprovado);
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
				int chDisciplinasObrigatoriasOptativas = gradeCurricularVO.getCargaHoraria() - gradeCurricularVO.getTotalCargaHorariaEstagio() - gradeCurricularVO.getTotalCargaHorariaAtividadeComplementar();
				historicoAlunoRelVO.setChGradeCurricular(chDisciplinasObrigatoriasOptativas+"");
			} else {
				historicoAlunoRelVO.setChGradeCurricular(gradeCurricularVO.getTotalCargaHorariaDisciplinasObrigatorias().toString());
			}
			if (!Uteis.isAtributoPreenchido(matriculaVO.getPercentualCumprido())) {
				getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matriculaVO, gradeCurricularVO.getCodigo(), usuarioVO, null);
			}
			historicoAlunoRelVO.setPercentualIntegralizacaoCurricular(matriculaVO.getPercentualCumprido());
		}
		
		if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
			
			List<GradeDisciplinaVO> listaGradeDisciplinaRelVOs = new ArrayList<GradeDisciplinaVO>(0);
			
			listaGradeDisciplinaRelVOs = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricular(gradeCurricularVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);

			
			for (GradeDisciplinaVO gradeDisciplinaVO : listaGradeDisciplinaRelVOs) {
				
				if(!gradeDisciplinaVO.getDisciplinaEstagio() && !gradeDisciplinaVO.getDisciplinaTCC()) {
					cargaHorariaTotalDisciplina+= gradeDisciplinaVO.getCargaHoraria();
				}	
				else if(gradeDisciplinaVO.getDisciplinaEstagio()) {
					cargaHorariaTotalEstagio+= gradeDisciplinaVO.getCargaHoraria();
				}	
				else if(gradeDisciplinaVO.getDisciplinaTCC()) {
					cargaHorariaTotalTcc+= gradeDisciplinaVO.getCargaHoraria();
				}
			}			
			
			if(Uteis.isAtributoPreenchido(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs())) {
				historicoAlunoRelVO.setExisteDisplinasEstagio(Boolean.TRUE);			
			}else {
				cargaHorariaTotalEstagio = 0;
				List<GradeCurricularEstagioVO> listaGradeCurricularEstagioVOs = new ArrayList<GradeCurricularEstagioVO>(0);
				listaGradeCurricularEstagioVOs = getFacadeFactory().getGradeCurricularEstagioFacade().consultarPorGradeCurricularMatriculaHistoricoRel(gradeCurricularVO.getCodigo(), matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				
				historicoAlunoRelVO.getListaHistoricoAlunoComponenteEstagioRelVOs().addAll(listaGradeCurricularEstagioVOs);
				
				if(Uteis.isAtributoPreenchido(historicoAlunoRelVO.getListaHistoricoAlunoComponenteEstagioRelVOs())) {
					historicoAlunoRelVO.setExisteComponentesEstagio(Boolean.TRUE);
					for (GradeCurricularEstagioVO gradeCurricularEstagioVO : historicoAlunoRelVO.getListaHistoricoAlunoComponenteEstagioRelVOs()) {						
						cargaHorariaTotalEstagio += gradeCurricularEstagioVO.getCargaHorarioObrigatorio();
						cargaHorariaAprovadaEstagio += gradeCurricularEstagioVO.getCargaHorariaAprovada();
						
					}
				}
				
			}
			
			historicoAlunoRelVO.setCargaHorariaTotalDisciplina(cargaHorariaTotalDisciplina);
			historicoAlunoRelVO.setCargaHorariaAprovadaDisciplina(cargaHorariaAprovadaDisciplina);
			historicoAlunoRelVO.setCargaHorariaPendenteDisciplina(cargaHorariaTotalDisciplina - cargaHorariaAprovadaDisciplina);
			historicoAlunoRelVO.setCargaHorariaTotalEstagio(cargaHorariaTotalEstagio);
			historicoAlunoRelVO.setCargaHorariaAprovadaEstagio(cargaHorariaAprovadaEstagio);
			historicoAlunoRelVO.setCargaHorariaPendenteEstagio(cargaHorariaTotalEstagio - cargaHorariaAprovadaEstagio);
			historicoAlunoRelVO.setCargaHorariaTotalTcc(cargaHorariaTotalTcc);	
			historicoAlunoRelVO.setCargaHorariaAprovadaTcc(cargaHorariaAprovadaTcc);
			historicoAlunoRelVO.setCargaHorariaPendenteTcc(cargaHorariaTotalTcc - cargaHorariaAprovadaTcc);
		}
		validarCargaHorariaEstagioUtilizarXML(historicoAlunoRelVO, tipoLayout);
		if (tipoLayout.equals("HistoricoAlunoPos3Rel") || tipoLayout.equals("HistoricoAlunoPos3ExcelRel") || tipoLayout.equals("HistoricoAlunoPos2Rel") || tipoLayout.equals("HistoricoAlunoPos2ExcelRel") || tipoLayout.equals("HistoricoAlunoPos14Rel") || tipoLayout.equals("HistoricoAlunoPos15Rel") || tipoLayout.equals("HistoricoAlunoPos17Rel") || tipoLayout.equals("HistoricoAlunoPos17ExcelRel") || tipoLayout.equals("HistoricoAlunoPos18Rel") || tipoLayout.equals("HistoricoAlunoPos18ExcelRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19) ) {
			historicoAlunoRelVO.setChCumprida(chDisciplina.toString());
		} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
			historicoAlunoRelVO.setChCumprida(chCumprida.toString());
		} else if (tipoLayout.equals(HISTORICO_ALUNO_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
			Integer cargaHorariaCumprida = Integer.valueOf(historicoAlunoRelVO.getChCumprida());
			Integer cargaHorariaExigida = Integer.valueOf(historicoAlunoRelVO.getChExigida());
			Integer cargaHorariaCumpridaTotal = cargaHorariaCumprida + Integer.valueOf(historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar());
			historicoAlunoRelVO.setChCumprida(cargaHorariaCumpridaTotal >= cargaHorariaExigida ? cargaHorariaExigida.toString() : cargaHorariaCumpridaTotal.toString());
		} else if (tipoLayout.equals("HistoricoAlunoLayout14Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC)) {
		
			Integer cargaHorariaCurs = Integer.valueOf(historicoAlunoRelVO.getCargaHorariaCursada().equals("--") ? "0" :  historicoAlunoRelVO.getCargaHorariaCursada()  );
			Integer cargaHorariaEstagio = historicoAlunoRelVO.getCargaHorariaRealizadaEstagio();
			Integer cargaHorariaAtComp = Integer.valueOf(historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar().equals("--") ? "0" : historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar());
			Integer total = cargaHorariaCurs + cargaHorariaEstagio + cargaHorariaAtComp;
			historicoAlunoRelVO.setCargaHorariaCursada(total);			
		}
		if (filtroDisciplinasACursar) {
			// consulta as disciplinas a cursar.
			boolean utilizarNomeCertificacaoPeriodoLetivo = tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2) || tipoLayout.equals("DiplomaAlunoMedio2RelVerso") || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayout.equals("HistoricoAlunoNivelTecnico2Rel") || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3);
			getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaHistoricoDisciplinaACursar(historicoAlunoRelVO.getMatricula(), ordem, historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), utilizarNomeCertificacaoPeriodoLetivo, usuarioVO);
			if (tipoLayout.equals(HISTORICO_ALUNO_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
				for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
					historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelVO.getSituacaoFinal()));
				}
			} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)) {
				Ordenacao.ordenarLista(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), "numeroPeriodoLetivo");
			}
		}
		/**
		 * Criado para a ABEU pois existem matricula periodo trancada, cancelada,
		 * abandonada e transferida que não possuem disciplinas no histórico, porém
		 * devem ser apresentadadas no historico com a sua devida situação, este está
		 * habilitado para o LAYOUT 4 e quando a Situacao Disciplina for TODAS
		 */
		realizarBuscaMatriculaPeriodoEvasaoSemVinculoComHistorico(historicoAlunoRelVO, tipoLayout, filtroRelatorioAcademicoVO, matriculaVO, gradeCurricularVO,ordem);
		// lista.add(historicoAlunoRelVO);
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)) {
			if (Uteis.isAtributoPreenchido(gradeCurricularVO.getSistemaAvaliacao())) {
				historicoAlunoRelVO.setPeriodicidadeCurso(gradeCurricularVO.getSistemaAvaliacao());
			}
			if (matriculaVO.getDataProcessoSeletivo() != null && (matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()))) {

				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataProcessoSeletivo()));
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataProcessoSeletivo()));
				int mes = Uteis.getMesData(matriculaVO.getDataProcessoSeletivo());
				String mesStr = String.valueOf(mes);
				if (mes < 10) {
					mesStr = "0" + String.valueOf(mes);
				}
				historicoAlunoRelVO.setMesIngresso(mesStr);

			}
			Double chdis = ((new Double(historicoAlunoRelVO.getChDisciplina()) / 60) * 50);
			String chdisStr = chdis.toString();
			if (chdisStr.contains(".")) {
				chdisStr = chdisStr.substring(0, chdisStr.indexOf("."));
			}
			historicoAlunoRelVO.setChDisciplina(chdisStr + "");
			historicoAlunoRelVO.setChGradeCurricular(gradeCurricularVO.getCargaHoraria().toString());
		}
		historicoAlunoRelVO.setNomeMatriz(gradeCurricularVO.getNome());
		historicoAlunoRelVO.setTotalcargahorariaatividadecomplementar(Integer.toString(gradeCurricularVO.getTotalCargaHorariaAtividadeComplementar()));
				
		if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
			TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO = new TransferenciaMatrizCurricularVO();
			transferenciaMatrizCurricularVO = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorMatriculaCodigoGradeAtual(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), usuarioVO);
			
			if(Uteis.isAtributoPreenchido(transferenciaMatrizCurricularVO)) {
				List<HistoricoAlunoDisciplinaRelVO> listaAlunoDisciplinaRelMatrizAnteriorVOs = montarHistoricoDisciplinas(matriculaVO, false, filtroRelatorioAcademicoVO, ordem, transferenciaMatrizCurricularVO.getGradeOrigem(), matriculaVO.getCurso().getNivelEducacionalPosGraduacao(), tipoLayout, apresentarInstituicaoDisciplinaAproveitada, apresentarDisciplinaAnoSemestreTransferenciaGrade, apresentarDisciplinaForaGrade, apresentarApenasUltimoHistoricoDisciplina, apresentarApenasProfessorTitulacaoTurmaOrigem, usuarioVO, regraApresentacaoProfessorDisciplinaAproveitamento, historicoAlunoRelVO, dataInicioTerminoAlteracoesCadastrais, configuracaoLayoutHistoricoVO, xmlDiploma);
				
				for(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelAtual : historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
					for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelAnterior : listaAlunoDisciplinaRelMatrizAnteriorVOs) {
						if(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelAtual.getSituacaoHistoricoApresentarAdaptacaoMatriz())) {
							if(historicoAlunoDisciplinaRelAtual.getCodigoDisciplina().equals(historicoAlunoDisciplinaRelAnterior.getCodigoDisciplina())) {
								if(historicoAlunoDisciplinaRelAtual.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO.getDescricao()) || historicoAlunoDisciplinaRelAtual.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO.getValor()) || historicoAlunoDisciplinaRelAtual.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao()) || historicoAlunoDisciplinaRelAtual.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor())){
									historicoAlunoDisciplinaRelAtual.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao("AA"));
								}else {
									historicoAlunoDisciplinaRelAtual.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelAtual.getSituacaoHistorico()));
								}
								break;
							}else {
								historicoAlunoDisciplinaRelAtual.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelAtual.getSituacaoHistorico()));
							}
						}
					}
				}				
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().addAll(listaAlunoDisciplinaRelMatrizAnteriorVOs);			
						
				
				Collections.sort(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), Comparator.comparing(HistoricoAlunoDisciplinaRelVO::getAnoSemstre)
			            .thenComparing(h -> h.getGradeCurricularVO().getCodigo()));		
						
			}
			
			
		}
		
		return historicoAlunoRelVO;
	}

	public void montarUnidadeEnsinoCurso(HistoricoAlunoRelVO historicoAlunoRelVO, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), usuarioVO);
		if (!unidadeEnsinoCursoVO.getMantenedora().equals("")) {
			historicoAlunoRelVO.setMantenedora(unidadeEnsinoCursoVO.getMantenedora());
		} else {
			historicoAlunoRelVO.setMantenedora(matriculaVO.getUnidadeEnsino().getMantenedora());
		}
		if (!unidadeEnsinoCursoVO.getMantida().equals("")) {
			historicoAlunoRelVO.setMantida(unidadeEnsinoCursoVO.getMantida());
		}
	}

	public void realizarAlteracaoDescricaoPeriodoLetivoEnsinoFundamental(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs, String anoLegislacao, String tipoLayout) throws Exception {
		if ((tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout1Rel")) && anoLegislacao != null && !anoLegislacao.trim().isEmpty()) {
			int menorPeriodoLetivo = 100;

			Map<Integer, String> periodoLetivoVOs = new HashMap<Integer, String>(0);
			for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoDisciplinaRelVOs) {

				if (historicoAlunoDisciplinaRelVO.getAnoSemstre() != null && !historicoAlunoDisciplinaRelVO.getAnoSemstre().equals("")) {
					Integer anoHistorico = historicoAlunoDisciplinaRelVO.getAnoSemstre().contains("/") ? Integer.valueOf(historicoAlunoDisciplinaRelVO.getAnoSemstre().substring(0, 4)) : Integer.valueOf(historicoAlunoDisciplinaRelVO.getAnoSemstre());
					if (historicoAlunoDisciplinaRelVO.getAnoSemstre() != null && anoHistorico < Integer.valueOf(anoLegislacao) && !periodoLetivoVOs.containsKey(historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo())) {
						if (menorPeriodoLetivo > historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo()) {
							menorPeriodoLetivo = historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo();
						}
						periodoLetivoVOs.put(historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo(), historicoAlunoDisciplinaRelVO.getNomePeriodo());
					}
				}

			}
			if (menorPeriodoLetivo != 100) {
				for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoDisciplinaRelVOs) {
					if (periodoLetivoVOs.containsKey(historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo()) || (historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo() < menorPeriodoLetivo && historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo() > 0)) {
						historicoAlunoDisciplinaRelVO.setNomePeriodo((historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo()) + "ª SÉRIE");
					}
				}
			}
		}

	}

	/**
	 * Método responsável por montar as disciplinas equivalentes da transferencia de
	 * grade.
	 * 
	 * @param historicoVOs
	 *            Lista de Historico do aluno com todas as disciplinas da grade.
	 * @param matricula
	 * @param gradeAtual
	 *            Verifica se a grade é atual de acordo com a grade na matricula
	 *            periodo do mesmo
	 * @param usuarioVO
	 * @throws Exception
	 * @author Carlos
	 */
	public void executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoHistoricoGradeMigradaEquivalenteTransferenciaGrade(List<HistoricoVO> historicoVOs, String matricula, Integer gradeAtual, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, UsuarioVO usuarioVO) throws Exception {
		Boolean usarSituacaoAprovadoAproveitamento = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorMatriculaCurso(matricula, usuarioVO);
		if (usarSituacaoAprovadoAproveitamento) {
			List<HistoricoGradeMigradaEquivalenteVO> historicoGradeMigradaEquivalenteVOs = getFacadeFactory().getHistoricoGradeMigradaEquivalenteFacade().consultarPorMatriculaHistoricoGradeMigradaEquivalenteTransferidasGrade(matricula, gradeAtual, usuarioVO);
			if (!historicoGradeMigradaEquivalenteVOs.isEmpty()) {
				for (HistoricoGradeMigradaEquivalenteVO historicoGradeMigradaEquivalenteVO : historicoGradeMigradaEquivalenteVOs) {
					for (HistoricoVO historicoVO : historicoVOs) {
						if (historicoVO.getDisciplina().getCodigo().equals(historicoGradeMigradaEquivalenteVO.getDisciplinaVO().getCodigo())) {
							historicoVO.setSituacao("AA");
							getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(historicoGradeMigradaEquivalenteVO.getMatriculaPeriodoApresentarHistoricoVO(), NivelMontarDados.BASICO, null, usuarioVO);
							historicoVO.setMatriculaPeriodo(historicoGradeMigradaEquivalenteVO.getMatriculaPeriodoApresentarHistoricoVO());
							if (apresentarDisciplinaAnoSemestreTransferenciaGrade) {
								historicoVO.setAnoHistorico(historicoGradeMigradaEquivalenteVO.getMatriculaPeriodoApresentarHistoricoVO().getAno());
								historicoVO.setSemestreHistorico(historicoGradeMigradaEquivalenteVO.getMatriculaPeriodoApresentarHistoricoVO().getSemestre());
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Método responsável por montar as disciplinas iguais que o aluno possuia na
	 * grade anterior.
	 * 
	 * @param historicoVOs
	 * @param matricula
	 * @param gradeAtual
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoTransferenciaGrade(List<HistoricoVO> historicoVOs, String matricula, Integer gradeAtual, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, UsuarioVO usuarioVO) throws Exception {
		Boolean usarSituacaoAprovadoAproveitamento = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorMatriculaCurso(matricula, usuarioVO);
		if (usarSituacaoAprovadoAproveitamento) {
			List<HistoricoGradeVO> historicoGradeVOs = getFacadeFactory().getHistoricoGradeFacade().consultarPorMatriculaGradeMigrarAlunoDisciplinasIguaisTransferidasGrade(matricula, gradeAtual, usuarioVO);
			if (!historicoGradeVOs.isEmpty()) {
				for (HistoricoGradeVO historicoGradeVO : historicoGradeVOs) {
					for (HistoricoVO historicoVO : historicoVOs) {
						if (historicoVO.getMatriculaPeriodoTurmaDisciplina().getDisciplinaEquivale()) {

							if (historicoVO.getDisciplina().getCodigo().equals(historicoGradeVO.getDisciplinaEquivalenteVO().getCodigo()) && historicoVO.getSituacao().equals(historicoGradeVO.getSituacao()) && !historicoVO.getSituacao().equals("TR")) {
								historicoVO.setSituacao("AA");
								getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO(), NivelMontarDados.BASICO, null, usuarioVO);
								historicoVO.setMatriculaPeriodo(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO());
								historicoVO.setDisciplinaIgualGradeAnterior(Boolean.TRUE);
								if (apresentarDisciplinaAnoSemestreTransferenciaGrade) {
									historicoVO.setAnoHistorico(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO().getAno());
									historicoVO.setSemestreHistorico(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO().getSemestre());
								}
								break;
							}
						} else {
							if (historicoVO.getDisciplina().getCodigo().equals(historicoGradeVO.getDisciplinaVO().getCodigo()) && historicoVO.getSituacao().equals(historicoGradeVO.getSituacao()) && !historicoVO.getSituacao().equals("TR")) {
								historicoVO.setSituacao("AA");
								getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO(), NivelMontarDados.BASICO, null, usuarioVO);
								historicoVO.setMatriculaPeriodo(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO());
								historicoVO.setDisciplinaIgualGradeAnterior(Boolean.TRUE);
								if (apresentarDisciplinaAnoSemestreTransferenciaGrade) {
									historicoVO.setAnoHistorico(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO().getAno());
									historicoVO.setSemestreHistorico(historicoGradeVO.getMatriculaPeriodoApresentarHistoricoVO().getSemestre());
								}
								break;
							}
						}
					}
				}
			}
		}
	}

	public HistoricoVO realizarCriacaoHistoricoDisciplinaForaGrade(MatriculaVO matriculaVO, DisciplinaForaGradeVO disciplinaForaGradeVO, UsuarioVO usuarioVO) throws Exception {
		HistoricoVO obj = new HistoricoVO();
		obj.setMatricula(matriculaVO);
		obj.setAnoHistorico(disciplinaForaGradeVO.getAno());
		obj.setSemestreHistorico(disciplinaForaGradeVO.getSemestre());
		obj.setMatriculaPeriodo(new MatriculaPeriodoVO());
		obj.getMatriculaPeriodo().setPeridoLetivo(disciplinaForaGradeVO.getPeriodoLetivo());
		obj.getMatriculaPeriodo().setAno(disciplinaForaGradeVO.getAno());
		obj.getMatriculaPeriodo().setSemestre(disciplinaForaGradeVO.getSemestre());
		obj.getMatriculaPeriodo().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor());
		obj.setCreditoDisciplina(disciplinaForaGradeVO.getNumeroCredito());
		obj.getDisciplina().setNome(disciplinaForaGradeVO.getDisciplina());
		obj.setMediaFinal(disciplinaForaGradeVO.getNota());
		obj.setUtilizaNotaFinalConceito(disciplinaForaGradeVO.getUsarNotaConceito());
		obj.setNotaFinalConceito(disciplinaForaGradeVO.getNotaConceito());
		obj.setFreguencia(disciplinaForaGradeVO.getFrequencia());
		obj.setSituacao(disciplinaForaGradeVO.getSituacao());
		obj.getGradeDisciplinaVO().setCargaHoraria(disciplinaForaGradeVO.getCargaHoraria());
		obj.getGradeDisciplinaVO().setNrCreditos(disciplinaForaGradeVO.getNumeroCredito());
		obj.setCargaHorariaCursada(disciplinaForaGradeVO.getCargaHorariaCursada());
		obj.setHistoricoDisciplinaForaGrade(Boolean.TRUE);
		obj.setInstituicao(disciplinaForaGradeVO.getInstituicaoEnsino());
		obj.setCidade(disciplinaForaGradeVO.getCidade().getNome());
		obj.setEstado(disciplinaForaGradeVO.getCidade().getEstado().getSigla());
		obj.setHistoricoDisciplinaForaGrade(true);
		return obj;
	}

	public void executarInclusaoDisciplinaForaGradeHistorico(MatriculaVO matriculaVO, List<HistoricoVO> historicoVOs, UsuarioVO usuarioVO) throws Exception {
		List<InclusaoDisciplinaForaGradeVO> inclusaoDisciplinaForaGradeVOs = getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), usuarioVO);
		List<HistoricoVO> listaHistoricoForaGradeVOs = new ArrayList<HistoricoVO>(0);
		for (InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO : inclusaoDisciplinaForaGradeVOs) {
			getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().carregarDados(inclusaoDisciplinaForaGradeVO, NivelMontarDados.TODOS, usuarioVO);
			for (DisciplinaForaGradeVO disciplinaForaGradeVO : inclusaoDisciplinaForaGradeVO.getDisciplinaForaGradeVOs()) {
				listaHistoricoForaGradeVOs.add(realizarCriacaoHistoricoDisciplinaForaGrade(inclusaoDisciplinaForaGradeVO.getMatriculaVO(), disciplinaForaGradeVO, usuarioVO));
			}
		}
		historicoVOs.addAll(listaHistoricoForaGradeVOs);
	}

	/**
	 * O objetivo desse metodo é resolver uma situação causada pela importação de
	 * dados da Instituição PROCESSUS BSB. Ele realiza uma varredura na listagem de
	 * histórico, crusando com a grade disciplina do curso e removendo possíveis
	 * duplicidades. Resp: Thyago Jayme Data: 08/03/2014
	 * 
	 * @param historicoVOs
	 * @throws Exception
	 */
	private void verificarDuplicacaoDisciplinaHistoricoPROCESSUSLayout5(List<HistoricoVO> historicoVOs) throws Exception {
		List<HistoricoVO> listaRemover = new ArrayList<>();

		Iterator<HistoricoVO> i = historicoVOs.iterator();
		while (i.hasNext()) {
			HistoricoVO hist = (HistoricoVO) i.next();
			Integer codHist = hist.getCodigo().intValue();
			Integer codGrad = hist.getGradeDisciplinaVO().getCodigo().intValue();
			Iterator<HistoricoVO> j = historicoVOs.iterator();
			while (j.hasNext()) {
				HistoricoVO h = (HistoricoVO) j.next();
				if (h.getCodigo().intValue() == codHist && h.getGradeDisciplinaVO().getCodigo().intValue() != codGrad) {
					// / verificar hist esta na base.
					Integer codPer = getFacadeFactory().getHistoricoFacade().consultarPeriodoLetivoPorCodigoHistorico(hist.getCodigo());// 108
					if (hist.getMatriculaPeriodo().getPeridoLetivo().getCodigo().intValue() != codPer) {
						listaRemover.add(hist);
						break;
					}
				}
			}
		}
		Iterator<HistoricoVO> o = listaRemover.iterator();
		while (o.hasNext()) {
			HistoricoVO histo = (HistoricoVO) o.next();
			Iterator<HistoricoVO> k = historicoVOs.iterator();
			int posicao = 0;
			while (k.hasNext()) {
				HistoricoVO histAtual = (HistoricoVO) k.next();
				if (histo.getCodigo().intValue() == histAtual.getCodigo().intValue() && histo.getGradeDisciplinaVO().getCodigo().intValue() == histAtual.getGradeDisciplinaVO().getCodigo().intValue()) {
					int qtdHis = 0;
					Iterator<HistoricoVO> p = historicoVOs.iterator();
					while (p.hasNext()) {
						HistoricoVO u = (HistoricoVO) p.next();
						if (u.getCodigo().intValue() == histAtual.getCodigo().intValue()) {
							qtdHis++;
						}
					}
					if (qtdHis >= 2) {
						historicoVOs.remove(posicao);
					}
					break;
				}
				posicao++;
			}
		}
	}

	private List<HistoricoAlunoDisciplinaRelVO> montarHistoricoDisciplinas(MatriculaVO matriculaVO, boolean gradeAtualAluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int ordem, GradeCurricularVO gradeCurricularVO, boolean posGraduacao, String tipoLayout, Boolean apresentarInstituicaoDisciplinaAproveitada, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, Boolean apresentarDisciplinaForaGrade, boolean apresentarApenasUltimoHistoricoDisciplina, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, UsuarioVO usuarioVO, String regraApresentacaoProfessorDisciplinaAproveitamento, HistoricoAlunoRelVO historicoAlunoRelVO, Boolean dataInicioTerminoAlteracoesCadastrais, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {

		List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		List<HistoricoVO> historicoVOs = null;
		List<HistoricoVO> historicoDisciplinasForaGradeVOs = new ArrayList<HistoricoVO>(0);
		List<HistoricoGradeMigradaEquivalenteVO> historicoGradeDisciplinasEquivalentesMigrarVOs = new ArrayList<HistoricoGradeMigradaEquivalenteVO>(0);
		if (gradeAtualAluno) {
			historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), ordem, apresentarDisciplinaForaGrade, null, false, NivelMontarDados.BASICO, apresentarApenasUltimoHistoricoDisciplina, xmlDiploma, usuarioVO);
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
				verificarDuplicacaoDisciplinaHistoricoPROCESSUSLayout5(historicoVOs);
			}
			if (apresentarDisciplinaForaGrade) {
				executarInclusaoDisciplinaForaGradeHistorico(matriculaVO, historicoVOs, usuarioVO);
			}
			historicoDisciplinasForaGradeVOs = getFacadeFactory().getHistoricoFacade().consultaHistoricoDisciplinasForaGradeRapidaPorMatricula(matriculaVO, ordem, false, NivelMontarDados.BASICO, usuarioVO);
			// historicoGradeDisciplinasEquivalentesMigrarVOs =
			// getFacadeFactory().getHistoricoGradeMigradaEquivalenteFacade().consultarPorMatriculaHistoricoGradeMigradaEquivalenteTransferidasGrade(matriculaVO.getMatricula(),
			// gradeCurricularVO.getCodigo(), usuarioVO);
			// executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoTransferenciaGrade(historicoVOs,
			// matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(),
			// apresentarDisciplinaAnoSemestreTransferenciaGrade, usuarioVO);
			// executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoHistoricoGradeMigradaEquivalenteTransferenciaGrade(historicoVOs,
			// matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(),
			// apresentarDisciplinaAnoSemestreTransferenciaGrade, usuarioVO);
		} else {
			historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaHistoricoTransferenciaMatrizCurricular(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), xmlDiploma, usuarioVO);
			if (historicoVOs.isEmpty()) {
				historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), ordem, apresentarDisciplinaForaGrade, null, false, NivelMontarDados.BASICO, apresentarApenasUltimoHistoricoDisciplina, xmlDiploma, usuarioVO);
				if (apresentarDisciplinaForaGrade) {
					executarInclusaoDisciplinaForaGradeHistorico(matriculaVO, historicoVOs, usuarioVO);
				}
			}
			executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoTransferenciaGrade(historicoVOs, matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), apresentarDisciplinaAnoSemestreTransferenciaGrade, usuarioVO);
			executarAlteracaoSituacaoDisciplinaAprovadoAproveitamentoHistoricoGradeMigradaEquivalenteTransferenciaGrade(historicoVOs, matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), apresentarDisciplinaAnoSemestreTransferenciaGrade, usuarioVO);
		}

		for (HistoricoVO historicoDisciplinasForaGrade : historicoDisciplinasForaGradeVOs) {
			for (int j = 0; j < historicoVOs.size(); j++) {
				HistoricoVO historicoVO = historicoVOs.get(j);
				if (historicoVO.getDisciplina().getCodigo().equals(historicoDisciplinasForaGrade.getDisciplina().getCodigo())) {
					break;
				}
				if (j + 1 == historicoVOs.size()) {
					historicoVOs.add(historicoDisciplinasForaGrade);
				}
			}
		}

		for (HistoricoGradeMigradaEquivalenteVO historicoGradeDisciplinasEquivalentesMigrar : historicoGradeDisciplinasEquivalentesMigrarVOs) {
			for (int j = 0; j < historicoVOs.size(); j++) {
				HistoricoVO historicoVO = historicoVOs.get(j);
				if (historicoVO.getDisciplina().getCodigo().equals(historicoGradeDisciplinasEquivalentesMigrar.getDisciplinaVO().getCodigo())) {
					break;
				}
				if (j + 1 == historicoVOs.size()) {
					HistoricoVO hist = new HistoricoVO();
					hist.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(historicoGradeDisciplinasEquivalentesMigrar.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, usuarioVO));
					getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(historicoGradeDisciplinasEquivalentesMigrar.getMatriculaPeriodoApresentarHistoricoVO(), null, usuarioVO);
					hist.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorCodigoPeriodoLetivoCodigoDisciplina(historicoGradeDisciplinasEquivalentesMigrar.getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(), historicoGradeDisciplinasEquivalentesMigrar.getDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
					hist.setMatriculaPeriodo(historicoGradeDisciplinasEquivalentesMigrar.getMatriculaPeriodoApresentarHistoricoVO());
					hist.setSituacao(historicoGradeDisciplinasEquivalentesMigrar.getSituacao());
					hist.setMediaFinal(historicoGradeDisciplinasEquivalentesMigrar.getMediaFinal());
					hist.setFreguencia(100.00);
					hist.setCargaHorariaCursada(hist.getGradeDisciplinaVO().getCargaHoraria());
					hist.setInstituicao(matriculaVO.getUnidadeEnsino().getNomeExpedicaoDiploma());
					hist.setCidade(matriculaVO.getUnidadeEnsino().getCidade().getNome());
					hist.setEstado(matriculaVO.getUnidadeEnsino().getCidade().getEstado().getNome());
					historicoVOs.add(hist);
				}
			}
		}
		if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2)) {
			Ordenacao.ordenarLista(historicoVOs, "disciplinaOrdenacao");
		} else if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
			Ordenacao.ordenarLista(historicoVOs, "gradeDisciplinaOrdemOrdenacao");
		} else if (tipoLayout.equals("HistoricoAlunoPos15Rel")) {
			Ordenacao.ordenarLista(historicoVOs, "anoSemestreOrdenacao");
		} else {
			switch (OrdemHistoricoDisciplina.getEnum(ordem)) {
			case ANO_SEMESTRE:
				Ordenacao.ordenarLista(historicoVOs, "anoSemestreOrdenacao");
				break;
			case PERIODO_LETIVO:
				Ordenacao.ordenarLista(historicoVOs, "periodoLetivoOrdenacao");
				break;
			case DATA_PROGRAMACAO:
				Ordenacao.ordenarLista(historicoVOs, "data");
				break;
			default:
				break;
			}
		}

		HashMap<Integer, Integer> mapTotalDiaLetivoAno = new HashMap<Integer, Integer>(0);

		// FiltroSituacaoDisciplina filtroEnum =
		// FiltroSituacaoDisciplina.getEnum(filtro);
		if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2) || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel")) {
			filtroRelatorioAcademicoVO.realizarMarcarTodasSituacoesHistorico();
			// filtroEnum = FiltroSituacaoDisciplina.TODAS;
		}

		if ((tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3)) || (Uteis.getIsValorNumerico(tipoLayout) && matriculaVO.getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor()))) {
			realizarExclusaoHistoricoMesmoPeriodoLetivoCanceladoAbandonoCurso(historicoVOs, matriculaVO.getMatricula(), apresentarDisciplinaForaGrade);
		}
		
		
		if(!tipoLayout.equals("HistoricoAlunoPos18Rel") &&  !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)&& !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {							
			obterPeriodoCursado(matriculaVO, historicoAlunoRelVO, usuarioVO, historicoVOs, tipoLayout, dataInicioTerminoAlteracoesCadastrais);
		}			
		
		List<HistoricoVO> listaHistoricosCoeficienteRendimentoAcumulado = new ArrayList<HistoricoVO>();
		listaHistoricosCoeficienteRendimentoAcumulado.addAll(historicoVOs);
		Map<String, PeriodoLetivoAtivoUnidadeEnsinoCursoVO> mapPeriodoLetivoAtivoUnidadeEnsinoCursoVO = new HashMap<String, PeriodoLetivoAtivoUnidadeEnsinoCursoVO>(0);
		for(HistoricoVO p : historicoVOs) {
			try {
				if (p.getSituacao().equals("AE")) {
					if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
						p.setSituacao("AP");
					}
				} else if (p.getSituacao().equals("CE")) {
					if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) {
						p.setSituacao("CS");
					}
				} else if (p.getSituacao().equals("CO") || p.getSituacao().equals("CE")) {
					if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
						p.setSituacao("CS");
					}
				}
				SituacaoHistorico situacao = SituacaoHistorico.getEnum(p.getSituacao());
				if ((!filtroRelatorioAcademicoVO.getAprovado() && !filtroRelatorioAcademicoVO.getReprovado() && !filtroRelatorioAcademicoVO.getCursando() && !filtroRelatorioAcademicoVO.getTrancamentoHistorico() && !filtroRelatorioAcademicoVO.getAbandonoHistorico() && !filtroRelatorioAcademicoVO.getCanceladoHistorico() && !filtroRelatorioAcademicoVO.getTransferidoHistorico() && !filtroRelatorioAcademicoVO.getPreMatriculaHistorico() && !filtroRelatorioAcademicoVO.getJubilado()) || (filtroRelatorioAcademicoVO.getAprovado() && situacao.getHistoricoAprovado()) || (filtroRelatorioAcademicoVO.getReprovado() && situacao.getHistoricoReprovado()) || (filtroRelatorioAcademicoVO.getCursando() && situacao.getHistoricoCursando()) || (filtroRelatorioAcademicoVO.getTrancamentoHistorico() && situacao.isTrancamento()) || (filtroRelatorioAcademicoVO.getAbandonoHistorico() && situacao.isAbandonoCurso()) || (filtroRelatorioAcademicoVO.getCanceladoHistorico() && situacao.isCancelado())
						|| (filtroRelatorioAcademicoVO.getTransferidoHistorico() && situacao.isTransferido()) || (filtroRelatorioAcademicoVO.getPreMatriculaHistorico() && situacao.isPreMatricula()) || (filtroRelatorioAcademicoVO.getJubilado() && situacao.isJubilado())) {
					if(!mapPeriodoLetivoAtivoUnidadeEnsinoCursoVO.containsKey(p.getAnoSemestreApresentar())) {
						mapPeriodoLetivoAtivoUnidadeEnsinoCursoVO.put(p.getAnoSemestreApresentar(), getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(p.getAnoHistorico(), p.getSemestreHistorico(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					}
					p.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(mapPeriodoLetivoAtivoUnidadeEnsinoCursoVO.get(p.getAnoSemestreApresentar()));
					historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(matriculaVO, p, gradeCurricularVO, posGraduacao, tipoLayout, apresentarInstituicaoDisciplinaAproveitada, apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno, usuarioVO, listaHistoricosCoeficienteRendimentoAcumulado, regraApresentacaoProfessorDisciplinaAproveitamento,ordem, gradeAtualAluno, historicoAlunoRelVO.getTrazerTodosProfessoresDisciplinas(), configuracaoLayoutHistoricoVO, xmlDiploma));
				}								
			
			} catch (Exception e) {
				throw e;
			}
		};

		/*
		 * FiltroSituacaoDisciplina filtroEnum = FiltroSituacaoDisciplina.getEnum(1);
		 * for (HistoricoVO historicoVO : historicoVOs) { if
		 * (historicoVO.getSituacao().equals("AE")) { historicoVO.setSituacao("AP"); }
		 * else if (historicoVO.getSituacao().equals("CE")) {
		 * historicoVO.setSituacao("CS"); } else if
		 * (historicoVO.getSituacao().equals("CO") ||
		 * historicoVO.getSituacao().equals("CE")) { if
		 * (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) ||
		 * tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) ||
		 * tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) ||
		 * tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL)) {
		 * historicoVO.setSituacao("CS"); } } SituacaoHistorico situacao =
		 * SituacaoHistorico.getEnum(historicoVO.getSituacao()); switch (filtroEnum) {
		 * case TODAS:
		 * historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(
		 * matriculaVO, historicoVO, gradeCurricularVO, posGraduacao, tipoLayout,
		 * apresentarInstituicaoDisciplinaAproveitada,
		 * apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno,
		 * usuarioVO)); break;
		 * 
		 * case APROVADAS: if (situacao.getHistoricoAprovado()) {
		 * historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(
		 * matriculaVO, historicoVO, gradeCurricularVO, posGraduacao, tipoLayout,
		 * apresentarInstituicaoDisciplinaAproveitada,
		 * apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno,
		 * usuarioVO)); } break;
		 * 
		 * case APROVADAS_REPROVADAS: if (situacao.getHistoricoAprovado() ||
		 * situacao.getHistoricoReprovado()) {
		 * historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(
		 * matriculaVO, historicoVO, gradeCurricularVO, posGraduacao, tipoLayout,
		 * apresentarInstituicaoDisciplinaAproveitada,
		 * apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno,
		 * usuarioVO)); } break;
		 * 
		 * case APROVADAS_CURSANDO: if (situacao.getHistoricoAprovado() ||
		 * historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()) ||
		 * historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.
		 * getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.
		 * CURSANDO_POR_CORRESPONDENCIA.getValor())) {
		 * historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(
		 * matriculaVO, historicoVO, gradeCurricularVO, posGraduacao, tipoLayout,
		 * apresentarInstituicaoDisciplinaAproveitada,
		 * apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno,
		 * usuarioVO)); } break;
		 * 
		 * case APROVADAS_REPROVADAS_CURSANDO: if (situacao.getHistoricoAprovado() ||
		 * situacao.getHistoricoReprovado() ||
		 * historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO.getValor()) ||
		 * historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.
		 * getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.
		 * CURSANDO_POR_CORRESPONDENCIA.getValor())) {
		 * historicoAlunoDisciplinaRelVOs.add(preencherHistoricoAlunoDisciplinaRelVO(
		 * matriculaVO, historicoVO, gradeCurricularVO, posGraduacao, tipoLayout,
		 * apresentarInstituicaoDisciplinaAproveitada,
		 * apresentarApenasProfessorTitulacaoTurmaOrigem, mapTotalDiaLetivoAno,
		 * usuarioVO)); } break;
		 * 
		 * } }
		 */
		if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2)) {
			Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "nomeDisciplina");
		} else if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
			Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "ordem");
		} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC)) {
			Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "historicoDisciplinaOptativa");
		}
		else {
			switch (OrdemHistoricoDisciplina.getEnum(ordem)) {
			case ANO_SEMESTRE:
				Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "ordenarAnoSemestre");
				break;
			case ANO_SEMESTRE_PERIODO_SITUACAO:
				Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "ordenarAnoSemestrePeriodoSituacao");
				break;
			case ANO_SEMESTRE_SITUACAO_PERIODO:
				Ordenacao.ordenarLista(historicoAlunoDisciplinaRelVOs, "ordenarAnoSemestreSituacaoPeriodo");
				break;
			default:
				break;
			}
		}
		if(tipoLayout.equals("HistoricoAlunoPos18Rel")  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
			historicoAlunoRelVO.setMediaGlobal(realizarCalculoMediaGlobal(historicoAlunoDisciplinaRelVOs));
			Double percentualFrequencia = realizarCalculoPercentualGlobalFrequencia(historicoAlunoDisciplinaRelVOs);
			if (percentualFrequencia >= 100) {
				historicoAlunoRelVO.setPercentualGlobalFrequencia(100.0);
			} else {
				historicoAlunoRelVO.setPercentualGlobalFrequencia(realizarCalculoPercentualGlobalFrequencia(historicoAlunoDisciplinaRelVOs));
			}
			
			//historicoAlunoRelVO.setAproveitamentoAprendizagem(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarCalculoCoeficienteRendimento(matriculaVO.getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento(), historicoVOs));
			
			int qtdDisciplinasSomarAproveitamentoAprendizagem = 0;
			Double somaMediaDisciplinas = 0.0;
			Double aproveitamentoAprendizagem = 0.0;

			for (HistoricoAlunoDisciplinaRelVO historicoALuno :  historicoAlunoDisciplinaRelVOs) {
				if((historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.APROVADO.getDescricao()) 
						|| historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao())
						|| historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.APROVADO_COM_DEPENDENCIA.getDescricao()) 
						|| historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.REPROVADO.getDescricao())
						|| historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.REPROVADO_FALTA.getDescricao())
						|| historicoALuno.getSituacaoFinal().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getDescricao())) 
						&& !historicoALuno.getDisciplinaTcc() && !historicoALuno.getDisciplinaEstagio()) {
					if (Uteis.isAtributoPreenchido(historicoALuno.getMediaFinal()) && !historicoALuno.getMediaFinal().equals("CC")) {					
						somaMediaDisciplinas += historicoALuno.getMediaFinalCalculo();
						qtdDisciplinasSomarAproveitamentoAprendizagem ++;
					}
				}
			}
			if(somaMediaDisciplinas != 0.0) {
				aproveitamentoAprendizagem = somaMediaDisciplinas / (qtdDisciplinasSomarAproveitamentoAprendizagem * 10) * 100;

				historicoAlunoRelVO.setAproveitamentoAprendizagem(Uteis.arrendondarForcandoCadasDecimais(aproveitamentoAprendizagem, 1));		
			}				
					
		}
		else{
			historicoAlunoRelVO.setMediaGlobal(realizarCalculoMediaGlobal(historicoAlunoDisciplinaRelVOs, matriculaVO.getCurso().getConfiguracaoAcademico(), historicoVOs));
		}
		return historicoAlunoDisciplinaRelVOs;
	}

	private HashMap<String, Date> preencherDataPeriodoCursado(List<HistoricoVO> historicoVOs , Date dataMatricula) throws Exception {
		HashMap<String, Date> datas = new HashMap<String, Date>(0);
		List<Map<String, Date>> listaDatas = new ArrayList<>(0);
		historicoVOs.stream().map(HistoricoVO::getCodigo).map(getFacadeFactory().getHistoricoFacade()::carregarUltimoPeriodoAulaDisciplinaAluno).filter(Uteis::isAtributoPreenchido).forEach(listaDatas::add);
		if (Uteis.isAtributoPreenchido(dataMatricula)) {
			listaDatas.stream().map(data -> data.get("datainicio")).filter(data -> Uteis.isAtributoPreenchido(data) && data.after(dataMatricula)).min(Comparator.naturalOrder()).ifPresent(dataInicio -> datas.put("dataInicioCurso", dataInicio));
		}else {
		listaDatas.stream().map(data -> data.get("datainicio")).filter(Uteis::isAtributoPreenchido).min(Comparator.naturalOrder()).ifPresent(dataInicio -> datas.put("dataInicioCurso", dataInicio));
		}
		listaDatas.stream().map(data -> data.get("datatermino")).filter(Uteis::isAtributoPreenchido).max(Comparator.naturalOrder()).ifPresent(dataFim -> datas.put("dataFinalCurso", dataFim));
		return datas;
	}

	public void realizarInclusaoPeriodoLetivoNaoCursadoEnsinoMedio(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs, GradeCurricularVO gradeCurricularVO, String nivelEducacional) throws Exception {
		HashMap<String, PeriodoLetivoVO> mapPeriodoLetivoVOs = new HashMap<String, PeriodoLetivoVO>(0);
		HashMap<String, String> mapPeriodoLetivoVOExistentes = new HashMap<String, String>(0);
		if (gradeCurricularVO.getPeriodoLetivosVOs().isEmpty()) {
			gradeCurricularVO.setPeriodoLetivosVOs(getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(gradeCurricularVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoDisciplinaRelVOs) {
			if (!mapPeriodoLetivoVOExistentes.containsKey(historicoAlunoDisciplinaRelVO.getNomePeriodo())) {
				mapPeriodoLetivoVOExistentes.put(historicoAlunoDisciplinaRelVO.getNomePeriodo(), historicoAlunoDisciplinaRelVO.getNomePeriodo());
			}
		}
		for (PeriodoLetivoVO periodoLetivoVO : gradeCurricularVO.getPeriodoLetivosVOs()) {
			if (Uteis.isAtributoPreenchido(periodoLetivoVO.getNomeCertificacao())) {
				if (!mapPeriodoLetivoVOExistentes.containsKey(periodoLetivoVO.getNomeCertificacao())) {
					mapPeriodoLetivoVOs.put(periodoLetivoVO.getNomeCertificacao().trim(), periodoLetivoVO);
				}
			} else {
				if (!mapPeriodoLetivoVOExistentes.containsKey(periodoLetivoVO.getDescricao())) {
					mapPeriodoLetivoVOs.put(periodoLetivoVO.getDescricao().trim(), periodoLetivoVO);
				}
			}

		}
		if (nivelEducacional.equals("BA") && mapPeriodoLetivoVOs.size() + mapPeriodoLetivoVOExistentes.size() < 9 && (mapPeriodoLetivoVOs.size() + mapPeriodoLetivoVOExistentes.size()) > 3) {
			mapPeriodoLetivoVOs.put("--", null);
		}
		if (nivelEducacional.equals("PR") && mapPeriodoLetivoVOs.size() + mapPeriodoLetivoVOExistentes.size() < 4) {
			mapPeriodoLetivoVOs.put("--", null);
		}
		historicoAlunoDisciplinaRelVOs.addAll(realizarCriacaoHistoricoNaoCursado(mapPeriodoLetivoVOs));

		mapPeriodoLetivoVOs.clear();
		mapPeriodoLetivoVOs = null;
		mapPeriodoLetivoVOExistentes.clear();
		mapPeriodoLetivoVOExistentes = null;
	}

	public List<HistoricoAlunoDisciplinaRelVO> realizarCriacaoHistoricoNaoCursado(HashMap<String, PeriodoLetivoVO> mapPeriodoLetivoVOs) {
		List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs = new ArrayList<HistoricoAlunoDisciplinaRelVO>(0);
		for (String periodoLetivo : mapPeriodoLetivoVOs.keySet()) {
			HistoricoAlunoDisciplinaRelVO obj = new HistoricoAlunoDisciplinaRelVO();
			PeriodoLetivoVO periodoLetivoVO = mapPeriodoLetivoVOs.get(periodoLetivo);
			if (periodoLetivoVO == null) {
				obj.setNomePeriodo("--");
				obj.setNumeroPeriodoLetivo(9);
			} else {
				obj.setNomePeriodo(periodoLetivo);
				obj.setNumeroPeriodoLetivo(periodoLetivoVO.getPeriodoLetivo());
			}
			obj.setChDisciplina("0");
			obj.setCrDisciplina("0");
			historicoAlunoDisciplinaRelVOs.add(obj);
		}
		return historicoAlunoDisciplinaRelVOs;
	}

	private HistoricoAlunoDisciplinaRelVO preencherHistoricoAlunoDisciplinaRelVO(MatriculaVO matriculaVO, HistoricoVO historicoVO, GradeCurricularVO gradeCurricularVO, boolean posGraduacao, String tipoLayout, Boolean apresentarInstituicaoDisciplinaAproveitada, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, HashMap<Integer, Integer> mapTotalDiaLetivoAno, UsuarioVO usuarioVO, List<HistoricoVO> historicoVOs, String regraApresentacaoProfessorDisciplinaAproveitamento,int ordem, Boolean gradeAtualAluno, Boolean trazerTodosProfessoresDisciplinas, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) {
		try {
			HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO = new HistoricoAlunoDisciplinaRelVO();
			PeriodoLetivoVO periodoLetivoVO = new PeriodoLetivoVO();
			historicoVO.setMatricula(matriculaVO);
			historicoAlunoDisciplinaRelVO.setHistoricoVO(historicoVO);
			historicoAlunoDisciplinaRelVO.setDisciplinaForaGrade(historicoVO.getHistoricoDisciplinaForaGrade());
			historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getDataInicioAula());
			historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getDataFimAula());
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
				historicoAlunoDisciplinaRelVO.setSituacaoHistorico(historicoVO.getSituacao_Apresentar());
			}else {
			
				historicoAlunoDisciplinaRelVO.setSituacaoHistorico(historicoVO.getSituacao());
			}
			historicoAlunoDisciplinaRelVO.setSituacaoHistoricoEnum(SituacaoHistorico.getEnum(historicoVO.getSituacao()));
			historicoAlunoDisciplinaRelVO.setDisciplinaAtividadeComplementar(historicoVO.getHistoricoDisciplinaAtividadeComplementar());
			historicoAlunoDisciplinaRelVO.setTotalDiaLetivoAno(historicoVO.getTotalDiaLetivoAno());
			historicoAlunoDisciplinaRelVO.setObservacao(historicoVO.getObservacao());
			historicoAlunoDisciplinaRelVO.setTotalFalta(historicoVO.getTotalFalta());
			historicoAlunoDisciplinaRelVO.setTipoHistorico(TipoHistorico.getEnum(historicoVO.getTipoHistorico()));
			historicoAlunoDisciplinaRelVO.setDisciplinaDependencia(historicoVO.getTipoHistorico().equals(TipoHistorico.DEPENDENCIA.getValor()));
			historicoAlunoDisciplinaRelVO.setDisciplinaAdaptacao(historicoVO.getTipoHistorico().equals(TipoHistorico.ADAPTACAO.getValor()));
			historicoAlunoDisciplinaRelVO.setDisciplinaReferenteAUmGrupoOptativa(historicoVO.getDisciplinaReferenteAUmGrupoOptativa());
			historicoAlunoDisciplinaRelVO.setHistoricoDisciplinaOptativa(historicoVO.getHistoricoDisciplinaOptativa());
			if (matriculaVO.getCurso().getNivelEducacional().equals("BA")) {
				if (!Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAnoMudancaLegislacao()).trim().isEmpty() && Integer.valueOf(Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAnoMudancaLegislacao())) > historicoVO.getAnoHistoricoInteiro()
				// && (historicoVO.getSituacao().equals("AA") ||
				// historicoVO.getSituacao().equals("IS") ||
				// historicoVO.getSituacao().equals("CC") ||
				// historicoVO.getSituacao().equals("CH"))
						&& historicoVO.getPeriodoLetivoCursada().getCodigo() > 0) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoCursada().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				} else if (!Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAnoMudancaLegislacao()).trim().isEmpty() && Integer.valueOf(Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAnoMudancaLegislacao())) > historicoVO.getAnoHistoricoInteiro() && historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo() > 0) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				} else if (historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo() > 0) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				} else if (historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo() > 0) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				}
			} else if (matriculaVO.getCurso().getNivelEducacional().equals("ME") || matriculaVO.getCurso().getNivelEducacional().equals("PR")) {
				periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina(historicoVO.getDisciplina().getCodigo(), gradeCurricularVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (periodoLetivoVO.isNovoObj()) {
					if (historicoVO.getPeriodoLetivoCursada().getCodigo() > 0) {
						periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoCursada().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					} else if (historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo() > 0) {
						periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					} else if (historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo() > 0) {
						periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					}
				}
			} else {
				if (((matriculaVO.getCurso().getConfiguracaoAcademico().getApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico() && historicoVO.getGradeDisciplinaVO().getTipoDisciplina().equals("OP")) || historicoVO.getHistoricoDisciplinaForaGrade()) && historicoVO.getMatriculaPeriodo().getPeridoLetivo().getPeriodoLetivo() > 0) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				} else {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina(historicoVO.getDisciplina().getCodigo(), gradeCurricularVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (periodoLetivoVO.isNovoObj()) {
						if (historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo() > 0) {
							periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoMatrizCurricular().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
						} else if (historicoVO.getPeriodoLetivoCursada().getCodigo() > 0) {
							periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getPeriodoLetivoCursada().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
						} else {
							periodoLetivoVO = historicoVO.getMatriculaPeriodo().getPeridoLetivo();
						}

					}
				}
			}

			// if
			// (!mapTotalDiaLetivoAno.containsKey(periodoLetivoVO.getPeriodoLetivo()))
			// {
			// Integer totalDia =
			// getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarQuantidadeDiasLetivoAno(historicoVO.getMatricula().getMatricula(),
			// historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo());
			// mapTotalDiaLetivoAno.put(periodoLetivoVO.getPeriodoLetivo(),
			// totalDia);
			// historicoAlunoDisciplinaRelVO.setTotalDiaLetivoAno(totalDia);
			// } else {
			// historicoAlunoDisciplinaRelVO.setTotalDiaLetivoAno(mapTotalDiaLetivoAno.get(periodoLetivoVO.getPeriodoLetivo())
			historicoAlunoDisciplinaRelVO.setTotalDiaLetivoAno(historicoVO.getTotalDiaLetivoAno());
			// }
			if (!historicoVO.getMediaFinalConceito().getSituacao().trim().isEmpty()) {
				if (SituacaoHistorico.REPROVADO_FALTA.getValor().equals(historicoVO.getSituacao()) || SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor().equals(historicoVO.getSituacao())) {
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(SituacaoHistorico.REPROVADO.getValor());
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodoEnum(SituacaoHistorico.REPROVADO);
					if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
						historicoAlunoDisciplinaRelVO.setSituacaoHistorico(SituacaoHistorico.REPROVADO.getValor());
					}
				} else {
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodoEnum(historicoAlunoDisciplinaRelVO.getSituacaoHistoricoEnum());
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(historicoVO.getMediaFinalConceito().getAbreviaturaConceitoNota());					
					if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
						historicoAlunoDisciplinaRelVO.setSituacaoHistorico(historicoVO.getMediaFinalConceito().getSituacao());
					}
				}
				if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
					SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());
					if (situacao.getHistoricoCursando()){
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.CURSANDO.getDescricao());
					}else {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(historicoVO.getSituacao_Apresentar());
					}
					
				}
			} else {
				SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());
				historicoAlunoDisciplinaRelVO.setSituacaoPeriodoEnum(situacao);
				if (situacao.getHistoricoCursando() && (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC))) {
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(SituacaoHistorico.CURSANDO.getValor());
				} else {
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(historicoVO.getSituacao());
				}
			}
			if (historicoVO.getAnoHistorico().equals(historicoVO.getMatriculaPeriodo().getAno()) && historicoVO.getSemestreHistorico().equals(historicoVO.getMatriculaPeriodo().getSemestre()) && ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) || (!historicoVO.getSituacao().equals("AA") && !historicoVO.getSituacao().equals("IS") && !historicoVO.getSituacao().equals("CC") && !historicoVO.getSituacao().equals("CH") && !historicoVO.getSituacao().equals("AB")))) {
				historicoAlunoDisciplinaRelVO.setSituacaoMatriculaPeriodo(historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
			} else {
				historicoAlunoDisciplinaRelVO.setSituacaoMatriculaPeriodo("FI");
			}
			historicoAlunoDisciplinaRelVO.setInstituicao(historicoVO.getInstituicao());
			historicoAlunoDisciplinaRelVO.setCidade(historicoVO.getCidade());
			historicoAlunoDisciplinaRelVO.setEstado(historicoVO.getEstado());
			if (historicoVO.getConfiguracaoAcademico().getApresentarTotalAulaRegistradaComoCargaHorariaCursadaNoHistorico()) {
				Integer cargaHorariaCursada = getFacadeFactory().getRegistroAulaFacade().consultarTotalRegistroAula(historicoVO.getMatricula().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getMatriculaPeriodo().getSemestre(), historicoVO.getMatriculaPeriodo().getAno(), null, false, usuarioVO);
				if (Uteis.isAtributoPreenchido(cargaHorariaCursada)) {
					historicoAlunoDisciplinaRelVO.setCargaHorariaCursada(cargaHorariaCursada);
				} else {
					historicoAlunoDisciplinaRelVO.setCargaHorariaCursada(historicoVO.getCargaHorariaCursada());
				}
			} else {
				historicoAlunoDisciplinaRelVO.setCargaHorariaCursada(historicoVO.getCargaHorariaCursada());
			}
			historicoAlunoDisciplinaRelVO.setChDisciplina(Uteis.formatarDecimal(historicoVO.getGradeDisciplinaVO().getCargaHoraria().doubleValue(), "0"));
			historicoAlunoDisciplinaRelVO.setCrDisciplina(historicoVO.getGradeDisciplinaVO().getNrCreditos().toString());
			historicoAlunoDisciplinaRelVO.setDiversificada(historicoVO.getGradeDisciplinaVO().getDiversificada());
			if (xmlDiploma) {
				historicoAlunoDisciplinaRelVO.setCargaHorariaPratica(historicoVO.getGradeDisciplinaVO().getCargaHorariaPratica());
			}
			historicoAlunoDisciplinaRelVO.setDisciplinaTcc(historicoVO.getGradeDisciplinaVO().getDisciplinaTCC());
			if (historicoVO.getSituacao().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor())) {

				boolean aprovadaAproveitamento = getFacadeFactory().getDisciplinaAproveitadasFacade().consultarExistenciaRegistroEmDisciplinaAproveitada(historicoVO.getMatricula().getMatricula(), historicoVO.getDisciplina().getCodigo(), usuarioVO);
				if (aprovadaAproveitamento && !historicoVO.getDisciplinaIgualGradeAnterior()) {
					if (!historicoVO.getAnoHistorico().trim().isEmpty() || !historicoVO.getSemestreHistorico().trim().isEmpty()) {
						if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
						} else {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
						}
					} else {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(getFacadeFactory().getDisciplinaAproveitadasFacade().consultarAnoSemestreDisciplinaAproveitada(historicoVO.getMatricula().getMatricula(), historicoVO.getDisciplina().getCodigo(), usuarioVO));
						if (historicoAlunoDisciplinaRelVO.getAnoSemstre().contains("null")) {
							if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
								historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
							} else if (!historicoVO.getAnoHistorico().trim().isEmpty() && historicoVO.getSemestreHistorico().trim().isEmpty()) {
								historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
							} else {
								historicoAlunoDisciplinaRelVO.setAnoSemstre("");
							}
						}
					}
				} else {
					if (!historicoVO.getAnoHistorico().trim().isEmpty() || !historicoVO.getSemestreHistorico().trim().isEmpty()) {
						if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
						} else {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
						}
					} else {
						if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
						} else if (!historicoVO.getAnoHistorico().trim().isEmpty() && historicoVO.getSemestreHistorico().trim().isEmpty()) {
							historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
						} else {
							historicoAlunoDisciplinaRelVO.setAnoSemstre("");
						}
					}
				}
				if (historicoVO.getApresentarAprovadoHistorico()) {
					historicoVO.setSituacao("AP");
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodoEnum(SituacaoHistorico.APROVADO);
					historicoAlunoDisciplinaRelVO.setSituacaoHistoricoEnum(SituacaoHistorico.APROVADO);
					historicoAlunoDisciplinaRelVO.setSituacaoHistorico("AP");
					historicoAlunoDisciplinaRelVO.setSituacaoPeriodo("AP");
					if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_EXCEL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC_EXCEL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
						historicoAlunoDisciplinaRelVO.setSituacaoHistorico(SituacaoHistorico.APROVADO.getDescricao());
						historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(SituacaoHistorico.APROVADO.getDescricao());
					}
				}
			} else {
				if (!historicoVO.getAnoHistorico().trim().isEmpty() || !historicoVO.getSemestreHistorico().trim().isEmpty()) {
					if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
					} else {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
					}
				} else {
					if (!historicoVO.getAnoHistorico().trim().isEmpty() && !historicoVO.getSemestreHistorico().trim().isEmpty()) {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico() + "/" + historicoVO.getSemestreHistorico());
					} else if (!historicoVO.getAnoHistorico().trim().isEmpty() && historicoVO.getSemestreHistorico().trim().isEmpty()) {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
					} else if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2)) {
						historicoAlunoDisciplinaRelVO.setAnoSemstre(historicoVO.getAnoHistorico());
					} else {
						historicoAlunoDisciplinaRelVO.setAnoSemstre("");
					}

				}
			}

			if (Uteis.isAtributoPreenchido(historicoVO.getPeriodoLetivoMatrizCurricular().getPeriodoLetivo())) {
				historicoAlunoDisciplinaRelVO.setNumeroPeriodoLetivo(historicoVO.getPeriodoLetivoMatrizCurricular().getPeriodoLetivo());
			}

			if (matriculaVO.getCurso().getConfiguracaoAcademico().getApresentarSiglaConcessaoCredito() && historicoVO.getSituacao().equals("CC")) {
				historicoAlunoDisciplinaRelVO.setUtilizaMediaConceito(true);
				historicoAlunoDisciplinaRelVO.setMediaFinal("CC");
			} else if (historicoVO.getUtilizaNotaFinalConceito()) {
				historicoAlunoDisciplinaRelVO.setUtilizaMediaConceito(true);
				historicoAlunoDisciplinaRelVO.setMediaFinal(historicoVO.getNotaFinalConceito());
				if (Uteis.isAtributoPreenchido(historicoVO.getMediaFinalConceito().getCodigo())) {
					historicoAlunoDisciplinaRelVO.setNotaConceito(historicoVO.getMediaFinalConceito().getConceitoNota());	
				}
			} else if (historicoVO.getMediaFinalConceito().getCodigo() == 0) {
				historicoAlunoDisciplinaRelVO.setUtilizaMediaConceito(false);
				if (historicoVO.getMediaFinal() != null && historicoVO.getMediaFinal().equals(0.0)) {
					historicoAlunoDisciplinaRelVO.setMediaFinal(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
					historicoAlunoDisciplinaRelVO.setMediaFinalCalculo(0.0);
				} else {
					if (Uteis.isAtributoPreenchido(historicoVO.getMediaFinal())) {
						historicoAlunoDisciplinaRelVO.setMediaFinal(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
						historicoAlunoDisciplinaRelVO.setMediaFinalCalculo(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(historicoVO.getMediaFinal(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
					}
				}
			} else {
				// if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL)) {
				// historicoAlunoDisciplinaRelVO.setMediaFinal(Uteis.formatarDecimalDuasCasas(historicoVO.getMediaFinal()));
				// } else {
				historicoAlunoDisciplinaRelVO.setMediaFinal(historicoVO.getMediaFinalConceito().getAbreviaturaConceitoNota());
				historicoAlunoDisciplinaRelVO.setUtilizaMediaConceito(true);
				// }
			}
			if (historicoVO.getMediaFinalConceito().getSituacao().trim().isEmpty()) {
				if (tipoLayout.equals("HistoricoAlunoPosRel")) {
					SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());
					if (situacao.getHistoricoCursando()) {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao(SituacaoHistorico.CURSANDO.getValor()));
					} else {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao(historicoVO.getSituacao()));
					}
				} else if (tipoLayout.equals("HistoricoAlunoPos2Rel") || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) || tipoLayout.equals("HistoricoAlunoPos14Rel") || tipoLayout.equals("HistoricoAlunoPos15Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
					SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());
					if (situacao.getHistoricoCursando()) {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.CURSANDO.getValor());
					} else {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(historicoVO.getSituacao());
					}
				} else {
					SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());					
					if (situacao.getHistoricoCursando() && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) {						
						if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
							historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.CURSANDO.getValor());
						}else {
							historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao(SituacaoHistorico.CURSANDO.getValor()));
						}
						
					} else {
						if ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) && historicoVO.getSituacao().equals("CC")) {
							historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao("Aprovado por Crédito"));
						} else {
							if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
								historicoAlunoDisciplinaRelVO.setSituacaoFinal(historicoVO.getSituacao());
							}else {
								historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.getDescricao(historicoVO.getSituacao()));
							}
							
						}
					}
				}
			} else {
				if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
					SituacaoHistorico situacao = SituacaoHistorico.getEnum(historicoVO.getSituacao());
					if (situacao.getHistoricoCursando()){
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoHistorico.CURSANDO.getDescricao());
					}else {
						historicoAlunoDisciplinaRelVO.setSituacaoFinal(historicoVO.getSituacao_Apresentar());
					}
					
				}else {
					historicoAlunoDisciplinaRelVO.setSituacaoFinal(historicoVO.getMediaFinalConceito().getSituacao());
				}
				
			}
			historicoAlunoDisciplinaRelVO.setCodigoDisciplina(historicoVO.getDisciplina().getCodigo());
			historicoAlunoDisciplinaRelVO.setOrdem(historicoVO.getGradeDisciplinaVO().getOrdem());
			if (!historicoVO.getInstituicao().equals("") && apresentarInstituicaoDisciplinaAproveitada && !tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
				historicoAlunoDisciplinaRelVO.setNomeDisciplina(historicoVO.getDisciplina().getNome() + " - " + historicoVO.getInstituicao());
			} else {
				if ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) && !historicoVO.getGradeDisciplinaVO().getNomeChancela().trim().isEmpty()) {
					historicoAlunoDisciplinaRelVO.setNomeDisciplina(historicoVO.getGradeDisciplinaVO().getNomeChancela());
				} else {
					historicoAlunoDisciplinaRelVO.setNomeDisciplina(historicoVO.getDisciplina().getNome());
				}
				if (tipoLayout.equals("DocumentoIntegralizacaoCurricularRel")) {
					historicoAlunoDisciplinaRelVO.setNomeDisciplina(historicoVO.getDisciplina().getCodigo() + "  " + historicoVO.getDisciplina().getNome());
				}
			}
			if((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_EXCEL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC_EXCEL)) && historicoAlunoDisciplinaRelVO.getHistoricoVO().getHistoricoPorEquivalencia()) {
				List<HistoricoVO> disciplinasCursadasPorEquivalencia = getFacadeFactory().getHistoricoFacade().consultarHistoricoEquivalentePorMatriculaMapaEquivalenciaDisciplinaNomeDisciplinaEquivalente(historicoAlunoDisciplinaRelVO.getHistoricoVO().getMatricula().getMatricula(), historicoAlunoDisciplinaRelVO.getHistoricoVO().getMapaEquivalenciaDisciplina().getCodigo(), false, usuarioVO);
				
				StringBuilder disciplinasConcatenadas = new StringBuilder();

				for (HistoricoVO disciplina : disciplinasCursadasPorEquivalencia) {
				    String nomeDisciplina = disciplina.getDisciplina().getNome();
				    if (disciplinasConcatenadas.length() > 0) {
				        disciplinasConcatenadas.append(", ");
				    }
				    disciplinasConcatenadas.append(nomeDisciplina);
				}
				historicoAlunoDisciplinaRelVO.setNomeDisciplinaEquivalente("Equivalência(s) cursada(s): " + disciplinasConcatenadas.toString() + ".");
				historicoAlunoDisciplinaRelVO.setApresentarDisciplinasEquivalentes(true);
			} else {
				historicoAlunoDisciplinaRelVO.setApresentarDisciplinasEquivalentes(false);
			}
			historicoAlunoDisciplinaRelVO.setNumeroPeriodoLetivo(periodoLetivoVO.getPeriodoLetivo());
			historicoAlunoDisciplinaRelVO.setPeriodoLetivo(periodoLetivoVO);
			if (tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT2) || tipoLayout.equals("DiplomaAlunoMedio2RelVerso") || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO) || tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayout.equals("HistoricoAlunoNivelTecnico2Rel") || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL) || tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) || tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel")) {
				if (Uteis.isAtributoPreenchido(periodoLetivoVO.getNomeCertificacao())) {
					historicoAlunoDisciplinaRelVO.setNomePeriodo(periodoLetivoVO.getNomeCertificacao());
				} else {
					historicoAlunoDisciplinaRelVO.setNomePeriodo(periodoLetivoVO.getDescricao());
				}

			} else {
				historicoAlunoDisciplinaRelVO.setNomePeriodo(periodoLetivoVO.getPeriodoLetivo() + "°");				
			}

			
				if ((validarTipoLayoutGraduacao(tipoLayout, configuracaoLayoutHistoricoVO)  || xmlDiploma) && (historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor())
						|| (historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()) || historicoVO.getHistoricoDisciplinaAproveitada())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getValor())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CREDITO.getValor())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CREDITO.getDescricao())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVEITAMENTO_BANCA.getValor())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVEITAMENTO_BANCA.getDescricao())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.ISENTO.getValor())
						|| historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.ISENTO.getDescricao()))) { 
					definirApresentacaoProfessorAproveitamentoDisciplina(historicoAlunoDisciplinaRelVO, matriculaVO, historicoVO, apresentarApenasProfessorTitulacaoTurmaOrigem, posGraduacao, tipoLayout, regraApresentacaoProfessorDisciplinaAproveitamento, usuarioVO, configuracaoLayoutHistoricoVO, xmlDiploma);
					
				} else {
					if (posGraduacao || validarTipoLayoutGraduacao(tipoLayout, configuracaoLayoutHistoricoVO) || tipoLayout.equals("HistoricoAlunoPosPaisagemRel") || xmlDiploma) {	
						
						if (historicoVO.getHistoricoPorEquivalencia() || historicoVO.getHistoricoDisciplinaComposta()) {
							definirApresentacaoProfessorPrivilegiandoProfessorDefinidoPorCursoEDisciplina(historicoAlunoDisciplinaRelVO, matriculaVO, historicoVO, apresentarApenasProfessorTitulacaoTurmaOrigem, posGraduacao, tipoLayout, regraApresentacaoProfessorDisciplinaAproveitamento, usuarioVO, configuracaoLayoutHistoricoVO, xmlDiploma);
						} else {
							definirApresentacaoProfessorPrivilegiandoProgramacaoAula(historicoAlunoDisciplinaRelVO, matriculaVO, historicoVO, apresentarApenasProfessorTitulacaoTurmaOrigem, posGraduacao, tipoLayout, regraApresentacaoProfessorDisciplinaAproveitamento, usuarioVO, configuracaoLayoutHistoricoVO, xmlDiploma);
						}
					}
				}
			if(trazerTodosProfessoresDisciplinas) {
				List<FuncionarioVO> professores = new ArrayList<FuncionarioVO>(0);	
				professores = getFacadeFactory().getFuncionarioFacade().consultarProfessoresPorCursoDisciplinaAnoSemestre(matriculaVO.getCurso().getCodigo(), historicoVO.getDisciplina().getCodigo(), historicoAlunoDisciplinaRelVO.getAno(), historicoAlunoDisciplinaRelVO.getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, usuarioVO);
				if(Uteis.isAtributoPreenchido(professores)) {
					montarProfessoresDisciplina(professores, historicoAlunoDisciplinaRelVO);				
				}
			}
			
			
			historicoAlunoDisciplinaRelVO.setFreqDisciplina(historicoVO.getFreguencia());

			if (historicoVO.getConfiguracaoAcademico().getOcultarMediaFinalDisciplinaCasoReprovado()) {
				if ((historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor()))) {
					historicoAlunoDisciplinaRelVO.setMediaFinal(null);
				}
			}
			historicoAlunoDisciplinaRelVO.setDisciplinaEstagio(historicoVO.getGradeDisciplinaVO().getDisciplinaEstagio());
			historicoAlunoDisciplinaRelVO.setNrCreditos(historicoVO.getGradeDisciplinaVO().getNrCreditos());

			if (Uteis.isAtributoPreenchido(historicoVO.getDisciplina().getAbreviatura())) {
				historicoAlunoDisciplinaRelVO.setAbreviaturaDisciplina(historicoVO.getDisciplina().getAbreviatura());
			}

			if (Uteis.isAtributoPreenchido(matriculaVO.getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento())) {
				try {
					List<HistoricoVO> listaHistoricos = new ArrayList<HistoricoVO>();
					for (HistoricoVO obj : historicoVOs) {
						if(OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor() == OrdemHistoricoDisciplina.getEnum(ordem).getValor()) {
							if (obj.getAnoSemestreApresentar().equals(historicoAlunoDisciplinaRelVO.getAnoSemstre())) {
								listaHistoricos.add(obj);
							}
							
						}
                        if(OrdemHistoricoDisciplina.PERIODO_LETIVO.getValor() == OrdemHistoricoDisciplina.getEnum(ordem).getValor()) {
                        	if (obj.getMatriculaPeriodo().getPeriodoLetivo().getPeriodoLetivo().equals(historicoAlunoDisciplinaRelVO.getNumeroPeriodoLetivo())) {
    							listaHistoricos.add(obj);
    						}
							
						}
						
					}
					String patternCasasDemais = "";
					
					historicoAlunoDisciplinaRelVO.setCoeficienteRendimento(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarCalculoCoeficienteRendimento(matriculaVO.getCurso().getConfiguracaoAcademico().getFormulaCoeficienteRendimento(), listaHistoricos));
					if (Uteis.isAtributoPreenchido(matriculaVO.getCurso().getConfiguracaoAcademico().getCasasDecimaisCoeficienteRendimento())) {
						patternCasasDemais = "###0.";
						for (int i = 0; i < matriculaVO.getCurso().getConfiguracaoAcademico().getCasasDecimaisCoeficienteRendimento(); i++) {
							patternCasasDemais += 0;
						}
						historicoAlunoDisciplinaRelVO.setCasasDecimaisCoeficienteRendimento(patternCasasDemais);
					}				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			historicoAlunoDisciplinaRelVO.setGradeCurricularVO(gradeCurricularVO);			
			
			if(tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel") || Uteis.getIsValorNumerico(tipoLayout)) {
				historicoAlunoDisciplinaRelVO.setCargaHorariaGradeDisciplina(historicoVO.getGradeDisciplinaVO().getCargaHorariaTotalPraticaTeorica());
				historicoAlunoDisciplinaRelVO.setCargaHorariaPratica(historicoVO.getGradeDisciplinaVO().getCargaHorariaPratica());
				historicoAlunoDisciplinaRelVO.setCargaHorariaTeorica(historicoVO.getGradeDisciplinaVO().getCargaHorariaTotalPraticaTeorica() - historicoVO.getGradeDisciplinaVO().getCargaHorariaPratica());
			}
			
			if(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR)) {
				if(gradeAtualAluno){
					historicoAlunoDisciplinaRelVO.setGradeAtualAluno(true);
					if(Uteis.isAtributoPreenchido(historicoVO.getTransferenciaMatrizCurricularMatricula())){
						if(historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO.getValor()) || historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao()) || historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor())) {
							historicoAlunoDisciplinaRelVO.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao("AA"));
						}else {
							historicoAlunoDisciplinaRelVO.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelVO.getSituacaoHistorico()));
						}
					}
					else {
						historicoAlunoDisciplinaRelVO.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelVO.getSituacaoHistorico()));
					}
				}else {
					historicoAlunoDisciplinaRelVO.setGradeAtualAluno(false);
					historicoAlunoDisciplinaRelVO.setSituacaoHistoricoApresentarAdaptacaoMatriz(SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelVO.getSituacaoHistorico()));
				}

				
			}
			if(tipoLayout.equals("HistoricoAlunoPos18Rel")  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)  ) {
				
//				HashMap<String, Date> datas = new HashMap<String, Date>(0);
				
				if(Uteis.isAtributoPreenchido(historicoVO.getDataInicioAula())) {
					historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getDataInicioAula());
				}else {
//					datas = getFacadeFactory().getHistoricoFacade().carregarUltimoPeriodoAulaDisciplinaAluno(historicoVO.getCodigo());
//					historicoAlunoDisciplinaRelVO.setDataInicioAula(datas.get("datainicio"));
				}	
				
				if(Uteis.isAtributoPreenchido(historicoVO.getDataFimAula())) {
					historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getDataFimAula());
				}else {
//					datas = getFacadeFactory().getHistoricoFacade().carregarUltimoPeriodoAulaDisciplinaAluno(historicoVO.getCodigo());
//					historicoAlunoDisciplinaRelVO.setDataFimAula(datas.get("datatermino"));
				}
				if(Uteis.isAtributoPreenchido(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO()) && Uteis.isAtributoPreenchido(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo())) {
					if(historicoVO.getBimestre() != null && historicoVO.getBimestre().equals(1)) {
						if (Uteis.isAtributoPreenchido(historicoVO.getDataInicioAula()) && Uteis.isAtributoPreenchido(historicoVO.getDataFimAula())) {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getDataInicioAula());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getDataFimAula());
						} else {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoPrimeiroBimestre());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre());
						}
					}else if(historicoVO.getBimestre() != null && historicoVO.getBimestre().equals(2)) {
						if (Uteis.isAtributoPreenchido(historicoVO.getDataInicioAula()) && Uteis.isAtributoPreenchido(historicoVO.getDataFimAula())) {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getDataInicioAula());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getDataFimAula());
						} else {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivoSegundoBimestre());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoSegundoBimestre());
						}
					}else {
						if (Uteis.isAtributoPreenchido(historicoVO.getDataInicioAula()) && Uteis.isAtributoPreenchido(historicoVO.getDataFimAula())) {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getDataInicioAula());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getDataFimAula());
						} else {
							historicoAlunoDisciplinaRelVO.setDataInicioAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
							historicoAlunoDisciplinaRelVO.setDataFimAula(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo());
						}
					}
				}
				
				if(Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getDataInicioAula()) && Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getDataFimAula())) {
					historicoAlunoDisciplinaRelVO.setPeriodoDisciplinaCursado(Uteis.getData(historicoAlunoDisciplinaRelVO.getDataInicioAula(), "dd/MM/yyyy") +" a "+ Uteis.getData(historicoAlunoDisciplinaRelVO.getDataFimAula(), "dd/MM/yyyy"));
				}				
			
				if(Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getDataFimAula())) {					
			
					String bimestreConclusao = "";
					
					int anoConclusao = Uteis.getAnoData(historicoAlunoDisciplinaRelVO.getDataFimAula());
					if(Uteis.isAtributoPreenchido(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO()) && historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre() != null) {						
						if(historicoAlunoDisciplinaRelVO.getDataFimAula().before(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre())
								|| Uteis.getData(historicoAlunoDisciplinaRelVO.getDataFimAula()).equals( Uteis.getData(historicoVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivoPrimeiroBimestre()))) {
							if(historicoVO.getSemestreHistorico().equals("2")) {
								bimestreConclusao = "3º";
							}else {
								bimestreConclusao = "1º";
							}
						}else {
							if(historicoVO.getSemestreHistorico().equals("2")) {
								bimestreConclusao = "4º";
							}else {
								bimestreConclusao = "2º";
							}
						}
					}else {
						int mesConclusao = Uteis.getMesData(historicoAlunoDisciplinaRelVO.getDataFimAula());
											
						if(mesConclusao == 1 || mesConclusao == 2 || mesConclusao == 3) {
							bimestreConclusao = "1º";
						}
						else if(mesConclusao == 4 || mesConclusao == 5 || mesConclusao == 6 || mesConclusao == 7 ) {
							bimestreConclusao = "2º";
						}
						else if(mesConclusao == 8 || mesConclusao == 9 ) {
							bimestreConclusao = "3º";
						}
						else if(mesConclusao == 10 || mesConclusao == 11 || mesConclusao == 12) {
							bimestreConclusao = "4º";
						}
					}
					
					historicoAlunoDisciplinaRelVO.setBimestreAnoConclusao(bimestreConclusao +"/"+ anoConclusao);
					
				}
				
				historicoAlunoDisciplinaRelVO.setDisciplinaTcc(historicoVO.getGradeDisciplinaVO().getDisciplinaTCC());
				if(historicoAlunoDisciplinaRelVO.getDisciplinaTcc()) {
					historicoAlunoDisciplinaRelVO.setTituloMonografia(matriculaVO.getTituloMonografia());
					historicoAlunoDisciplinaRelVO.setOrientadorMonografia(matriculaVO.getOrientadorMonografia());
					historicoAlunoDisciplinaRelVO.setTitulacaoOrientadorMonografia(matriculaVO.getTitulacaoOrientadorMonografia());
				}
				
			}					

			return historicoAlunoDisciplinaRelVO;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void definirApresentacaoProfessorAproveitamentoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, MatriculaVO matriculaVO, HistoricoVO historicoVO, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, Boolean posGraduacao, String tipoLayout, String regraApresentacaoProfessorDisciplinaAproveitamento, UsuarioVO usuarioVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {
		if (regraApresentacaoProfessorDisciplinaAproveitamento.equals("PROFESSOR_APROVEITAMENTO_DISCIPLINA")) {
			// Todas as portarias MEC 1095 entram nessa condição para carregamento do
			// professor titular.
			DisciplinaAproveitadaAlteradaMatriculaVO disciplinaAproveitadaAlteradaMatriculaVO = getFacadeFactory().getDisciplinaAproveitadaAlteradaMatriculaFacade().consultarAproveitamentoPorMatricula(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);
			DisciplinasAproveitadasVO disciplinasAproveitadas = getFacadeFactory().getDisciplinaAproveitadasFacade().consultarAproveitamentoPorMatriculaDisciplina(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);
			historicoAlunoDisciplinaRelVO.setProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor() : disciplinasAproveitadas.getNomeProfessor());
			if (Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) || Uteis.isAtributoPreenchido(disciplinasAproveitadas.getTitulacaoProfessor())) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? NivelFormacaoAcademica.getEnumPorValor(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()).getDescricao() : NivelFormacaoAcademica.getEnumPorValor(disciplinasAproveitadas.getTitulacaoProfessor()).getDescricao());
			}
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			historicoAlunoDisciplinaRelVO.setSiglaProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			if (!tipoLayout.equals(HISTORICO_ALUNO_POS_18)) {
				montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getSexoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getSexoProfessor() : disciplinasAproveitadas.getSexoProfessor(), Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				consultarDadosProfessorMinistrouAula(historicoAlunoDisciplinaRelVO, historicoVO, tipoLayout, true, posGraduacao, configuracaoLayoutHistoricoVO, xmlDiploma);
			}
		} else if (regraApresentacaoProfessorDisciplinaAproveitamento.equals("PROFESSOR_TURMA_BASE")) {
			consultarDadosProfessorMinistrouAula(historicoAlunoDisciplinaRelVO, historicoVO, tipoLayout, true, posGraduacao, configuracaoLayoutHistoricoVO, xmlDiploma);
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
						FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(matriculaVO.getMatricula(), historicoVO.getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,historicoVO.getHistoricoPorEquivalencia(),historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico(), usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = consultarNomeProfessorTitularMinistrouAula(matriculaVO.getMatricula(), historicoVO.getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorCursoDisciplinaAnoSemestre(matriculaVO.getCurso().getCodigo(), historicoVO.getDisciplina().getCodigo(), historicoAlunoDisciplinaRelVO.getAno(), historicoAlunoDisciplinaRelVO.getSemestre(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
		} else if (regraApresentacaoProfessorDisciplinaAproveitamento.equals("COORDENADOR_CURSO")) {
			FuncionarioVO coordenadorCurso = consultarCordenadorCurso(matriculaVO.getMatricula(), matriculaVO.getCurso().getCodigo(), matriculaVO.getTurma(), matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(coordenadorCurso)) {
				historicoAlunoDisciplinaRelVO.setProfessor(coordenadorCurso.getPessoa().getNome());
				montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, coordenadorCurso.getPessoa().getSexo(), coordenadorCurso.getEscolaridade());
			}
		} else {
			historicoAlunoDisciplinaRelVO.setProfessor("");
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("");
			historicoAlunoDisciplinaRelVO.setSiglaProfessor("");
		}
	}
	
	private void definirApresentacaoProfessorPrivilegiandoProgramacaoAula(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, MatriculaVO matriculaVO, HistoricoVO historicoVO, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, Boolean posGraduacao, String tipoLayout, String regraApresentacaoProfessorDisciplinaAproveitamento, UsuarioVO usuarioVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {
		DisciplinaAproveitadaAlteradaMatriculaVO disciplinaAproveitadaAlteradaMatriculaVO = getFacadeFactory().getDisciplinaAproveitadaAlteradaMatriculaFacade().consultarAproveitamentoPorMatricula(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);
		DisciplinasAproveitadasVO disciplinasAproveitadas = getFacadeFactory().getDisciplinaAproveitadasFacade().consultarAproveitamentoPorMatriculaDisciplina(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);

		if ((Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO) && Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor()) 
				&& Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor())) 
		|| Uteis.isAtributoPreenchido(disciplinasAproveitadas) && Uteis.isAtributoPreenchido(disciplinasAproveitadas.getNomeProfessor()) 
		&& Uteis.isAtributoPreenchido(disciplinasAproveitadas.getTitulacaoProfessor())) {
			historicoAlunoDisciplinaRelVO.setProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor() : disciplinasAproveitadas.getNomeProfessor());
			if (Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) || Uteis.isAtributoPreenchido(disciplinasAproveitadas.getTitulacaoProfessor())) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? NivelFormacaoAcademica.getEnumPorValor(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()).getDescricao() : NivelFormacaoAcademica.getEnumPorValor(disciplinasAproveitadas.getTitulacaoProfessor()).getDescricao());
			}
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			historicoAlunoDisciplinaRelVO.setSiglaProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, disciplinasAproveitadas.getSexoProfessor(), disciplinasAproveitadas.getTitulacaoProfessor());
		}
		if((regraApresentacaoProfessorDisciplinaAproveitamento.equals("NAO_APRESENTAR")) && historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao())){
			historicoAlunoDisciplinaRelVO.setProfessor("");
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("");
		    historicoAlunoDisciplinaRelVO.setSiglaProfessor("");
		}else {
		if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
			consultarDadosProfessorMinistrouAula(historicoAlunoDisciplinaRelVO, historicoVO, tipoLayout, apresentarApenasProfessorTitulacaoTurmaOrigem, posGraduacao, configuracaoLayoutHistoricoVO, xmlDiploma);
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(matriculaVO.getMatricula(), historicoVO.getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, historicoVO.getHistoricoPorEquivalencia(), historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico(), usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = consultarNomeProfessorTitularMinistrouAula(matriculaVO.getMatricula(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorCursoDisciplinaAnoSemestre(matriculaVO.getCurso().getCodigo(), historicoVO.getDisciplina().getCodigo(), historicoAlunoDisciplinaRelVO.getAno(), historicoAlunoDisciplinaRelVO.getSemestre(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			// Metodo criado especificamente para o layout 18 onde quem usa e a faahf
			if ((!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor()) && tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC))) {
				FuncionarioVO coordenadorCurso = consultarCordenadorCurso(matriculaVO.getMatricula(), matriculaVO.getCurso().getCodigo(), matriculaVO.getTurma(), matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(coordenadorCurso)) {
					historicoAlunoDisciplinaRelVO.setProfessor(coordenadorCurso.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, coordenadorCurso.getPessoa().getSexo(), coordenadorCurso.getEscolaridade());
				}
			}
		}
	  }
	}
	
	private void definirApresentacaoProfessorPrivilegiandoProfessorDefinidoPorCursoEDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, MatriculaVO matriculaVO, HistoricoVO historicoVO, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, Boolean posGraduacao, String tipoLayout, String regraApresentacaoProfessorDisciplinaAproveitamento, UsuarioVO usuarioVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {
		DisciplinaAproveitadaAlteradaMatriculaVO disciplinaAproveitadaAlteradaMatriculaVO = getFacadeFactory().getDisciplinaAproveitadaAlteradaMatriculaFacade().consultarAproveitamentoPorMatricula(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);
		DisciplinasAproveitadasVO disciplinasAproveitadas = getFacadeFactory().getDisciplinaAproveitadasFacade().consultarAproveitamentoPorMatriculaDisciplina(matriculaVO.getMatricula(), historicoAlunoDisciplinaRelVO.getCodigoDisciplina(), usuarioVO);
		
		if((regraApresentacaoProfessorDisciplinaAproveitamento.equals("NAO_APRESENTAR")) && historicoAlunoDisciplinaRelVO.getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao())){
			historicoAlunoDisciplinaRelVO.setProfessor("");
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("");
		    historicoAlunoDisciplinaRelVO.setSiglaProfessor("");
		}else {
		if ((Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO) && Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor()) 
				&& Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor())) 
		|| Uteis.isAtributoPreenchido(disciplinasAproveitadas) && Uteis.isAtributoPreenchido(disciplinasAproveitadas.getNomeProfessor()) 
		&& Uteis.isAtributoPreenchido(disciplinasAproveitadas.getTitulacaoProfessor())) {
			historicoAlunoDisciplinaRelVO.setProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getNomeProfessor() : disciplinasAproveitadas.getNomeProfessor());
			if (Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) || Uteis.isAtributoPreenchido(disciplinasAproveitadas.getTitulacaoProfessor())) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? NivelFormacaoAcademica.getEnumPorValor(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()).getDescricao() : NivelFormacaoAcademica.getEnumPorValor(disciplinasAproveitadas.getTitulacaoProfessor()).getDescricao());
			}
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			historicoAlunoDisciplinaRelVO.setSiglaProfessor(Uteis.isAtributoPreenchido(disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor()) ? disciplinaAproveitadaAlteradaMatriculaVO.getTitulacaoProfessor() : disciplinasAproveitadas.getTitulacaoProfessor());
			montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, disciplinasAproveitadas.getSexoProfessor(), disciplinasAproveitadas.getTitulacaoProfessor());
		}
		if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
			FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorCursoDisciplinaAnoSemestre(matriculaVO.getCurso().getCodigo(), historicoVO.getDisciplina().getCodigo(), historicoAlunoDisciplinaRelVO.getAno(), historicoAlunoDisciplinaRelVO.getSemestre(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
				montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
			}
		}
		if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
			consultarDadosProfessorMinistrouAula(historicoAlunoDisciplinaRelVO, historicoVO, tipoLayout, apresentarApenasProfessorTitulacaoTurmaOrigem, posGraduacao, configuracaoLayoutHistoricoVO, xmlDiploma);
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(matriculaVO.getMatricula(), historicoVO.getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX,historicoVO.getHistoricoPorEquivalencia(),historicoVO.getAnoHistorico(), historicoVO.getSemestreHistorico(), usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
				FuncionarioVO funcionarioVO = consultarNomeProfessorTitularMinistrouAula(matriculaVO.getMatricula(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(),  Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				if (Uteis.isAtributoPreenchido(funcionarioVO)) {
					historicoAlunoDisciplinaRelVO.setProfessor(funcionarioVO.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, funcionarioVO.getPessoa().getSexo(), funcionarioVO.getPessoa().getFormacaoAcademicaVOs().isEmpty() ? "" : funcionarioVO.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());
				}
			}
			// Metodo criado especificamente para o layout 18 onde quem usa e a faahf
			if ((!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor()) && tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC))) {
				FuncionarioVO coordenadorCurso = consultarCordenadorCurso(matriculaVO.getMatricula(), matriculaVO.getCurso().getCodigo(), matriculaVO.getTurma(), matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(coordenadorCurso)) {
					historicoAlunoDisciplinaRelVO.setProfessor(coordenadorCurso.getPessoa().getNome());
					montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, coordenadorCurso.getPessoa().getSexo(), coordenadorCurso.getEscolaridade());
				}
			}
		}
	  }
	}

	private FuncionarioVO consultarCordenadorCurso(String codigoMatricula, Integer codigoCurso, String codigoTurma, Integer codigoUnidadeEnsino, int nivelmontardadosDadosbasicos, UsuarioVO usuarioVO) {
		FuncionarioVO coordenador = new FuncionarioVO();
		try {
			coordenador = getFacadeFactory().getFuncionarioFacade().consultaCordenadorPorCurso(codigoMatricula, codigoCurso, codigoTurma, codigoUnidadeEnsino, nivelmontardadosDadosbasicos, usuarioVO);
			return coordenador;
		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public boolean validarTipoLayoutGraduacao(String tipoLayout, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		switch (tipoLayout) {
		// case "HistoricoAlunoRelPortariaMEC":
		// return true;
		case HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC:
			return true;	
		case HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC:
			return true;	
		case HISTORICO_ALUNO_POS_17:
			return true;
		case HISTORICO_ALUNO_POS_18:
			return true;
		case HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC:
			return true;
		case HISTORICO_ALUNO_LAYOUT_24:
			return true;
		case HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC:
			return true;
		default:
			return (Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO) && (configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR) || configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA)));			
		}
	}

	private void obterDadosFormacaoAcademicaNivelMedio(PessoaVO pessoaVO, HistoricoAlunoRelVO historicoAlunoRelVO) {
		FormacaoAcademicaVO formacaoAcademicaVO = pessoaVO.getFormacaoAcademicaNivelMedio();
		if (Uteis.isAtributoPreenchido(formacaoAcademicaVO)) {
			historicoAlunoRelVO.setConclusaoEnsMedio(formacaoAcademicaVO.getInstituicao());
			historicoAlunoRelVO.setAnoConclusaoEnsMedio(formacaoAcademicaVO.getAnoDataFim_Apresentar());
			historicoAlunoRelVO.setAnoSemestreConclusaoEnsMedio(formacaoAcademicaVO.getAnoSemestreConclusao_Apresentar());
			historicoAlunoRelVO.setSemestreConclusaoEnsMedio(formacaoAcademicaVO.getSemestreDataFim_Apresentar());
			historicoAlunoRelVO.setCidadeConclusaoEnsMedio(formacaoAcademicaVO.getCidade().getNome());
			historicoAlunoRelVO.setEstadoConclusaoEnsMedio(formacaoAcademicaVO.getCidade().getEstado().getSigla());
		}
	}

	public String consultarCidadePorUnidadeEnsinoMatriz(UsuarioVO usuarioVO) throws Exception {
		CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorUnidadeEnsinoMatriz(false, usuarioVO);
		if (cidadeVO != null && cidadeVO.getCodigo() != 0) {
			return cidadeVO.getNome();
		}
		return "";

	}

	private void obterDadosUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO, HistoricoAlunoRelVO historicoAlunoRelVO, Boolean utilizarCidadeUnidadeMatriz, Date dataExpedicaoDiploma, UsuarioVO usuarioVO) throws Exception {
		// historicoAlunoRelVO.setMantenedora(unidadeEnsinoVO.getMantenedora());
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNomeExpedicaoDiploma())) {
			historicoAlunoRelVO.setMantida(unidadeEnsinoVO.getNomeExpedicaoDiploma());
		} else {
			historicoAlunoRelVO.setMantida(unidadeEnsinoVO.getNome());
		}
		if (utilizarCidadeUnidadeMatriz) {
			String cidade = consultarCidadePorUnidadeEnsinoMatriz(usuarioVO);
			if (!cidade.equals("")) {
				historicoAlunoRelVO.setCidade(cidade);
			} else {
				historicoAlunoRelVO.setCidade(unidadeEnsinoVO.getCidade().getNome());
			}

			if (!Uteis.getData(dataExpedicaoDiploma).equals(Uteis.getData(new Date()))) {
				historicoAlunoRelVO.setCidadeDataAtual(historicoAlunoRelVO.getCidade() + ", " + Uteis.getData(dataExpedicaoDiploma, "dd/MM/yyyy"));
			} else {
				historicoAlunoRelVO.setCidadeDataAtual(historicoAlunoRelVO.getCidade() + ", " + Uteis.getData(new Date(), "dd/MM/yyyy"));
			}
		} else {
			historicoAlunoRelVO.setCidade(unidadeEnsinoVO.getCidade().getNome());
			if (!Uteis.getData(dataExpedicaoDiploma).equals(Uteis.getData(new Date()))) {
				historicoAlunoRelVO.setCidadeDataAtual(historicoAlunoRelVO.getCidade() + ", " + Uteis.getData(dataExpedicaoDiploma, "dd/MM/yyyy"));
			} else {
				historicoAlunoRelVO.setCidadeDataAtual(historicoAlunoRelVO.getCidade() + ", " + Uteis.getData(new Date(), "dd/MM/yyyy"));
			}
		}
		historicoAlunoRelVO.setDataExpedicaoDiploma(dataExpedicaoDiploma);
		historicoAlunoRelVO.setDataExpedicaoDiplomaString(Uteis.getDataCidadeDiaMesPorExtensoEAno("",
				dataExpedicaoDiploma, true));
		historicoAlunoRelVO.setDataAtual(Uteis.getData(new Date(), "dd/MM/yyyy"));
		historicoAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(unidadeEnsinoVO.getCidade().getNome(), new Date(), false));
		historicoAlunoRelVO.setEndereco(unidadeEnsinoVO.getEndereco());
		historicoAlunoRelVO.setCaixaPostal(unidadeEnsinoVO.getCaixaPostal());
		historicoAlunoRelVO.setNumero(unidadeEnsinoVO.getNumero());
		historicoAlunoRelVO.setBairro(unidadeEnsinoVO.getSetor());
		historicoAlunoRelVO.setComplemento(unidadeEnsinoVO.getComplemento());
		historicoAlunoRelVO.setCep(unidadeEnsinoVO.getCEP());
		historicoAlunoRelVO.setComplemento(unidadeEnsinoVO.getComplemento());
		historicoAlunoRelVO.setEstado(unidadeEnsinoVO.getCidade().getEstado().getSigla());
		historicoAlunoRelVO.setFone(unidadeEnsinoVO.getTelComercial1());
		historicoAlunoRelVO.setSite(unidadeEnsinoVO.getSite());
		historicoAlunoRelVO.setEmail(unidadeEnsinoVO.getEmail());
		historicoAlunoRelVO.setRazaoSocial(unidadeEnsinoVO.getRazaoSocial());
		historicoAlunoRelVO.setCnpj(unidadeEnsinoVO.getCNPJ());
		historicoAlunoRelVO.setInscEstadual(unidadeEnsinoVO.getInscEstadual());
		historicoAlunoRelVO.setInscMunicipal(unidadeEnsinoVO.getInscMunicipal());
		historicoAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso()) && historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? unidadeEnsinoVO.getCredenciamentoPortariaEAD() : unidadeEnsinoVO.getCredenciamentoPortaria());
		historicoAlunoRelVO.setCredenciamento(Uteis.isAtributoPreenchido(historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso()) && historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? unidadeEnsinoVO.getCredenciamentoEAD() : unidadeEnsinoVO.getCredenciamento());
		historicoAlunoRelVO.setNomeExpedicaoDiploma(unidadeEnsinoVO.getNomeExpedicaoDiploma());
		historicoAlunoRelVO.setDataPublicacaoDoEmpresa(Uteis.getData(Uteis.isAtributoPreenchido(historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso()) && historicoAlunoRelVO.getMatriculaVO().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? unidadeEnsinoVO.getDataPublicacaoDOEAD() : unidadeEnsinoVO.getDataPublicacaoDO(), "dd/MM/yyyy"));
		historicoAlunoRelVO.setCargoFuncionarioPrincipal(montarNomeCargoFuncionario(historicoAlunoRelVO.getCargoFuncionarioPrincipal(), usuarioVO));
		historicoAlunoRelVO.setCargoFuncionarioSecundario(montarNomeCargoFuncionario(historicoAlunoRelVO.getCargoFuncionarioSecundario(), usuarioVO));
		historicoAlunoRelVO.setCargoFuncionarioTerciario(montarNomeCargoFuncionario(historicoAlunoRelVO.getCargoFuncionarioTerciario(), usuarioVO));

		historicoAlunoRelVO.setCnpjMantenedora(unidadeEnsinoVO.getCnpjMantenedora());
		historicoAlunoRelVO.setUnidadeCertificadora(unidadeEnsinoVO.getUnidadeCertificadora());
		historicoAlunoRelVO.setCnpjUnidadeCertificadora(unidadeEnsinoVO.getCnpjUnidadeCertificadora());
		historicoAlunoRelVO.setCodigoIESUnidadeCertificadora(unidadeEnsinoVO.getCodigoIESUnidadeCertificadora());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico.HistoricoAlunoRelInterfaceFacade#
	 * montarNomeCargoFuncionario(negocio.comuns. administrativo.CargoVO)
	 */
	public CargoVO montarNomeCargoFuncionario(CargoVO cargoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			cargoVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			return cargoVO;
		} catch (Exception e) {
		}
		return new CargoVO();
	}

	private void obterDadosAluno(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricular, boolean gradeAtualAluno, HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout, String observacaoComplementarIntegralizado, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, boolean utilizarCidadeUnidadeMatriz, Boolean apresentarDisciplinaForaGrade, UsuarioVO usuarioVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) throws Exception {
		ProgramacaoFormaturaAlunoVO programacaoFormaturaAluno = null;
		ExpedicaoDiplomaVO expedicaoDiploma = null;
		PessoaVO obj = matriculaVO.getAluno();
		obterDadosFormacaoAcademicaNivelMedio(obj, historicoAlunoRelVO);
		historicoAlunoRelVO.setGradeCurricularAtual(gradeAtualAluno);
		if (matriculaVO.getCurso().getNivelEducacional().equals("PR")) {
			historicoAlunoRelVO.setCursoProfissionalizante(true);
		} else {
			historicoAlunoRelVO.setCursoProfissionalizante(false);
		}

		historicoAlunoRelVO.setFormaIngresso(FormaIngresso.getDescricao(matriculaVO.getFormaIngresso()));
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC)
				|| tipoLayout.equals("HistoricoAlunoLayout11PortariaMECRel")
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC)			
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {			
			
					MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaBasicaPrimeiraMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if ((matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()))
					|| (matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()))) {
				
				if (matriculaVO.getDataProcessoSeletivo() != null) {
					historicoAlunoRelVO.setDataProcessoSeletivo(Uteis.getDataAno4Digitos(matriculaVO.getDataProcessoSeletivo()));
				}
				
		    	if (!matriculaVO.getAnoIngresso().trim().equals("") && !matriculaVO.getSemestreIngresso().trim().equals("")) {
				    historicoAlunoRelVO.setAnoIngresso(matriculaVO.getAnoIngresso());
					historicoAlunoRelVO.setSemestreIngresso(matriculaVO.getSemestreIngresso());					
					historicoAlunoRelVO.setMesIngresso(matriculaVO.getMesIngresso());
					
					if(!Uteis.isAtributoPreenchido(historicoAlunoRelVO.getMesIngresso()) && Uteis.isAtributoPreenchido(matriculaVO.getDataInicioCurso())) {
						historicoAlunoRelVO.setMesIngresso(String.valueOf(Uteis.getMesData(matriculaVO.getDataInicioCurso())));
						
					}
				
				}else if(!matriculaVO.getAnoIngresso().trim().equals("") &&  !matriculaVO.getMesIngresso().trim().equals("")) {					
					historicoAlunoRelVO.setAnoIngresso(matriculaVO.getAnoIngresso());				
					Date data = new Date();				
					data.setMonth(Integer.parseInt(matriculaVO.getMesIngresso()));
					historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(data));
					historicoAlunoRelVO.setMesIngresso(matriculaVO.getMesIngresso());					
					
				} else {
					historicoAlunoRelVO.setAnoIngresso("");
					historicoAlunoRelVO.setSemestreIngresso("");
				}
			} else {
				if (matriculaVO.getDataProcessoSeletivo() != null) {
					historicoAlunoRelVO.setDataProcessoSeletivo(Uteis.getDataAno4Digitos(matriculaVO.getDataProcessoSeletivo()));
				}
				if (!matriculaVO.getAnoIngresso().trim().equals("") && !matriculaVO.getSemestreIngresso().trim().equals("") && (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC))) {
					historicoAlunoRelVO.setAnoIngresso(matriculaVO.getAnoIngresso());
					historicoAlunoRelVO.setSemestreIngresso(matriculaVO.getSemestreIngresso());
					historicoAlunoRelVO.setMesIngresso(matriculaVO.getMesIngresso());
					

				} else if (matriculaVO.getDataInicioCurso() != null && (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) ||!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC ))) {
					historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataInicioCurso()));
					historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataInicioCurso()));
					historicoAlunoRelVO.setMesIngresso(String.valueOf(Uteis.getMesData(matriculaVO.getDataInicioCurso())));
					
				} else if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) || !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC)){
					historicoAlunoRelVO.setAnoIngresso(matriculaPeriodoVO.getAno());
					historicoAlunoRelVO.setSemestreIngresso(matriculaPeriodoVO.getSemestre());
					
				}
				historicoAlunoRelVO.setDataProcessoSeletivo(Uteis.getDataAno4Digitos(matriculaVO.getDataProcessoSeletivo()));
			}

		} else {
			if (matriculaVO.getDataProcessoSeletivo() != null) {
				historicoAlunoRelVO.setDataProcessoSeletivo(Uteis.getDataAno4Digitos(matriculaVO.getDataProcessoSeletivo()));
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL)) {
					historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataProcessoSeletivo()));
					historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataProcessoSeletivo()));
					historicoAlunoRelVO.setMesIngresso(String.valueOf(Uteis.getMesData(matriculaVO.getDataProcessoSeletivo())));
				}
			}
			if (!matriculaVO.getAnoIngresso().trim().equals("") && !matriculaVO.getSemestreIngresso().trim().equals("")) {
				if (((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL)) 
						&& historicoAlunoRelVO.getSemestreIngresso().trim().isEmpty() && historicoAlunoRelVO.getAnoIngresso().trim().isEmpty() && historicoAlunoRelVO.getMesIngresso().trim().isEmpty())
						|| (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL))) {
					historicoAlunoRelVO.setAnoIngresso(matriculaVO.getAnoIngresso());
					historicoAlunoRelVO.setSemestreIngresso(matriculaVO.getSemestreIngresso());
					historicoAlunoRelVO.setMesIngresso(matriculaVO.getMesIngresso());
				}

			} else if (matriculaVO.getDataInicioCurso() != null) {
				if (((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL)) && matriculaVO.getDataProcessoSeletivo() == null)
						|| (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL))) {
					historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataInicioCurso()));
					historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataInicioCurso()));
					historicoAlunoRelVO.setMesIngresso(String.valueOf(Uteis.getMesData(matriculaVO.getDataInicioCurso())));
				}
				
			}  else if (matriculaVO.getDataProcessoSeletivo() != null) {
				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getDataProcessoSeletivo()));
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getDataProcessoSeletivo()));
				historicoAlunoRelVO.setMesIngresso(String.valueOf(Uteis.getMesData(matriculaVO.getDataProcessoSeletivo())));
				
				
			}
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL)) {
				Optional.ofNullable(matriculaVO.getDataProcessoSeletivo())
					.filter(Uteis::isAtributoPreenchido)
					.map(MesAnoEnum::getMesData)
					.filter(Uteis::isAtributoPreenchido)
					.map(m -> m.getMes() + " de " + Uteis.getAno(matriculaVO.getDataProcessoSeletivo()))
					.ifPresent(historicoAlunoRelVO::setDataProcessoSeletivo);
			} else {
				historicoAlunoRelVO.setDataProcessoSeletivo(Uteis.getDataAno4Digitos(matriculaVO.getDataProcessoSeletivo()));
			}
		}
		if (matriculaVO.getFormaIngresso().equals(FormaIngresso.PORTADOR_DE_DIPLOMA.getValor())) {
			PortadorDiplomaVO portadorDiplomaVO = getFacadeFactory().getPortadorDiplomaFacade().consultarInstitiucaoEnsinoIngressoPorMatricula(matriculaVO.getMatricula(), usuarioVO);
			if (Uteis.isAtributoPreenchido(portadorDiplomaVO)) {
				historicoAlunoRelVO.setInstituicaoIngresso(portadorDiplomaVO.getInstituicaoEnsino());
				historicoAlunoRelVO.setCursoOrigem(portadorDiplomaVO.getCurso());
				historicoAlunoRelVO.setCidadeIngresso(portadorDiplomaVO.getCidade().getNome());
				historicoAlunoRelVO.setEstadoIngresso(portadorDiplomaVO.getCidade().getEstado().getSigla());

				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(portadorDiplomaVO.getData()));
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(portadorDiplomaVO.getData()));

			}
		} else if (matriculaVO.getTransferenciaEntrada().getCodigo() > 0 && (matriculaVO.getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_INTERNA.getValor()))) {
			if (matriculaVO.getTransferenciaEntrada().getInstituicaoOrigem().trim().isEmpty()) {
				matriculaVO.setTransferenciaEntrada(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(matriculaVO.getTransferenciaEntrada().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null, usuarioVO));
			}
			historicoAlunoRelVO.setInstituicaoIngresso(matriculaVO.getTransferenciaEntrada().getInstituicaoOrigem());
			if(!Uteis.isAtributoPreenchido(matriculaVO.getAnoIngresso())) {
				historicoAlunoRelVO.setAnoIngresso(Uteis.getAno(matriculaVO.getTransferenciaEntrada().getData()));
			}if(!Uteis.isAtributoPreenchido(matriculaVO.getSemestreIngresso())) {
				historicoAlunoRelVO.setSemestreIngresso(Uteis.getSemestreData(matriculaVO.getTransferenciaEntrada().getData()));
			}
			historicoAlunoRelVO.setCursoOrigem(matriculaVO.getTransferenciaEntrada().getCursoOrigem());
			historicoAlunoRelVO.setCidadeIngresso(matriculaVO.getTransferenciaEntrada().getCidade().getNome());
			historicoAlunoRelVO.setEstadoIngresso(matriculaVO.getTransferenciaEntrada().getCidade().getEstado().getSigla());
			if (!matriculaVO.getTransferenciaEntrada().getCidade().getCodigo().equals(0)) {
				CidadeVO cidadeTransf = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(matriculaVO.getTransferenciaEntrada().getCidade().getCodigo(), false, usuarioVO);
				historicoAlunoRelVO.setNomeCidadeOrigem(cidadeTransf.getNome());
				historicoAlunoRelVO.setEstadoCidadeOrigem(cidadeTransf.getEstado().getSigla());
			}
		} else {
			if (!matriculaVO.getUnidadeEnsino().getNomeExpedicaoDiploma().trim().isEmpty()) {
				historicoAlunoRelVO.setInstituicaoIngresso(matriculaVO.getUnidadeEnsino().getNomeExpedicaoDiploma());
			} else {
				historicoAlunoRelVO.setInstituicaoIngresso(matriculaVO.getUnidadeEnsino().getNome());
			}
		}
		historicoAlunoRelVO.setLocalProcessoSeletivo(matriculaVO.getLocalProcessoSeletivo());
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) {
			if (historicoAlunoRelVO.getAnoIngresso().equals("") && !matriculaVO.getAnoIngresso().equals("")) {
				historicoAlunoRelVO.setAnoIngresso(matriculaVO.getAnoIngresso());
			}
			if (historicoAlunoRelVO.getSemestreIngresso().equals("") && !matriculaVO.getSemestreIngresso().equals("")) {
				historicoAlunoRelVO.setSemestreIngresso(matriculaVO.getSemestreIngresso());
			}
		}
		if (gradeAtualAluno) {
			if(Uteis.isAtributoPreenchido(matriculaVO.getAnoConclusao()) &&  Uteis.isAtributoPreenchido(matriculaVO.getSemestreConclusao()) ) {
				historicoAlunoRelVO.setConclusaoCurso(matriculaVO.getAnoConclusao() + "/" + matriculaVO.getSemestreConclusao());
			}else {
				historicoAlunoRelVO.setConclusaoCurso("");
			}			
			
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) ||
					tipoLayout.equals("HistoricoAlunoResidenciaMedicaRel") ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || 
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) ||
					tipoLayout.equals("HistoricoAlunoPos12Rel") ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) ||
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || 
					tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
				historicoAlunoRelVO.setConclusaoCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataConclusaoCurso()));
			}
		} else {
			historicoAlunoRelVO.setConclusaoCurso("");
		}
		if (matriculaVO.getFormaIngresso().equals(FormaIngresso.ENEM.getValor()) && (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC))) {
			historicoAlunoRelVO.setInstituicaoIngresso("----");
			historicoAlunoRelVO.setNotaEnem(matriculaVO.getNotaEnem());
		}
		if (Uteis.isAtributoPreenchido(matriculaVO.getClassificacaoIngresso()) && (matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()))) {
			historicoAlunoRelVO.setClassificacaoIngresso(!matriculaVO.getClassificacaoIngresso().equals(0) ? matriculaVO.getClassificacaoIngresso().toString() : "");
		}
		if (matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) || matriculaVO.getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor()) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
			if (!Uteis.isAtributoPreenchido(matriculaVO.getDisciplinasProcSeletivo()) && Uteis.isAtributoPreenchido(matriculaVO.getInscricao()) && Uteis.isAtributoPreenchido(matriculaVO.getInscricao().getProcSeletivo())) {
				matriculaVO.setDisciplinasProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarDisciplinasProcSeletivoString(matriculaVO.getInscricao().getProcSeletivo()));
				historicoAlunoRelVO.setTotalPontoProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPontosResultadoProcessoSeletivoPorInscricao(matriculaVO.getInscricao().getCodigo()).doubleValue());
			}
			if (Uteis.isAtributoPreenchido(matriculaVO.getTotalPontoProcSeletivo()) && historicoAlunoRelVO.getTotalPontoProcSeletivo().equals(0.0) && matriculaVO.getTotalPontoProcSeletivo() > 0) {
				historicoAlunoRelVO.setTotalPontoProcSeletivo(matriculaVO.getTotalPontoProcSeletivo());
			}
			if (!Uteis.isAtributoPreenchido(historicoAlunoRelVO.getTotalPontoProcSeletivo()) && !Uteis.isAtributoPreenchido(matriculaVO.getTotalPontoProcSeletivo()) && matriculaVO.getInscricao().getCodigo() > 0) {
				historicoAlunoRelVO.setTotalPontoProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPontosResultadoProcessoSeletivoPorInscricao(matriculaVO.getInscricao().getCodigo()).doubleValue());
			}
			// if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_REL)) {
			// historicoAlunoRelVO.setDiscProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarResultadoProcessoSeletivoDescritivoSemQtdeAcertosPorInscricao(matriculaVO.getInscricao().getCodigo()));
			// if (historicoAlunoRelVO.getDiscProcSeletivo().equals("")) {
			// historicoAlunoRelVO.setDiscProcSeletivo(matriculaVO.getDisciplinasProcSeletivo());
			// }
			// } else {
			historicoAlunoRelVO.setDiscProcSeletivo(matriculaVO.getDisciplinasProcSeletivo());
			// }
			// Se for Transferência Externa então colocamos no lugar das
			// disciplinas do PS, a IES de origem do aluno e o curso.
		} else {
			historicoAlunoRelVO.setDiscProcSeletivo("");
			// historicoAlunoRelVO.setDiscProcSeletivo(getFacadeFactory().getTransferenciaEntradaFacade().consultarInstituicaoOrigemCursoOrigemPorMatricula(matriculaVO.getMatricula()));
		}
		historicoAlunoRelVO.setMatricula(matriculaVO.getMatricula());
		historicoAlunoRelVO.setNome(obj.getNome());
		historicoAlunoRelVO.setNomePai(getFacadeFactory().getFiliacaoFacade().consultarNomeFiliacaoPorTipoFiliacaoEPessoa("PA", obj.getCodigo()));
		historicoAlunoRelVO.setNomeMae(getFacadeFactory().getFiliacaoFacade().consultarNomeFiliacaoPorTipoFiliacaoEPessoa("MA", obj.getCodigo()));
		historicoAlunoRelVO.setDataNasc(Uteis.getDataAno4Digitos(obj.getDataNasc()));
		historicoAlunoRelVO.setRg(obj.getRG());
		historicoAlunoRelVO.setSexo(obj.getSexo_Apresentar());
		historicoAlunoRelVO.setOrgaoExpedidor(obj.getOrgaoEmissor());
		if ((tipoLayout.equals(HISTORICO_ALUNO_ENSINO_MEDIO_LAYOUT3) && matriculaVO.getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())) || tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
			historicoAlunoRelVO.setDataEmissaoRg(Uteis.getDataAno4Digitos(obj.getDataEmissaoRG()));
		} else {
			historicoAlunoRelVO.setDataEmissaoRg(obj.getDataEmissaoRG_Apresentar());
		}
		historicoAlunoRelVO.setDataEmissaoDocMilitar(Uteis.getData(obj.getDataExpedicaoCertificadoMilitar()));
		historicoAlunoRelVO.setOrgaoExpedidorDocMilitar(obj.getOrgaoExpedidorCertificadoMilitar());
		historicoAlunoRelVO.setZonaEleitoral(obj.getZonaEleitoral());
		historicoAlunoRelVO.setDataEmissaoTituloEleitor(Uteis.getData(obj.getDataExpedicaoTituloEleitoral()));
		historicoAlunoRelVO.setSecaoZonaEleitoral(obj.getSecaoZonaEleitoral());
		historicoAlunoRelVO.setUf(obj.getCidade().getEstado().getSigla());
		historicoAlunoRelVO.setEstadoEmissaoRg(obj.getEstadoEmissaoRG_Apresentar());
		historicoAlunoRelVO.setCpf(obj.getCPF());
		if (obj.getSituacaoMilitar() != null && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)) {
			historicoAlunoRelVO.setDocMilitar(obj.getSituacaoMilitar().getValorApresentar() + " - " + obj.getCertificadoMilitar());
		} else {
			historicoAlunoRelVO.setDocMilitar(obj.getCertificadoMilitar());
		}
		historicoAlunoRelVO.setTituloEleitor(obj.getTituloEleitoral());
		Pessoa.montarDadosNaturalidade(obj, usuarioVO);
		historicoAlunoRelVO.setNaturalidade(obj.getNaturalidade().getNome());
		historicoAlunoRelVO.setUf(obj.getNaturalidade().getEstado().getSigla());
		Pessoa.montarDadosNacionalidade(obj, usuarioVO);
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC)) {
			historicoAlunoRelVO.setEstadoCidadeOrigem(obj.getNaturalidade().getEstado().getNome());
		}
		historicoAlunoRelVO.setNacionalidade(obj.getNacionalidade().getNacionalidade());
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
			if (utilizarCidadeUnidadeMatriz) {
				String cidade = consultarCidadePorUnidadeEnsinoMatriz(usuarioVO);
				if (cidade.equals("")) {
					cidade = matriculaVO.getUnidadeEnsino().getCidade().getNome();
				}
				historicoAlunoRelVO.setDataPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade, new Date(), true));
			} else {
				historicoAlunoRelVO.setDataPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), true));
			}
		} else {
			historicoAlunoRelVO.setDataPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		}

		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC) ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19) ) {
			historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataInicioCurso()));
		}

		if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			TurmaVO UltimaturmaMatricula = null;
			if (matriculaVO.getDataInicioCurso() == null) {
				historicoAlunoRelVO.setDataInicioTurma(getFacadeFactory().getMatriculaFacade().consultarDataInicioTurmaAgrupadaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
				if (historicoAlunoRelVO.getDataInicioTurma() == null) {
					TurmaVO turma = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), usuarioVO);
					UltimaturmaMatricula = turma;
					matriculaVO.setDataInicioCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(turma.getCodigo(), "", ""));
					historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataInicioCurso(), false) + " a ").toLowerCase());
					historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataInicioCurso()));
				} else {
					historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", historicoAlunoRelVO.getDataInicioTurma(), false) + " a ").toLowerCase());
					historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(historicoAlunoRelVO.getDataInicioTurma()));
				}
			} else {
				historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataInicioCurso(), false)  + " a ").toLowerCase());
				historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataInicioCurso()));
			}

			if (matriculaVO.getDataConclusaoCurso() == null) {
				if (UltimaturmaMatricula != null) {
					matriculaVO.setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(UltimaturmaMatricula.getCodigo(), "", ""));
				} else {
					TurmaVO turma = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), usuarioVO);
					matriculaVO.setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(turma.getCodigo(), "", ""));
				}
				historicoAlunoRelVO.setPeriodoCursado((historicoAlunoRelVO.getPeriodoCursado() + (   Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataConclusaoCurso(), false))   ).toLowerCase());
			} else {
				historicoAlunoRelVO.setPeriodoCursado((historicoAlunoRelVO.getPeriodoCursado() + ( Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataConclusaoCurso(), false))   ).toLowerCase());
				if (Uteis.isAtributoPreenchido(matriculaVO.getDataInicioCurso()) && matriculaVO.getDataInicioCurso().compareTo(matriculaVO.getDataConclusaoCurso()) < 0) {
					historicoAlunoRelVO.setDataConclusaoCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataConclusaoCurso()));
				}
			}
		}

		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC)||
				tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)
				|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
			if (Uteis.isAtributoPreenchido(matriculaVO.getDataConclusaoCurso())) {
				historicoAlunoRelVO.setDataConclusaoCurso(UteisData.getDataAplicandoFormatacao(matriculaVO.getDataConclusaoCurso(), "dd/MM/yyyy"));
			}
		}
		
		if(tipoLayout.equals(HISTORICO_ALUNO_POS_18) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)){
			historicoAlunoRelVO.setAnoConclusao(matriculaVO.getAnoConclusao());
			historicoAlunoRelVO.setSemestreConclusao(matriculaVO.getSemestreConclusao());
		}

		if (matriculaVO.getCurso().getNivelEducacional().equals("SU") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
			// Matricula.montarDadosEnade(matriculaVO,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			matriculaVO.setMatriculaEnadeVOs(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatricula(matriculaVO.getMatricula(), usuarioVO, false));
			matriculaVO.getMatriculaEnadeVOs().forEach(me -> {
				if (!historicoAlunoRelVO.getEnade().trim().isEmpty()) {
					historicoAlunoRelVO.setEnade(historicoAlunoRelVO.getEnade() + "\n");
				}
				if (me.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.REALIZACAO) || me.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.DISPENSA)) {
					if ((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC)) && me.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.DISPENSA)) {
						historicoAlunoRelVO.setEnade(historicoAlunoRelVO.getEnade() + " " + me.getTextoEnade().getTexto());
					} else {
						historicoAlunoRelVO.setEnade(historicoAlunoRelVO.getEnade() + " " + me.getTextoEnade().getTexto() + (me.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.DISPENSA) && me.getDataEnade() != null ? " - " : " ") + Uteis.getData(me.getDataEnade(), "dd/MM/yyyy"));
					}
				} else {
					historicoAlunoRelVO.setEnade(historicoAlunoRelVO.getEnade() + " " + me.getTextoEnade().getTexto());
				}
			});
		}

		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
			// MONTANDO DATA INICIO DA TURMA PARA LAYOUT 5 DO HISTÓRICO DO ALUNO
			// LEMBRAR DE CORRIGIR A QUESTÃO DA DATA DE INICIO DA TURMA.
			if (Uteis.isAtributoPreenchido(matriculaVO.getDataInicioCurso())) {
				historicoAlunoRelVO.setDataMatricula(matriculaVO.getDataInicioCurso());
			} else {
				historicoAlunoRelVO.setDataInicioTurma(getFacadeFactory().getMatriculaFacade().consultarDataInicioTurmaAgrupadaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
			}
		}
		obterCargasHorariasHistorico(matriculaVO, gradeCurricular, gradeAtualAluno, historicoAlunoRelVO, usuarioVO, tipoLayout, apresentarDisciplinaForaGrade);
		integralizarCurriculo(matriculaVO, gradeCurricular.getCodigo(), historicoAlunoRelVO, tipoLayout, usuarioVO);
		inserirObservacaoHistorico(matriculaVO, gradeCurricular, historicoAlunoRelVO, tipoLayout, observacaoComplementarIntegralizado, apresentarObservacaoTransferenciaMatrizCurricular, observacaoTransferenciaMatrizCurricular, usuarioVO, configuracaoLayoutHistoricoVO);

		try {
			if (gradeAtualAluno) {
				if (Uteis.isAtributoPreenchido(matriculaVO.getDataColacaoGrau())) {
					Date dataColacao = getFacadeFactory().getMatriculaFacade().obterDataColacaoGrauMatricula(matriculaVO.getMatricula());
					historicoAlunoRelVO.setColacaoGrau(Uteis.getData(dataColacao, "dd/MM/yyyy"));
				} else {
					programacaoFormaturaAluno = getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarPorMatriculaProgramacaoFormaturaAluno(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					if (Uteis.isAtributoPreenchido(programacaoFormaturaAluno) && Uteis.isAtributoPreenchido(programacaoFormaturaAluno.getColacaoGrau())) {
						historicoAlunoRelVO.setColacaoGrau(Uteis.getData(programacaoFormaturaAluno.getColacaoGrau().getData(), "dd/MM/yyyy"));
					}
				}
			} else {
				historicoAlunoRelVO.setColacaoGrau("");
			}

		} catch (Exception e) {
		}

		try {
			if (gradeAtualAluno) {
				expedicaoDiploma = (ExpedicaoDiplomaVO) getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(expedicaoDiploma)) {
					historicoAlunoRelVO.setExpedicaoDiploma(Uteis.getData(expedicaoDiploma.getDataExpedicao(), "dd/MM/yyyy"));
				}
			} else {
				historicoAlunoRelVO.setExpedicaoDiploma("");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)) {
			if (!historicoAlunoRelVO.getExpedicaoDiploma().equals("") && !historicoAlunoRelVO.getColacaoGrau().equals("")) {
				historicoAlunoRelVO.setCurriculoIntegralizado(Boolean.TRUE);
			}
		}
	}

	private void obterPeriodoCursado(MatriculaVO matriculaVO, HistoricoAlunoRelVO historicoAlunoRelVO, UsuarioVO usuarioVO, List<HistoricoVO> historicos, String tipoLayout, Boolean dataInicioTerminoAlteracoesCadastrais) throws Exception {
		if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			if (tipoLayout.equals("HistoricoAlunoPos3Rel") || tipoLayout.equals("HistoricoAlunoPos17Rel")) {
				if (Objects.nonNull(dataInicioTerminoAlteracoesCadastrais) && dataInicioTerminoAlteracoesCadastrais) {
					if (Uteis.isAtributoPreenchido(matriculaVO.getDataInicioCurso()) && Uteis.isAtributoPreenchido(matriculaVO.getDataConclusaoCurso())) {
						String periodoCurso = Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataInicioCurso(), false).toLowerCase()    + " a "  + Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataConclusaoCurso(), false).toLowerCase();
						historicoAlunoRelVO.setPeriodoCursado(periodoCurso);
					}
				} else {
					HashMap<String, Date> mapaDatas = preencherDataPeriodoCursado(historicos , matriculaVO.getData());
					if (Uteis.isAtributoPreenchido(mapaDatas.get("dataInicioCurso")) && Uteis.isAtributoPreenchido(mapaDatas.get("dataFinalCurso"))) {
						historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", mapaDatas.get("dataInicioCurso"), false)	+ " a " ).toLowerCase()	+ (Uteis.getDataCidadeDiaMesPorExtensoEAno("", mapaDatas.get("dataFinalCurso"),	false)).toLowerCase());
					}
				}
			} else {
				TurmaVO UltimaturmaMatricula = null;
				if (Objects.nonNull(dataInicioTerminoAlteracoesCadastrais) && !dataInicioTerminoAlteracoesCadastrais) {
					historicoAlunoRelVO.setDataInicioTurma(getFacadeFactory().getMatriculaFacade().consultarDataInicioTurmaAgrupadaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
					if (historicoAlunoRelVO.getDataInicioTurma() == null) {
						TurmaVO turma = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), usuarioVO);
						UltimaturmaMatricula = turma;
						if(matriculaVO.getDataInicioCurso() == null) {
							matriculaVO.setDataInicioCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(turma.getCodigo(), "", ""));	
						}
						historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataInicioCurso(), false) + " a ").toLowerCase());
						historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataInicioCurso()));
					} else {
						historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", historicoAlunoRelVO.getDataInicioTurma(), false) + " a ").toLowerCase());
						historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(historicoAlunoRelVO.getDataInicioTurma()));
					}
				} else {
					historicoAlunoRelVO.setPeriodoCursado((Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataInicioCurso(), false)	+ " a ").toLowerCase());
					historicoAlunoRelVO.setDataInicioCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataInicioCurso()));
				}
				if (Objects.nonNull(dataInicioTerminoAlteracoesCadastrais) && !dataInicioTerminoAlteracoesCadastrais) {
					if (UltimaturmaMatricula != null && matriculaVO.getDataConclusaoCurso() == null) {
						matriculaVO.setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(UltimaturmaMatricula.getCodigo(), "", ""));
					} else {
						TurmaVO turma = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), usuarioVO);
						if(matriculaVO.getDataConclusaoCurso() == null) {
							matriculaVO.setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(turma.getCodigo(), "", ""));	
						}
					}
					historicoAlunoRelVO.setPeriodoCursado((historicoAlunoRelVO.getPeriodoCursado()	+ (Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataConclusaoCurso(), false))).toLowerCase());
				} else {
					historicoAlunoRelVO.setPeriodoCursado((historicoAlunoRelVO.getPeriodoCursado() + (Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaVO.getDataConclusaoCurso(), false))).toLowerCase());
					if (Uteis.isAtributoPreenchido(matriculaVO.getDataInicioCurso()) && Uteis.isAtributoPreenchido(matriculaVO.getDataConclusaoCurso()) && matriculaVO.getDataInicioCurso().compareTo(matriculaVO.getDataConclusaoCurso()) < 0) {
						historicoAlunoRelVO.setDataConclusaoCurso(Uteis.getDataAno4Digitos(matriculaVO.getDataConclusaoCurso()));
					}
				}
			}
		}
	}

	private void inserirObservacaoHistorico(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricular, HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout, String observacaoComplementarIntegralizado, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, UsuarioVO usuario, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		historicoAlunoRelVO.setSistemaAvaliacao(gradeCurricular.getSistemaAvaliacao());
		historicoAlunoRelVO.setCompetenciaProfissional(gradeCurricular.getCompetenciaProfissional());
		historicoAlunoRelVO.setGradeCurricularVO(gradeCurricular);
		if (!gradeCurricular.getObservacaoHistorico().trim().isEmpty() && !Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
			historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "\n" + gradeCurricular.getObservacaoHistorico());
		}
		if (apresentarObservacaoTransferenciaMatrizCurricular && !observacaoTransferenciaMatrizCurricular.equals("") && !Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
			historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "\n " + observacaoTransferenciaMatrizCurricular);
		}
		if (apresentarObservacaoTransferenciaMatrizCurricular && !observacaoTransferenciaMatrizCurricular.equals("") && Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
			historicoAlunoRelVO.setObsHistoricoTransferenciaMatrizCurricular(observacaoTransferenciaMatrizCurricular);
		}
		if (Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO)) {
			historicoAlunoRelVO.setObsHistoricoIntegralizado(observacaoComplementarIntegralizado);
			return;
		}
		if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar()) && !matriculaVO.getCurso().getNivelEducacionalPosGraduacao() && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC) && !tipoLayout.equals("DocumentoIntegralizacaoCurricularRel") && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_ADAPTACAO_MATRIZ_CURRICULAR) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL)
			&& !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL)  && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC)) {
			historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "\n Total de Horas de Atividades Complementares: " + historicoAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar() + "\n");
		}
		if (Uteis.isAtributoPreenchido(matriculaVO.getTituloMonografia()) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) && !tipoLayout.equals("DocumentoIntegralizacaoCurricularRel") && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC) && !tipoLayout.equals("HistoricoAlunoPos12Rel") && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)
				&& !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) && !tipoLayout.equals("HistoricoAlunoPos14Rel") && !tipoLayout.equals("HistoricoAlunoPos15Rel") && !tipoLayout.equals("HistoricoAlunoPos3Rel")) {
			historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "Título Monografia: " + matriculaVO.getTituloMonografia() + "\n");
			if (matriculaVO.getNotaMonografia() != null) {
				historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "Nota Monografia: " + matriculaVO.getNotaMonografia() + "\n");
			}
		}
		if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getEnade()) && !matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoResidenciaMedicaRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
				historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico());
				if (historicoAlunoRelVO.getCurriculoIntegralizado()) {
					historicoAlunoRelVO.setObsHistoricoIntegralizado(observacaoComplementarIntegralizado);
				}
			}
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoLayoutPortariaMECRel")
					|| tipoLayout.equals(HISTORICO_ALUNO_REL)   || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC))

			 {

				historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico());
			} else {
				historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + historicoAlunoRelVO.getEnade() + "\n");
			}
		} else if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao() && tipoLayout.equals("HistoricoAlunoPos14Rel")) {
			if (historicoAlunoRelVO.getCurriculoIntegralizado()) {
				historicoAlunoRelVO.setObsHistoricoIntegralizado(observacaoComplementarIntegralizado);
			}
		}
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)) {
			historicoAlunoRelVO.setObsHistorico(historicoAlunoRelVO.getObsHistorico() + "");
			if (historicoAlunoRelVO.getCurriculoIntegralizado()) {
				historicoAlunoRelVO.setObsHistorico(" Currículo Integralizado. \n" + " " + historicoAlunoRelVO.getObsHistorico());
			} else {
				historicoAlunoRelVO.setObsHistorico(" Currículo Não Integralizado. \n" + " " + historicoAlunoRelVO.getObsHistorico());
			}
		}
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC)) {
			historicoAlunoRelVO.setObsHistorico(gradeCurricular.getSistemaAvaliacao());
		}
	}

	public boolean getApresentarEnade(String enade, String obsHistorico) {
		char[] letrasEnade = enade.toCharArray();
		int index = 0;
		for (char c : letrasEnade) {
			if (obsHistorico.charAt(index) != c) {
				return false;
			}
			index++;
		}
		return true;
	}

	private void integralizarCurriculo(MatriculaVO matricula, Integer gradeCurricular, HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout, UsuarioVO usuarioVO) throws Exception {
		try {
			boolean matriculaIntegralizada = getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matricula, gradeCurricular, usuarioVO, null);
			if (matriculaIntegralizada && (!matricula.getCurso().getNivelEducacionalPosGraduacao() || Uteis.getIsValorNumerico(tipoLayout))) {
				historicoAlunoRelVO.setCurriculoIntegralizado(Boolean.TRUE);
			}
		} catch (Exception e) {
		}
	}

	// private void obterCargasHorariasHistorico(String matricula,
	// GradeCurricularVO gradeCurricular) throws Exception {
	// if (Uteis.isAtributoPreenchido(gradeCurricular)) {
	// Integer cargaHorariaCumprida =
	// getFacadeFactory().getHistoricoFacade().consultarCargaHorariaCumpridaNoHistorico(matricula,
	// gradeCurricular.getCodigo());
	// if (Uteis.isAtributoPreenchido(cargaHorariaCumprida)) {
	// historicoAlunoRelVO.setChCumprida(cargaHorariaCumprida + "");
	// } else {
	// historicoAlunoRelVO.setChCumprida("0");
	// }
	// Integer cargaHorariaExigida =
	// getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(gradeCurricular.getCodigo());
	// if (Uteis.isAtributoPreenchido(cargaHorariaExigida)) {
	// historicoAlunoRelVO.setChExigida(cargaHorariaExigida + "");
	// } else {
	// historicoAlunoRelVO.setChExigida("0");
	// }
	// }
	// }
	private void obterCargasHorariasHistorico(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricular, boolean gradeAtualAluno, HistoricoAlunoRelVO historicoAlunoRelVO, UsuarioVO usuarioVO, String tipoLayout, Boolean apresentarDisciplinaForaGrade) throws Exception {
		if (Uteis.isAtributoPreenchido(gradeCurricular)) {
			boolean apresentarDisciplinaForaGradeConformeTipoLayout = (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC)) ? false : apresentarDisciplinaForaGrade;
			Integer cargaHorariaCumprida = 0;
			cargaHorariaCumprida = getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(matriculaVO.getMatricula(), gradeCurricular.getCodigo(), apresentarDisciplinaForaGradeConformeTipoLayout, usuarioVO);
			int cargaHorariaAtividadeComplementarRealizada = 0;
			if (Uteis.isAtributoPreenchido(matriculaVO.getHorasComplementares())) {
				cargaHorariaAtividadeComplementarRealizada = matriculaVO.getHorasComplementares().intValue();
			} else {
				cargaHorariaAtividadeComplementarRealizada = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(matriculaVO.getMatricula(), gradeCurricular.getCodigo(), false);
			}
			int cargaHorariaObrigatoriaAtividadeComplementar = gradeCurricular.getTotalCargaHorariaAtividadeComplementar();
			if (Uteis.isAtributoPreenchido(cargaHorariaObrigatoriaAtividadeComplementar) && cargaHorariaAtividadeComplementarRealizada > cargaHorariaObrigatoriaAtividadeComplementar) {
				cargaHorariaAtividadeComplementarRealizada = cargaHorariaObrigatoriaAtividadeComplementar;
			}
			historicoAlunoRelVO.setCargaHorariaRealizadaAtividadeComplementar(String.valueOf(cargaHorariaAtividadeComplementarRealizada));
			if (Uteis.isAtributoPreenchido(cargaHorariaCumprida)) {
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL)
						|| tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC)) {
					cargaHorariaCumprida = cargaHorariaCumprida + matriculaVO.getHorasComplementares().intValue();
				} else {
					cargaHorariaCumprida = cargaHorariaCumprida + cargaHorariaAtividadeComplementarRealizada;
				}
				historicoAlunoRelVO.setChCumprida(cargaHorariaCumprida + "");
			} else {
				historicoAlunoRelVO.setChCumprida("0");
			}
			Integer cargaHorariaExigida = getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(gradeCurricular.getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(cargaHorariaExigida)) {
				historicoAlunoRelVO.setChExigida(cargaHorariaExigida + "");
			} else {
				historicoAlunoRelVO.setChExigida("0");
			}
			Integer creditoExigido = getFacadeFactory().getGradeCurricularFacade().consultarCreditoExigidoGrade(gradeCurricular.getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(creditoExigido)) {
				historicoAlunoRelVO.setCrDisciplina(creditoExigido + "");
			} else {
				historicoAlunoRelVO.setCrDisciplina("0");
			}
			Integer cargaHorariaExigidaPeriodoLetivo = getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGradePeriodoLetivos(gradeCurricular.getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(cargaHorariaExigidaPeriodoLetivo)) {
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
					gradeCurricular = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricular.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					historicoAlunoRelVO.setChExigidaPeriodoLetivos(String.valueOf(gradeCurricular.getCargaHoraria()));
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)) {
					Double valor = ((new Double(cargaHorariaExigidaPeriodoLetivo) / 60) * 50);
					String valorStr = valor.toString();
					if (valorStr.contains(".")) {
						valorStr = valorStr.substring(0, valorStr.indexOf("."));
					}
					historicoAlunoRelVO.setChExigidaPeriodoLetivos(valorStr + "");
				} else {
					historicoAlunoRelVO.setChExigidaPeriodoLetivos(cargaHorariaExigidaPeriodoLetivo + "");
				}
			} else {
				historicoAlunoRelVO.setChExigidaPeriodoLetivos("0");
			}
			historicoAlunoRelVO.setCargaHorariaObrigatoriaAtividadeComplementar(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorariaObrigatoria(gradeCurricular.getCodigo()).toString());
			historicoAlunoRelVO.setCargaHorariaObrigatoriaEstagio(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaObrigatoriaEstagioPorMatrizCurricular(gradeCurricular.getCodigo()));
			Boolean desconsiderarDisciplinaEstagioObrigatorio = tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC);
			historicoAlunoRelVO.setCargaHorariaDisciplinaObrigatoria(getFacadeFactory().getGradeCurricularFacade().consultarTotalDisciplinaObrigatoria(gradeCurricular.getCodigo(), desconsiderarDisciplinaEstagioObrigatorio));
			if ((gradeCurricular.getCargaHoraria() - historicoAlunoRelVO.getCargaHorariaDisciplinaObrigatoria() - historicoAlunoRelVO.getCargaHorariaObrigatoriaEstagio() - (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) ? Integer.valueOf(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) : 0)) > 0) {
				historicoAlunoRelVO.setCargaHorariaDisciplinaOptativaSerCumprida((gradeCurricular.getCargaHoraria() - historicoAlunoRelVO.getCargaHorariaDisciplinaObrigatoria() - historicoAlunoRelVO.getCargaHorariaObrigatoriaEstagio() - (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) ? Integer.valueOf(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) : 0)));
			}
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC)) {
				Integer cargaHorariaSomada = historicoAlunoRelVO.getCargaHorariaDisciplinaObrigatoria() + historicoAlunoRelVO.getCargaHorariaDisciplinaOptativaSerCumprida();
				historicoAlunoRelVO.setChExigidaPeriodoLetivos(cargaHorariaSomada.toString());
			}
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
				historicoAlunoRelVO.setChExigidaPeriodoLetivos(historicoAlunoRelVO.getCargaHorariaDisciplinaObrigatoria().toString());
				historicoAlunoRelVO.setCargaHorariaDisciplinaEstagioExigida(getFacadeFactory().getGradeCurricularFacade().consultarTotalDisciplinaEstagio(gradeCurricular.getCodigo()));
				historicoAlunoRelVO.setCargaHorariaDisciplinaOptativaSerCumprida((gradeCurricular.getCargaHoraria() - historicoAlunoRelVO.getCargaHorariaDisciplinaObrigatoria() - historicoAlunoRelVO.getCargaHorariaObrigatoriaEstagio() - historicoAlunoRelVO.getCargaHorariaDisciplinaEstagioExigida() - (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) ? Integer.valueOf(historicoAlunoRelVO.getCargaHorariaObrigatoriaAtividadeComplementar()) : 0)));
			}
		}
	}

	private void obterDadosCurso(CursoVO obj, Date dataMatricula, Integer codigoAutorizacaoCurso,
			boolean gradeAtualAluno, HistoricoAlunoRelVO historicoAlunoRelVO, Integer gradeCurricular, String matricula,
			String tipoLayout, Integer codigoRenovacaoReconhecimento, UsuarioVO usuarioVO, GradeCurricularVO gradeCurricularVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) throws Exception {
		try {
			historicoAlunoRelVO.setNomeCurso(obj.getNomeCursoEmRelatorio());
			historicoAlunoRelVO.setCodigoEmecCurso(obj.getIdCursoInep());
			if (gradeAtualAluno) {
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoResidenciaMedicaRel") || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC)) {
					/**
					 * Regra adicionada devido a solicitação pela unidade de ensino fasug (Alex)
					 * atravez do chamado 3950, para que apenas apareca o título obtido pelo aluno
					 * quando ele esteja cursando o ultimo periodo.
					 */
					PeriodoLetivoVO ultimoPeriodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarUltimoPeriodoLetivoGradeCurricular(gradeCurricular, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					MatriculaPeriodoVO ultimaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					if (ultimaMatriculaPeriodo.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor()) || ultimoPeriodoLetivo.getCodigo().equals(ultimaMatriculaPeriodo.getPeridoLetivo().getCodigo())) {
						if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
							historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormandoFeminino());
						} else {
							historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormando());
						}
					} else {
						historicoAlunoRelVO.setTituloCurso("");
					}
					historicoAlunoRelVO.setTitulacaoCurso(obj.getTitulo());

					if (obj.getPeriodicidade().equals("SE")) {
						historicoAlunoRelVO.setAnoSemestreIntegralizouMatrizCurricular(ultimaMatriculaPeriodo.getAno() + "/" + ultimaMatriculaPeriodo.getSemestre());
					} else if (obj.getPeriodicidade().equals("AN")) {
						historicoAlunoRelVO.setAnoSemestreIntegralizouMatrizCurricular(ultimaMatriculaPeriodo.getAno());
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL)) {
					historicoAlunoRelVO.setTituloCurso(obj.getTitulo_Apresentar());

					if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
						historicoAlunoRelVO.setHabilitacao(obj.getTitulacaoDoFormandoFeminino());
					} else {
						historicoAlunoRelVO.setHabilitacao(obj.getTitulacaoDoFormando());
					}
					if (!Uteis.isAtributoPreenchido(historicoAlunoRelVO.getHabilitacao())) {
						historicoAlunoRelVO.setHabilitacao(obj.getHabilitacao());
					}
				}else if(Uteis.getIsValorNumerico(tipoLayout)) {
					historicoAlunoRelVO.setTituloCurso(obj.getTitulo_Apresentar());
					if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
						historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormandoFeminino());
					} else {
						historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormando());
					}
					historicoAlunoRelVO.setGrauCurso(obj.getTitulo_Apresentar());					
					if (!Uteis.isAtributoPreenchido(historicoAlunoRelVO.getHabilitacao())) {
						historicoAlunoRelVO.setHabilitacao(obj.getHabilitacao());
					}					
					MatriculaPeriodoVO ultimaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					if (obj.getPeriodicidade().equals("SE")) {
						historicoAlunoRelVO.setAnoSemestreIntegralizouMatrizCurricular(ultimaMatriculaPeriodo.getAno() + "/" + ultimaMatriculaPeriodo.getSemestre());
					} else if (obj.getPeriodicidade().equals("AN")) {
						historicoAlunoRelVO.setAnoSemestreIntegralizouMatrizCurricular(ultimaMatriculaPeriodo.getAno());
					}
				} else {
					if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
						historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormandoFeminino());
					} else {
						historicoAlunoRelVO.setTituloCurso(obj.getTitulacaoDoFormando());
					}
					historicoAlunoRelVO.setGrauCurso(obj.getTitulo_Apresentar());
					// if (obj.getTitulo().equals("TC")) {
					// if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
					// historicoAlunoRelVO.setTituloCurso("Tecnóloga");
					// } else {
					// historicoAlunoRelVO.setTituloCurso(obj.getTitulo_Apresentar());
					// }
					// } else if (obj.getTitulo().equals("BA")) {
					// if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
					// historicoAlunoRelVO.setTituloCurso("Bacharela");
					// } else {
					// historicoAlunoRelVO.setTituloCurso("Bacharel");
					// }
					// } else if (obj.getTitulo().equals("LI")) {
					// if (historicoAlunoRelVO.getSexo().equals("Feminino")) {
					// historicoAlunoRelVO.setTituloCurso("Licenciada");
					// } else {
					// historicoAlunoRelVO.setTituloCurso("Licenciado");
					// }
					// } else {
					// historicoAlunoRelVO.setTituloCurso(obj.getTitulo_Apresentar());
					// }
				}
			} else {
				historicoAlunoRelVO.setTituloCurso("");
			}
			
			//Caso a Habilitação da GradeCurricular esteja preenchida utilizar o mesmo senão utilizar a do curso.
			if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_REL)) {
				if (Uteis.isAtributoPreenchido(gradeCurricularVO.getHabilitacao())) {
					historicoAlunoRelVO.setHabilitacao(gradeCurricularVO.getHabilitacao());
				} else {
					historicoAlunoRelVO.setHabilitacao(obj.getHabilitacao());
				}
			}

			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC)) {
				historicoAlunoRelVO.setPeriodicidadeCurso(obj.getPeriodicidade());
			} else {
				historicoAlunoRelVO.setPeriodicidadeCurso(obj.getPeriodicidade_Apresentar());
			}
			historicoAlunoRelVO.setAreaConhecimentoCurso(obj.getAreaConhecimento().getNome());
			historicoAlunoRelVO.setListaConfiguracaoAcademicoNotaConceitoVO(obj.getConfiguracaoAcademico().getConfiguracaoAcademicaMediaFinalConceito());
			if(tipoLayout.equals(HISTORICO_ALUNO_POS_18) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) ||  tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) { 
				historicoAlunoRelVO.setNivelEducacionalCurso(TipoNivelEducacional.getDescricao(obj.getNivelEducacional()));	
				if(tipoLayout.equals(HISTORICO_ALUNO_POS_18)) {
					historicoAlunoRelVO.setTitulacaoCurso(TituloCursoPos.getDescricao(obj.getTitulo()));
				}else {
					historicoAlunoRelVO.setTitulacaoCurso(obj.getTitulo_Apresentar());
				}
				executarMontagemDadosAutorizacaoCurso(obj, historicoAlunoRelVO, tipoLayout, dataMatricula, codigoAutorizacaoCurso, codigoRenovacaoReconhecimento, configuracaoLayoutHistoricoVO);
				if(!Uteis.isAtributoPreenchido(historicoAlunoRelVO.getReconhecimento())) {
					historicoAlunoRelVO.setReconhecimento(obj.getNrRegistroInterno());
				}
			}else {
				historicoAlunoRelVO.setNivelEducacionalCurso(obj.getNivelEducacional().toUpperCase());	
				executarMontagemDadosAutorizacaoCurso(obj, historicoAlunoRelVO, tipoLayout, dataMatricula, codigoAutorizacaoCurso, codigoRenovacaoReconhecimento, configuracaoLayoutHistoricoVO);
			}				

		} catch (Exception e) {
			throw e;
		}
	}

	private void consultarDadosProfessorMinistrouAula(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, HistoricoVO historicoVO, String tipoLayout, Boolean apresentarApenasProfessorTitulacaoTurmaOrigem, boolean posGraduacao, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception {
		Boolean apresentarApenasProfessorTitulacaoTurmaOrigemAcordoTipoLayout = Boolean.FALSE;
		if (apresentarApenasProfessorTitulacaoTurmaOrigem != null) {
			apresentarApenasProfessorTitulacaoTurmaOrigemAcordoTipoLayout = apresentarApenasProfessorTitulacaoTurmaOrigem && (tipoLayout.equals("HistoricoAlunoPos3Rel") || tipoLayout.equals("HistoricoAlunoPos2Rel") || tipoLayout.equals("HistoricoAlunoPos17Rel") || tipoLayout.equals("HistoricoAlunoPos18Rel"));
		} else {
			apresentarApenasProfessorTitulacaoTurmaOrigemAcordoTipoLayout = tipoLayout.equals("HistoricoAlunoPos2Rel");
		}
		Optional<SqlRowSet> optProfessorMinistrouAula = getFacadeFactory().getHistoricoFacade().consultarProfessorTitularTitulacao(historicoVO, apresentarApenasProfessorTitulacaoTurmaOrigemAcordoTipoLayout,true);
		optProfessorMinistrouAula.ifPresent(s -> montarDadosConsultaProfessorMinistrouAula(s, historicoAlunoDisciplinaRelVO, tipoLayout, configuracaoLayoutHistoricoVO, xmlDiploma));
	}

	private FuncionarioVO consultarNomeProfessorTitularMinistrouAula(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino,int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		FuncionarioVO funcionarioVO = new FuncionarioVO();
		funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarProfessorComAulaProgramadaPorMatriculaDisciplinaNome(matricula, disciplina, nomeProfessor, unidadeEnsino, false, nivelMontarDados, usuarioVO);
		// if (funcionarioVO == null) {
		// funcionarioVO =
		// getFacadeFactory().getFuncionarioFacade().consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(matricula,
		// disciplina, nomeProfessor, unidadeEnsino, true, false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		// }
		return funcionarioVO != null ? funcionarioVO : new FuncionarioVO();
	}

	public static String getDesignIReportRelatorio(TipoNivelEducacional tipoNivelEducacional, String tipoLayout, boolean imprimirExcel) throws Exception {		
		if (tipoNivelEducacional != null) {
			switch (tipoNivelEducacional) {
			case BASICO:
				if (tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout1Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoEnsinoBasicoLayout2Rel.jrxml");
				}
			case MEDIO:
				if (tipoLayout.equals("HistoricoAlunoEnsinoMedio")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeEnsinoMedio() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout3Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoEnsinoMedioLayout3Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout2Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoEnsinoMedioLayout2Rel.jrxml");
				}
			case SEQUENCIAL:
				return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeSeq() + ".jrxml");
			case SUPERIOR:
				if (tipoLayout.equals("HistoricoAlunoRel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoExcelRel" + ".jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout2Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout2Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout2ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout3Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout3Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout3ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout4Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout4Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout4ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout5Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout5Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout5ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout6Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout6Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout6ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout8Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout8Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout8ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout9Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout9Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout9ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout10Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout10Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout10ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout11Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout11Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout11ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout7Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout7Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout7ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout12Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout12Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout12ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_REL + ".jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout13Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout13Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout13ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("DiplomaAlunoSuperior5RelVerso")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DiplomaAlunoSuperior5RelVerso.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout14Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout14Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout14ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout15Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout15Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout15ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout16Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout16Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout16ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout17Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout17Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout17ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_2_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_3_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_4_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_6_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_7_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_8_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_9_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_10_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_11_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_13_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_14_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_15_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_16_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_17_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_18_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_19_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				}else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_20_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_21_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				}else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24 + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_EXCEL + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC + ".jrxml");
					}
				} 
				
			case POS_GRADUACAO:
				if (tipoLayout.equals("HistoricoAlunoPosRel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos2Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos2() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos14Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos14() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos3Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos3() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoVersoDiplomaRel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoDiploma() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoVersoDiplomaRel2")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoDiploma2() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos2Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos2Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos14Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos14Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos3Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos3Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPosPaisagemRel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePosPaisagem() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoResidenciaMedicaRel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoResidenciaMedicaRel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos12Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos12() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos16Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadePos16() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos16Excel() + ".jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoPos17Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadePos17() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos17Excel() + ".jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoPos18Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadePos18() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos18Excel() + ".jrxml");
					}
			}
				
			case EXTENSAO:
				if (tipoLayout.equals("HistoricoAlunoPosRel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos2Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos2() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos14Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos14() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos3Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos3() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoVersoDiplomaRel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoDiploma() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoVersoDiplomaRel2")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeVersoDiploma2() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos2Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos2Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos14Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos14Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos3Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos3Excel() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPosPaisagemRel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePosPaisagem() + ".jrxml");
				}else if (tipoLayout.equals("HistoricoAlunoPos17Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadePos17() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos17Excel() + ".jrxml");
					}
				}else if (tipoLayout.equals("HistoricoAlunoPos18Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadePos18() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos18Excel() + ".jrxml");
					}
				}else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico"	+ File.separator + getIdEntidadeExt19() + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExt19Excel() + ".jrxml");
			   		}
			    }
			case GRADUACAO_TECNOLOGICA:
				// return ("relatorio" + File.separator + "designRelatorio" +
				// File.separator + "academico" + File.separator +
				// getIdEntidade() + ".jrxml");
				if (tipoLayout.equals("HistoricoAlunoRel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout2Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout2Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout3Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout3Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout4Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout4Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout5Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout5Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout6Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout6Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout13Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout13Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout8Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout8Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout10Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout10Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout10ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout12Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout12Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout11Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout11Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout11ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout17Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout17Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout17ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals("HistoricoAlunoLayout15Rel")) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout15Rel.jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout15ExcelRel.jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_2_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_3_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_3_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_4_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_5_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_6_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_7_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_8_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_9_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_10_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_11_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_12_MEDICINA_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_13_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_14_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_14_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_15_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_16_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_16_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_17_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_18_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_19_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_19_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				}else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_20_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_20_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_21_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_22_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24 + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_EXCEL + ".jrxml");
					}
				} else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC + ".jrxml");
					}
				} 

			case PROFISSIONALIZANTE:
				if (tipoLayout.equals("HistoricoAlunoNivelTecnico2Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoNivelTecnico2Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout3Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout3Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout6Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout6Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout8Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout8Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout12Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout12Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoLayout17Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout17Rel.jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel")) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoNivelTecnicoLayout4Rel.jrxml");
				}else {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoNivelTecnicoRel.jrxml");
				}

			case MESTRADO:
				if (tipoLayout.equals("HistoricoAlunoPos15Rel") && !imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos15() + ".jrxml");
				} else if (tipoLayout.equals("HistoricoAlunoPos15Rel") && imprimirExcel) {
					return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePos15() + ".jrxml");
				}else if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC)) {
					if (!imprimirExcel) {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_23_PORTARIA_MEC + ".jrxml");
					} else {
						return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + HISTORICO_ALUNO_LAYOUT_23_EXCEL_PORTARIA_MEC + ".jrxml");
					}
				} 

			default:
				break;
			}
		} else {
			throw new ConsistirException("O curso do aluno selecionado não possui nível educacional");
		}
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return (HISTORICO_ALUNO_REL);
	}

	public static String getIdEntidadeEnsinoMedio() {
		return (HISTORICO_ALUNO_ENSINO_MEDIO);
	}

	public static String getIdEntidadePos() {
		return ("HistoricoAlunoPosRel");
	}

	public static String getIdEntidadePosPaisagem() {
		return ("HistoricoAlunoPosPaisagemRel");
	}

	public static String getIdEntidadePos2() {
		return ("HistoricoAlunoPos2Rel");
	}

	public static String getIdEntidadePos3() {
		return ("HistoricoAlunoPos3Rel");
	}

	public static String getIdEntidadeVersoDiploma() {
		return ("HistoricoAlunoVersoDiplomaRel");
	}

	public static String getIdEntidadeVersoDiploma2() {
		return ("HistoricoAlunoVersoDiplomaRel2");
	}

	public static String getIdEntidadePos2Excel() {
		return ("HistoricoAlunoPos2ExcelRel");
	}

	public static String getIdEntidadePos3Excel() {
		return ("HistoricoAlunoPos3ExcelRel");
	}

	public static String getIdEntidadeSeq() {
		return ("HistoricoAlunoSeqRel");
	}


	public static String getIdEntidadePos14() {
		return ("HistoricoAlunoPos14Rel");
	}

	public static String getIdEntidadePos14Excel() {
		return ("HistoricoAlunoPos14ExcelRel");
	}

	public static String getIdEntidadePos15() {
		return ("HistoricoAlunoPos15Rel");
	}

	public static String getIdEntidadePos15Excel() {
		return ("HistoricoAlunoPos15ExcelRel");
	}
	
	public static String getIdEntidadePos16() {
		return ("HistoricoAlunoPos16Rel");
	}

	public static String getIdEntidadePos16Excel() {
		return ("HistoricoAlunoPos16ExcelRel");
	}
	
	public static String getIdEntidadePos17() {
		return ("HistoricoAlunoPos17Rel");
	}

	public static String getIdEntidadePos17Excel() {
		return ("HistoricoAlunoPos17ExcelRel");
	}
	
	
	public static String getIdEntidadePos18() {
		return ("HistoricoAlunoPos18Rel");
	}

	public static String getIdEntidadePos18Excel() {
		return ("HistoricoAlunoPos18Rel");
	}
	
	public static String getIdEntidadeExt19() {
		return ("HistoricoAlunoLayout19ExtRel");
	}
	
	public static String getIdEntidadeExt19Excel() {
		return ("HistoricoAlunoLayout19ExtExcelRel");
	}

	public void montarDadosSituacaoFinal(List<HistoricoAlunoDisciplinaRelVO> objs, MatriculaVO matriculaVO, Boolean desconsiderarSituacaoMatriculaPeriodo, String tipoLayout) throws Exception {
		Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplina(objs, desconsiderarSituacaoMatriculaPeriodo);
		Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatricula, realizarGeracaoMapMatriculaConfiguracaoAcademicaFechamentoPeriodo(objs, matriculaVO.getMatricula(), desconsiderarSituacaoMatriculaPeriodo));

		for (String anoSemestre : mapMatriculaSituacaoFinal.keySet()) {
			SituacaoHistorico situacaoHistorico = mapMatriculaSituacaoFinal.get(anoSemestre);

			if (situacaoHistorico.equals(SituacaoHistorico.APROVADO_COM_DEPENDENCIA)) {
				int qtdeRepro = 0;
				int qtdeDepAprov = 0;
				for (HistoricoAlunoDisciplinaRelVO obj2 : objs) {
					if (obj2.getAnoSemstre().equals(obj2.getAnoSemstre()) && (obj2.getSituacaoPeriodo().equals("RE") || obj2.getSituacaoPeriodo().equals("RF") || obj2.getSituacaoPeriodo().equals("RP"))) {
						qtdeRepro++;
						for (HistoricoAlunoDisciplinaRelVO obj3 : objs) {
							if (obj3.getAnoSemstre().compareTo(obj2.getAnoSemstre()) >= 1 && obj3.getCodigoDisciplina().equals(obj2.getCodigoDisciplina()) && obj3.getDisciplinaDependencia() && (obj3.getSituacaoPeriodo().equals("AP") || obj3.getSituacaoPeriodo().equals("AA") || obj3.getSituacaoPeriodo().equals("CC") || obj3.getSituacaoPeriodo().equals("CH") || obj3.getSituacaoPeriodo().equals("CR") || obj3.getSituacaoPeriodo().equals("AE"))) {
								qtdeDepAprov++;
								break;
							}
						}
					}
				}
				if (qtdeRepro == qtdeDepAprov) {
					mapMatriculaSituacaoFinal.put(anoSemestre, SituacaoHistorico.APROVADO);
				}
			}
		}
		for (HistoricoAlunoDisciplinaRelVO obj : objs) {
			if (mapMatriculaSituacaoFinal.containsKey(obj.getAnoSemstre() + "_" + obj.getNomePeriodo())) {
				SituacaoHistorico situacaoHistorico = mapMatriculaSituacaoFinal.get(obj.getAnoSemstre() + "_" + obj.getNomePeriodo());
				obj.setSituacaoPeriodo(situacaoHistorico.getDescricao());
				obj.setSituacaoPeriodoEnum(situacaoHistorico);
				obj.setSituacaoFinalHistoricoEnum(situacaoHistorico);
				if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) || tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel")) {
					obj.setSituacaoFinal(situacaoHistorico.getDescricao());
				} else {
					obj.setSituacaoFinal(situacaoHistorico.getValor());
				}
			}
		}
	}

	private Map<String, Map<String, String>> realizarGeracaoMapMatriculaDisciplina(List<HistoricoAlunoDisciplinaRelVO> objs, Boolean desconsiderarSituacaoMatriculaPeriodo) throws Exception {
		Map<String, Map<String, String>> mapMatricula = new HashMap<String, Map<String, String>>(0);
		for (HistoricoAlunoDisciplinaRelVO obj : objs) {
			if (!mapMatricula.containsKey(obj.getAnoSemstre() + "_" + obj.getNomePeriodo())) {
				mapMatricula.put(obj.getAnoSemstre() + "_" + obj.getNomePeriodo(), new HashMap<String, String>(0));
			}
			if (!mapMatricula.get(obj.getAnoSemstre() + "_" + obj.getNomePeriodo()).containsKey(obj.getNomeDisciplina())) {
				SituacaoMatriculaPeriodoEnum sit = SituacaoMatriculaPeriodoEnum.getEnumPorValor(obj.getSituacaoMatriculaPeriodo());

				if (sit != null && !desconsiderarSituacaoMatriculaPeriodo && (sit.equals(SituacaoMatriculaPeriodoEnum.TRANCADA) || sit.equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA) || sit.equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA) || sit.equals(SituacaoMatriculaPeriodoEnum.CANCELADA) || sit.equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO) || sit.equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA))) {
					mapMatricula.get(obj.getAnoSemstre() + "_" + obj.getNomePeriodo()).put(obj.getNomeDisciplina(), obj.getSituacaoMatriculaPeriodo());
				} else {
					mapMatricula.get(obj.getAnoSemstre() + "_" + obj.getNomePeriodo()).put(obj.getNomeDisciplina(), obj.getSituacaoFinal());
				}
			}
		}
		return mapMatricula;
	}

	/**
	 * Criado para a ABEU pois existem matricula periodo trancada, cancelada,
	 * abandonada e transferida que não possuem disciplinas no histórico, porém
	 * devem ser apresentadadas no historico com a sua devida situação, este está
	 * habilitado para o LAYOUT 4 e quando a Situacao Disciplina for TODAS
	 */		
	public void realizarBuscaMatriculaPeriodoEvasaoSemVinculoComHistorico(HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO,int ordem) throws Exception {
		if (((tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_21_PORTARIA_MEC)  || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_22_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC)) && filtroRelatorioAcademicoVO.getAprovado() && filtroRelatorioAcademicoVO.getReprovado() && filtroRelatorioAcademicoVO.getTrancamentoHistorico() && filtroRelatorioAcademicoVO.getAbandonoHistorico() && filtroRelatorioAcademicoVO.getCursando() && filtroRelatorioAcademicoVO.getTransferidoHistorico() && filtroRelatorioAcademicoVO.getCanceladoHistorico())
				|| (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)   
						|| 	(tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC ))					
						&& ((filtroRelatorioAcademicoVO.getAprovado() && filtroRelatorioAcademicoVO.getReprovado() && filtroRelatorioAcademicoVO.getTrancamentoHistorico() && filtroRelatorioAcademicoVO.getAbandonoHistorico() && filtroRelatorioAcademicoVO.getCursando() && filtroRelatorioAcademicoVO.getTransferidoHistorico() && filtroRelatorioAcademicoVO.getCanceladoHistorico()) || filtroRelatorioAcademicoVO.getAprovado()))) {
			
			List<MatriculaPeriodoVO> matriculaPeriodoVOs = consultarMatriculaPeriodoEvasaoSemVinculoComHistorico(matriculaVO, gradeCurricularVO, tipoLayout);
			
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_18_PORTARIA_MEC) ) {
                if(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().isEmpty()) {
                	return; 
                }
				for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRel : historicoAlunoRelVO
						.getListaHistoricoAlunoDisciplinaRelVOs()) {
					if (historicoAlunoDisciplinaRel.getSituacaoHistorico().equals("RE")
							&& historicoAlunoDisciplinaRel.getSituacaoMatriculaPeriodo().equals("CA")) {
						return;
					} else if (!filtroRelatorioAcademicoVO.getReprovado()
							|| !filtroRelatorioAcademicoVO.getCanceladoHistorico()
							|| !filtroRelatorioAcademicoVO.getAbandonoHistorico()
							|| !filtroRelatorioAcademicoVO.getPreMatriculaHistorico()
							|| !filtroRelatorioAcademicoVO.getTrancamentoHistorico()
							|| !filtroRelatorioAcademicoVO.getTransferidoHistorico()) {
						return;

					} else {
						for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
							if (matriculaPeriodoVO.getSituacaoMatriculaPeriodo()
									.equals(historicoAlunoDisciplinaRel.getSituacaoMatriculaPeriodo())) {
								return;
							}
						}
					}

				}
			}			
			for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
				HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO = new HistoricoAlunoDisciplinaRelVO();
				if (!matriculaPeriodoVO.getSemestre().trim().isEmpty()) {
					historicoAlunoDisciplinaRelVO.setAnoSemstre(matriculaPeriodoVO.getAno() + "/" + matriculaPeriodoVO.getSemestre());
				} else if (!matriculaPeriodoVO.getAno().trim().isEmpty()) {
					historicoAlunoDisciplinaRelVO.setAnoSemstre(matriculaPeriodoVO.getAno());
				}
				historicoAlunoDisciplinaRelVO.setNumeroPeriodoLetivo(matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo());
				//historicoAlunoDisciplinaRelVO.setNomePeriodo(matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo()+"°");
				historicoAlunoDisciplinaRelVO.setSituacaoFinal(SituacaoMatriculaPeriodoEnum.getEnumPorValor(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()).getValor());
				historicoAlunoDisciplinaRelVO.setSituacaoFinalEnum(SituacaoMatriculaPeriodoEnum.getEnumPorValor(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()));
				historicoAlunoDisciplinaRelVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getEnumPorValor(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()).getValor());
				historicoAlunoDisciplinaRelVO.setSituacaoPeriodo(SituacaoMatriculaPeriodoEnum.getEnumPorValor(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()).getDescricao());
				historicoAlunoDisciplinaRelVO.setNomeDisciplina(SituacaoMatriculaPeriodoEnum.getEnumPorValor(matriculaPeriodoVO.getSituacaoMatriculaPeriodo()).getDescricao());
				
				// // trecho de codigo adicionado para tratar requisito do cliente, que pediu
				// para apresentar os testos abaixo, somente no layout 6;
				// if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL)) {
				// if
				// (historicoAlunoDisciplinaRelVO.getNomeDisciplina().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getDescricao()))
				// {
				// historicoAlunoDisciplinaRelVO.setNomeDisciplina("Trancamento");
				// }
				// if
				// (historicoAlunoDisciplinaRelVO.getNomeDisciplina().equals(SituacaoMatriculaPeriodoEnum.CANCELADA.getDescricao()))
				// {
				// historicoAlunoDisciplinaRelVO.setNomeDisciplina("Cancelamento");
				// }
				// }
				
				historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().add(historicoAlunoDisciplinaRelVO);
			}
			if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_POS_18) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_EXCEL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC_EXCEL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_EXT19)) {
				switch (OrdemHistoricoDisciplina.getEnum(ordem)) {				
				case PERIODO_LETIVO:
				    Ordenacao.ordenarLista(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), "numeroPeriodoLetivo");					
					break;				
				case ANO_SEMESTRE:
					Ordenacao.ordenarLista(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), "ordenarAnoSemestre");
					break;
				case ANO_SEMESTRE_PERIODO_SITUACAO:
					Ordenacao.ordenarLista(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), "ordenarAnoSemestrePeriodoSituacao");
					break;
				case ANO_SEMESTRE_SITUACAO_PERIODO:
					Ordenacao.ordenarLista(historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), "ordenarAnoSemestreSituacaoPeriodo");
					break;
				default:
					break;
				}
			}
		}
	}

	private List<MatriculaPeriodoVO> consultarMatriculaPeriodoEvasaoSemVinculoComHistorico(MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, String tipoLayout) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct m.codigo, m.ano, m.semestre, m.situacaomatriculaperiodo , p.periodoletivo  as periodo from matriculaperiodo m ");
		sql.append(" inner join periodoletivo p on p.codigo = m.periodoletivomatricula");
		sql.append(" where m.matricula = '").append(matriculaVO.getMatricula()).append("'  ");
		sql.append(" and m.situacaomatriculaperiodo in ('TS', 'TI', 'CA', 'AC', 'TR','PR','PC') ");
		sql.append(" and m.codigo not in (select matriculaperiodo from historico where matricula = '").append(matriculaVO.getMatricula()).append("' and matrizcurricular = ").append(gradeCurricularVO.getCodigo());
		sql.append(" and historico.situacao in ('AA','CC','IS','CH','AP','AE','AB') ");
		sql.append(" and m.ano = historico.anohistorico and m.semestre = historico.semestrehistorico ");
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) || 
				tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) {
			sql.append(" and historicodisciplinaforagrade = false");
		}
		sql.append(") order by m.ano, m.semestre, m.situacaomatriculaperiodo; ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaPeriodoVO> matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		MatriculaPeriodoVO matriculaPeriodoVO = null;
		while (rs.next()) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
			matriculaPeriodoVO.getPeriodoLetivo().setPeriodoLetivo(rs.getInt("periodo"));
			matriculaPeriodoVO.setCodigo(rs.getInt("codigo"));
			matriculaPeriodoVO.setAno(rs.getString("ano"));
			matriculaPeriodoVO.setSemestre(rs.getString("semestre"));
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo(rs.getString("situacaomatriculaperiodo"));
			matriculaPeriodoVOs.add(matriculaPeriodoVO);
		}
		return matriculaPeriodoVOs;
	}

	public void montarDadosCursoEnderecoEstabelecimentoAnterior(Integer aluno, HistoricoAlunoRelVO obj, UsuarioVO usuarioVO) throws Exception {
		FormacaoAcademicaVO dadosAnterior = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoa(aluno, false, usuarioVO);
		obj.setCursoAnterior(dadosAnterior.getCurso());
		obj.setEstabelecimentoAnterior(dadosAnterior.getInstituicao());
		obj.setEnderecoEstabelecimentoAnterior("");
	}

	/**
	 * Responsável por executar a montagem dos dados pertinentes à Autorização do
	 * Curso. Os dados referentes a nrRegistroInterno e publicacaoDO do
	 * HistoricoAlunoRelVO são preenchidos através dos atributos nrRegistroInterno e
	 * dataPublicacaoDO que são cadastrados no curso (Dados Básicos). Os dados
	 * referente a reconhecimento e dataReconhecimentodo HistoricoAlunoRelVO são
	 * preenchidos através dos atributos nome e data da autorização que são
	 * cadastrados no curso (Reconhecimento Curso). Os dados referentes a
	 * primeiroReconhecimento e dataPrimeiroReconhecimentodo HistoricoAlunoRelVO são
	 * preenchidos através dos atributos nome e data da primeira autorização que são
	 * cadastrados no curso (Reconhecimento Curso).
	 * 
	 * @author Wellington Rodrigues - 15/04/2015
	 * @param cursoVO
	 * @param historicoAlunoRelVO
	 * @param tipoLayout
	 * @param dataMatricula
	 * @param autorizacaoCurso
	 * @throws Exception
	 */
	public void executarMontagemDadosAutorizacaoCurso(CursoVO cursoVO, HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout, Date dataMatricula, Integer codigoAutorizacaoCurso, Integer codigoRenovacaoReconhecimento, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) throws Exception {
		historicoAlunoRelVO.setNrRegistroInterno(cursoVO.getNrRegistroInterno());
		historicoAlunoRelVO.setPublicacaoDO(Uteis.getData(cursoVO.getDataPublicacaoDO(), "dd/MM/yyyy"));

		AutorizacaoCursoVO renovacaoReconhecimentoCursoVO = new AutorizacaoCursoVO();
		if (Uteis.isAtributoPreenchido(codigoRenovacaoReconhecimento)) {
			renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(codigoRenovacaoReconhecimento, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		} else {
			/* Consulta última renovação */
			renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(cursoVO.getCodigo(), dataMatricula, Uteis.NIVELMONTARDADOS_COMBOBOX);
			if (!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
				if ((!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)) || (dataMatricula == null && (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC)))) {
					if (cursoVO.getNivelEducacionalPosGraduacao()) {
						renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoPos(cursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
					} else {
						renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(cursoVO.getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
					}
				}
			}
		}

		// historicoAlunoRelVO.setReconhecimento("100 CARACTERES - TESTE ATE QUEBRAR A
		// LINHA DO RECONHECIMENTO QUE VEM DA RENOVACAO DE MATRICULA AAAAA");
		// historicoAlunoRelVO.setCredenciamentoPortaria("TESTE");
		if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
			historicoAlunoRelVO.setReconhecimento(renovacaoReconhecimentoCursoVO.getNome());
			historicoAlunoRelVO.setDataReconhecimento(Uteis.getData(renovacaoReconhecimentoCursoVO.getData(), "dd/MM/yyyy"));
		}
		renovacaoReconhecimentoCursoVO = null;
		// if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_PORTARIA_MEC) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(
		// HISTORICO_ALUNO_LAYOUT_4_PORTARIA_MEC) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_PORTARIA_MEC) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_7_PORTARIA_MEC) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_10_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_10_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_PORTARIA_MEC)||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) ||
		// tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_PORTARIA_MEC)
		//
		// )
		//
		// {
		if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_4_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_5_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_6_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_8_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_9_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_10_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_13_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_15_REL) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_17_REL) || validarTipoLayoutGraduacao(tipoLayout, configuracaoLayoutHistoricoVO) || Uteis.getIsValorNumerico(tipoLayout)) {
			AutorizacaoCursoVO autorizacaoCursoVO = new AutorizacaoCursoVO();
			if (Uteis.isAtributoPreenchido(codigoAutorizacaoCurso)) {
				autorizacaoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(codigoAutorizacaoCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			} else {
				autorizacaoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(cursoVO.getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
			}
			if (Uteis.isAtributoPreenchido(autorizacaoCursoVO)) {
				historicoAlunoRelVO.setPrimeiroReconhecimento(autorizacaoCursoVO.getNome());
				historicoAlunoRelVO.setDataPrimeiroReconhecimento(Uteis.getData(autorizacaoCursoVO.getData(), "dd/MM/yyyy"));
			}

			if (historicoAlunoRelVO.getDataReconhecimento().equals(historicoAlunoRelVO.getDataPrimeiroReconhecimento())) {
				historicoAlunoRelVO.setDataReconhecimento("");
				historicoAlunoRelVO.setReconhecimento("");
			}
			autorizacaoCursoVO = null;
		}
	}

	private void realizarExclusaoHistoricoMesmoPeriodoLetivoCanceladoAbandonoCurso(List<HistoricoVO> listaHistoricoVOs, String matricula, Boolean apresentarDisciplinaForaGrade) throws Exception {
		List<Integer> matriculaPeriodoConsiderar = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoMesmoPeriodoLetivoDesconsiderandoCanceladaAbandonoCurso(matricula);
		for (Iterator<HistoricoVO> iterator = listaHistoricoVOs.iterator(); iterator.hasNext();) {
			HistoricoVO historicoVO = iterator.next();
			if (historicoVO.getHistoricoDisciplinaForaGrade() && apresentarDisciplinaForaGrade) {
				continue;
			}
			if (!matriculaPeriodoConsiderar.contains(historicoVO.getMatriculaPeriodo().getCodigo())) {
				iterator.remove();
			}
		}
	}

	@Override
	public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(HistoricoAlunoRelVO historicoAlunoRel,
			String tipoLayout) {		
		StringBuilder stringLengenda = new StringBuilder();
		HashMap<String, String> mapaSituacao = new HashMap<String, String>(0);
		for (Iterator<HistoricoAlunoDisciplinaRelVO> iterator = historicoAlunoRel.getListaHistoricoAlunoDisciplinaRelVOs().iterator(); iterator.hasNext();) {
			HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO = (HistoricoAlunoDisciplinaRelVO) iterator.next();
			if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_PORTARIA_MEC) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_REL) && !tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_12_MEDICINA_REL) && !tipoLayout.equals("HistoricoAlunoPos15Rel")) {
				mapaSituacao.put(historicoAlunoDisciplinaRelVO.getSituacaoPeriodo(), SituacaoHistorico.getDescricao(historicoAlunoDisciplinaRelVO.getSituacaoPeriodo()));
			}

		}
		for (Map.Entry<String, String> elemento : mapaSituacao.entrySet()) {
			stringLengenda.append(elemento.getKey());
			stringLengenda.append(" - ");
			stringLengenda.append(elemento.getValue());
			stringLengenda.append(" / ");
		}
		mapaSituacao = null;
		if(Uteis.isAtributoPreenchido(stringLengenda)) {			
			return stringLengenda.toString().substring(0, stringLengenda.toString().length() - 2);
			}
			return "";
	}
   
	@Override
	public String realizarCriacaoLegendaTitulacaoProfessorDisciplinaHistorico(HistoricoAlunoRelVO historicoAlunoRel, String tipoLayout) {
		StringBuilder stringLengenda = new StringBuilder();
		HashMap<String, String> mapaSituacao = new HashMap<String, String>(0);
		for (Iterator<HistoricoAlunoDisciplinaRelVO> iterator = historicoAlunoRel.getListaHistoricoAlunoDisciplinaRelVOs().iterator(); iterator.hasNext();) {
			HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO = (HistoricoAlunoDisciplinaRelVO) iterator.next();
			if (tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_11_EXCEL_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_PORTARIA_MEC) || tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_2_EXCEL_PORTARIA_MEC)) {
				if (historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor().equals("MS")) {
					historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("ME");
				}
				if (historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor().equals("EP")) {
					historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("ES");
				}
			}
			if(Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor())) {
				mapaSituacao.put(historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor(), historicoAlunoDisciplinaRelVO.getTitulacaoProfessor());
			}	
		}
		for (Map.Entry<String, String> elemento : mapaSituacao.entrySet()) {			
				stringLengenda.append(elemento.getKey());
				stringLengenda.append(" - ");
				stringLengenda.append(elemento.getValue());
				stringLengenda.append(" / ");
		}
		mapaSituacao = null;
		if(Uteis.isAtributoPreenchido(stringLengenda)) {
			return stringLengenda.toString().substring(0, stringLengenda.toString().length() - 2);
		}
	    return "";
	}

	public Double calcularMediaNotasSemestreAluno(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs, MatriculaPeriodoVO matriculaPeriodoVO) {
		Double somatorio = 0.0;
		int quantidade = 0;
		String semestre = matriculaPeriodoVO.getAno() + "/" + matriculaPeriodoVO.getSemestre();
		if (historicoAlunoDisciplinaRelVOs.isEmpty()) {
			return 0.0;
		}
		for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoDisciplinaRelVOs) {
			if (!historicoAlunoDisciplinaRelVO.getAnoSemstre().equals(semestre) && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("PR") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("AC") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("AA") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("CC") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("IS") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("CH") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("AB") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("NC") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("CA") && !historicoAlunoDisciplinaRelVO.getSituacaoPeriodo().equals("TF")) {
				somatorio += historicoAlunoDisciplinaRelVO.getMediaFinalCalculo();
				quantidade++;
			}
		}
		if (quantidade == 0) {
			return 0.0;
		}
		return Uteis.arrendondarForcando2CasasDecimais(somatorio / quantidade);
	}

	private void montarDadosConsultaProfessorMinistrouAula(SqlRowSet sqlRowSet, HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, String tipoLayout, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) {
		String professor = "";
		if (tipoLayout.equals("HistoricoAlunoPosRel") || tipoLayout.equals("HistoricoAlunoPosPaisagemRel")) {
			if (sqlRowSet.getString("sexo") != null) {
				if (sqlRowSet.getString("sexo").equals("F")) {
					professor = "Profª ";
				} else {
					professor = "Profº ";
				}
			} else {
				professor = "Prof ";
			}
		}
		historicoAlunoDisciplinaRelVO.setProfessor(professor + sqlRowSet.getString("nome"));

		String escolaridade = "" + sqlRowSet.getString("escolaridade");
		String sexo = "" + sqlRowSet.getString("sexo");
		if (tipoLayout.equals("HistoricoAlunoPos3Rel") || tipoLayout.equals("HistoricoAlunoPos16Rel") || tipoLayout.equals("HistoricoAlunoPos17Rel") || tipoLayout.equals("HistoricoAlunoPos18Rel") || validarTipoLayoutGraduacao(tipoLayout, configuracaoLayoutHistoricoVO) || xmlDiploma) {
			montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(historicoAlunoDisciplinaRelVO, sexo, escolaridade);
			return;
		}
		if (sexo.equals("F") && escolaridade.equalsIgnoreCase("DR")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("DRA");
		} else if (sexo.equals("F") && escolaridade.equalsIgnoreCase("GR")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("GRA");
		} else if (escolaridade.equalsIgnoreCase("EP")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("ESP");
		} else {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor(escolaridade);
		}
	}

	public void montarDadosCursoGraduacaoVindoFormacaoAcademica(Integer aluno, HistoricoAlunoRelVO obj, UsuarioVO usuarioVO) throws Exception {
		FormacaoAcademicaVO dadosGraduacao = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridadeOrdenandoUltimaDataConclusao(aluno, NivelFormacaoAcademica.GRADUACAO, false, usuarioVO);
		obj.setNomeCursoGraduacao(dadosGraduacao.getCurso());
		obj.setInstituicaoCursoGraduacao(dadosGraduacao.getInstituicao());
		obj.setCidadeCursoGraduacao(dadosGraduacao.getCidade().getNome());
		obj.setEstadoCursoGraduacao(dadosGraduacao.getCidade().getEstado().getNome());
		if (dadosGraduacao.getSituacao().equals("CU")) {
			obj.setAnoConclusaoCursoGraduacao(UteisData.getAnoDataString(dadosGraduacao.getDataFim()));
		} else {
			obj.setAnoConclusaoCursoGraduacao(dadosGraduacao.getAnoDataFim());
		}
		obj.setModalidadeConclusaoCursoGraduacao(dadosGraduacao.getModalidade());
		if (Uteis.isAtributoPreenchido(dadosGraduacao.getAnoSemestreConclusao_Apresentar())) {
			obj.setAnoSemestreConclusaoCurso(dadosGraduacao.getAnoSemestreConclusao_Apresentar());
		}
	}

	public static String getIdEntidadePos12() {
		return ("HistoricoAlunoPos12Rel");
	}

	private void montarNomeclaturaFormacaoAcademicaLayoutPosGraduacao(
			HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, String sexo, String escolaridade) {
		
		if (escolaridade.equalsIgnoreCase("GR")) {
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Graduada");
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("GRA");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Grad");
			} else {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Graduado");
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("GR");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Grad");
			}
		} else if (escolaridade.equalsIgnoreCase("DR")) {
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Doutora");
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("DRA");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Dra");
			} else {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Doutor");
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("DR");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Dr");
			}
		} else if (escolaridade.equalsIgnoreCase("PO")) {
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Pós-Graduada");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Pos");
			} else {
				historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Pós-Graduado");
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Pos");
			}
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("POS");
			
		}else if (escolaridade.equalsIgnoreCase("EM")) {
         	historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Ens.Médio");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("EM");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. EM");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. EM");
			}
			
		} else if (escolaridade.equalsIgnoreCase("EF")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Ens.Fundamental");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("EF");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. EF");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. EF");
			}
			
		}else if (escolaridade.equalsIgnoreCase("MS")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Mestre");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("Me");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Me");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Me");
			}
			
		} else if (escolaridade.equalsIgnoreCase("EP")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Especialista");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("Esp");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Esp");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Esp");
			}
			
		} else if (escolaridade.equalsIgnoreCase("TE")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Técnico");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("TE");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. Te");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. Te");
			}
			
		} else if (escolaridade.equalsIgnoreCase("PD")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Pós-Doutorado");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("PD");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Profa. PhD");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaProfessor("Prof. PhD");
			}
		}
		else if (escolaridade.equalsIgnoreCase("EX")) {
			historicoAlunoDisciplinaRelVO.setTitulacaoProfessor("Extensão");
			historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("EX");
			if (sexo.contains("F")) {
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("Profa. Ex");
			}else {
				historicoAlunoDisciplinaRelVO.setSiglaTitulacaoProfessor("Prof. Ex");
			}
			
		}
	}

	private Integer consultarCargaHorariaEstagioRealizadoSomadoDisciplinasEstagioGradeDisciplina(String matricula, List<HistoricoAlunoDisciplinaRelVO> listaAlunoDisciplinaRelVOs, Boolean xmlDiploma) throws Exception {
		if (xmlDiploma) {
			return getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioRealizadoMatriculaVinculadosOutrasDisciplinas(matricula, null);
		}
		List<Integer> disciplinas = listaAlunoDisciplinaRelVOs.stream().filter(h -> validarUsoHistoricoAlunoDisciplinaRelVOParaEstagio(h, true)).map(HistoricoAlunoDisciplinaRelVO::getCodigoDisciplina).collect(Collectors.toList());
		Integer cargaDisciplinasEstagioGradeDisciplina = listaAlunoDisciplinaRelVOs.stream().filter(h -> validarUsoHistoricoAlunoDisciplinaRelVOParaEstagio(h, true)).mapToInt(h -> Integer.valueOf(h.getChDisciplina())).sum();
		return cargaDisciplinasEstagioGradeDisciplina;
	}

	private Boolean validarUsoHistoricoAlunoDisciplinaRelVOParaEstagio(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, boolean estagio) {
		return !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PR") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("PC") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("AC") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TR")
				&& !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("CA") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TS") && !historicoAlunoDisciplinaRelVO.getSituacaoMatriculaPeriodo().equals("TI") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getValor()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().equals(SituacaoHistorico.NAO_CURSADA.getDescricao()) && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado") && !historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim().equalsIgnoreCase("Reprovado Falta") && !historicoAlunoDisciplinaRelVO.getDisciplinaForaGrade() && !historicoAlunoDisciplinaRelVO.getTipoHistoricoComplementacaoCargaHoraria() && !historicoAlunoDisciplinaRelVO.getDisciplinaAtividadeComplementar() && historicoAlunoDisciplinaRelVO.getDisciplinaEstagio().equals(estagio);
	}

	private Integer valorCargaHorariaDisciplinaConformeSituacoes(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, List<SituacaoHistorico> situacaoHistoricos) throws Exception {
		if (validarHistoricoDisciplinaAlunoConformeSituacoes(historicoAlunoDisciplinaRelVO, situacaoHistoricos)) {
			return Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina());
		}
		return 0;
	}

	private Integer valorCreditoDisciplinaConformeSituacoes(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, List<SituacaoHistorico> situacaoHistoricos) throws Exception {
		if (validarHistoricoDisciplinaAlunoConformeSituacoes(historicoAlunoDisciplinaRelVO, situacaoHistoricos)) {
			return Integer.valueOf(historicoAlunoDisciplinaRelVO.getCrDisciplina());
		}
		return 0;
	}

	private boolean validarHistoricoDisciplinaAlunoConformeSituacoes(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, List<SituacaoHistorico> situacaoHistoricos) throws Exception {
		return situacaoHistoricos.stream().anyMatch(sh -> sh.getDescricao().trim().equals(historicoAlunoDisciplinaRelVO.getSituacaoFinal().trim()));
	}

	private Map<String, ConfiguracaoAcademicoVO> realizarGeracaoMapMatriculaConfiguracaoAcademicaFechamentoPeriodo(List<HistoricoAlunoDisciplinaRelVO> objs, String matricula, Boolean desconsiderarSituacaoMatriculaPeriodo) throws Exception {
		Map<String, ConfiguracaoAcademicoVO> mapMatricula = new HashMap<String, ConfiguracaoAcademicoVO>(0);
		for (HistoricoAlunoDisciplinaRelVO obj : objs) {
			if (!mapMatricula.containsKey(obj.getAnoSemstre() + "_" + obj.getNomePeriodo())) {
				mapMatricula.put(obj.getAnoSemstre() + "_" + obj.getNomePeriodo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(matricula, obj.getAno(), obj.getSemestre()));
			}
		}
		return mapMatricula;
	}
			
	/**
	 * Monta o diretorio com o nome do arquivo(relatorio) pelo nome informado Ex:
	 * /urs/nomeRelatorio.jrxml
	 * 
	 * @param nomeRelatorio
	 * @return
	 */
	public static String montarUrlRelatorioJRXMLPeloNome(String nomeRelatorio) {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + nomeRelatorio + ".jrxml";
	}
	
	public Double realizarCalculoMediaGlobal(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<HistoricoVO> listaHistoricos) throws Exception {
		double media = 0.0;
		if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO.getFormulaCoeficienteRendimento())) {
			media = getFacadeFactory().getConfiguracaoAcademicoFacade().realizarCalculoCoeficienteRendimento(configuracaoAcademicoVO.getFormulaCoeficienteRendimento(), listaHistoricos);
		} else {
			double somatorio = 0.0;
			int quantidade = 0;
			for (HistoricoAlunoDisciplinaRelVO historicoALuno :  historicoAlunoDisciplinaRelVOs) {
				if (Uteis.isAtributoPreenchido(historicoALuno.getMediaFinal())
						&& !historicoALuno.getMediaFinal().equals("CC") && !historicoALuno.getUtilizaMediaConceito()) {
					somatorio += historicoALuno.getMediaFinalCalculo();
					quantidade++;
				}
			}
			if (Uteis.isAtributoPreenchido(somatorio)) {
				media = somatorio / quantidade;
			}
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademicoVO.getCasasDecimaisCoeficienteRendimento())) {
			return Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(media, configuracaoAcademicoVO.getCasasDecimaisCoeficienteRendimento());
		} else {
			return Uteis.arrendondarForcando2CadasDecimais(media);
		}
	}
	
	public Double realizarCalculoMediaGlobal(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs) throws Exception {
		double media = 0.0;
		double somatorio = 0.0;
		int quantidade = 0;
		for (HistoricoAlunoDisciplinaRelVO historicoALuno : historicoAlunoDisciplinaRelVOs) {
			if (Uteis.isAtributoPreenchido(historicoALuno.getMediaFinal()) && !historicoALuno.getMediaFinal().equals("CC") && !historicoALuno.getUtilizaMediaConceito()) {
				somatorio += historicoALuno.getMediaFinalCalculo();
				quantidade++;
			}
		}
		if (Uteis.isAtributoPreenchido(somatorio)) {
			media = somatorio / quantidade;
		}
		return Uteis.arrendondarForcando2CadasDecimais(media);
	}
	
	public Double realizarCalculoPercentualGlobalFrequencia(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs) throws Exception {
		Double percentualFrequencia = 0.0;

		Double somatorioFrequencia = 0.0;
			int quantidade = 0;
			for (HistoricoAlunoDisciplinaRelVO historicoALuno :  historicoAlunoDisciplinaRelVOs) {
				if (Uteis.isAtributoPreenchido(historicoALuno.getMediaFinal()) && !historicoALuno.getMediaFinal().equals("CC")) {
					somatorioFrequencia += historicoALuno.getFreqDisciplina();
					quantidade++;
				}
			}
			if (Uteis.isAtributoPreenchido(somatorioFrequencia)) {
				percentualFrequencia = somatorioFrequencia / quantidade;
			}	
		return Uteis.arrendondarForcando2CadasDecimais(percentualFrequencia);		
	}
	
	public void montarProfessoresDisciplina(List<FuncionarioVO> professores, HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO) {
		StringBuilder professoresDisciplina = new StringBuilder();
		
		for (FuncionarioVO professor : professores) {				

			professor.setEscolaridade(professor.getPessoa().getSiglaMaiorTitulacaoNivelEscolaridade());			
	
			if(professor.getPessoa().getSexo().contains("F")) {
				professoresDisciplina.append("Profa. ");		
	
				if(professor.getEscolaridade().equalsIgnoreCase("DR")) {
					professoresDisciplina.append("Dra. ");
				}
			}else {
				professoresDisciplina.append("Prof. ");
				if(professor.getEscolaridade().equalsIgnoreCase("DR")) {
					professoresDisciplina.append("Dr. ");
				}
			}
				
			if(professor.getEscolaridade().equalsIgnoreCase("PO")) {
				professoresDisciplina.append("POS. ");
			}
			else if(professor.getEscolaridade().equalsIgnoreCase("ME")) {
				professoresDisciplina.append("Ms. ");
			}
			else if(professor.getEscolaridade().equalsIgnoreCase("EP")) {
				professoresDisciplina.append("Esp. ");
			}
			else if(professor.getEscolaridade().equalsIgnoreCase("PD")) {
				professoresDisciplina.append("PhD. ");
			}
			
			professoresDisciplina.append(professor.getPessoa().getNome()).append(" / ");																
	
		}		
		historicoAlunoDisciplinaRelVO.setProfessoresDisciplinas(professoresDisciplina.substring(0, professoresDisciplina.length()-2));
	}

	public void validarCargaHorariaEstagioUtilizarXML(HistoricoAlunoRelVO historicoAlunoRelVO, String tipoLayout) throws Exception {
		if (getFacadeFactory().getEstagioFacade().validarAlunoUtilizaComponenteEstagio(historicoAlunoRelVO.getMatriculaVO().getMatricula())) {
			if (!tipoLayout.equals(HISTORICO_ALUNO_LAYOUT_24_PORTARIA_MEC)) {
				if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getCargaHorariaDisciplinaEstagioRealizada())) {
					historicoAlunoRelVO.setCargaHorariaAprovadaEstagio(historicoAlunoRelVO.getCargaHorariaDisciplinaEstagioRealizada());
				} else {
					historicoAlunoRelVO.setCargaHorariaAprovadaEstagio(getFacadeFactory().getEstagioFacade().consultarCargaHorariaEstagioRealizadoMatriculaVinculadosOutrasDisciplinas(historicoAlunoRelVO.getMatriculaVO().getMatricula(), null));
				}
			}
			historicoAlunoRelVO.setCargaHorariaUtilizar("COMPONENTE_ESTAGIO");
		} else {
			historicoAlunoRelVO.setCargaHorariaUtilizar("DISCIPLINA_ESTAGIO");
		}
	}
}