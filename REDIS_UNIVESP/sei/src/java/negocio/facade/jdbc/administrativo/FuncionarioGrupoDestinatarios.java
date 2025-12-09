package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FuncionarioGrupoDestinatariosInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class FuncionarioGrupoDestinatarios extends ControleAcesso implements FuncionarioGrupoDestinatariosInterfaceFacade {

	protected static String idEntidade;

	public FuncionarioGrupoDestinatarios() throws Exception {
		super();
		setIdEntidade("GrupoDestinatarios");
	}

	public FuncionarioGrupoDestinatariosVO novo() throws Exception {
		FuncionarioGrupoDestinatarios.incluir(getIdEntidade());
		FuncionarioGrupoDestinatariosVO obj = new FuncionarioGrupoDestinatariosVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final FuncionarioGrupoDestinatariosVO obj) throws Exception {

		final String sql = "INSERT INTO funcionarioGrupoDestinatarios ( funcionario, grupoDestinatarios ) VALUES ( ?, ? )";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				if (obj.getFuncionario().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getFuncionario().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setInt(2, obj.getGrupoDestinatarios());
				return sqlInserir;
			}
		});
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FuncionarioGrupoDestinatariosVO obj) throws Exception {
		String sql = "DELETE FROM funcionarioGrupoDestinatarios WHERE funcionario = ? AND grupoDestinatarios = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getFuncionario().getCodigo(), obj.getGrupoDestinatarios() });
	}
	
	public List<FuncionarioGrupoDestinatariosVO> consultarPorCodigoGrupoDestinatarios(Integer codigoGrupo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sql = "SELECT * FROM funcionarioGrupoDestinatarios WHERE grupoDestinatarios = " + codigoGrupo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public static List<FuncionarioGrupoDestinatariosVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FuncionarioGrupoDestinatariosVO> vetResultado = new ArrayList<FuncionarioGrupoDestinatariosVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static FuncionarioGrupoDestinatariosVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FuncionarioGrupoDestinatariosVO obj = new FuncionarioGrupoDestinatariosVO();
		obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
		obj.setGrupoDestinatarios(dadosSQL.getInt("grupoDestinatarios"));
		montarDadosFuncionario(obj, nivelMontarDados, usuario);
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}
	
	public static void montarDadosFuncionario(FuncionarioGrupoDestinatariosVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarFuncionarioParaGrupoDestinatarios(obj.getFuncionario().getCodigo(), nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGrupoDestinatarios(List<FuncionarioGrupoDestinatariosVO> listaFuncionarioGrupoDestinatariosVOs) throws Exception {
		for (FuncionarioGrupoDestinatariosVO FuncionarioGrupoDestinatariosVO : listaFuncionarioGrupoDestinatariosVOs) {
			excluir(FuncionarioGrupoDestinatariosVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGrupoDestinatarioss(Integer codigoGrupo, List<FuncionarioGrupoDestinatariosVO> FuncionarioGrupoDestinatariosVOs) throws Exception {
		String str = "DELETE FROM funcionarioGrupoDestinatarios WHERE grupoDestinatarios = " + codigoGrupo;
		for (FuncionarioGrupoDestinatariosVO FuncionarioGrupoDestinatariosVO : FuncionarioGrupoDestinatariosVOs) {
			str += " AND funcionario <> " + FuncionarioGrupoDestinatariosVO.getFuncionario().getCodigo();
		}
		getConexao().getJdbcTemplate().update(str);
		for (FuncionarioGrupoDestinatariosVO FuncionarioGrupoDestinatariosVO : FuncionarioGrupoDestinatariosVOs) {
			if (FuncionarioGrupoDestinatariosVO.getGrupoDestinatarios() == null || FuncionarioGrupoDestinatariosVO.getGrupoDestinatarios() == 0) {
				FuncionarioGrupoDestinatariosVO.setGrupoDestinatarios(codigoGrupo);
				incluir(FuncionarioGrupoDestinatariosVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirListaFuncionarioGrupoDestinatarios(Integer codigo, List<FuncionarioGrupoDestinatariosVO> listaFuncionarioGrupoDestinatarios) throws Exception {
		for (FuncionarioGrupoDestinatariosVO fgdVO : listaFuncionarioGrupoDestinatarios) {
			fgdVO.setGrupoDestinatarios(codigo);
			incluir(fgdVO);
		}
	}

	public static String getIdEntidade() {
		return FuncionarioGrupoDestinatarios.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FuncionarioGrupoDestinatarios.idEntidade = idEntidade;
	}
}