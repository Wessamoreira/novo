/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.compras;

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
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorCategoriaProdutoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.FornecedorCategoriaProdutoInterfaceFacade;

/**
 *
 * @author Rodrigo
 */
@Repository
@Scope("singleton")
@Lazy 
public class FornecedorCategoriaProduto extends ControleAcesso implements FornecedorCategoriaProdutoInterfaceFacade{

    protected static String idEntidade;

    public FornecedorCategoriaProduto() throws Exception {
        super();
        setIdEntidade("Fornecedor");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FornecedorCategoriaProdutoVO</code>.
     */
    public FornecedorCategoriaProdutoVO novo() throws Exception {
        FornecedorCategoriaProduto.incluir(getIdEntidade());
        FornecedorCategoriaProdutoVO obj = new FornecedorCategoriaProdutoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FornecedorCategoriaProdutoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FornecedorCategoriaProdutoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void incluir(final FornecedorCategoriaProdutoVO obj) throws Exception {
        FornecedorCategoriaProdutoVO.validarDados(obj);
        final String sql = "INSERT INTO FornecedorCategoriaProduto( fornecedor, categoriaProduto) VALUES ( ?, ?) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getFornecedor().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getFornecedor().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getCategoriaProdutoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getCategoriaProdutoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
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
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FornecedorCategoriaProdutoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FornecedorCategoriaProdutoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void alterar(final FornecedorCategoriaProdutoVO obj) throws Exception {
        FornecedorCategoriaProdutoVO.validarDados(obj);
        final String sql = "UPDATE FornecedorCategoriaProduto set fornecedor=?, categoriaProduto=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getFornecedor().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getFornecedor().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getCategoriaProdutoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getCategoriaProdutoVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FornecedorCategoriaProdutoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FornecedorCategoriaProdutoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluir(FornecedorCategoriaProdutoVO obj) throws Exception {
        FornecedorCategoriaProduto.excluir(getIdEntidade());
        String sql = "DELETE FROM FornecedorCategoriaProduto WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>FornecedorCategoriaProduto</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorCategoriaProdutoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @SuppressWarnings("static-access")
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FornecedorCategoriaProduto WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FornecedorCategoriaProdutoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FornecedorCategoriaProdutoVO</code>.
     * @return  O objeto da classe <code>FornecedorCategoriaProdutoVO</code> com os dados devidamente montados.
     */
    public static FornecedorCategoriaProdutoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FornecedorCategoriaProdutoVO obj = new FornecedorCategoriaProdutoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setFornecedor(new Integer(dadosSQL.getInt("fornecedor")));
        obj.getCategoriaProdutoVO().setCodigo(dadosSQL.getInt("categoriaProduto"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosCategoriaProduto(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    public static void montarDadosCategoriaProduto(FornecedorCategoriaProdutoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCategoriaProdutoVO().getCodigo().intValue() == 0) {
            obj.setCategoriaProdutoVO(new CategoriaProdutoVO());
            return;
        }
        obj.setCategoriaProdutoVO(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCategoriaProdutoVO().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>FornecedorCategoriaProdutoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FornecedorCategoriaProduto</code>.
     * @param <code>devolucaoCompra</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @SuppressWarnings("static-access")
    public void excluirFornecedorCategoriaProdutos(Integer fornecedor) throws Exception {
        FornecedorCategoriaProduto.excluir(getIdEntidade());
        String sql = "DELETE FROM FornecedorCategoriaProduto WHERE (fornecedor = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{fornecedor});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>FornecedorCategoriaProdutoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFornecedorCategoriaProdutos</code> e <code>incluirFornecedorCategoriaProdutos</code> disponíveis na classe <code>FornecedorCategoriaProduto</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarFornecedorCategoriaProdutos(Integer fornecedor, List objetos) throws Exception {
        excluirFornecedorCategoriaProdutos(fornecedor);
        incluirFornecedorCategoriaProdutos(fornecedor, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>FornecedorCategoriaProdutoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>compra.DevolucaoCompra</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirFornecedorCategoriaProdutos(Integer fornecedorPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            FornecedorCategoriaProdutoVO obj = (FornecedorCategoriaProdutoVO) e.next();
            obj.setFornecedor(fornecedorPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>FornecedorCategoriaProdutoVO</code> relacionados a um objeto da classe <code>compra.DevolucaoCompra</code>.
     * @param devolucaoCompra  Atributo de <code>compra.DevolucaoCompra</code> a ser utilizado para localizar os objetos da classe <code>FornecedorCategoriaProdutoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>FornecedorCategoriaProdutoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    
    public static List<FornecedorCategoriaProdutoVO> consultarFornecedorCategoriaProdutos(Integer fornecedor, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FornecedorCategoriaProduto.consultar(getIdEntidade());
        List<FornecedorCategoriaProdutoVO> objetos = new ArrayList<FornecedorCategoriaProdutoVO>(0);
        String sql = "SELECT * FROM FornecedorCategoriaProduto WHERE fornecedor = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{fornecedor});
        while (resultado.next()) {
            FornecedorCategoriaProdutoVO novoObj = new FornecedorCategoriaProdutoVO();
            novoObj = FornecedorCategoriaProduto.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FornecedorCategoriaProdutoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @SuppressWarnings("static-access")
    public FornecedorCategoriaProdutoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FornecedorCategoriaProduto WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( FornecedorCategoriaProduto ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FornecedorCategoriaProduto.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        FornecedorCategoriaProduto.idEntidade = idEntidade;
    }
}
