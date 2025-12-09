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
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorTurnoNumeroAulaVO;
import negocio.comuns.academico.HorarioProfessorTurnoVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaProfessorDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.GraduacaoPosGraduacaoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioProfessorDiaInterfaceFacade;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 * 
 * @author Otimize-TI
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class HorarioProfessorDia extends ControleAcesso implements HorarioProfessorDiaInterfaceFacade {

	private static final long serialVersionUID = 8538291071623870486L;

	protected static String idEntidade;

	public HorarioProfessorDia() throws Exception {
		super();
		setIdEntidade("ProgramacaoAula");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#novo()
	 */
	public HorarioProfessorDiaVO novo() throws Exception {
		HorarioProfessorDia.incluir(getIdEntidade());
		HorarioProfessorDiaVO obj = new HorarioProfessorDiaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#incluir
	 * (negocio.comuns.academico.HorarioProfessorDiaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioProfessorDiaVO obj, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "SELECT * FROM HorarioProfessorDia WHERE HorarioProfessorDia.horarioProfessor = " + obj.getHorarioProfessor().getCodigo() + " and HorarioProfessordia.data = '" + Uteis.getDataJDBC(obj.getData()) + "' ORDER BY HorarioProfessordia.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			String sql = "DELETE FROM HorarioProfessorDia WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { tabelaResultado.getInt("codigo") });
		}
		final String sql = "INSERT INTO HorarioProfessorDia( data, horarioProfessor) VALUES ( ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
				if (obj.getHorarioProfessor().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getHorarioProfessor().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getHorarioProfessorDiaItemFacade().persistirHorarioTurmaDiaItem(obj, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#alterar
	 * (negocio.comuns.academico.HorarioProfessorDiaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioProfessorDiaVO obj, final UsuarioVO usuarioVO) throws Exception {

		final String sql = "UPDATE HorarioProfessorDia set data=?, horarioProfessor=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
				if (obj.getHorarioProfessor().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getHorarioProfessor().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setInt(3, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, usuarioVO);
			return;
		}
		;
		getFacadeFactory().getHorarioProfessorDiaItemFacade().persistirHorarioTurmaDiaItem(obj, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#excluir
	 * (negocio.comuns.academico.HorarioProfessorDiaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(HorarioProfessorDiaVO obj, UsuarioVO usuarioVO) throws Exception {
		String sql = "DELETE FROM HorarioProfessorDia WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorDataProfessor(Date dataAula, Integer codigoProfessor, UsuarioVO usuario) throws Exception {
		HorarioProfessorDia.excluir(getIdEntidade());
		StringBuilder sql = new StringBuilder("DELETE FROM HorarioProfessorDia ");
		sql.append("WHERE HorarioProfessorDia.codigo in ( ");
		sql.append("select hpd.codigo from horarioprofessordia hpd ");
		sql.append("inner join horarioprofessor hp on hp.codigo = hpd.horarioprofessor ");
		sql.append("where data = '").append(Uteis.getDataJDBC(dataAula)).append("' and hp.professor = ").append(codigoProfessor).append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * consultarPorDiaProfessorTurno(java.util.Date, java.lang.Integer,
	 * java.lang.Integer, boolean, int)
	 */
	public HorarioProfessorDiaVO consultarPorDiaProfessorTurno(Date valorConsulta, Integer professor, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioProfessorDia.data, HorarioProfessorDia.horarioProfessor, HorarioProfessorDia.codigo FROM HorarioProfessorDia inner join HorarioProfessor on HorarioProfessorDia.horarioProfessor = HorarioProfessor.codigo and HorarioProfessor.professor = " + professor.intValue() + " and HorarioProfessor.turno = " + turno.intValue() + " and HorarioProfessorDia.data::date  = '" + Uteis.getDataJDBC(valorConsulta) + "' " + " group by HorarioProfessorDia.data, HorarioProfessorDia.horarioProfessor, HorarioProfessorDia.codigo ORDER BY HorarioProfessordia.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Responsï¿½vel por montar os dados de vï¿½rios objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da
	 * operaï¿½ï¿½o <code>montarDados</code> que realiza o trabalho para um
	 * objeto por vez.
	 * 
	 * @return List Contendo vï¿½rios objetos da classe
	 *         <code>HorarioProfessorDiaVO</code> resultantes da consulta.
	 */
	public static List<HorarioProfessorDiaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<HorarioProfessorDiaVO> vetResultado = new ArrayList<HorarioProfessorDiaVO>(0);
		while (tabelaResultado.next()) {
			HorarioProfessorDiaVO obj = new HorarioProfessorDiaVO();
			obj = montarDados(tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsï¿½vel por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>HorarioProfessorDiaVO</code>.
	 * 
	 * @return O objeto da classe <code>HorarioProfessorDiaVO</code> com os
	 *         dados devidamente montados.
	 */
	public static HorarioProfessorDiaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		HorarioProfessorDiaVO obj = new HorarioProfessorDiaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));		
		obj.getHorarioProfessor().setCodigo(new Integer(dadosSQL.getInt("horarioProfessor")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			obj.setHorarioProfessorDiaItemVOs(getFacadeFactory().getHorarioProfessorDiaItemFacade().consultarPorHorarioProfessorDia(obj.getCodigo()));
		}
		return obj;
	}

	/***
	 * Mï¿½todo responsï¿½vel por montar os dados das aulas de um dia
	 * especï¿½fico
	 * 
	 * @param horarioProfessorDia
	 * @throws Exception
	 */
	public static void montarDadosHorarioTurmaDiaItem(HorarioProfessorDiaVO horarioProfessorDia, String nivelEducacional) throws Exception {
		SqlRowSet tabelaResultado;
		for (HorarioProfessorDiaItemVO diaItem : horarioProfessorDia.getHorarioProfessorDiaItemVOs()) {
			if (!diaItem.getDisciplinaLivre() && !diaItem.getTurmaLivre()) {
				StringBuilder sqlStr = new StringBuilder();
				sqlStr.append("select disciplina.nome, identificadorTurma, sala, curso.codigo as \"curso.codigo\", curso.niveleducacional, unidadeEnsino.codigo as  \"unidadeEnsino.codigo\", unidadeEnsino.nome as  \"unidadeEnsino.nome\", ");
				sqlStr.append(" cidade.nome as  \"cidade.nome\", cidade.codigo as  \"cidade.codigo\" ");
				sqlStr.append(" from disciplina, turma ");
				sqlStr.append(" left join curso on curso.codigo = turma.curso ");
				sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino");
				sqlStr.append(" inner join cidade on unidadeEnsino.cidade = cidade.codigo where turma.codigo = ");
				sqlStr.append(diaItem.getTurmaVO().getCodigo());
				sqlStr.append(" and disciplina.codigo = ");
				sqlStr.append(diaItem.getDisciplinaVO().getCodigo());
				if (!nivelEducacional.equals("")) {
					sqlStr.append(" and curso.nivelEducacional = '");
					sqlStr.append(nivelEducacional);
					sqlStr.append("'");
				}
				// String sqlStr =
				// "select disciplina.nome, identificadorTurma, sala, curso.niveleducacional, unidadeEnsino.codigo as  \"unidadeEnsino.codigo\", unidadeEnsino.nome as  \"unidadeEnsino.nome\", cidade.nome as  \"cidade.nome\", cidade.codigo as  \"cidade.codigo\" from disciplina, turma  left join curso on curso.codigo = turma.curso inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino inner join cidade on unidadeEnsino.cidade = cidade.codigo where turma.codigo = "
				// + diaItem.getTurmaVO().getCodigo() +
				// " and disciplina.codigo = " +
				// diaItem.getDisciplinaVO().getCodigo();
				tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				if (tabelaResultado.next()) {
					diaItem.getDisciplinaVO().setNome(tabelaResultado.getString("nome"));
					diaItem.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
					diaItem.getTurmaVO().setSala(tabelaResultado.getString("sala"));
					diaItem.getTurmaVO().getCurso().setNivelEducacional(tabelaResultado.getString("niveleducacional"));
					diaItem.getTurmaVO().getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
					diaItem.getTurmaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
					diaItem.getTurmaVO().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino.codigo"));
					diaItem.getTurmaVO().getUnidadeEnsino().getCidade().setNome(tabelaResultado.getString("cidade.nome"));
					diaItem.getTurmaVO().getUnidadeEnsino().getCidade().setCodigo(tabelaResultado.getInt("cidade.codigo"));
				}
				sqlStr = null;
			}
		}
		tabelaResultado = null;
	}

	public static List<HorarioProfessorDiaItemVO> montarDadosHorarioTurmaDiaItemTurmaAgrupada(HorarioProfessorDiaItemVO diaItem, Integer turma, Integer curso, String nivelEducacional) throws Exception {
		SqlRowSet tabelaResultado;
		List<HorarioProfessorDiaItemVO> listaHorarioProfessorDiaItemVO = new ArrayList<HorarioProfessorDiaItemVO>(0);
		if (!diaItem.getDisciplinaLivre() && !diaItem.getTurmaLivre()) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select disciplina.nome, turma.identificadorTurma as \"turma.identificadorTurma\", turma.sala as \"turma.sala\", ");
			sqlStr.append("curso.codigo as \"curso.codigo\", curso.niveleducacional, curso.periodicidade as \"curso.periodicidade\", curso.liberarRegistroAulaEntrePeriodo as \"curso.liberarRegistroAulaEntrePeriodo\", ");
			sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as  \"unidadeEnsino.nome\", ");
			sqlStr.append("cidade.nome as  \"cidade.nome\", cidade.codigo as  \"cidade.codigo\", ");
			sqlStr.append("t2.turmaagrupada as \"t2.turmaagrupada\", turma.codigo as \"turma.codigo\" ");
			sqlStr.append("from disciplina, turma ");
			sqlStr.append("INNER JOIN turmaagrupada ta on ta.turma = turma.codigo ");
			sqlStr.append("left join turma t2 on t2.codigo = ta.turmaorigem ");
			sqlStr.append("left join curso on curso.codigo = turma.curso ");
			sqlStr.append("inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
			sqlStr.append("inner join cidade on unidadeEnsino.cidade = cidade.codigo ");
			sqlStr.append("where 1 = 1 ");
			if (!diaItem.getTurmaVO().getCodigo().equals(0)) {
				sqlStr.append("and ta.turmaorigem = ").append(diaItem.getTurmaVO().getCodigo()).append(" ");
			}
			if (turma != null && !turma.equals(0)) {
				sqlStr.append("and ta.turma = ").append(turma).append(" ");
			}
			if (curso != null && !curso.equals(0)) {
				sqlStr.append("and turma.curso = ").append(curso).append(" ");
			}
			sqlStr.append("and disciplina.codigo = ").append(diaItem.getDisciplinaVO().getCodigo()).append(" ");
			if (!nivelEducacional.equals("")) {
				sqlStr.append("and curso.nivelEducacional = '").append(nivelEducacional).append("' ");
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (tabelaResultado.next()) {
				HorarioProfessorDiaItemVO diaItemVO = new HorarioProfessorDiaItemVO();
				diaItemVO = diaItem.getClone();
				if (tabelaResultado.getBoolean("t2.turmaagrupada")) {
					diaItemVO.setTurmaVO(diaItem.getTurmaVO().getClone());
					diaItemVO.getTurmaVO().setCurso(diaItem.getTurmaVO().getCurso().getClone());
					diaItemVO.getTurmaVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
				}
				diaItemVO.getDisciplinaVO().setNome(tabelaResultado.getString("nome"));
				diaItemVO.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorTurma"));
				diaItemVO.getTurmaVO().setSala(tabelaResultado.getString("turma.sala"));
				diaItemVO.getTurmaVO().getCurso().setNivelEducacional(tabelaResultado.getString("niveleducacional"));
				diaItemVO.getTurmaVO().getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
				diaItemVO.getTurmaVO().getCurso().setPeriodicidade(tabelaResultado.getString("curso.periodicidade"));
				diaItemVO.getTurmaVO().getCurso().setLiberarRegistroAulaEntrePeriodo(tabelaResultado.getBoolean("curso.liberarRegistroAulaEntrePeriodo"));
				diaItemVO.getTurmaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
				diaItemVO.getTurmaVO().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino.codigo"));
				diaItemVO.getTurmaVO().getUnidadeEnsino().getCidade().setNome(tabelaResultado.getString("cidade.nome"));
				diaItemVO.getTurmaVO().getUnidadeEnsino().getCidade().setCodigo(tabelaResultado.getInt("cidade.codigo"));
				listaHorarioProfessorDiaItemVO.add(diaItemVO);
			}
			sqlStr = null;
		}
		tabelaResultado = null;
		return listaHorarioProfessorDiaItemVO;
	}

	/***
	 * Mï¿½todo responsï¿½vel por montar os dados das aulas de um dia
	 * especï¿½fico para uma disciplina especï¿½fica
	 * 
	 * @param dia
	 * @param codigoDisciplina
	 * @throws Exception
	 */
	public static void montarDadosHorarioTurmaDiaItem(HorarioProfessorDiaVO dia, Integer codigoDisciplina) throws Exception {
		SqlRowSet tabelaResultado;
		for (HorarioProfessorDiaItemVO diaItem : dia.getHorarioProfessorDiaItemVOs()) {
			if (!diaItem.getDisciplinaLivre() && !diaItem.getTurmaLivre()) {
				if (codigoDisciplina.intValue() == diaItem.getDisciplinaVO().getCodigo().intValue()) {
					String sqlStr = "select nome, identificadorTurma, sala from disciplina, turma where turma.codigo = " + diaItem.getTurmaVO().getCodigo() + " and disciplina.codigo = " + diaItem.getDisciplinaVO().getCodigo();
					tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

					if (tabelaResultado.next()) {
						diaItem.getDisciplinaVO().setNome(tabelaResultado.getString("nome"));
						diaItem.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
						diaItem.getTurmaVO().setSala(tabelaResultado.getString("sala"));
					}
					sqlStr = null;
				}

			}
		}
		tabelaResultado = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * excluirHorarioProfessorDias(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirHorarioProfessorDias(Integer horarioProfessor, UsuarioVO usuarioVO) throws Exception {
		String sql = "DELETE FROM HorarioProfessorDia WHERE (horarioProfessor = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { horarioProfessor });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * alterarHorarioProfessorDias(java.lang.Integer, int, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHorarioProfessorDias(Integer horarioProfessor, TurnoVO turnoVO, List<HorarioProfessorDiaVO> objetos, UsuarioVO usuario) throws Exception {
		// String sql =
		// "DELETE FROM HorarioProfessorDia WHERE (horarioProfessor = ?)";
		Integer nrAula = 0;
		// for (HorarioProfessorDiaVO horarioProfessorDiaVO : objetos) {
		// nrAula =
		// getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turnoVO,
		// horarioProfessorDiaVO.getDiaSemanaEnum());
		// if (horarioProfessorDiaVO.getExisteAula(nrAula) &&
		// horarioProfessorDiaVO.getCodigo().intValue() > 0) {
		// sql += " and codigo <> " + horarioProfessorDiaVO.getCodigo();
		// }
		// }
		// getConexao().getJdbcTemplate().update(sql, horarioProfessor);
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : objetos) {
			nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turnoVO, horarioProfessorDiaVO.getDiaSemanaEnum());
			if (horarioProfessorDiaVO.getExisteAula(nrAula)) {
				if (horarioProfessorDiaVO.getCodigo().intValue() == 0) {
					horarioProfessorDiaVO.getHorarioProfessor().setCodigo(horarioProfessor);
					incluir(horarioProfessorDiaVO, usuario);
				} else {
					horarioProfessorDiaVO.getHorarioProfessor().setCodigo(horarioProfessor);
					alterar(horarioProfessorDiaVO, usuario);
				}
			} else {
				excluir(horarioProfessorDiaVO, usuario);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * incluirHorarioProfessorDias(java.lang.Integer, int, java.util.List)
	 */
	public void incluirHorarioProfessorDias(Integer horarioProfessorPrm, TurnoVO turnoVO, List objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator e = objetos.iterator();
		Integer nrAula = 0;
		while (e.hasNext()) {
			HorarioProfessorDiaVO obj = (HorarioProfessorDiaVO) e.next();
			obj.getHorarioProfessor().setCodigo(horarioProfessorPrm);
			nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turnoVO, obj.getDiaSemanaEnum());
			if (obj.getExisteAula(nrAula)) {
				incluir(obj, usuarioVO);
			}
		}
	}

	/**
	 * Operaï¿½ï¿½o responsï¿½vel por consultar todos os
	 * <code>HorarioProfessorDiaVO</code> relacionados a um objeto da classe
	 * <code>academico.HorarioTuma</code>.
	 * 
	 * @param horarioProfessor
	 *            Atributo de <code>academico.HorarioTuma</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>HorarioProfessorDiaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>HorarioProfessorDiaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexï¿½o com o BD ou restriï¿½ï¿½o de acesso a
	 *                esta operaï¿½ï¿½o.
	 */
	public static List consultarHorarioProfessorDiasAnoAtualSemestreAtual(Integer horarioProfessor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioProfessorDia.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sqlStr = new StringBuilder("SELECT data, horarioprofessor,  codigo FROM HorarioProfessorDia WHERE horarioProfessor = ?");
		if (Integer.valueOf(Uteis.getSemestreAtual()) == 1) {
			sqlStr.append(" and EXTRACT(MONTH FROM data) between 1 and 7 ");
		} else {
			sqlStr.append(" and EXTRACT(MONTH FROM data) between 8 and 12 ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { horarioProfessor });
		while (tabelaResultado.next()) {
			HorarioProfessorDiaVO novoObj = new HorarioProfessorDiaVO();
			novoObj = HorarioProfessorDia.montarDados(tabelaResultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public static List consultarHorarioProfessorDiasAnoSemestre(Integer horarioProfessor, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioProfessorDia.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sqlStr = new StringBuilder("SELECT data, horarioprofessor, codigo FROM HorarioProfessorDia WHERE horarioProfessor = ? ");
		if (!semestre.equals("")) {
			if (semestre.equals("1")) {
				sqlStr.append("and data between '").append(ano).append("-01-01' and '").append(ano).append("-07-20' ");
			} else {
				sqlStr.append("and data between '").append(ano).append("-07-21' and '").append(ano).append("-12-31' ");
			}
		}
		// sqlStr.append("and EXTRACT(YEAR FROM data) between ").append(ano).append(" and ").append(ano);
		sqlStr.append(" ORDER BY data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { horarioProfessor });
		while (tabelaResultado.next()) {
			HorarioProfessorDiaVO novoObj = new HorarioProfessorDiaVO();
			novoObj = HorarioProfessorDia.montarDados(tabelaResultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operaï¿½ï¿½o responsï¿½vel por consultar todos os
	 * <code>HorarioProfessorDiaVO</code> relacionados a um objeto da classe
	 * <code>academico.HorarioTuma</code>.
	 * 
	 * @param horarioProfessor
	 *            Atributo de <code>academico.HorarioTuma</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>HorarioProfessorDiaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>HorarioProfessorDiaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexï¿½o com o BD ou restriï¿½ï¿½o de acesso a
	 *                esta operaï¿½ï¿½o.
	 */
	public static List<HorarioProfessorDiaVO> consultarHorarioProfessorDias(Integer horarioProfessor, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioProfessorDia.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<HorarioProfessorDiaVO> objetos = new ArrayList<HorarioProfessorDiaVO>(0);
		StringBuilder sqlStr = new StringBuilder("SELECT data, horarioprofessor, codigo FROM HorarioProfessorDia WHERE horarioProfessor = ? ");
		if (dataInicio != null && dataFim != null) {
			sqlStr.append(" and data >= ? and data <= ?");
		}
		SqlRowSet tabelaResultado = null;
		if (dataInicio != null && dataFim != null) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { horarioProfessor, dataInicio, dataFim });
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { horarioProfessor });
		}
		while (tabelaResultado.next()) {
			HorarioProfessorDiaVO novoObj = new HorarioProfessorDiaVO();
			novoObj = HorarioProfessorDia.montarDados(tabelaResultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, boolean, int)
	 */
	public HorarioProfessorDiaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM HorarioProfessorDia WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Nï¿½o Encontrados ( HorarioProfessorDia ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por retornar o identificador desta classe. Este
	 * identificar ï¿½ utilizado para verificar as permissï¿½es de acesso as
	 * operaï¿½ï¿½es desta classe.
	 */
	public static String getIdEntidade() {
		return HorarioProfessorDia.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.HorarioProfessorDiaInterfaceFacade#
	 * setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		HorarioProfessorDia.idEntidade = idEntidade;
	}

	public List<HorarioProfessorDiaVO> consultarPorHorarioProfessorDiaContendoDisciplina(Integer codigoDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT HorarioProfessorDia.* FROM HorarioProfessorDia WHERE ");
		sqlStr.append(" exists (select horarioprofessordiaitem.codigo from horarioprofessordiaitem where horarioprofessordiaitem.HorarioProfessorDia = HorarioProfessorDia.codigo and horarioprofessordiaitem.disciplina = ").append(codigoDisciplina).append(" limit 1)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
	}

	/**
	 * Este mï¿½todo valida se ainda existe disponibilidade no horario do
	 * professor para a aula e dia informado.
	 * 
	 * @param horarioProfessorDiaVO
	 * @param nrAula
	 * @param disciplina
	 * @param novaTurma
	 * 
	 * @throws ConsistirException
	 */
	public void executarValidacaoProfessorHorarioDisponivelDia(HorarioProfessorDiaVO horarioProfessorDiaVO, Integer nrAula, DisciplinaVO disciplina, TurmaVO novaTurma) throws ConsistirException {
		for (HorarioProfessorDiaItemVO horarioProfessorDiaItem : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
			if (horarioProfessorDiaItem.getNrAula().intValue() == nrAula.intValue()) {
				if ((horarioProfessorDiaItem.getDisciplinaLivre() && horarioProfessorDiaItem.getTurmaLivre())) {
					return;
				}
				if (!horarioProfessorDiaItem.getDisciplinaVO().getCodigo().equals(disciplina.getCodigo()) || !novaTurma.getCodigo().equals(horarioProfessorDiaItem.getTurmaVO().getCodigo())) {
					throw new ConsistirException("O horário '" + horarioProfessorDiaItem.getHorario() + " do dia " + horarioProfessorDiaVO.getData_Apresentar() + " não está mais disponível no horário do professor.");
				}
			}
		}
	}

	/***
	 * Mï¿½todo responsï¿½vel por montar os dados das aulas do dia e turno
	 * informados como parï¿½metros
	 * 
	 * @param horarioProfessorDiaVO
	 * @pram turno
	 * 
	 */
	public void montarDadosHorarioProfessorDiaItemVOsRegistroAula(HorarioProfessorVO horarioProfessorVO, HorarioProfessorDiaVO horarioProfessorDiaVO, UsuarioVO usuarioVO) throws Exception {

		int index = 0;
		for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
			horarioProfessorDiaItemVO.setProfessor(horarioProfessorVO.getProfessor().getNome());
			horarioProfessorDiaItemVO.setTelefoneProfessor(horarioProfessorVO.getProfessor().getTelefoneRes());
			horarioProfessorDiaItemVO.setCelularProfessor(horarioProfessorVO.getProfessor().getCelular());
			horarioProfessorDiaItemVO.setEmailProfessor(horarioProfessorVO.getProfessor().getEmail());
			if (!getFacadeFactory().getHorarioTurmaDiaFacade().executarVerificarSeHaAulaNaTurmaParaDeterminadoProfessor(horarioProfessorDiaVO.getData(), horarioProfessorDiaItemVO.getNrAula(), horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo(), horarioProfessorVO.getProfessor().getCodigo(), horarioProfessorDiaItemVO.getTurmaVO().getCodigo().intValue())) {
				horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().remove(index);
				montarDadosHorarioProfessorDiaItemVOsRegistroAula(horarioProfessorVO, horarioProfessorDiaVO, usuarioVO);
				return;
			}
			index++;
		}
	}

	/***
	 * Mï¿½todo responsï¿½vel por montar os dados das aulas do dia e turno
	 * informados como parï¿½metros
	 * 
	 * @param horarioProfessorDiaVO
	 * @pram turno
	 * 
	 */
	public void montarDadosHorarioProfessorDiaItemVOs(HorarioProfessorDiaVO horarioProfessorDiaVO, HorarioProfessorVO horarioProfessorVO, TurnoVO turno, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		int x = 1;
		if (Uteis.isAtributoPreenchido(horarioProfessorDiaVO)) {
			horarioProfessorDiaVO.setHorarioProfessorDiaItemVOs(getFacadeFactory().getHorarioProfessorDiaItemFacade().consultarPorHorarioProfessorDia(horarioProfessorDiaVO.getCodigo()));
		}
		Integer nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turno, horarioProfessorDiaVO.getDiaSemanaEnum());
		try {
			while (x <= nrAula) {
				boolean existe = false;
				for (HorarioProfessorDiaItemVO diaItem : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					if (diaItem.getNrAula().equals(x)) {
						existe = true;
						break;
					}
				}
				if (!existe) {
					HorarioProfessorDiaItemVO horarioProfessorDiaItem = new HorarioProfessorDiaItemVO();
					horarioProfessorDiaItem.setNrAula(x);
					TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioProfessorVO.getTurno(), horarioProfessorDiaVO.getDiaSemanaEnum(), x);
					horarioProfessorDiaItem.setHorario(turnoHorarioVO.getHorarioInicioAula()+" "+UteisJSF.internacionalizar("prt_a")+" "+turnoHorarioVO.getHorarioFinalAula());
					horarioProfessorDiaItem.setHorarioInicio(turnoHorarioVO.getHorarioInicioAula());
					horarioProfessorDiaItem.setHorarioTermino(turnoHorarioVO.getHorarioFinalAula());
					horarioProfessorDiaItem.setDuracaoAula(turnoHorarioVO.getDuracaoAula());
					horarioProfessorDiaItem.getDisciplinaVO().setCodigo(horarioProfessorDiaVO.getCodigoDisciplina(x));
					horarioProfessorDiaItem.getTurmaVO().setCodigo(horarioProfessorDiaVO.getCodigoTurma(x));
					horarioProfessorDiaItem.setData(horarioProfessorDiaVO.getData());
					horarioProfessorDiaItem.getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(horarioProfessorDiaItem.getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
					try {
						horarioProfessorDiaItem.setProfessor(horarioProfessorVO.getProfessor().getNome());
						horarioProfessorDiaItem.setTelefoneProfessor(horarioProfessorVO.getProfessor().getTelefoneRes());
						horarioProfessorDiaItem.setCelularProfessor(horarioProfessorVO.getProfessor().getCelular());
						horarioProfessorDiaItem.setEmailProfessor(horarioProfessorVO.getProfessor().getEmail());
						if (!horarioProfessorVO.getProfessor().getFormacaoAcademicaVOs().isEmpty()) {
							horarioProfessorDiaItem.setTitulacaoProfessor(horarioProfessorVO.getProfessor().getFormacaoAcademicaVOs().get(0).getEscolaridade_Apresentar());
						}
						if (getFacadeFactory().getHorarioTurmaDiaFacade().executarVerificarSeHaAulaNaTurmaParaDeterminadoProfessor(horarioProfessorDiaVO.getData(), horarioProfessorDiaItem.getNrAula(), horarioProfessorDiaItem.getDisciplinaVO().getCodigo(), horarioProfessorVO.getProfessor().getCodigo(), horarioProfessorDiaItem.getTurmaVO().getCodigo().intValue())) {
							horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().add(horarioProfessorDiaItem);
						}
					} catch (Exception ex) {

					}
				}
				x++;
			}
		} catch (Exception ex) {

		}
		Ordenacao.ordenarLista(horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs(), "nrAula");
	}

	@Override
	public List<HorarioProfessorDiaVO> consultarProfessorPorPeriodoTurmaAgrupada(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaCompleta(null, null, codigoProfessor, disciplina, null, null, null, curso, unidadeEnsino, null, null, permiteInformarFuncionarioCargo, funcionarioCargoVO);
		sqlStr.append(" where 1 = 1 ");
		if (turma != null && turma > 0) {
			sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma.codigo in (select turmaorigem from turmaagrupada where turmaagrupada.turma = " + turma + ")) ");
		}

		if (dataInicio != null) {
			sqlStr.append(" and horarioprofessordia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}

		if (dataFim != null) {
			sqlStr.append(" and horarioprofessordia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}

		if (permiteInformarFuncionarioCargo && Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			sqlStr.append(" and horarioturmadiaitem.funcionariocargo = ").append(funcionarioCargoVO.getCodigo());
		}

		if(ordenacao.equals("unidadeEnsino")) {
			sqlStr.append(" order by unidadeensino.nome, horarioprofessordia.data, horarioprofessordiaitem.horarioinicio ");
		}
		else if(ordenacao.equals("data")) {
			sqlStr.append(" order by horarioprofessordia.data, horarioprofessordiaitem.horarioinicio ");
		}
		else if(ordenacao.equals("disciplina")) {
			sqlStr.append(" order by disciplina.nome, horarioprofessordia.data, horarioprofessordiaitem.horarioinicio ");
		}
		else if(ordenacao.equals("curso")) {
			sqlStr.append(" order by curso.nome, horarioprofessordia.data, horarioprofessordiaitem.horarioinicio");
		}
		else {
			sqlStr.append(getSqlOrdenacaoConsultaCompleta());
		}
				
		return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	}
	
	
	@Override
	public List<DataEventosRSVO> consultarRapidaHorarioProfessorDia(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaRapidaDadosBasico(null, null, codigoProfessor, disciplina, null, null, null, curso, unidadeEnsino, null, null, permiteInformarFuncionarioCargo, funcionarioCargoVO);
		sqlStr.append(" where 1 = 1 ");
		if (turma != null && turma > 0) {
			sqlStr.append(" and (turma.codigo = ").append(turma).append(" or turma.codigo in (select turmaorigem from turmaagrupada where turmaagrupada.turma = " + turma + ")) ");
		}		
		if (dataInicio != null) {
			sqlStr.append(" and horarioprofessordia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}		
		if (dataFim != null) {
			sqlStr.append(" and horarioprofessordia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}		
		if (permiteInformarFuncionarioCargo && Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			sqlStr.append(" and horarioturmadiaitem.funcionariocargo = ").append(funcionarioCargoVO.getCodigo());
		}
		sqlStr.append(" order by horarioprofessordia.data ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DataEventosRSVO> dataEventosRSVOs =  new ArrayList<>();
		while (rs.next()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("data"));
			DataEventosRSVO dataEventosRSVO = new DataEventosRSVO();
			dataEventosRSVO.setAno(calendar.get(Calendar.YEAR));
			dataEventosRSVO.setMes(calendar.get(Calendar.MONTH));
			dataEventosRSVO.setDia(calendar.get(Calendar.DAY_OF_MONTH));
			dataEventosRSVO.setColor("#b8b8b8");
			dataEventosRSVO.setTextColor("#000000");
			dataEventosRSVO.setStyleClass("horarioRegistroLancado");
			dataEventosRSVO.setData(rs.getDate("data"));
			dataEventosRSVOs.add(dataEventosRSVO);
		}
		return dataEventosRSVOs;
		
	}

	public List<HorarioProfessorDiaVO> consultarProfessorPorPeriodoVisaoCoordenador(Integer codigoProfessor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, Integer codigoCoordenador, String ordenacao) throws Exception {
		StringBuilder sqlStr = getSqlConsultaCompleta(null, null, codigoProfessor, disciplina, null, null, null, curso, unidadeEnsino, null, null, false, null);
		sqlStr.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append(" OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (turma != null && turma > 0) {
			sqlStr.append(" and  (turma.codigo = ").append(turma).append(" or turma.codigo in (select turmaorigem from turmaagrupada where turmaagrupada.turma = " + turma + ") ");
		}
		
		
		if (!visaoProfessor) {
		if (dataInicio != null) {
			sqlStr.append(" and data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataInicio));
			sqlStr.append("'");
		}
		if (dataFim != null) {
			sqlStr.append(" and data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append("'");
		}
	} else {
		if (dataInicio != null) {
			sqlStr.append(" and true= (case when (curso.nivelEducacional in ('EX', 'PO')) then (data >= '").append(Uteis.getDataJDBC(dataInicio)).append("') else ");
			sqlStr.append(" case when  (data::DATE > current_date and extract(month from data::DATE) > 7 and extract(month from current_date)<=7) then false ");
			sqlStr.append(" else case when  (data::DATE > current_date ");
			sqlStr.append(" and ((extract(month from data::DATE) > 7 and extract(month from current_date)>7) or (extract(month from data::DATE) <= 7 and extract(month from current_date)<=7)))");
			sqlStr.append(" then data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' else case when (data<=current_date) then data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sqlStr.append(" else data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' end end end end)");
		}
		if (dataFim != null) {
			sqlStr.append(" and true= (case when (curso.nivelEducacional in ('EX', 'PO')) then (data <= '").append(Uteis.getDataJDBC(dataFim)).append("') else ");
			sqlStr.append(" case when  (data::DATE > current_date and extract(month from data::DATE) > 7 and extract(month from current_date)<=7) then false ");
			sqlStr.append(" else case when  (data::DATE > current_date ");
			sqlStr.append(" and ((extract(month from data::DATE) > 7 and extract(month from current_date)>7) or (extract(month from data::DATE) <= 7 and extract(month from current_date)<=7)))");
			sqlStr.append(" then data <= '").append(Uteis.getDataJDBC(dataFim)).append("' else case when (data<=current_date) then data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			sqlStr.append(" else data <= '").append(Uteis.getDataJDBC(dataFim)).append("' end end end end)");
		}
		if (dataInicio == null && dataFim == null) {
			sqlStr.append(" and true= (case when (curso.nivelEducacional in ('EX', 'PO')) then true else ");
			sqlStr.append(" case when  (data::DATE > current_date and extract(month from data::DATE) > 7 and extract(month from current_date)<=7) then false ");
			sqlStr.append(" else case when  (data::DATE > current_date ");
			sqlStr.append(" and ((extract(month from data::DATE) > 7 and extract(month from current_date)>7) or (extract(month from data::DATE) <= 7 and extract(month from current_date)<=7)))");
			sqlStr.append(" then true else data<=current_date end end end) ");
		}

	}
		
		sqlStr.append(getSqlOrdenacaoConsultaCompleta());
		return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	}

	public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String nivelEducacional, UsuarioVO usuario, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		return consultarHorariosProfessorSeparadoPorNivelEducacional(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, false, nivelEducacional, usuario, ordenacao, permiteInformarFuncionarioCargo, funcionarioCargoVO);
	}

	public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, Boolean visaoCoordenador, String nivelEducacional, UsuarioVO usuario, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> listaHorarioFinal = new EnumMap<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>>(GraduacaoPosGraduacaoEnum.class);
		List<HorarioProfessorVO> horarioProfessorVOs = new ArrayList<HorarioProfessorVO>(0);
		List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = new ArrayList<HorarioProfessorDiaVO>(0);
		HorarioProfessorVO horarioProfessorVO = new HorarioProfessorVO();
		List<HorarioProfessorDiaItemVO> listaHorarioGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
		List<HorarioProfessorDiaItemVO> listaHorarioPosGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
		// String semestreAtual = Uteis.getSemestreData(dataInicio);
		String semestreAtual = Uteis.getSemestreAtual();
		int mesDataHorarioProfessor = 0;
		int anoAtual = Uteis.getAnoData(new Date());
		Integer anoHorarioProfessorDia = null;
		try {
			if (!visaoCoordenador) {
				horarioProfessorDiaVOs = consultarProfessorPorPeriodoTurmaAgrupada(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, ordenacao, permiteInformarFuncionarioCargo, funcionarioCargoVO);
			} else {
				horarioProfessorDiaVOs = consultarProfessorPorPeriodoVisaoCoordenador(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, usuario.getPessoa().getCodigo(), ordenacao);
			}
			for (HorarioProfessorDiaVO obj : horarioProfessorDiaVOs) {
				horarioProfessorVO = selecionarHorarioProfessor(horarioProfessorVOs, obj.getHorarioProfessor().getCodigo(), dataInicio, dataFim, usuario);
				
//				montarDadosHorarioProfessorDiaItemVOs(obj, horarioProfessorVO, horarioProfessorVO.getTurno(), dataInicio, dataFim, usuario);
//				montarDadosHorarioTurmaDiaItem(obj, nivelEducacional);
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVOProgramacao : obj.getHorarioProfessorDiaItemVOs()) {
					horarioProfessorDiaItemVOProgramacao.setData(obj.getData());
					horarioProfessorDiaItemVOProgramacao.setCodProfessor(horarioProfessorVO.getProfessor().getCodigo());
					horarioProfessorDiaItemVOProgramacao.setProfessor(horarioProfessorVO.getProfessor().getNome());
					horarioProfessorDiaItemVOProgramacao.setCelularProfessor(horarioProfessorVO.getProfessor().getCelular());
					horarioProfessorDiaItemVOProgramacao.setTelefoneProfessor(horarioProfessorVO.getProfessor().getTelefoneRes());
					horarioProfessorDiaItemVOProgramacao.setEmailProfessor(horarioProfessorVO.getProfessor().getEmail());
					if (!horarioProfessorVO.getProfessor().getFormacaoAcademicaVOs().isEmpty()) {
						horarioProfessorDiaItemVOProgramacao.setTitulacaoProfessor(horarioProfessorVO.getProfessor().getFormacaoAcademicaVOs().get(0).getEscolaridade_Apresentar());
					}
					
					if(horarioProfessorDiaItemVOProgramacao.getDisciplinaVO().getCargaHorariaPrevista() == 0) {
						List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVO = getFacadeFactory().getHorarioTurmaFacade()
								.consultarHorarioTurmaDisciplinaProgramadaPorTurmaEDisciplinaTrazendoSala(horarioProfessorDiaItemVOProgramacao.getTurmaVO().getCodigo(), horarioProfessorDiaItemVOProgramacao.getDisciplinaVO().getCodigo(), "", "", false);
						HorarioTurmaDisciplinaProgramadaVO cargahoraria = horarioTurmaDisciplinaProgramadaVO.iterator().next();
						horarioProfessorDiaItemVOProgramacao.getDisciplinaVO().setCargaHorariaPrevista(cargahoraria.getChDisciplina());
						
					}
					// horarioProfessorDiaItemVOProgramacao.setProfessor(horarioProfessorVO.getNomeProfessor());
					// horarioProfessorDiaItemVOProgramacao.setCodProfessor(horarioProfessorVO.getProfessor().getCodigo());
					// List<HorarioProfessorDiaItemVO>
					// listaHorarioProfessorDiaItem = new
					// ArrayList<HorarioProfessorDiaItemVO>(0);
					// if
					// (horarioProfessorDiaItemVOProgramacao.getTurmaVO().getIdentificadorTurma().equals(""))
					// {
					// listaHorarioProfessorDiaItem.addAll(montarDadosHorarioTurmaDiaItemTurmaAgrupada(horarioProfessorDiaItemVOProgramacao,
					// turma, curso, nivelEducacional));
					// } else {
					// if (turma != 0) {
					// if (turma ==
					// horarioProfessorDiaItemVOProgramacao.getTurmaVO().getCodigo().intValue())
					// {
					// listaHorarioProfessorDiaItem.add(horarioProfessorDiaItemVOProgramacao);
					// }
					// } else {
					// listaHorarioProfessorDiaItem.add(horarioProfessorDiaItemVOProgramacao);
					// }
					// }
					// for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO
					// : listaHorarioProfessorDiaItem) {
					if (horarioProfessorDiaItemVOProgramacao.getTurmaVO().getPeriodicidade() != null && horarioProfessorDiaItemVOProgramacao.getTurmaVO().getPeriodicidade().equals("IN")) {
						adicionarHorarioProfessorPosGraduacao(listaHorarioPosGraduacao, horarioProfessorDiaItemVOProgramacao);
					} else {
						anoHorarioProfessorDia = Uteis.getAnoData(obj.getData());
						if (visaoProfessor) {
							if (usuario.getVisaoLogar().equals("professor") || usuario.getVisaoLogar().equals("aluno")) {
								if (horarioProfessorDiaItemVOProgramacao.getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo()) {
									listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
								} else {
									if (anoHorarioProfessorDia != null && anoHorarioProfessorDia.intValue() == anoAtual) {
										if (semestreAtual.equals("1")) {
											mesDataHorarioProfessor = Uteis.getMesData(obj.getData());
											if (mesDataHorarioProfessor <= 7) {
												listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
											}
										} else {
											mesDataHorarioProfessor = Uteis.getMesData(obj.getData());
											if (mesDataHorarioProfessor >= 7) {
												listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
											}
										}
									}
								}
							} else {
								if (!usuario.getVisaoLogar().equals("professor") && !usuario.getVisaoLogar().equals("aluno")) {
									listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
								}
							}
						} else {
							if (usuario.getVisaoLogar().equals("professor") || usuario.getVisaoLogar().equals("aluno")) {
								if (anoHorarioProfessorDia != null && anoHorarioProfessorDia.intValue() == anoAtual) {
									if (semestreAtual.equals("1")) {
										mesDataHorarioProfessor = Uteis.getMesData(obj.getData());
										if (mesDataHorarioProfessor <= 7) {
											listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
										}
									} else {
										mesDataHorarioProfessor = Uteis.getMesData(obj.getData());
										if (mesDataHorarioProfessor > 7) {
											listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
										}
									}
								}
							} else {
								if (!usuario.getVisaoLogar().equals("professor") && !usuario.getVisaoLogar().equals("aluno")) {
									listaHorarioGraduacao.add(horarioProfessorDiaItemVOProgramacao);
								}
							}
						}
						anoHorarioProfessorDia = null;
					}
					// }
					// listaHorarioProfessorDiaItem.clear();
				}
				// listaHorarioItem.addAll(obj.getHorarioProfessorDiaItemVOs());
				// removerObjetoMemoria(obj);
			}
			listaHorarioFinal.put(GraduacaoPosGraduacaoEnum.GRADUACAO, listaHorarioGraduacao);
			listaHorarioFinal.put(GraduacaoPosGraduacaoEnum.POS_GRADUACAO, listaHorarioPosGraduacao);
			return listaHorarioFinal;
		} catch (Exception e) {
			throw e;

		} finally {
			semestreAtual = null;
			Uteis.liberarListaMemoria(horarioProfessorDiaVOs);
			Uteis.liberarListaMemoria(horarioProfessorVOs);
			removerObjetoMemoria(horarioProfessorVO);
		}
	}

	/*
	 * Mï¿½todo nï¿½o esta sendo usado
	 */

	private void adicionarHorarioProfessorPosGraduacao(List<HorarioProfessorDiaItemVO> listaHorarioPosGraduacao, HorarioProfessorDiaItemVO horarioProfessorDiaItemVO) {
		for (HorarioProfessorDiaItemVO obj : listaHorarioPosGraduacao) {
			if (obj.getDisciplinaVO().getCodigo().intValue() == horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo().intValue() && obj.getTurmaVO().getCodigo().intValue() == horarioProfessorDiaItemVO.getTurmaVO().getCodigo().intValue()) {
				if (obj.getDataFinal() == null || obj.getDataFinal().compareTo(horarioProfessorDiaItemVO.getData()) < 0) {
					obj.setDataFinal(horarioProfessorDiaItemVO.getData());
				}
				Uteis.removerObjetoMemoria(horarioProfessorDiaItemVO);
				return;
			}
		}
		listaHorarioPosGraduacao.add(horarioProfessorDiaItemVO);
	}

	public HorarioProfessorVO selecionarHorarioProfessor(List<HorarioProfessorVO> horarioProfessorVOs, Integer horarioProfessor, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {

		for (HorarioProfessorVO obj : horarioProfessorVOs) {
			if (obj.getCodigo().intValue() == horarioProfessor.intValue()) {
				return obj;
			}
		}
		HorarioProfessorVO horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade().consultaRapidaPorChavePrimaria(horarioProfessor, usuario);
		horarioProfessorVOs.add(horarioProfessorVO);
		return horarioProfessorVO;
	}

	public String getSqlOrdenacaoConsultaCompleta() {
		return " order by horarioprofessordia.data, unidadeensino.nome, horarioprofessordiaitem.horarioinicio ";
	}

	public StringBuilder getSqlConsultaRapidaDadosBasico(Integer horarioProfessor, Integer turno, Integer professor, Integer disciplina, Integer turma, Date dataInicio, Date dataTermino, Integer curso, Integer unidadeEnsino, String ano, String semestre, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) {
		StringBuilder sql = new StringBuilder("");

		sql.append(" select distinct horarioProfessor.codigo as horarioprofessor, ");
		sql.append(" professor.codigo as professor, professor.nome as professor_nome,  ");
		sql.append(" ('0'::VARCHAR(1)||TO_CHAR(horarioprofessordia.data, 'D'))::VARCHAR(2) as diasemana,   ");
		sql.append(" horarioprofessordia.codigo as horarioprofessordia, horarioprofessordia.data ");
		sql.append(" from horarioprofessor ");
		sql.append(" inner join pessoa as professor on professor.codigo = horarioProfessor.professor ");
		if (horarioProfessor != null && horarioProfessor > 0) {
			sql.append(" and horarioProfessor.codigo = ").append(horarioProfessor);
		}
		if(Uteis.isAtributoPreenchido(professor)){
			sql.append(" and professor.codigo = ").append(professor);
		}
		sql.append(" inner join horarioprofessordia on horarioprofessordia.horarioprofessor = horarioProfessor.codigo ");
		if(Uteis.isAtributoPreenchido(dataInicio) || Uteis.isAtributoPreenchido(dataTermino)){
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioprofessordia.data", false));
		}
		sql.append(" left join horarioprofessordiaitem on horarioprofessordiaitem.horarioprofessordia = horarioProfessorDia.codigo ");

		sql.append(" inner join turma on turma.codigo = horarioprofessordiaitem.turma ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0 && turma == null) {
			sql.append(" and (turma.curso = ").append(curso).append(" or ").append(" turma.codigo in (select turmaorigem from turmaAgrupada inner join turma as t on t.codigo = turmaAgrupada.turmaorigem where turmaAgrupada.turma = turma.codigo and t.curso = " + curso + " ) )");
		}
		if(Uteis.isAtributoPreenchido(turma)){
			sql.append(" and turma.codigo = ").append(turma);
		}
		sql.append(" inner join turno on turma.turno = turno.codigo ");
		if(Uteis.isAtributoPreenchido(turno)){
			sql.append(" and turno.codigo = ").append(turno);
		}
		sql.append(" inner join disciplina on horarioprofessordiaitem.disciplina = disciplina.codigo ");
		if(Uteis.isAtributoPreenchido(disciplina)){
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		sql.append(" left join salalocalaula as sala on sala.codigo = horarioprofessordiaitem.sala ");
		sql.append(" left join localaula on localaula.codigo = sala.localaula ");		
		sql.append(" left join usuario as ulch on horarioprofessordiaitem.usuarioliberacaochoquehorario = ulch.codigo ");		
		sql.append(" inner join horarioturmadiaitem on horarioprofessordiaitem.horarioturmadiaitem = horarioturmadiaitem.codigo ");
		sql.append(" inner join horarioturmadia on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo ");
		if (permiteInformarFuncionarioCargo && Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			sql.append(" inner join funcionariocargo on horarioturmadiaitem.funcionariocargo = funcionariocargo.codigo ");			
		}
		if (ano != null && !ano.trim().isEmpty()) {			
			sql.append(" and horarioturma.anovigente = '").append(ano).append("' ");
			if (semestre != null && !semestre.trim().isEmpty()) {
				sql.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
			}			
		}
		sql.append(" left join registroaula on registroaula.turma = turma.codigo and registroaula.disciplina = horarioprofessordiaitem.disciplina and registroaula.data = horarioProfessorDia.data and registroaula.nrAula = horarioprofessordiaitem.nrAula ");		
		sql.append(" and registroaula.ano = horarioturma.anovigente ");
		sql.append(" and registroaula.semestre = horarioturma.semestrevigente ");			
		sql.append(" left join curso on curso.codigo = turma.curso ");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join cidade on unidadeensino.cidade = cidade.codigo ");
		return sql;
	}
	
	
	public StringBuilder getSqlConsultaCompleta(Integer horarioProfessor, Integer turno, Integer professor, Integer disciplina, Integer turma, Date dataInicio, Date dataTermino, Integer curso, Integer unidadeEnsino, String ano, String semestre, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO) {
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" select distinct horarioProfessor.codigo as horarioprofessor, professor.codigo as professor, professor.nome as professor_nome, ('0'::VARCHAR(1)||TO_CHAR(horarioprofessordia.data, 'D'))::VARCHAR(2) as diasemana, ");
		sql.append(" turno.codigo as turno, turno.nome as turno_nome, turno.ocultarhorarioaulavisaoprofessor, horarioprofessordia.codigo as horarioprofessordia, horarioprofessordia.data, ");
		sql.append(" disciplina.codigo as disciplina, disciplina.nome as disciplina_nome, turma.codigo as turma, turma.identificadorturma, horarioprofessordiaitem.nraula, ");
		sql.append(" horarioprofessordiaitem.duracaoaula, horarioprofessordiaitem.horarioinicio as horarioinicioaula, horarioprofessordiaitem.horariotermino as horariofinalaula, ");
		sql.append(" horarioprofessordiaitem.codigo as horarioprofessordiaitem, horarioprofessordiaitem.sala, sala.sala as sala_sala, localaula.local as localaula_local, ");
		sql.append(" registroaula.codigo > 0 as existeAulaRegistrada, curso.codigo as curso_codigo, curso.nome as curso_nome, curso.liberarRegistroAulaEntrePeriodo, horarioprofessordiaitem.horarioturmadiaitem, unidadeensino.codigo as unidadeEnsino, unidadeensino.nome as unidadeEnsinoNome, ");
		sql.append(" googlemeet.codigo as \"googlemeet.codigo\",");
		sql.append(" googlemeet.ideventocalendar as \"googlemeet.ideventocalendar\",");		
		sql.append(" googlemeet.ano as \"googlemeet.ano\",");		
		sql.append(" googlemeet.semestre as \"googlemeet.semestre\",");		
		sql.append(" googlemeet.linkgooglemeet as \"googlemeet.linkgooglemeet\",");
		sql.append(" googlemeet.horarioinicio as \"googlemeet.horarioinicio\",");
		sql.append(" googlemeet.horariotermino as \"googlemeet.horariotermino\",");
		sql.append(" googlemeet.diaevento as \"googlemeet.diaevento\",");	
					
		sql.append(" classroomGoogle.codigo as \"classroomGoogle.codigo\", classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
		sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");
		sql.append(" classroomGoogle.professorEad as \"classroomGoogle.professorEad\",  classroomGoogle.ano as \"classroomGoogle.ano\", ");
		sql.append(" classroomGoogle.semestre as \"classroomGoogle.semestre\", ");
		sql.append(" classroomGoogle.idTurma as \"classroomGoogle.idTurma\", ");
		sql.append(" classroomGoogle.emailTurma as \"classroomGoogle.emailTurma\", ");
		
		sql.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", salaaulablackboard.linkSalaAulaBlackboard as \"salaaulablackboard.linkSalaAulaBlackboard\", ");
		sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  salaaulablackboard.id as \"salaaulablackboard.id\", ");
		sql.append(" salaaulablackboard.programacaoTutoriaOnline as \"salaaulablackboard.programacaoTutoriaOnline\",  salaaulablackboard.ano as \"salaaulablackboard.ano\", ");
		sql.append(" salaaulablackboard.semestre as \"salaaulablackboard.semestre\", ");				
		
		sql.append(" ulch.codigo as \"ulch.codigo\", ulch.username as \"ulch.username\", horarioturmadia.codigo as horarioturmadia, horarioturma.codigo as horarioturma, horarioturma.anovigente, horarioturma.semestrevigente, cidade.nome as cidadeUnidadeEnsino , turma.anual as \"turma.anual\" , turma.semestral as \"turma.semestral\",  ");
		sql.append(" case when curso.periodicidade is not null then curso.periodicidade else ( ");
		sql.append(" select distinct curso.periodicidade from curso where ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada = false ");
		sql.append(" and curso.codigo in ( select distinct t.curso from turma t where t.codigo = turma.turmaprincipal)) or (turma.turmaAgrupada and curso.codigo in (");
		sql.append(" select distinct t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo))) limit 1 ) end as curso_periodicidade ");
		sql.append(" from horarioprofessor ");
		sql.append(" inner join pessoa as professor on professor.codigo = horarioProfessor.professor ");
		if (horarioProfessor != null && horarioProfessor > 0) {
			sql.append(" and horarioProfessor.codigo = ").append(horarioProfessor);
		}
		if(Uteis.isAtributoPreenchido(professor)){
			sql.append(" and professor.codigo = ").append(professor);
		}
		sql.append(" inner join horarioprofessordia on horarioprofessordia.horarioprofessor = horarioProfessor.codigo ");
		if(Uteis.isAtributoPreenchido(dataInicio) || Uteis.isAtributoPreenchido(dataTermino)){
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioprofessordia.data", false));
		}
		sql.append(" left join horarioprofessordiaitem on horarioprofessordiaitem.horarioprofessordia = horarioProfessorDia.codigo ");
		
		sql.append(" inner join turma on turma.codigo = horarioprofessordiaitem.turma ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && curso > 0 && turma == null) {
			sql.append(" and (turma.curso = ").append(curso).append(" or ").append(" turma.codigo in (select turmaorigem from turmaAgrupada inner join turma as t on t.codigo = turmaAgrupada.turmaorigem where turmaAgrupada.turma = turma.codigo and t.curso = " + curso + " ) )");
		}
		if(Uteis.isAtributoPreenchido(turma)){
			sql.append(" and turma.codigo = ").append(turma);
		}
		sql.append(" inner join turno on turma.turno = turno.codigo ");
		if(Uteis.isAtributoPreenchido(turno)){
			sql.append(" and turno.codigo = ").append(turno);
		}
		sql.append(" inner join disciplina on horarioprofessordiaitem.disciplina = disciplina.codigo ");
		if(Uteis.isAtributoPreenchido(disciplina)){
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		sql.append(" left join salalocalaula as sala on sala.codigo = horarioprofessordiaitem.sala ");
		sql.append(" left join localaula on localaula.codigo = sala.localaula ");		
		sql.append(" left join usuario as ulch on horarioprofessordiaitem.usuarioliberacaochoquehorario = ulch.codigo ");		
		sql.append(" inner join horarioturmadiaitem on horarioprofessordiaitem.horarioturmadiaitem = horarioturmadiaitem.codigo ");
		sql.append(" inner join horarioturmadia on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo ");
		if (ano != null && !ano.trim().isEmpty()) {			
			sql.append(" and horarioturma.anovigente = '").append(ano).append("' ");
			if (semestre != null && !semestre.trim().isEmpty()) {
				sql.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
			}			
		}		
		sql.append(" left join googlemeet on horarioturmadiaitem.googlemeet = googlemeet.codigo ");
		sql.append(" left join classroomgoogle  on ((classroomGoogle.codigo = googlemeet.classroomGoogle) or "); 
		sql.append(" (classroomGoogle.ano  = horarioturma.anovigente  and classroomGoogle.semestre  = horarioturma.semestrevigente ");  
		sql.append(" and classroomGoogle.disciplina = horarioturmadiaitem.disciplina and classroomGoogle.turma  = horarioturma.turma )) ");
		sql.append(" left join salaaulablackboard  on (salaaulablackboard.ano  = horarioturma.anovigente  and salaaulablackboard.semestre  = horarioturma.semestrevigente ");  
		sql.append(" and salaaulablackboard.disciplina = horarioturmadiaitem.disciplina and salaaulablackboard.turma  = horarioturma.turma ) ");
		if (permiteInformarFuncionarioCargo && Uteis.isAtributoPreenchido(funcionarioCargoVO)) { 
			sql.append(" inner join funcionariocargo on horarioturmadiaitem.funcionariocargo = funcionariocargo.codigo ");			
		}
		sql.append(" left join registroaula on registroaula.turma = turma.codigo and registroaula.disciplina = horarioprofessordiaitem.disciplina and registroaula.data = horarioProfessorDia.data and registroaula.nrAula = horarioprofessordiaitem.nrAula ");		
		sql.append(" and registroaula.ano = horarioturma.anovigente ");
		sql.append(" and registroaula.semestre = horarioturma.semestrevigente ");			
		sql.append(" left join curso on curso.codigo = turma.curso ");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join cidade on unidadeensino.cidade = cidade.codigo ");
		return sql;
	}

	/**
	 * Este método realizar busca do horario do professor com base nos filtros
	 * abaixo para ignorar qualquer um dos filtros passar null como parametro
	 * 
	 * @param horarioProfessor
	 * @param turno
	 * @param professor
	 * @param disciplina
	 * @param turma
	 * @param dataInicio
	 * @param dataTermino
	 * @return
	 * @throws Exception
	 *             Este irá retornar todos os dias cadastros de aulas do
	 *             professor ordenado pelo turno, dataAula e numero aula
	 */
	@Override
	public List<HorarioProfessorDiaVO> consultarHorarioProfessorDia(Integer horarioProfessor, Integer turno, Integer professor, Integer disciplina, Integer turma, Date dataInicio, Date dataTermino, DiaSemana[] diaSemanas, List<Date> datas, Integer unidadeEnsino, String ano, String semestre) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta(horarioProfessor, turno, professor, disciplina, turma, dataInicio, dataTermino, null, unidadeEnsino, ano, semestre, false, null);
		sql.append(" where horarioProfessorDia.data is not null ");
		if (diaSemanas != null && diaSemanas.length > 0) {
			sql.append(" and ('0'::VARCHAR(1)||TO_CHAR(horarioprofessordia.data, 'D'))::VARCHAR(2) in ('00'");
			for (DiaSemana diaSemana : diaSemanas) {
				sql.append(", '").append(diaSemana.getValor()).append("' ");
			}
			sql.append(") ");
		}
		if (datas != null && !datas.isEmpty()) {
			sql.append(" and (");
			int x = 0;
			for (Date data : datas) {
				if (x > 0) {
					sql.append(" or ");
				}
				sql.append(" horarioProfessorDia.data = '").append(Uteis.getDataJDBC(data)).append("' ");
				x++;
			}
			sql.append(")");
		}

		sql.append(getSqlOrdenacaoConsultaCompleta());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaCompleta(rs);
	}

	private List<HorarioProfessorDiaVO> montarDadosConsultaCompleta(SqlRowSet rs) throws Exception {
		Integer horarioProfessorDia = null;
		List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = new ArrayList<HorarioProfessorDiaVO>(0);
		HorarioProfessorDiaVO horarioProfessorDiaVO = null;
		while (rs.next()) {
			if (horarioProfessorDia == null || !horarioProfessorDia.equals(rs.getInt("horarioprofessordia"))) {
				horarioProfessorDia = rs.getInt("horarioprofessordia");
				horarioProfessorDiaVO = montarDadosConsultaCompletaHorarioProfessorDia(rs);
				horarioProfessorDiaVOs.add(horarioProfessorDiaVO);
			}
			if (rs.getBoolean("existeAulaRegistrada") && !horarioProfessorDiaVO.getIsLancadoRegistro()) {
				horarioProfessorDiaVO.setIsLancadoRegistro(rs.getBoolean("existeAulaRegistrada"));
			}
			horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().add(montarDadosConsultaCompletaHorarioProfessorDiaItem(rs));
		}
		return horarioProfessorDiaVOs;
	}

	private HorarioProfessorDiaVO montarDadosConsultaCompletaHorarioProfessorDia(SqlRowSet rs) throws Exception {
		HorarioProfessorDiaVO horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		horarioProfessorDiaVO.setCodigo(rs.getInt("horarioprofessordia"));
		horarioProfessorDiaVO.setNovoObj(false);
		horarioProfessorDiaVO.setData(rs.getDate("data"));
		horarioProfessorDiaVO.setIsLancadoRegistro(rs.getBoolean("existeAulaRegistrada"));		
		horarioProfessorDiaVO.getHorarioProfessor().setCodigo(rs.getInt("horarioprofessor"));		
		horarioProfessorDiaVO.getHorarioProfessor().getProfessor().setCodigo(rs.getInt("professor"));	
		horarioProfessorDiaVO.getHorarioProfessor().getProfessor().setNome(rs.getString("professor_nome"));
		return horarioProfessorDiaVO;
	}

	private HorarioProfessorDiaItemVO montarDadosConsultaCompletaHorarioProfessorDiaItem(SqlRowSet rs) throws Exception {
		HorarioProfessorDiaItemVO horarioProfessorDiaItemVO = new HorarioProfessorDiaItemVO();
		horarioProfessorDiaItemVO.setNovoObj(false);
		horarioProfessorDiaItemVO.setCodigo(rs.getInt("horarioProfessorDiaItem"));
		horarioProfessorDiaItemVO.getHorarioProfessorDiaVO().setCodigo(rs.getInt("horarioProfessorDia"));
		horarioProfessorDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
		horarioProfessorDiaItemVO.getSala().setSala(rs.getString("sala_sala"));
		horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("localAula_local"));
		horarioProfessorDiaItemVO.setDuracaoAula(rs.getInt("duracaoaula"));
		horarioProfessorDiaItemVO.setData(rs.getDate("data"));
		horarioProfessorDiaItemVO.setNrAula(rs.getInt("nrAula"));
		horarioProfessorDiaItemVO.setHorario(rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.setHorarioInicio(rs.getString("horarioinicioaula"));
		horarioProfessorDiaItemVO.setHorarioTermino(rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().setCodigo(rs.getInt("turma"));
		horarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma(rs.getString("identificadorTurma"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setCodigo(rs.getInt("curso_codigo"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setNome(rs.getString("curso_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setPeriodicidade(rs.getString("curso_periodicidade"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setLiberarRegistroAulaEntrePeriodo(rs.getBoolean("liberarRegistroAulaEntrePeriodo"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setCodigo(rs.getInt("turno"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setNome(rs.getString("turno_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setOcultarHorarioAulaVisaoProfessor(rs.getBoolean("ocultarhorarioaulavisaoprofessor"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsinoNome"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().getCidade().setNome(rs.getString("cidadeUnidadeEnsino"));
		horarioProfessorDiaItemVO.setAulaJaRegistrada(rs.getBoolean("existeAulaRegistrada"));
		horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setCodigo(rs.getInt("ulch.codigo"));
		horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setUsername(rs.getString("ulch.username"));
		
		horarioProfessorDiaItemVO.setCodProfessor(rs.getInt("professor"));
		horarioProfessorDiaItemVO.setProfessor(rs.getString("professor_nome"));
		
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setCodigo(rs.getInt("horarioturmadiaitem"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().setCodigo(rs.getInt("horarioturmadia"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setCodigo(rs.getInt("horarioturma"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setAnoVigente(rs.getString("anovigente"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setSemestreVigente(rs.getString("semestrevigente"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setTurma(horarioProfessorDiaItemVO.getTurmaVO());
		
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setDuracaoAula(rs.getInt("duracaoaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setData(rs.getDate("data"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setNrAula(rs.getInt("nrAula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorario(rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorarioInicio(rs.getString("horarioinicioaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorarioTermino(rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getFuncionarioVO().setCodigo(rs.getInt("professor"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getFuncionarioVO().setNome(rs.getString("professor_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setCodigo(rs.getInt("googlemeet.codigo"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getProfessorVO().setCodigo(rs.getInt("professor"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getProfessorVO().setNome(rs.getString("professor_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setAno(rs.getString("googlemeet.ano"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setSemestre(rs.getString("googlemeet.semestre"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setIdEventoCalendar(rs.getString("googlemeet.ideventocalendar"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setLinkGoogleMeet(rs.getString("googlemeet.linkgooglemeet"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setHorarioInicio(rs.getString("googlemeet.horarioinicio"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setHorarioTermino(rs.getString("googlemeet.horariotermino"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setDiaEvento(rs.getTimestamp("googlemeet.diaevento"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setTurmaVO(horarioProfessorDiaItemVO.getTurmaVO());
		if(Uteis.isAtributoPreenchido(rs.getInt("classroomGoogle.codigo"))) {
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setCodigo((rs.getInt("classroomGoogle.codigo")));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setAno(rs.getString("classroomGoogle.ano"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setSemestre(rs.getString("classroomGoogle.semestre"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setLinkClassroom(rs.getString("classroomGoogle.linkClassroom"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdClassRoomGoogle(rs.getString("classroomGoogle.idClassRoomGoogle"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdTurma(rs.getString("classroomGoogle.idTurma"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setEmailTurma(rs.getString("classroomGoogle.emailTurma"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdCalendario(rs.getString("classroomGoogle.idCalendario"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().getProfessorEad().setCodigo(rs.getInt("classroomGoogle.professorEad"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getTurmaVO());
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO());
		}
		if(Uteis.isAtributoPreenchido(rs.getInt("salaaulablackboard.codigo"))) {
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setCodigo((rs.getInt("salaaulablackboard.codigo")));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setAno(rs.getString("salaaulablackboard.ano"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setSemestre(rs.getString("salaaulablackboard.semestre"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(rs.getString("salaaulablackboard.linkSalaAulaBlackboard"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(rs.getString("salaaulablackboard.idSalaAulaBlackboard"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setId(rs.getString("salaaulablackboard.id"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().getProgramacaoTutoriaOnlineVO().setCodigo(rs.getInt("salaaulablackboard.programacaoTutoriaOnline"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setTurmaVO(horarioProfessorDiaItemVO.getTurmaVO());
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setDisciplinaVO(horarioProfessorDiaItemVO.getDisciplinaVO());
		}
		horarioProfessorDiaItemVO.getTurmaVO().setAnual(rs.getBoolean("turma.anual"));
		horarioProfessorDiaItemVO.getTurmaVO().setSemestral(rs.getBoolean("turma.semestral"));
		
		
		return horarioProfessorDiaItemVO;
	}

	@Override
	public void realizarCriacaoHorarioProfessorDiaItemVOs(HorarioProfessorDiaVO horarioProfessorDiaVO, HorarioProfessorVO horarioProfessorVO, UsuarioVO usuarioVO) {

		int x = 1;
		Integer nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioProfessorVO.getTurno(), horarioProfessorDiaVO.getDiaSemanaEnum());
//		if (nrAula.intValue() == horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().size()
//				&& !Uteis.isAtributoPreenchido(horarioProfessorVO.getOperacaoFuncionalidadeVO().getResponsavel())) {
//			return;
//		}
		q: while (x <= nrAula) {
			for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
				if (horarioProfessorDiaItemVO.getNrAula().intValue() == x 
						&& !Uteis.isAtributoPreenchido(horarioProfessorVO.getOperacaoFuncionalidadeVO().getResponsavel())) {
					x++;
					continue q;
				}
			}
			HorarioProfessorDiaItemVO horarioProfessorDiaItem = new HorarioProfessorDiaItemVO();
			horarioProfessorDiaItem.setNrAula(x);
			horarioProfessorDiaItem.getDisciplinaVO().setCodigo(0);
			horarioProfessorDiaItem.getTurmaVO().setCodigo(0);
			horarioProfessorDiaItem.setData(horarioProfessorDiaVO.getData());
			TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioProfessorVO.getTurno(), horarioProfessorDiaVO.getDiaSemanaEnum(), x);
			horarioProfessorDiaItem.setHorario(turnoHorarioVO.getHorarioInicioAula()+" "+UteisJSF.internacionalizar("prt_a")+" "+turnoHorarioVO.getHorarioFinalAula());
			horarioProfessorDiaItem.setHorarioInicio(turnoHorarioVO.getHorarioInicioAula());
			horarioProfessorDiaItem.setHorarioTermino(turnoHorarioVO.getHorarioFinalAula());
			horarioProfessorDiaItem.setDuracaoAula(turnoHorarioVO.getDuracaoAula());
			horarioProfessorDiaItem.setUsuarioLiberacaoChoqueHorario(horarioProfessorVO.getOperacaoFuncionalidadeVO().getResponsavel());
			horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().add(horarioProfessorDiaItem);
			x++;
		}
		Ordenacao.ordenarLista(horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs(), "nrAula");
	}
	

	@Override
	public Map<String, Integer> consultarQuantidadeAulaProgramadaProfessorPorData(List<Date> datas, Integer professor) {
		Map<String, Integer> qtdeAulaPorDiaProfessor = new HashMap<String, Integer>(0);
		if (Uteis.isAtributoPreenchido(datas)) {
			StringBuilder sql = new StringBuilder("select horarioprofessordia.data, count(horarioprofessordiaitem.codigo) as qtde from horarioprofessordiaitem ");
			sql.append(" inner join horarioprofessordia on horarioprofessordia.codigo = horarioprofessordiaitem.horarioprofessordia ");
			sql.append(" inner join horarioprofessor on horarioprofessor.codigo = horarioprofessordia.horarioprofessor ");
			sql.append(" where horarioprofessordiaitem.disciplina is not null and horarioprofessor.professor = ").append(professor);
			sql.append(" and horarioprofessordia.data in (");
			boolean virgula = false;
			for (Date data : datas) {
				sql.append(virgula ? ", " : "").append("'").append(Uteis.getDataJDBC(data)).append("'");
				virgula = true;
			}
			sql.append(") group by horarioprofessordia.data");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (rs.next()) {
				qtdeAulaPorDiaProfessor.put(Uteis.getData(rs.getDate("data")), rs.getInt("qtde"));
			}
		}
		return qtdeAulaPorDiaProfessor;
	}
	
	
//	-------------------------------------------- RELATÓRIO SEMANAL ---------------------------------------
	
	@Override
	public List<HorarioProfessorTurnoVO> consultarMeusHorariosProfessor(PessoaVO professorVO, Date dataBase, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		if (!professorVO.getCodigo().equals(0)) {
			Date dataInicio = dataBase;
			Date dataTermino = null;
			if (dataBase != null) {
				dataInicio = (UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(dataBase)), 1));
				dataTermino = (UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(dataBase), 1));							
			}
			sql.append(" select horarioturma.turma as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
			sql.append(" horarioturmadia.data,  horarioturmadiaitem.disciplina as \"disciplina.codigo\", horarioturmadiaitem.professor as \"pessoa.codigo\", ");
			sql.append(" horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, horarioturmadia.ocultardataaula, ");
			sql.append(" horarioturmadiaitem.horarioinicio as horarioinicioaula,  horarioturmadiaitem.horariotermino as horariofinalaula, to_char(horarioturmadia.data, 'D')::INT as diasemana, turno.codigo as \"turno.codigo\", ");
			sql.append(" case when configuracaogeralsistema.naoApresentarProfessorVisaoAluno and ").append(usuario.getIsApresentarVisaoAluno()||usuario.getIsApresentarVisaoPais() ? " 1 = 1" : " 1 = 0 ").append(" then '' else professor.nome end as \"pessoa.nome\",  ");
			sql.append(" disciplina.nome as \"disciplina.nome\",");
			sql.append(" turno.nome as \"turno.nome\", configuracaogeralsistema.naoApresentarProfessorVisaoAluno, ");
			sql.append(" sala.codigo as \"sala.codigo\", sala.sala as \"sala.sala\", localaula.local as \"localaula.local\", ");
			sql.append(" turno.ocultarhorarioaulavisaoprofessor as \"turno.ocultarhorarioaulavisaoprofessor\" ");
			sql.append(" from horarioturma ");						
			sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
			sql.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
			sql.append(" inner join turma on turma.codigo = horarioturma.turma");
			sql.append(" inner join turno on turno.codigo = turma.turno");
			sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
			if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() > 0) {
				sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
			}
			sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");

			sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor");
			sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina");
			sql.append(" left join salalocalaula sala on sala.codigo = horarioturmadiaitem.sala ");
			sql.append(" left join localaula on localaula.codigo = sala.localaula ");
			sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadia.data", false));
			sql.append(" and professor.codigo = ").append(professorVO.getCodigo());
			if(Uteis.isAtributoPreenchido(turmaVO.getCodigo())){
				sql.append(" and (((turma.codigo in (").append(turmaVO.getCodigo()).append("))  ");
				sql.append(" or (turma.codigo in (select turmaOrigem from TurmaAgrupada inner join turma t on t.codigo = TurmaAgrupada.turmaOrigem where ((t.subturma = false) or (t.subturma and t.tiposubturma = 'GERAL')) and TurmaAgrupada.turma in (" + turmaVO.getCodigo() + ")))))");
			}
			if(Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())){
				sql.append(" and ((disciplina.codigo = ").append(disciplinaVO.getCodigo()).append(" ) ");
				sql.append(" or (turma.turmaagrupada and disciplina.codigo in ( ");
				sql.append(" select ").append(disciplinaVO.getCodigo());
				sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(disciplinaVO.getCodigo());
				sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(disciplinaVO.getCodigo());
				sql.append(" ))) ");	
			}
//			if (turno != null && turno > 0) {
//				sql.append(" and turno.codigo = ").append(turno);
//			}
			sql.append(" and horarioturmadia.ocultardataaula = false ");			

			sql.append(" order by data, nrAula");				
				
			List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs = montarDadosHorarioProfessorPorMatriculaPeriodoDisciplinaSemanal(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), professorVO, dataInicio, dataTermino, usuario, false);			
			if (dataBase != null) {
				List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(dataInicio, dataTermino, 0, ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				for (HorarioProfessorTurnoVO horarioProfessorTurnoVO : horarioProfessorTurnoVOs) {
					horarioProfessorTurnoVO.setDataInicio(dataInicio);
					horarioProfessorTurnoVO.setDataTermino(dataTermino);					
					for (HorarioProfessorTurnoVO horarioProfessorTurnoSemanaVO : horarioProfessorTurnoVO.getHorarioProfessorTurnoVOs()) {
						horarioProfessorTurnoSemanaVO.setDataSegunda(horarioProfessorTurnoSemanaVO.getDataInicio());
						horarioProfessorTurnoSemanaVO.setDataTerca(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 1));
						horarioProfessorTurnoSemanaVO.setDataQuarta(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 2));
						horarioProfessorTurnoSemanaVO.setDataQuinta(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 3));
						horarioProfessorTurnoSemanaVO.setDataSexta(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 4));
						horarioProfessorTurnoSemanaVO.setDataSabado(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 5));
						horarioProfessorTurnoSemanaVO.setDataDomingo(UteisData.obterDataFutura(horarioProfessorTurnoSemanaVO.getDataInicio(), 6));
						horarioProfessorTurnoSemanaVO.setFeriadoDomingo(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataDomingo()));
						horarioProfessorTurnoSemanaVO.setFeriadoSegunda(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataSegunda()));
						horarioProfessorTurnoSemanaVO.setFeriadoTerca(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataTerca()));
						horarioProfessorTurnoSemanaVO.setFeriadoQuarta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataQuarta()));
						horarioProfessorTurnoSemanaVO.setFeriadoQuinta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataQuinta()));
						horarioProfessorTurnoSemanaVO.setFeriadoSexta(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataSexta()));
						horarioProfessorTurnoSemanaVO.setFeriadoSabado(getFacadeFactory().getFeriadoFacade().executarValidacaoDataFeriado(feriadoVOs, horarioProfessorTurnoSemanaVO.getDataSabado()));
					}
				}
			}
			return horarioProfessorTurnoVOs;
		}
		return new ArrayList<HorarioProfessorTurnoVO>(0);
	}
	
	public List<HorarioProfessorTurnoVO> montarDadosHorarioProfessorPorMatriculaPeriodoDisciplinaSemanal(SqlRowSet rs, PessoaVO professorVO, Date dataInicioPeriodo, Date dataTerminoPeriodo,  UsuarioVO usuario, boolean visaoMensal) throws Exception {
		Map<Integer, HorarioProfessorTurnoVO> horarioProfessorTurnoVOs = new HashMap<Integer, HorarioProfessorTurnoVO>(0);
		TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO = null;
		HorarioProfessorTurnoVO horarioProfessorTurnoVO = null;
		HorarioProfessorTurnoVO horarioProfessorTurnoSemanalVO = null;
		while (rs.next()) {
			Date dataInicio = rs.getDate("data");
			if(Uteis.getDiaSemana(dataInicio) == 1){
				dataInicio = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataInicio), -1);
			}
			dataInicio =  UteisData.obterDataFutura(UteisData.getPrimeiroDiaSemana(dataInicio),+1);			
			Date dataTermino = UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(dataInicio)), +1);			
			String key = rs.getInt("turno.codigo")+"-"+Uteis.getData(dataInicio)+" a "+Uteis.getData(dataTermino);
			//Montar o horario do aluno no turno especifico
			if(!horarioProfessorTurnoVOs.containsKey(rs.getInt("turno.codigo"))){
				horarioProfessorTurnoVO = realizarInicializarHorarioProfessorTurno(rs.getInt("turno.codigo"), usuario);
				horarioProfessorTurnoVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
				horarioProfessorTurnoVOs.put(rs.getInt("turno.codigo"), horarioProfessorTurnoVO);
			}else{
				horarioProfessorTurnoVO = horarioProfessorTurnoVOs.get(rs.getInt("turno.codigo"));
			}
			//Montar o horario do aluno no turno e semana especifico
			if (!horarioProfessorTurnoVO.getHorarioProfessorTurnoSemanaVOs().containsKey(key)) {
				horarioProfessorTurnoSemanalVO = realizarInicializarHorarioProfessorTurno(rs.getInt("turno.codigo"), usuario);
				horarioProfessorTurnoSemanalVO.setDataInicio(dataInicio);
				horarioProfessorTurnoSemanalVO.setDataTermino(dataTermino);
				horarioProfessorTurnoSemanalVO.setNaoApresentarProfessorVisaoAluno(rs.getBoolean("naoApresentarProfessorVisaoAluno"));
				horarioProfessorTurnoVO.getHorarioProfessorTurnoSemanaVOs().put(key, horarioProfessorTurnoSemanalVO);				
			}else{				
				horarioProfessorTurnoSemanalVO = horarioProfessorTurnoVO.getHorarioProfessorTurnoSemanaVOs().get(key);
			}
			
			turmaProfessorDisciplinaVO = new TurmaProfessorDisciplinaVO();
			turmaProfessorDisciplinaVO.getTurmaVO().setCodigo(rs.getInt("turma.codigo"));
			turmaProfessorDisciplinaVO.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			turmaProfessorDisciplinaVO.getProfessorVO().setCodigo(rs.getInt("pessoa.codigo"));
			turmaProfessorDisciplinaVO.getProfessorVO().setNome(rs.getString("pessoa.nome"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina.codigo"));
			turmaProfessorDisciplinaVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
			turmaProfessorDisciplinaVO.getSala().setCodigo(rs.getInt("sala.codigo"));
			turmaProfessorDisciplinaVO.getSala().setSala(rs.getString("sala.sala"));
			turmaProfessorDisciplinaVO.getSala().getLocalAula().setLocal(rs.getString("localaula.local"));
			adicionarTurmaProfessorDisciplinaVO(horarioProfessorTurnoSemanalVO, turmaProfessorDisciplinaVO, horarioProfessorTurnoVO.getTurno().getNaoApresentarHorarioVisaoAluno() ? null : rs.getInt("nrAula"), DiaSemana.getEnum(rs.getInt("diasemana")), rs.getDate("data"));
		}
		List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs2 = new ArrayList<HorarioProfessorTurnoVO>(horarioProfessorTurnoVOs.values());		
		for (Iterator iterator = horarioProfessorTurnoVOs2.iterator(); iterator.hasNext();) {
			HorarioProfessorTurnoVO horarioProfessorTurnoVO2 = (HorarioProfessorTurnoVO) iterator.next();
			horarioProfessorTurnoVO2.setPossuiAulaDomingo(false);
			horarioProfessorTurnoVO2.setPossuiAulaSegunda(false);
			horarioProfessorTurnoVO2.setPossuiAulaTerca(false);
			horarioProfessorTurnoVO2.setPossuiAulaQuarta(false);
			horarioProfessorTurnoVO2.setPossuiAulaQuinta(false);
			horarioProfessorTurnoVO2.setPossuiAulaSexta(false);
			horarioProfessorTurnoVO2.setPossuiAulaSabado(false);
			for (Iterator iterator3 = horarioProfessorTurnoVO2.getHorarioProfessorTurnoVOs().iterator(); iterator3.hasNext();) {
				HorarioProfessorTurnoVO horarioProfessorTurnoSemanal = (HorarioProfessorTurnoVO) iterator3.next();
				horarioProfessorTurnoSemanal.setPossuiAulaDomingo(false);
				horarioProfessorTurnoSemanal.setPossuiAulaSegunda(false);
				horarioProfessorTurnoSemanal.setPossuiAulaTerca(false);
				horarioProfessorTurnoSemanal.setPossuiAulaQuarta(false);
				horarioProfessorTurnoSemanal.setPossuiAulaQuinta(false);
				horarioProfessorTurnoSemanal.setPossuiAulaSexta(false);
				horarioProfessorTurnoSemanal.setPossuiAulaSabado(false);
			for (Iterator iterator2 = horarioProfessorTurnoSemanal.getHorarioProfessorTurnoNumeroAulaVOs().iterator(); iterator2.hasNext();) {
				HorarioProfessorTurnoNumeroAulaVO horarioProfessorTurnoNumeroAulaVO = (HorarioProfessorTurnoNumeroAulaVO) iterator2.next();
				if(horarioProfessorTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty() && horarioProfessorTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()
						&& horarioProfessorTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty() && horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()
						&& horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty() && horarioProfessorTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()
						&& horarioProfessorTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
					iterator2.remove();
				}else{
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaDomingo(true);
						horarioProfessorTurnoSemanal.setPossuiAulaDomingo(true);
					} 
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaSegunda(true);
						horarioProfessorTurnoSemanal.setPossuiAulaSegunda(true);
					}
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaTercaApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaTerca(true);
						horarioProfessorTurnoSemanal.setPossuiAulaTerca(true);
					}
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaQuarta(true);
						horarioProfessorTurnoSemanal.setPossuiAulaQuarta(true);
					}
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaQuinta(true);
						horarioProfessorTurnoSemanal.setPossuiAulaQuinta(true);
					}
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSextaApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaSexta(true);
						horarioProfessorTurnoSemanal.setPossuiAulaSexta(true);
					}
					if(!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().isEmpty()){
						horarioProfessorTurnoVO2.setPossuiAulaSabado(true);
						horarioProfessorTurnoSemanal.setPossuiAulaSabado(true);
					}
				}
			}
			}
		}
		if (!horarioProfessorTurnoVOs2.isEmpty()) {
			Ordenacao.ordenarLista(horarioProfessorTurnoVOs2.get(0).getHorarioProfessorTurnoVOs(), "dataInicio");
		}
		return horarioProfessorTurnoVOs2;
	}

	public HorarioProfessorTurnoVO realizarInicializarHorarioProfessorTurno(Integer turno, UsuarioVO usuario) throws Exception {
		HorarioProfessorTurnoVO horarioProfessorTurnoVO = new HorarioProfessorTurnoVO();
		horarioProfessorTurnoVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(turno, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioSegunda(), DiaSemana.SEGUNGA);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioTerca(), DiaSemana.TERCA);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioQuarta(), DiaSemana.QUARTA);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioQuinta(), DiaSemana.QUINTA);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioSexta(), DiaSemana.SEXTA);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioSabado(), DiaSemana.SABADO);
		realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(horarioProfessorTurnoVO, horarioProfessorTurnoVO.getTurno().getTurnoHorarioDomingo(), DiaSemana.DOMINGO);
		return horarioProfessorTurnoVO;

	}
	
	public void adicionarTurmaProfessorDisciplinaVO(HorarioProfessorTurnoVO horarioProfessorTurnoVO, TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO, Integer numeroAula, DiaSemana diaSemana, Date dataAula) {
		HorarioProfessorTurnoNumeroAulaVO obj = null;
		obj = horarioProfessorTurnoVO.getObterHorarioProfessorTurnoNumeroAulaVO(numeroAula, diaSemana);
		switch (diaSemana) {
		case SEGUNGA:
			obj.getTurmaProfessorDisciplinaSegundaVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataSegunda(dataAula);
			break;
		case TERCA:
			obj.getTurmaProfessorDisciplinaTercaVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataTerca(dataAula);
			break;
		case QUARTA:
			obj.getTurmaProfessorDisciplinaQuartaVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataQuarta(dataAula);
			break;
		case QUINTA:
			obj.getTurmaProfessorDisciplinaQuintaVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataQuinta(dataAula);
			break;
		case SEXTA:
			obj.getTurmaProfessorDisciplinaSextaVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataSexta(dataAula);
			break;
		case SABADO:
			obj.getTurmaProfessorDisciplinaSabadoVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataSabado(dataAula);
			break;
		case DOMINGO:
			obj.getTurmaProfessorDisciplinaDomingoVOs().add(turmaProfessorDisciplinaVO);
			horarioProfessorTurnoVO.setDataDomingo(dataAula);
			break;

		default:
			return;
		}
	}
	
	public void realizarInicializarHorarioProfessorTurnoHorarioDiaSemana(HorarioProfessorTurnoVO horarioProfessorTurnoVO, List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws Exception {
		for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
			HorarioProfessorTurnoNumeroAulaVO horarioProfessorTurnoNumeroAulaVO = horarioProfessorTurnoVO.getObterHorarioProfessorTurnoNumeroAulaVO(turnoHorarioVO.getNumeroAula(), diaSemana);
			horarioProfessorTurnoNumeroAulaVO.setNumeroAula(turnoHorarioVO.getNumeroAula());

			switch (diaSemana) {
			case SEGUNGA:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioSegunda(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoSegunda(turnoHorarioVO.getHorarioFinalAula());
				break;
			case TERCA:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioTerca(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoTerca(turnoHorarioVO.getHorarioFinalAula());
				break;
			case QUARTA:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioQuarta(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoQuarta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case QUINTA:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioQuinta(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoQuinta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case SEXTA:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioSexta(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoSexta(turnoHorarioVO.getHorarioFinalAula());
				break;
			case SABADO:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioSabado(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoSabado(turnoHorarioVO.getHorarioFinalAula());
				break;
			case DOMINGO:
				horarioProfessorTurnoNumeroAulaVO.setHorarioInicioDomingo(turnoHorarioVO.getHorarioInicioAula());
				horarioProfessorTurnoNumeroAulaVO.setHorarioTerminoDomingo(turnoHorarioVO.getHorarioFinalAula());
				break;

			default:
				return;
			}
			adicionarHorarioProfessorTurnoNumeroAulaVO(horarioProfessorTurnoVO, horarioProfessorTurnoNumeroAulaVO);

		}
	}
	
	public void adicionarHorarioProfessorTurnoNumeroAulaVO(HorarioProfessorTurnoVO horarioProfessorTurnoVO, HorarioProfessorTurnoNumeroAulaVO horarioProfessorTurnoNumeroAulaVO) {
		int index = 0;
		for (HorarioProfessorTurnoNumeroAulaVO obj : horarioProfessorTurnoVO.getHorarioProfessorTurnoNumeroAulaVOs()) {
			if (horarioProfessorTurnoNumeroAulaVO.getNumeroAula().equals(obj.getNumeroAula())) {
				horarioProfessorTurnoVO.getHorarioProfessorTurnoNumeroAulaVOs().set(index, horarioProfessorTurnoNumeroAulaVO);
				return;
			}
			index++;
		}
		horarioProfessorTurnoVO.getHorarioProfessorTurnoNumeroAulaVOs().add(horarioProfessorTurnoNumeroAulaVO);
	}

	@Override
	public List<CronogramaDeAulasRelVO> criarObjetoRelatorioSemanal(List<HorarioProfessorTurnoVO> lista, UsuarioVO usuarioVO) {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			HorarioProfessorTurnoVO horario = (HorarioProfessorTurnoVO)i.next();
			List<HorarioProfessorTurnoVO> horarioSemana = new ArrayList<HorarioProfessorTurnoVO>(horario.getHorarioProfessorTurnoSemanaVOs().values());
			Iterator o = horarioSemana.iterator();
			while (o.hasNext()) {
				HorarioProfessorTurnoVO h = (HorarioProfessorTurnoVO)o.next();
			
				Iterator j = h.getHorarioProfessorTurnoNumeroAulaVOs().iterator();
				while (j.hasNext()) {
					HorarioProfessorTurnoNumeroAulaVO horarioProfessorTurnoNumeroAulaVO = (HorarioProfessorTurnoNumeroAulaVO)j.next();
					
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaDomingoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorDomingoApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaDomingoApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaDomingoApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaDomingoApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioDomingo());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoDomingo());
						}
						cronograma.setData(h.getDataDomingo());	
						cronograma.setDataInicio(h.getDataDomingo());	
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("0Domingo");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSegundaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorSegundaApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaSegundaApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaSegundaApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaSegundaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioSegunda());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoSegunda());
						}
						cronograma.setData(h.getDataSegunda());
						cronograma.setDataInicio(h.getDataSegunda());
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("1Segunda");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaTercaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorTercaApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaTercaApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaTercaApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaTercaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioTerca());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoTerca());
						}
						cronograma.setDataInicio(h.getDataTerca());	
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("2Terça");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuartaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorQuartaApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuartaApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaQuartaApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaQuartaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioQuarta());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoQuarta());
						}
						cronograma.setData(h.getDataQuarta());	
						cronograma.setDataInicio(h.getDataQuarta());	
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("3Quarta");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuintaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorQuintaApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaQuintaApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaQuintaApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaQuintaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioQuinta());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoQuinta());
						}
						cronograma.setData(h.getDataQuinta());	
						cronograma.setDataInicio(h.getDataQuinta());	
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("4Quinta");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSextaApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorSextaApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaSextaApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaSextaApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaSextaApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioSexta());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoSexta());
						}
						cronograma.setData(h.getDataSexta());		
						cronograma.setDataInicio(h.getDataSexta());		
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("5Sexta");
						listaObjetos.add(cronograma);
					}
					if (!horarioProfessorTurnoNumeroAulaVO.getDisciplinaSabadoApresentar().equals("")) {
						CronogramaDeAulasRelVO cronograma = new CronogramaDeAulasRelVO();
						cronograma.setProfessor(horarioProfessorTurnoNumeroAulaVO.getProfessorSabadoApresentar());
						cronograma.setDisciplina(horarioProfessorTurnoNumeroAulaVO.getDisciplinaSabadoApresentar());
						cronograma.setTurma(horarioProfessorTurnoNumeroAulaVO.getTurmaSabadoApresentar());
						cronograma.setLocal(horarioProfessorTurnoNumeroAulaVO.getSalaSabadoApresentar());
						if(h.getTurno().getNaoApresentarHorarioVisaoAluno()){
							cronograma.setHorarioInicio("");
							cronograma.setHorarioTermino("");
						}else{
							cronograma.setHorarioInicio(horarioProfessorTurnoNumeroAulaVO.getHorarioInicioSabado());
							cronograma.setHorarioTermino(horarioProfessorTurnoNumeroAulaVO.getHorarioTerminoSabado());
						}
						cronograma.setData(h.getDataSabado());	
						cronograma.setDataInicio(h.getDataSabado());	
						cronograma.setOcultarhorarioaulavisaoprofessor(h.getTurno().getOcultarHorarioAulaVisaoProfessor());
						cronograma.setModulo("6Sábado");
						listaObjetos.add(cronograma);
					}
				}
			}
		}
		return listaObjetos;
	}
	
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HorarioAulaProfessorRel.jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	@Override
	public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(
			Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina,
			Date dataInicio, Date dataFim, Boolean visaoProfessor, Boolean visaoCoordenador, String nivelEducacional,
			UsuarioVO usuario, String ordenacao) throws Exception {
		return consultarHorariosProfessorSeparadoPorNivelEducacional(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, false, nivelEducacional, usuario, ordenacao, false, null);
	}

	@Override
	public List<HorarioProfessorDiaVO> consultarProfessorPorPeriodoTurmaAgrupada(Integer codigoProfessor,
			Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim,
			Boolean visaoProfessor, String ordenacao) throws Exception {
		return consultarProfessorPorPeriodoTurmaAgrupada(codigoProfessor, unidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, ordenacao, false, null);
	}

	@Override
	public List<CursoVO> montarCursoVinculadoProfessor(List<HorarioProfessorDiaItemVO> horarioProfessorDiaItemVOs) {
		List<CursoVO> listaCursoProfessor = new ArrayList<CursoVO>(); 
		for( HorarioProfessorDiaItemVO itemVO : horarioProfessorDiaItemVOs) {
			if(!listaCursoProfessor.contains(itemVO.getTurmaVO().getCurso())) {
				listaCursoProfessor.add(itemVO.getTurmaVO().getCurso());
			}
		}
		return listaCursoProfessor;
	}
	
	@Override
	public List<HorarioProfessorDiaVO> consultarHorarioProfessorDiaRegistroAulaAutomaticamente(UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct horarioProfessor.codigo as horarioprofessor, curso.niveleducacional, professor.codigo as professor, professor.nome as professor_nome, ('0'::VARCHAR(1)||TO_CHAR(horarioprofessordia.data, 'D'))::VARCHAR(2) as diasemana, ");
		sql.append(" turno.codigo as turno, turno.nome as turno_nome, turno.ocultarhorarioaulavisaoprofessor, horarioprofessordia.codigo as horarioprofessordia, horarioprofessordia.data, ");
		sql.append(" disciplina.codigo as disciplina, disciplina.nome as disciplina_nome, turma.codigo as turma, turma.identificadorturma, turma.turmaagrupada, turma.anual, turma.semestral, turma.subturma, turma.tiposubturma, horarioprofessordiaitem.nraula, ");
		sql.append(" horarioprofessordiaitem.duracaoaula, horarioprofessordiaitem.horarioinicio as horarioinicioaula, horarioprofessordiaitem.horariotermino as horariofinalaula, ");
		sql.append(" horarioprofessordiaitem.codigo as horarioprofessordiaitem, horarioprofessordiaitem.sala, sala.sala as sala_sala, localaula.local as localaula_local, ");
		sql.append(" registroaula.codigo > 0 as existeAulaRegistrada, curso.codigo as curso_codigo, curso.nome as curso_nome, curso.periodicidade as curso_periodicidade, curso.liberarRegistroAulaEntrePeriodo, horarioprofessordiaitem.horarioturmadiaitem, unidadeensino.codigo as unidadeEnsino, unidadeensino.nome as unidadeEnsinoNome, ");
		sql.append(" googlemeet.codigo as \"googlemeet.codigo\",");
		sql.append(" googlemeet.ideventocalendar as \"googlemeet.ideventocalendar\",");		
		sql.append(" googlemeet.ano as \"googlemeet.ano\",");		
		sql.append(" googlemeet.semestre as \"googlemeet.semestre\",");		
		sql.append(" googlemeet.linkgooglemeet as \"googlemeet.linkgooglemeet\",");
		sql.append(" googlemeet.horarioinicio as \"googlemeet.horarioinicio\",");
		sql.append(" googlemeet.horariotermino as \"googlemeet.horariotermino\",");
		sql.append(" googlemeet.diaevento as \"googlemeet.diaevento\",");	
		sql.append(" classroomGoogle.codigo as \"classroomGoogle.codigo\", classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
		sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");
		sql.append(" classroomGoogle.professorEad as \"classroomGoogle.professorEad\",  classroomGoogle.ano as \"classroomGoogle.ano\", ");
		sql.append(" classroomGoogle.semestre as \"classroomGoogle.semestre\", ");
		sql.append(" classroomGoogle.idTurma as \"classroomGoogle.idTurma\", ");
		sql.append(" classroomGoogle.emailTurma as \"classroomGoogle.emailTurma\", ");		
		sql.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", salaaulablackboard.linkSalaAulaBlackboard as \"salaaulablackboard.linkSalaAulaBlackboard\", ");
		sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  salaaulablackboard.id as \"salaaulablackboard.id\", ");
		sql.append(" salaaulablackboard.programacaoTutoriaOnline as \"salaaulablackboard.programacaoTutoriaOnline\",  salaaulablackboard.ano as \"salaaulablackboard.ano\", ");
		sql.append(" salaaulablackboard.semestre as \"salaaulablackboard.semestre\", ");
		sql.append(" ulch.codigo as \"ulch.codigo\", ulch.username as \"ulch.username\", horarioturmadia.codigo as horarioturmadia, horarioturma.codigo as horarioturma, horarioturma.anovigente, horarioturma.semestrevigente, cidade.nome as cidadeUnidadeEnsino, horarioturmadisciplinaprogramada.conteudo as conteudoRegistroAulaAutomatico");
		sql.append(" from horarioprofessor ");
		sql.append(" inner join pessoa as professor on professor.codigo = horarioProfessor.professor ");
		sql.append(" inner join horarioprofessordia on horarioprofessordia.horarioprofessor = horarioProfessor.codigo ");
		sql.append(" left join horarioprofessordiaitem on horarioprofessordiaitem.horarioprofessordia = horarioProfessorDia.codigo ");
		sql.append(" inner join turma on turma.codigo = horarioprofessordiaitem.turma ");
		sql.append(" inner join turno on turma.turno = turno.codigo ");
		sql.append(" inner join disciplina on horarioprofessordiaitem.disciplina = disciplina.codigo ");
		sql.append(" left join salalocalaula as sala on sala.codigo = horarioprofessordiaitem.sala ");
		sql.append(" left join localaula on localaula.codigo = sala.localaula ");		
		sql.append(" left join usuario as ulch on horarioprofessordiaitem.usuarioliberacaochoquehorario = ulch.codigo ");		
		sql.append(" inner join horarioturmadiaitem on horarioprofessordiaitem.horarioturmadiaitem = horarioturmadiaitem.codigo ");
		sql.append(" inner join horarioturmadia on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sql.append(" inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo ");	
		sql.append(" left join googlemeet on horarioturmadiaitem.googlemeet = googlemeet.codigo ");
		sql.append(" left join classroomgoogle  on ((classroomGoogle.codigo = googlemeet.classroomGoogle) or "); 
		sql.append(" (classroomGoogle.ano  = horarioturma.anovigente  and classroomGoogle.semestre  = horarioturma.semestrevigente ");  
		sql.append(" and classroomGoogle.disciplina = horarioturmadiaitem.disciplina and classroomGoogle.turma  = horarioturma.turma )) ");
		sql.append(" left join salaaulablackboard  on ( salaaulablackboard.ano  = horarioturma.anovigente  and salaaulablackboard.semestre  = horarioturma.semestrevigente ");  
		sql.append(" and salaaulablackboard.disciplina = horarioturmadiaitem.disciplina and salaaulablackboard.turma  = horarioturma.turma ) ");
		sql.append(" left join registroaula on registroaula.turma = turma.codigo and registroaula.disciplina = horarioprofessordiaitem.disciplina and registroaula.data = horarioProfessorDia.data and registroaula.nrAula = horarioprofessordiaitem.nrAula ");		
		sql.append(" and registroaula.ano = horarioturma.anovigente ");
		sql.append(" and registroaula.semestre = horarioturma.semestrevigente ");
		sql.append(" inner join curso on (turma.turmaagrupada and curso.codigo =  ( ");
		sql.append(" select t.curso from turmaagrupada ");
		sql.append(" inner join turma as t on t.codigo = turmaagrupada.turma ");
		sql.append(" where turmaagrupada.turmaorigem  = turma.codigo ");
		sql.append(" limit 1)) or (turma.turmaagrupada = false and curso.codigo =  turma.curso) ");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" inner join cidade on unidadeensino.cidade = cidade.codigo ");
		sql.append("inner join horarioturmadisciplinaprogramada on horarioturmadisciplinaprogramada.horarioturma = horarioturma.codigo and horarioturmadisciplinaprogramada.disciplina = horarioturmadiaitem.disciplina ");
		sql.append("where horarioturmadisciplinaprogramada.registraraulaautomaticamente = true ");
		sql.append("and horarioturmadia.data <= (current_date - '1 day'::interval) ");
		sql.append("and registroaula.codigo is null ");
		sql.append("and (horarioprofessordiaitem.registroAulaAutomaticoSucesso is null)");
		return montarDadosConsultaCompletaRegistroAulaAutomatico(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<HorarioProfessorDiaVO> montarDadosConsultaCompletaRegistroAulaAutomatico(SqlRowSet rs) throws Exception {
		Integer horarioProfessorDia = null;
		List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = new ArrayList<HorarioProfessorDiaVO>(0);
		HorarioProfessorDiaVO horarioProfessorDiaVO = null;
		while (rs.next()) {
			if (horarioProfessorDia == null || !horarioProfessorDia.equals(rs.getInt("horarioprofessordia"))) {
				horarioProfessorDia = rs.getInt("horarioprofessordia");
				horarioProfessorDiaVO = montarDadosConsultaCompletaHorarioProfessorDia(rs);
				horarioProfessorDiaVO.setAnoVigente(rs.getString("anoVigente"));
				horarioProfessorDiaVO.setSemestreVigente(rs.getString("semestreVigente"));
				horarioProfessorDiaVOs.add(horarioProfessorDiaVO);
			}
			if (rs.getBoolean("existeAulaRegistrada") && !horarioProfessorDiaVO.getIsLancadoRegistro()) {
				horarioProfessorDiaVO.setIsLancadoRegistro(rs.getBoolean("existeAulaRegistrada"));
			}
			horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs().add(montarDadosConsultaCompletaHorarioProfessorDiaItemRegistroAulaAutomatico(rs));
		}
		return horarioProfessorDiaVOs;
	}
	
	private HorarioProfessorDiaItemVO montarDadosConsultaCompletaHorarioProfessorDiaItemRegistroAulaAutomatico(SqlRowSet rs) throws Exception {
		HorarioProfessorDiaItemVO horarioProfessorDiaItemVO = new HorarioProfessorDiaItemVO();
		horarioProfessorDiaItemVO.setNovoObj(false);
		horarioProfessorDiaItemVO.setCodigo(rs.getInt("horarioProfessorDiaItem"));
		horarioProfessorDiaItemVO.getHorarioProfessorDiaVO().setCodigo(rs.getInt("horarioProfessorDia"));
		horarioProfessorDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
		horarioProfessorDiaItemVO.getSala().setSala(rs.getString("sala_sala"));
		horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("localAula_local"));
		horarioProfessorDiaItemVO.setDuracaoAula(rs.getInt("duracaoaula"));
		horarioProfessorDiaItemVO.setData(rs.getDate("data"));
		horarioProfessorDiaItemVO.setNrAula(rs.getInt("nrAula"));
		horarioProfessorDiaItemVO.setHorario(rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.setHorarioInicio(rs.getString("horarioinicioaula"));
		horarioProfessorDiaItemVO.setHorarioTermino(rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().setCodigo(rs.getInt("turma"));
		horarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma(rs.getString("identificadorTurma"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setCodigo(rs.getInt("curso_codigo"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setNome(rs.getString("curso_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setPeriodicidade(rs.getString("curso_periodicidade"));
		horarioProfessorDiaItemVO.getTurmaVO().getCurso().setLiberarRegistroAulaEntrePeriodo(rs.getBoolean("liberarRegistroAulaEntrePeriodo"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setCodigo(rs.getInt("turno"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setNome(rs.getString("turno_nome"));
		horarioProfessorDiaItemVO.getTurmaVO().getTurno().setOcultarHorarioAulaVisaoProfessor(rs.getBoolean("ocultarhorarioaulavisaoprofessor"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsinoNome"));
		horarioProfessorDiaItemVO.getTurmaVO().getUnidadeEnsino().getCidade().setNome(rs.getString("cidadeUnidadeEnsino"));
		horarioProfessorDiaItemVO.getTurmaVO().setTurmaAgrupada(rs.getBoolean("turmaagrupada"));
		horarioProfessorDiaItemVO.getTurmaVO().setAnual(rs.getBoolean("anual"));
		horarioProfessorDiaItemVO.getTurmaVO().setSemestral(rs.getBoolean("semestral"));
		horarioProfessorDiaItemVO.getTurmaVO().setSubturma(rs.getBoolean("subturma"));
		if (rs.getString("tiposubturma") != null) {
			horarioProfessorDiaItemVO.getTurmaVO().setTipoSubTurma(TipoSubTurmaEnum.valueOf(rs.getString("tiposubturma")));
		}
		horarioProfessorDiaItemVO.setAulaJaRegistrada(rs.getBoolean("existeAulaRegistrada"));
		horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setCodigo(rs.getInt("ulch.codigo"));
		horarioProfessorDiaItemVO.getUsuarioLiberacaoChoqueHorario().setUsername(rs.getString("ulch.username"));
		
		horarioProfessorDiaItemVO.setCodProfessor(rs.getInt("professor"));
		horarioProfessorDiaItemVO.setProfessor(rs.getString("professor_nome"));
		
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setCodigo(rs.getInt("horarioturmadiaitem"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().setCodigo(rs.getInt("horarioturmadia"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setCodigo(rs.getInt("horarioturma"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setAnoVigente(rs.getString("anovigente"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setSemestreVigente(rs.getString("semestrevigente"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().setTurma(horarioProfessorDiaItemVO.getTurmaVO());
		
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setDuracaoAula(rs.getInt("duracaoaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setData(rs.getDate("data"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setNrAula(rs.getInt("nrAula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorario(rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorarioInicio(rs.getString("horarioinicioaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().setHorarioTermino(rs.getString("horariofinalaula"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getFuncionarioVO().setCodigo(rs.getInt("professor"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getFuncionarioVO().setNome(rs.getString("professor_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setCodigo(rs.getInt("googlemeet.codigo"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getProfessorVO().setCodigo(rs.getInt("professor"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getProfessorVO().setNome(rs.getString("professor_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setAno(rs.getString("googlemeet.ano"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setSemestre(rs.getString("googlemeet.semestre"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setIdEventoCalendar(rs.getString("googlemeet.ideventocalendar"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setLinkGoogleMeet(rs.getString("googlemeet.linkgooglemeet"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setHorarioInicio(rs.getString("googlemeet.horarioinicio"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setHorarioTermino(rs.getString("googlemeet.horariotermino"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setDiaEvento(rs.getTimestamp("googlemeet.diaevento"));
		horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().setTurmaVO(horarioProfessorDiaItemVO.getTurmaVO());
		if(Uteis.isAtributoPreenchido(rs.getInt("classroomGoogle.codigo"))) {
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setCodigo((rs.getInt("classroomGoogle.codigo")));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setAno(rs.getString("classroomGoogle.ano"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setSemestre(rs.getString("classroomGoogle.semestre"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setLinkClassroom(rs.getString("classroomGoogle.linkClassroom"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdClassRoomGoogle(rs.getString("classroomGoogle.idClassRoomGoogle"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdTurma(rs.getString("classroomGoogle.idTurma"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setEmailTurma(rs.getString("classroomGoogle.emailTurma"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setIdCalendario(rs.getString("classroomGoogle.idCalendario"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().getProfessorEad().setCodigo(rs.getInt("classroomGoogle.professorEad"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getTurmaVO());
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getGoogleMeetVO().getDisciplinaVO());
		}
		if(Uteis.isAtributoPreenchido(rs.getInt("salaaulablackboard.codigo"))) {
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setCodigo((rs.getInt("salaaulablackboard.codigo")));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setAno(rs.getString("salaaulablackboard.ano"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setSemestre(rs.getString("salaaulablackboard.semestre"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(rs.getString("salaaulablackboard.linkSalaAulaBlackboard"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(rs.getString("salaaulablackboard.idSalaAulaBlackboard"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setId(rs.getString("salaaulablackboard.id"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().getProgramacaoTutoriaOnlineVO().setCodigo(rs.getInt("salaaulablackboard.programacaoTutoriaOnline"));
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setTurmaVO(horarioProfessorDiaItemVO.getTurmaVO());
			horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO().setDisciplinaVO(horarioProfessorDiaItemVO.getDisciplinaVO());
		}
		horarioProfessorDiaItemVO.setConteudoRegistroAulaAutomatico(rs.getString("conteudoRegistroAulaAutomatico"));
		horarioProfessorDiaItemVO.setNivelEducacional(rs.getString("niveleducacional"));
		
		return horarioProfessorDiaItemVO;
	}
	
	public Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> consultarHorariosProfessorSeparadoPorNivelEducacional(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Boolean visaoProfessor, String nivelEducacional, UsuarioVO usuario, String ordenacao) throws Exception {
		return consultarHorariosProfessorSeparadoPorNivelEducacional(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataInicio, dataFim, visaoProfessor, false, nivelEducacional, usuario, ordenacao);
	}
	
}


