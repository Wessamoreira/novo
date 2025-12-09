package relatorio.negocio.jdbc.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AtividadeComplementarRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EventoAtividadeComplementarVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.interfaces.academico.AtividadeComplementarRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AtividadeComplementarRel extends SuperRelatorio implements AtividadeComplementarRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3074127084237128596L;

	@Override
	public List<AtividadeComplementarRelVO> criarObjetoLayoutAtividadeComplementarAnalitico(CursoVO curso, TurmaVO turma, String matricula, UnidadeEnsinoVO unidade, String ano, String semestre, String situacao, Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado, String filtro) throws Exception {
		this.validarDados(unidade, curso, turma, matricula, filtro, ano, semestre);
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(" select matricula.matricula, pessoa.nome, gradecurricular.totalcargahorariaatividadecomplementar  as cargaHorariaExigida,  ");
			sql.append(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().getSqlTotalHorasConsideradas()).append("  as totalHorasConsideradas,		");
			sql.append(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().getSqlTotalHorasRealizadas()).append("  as cargaHorariaRealizadaAtividadeComplementar,		");
			sql.append(" turma.identificadorturma, curso.nome as curso, gradecurricular.codigo, ");
			sql.append(" tipoAtividadeComplementar.nome as tipoAtividadeComplementarNome, ");
			sql.append(" tipoAtividadeComplementar.codigo as tipoAtividadeComplementarCodigo, ");
			sql.append(" registroatividadecomplementar.nomeevento, ");
			sql.append(" registroatividadecomplementar.data, responsavelalteracao.nome as responsavelUltimaAlteracao, registroatividadecomplementar.dataUltimaAlteracao, ");
			sql.append(" registroatividadecomplementarmatricula.responsaveldeferimentoindeferimento,registroatividadecomplementarmatricula.datadeferimentoindeferimento, registroatividadecomplementarmatricula.justificativaalteracaochconsiderada, ");
			sql.append(" registroatividadecomplementarmatricula.responsaveleditarCHconsiderada,registroatividadecomplementarmatricula.dataeditarCHconsiderada, ");
			sql.append(" registroatividadecomplementarmatricula.cargahorariaevento,registroatividadecomplementarmatricula.observacao, ");
			sql.append(" registroatividadecomplementarmatricula.cargahorariaconsiderada, ");
			sql.append(" registroatividadecomplementarmatricula.codigo as \"registroatividadecomplementarmatricula.codigo\", ");
			sql.append(" gradecurriculartipoatividadecomplementar.cargahoraria as cargahorariamaximatipoatividade, ");
			sql.append(" gradecurriculartipoatividadecomplementar.horasminimasexigida as horasminimasexigidatipoatividade, ");
			sql.append(" arquivo.codigo as arquivoCodigo, arquivo.nome as arquivoNome, arquivo.pastaBaseArquivo as pastaBaseArquivo, arquivo.servidorArquivoOnline as servidorArquivoOnline ");
			sql.append(" from matricula  INNER JOIN pessoa on pessoa.codigo = matricula.aluno  ");
			sql.append(" INNER JOIN curso on matricula.curso = curso.codigo  ");
			
			sql.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo  in (");
			sql.append(" select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");			
			if(ano != null &&!ano.trim().isEmpty()){
				sql.append(" and mp.ano = '").append(ano).append("' ");
			}
			if(semestre != null && !semestre.trim().isEmpty()){
				sql.append(" and mp.semestre = '").append(semestre).append("' ");
			}
			sql.append(" and mp.situacaomatriculaperiodo not in ('PC') order by ano||'/'||semestre desc limit 1)  ");
			sql.append(" INNER JOIN turma on matriculaperiodo.turma = turma.codigo  ");
			sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual   ");
			sql.append(" left JOIN registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.matricula = matricula.matricula ");
			sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' "); 
			sql.append(" left JOIN registroatividadecomplementar on registroatividadecomplementar.codigo = registroatividadecomplementarmatricula.registroatividadecomplementar ");
			sql.append(" left JOIN tipoAtividadeComplementar on tipoAtividadeComplementar.codigo = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
			sql.append(" LEFT JOIN gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual ");
			sql.append(" left join arquivo on arquivo.codigo = registroatividadecomplementarmatricula.arquivo ");
			sql.append(" LEFT JOIN usuario as responsavel on responsavel.codigo = registroatividadecomplementar.responsavelUltimaAlteracao ");
			sql.append(" left join pessoa as responsavelalteracao on responsavelalteracao.codigo = responsavel.pessoa ");
			sql.append(obterClausulaWhere(curso, turma, matricula, unidade, ano, semestre, situacao, ativo, trancado, cancelado, abandonado, formado, transferenciaInterna, transferenciaExterna, preMatricula, preMatriculaCancelada, concluido, jubilado, filtro));
			sql.append(" group by matricula.matricula, pessoa.nome, gradecurricular.codigo, turma.identificadorturma, curso.nome  , ");
			sql.append(" tipoAtividadeComplementar.nome, registroatividadecomplementar.nomeevento, registroatividadecomplementarmatricula.cargahorariaevento, registroatividadecomplementarmatricula.cargahorariaconsiderada,gradecurriculartipoatividadecomplementar.horasminimasexigida, ");
			sql.append(" registroatividadecomplementarmatricula.codigo, registroatividadecomplementar.data, tipoAtividadeComplementar.codigo, gradecurriculartipoatividadecomplementar.cargahoraria, ");
			sql.append(" arquivo.codigo, arquivo.nome, responsavelalteracao.nome, registroatividadecomplementar.dataUltimaAlteracao, arquivo.pastaBaseArquivo, arquivo.servidorArquivoOnline ");
			sql.append(" order by pessoa.nome, matricula.matricula, registroatividadecomplementar.data ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<AtividadeComplementarRelVO> listaRetorno = new ArrayList<AtividadeComplementarRelVO>(0);
			AtividadeComplementarRelVO obj = null;
			String matriculaTmp = "";
			Integer totalCargaHorariaConsiderada = 0;
			Integer totalCargaHorariaConsideradaTipoAtividade = 0;
			Integer tipoAtividadeComplementar = 0;
			Map<Integer, Integer> mapTipoAtividadeCargaHorariaConsiderar = new HashMap<Integer, Integer>(0);
			while (rs.next()) {
				if (matriculaTmp.equals("") || !matriculaTmp.equals(rs.getString("matricula"))) {
					totalCargaHorariaConsiderada = 0;
					obj = montarDadosAtividadeComplementarRel(rs);
					totalCargaHorariaConsiderada = obj.getTotalHorasConsideradas();
					matriculaTmp = obj.getMatricula();
					listaRetorno.add(obj);
				}

				if (tipoAtividadeComplementar == 0 || !tipoAtividadeComplementar.equals(rs.getInt("tipoAtividadeComplementarCodigo"))) {
					if (tipoAtividadeComplementar > 0) {
						mapTipoAtividadeCargaHorariaConsiderar.put(tipoAtividadeComplementar, totalCargaHorariaConsideradaTipoAtividade);
					}
					tipoAtividadeComplementar = rs.getInt("tipoAtividadeComplementarCodigo");
					if (mapTipoAtividadeCargaHorariaConsiderar.containsKey(tipoAtividadeComplementar)) {
						totalCargaHorariaConsideradaTipoAtividade = mapTipoAtividadeCargaHorariaConsiderar.get(tipoAtividadeComplementar);
					} else {
						totalCargaHorariaConsideradaTipoAtividade = rs.getInt("cargahorariamaximatipoatividade");
					}
				}
				if (totalCargaHorariaConsiderada < totalCargaHorariaConsideradaTipoAtividade) {
					totalCargaHorariaConsideradaTipoAtividade = totalCargaHorariaConsiderada;
				}
				EventoAtividadeComplementarVO eventoAtividadeComplementarVO = montarDadosEventoAtividadeComplementarVO(rs, totalCargaHorariaConsideradaTipoAtividade);
				if(Uteis.isAtributoPreenchido(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar())){
					obj.getListaEventoAtividadeComplementar().add(eventoAtividadeComplementarVO);
				}
				if (totalCargaHorariaConsideradaTipoAtividade > eventoAtividadeComplementarVO.getCargaHorariaConsiderada()) {
					totalCargaHorariaConsideradaTipoAtividade = totalCargaHorariaConsideradaTipoAtividade - eventoAtividadeComplementarVO.getCargaHorariaConsiderada();
				} else {
					totalCargaHorariaConsideradaTipoAtividade = 0;
				}
				if (totalCargaHorariaConsiderada > eventoAtividadeComplementarVO.getCargaHorariaConsiderada()) {
					totalCargaHorariaConsiderada = totalCargaHorariaConsiderada - eventoAtividadeComplementarVO.getCargaHorariaConsiderada();
				} else {
					totalCargaHorariaConsiderada = 0;
				}

			}
			for (AtividadeComplementarRelVO atividadeComplementarRelVO : listaRetorno) {
				Ordenacao.ordenarLista(atividadeComplementarRelVO.getListaEventoAtividadeComplementar(), "dataEvento");
			}

			return listaRetorno;
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarDados(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, String matricula, String filtro, String ano, String semestre) throws Exception {
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("É necessário informar a UNIDADE DE ENSINO para geração desse relatório.");
		}

		if (curso != null && curso.getCodigo() == 0 && turma.getCodigo() == 0 && matricula.trim().equals("") && filtro.equals("")) {
			throw new ConsistirException("É necessário informar ao menos um CURSO, ou uma MATRÍCULA ou uma TURMA para geração desse relatório.");
		}

		if (curso != null && curso.getNome().equals("") && filtro.equals("curso")) {
			throw new ConsistirException("É necessário informar o CURSO  para geração desse relatório.");
		}

		if (turma != null && turma.getIdentificadorTurma().equals("") && filtro.equals("turma")) {
			throw new ConsistirException("É necessário informar a TURMA  para geração desse relatório.");
		}

		if (matricula.trim().equals("") && filtro.equals("matricula")) {
			throw new ConsistirException("É necessário informar a MATRÍCULA  para geração desse relatório.");
		}
		
		if (Uteis.isAtributoPreenchido(curso)) {
			if ((curso.getPeriodicidade().equals("AN") || curso.getPeriodicidade().equals("SE")) && !Uteis.isAtributoPreenchido(ano)) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			} 
			if(curso.getPeriodicidade().equals("SE") && !Uteis.isAtributoPreenchido(semestre)) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");		
			}
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			if ((turma.getAnual() || turma.getSemestral()) && !Uteis.isAtributoPreenchido(ano)) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			} 
			if(turma.getSemestral() && !Uteis.isAtributoPreenchido(semestre)) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");		
			}
		}

	}

	private String obterClausulaWhere(CursoVO curso, TurmaVO turma, String matricula, UnidadeEnsinoVO unidade, String ano, String semestre, String situacao, Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado, String filtro) {
		StringBuilder sql = new StringBuilder("");
		sql.append("  WHERE 1=1 ");
		if (matricula != null && !matricula.trim().equals("")) {
			sql.append(" and matricula.matricula = '").append(matricula.trim()).append("' ");
		}
		if (turma != null && turma.getCodigo() != null && turma.getCodigo().intValue() > 0) {
			sql.append(" and turma.codigo = ").append(turma.getCodigo());

		}

		if (curso != null && curso.getCodigo() != null && curso.getCodigo().intValue() > 0) {
			sql.append(" and curso.codigo = ").append(curso.getCodigo());
		}

		if (unidade != null && unidade.getCodigo() != null && unidade.getCodigo().intValue() > 0) {
			sql.append(" and matricula.unidadeensino = ").append(unidade.getCodigo());
		}

		if (ano != null && !ano.equals("")) {
			sql.append(" and matriculaperiodo.ano = '").append(ano).append("'");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("'");
		}

		String situacaoMatricula = this.adicionarFiltrosSituacaoAcademica(ativo, trancado, cancelado, abandonado, formado, transferenciaInterna, transferenciaExterna, preMatricula, preMatriculaCancelada, concluido, jubilado);

		if (Uteis.isAtributoPreenchido(situacaoMatricula) && curso.getPeriodicidade().equals("IN")) {
			sql.append(" AND matricula.situacao IN (" + situacaoMatricula + ")");

		} else if (Uteis.isAtributoPreenchido(situacaoMatricula)) {
			sql.append(" AND matriculaperiodo.situacaomatriculaperiodo IN (" + situacaoMatricula + ")");
		}
		
		
		if (situacao != null && situacao.equals("Concluido")) {
			sql.append("AND  (select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ( ");
			sql.append(" 		select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
			sql.append(" 		registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
			sql.append(" 		gradecurriculartipoatividadecomplementar.cargahoraria ");
			sql.append(" 		from registroatividadecomplementarmatricula ");
			sql.append(" 		inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
			sql.append(" 		where matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual");
			sql.append(" 		group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
			sql.append(" 		gradecurriculartipoatividadecomplementar.cargahoraria ");
			sql.append(" 		) as t) >= gradecurricular.totalcargahorariaatividadecomplementar ");
		} else if (situacao != null && situacao.equals("Pendencia")) {
			sql.append("AND  (select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ( ");
			sql.append(" 		select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
			sql.append(" 		registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
			sql.append(" 		gradecurriculartipoatividadecomplementar.cargahoraria ");
			sql.append(" 		from registroatividadecomplementarmatricula ");
			sql.append(" 		inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
			sql.append(" 		where matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual");
			sql.append(" 		group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
			sql.append(" 		gradecurriculartipoatividadecomplementar.cargahoraria ");
			sql.append(" 		) as t) < gradecurricular.totalcargahorariaatividadecomplementar ");
		}

		return sql.toString();
	}

	@Override
	public List<AtividadeComplementarRelVO> criarObjetoLayoutAtividadeComplementarSintetico(CursoVO curso, TurmaVO turma, String matricula, UnidadeEnsinoVO unidade, String ano, String semestre, String situacao, Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado,  String filtro) throws Exception {
		this.validarDados(unidade, curso, turma, matricula, filtro, ano, semestre);
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append("  select distinct matricula.matricula, pessoa.nome, gradecurricular.totalcargahorariaatividadecomplementar  as cargaHorariaExigida,  ");
			sql.append(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().getSqlTotalHorasConsideradas()).append("  as totalHorasConsideradas,		");
			sql.append(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().getSqlTotalHorasRealizadas()).append("  as cargaHorariaRealizadaAtividadeComplementar,		");
			sql.append("  turma.identificadorturma, curso.nome as curso, gradecurricular.codigo ");
			sql.append("  from matricula  INNER JOIN pessoa on pessoa.codigo = matricula.aluno  ");
			sql.append("  INNER JOIN curso on matricula.curso = curso.codigo  ");
			sql.append("  INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo  in (");
			sql.append(" select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");			
			if(!ano.trim().isEmpty()){
				sql.append(" and mp.ano = '").append(ano).append("' ");
			}
			if(!semestre.trim().isEmpty()){
				sql.append(" and mp.semestre = '").append(semestre).append("' ");
			}
			sql.append(" and mp.situacaomatriculaperiodo not in ('PC') order by ano||'/'||semestre desc limit 1)  ");
			sql.append("  INNER JOIN turma on matriculaperiodo.turma = turma.codigo  ");
			sql.append("  INNER JOIN gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual   ");
			sql.append(obterClausulaWhere(curso, turma, matricula, unidade, ano, semestre, situacao, ativo, trancado, cancelado, abandonado, formado, transferenciaInterna, transferenciaExterna, preMatricula, preMatriculaCancelada, concluido, jubilado, filtro));

			sql.append(" group by matricula.matricula, pessoa.nome, gradecurricular.codigo, turma.identificadorturma, curso.nome   ");
			sql.append(" order by pessoa.nome, matricula.matricula ");

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			List<AtividadeComplementarRelVO> listaRetorno = new ArrayList<AtividadeComplementarRelVO>(0);

			while (rs.next()) {
				listaRetorno.add(montarDadosAtividadeComplementarRel(rs));
			}
			return listaRetorno;
		} catch (Exception e) {
			throw e;
		}
	}

	public AtividadeComplementarRelVO montarDadosAtividadeComplementarRel(SqlRowSet rs) {
		AtividadeComplementarRelVO obj = new AtividadeComplementarRelVO();
		obj.setMatricula(rs.getString("matricula"));
		obj.setNomeAluno(rs.getString("nome"));
		obj.setQtdHorasExigidas(rs.getInt("cargaHorariaExigida"));
		obj.setTurma(rs.getString("identificadorturma"));
		obj.setCurso(rs.getString("curso"));
		obj.setGradeCurricular(rs.getInt("codigo"));
		obj.setQtdHorasRealizadas(rs.getInt("cargaHorariaRealizadaAtividadeComplementar"));
		if (rs.getInt("cargaHorariaExigida") < rs.getInt("totalHorasConsideradas")) {
			obj.setTotalHorasConsideradas(rs.getInt("cargaHorariaExigida"));
		} else {
			obj.setTotalHorasConsideradas(rs.getInt("totalHorasConsideradas"));
		}
		return obj;
	}

	public EventoAtividadeComplementarVO montarDadosEventoAtividadeComplementarVO(SqlRowSet rs, Integer totalCargaHorariaConsiderada) {
		EventoAtividadeComplementarVO obj = new EventoAtividadeComplementarVO();
		obj.setAtividadeComplementar(rs.getString("tipoAtividadecomplementarnome"));
		obj.setCodigoTipoAtividadeComplementar(rs.getInt("tipoAtividadeComplementarCodigo"));
		obj.setNomeEvento(rs.getString("nomeevento"));
		obj.setCargaHorariaEvento(rs.getInt("cargahorariaevento"));
		obj.setCargaHorariaRealizada(rs.getInt("cargahorariaconsiderada"));
		obj.setDataEvento(rs.getDate("data"));
		obj.setCargaHorariaMaximaConsiderada(rs.getInt("cargahorariamaximatipoatividade"));
		obj.getArquivoVO().setCodigo(rs.getInt("arquivoCodigo"));
		obj.getArquivoVO().setNome(rs.getString("arquivoNome"));
		obj.getArquivoVO().setPastaBaseArquivo(rs.getString("pastaBaseArquivo"));
		if(rs.getObject("servidorArquivoOnline") != null) {
		obj.getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(rs.getString("servidorArquivoOnline")));
		}
		if (totalCargaHorariaConsiderada > rs.getInt("cargahorariaconsiderada")) {
			obj.setCargaHorariaConsiderada(rs.getInt("cargahorariaconsiderada"));
		} else if (totalCargaHorariaConsiderada > 0) {
			obj.setCargaHorariaConsiderada(totalCargaHorariaConsiderada);
		}
		obj.setResponsavelUltimaAlteracao(rs.getString("responsavelUltimaAlteracao"));
		obj.setDataUltimaAlteracao(rs.getTimestamp("dataUltimaAlteracao"));
		obj.setObservacao(rs.getString("observacao"));
		obj.getRegistroAtividadeComplementarMatriculaVO().setCodigo(rs.getInt("registroatividadecomplementarmatricula.codigo"));
		obj.getRegistroAtividadeComplementarMatriculaVO().setObservacao(rs.getString("observacao"));
		obj.getRegistroAtividadeComplementarMatriculaVO().getResponsavelDeferimentoIndeferimento().setCodigo(rs.getInt("responsaveldeferimentoindeferimento"));
		obj.getRegistroAtividadeComplementarMatriculaVO().setDataDeferimentoIndeferimento(rs.getTimestamp("datadeferimentoindeferimento"));
		obj.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().setCodigo(rs.getInt("responsaveleditarCHconsiderada"));
		obj.getRegistroAtividadeComplementarMatriculaVO().setDataEditarCHConsiderada(rs.getTimestamp("dataeditarCHconsiderada"));
		obj.getRegistroAtividadeComplementarMatriculaVO().setJustificativaAlteracaoCHConsiderada(rs.getString("justificativaalteracaochconsiderada"));
		return obj;
	}

	// public List<EventoAtividadeComplementarVO>
	// consultarEventosRealizados(String matricula) throws Exception {
	// StringBuilder sqlStr = new StringBuilder();
	// sqlStr.append("  select tipoAtividadeComplementar.nome as tipoAtividadeComplementarNome, registroatividadecomplementar.nomeevento, ");
	// sqlStr.append("  registroatividadecomplementarmatricula.cargahorariaevento  from matricula ");
	// sqlStr.append("  INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
	// sqlStr.append("  INNER JOIN curso on matricula.curso = curso.codigo ");
	// sqlStr.append("  INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo ");
	// sqlStr.append("  in (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by ano||'/'||semestre desc limit 1) ");
	// sqlStr.append("  INNER JOIN turma on matriculaperiodo.turma = turma.codigo ");
	// sqlStr.append("  INNER JOIN gradecurricular on gradecurricular.codigo = matriculaperiodo.gradecurricular ");
	// sqlStr.append("  INNER JOIN registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.matricula = matricula.matricula ");
	// sqlStr.append("  INNER JOIN registroatividadecomplementar on registroatividadecomplementar.codigo = registroatividadecomplementarmatricula.registroatividadecomplementar ");
	// sqlStr.append("  INNER JOIN tipoAtividadeComplementar on tipoAtividadeComplementar.codigo = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
	// sqlStr.append("  WHERE matricula.matricula = '").append(matricula).append("' ");
	// sqlStr.append("  group by tipoAtividadeComplementar.nome, registroatividadecomplementar.nomeevento, registroatividadecomplementarmatricula.cargahorariaevento ");
	// sqlStr.append("  order by registroatividadecomplementar.nomeevento  ");
	//
	// SqlRowSet rs =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// List<EventoAtividadeComplementarVO> listaRetorno = new ArrayList(0);
	// while (rs.next()) {
	// EventoAtividadeComplementarVO obj = new EventoAtividadeComplementarVO();
	// obj.setAtividadeComplementar(rs.getString("tipoAtividadecomplementarnome"));
	// obj.setNomeEvento(rs.getString("tipoAtividadecomplementarnome"));
	// obj.setCargaHorariaEvento(rs.getInt("cargahorariaevento"));
	// listaRetorno.add(obj);
	//
	// }
	// return listaRetorno;
	//
	// }

	// public Integer consultarCargaHorariaRealizada(String matricula, Integer
	// matrizCurricular) throws Exception {
	// StringBuilder sqlStr = new StringBuilder();
	// sqlStr.append(" select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ( ");
	// sqlStr.append(" select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
	// sqlStr.append(" registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
	// sqlStr.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
	// sqlStr.append(" from registroatividadecomplementarmatricula ");
	// sqlStr.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
	// sqlStr.append(" where matricula = '").append(matricula).append("' and gradecurriculartipoatividadecomplementar.gradecurricular = ").append(matrizCurricular);
	// sqlStr.append(" group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
	// sqlStr.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
	// sqlStr.append(" ) as t ");
	//
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// if (tabelaResultado.next()) {
	// return
	// tabelaResultado.getInt("cargaHorariaRealizadaAtividadeComplementar");
	// }
	//
	// return 0;
	// }
	//
	// public Integer consultarTotalCargaHorariaConsiderada(String matricula)
	// throws Exception {
	// StringBuilder sqlStr = new StringBuilder();
	// sqlStr.append(" select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsideradas from registroatividadecomplementarmatricula ");
	// sqlStr.append(" where matricula = '").append(matricula + "'");
	//
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// if (tabelaResultado.next()) {
	// return tabelaResultado.getInt("totalHorasConsideradas");
	// }
	//
	// return 0;
	// }

	public String adicionarFiltrosSituacaoAcademica(Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado) {
		String retorno = "";

		if (ativo != null && ativo) {
			retorno += "'AT',";
		}
		if (preMatricula != null && preMatricula) {
			retorno += " 'PR',";
		}
		if (preMatriculaCancelada != null && preMatriculaCancelada) {
			retorno += " 'PC',";
		}
		if (trancado != null && trancado) {
			retorno += " 'TR',";
		}
		if (concluido != null && concluido) {
			retorno += " 'FI',";
		}
		if (cancelado != null && cancelado) {
			retorno += " 'CA',";
		}

		if (abandonado != null && abandonado) {
			retorno += " 'AC',";
		}

		if (formado != null && formado) {
			retorno += " 'FO',";
		}
		
		if (jubilado != null && jubilado) {
			retorno += " 'JU',";
		}

		if (transferenciaExterna != null && transferenciaExterna) {
			retorno += " 'TS',";
		}

		if (transferenciaInterna != null && transferenciaInterna) {
			retorno += " 'TI',";
		}

		if (retorno.equals("")) {
			return retorno;
		} else {
			return retorno.substring(0, retorno.length() - 1);
		}

	}
}
