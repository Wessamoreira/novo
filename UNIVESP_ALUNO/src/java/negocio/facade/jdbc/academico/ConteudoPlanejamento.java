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

import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConteudoPlanejamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ConteudoPlanejamentoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ConteudoPlanejamentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ConteudoPlanejamentoVO
 * @see ControleAcesso
 * @see Disciplina
 */
@Repository
@Scope("singleton")
@Lazy 
public class ConteudoPlanejamento extends ControleAcesso implements ConteudoPlanejamentoInterfaceFacade {

	private static final long serialVersionUID = 3031625824589654381L;

	protected static String idEntidade;

	public ConteudoPlanejamento() throws Exception {
		super();
		setIdEntidade("Disciplina");
	}

	public ConteudoPlanejamentoVO novo() throws Exception {
		ConteudoPlanejamento.incluir(getIdEntidade());
		ConteudoPlanejamentoVO obj = new ConteudoPlanejamentoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConteudoPlanejamentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConteudoPlanejamentoVO.validarDados(obj);
			final String sql = "INSERT INTO ConteudoPlanejamento( conteudo, habilidade, atitude, metodologia, cargahoraria, classificacao, disciplina, planoEnsino, ordem, praticaSupervisionada ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getConteudo());
					sqlInserir.setString(2, obj.getHabilidade());
					sqlInserir.setString(3, obj.getAtitude());
					sqlInserir.setString(4, obj.getMetodologia());
					sqlInserir.setBigDecimal(5, obj.getCargahoraria());
					sqlInserir.setString(6, obj.getClassificacao());
					if (obj.getDisciplina().intValue() != 0) {
						sqlInserir.setInt(7, obj.getDisciplina().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getPlanoEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getPlanoEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setInt(9, obj.getOrdem());
					sqlInserir.setString(10, obj.getPraticaSupervisionada());
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
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConteudoPlanejamentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConteudoPlanejamentoVO.validarDados(obj);
			final String sql = "UPDATE ConteudoPlanejamento set conteudo=?, habilidade=?, atitude=?, metodologia=?, cargahoraria=?, classificacao=?, disciplina=?, planoEnsino=?, ordem=?, praticaSupervisionada=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getConteudo());
					sqlAlterar.setString(2, obj.getHabilidade());
					sqlAlterar.setString(3, obj.getAtitude());
					sqlAlterar.setString(4, obj.getMetodologia());
					sqlAlterar.setBigDecimal(5, obj.getCargahoraria());
					sqlAlterar.setString(6, obj.getClassificacao());
					if (obj.getDisciplina().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getDisciplina().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getPlanoEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getPlanoEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					sqlAlterar.setInt(9, obj.getOrdem());
					sqlAlterar.setString(10, obj.getPraticaSupervisionada());
					sqlAlterar.setInt(11, obj.getCodigo().intValue());
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
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		for (ConteudoPlanejamentoVO obj : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			obj.setPlanoEnsino(planoEnsinoVO);
			obj.setDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
			incluir(obj, usuarioVO);
		}
	}

	/**
	 * Método responsável por excluir apenas Conteudo Planejamento que não estejam da lista do Plano Ensino
	 * @param planoEnsinoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ConteudoPlanejamento where planoEnsino =  ").append(planoEnsinoVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (ConteudoPlanejamentoVO obj : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	/**
	 * Método responsável por excluir apenas Conteudo Planejamento que estejam da lista do Plano Ensino
	 * @param planoEnsinoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM ConteudoPlanejamento where planoEnsino =  ").append(planoEnsinoVO.getCodigo());
		sql.append(" and codigo in (0");
		for (ConteudoPlanejamentoVO obj : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConteudoPlanejamentoVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		removerConteudoPlanejamentoVOs(planoEnsinoVO);
		for (ConteudoPlanejamentoVO obj : planoEnsinoVO.getConteudoPlanejamentoVOs()) {
			obj.setPlanoEnsino(planoEnsinoVO);
			obj.setDisciplina(planoEnsinoVO.getDisciplina().getCodigo());
			if (obj.isNovoObj()) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConteudoPlanejamentoVO obj, UsuarioVO usuario) throws Exception {
		ConteudoPlanejamento.excluir(getIdEntidade());
		String sql = "DELETE FROM ConteudoPlanejamento WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}
	
	@Override
	public List<ConteudoPlanejamentoVO> consultarConteudoPlanejamentoPorPlanoEnsino(Integer planoEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);		
		String sql = "SELECT * FROM ConteudoPlanejamento WHERE planoEnsino = ? order by ordem";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { planoEnsino });
		return montarDadosConsulta(resultado, usuario);		
	}

	public List<ConteudoPlanejamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConteudoPlanejamento WHERE codigo >= ? ORDER BY ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.intValue());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ConteudoPlanejamentoVO</code> resultantes da consulta.
	 */
	public static List<ConteudoPlanejamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<ConteudoPlanejamentoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>ConteudoPlanejamentoVO</code>.
	 * 
	 * @return O objeto da classe <code>ConteudoPlanejamentoVO</code> com os dados devidamente montados.
	 */
	public static ConteudoPlanejamentoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ConteudoPlanejamentoVO obj = new ConteudoPlanejamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setConteudo(dadosSQL.getString("conteudo"));
		obj.setHabilidade(dadosSQL.getString("habilidade"));
		obj.setAtitude(dadosSQL.getString("atitude"));
		obj.setMetodologia(dadosSQL.getString("metodologia"));
		obj.setCargahoraria(dadosSQL.getBigDecimal("cargahoraria"));
		obj.setOrdem(new Integer(dadosSQL.getInt("ordem")));
		obj.setClassificacao(dadosSQL.getString("classificacao"));
		obj.setDisciplina(new Integer(dadosSQL.getInt("disciplina")));
		obj.setPraticaSupervisionada(dadosSQL.getString("praticaSupervisionada"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	/**
	 * Consulta o {@link ConteudoPlanejamentoVO } pelo codigo informado.
	 * 
	 */
	public ConteudoPlanejamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConteudoPlanejamento WHERE codigo = ? order by ordem";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConteudoPlanejamento ).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConteudoPlanejamento.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConteudoPlanejamento.idEntidade = idEntidade;
	}
}