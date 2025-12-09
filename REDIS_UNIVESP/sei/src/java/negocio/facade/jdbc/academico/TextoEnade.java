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

import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TextoEnadeInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class TextoEnade extends ControleAcesso implements TextoEnadeInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7479094921271407753L;

	@Override
	public void incluirTextoEnadeVOs(EnadeVO enadeVO) throws Exception {
		for (TextoEnadeVO textoEnadeVO : enadeVO.getTextoEnadeVOs()) {
			textoEnadeVO.setEnade(enadeVO);
			incluir(textoEnadeVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final TextoEnadeVO textoEnadeVO) throws Exception {
		validarDados(textoEnadeVO);
		textoEnadeVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO textoEnade (texto, enade, tipoTextoEnade) values (?,?,?) returning codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, textoEnadeVO.getTexto());
				ps.setInt(2, textoEnadeVO.getEnade().getCodigo());
				ps.setString(3, textoEnadeVO.getTipoTextoEnade().name());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {

					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		textoEnadeVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final TextoEnadeVO textoEnadeVO) throws Exception {
		validarDados(textoEnadeVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE textoEnade set texto = ?, enade = ?, tipoTextoEnade = ? where codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, textoEnadeVO.getTexto());
				ps.setInt(2, textoEnadeVO.getEnade().getCodigo());
				ps.setString(3, textoEnadeVO.getTipoTextoEnade().name());
				ps.setInt(4, textoEnadeVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(textoEnadeVO);
			return;
		}
		textoEnadeVO.setNovoObj(false);
	}

	@Override
	public void alterarTextoEnadeVOs(EnadeVO enadeVO) throws Exception {
		excluirTextoEnadeVOs(enadeVO);
		for (TextoEnadeVO textoEnadeVO : enadeVO.getTextoEnadeVOs()) {
			textoEnadeVO.setEnade(enadeVO);
			if (textoEnadeVO.getNovoObj()) {
				incluir(textoEnadeVO);
			} else {
				alterar(textoEnadeVO);
			}
		}

	}

	@Override
	public void excluirTextoEnadeVOs(EnadeVO enadeVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM TextoEnade where enade = " + enadeVO.getCodigo() + " and codigo not in (0");
		for (TextoEnadeVO textoEnadeVO : enadeVO.getTextoEnadeVOs()) {
			sql.append(", ").append(textoEnadeVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());

	}

	@Override
	public void excluirTextoEnadePorEnade(EnadeVO enadeVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM TextoEnade where enade = " + enadeVO.getCodigo() + " ");
		getConexao().getJdbcTemplate().update(sql.toString());
		
	}
	
	@Override
	public List<TextoEnadeVO> consultarPorEnade(Integer enade) throws Exception {
		StringBuilder sql = new StringBuilder("select * from textoenade where enade = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), enade);
		return montarDadosConsulta(rs);
	}
	
	@Override
	public List<TextoEnadeVO> consultarUltimosTextosCadastrados() throws Exception {
		StringBuilder sql = new StringBuilder("select tipoTextoEnade, texto from textoenade where enade = (select max(codigo) from enade) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TextoEnadeVO> textoEnadeVOs = new ArrayList<TextoEnadeVO>(0);
		while (rs.next()) {
			TextoEnadeVO textoEnadeVO = new TextoEnadeVO();
			textoEnadeVO.setNovoObj(true);
			textoEnadeVO.setTexto(rs.getString("texto"));
			textoEnadeVO.setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(rs.getString("tipoTextoEnade")));
			textoEnadeVOs.add(textoEnadeVO);
		}
		return textoEnadeVOs;
	}

	private List<TextoEnadeVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<TextoEnadeVO> textoEnadeVOs = new ArrayList<TextoEnadeVO>(0);
		while (rs.next()) {
			textoEnadeVOs.add(montarDados(rs));
		}
		return textoEnadeVOs;
	}

	private TextoEnadeVO montarDados(SqlRowSet rs) throws Exception {
		TextoEnadeVO textoEnadeVO = new TextoEnadeVO();
		textoEnadeVO.setNovoObj(false);
		textoEnadeVO.setCodigo(rs.getInt("codigo"));
		textoEnadeVO.getEnade().setCodigo(rs.getInt("enade"));
		textoEnadeVO.setTexto(rs.getString("texto"));
		textoEnadeVO.setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(rs.getString("tipoTextoEnade")));
		return textoEnadeVO;
	}

	@Override
	public TextoEnadeVO consultarPorChavePrimaria(Integer textoEnade) throws Exception {
		StringBuilder sql = new StringBuilder("select * from textoenade where codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), textoEnade);
		if (rs.next()) {
			return montarDados(rs);
		}
		throw new Exception("Dados não encontrados (Texto Enade).");
	}

	@Override
	public void validarDados(TextoEnadeVO textoEnadeVO) throws ConsistirException {
		if(textoEnadeVO.getTexto().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("Deve ser informado um texto para que seja adicionado a lista."));
		}

	}
	
	

}
