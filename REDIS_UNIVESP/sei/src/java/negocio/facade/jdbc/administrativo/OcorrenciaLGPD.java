package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.OcorrenciaLGPDVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.OcorrenciaLGPDInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class OcorrenciaLGPD extends ControleAcesso implements OcorrenciaLGPDInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final OcorrenciaLGPDVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "INSERT INTO OcorrenciaLGPD(dataCadastro,pessoa,tipoOcorrencia,created,updated,codigoCreated,codigoUpdated,nomeCreated,nomeUpdated) VALUES (?,?,?,?,?,?,?,?,?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int x = 1;

					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(x++, usuarioVO.getPessoa().getCodigo());
					sqlInserir.setString(x++, obj.getTipoOcorrenciaEnum().name());
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getCreated()));
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getUpdated()));
					sqlInserir.setInt(x++,obj.getCodigoCreated());
					sqlInserir.setInt(x++, obj.getCodigoUpdated());
					sqlInserir.setString(x++, obj.getNomeCreated());
					sqlInserir.setString(x++,obj.getNomeUpdated());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));

		} catch (Exception e) {
			obj.setNovoObj(Boolean.FALSE);
			throw e;
		}
	}
}
