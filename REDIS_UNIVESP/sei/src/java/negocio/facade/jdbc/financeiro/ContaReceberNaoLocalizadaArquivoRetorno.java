/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ContaReceberNaoLocalizadaArquivoRetornoInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaReceberNaoLocalizadaArquivoRetorno extends ControleAcesso implements ContaReceberNaoLocalizadaArquivoRetornoInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public ContaReceberNaoLocalizadaArquivoRetorno() throws Exception {
        super();
        setIdEntidade("ContaReceberNaoLocalizadaArquivoRetorno");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ContaReceberNaoLocalizadaArquivoRetorno(nossoNumero, valor, valorRecebido, dataVcto, situacao, dataPagamento, tratada, contaCorrente) VALUES (?,?,?,?,?,?,?,?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNossoNumero());
                    sqlInserir.setDouble(2, obj.getValor());
                    sqlInserir.setDouble(3, obj.getValorRecebido());
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataVcto()));
                    sqlInserir.setString(5, obj.getSituacao());
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlInserir.setBoolean(7, obj.getTratada());
                    sqlInserir.setInt(8, obj.getContaCorrenteVO().getCodigo());
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
    public void alterar(final ContaReceberNaoLocalizadaArquivoRetornoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE ContaReceberNaoLocalizadaArquivoRetorno SET nossoNumero=?, valor=?, valorRecebido=?, dataVcto=?, situacao=?, dataPagamento=?, tratada=?, observacao=?, contaCorrente=?, contareceber=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNossoNumero());
                    sqlAlterar.setDouble(2, obj.getValor());
                    sqlAlterar.setDouble(3, obj.getValorRecebido());
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataVcto()));
                    sqlAlterar.setString(5, obj.getSituacao());
                    sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlAlterar.setBoolean(7, obj.getTratada());
                    sqlAlterar.setString(8, obj.getObservacao());
                    sqlAlterar.setInt(9, obj.getContaCorrenteVO().getCodigo());
                    if (obj.getContaReceberVO().getCodigo().intValue() > 0) {
                    	sqlAlterar.setInt(10, obj.getContaReceberVO().getCodigo());
                    } else {
                    	sqlAlterar.setNull(10, 0);
                    }
                    sqlAlterar.setInt(11, obj.getCodigo());
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
        str.append("SELECT ContaReceberNaoLocalizadaArquivoRetorno.codigo as codigo, ContaReceberNaoLocalizadaArquivoRetorno.tratada as tratada, ");
        str.append("ContaReceberNaoLocalizadaArquivoRetorno.nossoNumero AS nossoNumero, ContaReceberNaoLocalizadaArquivoRetorno.valor AS valor, ");
        str.append("ContaReceberNaoLocalizadaArquivoRetorno.valorRecebido AS valorRecebido, ContaReceberNaoLocalizadaArquivoRetorno.dataVcto AS dataVcto, ");
        str.append("ContaReceberNaoLocalizadaArquivoRetorno.situacao AS situacao, ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento AS dataPgto, ");
        str.append("ContaReceberNaoLocalizadaArquivoRetorno.observacao AS observacao, ContaReceberNaoLocalizadaArquivoRetorno.contaCorrente AS contaCorrente, ");
        str.append("ContaReceberNaoLocalizadaArquivoRetorno.contaReceber AS contaReceber, ");
        str.append("formapagamentonegociacaorecebimento.datacredito AS datacredito ");
        str.append(" FROM ContaReceberNaoLocalizadaArquivoRetorno ");
        str.append(" left join  contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = ContaReceberNaoLocalizadaArquivoRetorno.contareceber ");
        str.append(" left join  formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.negociacaorecebimento = contarecebernegociacaorecebimento.negociacaorecebimento ");
        return str;
    }

    public ContaReceberNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroUnico(String valorConsulta, Date dataOcorrencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaReceberNaoLocalizadaArquivoRetorno.nossoNumero = '").append(valorConsulta).append("' ");
        sqlStr.append(" and ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento = '").append(Uteis.getDataJDBC(dataOcorrencia)).append("' ");
        sqlStr.append(" ORDER BY ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento desc , ContaReceberNaoLocalizadaArquivoRetorno.dataVcto desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        }
        return new ContaReceberNaoLocalizadaArquivoRetornoVO();
    }

    public List consultarPorNossoNumero(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaReceberNaoLocalizadaArquivoRetorno.nossoNumero ilike '").append(valorConsulta).append("%' ");
        sqlStr.append(" ORDER BY ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaReceberNaoLocalizadaArquivoRetorno.dataVcto desc  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public ContaReceberNaoLocalizadaArquivoRetornoVO consultarPorNossoNumeroVinculadoContaReceber(String valorConsulta, int nivelMontarDados, UsuarioVO usuario)  {
    	try {
	    	StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaReceberNaoLocalizadaArquivoRetorno.nossoNumero = '").append(valorConsulta).append("' and ContaReceberNaoLocalizadaArquivoRetorno.contareceber is not null ");
	    	sqlStr.append(" ORDER BY ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaReceberNaoLocalizadaArquivoRetorno.dataVcto desc limit 1 ");
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	        if (tabelaResultado.next()) {
	            return montarDados(tabelaResultado, nivelMontarDados, usuario);
	        }
	        return null;
    	} catch (Exception e) {
            return null;
		}
	}
    
    public List consultarPorPeriodoDataVcto(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaReceberNaoLocalizadaArquivoRetorno.dataVcto >= '").append(dataInicial).append("' AND ContaReceberNaoLocalizadaArquivoRetorno.dataVcto <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
        sqlStr.append(" ORDER BY ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaReceberNaoLocalizadaArquivoRetorno.dataVcto desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List consultarPorTratada(Boolean tratada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsulta().append("WHERE ContaReceberNaoLocalizadaArquivoRetorno.tratada = ").append(tratada);
        sqlStr.append(" ORDER BY ContaReceberNaoLocalizadaArquivoRetorno.dataPagamento desc, ContaReceberNaoLocalizadaArquivoRetorno.dataVcto desc ");
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

    public static ContaReceberNaoLocalizadaArquivoRetornoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ContaReceberNaoLocalizadaArquivoRetornoVO obj = new ContaReceberNaoLocalizadaArquivoRetornoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setNossoNumero((dadosSQL.getString("nossoNumero")));
        obj.setValor((dadosSQL.getDouble("valor")));
        obj.setTratada((dadosSQL.getBoolean("tratada")));
        obj.setValorRecebido((dadosSQL.getDouble("valorRecebido")));
        obj.setDataPagamento((dadosSQL.getDate("dataPgto")));
        obj.setDataVcto((dadosSQL.getDate("dataVcto")));
        obj.setSituacao((dadosSQL.getString("situacao")));
        obj.setObservacao((dadosSQL.getString("observacao")));
        obj.getContaCorrenteVO().setCodigo((dadosSQL.getInt("contaCorrente")));
        obj.getContaReceberVO().setCodigo((dadosSQL.getInt("contaReceber")));
        montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosContaReceber(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        if(Uteis.isAtributoPreenchido(dadosSQL.getDate("datacredito"))) {
        	obj.getContaReceberVO().setDataCredito(dadosSQL.getDate("datacredito"));	
        }
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }
    
	public static void montarDadosContaCorrente(ContaReceberNaoLocalizadaArquivoRetornoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrenteVO().getCodigo() == 0) {
			return;
		}
		obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosContaReceber(ContaReceberNaoLocalizadaArquivoRetornoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaReceberVO().getCodigo() == 0) {
			return;
		}
		obj.getContaReceberVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
		getFacadeFactory().getContaReceberFacade().carregarDados(obj.getContaReceberVO(), NivelMontarDados.BASICO, null, usuario);
	}
	
    
}
