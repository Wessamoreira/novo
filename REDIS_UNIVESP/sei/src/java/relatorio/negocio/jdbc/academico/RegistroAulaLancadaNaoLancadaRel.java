package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.academico.RegistroAulaLancadaNaoLancadaRelVO;
import relatorio.negocio.interfaces.academico.RegistroAulaLancadaNaoLancadaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Danilo
 *
 */
@Repository
public class RegistroAulaLancadaNaoLancadaRel extends SuperRelatorio implements RegistroAulaLancadaNaoLancadaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	private void validarDados(Integer unidadeEnsino, String periodicidade, String ano, String semestre, UsuarioVO usuarioVO, Integer turma) throws ConsistirException {
		if ((usuarioVO.getIsApresentarVisaoProfessor() || usuarioVO.getIsApresentarVisaoCoordenador()) && !Uteis.isAtributoPreenchido(turma)) {
			throw new ConsistirException("O campo TURMA deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsino"));
		}
		if ((periodicidade.equals(PeriodicidadeEnum.ANUAL.getValor()) || periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor())) && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		}
		if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor()) && !Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		}
	}

	@Override
	public List<RegistroAulaLancadaNaoLancadaRelVO> consultaRegistroAulaLancadaNaoLancadaRelatorio(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String situacaoRegistroAula, String periodicidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		validarDados(unidadeEnsino, periodicidade, ano, semestre, usuario, turma);
		List<RegistroAulaLancadaNaoLancadaRelVO> aulaLancadaNaoLancadaRelVOs = new ArrayList<RegistroAulaLancadaNaoLancadaRelVO>(0);
		if (situacaoRegistroAula.equals("RE") || situacaoRegistroAula.equals("RN")) {
			aulaLancadaNaoLancadaRelVOs.addAll(consultaRegistroAulaLancada(unidadeEnsino, curso, turno, turma, disciplina, professor, ano, semestre, dataInicio, dataFim, periodicidade, controlarAcesso, usuario));
		}
		if (situacaoRegistroAula.equals("NA") || situacaoRegistroAula.equals("RN")) {
			aulaLancadaNaoLancadaRelVOs.addAll(consultaRegistroAulaNaoLancada(unidadeEnsino, curso, turno, turma, disciplina, professor, ano, semestre, dataInicio, dataFim, periodicidade, controlarAcesso, usuario));
		}
		Ordenacao.ordenarLista(aulaLancadaNaoLancadaRelVOs, "ordenacao");
		return aulaLancadaNaoLancadaRelVOs;
	}

	private List<RegistroAulaLancadaNaoLancadaRelVO> consultaRegistroAulaLancada(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String periodicidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_nome, professor_nome, ");
		sqlStr.append("registroaula_data, registroaula_dataregistroaula, responsavelregistro_nome, ");
		sqlStr.append("count(distinct registroaula_nraula) as qtdAulasRegistradas ");
		sqlStr.append("from ( ");
		sqlStr.append("select unidadeensino.nome as unidadeensino_nome, turno.nome as turno_nome, turma.identificadorturma, disciplina.nome as disciplina_nome, professor.nome as professor_nome, ");
		sqlStr.append("registroaula.data as registroaula_data, registroaula.dataregistroaula as registroaula_dataregistroaula, responsavelregistro.nome as responsavelregistro_nome, ");
		sqlStr.append("frequenciaaula.presente as frequenciaaula_presente, frequenciaaula.abonado as frequenciaaula_abonado, registroaula.nraula as registroaula_nraula, ");
		sqlStr.append("case when turma.turmaagrupada then array_to_string(array(select distinct c.nome from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma inner join curso as c on c.codigo = t.curso where turmaagrupada.turmaorigem = turma.codigo order by c.nome), ', ') else curso.nome end as curso_nome ");
		sqlStr.append("from frequenciaaula ");
		sqlStr.append("inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sqlStr.append("inner join disciplina on disciplina.codigo = registroaula.disciplina ");
		sqlStr.append("inner join pessoa professor on professor.codigo = registroaula.professor ");
		sqlStr.append("inner join turma on turma.codigo = registroaula.turma ");
		sqlStr.append("left join turmaagrupada on turmaagrupada.turmaOrigem = turma.codigo ");
		sqlStr.append("left join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo in (case when turma.turmaagrupada then turma2.curso else turma.curso end) ");
		sqlStr.append("inner join turno on turno.codigo = turma.turno ");
		sqlStr.append("inner join usuario responsavelregistro on responsavelregistro.codigo = registroaula.responsavelregistroaula ");
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo in (case when turma.turmaagrupada then turma2.unidadeensino else turma.unidadeensino end) ");
		sqlStr.append("where unidadeensino.codigo = ").append(unidadeEnsino);
		sqlStr.append(" and curso.periodicidade = '").append(periodicidade).append("'");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and turno.codigo = ").append(turno);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma2.codigo = ").append(turma).append(")");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sqlStr.append(" and professor.codigo = ").append(professor);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and registroaula.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and registroaula.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" and registroaula.data >= '").append(dataInicio).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" and registroaula.data <= '").append(dataFim).append("'");
		}
		sqlStr.append(") as t ");
		sqlStr.append("group by unidadeensino_nome, curso_nome, turno_nome, identificadorturma, disciplina_nome, professor_nome, ");
		sqlStr.append("registroaula_data, registroaula_dataregistroaula, responsavelregistro_nome ");
		sqlStr.append("order by curso_nome, turno_nome, identificadorturma, disciplina_nome, professor_nome, registroaula_data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRegistroAulaLancada(tabelaResultado);
	}

	private List<RegistroAulaLancadaNaoLancadaRelVO> consultaRegistroAulaNaoLancada(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String periodicidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		SqlRowSet tabelaResultado = getFacadeFactory().getHorarioTurmaFacade().executarConsultaAulasProgramadasNaoRegistradasRegistroAulaLancadaNaoLancadaRel(unidadeEnsino, curso, turno, turma, disciplina, professor, ano, semestre, dataInicio, dataFim, "", "", periodicidade);
		return montarDadosConsultaRegistroAulaNaoLancada(tabelaResultado);
	}

	private List<RegistroAulaLancadaNaoLancadaRelVO> montarDadosConsultaRegistroAulaLancada(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroAulaLancadaNaoLancadaRelVO> aulaLancadaNaoLancadaRelVOs = new ArrayList<RegistroAulaLancadaNaoLancadaRelVO>(0);
		while (tabelaResultado.next()) {
			aulaLancadaNaoLancadaRelVOs.add(montarDadosRegistroAulaLancada(tabelaResultado));
		}
		return aulaLancadaNaoLancadaRelVOs;
	}

	private RegistroAulaLancadaNaoLancadaRelVO montarDadosRegistroAulaLancada(SqlRowSet dadosSQL) throws Exception {
		RegistroAulaLancadaNaoLancadaRelVO obj = new RegistroAulaLancadaNaoLancadaRelVO();
		obj.setCurso(dadosSQL.getString("curso_nome"));
		obj.setDataAula(Uteis.getDataJDBC(dadosSQL.getDate("registroaula_data")));
		obj.setDataLancamento(Uteis.getDataJDBC(dadosSQL.getDate("registroaula_dataregistroaula")));
		obj.setDisciplina(dadosSQL.getString("disciplina_nome"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.setNomeProfessor(dadosSQL.getString("professor_nome"));
		obj.setNomeTurno(dadosSQL.getString("turno_nome"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		obj.setUsuario(dadosSQL.getString("responsavelregistro_nome"));
		obj.setQtdAulasRegistradas(dadosSQL.getInt("qtdAulasRegistradas"));
		return obj;
	}

	private List<RegistroAulaLancadaNaoLancadaRelVO> montarDadosConsultaRegistroAulaNaoLancada(SqlRowSet tabelaResultado) throws Exception {
		List<RegistroAulaLancadaNaoLancadaRelVO> aulaLancadaNaoLancadaRelVOs = new ArrayList<RegistroAulaLancadaNaoLancadaRelVO>(0);
		while (tabelaResultado.next()) {
			aulaLancadaNaoLancadaRelVOs.add(montarDadosRegistroAulaNaoLancada(tabelaResultado));
		}
		return aulaLancadaNaoLancadaRelVOs;
	}

	private RegistroAulaLancadaNaoLancadaRelVO montarDadosRegistroAulaNaoLancada(SqlRowSet dadosSQL) throws Exception {
		RegistroAulaLancadaNaoLancadaRelVO obj = new RegistroAulaLancadaNaoLancadaRelVO();
		obj.setCurso(dadosSQL.getString("curso_nome"));
		obj.setDataAula(Uteis.getDataJDBC(dadosSQL.getDate("horarioturmadia_data")));
		obj.setDisciplina(dadosSQL.getString("disciplina_nome"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.setNomeProfessor(dadosSQL.getString("professor_nome"));
		obj.setNomeTurno(dadosSQL.getString("turno_nome"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		obj.setQtdAulasNaoRegistradas(dadosSQL.getInt("qtdAulasNaoRegistradas"));
		return obj;
	}

	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public static String getIdEntidade() {
		return "RegistroAulaLancadaNaoLancadaRel";
	}
}
