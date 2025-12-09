package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.SituacaoMatriculaPeriodo;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralFormaIngressoRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralSituacaoMatriculaRelVO;
import relatorio.negocio.interfaces.academico.AlunosMatriculadosGeralRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class AlunosMatriculadosGeralRel extends SuperRelatorio implements AlunosMatriculadosGeralRelInterfaceFacade {

	private static final long serialVersionUID = 9080554898240819169L;

	public AlunosMatriculadosGeralRel() {
	}

	public void validarDados(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, TurmaVO turmaVO, ProcessoMatriculaVO processoMatricula, String tipoRelatorioCalVetGer, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception {
		boolean selecionado = false;
		for (UnidadeEnsinoVO ueVO : unidadeEnsinoVOs) {
			if (ueVO.getFiltrarUnidadeEnsino()) {
				selecionado = true;
				break;
			}
		}
		if (!selecionado) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosPorUnidadeCursoTurnoTurmaRel_unidadeEnsino"));
		}
		if (!filtroRelatorioAcademicoVO.getFiltrarCursoAnual() && !filtroRelatorioAcademicoVO.getFiltrarCursoSemestral() && !filtroRelatorioAcademicoVO.getFiltrarCursoIntegral()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_filtrarCurso"));
		}
		if (!filtroRelatorioAcademicoVO.getFiltrarCursoAnual() && !filtroRelatorioAcademicoVO.getFiltrarCursoSemestral() && !filtroRelatorioAcademicoVO.getFiltrarCursoIntegral()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_filtrarCurso"));
		}
		if (!calouro && !veterano && !transInterna && !transExterna) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_tipoRelatorio"));
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData() && Uteis.isAtributoPreenchido(turmaVO)) {
			if (!filtroRelatorioAcademicoVO.getFiltrarCursoIntegral()) {
				if (filtroRelatorioAcademicoVO.getDataInicio() == null) {
					throw new Exception("A DATA DE INÍCIO deve ser informado!");
				}
				if (filtroRelatorioAcademicoVO.getDataTermino() == null) {
					throw new Exception("A DATA FINAL deve ser informado!");
				}
			}

			if (Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getDataInicio()) && Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getDataTermino()) && (filtroRelatorioAcademicoVO.getDataInicio().compareTo(filtroRelatorioAcademicoVO.getDataTermino()) >= 1)) {
				throw new Exception("A DATA DE INÍCIO não pode ser maior que a DATA FINAL!");
			}
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAno() || filtroRelatorioAcademicoVO.getFiltrarCursoAnual()) {
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}

		}
		if (filtroRelatorioAcademicoVO.getFiltrarCursoSemestral()) {
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}
			if (filtroRelatorioAcademicoVO.getSemestre().trim().isEmpty()) {
				throw new Exception("A SEMESTRE deve ser informado!");
			}
		} 


	}

	public List<AlunosMatriculadosGeralRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,
			List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs,
			List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao,
			Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna,
			Boolean considerarCursosAnuaisSemestrais,
			List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs,
			List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs)
			throws Exception {
		validarDados(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, cursoVOs, turmaVO, processoMatricula,
				tipoRelatorioCalVetGer, calouro, veterano, transInterna, transExterna,
				considerarCursosAnuaisSemestrais);
		AlunosMatriculadosGeralRel.emitirRelatorio(getIdEntidade(), false, null);
		return executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurma(filtroRelatorioAcademicoVO, unidadeEnsinoVOs,
				processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro,
				veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais,
				listaAlunosMatriculadosGeralFormaIngressoRelVOs, listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs);
	}

	public List<AlunosMatriculadosGeralRelVO> criarObjetoSintetico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception {
		AlunosMatriculadosGeralRel.emitirRelatorio(getIdEntidade(), false, null);
		return executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaSintetico(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turma, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais);
	}
	
	public List<AlunosMatriculadosGeralRelVO> criarObjetoAnaliticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws Exception {
		validarDados(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, cursoVOs, turmaVO, processoMatricula, tipoRelatorioCalVetGer, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais);
		AlunosMatriculadosGeralRel.emitirRelatorio(getIdEntidade(), false, null);
		return executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaAnaliticoPorCursoPeriodoLetivo(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais, listaAlunosMatriculadosGeralFormaIngressoRelVOs, listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs);
	}
	
	public List<AlunosMatriculadosGeralRelVO> criarObjetoSinteticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception {
		AlunosMatriculadosGeralRel.emitirRelatorio(getIdEntidade(), false, null);
		return executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaSinteticoPorCursoPeriodoLetivo(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turma, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais);
	}

	private List<AlunosMatriculadosGeralRelVO> executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaSintetico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select unidadeensino, curso, turma, turno, ");
		sql.append("sum(case when veterano then 1 else 0 end) as qtdeVeterano, sum(case when calouro then 1 else 0 end) as qtdeCalouro, sum(case when transfExterna then 1 else 0 end) as qtdeTransfExterna, sum(case when transfInterna then 1 else 0 end) as qtdeTransfInterna ");
		sql.append("from ( ");
		sql.append("select unidadeensino.nome as unidadeensino, curso.nome as curso, turno.nome as turno, turma.identificadorturma as turma, ");
		sql.append("(0 < (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as veterano, ");
		sql.append("(formaingresso not in ('TI' , 'TE') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF') and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as calouro, ");
		sql.append("((formaingresso in ('TE') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfExterna, ");
		sql.append("((formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfInterna ");
		sql.append(realizarGeracaoClausulaFrom());
		sql.append(realizarGeracaoClausulaWhere(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais));
		sql.append(") as t group by unidadeensino, curso, turno, turma ");
		sql.append("order by unidadeensino, curso, turno, turma ");
		SqlRowSet sql2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosAlunoUnidadeEnsinoCursoTurmaSintetico(sql2);
	}

	private List<AlunosMatriculadosGeralRelVO> executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurma(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select matricula.matricula, curso.nome as curso, curso.periodicidade, matper.data, matricula.formaingresso, turma.identificadorTurma as turma, pessoa.nome, pessoa.email, pessoa.celular, pessoa.telefoneres, valor as valorMatricula, turno.nome as turno, unidadeensino.nome as unidadeensino, matper.ano, matper.semestre, matper.situacaomatriculaperiodo,");
		sql.append("(0 < (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as veterano, ");
		sql.append("(formaingresso not in ('TI' , 'TE') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF') and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as calouro, ");
		sql.append("((formaingresso in ('TE') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfExterna, ");
		sql.append("((formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfInterna ");
		sql.append(realizarGeracaoClausulaFrom());
		sql.append(realizarGeracaoClausulaWhere(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais));
		sql.append("group by matricula.matricula, curso.nome, matper.data, matricula.formaingresso, turma.identificadorTurma, pessoa.nome, pessoa.email, pessoa.celular, pessoa.telefoneres, valorMatricula, turno.nome, unidadeensino.nome, curso.periodicidade, matper.ano, matper.semestre, matper.situacaomatriculaperiodo ");
		sql.append("order by unidadeensino.nome, curso.nome, turno.nome, turma.identificadorTurma, pessoa.nome");
		SqlRowSet sql2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosAlunoUnidadeEnsinoCursoTurma(sql2, filtroRelatorioAcademicoVO, listaAlunosMatriculadosGeralFormaIngressoRelVOs, listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs);
	}
	
	private List<AlunosMatriculadosGeralRelVO> executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaAnaliticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select matricula.matricula, curso.nome as curso, curso.periodicidade, matper.data, matricula.formaingresso, pl.descricao as periodoletivo, pessoa.nome, pessoa.email, pessoa.celular, pessoa.telefoneres, valor as valorMatricula, turno.nome as turno, unidadeensino.nome as unidadeensino, matper.ano, matper.semestre, matper.situacaomatriculaperiodo, ");
		sql.append("(0 < (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as veterano, ");
		sql.append("(formaingresso not in ('TI' , 'TE') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF') and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as calouro, ");
		sql.append("((formaingresso in ('TE') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfExterna, ");
		sql.append("((formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfInterna ");
		sql.append(realizarGeracaoClausulaFromPorCursoPeriodoLetivo());
		sql.append(realizarGeracaoClausulaWhere(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais));
		sql.append("group by matricula.matricula, curso.nome, matper.data, matricula.formaingresso, pl.descricao, pessoa.nome, pessoa.email, pessoa.celular, pessoa.telefoneres, valorMatricula, turno.nome, unidadeensino.nome, curso.periodicidade, matper.ano, matper.semestre, matper.situacaomatriculaperiodo ");
		sql.append("order by unidadeensino.nome, curso.nome, turno.nome, pl.descricao, pessoa.nome");
		SqlRowSet sql2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosAlunoUnidadeEnsinoCursoPeriodoLetivoAnalitico(sql2, filtroRelatorioAcademicoVO, listaAlunosMatriculadosGeralFormaIngressoRelVOs, listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs);
	}
	
	private List<AlunosMatriculadosGeralRelVO> executarConsultaParametrizadaAlunoUnidadeEnsinoCursoTurmaSinteticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws SQLException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select unidadeensino, curso, periodoLetivo, turno, ");
		sql.append("sum(case when veterano then 1 else 0 end) as qtdeVeterano, sum(case when calouro then 1 else 0 end) as qtdeCalouro, ");
		sql.append(" sum(case when transfExterna then 1 else 0 end) as qtdeTransfExterna, sum(case when transfInterna then 1 else 0 end) as qtdeTransfInterna ");
		sql.append("from ( ");
		sql.append("select unidadeensino.nome as unidadeensino, curso.nome as curso, turno.nome as turno, pl.descricao as periodoletivo, ");
		sql.append("(0 < (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as veterano, ");
		sql.append("(formaingresso not in ('TI' , 'TE') and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF') and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as calouro, ");
		sql.append(" ((formaingresso in ('TE') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('EX') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfExterna, ");
		sql.append(" ((formaingresso in ('TI') or matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN') and matricula is not null and transferenciaentrada.situacao = 'EF')) and 0 = (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then mp.data < matper.data else (mp.ano||'/'||mp.semestre) < (matper.ano||'/'||matper.semestre) end)) as transfInterna  ");
		sql.append(realizarGeracaoClausulaFromPorCursoPeriodoLetivo());
		sql.append(realizarGeracaoClausulaWhere(filtroRelatorioAcademicoVO, unidadeEnsinoVOs, processoMatricula, cursoVOs, turnoVOs, turmaVO, tipoRelatorioCalVetGer, formaRenovacao, calouro, veterano, transInterna, transExterna, considerarCursosAnuaisSemestrais));
		sql.append(") as t group by unidadeensino, curso, turno, periodoLetivo ");
		sql.append("order by 1,2,4,3 ");
		SqlRowSet sql2 = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosAlunoUnidadeEnsinoCursoPeriodoLetivoSintetico(sql2);
	}

	private StringBuilder realizarGeracaoClausulaFrom() {
		StringBuilder sql = new StringBuilder();
		sql.append("from matriculaPeriodo matper ");
		sql.append("inner join matricula on matricula.matricula = matper.matricula ");
		sql.append("inner join unidadeensinocurso on unidadeensinocurso.codigo = matper.unidadeensinocurso ");
		sql.append("inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append("inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append("inner join turma on turma.codigo = matper.turma ");
		sql.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append("left join contareceber on contareceber.matriculaperiodo = matper.codigo and contareceber.tipoorigem = 'MAT' ");
		return sql;
	}
	
	private StringBuilder realizarGeracaoClausulaFromPorCursoPeriodoLetivo() {
		StringBuilder sql = new StringBuilder();
		sql.append("from matriculaPeriodo matper ");
		sql.append("inner join periodoletivo as pl on pl.codigo = matper.periodoletivomatricula ");
		sql.append("inner join matricula on matricula.matricula = matper.matricula ");
		sql.append("inner join unidadeensinocurso on unidadeensinocurso.codigo = matper.unidadeensinocurso ");
		sql.append("inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append("inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append("inner join turma on turma.codigo = matper.turma ");
		sql.append("inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append("left join contareceber on contareceber.matriculaperiodo = matper.codigo and contareceber.tipoorigem = 'MAT' ");
		return sql;
	}

	private StringBuilder realizarGeracaoClausulaWhere(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) {
		StringBuilder sql = new StringBuilder();
		sql.append(" where ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matper"));
		sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matper"));
		sql.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		// Filtro opcional: Curso
		sql.append(adicionarFiltroCurso(cursoVOs));
		// Filtro opcional: Turno
		sql.append(adicionarFiltroTurno(turnoVOs));
		// Filtro opcional: Turma
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sql.append(" and turma.codigo = ").append(turmaVO.getCodigo()).append(" ");
		}
		if (processoMatricula != null && processoMatricula.getCodigo() != 0) {
			sql.append(" and matper.processomatricula = ").append(processoMatricula.getCodigo());
		}
		String andOr = " and ( ";
		boolean filtrarCursoSemestral = (filtroRelatorioAcademicoVO.getFiltrarCursoSemestral() && !Uteis.isAtributoPreenchido(turmaVO)) || (filtroRelatorioAcademicoVO.getFiltrarCursoSemestral() && Uteis.isAtributoPreenchido(turmaVO) && filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre());
		if (filtrarCursoSemestral) {
			sql.append(andOr).append(" (curso.periodicidade = 'SE' and matper.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("' ");
			sql.append(" and matper.semestre = '").append(filtroRelatorioAcademicoVO.getSemestre()).append("' ) ");
			andOr = " or ";
		}
		boolean filtrarCursoAnual = (filtroRelatorioAcademicoVO.getFiltrarCursoAnual() && !Uteis.isAtributoPreenchido(turmaVO)) || (filtroRelatorioAcademicoVO.getFiltrarCursoAnual() && Uteis.isAtributoPreenchido(turmaVO) && filtroRelatorioAcademicoVO.getFiltrarPorAno());
		if (filtrarCursoAnual) {
			sql.append(andOr).append(" (curso.periodicidade = 'AN' and matper.ano = '").append(filtroRelatorioAcademicoVO.getAno()).append("') ");
			andOr = " or ";
		}
		boolean filtrarCursoIntegral = (filtroRelatorioAcademicoVO.getFiltrarCursoIntegral() && !Uteis.isAtributoPreenchido(turmaVO)) || (filtroRelatorioAcademicoVO.getFiltrarCursoIntegral() && Uteis.isAtributoPreenchido(turmaVO) && filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData());
		if (filtrarCursoIntegral) {
			sql.append(andOr).append(" (curso.periodicidade = 'IN' and  matper.codigo = (select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.situacaomatriculaperiodo != 'PC' order by matriculaperiodo.data desc limit 1)) ");
			if(!considerarCursosAnuaisSemestrais){
				sql.append(" and ").append(realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "matper.data", true));
			}
		}
		if (filtrarCursoSemestral || filtrarCursoAnual || filtrarCursoIntegral) {
			sql.append(" ) ");
		}
		if (considerarCursosAnuaisSemestrais &&(Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getDataInicio()) || Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getDataTermino()))) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "matper.data", true));
		}
		// Filtro Forma Renovacao
		if (formaRenovacao.equals("renovacaoOnline")) {
			sql.append(" and aceitoutermocontratorenovacaoonline = true ");
		} else if (formaRenovacao.equals("renovacaoSecretaria")) {
			sql.append(" and aceitoutermocontratorenovacaoonline = false ");
		}
		if(Uteis.isAtributoPreenchido(filtroRelatorioAcademicoVO.getFormaIngresso())){
			sql.append(" and matricula.formaingresso = '").append(filtroRelatorioAcademicoVO.getFormaIngresso().getValor()).append("' ");
		}
		sql.append(" and ( ");
		boolean or = false;
		if (veterano) {
			// Alunos veteranos
			sql.append(or ? " or " : " ").append(" (0 < (select count(matper2.codigo) from matriculaperiodo matper2 ");
			sql.append(" where matper2.matricula = matper.matricula  and matper2.situacaomatriculaperiodo != 'PC' ");
			sql.append(" and case when curso.periodicidade = 'IN' then matper2.data < matper.data else (matper2.ano||'/'||matper2.semestre) < (matper.ano||'/'||matper.semestre) end )  ) ");
			or = true;
		}
		if (calouro) {
			// Alunos calouros //
			sql.append(or ? " or " : " ").append(" ( formaingresso not in ('TI' , 'TE') ");
			sql.append(" and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF' )  ");
			sql.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matper.matricula  ");
			sql.append(" and matper2.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then matper2.data < matper.data else (matper2.ano||'/'||matper2.semestre) < (matper.ano||'/'||matper.semestre) end ");
			sql.append(" ) ) ");
			or = true;
		}
		if (transInterna) {
			// alunos transferenciaInterna
			sql.append(or ? " or " : " ").append(" ((formaingresso = 'TI'  ");
			sql.append(" or (matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada  = 'IN' and matricula is not null and transferenciaentrada.situacao = 'EF' )))   ");
			sql.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matper.matricula  and matper2.situacaomatriculaperiodo != 'PC'  and case when curso.periodicidade = 'IN' then matper2.data < matper.data else (matper2.ano||'/'||matper2.semestre) < (matper.ano||'/'||matper.semestre) end ) )  ");
			or = true;
		}
		if (transExterna) {
			// alunos transferenciaExterna
			sql.append(or ? " or " : " ").append(" ((formaingresso = 'TE' ");
			sql.append(" or (matricula.matricula in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada  = 'EX' and matricula is not null and transferenciaentrada.situacao = 'EF' )))  ");
			sql.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matper.matricula and matper2.situacaomatriculaperiodo != 'PC'  and case when curso.periodicidade = 'IN' then matper2.data < matper.data else (matper2.ano||'/'||matper2.semestre) < (matper.ano||'/'||matper.semestre) end ) )  ");
			or = true;
		}
		sql.append(" ) ");
		return sql;
	}

	private List<AlunosMatriculadosGeralRelVO> montarDadosAlunoUnidadeEnsinoCursoTurma(SqlRowSet sql2, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws Exception {
		List<AlunosMatriculadosGeralRelVO> listaConsulta = new ArrayList<AlunosMatriculadosGeralRelVO>(0);
		while (sql2.next()) {
			AlunosMatriculadosGeralRelVO obj = new AlunosMatriculadosGeralRelVO();
			obj.setMatriculaAluno(sql2.getString("matricula"));
			obj.setNomeAluno(sql2.getString("nome"));
			obj.setCurso(sql2.getString("curso"));
			obj.setTurma(sql2.getString("turma"));
			obj.setTurno(sql2.getString("turno"));
			obj.setUnidadeEnsino(sql2.getString("unidadeensino"));
			obj.setData(Uteis.getData(sql2.getDate("data")));
			if (sql2.getString("formaingresso") == null) {
				obj.setFormaIngresso("");
			} else {
				obj.setFormaIngresso(FormaIngresso.getDescricao(sql2.getString("formaingresso")));
			}
			obj.setEmail(sql2.getString("email"));
			obj.setCelular(sql2.getString("celular"));
			obj.setTelefoneRes(sql2.getString("telefoneres"));
			obj.setValorMatricula(sql2.getDouble("valorMatricula"));
			obj.setCalouro(sql2.getBoolean("calouro"));
			obj.setVeterano(sql2.getBoolean("veterano"));
			obj.setTransfExterna(sql2.getBoolean("transfExterna"));
			obj.setTransfInterna(sql2.getBoolean("transfInterna"));	
			obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getDescricao(sql2.getString("situacaomatriculaperiodo")));
			validarApresentarSubtotalFormaIngresso(listaAlunosMatriculadosGeralFormaIngressoRelVOs, obj, filtroRelatorioAcademicoVO);
			apresentarSubtotalStatusMatricula(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, obj);
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	private List<AlunosMatriculadosGeralRelVO> montarDadosAlunoUnidadeEnsinoCursoTurmaSintetico(SqlRowSet sql2) throws Exception {
		List<AlunosMatriculadosGeralRelVO> listaConsulta = new ArrayList<AlunosMatriculadosGeralRelVO>(0);
		while (sql2.next()) {
			AlunosMatriculadosGeralRelVO obj = new AlunosMatriculadosGeralRelVO();
			obj.setCurso(sql2.getString("curso"));
			obj.setTurma(sql2.getString("turma"));
			obj.setUnidadeEnsino(sql2.getString("unidadeEnsino"));
			obj.setTurno(sql2.getString("turno"));
			obj.setQtdeCalouro(sql2.getInt("qtdeCalouro"));
			obj.setQtdeVeterano(sql2.getInt("qtdeVeterano"));
			obj.setQtdeTransExterna(sql2.getInt("qtdeTransfExterna"));
			obj.setQtdeTransInterna(sql2.getInt("qtdeTransfInterna"));			
			obj.setQtdTurma(String.valueOf(obj.getQtdeCalouro() + obj.getQtdeVeterano() + obj.getQtdeTransExterna() + obj.getQtdeTransInterna()));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}
	
	private List<AlunosMatriculadosGeralRelVO> montarDadosAlunoUnidadeEnsinoCursoPeriodoLetivoAnalitico(SqlRowSet sql2, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws Exception {
		List<AlunosMatriculadosGeralRelVO> listaConsulta = new ArrayList<AlunosMatriculadosGeralRelVO>(0);
		while (sql2.next()) {
			AlunosMatriculadosGeralRelVO obj = new AlunosMatriculadosGeralRelVO();
			obj.setMatriculaAluno(sql2.getString("matricula"));
			obj.setNomeAluno(sql2.getString("nome"));
			obj.setCurso(sql2.getString("curso"));
			obj.setPeriodoLetivo(sql2.getString("periodoletivo"));
			obj.setTurno(sql2.getString("turno"));
			obj.setUnidadeEnsino(sql2.getString("unidadeensino"));
			obj.setData(Uteis.getData(sql2.getDate("data")));
			if (sql2.getString("formaingresso") == null) {
				obj.setFormaIngresso("");
			} else {
				obj.setFormaIngresso(FormaIngresso.getDescricao(sql2.getString("formaingresso")));
			}
			obj.setEmail(sql2.getString("email"));
			obj.setCelular(sql2.getString("celular"));
			obj.setTelefoneRes(sql2.getString("telefoneres"));
			obj.setValorMatricula(sql2.getDouble("valorMatricula"));
			obj.setCalouro(sql2.getBoolean("calouro"));
			obj.setVeterano(sql2.getBoolean("veterano"));
			obj.setTransfExterna(sql2.getBoolean("transfExterna"));
			obj.setTransfInterna(sql2.getBoolean("transfInterna"));
			obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.getDescricao(sql2.getString("situacaomatriculaperiodo")));
			validarApresentarSubtotalFormaIngresso(listaAlunosMatriculadosGeralFormaIngressoRelVOs, obj, filtroRelatorioAcademicoVO);
			apresentarSubtotalStatusMatricula(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, obj);
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}
	
	private List<AlunosMatriculadosGeralRelVO> montarDadosAlunoUnidadeEnsinoCursoPeriodoLetivoSintetico(SqlRowSet sql2) throws Exception {
		List<AlunosMatriculadosGeralRelVO> listaConsulta = new ArrayList<AlunosMatriculadosGeralRelVO>(0);
		while (sql2.next()) {
			AlunosMatriculadosGeralRelVO obj = new AlunosMatriculadosGeralRelVO();
			obj.setCurso(sql2.getString("curso"));
			obj.setPeriodoLetivo(sql2.getString("periodoletivo"));
			obj.setUnidadeEnsino(sql2.getString("unidadeEnsino"));
			obj.setTurno(sql2.getString("turno"));
			obj.setQtdeCalouro(sql2.getInt("qtdeCalouro"));
			obj.setQtdeVeterano(sql2.getInt("qtdeVeterano"));
			obj.setQtdeTransExterna(sql2.getInt("qtdeTransfExterna"));
			obj.setQtdeTransInterna(sql2.getInt("qtdeTransfInterna"));
			obj.setQtdPeriodoLetivo(String.valueOf(obj.getQtdeCalouro() + obj.getQtdeVeterano() + obj.getQtdeTransExterna() + obj.getQtdeTransInterna()));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}
	
	public void validarApresentarSubtotalFormaIngresso(List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, AlunosMatriculadosGeralRelVO obj, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO){
		if(filtroRelatorioAcademicoVO.getApresentarSubtotalFormaIngresso()){
			AlunosMatriculadosGeralFormaIngressoRelVO formaIngresso = consultarSeExiteFormaIngresso(listaAlunosMatriculadosGeralFormaIngressoRelVOs, obj);
			if(Uteis.isAtributoPreenchido(formaIngresso.getDescricao())){
				formaIngresso.setTotal(formaIngresso.getTotal()+1);
				addAlunosMatriculadosGeralFormaIngressoRelVO(listaAlunosMatriculadosGeralFormaIngressoRelVOs, obj, formaIngresso);
			}else if(!obj.getFormaIngresso().trim().isEmpty()){
				formaIngresso.setDescricao(obj.getFormaIngresso());
				formaIngresso.setTotal(1);
				addAlunosMatriculadosGeralFormaIngressoRelVO(listaAlunosMatriculadosGeralFormaIngressoRelVOs, obj, formaIngresso);
			}
		}
	}
	
	public AlunosMatriculadosGeralFormaIngressoRelVO consultarSeExiteFormaIngresso(List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, AlunosMatriculadosGeralRelVO obj){
		for (AlunosMatriculadosGeralFormaIngressoRelVO objExistente : listaAlunosMatriculadosGeralFormaIngressoRelVOs) {
			if(objExistente.getDescricao().equals(obj.getFormaIngresso())){
				return objExistente;
			}
		}
		return new AlunosMatriculadosGeralFormaIngressoRelVO();
	}
	
	public void addAlunosMatriculadosGeralFormaIngressoRelVO(List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, AlunosMatriculadosGeralRelVO obj, AlunosMatriculadosGeralFormaIngressoRelVO formaIngresso){
		int index = 0;
		for (AlunosMatriculadosGeralFormaIngressoRelVO objExistente : listaAlunosMatriculadosGeralFormaIngressoRelVOs) {
			if(objExistente.getDescricao().equals(formaIngresso.getDescricao())){
				listaAlunosMatriculadosGeralFormaIngressoRelVOs.set(index, formaIngresso);
				return;
			}
			index++;
		}
		listaAlunosMatriculadosGeralFormaIngressoRelVOs.add(formaIngresso);
	}

	public void apresentarSubtotalStatusMatricula(List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, AlunosMatriculadosGeralRelVO obj){

		AlunosMatriculadosGeralSituacaoMatriculaRelVO situacaoMatricula = consultarSeExiteSituacaoStatus(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, obj);
			if(Uteis.isAtributoPreenchido(situacaoMatricula.getDescricao())){
				situacaoMatricula.setTotal(situacaoMatricula.getTotal()+1);
				addAlunosMatriculadosGeralSituacaoMatriculaRelVO(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, obj, situacaoMatricula);
			}else if(!obj.getSituacaoMatriculaPeriodo().trim().isEmpty()){
				situacaoMatricula.setDescricao(obj.getSituacaoMatriculaPeriodo());
				situacaoMatricula.setTotal(1);
				addAlunosMatriculadosGeralSituacaoMatriculaRelVO(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, obj, situacaoMatricula);
			}

	}	

	public AlunosMatriculadosGeralSituacaoMatriculaRelVO consultarSeExiteSituacaoStatus(List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, AlunosMatriculadosGeralRelVO obj){
		for (AlunosMatriculadosGeralSituacaoMatriculaRelVO objExistente : listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) {
			if(objExistente.getDescricao().equals(obj.getSituacaoMatriculaPeriodo())){
				return objExistente;
			}
		}
		return new AlunosMatriculadosGeralSituacaoMatriculaRelVO();
	}
	
	public void addAlunosMatriculadosGeralSituacaoMatriculaRelVO(List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs, AlunosMatriculadosGeralRelVO obj, AlunosMatriculadosGeralSituacaoMatriculaRelVO situacaoMatricula){
		int index = 0;
		for (AlunosMatriculadosGeralSituacaoMatriculaRelVO objExistente : listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) {
			if(objExistente.getDescricao().equals(situacaoMatricula.getDescricao())){
				listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs.set(index, situacaoMatricula);
				return;
			}
			index++;
		}
		listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs.add(situacaoMatricula);
	}
	
	@Override
	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String getDesignIReportRelatorioAnaliticoFormaRenovacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeAnaliticoFormaRenovacao() + ".jrxml");
	}
	
	public String getDesignIReportRelatorioAnaliticoPorCursoPeriodoLetivo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeAnaliticoPorCursoPeriodoLetivo() + ".jrxml");
	}
	
	public String getDesignIReportRelatorioSinteticoPorCursoPeriodoLetivo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeSinteticoPorCursoPeriodoLetivo() + ".jrxml");
	}

	public String getDesignIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Sintetico.jrxml");
	}

	// public static String getDesignIReportRelatorioSintetico() {
	// return ("relatorio" + File.separator + "designRelatorio" + File.separator
	// + "academico" + File.separator + getIdEntidadeSintetico() + ".jrxml");
	// }
	public String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	public String getDesignIReportRelatorioExcelSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + "Sintetico.jrxml");
	}
	
	public String getDesignIReportRelatorioExcelAnaliticoPorCursoPeriodoLetivo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + "AnaliticoPorCursoPeriodoLetivo.jrxml");
	}
	
	public String getDesignIReportRelatorioExcelSinteticoPorCursoPeriodoLetivo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + "SinteticoPorCursoPeriodoLetivo.jrxml");
	}
	
	public String getDesignIReportRelatorioExcelAnaliticoFormaRenovacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + "FormaRenovacaoMatricula.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AlunosMatriculadosGeralRel");
	}

	public static String getIdEntidadeSintetico() {
		return ("AlunosMatriculadosGeralSinteticoRel");
	}

	public static String getIdEntidadeExcel() {
		return ("AlunosMatriculadosGeralExcelRel");
	}

	public static String getIdEntidadeAnaliticoFormaRenovacao() {
		return ("AlunosMatriculadosGeralFormaRenovacaoMatriculaRel");
	}
	
	public static String getIdEntidadeAnaliticoPorCursoPeriodoLetivo() {
		return ("AlunosMatriculadosGeralAnaliticoPorCursoPeriodoLetivoRel");
	}
	
	public static String getIdEntidadeSinteticoPorCursoPeriodoLetivo() {
		return ("AlunosMatriculadosGeralSinteticoPorCursoPeriodoLetivoRel");
	}

	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" and matricula.unidadeEnsino in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroCurso(List<CursoVO> cursoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and curso.codigo in (0");
		for (CursoVO cursoVO : cursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(", ").append(cursoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroTurno(List<TurnoVO> turnoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and turno.codigo in (0");
		for (TurnoVO turnoVO : turnoVOs) {
			if (turnoVO.getFiltrarTurnoVO()) {
				sql.append(", ").append(turnoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}

}