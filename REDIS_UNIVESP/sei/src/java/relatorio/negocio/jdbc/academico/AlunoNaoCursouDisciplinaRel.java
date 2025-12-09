/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.joining;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.AlunoNaoCursouDisciplinaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Otimize-Not
 */
@Service
@Scope
@Lazy
public class AlunoNaoCursouDisciplinaRel extends SuperRelatorio implements AlunoNaoCursouDisciplinaRelInterfaceFacade {

    public AlunoNaoCursouDisciplinaRel() {
    }

    @Override
    public void validarDados(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws ConsistirException {
    	alunoNaoCursouDisciplinaFiltroRelVO.getUnidadeEnsinoVOs().stream()
    		.filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).findAny().orElseThrow(() -> new ConsistirException("Deve ser selecionada uma Unidade de Ensino (Aluno Não Cursou Disciplina)."));
    	
    	alunoNaoCursouDisciplinaFiltroRelVO.getCursoVOs().stream()
    		.filter(CursoVO::getFiltrarCursoVO).findAny().orElseThrow(() -> new ConsistirException("Deve ser selecionado um Curso (Aluno Não Cursou Disciplina)."));
    	
    	if (!alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoAnual() && !alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral() && !alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoSemestral()) {
    		throw new ConsistirException("Deve ser selecionada uma periodicidade");
    	}
    	
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral() && alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseInicio().isEmpty()) {
    		throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunoNaoCursouDisciplinaRel_periodoInicio"));
    	}
    	
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral() && alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseFim().isEmpty()) {
    		throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunoNaoCursouDisciplinaRel_periodoFim"));
    	}
    	
    	if ((alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoAnual() || alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoSemestral()) && alunoNaoCursouDisciplinaFiltroRelVO.getAno().isEmpty()) {
    		throw new ConsistirException("Deve ser informado o ano (Aluno Não Cursou Disciplina).");
    	}
		
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoSemestral() && alunoNaoCursouDisciplinaFiltroRelVO.getSemestre().isEmpty()) {
			throw new ConsistirException("Deve ser informado o semestre (Aluno Não Cursou Disciplina).");
		}
    	
    	if ((alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoAnual() || alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoSemestral()) && alunoNaoCursouDisciplinaFiltroRelVO.getAno().length() < 4) {
    		throw new ConsistirException("Deve ser informado corretamente o ano, 4 dígitos (Aluno Não Cursou Disciplina).");
    	}
    	
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral() && alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseInicio().length() < 4) {
    		throw new ConsistirException("Deve ser informado corretamente o ano início, 4 dígitos (Aluno Não Cursou Disciplina).");
    	}
    	
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral() && alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseFim().length() < 4) {
    		throw new ConsistirException("Deve ser informado corretamente o ano fim, 4 dígitos (Aluno Não Cursou Disciplina).");
    	}
    }

    @Override
    public List<AlunoNaoCursouDisciplinaRelVO> consultarRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
    	validarDados(alunoNaoCursouDisciplinaFiltroRelVO);
        List<AlunoNaoCursouDisciplinaRelVO> lista = new ArrayList<AlunoNaoCursouDisciplinaRelVO>(0);
        StringBuilder sb = new StringBuilder().append(realizarMontarConsultaSQL(alunoNaoCursouDisciplinaFiltroRelVO, filtroRelatorioAcademicoVO));
        SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        while (sqlRowSet.next()) {
            lista.add(montarDados(sqlRowSet));
        }
        return lista;
    }

    private StringBuilder realizarMontarConsultaSQL(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ( ");
        montarConsultaDisciplina(alunoNaoCursouDisciplinaFiltroRelVO, sb);
        adicionarFiltrosRelatorio(alunoNaoCursouDisciplinaFiltroRelVO, filtroRelatorioAcademicoVO, sb);
        if (alunoNaoCursouDisciplinaFiltroRelVO.getConsiderarDisciplinaGrupoOptativas()) {
        	sb.append(" UNION ");
        	montarConsultaDisciplinaGrupoOptativas(alunoNaoCursouDisciplinaFiltroRelVO, sb);
        	adicionarFiltrosRelatorio(alunoNaoCursouDisciplinaFiltroRelVO, filtroRelatorioAcademicoVO, sb);
        }
        sb.append(" ) AS t ");
        if (alunoNaoCursouDisciplinaFiltroRelVO.getAgruparApenasPorDisciplinaCargaHoraria()) {
        	sb.append(" ORDER BY codigoDisciplina, cargaHorariaDisciplina, plcodigo, pessoa_nome, unidadeensino_nome, curso_nome ");
        } else {
        	sb.append(" ORDER BY codigoDisciplina, cargaHorariaDisciplina, plcodigo, unidadeensino_nome, curso_nome, pessoa_nome ");
        }
        return sb;
    }
    
	private String getSQLConsultaRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) {
		return new StringBuilder()
			.append(" SELECT m.matricula AS matricula_matricula, p.nome AS pessoa_nome, p.cpf AS pessoa_cpf, p.telefoneres AS pessoa_telefoneres, ")
			.append(" p.telefonecomer AS pessoa_telefonecomer, p.telefonerecado AS pessoa_telefonerecado, p.celular AS pessoa_celular, p.email AS pessoa_email, ")
			.append(" c.nome AS curso_nome, ue.nome  AS unidadeensino_nome, m.anoingresso AS matricula_anoingresso, disciplina.nome as nomeDisciplina, ")
			.append(" gd.cargahoraria as cargaHorariaDisciplina, disciplina.codigo as codigoDisciplina, ")
			.append(alunoNaoCursouDisciplinaFiltroRelVO.getApresentarPeriodoLetivoDisciplina() ? 
					" pl.codigo as plcodigo, pl.descricao as pldescricao" : " '' as plcodigo,  '' as pldescricao ")
			.append(" FROM matricula m ")
			.append(" INNER JOIN matriculaperiodo mp on m.matricula = mp.matricula ")
			.append(" INNER JOIN periodoletivo plmp on plmp.codigo = mp.periodoletivomatricula ")
			.append(" INNER JOIN pessoa p on  p.codigo = m.aluno ")
			.append(" INNER JOIN curso c on m.curso = c.codigo ")
			.append(" INNER JOIN unidadeensino ue on ue.codigo = m.unidadeensino ")
			.append(" INNER JOIN gradecurricular gc on m.gradecurricularatual = gc.codigo ")
			.append(" INNER JOIN periodoletivo pl on gc.codigo = pl.gradecurricular and pl.periodoletivo <= plmp.periodoletivo ")
		.toString();
	}
	
	private void montarConsultaDisciplina(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
		stringBuilder.append(getSQLConsultaRelatorio(alunoNaoCursouDisciplinaFiltroRelVO));
		stringBuilder.append(" INNER JOIN gradedisciplina gd on gd.periodoletivo = pl.codigo ");        	
        if (!alunoNaoCursouDisciplinaFiltroRelVO.getConsiderarDisciplinaOptativaForaGrupoOptativas()) {        
        	stringBuilder.append(" and gd.tipodisciplina in ('OB', 'LG') ");        	
        }
        stringBuilder.append(" INNER JOIN disciplina on disciplina.codigo = gd.disciplina WHERE 1 = 1 ");
	}
	
	private void montarConsultaDisciplinaGrupoOptativas(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
		stringBuilder.append(getSQLConsultaRelatorio(alunoNaoCursouDisciplinaFiltroRelVO));
		stringBuilder.append(" INNER JOIN gradecurriculargrupooptativa on gradecurriculargrupooptativa.gradecurricular = gc.codigo and pl.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		stringBuilder.append(" and pl.codigo = (select pl2.codigo from periodoletivo pl2 where pl2.gradecurricular = gc.codigo and pl2.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo order by pl2.periodoletivo desc limit 1)");
		stringBuilder.append(" INNER JOIN gradecurriculargrupooptativadisciplina gd on gd.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		stringBuilder.append(" INNER JOIN disciplina on disciplina.codigo = gd.disciplina ");
		stringBuilder.append(" WHERE pl.controleoptativagrupo ");
	}
	
	private void groupBy(StringBuilder stringBuilder) {
		stringBuilder.append(" GROUP BY m.matricula, p.nome, p.cpf, p.telefoneres, p.telefonecomer, p.telefonerecado, p.celular, c.nome, ue.nome, p.email, m.anoingresso, codigoDisciplina, nomedisciplina, cargaHorariaDisciplina, plcodigo, pldescricao ");
	}

	private void adicionarFiltrosRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, StringBuilder stringBuilder) {
		stringBuilder.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		stringBuilder.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
    	adicionarFiltrosCursoTurmaUnidadeEnsino(alunoNaoCursouDisciplinaFiltroRelVO, stringBuilder);
    	adicionarFiltroPeriodicidade(alunoNaoCursouDisciplinaFiltroRelVO, stringBuilder);
    	adicionarFiltroDisciplina(alunoNaoCursouDisciplinaFiltroRelVO, stringBuilder);
    	adicionarConsultaExistenciaHistoricoReprovacao(alunoNaoCursouDisciplinaFiltroRelVO, stringBuilder);
    	adicionarConsultaInexistenciaHistoricoNaoReprovacao(stringBuilder);
    	groupBy(stringBuilder);
	}

	public AlunoNaoCursouDisciplinaRelVO montarDados(SqlRowSet sqlRowSet) {
        AlunoNaoCursouDisciplinaRelVO alunoNaoCursouDisciplinaRelVO = new AlunoNaoCursouDisciplinaRelVO();
        alunoNaoCursouDisciplinaRelVO.setAluno(sqlRowSet.getString("pessoa_nome"));
        alunoNaoCursouDisciplinaRelVO.setAnoMatricula(sqlRowSet.getString("matricula_anoingresso"));
        alunoNaoCursouDisciplinaRelVO.setCelular(sqlRowSet.getString("pessoa_celular"));
        alunoNaoCursouDisciplinaRelVO.setCpf(sqlRowSet.getString("pessoa_cpf"));
        alunoNaoCursouDisciplinaRelVO.setCurso(sqlRowSet.getString("curso_nome"));
        alunoNaoCursouDisciplinaRelVO.setEmail(sqlRowSet.getString("pessoa_email"));
        alunoNaoCursouDisciplinaRelVO.setMatricula(sqlRowSet.getString("matricula_matricula"));
        alunoNaoCursouDisciplinaRelVO.setTelefoneComer(sqlRowSet.getString("pessoa_telefoneComer"));
        alunoNaoCursouDisciplinaRelVO.setTelefoneRec(sqlRowSet.getString("pessoa_telefoneRecado"));
        alunoNaoCursouDisciplinaRelVO.setTelefoneRes(sqlRowSet.getString("pessoa_telefoneRes"));
        alunoNaoCursouDisciplinaRelVO.setUnidadeEnsino(sqlRowSet.getString("unidadeEnsino_nome"));
        alunoNaoCursouDisciplinaRelVO.setCodigoDisciplina(sqlRowSet.getInt("codigoDisciplina"));
        alunoNaoCursouDisciplinaRelVO.setNomeDisciplina(sqlRowSet.getString("nomeDisciplina"));
        alunoNaoCursouDisciplinaRelVO.setCargaHorariaDisciplina(sqlRowSet.getString("cargaHorariaDisciplina"));
        alunoNaoCursouDisciplinaRelVO.setPeriodoLetivoDescricao(sqlRowSet.getString("pldescricao"));
        return alunoNaoCursouDisciplinaRelVO;
    }
    
    private void adicionarFiltrosCursoTurmaUnidadeEnsino(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
    		stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getCursoVOs().stream()
    				.filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getCodigo).map(String::valueOf).collect(joining(", ", " AND c.codigo IN (", ") ")));
    		if (Uteis.isAtributoPreenchido(alunoNaoCursouDisciplinaFiltroRelVO.getTurmaVO())) {
    			stringBuilder.append(" AND mp.turma = ").append(alunoNaoCursouDisciplinaFiltroRelVO.getTurmaVO().getCodigo()).append(" ");
    		}
    	}
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getUnidadeEnsinoVOs().stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
    		stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getUnidadeEnsinoVOs().stream()
    				.filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getCodigo).map(String::valueOf).collect(joining(", ", " AND ue.codigo IN (", ") ")));
        }
    }
    
    private void adicionarFiltroPeriodicidade(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
    	if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoAnual()) {
    		stringBuilder.append(" AND mp.ano = '").append(alunoNaoCursouDisciplinaFiltroRelVO.getAno()).append("' ");
        	
        } else if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoSemestral()) {
        	stringBuilder.append(" AND mp.ano = '").append(alunoNaoCursouDisciplinaFiltroRelVO.getAno()).append("' ");
        	stringBuilder.append(" AND mp.semestre = '").append(alunoNaoCursouDisciplinaFiltroRelVO.getSemestre()).append("' ");
        	
        } else if (alunoNaoCursouDisciplinaFiltroRelVO.getFiltrarCursoIntegral()) {
        	stringBuilder.append("AND ((m.anoingresso is not null and m.anoingresso::int >= ");
        	stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseInicio()).append(" ");
        	stringBuilder.append("AND m.anoingresso::int <= ");
        	stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseFim()).append(" ) ");
        	stringBuilder.append("or ((m.anoingresso is null) and extract(year from m.data)::int >= ");
        	stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseInicio()).append(" ");
        	stringBuilder.append("and extract(year from m.data)::int <= ");
        	stringBuilder.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseFim()).append(" )) ");
        }
    }
    
    private void adicionarFiltroDisciplina(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
    	if (Uteis.isAtributoPreenchido(alunoNaoCursouDisciplinaFiltroRelVO.getDisciplinaVO())) {
    		stringBuilder.append(" AND disciplina.codigo = ").append(alunoNaoCursouDisciplinaFiltroRelVO.getDisciplinaVO().getCodigo()).append(" ");
        }
    }
    
    private void adicionarConsultaExistenciaHistoricoReprovacao(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, StringBuilder stringBuilder) {
    	if (!alunoNaoCursouDisciplinaFiltroRelVO.getTrazerAlunosReprovaramNaoCursamDisciplina()) {
    		stringBuilder.append(" AND NOT exists (select historico.codigo from historico where historico.matricula = m.matricula and historico.matrizcurricular = m.gradecurricularatual and historico.disciplina = disciplina.codigo ");
    		Optional.ofNullable(SituacaoHistorico.getSituacoesDeReprovacao())
				.filter(Uteis::isAtributoPreenchido).map(s -> s.stream().map(SituacaoHistorico::getValor)
				.collect(joining("', '", " AND historico.situacao IN ('", "') ")))
				.ifPresent(stringBuilder::append);
    		stringBuilder.append(" ) ");
        }
    }
    
    private void adicionarConsultaInexistenciaHistoricoNaoReprovacao(StringBuilder stringBuilder) {
    	stringBuilder.append(" AND NOT exists (select historico.codigo from historico where historico.matricula = m.matricula and historico.matrizcurricular = m.gradecurricularatual and historico.disciplina = disciplina.codigo ");
    	Optional.ofNullable(SituacaoHistorico.getSituacoesDeReprovacao())
			.filter(Uteis::isAtributoPreenchido).map(s -> s.stream().map(SituacaoHistorico::getValor)
			.collect(joining("', '", " AND historico.situacao NOT IN ('", "') ")))
			.ifPresent(stringBuilder::append);
    	stringBuilder.append(" ) ");
    }

    @Override
    public String designIReportRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
    	if (TipoRelatorioEnum.PDF.equals(tipoRelatorioEnum)) {
    		 return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    	} else if (TipoRelatorioEnum.EXCEL.equals(tipoRelatorioEnum)) {
    		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Excel.jrxml"); 
    	}
        return "";
    }

    @Override
    public String caminhoIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("AlunoNaoCursouDisciplinaRel");
    }
}
