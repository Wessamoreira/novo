package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioUnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConvenioUnidadeEnsinoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ConvenioUnidadeEnsinoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ConvenioUnidadeEnsinoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ConvenioUnidadeEnsinoVO
 * @see ControleAcesso
 * @see Convenio
 */
@Repository
@Scope("singleton")
@Lazy 
public class ConvenioUnidadeEnsino extends ControleAcesso implements ConvenioUnidadeEnsinoInterfaceFacade{

    protected static String idEntidade;

    public ConvenioUnidadeEnsino() throws Exception {
        super();
        setIdEntidade("Convenio");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ConvenioUnidadeEnsinoVO</code>.
     */
    public ConvenioUnidadeEnsinoVO novo() throws Exception {
        ConvenioUnidadeEnsino.incluir(getIdEntidade());
        ConvenioUnidadeEnsinoVO obj = new ConvenioUnidadeEnsinoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ConvenioUnidadeEnsinoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ConvenioUnidadeEnsinoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ConvenioUnidadeEnsinoVO obj) throws Exception {
        ConvenioUnidadeEnsinoVO.validarDados(obj);
        final String sql = "INSERT INTO ConvenioUnidadeEnsino( convenio, unidadeEnsino ) VALUES ( ?, ? )";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getConvenio(), obj.getUnidadeEnsino().getCodigo()});
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConvenioUnidadeEnsinoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ConvenioUnidadeEnsinoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ConvenioUnidadeEnsinoVO obj) throws Exception {
        ConvenioUnidadeEnsinoVO.validarDados(obj);
        ConvenioUnidadeEnsino.alterar(getIdEntidade());
        final String sql = "UPDATE ConvenioUnidadeEnsino set  WHERE ((convenio = ?) and (unidadeEnsino = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                if (obj.getConvenio().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getConvenio().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ConvenioUnidadeEnsinoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ConvenioUnidadeEnsinoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ConvenioUnidadeEnsinoVO obj) throws Exception {
        ConvenioUnidadeEnsino.excluir(getIdEntidade());
        String sql = "DELETE FROM ConvenioUnidadeEnsino WHERE ((convenio = ?) and (unidadeEnsino = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getConvenio(), obj.getUnidadeEnsino().getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>ConvenioUnidadeEnsino</code> através do valor do atributo 
     * <code>nome</code> da classe <code>UnidadeEnsino</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ConvenioUnidadeEnsinoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ConvenioUnidadeEnsino.* FROM ConvenioUnidadeEnsino, UnidadeEnsino WHERE ConvenioUnidadeEnsino.unidadeEnsino = UnidadeEnsino.codigo and upper( UnidadeEnsino.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ConvenioUnidadeEnsino</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>Convenio</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ConvenioUnidadeEnsinoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoConvenio(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT ConvenioUnidadeEnsino.* FROM ConvenioUnidadeEnsino, Convenio WHERE ConvenioUnidadeEnsino.convenio = Convenio.codigo and upper( Convenio.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Convenio.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ConvenioUnidadeEnsinoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ConvenioUnidadeEnsinoVO</code>.
     * @return  O objeto da classe <code>ConvenioUnidadeEnsinoVO</code> com os dados devidamente montados.
     */
    public static ConvenioUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ConvenioUnidadeEnsinoVO obj = new ConvenioUnidadeEnsinoVO();
        obj.setConvenio(new Integer(dadosSQL.getInt("convenio")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>ConvenioUnidadeEnsinoVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(ConvenioUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ConvenioUnidadeEnsinoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ConvenioUnidadeEnsino</code>.
     * @param <code>convenio</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirConvenioUnidadeEnsinos(Integer convenio) throws Exception {
        String sql = "DELETE FROM ConvenioUnidadeEnsino WHERE (convenio = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{convenio});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ConvenioUnidadeEnsinoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirConvenioUnidadeEnsinos</code> e <code>incluirConvenioUnidadeEnsinos</code> disponíveis na classe <code>ConvenioUnidadeEnsino</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarConvenioUnidadeEnsinos(Integer convenio, List objetos) throws Exception {
        excluirConvenioUnidadeEnsinos(convenio);
        incluirConvenioUnidadeEnsinos(convenio, objetos);
    }

    /**
     * Operação responsável por incluir objetos da <code>ConvenioUnidadeEnsinoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.Convenio</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirConvenioUnidadeEnsinos(Integer convenioPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ConvenioUnidadeEnsinoVO obj = (ConvenioUnidadeEnsinoVO) e.next();
            obj.setConvenio(convenioPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ConvenioUnidadeEnsinoVO</code> relacionados a um objeto da classe <code>financeiro.Convenio</code>.
     * @param convenio  Atributo de <code>financeiro.Convenio</code> a ser utilizado para localizar os objetos da classe <code>ConvenioUnidadeEnsinoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ConvenioUnidadeEnsinoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarConvenioUnidadeEnsinos(Integer convenio, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ConvenioUnidadeEnsino.consultar(getIdEntidade());
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM ConvenioUnidadeEnsino WHERE convenio = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{convenio});
        while (resultado.next()) {
            ConvenioUnidadeEnsinoVO novoObj = new ConvenioUnidadeEnsinoVO();
            novoObj = ConvenioUnidadeEnsino.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ConvenioUnidadeEnsinoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ConvenioUnidadeEnsinoVO consultarPorChavePrimaria(Integer convenioPrm, Integer unidadeEnsinoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ConvenioUnidadeEnsino WHERE convenio = ? and unidadeEnsino = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{convenioPrm, unidadeEnsinoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ConvenioUnidadeEnsino ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ConvenioUnidadeEnsino.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ConvenioUnidadeEnsino.idEntidade = idEntidade;
    }
}
