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
import negocio.comuns.biblioteca.BibliotecaExternaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.BibliotecaExternaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>BibliotecaExternaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>BibliotecaExternaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see BibliotecaExternaVO
 * @see ControleAcesso
 * @see ConfiguracaoBiblioteca
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class BibliotecaExterna extends ControleAcesso implements BibliotecaExternaInterfaceFacade {

    protected static String idEntidade;

    public BibliotecaExterna() throws Exception {
        super();
    }

    public BibliotecaExternaVO novo() throws Exception {
        BibliotecaExterna.incluir(getIdEntidade());
        BibliotecaExternaVO obj = new BibliotecaExternaVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final BibliotecaExternaVO obj, UsuarioVO usuarioVO) throws Exception {
        BibliotecaExternaVO.validarDados(obj);
        final String sql = "INSERT INTO bibliotecaExterna( nome, configuracaoBiblioteca, url ) VALUES ( ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setString(1, obj.getNome());
                sqlInserir.setInt(2, obj.getConfiguracaoBiblioteca());
                sqlInserir.setString(3, obj.getUrl());

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
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final BibliotecaExternaVO obj, UsuarioVO usuarioVO) throws Exception {
        BibliotecaExternaVO.validarDados(obj);
        final String sql = "UPDATE bibliotecaExterna set nome=?, configuracaoBiblioteca=?, url=? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getNome());
                sqlAlterar.setInt(2, obj.getConfiguracaoBiblioteca());
                sqlAlterar.setString(3, obj.getUrl());
                sqlAlterar.setInt(4, obj.getCodigo());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(BibliotecaExternaVO obj, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM bibliotecaExterna WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List consultarPorNomeConfiguracaoBiblioteca(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT bibliotecaExterna.* FROM bibliotecaExterna ");
        sqlStr.append("Inner join configuracaoBiblioteca cb on cb.codigo = bibliotecaExterna.configuracaobiblioteca ");
        sqlStr.append("WHERE upper(cb.nome) like('").append(valorConsulta.toUpperCase()).append("%') ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorCodigoConfiguracaoBiblioteca(Integer valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT bibliotecaExterna.* FROM bibliotecaExterna ");
        sqlStr.append("Inner join configuracaoBiblioteca cb on cb.codigo = bibliotecaExterna.configuracaobiblioteca ");
        sqlStr.append("WHERE cb.codigo = ").append(valorConsulta).append(" ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorUrl(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT bibliotecaExterna.* FROM bibliotecaExterna "
                + "WHERE upper(bibliotecaExterna.url) like('" + valorConsulta.toUpperCase() + "%') ORDER BY linkBiblioteca.url";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    public List consultarPorNome(String valorConsulta, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT bibliotecaExterna.* FROM bibliotecaExterna "
                + "WHERE upper(bibliotecaExterna.nome) like('" + valorConsulta.toUpperCase() + "%') ORDER BY linkBiblioteca.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>BibliotecaExternaVO</code> resultantes da consulta.
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
     * objeto da classe <code>BibliotecaExternaVO</code>.
     *
     * @return O objeto da classe <code>BibliotecaExternaVO</code> com os dados devidamente montados.
     */
    public static BibliotecaExternaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        BibliotecaExternaVO obj = new BibliotecaExternaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setConfiguracaoBiblioteca(dadosSQL.getInt("configuracaoBiblioteca"));
        obj.setUrl(dadosSQL.getString("url"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirBibliotecasExternas(Integer configuracaoBiblioteca, UsuarioVO usuarioVO) throws Exception {
        String sql = "DELETE FROM BibliotecaExterna WHERE (configuracaoBiblioteca = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sql, new Object[]{configuracaoBiblioteca});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarBibliotecasExternas(Integer configuracaoBiblioteca, List<BibliotecaExternaVO> objetos, UsuarioVO usuarioVO) throws Exception {
    	String str = "DELETE FROM BibliotecaExterna WHERE configuracaoBiblioteca = " + configuracaoBiblioteca;
        Iterator<BibliotecaExternaVO> i = objetos.iterator();
        while (i.hasNext()) {
        	BibliotecaExternaVO objeto = (BibliotecaExternaVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

        Iterator<BibliotecaExternaVO> e = objetos.iterator();
        while (e.hasNext()) {
        	BibliotecaExternaVO obj = (BibliotecaExternaVO) e.next();
            if (obj.getCodigo().equals(0)) {
            	obj.setConfiguracaoBiblioteca(configuracaoBiblioteca);
            	incluir(obj, usuarioVO);
            } else {
            	obj.setConfiguracaoBiblioteca(configuracaoBiblioteca);
            	alterar(obj, usuarioVO);
            }
        }
        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirBibliotecaExternas(Integer configuracaoBiblioteca, List<BibliotecaExternaVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<BibliotecaExternaVO> e = objetos.iterator();
        while (e.hasNext()) {
            BibliotecaExternaVO obj = (BibliotecaExternaVO) e.next();
            obj.setConfiguracaoBiblioteca(configuracaoBiblioteca);
            incluir(obj, usuario);
        }
    }

    public BibliotecaExternaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM BibliotecaExterna WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return BibliotecaExterna.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        BibliotecaExterna.idEntidade = idEntidade;
    }
}
