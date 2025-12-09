/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.LogContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.LogContaCorrenteInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class LogContaCorrente extends ControleAcesso implements LogContaCorrenteInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public LogContaCorrente() throws Exception {
        super();
        setIdEntidade("LogContaCorrente");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogContaCorrenteVO obj, final UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO LogContaCorrente(responsavel, dataAlteracao, acao, dataAbertura, numero, digito, agencia, contacaixa, carteira, contacorrente, codigocedente, convenio) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) returning codigo";
            
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, usuario.getCodigo());
                    sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
                    sqlInserir.setString(3, obj.getAcao());
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataAbertura()));
                    sqlInserir.setString(5, obj.getNumero());
                    sqlInserir.setString(6, obj.getDigito());
                    sqlInserir.setString(7, obj.getAgencia());
                    sqlInserir.setBoolean(8, obj.getContaCaixa());
                    sqlInserir.setString(9, obj.getCarteira());
                    sqlInserir.setInt(10, obj.getContaCorrente());
                    sqlInserir.setString(11, obj.getCodigoCedente());
                    sqlInserir.setString(12, obj.getConvenio());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            throw e;
        }
    }
}
