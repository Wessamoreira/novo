package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AtividadeComplementarRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EventoAtividadeComplementarVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaPeriodoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.AcompanhamentoAtividadeComplementarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AcompanhamentoAtividadeComplementar extends ControleAcesso implements AcompanhamentoAtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AcompanhamentoAtividadeComplementar() {
		super();
		this.setIdEntidade("AcompanhamentoAtividadeComplementar");
	}

	public static String getIdEntidade() {
		return AcompanhamentoAtividadeComplementar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		AcompanhamentoAtividadeComplementar.idEntidade = idEntidade;
	}

	@Override
	public String getSqlTotalHorasConsideradas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ( ");
		sql.append(" select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
		sql.append(" registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sql.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
		sql.append(" from registroatividadecomplementarmatricula ");
		sql.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
		sql.append(" where matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");		
		sql.append(" group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sql.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
		sql.append(" ) as t)	");
		return sql.toString();
	}

	@Override
	public String getSqlTotalHorasRealizadas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");		
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}
	
	@Override
	public String getSqlTotalHorasIndeferido() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula ");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}
	
	@Override
	public String getSqlTotalHorasAguardandoDeferimento() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula ");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultar(Integer curso, Integer codigoTurma, String ano, String semestre, String situacao, String matricula, Integer codigoTipoAtividadeComplementar, SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatriculaEnum, boolean controlarAcesso, Integer codigoGradeCurricular, DataModelo dataModelo, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH cte_filtro AS ( SELECT DISTINCT COUNT(*) OVER() AS totalRegistroConsulta, matricula.matricula, dados_dataRegistro.data, pessoa.nome, gradeCurricular.totalCargaHorariaAtividadeComplementar AS cargaHorariaExigida, matricula.gradeCurricularAtual FROM matricula ");
		sql.append(" INNER JOIN LATERAL ( SELECT matriculaPeriodo.codigo, matriculaPeriodo.matricula, matriculaPeriodo.ano, matriculaPeriodo.semestre, matriculaPeriodo.situacaoMatriculaPeriodo, matriculaPeriodo.turma, matriculaPeriodo.periodoLetivoMatricula ");
		sql.append(" FROM matriculaPeriodo WHERE matriculaPeriodo.matricula = matricula.matricula AND matriculaPeriodo.situacaoMatriculaPeriodo NOT IN ('PC') ORDER BY matriculaPeriodo.ano || '/' || matriculaPeriodo.semestre DESC LIMIT 1) AS matriculaPeriodo ON matriculaPeriodo.codigo IS NOT NULL ");
		sql.append(" INNER JOIN gradeCurricular ON gradeCurricular.codigo = matricula.gradeCurricularAtual ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sql.append(" LEFT JOIN LATERAL (SELECT r2.DATA FROM registroAtividadeComplementarMatricula r INNER JOIN registroAtividadeComplementar r2 ON r2.codigo = r.registroAtividadeComplementar WHERE r.matricula = matricula.matricula ORDER BY r2.DATA DESC LIMIT 1) AS dados_dataRegistro ON 1= 1 ");
		sql.append(" WHERE (gradeCurricular.totalCargaHorariaAtividadeComplementar > 0 OR dados_dataRegistro.DATA IS NOT NULL) ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" AND matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(codigoTurma)) {
			sql.append(" AND matriculaPeriodo.turma = ").append(codigoTurma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" AND matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(codigoTipoAtividadeComplementar)) {
			sql.append(" AND EXISTS (SELECT matricula FROM registroAtividadeComplementarMatricula WHERE registroAtividadeComplementarMatricula.matricula = matricula.matricula AND registroAtividadeComplementarMatricula.tipoAtividadeComplementar = ").append(codigoTipoAtividadeComplementar).append(" LIMIT 1) ");
		}
		if (situacao != null && situacao.equals("AT")) {
			sql.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo IN ('AT', 'FI', 'CO')");
		}
		if (Uteis.isAtributoPreenchido(situacaoAtividadeComplementarMatriculaEnum)) {
			sql.append(" AND EXISTS (SELECT matricula FROM registroAtividadeComplementarMatricula WHERE registroAtividadeComplementarMatricula.matricula = matricula.matricula AND registroAtividadeComplementarMatricula.situacaoAtividadeComplementarMatricula = '").append(situacaoAtividadeComplementarMatriculaEnum.name()).append("' LIMIT 1) ");
		}
		if (dataInicio != null || dataFim != null) {
			if (dataInicio != null && dataFim != null) {
				sql.append(" AND EXISTS (SELECT FROM registroAtividadeComplementarMatricula r INNER JOIN registroAtividadeComplementar r2 ON r2.codigo = r.registroAtividadeComplementar WHERE r.matricula = matricula.matricula AND r2.data >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)) + "' AND r2.data <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)) + "') ");
			} else if (dataInicio != null && dataFim == null) {
				sql.append(" AND EXISTS (SELECT FROM registroAtividadeComplementarMatricula r INNER JOIN registroAtividadeComplementar r2 ON r2.codigo = r.registroAtividadeComplementar WHERE r.matricula = matricula.matricula AND r2.data >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)) + "') ");
			} else if (dataInicio == null && dataFim != null) {
				sql.append(" AND EXISTS (SELECT FROM registroAtividadeComplementarMatricula r INNER JOIN registroAtividadeComplementar r2 ON r2.codigo = r.registroAtividadeComplementar WHERE r.matricula = matricula.matricula AND r2.data <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFim)) + "') ");
			}
		}
		if (Uteis.isAtributoPreenchido(situacao) && (situacao.equals("CO") || situacao.equals("PF"))) {
			if (situacao.equals("CO")) {
				sql.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo = 'FI' ");
			} else {
				sql.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
			}
			sql.append(" AND 0 = (SELECT COUNT(codigo) FROM matriculaPeriodo mp WHERE mp.matricula = matriculaPeriodo.matricula ");
			sql.append(" AND mp.situacaoMatriculaPeriodo NOT IN ('PC') AND mp.codigo != matriculaPeriodo.codigo ");
			sql.append(" AND (mp.ano || '/' || mp.semestre) > (matriculaPeriodo.ano || '/' || matriculaPeriodo.semestre)) ");
			sql.append(" AND (SELECT COUNT(DISTINCT disciplina.codigo) FROM historico ");
			sql.append(" INNER JOIN disciplina on disciplina.codigo = historico.disciplina ");
			sql.append(" WHERE disciplina.tipoDisciplina IN ('OB', 'LB') ");
			sql.append(" AND historico.matricula = matricula.matricula ");
			sql.append(" AND historico.situacao IN ('AA', 'AP', 'CS', 'CC')) ");
			sql.append(" >= (SELECT COUNT(DISTINCT disciplina.codigo) FROM gradeCurricular ");
			sql.append(" INNER JOIN periodoLetivo AS pr ON gradeCurricular.codigo = pr.gradeCurricular ");
			sql.append(" INNER JOIN gradeDisciplina AS grDisc ON grDisc.periodoLetivo = pr.codigo ");
			sql.append(" INNER JOIN disciplina ON grDisc.disciplina = disciplina.codigo ");
			sql.append(" WHERE gradeCurricular.codigo = matricula.gradeCurricularAtual ");
			sql.append(" AND disciplina.tipoDisciplina IN ('OB', 'LG')) ");
		}
		sql.append(" GROUP BY matricula.matricula, pessoa.nome, gradeCurricular.totalCargaHorariaAtividadeComplementar, matricula.gradeCurricularAtual, dados_dataRegistro.data ");
		sql.append(" ORDER BY pessoa.nome, matricula.matricula ");
		if (Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
			sql.append(" LIMIT ").append(dataModelo.getLimitePorPagina()).append(" OFFSET ").append(dataModelo.getOffset());
		}
		sql.append(" ) ");
		sql.append(" SELECT cte_filtro.*, resultado.cargaHorariaConsiderada AS cargaHorariaConsiderada, resultado.cargaHorariaDeferida AS cargaHorariaRealizada, ");
		sql.append(" resultado.cargaHorariaIndeferido, resultado.cargaHorariaAguardandoDeferimento, (cte_filtro.cargaHorariaExigida - resultado.cargaHorariaConsiderada) AS cargaHorariaPendente ");
		sql.append(" FROM cte_filtro ");
		sql.append(" LEFT JOIN LATERAL ( SELECT cargaHorariaConsiderada, cargaHorariaIndeferido, cargaHorariaDeferida, cargaHorariaAguardandoDeferimento ");
		sql.append(" FROM ( SELECT ");
		sql.append(" SUM(cargaHorariaConsiderada) AS cargaHorariaConsiderada, COALESCE(SUM(cargaHorariaIndeferida), 0.0) AS cargaHorariaIndeferido, ");
		sql.append(" COALESCE(SUM(cargaHorariaDeferida), 0.0) AS cargaHorariaDeferida, COALESCE(SUM(cargaHorariaAguardandoDeferimento), 0.0) AS cargaHorariaAguardandoDeferimento ");
		sql.append(" FROM ( SELECT 'QUALQUER' AS tipo, cargaHorariaExigida, SUM(cargaHorariaRealizadaAtividadeComplementar) AS cargaHorariaRealizadaAtividadeComplementar, ");
		sql.append(" CASE WHEN SUM(CASE WHEN cargaHorariaRealizadaAtividadeComplementar > cargaHoraria THEN cargaHoraria ELSE cargaHorariaRealizadaAtividadeComplementar END) > cargaHorariaExigida THEN cargaHorariaExigida ");
		sql.append(" ELSE SUM(CASE WHEN cargaHorariaRealizadaAtividadeComplementar > cargaHoraria THEN cargaHoraria ELSE cargaHorariaRealizadaAtividadeComplementar END) END AS cargaHorariaConsiderada, ");
		sql.append(" CASE WHEN SUM(CASE WHEN cargaHorariaRealizadaAtividadeComplementar > cargaHoraria THEN cargaHoraria ELSE cargaHorariaRealizadaAtividadeComplementar END) < cargaHorariaExigida THEN cargaHorariaExigida - SUM(CASE WHEN cargaHorariaRealizadaAtividadeComplementar > cargaHoraria THEN cargaHoraria else cargaHorariaRealizadaAtividadeComplementar END) ");
		sql.append(" ELSE 0.0 END AS cargaHorariaPendente, 0 AS cargaHorariaExcedida, ");
		sql.append(" COALESCE(SUM(cargaHorariaIndeferida), 0.0) AS cargaHorariaIndeferida, ");
		sql.append(" COALESCE(SUM(cargaHorariaDeferida), 0.0) AS cargaHorariaDeferida, ");
		sql.append(" COALESCE(SUM(cargaHorariaAguardandoDeferimento), 0.0) AS cargaHorariaAguardandoDeferimento ");
		sql.append(" FROM (SELECT COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) AS cargaHorariaRealizadaAtividadeComplementar, ");
		sql.append(" gctac.tipoAtividadeComplementar AS tipoAtividadeComplementar, gctac.cargaHoraria, ");
		sql.append(" gradeCurricular.totalCargaHorariaAtividadeComplementar - COALESCE (( ");
		sql.append(" SELECT SUM(horasMinimasExigida) FROM gradeCurricularTipoAtividadeComplementar WHERE gradeCurricularTipoAtividadeComplementar.gradeCurricular = gradeCurricular.codigo ");
		sql.append(" AND gradeCurricularTipoAtividadeComplementar.horasMinimasExigida > 0.0), 0) AS cargaHorariaExigida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'INDEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaIndeferida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaDeferida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'AGUARDANDO_DEFERIMENTO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaAguardandoDeferimento ");
		sql.append(" FROM gradeCurricularTipoAtividadeComplementar gctac ");
		sql.append(" INNER JOIN gradeCurricular ON gradeCurricular.codigo = gctac.gradeCurricular ");
		sql.append(" LEFT JOIN registroAtividadeComplementarMatricula racm ON gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar AND racm.matricula = cte_filtro.matricula ");
		sql.append(" WHERE gctac.gradeCurricular = cte_filtro.gradeCurricularAtual AND gctac.horasMinimasExigida = 0.0 AND gctac.horasMinimasExigida = 0.0 ");
		sql.append(" GROUP BY gctac.tipoAtividadeComplementar, gctac.cargaHoraria, gradeCurricular.totalCargaHorariaAtividadeComplementar, gradeCurricular.codigo) AS atividade ");
		sql.append(" GROUP BY cargaHorariaExigida ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT tipoAtividadeComplementar.nome AS tipo, gctac.horasMinimasExigida AS cargaHorariaExigida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) AS cargaHorariaRealizadaAtividadeComplementar, ");
		sql.append(" CASE ");
		sql.append(" WHEN COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) > gctac.cargaHoraria THEN gctac.cargaHoraria ");
		sql.append(" ELSE COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) ");
		sql.append(" END AS cargaHorariaConsiderada,  ");
		sql.append(" CASE ");
		sql.append(" WHEN COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) < gctac.horasMinimasExigida THEN gctac.horasMinimasExigida - COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) ");
		sql.append(" ELSE 0 END AS cargaHorariaPendente, ");
		sql.append(" CASE WHEN (CASE ");
		sql.append(" WHEN COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) > gctac.cargaHoraria THEN gctac.cargaHoraria ");
		sql.append(" ELSE COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) ");
		sql.append(" END) > gctac.horasMinimasExigida THEN (CASE ");
		sql.append(" WHEN COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) > gctac.cargaHoraria THEN gctac.cargaHoraria ");
		sql.append(" ELSE COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0) ");
		sql.append(" END) - gctac.horasMinimasExigida ELSE 0 END as cargaHorariaExigida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'INDEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaIndeferida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'DEFERIDO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaDeferida, ");
		sql.append(" COALESCE(SUM(CASE WHEN racm.situacaoAtividadeComplementarMatricula = 'AGUARDANDO_DEFERIMENTO' THEN racm.cargaHorariaConsiderada ELSE 0.0 END), 0.0) AS cargaHorariaAguardandoDeferimento ");
		sql.append(" FROM gradeCurricularTipoAtividadeComplementar gctac ");
		sql.append(" INNER JOIN gradeCurricular ON gradeCurricular.codigo = gctac.gradeCurricular ");
		sql.append(" INNER JOIN tipoAtividadeComplementar ON tipoAtividadeComplementar.codigo = gctac.tipoAtividadeComplementar ");
		sql.append(" LEFT JOIN registroAtividadeComplementarMatricula racm ON gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar ");
		sql.append(" WHERE gctac.gradeCurricular = cte_filtro.gradeCurricularAtual AND gctac.horasMinimasExigida > 0.0 AND racm.matricula = cte_filtro.matricula ");
		sql.append(" GROUP BY gctac.tipoAtividadeComplementar, gctac.cargaHoraria, gradeCurricular.totalCargaHorariaAtividadeComplementar, gradeCurricular.codigo, gctac.horasMinimasExigida, tipoAtividadeComplementar.nome) AS atividade) AS resultado ");
		sql.append(" WHERE (resultado.cargaHorariaConsiderada IS NULL) = FALSE) AS resultado ON TRUE ");
		sql.append(" ORDER BY cte_filtro.nome, cte_filtro.matricula ");
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("totalRegistroConsulta"));
		}
		tabelaResultado.beforeFirst();
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		return consultarPorMatriculaVisaoAluno(matricula, null, controlarAcesso, usuario);
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatriculaVisaoAluno(String matricula, Integer codigoTipoAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<AtividadeComplementarRelVO> atividadeComplementarRelVOs = getFacadeFactory().getAtividadeComplementarRelFacade().criarObjetoLayoutAtividadeComplementarAnalitico(null, null, matricula, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "matricula");
		Map<Integer, RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = new HashMap<Integer, RegistroAtividadeComplementarMatriculaVO>(0);
		for (AtividadeComplementarRelVO atividadeComplementarRelVO : atividadeComplementarRelVOs) {
			Ordenacao.ordenarListaDecrescente(atividadeComplementarRelVO.getListaEventoAtividadeComplementar(), "dataEvento");
		}
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matricula, false, usuario);
		for (AtividadeComplementarRelVO atividadeComplementarRelVO : atividadeComplementarRelVOs) {
			for (EventoAtividadeComplementarVO eventoAtividadeComplementarVO : atividadeComplementarRelVO.getListaEventoAtividadeComplementar()) {
				if (codigoTipoAtividadeComplementar == null || codigoTipoAtividadeComplementar.intValue() == 0 || eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar().equals(codigoTipoAtividadeComplementar)) {
					String ano = cursoVO.getIntegral() ? "" : Uteis.getAno(eventoAtividadeComplementarVO.getDataEvento());
					String semestre = cursoVO.getSemestral() ?  Uteis.getSemestreData(eventoAtividadeComplementarVO.getDataEvento()) : "";
					if (registroAtividadeComplementarMatriculaVOs.containsKey(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar())) {
						RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO = registroAtividadeComplementarMatriculaVOs.get(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar());
						if(!registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().containsKey(ano+semestre)) {
							RegistroAtividadeComplementarMatriculaPeriodoVO  registroAtividadeComplementarMatriculaPeriodoVO = new RegistroAtividadeComplementarMatriculaPeriodoVO(ano, semestre);
							registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().put(ano+semestre, registroAtividadeComplementarMatriculaPeriodoVO);
							registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementarMatriculaPeriodoVOs().add(registroAtividadeComplementarMatriculaPeriodoVO);
						}
						RegistroAtividadeComplementarMatriculaPeriodoVO registroAtividadeComplementarMatriculaPeriodoVO = registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().get(ano+semestre);
						registroAtividadeComplementarMatriculaVO.setCargaHorariaEvento(registroAtividadeComplementarMatriculaVO.getCargaHorariaEvento() + eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaEvento(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaEvento() + eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setArquivoVO(eventoAtividadeComplementarVO.getArquivoVO());
						registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs().add(eventoAtividadeComplementarVO);
					} else {
						RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO = new RegistroAtividadeComplementarMatriculaVO();
						if(!registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().containsKey(ano+semestre)) {
							RegistroAtividadeComplementarMatriculaPeriodoVO  registroAtividadeComplementarMatriculaPeriodoVO = new RegistroAtividadeComplementarMatriculaPeriodoVO(ano, semestre);
							registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().put(ano+semestre, registroAtividadeComplementarMatriculaPeriodoVO);
							registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementarMatriculaPeriodoVOs().add(registroAtividadeComplementarMatriculaPeriodoVO);
						}
						RegistroAtividadeComplementarMatriculaPeriodoVO registroAtividadeComplementarMatriculaPeriodoVO = registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().get(ano+semestre);
						registroAtividadeComplementarMatriculaVO.getMatriculaVO().setMatricula(atividadeComplementarRelVO.getMatricula());
						registroAtividadeComplementarMatriculaVO.getMatriculaVO().getAluno().setNome(atividadeComplementarRelVO.getNomeAluno());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaEvento(eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaExigida(eventoAtividadeComplementarVO.getCargaHorariaMaximaConsiderada());
						registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().setCodigo(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar());
						registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().setNome(eventoAtividadeComplementarVO.getAtividadeComplementar());
						registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().getResponsavelUltimaAlteracao().getPessoa().setNome(eventoAtividadeComplementarVO.getResponsavelUltimaAlteracao());
						registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().setDataUltimaAlteracao(eventoAtividadeComplementarVO.getDataUltimaAlteracao());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaConsiderada(eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaRealizada(eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaEvento(eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setArquivoVO(eventoAtividadeComplementarVO.getArquivoVO());
						registroAtividadeComplementarMatriculaVO.setObservacao(eventoAtividadeComplementarVO.getObservacao());
						registroAtividadeComplementarMatriculaVO.setCodigo(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getCodigo());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setDataDeferimentoIndeferimento(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getDataDeferimentoIndeferimento());
						if(Uteis.isAtributoPreenchido(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelDeferimentoIndeferimento().getCodigo())) {
							registroAtividadeComplementarMatriculaVO.setResponsavelDeferimentoIndeferimento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelDeferimentoIndeferimento().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
						}
						registroAtividadeComplementarMatriculaVO.setDataEditarCHConsiderada(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getDataEditarCHConsiderada());
						registroAtividadeComplementarMatriculaVO.setJustificativaAlteracaoCHConsiderada(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getJustificativaAlteracaoCHConsiderada());
						if(Uteis.isAtributoPreenchido(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo()) && !eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo().equals(0)) {
							registroAtividadeComplementarMatriculaVO.setResponsavelEditarCHConsiderada(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
						}
						registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs().add(eventoAtividadeComplementarVO);
						registroAtividadeComplementarMatriculaVOs.put(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar(), registroAtividadeComplementarMatriculaVO);
					}
				}
			}
		}
		List<RegistroAtividadeComplementarMatriculaVO> listaFinal = new ArrayList<RegistroAtividadeComplementarMatriculaVO>();
		listaFinal.addAll(registroAtividadeComplementarMatriculaVOs.values());
		
		return listaFinal;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(this.montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	// public List<RegistroAtividadeComplementarMatriculaVO>
	// montarDadosConsultaPorMatricula(SqlRowSet tabelaResultado, UsuarioVO
	// usuario) throws Exception {
	// List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new
	// ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
	// while (tabelaResultado.next()) {
	// vetResultado.add(this.montarDadosPorMatricula(tabelaResultado, usuario));
	// }
	// return vetResultado;
	// }

	// public List<RegistroAtividadeComplementarMatriculaVO>
	// montarDadosConsultaPorMatriculaVisaoAluno(SqlRowSet tabelaResultado,
	// UsuarioVO usuario) throws Exception {
	// List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new
	// ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
	// while (tabelaResultado.next()) {
	// vetResultado.add(this.montarDadosPorMatriculaVisaoAluno(tabelaResultado,
	// usuario));
	// }
	// return vetResultado;
	// }

	public RegistroAtividadeComplementarMatriculaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
		obj.setCargaHorariaExigida((int)(dadosSQL.getDouble("cargaHorariaExigida")));
		obj.setCargaHorariaRealizada((int)(dadosSQL.getDouble("cargaHorariaRealizada")));
		obj.setCargaHorariaIndeferido((int)(dadosSQL.getDouble("cargaHorariaIndeferido")));
		obj.setCargaHorariaAguardandoDeferimento((int)(dadosSQL.getDouble("cargaHorariaAguardandoDeferimento")));
		if (dadosSQL.getDouble("cargaHorariaConsiderada") > dadosSQL.getDouble("cargaHorariaExigida")) {
			obj.setCargaHorariaConsiderada((int)(dadosSQL.getDouble("cargaHorariaExigida")));
		} else {
			obj.setCargaHorariaConsiderada((int)(dadosSQL.getDouble("cargaHorariaConsiderada")));
		}
		obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularAtual"));
		obj.setAtividadeComplementarIntegraliazada(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().realizarValidacaoIntegracaoAtividadeComplementar(obj.getMatriculaVO().getMatricula(), obj.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
		obj.setCargaHorariaPendente((int)(dadosSQL.getDouble("cargaHorariaPendente")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	// public RegistroAtividadeComplementarMatriculaVO
	// montarDadosPorMatricula(SqlRowSet dadosSQL, UsuarioVO usuario) throws
	// Exception {
	// RegistroAtividadeComplementarMatriculaVO obj = new
	// RegistroAtividadeComplementarMatriculaVO();
	// obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
	// obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
	// obj.getTipoAtividadeComplementarVO().setCodigo(dadosSQL.getInt("tipoAtividadeComplementarCodigo"));
	// obj.getTipoAtividadeComplementarVO().setNome(dadosSQL.getString("tipoAtividadeComplementarNome"));
	// obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
	// obj.setCargaHorariaExigida(dadosSQL.getInt("cargaHorariaExigida"));
	// if (obj.getCargaHorariaConsiderada() > obj.getCargaHorariaExigida()) {
	// obj.setCargaHorariaPendente(0);
	// } else {
	// obj.setCargaHorariaPendente(obj.getCargaHorariaExigida() -
	// obj.getCargaHorariaConsiderada());
	// }
	//
	// obj.setNovoObj(Boolean.FALSE);
	// return obj;
	// }

	// public RegistroAtividadeComplementarMatriculaVO
	// montarDadosPorMatriculaVisaoAluno(SqlRowSet dadosSQL, UsuarioVO usuario)
	// throws Exception {
	// RegistroAtividadeComplementarMatriculaVO obj = new
	// RegistroAtividadeComplementarMatriculaVO();
	// obj.getRegistroAtividadeComplementar().setCodigo(dadosSQL.getInt("codigo"));
	// obj.getRegistroAtividadeComplementar().setNomeEvento(dadosSQL.getString("nomeEvento"));
	// if (dadosSQL.getTimestamp("data") == null) {
	// obj.getRegistroAtividadeComplementar().setData(dadosSQL.getTimestamp("data"));
	// } else {
	// obj.getRegistroAtividadeComplementar().setData(new
	// Date(dadosSQL.getTimestamp("data").getTime()));
	// }
	// obj.setCodigo(dadosSQL.getInt("registroATividadeComplementarMatricula"));
	// obj.setCargaHorariaEvento(dadosSQL.getInt("cargaHorariaEvento"));
	// obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
	// obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
	// if (!obj.getArquivoVO().getCodigo().equals(0)) {
	// obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	// }
	// obj.setNovoObj(Boolean.FALSE);
	// return obj;
	// }

	public static void validarConsulta(Integer codigoCurso) throws ConsistirException {
		if (codigoCurso == null || codigoCurso.equals(0)) {
			throw new ConsistirException("O campo CURSO deve ser informado");
		}
	}

	public static void validarDadosFiltroConsulta(Integer curso, Integer turma, String matricula, String periodicidade, String semestre, String ano) throws ConsistirException {
		if (turma > 0 && periodicidade != null && periodicidade.equals("AN") && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException("O campo Ano deve ser informado");
		}
		if (turma > 0 && periodicidade != null && periodicidade.equals("SE") && !Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException("O campo Semestre deve ser selecionado");
		}
	}
	
	private String getSqlTotalHorasRealizadasAtividadeComplementar(String matricula , Integer matrizCurricular) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select * from ( ");
		sqlStr.append(" select sum(cargaHorariaConsiderada) as cargaHorariaConsiderada ");
		sqlStr.append(" from (");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistroAtividadeComplementarMatricula(matricula, matrizCurricular, sqlStr);
		sqlStr.append("	union all ");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sqlStr);
		sqlStr.append(") as atividade ");
		sqlStr.append(" ) as resultado ");
		sqlStr.append(" where (resultado.cargahorariaconsiderada is null) = false ");
		sqlStr.append(" ) ");
		return sqlStr.toString();
	}
	
	private String getSqlTotalHorasAtividadeComplementarPendente(String matricula , Integer matrizCurricular) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select * from ( ");
		sqlStr.append(" select case when sum(cargahorariapendente-cargahorariaexcedida) < 0 then  0 else sum(cargahorariapendente-cargahorariaexcedida) end as cargahorariapendente");
		sqlStr.append(" from (");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistroAtividadeComplementarMatricula(matricula, matrizCurricular, sqlStr);
		sqlStr.append("	union all ");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sqlStr);
		sqlStr.append(") as atividade ");
		sqlStr.append(" ) as resultado ");
		sqlStr.append(" where (resultado.cargahorariapendente is null) = false ");
		sqlStr.append(" ) ");
		return sqlStr.toString();
	}

	public void validarDadosCargaHorariaRealizada(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaIncluirVO, List<RegistroAtividadeComplementarMatriculaVO> ListaConsultaRegistroAtividadeComplementarMatriculaVOs, List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs) throws Exception {
		Integer atvComplementar = registroAtividadeComplementarMatriculaIncluirVO.getTipoAtividadeComplementarVO().getCodigo();
		if(ListaConsultaRegistroAtividadeComplementarMatriculaVOs.stream().anyMatch(c -> c.getTipoAtividadeComplementarVO().getCodigo().equals(atvComplementar)) || listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs.stream().anyMatch(c -> c.getTipoAtividadeComplementarVO().getCodigo().equals(atvComplementar))){
			throw new Exception("Já existe um registro de atividade complementar (" + registroAtividadeComplementarMatriculaIncluirVO.getTipoAtividadeComplementarVO().getNome() + "), por isto não é possível realizar um novo cadastro.");
		}
		if (registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada() > 9999) {
			throw new Exception("C.H Realizada deve ser menor que 9999.");
		}
		if (registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada() <= 0) {
			throw new Exception("C.H Realizada deve ser maior que zero.");
		}
		if (!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada())) {
			throw new Exception("C.H Realizada deve ser informada.");
		}
		if (!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaIncluirVO.getArquivoVO().getDescricao())) {
			throw new Exception("O campo ARQUIVO deve ser informado.");
		}
	}
	
	public RegistroAtividadeComplementarMatriculaVO consultarCargaHorariaTotal(String matricula, boolean controlarAcesso, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT	gradecurricular.totalCargaHorariaAtividadeComplementar as cargahorariaexigida,");
		sql.append(getSqlTotalHorasRealizadasAtividadeComplementar(matricula,codigoGradeCurricular)).append(" as cargahorariaconsiderada, ");
		sql.append(getSqlTotalHorasIndeferido()).append(" as cargaHorariaIndeferido, ");
		sql.append(getSqlTotalHorasAguardandoDeferimento()).append(" as cargaHorariaAguardandoDeferimento, ");
		sql.append(getSqlTotalHorasAtividadeComplementarPendente(matricula , codigoGradeCurricular)).append(" as cargaHorariaPendente, ");
		sql.append(getSqlTotalHorasRealizadas()).append(" as cargahorariarealizada ");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula= '").append(matricula).append("' ");
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		if (tabelaResultado.next()) {
			obj.setCargaHorariaExigida(tabelaResultado.getInt("cargahorariaexigida"));
			obj.setCargaHorariaConsiderada(tabelaResultado.getInt("cargahorariaconsiderada"));
			obj.setCargaHorariaIndeferido(tabelaResultado.getInt("cargaHorariaIndeferido"));
			obj.setCargaHorariaAguardandoDeferimento(tabelaResultado.getInt("cargaHorariaAguardandoDeferimento"));
			obj.setCargaHorariaPendente((int)(tabelaResultado.getDouble("cargaHorariaPendente")));
			obj.setCargaHorariaRealizada(tabelaResultado.getInt("cargaHorariaRealizada"));
		}
		return obj;
	}
}
