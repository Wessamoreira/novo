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
import negocio.comuns.financeiro.ControleGeracaoParcelaTurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleGeracaoParcelaTurmaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ControleGeracaoParcelaTurmaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ControleGeracaoParcelaTurmaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ControleGeracaoParcelaTurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleGeracaoParcelaTurma extends ControleAcesso implements ControleGeracaoParcelaTurmaInterfaceFacade {

    protected static String idEntidade;

    public ControleGeracaoParcelaTurma() throws Exception {
        super();
        setIdEntidade("ControleGeracaoParcelaTurma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>.
     */
    public ControleGeracaoParcelaTurmaVO novo() throws Exception {
        ControleGeracaoParcelaTurma.incluir(getIdEntidade());
        ControleGeracaoParcelaTurmaVO obj = new ControleGeracaoParcelaTurmaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ControleGeracaoParcelaTurmaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleGeracaoParcelaTurma.incluir(getIdEntidade(), true, usuario);
            ControleGeracaoParcelaTurmaVO.validarDados(obj);
            final String sql = "INSERT INTO ControleGeracaoParcelaTurma( nome, dataVencimentoMatricula, "
                    + "mesVencimentoPrimeiraMensalidades, diaVencimentoPrimeiraMensalidades, "
                    + "anoVencimentoPrimeiraMensalidades, "
                    + "usarDataVencimentoDataMatricula, mesSubsequenteMatricula,"
                    + "mesDataBaseGeracaoParcelas, zerarValorDescontoPlanoFinanceiroAluno, "
                    + "utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual, "
                    + "qtdeDiasAvancarDataVencimentoMatricula, consideraCompetenciaVctoMatricula, dataCompetenciaMatricula, consideraCompetenciaVctoMensalidade, dataCompetenciaMensalidade, utilizarOrdemDescontoConfiguracaoFinanceira, acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno "
                    + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    int i = 1;
                    sqlInserir.setString(i++, obj.getNome());

                    if (obj.getDataVencimentoMatricula() != null) {
                        sqlInserir.setDate(i++, Uteis.getDataJDBC(obj.getDataVencimentoMatricula()));
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getMesVencimentoPrimeiraMensalidade() != null && obj.getMesVencimentoPrimeiraMensalidade() != 0) {
                        sqlInserir.setInt(i++, obj.getMesVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getDiaVencimentoPrimeiraMensalidade() != null && obj.getDiaVencimentoPrimeiraMensalidade() != 0) {
                        sqlInserir.setInt(i++, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    if (obj.getAnoVencimentoPrimeiraMensalidade() != null && obj.getAnoVencimentoPrimeiraMensalidade() != 0) {
                        sqlInserir.setInt(i++, obj.getAnoVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlInserir.setNull(i++, 0);
                    }
                    sqlInserir.setBoolean(i++, obj.getUsarDataVencimentoDataMatricula());
                    sqlInserir.setBoolean(i++, obj.getMesSubsequenteMatricula());
                    sqlInserir.setBoolean(i++, obj.getMesDataBaseGeracaoParcelas());
                    sqlInserir.setBoolean(i++, obj.getZerarValorDescontoPlanoFinanceiroAluno());
                    sqlInserir.setBoolean(i++, obj.getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual());
                    sqlInserir.setInt(i++, obj.getQtdeDiasAvancarDataVencimentoMatricula());
                    sqlInserir.setBoolean(i++, obj.getConsiderarCompetenciaVctoMatricula());
                    sqlInserir.setDate(i++, Uteis.getDataJDBC(obj.getDataCompetenciaMatricula()));
                    sqlInserir.setBoolean(i++, obj.getConsiderarCompetenciaVctoMensalidade());
                    sqlInserir.setDate(i++, Uteis.getDataJDBC(obj.getDataCompetenciaMensalidade()));
                    sqlInserir.setBoolean(i++, obj.getUtilizarOrdemDescontoConfiguracaoFinanceira());
                    sqlInserir.setBoolean(i++, obj.getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleGeracaoParcelaTurmaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {

            ControleGeracaoParcelaTurma.alterar(getIdEntidade(), true, usuario);
            ControleGeracaoParcelaTurmaVO.validarDados(obj);
            final String sql = "UPDATE ControleGeracaoParcelaTurma set nome=?,"
                    + "dataVencimentoMatricula = ?, "
                    + "mesVencimentoPrimeiraMensalidades = ?, diaVencimentoPrimeiraMensalidades = ?, "
                    + "anoVencimentoPrimeiraMensalidades = ?, "
                    + "usarDataVencimentoDataMatricula = ?, mesSubsequenteMatricula = ?,"
                    + "mesDataBaseGeracaoParcelas = ?, zerarValorDescontoPlanoFinanceiroAluno = ?, "
                    + "utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual = ?, "
                    + "qtdeDiasAvancarDataVencimentoMatricula = ?, consideraCompetenciaVctoMatricula=?, dataCompetenciaMatricula=?, consideraCompetenciaVctoMensalidade=?, dataCompetenciaMensalidade=?, utilizarOrdemDescontoConfiguracaoFinanceira=?, acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno = ? "
                    + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 1;
                    sqlAlterar.setString(i++, obj.getNome());

                    if (obj.getDataVencimentoMatricula() != null) {
                        sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getDataVencimentoMatricula()));
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getMesVencimentoPrimeiraMensalidade() != null && obj.getMesVencimentoPrimeiraMensalidade() != 0) {
                        sqlAlterar.setInt(i++, obj.getMesVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getDiaVencimentoPrimeiraMensalidade() != null && obj.getDiaVencimentoPrimeiraMensalidade() != 0) {
                        sqlAlterar.setInt(i++, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getAnoVencimentoPrimeiraMensalidade() != null && obj.getAnoVencimentoPrimeiraMensalidade() != 0) {
                        sqlAlterar.setInt(i++, obj.getAnoVencimentoPrimeiraMensalidade().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setBoolean(i++, obj.getUsarDataVencimentoDataMatricula());
                    sqlAlterar.setBoolean(i++, obj.getMesSubsequenteMatricula());
                    sqlAlterar.setBoolean(i++, obj.getMesDataBaseGeracaoParcelas());
                    sqlAlterar.setBoolean(i++, obj.getZerarValorDescontoPlanoFinanceiroAluno());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual());
                    sqlAlterar.setInt(i++, obj.getQtdeDiasAvancarDataVencimentoMatricula());
                    sqlAlterar.setBoolean(i++, obj.getConsiderarCompetenciaVctoMatricula());
                    sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getDataCompetenciaMatricula()));
                    sqlAlterar.setBoolean(i++, obj.getConsiderarCompetenciaVctoMensalidade());
                    sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getDataCompetenciaMensalidade()));
                    sqlAlterar.setBoolean(i++, obj.getUtilizarOrdemDescontoConfiguracaoFinanceira());
                    sqlAlterar.setBoolean(i++, obj.getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno());
                    sqlAlterar.setInt(i++, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleGeracaoParcelaTurmaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final ControleGeracaoParcelaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleGeracaoParcelaTurma.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ControleGeracaoParcelaTurma WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleGeracaoParcelaTurma</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleGeracaoParcelaTurmaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleGeracaoParcelaTurma WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>ControleGeracaoParcelaTurma</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleGeracaoParcelaTurmaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleGeracaoParcelaTurma WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ControleGeracaoParcelaTurmaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>.
     * @return  O objeto da classe <code>ControleGeracaoParcelaTurmaVO</code> com os dados devidamente montados.
     */
    public static ControleGeracaoParcelaTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleGeracaoParcelaTurmaVO obj = new ControleGeracaoParcelaTurmaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        if (dadosSQL.getInt("mesVencimentoPrimeiraMensalidades") != 0) {
            obj.setMesVencimentoPrimeiraMensalidade(dadosSQL.getInt("mesVencimentoPrimeiraMensalidades"));
        }
        if (dadosSQL.getInt("diaVencimentoPrimeiraMensalidades") != 0) {
            obj.setDiaVencimentoPrimeiraMensalidade(dadosSQL.getInt("diaVencimentoPrimeiraMensalidades"));
        }
        if (dadosSQL.getInt("anoVencimentoPrimeiraMensalidades") != 0) {
            obj.setAnoVencimentoPrimeiraMensalidade(dadosSQL.getInt("anoVencimentoPrimeiraMensalidades"));
        }
        obj.setQtdeDiasAvancarDataVencimentoMatricula(dadosSQL.getInt("qtdeDiasAvancarDataVencimentoMatricula"));
        obj.setDataVencimentoMatricula(dadosSQL.getDate("dataVencimentoMatricula"));

        obj.setConsiderarCompetenciaVctoMatricula(dadosSQL.getBoolean("consideraCompetenciaVctoMatricula"));
        obj.setDataCompetenciaMatricula(dadosSQL.getDate("dataCompetenciaMatricula"));
        obj.setConsiderarCompetenciaVctoMensalidade(dadosSQL.getBoolean("consideraCompetenciaVctoMensalidade"));
        obj.setDataCompetenciaMensalidade(dadosSQL.getDate("dataCompetenciaMensalidade"));
        
        obj.setUsarDataVencimentoDataMatricula(dadosSQL.getBoolean("usarDataVencimentoDataMatricula"));
        obj.setMesSubsequenteMatricula(dadosSQL.getBoolean("mesSubsequenteMatricula"));
        obj.setMesDataBaseGeracaoParcelas(dadosSQL.getBoolean("mesDataBaseGeracaoParcelas"));
        obj.setZerarValorDescontoPlanoFinanceiroAluno(dadosSQL.getBoolean("zerarValorDescontoPlanoFinanceiroAluno"));
        obj.setUtilizarOrdemDescontoConfiguracaoFinanceira(dadosSQL.getBoolean("utilizarOrdemDescontoConfiguracaoFinanceira"));
        obj.setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(dadosSQL.getBoolean("utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual"));
        obj.setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(dadosSQL.getBoolean("acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ControleGeracaoParcelaTurmaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ControleGeracaoParcelaTurmaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ControleGeracaoParcelaTurma WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ControleGeracaoParcelaTurma ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ControleGeracaoParcelaTurma.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        ControleGeracaoParcelaTurma.idEntidade = idEntidade;
    }
}
