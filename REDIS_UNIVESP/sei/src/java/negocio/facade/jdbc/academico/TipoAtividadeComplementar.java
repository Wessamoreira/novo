package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoAtividadeComplementarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoAtividadeComplementar extends ControleAcesso implements TipoAtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TipoAtividadeComplementar() {
		super();
		this.setIdEntidade("TipoAtividadeComplementar");
	}

	public TipoAtividadeComplementarVO novo() throws Exception {
		TipoAtividadeComplementar.incluir(TipoAtividadeComplementar.getIdEntidade());
		TipoAtividadeComplementarVO obj = new TipoAtividadeComplementarVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			validarUnicidade(obj, usuario);
			TipoAtividadeComplementar.incluir(TipoAtividadeComplementar.getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO tipoAtividadeComplementar( nome, tipoAtividadeComplementarSuperior ) " + "VALUES (?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					if (obj.getTipoAtividadeComplementarSuperior().getCodigo() == 0) {
						sqlInserir.setNull(2, 0);
					} else {
						sqlInserir.setInt(2, obj.getTipoAtividadeComplementarSuperior().getCodigo());
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			validarUnicidade(obj, usuario);
			TipoAtividadeComplementar.alterar(TipoAtividadeComplementar.getIdEntidade(), true, usuario);
			final String sql = "UPDATE tipoAtividadeComplementar set nome = ?, tipoAtividadeComplementarSuperior = ? where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					if (obj.getTipoAtividadeComplementarSuperior().getCodigo() == 0) {
						sqlAlterar.setNull(2, 0);
					} else {
						sqlAlterar.setInt(2, obj.getTipoAtividadeComplementarSuperior().getCodigo());
					}
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
			getAplicacaoControle().removerTipoAtividadeComplementar(obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		try {
			TipoAtividadeComplementar.excluir(TipoAtividadeComplementar.getIdEntidade(), true, usuario);
			String sql = "DELETE FROM tipoAtividadeComplementar where codigo=?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			TipoAtividadeComplementar.getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}

	}

	public ArrayList<TipoAtividadeComplementarVO> consultar(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipoAtividadeComplementar ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (this.montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public ArrayList<TipoAtividadeComplementarVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipoAtividadeComplementar WHERE sem_acentos(trim(nome)) ilike(sem_acentos(trim(?))) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (this.montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public ArrayList<TipoAtividadeComplementarVO> consultarPorCodigo(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipoAtividadeComplementar WHERE codigo = ? ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		return (this.montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public TipoAtividadeComplementarVO consultarPorChavePrimaria(int valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getTipoAtividadeComplementarVO(valorConsulta, usuario);
		}

	public TipoAtividadeComplementarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TipoAtividadeComplementarVO obj = new TipoAtividadeComplementarVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getTipoAtividadeComplementarSuperior().setCodigo(dadosSQL.getInt("tipoAtividadeComplementarSuperior"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		// Nunca colocar este montar dados acima da linha anterior if
		// (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS senão poderá
		// gerar loop infinito
		montarDadosTipoAtividadeComplementarSuperior(obj, usuario);
		return obj;
	}

	public static void montarDadosTipoAtividadeComplementarSuperior(TipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTipoAtividadeComplementarSuperior().getCodigo().intValue() == 0) {
			obj.setTipoAtividadeComplementarSuperior(new TipoAtividadeComplementarVO());
			return;
		}
		obj.setTipoAtividadeComplementarSuperior(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(obj.getTipoAtividadeComplementarSuperior().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
	}

	public ArrayList<TipoAtividadeComplementarVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ArrayList<TipoAtividadeComplementarVO> vetResultado = new ArrayList<TipoAtividadeComplementarVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static void validarDados(TipoAtividadeComplementarVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Tipo Atividade Complementar) deve ser informado.");
		}
		if (!obj.getTipoAtividadeComplementarSuperior().getCodigo().equals(0) && obj.getCodigo().intValue() == obj.getTipoAtividadeComplementarSuperior().getCodigo().intValue()) {
			throw new ConsistirException("O campo TIPO ATIVIDADE COMPLEMENTAR SUPERIOR (Tipo Atividade Complementar) não pode ser o próprio tipo atividade complementar.");
		}
		if (!obj.getCodigo().equals(0) && !obj.getTipoAtividadeComplementarSuperior().getCodigo().equals(0) 
				&& obj.getTipoAtividadeComplementarSuperior().getTipoAtividadeComplementarSuperior().getCodigo().intValue() > 0				
				&& obj.getTipoAtividadeComplementarSuperior().getTipoAtividadeComplementarSuperior().getCodigo().intValue() == obj.getCodigo().intValue()) {
			throw new ConsistirException("O campo TIPO ATIVIDADE COMPLEMENTAR SUPERIOR informado tem como superior este próprio tipo de atividade.");
		}
	}

	public void validarUnicidade(TipoAtividadeComplementarVO obj, UsuarioVO usuario) throws Exception, ConsistirException {
		StringBuilder sql = new StringBuilder("select codigo from tipoAtividadeComplementar where upper(sem_acentos(trim(nome))) =  upper(sem_acentos(trim(?))) and codigo != ? ");
		if (getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNome(), obj.getCodigo()).next()) {
			throw new ConsistirException("Já existe um Tipo Atividade Complementar com esse nome.");
		}

	}

	public static String getIdEntidade() {
		return TipoAtividadeComplementar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		TipoAtividadeComplementar.idEntidade = idEntidade;
	}
	
	public ArrayList<TipoAtividadeComplementarVO> consultarPorCursoTurmaMatricula(Integer curso, String identificadorTurma, boolean turmaAgrupada, String matricula, Integer tipoAtividadeComplementarAtual, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct tac.* from tipoatividadecomplementar tac ");
		sqlStr.append("inner join gradecurriculartipoatividadecomplementar gctac on gctac.tipoatividadecomplementar = tac.codigo ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append("inner join gradecurricular gc on gc.codigo = gctac.gradecurricular ");
		}
		if (Uteis.isAtributoPreenchido(identificadorTurma)) {
			sqlStr.append("inner join turma t on t.gradecurricular = gctac.gradecurricular ");
			if (turmaAgrupada) {
				sqlStr.append("inner join turmaagrupada ta on ta.turma = t.codigo ");
				sqlStr.append("inner join turma t2 on t2.codigo = ta.turmaorigem ");
			}
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append("inner join matricula m on m.gradecurricularatual = gctac.gradecurricular ");
		}
		sqlStr.append("where 1 = 1 ");
		sqlStr.append(" and gctac.permiteCadastrarAtividadeParaAluno = true ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and gc.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(identificadorTurma)) {
			if (turmaAgrupada) {
				sqlStr.append(" and t2.identificadorturma = '").append(identificadorTurma).append("'");
			} else {
				sqlStr.append(" and t.identificadorturma = '").append(identificadorTurma).append("'");
			}
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and m.matricula = '").append(matricula).append("'");
		}
		if (Uteis.isAtributoPreenchido(tipoAtividadeComplementarAtual)) {
			sqlStr.append(" union select tac2.* from tipoatividadecomplementar tac2 where tac2.codigo = ").append(tipoAtividadeComplementarAtual);
		}
		sqlStr.append(" order by nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (this.montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<TipoAtividadeComplementarVO> consultarTipoAtividadeComplementarComCargaHorariasPeriodoLetivo(Integer tipoAtividadeComplementar, Integer curso, String matricula, PeriodicidadeEnum periodicidadeEnum, Date dataBase, Integer codigoRegistroAtividadeComplementarMatriculaDesconsiderar) throws Exception {
		List<TipoAtividadeComplementarVO> lista = new ArrayList<TipoAtividadeComplementarVO>();
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select tipoatividadecomplementar.codigo, tipoatividadecomplementar.nome, ");
		sqlStr.append(" tipoatividadecomplementar.tipoatividadecomplementarsuperior,  ");
		sqlStr.append(" gradeCurricularTipoAtividadeComplementar.horamaximaporperiodoletivo, ");		
		sqlStr.append(" sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as qtdecargahorariaconsiderada ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select (case when tipoatividadecomplementar.tipoatividadecomplementarsuperior is null then tipoatividadecomplementar.codigo else tipoatividadecomplementar.tipoatividadecomplementarsuperior end) tipoatividadecomplementarsuperior ");
		sqlStr.append(" from tipoatividadecomplementar  where codigo = ").append(tipoAtividadeComplementar);
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join tipoatividadecomplementar on tipoatividadecomplementar.tipoatividadecomplementarsuperior = t.tipoatividadecomplementarsuperior or tipoatividadecomplementar.codigo = t.tipoatividadecomplementarsuperior ");
		sqlStr.append(" inner join gradeCurricularTipoAtividadeComplementar on gradeCurricularTipoAtividadeComplementar.tipoatividadecomplementar = tipoatividadecomplementar.codigo");
		sqlStr.append(" inner join gradecurricular on gradeCurricularTipoAtividadeComplementar.gradecurricular = gradecurricular.codigo and gradecurricular.curso = ").append(curso);
		sqlStr.append(" inner join matricula on matricula.gradecurricularatual = gradecurricular.codigo and matricula.matricula = '").append(matricula).append("' ");		
		sqlStr.append(" left join registroatividadecomplementarmatricula on registroatividadecomplementarmatricula.tipoatividadecomplementar = tipoatividadecomplementar.codigo and registroatividadecomplementarmatricula.matricula ='").append(matricula).append("' ");
		sqlStr.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		if(Uteis.isAtributoPreenchido(codigoRegistroAtividadeComplementarMatriculaDesconsiderar)) {
			sqlStr.append(" and 	registroatividadecomplementarmatricula.codigo != ").append(codigoRegistroAtividadeComplementarMatriculaDesconsiderar);
		}
		if (periodicidadeEnum != null && dataBase != null) {
			sqlStr.append(" and exists (select codigo from registroatividadecomplementar where registroatividadecomplementarmatricula.registroatividadecomplementar = registroatividadecomplementar.codigo ");
			if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
				sqlStr.append(" and extract(year from registroatividadecomplementar.data) = ").append(Uteis.getAno(dataBase));
			} else if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and extract(year from registroatividadecomplementar.data) = ").append(Uteis.getAno(dataBase));
				if (Uteis.getSemestreData(dataBase).equals("1")) {
					sqlStr.append(" and extract(month from registroatividadecomplementar.data) >= 1 and extract(month from registroatividadecomplementar.data) <= 7 ");
				} else {
					sqlStr.append(" and extract(month from registroatividadecomplementar.data) >= 8 and extract(month from registroatividadecomplementar.data) <= 12 ");
				}
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append(" group by tipoatividadecomplementar.codigo, tipoatividadecomplementar.nome, tipoatividadecomplementar.tipoatividadecomplementarsuperior,gradeCurricularTipoAtividadeComplementar.horamaximaporperiodoletivo ");
		
		sqlStr.append(" order by tipoatividadecomplementar.tipoatividadecomplementarsuperior desc, tipoatividadecomplementar.nome ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (rs.next()) {
			TipoAtividadeComplementarVO obj = new TipoAtividadeComplementarVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			obj.setCargaHorasPermitidasPeriodoLetivo(rs.getInt("horamaximaporperiodoletivo"));
			obj.setCargaHorasJaRealizadaPeriodoLetivo(rs.getInt("qtdecargahorariaconsiderada"));
			lista.add(obj);
		}
		return lista;
	}
	
	@Override
	public ArrayList<TipoAtividadeComplementarVO> consultarPorTipoSuperior(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT tipoAtividadeComplementar.* FROM tipoAtividadeComplementar inner join tipoAtividadeComplementar as superior on superior.codigo = tipoAtividadeComplementar.tipoAtividadeComplementarSuperior  WHERE sem_acentos(trim(superior.nome)) ilike(sem_acentos(trim(?))) ORDER BY tipoAtividadeComplementar.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (this.montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}
	
	@Override
	public TipoAtividadeComplementarVO consultarPorNomeTipoAtividade(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM tipoAtividadeComplementar WHERE sem_acentos(trim(nome)) ilike(sem_acentos(trim(?))) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		
		return tabelaResultado.next() ? (this.montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)) : null;
	}

	@Override
	public TipoAtividadeComplementarVO consultarPorChavePrimariaUnico(int valorConsulta, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM tipoAtividadeComplementar WHERE codigo = ? ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
}
		return new TipoAtividadeComplementarVO();
	}

}
