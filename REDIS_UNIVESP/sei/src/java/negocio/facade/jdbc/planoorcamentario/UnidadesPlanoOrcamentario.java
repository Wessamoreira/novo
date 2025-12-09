package negocio.facade.jdbc.planoorcamentario;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.facade.jdbc.academico.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.planoorcamentario.UnidadesPlanoOrcamentarioInterfaceFacade;

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

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>UnidadesPlanoOrcamentarioVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>UnidadesPlanoOrcamentarioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see UnidadesPlanoOrcamentarioVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class UnidadesPlanoOrcamentario extends ControleAcesso implements UnidadesPlanoOrcamentarioInterfaceFacade {

    protected static String idEntidade;

    public UnidadesPlanoOrcamentario() throws Exception {
        super();
        setIdEntidade("PlanoOrcamentario");
    }

    public UnidadesPlanoOrcamentarioVO novo() throws Exception {
        UnidadesPlanoOrcamentario.incluir(getIdEntidade());
        UnidadesPlanoOrcamentarioVO obj = new UnidadesPlanoOrcamentarioVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        UnidadesPlanoOrcamentarioVO.validarDados(obj);
        final String sql = "INSERT INTO UnidadesPlanoOrcamentario( planoOrcamentario, unidadeEnsino ) VALUES ( ? , ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getPlanoOrcamentario().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
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

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        UnidadesPlanoOrcamentarioVO.validarDados(obj);
        final String sql = "UPDATE UnidadesPlanoOrcamentario set planoOrcamentario=?, unidadeEnsino=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getPlanoOrcamentario().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        UnidadesPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM UnidadesPlanoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT UnidadesPlanoOrcamentario.* FROM UnidadesPlanoOrcamentario, PlanoOrcamentario WHERE UnidadesPlanoOrcamentario.planoOrcamentario = PlanoOrcamentario.codigo and PlanoOrcamentario.nome like('" + valorConsulta + "%') ORDER BY PlanoOrcamentario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM UnidadesPlanoOrcamentario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>UnidadesPlanoOrcamentarioVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>UnidadesPlanoOrcamentarioVO</code>.
     *
     * @return O objeto da classe <code>UnidadesPlanoOrcamentarioVO</code> com os dados devidamente montados.
     */
    public static UnidadesPlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        UnidadesPlanoOrcamentarioVO obj = new UnidadesPlanoOrcamentarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getPlanoOrcamentario().setCodigo(new Integer(dadosSQL.getInt("planoOrcamentario")));
        obj.setNovoObj(Boolean.FALSE);
        //montarDadosPlanoOrcamentario(obj, usuario);
        montarDadosUnidadeEnsino(obj, usuario);
        return obj;
    }

    public static void montarDadosPlanoOrcamentario(UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoOrcamentario().getCodigo().intValue() == 0) {
            obj.setPlanoOrcamentario(new PlanoOrcamentarioVO());
            return;
        }
        obj.setPlanoOrcamentario(getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorChavePrimaria(obj.getPlanoOrcamentario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosUnidadeEnsino(UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirUnidadesPlanoOrcamentarios(Integer planoOrcamentario, UsuarioVO usuario) throws Exception {
        UnidadesPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM UnidadesPlanoOrcamentario WHERE (planoOrcamentario = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{planoOrcamentario});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarUnidadesPlanoOrcamentarios(Integer planoOrcamentario, List objetos, UsuarioVO usuario) throws Exception {
        excluirUnidadesPlanoOrcamentarios(planoOrcamentario, usuario);
        incluirUnidadesPlanoOrcamentarios(planoOrcamentario, objetos, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirUnidadesPlanoOrcamentarios(Integer planoOrcamentarioPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            UnidadesPlanoOrcamentarioVO obj = (UnidadesPlanoOrcamentarioVO) e.next();
            obj.getPlanoOrcamentario().setCodigo(planoOrcamentarioPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>UnidadesPlanoOrcamentarioVO</code> relacionados a um objeto da classe
     * <code>academico.Curso</code>.
     *
     * @param curso
     *            Atributo de <code>academico.Curso</code> a ser utilizado para localizar os objetos da classe
     *            <code>UnidadesPlanoOrcamentarioVO</code>.
     * @return List Contendo todos os objetos da classe <code>UnidadesPlanoOrcamentarioVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarUnidadesPlanoOrcamentarios(Integer planoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM UnidadesPlanoOrcamentario WHERE planoOrcamentario = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{planoOrcamentario});
        while (resultado.next()) {
            objetos.add(UnidadesPlanoOrcamentario.montarDados(resultado, usuario));
        }
        return objetos;
    }

    public UnidadesPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM UnidadesPlanoOrcamentario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("select unidadesplanoorcamentario.codigo, ");
        str.append(" planoOrcamentario.codigo AS \"planoOrcamentario.codigo\", planoorcamentario.nome AS \"planoorcamentario.nome\", ");
        str.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\" ");
        str.append(" from unidadesplanoorcamentario ");
        str.append(" inner join planoorcamentario on planoorcamentario.codigo = unidadesplanoorcamentario.planoorcamentario ");
        str.append(" inner join unidadeEnsino on unidadeensino.codigo = unidadesplanoorcamentario.unidadeEnsino ");
        return str;
    }

    public List<UnidadesPlanoOrcamentarioVO> consultaRapidaPorPlanoOrcamentario(Integer planoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE planoOrcamentario.codigo = ");
        sqlStr.append(planoOrcamentario.intValue());
        sqlStr.append(" ORDER BY unidadeensino.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public Boolean consultarPorUnidadeEnsinoPlanoOrcamentario(Integer planoOrcamentario, Integer unidadeEnsino) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from unidadesplanoorcamentario  where unidadeensino = ");
        sb.append(unidadeEnsino);
        sb.append(" and planoorcamentario = ");
        sb.append(planoOrcamentario);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public List<UnidadesPlanoOrcamentarioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<UnidadesPlanoOrcamentarioVO> vetResultado = new ArrayList<UnidadesPlanoOrcamentarioVO>(0);
        while (tabelaResultado.next()) {
            UnidadesPlanoOrcamentarioVO obj = new UnidadesPlanoOrcamentarioVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    private void montarDadosBasico(UnidadesPlanoOrcamentarioVO obj, SqlRowSet dadosSQL) throws Exception {
        //Dados UnidadesPlanoOrcamentario
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));

        // Dados do Plano Orçamentário
        obj.getPlanoOrcamentario().setCodigo(dadosSQL.getInt("planoOrcamentario.codigo"));
        obj.getPlanoOrcamentario().setNome(dadosSQL.getString("planoorcamentario.nome"));

        //Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
        
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return UnidadesPlanoOrcamentario.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        UnidadesPlanoOrcamentario.idEntidade = idEntidade;
    }
}
