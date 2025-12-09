package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FraseInspiracaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FraseInspiracaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FraseInspiracaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FraseInspiracaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class FraseInspiracao extends ControleAcesso implements FraseInspiracaoInterfaceFacade {

    protected static String idEntidade;

    public FraseInspiracao() throws Exception {
        super();
        setIdEntidade("FraseInspiracao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FraseInspiracaoVO</code>.
     */
    public FraseInspiracaoVO novo() throws Exception {
        FraseInspiracao.incluir(getIdEntidade());
        FraseInspiracaoVO obj = new FraseInspiracaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FraseInspiracaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FraseInspiracaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FraseInspiracaoVO obj, boolean controleAcesso,UsuarioVO usuario) throws Exception {
        try {
            FraseInspiracaoVO.validarDados(obj);
            FraseInspiracao.incluir(getIdEntidade(), controleAcesso,usuario);
            final String sql = "INSERT INTO FraseInspiracao( frase, autor, quantidadeExibicoes, dataUltimaExibicao, responsavelCadastro) VALUES ( ?, ?, ?, ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getFrase());
                    sqlInserir.setString(2, obj.getAutor());
                    sqlInserir.setInt(3, obj.getQuantidadeExibicoes());
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataUltimaExibicao()));
                    if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getResponsavelCadastro().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FraseInspiracaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FraseInspiracaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FraseInspiracaoVO obj) throws Exception {
        try {
            FraseInspiracaoVO.validarDados(obj);
            FraseInspiracao.alterar(getIdEntidade());
            final String sql = "UPDATE FraseInspiracao set frase=?, autor=?, quantidadeExibicoes=?, dataUltimaExibicao=?, responsavelCadastro=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getFrase());
                    sqlAlterar.setString(2, obj.getAutor());
                    sqlAlterar.setInt(3, obj.getQuantidadeExibicoes());
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataUltimaExibicao()));
                    if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getResponsavelCadastro().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }

                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FraseInspiracaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FraseInspiracaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(FraseInspiracaoVO obj) throws Exception {
        try {
            FraseInspiracao.excluir(getIdEntidade());
            String sql = "DELETE FROM FraseInspiracao WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Cargo</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FraseInspiracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorFrase(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FraseInspiracao WHERE lower (frase) like('" + valorConsulta.toLowerCase() + "%') ORDER BY frase";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public FraseInspiracaoVO consultarFraseRandom(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        try {
            ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
            String sqlStr = "SELECT * FROM FraseInspiracao WHERE lower (frase) like('" + valorConsulta.toLowerCase() + "%') ORDER BY frase";
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            List frases = montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
            if (!frases.isEmpty()) {
                int pos = new Random().nextInt(frases.size());
                return (FraseInspiracaoVO) frases.get(pos);
            }
            return new FraseInspiracaoVO();
        } catch (Exception e) {
            return new FraseInspiracaoVO();
        }
    }

    public List consultarPorAutor(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FraseInspiracao WHERE lower (autor) like('" + valorConsulta.toLowerCase() + "%') ORDER BY autor";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Cargo</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FraseInspiracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FraseInspiracao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FraseInspiracaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FraseInspiracaoVO</code>.
     * @return  O objeto da classe <code>FraseInspiracaoVO</code> com os dados devidamente montados.
     */
    public static FraseInspiracaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        FraseInspiracaoVO obj = new FraseInspiracaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setFrase(dadosSQL.getString("frase"));
        obj.setAutor(dadosSQL.getString("autor"));
        obj.setDataUltimaExibicao(dadosSQL.getDate("dataUltimaExibicao"));
        obj.setQuantidadeExibicoes(dadosSQL.getInt("quantidadeExibicoes"));
        obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public static void montarDadosResponsavel(FraseInspiracaoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
            obj.setResponsavelCadastro(new UsuarioVO());
            return;
        }
        obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FraseInspiracaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FraseInspiracaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM FraseInspiracao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(FRASE INSPIRAÇÃO).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    public static String getIdEntidade() {
        return FraseInspiracao.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        FraseInspiracao.idEntidade = idEntidade;
    }
}
