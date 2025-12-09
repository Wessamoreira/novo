/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.ResultadoProcessoSeletivoDisciplinaRelVO;
import relatorio.negocio.comuns.processosel.ResultadoProcessoSeletivoRelVO;
import relatorio.negocio.interfaces.processosel.ResultadoProcessoSeletivoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ResultadoProcessoSeletivoRel extends SuperRelatorio implements ResultadoProcessoSeletivoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public void validarDados(List<ProcSeletivoVO> listaProcSeletivoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, String ano) throws Exception {
		if (listaProcSeletivoVOs.isEmpty()) {
			throw new Exception("O campo PROCESSO SELETIVO deve ser informado.");
		}
		boolean unidadeSelecionada = false;
		for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				unidadeSelecionada = true;
				break;
			}
		}
		if (!unidadeSelecionada) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (ano.equals("")) {
			throw new Exception("O campo ANO deve ser informado.");
		}
	}
	
	public List<ResultadoProcessoSeletivoRelVO> criarObjeto(List<ProcSeletivoVO> listaProcSeletivoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Boolean apresentarNota, Boolean apresentarNotaPorDisciplina, String ordenacao, SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivoEnum, Date dataProvaInicio, Date dataProvaFim, String ano, String semestre, Boolean apresentarQuantidadeAcerto, Boolean apresentarSituacaoResultadoProcessoSeletivo, InscricaoVO inscricao, UsuarioVO usuarioVO) throws Exception {
		validarDados(listaProcSeletivoVOs, listaUnidadeEnsinoVOs, ano);
		return executarPesquisaParametrizada(listaProcSeletivoVOs, listaUnidadeEnsinoVOs, listaCursoVOs, listaTurnoVOs, apresentarNota, apresentarNotaPorDisciplina, ordenacao, situacaoResultadoProcessoSeletivoEnum, dataProvaInicio, dataProvaFim, ano, semestre, apresentarQuantidadeAcerto, apresentarSituacaoResultadoProcessoSeletivo, inscricao, usuarioVO);
	}

	private List<ResultadoProcessoSeletivoRelVO> executarPesquisaParametrizada(List<ProcSeletivoVO> listaProcSeletivoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Boolean apresentarNota, Boolean apresentarNotaPorDisciplina, String ordenacao, SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivoEnum, Date dataProvaInicio, Date dataProvaFim, String ano, String semestre, Boolean apresentarQuantidadeAcerto, Boolean apresentarSituacaoResultadoProcessoSeletivo, InscricaoVO inscricao, UsuarioVO usuarioVO) {

		StringBuilder sql = new StringBuilder("");
		sql.append("select * from ( ");
		sql.append("select * from ( ");
		sql.append(SQLrealizarClassificacaoCandidatoProcessoSeletivo(listaProcSeletivoVOs, listaUnidadeEnsinoVOs, listaCursoVOs, listaTurnoVOs, situacaoResultadoProcessoSeletivoEnum, dataProvaInicio, dataProvaFim, ano, semestre, usuarioVO));
		sql.append("resultadoprocessoseletivo.codigo AS resultadoprocessoseletivo, inscricao.codigo AS numeroInscricao, inscricao.chamada as chamada, inscricao.candidatoconvocadomatricula, ");
		sql.append("pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS nomeCandidato, pessoa.email, pessoa.telefoneres, pessoa.telefoneComer, pessoa.celular, ");
		sql.append("pessoa.datanasc, pessoa.sexo, pessoa.endereco, pessoa.complemento, pessoa.numero, pessoa.setor, cidade.nome as cidade, estado.sigla as estado, pessoa.cep,  ");
		sql.append("medianotasprocseletivo, notaredacao, somatorioacertos, unidadeensinocurso.codigo as unidadeensinocurso, inscricao.cursoopcao1, inscricao.cursoopcao2, inscricao.cursoopcao3, curso.nome as  nomeCurso , turno.nome as nomeTurno, itemprocessoseletivodataprova as codigoDataProva, ");
		sql.append("sala, procseletivocurso.numerovaga, unidadeEnsino.nome as unidadeEnsino_nome, procseletivo.regimeAprovacao, procseletivo.descricao AS processoSeletivo, ");
		sql.append("(select matricula.inscricao from matricula where matricula.inscricao = inscricao.codigo limit 1) is not null as matriculado, ");
		sql.append(" resultadoprocessoseletivo.resultadoprimeiraopcao, resultadoprocessoseletivo.resultadosegundaopcao, resultadoprocessoseletivo.resultadoterceiraopcao, nropcoescurso ");
		sql.append("from resultadoprocessoseletivo ");
		sql.append("inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao ");

		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO)) {
			sql.append(" INNER JOIN unidadeensinocurso unidadeensinocurso ON ((unidadeensinocurso.codigo = inscricao.cursoopcao1 AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP')");
			sql.append(" OR (unidadeensinocurso.codigo = inscricao.cursoopcao2 AND resultadoprocessoseletivo.resultadosegundaopcao = 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' )");
			sql.append(" OR (unidadeensinocurso.codigo = inscricao.cursoopcao3 AND resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' AND resultadoprocessoseletivo.resultadosegundaopcao <> 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP'))");
		}
		
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_1_OPCAO)) {
			sql.append(" INNER JOIN unidadeensinocurso unidadeensinocurso ON (unidadeensinocurso.codigo = inscricao.cursoopcao1 AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'AP' AND resultadoprocessoseletivo.resultadosegundaopcao <> 'AP' AND resultadoprocessoseletivo.resultadoterceiraopcao <> 'AP')");
		}
		
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_2_OPCAO)) {
			sql.append(" INNER JOIN unidadeensinocurso unidadeensinocurso ON (unidadeensinocurso.codigo = inscricao.cursoopcao2 AND resultadoprocessoseletivo.resultadosegundaopcao = 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' AND resultadoprocessoseletivo.resultadoterceiraopcao <> 'AP')");
		}
		
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_3_OPCAO)) {
			sql.append(" INNER JOIN unidadeensinocurso unidadeensinocurso ON (unidadeensinocurso.codigo = inscricao.cursoopcao3 AND resultadoprocessoseletivo.resultadoterceiraopcao = 'AP' AND resultadoprocessoseletivo.resultadoprimeiraopcao <> 'AP' AND resultadoprocessoseletivo.resultadosegundaopcao <> 'AP')");
		}
		
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.REPROVADO)) {
		    sql.append(" INNER JOIN unidadeensinocurso unidadeensinocurso ON ((unidadeensinocurso.codigo = inscricao.cursoopcao1 AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' AND resultadoprocessoseletivo.resultadosegundaopcao = 'RE' AND resultadoprocessoseletivo.resultadoterceiraopcao = 'RE')");
		    sql.append(" OR (unidadeensinocurso.codigo = inscricao.cursoopcao2 AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' AND resultadoprocessoseletivo.resultadosegundaopcao = 'RE' AND resultadoprocessoseletivo.resultadoterceiraopcao = 'RE')");
		    sql.append(" OR (unidadeensinocurso.codigo = inscricao.cursoopcao3 AND resultadoprocessoseletivo.resultadoprimeiraopcao = 'RE' AND resultadoprocessoseletivo.resultadosegundaopcao = 'RE' AND resultadoprocessoseletivo.resultadoterceiraopcao = 'RE'))");
		}
		
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.TODOS)) {		
			sql.append("INNER JOIN unidadeensinocurso unidadeensinocurso ON (unidadeensinocurso.codigo = inscricao.cursoopcao1 or unidadeensinocurso.codigo = inscricao.cursoopcao2 or unidadeensinocurso.codigo = inscricao.cursoopcao3)  ");
		}
		sql.append("inner join unidadeEnsino on unidadeEnsino.codigo = unidadeensinocurso.unidadeEnsino ");
		sql.append("inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = inscricao.procseletivo ");
		sql.append("and unidadeEnsino.codigo = procseletivounidadeensino.unidadeEnsino ");
		sql.append("inner join procseletivocurso on procseletivocurso.unidadeensinocurso = unidadeensinocurso.codigo ");
		sql.append("and procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
		sql.append("inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append("inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append("inner join pessoa on pessoa.codigo = inscricao.candidato  ");
		sql.append("left join cidade on pessoa.cidade = cidade.codigo ");
		sql.append("left join estado on estado.codigo = cidade.estado ");
		sql.append("inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		if (dataProvaInicio != null || dataProvaFim != null) {
			sql.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		}
		sql.append(" where 1=1 ");
		sql.append("and inscricao.procseletivo in(");
		for (ProcSeletivoVO procSeletivoVO : listaProcSeletivoVOs) {
			if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
				sql.append(procSeletivoVO.getCodigo()).append(", ");
			}
		}
		sql.append("0) ");
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsinoVOs)) {
			sql.append("and unidadeensinocurso.unidadeEnsino in(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sql.append(unidadeEnsinoVO.getCodigo()).append(", ");
				}
			}
			sql.append("0) ");
		}
		String filtroCurso = realizarFiltroCurso(listaCursoVOs);
		if (!filtroCurso.equals("")) {
			sql.append("and unidadeensinocurso.curso in(");
			sql.append(filtroCurso);
			sql.append("0) ");
		}
		String filtroTurno = realizarFiltroTurno(listaTurnoVOs);
		if (!filtroTurno.equals("")) {
			sql.append("and unidadeensinocurso.turno in(");
			sql.append(filtroTurno);
			sql.append("0) ");
		}
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO)) {
			sql.append(" and (resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_1_OPCAO)) {
			sql.append(" and (resultadoprimeiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_2_OPCAO)) {
			sql.append(" and (resultadosegundaopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_3_OPCAO)) {
			sql.append(" and (resultadoterceiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.REPROVADO)) {
			sql.append(" and (resultadoprimeiraopcao = 'RE' AND resultadosegundaopcao = 'RE' AND resultadoterceiraopcao = 'RE') ");
		}
		if (dataProvaInicio != null) {
			sql.append("and itemprocseletivodataprova.dataprova::date >= '").append(Uteis.getDataJDBC(dataProvaInicio)).append("' ");
		}
		if (dataProvaFim != null) {
			sql.append("and itemprocseletivodataprova.dataprova::date <= '").append(Uteis.getDataJDBC(dataProvaFim)).append("' ");
		}
		if (!ano.equals("")) {
			sql.append(" and procseletivo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sql.append(" and procseletivo.semestre = '").append(semestre).append("' ");
		}
		if (inscricao.getCodigo().intValue() > 0) {
			sql.append(" and inscricao.codigo = ").append(inscricao.getCodigo());
		}		
		sql.append(") as t ");
		
		sql.append(" order by case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end ) as t ");
		if (ordenacao.equals("nomeCandidatoProcessoSeletivo")) {
			sql.append(" order by processoSeletivo, unidadeEnsino_nome, nomeCurso, nomeTurno, nomeCandidato ");
		} else if (ordenacao.equals("classificacaoCandidatoProcessoSeletivo")) {
			sql.append(" order by nomeCurso, nomeTurno, case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, classificacao, processoSeletivo, unidadeEnsino_nome ");
		} else if (ordenacao.equals("processoSeletivoNomeCandidato")) {
			sql.append(" order by nomeCurso, nomeTurno, processoSeletivo, unidadeEnsino_nome, nomeCandidato, case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, classificacao ");
		} else {
			sql.append(" order by nomeCurso, nomeTurno, processoSeletivo, unidadeEnsino_nome, case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, classificacao, nomeCandidato ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDados(tabelaResultado, apresentarNota, apresentarNotaPorDisciplina, apresentarQuantidadeAcerto, apresentarSituacaoResultadoProcessoSeletivo, usuarioVO);
	}

	public StringBuilder SQLrealizarClassificacaoCandidatoProcessoSeletivo(List<ProcSeletivoVO> listaProcSeletivoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivoEnum, Date dataProvaInicio, Date dataProvaFim, String ano, String semestre, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT ROW_NUMBER () OVER ( PARTITION BY case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, procseletivo.codigo, unidadeensinocurso.unidadeensino, curso.codigo, turno.codigo ");
		sqlStr.append(" ORDER BY CASE WHEN (procseletivo.regimeaprovacao = 'quantidadeAcertosRedacao') THEN (medianotasprocseletivo + notaredacao) ELSE medianotasprocseletivo END DESC, ");
		sqlStr.append("  notaredacao DESC,  ARRAY( ");
		sqlStr.append("  SELECT rank FROM (SELECT distinct RANK() OVER( PARTITION BY case when resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP' then 1 else 2 end, ins.procseletivo, uec.unidadeEnsino, uec.curso, uec.turno,  ");
		sqlStr.append("  dgdps.ordemcriteriodesempate ORDER BY uec.unidadeensino, uec.curso, uec.turno ,  dgdps.ordemcriteriodesempate, rdps.nota DESC ),  ");
		sqlStr.append("  ins.codigo as inscricao, uec.unidadeEnsino, uec.curso, uec.turno,  rdps.disciplinaprocseletivo,  dgdps.ordemcriteriodesempate,  rdps.nota ");
		sqlStr.append(" FROM resultadoprocessoseletivo   as rps INNER JOIN inscricao as ins ON ins.codigo = rps.inscricao  ");
		sqlStr.append("  INNER JOIN unidadeensinocurso uec ON ((uec.codigo = ins.cursoopcao1 and resultadoprimeiraopcao = 'AP') or (uec.codigo = ins.cursoopcao2 and resultadosegundaopcao = 'AP') or (uec.codigo = ins.cursoopcao3  and resultadoterceiraopcao = 'AP'))  ");

		sqlStr.append("  INNER JOIN procseletivounidadeensino psue ON psue.procseletivo = ins.procseletivo   AND uec.unidadeensino = psue.unidadeEnsino    ");
		sqlStr.append("  INNER JOIN procseletivocurso psc ON psc.unidadeensinocurso = uec.codigo   and psc.procseletivounidadeensino = psue.codigo    ");
		sqlStr.append("  INNER JOIN grupodisciplinaprocseletivo gdps on gdps.codigo = psc.grupodisciplinaprocseletivo ");
		sqlStr.append("  INNER JOIN disciplinasgrupodisciplinaprocseletivo dgdps ON dgdps.grupodisciplinaprocseletivo = gdps.codigo ");
		sqlStr.append("  INNER JOIN resultadodisciplinaprocseletivo rdps ON rdps.resultadoprocessoseletivo = rps.codigo ");
		sqlStr.append("  AND rdps.disciplinaprocseletivo = dgdps.disciplinasprocseletivo ");
		sqlStr.append(" INNER JOIN procseletivo ON procseletivo.codigo = ins.procseletivo ");
		if (dataProvaInicio != null || dataProvaFim != null) {
			sqlStr.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = ins.itemprocessoseletivodataprova ");
		}
		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append("and ins.procseletivo in(");
		for (ProcSeletivoVO procSeletivoVO : listaProcSeletivoVOs) {
			if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
				sqlStr.append(procSeletivoVO.getCodigo()).append(", ");
			}
		}
		sqlStr.append("0) ");
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsinoVOs)) {
			sqlStr.append("and uec.unidadeEnsino in(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sqlStr.append(unidadeEnsinoVO.getCodigo()).append(", ");
				}
			}
			sqlStr.append("0) ");
		}
		String filtroCurso = realizarFiltroCurso(listaCursoVOs);
		if (!filtroCurso.equals("")) {
			sqlStr.append("and uec.curso in(");
			sqlStr.append(filtroCurso);
			sqlStr.append("0) ");
		}
		String filtroTurno = realizarFiltroTurno(listaTurnoVOs);
		if (!filtroTurno.equals("")) {
			sqlStr.append("and uec.turno in(");
			sqlStr.append(filtroTurno);
			sqlStr.append("0) ");
		}
		if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO)) {
			sqlStr.append(" and (resultadoprimeiraopcao = 'AP' or resultadosegundaopcao = 'AP' or resultadoterceiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_1_OPCAO)) {
			sqlStr.append(" and (resultadoprimeiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_2_OPCAO)) {
			sqlStr.append(" and (resultadosegundaopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.APROVADO_3_OPCAO)) {
			sqlStr.append(" and (resultadoterceiraopcao = 'AP') ");
		} else if (situacaoResultadoProcessoSeletivoEnum.equals(SituacaoResultadoProcessoSeletivoEnum.REPROVADO)) {
			sqlStr.append(" and (resultadoprimeiraopcao = 'RE' or resultadosegundaopcao = 'RE' or resultadoterceiraopcao = 'RE') ");
		}
		sqlStr.append(" AND CASE WHEN (dgdps.ordemcriteriodesempate IS NULL OR dgdps.ordemcriteriodesempate = '') THEN 0 ELSE dgdps.ordemcriteriodesempate::INT END > 0 ");
		if (dataProvaInicio != null) {
			sqlStr.append("and itemprocseletivodataprova.dataprova::date >= '").append(Uteis.getDataJDBC(dataProvaInicio)).append("' ");
		}
		if (dataProvaFim != null) {
			sqlStr.append("and itemprocseletivodataprova.dataprova::date <= '").append(Uteis.getDataJDBC(dataProvaFim)).append("' ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" and procseletivo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" and procseletivo.semestre = '").append(semestre).append("' ");
		}
		sqlStr.append(" ) AS t WHERE t.inscricao = inscricao.codigo ");
		sqlStr.append(" ORDER BY ordemcriteriodesempate ,RANK), datanasc ) AS classificacao, ");
		return sqlStr;
	}
	
	public String realizarFiltroCurso(List<CursoVO> listaCursoVOs) {
		StringBuilder sb = new StringBuilder();
		for (CursoVO cursoVO : listaCursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sb.append(cursoVO.getCodigo()).append(", ");
			}
		}
		return sb.toString();
	}

	public String realizarFiltroTurno(List<TurnoVO> listaTurnoVOs) {
		StringBuilder sb = new StringBuilder();
		for (TurnoVO turnoVO : listaTurnoVOs) {
			if (turnoVO.getFiltrarTurnoVO()) {
				sb.append(turnoVO.getCodigo()).append(", ");
			}
		}
		return sb.toString();
	}


	private List<ResultadoProcessoSeletivoRelVO> montarDados(SqlRowSet dadosSQL, Boolean apresentarNota, Boolean apresentarNotaPorDisciplina, Boolean apresentarQuantidadeAcerto, Boolean apresentarSituacaoResultadoProcessoSeletivo, UsuarioVO usuarioVO) {
		List<ResultadoProcessoSeletivoRelVO> listaObjs = new ArrayList<ResultadoProcessoSeletivoRelVO>(0);
		while (dadosSQL.next()) {
			ResultadoProcessoSeletivoRelVO obj = new ResultadoProcessoSeletivoRelVO();
			obj.setProcessoSeletivo(dadosSQL.getString("processoSeletivo"));
			obj.setCurso(dadosSQL.getString("nomeCurso"));
			obj.setInscricao(dadosSQL.getInt("numeroInscricao"));
			obj.setNomeCandidato(dadosSQL.getString("nomeCandidato"));
			obj.setDataNasc(Uteis.getData(dadosSQL.getDate("dataNasc")));
			PessoaVO p = new PessoaVO();
			p.setSexo(dadosSQL.getString("sexo"));
			obj.setSexo(p.getSexo_Apresentar());
			obj.setEndereco(dadosSQL.getString("endereco"));
			obj.setNumero(dadosSQL.getString("numero"));
			obj.setComplemento(dadosSQL.getString("complemento"));
			obj.setSetor(dadosSQL.getString("setor"));
			obj.setCidade(dadosSQL.getString("cidade"));
			obj.setEstado(dadosSQL.getString("estado"));
			obj.setCep(dadosSQL.getString("cep"));
			obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
			obj.setTelefoneComerc(dadosSQL.getString("telefoneComer"));
			obj.setCelular(dadosSQL.getString("celular"));	
			if((dadosSQL.getString("resultadoPrimeiraOpcao").equals("AP") && dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao1")) 
					|| (dadosSQL.getString("resultadoSegundaOpcao").equals("AP")  && dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao2") ) 
					|| (dadosSQL.getString("resultadoTerceiraOpcao").equals("AP")  && dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao3"))){
				obj.setClassificacao(dadosSQL.getInt("classificacao"));
			}else{
				obj.setClassificacao(null);
			}
			obj.setRegimeAprovacao(dadosSQL.getString("regimeAprovacao"));
			obj.setApresentarNota(apresentarNota);
			obj.setApresentarNotaPorDisciplina(apresentarNotaPorDisciplina);
			obj.setNotaRedacao(dadosSQL.getDouble("notaredacao"));
			obj.setMediaNotasProcSeletivo(dadosSQL.getDouble("medianotasprocseletivo"));
			obj.setNumeroAcertos(new Double(dadosSQL.getDouble("somatorioacertos")).intValue());
			obj.setChamada(dadosSQL.getInt("chamada"));
			obj.setTurno(dadosSQL.getString("nomeTurno"));
			obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino_nome"));
			obj.setApresentarQuantidadeAcerto(apresentarQuantidadeAcerto);
			obj.setApresentarSituacaoResultadoProcessoSeletivo(apresentarSituacaoResultadoProcessoSeletivo);
			if(dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao1")){
				obj.setResultadoPrimeiraOpcao(dadosSQL.getString("resultadoPrimeiraOpcao"));
			}else{
				obj.setResultadoPrimeiraOpcao("RE");
			}
			if(dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao2")){
				obj.setResultadoSegundaOpcao(dadosSQL.getString("resultadoSegundaOpcao"));
			}else{
				obj.setResultadoSegundaOpcao("RE");
			}
			if(dadosSQL.getInt("unidadeensinocurso") ==  dadosSQL.getInt("cursoopcao3")){
				obj.setResultadoTerceiraOpcao(dadosSQL.getString("resultadoTerceiraOpcao"));
			}else{
				obj.setResultadoTerceiraOpcao("RE");
			}
			obj.setNrOpcoesCurso(dadosSQL.getInt("nrOpcoesCurso"));
			if (apresentarNotaPorDisciplina) {
				obj.setListaResultadoProcessoSeletivoDisciplinaRelVOs(consultarPorResultadoProcessoSeletivo(dadosSQL.getInt("resultadoprocessoseletivo"), usuarioVO));
			}
			listaObjs.add(obj);
		}
		return listaObjs;
	}

	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public String designIReportRelatorioFicha() {
		return (caminhoBaseRelatorio() + getIdEntidadeFicha() + ".jrxml");
	}
	
	public String designIReportRelatorioExcel() {
		return (caminhoBaseRelatorio() + getIdEntidadeExcel() + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return "ResultadoProcessoSeletivoRel";
	}

	public static String getIdEntidadeFicha() {
		return "ResultadoProcessoSeletivoFichaRel";
	}
	
	public static String getIdEntidadeExcel() {
		return "ResultadoProcessoSeletivoRelRel";
	}
	
	public List<ResultadoProcessoSeletivoDisciplinaRelVO> consultarPorResultadoProcessoSeletivo(Integer codigoResultadoProcessoSeletivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select disciplinasProcSeletivo.nome, ResultadoDisciplinaProcSeletivo.nota, ResultadoDisciplinaProcSeletivo.quantidadeAcertos from ResultadoDisciplinaProcSeletivo ");
		sb.append(" inner join disciplinasProcSeletivo on disciplinasProcSeletivo.codigo = ResultadoDisciplinaProcSeletivo.disciplinaProcSeletivo ");
		sb.append(" where ResultadoDisciplinaProcSeletivo.resultadoProcessoSeletivo = ").append(codigoResultadoProcessoSeletivo);
		sb.append(" order by disciplinasProcSeletivo.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ResultadoProcessoSeletivoDisciplinaRelVO> listaResultadoProcessoSeletivoDisciplinaRelVOs = null;
		ResultadoProcessoSeletivoDisciplinaRelVO obj = null;
		while (tabelaResultado.next()) {
			if (listaResultadoProcessoSeletivoDisciplinaRelVOs == null) {
				listaResultadoProcessoSeletivoDisciplinaRelVOs = new ArrayList<ResultadoProcessoSeletivoDisciplinaRelVO>(0);
			}
			obj = new ResultadoProcessoSeletivoDisciplinaRelVO();
			obj.getResultadoDisciplinaProcSeletivoVO().getDisciplinaProcSeletivo().setNome(tabelaResultado.getString("nome"));
			obj.getResultadoDisciplinaProcSeletivoVO().setNota(tabelaResultado.getDouble("nota"));
			obj.getResultadoDisciplinaProcSeletivoVO().setQuantidadeAcertos(tabelaResultado.getInt("quantidadeAcertos"));
			listaResultadoProcessoSeletivoDisciplinaRelVOs.add(obj);
		}
		return listaResultadoProcessoSeletivoDisciplinaRelVOs;
	}

}
