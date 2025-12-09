package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.LogTransferenciaBibliotecaExemplarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.LogTransferenciaBibliotecaExemplarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>AutorVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>AutorVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see AutorVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class LogTransferenciaBibliotecaExemplar extends ControleAcesso implements LogTransferenciaBibliotecaExemplarInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public LogTransferenciaBibliotecaExemplar() throws Exception {
		super();
		setIdEntidade("logTransgerenciaBibliotecaExemplar");
	}

	public static void setIdEntidade(String idEntidade) {
		LogTransferenciaBibliotecaExemplar.idEntidade = idEntidade;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(final LogTransferenciaBibliotecaExemplarVO obj, final UsuarioVO usuario) throws Exception {
		try {
			// MotivoEmprestimoPorHora.incluir(getIdEntidade(), verificarAcesso,
			// usuarioVO);
			final String sql = "INSERT INTO logtransferenciacatalogoperiodico(datatranferencia,usuario,bibliotecaorigem,bibliotecadestino,exemplar) VALUES ( ?,?,?,?,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(2, obj.getResponsavel().getCodigo());
					sqlInserir.setInt(3, obj.getBibliotecaOrigem().getCodigo());
					sqlInserir.setInt(4, obj.getBibliotecaDestino().getCodigo());
					sqlInserir.setInt(5, obj.getExemplar().getCodigo());

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
	public List consultarPorExemplar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM logtransferenciacatalogoperiodico WHERE exemplar = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static LogTransferenciaBibliotecaExemplarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		LogTransferenciaBibliotecaExemplarVO obj = new LogTransferenciaBibliotecaExemplarVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataTransferencia(dadosSQL.getTimestamp("datatranferencia"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("usuario"))) {
			obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("usuario"), nivelMontarDados, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("bibliotecaorigem"))) {
			obj.setBibliotecaOrigem(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(dadosSQL.getInt("bibliotecaorigem"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("bibliotecadestino"))) {
			obj.setBibliotecaDestino(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(dadosSQL.getInt("bibliotecadestino"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		return obj;
	}
}
