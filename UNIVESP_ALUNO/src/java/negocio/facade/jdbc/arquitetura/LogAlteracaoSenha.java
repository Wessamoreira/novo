/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.arquitetura;

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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.LogAlteracaoSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.interfaces.arquitetura.LogAlteracaoSenhaInterfaceFacade;

/**
 *
 * @author Kennedy Souza
 */
@Repository
@Scope("singleton")
@Lazy
public class LogAlteracaoSenha extends ControleAcesso implements LogAlteracaoSenhaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public LogAlteracaoSenha() throws Exception {
		super();
		setIdEntidade("LogAlteracaoSenha");
	}

	public void setIdEntidade(String idEntidade) {
		LogAlteracaoSenha.idEntidade = idEntidade;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(LogAlteracaoSenhaVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO logalteracaosenha(usuario,usuarioresposaveloperacao,senha,dataalteracao)" + " VALUES ( ?,?,?,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getUsuario().getCodigo());
					sqlInserir.setInt(2, obj.getUsuarioResponsavelAlteracao().getCodigo());
					sqlInserir.setString(3, obj.getSenha());
					sqlInserir.setTimestamp(4, UteisData.getDataJDBCTimestamp(obj.getData()));
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
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}

	}

	@Override
	public void consultarSenhaJaUtilizada(String valorConsulta, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		List<LogAlteracaoSenhaVO> lista = new ArrayList<LogAlteracaoSenhaVO>();
		if (configuracaoGeralSistema.getNivelcontrolealteracaosenha() > 0) {
			String sqlStr = "SELECT * FROM logalteracaosenha WHERE usuario = " + usuario.getCodigo() + " order by codigo desc limit " + configuracaoGeralSistema.getNivelcontrolealteracaosenha().toString();
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

			lista.addAll(montarDadosConsulta(tabelaResultado));

			validarSenhaJaUtilizada(lista, valorConsulta, configuracaoGeralSistema);
		} else {
			String sqlStr = "SELECT * FROM logalteracaosenha WHERE senha = encode(digest('" + valorConsulta + "', 'sha256'::text), 'hex') and usuario = " + usuario.getCodigo() + " limit 1";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			while (tabelaResultado.next()) {
				throw new ConsistirException("Por Favor Cadastre uma Senha Nunca Utilizada Para Este Usuário ");

			}

		}

	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
		}
		return vetResultado;
	}

	public static LogAlteracaoSenhaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		LogAlteracaoSenhaVO obj = new LogAlteracaoSenhaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getTimestamp("dataalteracao"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getUsuarioResponsavelAlteracao().setCodigo(dadosSQL.getInt("usuarioresposaveloperacao"));
		obj.setSenha(dadosSQL.getString("senha"));
		return obj;
	}

	public void validarSenhaJaUtilizada(List<LogAlteracaoSenhaVO> lista, String valorConsulta, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		for (LogAlteracaoSenhaVO object : lista) {
			if (Uteis.encriptar(valorConsulta).equals(object.getSenha())) {
				throw new ConsistirException("Por Favor Cadastre uma Senha Diferente das ultimas " + configuracaoGeralSistema.getNivelcontrolealteracaosenha() + " Utilizada(s) Para Este Usuário");
			}
		}
	}
}
