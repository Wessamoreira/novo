package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroTrailerInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RegistroTrailerVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RegistroTrailerVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RegistroTrailerVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class RegistroTrailer extends ControleAcesso implements RegistroTrailerInterfaceFacade {

    protected static String idEntidade;

    public RegistroTrailer() throws Exception {
        super();
        setIdEntidade("RegistroArquivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RegistroTrailerVO</code>.
     */
    public RegistroTrailerVO novo() throws Exception {
        RegistroTrailerVO obj = new RegistroTrailerVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroTrailerVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RegistroTrailerVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroTrailerVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailer.incluir(getIdEntidade());
            RegistroTrailerVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroTrailer( registroArquivo, codigoBanco, numeroLote, tipoRegistro, quantidadeLote, quantidadeRegistro, quantidadeContas, identificacaoRetorno, identificacaoTipoRegistro, valorTotalRegistros, numeroAvisoBancario, quantidadeTitulosEmCobranca, valorTitulosEmCobranca, quantidadeRegistrosConfirmacaoEntrada, valorRegistrosConfirmacaoEntrada, quantidadeRegistrosLiquidacao, valorRegistroLiquidacao, valorRegistrosOcorrencia6, quantidadeRegistrosTitulosBaixados, valorRegistrosTitulosBaixados, quantidadeRegistrosAbatimentosCancelados, valorRegistrosAbatimentosCancelados, quantidadeRegistrosVencimentoAlterado, valorRegistrosVencimentoAlterado, quantidadeRegistrosAbatimentosConcedidos, valorRegistrosAbatimentosConcedidos, quantidadeRegistrosConfirmacaoProtesto, valorRegistrosConfirmacaoProtesto, valorTotalRateiosEfetuados, quantidadeTotalRateiosEfetuados, numeroSequencialRegistro ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getRegistroArquivo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getCodigoBanco());
                    sqlInserir.setInt(3, obj.getNumeroLote().intValue());
                    sqlInserir.setInt(4, obj.getTipoRegistro().intValue());
                    sqlInserir.setInt(5, obj.getQuantidadeLote().intValue());
                    sqlInserir.setInt(6, obj.getQuantidadeRegistro().intValue());
                    sqlInserir.setInt(7, obj.getQuantidadeContas().intValue());
                    sqlInserir.setInt(8, obj.getIdentificacaoRetorno().intValue());
                    sqlInserir.setInt(9, obj.getIdentificacaoTipoRegistro().intValue());
                    sqlInserir.setDouble(10, obj.getValorTotalRegistros().doubleValue());
                    sqlInserir.setInt(11, obj.getNumeroAvisoBancario().intValue());
                    sqlInserir.setInt(12, obj.getQuantidadeTitulosEmCobranca().intValue());
                    sqlInserir.setDouble(13, obj.getValorTitulosEmCobranca().doubleValue());
                    sqlInserir.setInt(14, obj.getQuantidadeRegistrosConfirmacaoEntrada().intValue());
                    sqlInserir.setDouble(15, obj.getValorRegistrosConfirmacaoEntrada().doubleValue());
                    sqlInserir.setInt(16, obj.getQuantidadeRegistrosLiquidacao().intValue());
                    sqlInserir.setDouble(17, obj.getValorRegistroLiquidacao().doubleValue());
                    sqlInserir.setDouble(18, obj.getValorRegistrosOcorrencia6().doubleValue());
                    sqlInserir.setInt(19, obj.getQuantidadeRegistrosTitulosBaixados().intValue());
                    sqlInserir.setDouble(20, obj.getValorRegistrosTitulosBaixados().doubleValue());
                    sqlInserir.setInt(21, obj.getQuantidadeRegistrosAbatimentosCancelados().intValue());
                    sqlInserir.setDouble(22, obj.getValorRegistrosAbatimentosCancelados().doubleValue());
                    sqlInserir.setInt(23, obj.getQuantidadeRegistrosVencimentoAlterado().intValue());
                    sqlInserir.setDouble(24, obj.getValorRegistrosVencimentoAlterado().doubleValue());
                    sqlInserir.setInt(25, obj.getQuantidadeRegistrosAbatimentosConcedidos().intValue());
                    sqlInserir.setDouble(26, obj.getValorRegistrosAbatimentosConcedidos().doubleValue());
                    sqlInserir.setInt(27, obj.getQuantidadeRegistrosConfirmacaoProtesto().intValue());
                    sqlInserir.setDouble(28, obj.getValorRegistrosConfirmacaoProtesto().doubleValue());
                    sqlInserir.setDouble(29, obj.getValorTotalRateiosEfetuados().doubleValue());
                    sqlInserir.setInt(30, obj.getQuantidadeTotalRateiosEfetuados().intValue());
                    sqlInserir.setInt(31, obj.getNumeroSequencialRegistro().intValue());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroTrailerVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroTrailerVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroTrailerVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailer.alterar(getIdEntidade());
            RegistroTrailerVO.validarDados(obj);
            final String sql = "UPDATE RegistroTrailer set registroArquivo=?, codigoBanco=?, numeroLote=?, tipoRegistro=?, quantidadeLote=?, quantidadeRegistro=?, quantidadeContas=?, identificacaoRetorno=?, identificacaoTipoRegistro=?, valorTotalRegistros=?, numeroAvisoBancario=?, quantidadeTitulosEmCobranca=?, valorTitulosEmCobranca=?, quantidadeRegistrosConfirmacaoEntrada=?, valorRegistrosConfirmacaoEntrada=?, quantidadeRegistrosLiquidacao=?, valorRegistroLiquidacao=?, valorRegistrosOcorrencia6=?, quantidadeRegistrosTitulosBaixados=?, valorRegistrosTitulosBaixados=?, quantidadeRegistrosAbatimentosCancelados=?, valorRegistrosAbatimentosCancelados=?, quantidadeRegistrosVencimentoAlterado=?, valorRegistrosVencimentoAlterado=?, quantidadeRegistrosAbatimentosConcedidos=?, valorRegistrosAbatimentosConcedidos=?, quantidadeRegistrosConfirmacaoProtesto=?, valorRegistrosConfirmacaoProtesto=?, valorTotalRateiosEfetuados=?, quantidadeTotalRateiosEfetuados=?, numeroSequencialRegistro=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getRegistroArquivo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getRegistroArquivo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getCodigoBanco());
                    sqlAlterar.setInt(3, obj.getNumeroLote().intValue());
                    sqlAlterar.setInt(4, obj.getTipoRegistro().intValue());
                    sqlAlterar.setInt(5, obj.getQuantidadeLote().intValue());
                    sqlAlterar.setInt(6, obj.getQuantidadeRegistro().intValue());
                    sqlAlterar.setInt(7, obj.getQuantidadeContas().intValue());
                    sqlAlterar.setInt(8, obj.getIdentificacaoRetorno().intValue());
                    sqlAlterar.setInt(9, obj.getIdentificacaoTipoRegistro().intValue());
                    sqlAlterar.setDouble(10, obj.getValorTotalRegistros().doubleValue());
                    sqlAlterar.setInt(11, obj.getNumeroAvisoBancario().intValue());
                    sqlAlterar.setInt(12, obj.getQuantidadeTitulosEmCobranca().intValue());
                    sqlAlterar.setDouble(13, obj.getValorTitulosEmCobranca().doubleValue());
                    sqlAlterar.setInt(14, obj.getQuantidadeRegistrosConfirmacaoEntrada().intValue());
                    sqlAlterar.setDouble(15, obj.getValorRegistrosConfirmacaoEntrada().doubleValue());
                    sqlAlterar.setInt(16, obj.getQuantidadeRegistrosLiquidacao().intValue());
                    sqlAlterar.setDouble(17, obj.getValorRegistroLiquidacao().doubleValue());
                    sqlAlterar.setDouble(18, obj.getValorRegistrosOcorrencia6().doubleValue());
                    sqlAlterar.setInt(19, obj.getQuantidadeRegistrosTitulosBaixados().intValue());
                    sqlAlterar.setDouble(20, obj.getValorRegistrosTitulosBaixados().doubleValue());
                    sqlAlterar.setInt(21, obj.getQuantidadeRegistrosAbatimentosCancelados().intValue());
                    sqlAlterar.setDouble(22, obj.getValorRegistrosAbatimentosCancelados().doubleValue());
                    sqlAlterar.setInt(23, obj.getQuantidadeRegistrosVencimentoAlterado().intValue());
                    sqlAlterar.setDouble(24, obj.getValorRegistrosVencimentoAlterado().doubleValue());
                    sqlAlterar.setInt(25, obj.getQuantidadeRegistrosAbatimentosConcedidos().intValue());
                    sqlAlterar.setDouble(26, obj.getValorRegistrosAbatimentosConcedidos().doubleValue());
                    sqlAlterar.setInt(27, obj.getQuantidadeRegistrosConfirmacaoProtesto().intValue());
                    sqlAlterar.setDouble(28, obj.getValorRegistrosConfirmacaoProtesto().doubleValue());
                    sqlAlterar.setDouble(29, obj.getValorTotalRateiosEfetuados().doubleValue());
                    sqlAlterar.setInt(30, obj.getQuantidadeTotalRateiosEfetuados().intValue());
                    sqlAlterar.setInt(31, obj.getNumeroSequencialRegistro().intValue());
                    sqlAlterar.setInt(32, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroTrailerVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroTrailerVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroTrailerVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroTrailer.excluir(getIdEntidade());
            String sql = "DELETE FROM RegistroTrailer WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroTrailer</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroTrailerVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroTrailer WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroTrailerVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>RegistroTrailerVO</code>.
     * @return  O objeto da classe <code>RegistroTrailerVO</code> com os dados devidamente montados.
     */
    public static RegistroTrailerVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RegistroTrailerVO obj = new RegistroTrailerVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setRegistroArquivo(new Integer(dadosSQL.getInt("registroArquivo")));
        obj.setCodigoBanco((dadosSQL.getString("codigoBanco")));
        obj.setNumeroLote(new Integer(dadosSQL.getInt("numeroLote")));
        obj.setTipoRegistro(new Integer(dadosSQL.getInt("tipoRegistro")));
        obj.setQuantidadeLote(new Integer(dadosSQL.getInt("quantidadeLote")));
        obj.setQuantidadeRegistro(new Integer(dadosSQL.getInt("quantidadeRegistro")));
        obj.setQuantidadeContas(new Integer(dadosSQL.getInt("quantidadeContas")));
        obj.setIdentificacaoRetorno(new Integer(dadosSQL.getInt("identificacaoRetorno")));
        obj.setIdentificacaoTipoRegistro(new Integer(dadosSQL.getInt("identificacaoTipoRegistro")));
        obj.setValorTotalRegistros(new Double(dadosSQL.getDouble("valorTotalRegistros")));
        obj.setNumeroAvisoBancario(new Integer(dadosSQL.getInt("numeroAvisoBancario")));
        obj.setQuantidadeTitulosEmCobranca(new Integer(dadosSQL.getInt("quantidadeTitulosEmCobranca")));
        obj.setValorTitulosEmCobranca(new Double(dadosSQL.getDouble("valorTitulosEmCobranca")));
        obj.setQuantidadeRegistrosConfirmacaoEntrada(new Integer(dadosSQL.getInt("quantidadeRegistrosConfirmacaoEntrada")));
        obj.setValorRegistrosConfirmacaoEntrada(new Double(dadosSQL.getDouble("valorRegistrosConfirmacaoEntrada")));
        obj.setQuantidadeRegistrosLiquidacao(new Integer(dadosSQL.getInt("quantidadeRegistrosLiquidacao")));
        obj.setValorRegistroLiquidacao(new Double(dadosSQL.getDouble("valorRegistroLiquidacao")));
        obj.setValorRegistrosOcorrencia6(new Double(dadosSQL.getDouble("valorRegistrosOcorrencia6")));
        obj.setQuantidadeRegistrosTitulosBaixados(new Integer(dadosSQL.getInt("quantidadeRegistrosTitulosBaixados")));
        obj.setValorRegistrosTitulosBaixados(new Double(dadosSQL.getDouble("valorRegistrosTitulosBaixados")));
        obj.setQuantidadeRegistrosAbatimentosCancelados(new Integer(dadosSQL.getInt("quantidadeRegistrosAbatimentosCancelados")));
        obj.setValorRegistrosAbatimentosCancelados(new Double(dadosSQL.getDouble("valorRegistrosAbatimentosCancelados")));
        obj.setQuantidadeRegistrosVencimentoAlterado(new Integer(dadosSQL.getInt("quantidadeRegistrosVencimentoAlterado")));
        obj.setValorRegistrosVencimentoAlterado(new Double(dadosSQL.getDouble("valorRegistrosVencimentoAlterado")));
        obj.setQuantidadeRegistrosAbatimentosConcedidos(new Integer(dadosSQL.getInt("quantidadeRegistrosAbatimentosConcedidos")));
        obj.setValorRegistrosAbatimentosConcedidos(new Double(dadosSQL.getDouble("valorRegistrosAbatimentosConcedidos")));
        obj.setQuantidadeRegistrosConfirmacaoProtesto(new Integer(dadosSQL.getInt("quantidadeRegistrosConfirmacaoProtesto")));
        obj.setValorRegistrosConfirmacaoProtesto(new Double(dadosSQL.getDouble("valorRegistrosConfirmacaoProtesto")));
        obj.setValorTotalRateiosEfetuados(new Double(dadosSQL.getDouble("valorTotalRateiosEfetuados")));
        obj.setQuantidadeTotalRateiosEfetuados(new Integer(dadosSQL.getInt("quantidadeTotalRateiosEfetuados")));
        obj.setNumeroSequencialRegistro(new Integer(dadosSQL.getInt("numeroSequencialRegistro")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroTrailerVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroTrailerVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroTrailer WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroTrailer ).");
        }
        return (montarDados(tabelaResultado,nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroTrailer.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroTrailer.idEntidade = idEntidade;
    }
}
