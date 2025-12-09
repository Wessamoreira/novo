package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.administrativo.VisaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.VisaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>VisaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>VisaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see VisaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Visao extends ControleAcesso implements VisaoInterfaceFacade {

    protected static String idEntidade;

    public Visao() throws Exception {
        super();
        setIdEntidade("Visao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>VisaoVO</code>.
     */
    public VisaoVO novo() throws Exception {
        Visao.incluir(getIdEntidade());
        VisaoVO obj = new VisaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>VisaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>VisaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final VisaoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            VisaoVO.validarDados(obj);
            Visao.incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO Visao( nome, valorCssTopoLogo, valorCssBackGround, valorCssMenu) VALUES ( ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getValorCssTopoLogo());
                    sqlInserir.setString(3, obj.getValorCssBackground());
                    sqlInserir.setString(4, obj.getValorCssMenu());
                    //sqlInserir.setString(2, obj.getCsspadrao());
                    //sqlInserir.setString(3, obj.getImagemBackground());
                    //sqlInserir.setString(4, obj.getNomeImagemBackground());
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>VisaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>VisaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final VisaoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            VisaoVO.validarDados(obj);
            Visao.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE Visao set nome=?, valorCssTopoLogo=?, valorCssBackGround=?, valorCssMenu=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getValorCssTopoLogo());
                    sqlAlterar.setString(3, obj.getValorCssBackground());
                    sqlAlterar.setString(4, obj.getValorCssMenu());
//            sqlAlterar.setString(3, obj.getImagemBackground());
//            sqlAlterar.setString(4, obj.getNomeImagemBackground());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>VisaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>VisaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(VisaoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            Visao.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Visao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

//    public static void consultarImagem(VisaoVO obj) throws Exception {
//        String caminho = new SuperControle().obterCaminhoWebFotos() + File.separator + obj.getNomeImagemBackground();
//        File file1 = new File(caminho);
//        file1.delete();
//        obj.setImagemBackground(caminho);
//        obj.setImagemBackground(obj.getImagemBackground().replace("\\", "/"));
//        String sql = "SELECT lo_export(imagemBackground, '" + obj.getImagemBackground() + "') " +
//                "FROM Visao " +
//                "WHERE codigo = " + obj.getCodigo().intValue();
//        Statement stm = con.createStatement();
//        stm.executeQuery(sql);
//    }
    /**
     * Responsável por realizar uma consulta de <code>Visao</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>VisaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Visao WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>Visao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>VisaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Visao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>VisaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>VisaoVO</code>.
     * @return  O objeto da classe <code>VisaoVO</code> com os dados devidamente montados.
     */
    public static VisaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        VisaoVO obj = new VisaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setValorCssTopoLogo(dadosSQL.getString("valorCssTopoLogo"));
        obj.setValorCssBackground(dadosSQL.getString("valorCssBackGround"));
        obj.setValorCssMenu(dadosSQL.getString("valorCssMenu"));
//        obj.setImagemBackground(dadosSQL.getString("imagemBackground"));
//        obj.setNomeImagemBackground(dadosSQL.getString("nomeImagemBackground"));
//        obj.setNovoObj(Boolean.FALSE);
//        if (obj.getImagemBackground() == null) {
//            obj.setImagemBackground("");
//        }
//        if (!obj.getImagemBackground().equals("")) {
//            consultarImagem(obj);
//        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>VisaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public VisaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM Visao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Visao ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /** Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Visao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Visao.idEntidade = idEntidade;
    }
}
