/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioProfessorDiaItemInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class HorarioProfessorDiaItem extends ControleAcesso implements HorarioProfessorDiaItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 921773475978261143L;
	private static String idEntidade;

	@Override
	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getNomeRel() + ".jrxml");
	}

	@Override
	public String designIReportRelatorio(String layout) {
		if (layout.equals("LAYOUT_DIA_A_DIA")) {
			return (caminhoBaseRelatorio() + getNomeRel() + ".jrxml");
		} else {
			return (caminhoBaseRelatorio() + "AgendaProfessorComAssinaturaRel" + ".jrxml");
		}
	}

	@Override
	public String designIReportRelatorioExcel() {
		return (caminhoBaseRelatorio() + getNomeExcelRel() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioExcel(String layout) {
		if (layout.equals("LAYOUT_DIA_A_DIA")) {
			return (caminhoBaseRelatorio() + getNomeRel() + ".jrxml");
		} else {
			return (caminhoBaseRelatorio() + "AgendaProfessorComAssinaturaRel" + ".jrxml");
		}
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#persistir(negocio.comuns.academico.HorarioTurmaDiaItemVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(HorarioProfessorDiaItemVO horarioProfessorDiaItemVO, UsuarioVO usuarioVO) throws Exception {
		alterar(horarioProfessorDiaItemVO, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioProfessorDiaItemVO horarioProfessorDiaItemVO, final UsuarioVO usuarioVO) throws Exception {
		horarioProfessorDiaItemVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO horarioprofessordiaitem ");
				sql.append(" (horarioprofessordia, data, nrAula, duracaoAula, horario, horarioInicio, horarioTermino, turma, disciplina, sala, horarioturmadiaitem, usuarioLiberacaoChoqueHorario) values ");
				sql.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, horarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getCodigo());
				ps.setDate(x++, Uteis.getDataJDBC(horarioProfessorDiaItemVO.getData()));
				ps.setInt(x++, horarioProfessorDiaItemVO.getNrAula());
				ps.setInt(x++, horarioProfessorDiaItemVO.getDuracaoAula());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorario());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorarioInicio());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorarioTermino());
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getTurmaVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getTurmaVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getDisciplinaVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getSala())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getSala().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				Uteis.setValuePreparedStatement(horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario(), x++, ps);
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}

		}));
		horarioProfessorDiaItemVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioProfessorDiaItemVO horarioProfessorDiaItemVO, final UsuarioVO usuarioVO) throws Exception {
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE horarioprofessordiaitem set ");
				sql.append(" horarioprofessordia = ?, data =?, nrAula =?, duracaoAula=?, horario=?, horarioInicio=?, horarioTermino=?, turma=?, disciplina=?, ");
				sql.append(" sala=?, horarioturmadiaitem=?,  usuarioLiberacaoChoqueHorario=? ");
				sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, horarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getCodigo());
				ps.setDate(x++, Uteis.getDataJDBC(horarioProfessorDiaItemVO.getData()));
				ps.setInt(x++, horarioProfessorDiaItemVO.getNrAula());
				ps.setInt(x++, horarioProfessorDiaItemVO.getDuracaoAula());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorario());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorarioInicio());
				ps.setString(x++, horarioProfessorDiaItemVO.getHorarioTermino());
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getTurmaVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getTurmaVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getDisciplinaVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getSala())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getSala().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO())) {
					ps.setInt(x++, horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				Uteis.setValuePreparedStatement(horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario(), x++, ps);
				ps.setInt(x++, horarioProfessorDiaItemVO.getCodigo());
				return ps;
			}
		}) == 0) {
			horarioProfessorDiaItemVO.setNovoObj(true);
			horarioProfessorDiaItemVO.setCodigo(0);
			incluir(horarioProfessorDiaItemVO, usuarioVO);
			return;
		}
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#persistirHorarioTurmaDiaItem(negocio.comuns.academico.HorarioTurmaDiaVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirHorarioTurmaDiaItem(HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO) throws Exception {
		for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
			if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO) && !Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getDisciplinaVO()) && !Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getTurmaVO())) {
				excluir(horarioProfessorDiaItemVO.getCodigo(), usuarioVO);
			} else if (Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getDisciplinaVO()) && Uteis.isAtributoPreenchido(horarioProfessorDiaItemVO.getTurmaVO())) {
				horarioProfessorDiaItemVO.setData(horarioProfessorDiaVO.getData());
				horarioProfessorDiaItemVO.setHorarioProfessorDiaVO(horarioProfessorDiaVO);
				persistir(horarioProfessorDiaItemVO, usuarioVO);
			}
		}
	}

	public StringBuilder getSqlConsulta() {
		StringBuilder sql = new StringBuilder("select horarioprofessordiaitem.codigo, horarioprofessordiaitem.horarioprofessordia, horarioprofessordiaitem.data, ");
		sql.append(" horarioprofessordiaitem.turma, horarioprofessordiaitem.disciplina, horarioprofessordiaitem.sala, horarioprofessordiaitem.duracaoaula, horarioprofessordiaitem.nrAula, ");
		sql.append(" horarioprofessordiaitem.horario, horarioprofessordiaitem.horarioinicio, horarioprofessordiaitem.horarioTermino, ");
		sql.append(" disciplina.nome as \"disciplina.nome\", sala.sala as \"sala.sala\", localaula.local as \"localaula.local\", ");
		sql.append(" professor.nome as \"professor.nome\", professor.email as \"professor.email\", professor.celular as \"professor.celular\", ");
		sql.append(" professor.telefoneres as \"professor.telefoneres\", professor.telefonecomer as \"professor.telefonecomer\", professor.telefonerecado as \"professor.telefonerecado\", ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", curso.nome as \"curso.nome\", horarioturmadiaitem, ");
		sql.append(" ulch.codigo as \"ulch.codigo\", ulch.username as \"ulch.username\" ");
		sql.append(" from horarioprofessordiaitem ");
		sql.append(" inner join horarioprofessordia on horarioprofessordia.codigo = horarioprofessordiaitem.horarioprofessordia ");
		sql.append(" inner join horarioprofessor on horarioprofessor.codigo = horarioprofessordia.horarioprofessor ");
		sql.append(" inner join pessoa as professor on professor.codigo = horarioprofessor.professor ");
		sql.append(" left join turma on turma.codigo = horarioprofessordiaitem.turma ");
		sql.append(" left join curso on curso.codigo = turma.curso ");
		sql.append(" left join disciplina on disciplina.codigo = horarioprofessordiaitem.disciplina ");
		sql.append(" left join salalocalaula as sala on sala.codigo = horarioprofessordiaitem.sala ");
		sql.append(" left join localaula on localaula.codigo = sala.localaula ");
		sql.append(" left join usuario as ulch on ulch.codigo = horarioprofessordiaitem.usuarioLiberacaoChoqueHorario ");
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#consultarPorHorarioTurmaDia(java.lang.Integer)
	 */
	@Override
	public List<HorarioProfessorDiaItemVO> consultarPorHorarioProfessorDia(Integer horarioProfessorDia) throws Exception {
		StringBuilder sql = getSqlConsulta();
		sql.append(" where horarioProfessorDia.codigo = ").append(horarioProfessorDia);
		sql.append(" order by nrAula");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	public List<HorarioProfessorDiaItemVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<HorarioProfessorDiaItemVO> horarioProfessorDiaItemVOs = new ArrayList<HorarioProfessorDiaItemVO>(0);
		while (rs.next()) {
			horarioProfessorDiaItemVOs.add(montarDados(rs));
		}
		return horarioProfessorDiaItemVOs;
	}

	public HorarioProfessorDiaItemVO montarDados(SqlRowSet rs) throws Exception {
		HorarioProfessorDiaItemVO HorarioProfessorDiaItemVO = new HorarioProfessorDiaItemVO();
		HorarioProfessorDiaItemVO.setNovoObj(false);
		HorarioProfessorDiaItemVO.setCodigo(rs.getInt("codigo"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().setCodigo(rs.getInt("horarioProfessorDia"));
		HorarioProfessorDiaItemVO.getTurmaVO().setCodigo(rs.getInt("turma.codigo"));
		HorarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		HorarioProfessorDiaItemVO.getTurmaVO().getCurso().setNome(rs.getString("curso.nome"));
		HorarioProfessorDiaItemVO.setHorario(rs.getString("horario"));
		HorarioProfessorDiaItemVO.setHorarioInicio(rs.getString("horarioInicio"));
		HorarioProfessorDiaItemVO.setHorarioTermino(rs.getString("horarioTermino"));
		HorarioProfessorDiaItemVO.setNrAula(rs.getInt("nrAula"));
		HorarioProfessorDiaItemVO.setDuracaoAula(rs.getInt("duracaoAula"));
		HorarioProfessorDiaItemVO.setData(rs.getDate("data"));
		HorarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		HorarioProfessorDiaItemVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		HorarioProfessorDiaItemVO.getDisciplinaVO().setNovoObj(HorarioProfessorDiaItemVO.getDisciplinaVO().getCodigo().equals(0));
		HorarioProfessorDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
		HorarioProfessorDiaItemVO.getSala().setNovoObj(HorarioProfessorDiaItemVO.getSala().getCodigo().equals(0));
		HorarioProfessorDiaItemVO.getSala().setSala(rs.getString("sala.sala"));
		HorarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("localAula.local"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().setData(rs.getDate("data"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setCodigo(rs.getInt("professor"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setNovoObj(false);
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setNome(rs.getString("professor.nome"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setEmail(rs.getString("professor.email"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setTelefoneComer(rs.getString("professor.telefoneComer"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setTelefoneRecado(rs.getString("professor.telefoneRecado"));
		HorarioProfessorDiaItemVO.getHorarioProfessorDiaVO().getHorarioProfessor().getProfessor().setTelefoneRes(rs.getString("professor.telefoneRes"));
		HorarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setCodigo(rs.getInt("horarioturmadiaitem"));
		HorarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setCodigo(rs.getInt("ulch.codigo"));
		HorarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setUsername(rs.getString("ulch.username"));
		return HorarioProfessorDiaItemVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#consultarPorTurmaDisciplinaProfessorSalaNumeroAula(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<HorarioProfessorDiaItemVO> consultarParametrizada(Integer turma, Integer disciplina, Integer professor, Integer sala, Integer numeroAula, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sql = getSqlConsulta();
		sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioprofessordiaitem.data", false));

		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sql.append(" and professor.codigo = ").append(professor);
		}
		if (Uteis.isAtributoPreenchido(sala)) {
			sql.append(" and sala.codigo = ").append(sala);
		}
		if (Uteis.isAtributoPreenchido(numeroAula)) {
			sql.append(" and horarioprofessordiaitem.nrAula = ").append(numeroAula);
		}
		sql.append(" order by turma.codigo, data, nraula ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	/**
	 * @return the idEntidade
	 */
	public static String getIdEntidade() {
		return idEntidade;
	}

	/**
	 * @param aIdEntidade
	 *            the idEntidade to set
	 */
	public static void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	/**
	 * @return the nomeRel
	 */
	public static String getNomeRel() {
		return "AgendaProfessorRel";
	}

	public static String getNomeExcelRel() {
		return "AgendaProfessorExcelRel";
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer horarioProfessorDiaItem, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.excluir(getIdEntidade());
		String sql = "DELETE FROM horarioProfessorDiaItem WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { horarioProfessorDiaItem });
	}

	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarNovaDisciplinaHorarioProfessorDiaItemPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.excluir(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder(" UPDATE horarioProfessorDiaItem set disciplina = ").append(novaDisciplina);
		sqlStr.append(" WHERE horarioProfessorDiaItem.turma = ").append(turma).append(" ");
		sqlStr.append(" and horarioProfessorDiaItem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" and horarioProfessorDiaItem.horarioTurmaDiaItem in(");
		sqlStr.append(" select horarioTurmaDiaItem.codigo FROM horarioTurmaDiaItem  ");
		sqlStr.append(" INNER JOIN HorarioTurmadia ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo  ");
		sqlStr.append(" INNER JOIN HorarioTurma ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo");
		sqlStr.append(" WHERE HorarioTurma.turma = ").append(turma).append(" ");
		sqlStr.append(" AND HorarioTurmadiaitem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorTurmaPorDisciplina(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.excluir(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder(" DELETE FROM horarioProfessorDiaItem  ");
		sqlStr.append(" WHERE horarioProfessorDiaItem.turma = ").append(turma).append(" ");
		sqlStr.append(" and horarioProfessorDiaItem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" and horarioProfessorDiaItem.horarioTurmaDiaItem in(");
		sqlStr.append(" select horarioTurmaDiaItem.codigo FROM horarioTurmaDiaItem  ");
		sqlStr.append(" INNER JOIN HorarioTurmadia ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo  ");
		sqlStr.append(" INNER JOIN HorarioTurma ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo");
		sqlStr.append(" WHERE HorarioTurma.turma = ").append(turma).append(" ");
		sqlStr.append(" AND HorarioTurmadiaitem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
		
	}
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoRegistroAulaAutomatico(HorarioProfessorDiaItemVO horarioProfessorDiaItemVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" UPDATE horarioProfessorDiaItem ");
		sqlStr.append(" set registroAulaAutomaticoSucesso = ").append(horarioProfessorDiaItemVO.getRegistroAulaAutomaticoSucesso()).append(",");
		sqlStr.append(" motivoErroRegistroAulaAutomatico = '").append(horarioProfessorDiaItemVO.getMotivoErroRegistroAulaAutomatico()).append("'");
		sqlStr.append(" WHERE horarioProfessorDiaItem.codigo = ").append(horarioProfessorDiaItemVO.getCodigo()).append(" ");
		getConexao().getJdbcTemplate().update(sqlStr.toString());

	}

}
