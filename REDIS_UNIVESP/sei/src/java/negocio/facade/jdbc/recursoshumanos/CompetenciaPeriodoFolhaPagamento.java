package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.CompetenciaPeriodoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>CompetenciaFolhaPagamento</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>CompetenciaPeriodoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class CompetenciaPeriodoFolhaPagamento extends SuperFacade<CompetenciaPeriodoFolhaPagamentoVO> implements CompetenciaPeriodoFolhaPagamentoInterfaceFacade<CompetenciaPeriodoFolhaPagamentoVO> {

	private static final long serialVersionUID = 6689165246933864617L;
	
	protected static String idEntidade;

	public CompetenciaPeriodoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("CompetenciaPeriodoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(CompetenciaPeriodoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		excluirTodosQueNaoEstaoNaListaDeCompetencia(obj, false, usuarioVO);
		
		for(CompetenciaPeriodoFolhaPagamentoVO periodo : obj.getPeriodos()) {
			persistir(periodo, false, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(CompetenciaPeriodoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			CompetenciaPeriodoFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" INSERT INTO CompetenciaPeriodoFolhaPagamento(competenciafolhapagamento, descricao, periodo) ")
					.append(" VALUES (?, ?, ?) returning codigo ")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCompetenciaFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(CompetenciaPeriodoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			CompetenciaPeriodoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				final StringBuilder sql = new StringBuilder("UPDATE CompetenciaPeriodoFolhaPagamento set competenciafolhapagamento=?, descricao=?, periodo=? WHERE codigo = ? ")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getCompetenciaFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj, ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(CompetenciaPeriodoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			CompetenciaPeriodoFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder("DELETE FROM CompetenciaPeriodoFolhaPagamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void excluirTodosQueNaoEstaoNaListaDeCompetencia(CompetenciaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		CompetenciaPeriodoFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<CompetenciaPeriodoFolhaPagamentoVO> i = obj.getPeriodos().iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM CompetenciaPeriodoFolhaPagamento WHERE CompetenciaPeriodoFolhaPagamento.competenciafolhapagamento = ? ");
	    while (i.hasNext()) {
	    	CompetenciaPeriodoFolhaPagamentoVO objeto = (CompetenciaPeriodoFolhaPagamentoVO) i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}

	@Override
	public void validarDados(CompetenciaPeriodoFolhaPagamentoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getPeriodo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_dataCompetencia"));
		}
	}

	public List<CompetenciaPeriodoFolhaPagamentoVO> consultarPorCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) throws Exception {
		
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM CompetenciaPeriodoFolhaPagamento ");
        sql.append(" WHERE CompetenciaFolhaPagamento = ? ");
        sql.append(" ORDER BY periodo asc ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  competenciaFolhaPagamentoVO.getCodigo());

        List<CompetenciaPeriodoFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CompetenciaPeriodoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		String sql = " SELECT * FROM CompetenciaPeriodoFolhaPagamento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception("Dados não encontrados (Competência Período Folha Pagamento).");
	}

	@Override
	public CompetenciaPeriodoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		CompetenciaPeriodoFolhaPagamentoVO obj = new CompetenciaPeriodoFolhaPagamentoVO();
		
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setPeriodo(tabelaResultado.getInt("periodo"));
		obj.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("competenciafolhapagamento")));

		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CompetenciaPeriodoFolhaPagamento.idEntidade = idEntidade;
	}

}