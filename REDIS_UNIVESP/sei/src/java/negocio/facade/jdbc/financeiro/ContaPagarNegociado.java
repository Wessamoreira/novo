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
import negocio.comuns.financeiro.ContaPagarNegociadoVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarNegociadoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy 
public class ContaPagarNegociado extends ControleAcesso implements ContaPagarNegociadoInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3875210010834308621L;    

    public ContaPagarNegociado() throws Exception {
        super();       
    }

   
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaPagarNegociadoVO obj, UsuarioVO usuario) throws Exception {    	
    	final String sql = "INSERT INTO ContaPagarNegociado( negociacaoContaPagar, contaPagar, valor ) VALUES ( ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getNegociacaoContaPagarVO().getCodigo());
                sqlInserir.setInt(2, obj.getContaPagarVO().getCodigo());                
                sqlInserir.setDouble(3, obj.getValor());
                return sqlInserir;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        getFacadeFactory().getContaPagarFacade().alterarSituacao(obj.getContaPagarVO().getCodigo(), SituacaoFinanceira.NEGOCIADO, usuario);
        obj.setNovoObj(Boolean.FALSE);
    }


   
    public List<ContaPagarNegociadoVO> consultarPorCodigoNegociacaoContaPagar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {        
        String sqlStr = "SELECT ContaPagarNegociado.* FROM ContaPagarNegociado inner join contapagar on contapagar.codigo = contaPagarNegociado.contapagar  WHERE ContaPagarNegociado.negociacaoContaPagar = " + valorConsulta.intValue() + " ORDER BY contapagar.dataVencimento, contapagar.parcela";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

   
    public static List<ContaPagarNegociadoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ContaPagarNegociadoVO> vetResultado = new ArrayList<ContaPagarNegociadoVO>(0);
        while (tabelaResultado.next()) {
        	ContaPagarNegociadoVO obj = new ContaPagarNegociadoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

   
    public static ContaPagarNegociadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ContaPagarNegociadoVO obj = new ContaPagarNegociadoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getNegociacaoContaPagarVO().setCodigo(dadosSQL.getInt("negociacaoContaPagar"));
        obj.getContaPagarVO().setCodigo(new Integer(dadosSQL.getInt("contaPagar")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }        
        montarDadosContaPagar(obj, nivelMontarDados, usuario);
        return obj;
    }

    
    public static void montarDadosContaPagar(ContaPagarNegociadoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {        
    	obj.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagarVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }

   
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirContaPagarNegociados(NegociacaoContaPagarVO negociacaoContaPagarVO, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("DELETE FROM ContaPagarNegociado WHERE (negociacaoContaPagar = ?)");      
        sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sql.toString(), new Object[]{negociacaoContaPagarVO.getCodigo()});
    }

  

   
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContaPagarNegociados(NegociacaoContaPagarVO negociacaoContaPagarVO, UsuarioVO usuario) throws Exception {
        for(ContaPagarNegociadoVO obj : negociacaoContaPagarVO.getContaPagarNegociadoVOs()) {
            obj.setNegociacaoContaPagarVO(negociacaoContaPagarVO);
            incluir(obj, usuario);
        }
    }

   
    public ContaPagarNegociadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM ContaPagarNegociado WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaPagarNegociado ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
  
  
}
