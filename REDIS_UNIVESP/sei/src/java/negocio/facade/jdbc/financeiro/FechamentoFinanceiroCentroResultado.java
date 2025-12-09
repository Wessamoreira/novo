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
import negocio.comuns.financeiro.FechamentoFinanceiroCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaCentroResultadoVO;
import negocio.comuns.financeiro.FechamentoFinanceiroContaVO;
import negocio.comuns.financeiro.enumerador.OrigemFechamentoFinanceiroCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FechamentoFinanceiroCentroResultadoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")

public class FechamentoFinanceiroCentroResultado extends ControleAcesso
		implements FechamentoFinanceiroCentroResultadoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6618559779108721502L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO,
			UsuarioVO usuarioVO) throws Exception {
		incluir(fechamentoFinanceiroCentroResultadoVO, "fechamentoFinanceiroCentroResultado",
				new AtributoPersistencia().add("tipoCentroResultadoOrigem", fechamentoFinanceiroCentroResultadoVO.getTipoCentroResultadoOrigemEnum())
						.add("codigoOrigem", fechamentoFinanceiroCentroResultadoVO.getCodigoOrigem())		
						.add("centroResultado", fechamentoFinanceiroCentroResultadoVO.getCentroResultado())
						.add("valor", fechamentoFinanceiroCentroResultadoVO.getValor())
						.add("valorContaReceberRecebido", fechamentoFinanceiroCentroResultadoVO.getValorContaReceberRecebido())
						.add("valorContaPagarPago", fechamentoFinanceiroCentroResultadoVO.getValorContaPagarPago())
						.add("valorContaReceberNegociado", fechamentoFinanceiroCentroResultadoVO.getValorContaReceberNegociado())
						.add("valorContaReceberCancelado", fechamentoFinanceiroCentroResultadoVO.getValorContaReceberCancelado())
						.add("valorContaReceberAReceber", fechamentoFinanceiroCentroResultadoVO.getValorContaReceberAReceber())
						.add("valorContaPagarAPagar", fechamentoFinanceiroCentroResultadoVO.getValorContaPagarAPagar())
						.add("valorContaPagarNegociado", fechamentoFinanceiroCentroResultadoVO.getValorContaPagarNegociado())
						.add("valorContaPagarCancelado", fechamentoFinanceiroCentroResultadoVO.getValorContaPagarCancelado())
						.add("tipoMovimentacaoCentroResultadoOrigem", fechamentoFinanceiroCentroResultadoVO.getTipoMovimentacaoCentroResultadoOrigemEnum())
						.add("origemFechamentoFinanceiroCentroResultadoOrigem", fechamentoFinanceiroCentroResultadoVO.getOrigemFechamentoFinanceiroCentroResultadoOrigem())
						.add("codOrigemFechamentoFinanceiro", fechamentoFinanceiroCentroResultadoVO.getCodOrigemFechamentoFinanceiro())
						.add("categoriaDespesa", fechamentoFinanceiroCentroResultadoVO.getCategoriaDespesa())
						.add("centroReceita", fechamentoFinanceiroCentroResultadoVO.getCentroReceita()),
				usuarioVO);
		fechamentoFinanceiroCentroResultadoVO.setNovoObj(false);
		incluirFechamentoFinanceiroContaCentroResultadoVOs(fechamentoFinanceiroCentroResultadoVO, usuarioVO);
	}

	private void incluirFechamentoFinanceiroContaCentroResultadoVOs(FechamentoFinanceiroCentroResultadoVO fechamentoFinanceiroCentroResultadoVO, UsuarioVO usuarioVO) throws Exception {
		for (FechamentoFinanceiroContaCentroResultadoVO fechamentoFinanceiroContaCentroResultadoVO : fechamentoFinanceiroCentroResultadoVO.getFechamentoFinanceiroContaCentroResultadoVOs()) {
			fechamentoFinanceiroContaCentroResultadoVO.setFechamentoFinanceiroCentroResultado(fechamentoFinanceiroCentroResultadoVO);
			getFacadeFactory().getFechamentoFinanceiroContaCentroResultadoFacade().incluir(fechamentoFinanceiroContaCentroResultadoVO, usuarioVO);
		}
	}

	

	public StringBuilder getSQLPadraoConsultaProcessamento() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM centroresultadoorigem ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarDadosParaFechamentoFinanceiroCentroResultadoVO(FechamentoFinanceiroContaVO ffc, UsuarioVO usuario) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaProcessamento();
		sb.append(" where  centroresultadoorigem.codorigem = '").append(ffc.getCodOrigemFechamentoFinanceiro()).append("' ");
		sb.append(" and  centroresultadoorigem.tipocentroresultadoorigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_RECEBER.name()).append("' ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (dadosSQL.next()) {
			FechamentoFinanceiroCentroResultadoVO obj = new FechamentoFinanceiroCentroResultadoVO();
			obj.setCodigoOrigem(dadosSQL.getInt("codOrigem"));
			obj.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.valueOf(dadosSQL.getString("tipoCentroResultadoOrigem")));
			obj.setValor(dadosSQL.getDouble("valor"));
			obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa")));
			obj.getCentroReceita().setCodigo((dadosSQL.getInt("centroreceita")));
			obj.getCentroResultado().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo")));
			obj.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.valueOf(dadosSQL.getString("tipoMovimentacaoCentroResultadoOrigemEnum")));
			obj.setOrigemFechamentoFinanceiroCentroResultadoOrigem(OrigemFechamentoFinanceiroCentroResultadoEnum.FECHAMENTO_FINANCEIRO_CONTA);
			obj.setCodOrigemFechamentoFinanceiro(ffc.getCodigo());
			incluir(obj, usuario);
			ffc.getFechamentoFinanceiroCentroResultadoVOs().add(obj);
		}
	}

	@Override
	public List<FechamentoFinanceiroCentroResultadoVO> consultarPorOrigemCodigoOrigem(
			OrigemFechamentoFinanceiroCentroResultadoEnum origemFechamentoFinanceiroCentroResultado,
			Integer codigoOrigem) throws Exception {

		return null;
	}

}
