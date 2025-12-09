package negocio.facade.jdbc.biblioteca;

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
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemRegistroEntradaAcervoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.biblioteca.ItemRegistroEntradaAcervoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ItemRegistroEntradaAcervoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ItemRegistroEntradaAcervoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ItemRegistroEntradaAcervoVO
 * @see ControleAcesso
 * @see RegistroEntradaAcervo
 */
@Repository
@Scope("singleton")
@Lazy
public class ItemRegistroEntradaAcervo extends ControleAcesso implements ItemRegistroEntradaAcervoInterfaceFacade {

    protected static String idEntidade;

    public ItemRegistroEntradaAcervo() throws Exception {
        super();
        setIdEntidade("RegistroEntradaAcervo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ItemRegistroEntradaAcervoVO</code>.
     */
    public ItemRegistroEntradaAcervoVO novo() throws Exception {
        ItemRegistroEntradaAcervo.incluir(getIdEntidade());
        ItemRegistroEntradaAcervoVO obj = new ItemRegistroEntradaAcervoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ItemRegistroEntradaAcervoVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ItemRegistroEntradaAcervoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemRegistroEntradaAcervoVO obj) throws Exception {
        try {
            ItemRegistroEntradaAcervoVO.validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItemRegistroEntradaAcervo.incluir(getIdEntidade());
            obj.realizarUpperCaseDados();

            final String sql = "INSERT INTO ItemRegistroEntradaAcervo( registroEntradaAcervo, exemplar, tipoEntrada ) VALUES ( ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getRegistroEntradaAcervo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getRegistroEntradaAcervo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getExemplar().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getExemplar().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setString(3, obj.getTipoEntrada());
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
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ItemRegistroEntradaAcervoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ItemRegistroEntradaAcervoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemRegistroEntradaAcervoVO obj) throws Exception {
        try {
            ItemRegistroEntradaAcervoVO.validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItemRegistroEntradaAcervo.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();

            final String sql = "UPDATE ItemRegistroEntradaAcervo set registroEntradaAcervo=?, exemplar=?, tipoEntrada=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getRegistroEntradaAcervo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getRegistroEntradaAcervo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getExemplar().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getExemplar().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setString(3, obj.getTipoEntrada());
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ItemRegistroEntradaAcervoVO</code>. Sempre
     * localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ItemRegistroEntradaAcervoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ItemRegistroEntradaAcervoVO obj) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItemRegistroEntradaAcervo.excluir(getIdEntidade());
            String sql = "DELETE FROM ItemRegistroEntradaAcervo WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoExemplar(Integer exemplar) throws Exception {
        try {
            String sql = "DELETE FROM ItemRegistroEntradaAcervo WHERE ((exemplar = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{exemplar});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarTipoEntradaItemRegistroEntradaAcervo(final ItemRegistroEntradaAcervoVO itemRegistroEntradaAcervoVO) throws Exception {
        try {
            final String sql = "UPDATE itemRegistroEntradaAcervo SET tipoEntrada = ? WHERE exemplar = ?";
            getConexao().getJdbcTemplate().update(sql, new Object[]{itemRegistroEntradaAcervoVO.getTipoEntrada(), itemRegistroEntradaAcervoVO.getExemplar().getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemRegistroEntradaAcervo</code> através do valor do atributo
     * <code>String tipoEntrada</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoEntrada(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemRegistroEntradaAcervo WHERE upper( tipoEntrada ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoEntrada";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemRegistroEntradaAcervo</code> através do valor do atributo
     * <code>codigo</code> da classe <code>Exemplar</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoExemplar(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemRegistroEntradaAcervo.* FROM ItemRegistroEntradaAcervo, Exemplar WHERE ItemRegistroEntradaAcervo.exemplar = Exemplar.codigo and Exemplar.codigo >= "
                + valorConsulta.intValue() + " ORDER BY Exemplar.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemRegistroEntradaAcervo</code> através do valor do atributo
     * <code>codigo</code> da classe <code>RegistroEntradaAcervo</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoRegistroEntradaAcervo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemRegistroEntradaAcervo.* FROM ItemRegistroEntradaAcervo, RegistroEntradaAcervo WHERE ItemRegistroEntradaAcervo.registroEntradaAcervo = RegistroEntradaAcervo.codigo and RegistroEntradaAcervo.codigo >= "
                + valorConsulta.intValue() + " ORDER BY RegistroEntradaAcervo.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemRegistroEntradaAcervo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemRegistroEntradaAcervo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ItemRegistroEntradaAcervoVO</code>.
     *
     * @return O objeto da classe <code>ItemRegistroEntradaAcervoVO</code> com os dados devidamente montados.
     */
    public static ItemRegistroEntradaAcervoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ItemRegistroEntradaAcervoVO obj = new ItemRegistroEntradaAcervoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setRegistroEntradaAcervo(dadosSQL.getInt("registroEntradaAcervo"));
        obj.getExemplar().setCodigo(dadosSQL.getInt("exemplar"));
        obj.setTipoEntrada(dadosSQL.getString("tipoEntrada"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosExemplar(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ExemplarVO</code> relacionado ao objeto
     * <code>ItemRegistroEntradaAcervoVO</code>. Faz uso da chave primária da classe <code>ExemplarVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosExemplar(ItemRegistroEntradaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getExemplar().getCodigo().intValue() == 0) {
            obj.setExemplar(new ExemplarVO());
            return;
        }
        getFacadeFactory().getExemplarFacade().carregarDados(obj.getExemplar(), 0, NivelMontarDados.BASICO, usuario);        
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ItemRegistroEntradaAcervoVO</code> no BD. Faz uso da
     * operação <code>excluir</code> disponível na classe <code>ItemRegistroEntradaAcervo</code>.
     *
     * @param <code>registroEntradaAcervo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItemRegistroEntradaAcervos(Integer registroEntradaAcervo) throws Exception {
            String sql = "DELETE FROM ItemRegistroEntradaAcervo WHERE (registroEntradaAcervo = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{registroEntradaAcervo});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ItemRegistroEntradaAcervoVO</code> contidos em um
     * Hashtable no BD. Faz uso da operação <code>excluirItemRegistroEntradaAcervos</code> e
     * <code>incluirItemRegistroEntradaAcervos</code> disponíveis na classe <code>ItemRegistroEntradaAcervo</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItemRegistroEntradaAcervos(Integer registroEntradaAcervo, List objetos) throws Exception {
        String sql = "DELETE FROM ItemRegistroEntradaAcervo WHERE registroEntradaAcervo = " + registroEntradaAcervo;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            ItemRegistroEntradaAcervoVO objeto = (ItemRegistroEntradaAcervoVO) i.next();
            sql += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(sql);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemRegistroEntradaAcervoVO objeto = (ItemRegistroEntradaAcervoVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ItemRegistroEntradaAcervoVO</code> no BD. Garantindo o
     * relacionamento com a entidade principal <code>biblioteca.RegistroEntradaAcervo</code> através do atributo de
     * vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItemRegistroEntradaAcervos(Integer registroEntradaAcervoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemRegistroEntradaAcervoVO obj = (ItemRegistroEntradaAcervoVO) e.next();
            obj.setRegistroEntradaAcervo(registroEntradaAcervoPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ItemRegistroEntradaAcervoVO</code> relacionados a um objeto da
     * classe <code>biblioteca.RegistroEntradaAcervo</code>.
     *
     * @param registroEntradaAcervo
     *            Atributo de <code>biblioteca.RegistroEntradaAcervo</code> a ser utilizado para localizar os objetos da
     *            classe <code>ItemRegistroEntradaAcervoVO</code>.
     * @return List Contendo todos os objetos da classe <code>ItemRegistroEntradaAcervoVO</code> resultantes da
     *         consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarItemRegistroEntradaAcervos(Integer registroEntradaAcervo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ItemRegistroEntradaAcervo.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM ItemRegistroEntradaAcervo WHERE registroEntradaAcervo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{registroEntradaAcervo});
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ItemRegistroEntradaAcervoVO</code> através de sua
     * chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ItemRegistroEntradaAcervoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ItemRegistroEntradaAcervo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ItemRegistroEntradaAcervo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ItemRegistroEntradaAcervo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ItemRegistroEntradaAcervo.idEntidade = idEntidade;
    }
}
