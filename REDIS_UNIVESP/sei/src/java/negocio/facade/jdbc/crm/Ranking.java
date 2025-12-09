package negocio.facade.jdbc.crm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.faces.model.SelectItem;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;
import negocio.comuns.crm.RankingTurmaConsultorAlunoVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;
import negocio.comuns.crm.enumerador.TagFormulaConfiguracaoRankingEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.RankingInterfaceFacade;

/**
 * 
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class Ranking extends ControleAcesso implements RankingInterfaceFacade {

	protected static String idEntidade;

	public Ranking() {
		super();
		setIdEntidade("ComissionamentoTurma");
	}

	public List<UnidadeEnsinoVO> montarListaSelectItemUnidadeEnsino(Integer unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, usuarioVO);
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		if (unidadeEnsinoLogado.equals(0)) {
			objs.add(new SelectItem(0, ""));
		}
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		return objs;
	}

	public List consultarCurso(String valorConsultaCurso, String campoConsultaCurso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List objs = new ArrayList(0);
		if (valorConsultaCurso.equals("")) {
			throw new Exception("Informe pelo menos 2 (dois) parâmetros para consulta.");
		}
		if (campoConsultaCurso.equals("codigo")) {
			if (valorConsultaCurso.equals("")) {
				valorConsultaCurso = "0";
			}
			int valorInt = Integer.parseInt(valorConsultaCurso);
			objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		}
		if (campoConsultaCurso.equals("nome")) {
			objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(valorConsultaCurso, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		}
		return objs;

	}

	public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO) throws Exception {
		if (campoConsulta.equals("identificadorTurma")) {
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(valorConsulta, curso, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, 0,0);
		}
		return new ArrayList(0);
	}

	@Deprecated
	public List<RankingTurmaVO> consultar(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select matricula.matricula, matricula.situacao, matriculaperiodo.situacaomatriculaperiodo, pessoa.nome as aluno, pessoa.codigo as aluno_codigo, ");
		sb.append(" pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefoneComer, pessoa.telefoneRecado, ");
		sb.append(" curso.nome as curso, turno.nome as turno, unidadeensino.nome as unidadeensino, ");
		sb.append(" turma.codigo  as turma_codigo, turma.identificadorTurma  as turma, ");
		sb.append(" funcionario.codigo as  consultor_codigo, consultor.nome as consultor, ");
		sb.append(" matriculaperiodo.bolsista, ");
		sb.append(" considerarRankingCrmSomenteMatriculAtivo, ");
		sb.append(" desconsiderarRankingCrmSomenteMatriculPR, ");
		sb.append(" desconsiderarRankingCrmAlunoBolsista, ");
		sb.append(" considerarRankingCrmPrimeiraMensalidade, ");
		sb.append(" (select situacao in ('RE', 'NE') from contareceber where tipoorigem  = 'MEN' ");
		sb.append(" and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem and situacao = 'RE' order by dataVencimento limit 1) as primeiraMensalidadePaga, ");
		sb.append(" desconsiderarParcelaEFaltaApartir3Meses, ");

		sb.append(" (select case when qtdRec + qtdARec >= 2 then case when qtdRec > 0 then false else true end ");
		sb.append(" else false end from ( select (select count(*) as qtdRec from contareceber where tipoorigem  = 'MEN'  ");
		sb.append(" and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem and datavencimento >= (current_date - 60) and datavencimento <= current_date and situacao in ('RE', 'NE') limit 1)  as qtdRec ");
		sb.append(" , (select count(*) as qtdARec from contareceber where tipoorigem  = 'MEN' and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem  ");
		sb.append(" and datavencimento >= (current_date - 60) and datavencimento <= current_date and situacao in ('AR') limit 1) as qtdARec ");
		sb.append(" from contareceber where matriculaperiodo.codigo::VARCHAR = contareceber.codorigem  limit 1) as t) as tresmesesdevendo,  ");

		sb.append(" (select qtdemodulototal >=2 and qtdemoduloposterior >= 2 as tresmesesfaltando from(select count(distinct registroaula.disciplina) as qtdemoduloposterior, qtdemodulototal,  ultimapresenca   from frequenciaaula ");
		sb.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula and frequenciaaula.matricula = matriculaperiodo.matricula and registroaula.data <= (current_date)  ");
		sb.append(" inner join (select count(distinct registroaula.disciplina) as qtdemodulototal,  max(case when presente = true then data else null end) as ultimapresenca ");
		sb.append(" from frequenciaaula inner join registroaula on registroaula.codigo = frequenciaaula.registroaula where frequenciaaula.matricula = matriculaperiodo.matricula ");
		sb.append(" and registroaula.data <= (current_date) ) as t on (registroaula.data > t.ultimapresenca or t.ultimapresenca is null) group by qtdemodulototal,  ultimapresenca) as t) as tresmesesfaltando, ");

		sb.append(" desconsiderarContratoNaoAssinadoApartir4Meses, ");
		sb.append(" considerarContratoAssinadoRankingCrm, ");
		sb.append(" (select true from documetacaomatricula inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento where documetacaomatricula.entregue = true and tipodocumento.contrato = true and documetacaomatricula.matricula = matriculaperiodo.matricula limit 1) as contratoassinado, ");
		sb.append(" qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm, ");
		sb.append(" desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm, ");
		sb.append(" count(t.data) as qtdeModuloAntesMatricula,  ");
		// sb.append(" (select count(*) from (select distinct turma, disciplina from horarioturmadetalhado(2480, null, null, null) where data < current_date) as t) as qtdeModuloJaOcorreram,  ");
		sb.append(" considerarAlunoAdimplenteSemContratoAssinadoRankingCrm, ");
		sb.append(" qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm, ");
		sb.append(" (select count(codigo) from contareceber  where datavencimento::DATE  < current_date and situacao = 'AR' and matriculaaluno = matriculaperiodo.matricula and tipoorigem in ('MEN', 'MAT')) as qtdeParcelaAtraso, ");
		sb.append(" (select codigo from funcionariocargo where funcionarioCargo.funcionario = funcionario.codigo and gerente = true limit 1) is not null as gerente, ");
		sb.append(" configuracaoRanking.codigo as \"configuracaoRanking.codigo\",  configuracaoRanking.nome as  \"configuracaoRanking.nome\", configuracaoRanking.percentualGerente as \"configuracaoRanking.percentualGerente\", configuracaoRanking.formulaCalculoComissao  as \"configuracaoRanking.formulaCalculoComissao\", ");
		sb.append(" comissionamentoTurma.codigo as \"comissionamentoTurma.codigo\", comissionamentoTurma.dataPrimeiroPagamento as \"comissionamentoTurma.dataPrimeiroPagamento\", ");
		sb.append(" comissionamentoTurma.dataUltimoPagamento as \"comissionamentoTurma.dataUltimoPagamento\", comissionamentoTurma.qtdeParcela as \"comissionamentoTurma.qtdeParcela\" ");

		sb.append(" from matriculaperiodo ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join comissionamentoTurma on comissionamentoTurma.turma = turma.codigo ");
		sb.append(" inner join configuracaoranking on comissionamentoTurma.configuracaoranking = configuracaoranking.codigo ");
		sb.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula  ");
		sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by ano||'/'||semestre desc limit 1  ) ");
		sb.append(" inner join curso on curso.codigo = matricula.curso  ");
		sb.append(" inner join turno on turno.codigo = matricula.turno  ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		// sb.append(" inner join configuracoes on configuracoes.codigo = unidadeensino.configuracoes ");
		// sb.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa as consultor on consultor.codigo = funcionario.pessoa ");
		sb.append(" inner join horarioturma on horarioturma.turma = turma.codigo and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre ");
		sb.append(" left join  (select max(data) as data2, horarioturma, ct.turma from horarioturmadetalhado(null, null,null, null) as ht ");
		sb.append(" inner join comissionamentoTurma ct on ct.turma = ht.turma		 ");
		sb.append(" group by disciplina, horarioturma, ct.turma) as t2 on ");
		sb.append(" t2.horarioturma = horarioturma.codigo and t2.data2 < current_date and t2.turma = turma.codigo ");
		sb.append(" left join  (select max(data) as data, horarioturma, ct.turma from horarioturmadetalhado(null, null,null, null) as ht ");
		sb.append(" inner join comissionamentoTurma ct on ct.turma = ht.turma		 ");
		sb.append(" group by disciplina, horarioturma, ct.turma) as t on ");
		sb.append(" t.horarioturma = horarioturma.codigo and t.data < matricula.data and t.turma = turma.codigo ");
		sb.append(" where to_char(comissionamentoTurma.dataUltimoPagamento, 'yy/MM') >= to_char(current_date, 'yy/MM') ");
		sb.append(" and to_char(comissionamentoTurma.dataPrimeiroPagamento, 'yy/MM') <= to_char(current_date, 'yy/MM') ");

		if (unidadeEnsino > 0) {
			sb.append(" and turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!curso.equals(0)) {
			sb.append(" AND turma.curso = ").append(curso);
		}
		if (!turma.equals(0)) {
			sb.append(" AND turma.codigo = ").append(turma);
		}

		sb.append("  group by configuracaoRanking.formulaCalculoComissao, matriculaperiodo.matricula,matricula.matricula, matricula.situacao, pessoa.nome, ");
		sb.append("  pessoa.telefoneRecado, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.codigo, pessoa.telefoneComer, pessoa.telefoneComer, ");
		sb.append(" turma.codigo, turma.identificadorTurma, ");
		sb.append(" funcionario.codigo, consultor.nome, ");
		sb.append(" matriculaperiodo.bolsista, ");
		sb.append(" considerarRankingCrmSomenteMatriculAtivo, ");
		sb.append(" desconsiderarRankingCrmAlunoBolsista, ");
		sb.append(" considerarRankingCrmPrimeiraMensalidade, ");
		sb.append(" considerarContratoAssinadoRankingCrm,  ");
		sb.append(" qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm, ");
		sb.append(" desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm, ");
		sb.append(" matriculaperiodo.codigo, ");
		sb.append(" considerarAlunoAdimplenteSemContratoAssinadoRankingCrm, ");
		sb.append(" qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm, ");
		sb.append(" curso.nome, turno.nome, unidadeensino.nome, ");
		sb.append(" configuracaoRanking.codigo, configuracaoRanking.nome, configuracaoRanking.percentualGerente, ");
		sb.append(" comissionamentoTurma.codigo, comissionamentoTurma.dataPrimeiroPagamento, ");
		sb.append(" comissionamentoTurma.dataUltimoPagamento, comissionamentoTurma.qtdeParcela, ");
		sb.append(" desconsiderarParcelaEFaltaApartir3Meses, desconsiderarContratoNaoAssinadoApartir4Meses, matriculaperiodo.situacaomatriculaperiodo, desconsiderarrankingcrmsomentematriculpr ");
		sb.append(" order by turma, consultor.nome, pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RankingTurmaVO> vetResultado = new ArrayList<RankingTurmaVO>(0);
		RankingTurmaVO obj = null;
		RankingVO rankingVO = null;
		RankingTurmaConsultorAlunoVO rankingTurmaConsultorAlunoVO = null;
		Integer turmaAtual = 0;
		Integer consultorAtual = 0;
		while (tabelaResultado.next()) {

			if (turmaAtual.intValue() != tabelaResultado.getInt("turma_codigo")) {
				obj = new RankingTurmaVO();
				obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma_codigo"));
				obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma"));
				obj.getComissionamentoTurmaVO().setCodigo(tabelaResultado.getInt("comissionamentoTurma.codigo"));
				obj.getComissionamentoTurmaVO().setDataPrimeiroPagamento(tabelaResultado.getDate("comissionamentoTurma.dataPrimeiroPagamento"));
				obj.getComissionamentoTurmaVO().setDataUltimoPagamento(tabelaResultado.getDate("comissionamentoTurma.dataUltimoPagamento"));
				obj.getComissionamentoTurmaVO().setQtdeParcela(tabelaResultado.getInt("comissionamentoTurma.qtdeParcela"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setCodigo(tabelaResultado.getInt("configuracaoRanking.codigo"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setNome(tabelaResultado.getString("configuracaoRanking.nome"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setPercentualGerente(tabelaResultado.getDouble("configuracaoRanking.percentualGerente"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setFormulaCalculoComissao(tabelaResultado.getString("configuracaoRanking.formulaCalculoComissao"));
				vetResultado.add(obj);
				turmaAtual = tabelaResultado.getInt("turma_codigo");
			}
			if (consultorAtual.intValue() != tabelaResultado.getInt("consultor_codigo")) {
				consultorAtual = tabelaResultado.getInt("consultor_codigo");
				rankingVO = new RankingVO();
				rankingVO.getConsultor().setCodigo(tabelaResultado.getInt("consultor_codigo"));
				rankingVO.getConsultor().getPessoa().setNome(tabelaResultado.getString("consultor"));
				rankingVO.setGerente(tabelaResultado.getBoolean("gerente"));
				rankingVO.setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(tabelaResultado.getInt("qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm"));
				rankingVO.getConfiguracaoRankingVO().setCodigo(tabelaResultado.getInt("configuracaoRanking.codigo"));
				rankingVO.getConfiguracaoRankingVO().setNome(tabelaResultado.getString("configuracaoRanking.nome"));
				rankingVO.getConfiguracaoRankingVO().setPercentualGerente(tabelaResultado.getDouble("configuracaoRanking.percentualGerente"));
				rankingVO.getConfiguracaoRankingVO().setFormulaCalculoComissao(tabelaResultado.getString("configuracaoRanking.formulaCalculoComissao"));
				obj.getRankingVOs().add(rankingVO);
			}
			rankingTurmaConsultorAlunoVO = new RankingTurmaConsultorAlunoVO();
			rankingTurmaConsultorAlunoVO.getTurmaVO().setCodigo(tabelaResultado.getInt("turma_codigo"));
			rankingTurmaConsultorAlunoVO.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().setSituacao(tabelaResultado.getString("situacao"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("aluno"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("aluno_codigo"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setEmail(tabelaResultado.getString("email"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneRes(tabelaResultado.getString("telefoneRes"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setCelular(tabelaResultado.getString("celular"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneComer(tabelaResultado.getString("telefoneComer"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneRecado(tabelaResultado.getString("telefoneRecado"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("curso"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getTurno().setNome(tabelaResultado.getString("turno"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino"));
			Integer qtdeModulos = 0;
			String sqlStr = "select count(*) as qtde from (select distinct turma, disciplina from horarioturmadetalhado(" + tabelaResultado.getInt("turma_codigo") + ", null, null, null) where data < current_date) as t";
			SqlRowSet res = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (res.next()) {
				qtdeModulos = res.getInt("qtde");
			}
			// rankingTurmaConsultorAlunoVO.setValorTotalAReceberTicketMedioCRM(getFacadeFactory().getComissionamentoTurmaFacade().consultarValorTotalAReceberTicketMedioCRM(rankingTurmaConsultorAlunoVO.getMatriculaVO().getMatricula()));
			rankingTurmaConsultorAlunoVO.setMotivo(realizarVerificacaoMensagemAlunoNaoContabilizado(tabelaResultado.getBoolean("considerarRankingCrmSomenteMatriculAtivo"), tabelaResultado.getBoolean("desconsiderarRankingCrmSomenteMatriculPR"), tabelaResultado.getBoolean("desconsiderarRankingCrmAlunoBolsista"), tabelaResultado.getBoolean("considerarRankingCrmPrimeiraMensalidade"), tabelaResultado.getBoolean("considerarContratoAssinadoRankingCrm"), tabelaResultado.getInt("desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm"), tabelaResultado.getBoolean("considerarAlunoAdimplenteSemContratoAssinadoRankingCrm"), tabelaResultado.getInt("qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm"), tabelaResultado.getBoolean("bolsista"), tabelaResultado.getBoolean("primeiraMensalidadePaga"), tabelaResultado.getBoolean("contratoAssinado"), tabelaResultado.getInt("qtdeModuloAntesMatricula"), tabelaResultado.getInt("qtdeParcelaAtraso"), tabelaResultado.getString("situacao"),
					tabelaResultado.getString("situacaoMatriculaPeriodo"), tabelaResultado.getBoolean("desconsiderarContratoNaoAssinadoApartir4Meses"), tabelaResultado.getBoolean("desconsiderarParcelaEFaltaApartir3Meses"), tabelaResultado.getBoolean("tresmesesdevendo"), tabelaResultado.getBoolean("tresmesesfaltando"), qtdeModulos));
			if (rankingTurmaConsultorAlunoVO.getMotivo().trim().isEmpty()) {
				rankingVO.getRankingTurmaConsultorAlunoAptoVOs().add(rankingTurmaConsultorAlunoVO);
			} else {
				rankingVO.getRankingTurmaConsultorAlunoInaptoVOs().add(rankingTurmaConsultorAlunoVO);
			}
		}
		// realizarCalculoQuantitativoEComissao(vetResultado, usuarioVO);
		return vetResultado;
	}

	public List<RankingTurmaVO> consultarRankingTurma(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select matricula.matricula, matricula.situacao, matriculaperiodo.situacaomatriculaperiodo, pessoa.nome as aluno, pessoa.codigo as aluno_codigo, ");
		sb.append(" pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.telefoneComer, pessoa.telefoneRecado, ");
		sb.append(" curso.nome as curso, turno.nome as turno, unidadeensino.nome as unidadeensino, ");
		sb.append(" turma.codigo  as turma_codigo, turma.identificadorTurma  as turma, ");
		sb.append(" funcionario.codigo as  consultor_codigo, consultor.nome as consultor, ");
		sb.append(" matriculaperiodo.bolsista, ");
		sb.append(" considerarRankingCrmSomenteMatriculAtivo, ");
		sb.append(" desconsiderarRankingCrmSomenteMatriculPR, ");
		sb.append(" desconsiderarRankingCrmAlunoBolsista, ");
		sb.append(" considerarRankingCrmPrimeiraMensalidade, ");
		sb.append(" (select situacao in ('RE', 'NE') from contareceber where tipoorigem  = 'MEN' ");
		sb.append(" and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem and situacao = 'RE' order by dataVencimento limit 1) as primeiraMensalidadePaga, ");
		sb.append(" desconsiderarParcelaEFaltaApartir3Meses, ");

		sb.append(" (select case when qtdRec + qtdARec >= 2 then case when qtdRec > 0 then false else true end ");
		sb.append(" else false end from ( select (select count(*) as qtdRec from contareceber where tipoorigem  = 'MEN'  ");
		sb.append(" and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem and datavencimento >= (current_date - 60) and datavencimento <= current_date and situacao in ('RE', 'NE') limit 1)  as qtdRec ");
		sb.append(" , (select count(*) as qtdARec from contareceber where tipoorigem  = 'MEN' and matriculaperiodo.codigo::VARCHAR = contareceber.codorigem  ");
		sb.append(" and datavencimento >= (current_date - 60) and datavencimento <= current_date and situacao in ('AR') limit 1) as qtdARec ");
		sb.append(" from contareceber where matriculaperiodo.codigo::VARCHAR = contareceber.codorigem  limit 1) as t) as tresmesesdevendo,  ");

		sb.append(" (select qtdemodulototal >=2 and qtdemoduloposterior >= 2 as tresmesesfaltando from(select count(distinct registroaula.disciplina) as qtdemoduloposterior, qtdemodulototal,  ultimapresenca   from frequenciaaula ");
		sb.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula and frequenciaaula.matricula = matriculaperiodo.matricula and registroaula.data <= (current_date)  ");
		sb.append(" inner join (select count(distinct registroaula.disciplina) as qtdemodulototal,  max(case when presente = true then data else null end) as ultimapresenca ");
		sb.append(" from frequenciaaula inner join registroaula on registroaula.codigo = frequenciaaula.registroaula where frequenciaaula.matricula = matriculaperiodo.matricula ");
		sb.append(" and registroaula.data <= (current_date) ) as t on (registroaula.data > t.ultimapresenca or t.ultimapresenca is null) group by qtdemodulototal,  ultimapresenca) as t) as tresmesesfaltando, ");

		sb.append(" desconsiderarContratoNaoAssinadoApartir4Meses, ");
		sb.append(" considerarContratoAssinadoRankingCrm, ");
		sb.append(" (select true from documetacaomatricula inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento where documetacaomatricula.entregue = true and tipodocumento.contrato = true and documetacaomatricula.matricula = matriculaperiodo.matricula limit 1) as contratoassinado, ");
		sb.append(" qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm, ");
		sb.append(" desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm, ");
		sb.append(" count(t.data) as qtdeModuloAntesMatricula,  ");
		// sb.append(" (select count(*) from (select distinct turma, disciplina from horarioturmadetalhado(turma.codigo, null, null, null) where data < current_date) as t) as qtdeModuloJaOcorreram,  ");
		sb.append(" considerarAlunoAdimplenteSemContratoAssinadoRankingCrm, ");
		sb.append(" qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm, ");
		sb.append(" (select count(codigo) from contareceber  where datavencimento::DATE  < current_date and situacao = 'AR' and matriculaaluno = matriculaperiodo.matricula and tipoorigem in ('MEN', 'MAT')) as qtdeParcelaAtraso, ");
		sb.append(" (select codigo from funcionariocargo where funcionarioCargo.funcionario = funcionario.codigo and gerente = true limit 1) is not null as gerente, ");
		sb.append(" configuracaoRanking.codigo as \"configuracaoRanking.codigo\",  configuracaoRanking.nome as  \"configuracaoRanking.nome\", configuracaoRanking.percentualGerente as \"configuracaoRanking.percentualGerente\", configuracaoRanking.formulaCalculoComissao  as \"configuracaoRanking.formulaCalculoComissao\", ");
		sb.append(" comissionamentoTurma.codigo as \"comissionamentoTurma.codigo\", comissionamentoTurma.dataPrimeiroPagamento as \"comissionamentoTurma.dataPrimeiroPagamento\", ");
		sb.append(" comissionamentoTurma.dataUltimoPagamento as \"comissionamentoTurma.dataUltimoPagamento\", comissionamentoTurma.qtdeParcela as \"comissionamentoTurma.qtdeParcela\" ");

		sb.append(" from matriculaperiodo ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join comissionamentoTurma on comissionamentoTurma.turma = turma.codigo ");
		sb.append(" inner join configuracaoranking on comissionamentoTurma.configuracaoranking = configuracaoranking.codigo ");
		sb.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula  ");
		sb.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by ano||'/'||semestre desc limit 1  ) ");
		sb.append(" inner join curso on curso.codigo = matricula.curso  ");
		sb.append(" inner join turno on turno.codigo = matricula.turno  ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		// sb.append(" inner join configuracoes on configuracoes.codigo = unidadeensino.configuracoes ");
		// sb.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa as consultor on consultor.codigo = funcionario.pessoa ");
		sb.append(" inner join horarioturma on horarioturma.turma = turma.codigo and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre ");
		sb.append(" left join  (select max(data) as data2, horarioturma, ct.turma from horarioturmadetalhado(null, null,null, null) as ht ");
		sb.append(" inner join comissionamentoTurma ct on ct.turma = ht.turma		 ");
		sb.append(" group by disciplina, horarioturma, ct.turma) as t2 on ");
		sb.append(" t2.horarioturma = horarioturma.codigo and t2.data2 < current_date and t2.turma = turma.codigo ");
		sb.append(" left join  (select max(data) as data, horarioturma, ct.turma from horarioturmadetalhado(null, null,null, null) as ht ");
		sb.append(" inner join comissionamentoTurma ct on ct.turma = ht.turma		 ");
		sb.append(" group by disciplina, horarioturma, ct.turma) as t on ");
		sb.append(" t.horarioturma = horarioturma.codigo and t.data < matricula.data and t.turma = turma.codigo ");
		sb.append(" where to_char(comissionamentoTurma.dataUltimoPagamento, 'yy/MM') >= to_char(current_date, 'yy/MM') ");
		sb.append(" and to_char(comissionamentoTurma.dataPrimeiroPagamento, 'yy/MM') <= to_char(current_date, 'yy/MM') ");

		if (unidadeEnsino > 0) {
			sb.append(" and turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!curso.equals(0)) {
			sb.append(" AND turma.curso = ").append(curso);
		}
		if (!turma.equals(0)) {
			sb.append(" AND turma.codigo = ").append(turma);
		}

		sb.append("  group by configuracaoRanking.formulaCalculoComissao, matriculaperiodo.matricula,matricula.matricula, matricula.situacao, pessoa.nome, ");
		sb.append("  pessoa.telefoneRecado, pessoa.email, pessoa.telefoneres, pessoa.celular, pessoa.codigo, pessoa.telefoneComer, pessoa.telefoneComer, ");
		sb.append(" turma.codigo, turma.identificadorTurma, ");
		sb.append(" funcionario.codigo, consultor.nome, ");
		sb.append(" matriculaperiodo.bolsista, ");
		sb.append(" considerarRankingCrmSomenteMatriculAtivo, ");
		sb.append(" desconsiderarRankingCrmAlunoBolsista, ");
		sb.append(" considerarRankingCrmPrimeiraMensalidade, ");
		sb.append(" considerarContratoAssinadoRankingCrm,  ");
		sb.append(" qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm, ");
		sb.append(" desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm, ");
		sb.append(" matriculaperiodo.codigo, ");
		sb.append(" considerarAlunoAdimplenteSemContratoAssinadoRankingCrm, ");
		sb.append(" qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm, ");
		sb.append(" curso.nome, turno.nome, unidadeensino.nome, ");
		sb.append(" configuracaoRanking.codigo, configuracaoRanking.nome, configuracaoRanking.percentualGerente, ");
		sb.append(" comissionamentoTurma.codigo, comissionamentoTurma.dataPrimeiroPagamento, ");
		sb.append(" comissionamentoTurma.dataUltimoPagamento, comissionamentoTurma.qtdeParcela, ");
		sb.append(" desconsiderarParcelaEFaltaApartir3Meses, desconsiderarContratoNaoAssinadoApartir4Meses, matriculaperiodo.situacaomatriculaperiodo, desconsiderarrankingcrmsomentematriculpr ");
		sb.append(" order by turma, consultor.nome, pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RankingTurmaVO> vetResultado = new ArrayList<RankingTurmaVO>(0);
		RankingTurmaVO obj = null;
		RankingVO rankingVO = null;
		RankingTurmaConsultorAlunoVO rankingTurmaConsultorAlunoVO = null;
		Integer turmaAtual = 0;
		Integer consultorAtual = 0;
		while (tabelaResultado.next()) {

			if (turmaAtual.intValue() != tabelaResultado.getInt("turma_codigo")) {
				obj = new RankingTurmaVO();
				obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma_codigo"));
				obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma"));
				obj.getComissionamentoTurmaVO().setCodigo(tabelaResultado.getInt("comissionamentoTurma.codigo"));
				obj.getComissionamentoTurmaVO().setDataPrimeiroPagamento(tabelaResultado.getDate("comissionamentoTurma.dataPrimeiroPagamento"));
				obj.getComissionamentoTurmaVO().setDataUltimoPagamento(tabelaResultado.getDate("comissionamentoTurma.dataUltimoPagamento"));
				obj.getComissionamentoTurmaVO().setQtdeParcela(tabelaResultado.getInt("comissionamentoTurma.qtdeParcela"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setCodigo(tabelaResultado.getInt("configuracaoRanking.codigo"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setNome(tabelaResultado.getString("configuracaoRanking.nome"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setPercentualGerente(tabelaResultado.getDouble("configuracaoRanking.percentualGerente"));
				obj.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setFormulaCalculoComissao(tabelaResultado.getString("configuracaoRanking.formulaCalculoComissao"));
				vetResultado.add(obj);
				turmaAtual = tabelaResultado.getInt("turma_codigo");
			}
			if (consultorAtual.intValue() != tabelaResultado.getInt("consultor_codigo")) {
				consultorAtual = tabelaResultado.getInt("consultor_codigo");
				rankingVO = new RankingVO();
				rankingVO.getConsultor().setCodigo(tabelaResultado.getInt("consultor_codigo"));
				rankingVO.getConsultor().getPessoa().setNome(tabelaResultado.getString("consultor"));
				rankingVO.setGerente(tabelaResultado.getBoolean("gerente"));
				rankingVO.setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(tabelaResultado.getInt("qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm"));
				rankingVO.getConfiguracaoRankingVO().setCodigo(tabelaResultado.getInt("configuracaoRanking.codigo"));
				rankingVO.getConfiguracaoRankingVO().setNome(tabelaResultado.getString("configuracaoRanking.nome"));
				rankingVO.getConfiguracaoRankingVO().setPercentualGerente(tabelaResultado.getDouble("configuracaoRanking.percentualGerente"));
				rankingVO.getConfiguracaoRankingVO().setFormulaCalculoComissao(tabelaResultado.getString("configuracaoRanking.formulaCalculoComissao"));
				obj.getRankingVOs().add(rankingVO);
			}
			rankingTurmaConsultorAlunoVO = new RankingTurmaConsultorAlunoVO();
			rankingTurmaConsultorAlunoVO.getTurmaVO().setCodigo(tabelaResultado.getInt("turma_codigo"));
			rankingTurmaConsultorAlunoVO.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().setSituacao(tabelaResultado.getString("situacao"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("aluno"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("aluno_codigo"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setEmail(tabelaResultado.getString("email"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneRes(tabelaResultado.getString("telefoneRes"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setCelular(tabelaResultado.getString("celular"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneComer(tabelaResultado.getString("telefoneComer"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getAluno().setTelefoneRecado(tabelaResultado.getString("telefoneRecado"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("curso"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getTurno().setNome(tabelaResultado.getString("turno"));
			rankingTurmaConsultorAlunoVO.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino"));

			Integer qtdeModulos = 0;
			String sqlStr = "select count(*) as qtde from (select distinct turma, disciplina from horarioturmadetalhado(" + tabelaResultado.getInt("turma_codigo") + ", null, null, null) where data < current_date) as t";
			SqlRowSet res = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (res.next()) {
				qtdeModulos = res.getInt("qtde");
			}

			// Integer qtdeModulos =
			// tabelaResultado.getInt("qtdeModuloJaOcorreram");
			rankingTurmaConsultorAlunoVO.setValorTotalAReceberTicketMedioCRM(getFacadeFactory().getComissionamentoTurmaFacade().consultarValorTotalAReceberTicketMedioCRM(rankingTurmaConsultorAlunoVO.getMatriculaVO().getMatricula()));
			rankingTurmaConsultorAlunoVO.setMotivo(realizarVerificacaoMensagemAlunoNaoContabilizado(tabelaResultado.getBoolean("considerarRankingCrmSomenteMatriculAtivo"), tabelaResultado.getBoolean("desconsiderarRankingCrmSomenteMatriculPR"), tabelaResultado.getBoolean("desconsiderarRankingCrmAlunoBolsista"), tabelaResultado.getBoolean("considerarRankingCrmPrimeiraMensalidade"), tabelaResultado.getBoolean("considerarContratoAssinadoRankingCrm"), tabelaResultado.getInt("desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm"), tabelaResultado.getBoolean("considerarAlunoAdimplenteSemContratoAssinadoRankingCrm"), tabelaResultado.getInt("qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm"), tabelaResultado.getBoolean("bolsista"), tabelaResultado.getBoolean("primeiraMensalidadePaga"), tabelaResultado.getBoolean("contratoAssinado"), tabelaResultado.getInt("qtdeModuloAntesMatricula"), tabelaResultado.getInt("qtdeParcelaAtraso"), tabelaResultado.getString("situacao"),
					tabelaResultado.getString("situacaoMatriculaPeriodo"), tabelaResultado.getBoolean("desconsiderarContratoNaoAssinadoApartir4Meses"), tabelaResultado.getBoolean("desconsiderarParcelaEFaltaApartir3Meses"), tabelaResultado.getBoolean("tresmesesdevendo"), tabelaResultado.getBoolean("tresmesesfaltando"), qtdeModulos));
			if (rankingTurmaConsultorAlunoVO.getMotivo().trim().isEmpty()) {
				rankingVO.getRankingTurmaConsultorAlunoAptoVOs().add(rankingTurmaConsultorAlunoVO);
			} else {
				rankingVO.getRankingTurmaConsultorAlunoInaptoVOs().add(rankingTurmaConsultorAlunoVO);
			}
		}
		realizarCalculoQuantitativoEComissao(vetResultado, usuarioVO);

		return vetResultado;
	}

	public void realizarCalculoQuantitativoEComissao(List<RankingTurmaVO> rankingTurmaVOs, UsuarioVO usuarioVO) throws Exception {
		for (RankingTurmaVO rankingTurmaVO : rankingTurmaVOs) {
			Ordenacao.ordenarListaDecrescente(rankingTurmaVO.getRankingVOs(), "qtdeMatriculadoContabilizado");
			ComissionamentoTurmaFaixaValorVO comissionamentoFaixaValor = null;
			rankingTurmaVO.getComissionamentoTurmaVO().getConfiguracaoRankingVO().setPercentualVOs(getFacadeFactory().getPercentualConfiguracaoRankingFacade().consultarPorConfiguracaoRanking(rankingTurmaVO.getComissionamentoTurmaVO().getConfiguracaoRankingVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			for (RankingVO rankingVO : rankingTurmaVO.getRankingVOs()) {
				rankingTurmaVO.setQtdeAluno(rankingTurmaVO.getQtdeAluno() + rankingVO.getQtdeMatriculadoContabilizado());
				rankingTurmaVO.setQtdeAlunoDesconsiderados(rankingTurmaVO.getQtdeAlunoDesconsiderados() + rankingVO.getQtdeMatriculadoNaoContabilizado());
			}

			comissionamentoFaixaValor = getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().consultarComissionamentoPorComissinamentoTurmaFaixa(rankingTurmaVO.getComissionamentoTurmaVO().getCodigo(), rankingTurmaVO.getQtdeAluno(), usuarioVO);
			getFacadeFactory().getComissionamentoTurmaFacade().carregarDados(rankingTurmaVO.getComissionamentoTurmaVO(), usuarioVO);
			if (rankingTurmaVO.getComissionamentoTurmaVO().getConsiderarTicketMedio()) {
				comissionamentoFaixaValor.setValor((rankingTurmaVO.getComissionamentoTurmaVO().getTicketMedio() * comissionamentoFaixaValor.getQtdeInicialAluno()) * comissionamentoFaixaValor.getPercComissao().doubleValue() / 100);
			} else {
				comissionamentoFaixaValor.setValor(comissionamentoFaixaValor.getValor());
			}

			rankingTurmaVO.setValorBaseCalculo(comissionamentoFaixaValor.getValor());

			Integer qtdConsultor = rankingTurmaVO.getQtdeConsultorConsiderar().intValue();
			executarCalculoPosicaoValorRanking(comissionamentoFaixaValor.getValor(), qtdConsultor, rankingTurmaVO.getRankingVOs(), rankingTurmaVO.getComissionamentoTurmaVO().getConfiguracaoRankingVO().getPercentualVOs(), comissionamentoFaixaValor, rankingTurmaVO.getQtdeAluno());
			for (RankingVO rankingVO : rankingTurmaVO.getRankingVOs()) {
				rankingTurmaVO.setValorTotalComissao(rankingTurmaVO.getValorTotalComissao() + rankingVO.getValor());
			}
		}
	}

	public String realizarVerificacaoMensagemAlunoNaoContabilizado(Boolean considerarRankingCrmSomenteMatriculAtivo, Boolean desconsiderarRankingCrmSomenteMatriculPR, Boolean desconsiderarRankingCrmAlunoBolsista, Boolean considerarRankingCrmPrimeiraMensalidade, Boolean considerarContratoAssinadoRankingCrm, Integer desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm, Boolean considerarAlunoAdimplenteSemContratoAssinadoRankingCrm, Integer qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm, Boolean bolsista, Boolean primeiraMensalidadePaga, Boolean contratoAssinado, Integer qtdeModuloAntesMatricula, Integer qtdeParcelaAtraso, String situacao, String situacaoMatriculaPeriodo, Boolean desconsiderarMatriculaContratoNaoAssinado4Meses, Boolean desconsiderarParcelaEFaltaApartir3Meses, Boolean tresmesesdevendo, Boolean tresmesesfaltando, Integer qtdModuloJaOcorrem) {
		if (considerarRankingCrmSomenteMatriculAtivo && !situacao.equals("AT") && !situacao.equals("CO")) {
			return UteisJSF.internacionalizar("msg_Ranking_alunoNaoAtivo");
		}
		if (!desconsiderarRankingCrmSomenteMatriculPR && (situacao.equals("AT") || situacao.equals("CO")) && situacaoMatriculaPeriodo.equals("PR")) {
			return UteisJSF.internacionalizar("msg_Ranking_alunoNaoPR");
		}
		if (desconsiderarRankingCrmAlunoBolsista && bolsista) {
			return UteisJSF.internacionalizar("msg_Ranking_alunoBolsista");
		}
		if (considerarRankingCrmPrimeiraMensalidade && !primeiraMensalidadePaga) {
			return UteisJSF.internacionalizar("msg_Ranking_primeiraMensalidadeNaoRecebida");
		}
		if (desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm > 0 && qtdeModuloAntesMatricula >= desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm) {
			return UteisJSF.internacionalizar("msg_Ranking_matriculaAposConcluidoModulo").replace("{0}", qtdeModuloAntesMatricula.toString());
		}
		if (desconsiderarMatriculaContratoNaoAssinado4Meses && !contratoAssinado && qtdModuloJaOcorrem > 4) {
			return UteisJSF.internacionalizar("msg_Ranking_matriculaApos4ModulosContratoNaoAssinado");
		}
		if (desconsiderarParcelaEFaltaApartir3Meses && tresmesesdevendo && tresmesesfaltando) {
			return UteisJSF.internacionalizar("msg_Ranking_matriculaTresMesesDevendoEFaltando");
		}
		if (considerarAlunoAdimplenteSemContratoAssinadoRankingCrm && !contratoAssinado && qtdeParcelaAtraso > 0) {
			return UteisJSF.internacionalizar("msg_Ranking_contratoNaoAssinadoInadimplente").replace("{0}", qtdeParcelaAtraso.toString());
		}
		if (!contratoAssinado && qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm > 0 && qtdeParcelaAtraso >= qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm) {
			return UteisJSF.internacionalizar("msg_Ranking_contratoNaoAssinadoInadimplente").replace("{0}", qtdeParcelaAtraso.toString());
		}
		return "";
	}

	public List<RankingVO> consultarRanking(RankingTurmaVO obj, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception {
		Double valorComissao = 0.0;
		Integer qtdeConsultor = 0;
		ComissionamentoTurmaFaixaValorVO comissionamentoFaixaValor = new ComissionamentoTurmaFaixaValorVO();
		List<PercentualConfiguracaoRankingVO> listaPercentualRankingVOs = getFacadeFactory().getPercentualConfiguracaoRankingFacade().consultarPorTurmaComissionamentoTurma(obj.getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		List<RankingVO> listaRankingVO = consultarRankingConsultorPorTurmaMes(obj.getTurmaVO().getCodigo(), valorConsultaMes, usuarioVO);
		valorComissao = getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().consultarValorComissionamentoPorTurma(obj.getTurmaVO().getCodigo(), obj.getQtdeAluno(), usuarioVO);
		comissionamentoFaixaValor = getFacadeFactory().getComissionamentoTurmaFaixaValorFacade().consultarComissionamentoPorTurmaFaixa(obj.getTurmaVO().getCodigo(), obj.getQtdeAluno(), usuarioVO);
		qtdeConsultor = consultarQtdeConsultorPorTurmaMes(obj.getTurmaVO().getCodigo(), valorConsultaMes, usuarioVO);
		qtdeConsultor = qtdeConsultor + 1;
		executarCalculoPosicaoValorRanking(valorComissao, qtdeConsultor, listaRankingVO, listaPercentualRankingVOs, comissionamentoFaixaValor, obj.getQtdeAluno());
		return listaRankingVO;
	}

	/**
	 * Método Responsável por definir a posição do consultor e calcular o valor
	 * da comissão de acordo com o percentual da configuração Ranking.
	 * 
	 * @param valorComissao
	 * @param qtdeConsultor
	 * @param listaRankingVOs
	 * @param listaPercenturalRankingVOs
	 * @throws Exception
	 */
	public void executarCalculoPosicaoValorRanking(Double valorComissao, Integer qtdeConsultor, List<RankingVO> listaRankingVOs, List<PercentualConfiguracaoRankingVO> listaPercenturalRankingVOs, ComissionamentoTurmaFaixaValorVO comissionamentoFaixaValor, Integer qtdeAlunoTurma) throws Exception {

		TreeMap<Integer, List<RankingVO>> naturalOrderMap = new TreeMap<Integer, List<RankingVO>>();
		for (RankingVO rankingVO : listaRankingVOs) {
			if (naturalOrderMap.containsKey(rankingVO.getQtdeMatriculadoContabilizado())) {
				((List<RankingVO>) naturalOrderMap.get(rankingVO.getQtdeMatriculadoContabilizado())).add(rankingVO);
			} else {
				List<RankingVO> rankingVOs = new ArrayList<RankingVO>(0);
				rankingVOs.add(rankingVO);
				naturalOrderMap.put(rankingVO.getQtdeMatriculadoContabilizado(), rankingVOs);
			}
		}
		int posicao = 0;
		int qtdePosicao = 0;
		continuar: for (Integer qtdeMatricula : naturalOrderMap.descendingKeySet()) {
			posicao++;
			List<RankingVO> rankingVOs = naturalOrderMap.get(qtdeMatricula);
			qtdePosicao = rankingVOs.size();
			for (PercentualConfiguracaoRankingVO percentualConfiguracaoRankingVO : listaPercenturalRankingVOs) {
				if (percentualConfiguracaoRankingVO.getPosicao().intValue() == posicao && (percentualConfiguracaoRankingVO.getQtdePosicao().intValue() == qtdePosicao || (qtdePosicao == 1 && qtdePosicao - 1 == percentualConfiguracaoRankingVO.getQtdePosicao()))) {
					int qtdConsultPosicao = 0;
					for (RankingVO rankingVO : rankingVOs) {
						rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao() + "º");
						if (rankingVO.getQtdeMatriculadoContabilizado() >= rankingVO.getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm()) {
							qtdConsultPosicao++;
							ScriptEngineManager factory = new ScriptEngineManager();
							// create a JavaScript engine
							ScriptEngine engine = factory.getEngineByName("JavaScript");
							// evaluate JavaScript code from String
							if (rankingVO.getConfiguracaoRankingVO().getFormulaCalculoComissao().trim().isEmpty()) {
								throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_formulaCalculoComissaoNaoConfigurado"));
							}
							try {
								String formula = rankingVO.getConfiguracaoRankingVO().getFormulaCalculoComissao();
								for (TagFormulaConfiguracaoRankingEnum tag : TagFormulaConfiguracaoRankingEnum.values()) {
									switch (tag) {
									case PERC_RANKING:
										formula = formula.replaceAll(tag.name(), percentualConfiguracaoRankingVO.getPercentual().toString());
										break;
									case QTDE_ALUNO:
										formula = formula.replaceAll(tag.name(), rankingVO.getQtdeMatriculadoContabilizado().toString());
										break;
									case QTDE_CONSULTOR:
										formula = formula.replaceAll(tag.name(), qtdeConsultor.toString());
										break;
									case VALOR_BASE:
										formula = formula.replaceAll(tag.name(), valorComissao.toString());
										break;
									default:
										break;
									}
								}
								Object result = engine.eval(formula);
								if (result instanceof Number) {
									rankingVO.setValor(((Number) result).doubleValue());
								} else {
									rankingVO.setValor(0.0);
								}
							} catch (Exception e) {
								throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_formulaCalculoInvalido"));
							}

							// rankingVO.setValor(Uteis.arrendondarForcando2CadasDecimais(executarCalculoValorComissao(valorComissao,
							// qtdeConsultor,
							// percentualConfiguracaoRankingVO.getPercentual(),
							// rankingVO.getConfiguracaoRankingVO().getPercentualGerente(),
							// rankingVO.getGerente())));
						} else {
							rankingVO.setValor(0.0);
							rankingVO.setPosicao(percentualConfiguracaoRankingVO.getPosicao() + "º ");
						}
						rankingVO.setQtdeInicialAluno(comissionamentoFaixaValor.getQtdeInicialAluno());
						rankingVO.setQtdeFinalAluno(comissionamentoFaixaValor.getQtdeFinalAluno());
						// if(rankingVO.getGerente()){
						// rankingVO.setValorComissao(rankingVO.getConfiguracaoRankingVO().getPercentualGerente());
						// }else{
						rankingVO.setValorComissao(percentualConfiguracaoRankingVO.getPercentual());
						// }
						rankingVO.setQtdeAlunoTurma(qtdeAlunoTurma);
					}
					if (qtdConsultPosicao > 1) {
						posicao = posicao + (qtdConsultPosicao - 1);
					}
					continue continuar;
				}
			}
			for (RankingVO rankingVO : rankingVOs) {
				rankingVO.setPosicao("Posição " + posicao + "/" + qtdePosicao + " não configurada.");
			}

		}
	}

	/**
	 * Método responsavel por calcular o valor da Comissão do Consultor
	 * 
	 * @param valorComissao
	 * @param qtdeConsultor
	 * @param percentual
	 * @param percentualGerente
	 * @param gerente
	 * @return
	 * @throws Exception
	 */
	public Double executarCalculoValorComissao(Double valorComissao, Integer qtdeConsultor, Double percentual, Double percentualGerente, Boolean gerente) throws Exception {
		Double valor = 0.0;
		Double valorTemp = 0.0;
		if (!valorComissao.equals(0.0)) {
			if (gerente) {
				if (percentualGerente.equals(0.0)) {
					throw new Exception("Problema com a Configuração do Ranking, o campo PERCENTUAL GERENTE está zerado.");
				}
				String perc = percentual.toString().substring(percentual.toString().indexOf(".") + 1);
				String percGerente = percentualGerente.toString().substring(percentualGerente.toString().indexOf(".") + 1);
				Integer somaPerc = Integer.parseInt(perc) + Integer.parseInt(percGerente);
				Double percGerenteFinal = Double.parseDouble("1." + somaPerc);

				valorTemp = valorComissao / qtdeConsultor;
				valor = valorTemp * percGerenteFinal;
				return valor;
			} else {
				valorTemp = valorComissao / qtdeConsultor;
				valor = valorTemp * percentual;
				return valor;
			}
		}
		return 0.0;
	}

	public Integer consultarQtdeConsultorPorTurmaMes(Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(DISTINCT codigo) AS qtdeConsultor FROM (");
		sb.append(" SELECT DISTINCT (pessoa.codigo) ");
		sb.append(" FROM comissionamentoTurma  ");
		sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
		sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
		sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
		sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
		sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
		sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sb.append(" WHERE turma.codigo = ").append(turma);
		sb.append(" AND (matriculaperiodo.situacaoMatriculaperiodo = 'AT' or matriculaperiodo.situacaoMatriculaperiodo = 'PR') and matriculaperiodo.bolsista = false  ");
		sb.append(" AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ");
		// sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
		// sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
		sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
		sb.append(") AS t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtdeConsultor");
		}
		return 0;
	}

	public List<RankingVO> consultarRankingConsultorPorConsultorMes(Integer consultorPessoa, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT turma.identificadorTurma, turma.codigo AS \"turma.codigo\", pessoa.nome, pessoa.codigo AS \"pessoa.codigo\",  ");
		sb.append(" configuracaoRanking.codigo AS \"configuracaoRanking.codigo\", configuracaoRanking.nome AS \"configuracaoRanking.nome\", count(pessoa.codigo) AS qtdeMatriculado,  ");
		sb.append(" configuracaoRanking.percentualGerente AS \"configuracaoRanking.percentualGerente\", funcionariocargo.gerente ");
		sb.append(" FROM comissionamentoTurma  ");
		sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
		sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
		sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
		sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
		sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
		sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND (matriculaperiodo.situacaoMatriculaperiodo = 'AT' or matriculaperiodo.situacaoMatriculaperiodo = 'PR') and matriculaperiodo.bolsista = false  ");
		sb.append(" AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ");
		// sb.append(" WHERE EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
		if (!consultorPessoa.equals(0)) {
			sb.append(" AND pessoa.codigo = ").append(consultorPessoa);
		}
		// sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
		sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
		sb.append(" GROUP BY turma.identificadorTurma, pessoa.nome, pessoa.codigo, turma.codigo, configuracaoRanking.codigo, configuracaoRanking.nome, funcionariocargo.gerente, configuracaoRanking.percentualGerente ");
		sb.append(" ORDER BY pessoa.nome, pessoa.codigo, qtdeMatriculado DESC ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public List<RankingVO> consultarRankingConsultorPorTurmaMes(Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT turma.identificadorTurma, turma.codigo AS \"turma.codigo\", pessoa.nome, pessoa.codigo AS \"pessoa.codigo\",  ");
		sb.append(" configuracaoRanking.codigo AS \"configuracaoRanking.codigo\", configuracaoRanking.nome AS \"configuracaoRanking.nome\", count(pessoa.codigo) AS qtdeMatriculado,  ");
		sb.append(" configuracaoRanking.percentualGerente AS \"configuracaoRanking.percentualGerente\", funcionariocargo.gerente ");
		sb.append(" FROM comissionamentoTurma  ");
		sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = comissionamentoTurma.configuracaoRanking ");
		sb.append(" INNER JOIN turma ON turma.codigo = comissionamentoTurma.turma ");
		sb.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.turma = turma.codigo ");
		sb.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" INNER JOIN funcionario ON funcionario.codigo = matricula.consultor ");
		sb.append(" INNER JOIN funcionariocargo ON funcionarioCargo.funcionario = funcionario.codigo ");
		sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sb.append(" WHERE turma.codigo = ").append(turma);
		sb.append(" AND (matriculaperiodo.situacaoMatriculaperiodo = 'AT' or matriculaperiodo.situacaoMatriculaperiodo = 'PR') and matriculaperiodo.bolsista = false  ");
		sb.append(" AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ");
		// sb.append(" AND EXTRACT(MONTH FROM matricula.data) = ").append(Uteis.getMesReferencia(valorConsultaMes.toUpperCase()));
		// sb.append(" AND matricula.data >= comissionamentoTurma.dataPrimeiroPagamento and matricula.data <= comissionamentoTurma.dataUltimoPagamento ");
		// sb.append(" AND (funcionarioCargo.consultor = true OR funcionariocargo.gerente = true) ");
		sb.append(" GROUP BY turma.identificadorTurma, pessoa.nome, pessoa.codigo, turma.codigo, configuracaoRanking.codigo, configuracaoRanking.nome, funcionariocargo.gerente, configuracaoRanking.percentualGerente ");
		sb.append(" ORDER BY qtdeMatriculado DESC ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuarioVO));
		}
		return vetResultado;
	}

	public RankingVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		RankingVO obj = new RankingVO();
		obj.getRankingTurmaVO().getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getRankingTurmaVO().getTurmaVO().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.getConsultor().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getConsultor().getPessoa().setNome(dadosSQL.getString("nome"));
		obj.getConfiguracaoRankingVO().setCodigo(dadosSQL.getInt("configuracaoRanking.codigo"));
		obj.getConfiguracaoRankingVO().setNome(dadosSQL.getString("configuracaoRanking.nome"));
		obj.getConfiguracaoRankingVO().setPercentualGerente(dadosSQL.getDouble("configuracaoRanking.percentualGerente"));
		obj.setGerente(dadosSQL.getBoolean("gerente"));
		// obj.setQtdeMatriculado(dadosSQL.getInt("qtdeMatriculado"));
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ComissionamentoTurma.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ComissionamentoTurma.idEntidade = idEntidade;
	}
}