package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ItemPlanoFinanceiroAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ItemPlanoFinanceiroAlunoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ItemPlanoFinanceiroAlunoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ItemPlanoFinanceiroAlunoVO
 * @see ControleAcesso
 * @see PlanoFinanceiroAluno
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class ItemPlanoFinanceiroAluno extends ControleAcesso implements ItemPlanoFinanceiroAlunoInterfaceFacade {

    protected static String idEntidade;

    public ItemPlanoFinanceiroAluno() throws Exception {
        super();
        setIdEntidade("Matricula");
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#novo()
	 */
    public ItemPlanoFinanceiroAlunoVO novo() throws Exception {
        ItemPlanoFinanceiroAluno.incluir(getIdEntidade());
        ItemPlanoFinanceiroAlunoVO obj = new ItemPlanoFinanceiroAlunoVO();
        return obj;
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#incluir(negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemPlanoFinanceiroAlunoVO obj) throws Exception {
        ItemPlanoFinanceiroAlunoVO.validarDados(obj);
        final String sql = "INSERT INTO ItemPlanoFinanceiroAluno( planoDesconto, convenio, planoFinanceiroAluno, tipoItemPlanoFinanceiro ) VALUES ( ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
        if (obj.getPlanoDesconto().getCodigo().intValue() != 0) {
            sqlInserir.setInt(1, obj.getPlanoDesconto().getCodigo().intValue());
        } else {
            sqlInserir.setNull(1, 0);
        }
        if (obj.getConvenio().getCodigo().intValue() != 0) {
            sqlInserir.setInt(2, obj.getConvenio().getCodigo().intValue());
        } else {
            sqlInserir.setNull(2, 0);
        }
        sqlInserir.setInt(3, obj.getPlanoFinanceiroAluno().intValue());
        sqlInserir.setString(4, obj.getTipoItemPlanoFinanceiro());
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

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#alterar(negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemPlanoFinanceiroAlunoVO obj) throws Exception {
        ItemPlanoFinanceiroAlunoVO.validarDados(obj);
        ItemPlanoFinanceiroAluno.alterar(getIdEntidade());
        final String sql = "UPDATE ItemPlanoFinanceiroAluno set planoDesconto=?, convenio=?, planoFinanceiroAluno=?, tipoItemPlanoFinanceiro=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
        if (obj.getPlanoDesconto().getCodigo().intValue() != 0) {
        sqlAlterar.setInt(1, obj.getPlanoDesconto().getCodigo().intValue());
        } else {
            sqlAlterar.setNull(1, 0);
        }
        if (obj.getConvenio().getCodigo().intValue() != 0) {
            sqlAlterar.setInt(2, obj.getConvenio().getCodigo().intValue());
        } else {
            sqlAlterar.setNull(2, 0);
        }
        sqlAlterar.setInt(3, obj.getPlanoFinanceiroAluno().intValue());
        
            sqlAlterar.setString(4, obj.getTipoItemPlanoFinanceiro());
        
        sqlAlterar.setInt(5, obj.getCodigo().intValue());
        return sqlAlterar;
            }
        });
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#excluir(negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ItemPlanoFinanceiroAlunoVO obj) throws Exception {
        ItemPlanoFinanceiroAluno.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemPlanoFinanceiroAluno WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#consultarPorCodigoPlanoFinanceiroAluno(java.lang.Integer, boolean)
	 */
    public List consultarPorCodigoPlanoFinanceiroAluno(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemPlanoFinanceiroAluno.* FROM ItemPlanoFinanceiroAluno, PlanoFinanceiroAluno WHERE ItemPlanoFinanceiroAluno.planoFinanceiroAluno = PlanoFinanceiroAluno.codigo and PlanoFinanceiroAluno.codigo = " + valorConsulta.intValue() + " ORDER BY PlanoFinanceiroAluno.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#consultarPorDescricaoConvenio(java.lang.String, boolean)
	 */
    public List consultarPorDescricaoConvenio(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemPlanoFinanceiroAluno.* FROM ItemPlanoFinanceiroAluno, Convenio WHERE ItemPlanoFinanceiroAluno.convenio = Convenio.codigo and Convenio.descricao like('" + valorConsulta + "%') ORDER BY Convenio.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#consultarPorNomePlanoDesconto(java.lang.String, boolean)
	 */
    public List consultarPorNomePlanoDesconto(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ItemPlanoFinanceiroAluno.* FROM ItemPlanoFinanceiroAluno, PlanoDesconto WHERE ItemPlanoFinanceiroAluno.planoDesconto = PlanoDesconto.codigo and PlanoDesconto.nome like('" + valorConsulta + "%') ORDER BY PlanoDesconto.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean)
	 */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemPlanoFinanceiroAluno WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ItemPlanoFinanceiroAlunoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ItemPlanoFinanceiroAlunoVO obj = new ItemPlanoFinanceiroAlunoVO();
            obj = montarDados(tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ItemPlanoFinanceiroAlunoVO</code>.
     * @return  O objeto da classe <code>ItemPlanoFinanceiroAlunoVO</code> com os dados devidamente montados.
     */
    public static ItemPlanoFinanceiroAlunoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        ItemPlanoFinanceiroAlunoVO obj = new ItemPlanoFinanceiroAlunoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPlanoDesconto().setCodigo(new Integer(dadosSQL.getInt("planoDesconto")));
        obj.getConvenio().setCodigo(new Integer(dadosSQL.getInt("convenio")));
        obj.setPlanoFinanceiroAluno(new Integer(dadosSQL.getInt("planoFinanceiroAluno")));
        obj.setTipoItemPlanoFinanceiro(dadosSQL.getString("tipoItemPlanoFinanceiro"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosPlanoDesconto(obj, usuario);
        montarDadosConvenio(obj, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ConvenioVO</code> relacionado ao objeto <code>ItemPlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>ConvenioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosConvenio(ItemPlanoFinanceiroAlunoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getConvenio().getCodigo().intValue() == 0) {
            obj.setConvenio(new ConvenioVO());
            return;
        }
        obj.setConvenio(getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(obj.getConvenio().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PlanoDescontoVO</code> relacionado ao objeto <code>ItemPlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>PlanoDescontoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPlanoDesconto(ItemPlanoFinanceiroAlunoVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoDesconto().getCodigo().intValue() == 0) {
            obj.setPlanoDesconto(new PlanoDescontoVO());
            return;
        }
        obj.setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(obj.getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#excluirItemPlanoFinanceiroAlunos(java.lang.Integer)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItemPlanoFinanceiroAlunos(Integer planoFinanceiroAluno) throws Exception {
        String sql = "DELETE FROM ItemPlanoFinanceiroAluno WHERE (planoFinanceiroAluno = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{planoFinanceiroAluno});
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#alterarItemPlanoFinanceiroAlunos(java.lang.Integer, java.util.List)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItemPlanoFinanceiroAlunos(Integer planoFinanceiroAluno, List objetos) throws Exception {
        excluirItemPlanoFinanceiroAlunos(planoFinanceiroAluno);
        incluirItemPlanoFinanceiroAlunos(planoFinanceiroAluno, objetos);
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#incluirItemPlanoFinanceiroAlunos(java.lang.Integer, java.util.List)
	 */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItemPlanoFinanceiroAlunos(Integer planoFinanceiroAlunoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) e.next();
            obj.setPlanoFinanceiroAluno(planoFinanceiroAlunoPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ItemPlanoFinanceiroAlunoVO</code> relacionados a um objeto da classe <code>academico.PlanoFinanceiroAluno</code>.
     * @param planoFinanceiroAluno  Atributo de <code>academico.PlanoFinanceiroAluno</code> a ser utilizado para localizar os objetos da classe <code>ItemPlanoFinanceiroAlunoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ItemPlanoFinanceiroAlunoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarItemPlanoFinanceiroAlunos(Integer planoFinanceiroAluno,boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ItemPlanoFinanceiroAluno.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sqlStr = "SELECT * FROM ItemPlanoFinanceiroAluno WHERE planoFinanceiroAluno = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{planoFinanceiroAluno});
        while (tabelaResultado.next()) {
            ItemPlanoFinanceiroAlunoVO novoObj = new ItemPlanoFinanceiroAlunoVO();
            novoObj = ItemPlanoFinanceiroAluno.montarDados(tabelaResultado, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer)
	 */
    public ItemPlanoFinanceiroAlunoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM ItemPlanoFinanceiroAluno WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }
   
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ItemPlanoFinanceiroAluno.idEntidade;
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.academico.ItemPlanoFinanceiroAlunoInterfaceFacade#setIdEntidade(java.lang.String)
	 */
    public void setIdEntidade(String idEntidade) {
        ItemPlanoFinanceiroAluno.idEntidade = idEntidade;
    }
}