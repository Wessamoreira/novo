package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurmaDisciplinaInclusaoSugeridaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaDisciplinaInclusaoSugeridaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TurmaDisciplinaInclusaoSugerida extends ControleAcesso implements TurmaDisciplinaInclusaoSugeridaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public TurmaDisciplinaInclusaoSugerida() throws Exception {
		super();
	}

	public void validarDados(TurmaDisciplinaInclusaoSugeridaVO obj) throws Exception {
		if (obj.getTurmaVO().getCodigo().equals(0)) {
			throw new Exception("O campo (TURMA) deve ser informado!");
		}
		if (obj.getTurmaDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo (TURMA/DISCIPLINA) deve ser informado!");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaDisciplinaInclusaoSugeridaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO TurmaDisciplinaInclusaoSugerida( turma, turmaDisciplina ) VALUES ( ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getTurmaVO().getCodigo());
				sqlInserir.setInt(2, obj.getTurmaDisciplinaVO().getCodigo());
				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaDisciplinaInclusaoSugeridaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE TurmaDisciplinaInclusaoSugerida set turma=?, turmaDisciplina=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getTurmaVO().getCodigo());
				sqlAlterar.setInt(2, obj.getTurmaDisciplinaVO().getCodigo().intValue());
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaDisciplinaInclusaoSugeridaVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM TurmaDisciplinaInclusaoSugerida WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorTurma(TurmaVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM TurmaDisciplinaInclusaoSugerida WHERE (turma = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, Integer turma, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM TurmaDisciplinaInclusaoSugerida WHERE turma = ").append(turma);
		if (!turmaDisciplinaInclusaoSugeridaVOs.isEmpty()) {
			sb.append(" and codigo not in(");
			boolean virgula = false;
			for (TurmaDisciplinaInclusaoSugeridaVO turmaDisciplinaInclusaoSugeridaVO : turmaDisciplinaInclusaoSugeridaVOs) {
				if (!virgula) {
					sb.append(turmaDisciplinaInclusaoSugeridaVO.getCodigo());
				} else {
					sb.append(", ").append(turmaDisciplinaInclusaoSugeridaVO.getCodigo());
				}
				virgula = true;
			}
			sb.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
		getConexao().getJdbcTemplate().update(sb.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTurmaDisciplinaInclusaoSugeridaVOs(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			TurmaDisciplinaInclusaoSugeridaVO obj = (TurmaDisciplinaInclusaoSugeridaVO) e.next();
			obj.setTurmaVO(turma);
			incluir(obj, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaDisciplinaInclusaoSugeridaVOs(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception {
		excluirTurmaDisciplinaInclusaoSugerida(turma.getTurmaDisciplinaInclusaoSugeridaVOs(), turma.getCodigo(), usuario);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			TurmaDisciplinaInclusaoSugeridaVO obj = (TurmaDisciplinaInclusaoSugeridaVO) e.next();
			if (obj.getCodigo().equals(0)) {
				obj.setTurmaVO(turma);
				incluir(obj, usuario);
			} else {
				alterar(obj, usuario);
			}
		}
	}

	public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception {
		obj.setTurmaDisciplinaInclusaoSugeridaVOs(this.consultaRapidaPorTurma(obj.getCodigo(), usuario));
	}

	public List<TurmaDisciplinaInclusaoSugeridaVO> consultaRapidaPorTurma(Integer turma, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select TurmaDisciplinaInclusaoSugerida.codigo, TurmaDisciplinaInclusaoSugerida.turma, turmadisciplina.codigo AS \"turmadisciplina.codigo\", ");
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
//		OBTEM A CARGA HORÁRIA
		sb.append(" case when gradedisciplina.cargaHoraria is not null then gradedisciplina.cargaHoraria else ");
		sb.append(" case when gradecurriculargrupooptativadisciplina.cargahoraria is not null then gradecurriculargrupooptativadisciplina.cargahoraria else ");
		sb.append(" (");
		sb.append(" select distinct gradedisciplina.cargahoraria from periodoletivo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append(" where periodoletivo.codigo = turma.periodoletivo ");
		sb.append(" )");
		sb.append(" end end AS cargaHoraria, ");
//		OBTEM O NRCREDITOS
		sb.append(" case when gradedisciplina.nrcreditos is not null then gradedisciplina.nrcreditos else ");
		sb.append(" case when gradecurriculargrupooptativadisciplina.nrcreditos is not null then gradecurriculargrupooptativadisciplina.nrcreditos else ");
		sb.append(" (");
		sb.append(" select distinct gradedisciplina.nrcreditos from periodoletivo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append(" where periodoletivo.codigo = turma.periodoletivo ");
		sb.append(" )");
		sb.append(" end end AS nrcreditos ");

		sb.append(" from TurmaDisciplinaInclusaoSugerida ");
		sb.append(" inner join turmadisciplina on turmadisciplina.codigo = TurmaDisciplinaInclusaoSugerida.turmadisciplina ");
		sb.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append(" inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ");
		sb.append(" left join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sb.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append(" where TurmaDisciplinaInclusaoSugerida.turma = ").append(turma);
		sb.append(" order by turma.identificadorTurma, disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaDisciplinaInclusaoSugeridaVO> listaTurmaDisciplinaInclusaoSugeridaVOs = new ArrayList<TurmaDisciplinaInclusaoSugeridaVO>(0);
		while (tabelaResultado.next()) {
			TurmaDisciplinaInclusaoSugeridaVO obj = new TurmaDisciplinaInclusaoSugeridaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
			obj.getTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("turmadisciplina.codigo"));
			obj.getTurmaDisciplinaVO().setTurma(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaDisciplinaVO().getTurmaDescricaoVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaDisciplinaVO().getTurmaDescricaoVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorturma"));
			obj.getTurmaDisciplinaVO().getDisciplina().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getTurmaDisciplinaVO().getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
                        obj.getTurmaDisciplinaVO().getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
			obj.getTurmaDisciplinaVO().getGradeDisciplinaVO().setNrCreditos(tabelaResultado.getInt("nrCreditos"));
			
//                        obj.getTurmaDisciplinaVO().getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
//			obj.getTurmaDisciplinaVO().getGradeDisciplinaVO().setNrCreditos(tabelaResultado.getInt("nrCreditos"));
			listaTurmaDisciplinaInclusaoSugeridaVOs.add(obj);
		}
		return listaTurmaDisciplinaInclusaoSugeridaVOs;
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisciplinaEquivalenteVO</code>.
	 * 
	 * @return O objeto da classe <code>DisciplinaEquivalenteVO</code> com os
	 *         dados devidamente montados.
	 */
	public static TurmaDisciplinaInclusaoSugeridaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		TurmaDisciplinaInclusaoSugeridaVO obj = new TurmaDisciplinaInclusaoSugeridaVO();
		obj.getTurmaDisciplinaVO().setCodigo(dadosSQL.getInt("codigo"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public void adicionarTurmaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, List<TurmaDisciplinaVO> turmaDisciplinaVOs) {
		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
			if (turmaDisciplinaVO.getSelecionado()) {
				Boolean disciplinaIncluida = realizarVerificacaoExistenciaDisciplinaInclusaoSugerida(turmaDisciplinaInclusaoSugeridaVOs, turmaDisciplinaVO);
				if (!disciplinaIncluida) {
					TurmaDisciplinaInclusaoSugeridaVO obj = new TurmaDisciplinaInclusaoSugeridaVO();
					obj.setTurmaDisciplinaVO(turmaDisciplinaVO);
					turmaDisciplinaInclusaoSugeridaVOs.add(obj);
				}
			}
		}
	}

	public Boolean realizarVerificacaoExistenciaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, TurmaDisciplinaVO turmaDisciplinaVO) {
		for (TurmaDisciplinaInclusaoSugeridaVO turmaDisciplinaInclusaoSugeridaVO : turmaDisciplinaInclusaoSugeridaVOs) {
			if (turmaDisciplinaInclusaoSugeridaVO.getTurmaDisciplinaVO().getCodigo().equals(turmaDisciplinaVO.getCodigo())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public void removerTurmaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, TurmaDisciplinaInclusaoSugeridaVO obj) {
		int index = 0;
		for (TurmaDisciplinaInclusaoSugeridaVO turmaDisciplinaInclusaoSugeridaVO : turmaDisciplinaInclusaoSugeridaVOs) {
			if (turmaDisciplinaInclusaoSugeridaVO.getTurmaDisciplinaVO().getCodigo().equals(obj.getTurmaDisciplinaVO().getCodigo())) {
				turmaDisciplinaInclusaoSugeridaVOs.remove(index);
				return;
			}
			index++;
		}
	}

	public void validarDadosSelecaoTurma(TurmaVO turmaPrincipal, TurmaVO turmaSugerida) throws Exception {
		if (turmaPrincipal.getCodigo().equals(turmaSugerida.getCodigo())) {
			throw new Exception("Não é possível selecionar a turma " + turmaSugerida.getIdentificadorTurma().toUpperCase() + ", porque é igual a turma principal! Favor selecione outra turma. ");
		}
	}
}
