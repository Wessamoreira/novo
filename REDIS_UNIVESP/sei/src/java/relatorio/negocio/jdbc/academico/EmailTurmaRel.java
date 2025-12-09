package relatorio.negocio.jdbc.academico;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.negocio.comuns.academico.EmailTurmaRelVO;
import relatorio.negocio.interfaces.academico.EmailTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class EmailTurmaRel extends SuperRelatorio implements EmailTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public void validarDados(Integer unidadeEnsino, TurmaVO turmaVO, String ano, String semestre) throws Exception {
		if (unidadeEnsino == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
		}
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_turma"));
		}
		if ((turmaVO.getAnual() || turmaVO.getSemestral()) && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		}
		if (turmaVO.getSemestral() && !Uteis.isAtributoPreenchido(semestre)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		}
	}

	public EmailTurmaRelVO criarObjeto(Integer curso, Integer turma, String ano, String semestre, boolean alunoReposicao, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		return consultarAlunosTurmaSituacao(curso, turma, ano, semestre, alunoReposicao, disciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiro, usuario, filtroRelatorioAcademicoVO);
	}

	public EmailTurmaRelVO consultarAlunosTurmaSituacao(Integer curso, Integer turma, String ano, String semestre, boolean alunoReposicao, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT distinct pessoa.email, pessoa.email2 FROM Matricula");
		sqlStr.append(" INNER JOIN unidadeensino ON Matricula.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" INNER JOIN curso ON Matricula.curso = curso.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodo.turma = turma.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina mptd ON matriculaperiodo.codigo = mptd.matriculaperiodo");
		sqlStr.append(" WHERE 1=1 ");
		if (!alunoReposicao) {
			sqlStr.append(" AND (MatriculaPeriodo.turma = ").append(turma).append(" ) ");
		} else {
			sqlStr.append(" AND (MatriculaPeriodo.turma = ").append(turma).append(" or mptd.turma = ").append(turma).append(" ) ");
		}
		sqlStr.append(" AND pessoa.email IS NOT NULL AND pessoa.email != ''");
		if (!ano.equals("")) {
			sqlStr.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (disciplina != 0) {
			sqlStr.append(" and mptd.disciplina = ").append(disciplina);
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" ORDER BY Pessoa.email");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public static EmailTurmaRelVO montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EmailTurmaRelVO obj = new EmailTurmaRelVO();
		while (tabelaResultado.next()) {
			obj = montarDados(tabelaResultado, obj, nivelMontarDados, usuario);
		}
		return obj;
	}

	public static EmailTurmaRelVO montarDados(SqlRowSet dadosSQL, EmailTurmaRelVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setEmail(obj.getEmail() + dadosSQL.getString("email") + ";");
		String email2 = dadosSQL.getString("email2");
		if (email2 != null) {
			if (!email2.equals("")) {
				obj.setEmail(obj.getEmail() + dadosSQL.getString("email2") + ";");
			}
		}
		return obj;
	}

	public String getIdEntidadeEmailTurmaRel() {
		return "EmailTurmaRel";
	}
}
