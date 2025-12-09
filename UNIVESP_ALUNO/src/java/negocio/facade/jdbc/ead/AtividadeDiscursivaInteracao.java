package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.enumeradores.InteragidoPorEnum;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AtividadeDiscursivaInteracaoInterfaceFacade;

/*
 * @author Victor Hugo 17/09/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AtividadeDiscursivaInteracao extends ControleAcesso implements AtividadeDiscursivaInteracaoInterfaceFacade, Serializable {

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AtividadeDiscursivaInteracao.idEntidade = idEntidade;
	}

	public AtividadeDiscursivaInteracao() throws Exception {
		super();
		setIdEntidade("AtividadeDiscursivaInteracao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(atividadeDiscursivaInteracaoVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO atividadediscursivainteracao (interacao, interagidopor, " + "datainteracao, atividadediscursivarespostaaluno, usuariointeracao, interacaojalida) " + "VALUES (?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			atividadeDiscursivaInteracaoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, atividadeDiscursivaInteracaoVO.getInteracao());
						sqlInserir.setString(2, atividadeDiscursivaInteracaoVO.getInteragidoPorEnum().name());
						sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(atividadeDiscursivaInteracaoVO.getDataInteracao()));
						sqlInserir.setInt(4, atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getCodigo());
						sqlInserir.setInt(5, usuarioVO.getCodigo());
						sqlInserir.setBoolean(6, atividadeDiscursivaInteracaoVO.getInteracaoJaLida());

					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						atividadeDiscursivaInteracaoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(atividadeDiscursivaInteracaoVO, usuarioVO);
		} catch (Exception e) {
			atividadeDiscursivaInteracaoVO.setNovoObj(Boolean.TRUE);
			atividadeDiscursivaInteracaoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(AtividadeDiscursivaInteracaoVO obj) throws Exception {
		if (obj.getInteragidoPorEnum().isAluno() && obj.getAtividadeDiscursivaRepostaAlunoVO().getSituacaoRespostaAtividadeDiscursiva().isAguardandoAvaliacaoProfessor()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursivaInteracao_situacaoNaoPermitiEnviarInteracao"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (atividadeDiscursivaInteracaoVO.getCodigo().equals(0)) {
			incluir(atividadeDiscursivaInteracaoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(atividadeDiscursivaInteracaoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE atividadediscursivainteracao set interacao = ?, interagidopor = ?, datainteracao = ?, " + "atividadediscursivarespostaaluno = ?, usuariointeracao = ?, interacaojalida = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setString(1, atividadeDiscursivaInteracaoVO.getInteracao());
					sqlAlterar.setString(2, atividadeDiscursivaInteracaoVO.getInteragidoPorEnum().toString());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(atividadeDiscursivaInteracaoVO.getDataInteracao()));
					sqlAlterar.setInt(4, atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getCodigo());
					sqlAlterar.setInt(5, atividadeDiscursivaInteracaoVO.getUsuarioVO().getCodigo());
					sqlAlterar.setBoolean(6, atividadeDiscursivaInteracaoVO.getInteracaoJaLida());
					sqlAlterar.setInt(7, atividadeDiscursivaInteracaoVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM atividadediscursivainteracao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, atividadeDiscursivaInteracaoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AtividadeDiscursivaInteracaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AtividadeDiscursivaInteracaoVO obj = new AtividadeDiscursivaInteracaoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setInteracao(tabelaResultado.getString("interacao"));
		obj.setInteragidoPorEnum(InteragidoPorEnum.valueOf(tabelaResultado.getString("interagidopor")));
		obj.setDataInteracao(tabelaResultado.getTimestamp("datainteracao"));
		obj.getAtividadeDiscursivaRepostaAlunoVO().setCodigo(tabelaResultado.getInt("atividadediscursivarespostaaluno"));
		obj.getUsuarioVO().setCodigo(tabelaResultado.getInt("usuariointeracao"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			return obj;
		}
		return obj;
	}

	@Override
	public List<AtividadeDiscursivaInteracaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList<AtividadeDiscursivaInteracaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	public List<AtividadeDiscursivaInteracaoVO> consultarInteracoesPorChaveAtividadeDiscursivaAluno(Integer atividadeDiscursivaAluno, Integer limite, Integer offSet, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AtividadeDiscursivaInteracaoVO> lista = new ArrayList<AtividadeDiscursivaInteracaoVO>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from atividadediscursivainteracao where atividadediscursivarespostaaluno = ").append(atividadeDiscursivaAluno);
		sql.append(" order by datainteracao desc");
		sql.append(" limit ").append(limite).append(" offset ").append(offSet);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		return lista = montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}

	@Override
	public Integer consultarTotalRegistroInteracao(Integer atividadeDiscursivaAluno, Integer usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(codigo) as qtde from atividadediscursivainteracao ");
		sb.append("where atividadediscursivarespostaaluno = ").append(atividadeDiscursivaAluno);
		return getConexao().getJdbcTemplate().queryForObject(sb.toString(), Integer.class);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarInteracaoJaLidaAoVisualizarATela(Integer codigoAtividadeDiscursivaRespostaAluno, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		if (usuarioVO.getIsApresentarVisaoAluno()) {
			sqlStr.append("update atividadediscursivainteracao set interacaojalida = ").append(true);
			sqlStr.append(" where atividadediscursivarespostaaluno = ").append(codigoAtividadeDiscursivaRespostaAluno);
			sqlStr.append(" and atividadediscursivainteracao.interagidopor = '").append(InteragidoPorEnum.PROFESSOR).append("'");
		} else if (usuarioVO.getIsApresentarVisaoProfessor()) {
			sqlStr.append("update atividadediscursivainteracao set interacaojalida = ").append(true);
			sqlStr.append(" where atividadediscursivarespostaaluno = ").append(codigoAtividadeDiscursivaRespostaAluno);
			sqlStr.append(" and atividadediscursivainteracao.interagidopor = '").append(InteragidoPorEnum.ALUNO).append("'");
		} else {
			sqlStr.append("");
		}
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaInteracaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM atividadediscursivainteracao WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AtividadeDiscursivaInteracaoVO();
	}
	
	/**
	 * Método responsável por consultar a quantidade de interações na visão do aluno havendo a possibilidade de consultar tanto do professo quanto do aluno.
	 */
	@Override
	public Integer consultarQtdeInteracoesProfessorPorMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina, Boolean interacoesAluno, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(atividadediscursivainteracao.codigo) as qtdeInteracao from atividadediscursivainteracao");
		sql.append(" inner join atividadediscursivarespostaaluno on atividadediscursivainteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina");
		sql.append(" where matriculaperiodoturmadisciplina.codigo = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sql.append(" and atividadediscursivainteracao.interacaojalida = 'f'");
		if(interacoesAluno) {
			sql.append("and atividadediscursivainteracao.interagidopor = 'ALUNO'");
		} else {
			sql.append("and atividadediscursivainteracao.interagidopor = 'PROFESSOR'");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		Integer interacoes = 0;
		if (tabelaResultado.next()) {
			interacoes = tabelaResultado.getInt("qtdeInteracao");
		}
		return interacoes;
	}
	
	/**
	 * Método responsável por consultar as interacoes do aluno na visão do professor apresentando todas as interações de todas as atividades existentes do professor.
	 */
	@Override
	public Integer consultarQtdeInteracoesAlunosPorCodigoProfessor(Integer codigoProfessor, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder()
				.append("SELECT sum (qtdeAguardandoAvaliacaoProfessor + quantidadeInteracoes) as qtde from (")
				.append(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().getSQLConsultarInteracoesNaoLidasAlunosPorCodigoProfessor(codigoProfessor))
				.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		Integer interacoes = 0;
		if (tabelaResultado.next()) {
			interacoes = tabelaResultado.getInt("qtde");
		}
		return interacoes;
	}
	
	/**
	 * Método responsável por consultar todas as interações das atividades que o aluno possui na visão do aluno.
	 */
	@Override
	public Integer consultarQtdeInteracoesPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		if (!Uteis.isAtributoPreenchido(matricula)) {
			//Por algum motivo ainda desconhecido tem algum usuario nulo com matricula nula entrando nesta consulta, como ainda não foi
			// possível descobrir a causa se chegar vázia a matrícula não será continuada a consulta pois estava gerando lentidão no sistema.
			return 0;
		}
		
		sql.append(" SELECT  count(atividadediscursiva) as qtde FROM (");
		sql.append(" select distinct atividadediscursivarespostaaluno.atividadediscursiva from atividadediscursivainteracao");
		sql.append(" inner join atividadediscursivarespostaaluno on atividadediscursivainteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sql.append(" where matricula.matricula = '").append(matricula).append("'");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" and atividadediscursivainteracao.interacaojalida = 'f'");
		sql.append("and atividadediscursivainteracao.interagidopor = 'PROFESSOR'");
		sql.append(" AND matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual()).append("'");
		sql.append(" AND matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("'");
		
		sql.append(" UNION ");
		
		sql.append(" SELECT distinct atividadediscursiva.codigo ");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN matriculaperiodo ON  MatriculaPeriodo.matricula = Matricula.matricula  ");
		sql.append(" INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo  ");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sql.append(" INNER JOIN turma on (turma.codigo = matriculaperiodoturmadisciplina.turma) "); //  or turma.codigo in (select turmaorigem from turmaAgrupada where turma =  matriculaperiodoturmadisciplina.turma)) ");
		sql.append(" INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append(" INNER JOIN atividadediscursiva on atividadediscursiva.disciplina  = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" AND atividadediscursiva.ano  = matriculaperiodoturmadisciplina.ano and atividadediscursiva.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append(" LEFT JOIN atividadediscursivarespostaaluno on atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" AND atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo ");
		sql.append(" left join turma as turmaavaliacao on turmaavaliacao.codigo =  atividadediscursiva.turma ");

		sql.append(" where  ((atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' and  (");		
		sql.append("    (turmaavaliacao.turmaagrupada = false and turmaavaliacao.subturma = false and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma)  ");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmapratica )");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmateorica )");
		sql.append(" or (turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.GERAL.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma )");
		sql.append(" or (turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
		sql.append(" )) ");
		sql.append(" or  (atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("' and atividadediscursiva.matriculaPeriodoTurmaDisciplina = matriculaperiodoturmadisciplina.codigo )) ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append("and matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		}
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" AND matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual()).append("'");
		sql.append(" AND matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("'");
		sql.append(" and atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva <> 'AVALIADO'");
		sql.append(" ) AS T");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		Integer interacoes = 0;
		if (tabelaResultado.next()) {
			interacoes = tabelaResultado.getInt("qtde");
		}
		return interacoes;
	}	
}
