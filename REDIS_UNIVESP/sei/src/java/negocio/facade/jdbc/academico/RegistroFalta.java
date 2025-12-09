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

import negocio.comuns.academico.RegistroFaltaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.RegistroFaltaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroFalta extends ControleAcesso implements RegistroFaltaInterfaceFacade {

	private static final long serialVersionUID = 560459509718640369L;
	protected static String idEntidade;

	public RegistroFalta() throws Exception {
		super();
		setIdEntidade("RegistrarFalta");
	}

	public RegistroFalta novo() throws Exception {
		RegistroFalta.incluir(getIdEntidade());
		RegistroFalta obj = new RegistroFalta();
		return obj;
	}

	public void validarDados(RegistroFaltaVO registroFaltaVO, UsuarioVO usuarioVO) throws Exception {
		if (registroFaltaVO.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFaltaMatricula"));
		}
		if(registroFaltaVO.getAno().equals("") || registroFaltaVO.getAno().length() < 4 ){
			throw new Exception(UteisJSF.internacionalizar("Informe um ano valido."));
		}
		if (usuarioVO.getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFaltaResponsavelCadastro"));
		}
		if (registroFaltaVO.getMatriculaVO().getCurso().getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFaltaCurso"));
		}
		boolean existeFaltaRegistradaParaMatriculaMesmoDia = this.consultarFaltaExistenteMatriculaDataFalta(registroFaltaVO.getCodigo(),registroFaltaVO.getMatriculaVO().getMatricula(), registroFaltaVO.getDataFalta(), false, usuarioVO);
		if (existeFaltaRegistradaParaMatriculaMesmoDia) {
			throw new Exception(UteisJSF.internacionalizar("msg_RegistrarFaltaParaMatricula").replace("{0}", Uteis.getData(registroFaltaVO.getDataFalta())));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RegistroFaltaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final RegistroFaltaVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj, usuarioVO);
			RegistroFalta.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO registrofalta(matricula, ano, semestre, datafalta, motivofalta, bimestre, datacadastro, responsavelcadastro) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
						sqlInserir.setString(2, obj.getAno());
						sqlInserir.setString(3, obj.getSemestre());
						sqlInserir.setDate(4, Uteis.getSQLData(obj.getDataFalta()));
						sqlInserir.setString(5, obj.getMotivoFalta());
						sqlInserir.setString(6, obj.getBimestre());
						sqlInserir.setDate(7, Uteis.getSQLData(obj.getDataCadastro()));
						sqlInserir.setInt(8, usuarioVO.getCodigo());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final RegistroFaltaVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj, usuarioVO);
			RegistroFalta.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE registrofalta SET matricula=?, ano=?, semestre=?, datafalta=?, motivofalta=?, bimestre=?, datacadastro=?, responsavelcadastro=? WHERE (codigo =" + obj.getCodigo() + ")" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getMatriculaVO().getMatricula());
					sqlAlterar.setString(2, obj.getAno());
					sqlAlterar.setString(3, obj.getSemestre());
					sqlAlterar.setDate(4, Uteis.getSQLData(obj.getDataFalta()));
					sqlAlterar.setString(5, obj.getMotivoFalta());
					sqlAlterar.setString(6, obj.getBimestre());
					sqlAlterar.setDate(7, Uteis.getSQLData(obj.getDataCadastro()));
					sqlAlterar.setInt(8, usuarioVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroFaltaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM registrofalta WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public List<RegistroFaltaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<RegistroFaltaVO> registrarFaltaVOs = new ArrayList<RegistroFaltaVO>(0);
		while (tabelaResultado.next()) {
			registrarFaltaVOs.add(montarDados(tabelaResultado, nivelMontarDados, unidadeEnsino, usuarioVO));
		}
		return registrarFaltaVOs;
	}

	private RegistroFaltaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		RegistroFaltaVO obj = new RegistroFaltaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setDataFalta(tabelaResultado.getDate("datafalta"));
		obj.setMotivoFalta(tabelaResultado.getString("motivofalta"));
		obj.setBimestre(tabelaResultado.getString("bimestre"));
		obj.setDataCadastro(tabelaResultado.getDate("datacadastro"));
		obj.setResponsavelcadastro(tabelaResultado.getInt("responsavelcadastro"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaVO().getMatricula(), unidadeEnsino, NivelMontarDados.BASICO, usuarioVO));
		return obj;
	}

	public static void validarDadosConsulta(Integer unidadeEnsino) throws Exception {
		if (unidadeEnsino.equals(0)) {
			throw new  Exception(UteisJSF.internacionalizar("msg_RegistrarFaltaConsultaUnidadeEnsino"));
		}
	}

	@Override
	public List<RegistroFaltaVO> consultar(Integer unidadeEnsino, String matricula, Integer curso, Date dataInicio, Date dataFim, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		validarDadosConsulta(unidadeEnsino);
		RegistroFalta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT registrofalta.* FROM registrofalta ");
		sqlStr.append("INNER JOIN matricula on matricula.matricula = registrofalta.matricula ");
		sqlStr.append("WHERE matricula.unidadeEnsino = ").append(unidadeEnsino);
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" AND matricula.matricula = '").append(matricula).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataInicio)) {
			sqlStr.append(" AND registrofalta.dataFalta >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataFim)) {
			sqlStr.append(" AND registrofalta.dataFalta <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		sqlStr.append(" ORDER BY registrofalta.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, unidadeEnsino, usuarioVO);
	}

	public List<RegistroFaltaVO> consultarPorMatricula(String descricao, boolean verificarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		RegistroFalta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM registrofalta WHERE matricula ilike('").append(descricao).append("%') order by datafalta");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, unidadeEnsino, usuarioVO);
	}

	public boolean consultarFaltaExistenteMatriculaDataFalta(Integer codigo,String matricula, Date dataFalta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		RegistroFalta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT codigo from RegistroFalta ");
		sqlStr.append("WHERE matricula = '").append(matricula).append("' ");
		sqlStr.append("AND dataFalta = '").append(Uteis.getDataJDBC(dataFalta)).append("' ");
		if(Uteis.isAtributoPreenchido(codigo)){
			sqlStr.append("AND codigo NOT IN (").append(codigo).append(")");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	@Override
	public RegistroFaltaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		RegistroFalta.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM registrofalta WHERE codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, unidadeEnsino, usuarioVO);
		}
		return new RegistroFaltaVO();
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		RegistroFalta.idEntidade = idEntidade;
	}

}
