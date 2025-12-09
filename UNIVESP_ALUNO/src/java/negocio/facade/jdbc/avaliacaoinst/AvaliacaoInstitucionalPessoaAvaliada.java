package negocio.facade.jdbc.avaliacaoinst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoInstitucionalPessoaAvaliada extends ControleAcesso implements AvaliacaoInstitucionalPessoaAvaliadaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2742964929257141482L;
	protected static String idEntidade;

	public AvaliacaoInstitucionalPessoaAvaliada() throws Exception {
		super();
		setIdEntidade("AvaliacaoInstitucionalPessoaAvaliada");
	}

	public static String getIdEntidade() {
		return AvaliacaoInstitucionalPessoaAvaliada.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		AvaliacaoInstitucionalPessoaAvaliada.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<AvaliacaoInstitucionalPessoaAvaliadaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (AvaliacaoInstitucionalPessoaAvaliadaVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}	
		}
	}
	
	

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AvaliacaoInstitucionalPessoaAvaliadaVO obj, boolean verificarAcesso,  final UsuarioVO usuarioVO) throws Exception {
		try {

			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder("INSERT INTO AvaliacaoInstitucionalPessoaAvaliada ( avaliacaoInstitucional, pessoa) VALUES ( ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getAvaliacaoInstitucionalVO().getCodigo().intValue());
					sqlInserir.setInt(2, obj.getPessoaVO().getCodigo().intValue());
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoInstitucionalPessoaAvaliadaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
		alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder("UPDATE AvaliacaoInstitucionalPessoaAvaliada set avaliacaoInstitucional=?,  pessoa=? where codigo=? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getAvaliacaoInstitucionalVO().getCodigo().intValue());
					sqlAlterar.setInt(2, obj.getPessoaVO().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AvaliacaoInstitucionalPessoaAvaliadaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM AvaliacaoInstitucionalPessoaAvaliada WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });

		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( AvaliacaoInstitucionalPessoaAvaliada ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<AvaliacaoInstitucionalPessoaAvaliadaVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(" SELECT avaliacaoinstitucionalpessoaavaliada.codigo as \"avaliacaoinstitucionalpessoaavaliada.codigo\", ");
		sb.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sb.append(" avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional as \"avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional\" ");
		sb.append(" FROM avaliacaoinstitucionalpessoaavaliada ");
		sb.append(" inner join pessoa on pessoa.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
		sb.append(" WHERE avaliacaoinstitucionalpessoaavaliada.avaliacaoInstitucional = ? order by pessoa.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { avaliacaoInstitucional });
		List<AvaliacaoInstitucionalPessoaAvaliadaVO> vetResultado = new ArrayList<AvaliacaoInstitucionalPessoaAvaliadaVO>(0);
		while (tabelaResultado.next()) {
			AvaliacaoInstitucionalPessoaAvaliadaVO obj = new AvaliacaoInstitucionalPessoaAvaliadaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("avaliacaoinstitucionalpessoaavaliada.codigo")));
			obj.getAvaliacaoInstitucionalVO().setCodigo(tabelaResultado.getInt("avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional"));
			obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoaVO().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getPessoaVO().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoaOrdemNovaAntiga(obj.getPessoaVO().getCodigo(), false, usuario));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public List<AvaliacaoInstitucionalPessoaAvaliadaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<AvaliacaoInstitucionalPessoaAvaliadaVO> vetResultado = new ArrayList<AvaliacaoInstitucionalPessoaAvaliadaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public AvaliacaoInstitucionalPessoaAvaliadaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AvaliacaoInstitucionalPessoaAvaliadaVO obj = new AvaliacaoInstitucionalPessoaAvaliadaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getAvaliacaoInstitucionalVO().setCodigo(dadosSQL.getInt("avaliacaoInstitucional"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		
		return obj;
	}

}
