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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.NumeroDocumentoContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.NumeroDocumentoContaReceberInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class NumeroDocumentoContaReceber extends ControleAcesso implements NumeroDocumentoContaReceberInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public NumeroDocumentoContaReceber() throws Exception {
        super();
        setIdEntidade("NumeroDocumentoContaReceber");
    }

    public void validardados() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final NumeroDocumentoContaReceberVO obj) throws Exception {
        try {
            validardados();
            final String sql = "INSERT INTO NumeroDocumentoContaReceber( pessoa, parceiro, incremental ) VALUES ( ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getPessoaVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPessoaVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getParceiroVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getParceiroVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setInt(3, obj.getIncremental().intValue());
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
    private void alterar(final NumeroDocumentoContaReceberVO obj) throws Exception {
        try {
            validardados();
            final String sql = "UPDATE NumeroDocumentoContaReceber set pessoa=?, parceiro=?, incremental=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getPessoaVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPessoaVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getParceiroVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getParceiroVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setInt(3, obj.getIncremental());
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void excluir(NumeroDocumentoContaReceberVO obj) throws Exception {
        try {
            String sql = "DELETE FROM NumeroDocumentoContaReceber WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT * FROM NumeroDocumentoContaReceber WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            NumeroDocumentoContaReceberVO obj = new NumeroDocumentoContaReceberVO();
            obj = montarDados(tabelaResultado, nivelMontarDados);
            vetResultado.add(obj);
        }
        tabelaResultado = null;
        return vetResultado;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static NumeroDocumentoContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        NumeroDocumentoContaReceberVO obj = new NumeroDocumentoContaReceberVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
        obj.getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
        obj.setIncremental(new Integer(dadosSQL.getInt("incremental")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public NumeroDocumentoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM NumeroDocumentoContaReceber WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( NumeroMatricula ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public NumeroDocumentoContaReceberVO consultarPorParceiro(Integer parceiro, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM NumeroDocumentoContaReceber WHERE parceiro = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{parceiro});
        if (!tabelaResultado.next()) {
            return new NumeroDocumentoContaReceberVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public NumeroDocumentoContaReceberVO consultarPorPessoa(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM NumeroDocumentoContaReceber WHERE pessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{pessoa});
        if (!tabelaResultado.next()) {
            return new NumeroDocumentoContaReceberVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public NumeroDocumentoContaReceberVO inicializarDadosIncrementalTipoParceiro(Integer parceiro) {
        NumeroDocumentoContaReceberVO obj = new NumeroDocumentoContaReceberVO();
        obj.getParceiroVO().setCodigo(parceiro);
        obj.setIncremental(1);
        obj.setPessoaVO(new PessoaVO());
        return obj;
    }

    public NumeroDocumentoContaReceberVO inicializarDadosIncrementalTipoPessoa(Integer pessoa) {
        NumeroDocumentoContaReceberVO obj = new NumeroDocumentoContaReceberVO();
        obj.getPessoaVO().setCodigo(pessoa);
        obj.setIncremental(1);
        obj.setParceiroVO(new ParceiroVO());
        return obj;
    }

    public String gerarNumeroDocumentoTipoParceiro(Integer parceiro, UsuarioVO usuarioVO) throws Exception {
        String numeroDocumento = "";
        NumeroDocumentoContaReceberVO numeroDocumentoContaReceberVO = consultarPorParceiro(parceiro, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);

        if (numeroDocumentoContaReceberVO == null || numeroDocumentoContaReceberVO.getCodigo() == 0) {
            NumeroDocumentoContaReceberVO obj = inicializarDadosIncrementalTipoParceiro(parceiro);
            int tamanho = 10 - parceiro.toString().length();
            numeroDocumento += parceiro + Uteis.preencherComZerosPosicoesVagas(obj.getIncremental().toString(), tamanho);
            incluir(obj);
        } else {
            numeroDocumentoContaReceberVO.setIncremental(numeroDocumentoContaReceberVO.getIncremental() + 1);
            int tamanho = 10 - parceiro.toString().length();
            numeroDocumento += parceiro + Uteis.preencherComZerosPosicoesVagas(numeroDocumentoContaReceberVO.getIncremental().toString(), tamanho);
            alterar(numeroDocumentoContaReceberVO);
        }
        return numeroDocumento;
    }

    public String gerarNumeroDocumentoTipoPessoa(Integer pessoa, UsuarioVO usuarioVO) throws Exception {
        String numeroDocumento = "";
        NumeroDocumentoContaReceberVO numeroDocumentoContaReceberVO = consultarPorPessoa(pessoa, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);

        if (numeroDocumentoContaReceberVO == null || numeroDocumentoContaReceberVO.getCodigo() == 0) {
            NumeroDocumentoContaReceberVO obj = inicializarDadosIncrementalTipoPessoa(pessoa);
            int tamanho = 10 - pessoa.toString().length();
            numeroDocumento += pessoa + Uteis.preencherComZerosPosicoesVagas(obj.getIncremental().toString(), tamanho);
            incluir(obj);
        } else {
            numeroDocumentoContaReceberVO.setIncremental(numeroDocumentoContaReceberVO.getIncremental() + 1);
            int tamanho = 10 - pessoa.toString().length();
            numeroDocumento += pessoa + Uteis.preencherComZerosPosicoesVagas(numeroDocumentoContaReceberVO.getIncremental().toString(), tamanho);
            alterar(numeroDocumentoContaReceberVO);
        }
        return numeroDocumento;
    }
}
