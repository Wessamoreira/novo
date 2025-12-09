package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ObservacaoComplementarVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ObservacaoComplementarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ObservacaoComplementar extends ControleAcesso implements ObservacaoComplementarInterfaceFacade {

    private static String idEntidade;

    public ObservacaoComplementar() throws Exception {
        super();
        setIdEntidade("ObservacaoComplementar");
    }

    public static void validarDados(ObservacaoComplementarVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome() == null || obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Observação Complementar) deve ser informado.");
        }
        if (obj.getObservacao() == null || obj.getObservacao().equals("")) {
            throw new ConsistirException("O campo OBSERVAÇÃO (Observação Complementar) deve ser informado.");
        }
    }

    public ObservacaoComplementarVO novo() throws Exception {
        ObservacaoComplementar.incluir(getIdEntidade());
        ObservacaoComplementarVO obj = new ObservacaoComplementarVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO ObservacaoComplementar( nome, observacao, exigeAssinatura, funcionarioAssinatura, ordem, reapresentarNomeAluno, tituloObservacao) VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getObservacao());
                    sqlInserir.setBoolean(3, obj.getExigeAssinatura());
                    sqlInserir.setString(4, obj.getFuncionarioAssinatura());
                    sqlInserir.setInt(5, obj.getOrdem());
                    sqlInserir.setBoolean(6, obj.getReapresentarNomeAluno());
                    sqlInserir.setString(7, obj.getTituloObservacao());
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

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            ObservacaoComplementar.alterar(getIdEntidade());
            final String sql = "UPDATE ObservacaoComplementar set nome=?, observacao=?, exigeAssinatura=?, funcionarioAssinatura=?, ordem=?, reapresentarNomeAluno=?, tituloObservacao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getObservacao());
                    sqlAlterar.setBoolean(3, obj.getExigeAssinatura());
                    sqlAlterar.setString(4, obj.getFuncionarioAssinatura());
                    sqlAlterar.setInt(5, obj.getOrdem());
                    sqlAlterar.setBoolean(6, obj.getReapresentarNomeAluno());
                    sqlAlterar.setString(7, obj.getTituloObservacao());
                    sqlAlterar.setInt(8, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ObservacaoComplementarVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ObservacaoComplementarVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception {
        try {
            ObservacaoComplementar.excluir(getIdEntidade());
            String sql = "DELETE FROM ObservacaoComplementar WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ObservacaoComplementar</code> através do valor do atributo
     * <code>descricao</code> da classe <code>Bairro</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ObservacaoComplementarVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ObservacaoComplementar.* FROM ObservacaoComplementar WHERE upper(nome) like('" + valorConsulta.toUpperCase() + "%') ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>ObservacaoComplementar</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ObservacaoComplementarVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ObservacaoComplementar.* FROM ObservacaoComplementar WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ObservacaoComplementarVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ObservacaoComplementarVO obj = new ObservacaoComplementarVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ObservacaoComplementarVO</code>.
     * @return  O objeto da classe <code>ObservacaoComplementarVO</code> com os dados devidamente montados.
     */
    public static ObservacaoComplementarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ObservacaoComplementarVO obj = new ObservacaoComplementarVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setObservacao(dadosSQL.getString("observacao"));
        obj.setExigeAssinatura(dadosSQL.getBoolean("exigeAssinatura"));
        obj.setFuncionarioAssinatura(dadosSQL.getString("funcionarioAssinatura"));
        obj.setOrdem(dadosSQL.getInt("ordem"));
        obj.setReapresentarNomeAluno(dadosSQL.getBoolean("reapresentarNomeAluno"));
        obj.setTituloObservacao(dadosSQL.getString("tituloObservacao"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public ObservacaoComplementarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT ObservacaoComplementar.* FROM ObservacaoComplementar WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ObservacaoComplementar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public List<ObservacaoComplementarVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            if (campoConsulta.equals("codigo")) {
                if (valorConsulta.equals("")) {
                    valorConsulta = "0";
                }
                int valorInt = Integer.parseInt(valorConsulta);
                return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            if (campoConsulta.equals("nome")) {
                return consultarPorNome(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            }
            return new ArrayList(0);
        } catch (Exception e) {
            throw e;
        }
    }
}
