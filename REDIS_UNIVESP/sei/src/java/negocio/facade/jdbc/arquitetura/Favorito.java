package negocio.facade.jdbc.arquitetura;

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

import negocio.comuns.arquitetura.FavoritoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.FavoritoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>favoritoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>favoritoVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see favoritoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Favorito extends ControleAcesso implements FavoritoInterfaceFacade {

    protected static String idEntidade;

    public Favorito() throws Exception {
        super();
        setIdEntidade("Favorito");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>favoritoVO</code>.
     */
    public FavoritoVO novo() throws Exception {
        Favorito.incluir(getIdEntidade());
        FavoritoVO obj = new FavoritoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>favoritoVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>favoritoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FavoritoVO obj, final boolean verificarPermissao, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO Favorito( pagina, booleanMarcarFavoritar, scriptExecutar, usuario, propertMenu, tipoVisao, icone, removerControlador) VALUES ( ? , ? , ? , ? , ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getPagina());
                    sqlInserir.setString(2, obj.getBooleanMarcarFavoritar());
                    sqlInserir.setString(3, obj.getScriptExecutar());
                    sqlInserir.setInt(4, obj.getUsuario().intValue());
                    sqlInserir.setString(5, obj.getPropertMenu());
                    sqlInserir.setString(6, obj.getTipoVisao().name());
                    sqlInserir.setString(7, obj.getIcone());
                    sqlInserir.setString(8, obj.getRemoverControlador());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>favoritoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>favoritoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(FavoritoVO obj) throws Exception {
        try {
        	if(Uteis.isAtributoPreenchido(obj)){
        		String sql = "DELETE FROM Favorito WHERE codigo = ?";
        		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        	}else{
        		String sql = "DELETE FROM Favorito WHERE (pagina = ? and usuario = ? and tipoVisao = ?)";
        		getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getPagina(), obj.getUsuario().intValue(), obj.getTipoVisao().name()});
        	}
        } catch (Exception e) {
            throw e;
        }
    }

    public FavoritoVO consultarPorPagina(String valorConsulta, Integer codUsuario, TipoVisaoEnum tipoVisaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Favorito WHERE pagina ilike ? and tipoVisao = ? and usuario = ").append(codUsuario);
        sb.append(" ORDER BY pagina");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase()+"%", tipoVisaoEnum.name());
        if (!tabelaResultado.next()) {
            return new FavoritoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<FavoritoVO> consultarPorUsuario(Integer codUsuario, TipoVisaoEnum tipoVisaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Favorito where 1 = 1 ");
        if(Uteis.isAtributoPreenchido(codUsuario)){
        	sb.append("	and  usuario = ").append(codUsuario);
        }
        if(Uteis.isAtributoPreenchido(tipoVisaoEnum)){
        	sb.append("	and  tipoVisao = '").append(tipoVisaoEnum.name()).append("' ");
        }
        sb.append(" ORDER BY propertmenu ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>favoritoVO</code> resultantes da consulta.
     */
    public static List<FavoritoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<FavoritoVO> vetResultado = new ArrayList<FavoritoVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>favoritoVO</code>.
     *
     * @return O objeto da classe <code>favoritoVO</code> com os dados devidamente montados.
     */
    public static FavoritoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FavoritoVO obj = new FavoritoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setPagina(dadosSQL.getString("pagina"));
        obj.setBooleanMarcarFavoritar(dadosSQL.getString("booleanMarcarFavoritar"));
        obj.setScriptExecutar(dadosSQL.getString("scriptExecutar"));
        obj.setPropertMenu(dadosSQL.getString("propertMenu"));
        obj.setIcone(dadosSQL.getString("icone"));
        obj.setRemoverControlador(dadosSQL.getString("removerControlador"));
        obj.setUsuario(dadosSQL.getInt("usuario"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoVisao"))) {
        	obj.setTipoVisao(TipoVisaoEnum.valueOf(dadosSQL.getString("tipoVisao")));
        }
        obj.setNovoObj(false);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>favoritoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FavoritoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM Favorito WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados. (Favorito)");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Favorito.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Favorito.idEntidade = idEntidade;
    }
}
