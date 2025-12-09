package negocio.facade.jdbc.financeiro;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.FechamentoFinanceiroFormaPagamentoVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroFormaPagamentoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class FechamentoFinanceiroFormaPagamento extends ControleAcesso implements FechamentoFinanceiroFormaPagamentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8775530281987991259L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(FechamentoFinanceiroFormaPagamentoVO fechamentoFinanceiroFormaPagamentoVO, UsuarioVO usuarioVO)
			throws Exception {
		incluir(fechamentoFinanceiroFormaPagamentoVO, "fechamentoFinanceiroFormaPagamento", new AtributoPersistencia().add("fechamentoFinanceiroConta", fechamentoFinanceiroFormaPagamentoVO.getFechamentoFinanceiroContaVO())
				.add("valor", fechamentoFinanceiroFormaPagamentoVO.getValor())
				.add("formaPagamento", fechamentoFinanceiroFormaPagamentoVO.getFormaPagamento())
				.add("contaCorrente", fechamentoFinanceiroFormaPagamentoVO.getContaCorrente())
				.add("dataCompensacao", fechamentoFinanceiroFormaPagamentoVO.getDataCompensacao())
				,  usuarioVO);
		fechamentoFinanceiroFormaPagamentoVO.setNovoObj(false);

	}
	
	public StringBuilder getSQLPadraoConsultaProcessamento() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT formapagamentonegociacaorecebimento.contacorrente,  ");
		sb.append(" formapagamentonegociacaorecebimento.formapagamento, ");
		sb.append(" formapagamentonegociacaorecebimento.valorrecebimento, ");
		sb.append(" formapagamentonegociacaorecebimento.datacredito  ");
		sb.append(" from contarecebernegociacaorecebimento  ");
		sb.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.negociacaorecebimento = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sb.append("  ");
		sb.append("  ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDadosParaFechamentoFinanceiroFormaPagamentoVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamento();
		sb.append("where contarecebernegociacaorecebimento.contareceber = ").append(ffc.getCodOrigemFechamentoFinanceiro()).append(" ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (dadosSQL.next()) {
			FechamentoFinanceiroFormaPagamentoVO obj = new FechamentoFinanceiroFormaPagamentoVO();
			obj.getContaCorrente().setCodigo(dadosSQL.getInt("contacorrente"));
			obj.getFormaPagamento().setCodigo(dadosSQL.getInt("formapagamento"));
			obj.setValor(dadosSQL.getDouble("valorrecebimento"));
			obj.setDataCompensacao(dadosSQL.getDate("datacredito"));
			obj.setFechamentoFinanceiroContaVO(ffc);
			incluir(obj, usuario);
			ffc.getFechamentoFinanceiroFormaPagamentoVOs().add(obj);
		}
	}

	@Override
	public List<FechamentoFinanceiroFormaPagamentoVO> consultarPorFechamentoFinanceiroConta(
			Integer fechamentoFinanceiroConta) throws Exception {
		
		return null;
	}

}
