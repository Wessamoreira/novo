/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

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
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.PercentualConfiguracaoRankingInterfaceFacade;

/**
 *
 * @author Paulo Taucci
 */
@Repository
@Scope("singleton")
@Lazy
public class PercentualConfiguracaoRanking extends ControleAcesso implements PercentualConfiguracaoRankingInterfaceFacade {

    protected static String idEntidade;

    public PercentualConfiguracaoRanking() throws Exception {
        super();
        setIdEntidade("PercentualConfiguracaoRanking");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ConfiguracaoRankingVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ConfiguracaoRankingVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            validarDados(obj);
            /**
             * @author Leonardo Riciolle 
             * Comentado 28/10/2014
             *  Classe Subordinada
             */
             // PercentualConfiguracaoRanking.incluir(getIdEntidade());
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO PercentualConfiguracaoRanking( posicao, percentual, configuracaoRanking, qtdePosicao ) VALUES ( ?, ?, ?, ?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getPosicao());
                    sqlInserir.setDouble(2, obj.getPercentual().doubleValue());
                    sqlInserir.setInt(3, obj.getConfiguracaoRanking().getCodigo().intValue());
                    sqlInserir.setInt(4, obj.getQtdePosicao().intValue());
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
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ConfiguracaoRankingVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ConfiguracaoRankingVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            validarDados(obj);
            /**
             * @author Leonardo Riciolle 
             * Comentado 28/10/2014
             *  Classe Subordinada
             */
            // PercentualConfiguracaoRanking.alterar(getIdEntidade());
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE PercentualConfiguracaoRanking set posicao=?, percentual=?, configuracaoRanking=?, qtdePosicao=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getPosicao());
                    sqlAlterar.setDouble(2, obj.getPercentual().doubleValue());
                    sqlAlterar.setInt(3, obj.getConfiguracaoRanking().getCodigo().intValue());
                    sqlAlterar.setInt(4, obj.getQtdePosicao().intValue());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ConfiguracaoRankingVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ConfiguracaoRankingVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 28/10/2014
        	  *  Classe Subordinada
        	  */
            // PercentualConfiguracaoRanking.excluir(getIdEntidade());
            String sql = "DELETE FROM PercentualConfiguracaoRanking WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param ConfiguracaoRankingVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(PercentualConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioLogado);
        } else {
            alterar(obj, usuarioLogado);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ConfiguracaoRankingVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(PercentualConfiguracaoRankingVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getPosicao() == null || obj.getPosicao().equals(0)) {
            throw new Exception(UteisJSF.internacionalizar("msg_PercentualConfiguracaoRanking_posicao"));
        }
        if (obj.getPercentual() == null || obj.getPercentual() < 0) {
            throw new Exception(UteisJSF.internacionalizar("msg_PercentualConfiguracaoRanking_percentual"));
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>ConfiguracaoRankingVO</code>.
     */
    public void validarUnicidade(List<PercentualConfiguracaoRankingVO> lista, PercentualConfiguracaoRankingVO obj) throws ConsistirException {
      
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(PercentualConfiguracaoRankingVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ConfiguracaoRankingVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PercentualConfiguracaoRankingVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PercentualConfiguracaoRanking WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ConfiguracaoRankingVO</code> resultantes da consulta.
     */
    public static List<PercentualConfiguracaoRankingVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PercentualConfiguracaoRankingVO> vetResultado = new ArrayList<PercentualConfiguracaoRankingVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ConfiguracaoRankingVO</code>.
     * @return  O objeto da classe <code>ConfiguracaoRankingVO</code> com os dados devidamente montados.
     */
    public static PercentualConfiguracaoRankingVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PercentualConfiguracaoRankingVO obj = new PercentualConfiguracaoRankingVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setPosicao(dadosSQL.getInt("posicao"));
        obj.setPercentual(dadosSQL.getDouble("percentual"));
        obj.setQtdePosicao(dadosSQL.getInt("qtdePosicao"));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            obj.setConfiguracaoRanking(new ConfiguracaoRankingVO());
            obj.getConfiguracaoRanking().setCodigo(dadosSQL.getInt("configuracaoRanking"));
            return obj;
        }

        obj.setConfiguracaoRanking(getFacadeFactory().getConfiguracaoRankingFacade().consultarPorChavePrimaria(dadosSQL.getInt("configuracaoRanking"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ConfiguracaoRankingVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PercentualConfiguracaoRankingVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PercentualConfiguracaoRanking WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<ConfiguracaoRankingVO> consultarUnicidade(ConfiguracaoRankingVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PercentualConfiguracaoRanking.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PercentualConfiguracaoRanking.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>PercentualConfiguracaoRankingVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>PercentualConfiguracaoRankingVO</code>.
     * @param <code>configuracaoRanking</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM PercentualConfiguracaoRanking WHERE (configuracaoRanking = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{configuracaoRanking});
    }

    /**
     * Operação responsável por incluir objetos da <code>PercentualConfiguracaoRankingVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.ConfiguracaoRanking</code> através do atributo de vínculo.
     * @param percentualVOs List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, List percentualVOs) throws Exception {
        Iterator e = percentualVOs.iterator();
        while (e.hasNext()) {
            PercentualConfiguracaoRankingVO obj = (PercentualConfiguracaoRankingVO) e.next();
            obj.getConfiguracaoRanking().setCodigo(configuracaoRanking);
            incluir(obj, null);
        }
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PercentualConfiguracaoRankingVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPercentualConfiguracaoRankingVOs</code> e <code>incluirPercentualConfiguracaoRankingVOs</code> disponíveis na classe <code>PercentualConfiguracaoRanking</code>.
     * @param percentualVOs  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPercentualConfiguracaoRankingVOs(Integer configuracaoRanking, List percentualVOs, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM PercentualConfiguracaoRanking WHERE configuracaoRanking = ?";
        Iterator i = percentualVOs.iterator();
        while (i.hasNext()) {
            PercentualConfiguracaoRankingVO objeto = (PercentualConfiguracaoRankingVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{configuracaoRanking});
        Iterator e = percentualVOs.iterator();
        while (e.hasNext()) {
            PercentualConfiguracaoRankingVO objeto = (PercentualConfiguracaoRankingVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getConfiguracaoRanking().setCodigo(configuracaoRanking);
                incluir(objeto, null);
            } else {
                alterar(objeto, null);
            }
        }
    }

    public List<PercentualConfiguracaoRankingVO> consultarPorConfiguracaoRanking(Integer configuracaoRanking, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PercentualConfiguracaoRanking WHERE configuracaoRanking = ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, configuracaoRanking), nivelMontarDados, usuario));
    }

    public List<PercentualConfiguracaoRankingVO> consultarPorTurmaComissionamentoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT percentualconfiguracaoranking.* FROM percentualconfiguracaoranking  ");
        sb.append(" INNER JOIN configuracaoRanking ON configuracaoRanking.codigo = percentualconfiguracaoranking.configuracaoRanking ");
        sb.append(" INNER JOIN comissionamentoTurma ON comissionamentoTurma.configuracaoRanking = configuracaoRanking.codigo ");
        sb.append(" where comissionamentoTurma.turma = ").append(turma);
        sb.append(" ORDER BY posicao ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
}
