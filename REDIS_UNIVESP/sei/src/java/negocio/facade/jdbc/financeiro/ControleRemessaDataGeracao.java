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
import negocio.comuns.financeiro.ControleRemessaDataGeracaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleRemessaDataGeracaoInterfaceFacade;


/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleRemessaDataGeracao extends ControleAcesso implements ControleRemessaDataGeracaoInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public ControleRemessaDataGeracao() throws Exception {
        super();
        setIdEntidade("ControleRemessaDataGeracao");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleRemessaDataGeracaoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ControleRemessaDataGeracao(dataArquivo,nossoNumero, datavencimento, valor, desconto, codMovRemessa) VALUES (?,?,?,?,?,?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataArquivo()));
                    sqlInserir.setString(2, obj.getNossoNumero());                    
					if (obj.getDataVencimento() != null) {
						sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataVencimento()));
					} else {
						sqlInserir.setNull(3, 0);
					}                    
                    sqlInserir.setDouble(4, obj.getValor());
                    sqlInserir.setDouble(5, obj.getDesconto());  
                    sqlInserir.setString(6, obj.getCodMovRemessa());                    
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorNossoNumero(String nossoNumero, String tipoRemessa, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM ControleRemessaDataGeracao WHERE ((nossoNumero = '" + nossoNumero +"')) ";
            if (!tipoRemessa.equals("RE")) {
            	sql += " and codmovremessa <> '01' ";
            }
            sql += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
