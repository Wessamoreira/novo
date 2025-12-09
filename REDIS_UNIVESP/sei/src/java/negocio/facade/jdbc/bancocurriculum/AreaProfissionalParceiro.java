/**
 * 
 */
package negocio.facade.jdbc.bancocurriculum;

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
import negocio.comuns.bancocurriculum.AreaProfissionalParceiroVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.AreaProfissionalParceiroInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class AreaProfissionalParceiro extends ControleAcesso implements AreaProfissionalParceiroInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public AreaProfissionalParceiro() {
		super();
	}

	public void validarDados(AreaProfissionalParceiroVO obj) throws Exception {

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AreaProfissionalParceiroVO obj) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO AreaProfissionalParceiro( parceiro, areaProfissional ) VALUES ( ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);

				if (obj.getParceiroVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getParceiroVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getAreaProfissionalVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getAreaProfissionalVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AreaProfissionalParceiroVO obj) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE AreaProfissionalParceiro set parceiro=?, areaProfissional=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);

				if (obj.getParceiroVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getParceiroVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getAreaProfissionalVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getAreaProfissionalVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}

				sqlAlterar.setInt(18, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAreaProfissionalParceiroVOs(Integer parceiroPrm, List<AreaProfissionalParceiroVO> objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AreaProfissionalParceiroVO obj = (AreaProfissionalParceiroVO) e.next();
			obj.getParceiroVO().setCodigo(parceiroPrm);
			incluir(obj);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAreaProfissionalParceiro(Integer parceiro, List<AreaProfissionalParceiroVO> objetos) throws Exception {
		excluirPorParceiro(parceiro);
		incluirAreaProfissionalParceiroVOs(parceiro, objetos);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorParceiro(Integer parceiro) throws Exception {
		String sql = "DELETE FROM AreaProfissionalParceiro WHERE (parceiro = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { parceiro });
	}
	
	@Override
	public List<AreaProfissionalParceiroVO> consultarPorParceiro(Integer parceiro, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select areaprofissionalparceiro.codigo, parceiro.codigo AS \"parceiro.codigo\", parceiro.nome AS \"parceiro.nome\", ");
		sb.append(" areaprofissional.codigo AS \"areaprofissional.codigo\", areaprofissional.descricaoAreaProfissional AS \"areaprofissional.descricaoAreaProfissional\"  ");
		sb.append(" from areaprofissionalparceiro ");
		sb.append(" inner join parceiro on parceiro.codigo = areaprofissionalparceiro.parceiro ");
		sb.append(" inner join areaprofissional on areaProfissional.codigo = areaprofissionalparceiro.areaprofissional ");
		sb.append(" where parceiro.codigo = ").append(parceiro);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<AreaProfissionalParceiroVO> listaAreaProfissionalParceiroVOs = new ArrayList<AreaProfissionalParceiroVO>(0);
		while (tabelaResultado.next()) {
			AreaProfissionalParceiroVO obj = new AreaProfissionalParceiroVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getParceiroVO().setCodigo(tabelaResultado.getInt("parceiro.codigo"));
			obj.getParceiroVO().setNome(tabelaResultado.getString("parceiro.nome"));
			obj.getAreaProfissionalVO().setCodigo(tabelaResultado.getInt("areaprofissional.codigo"));
			obj.getAreaProfissionalVO().setDescricaoAreaProfissional(tabelaResultado.getString("areaprofissional.descricaoAreaProfissional"));
			listaAreaProfissionalParceiroVOs.add(obj);
		}
		return listaAreaProfissionalParceiroVOs;
	}
	
	@Override
	public void adicionarAreaProfissional(List<AreaProfissionalParceiroVO> listaAreaProfissionalParceiroVOs, AreaProfissionalParceiroVO areaProfissionalParceiroVO) {
		int index = 0;
		for (AreaProfissionalParceiroVO areaProfissionalVO : listaAreaProfissionalParceiroVOs) {
			if (areaProfissionalVO.getAreaProfissionalVO().getCodigo().equals(areaProfissionalParceiroVO.getAreaProfissionalVO().getCodigo())) {
				listaAreaProfissionalParceiroVOs.set(index, areaProfissionalParceiroVO);
				return;
			}
			index++;
		}
		listaAreaProfissionalParceiroVOs.add(areaProfissionalParceiroVO);
	}
	
	@Override
	public void removerAreaProfissionalParceiro(List<AreaProfissionalParceiroVO> listaAreaProfissionalParceiroVOs, AreaProfissionalParceiroVO areaProfissionalParceiroVO) {
		int index = 0;
		for (AreaProfissionalParceiroVO areaProfissionalVO : listaAreaProfissionalParceiroVOs) {
			if (areaProfissionalVO.getAreaProfissionalVO().getCodigo().equals(areaProfissionalParceiroVO.getAreaProfissionalVO().getCodigo())) {
				listaAreaProfissionalParceiroVOs.remove(index);
				return;
			}
			index++;
		}
	}
}
