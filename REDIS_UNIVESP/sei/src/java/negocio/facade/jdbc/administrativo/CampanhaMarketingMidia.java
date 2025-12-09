/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.CampanhaMarketingMidiaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaMarketingMidiaInterfaceFacade;

/**
 *
 * @author rodrigo
 */
@Repository
@Scope("singleton")
@Lazy 
public class CampanhaMarketingMidia extends ControleAcesso implements CampanhaMarketingMidiaInterfaceFacade{

    protected static String idEntidade;

    public CampanhaMarketingMidia() throws Exception {
        super();
        setIdEntidade("CampanhaMarketing");
    }

    public CampanhaMarketingMidiaVO novo() throws Exception {
        CampanhaMarketingMidia.incluir(getIdEntidade());
        CampanhaMarketingMidiaVO obj = new CampanhaMarketingMidiaVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CampanhaMarketingMidiaVO obj) throws Exception {
        CampanhaMarketingMidiaVO.validarDados(obj);
        final String sql = "INSERT INTO CampanhaMarketingMidia( midia, campanhaMarketing ) VALUES ( ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getMidia().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getCampanhaMarketing().intValue());
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
    public void alterar(final CampanhaMarketingMidiaVO obj) throws Exception {
        CampanhaMarketingMidiaVO.validarDados(obj);
        UnidadeEnsinoCurso.alterar(getIdEntidade());
        final String sql = "UPDATE CampanhaMarketingMidia set  midia=?, campanhaMarketing=? WHERE (codigo=?)";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getMidia().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getCampanhaMarketing().intValue());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>UnidadeEnsinoCursoVO</code>.
     * @return  O objeto da classe <code>UnidadeEnsinoCursoVO</code> com os dados devidamente montados.
     */
    public static CampanhaMarketingMidiaVO montarDados(SqlRowSet dadosSQL) throws Exception {
        CampanhaMarketingMidiaVO obj = new CampanhaMarketingMidiaVO();
        obj.setCampanhaMarketing(new Integer(dadosSQL.getInt("campanhaMarketing")));
        obj.getMidia().setCodigo(new Integer(dadosSQL.getInt("midia")));
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNovoObj(Boolean.FALSE);

        obj.setMidia(obj.getMidia());
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMidia(Integer campanhaMarketing) throws Exception {
        String sql = "DELETE FROM CampanhaMarketingMidia WHERE (campanhaMarketing = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[] {campanhaMarketing.intValue()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>UnidadeEnsinoCursoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirUnidadeEnsinoCursos</code> e <code>incluirUnidadeEnsinoCursos</code> disponíveis na classe <code>UnidadeEnsinoCurso</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarMidia(Integer campanhaMarketing, List objetos) throws Exception {
        excluirMidia(campanhaMarketing);
        incluirMidia(campanhaMarketing, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>UnidadeEnsinoCursoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>administrativo.UnidadeEnsino</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirMidia(Integer campanhaMarketing, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaMarketingMidiaVO obj = (CampanhaMarketingMidiaVO) e.next();
            obj.setCampanhaMarketing(campanhaMarketing);
            incluir(obj);
        }
    }

    public List consultarMidias(Integer campanhaMarketing) throws Exception {
        UnidadeEnsinoCurso.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM CampanhaMarketingMidia WHERE campanhaMarketing = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {campanhaMarketing.intValue()});
        while (resultado.next()) {
            CampanhaMarketingMidiaVO novoObj = new CampanhaMarketingMidiaVO();
            novoObj = CampanhaMarketingMidia.montarDados(resultado);
            objetos.add(novoObj);
        }
        return objetos;
    }

    public static void setIdEntidade(String idEntidade) {
        CampanhaMarketingMidia.idEntidade = idEntidade;
    }

    public static String getIdEntidade() {
        return CampanhaMarketing.idEntidade;
    }

    public CampanhaMarketingMidiaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM UnidadeEnsinoCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigo.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }
    
}
