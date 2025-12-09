/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.compras;

import java.io.File;
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

import negocio.comuns.compras.DevolucaoCompraItemImagemVO;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.DevolucaoCompraItemImagemInterfaceFacade;

/**
 *
 * @author Otimize-TI
 */
@Repository
@Scope("singleton")
@Lazy 
public class DevolucaoCompraItemImagem extends ControleAcesso implements DevolucaoCompraItemImagemInterfaceFacade{

    public DevolucaoCompraItemImagem() throws Exception {
        super();
    }

    public DevolucaoCompraItemImagemVO novo() throws Exception {

        DevolucaoCompraItemImagemVO obj = new DevolucaoCompraItemImagemVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>DevolucaoCompraItemVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final DevolucaoCompraItemImagemVO obj) throws Exception {

        final String sql = "INSERT INTO DevolucaoCompraItemImagem( devolucaoCompraItem, nomeImagem, imagem ) VALUES ( ?, ?, lo_import(?)) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getDevolucaoCompraItem().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getDevolucaoCompraItem().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getNomeImagem());
                sqlInserir.setString(3, obj.getImagem());
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final DevolucaoCompraItemImagemVO obj) throws Exception {
        final String sql = "UPDATE DevolucaoCompraItemImagem set devolucaoCompraItem=?, imagem = lo_import(?), nomeImagem=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getDevolucaoCompraItem().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getDevolucaoCompraItem().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getImagem());
                sqlAlterar.setString(3, obj.getNomeImagem());
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>DevolucaoCompraItemVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(DevolucaoCompraItemImagemVO obj) throws Exception {
        DevolucaoCompraItem.excluir(getIdEntidade());
        String sql = "DELETE FROM DevolucaoCompraItemImagem WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    @SuppressWarnings("static-access")
    public static void consultarImagem(DevolucaoCompraItemImagemVO obj) throws Exception {
        obj.setImagem(UteisJSF.getCaminhoWebFotos() + File.separator + obj.getNomeImagem());
        File file1 = new File(obj.getImagem());
        file1.delete();
        obj.setImagem(obj.getImagem().replace("\\", "/"));
        String sql = "SELECT lo_export(imagem, '" + obj.getImagem() + "') " +
                "FROM DevolucaoCompraItemImagem " +
                "WHERE codigo = " + obj.getCodigo().intValue();
        getConexao().getJdbcTemplate().queryForRowSet(sql);
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>DevolucaoCompraItemVO</code>.
     * @return  O objeto da classe <code>DevolucaoCompraItemVO</code> com os dados devidamente montados.
     */
    public static DevolucaoCompraItemImagemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        DevolucaoCompraItemImagemVO obj = new DevolucaoCompraItemImagemVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setImagem(dadosSQL.getString("imagem"));
        obj.setNomeImagem(dadosSQL.getString("nomeImagem"));
        obj.setDevolucaoCompraItem(new Integer(dadosSQL.getInt("devolucaoCompraItem")));
        obj.setNovoObj(Boolean.FALSE);
        consultarImagem(obj);
        obj.setImagemTemp("../imagem/" + obj.getNomeImagem());
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluirDevolucaoCompraItemImagems(Integer devolucaoCompraItem) throws Exception {
        DevolucaoCompraItem.excluir(getIdEntidade());
        String sql = "DELETE FROM DevolucaoCompraItemImagem WHERE (devolucaoCompraItem = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{devolucaoCompraItem});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>DevolucaoCompraItemVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirDevolucaoCompraItems</code> e <code>incluirDevolucaoCompraItems</code> disponíveis na classe <code>DevolucaoCompraItem</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDevolucaoCompraItemImagems(Integer devolucaoCompraItem, List objetos) throws Exception {
        excluirDevolucaoCompraItemImagems(devolucaoCompraItem);
        incluirDevolucaoCompraItemImagems(devolucaoCompraItem, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>DevolucaoCompraItemVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>compra.DevolucaoCompra</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDevolucaoCompraItemImagems(Integer devolucaoCompraItemPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            DevolucaoCompraItemImagemVO obj = (DevolucaoCompraItemImagemVO) e.next();
            obj.setDevolucaoCompraItem(devolucaoCompraItemPrm);
            incluir(obj);
        }
    }

    @SuppressWarnings("static-access")
    public static List consultarDevolucaoCompraItemImagems(Integer devolucaoCompraItem, int nivelMontarDados) throws Exception {
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DevolucaoCompraItemImagem WHERE devolucaoCompraItem = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql,new Object[]{devolucaoCompraItem});
        while (resultado.next()) {
            DevolucaoCompraItemImagemVO novoObj = new DevolucaoCompraItemImagemVO();
            novoObj = DevolucaoCompraItemImagem.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }
}
