package webservice.moodle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import negocio.comuns.academico.ArquivoLogVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ArquivoLogInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class IntegracaoMoodleRSLog extends ControleAcesso {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final String tipoLog, final String servico, final String dado) {
    	try {
	    	Integer codigo = 0;
	    	final String sql = "INSERT INTO IntegracaoMoodleRSLog( data, tipoLog, servico, dado ) VALUES ( ?, ?, ?, ? ) returning codigo";
	        codigo = ((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
	
	            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
	                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
	                sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
	                sqlInserir.setString(2, tipoLog); // tipoLog -> Entrada ou Saida
	                sqlInserir.setString(3, servico); // servico -> nome do serviço acionado
	                sqlInserir.setString(4, dado); // dado -> Json enviado, xml enviado ou retorno enviado.
	                return sqlInserir;
	            }
	        }, new ResultSetExtractor() {
	
	            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
	                if (arg0.next()) {
	                	Boolean novoObj = (Boolean.FALSE);
	                    return arg0.getInt("codigo");
	                }
	                return null;
	            }
	        }));
    	} catch (Exception e) {
    		
		}
    }

}
