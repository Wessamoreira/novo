package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FaixaSalarialVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialItemVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.ProgressaoSalarialItemInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class ProgressaoSalarialItem extends SuperFacade<ProgressaoSalarialItemVO> implements ProgressaoSalarialItemInterfaceFacade<ProgressaoSalarialItemVO> {

	private static final long serialVersionUID = 1880558427786266311L;

	@Override
	public void persistir(ProgressaoSalarialItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTodos(ProgressaoSalarialVO progressaoSalarialVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		excluirTodosQueNaoEstaoNaLista(progressaoSalarialVO, progressaoSalarialVO.getProgressaoSalarialItens(), validarAcesso, usuarioVO);
		
		for(ProgressaoSalarialItemVO progressaoItem : progressaoSalarialVO.getProgressaoSalarialItens()) {
			persistir(progressaoItem, validarAcesso, usuarioVO);
		}
	}
	
	@Override
	public void incluir(ProgressaoSalarialItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarialItem.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder(" INSERT INTO progressaoSalarialitem ( progressaosalarial, nivelsalarial, faixasalarial, valor )")
				        .append(" VALUES ( ?, ?, ?, ? )")
				        .append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getProgressaoSalarialVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getNivelSalarialVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getFaixaSalarialVO(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
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
	}

	@Override
	public void alterar(ProgressaoSalarialItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarialItem.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
	
				StringBuilder sql = new StringBuilder(" UPDATE progressaoSalarialitem set nivelsalarial=?, faixasalarial=?, valor=? ")
				        .append(" where codigo = ? and progressaosalarial = ? ");
	
				final PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getNivelSalarialVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFaixaSalarialVO(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getProgressaoSalarialVO(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});		
	}

	@Override
	public void excluir(ProgressaoSalarialItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarialItem.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM progressaoSalarialitem WHERE ((progressaosalarial = ? and codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getProgressaoSalarialVO().getCodigo(), obj.getCodigo() });
	}
	


	/**
	 * Exclui todos os registros que estao no banco e que nao estao mais dentro da lista de objetos oriundos do Form
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodosQueNaoEstaoNaLista(ProgressaoSalarialVO progressaoSalarialVO, List<ProgressaoSalarialItemVO> objetos, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarialItem.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(progressaoSalarialVO.getCodigo());
		
		Iterator i = objetos.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM progressaoSalarialitem WHERE progressaoSalarialitem.progressaosalarial = ? ");
	    while (i.hasNext()) {
	    	ProgressaoSalarialItemVO objeto = (ProgressaoSalarialItemVO)i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}
	
	/**
	 * Exclui todos os registros da Progressao Salarial
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodos(ProgressaoSalarialVO progressaoSalarialVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		StringBuilder str = new StringBuilder("DELETE FROM progressaoSalarialitem WHERE progressaoSalarialitem.progressaosalarial = ? ");
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), progressaoSalarialVO.getCodigo());
	}

	@Override
	public ProgressaoSalarialItemVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM progressaoSalarialitem WHERE progressaoSalarialitem.codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(ProgressaoSalarialItemVO obj) throws ConsistirException {
		if(obj.getProgressaoSalarialVO().getCodigo() <= 0 || obj.getNivelSalarialVO().getCodigo() <= 0 || obj.getFaixaSalarialVO().getCodigo() <= 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProgressaoSalarialItem_CamposObrigatoriosNaoPreenchidos"));
		}
	}

	@Override
	public ProgressaoSalarialItemVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		ProgressaoSalarialItemVO obj = new ProgressaoSalarialItemVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setProgressaoSalarialVO(Uteis.montarDadosVO(tabelaResultado.getInt("progressaosalarial"), ProgressaoSalarialVO.class, p -> getFacadeFactory().getProgressaoSalarialInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		obj.setNivelSalarialVO(Uteis.montarDadosVO(tabelaResultado.getInt("nivelsalarial"), NivelSalarialVO.class, p -> getFacadeFactory().getNivelSalarialInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		obj.setFaixaSalarialVO(Uteis.montarDadosVO(tabelaResultado.getInt("faixasalarial"), FaixaSalarialVO.class, p -> getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		obj.setValor(tabelaResultado.getBigDecimal("valor"));

		return obj;
	}
	
	public FaixaSalarialVO montarDadosFaixaSalarial(SqlRowSet tabelaResultado) throws Exception {
		FaixaSalarialVO obj = new FaixaSalarialVO(); 
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setValor(tabelaResultado.getInt("valor"));
		return obj;
	}
	
	public NivelSalarialVO montarDadosNivelSalarial(SqlRowSet tabelaResultado) throws Exception {
		NivelSalarialVO obj = new NivelSalarialVO(); 
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setValor(tabelaResultado.getInt("valor"));
		return obj;
	}
	
	
	public List<ProgressaoSalarialItemVO> consultarProgressaoTabelaItem(Long id) throws Exception {
		StringBuilder sql = new StringBuilder(" SELECT progressaoSalarialitem.codigo, progressaosalarial, nivelsalarial, faixasalarial, progressaoSalarialitem.valor FROM progressaoSalarialitem ");
		sql.append(" inner join progressaoSalarial on progressaoSalarial.codigo = progressaoSalarialitem.progressaoSalarial ");
		sql.append(" inner join nivelsalarial on nivelsalarial.codigo = progressaoSalarialitem.nivelsalarial ");
		sql.append(" inner join faixasalarial on faixasalarial.codigo = progressaoSalarialitem.faixasalarial ");
		sql.append(" WHERE progressaosalarial.codigo = ? ");
		sql.append(" order by nivelsalarial.valor, faixasalarial.valor ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);

		return (montarDadosLista(tabelaResultado));
	}

	
	private List<ProgressaoSalarialItemVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		
		List<ProgressaoSalarialItemVO> listaProgressaoSalarial = new ArrayList<>();
        
        while(tabelaResultado.next()) {
        	listaProgressaoSalarial.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaProgressaoSalarial;
	}
	
	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from progressaoSalarialitem "); 
		return sql.toString();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalDeRegistros(String valorConsulta, DataModelo dataModelo) {

		try {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(getSQLBasico(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}
	
	public List<ProgressaoSalarialItemVO> consultarPorProgressaoSalarial(Integer codigoProgressaoSalarial) throws Exception {
		String sql = " SELECT DISTINCT nivel.codigo, nivel.descricao, nivel.valor FROM progressaosalarialitem as item\r\n" + 
					 " INNER JOIN nivelsalarial as nivel on nivel.codigo = item.nivelsalarial\r\n" + 
					 " WHERE item.progressaosalarial = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoProgressaoSalarial);
		List<ProgressaoSalarialItemVO> lista = new ArrayList<>();
		while(rs.next()) {
			NivelSalarialVO nivel = montarDadosNivelSalarial(rs);
			ProgressaoSalarialItemVO obj = new ProgressaoSalarialItemVO();
			obj.setNivelSalarialVO(nivel);
			lista.add(obj);
		}
		return lista;
 	}
	
	public List<ProgressaoSalarialItemVO> consultarPorProgressaoSalarialPorNivel(Integer codigoNivelSalarial) throws Exception {
		String sql = "   SELECT DISTINCT ON (codigo) faixa.codigo, faixa.descricao, faixa.valor, item.valor as \"item.valor\"  FROM progressaosalarialitem as item \r\n" + 
				" INNER JOIN faixasalarial as faixa on faixa.codigo = item.faixasalarial\r\n" + 
				" WHERE item.nivelsalarial =  ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoNivelSalarial);
		List<ProgressaoSalarialItemVO> lista = new ArrayList<>();
		while(rs.next()) {
			FaixaSalarialVO faixa = montarDadosFaixaSalarial(rs);
			ProgressaoSalarialItemVO obj = new ProgressaoSalarialItemVO();
			obj.setValor(rs.getBigDecimal("item.valor"));
			obj.setFaixaSalarialVO(faixa);
			lista.add(obj);
		}
		return lista;
 	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public BigDecimal consultarSalarioPorNivelFaixaProgressao(Integer nivelSalarial, Integer faixaSalarial, Integer progressaoSalarial) {
		String sql = "select valor from progressaosalarialitem where nivelsalarial = ? and faixasalarial = ? and progressaosalarial = ?";
		try {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, nivelSalarial, faixaSalarial, progressaoSalarial);
			return (BigDecimal) Uteis.getSqlRowSetTotalizador(rs, "valor", TipoCampoEnum.BIG_DECIMAL);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

}