package negocio.facade.jdbc.biblioteca;

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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaNivelEducacionalVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ConfiguracaoBibliotecaNivelEducacionalInterface;

/**
 * 
 * @author Leonardo Riciolle Criado: 03/12/2014
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoBibliotecaNivelEducacional extends ControleAcesso implements ConfiguracaoBibliotecaNivelEducacionalInterface {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoBibliotecaNivelEducacional() {
		super();
		setIdEntidade("ConfiguracaoBibliotecaNivelEducacional");
	}
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoBibliotecaNivelEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO configuracaobibliotecaniveleducacional (biblioteca, configuracaobiblioteca, niveleducacional, unidadeensino) ");
			sql.append(" VALUES (?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					if (!obj.getBiblioteca().equals(0)) {
						sqlInserir.setInt(1, obj.getBiblioteca());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (!obj.getConfiguracaoBibliotecaVO().getCodigo().equals(0)) {
						sqlInserir.setInt(2, obj.getConfiguracaoBibliotecaVO().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (!obj.getNivelEducacional().equals("0")) {
						sqlInserir.setString(3, obj.getNivelEducacional());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (!obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(4, obj.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
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

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoBibliotecaNivelEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE configuracaobibliotecaniveleducacional SET biblioteca=?, configuracaobiblioteca=?, niveleducacional=?, unidadeensino=? ");
			sql.append(" WHERE (codigo = ?) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					if (!obj.getBiblioteca().equals(0)) {
                        sqlAlterar.setInt(1, obj.getBiblioteca());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
					if (!obj.getConfiguracaoBibliotecaVO().getCodigo().equals(0)) {
                        sqlAlterar.setInt(2, obj.getConfiguracaoBibliotecaVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
					if (!obj.getNivelEducacional().equals("0")) {
						sqlAlterar.setString(3, obj.getNivelEducacional());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (!obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
                        sqlAlterar.setInt(4, obj.getUnidadeEnsinoVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<ConfiguracaoBibliotecaNivelEducacionalVO> consultarPorBiblioteca(Integer biblioteca, int nivelMontarDados,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT * FROM configuracaobibliotecaniveleducacional WHERE biblioteca = ").append(biblioteca);
		sqlStr.append(" ORDER BY codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}
	
	public List<ConfiguracaoBibliotecaNivelEducacionalVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs = new ArrayList<ConfiguracaoBibliotecaNivelEducacionalVO>(0);
		while (tabelaResultado.next()) {
			configuracaoBibliotecaNivelEducacionalVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return configuracaoBibliotecaNivelEducacionalVOs;
	}
	
	public ConfiguracaoBibliotecaNivelEducacionalVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoBibliotecaNivelEducacionalVO obj = new ConfiguracaoBibliotecaNivelEducacionalVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setBiblioteca(tabelaResultado.getInt("biblioteca"));
		obj.getConfiguracaoBibliotecaVO().setCodigo(tabelaResultado.getInt("configuracaobiblioteca"));
		obj.setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(obj.getConfiguracaoBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		obj.setNivelEducacional(tabelaResultado.getString("niveleducacional"));
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
    public void incluirConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuario) throws Exception {
		try {
			Iterator<ConfiguracaoBibliotecaNivelEducacionalVO> iterator = configuracaoBibliotecaNivelEducacionalVOs.iterator();
	        while (iterator.hasNext()) {
	        	ConfiguracaoBibliotecaNivelEducacionalVO obj = (ConfiguracaoBibliotecaNivelEducacionalVO) iterator.next();
	        	obj.setBiblioteca(bibliotecaVO.getCodigo());
	        	incluir(obj, false, usuario);
	        }
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.SUPPORTS)
	public void alterarConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" DELETE FROM configuracaobibliotecaniveleducacional WHERE biblioteca = " + bibliotecaVO.getCodigo());
			sqlStr.append(" AND codigo not in (0");
			Iterator<ConfiguracaoBibliotecaNivelEducacionalVO> e = configuracaoBibliotecaNivelEducacionalVOs.iterator();
	        while (e.hasNext()) {
	        	ConfiguracaoBibliotecaNivelEducacionalVO obj = (ConfiguracaoBibliotecaNivelEducacionalVO) e.next();
	        	sqlStr.append(", " + obj.getCodigo());
	        }
	        sqlStr.append(") ");
	        getConexao().getJdbcTemplate().update(sqlStr.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));  
	        
	        Iterator<ConfiguracaoBibliotecaNivelEducacionalVO> i = configuracaoBibliotecaNivelEducacionalVOs.iterator();
	        while (i.hasNext()) {
	        	ConfiguracaoBibliotecaNivelEducacionalVO obj = (ConfiguracaoBibliotecaNivelEducacionalVO) i.next();
	        	if (obj.getCodigo().equals(0)) {
	        		obj.setBiblioteca(bibliotecaVO.getCodigo());
	        		incluir(obj, false, usuario);
	        	} else {
	        		obj.setBiblioteca(bibliotecaVO.getCodigo());
	        		alterar(obj, false, usuario);
	        	}
	        }
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void excluirConfiguracaoBibliotecaNivelEducacionalVOs(BibliotecaVO bibliotecaVO, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" DELETE FROM configuracaobibliotecaniveleducacional WHERE biblioteca = " + bibliotecaVO.getCodigo());
			getConexao().getJdbcTemplate().update(sqlStr.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void executarAdicionarConfiguracaoBibliotecaNivelEducacional(ConfiguracaoBibliotecaNivelEducacionalVO obj, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			obj.setConfiguracaoBibliotecaVO(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(obj.getConfiguracaoBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			Iterator<ConfiguracaoBibliotecaNivelEducacionalVO> iterator = configuracaoBibliotecaNivelEducacionalVOs.iterator();
	        while (iterator.hasNext()) {
	        	ConfiguracaoBibliotecaNivelEducacionalVO objExistente = (ConfiguracaoBibliotecaNivelEducacionalVO) iterator.next();
	        	if ((objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) && (objExistente.getConfiguracaoBibliotecaVO().getCodigo().equals(obj.getConfiguracaoBibliotecaVO().getCodigo())) && (objExistente.getNivelEducacional().equals(obj.getNivelEducacional()))) {
	        		throw new Exception("Já existe uma Configuração Biblioteca para Unidade de Ensino e Nível Educacional selecionado.");
				}
	        }
	        configuracaoBibliotecaNivelEducacionalVOs.add(obj);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void executarRemoverConfiguracaoBibliotecaNivelEducacional(ConfiguracaoBibliotecaNivelEducacionalVO obj, List<ConfiguracaoBibliotecaNivelEducacionalVO> configuracaoBibliotecaNivelEducacionalVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (Iterator<ConfiguracaoBibliotecaNivelEducacionalVO> iterator = configuracaoBibliotecaNivelEducacionalVOs.iterator(); iterator.hasNext();) {
				ConfiguracaoBibliotecaNivelEducacionalVO objExistente = (ConfiguracaoBibliotecaNivelEducacionalVO) iterator.next();
				if (objExistente.equals(obj)) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoBibliotecaNivelEducacional.idEntidade = idEntidade;
	}

}
