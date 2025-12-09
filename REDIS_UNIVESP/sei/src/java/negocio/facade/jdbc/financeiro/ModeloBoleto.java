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
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ModeloBoletoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ModeloBoletoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ModeloBoletoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ModeloBoletoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ModeloBoleto extends ControleAcesso implements ModeloBoletoInterfaceFacade {

    protected static String idEntidade;

    public ModeloBoleto() throws Exception {
        super();
        setIdEntidade("ModeloBoleto");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ModeloBoletoVO</code>.
     */
    public ModeloBoletoVO novo() throws Exception {
        ModeloBoleto.incluir(getIdEntidade());
        ModeloBoletoVO obj = new ModeloBoletoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ModeloBoletoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ModeloBoletoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ModeloBoletoVO obj,UsuarioVO usuario) throws Exception {
        try {
            ModeloBoletoVO.validarDados(obj);
            ModeloBoleto.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO ModeloBoleto( nome, observacoesGerais2, observacoesGerais1, localPagamento, extensaoImagem, apenasDescontoInstrucaoBoleto, "
            		+ "utilizarDescricaoDescontoPersonalizado, textoTopo, instrucao1, instrucao2, instrucao3, instrucao4, instrucao5, instrucao6, textoRodape, textoTopoInferior, instrucao1Inferior, instrucao2Inferior, instrucao3Inferior, instrucao4Inferior, instrucao5Inferior, instrucao6Inferior, textoRodapeInferior, ocultarCodBarraLinhaDigitavel"
            		+ ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getObservacoesGerais2());
                    sqlInserir.setString(3, obj.getObservacoesGerais1());
            
                    sqlInserir.setString(4, obj.getLocalPagamento());
                    sqlInserir.setString(5, obj.getExtensaoImagem());
                    sqlInserir.setBoolean(6, obj.getApenasDescontoInstrucaoBoleto());
                    sqlInserir.setBoolean(7, obj.getUtilizarDescricaoDescontoPersonalizado());
                    sqlInserir.setString(8, obj.getTextoTopo());
                    sqlInserir.setString(9, obj.getInstrucao1());
                    sqlInserir.setString(10, obj.getInstrucao2());
                    sqlInserir.setString(11, obj.getInstrucao3());
                    sqlInserir.setString(12, obj.getInstrucao4());
                    sqlInserir.setString(13, obj.getInstrucao5());
                    sqlInserir.setString(14, obj.getInstrucao6());
                    sqlInserir.setString(15, obj.getTextoRodape());
                    sqlInserir.setString(16, obj.getTextoTopo());
                    sqlInserir.setString(17, obj.getInstrucao1());
                    sqlInserir.setString(18, obj.getInstrucao2());
                    sqlInserir.setString(19, obj.getInstrucao3());
                    sqlInserir.setString(20, obj.getInstrucao4());
                    sqlInserir.setString(21, obj.getInstrucao5());
                    sqlInserir.setString(22, obj.getInstrucao6());
                    sqlInserir.setString(23, obj.getTextoRodape());
                    sqlInserir.setBoolean(24, obj.getOcultarCodBarraLinhaDigitavel());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ModeloBoletoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ModeloBoletoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ModeloBoletoVO obj,UsuarioVO usuario) throws Exception {
        try {
            ModeloBoletoVO.validarDados(obj);
            ModeloBoleto.alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE ModeloBoleto set nome=?, observacoesGerais2=?, observacoesGerais1=?, localPagamento=?, extensaoImagem=?, apenasDescontoInstrucaoBoleto=?,"
            		+ " utilizarDescricaoDescontoPersonalizado=?, textoTopo=?, instrucao1=?, instrucao2=?, instrucao3=?, instrucao4=?, instrucao5=?, instrucao6=?, textoRodape=?, textoTopoInferior=?, instrucao1Inferior=?, instrucao2Inferior=?, instrucao3Inferior=?, instrucao4Inferior=?, instrucao5Inferior=?, instrucao6Inferior=?, textoRodapeInferior=? , ocultarCodBarraLinhaDigitavel=? "
            		+ "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getObservacoesGerais2());
                    sqlAlterar.setString(3, obj.getObservacoesGerais1());
//                    sqlAlterar.setBytes(4, obj.getImagem());
                    sqlAlterar.setString(4, obj.getLocalPagamento());
                    sqlAlterar.setString(5, obj.getExtensaoImagem());
                    sqlAlterar.setBoolean(6, obj.getApenasDescontoInstrucaoBoleto());
                    sqlAlterar.setBoolean(7, obj.getUtilizarDescricaoDescontoPersonalizado());
                    sqlAlterar.setString(8, obj.getTextoTopo());
                    sqlAlterar.setString(9, obj.getInstrucao1());
                    sqlAlterar.setString(10, obj.getInstrucao2());
                    sqlAlterar.setString(11, obj.getInstrucao3());
                    sqlAlterar.setString(12, obj.getInstrucao4());
                    sqlAlterar.setString(13, obj.getInstrucao5());
                    sqlAlterar.setString(14, obj.getInstrucao6());
                    sqlAlterar.setString(15, obj.getTextoRodape());
                    sqlAlterar.setString(16, obj.getTextoTopoInferior());
                    sqlAlterar.setString(17, obj.getInstrucao1Inferior());
                    sqlAlterar.setString(18, obj.getInstrucao2Inferior());
                    sqlAlterar.setString(19, obj.getInstrucao3Inferior());
                    sqlAlterar.setString(20, obj.getInstrucao4Inferior());
                    sqlAlterar.setString(21, obj.getInstrucao5Inferior());
                    sqlAlterar.setString(22, obj.getInstrucao6Inferior());
                    sqlAlterar.setString(23, obj.getTextoRodapeInferior());
                    sqlAlterar.setBoolean(24, obj.getOcultarCodBarraLinhaDigitavel());
                    sqlAlterar.setInt(25, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ModeloBoletoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ModeloBoletoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ModeloBoletoVO obj,UsuarioVO usuario) throws Exception {
        try {
            ModeloBoleto.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ModeloBoleto WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } 
    }

    /**
     * Responsável por realizar uma consulta de <code>ModeloBoleto</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ModeloBoletoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ModeloBoleto WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>ModeloBoleto</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ModeloBoletoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ModeloBoleto WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List listarTodos(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM modeloBoleto";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(resultado, nivelMontarDados);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ModeloBoletoVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>ModeloBoletoVO</code>.
     * @return  O objeto da classe <code>ModeloBoletoVO</code> com os dados devidamente montados.
     */
    public static ModeloBoletoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ModeloBoletoVO obj = new ModeloBoletoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setObservacoesGerais2(dadosSQL.getString("observacoesGerais2"));
        obj.setObservacoesGerais1(dadosSQL.getString("observacoesGerais1"));
        //obj.setImagem((byte[])dadosSQL.getObject("imagem"));
        obj.setLocalPagamento(dadosSQL.getString("localPagamento"));
        obj.setExtensaoImagem(dadosSQL.getString("extensaoImagem"));
        obj.setApenasDescontoInstrucaoBoleto(dadosSQL.getBoolean("apenasDescontoInstrucaoBoleto"));
        obj.setUtilizarDescricaoDescontoPersonalizado(dadosSQL.getBoolean("utilizarDescricaoDescontoPersonalizado"));
        obj.setTextoTopo(dadosSQL.getString("textoTopo"));
        obj.setInstrucao1(dadosSQL.getString("instrucao1"));
        obj.setInstrucao2(dadosSQL.getString("instrucao2"));
        obj.setInstrucao3(dadosSQL.getString("instrucao3"));
        obj.setInstrucao4(dadosSQL.getString("instrucao4"));
        obj.setInstrucao5(dadosSQL.getString("instrucao5"));
        obj.setInstrucao6(dadosSQL.getString("instrucao6"));
        obj.setTextoRodape(dadosSQL.getString("textoRodape"));
        obj.setTextoTopoInferior(dadosSQL.getString("textoTopoInferior"));
        obj.setInstrucao1Inferior(dadosSQL.getString("instrucao1Inferior"));
        obj.setInstrucao2Inferior(dadosSQL.getString("instrucao2Inferior"));
        obj.setInstrucao3Inferior(dadosSQL.getString("instrucao3Inferior"));
        obj.setInstrucao4Inferior(dadosSQL.getString("instrucao4Inferior"));
        obj.setInstrucao5Inferior(dadosSQL.getString("instrucao5Inferior"));
        obj.setInstrucao6Inferior(dadosSQL.getString("instrucao6Inferior"));
        obj.setTextoRodapeInferior(dadosSQL.getString("textoRodapeInferior"));
        obj.setOcultarCodBarraLinhaDigitavel(dadosSQL.getBoolean("ocultarCodBarraLinhaDigitavel"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ModeloBoletoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ModeloBoletoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ModeloBoleto WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ModeloBoleto ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ModeloBoleto.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ModeloBoleto.idEntidade = idEntidade;
    }
}
