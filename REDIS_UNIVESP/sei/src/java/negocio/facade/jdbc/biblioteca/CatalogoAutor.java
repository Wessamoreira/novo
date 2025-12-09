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
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.CatalogoAutorInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CatalogoAutorVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CatalogoAutorVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see CatalogoAutorVO
 * @see ControleAcesso
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Scope("singleton")
@Repository
@Lazy
public class CatalogoAutor extends ControleAcesso implements CatalogoAutorInterfaceFacade {

	private static final long serialVersionUID = 7916124232269893581L;
	protected static String idEntidade;

    public CatalogoAutor() throws Exception {
        super();
        setIdEntidade("CatalogoAutor");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>CatalogoAutorVO</code>.
     */
    public CatalogoAutorVO novo() throws Exception {
        CatalogoAutor.incluir(getIdEntidade());
        CatalogoAutorVO obj = new CatalogoAutorVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CatalogoAutorVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CatalogoAutorVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getCatalogo().getCodigo() == 0) {
            throw new ConsistirException("O campo Catálogo deve ser informado.");
        }
        if (obj.getAutor().getCodigo() == 0) {
            throw new ConsistirException("O campo Autor deve ser informado.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>CatalogoAutorVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CatalogoAutorVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);

            final String sql = "INSERT INTO CatalogoAutor( autor, catalogo, tipoAutoria, siglaAutoria, ordemApresentacao ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getAutor().getCodigo());
                    sqlInserir.setInt(2, obj.getCatalogo().getCodigo());
                    sqlInserir.setString(3, obj.getTipoAutoria());
                    sqlInserir.setString(4, obj.getSiglaAutoria());
                    sqlInserir.setInt(5, obj.getOrdemApresentacao());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>CatalogoAutorVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CatalogoAutorVO obj) throws Exception {
        try {
            validarDados(obj);

            final String sql = "UPDATE CatalogoAutor set autor=?, catalogo=?, tipoAutoria=?, siglaAutoria=?, ordemApresentacao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getAutor().getCodigo());
                    sqlAlterar.setInt(2, obj.getCatalogo().getCodigo());
                    sqlAlterar.setString(3, obj.getTipoAutoria());
                    sqlAlterar.setString(4, obj.getSiglaAutoria());
                    sqlAlterar.setInt(5, obj.getOrdemApresentacao());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>CatalogoAutorVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CatalogoAutorVO obj) throws Exception {
        try {
            CatalogoAutor.excluir(getIdEntidade());
            String sql = "DELETE FROM CatalogoAutor WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCatalogoAutorCatalogos(Integer catalogo) throws Exception {
        String sql = "DELETE FROM CatalogoAutor WHERE (catalogo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{catalogo});
    }

    /**
     * Responsável por realizar uma consulta de <code>CatalogoAutor</code>
     * através do valor do atributo <code>Integer catalogo</code>. Retorna os
     * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>CatalogoAutorVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCatalogo(Integer catalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CatalogoAutor WHERE catalogo = " + catalogo + " ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>CatalogoAutor</code>
     * através do valor do atributo <code>Integer autor</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>CatalogoAutorVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoAutor(Integer codigoAutor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CatalogoAutor WHERE autor = " + codigoAutor + " ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public synchronized CatalogoAutorVO consultarPorCodigoAutorRegistroUnico(Integer codigoAutor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CatalogoAutor WHERE autor = " + codigoAutor + " LIMIT 1 ";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!rs.next()) {
        	return new CatalogoAutorVO();
        }
        return montarDados(rs, nivelMontarDados, usuario);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>CatalogoAutorVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>CatalogoAutorVO</code>.
     *
     * @return O objeto da classe <code>CatalogoAutorVO</code> com os dados
     *         devidamente montados.
     */
    public static CatalogoAutorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CatalogoAutorVO obj = new CatalogoAutorVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getAutor().setCodigo(dadosSQL.getInt("autor"));
        obj.setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(dadosSQL.getInt("catalogo"), nivelMontarDados, 0, usuario));
        obj.setTipoAutoria(dadosSQL.getString("tipoAutoria"));
        obj.setSiglaAutoria(dadosSQL.getString("siglaAutoria"));
        obj.setOrdemApresentacao(dadosSQL.getInt("ordemApresentacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosAutor(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosAutor(CatalogoAutorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAutor().getCodigo().intValue() == 0) {
            obj.setAutor(new AutorVO());
            return;
        }
        obj.setAutor(getFacadeFactory().getAutorFacade().consultarPorChavePrimaria(obj.getAutor().getCodigo(),
                nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>CatalogoAutorVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public CatalogoAutorVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CatalogoAutor WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaCatalogoAutorPorCodigoCatalogo(CatalogoVO catalogo, List<CatalogoAutorVO> objetos)
            throws Exception {
        String str = "DELETE FROM catalogoautor WHERE catalogo = " + catalogo.getCodigo();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CatalogoAutorVO objeto = (CatalogoAutorVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);

        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CatalogoAutorVO objeto = (CatalogoAutorVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.setCatalogo(catalogo);
                incluir(objeto, new UsuarioVO());
            } else {
                objeto.setCatalogo(catalogo);
                alterar(objeto);
            }
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return CatalogoAutor.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CatalogoAutor.idEntidade = idEntidade;
    }

    public List<CatalogoAutorVO> consultaRapidaNivelComboBoxPorCodigoCatalogo(Integer catalogo, UsuarioVO usuario) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT catalogoAutor.codigo, catalogoAutor.catalogo, catalogoAutor.tipoAutoria, catalogoAutor.siglaAutoria, catalogoAutor.ordemApresentacao, ");
        sb.append(" autor.codigo AS \"autor.codigo\", autor.nome AS \"autor.nome\" ");
        sb.append(" FROM catalogoautor ");
        sb.append(" INNER JOIN autor ON autor.codigo = catalogoAutor.autor ");
        sb.append(" where catalogoAutor.catalogo = ");
        sb.append(catalogo);
        sb.append(" order by catalogoAutor.ordemApresentacao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            CatalogoAutorVO obj = new CatalogoAutorVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.getCatalogo().setCodigo(tabelaResultado.getInt("catalogo"));
            obj.setTipoAutoria(tabelaResultado.getString("tipoAutoria"));
            obj.setSiglaAutoria(tabelaResultado.getString("siglaAutoria"));
            obj.setOrdemApresentacao(tabelaResultado.getInt("ordemApresentacao"));
            obj.getAutor().setCodigo(tabelaResultado.getInt("autor.codigo"));
            obj.getAutor().setNome(tabelaResultado.getString("autor.nome"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }
    
}
