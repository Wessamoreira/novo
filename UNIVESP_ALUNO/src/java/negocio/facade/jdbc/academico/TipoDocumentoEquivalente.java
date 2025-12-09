package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.TipoDocumentoEquivalenteVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoDocumentoEquivalenteInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TipoDocumentoEquivalenteVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TipoDocumentoEquivalenteVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TipoDocumentoEquivalenteVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class TipoDocumentoEquivalente extends ControleAcesso implements TipoDocumentoEquivalenteInterfaceFacade {

    protected static String idEntidade;

    public TipoDocumentoEquivalente() throws Exception {
        super();
        setIdEntidade("TipoDocumento");
    }

    public TipoDocumentoEquivalenteVO novo() throws Exception {
        TipoDocumentoEquivalente.incluir(getIdEntidade());
        TipoDocumentoEquivalenteVO obj = new TipoDocumentoEquivalenteVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoDocumentoEquivalenteVO obj) throws Exception {
        final String sql = "INSERT INTO TipoDocumentoEquivalente( tipoDocumento, tipoDocumentoEquivalente ) VALUES ( ?, ? )";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getTipoDocumento(), obj.getTipoDocumentoEquivalente().getCodigo()});
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoDocumentoEquivalenteVO obj) throws Exception {
        final String sql = "UPDATE TipoDocumentoEquivalente set  WHERE ((tipoDocumento = ?) and (tipoDocumentoEquivalente = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getTipoDocumento().intValue());
                sqlAlterar.setInt(2, obj.getTipoDocumentoEquivalente().getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TipoDocumentoEquivalenteVO obj) throws Exception {
        TipoDocumentoEquivalente.excluir(getIdEntidade());
        String sql = "DELETE FROM TipoDocumentoEquivalente WHERE ((tipoDocumento = ?) and (tipoDocumentoEquivalente = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getTipoDocumento(), obj.getTipoDocumentoEquivalente()});
    }

    public List consultarPorNomeTipoDocumentoEquivalente(String valorConsulta, boolean controleAcesso) throws Exception {
        String sqlStr = "SELECT TipoDocumentoEquivalente.* FROM TipoDocumentoEquivalente, TipoDocumento WHERE TipoDocumentoEquivalente.tipoDocumentoEquivalente = TipoDocumento.codigo and TipoDocumento.nome like('" + valorConsulta + "%') ORDER BY TipoDocumento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>TipoDocumentoEquivalenteVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>TipoDocumentoEquivalenteVO</code>.
     *
     * @return O objeto da classe <code>TipoDocumentoEquivalenteVO</code> com os dados devidamente montados.
     */
    public  TipoDocumentoEquivalenteVO montarDados(SqlRowSet dadosSQL) throws Exception {
        TipoDocumentoEquivalenteVO obj = new TipoDocumentoEquivalenteVO();
        obj.setTipoDocumento(new Integer(dadosSQL.getInt("tipoDocumento")));
        obj.getTipoDocumentoEquivalente().setCodigo(new Integer(dadosSQL.getInt("tipoDocumentoEquivalente")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosTipoDocumentoEquivalente(obj);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>TipoDocumentoEquivalenteVO</code> relacionado ao objeto
     * <code>TipoDocumentoEquivalenteVO</code>. Faz uso da chave primária da classe <code>TipoDocumentoEquivalenteVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosTipoDocumentoEquivalente(TipoDocumentoEquivalenteVO obj) throws Exception {
        if (obj.getTipoDocumentoEquivalente().getCodigo().intValue() == 0) {
            obj.setTipoDocumentoEquivalente(new TipoDocumentoVO());
            return;
        }
        obj.setTipoDocumentoEquivalente(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimariaUnico(obj.getTipoDocumentoEquivalente().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirTipoDocumentoEquivalentes(Integer tipoDocumento) throws Exception {
        TipoDocumentoEquivalente.excluir(getIdEntidade());
        String sql = "DELETE FROM TipoDocumentoEquivalente WHERE (tipoDocumento = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{tipoDocumento});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarTipoDocumentoEquivalentes(Integer tipoDocumento, List objetos) throws Exception {
        excluirTipoDocumentoEquivalentes(tipoDocumento);
        incluirTipoDocumentoEquivalentes(tipoDocumento, objetos);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirTipoDocumentoEquivalentes(Integer tipoDocumentoPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            TipoDocumentoEquivalenteVO obj = (TipoDocumentoEquivalenteVO) e.next();
            obj.setTipoDocumento(tipoDocumentoPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>TipoDocumentoEquivalenteVO</code> relacionados a um objeto da classe
     * <code>academico.Curso</code>.
     *
     * @param tipoDocumento
     *            Atributo de <code>academico.Curso</code> a ser utilizado para localizar os objetos da classe
     *            <code>TipoDocumentoEquivalenteVO</code>.
     * @return List Contendo todos os objetos da classe <code>TipoDocumentoEquivalenteVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarTipoDocumentoEquivalentes(Integer tipoDocumento, boolean controleAcesso) throws Exception {
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM TipoDocumentoEquivalente WHERE tipoDocumento = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{tipoDocumento});
        while (resultado.next()) {
            objetos.add(montarDados(resultado));
        }
        return objetos;
    }

    public TipoDocumentoEquivalenteVO consultarPorChavePrimaria(Integer tipoDocumentoPrm, Integer tipoDocumentoEquivalentePrm, boolean controleAcesso) throws Exception {
        String sql = "SELECT * FROM TipoDocumentoEquivalente WHERE tipoDocumento = ? and tipoDocumentoEquivalente = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{tipoDocumentoPrm, tipoDocumentoEquivalentePrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(Tipo Documento).");
        }
        return (montarDados(tabelaResultado));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TipoDocumentoEquivalente.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        TipoDocumentoEquivalente.idEntidade = idEntidade;
    }

}
