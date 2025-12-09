package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.EstatisticaAcademicaPorConvenioRelVO;
import relatorio.negocio.interfaces.academico.EstatisticaAcademicaPorConvenioRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EstatisticaAcademicaPorConvenioRel extends SuperRelatorio implements EstatisticaAcademicaPorConvenioRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public static void validarDados(Integer unidadeEnsino, Integer parceiro, Integer convenio, String ano, String semestre, String tipoLayout) throws Exception {
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado para a geração do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException("O campo ANO deve ser informado para a geração do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado para a geração do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(parceiro)) {
			throw new ConsistirException("O campo PARCEIRO deve ser informado para a geração do relatório.");
		}
	}

	public List<EstatisticaAcademicaPorConvenioRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer parceiro, Integer convenio, String ano, String semestre, Double mediaAproveitamentoIni, Double mediaAproveitamentoFim, Double mediaFrequenciaIni, Double mediaFrequenciaFim, Double mediaNotaIni, Double mediaNotaFim, String tipoLayout) throws Exception {
		List<EstatisticaAcademicaPorConvenioRelVO> objs = new ArrayList<EstatisticaAcademicaPorConvenioRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(unidadeEnsino, curso, turma, parceiro, convenio, ano, semestre, mediaAproveitamentoIni, mediaAproveitamentoFim, mediaFrequenciaIni, mediaFrequenciaFim, mediaNotaIni, mediaNotaFim, tipoLayout);
		while (dadosSQL.next()) {
			objs.add(montarDados(dadosSQL));
		}
		return objs;
	}

	private SqlRowSet executarConsultaParametrizada(Integer unidadeEnsino, Integer curso, Integer turma, Integer parceiro, Integer convenio, String ano, String semestre, Double mediaAproveitamentoIni, Double mediaAproveitamentoFim, Double mediaFrequenciaIni, Double mediaFrequenciaFim, Double mediaNotaIni, Double mediaNotaFim, String tipoLayout) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM ( ");
		sqlStr.append(" SELECT matricula.matricula, matricula.gradecurricularatual, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.email AS \"pessoa.email\", ");
		sqlStr.append(" curso.nome as \"curso.nome\", turma.identificadorturma, turno.nome as \"turno.nome\", convenio.descricao as \"convenio.descricao\", anohistorico, semestrehistorico, matricula.curso as curso, ");
		sqlStr.append(" trunc(avg(case when mediafinal is null then 0 else mediafinal end)::NUMERIC, 2) AS mediaNotas, trunc(avg(freguencia)::NUMERIC, 2) AS mediaFrequencia, ");
		sqlStr.append(" trunc(sum(case when mediafinal is null then 0 else mediafinal end)::NUMERIC, 2)   AS totalNotas, ");
		sqlStr.append(" sum(CASE WHEN historico.situacao IN ('AA', 'CC', 'CH', 'IS', 'AP', 'AE', 'AB') THEN 1 ELSE 0 END) as \"qtdeDisciplinasAprovadas\", ");
		sqlStr.append(" sum(CASE WHEN historico.situacao in ('RE', 'RF')  THEN 1 ELSE 0 END) as \"qtdeDisciplinasReprovadas\", ");
		sqlStr.append(" sum(CASE WHEN historico.situacao = 'CS' THEN 1 ELSE 0 END) as \"qtdeDisciplinasCursando\", ");
		sqlStr.append(" ((trunc(sum(CASE WHEN historico.situacao IN ('AA', 'CC', 'CH', 'IS', 'AP', 'AE') THEN 1 ELSE 0 END) / ");
		sqlStr.append(" (count(historico.codigo))::NUMERIC, 2)) * 100) AS mediaAproveitamento ");
		// sqlStr.append(" (sum(CASE WHEN historico.situacao IN ('AA', 'CC', 'CH', 'IS', 'AP', 'AE') THEN 1 ELSE 0 END) + sum(CASE WHEN historico.situacao = 'RE'  THEN 1 ELSE 0 END) + sum(CASE WHEN historico.situacao = 'CS' THEN 1 ELSE 0 END) ");
		// sqlStr.append(" )::NUMERIC, 2)) * 100) AS mediaAproveitamento ");
		sqlStr.append(" FROM historico ");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = historico.matricula ");
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula AND matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append(" INNER JOIN planofinanceiroaluno ON planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo AND planofinanceiroaluno.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN itemplanofinanceiroaluno ON itemplanofinanceiroaluno.planofinanceiroaluno = planofinanceiroaluno.codigo ");
		sqlStr.append(" INNER JOIN convenio ON convenio.codigo = itemplanofinanceiroaluno.convenio ");
		sqlStr.append(" INNER JOIN parceiro ON parceiro.codigo = convenio.parceiro ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = turma.turno ");
		sqlStr.append(" LEFT JOIN curso ON turma.curso = curso.codigo ");
		sqlStr.append(" WHERE historico.anohistorico = '").append(ano).append("' AND historico.semestrehistorico = '").append(semestre).append("' ");
		sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" AND parceiro.codigo = ").append(parceiro);
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND turma.codigo = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(convenio)) {
			sqlStr.append(" AND convenio.codigo = ").append(convenio);
		}
		sqlStr.append(" AND (historico.gradecurriculargrupooptativadisciplina IS NOT NULL or historico.gradedisciplina IS NOT NULL) ");
		sqlStr.append(" AND (historicodisciplinaforagrade = false OR historicodisciplinaforagrade IS NULL) ");
		sqlStr.append(" AND (historicoequivalente = false OR historicoequivalente IS NULL) ");
		sqlStr.append(" AND (historicodisciplinafazpartecomposicao = false OR historicodisciplinafazpartecomposicao IS NULL) ");
		sqlStr.append(" GROUP BY historico.anohistorico, historico.semestrehistorico, matricula.matricula, pessoa.codigo, pessoa.nome, pessoa.email, curso.nome, turma.identificadorturma, turno.nome, convenio.descricao ");
		sqlStr.append(" ) AS t WHERE 1=1 ");
		if (mediaNotaIni != null && mediaNotaFim != null) {
			sqlStr.append(" AND t.mediaNotas >= ").append(mediaNotaIni);
			sqlStr.append(" AND t.mediaNotas <= ").append(mediaNotaFim);
		}
		if (mediaFrequenciaIni != null && mediaFrequenciaFim != null) {
			sqlStr.append(" AND t.mediaFrequencia >= ").append(mediaFrequenciaIni);
			sqlStr.append(" AND t.mediaFrequencia <= ").append(mediaFrequenciaFim);
		}
		if (mediaAproveitamentoIni != null && mediaAproveitamentoFim != null) {
			sqlStr.append(" AND t.mediaAproveitamento >= ").append(mediaAproveitamentoIni);
			sqlStr.append(" AND t.mediaAproveitamento <= ").append(mediaAproveitamentoFim);
		}
		sqlStr.append(" ORDER BY t.curso, t.anohistorico, t.semestrehistorico ");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	private EstatisticaAcademicaPorConvenioRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		EstatisticaAcademicaPorConvenioRelVO obj = new EstatisticaAcademicaPorConvenioRelVO();
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularatual"));
		obj.getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getAluno().setEmail(dadosSQL.getString("pessoa.email"));
		obj.setCurso(dadosSQL.getString("curso.nome"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		obj.setTurno(dadosSQL.getString("turno.nome"));
		obj.setQtdeDisciplinasAprovadas(dadosSQL.getInt("qtdeDisciplinasAprovadas"));
		obj.setQtdeDisciplinasCursando(dadosSQL.getInt("qtdeDisciplinasCursando"));
		obj.setQtdeDisciplinasReprovadas(dadosSQL.getInt("qtdeDisciplinasReprovadas"));
		obj.setMediaNotas(dadosSQL.getDouble("mediaNotas"));
		obj.setTotalNotas(dadosSQL.getDouble("totalNotas"));
		obj.setMediaFrequencia(dadosSQL.getDouble("mediaFrequencia"));
		obj.setMediaAproveitamento(dadosSQL.getDouble("mediaAproveitamento"));
		obj.setAno(dadosSQL.getString("anohistorico"));
		obj.setSemestre(dadosSQL.getString("semestrehistorico"));
		obj.setConvenio(dadosSQL.getString("convenio.descricao"));
		obj.setConclusaoCurso(getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(obj.getMatricula(), obj.getMatricula().getGradeCurricularAtual().getCodigo(), null, null));
		return obj;
	}

	public void executarEnvioComunicadoInternoAluno(List<EstatisticaAcademicaPorConvenioRelVO> estatisticaAcademicaPorConvenioRelVOs, ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if (!Uteis.isAtributoPreenchido(comunicacaoInternaVO.getAssunto())) {
				throw new Exception("O campo ASSUNTO deve ser informado.");
			}
			ComunicadoInternoDestinatarioVO destinatario = null;
			comunicacaoInternaVO.setTipoDestinatario("AL");
			comunicacaoInternaVO.setTipoComunicadoInterno("LE");
			comunicacaoInternaVO.setPrioridade("NO");
			comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
			comunicacaoInternaVO.setData(new Date());
			comunicacaoInternaVO.setEnviarEmail(true);
			for (EstatisticaAcademicaPorConvenioRelVO estatisticaAcademica : estatisticaAcademicaPorConvenioRelVOs) {
				if (estatisticaAcademica.getSelecionado()) {
					destinatario = new ComunicadoInternoDestinatarioVO();
					destinatario.setTipoComunicadoInterno("LE");
					destinatario.setDestinatario(estatisticaAcademica.getAluno());
					destinatario.setEmail(estatisticaAcademica.getAluno().getEmail());
					destinatario.setNome(estatisticaAcademica.getAluno().getNome());
					comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(destinatario);
					destinatario = null;
				}
			}
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
		} catch (Exception e) {
			throw e;
		}
	}

	public static String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String designIReportRelatorioExcell() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return "EstatisticaAcademicaPorConvenioRel";
	}
	
	public static String getIdEntidadeExcel() {
		return ("EstatisticaAcademicaPorConvenioExcellRel");
	}

}
