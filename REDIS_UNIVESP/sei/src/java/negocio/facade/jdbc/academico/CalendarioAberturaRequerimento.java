package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CalendarioAberturaRequerimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CalendarioAberturaRequerimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CalendarioAberturaRequerimentoVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ArquivoVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see CalendarioAberturaRequerimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CalendarioAberturaRequerimento extends ControleAcesso implements CalendarioAberturaRequerimentoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CalendarioAberturaRequerimento() throws Exception {
		super();
		setIdEntidade("CalendarioAberturaRequerimento");
	}

	public static void setIdEntidade(String idEntidade) {
		CalendarioAberturaRequerimento.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	@Override
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CalendarioAberturaRequerimento.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM calendarioAberturaRequerimento WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CalendarioAberturaRequerimento.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM calendarioAberturaRequerimento WHERE sem_acentos( descricao ) ilike(sem_acentos(?)) ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static CalendarioAberturaRequerimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CalendarioAberturaRequerimentoVO obj = new CalendarioAberturaRequerimentoVO();

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())){
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(final CalendarioAberturaRequerimentoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			CalendarioAberturaRequerimento.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO calendarioAberturaRequerimento(descricao, unidadeensino) VALUES ( ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao().toUpperCase());
					if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())){
						sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());
					}else{
						sqlInserir.setNull(2, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getCalendarioAberturaTipoRequerimentoraPrazoFacade().incluir(obj.getCodigo(), obj.getCalendarioAberturaTipoRequerimentoraPrazoVOs(), usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}

	}

	@Override
	public void alterar(final CalendarioAberturaRequerimentoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			CalendarioAberturaRequerimento.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE calendarioAberturaRequerimento set descricao=?, unidadeensino=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())){
						sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());
					}else{
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());

					return sqlAlterar;
				}
			});
			getFacadeFactory().getCalendarioAberturaTipoRequerimentoraPrazoFacade().alterar(obj.getCodigo(), obj.getCalendarioAberturaTipoRequerimentoraPrazoVOs(), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(CalendarioAberturaRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			CalendarioAberturaRequerimento.excluir(getIdEntidade(), usuarioVO);
			String sql = "DELETE FROM calendarioAberturaRequerimento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public static void validarDados(CalendarioAberturaRequerimentoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException("O campo Descrição deve ser informado.");
		}
	}

}
