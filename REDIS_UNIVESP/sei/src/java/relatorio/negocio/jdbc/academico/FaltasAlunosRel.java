package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.academico.FaltasAlunosRelVO;
import relatorio.negocio.interfaces.academico.FaltasAlunosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Danilo
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class FaltasAlunosRel extends SuperRelatorio implements FaltasAlunosRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public static void validarDados(UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma, DisciplinaVO disciplina) throws ConsistirException {
		if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
		}
		if (unidadeEnsinoCurso == null || unidadeEnsinoCurso.getCodigo() == null || unidadeEnsinoCurso.getCodigo() == 0) {
			throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
		}
	}

	public List<FaltasAlunosRelVO> consultaFaltasAlunosRelatorio(Integer unidadeEnsino, Integer turma, Integer curso, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct d.nome as disciplina, d.codigo, p.nome as aluno, frequenciaaula.matricula,  ");
		sqlStr.append(" unidadeEnsino.nome as unidadeEnsino, curso.nome as curso,  ");
		sqlStr.append(" (select count (frequenciaaula.presente) from frequenciaaula  ");
		sqlStr.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sqlStr.append(" inner join matricula on frequenciaaula.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" where frequenciaaula.presente = false and frequenciaaula.abonado = false and disciplina.codigo = d.codigo and pessoa.codigo = p.codigo");
		if (!turma.equals(0)) {
			sqlStr.append(" AND registroaula.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND registroaula.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND registroaula.semestre = '").append(semestre).append("'");
		}
		sqlStr.append(") as qtdFaltas ");
		sqlStr.append(" from frequenciaaula  ");
		sqlStr.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula  ");
		sqlStr.append(" inner join disciplina d on d.codigo = registroaula.disciplina  ");
		sqlStr.append(" inner join matricula on frequenciaaula.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa as p on p.codigo = matricula.aluno  ");
		sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino  ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" where frequenciaaula.presente = false ");
		if (!disciplina.equals(0)) {
			sqlStr.append(" and d.codigo = ").append(disciplina);
		}
		if (!curso.equals(0)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if (!turma.equals(0)) {
			sqlStr.append(" AND registroaula.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND registroaula.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND registroaula.semestre = '").append(semestre).append("'");
		}
		sqlStr.append(" order by d.nome, p.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaFaltasAlunosRelatorio(tabelaResultado);
	}

	public List<FaltasAlunosRelVO> montarDadosConsultaFaltasAlunosRelatorio(SqlRowSet tabelaResultado) throws Exception {
		List<FaltasAlunosRelVO> vetResultado = new ArrayList<FaltasAlunosRelVO>(0);
		while (tabelaResultado.next()) {
			FaltasAlunosRelVO obj = new FaltasAlunosRelVO();
			montarDadosConsultaFaltasAlunosRelatorio(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosConsultaFaltasAlunosRelatorio(FaltasAlunosRelVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do RegistroAula
		obj.setQtdFaltas(dadosSQL.getInt("qtdfaltas"));
		obj.setDisciplina(dadosSQL.getString("disciplina"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setAluno(dadosSQL.getString("aluno"));
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("FaltasAlunosRel");
	}
}
