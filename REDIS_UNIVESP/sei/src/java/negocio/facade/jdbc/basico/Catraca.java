package negocio.facade.jdbc.basico;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CatracaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.CatracaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Catraca extends ControleAcesso implements CatracaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Catraca() throws Exception {
		super();
		setIdEntidade("Catraca");
	}

	@Override
	public void persistir(CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.isNovoObj()) {
			incluir(obj, controlarAcesso, usuarioVO);
		} else {
			alterar(obj, controlarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(idEntidade, controlarAcesso, usuarioVO);
		obj.setDataCadastro(new Date());
		obj.setResponsavelCadastro(usuarioVO);
		try {
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" INSERT INTO catraca (ip, serie, resolucao, descricao, unidadeensino, responsavelcadastro, datacadastro, mensagemliberar, mensagembloquear, duracaomensagem, duracaodesbloquear, direcao, situacao) ");
			sqlStr.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
					sqlInserir.setString(1, obj.getIp());
					sqlInserir.setString(2, obj.getSerie());
					sqlInserir.setInt(3, obj.getResolucao());
					sqlInserir.setString(4, obj.getDescricao());
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
						sqlInserir.setInt(5, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavelCadastro())) {
						sqlInserir.setInt(6, obj.getResponsavelCadastro().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataCadastro())) {
						sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataCadastro()));
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getMensagemLiberar());
					sqlInserir.setString(9, obj.getMensagemBloquear());
					sqlInserir.setInt(10, obj.getDuracaoMensagem());
					sqlInserir.setInt(11, obj.getDuracaoDesbloquear());
					sqlInserir.setString(12, obj.getDirecao());
					sqlInserir.setString(13, obj.getSituacao());
					return sqlInserir;
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
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(idEntidade, controlarAcesso, usuarioVO);
		obj.setDataCadastro(new Date());
		obj.setResponsavelCadastro(usuarioVO);
		try {
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" UPDATE catraca SET ip=?, serie=?, resolucao=?, descricao=?, unidadeensino=?, responsavelcadastro=?, datacadastro=?, mensagemliberar=?, mensagembloquear=?, duracaomensagem=?, duracaodesbloquear=?, direcao=?, situacao=? ");
			sqlStr.append(" WHERE (codigo = ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
					sqlAlterar.setString(1, obj.getIp());
					sqlAlterar.setString(2, obj.getSerie());
					sqlAlterar.setInt(3, obj.getResolucao());
					sqlAlterar.setString(4, obj.getDescricao());
					if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
						sqlAlterar.setInt(5, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getResponsavelCadastro())) {
						sqlAlterar.setInt(6, obj.getResponsavelCadastro().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataCadastro())) {
						sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataCadastro()));
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getMensagemLiberar());
					sqlAlterar.setString(9, obj.getMensagemBloquear());
					sqlAlterar.setInt(10, obj.getDuracaoMensagem());
					sqlAlterar.setInt(11, obj.getDuracaoDesbloquear());
					sqlAlterar.setString(12, obj.getDirecao());
					sqlAlterar.setString(13, obj.getSituacao());
					sqlAlterar.setInt(14, obj.getCodigo());
					return sqlAlterar;
				}
			});
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(false);
			throw e;
		}
	}

	@Override
	public void excluir(CatracaVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			Catraca.excluir(getIdEntidade(), true, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" DELETE FROM catraca WHERE (codigo = ?) ");
			getConexao().getJdbcTemplate().update(sqlStr.toString(), new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public CatracaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM catraca WHERE codigo = ").append(codigoPrm);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!rs.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Catraca ).");
		}
		return montarDados(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<CatracaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		String sqlStr = "SELECT * FROM catraca WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<CatracaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		return null;
	}

	@Override
	public List<CatracaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM catraca WHERE sem_acentos(descricao) ILIKE '%").append(valorConsulta).append("%' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<CatracaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM catraca WHERE situacao = '").append(valorConsulta).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public CatracaVO consultarPorNumeroSerie(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM catraca WHERE serie = '").append(valorConsulta).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!rs.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Catraca ).");
		}
		return montarDados(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public CatracaVO consultarPorEnderecoIP(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM catraca WHERE ip = '").append(valorConsulta).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!rs.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Catraca ).");
		}
		return montarDados(rs, nivelMontarDados, usuarioVO);
	}

	public void validarDados(CatracaVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException("Informe a Descrição.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getIp())) {
			throw new ConsistirException("Informe o Endereço IP.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getSerie())) {
			throw new ConsistirException("Informe o Número de Série.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getResolucao())) {
			throw new ConsistirException("Informe a Resolução.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getDuracaoDesbloquear())) {
			throw new ConsistirException("Informe a Duração Desbloquear.");
		}
	}

	public List<CatracaVO> montarDadosConsulta(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<CatracaVO> catracaVOs = new ArrayList<CatracaVO>(0);
		while (dadosSQL.next()) {
			catracaVOs.add(montarDados(dadosSQL, nivelMontarDados, usuarioVO));
		}
		return catracaVOs;
	}

	public CatracaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CatracaVO obj = new CatracaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setIp(dadosSQL.getString("ip"));
		obj.setSerie(dadosSQL.getString("serie"));
		obj.setResolucao(dadosSQL.getInt("resolucao"));
		obj.setMensagemLiberar(dadosSQL.getString("mensagemliberar"));
		obj.setMensagemBloquear(dadosSQL.getString("mensagembloquear"));
		obj.setDuracaoMensagem(dadosSQL.getInt("duracaomensagem"));
		obj.setDuracaoDesbloquear(dadosSQL.getInt("duracaodesbloquear"));
		obj.setDirecao(dadosSQL.getString("direcao"));
		obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelcadastro"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.setDataCadastro(dadosSQL.getDate("datacadastro"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		montarDadosResponsavel(obj, nivelMontarDados, usuarioVO);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuarioVO);
		obj.setNovoObj(false);
		return obj;
	}

	public void montarDadosResponsavel(CatracaVO obj, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
			obj.setResponsavelCadastro(new UsuarioVO());
			return;
		}
		obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados, usuarioVO));
	}

	public void montarDadosUnidadeEnsino(CatracaVO obj, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuarioVO));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Catraca.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Catraca.idEntidade = idEntidade;
	}

	public List<CatracaVO> consultarCatracaComboBox(String situacao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao from catraca where 1=1 ");
		if (!situacao.equals("")) {
			sb.append(" and situacao = '").append(situacao).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CatracaVO> listaCatracaVOs = new ArrayList<CatracaVO>(0);
		while (tabelaResultado.next()) {
			CatracaVO obj = new CatracaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			listaCatracaVOs.add(obj);
		}
		return listaCatracaVOs;
	}
}
