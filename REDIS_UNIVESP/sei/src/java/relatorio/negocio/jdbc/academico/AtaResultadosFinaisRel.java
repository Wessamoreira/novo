package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.AtaResultadosFinaisDisciplinasRelVO;
import relatorio.negocio.comuns.academico.AtaResultadosFinaisRelVO;
import relatorio.negocio.interfaces.academico.AtaResultadosFinaisRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AtaResultadosFinaisRel extends SuperRelatorio implements AtaResultadosFinaisRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public void validarDados(Integer turma, boolean apresentarCampoAno, String ano, boolean apresentarCampoSemestre, String semestre) throws Exception {
		if (!Uteis.isAtributoPreenchido(turma)) {
			throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_turma"));
		}
		if (!Uteis.isAtributoPreenchido(ano) && apresentarCampoAno) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		}
		if (!Uteis.isAtributoPreenchido(semestre) && apresentarCampoSemestre) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		}
	}
	
	@Override
	public void validarFuncionarios(String layout, FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO) throws Exception {
		if (layout.equals("Layout3")) {
			if (!Uteis.isAtributoPreenchido(funcionarioPrincipalVO)) {
				throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_responsavelPrimario"));
			}
			
			if (!Uteis.isAtributoPreenchido(funcionarioSecundarioVO)) {
				throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_responsavelSecundadrio"));
			}
		}
		
	}

	public AtaResultadosFinaisRelVO criarObjeto(TurmaVO turmaVO, String ano, String semestre, String layout, Boolean apresentarDisciplinaComposta, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno, Boolean apresentarDataTransferencia, FuncionarioVO funcionarioPrincipal, FuncionarioVO funcionarioSecundario, Date dataApuracao) throws Exception {
		AtaResultadosFinaisRelVO ataResultadosFinaisRelVO = new AtaResultadosFinaisRelVO();
		montarDadosAtaResultadoFinal(turmaVO, ano, semestre, funcionarioPrincipal, funcionarioSecundario, ataResultadosFinaisRelVO, dataApuracao);
		SqlRowSet dadosSQL = executarConsultaParametrizada(turmaVO, ano, semestre, apresentarDisciplinaComposta, filtroRelatorioAcademicoVO, tipoAluno);
		ataResultadosFinaisRelVO.setAtaResultadosFinaisDisciplinasRelVOs(montarDados(dadosSQL));
		Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplina(ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs());		
		Map<String, ConfiguracaoAcademicoVO> mapConf = realizarGeracaoMapMatriculaConfiguracaoAcademicaFechamentoPeriodoLetivo(ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs(), ano, semestre);		
		Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatricula, mapConf);
		for (AtaResultadosFinaisDisciplinasRelVO ataResultadosFinaisDisciplinasRelVO : ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs()) {
			if (layout.equals("Layout1")) {
				if (mapMatriculaSituacaoFinal.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).getValor().equals(SituacaoHistorico.TRANSFERIDO.getValor()) && apresentarDataTransferencia) {
					ataResultadosFinaisDisciplinasRelVO.setSituacaoFinal("Transferido ("+Uteis.getData(ataResultadosFinaisDisciplinasRelVO.getDataTransferencia(), "dd/MM/yyyy")+")");
				} else {
					ataResultadosFinaisDisciplinasRelVO.setSituacaoFinal(mapMatriculaSituacaoFinal.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).getDescricao());
				}
			} else if (layout.equals("Layout3")) {
				ataResultadosFinaisDisciplinasRelVO.setSituacaoFinal(SituacaoHistorico.getEnum(mapMatriculaSituacaoFinal.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).getValor()).getDescricao());
			} else {
				ataResultadosFinaisDisciplinasRelVO.setSituacaoFinal(mapMatriculaSituacaoFinal.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).getValor());
			}
		}
		return ataResultadosFinaisRelVO;
	}


	private void montarDadosAtaResultadoFinal(TurmaVO turmaVO, String ano, String semestre,
			FuncionarioVO funcionarioPrincipal, FuncionarioVO funcionarioSecundario,
			AtaResultadosFinaisRelVO ataResultadosFinaisRelVO, Date dataApuracao) {
		
		String nomePeriodoLetivo = !Uteis.isAtributoPreenchido(turmaVO.getPeridoLetivo().getNomeCertificacao()) ? turmaVO.getPeridoLetivo().getDescricao() : turmaVO.getPeridoLetivo().getNomeCertificacao();
		
		ataResultadosFinaisRelVO.setTurma(turmaVO.getIdentificadorTurma());
		ataResultadosFinaisRelVO.setAno(ano);
		ataResultadosFinaisRelVO.setSemestre(semestre);
		ataResultadosFinaisRelVO.setTurno(turmaVO.getTurno().getNome());
		ataResultadosFinaisRelVO.setFuncionarioPrincipal(funcionarioPrincipal.getPessoa().getNome());
		ataResultadosFinaisRelVO.setFuncionarioSecundario(funcionarioSecundario.getPessoa().getNome());
		ataResultadosFinaisRelVO.setPeriodoLetivo(Uteis.isAtributoPreenchido(turmaVO.getPeridoLetivo().getNomeCertificacao()) ?   turmaVO.getPeridoLetivo().getNomeCertificacao() : turmaVO.getPeridoLetivo().getDescricao());
		ataResultadosFinaisRelVO.setDescricaoAta("Aos " + UteisData.getDataPorExtenso(dataApuracao).toLowerCase() +", terminou-se o processo de apuração das notas "
				+ "finais e nota global dos alunos do(a) " + nomePeriodoLetivo +" "+ turmaVO.getDigitoTurma() +" turno "+ turmaVO.getTurno().getNome() + " do "+
				turmaVO.getCurso().getNome() +" deste Estabelecimento de Ensino com os seguintes resultados:");
		ataResultadosFinaisRelVO.setAnual(turmaVO.getAnual());
		ataResultadosFinaisRelVO.setSemestral(turmaVO.getSemestral());
		ataResultadosFinaisRelVO.setNivelEducacional(TipoNivelEducacional.getDescricao(turmaVO.getCurso().getNivelEducacional()));
		ataResultadosFinaisRelVO.setNomeCertificadoPeriodoLetivo(turmaVO.getPeridoLetivo().getNomeCertificacao());
		ataResultadosFinaisRelVO.setNomeDocumentacaoCurso(turmaVO.getCurso().getNomeDocumentacao());

	}

	private List<AtaResultadosFinaisDisciplinasRelVO> montarDados(SqlRowSet dadosSQL) {
		List<AtaResultadosFinaisDisciplinasRelVO> ataResultadosFinaisDisciplinasRelVOs = new ArrayList<AtaResultadosFinaisDisciplinasRelVO>(0);
		AtaResultadosFinaisDisciplinasRelVO ataResultadosFinaisDisciplinasRelVO = null;
		while (dadosSQL.next()) {
			ataResultadosFinaisDisciplinasRelVO = new AtaResultadosFinaisDisciplinasRelVO();
			ataResultadosFinaisDisciplinasRelVO.setOrdem(dadosSQL.getInt("ordem"));
			ataResultadosFinaisDisciplinasRelVO.setMatriculaAluno(dadosSQL.getString("matricula"));
			ataResultadosFinaisDisciplinasRelVO.setNomeAluno(dadosSQL.getString("pessoa.nome"));
			ataResultadosFinaisDisciplinasRelVO.setCodigoDisciplina(dadosSQL.getInt("disciplina.codigo"));
			ataResultadosFinaisDisciplinasRelVO.setNomeDisciplina(dadosSQL.getString("disciplina.nome").toUpperCase());
			if (dadosSQL.getObject("historico.mediafinal") != null) {
				ataResultadosFinaisDisciplinasRelVO.setMediaFinal(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("historico.mediafinal"), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
			}
			ataResultadosFinaisDisciplinasRelVO.setSituacao(dadosSQL.getString("historico.situacao"));
			ataResultadosFinaisDisciplinasRelVO.setSituacaoUltimaMatriculaPeriodo(dadosSQL.getString("situacaoultimamatriculaperiodo"));
			ataResultadosFinaisDisciplinasRelVO.setFreguencia(dadosSQL.getString("historico.freguencia"));
			ataResultadosFinaisDisciplinasRelVO.setCargaHorariaDisciplina(dadosSQL.getString("cargahorariadisciplina"));			
			ataResultadosFinaisDisciplinasRelVOs.add(ataResultadosFinaisDisciplinasRelVO);
		}
		return ataResultadosFinaisDisciplinasRelVOs;
	}

	private Map<String, Map<String, String>> realizarGeracaoMapMatriculaDisciplina(List<AtaResultadosFinaisDisciplinasRelVO> ataResultadosFinaisDisciplinasRelVOs) {
		Map<String, Map<String, String>> mapMatricula = new HashMap<String, Map<String, String>>(0);
		for (AtaResultadosFinaisDisciplinasRelVO ataResultadosFinaisDisciplinasRelVO : ataResultadosFinaisDisciplinasRelVOs) {
			if (!mapMatricula.containsKey(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno())) {
				mapMatricula.put(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno(), new HashMap<String, String>(0));
			}
			if (!mapMatricula.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).containsKey(ataResultadosFinaisDisciplinasRelVO.getCodigoDisciplina().toString())) {
				SituacaoMatriculaPeriodoEnum sit = SituacaoMatriculaPeriodoEnum.getEnumPorValor(ataResultadosFinaisDisciplinasRelVO.getSituacaoUltimaMatriculaPeriodo());
				if (sit.equals(SituacaoMatriculaPeriodoEnum.TRANCADA) || sit.equals(SituacaoMatriculaPeriodoEnum.CANCELADA) || sit.equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO) || sit.equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA)) {
					mapMatricula.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).put(ataResultadosFinaisDisciplinasRelVO.getCodigoDisciplina().toString(), ataResultadosFinaisDisciplinasRelVO.getSituacaoUltimaMatriculaPeriodo());
				} else {
					mapMatricula.get(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno()).put(ataResultadosFinaisDisciplinasRelVO.getCodigoDisciplina().toString(), ataResultadosFinaisDisciplinasRelVO.getSituacao());
				}
			}

		}
		return mapMatricula;
	}	

	public SqlRowSet executarConsultaParametrizada(TurmaVO turmaVO, String ano, String semestre, Boolean apresentarDisciplinaComposta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String tipoAluno) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select gradedisciplina.ordem, matricula.matricula, pessoa.nome as \"pessoa.nome\", sem_acentos(pessoa.nome) as nomeSemAcento, disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append("matriculaperiodo.situacaomatriculaperiodo as \"situacaoultimamatriculaperiodo\", historico.situacao as \"historico.situacao\", historico.mediafinal as \"historico.mediafinal\", ");
		sqlStr.append("historico.freguencia as \"historico.freguencia\", case when gradedisciplina.cargahoraria > 0 then gradedisciplina.cargahoraria else case when gradecurriculargrupooptativadisciplina.cargahoraria > 0 then gradecurriculargrupooptativadisciplina.cargahoraria else historico.cargahorariadisciplina end end as cargahorariadisciplina, ");
		sqlStr.append("quantidadecasasdecimaispermitiraposvirgula, case when periodoletivo.configuracaoacademico is null then curso.configuracaoacademico else periodoletivo.configuracaoacademico end as configuracaoAcademicoFechamentoPeriodo  ");
		sqlStr.append("from historico ");
		sqlStr.append("inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append("inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append("and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		if (Uteis.isAtributoPreenchido(ano) && !turmaVO.getIntegral()) {
			sqlStr.append("and mp.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre) && turmaVO.getSemestral()) {
			sqlStr.append("and mp.semestre = '").append(semestre).append("' ");
		}
		sqlStr.append("and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		sqlStr.append("and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		sqlStr.append("order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		sqlStr.append("inner join periodoletivo on matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append("inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {		
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");			
		} else if (turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turmapratica ");
		} else if (turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turmateorica ");
		} else {
			sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		}
		sqlStr.append("left join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sqlStr.append("left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		if (tipoAluno.equals("reposicao")) {
			sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
		}else if (tipoAluno.equals("normal")) {
				sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma = matriculaperiodo.turma ");
		} else {
			sqlStr.append("where 1 = 1 ");
		}
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" and ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(")))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
		} else if (turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turmapratica = ").append(turmaVO.getCodigo());
		} else if (turmaVO.getSubturma() && turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turmateorica = ").append(turmaVO.getCodigo());
		} else {						
			sqlStr.append(" and Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
		if (Uteis.isAtributoPreenchido(ano) && !turmaVO.getIntegral()) {
			sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre) && turmaVO.getSemestral()) {
			sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		if(!filtroRelatorioAcademicoVO.getTrazerAlunosComTransferenciaMatriz()){
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));	
		}		
		if (apresentarDisciplinaComposta) {
			sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
		} else {
			sqlStr.append(" and (historico.historicodisciplinafazpartecomposicao is null or historico.historicodisciplinafazpartecomposicao = false) ");
		}
		sqlStr.append("and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null  or historico.gradedisciplinacomposta is not null) ");
		sqlStr.append("and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		sqlStr.append("and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append("and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" order by nomeSemAcento, gradedisciplina.ordem ");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	/**
	 * Recupera o Desgner do Relatorio pelo tipo Layout inforamdo.
	 * 
	 * @param layout
	 * @return
	 */
	public static String getDesignIReportRelatorio(String layout) {
		switch (layout) {
		case "Layout1":
			return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml";
		case "Layout2":
			return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayout2() + ".jrxml";
		case "Layout3":
			return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayout3() + ".jrxml";
		default:
			return "";
		}
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public static String getIdEntidade() {
		return "AtaResultadosFinaisRel";
	}

	public static String getIdEntidadeLayout2() {
		return "AtaResultadosFinaisRelLayout2";
	}

	public static String getIdEntidadeLayout3() {
		return "AtaResultadosFinaisRelLayout3";
	}
	
	private Map<String, ConfiguracaoAcademicoVO> realizarGeracaoMapMatriculaConfiguracaoAcademicaFechamentoPeriodoLetivo(List<AtaResultadosFinaisDisciplinasRelVO> ataResultadosFinaisDisciplinasRelVOs, String ano, String semestre) throws Exception {
		Map<String, ConfiguracaoAcademicoVO> mapMatricula = new HashMap<String, ConfiguracaoAcademicoVO>(0);
		for (AtaResultadosFinaisDisciplinasRelVO ataResultadosFinaisDisciplinasRelVO : ataResultadosFinaisDisciplinasRelVOs) {
			if (!mapMatricula.containsKey(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno())) {
				mapMatricula.put(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(ataResultadosFinaisDisciplinasRelVO.getMatriculaAluno(), ano, semestre));
			}			
		}
		return mapMatricula;
	}
}
