package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.financeiro.ContaPagarNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarNaoLocalizadaArquivoRetornoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ContaPagarNaoLocalizadaArquivoRetorno extends ControleAcesso implements ContaPagarNaoLocalizadaArquivoRetornoInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 933172180694362709L;
	private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public ContaPagarNaoLocalizadaArquivoRetorno() throws Exception {
        super();
        setIdEntidade("ContaPagarNaoLocalizadaArquivoRetorno");
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaPagarNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ContaPagarNaoLocalizadaArquivoRetorno(nossoNumero, valorPago, dataVcto, dataPagamento, tratada, observacao) VALUES (?,?,?,?,?,?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setLong(1, obj.getNossoNumero());
                    sqlInserir.setDouble(2, obj.getValorPago());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataVcto()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlInserir.setBoolean(5, obj.getTratada());
                    sqlInserir.setString(6, obj.getObservacao());
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaPagarNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE ContaPagarNaoLocalizadaArquivoRetorno SET nossoNumero=?, valorPago=?, dataVcto=?, dataPagamento=?, tratada=?, observacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setLong(1, obj.getNossoNumero());
                    sqlAlterar.setDouble(2, obj.getValorPago());
                    sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataVcto()));
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlAlterar.setBoolean(5, obj.getTratada());
                    sqlAlterar.setString(6, obj.getObservacao());
                    sqlAlterar.setInt(7, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    private StringBuilder getSQLPadraoConsulta() {
        StringBuilder str = new StringBuilder();
        //Dados campanha
        str.append("SELECT ContaPagarNaoLocalizadaArquivoRetorno.codigo as codigo, ContaPagarNaoLocalizadaArquivoRetorno.tratada as tratada, ");
        str.append("ContaPagarNaoLocalizadaArquivoRetorno.nossoNumero AS nossoNumero, ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento AS dataPgto, ");
        str.append("ContaPagarNaoLocalizadaArquivoRetorno.valorPago AS valorPago, ContaPagarNaoLocalizadaArquivoRetorno.dataVcto AS dataVcto, ");
        str.append("ContaPagarNaoLocalizadaArquivoRetorno.observacao AS observacao ");
        str.append(" FROM ContaPagarNaoLocalizadaArquivoRetorno ");
        return str;
    }

    public ContaPagarNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroUnico(Long valorConsulta, Date dataOcorrencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaPagarNaoLocalizadaArquivoRetorno.nossoNumero = ").append(valorConsulta).append(" ");
        sqlStr.append(" and ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento = '").append(Uteis.getDataJDBC(dataOcorrencia)).append("' ");
        sqlStr.append(" ORDER BY ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento desc , ContaPagarNaoLocalizadaArquivoRetorno.dataVcto desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        }
        return new ContaPagarNaoLocalizadaArquivoRetornoVO();
    }

    public List consultarPorNossoNumero(Long valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaPagarNaoLocalizadaArquivoRetorno.nossoNumero = ").append(valorConsulta).append(" ");
        sqlStr.append(" ORDER BY ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaPagarNaoLocalizadaArquivoRetorno.dataVcto desc  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List consultarPorPeriodoDataVcto(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaPagarNaoLocalizadaArquivoRetorno.dataVcto >= '").append(dataInicial).append("' AND ContaPagarNaoLocalizadaArquivoRetorno.dataVcto <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
        sqlStr.append(" ORDER BY ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaPagarNaoLocalizadaArquivoRetorno.dataVcto desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List consultarPorTratada(Boolean tratada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaPagarNaoLocalizadaArquivoRetorno.tratada = ").append(tratada);
        sqlStr.append(" ORDER BY ContaPagarNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaPagarNaoLocalizadaArquivoRetorno.dataVcto desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static ContaPagarNaoLocalizadaArquivoRetornoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ContaPagarNaoLocalizadaArquivoRetornoVO obj = new ContaPagarNaoLocalizadaArquivoRetornoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNossoNumero((dadosSQL.getLong("nossoNumero")));        
        obj.setTratada((dadosSQL.getBoolean("tratada")));
        obj.setValorPago((dadosSQL.getDouble("valorPago")));
        obj.setDataPagamento((dadosSQL.getDate("dataPgto")));
        obj.setDataVcto((dadosSQL.getDate("dataVcto")));
        obj.setObservacao((dadosSQL.getString("observacao")));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

}
