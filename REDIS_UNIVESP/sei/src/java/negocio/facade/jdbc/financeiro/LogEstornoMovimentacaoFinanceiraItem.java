package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.LogEstornoMovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.LogEstornoMovimentacaoFinanceiraItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class LogEstornoMovimentacaoFinanceiraItem extends ControleAcesso implements LogEstornoMovimentacaoFinanceiraItemInterfaceFacade{
	
	public void preencherLogEstornoMovimentacaoFinanceira(MovimentacaoFinanceiraVO movimentacaoFinanceira, MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItem, UsuarioVO usuario) throws Exception {
		try { 
			LogEstornoMovimentacaoFinanceiraItemVO obj = new LogEstornoMovimentacaoFinanceiraItemVO();
			obj.setDataEstorno(new Date());
	        obj.setResponsavel(usuario.getCodigo());
	        obj.setCodigoContaOrigem(movimentacaoFinanceira.getContaCorrenteOrigem().getCodigo());
	        obj.setDescricaoContaOrigem(movimentacaoFinanceira.getContaCorrenteOrigem().getDescricaoCompletaConta());
	        obj.setCodigoContaDestino(movimentacaoFinanceira.getContaCorrenteDestino().getCodigo());
	        obj.setDescricaoContaDestino(movimentacaoFinanceira.getContaCorrenteDestino().getDescricaoCompletaConta());
	        obj.setValor(movimentacaoFinanceiraItem.getValor());
	        obj.setFormaPagamento(movimentacaoFinanceiraItem.getFormaPagamento().getNome());
	        obj.setCodigoCheque(movimentacaoFinanceiraItem.getCheque().getCodigo());
	        incluir(obj, usuario);
		} catch (Exception e) {
	         throw e;
	    }
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogEstornoMovimentacaoFinanceiraItemVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO LogEstornoMovimentacaoFinanceiraItem( dataestorno, responsavel, codigocontaorigem, descricaocontaorigem, "
				+ "codigocontadestino, descricaocontadestino, valor, formapagamento, codigocheque ) "
                                + "VALUES ( ?,?,?,?,?,?,?,?,? )";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataEstorno()));
				sqlInserir.setInt(2, obj.getResponsavel());
				sqlInserir.setInt(3, obj.getCodigoContaOrigem());
				sqlInserir.setString(4, obj.getDescricaoContaOrigem());
				sqlInserir.setInt(5, obj.getCodigoContaDestino());
				sqlInserir.setString(6, obj.getDescricaoContaDestino());
				sqlInserir.setDouble(7, obj.getValor());
                sqlInserir.setString(8, obj.getFormaPagamento());
                sqlInserir.setInt(9, obj.getCodigoCheque());
				return sqlInserir;
			}
		});
	}
	
}
