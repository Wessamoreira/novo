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
import negocio.comuns.financeiro.MatriculaPeriodoPreMatriculaVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.UnidadeEnsinoInterfaceFacade;
import negocio.interfaces.financeiro.MatriculaPeriodoPreMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaPeriodoVencimentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MatriculaPeriodoVencimentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MatriculaPeriodoVencimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class MatriculaPeriodoPreMatricula extends ControleAcesso implements MatriculaPeriodoPreMatriculaInterfaceFacade {

    protected static String idEntidade;
    protected UnidadeEnsinoInterfaceFacade unidadeEnsinoFacade = null;

    public MatriculaPeriodoPreMatricula() throws Exception {
        super();
        setIdEntidade("ContaReceber");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     */
    public MatriculaPeriodoVencimentoVO novo() throws Exception {
        MatriculaPeriodoPreMatricula.incluir(getIdEntidade());
        MatriculaPeriodoVencimentoVO obj = new MatriculaPeriodoVencimentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MatriculaPeriodoVencimentoVO obj,UsuarioVO usuario) throws Exception {
        try {
            MatriculaPeriodoVencimentoVO.validarDados(obj);
            final String sql = "INSERT INTO ContaReceber( data, codOrigem, tipoOrigem, situacao, descricaoPagamento, "
                    + "dataVencimento, valor, valorDesconto, juro, juroPorcentagem, multa, multaPorcentagem, "
                    + "nrDocumento, parcela, matriculaAluno, "
                    + "tipoPessoa , pessoa, "
                    + "descontoProgressivo, valorRecebido, tipoDesconto, unidadeEnsino, valorDescontoRecebido, "
                    + "descontoInstituicao, descontoConvenio, convenio, tipoBoleto, linhaDigitavelCodigoBarras, codigoBarra, beneficiario, nossonumero, parceiro) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setString(4, obj.getSituacao().getValor());
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataVencimento()));
                    sqlInserir.setDouble(7, obj.getValor().doubleValue());
                    sqlInserir.setDouble(8, obj.getValorDesconto().doubleValue());
                    sqlInserir.setString(14, obj.getParcela());
                    sqlInserir.setString(20, obj.getTipoDesconto());
                    if (obj.getValorDescontoInstituicao().doubleValue() != 0.0) {
                        sqlInserir.setDouble(23, obj.getValorDescontoInstituicao().doubleValue());
                    } else {
                        sqlInserir.setNull(23, 0);
                    }
                    if (obj.getValorDescontoConvenio().doubleValue() != 0.0) {
                        sqlInserir.setDouble(24, obj.getValorDescontoConvenio().doubleValue());
                    } else {
                        sqlInserir.setNull(24, 0);
                    }
                    sqlInserir.setString(26, obj.getTipoBoleto());
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
            alterar(obj, false, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaPeriodoVencimentoVO obj, boolean modificarSituacaoContaReceber,UsuarioVO usuario) throws Exception {
        try {
            MatriculaPeriodoVencimentoVO.validarDados(obj);
            final String sql = "UPDATE ContaReceber set data=?, codOrigem=?, tipoOrigem=?, situacao=?, descricaoPagamento=?, dataVencimento=?, valor=?, "
                    + "valorDesconto=?, juro=?, juroPorcentagem=?, multa=?, multaPorcentagem=?, nrDocumento=?, parcela=?, "
                    + "matriculaAluno=?, "
                    + "tipoPessoa=?, pessoa=?, descontoProgressivo = ?, valorRecebido=?, tipoDesconto = ?, "
                    + "unidadeEnsino=?, valorDescontoRecebido=?, descontoInstituicao=?, descontoConvenio=?, convenio=?, tipoBoleto=?, "
                    + "linhaDigitavelCodigoBarras=?, codigobarra=?, beneficiario=?, nossonumero=?, parceiro=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setString(4, obj.getSituacao().getValor());
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataVencimento()));
                    sqlAlterar.setDouble(7, obj.getValor().doubleValue());
                    sqlAlterar.setDouble(8, obj.getValorDesconto().doubleValue());
                    sqlAlterar.setString(14, obj.getParcela());
                    sqlAlterar.setString(20, obj.getTipoDesconto());
                    if (obj.getValorDescontoInstituicao().doubleValue() != 0.0) {
                        sqlAlterar.setDouble(23, obj.getValorDescontoInstituicao().doubleValue());
                    } else {
                        sqlAlterar.setNull(23, 0);
                    }
                    if (obj.getValorDescontoConvenio().doubleValue() != 0.0) {
                        sqlAlterar.setDouble(24, obj.getValorDescontoConvenio().doubleValue());
                    } else {
                        sqlAlterar.setNull(24, 0);
                    }
                    sqlAlterar.setString(26, obj.getTipoBoleto());
                    sqlAlterar.setInt(32, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaPeriodoVencimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MatriculaPeriodoVencimentoVO obj,UsuarioVO usuario) throws Exception {
        try {
            MatriculaPeriodoPreMatricula.excluir(getIdEntidade());
            String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } 
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorTipoOrigemCodOrigem(String tipoOrigem, Integer codOrigem,UsuarioVO usuario) throws Exception {
        try {
            MatriculaPeriodoPreMatricula.excluir(getIdEntidade());
            String sql = "DELETE FROM ContaReceber WHERE ((tipoOrigem = ?) and (codOrigem = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{tipoOrigem, String.valueOf(codOrigem)});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao,UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((tipoOrigem = ?) and (codOrigem = ?))";
        if (!situacao.equals("")) {
            sql += " and (situacao) = '" + situacao.toUpperCase() + "' ";
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{tipoOrigem.toUpperCase(), codigoOrigem.toString()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario)});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(Integer codigoContaReceber,UsuarioVO usuario) throws Exception {
        MatriculaPeriodoPreMatricula.excluir(getIdEntidade());
        String sql = "DELETE FROM MatriculaPeriodoVencimento WHERE ((contaReceber = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{codigoContaReceber});
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MatriculaPeriodoVencimentoVO</code> resultantes da consulta.
     */
    public static List<MatriculaPeriodoPreMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MatriculaPeriodoPreMatriculaVO> vetResultado = new ArrayList<MatriculaPeriodoPreMatriculaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     * @return  O objeto da classe <code>MatriculaPeriodoVencimentoVO</code> com os dados devidamente montados.
     */
    public static MatriculaPeriodoPreMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MatriculaPeriodoPreMatriculaVO obj = new MatriculaPeriodoPreMatriculaVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setSituacao(SituacaoVencimentoMatriculaPeriodo.getEnum(dadosSQL.getString("situacao")));
        obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setValorDesconto(new Double(dadosSQL.getDouble("valorDesconto")));
        obj.setTipoDesconto(dadosSQL.getString("tipoDesconto"));
        obj.setValorDescontoInstituicao(new Double(dadosSQL.getDouble("descontoInstituicao")));
        obj.setValorDescontoConvenio(new Double(dadosSQL.getDouble("descontoConvenio")));
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MatriculaPeriodoVencimentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MatriculaPeriodoPreMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM MatriculaPeriodoPreMatricula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Pré-Matrícula da Matrícula Período ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MatriculaPeriodoPreMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MatriculaPeriodoPreMatricula.idEntidade = idEntidade;
    }

}
