package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ReferenciaBibliograficaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ReferenciaBibliograficaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ReferenciaBibliograficaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ReferenciaBibliograficaVO
 * @see ControleAcesso
 * @see Disciplina
 */
@Repository
@Scope("singleton")
@Lazy
public class ReferenciaBibliografica extends ControleAcesso implements ReferenciaBibliograficaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1587997282640702112L;
	protected static String idEntidade;

	public ReferenciaBibliografica() throws Exception {
		super();
		setIdEntidade("Disciplina");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * novo()
	 */
	public ReferenciaBibliograficaVO novo() throws Exception {
		ReferenciaBibliografica.incluir(getIdEntidade());
		ReferenciaBibliograficaVO obj = new ReferenciaBibliograficaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * incluir(negocio.comuns.academico. ReferenciaBibliograficaVO)
	 */
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ReferenciaBibliograficaVO obj, final UsuarioVO usuario) throws Exception {
		try {
			ReferenciaBibliograficaVO.validarDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO ReferenciaBibliografica ");
			sql.append(" (tipoReferencia, disciplina,  publicacaoExistenteBiblioteca, catalogo, planoEnsino, ");
			sql.append(" titulo, anoPublicacao,  edicao, localPublicacao, ISBN, ");
			sql.append(" autores, tipoPublicacao , subtitulo, justificativa ");
			sql.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getTipoReferencia());
					sqlInserir.setInt(2, obj.getDisciplina().intValue());
					sqlInserir.setBoolean(3, obj.getPublicacaoExistenteBiblioteca());
					if (obj.getCatalogo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getCatalogo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getPlanoEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getPlanoEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getTitulo());
					sqlInserir.setString(7, obj.getAnoPublicacao());
					sqlInserir.setString(8, obj.getEdicao());
					sqlInserir.setString(9, obj.getLocalPublicacao());
					sqlInserir.setString(10, obj.getISBN());
					sqlInserir.setString(11, obj.getAutores());
					sqlInserir.setString(12, obj.getTipoPublicacao());
					sqlInserir.setString(13, obj.getSubtitulo());
					sqlInserir.setString(14, obj.getJustificativa());
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
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * alterar(negocio.comuns.academico. ReferenciaBibliograficaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ReferenciaBibliograficaVO obj, final UsuarioVO usuario) throws Exception {
		try {
			ReferenciaBibliograficaVO.validarDados(obj);
			ReferenciaBibliografica.alterar(getIdEntidade());
			final StringBuilder sql = new StringBuilder("UPDATE ReferenciaBibliografica set ");
			sql.append(" titulo=?, anoPublicacao=?,  edicao=?, ISBN=?, autores = ?, tipoPublicacao=?, localPublicacao = ?, ");
			sql.append(" tipoReferencia=?, disciplina=?,  publicacaoExistenteBiblioteca=?, catalogo=?, planoEnsino = ? , subtitulo=?, justificativa=? ");
			sql.append(" WHERE ((codigo = ?))");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getTitulo());
					sqlAlterar.setString(2, obj.getAnoPublicacao());
					sqlAlterar.setString(3, obj.getEdicao());
					sqlAlterar.setString(4, obj.getISBN());
					sqlAlterar.setString(5, obj.getAutores());
					sqlAlterar.setString(6, obj.getTipoPublicacao());
					sqlAlterar.setString(7, obj.getLocalPublicacao());
					sqlAlterar.setString(8, obj.getTipoReferencia());
					sqlAlterar.setInt(9, obj.getDisciplina().intValue());
					sqlAlterar.setBoolean(10, obj.getPublicacaoExistenteBiblioteca().booleanValue());
					if (obj.getCatalogo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getCatalogo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					if (obj.getPlanoEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getPlanoEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setString(13, obj.getSubtitulo());
					sqlAlterar.setString(14, obj.getJustificativa());
					sqlAlterar.setInt(15, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, usuario);
				return;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * excluir(negocio.comuns.academico. ReferenciaBibliograficaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ReferenciaBibliograficaVO obj, UsuarioVO usuario) throws Exception {
		ReferenciaBibliografica.excluir(getIdEntidade());
		String sql = "DELETE FROM ReferenciaBibliografica WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorTituloExemplar(java.lang.String, boolean)
	 */
	public List<ReferenciaBibliograficaVO> consultarPorTituloExemplar(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ReferenciaBibliografica.* FROM ReferenciaBibliografica, Catalogo, Titulo WHERE ReferenciaBibliografica.obra = Catalogo.codigo and Catalogo.titulo = Tilulo.codigo and lower (Titulo.titulo) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Titulo.titulo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorNomeDisciplina(java.lang.String, boolean)
	 */
	public List<ReferenciaBibliograficaVO> consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ReferenciaBibliografica.* FROM ReferenciaBibliografica, Disciplina WHERE ReferenciaBibliografica.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta + "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorTipoReferencia(java.lang.String, boolean)
	 */
	public List<ReferenciaBibliograficaVO> consultarPorTipoReferencia(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReferenciaBibliografica WHERE tipoReferencia like('" + valorConsulta + "%') ORDER BY tipoReferencia";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorTipoPublicacao(java.lang.String, boolean)
	 */
	public List<ReferenciaBibliograficaVO> consultarPorTipoPublicacao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReferenciaBibliografica WHERE tipoPublicacao like('" + valorConsulta + "%') ORDER BY tipoPublicacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorCodigo(java.lang.Integer, boolean)
	 */
	public List<ReferenciaBibliograficaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReferenciaBibliografica WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ReferenciaBibliograficaVO</code> resultantes da consulta.
	 */
	public  List<ReferenciaBibliograficaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<ReferenciaBibliograficaVO> vetResultado = new ArrayList<ReferenciaBibliograficaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ReferenciaBibliograficaVO</code>.
	 * 
	 * @return O objeto da classe <code>ReferenciaBibliograficaVO</code> com os
	 *         dados devidamente montados.
	 */
	public  ReferenciaBibliograficaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ReferenciaBibliograficaVO obj = new ReferenciaBibliograficaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setPublicacaoExistenteBiblioteca(new Boolean(dadosSQL.getBoolean("publicacaoExistenteBiblioteca")));
		if (!obj.getPublicacaoExistenteBiblioteca()) {
			obj.setTitulo(dadosSQL.getString("titulo"));
			obj.setSubtitulo(dadosSQL.getString("subtitulo"));
			obj.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
			obj.setEdicao(dadosSQL.getString("edicao"));
			obj.setLocalPublicacao(dadosSQL.getString("localPublicacao"));
			obj.setISBN(dadosSQL.getString("ISBN"));
			obj.setAutores(dadosSQL.getString("autores"));
			obj.setTipoPublicacao(dadosSQL.getString("tipoPublicacao"));		
		}else{
			obj.getCatalogo().setCodigo(new Integer(dadosSQL.getInt("catalogo")));
			montarDadosCatalogo(obj, usuario);
		}		
		obj.setDisciplina(new Integer(dadosSQL.getInt("disciplina")));		
		obj.getPlanoEnsino().setCodigo(new Integer(dadosSQL.getInt("planoEnsino")));
		obj.setTipoReferencia(dadosSQL.getString("tipoReferencia"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.setNovoObj(Boolean.FALSE);
	
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ExemplarVO</code> relacionado ao objeto
	 * <code>ReferenciaBibliograficaVO</code>. Faz uso da chave primária da
	 * classe <code>ExemplarVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosCatalogo(ReferenciaBibliograficaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCatalogo().getCodigo().intValue() == 0) {
			obj.setCatalogo(new CatalogoVO());
			return;
		}
//		obj.setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirReferenciaBibliograficas(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuario) throws Exception {
		for (ReferenciaBibliograficaVO obj : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			obj.setPlanoEnsino(planoEnsinoVO);
			obj.setDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
			incluir(obj, usuario);
		}
	}

	/**
	 * Método responsável por excluir apenas Referencia Bibliografica que não estejam da lista do Plano Ensino
	 * @param planoEnsinoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerReferenciaBibliograficaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ReferenciaBibliografica where planoEnsino =  ").append(planoEnsinoVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (ReferenciaBibliograficaVO obj : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}
	
	/**
	 * Método responsável por excluir apenas Referencia Bibliografica que estejam da lista do Plano Ensino
	 * @param planoEnsinoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirReferenciaBibliograficaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ReferenciaBibliografica where planoEnsino =  ").append(planoEnsinoVO.getCodigo());
		sql.append(" and codigo in (0");
		for (ReferenciaBibliograficaVO obj : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarReferenciaBibliograficas(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuario) throws Exception {
		removerReferenciaBibliograficaVOs(planoEnsinoVO, usuario);
		for (ReferenciaBibliograficaVO obj : planoEnsinoVO.getReferenciaBibliograficaVOs()) {
			obj.setPlanoEnsino(planoEnsinoVO);
			obj.setDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
			if (obj.isNovoObj()) {
				incluir(obj, usuario);
			} else {
				alterar(obj, usuario);
			}
		}
	}	

	@Override
	public List<ReferenciaBibliograficaVO> consultarReferenciaBibliograficaPorPlanoEnsino(Integer planoEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ReferenciaBibliografica WHERE planoEnsino = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { planoEnsino });
		return montarDadosConsulta(resultado, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer)
	 */
	public ReferenciaBibliograficaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ReferenciaBibliografica WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ReferenciaBibliografica.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.ReferenciaBibliograficaInterfaceFacade#
	 * setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		ReferenciaBibliografica.idEntidade = idEntidade;
	}
}