/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TaxaValorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TaxaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Taxa extends ControleAcesso implements TaxaInterfaceFacade {

	protected static String idEntidade;

	public Taxa() throws Exception {
		super();
		setIdEntidade("TaxaRequerimento");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TaxaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			Taxa.incluir(getIdEntidade(), true, usuario);
			validarDados(obj);
			final String sql = "INSERT INTO taxa(descricao, situacao) VALUES (?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlInserir = conn.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setString(2, obj.getSituacao());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet res) throws SQLException, DataAccessException {
					if (res.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return res.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getTaxaValorFacade().incluirTaxaValor(obj, usuario);

		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TaxaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {

		try {

			Taxa.alterar(getIdEntidade(), true, usuario);
			validarDados(obj);
			final String sql = "UPDATE taxa SET descricao=?, situacao=? WHERE ((codigo = ? ))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getSituacao());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getTaxaValorFacade().alterarOpcoesTaxaValor(obj, usuario);

		} catch (Exception e) {

			throw e;
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void ativarTaxa(final Integer codTaxa, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			Taxa.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE taxa set situacao = 'AT' where ((codigo = ? ))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setInt(1, codTaxa.intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {

			throw e;
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inativarTaxa(final Integer codTaxa, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			Taxa.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE taxa set situacao = 'IN' where ((codigo = ? ))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setInt(1, codTaxa.intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {

			throw e;
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTaxa(TaxaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			for (TaxaValorVO taxaValorVO : obj.getTaxaValorVOs()) {
				getFacadeFactory().getTaxaValorFacade().excluirTaxaValor(taxaValorVO, usuarioVO);
			}
			TaxaValor.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM taxa WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw new Exception("Este registro é referenciado por outro cadastro, por isto não pode ser excluído e/ou modificado.");
		} finally {
		}
	}
	
	@Override
	public TaxaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM taxa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Taxa ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public static List<TaxaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TaxaVO> vetResultado = new ArrayList<TaxaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}	
	
	@SuppressWarnings("unchecked")
	public static TaxaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TaxaVO obj = new TaxaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		/*if(obj.getSituacao().equals("AT")){
			obj.setSituacao("ATIVA");
		}else{
			obj.setSituacao("INATIVA");
		}*/
		obj.setTaxaValorVOs(getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(obj.getCodigo(), nivelMontarDados));
		return obj;
	}
	
	@Override
	public List<TaxaVO> consultarDescricaoTaxa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from taxa where upper(sem_acentos(descricao) ) like(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) order by descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}	
	
	@Override
	public List<TaxaVO> consultarTaxaPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from taxa where situacao = '" + valorConsulta.toUpperCase() + "' order by descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}	
	
	@Override
	public Double consultarValorTaxaAtual(Integer taxa) throws Exception {
		
		String sqlStr = "select taxavalor.valor from taxavalor where taxa = ? and taxavalor.data <= current_date  order by taxavalor.data desc limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, taxa);
		if(tabelaResultado.next()){
			return tabelaResultado.getDouble("valor");
		}
		return 0.0;		
	}
	
	@Override
	public void adicionarObjTaxaValorVOs(TaxaVO taxaVO, TaxaValorVO taxaValorVO) throws Exception {
		int index = 0;
		Iterator i = taxaVO.getTaxaValorVOs().iterator();
		while (i.hasNext()) {
			TaxaValorVO objExistente = (TaxaValorVO) i.next();
			if (objExistente.getData().equals(taxaValorVO.getData())) {
				taxaVO.getTaxaValorVOs().set(index, taxaValorVO);
				return;
			}
			index++;
		}
		taxaVO.getTaxaValorVOs().add(taxaValorVO);
	}	


	public void validarDados(TaxaVO taxaVO) throws Exception {
		if (taxaVO.getDescricao().equals("")) {
			throw new ConsistirException("O Campo descrição (Taxa é necessário) !");
		}

		if (taxaVO.getTaxaValorVOs().isEmpty()) {
			throw new ConsistirException("Inisira pelo menos uma opção de valor !");
		}

	}

	public void setIdEntidade(String idEntidade) {
		Taxa.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return Taxa.idEntidade;
	}
}
